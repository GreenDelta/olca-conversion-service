package app.model

import app.Server
import org.openlca.core.database.IDatabase
import org.openlca.core.database.ProcessDao
import org.openlca.io.ecospold1.output.EcoSpold1Export
import org.openlca.io.ecospold1.output.ExportConfig
import java.io.File

class ExportEcoSpold1: Export {

    override val format = Format.ECOSPOLD_1

    private val cache = Server.cache!!

    override fun doIt(db: IDatabase): File {
        val dir = cache.tempDir()
        val exp = EcoSpold1Export(dir, ExportConfig.getDefault())
        ProcessDao(db).all.forEach { exp.export(it) }
        exp.close()
        return cache.zipFilesAndClean(dir)
    }
}
