package app.model

import org.openlca.core.database.IDatabase
import org.openlca.core.database.ProcessDao
import org.slf4j.LoggerFactory
import java.io.BufferedOutputStream
import java.io.File
import java.nio.charset.Charset
import java.util.UUID
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipOutputStream

class Cache(private val dir: File) {

    init {
        if (!dir.exists()) {
            dir.mkdirs()
        }
    }

    /**
     * Conversion results are stored in plain zip files. This method
     * returns a new zip file that does not yet exist.
     */
    fun nextZip(): File {
        val name = UUID.randomUUID().toString() + ".zip"
        return File(dir, name)
    }

    /**
     * Returns the file with the given name from the cache directory.
     */
    fun getFile(name: String): File {
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

    fun tempFile(ext: String = ""): File {
        val temp = File(dir, "temp")
        if (!temp.exists()) {
            temp.mkdirs()
        }
        return File(temp, UUID.randomUUID().toString() + ext)
    }

    /**
     * Returns the first process from the given zip file in the target format
     * of a conversion. During the conversion we import the processes from
     * the source format into a database. Thus, there should be a file with
     * a process ID in the given zip. This is a convenience method so that we
     * can directly return a converted process in a conversion request.
     */
    fun firstProcess(db: IDatabase, zipFile: File): String {
        try {
            val descriptors = ProcessDao(db).descriptors
            if (descriptors.isEmpty())
                return ""
            val id = descriptors.first().refId
            ZipFile(zipFile).use { zip ->
                val e = findEntry(id, zip) ?: return ""
                zip.getInputStream(e).buffered().use { stream ->
                    return String(stream.readBytes(),
                            Charset.forName("utf-8"))
                }
            }
        } catch (e: Exception) {
            val log = LoggerFactory.getLogger(javaClass)
            log.error("failed to get process text from $zipFile", e)
            return ""
        }
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
     * not create zip files but currently export their results into a folder.
     * The created zip file is the returned by this method.
     */
    fun zipFilesAndClean(tempDir: File): File {
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
        val zip = nextZip()
        tmp.copyTo(zip, overwrite = true)
        tmp.delete()
        tempDir.deleteRecursively()
        return zip
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