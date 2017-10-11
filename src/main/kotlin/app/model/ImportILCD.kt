package app.model

import app.Server
import org.openlca.core.database.ProcessDao
import org.openlca.ilcd.io.SodaClient
import org.openlca.ilcd.io.SodaConnection
import org.openlca.io.ilcd.ILCDImport
import org.openlca.io.ilcd.input.ImportConfig

class ImportILCD : Import {

    override fun doIt(url: String): String {
        if (url == null || url.isEmpty() || !url.contains("/processes/"))
            throw Exception("Invalid URL: $url")
        val parts = url.split("/processes/")
        val id = parts[1]
        if (exists(id))
            return id
        val con = SodaConnection()
        con.url = parts[0]
        val client = SodaClient(con)
        client.connect()
        val conf = ImportConfig(client, Server.db)
        val imp = ILCDImport(conf)
        imp.importProcess(id)
        return id
    }

    private fun exists(id: String): Boolean {
        val dao = ProcessDao(Server.db)
        return dao.contains(id) != null
    }
}