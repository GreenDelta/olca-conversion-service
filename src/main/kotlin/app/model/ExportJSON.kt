package app.model

import app.Server
import org.openlca.core.database.IDatabase
import org.openlca.core.database.ProcessDao
import org.openlca.jsonld.ZipStore
import org.openlca.jsonld.output.JsonExport
import java.io.File

class ExportJSON : Export {

    override val format = Format.JSON_LD

    override fun doIt(db: IDatabase): File {
        val file = Server.cache!!.nextZip()
        val store = ZipStore.open(file)
        val exp = JsonExport(db, store)
        ProcessDao(db).all.forEach { exp.write(it) }
        store.close()
        return file
    }
}
