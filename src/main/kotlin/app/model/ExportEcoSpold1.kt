package app.model

import app.Server
import org.openlca.core.database.IDatabase
import org.openlca.core.model.Process
import org.openlca.io.ecospold1.output.EcoSpold1Export
import org.openlca.io.ecospold1.output.ExportConfig
import java.io.BufferedOutputStream
import java.io.File
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

class ExportEcoSpold1: Export {

    override val format = Format.ECOSPOLD_1

    private val cache = Server.cache!!

    override fun doIt(p: Process, db: IDatabase): File {
        val file = cache.file(p.refId, format)
        if (file.exists())
            return file
        val dir = cache.tempDir()
        val exp = EcoSpold1Export(dir, ExportConfig.getDefault())
        exp.export(p)
        exp.close()
        cache.zipFilesAndClean(dir, file)
        return file
    }
}
