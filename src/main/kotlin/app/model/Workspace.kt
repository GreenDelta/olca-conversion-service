package app.model

import java.io.File

class Workspace(path: String) {

    private val dir = File(path)

    init {
        if (!dir.exists()){
            dir.mkdirs()
        }
    }

    fun file(processID: String, format: Format): File {
        val name = "${processID}_${format.label}.zip"
        return File(dir, name)
    }
}