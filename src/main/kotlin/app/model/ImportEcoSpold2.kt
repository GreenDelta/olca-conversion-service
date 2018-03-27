package app.model

import app.Server
import org.openlca.core.database.IDatabase
import org.openlca.io.ecospold2.input.EcoSpold2Import
import org.openlca.io.ecospold2.input.ImportConfig
import org.slf4j.LoggerFactory
import java.net.URL

class ImportEcoSpold2 : Import {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun doIt(setup: ConversionSetup, db: IDatabase) {
        log.info("import EcoSpold 2 from {}", setup.url)
        val tempFile = Server.cache!!.tempFile(ext = ".spold")
        log.debug("copy data to {}", tempFile)
        URL(setup.url).openStream().use { input ->
            tempFile.outputStream().use { output ->
                input.copyTo(output)
            }
        }
        val config = ImportConfig(db)
        config.flowMap = setup.flowMap()
        val imp = EcoSpold2Import(config)
        imp.setFiles(arrayOf(tempFile))
        imp.run()
        log.debug("data imported; delete file {}", tempFile)
        tempFile.delete()
    }
}
