package app.model

import app.Server
import org.openlca.core.database.IDatabase
import org.openlca.io.simapro.csv.input.SimaProCsvImport
import org.slf4j.LoggerFactory
import java.net.URL

class ImportSimaProCsv: Import {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun doIt(setup: ConversionSetup, db: IDatabase) {
        log.info("import SimaPro CSV from {}", setup.url)
        val tempFile = Server.cache!!.tempFile(ext = ".csv")
        log.debug("copy data to {}", tempFile)
        URL(setup.url).openStream().use { input ->
            tempFile.outputStream().use { output ->
                input.copyTo(output)
            }
        }
        val imp = SimaProCsvImport(db, arrayOf(tempFile))
        imp.run()
        log.debug("data imported; delete file {}", tempFile)
        tempFile.delete()
    }
}
