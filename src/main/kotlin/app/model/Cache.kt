package app.model

import org.slf4j.LoggerFactory
import java.io.BufferedOutputStream
import java.io.File
import java.nio.charset.Charset
import java.util.*
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipOutputStream

class Cache(private val dir: File) {

    init {
        if (!dir.exists()) {
            dir.mkdirs()
        }
    }

    fun file(processID: String, format: Format): File {
        val name = "${processID}_${format.name}.zip"
        return File(dir, name)
    }

    fun file(name: String): File {
        return File(dir, name)
    }

    fun tempDir(): File {
        val temp = File(dir, "temp")
        if (!temp.exists()) {
            temp.mkdirs()
        }
        val d = File(temp, UUID.randomUUID().toString())
        d.mkdirs()
        return d
    }

    fun tempFile(): File {
        val temp = File(dir, "temp")
        if (!temp.exists()) {
            temp.mkdirs()
        }
        return File(temp, UUID.randomUUID().toString())
    }

    /**
     * Returns the process with the given ID and format from the respective
     * ZIP file in the cache (so there must be a conversion result for
     * this process in the cache). It returns an empty string if no such
     * process could be found.
     */
    fun process(id: String, format: Format): String {
        val f = file(id, format)
        if (!f.exists())
            return ""
        try {
            ZipFile(f).use { zip ->
                val e = findEntry(id, zip) ?: return ""
                zip.getInputStream(e).buffered().use { stream ->
                    return String(stream.readBytes(),
                            Charset.forName("utf-8"))
                }
            }
        } catch (e: Exception) {
            val log = LoggerFactory.getLogger(javaClass)
            log.error("failed to get process text from " + f, e)
        }
        return ""
    }

    private fun findEntry(id: String, zip: ZipFile): ZipEntry? {
        for (e in zip.entries()) {
            if (e.isDirectory)
                continue
            if (e.name.contains(id))
                return e
        }
        return null
    }

    /**
     * Creates a zip file from the files in the given temporary folder and
     * then deletes the folder. This is used for the EcoSpold exports that do
     * not create zip files.
     */
    fun zipFilesAndClean(tempDir: File, zip: File) {
        val tmp = tempFile()
        val stream = ZipOutputStream(BufferedOutputStream(tmp.outputStream()))
        val files = mutableListOf<File>()
        collectFiles(tempDir, files)
        stream.use { s ->
            files.forEach { file ->
                val entry = ZipEntry(file.name)
                s.putNextEntry(entry)
                file.inputStream().use { it.copyTo(s) }
            }
        }
        tmp.copyTo(zip, overwrite = true)
        tmp.delete()
        tempDir.deleteRecursively()
    }

    private fun collectFiles(dir: File, files: MutableList<File>) {
        dir.listFiles().forEach { f ->
            if (f.isDirectory) {
                collectFiles(f, files)
            } else {
                files.add(f)
            }
        }
    }
}