package app.model

import java.io.File

interface Export {

    fun doIt(id: String): File
}