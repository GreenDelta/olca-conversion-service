package app.model

import app.Server
import org.openlca.core.database.ProcessDao
import org.openlca.core.model.Process

internal object DB {

    val db = Server.db!!

    fun get(id: String): Process? {
        val dao = ProcessDao(Server.db)
        return dao.getForRefId(id)
    }
}
