package app.model

import app.Server
import org.openlca.core.database.IDatabase
import org.openlca.core.database.ProcessDao
import org.openlca.io.ecospold2.output.EcoSpold2Export
import java.io.File

class ExportEcoSpold2 : Export {

    override val format = Format.ECOSPOLD_2

    private val cache = Server.cache!!

    override fun doIt(db: IDatabase): File {
        val dir = cache.tempDir()
        val exp = EcoSpold2Export(dir, db, ProcessDao(db).descriptors)
        exp.run()
        return cache.zipFilesAndClean(dir)
    }

}