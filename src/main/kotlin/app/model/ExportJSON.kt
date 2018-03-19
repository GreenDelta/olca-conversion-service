package app.model

import app.Server
import org.openlca.core.database.IDatabase
import org.openlca.core.model.Process
import org.openlca.jsonld.ZipStore
import org.openlca.jsonld.output.JsonExport
import java.io.File

class ExportJSON : Export {

    override val format = Format.JSON_LD

    override fun doIt(p: Process, db: IDatabase): File {
        val file = Server.cache!!.file(p.refId, Format.JSON_LD)
        if (file.exists())
            return file
        val store = ZipStore.open(file)
        val exp = JsonExport(db, store)
        exp.write(p)
        store.close()
        return file
    }
}
