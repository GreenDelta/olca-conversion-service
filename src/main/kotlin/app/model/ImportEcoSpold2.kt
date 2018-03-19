package app.model

import app.Server
import org.openlca.core.database.IDatabase
import org.openlca.core.database.ProcessDao
import org.openlca.core.model.Process
import org.openlca.io.ecospold2.input.EcoSpold2Import
import org.slf4j.LoggerFactory
import java.net.URL

class ImportEcoSpold2 : Import {

    val log = LoggerFactory.getLogger(javaClass)

    override fun doIt(url: String, db: IDatabase): Process {
        log.info("import EcoSpold 2 from {}", url)
        val tempFile = Server.cache!!.tempFile(ext = ".spold")
        log.debug("copy data to {}", tempFile)
        URL(url).openStream().use { input ->
            tempFile.outputStream().use { output ->
                input.copyTo(output)
            }
        }
        val imp = EcoSpold2Import(db, arrayOf(tempFile))
        imp.run()
        log.debug("data imported; delete file {}", tempFile)
        tempFile.delete()
        return findProcess(db)
    }

    private fun findProcess(db: IDatabase): Process {
        val dao = ProcessDao(db)
        val descriptors = dao.descriptors
        if (descriptors.size > 0) {
            return dao.getForId(descriptors.first().id)
        }
        throw Exception("Did not found a process")
    }
}