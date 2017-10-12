package app.model

import java.io.File
import java.util.*

class Workspace(path: String) {

    private val dir = File(path)

    init {
        if (!dir.exists()){
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
}