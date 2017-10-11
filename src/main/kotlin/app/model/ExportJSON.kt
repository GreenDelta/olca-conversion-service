package app.model

import app.Server
import org.openlca.core.database.ProcessDao
import org.openlca.jsonld.ZipStore
import org.openlca.jsonld.output.JsonExport
import java.io.File

class ExportJSON: Export {

    override fun doIt(id: String): File {
        val file = Server.workspace!!.file(id, Format.JSON_LD)
        if (file.exists())
            return file
        val store = ZipStore.open(file)
        val exp = JsonExport(Server.db, store)
        val dao = ProcessDao(Server.db)
        val proc = dao.getForRefId(id)
        exp.write(proc)
        store.close()
        return file
    }
}