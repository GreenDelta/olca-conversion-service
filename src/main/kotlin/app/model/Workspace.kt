package app.model

import org.slf4j.LoggerFactory
import java.io.File
import java.nio.charset.Charset
import java.util.*
import java.util.zip.ZipEntry
import java.util.zip.ZipFile

class Workspace(path: String) {

    private val dir = File(path)

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
     * ZIP file in the workspace (so there must be a conversion result for
     * this process in the workspace). It returns an empty string if no such
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
}