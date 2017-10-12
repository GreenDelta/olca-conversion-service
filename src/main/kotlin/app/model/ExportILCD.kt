package app.model

import app.Server
import org.openlca.core.model.Process
import org.openlca.io.ilcd.ILCDExport
import org.openlca.io.ilcd.output.ExportConfig
import java.io.File

class ExportILCD : Export {

    override val format = Format.ILCD

    override fun doIt(p: Process): File {
        val file = Server.workspace!!.file(p.refId, Format.JSON_LD)
        if (file.exists())
            return file
        val conf = ExportConfig(Server.db, file)
        val exp = ILCDExport(conf)
        exp.export(p)
        exp.close()
        return file
    }
}
