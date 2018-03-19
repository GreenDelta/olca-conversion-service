package app.model

import app.Server
import org.openlca.core.database.IDatabase
import org.openlca.core.model.Process
import org.openlca.io.ilcd.ILCDExport
import org.openlca.io.ilcd.output.ExportConfig
import java.io.File

class ExportILCD : Export {

    override val format = Format.ILCD

    override fun doIt(p: Process, db: IDatabase): File {
        val file = Server.cache!!.file(p.refId, format)
        if (file.exists())
            return file
        val conf = ExportConfig(db, file)
        val exp = ILCDExport(conf)
        exp.export(p)
        exp.close()
        return file
    }
}
