package app.model

import app.Server
import org.openlca.core.database.IDatabase
import org.openlca.core.database.ProcessDao
import org.openlca.io.ilcd.ILCDExport
import org.openlca.io.ilcd.output.ExportConfig
import java.io.File

class ExportILCD : Export {

    override val format = Format.ILCD

    override fun doIt(db: IDatabase): File {
        val file = Server.cache!!.nextZip()
        val conf = ExportConfig(db, file)
        val exp = ILCDExport(conf)
        ProcessDao(db).all.forEach { exp.export(it) }
        exp.close()
        return file
    }
}
