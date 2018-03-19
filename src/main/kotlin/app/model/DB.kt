package app.model

import app.Server
import org.openlca.core.database.IDatabase
import org.openlca.core.database.ProcessDao
import org.openlca.core.model.Process

internal object DB {

    fun get(id: String, db: IDatabase): Process? {
        val dao = ProcessDao(db)
        return dao.getForRefId(id)
    }
}
