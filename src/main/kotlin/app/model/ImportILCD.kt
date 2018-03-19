package app.model

import org.openlca.core.database.IDatabase
import org.openlca.core.model.Process
import org.openlca.ilcd.io.SodaClient
import org.openlca.ilcd.io.SodaConnection
import org.openlca.io.ilcd.ILCDImport
import org.openlca.io.ilcd.input.ImportConfig

class ImportILCD : Import {

    override fun doIt(url: String, db: IDatabase): Process {
        if (url.isEmpty() || !url.contains("/processes/"))
            throw Exception("Invalid URL: $url")
        val parts = url.split("/processes/")
        val id = parts[1]
        val p = DB.get(id, db)
        if (p != null)
            return p
        val con = SodaConnection()
        con.url = parts[0]
        val client = SodaClient(con)
        client.connect()
        val conf = ImportConfig(client, db)
        val imp = ILCDImport(conf)
        imp.importProcess(id)
        return DB.get(id, db)!!
    }
}