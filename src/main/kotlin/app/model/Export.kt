package app.model

import org.openlca.core.database.IDatabase
import org.openlca.core.model.Process
import java.io.File

interface Export {

    val format: Format

    fun doIt(p: Process, db: IDatabase): File
}