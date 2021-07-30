package app.model

import org.openlca.core.database.IDatabase
import org.openlca.ilcd.io.ZipStore
import org.openlca.io.ilcd.ILCDImport
import org.openlca.io.ilcd.input.ImportConfig
import app.Server
import org.slf4j.LoggerFactory
import java.net.URL


class ImportILCD : Import {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun doIt(setup: ConversionSetup, db: IDatabase) {
        val url = setup.url
        if (url.isEmpty() || !url.contains("/processes/"))
            throw Exception("Invalid URL: $url")
        val parts = url.split("/processes/")
        val id = parts[1]
        val baseurl = parts[0]

		// the original URL looks like https://acme.org/resource/processes/f00b951c-e67f-4f7f-8dd4-665de3974e18?version=00.00.000
		// we want to extract the base URL, the UUID and the version number
		
		// TODO extract the parts	
		
		val uuid = "a145b74c-c838-4b88-81cb-981aee9e8e2b"
		val version = "02.25.099"
		
		val zipurl = baseurl.plus("/processes/").plus(uuid).plus("/zipexport?").plus(version)
		
		// instead of creating a SodaClient as DatasSource, we'll fetch
		// the dataset including its dependencies as ZIP
        log.debug("calling URL {}", zipurl)
        
		val tempFile = Server.cache!!.tempFile(ext = ".zip")
        log.debug("copy data to {}", tempFile)
        URL(zipurl).openStream().use { input ->
            tempFile.outputStream().use { output ->
                input.copyTo(output)
            }
        }

		val store = ZipStore(tempFile)
		
        val conf = ImportConfig(store, db)
        conf.flowMap = setup.flowMap()
        val imp = ILCDImport(conf)
        imp.run()
		
        log.debug("data imported; delete file {}", tempFile)
        tempFile.delete()

    }
}
