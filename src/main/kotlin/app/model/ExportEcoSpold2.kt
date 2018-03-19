package app.model

import app.Server
import org.openlca.core.database.IDatabase
import org.openlca.core.model.Process
import org.openlca.core.model.descriptors.Descriptors
import org.openlca.io.ecospold2.output.EcoSpold2Export
import java.io.File

class ExportEcoSpold2 : Export {

    override val format = Format.ECOSPOLD_2

    private val cache = Server.cache!!

    override fun doIt(p: Process, db: IDatabase): File {
        val file = cache.file(p.refId, format)
        if (file.exists())
            return file
        val dir = cache.tempDir()
        val descriptors = listOf(Descriptors.toDescriptor(p))
        val exp = EcoSpold2Export(dir, db, descriptors)
        exp.run()
        cache.zipFilesAndClean(dir, file)
        return file
    }

}