package app.model

import org.openlca.core.database.IDatabase
import org.openlca.ilcd.io.SodaClient
import org.openlca.ilcd.io.SodaConnection
import org.openlca.io.ilcd.input.ImportConfig
import org.openlca.io.ilcd.input.ProcessImport
import org.openlca.io.ilcd.input.ProviderLinker

class ImportILCD : Import {

    override fun doIt(setup: ConversionSetup, db: IDatabase) {
        val url = setup.url
        if (url.isEmpty() || !url.contains("/processes/"))
            throw Exception("Invalid URL: $url")
        val parts = url.split("/processes/")
        val id = parts[1]
        val con = SodaConnection()
        con.url = parts[0]
        val client = SodaClient(con)
        client.connect()
        val conf = ImportConfig(client, db)
        conf.flowMap = setup.flowMap()
        val linker = ProviderLinker()
        val imp = ProcessImport(conf, linker)
        imp.run(id)
    }
}
