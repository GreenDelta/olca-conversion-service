package app.model

import app.Server
import org.openlca.ilcd.io.SodaClient
import org.openlca.ilcd.io.SodaConnection
import org.openlca.io.ilcd.ILCDImport
import org.openlca.io.ilcd.input.ImportConfig

class ImportILCD : Import {

    override fun doIt(url: String): String {
        if(url == null || url.isEmpty() || !url.contains("/processes/"))
            throw Exception("Invalid URL: $url")
        val parts = url.split("/processes/")
        val con = SodaConnection()
        con.url = parts[0]
        val client = SodaClient(con)
        client.connect()
        val conf = ImportConfig(client, Server.db)
        val imp = ILCDImport(conf)
        val id = parts[1]
        imp.importProcess(id)
        return id
    }
}