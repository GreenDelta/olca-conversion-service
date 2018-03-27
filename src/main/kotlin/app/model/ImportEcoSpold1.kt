package app.model

import org.openlca.core.database.IDatabase
import org.openlca.ecospold.io.DataSetType
import org.openlca.io.ecospold1.input.EcoSpold01Import
import org.openlca.io.ecospold1.input.ImportConfig
import java.net.URL

class ImportEcoSpold1 : Import {

    override fun doIt(setup: ConversionSetup, db: IDatabase) {
        val config = ImportConfig(db)
        config.flowMap = setup.flowMap()
        val imp = EcoSpold01Import(config)
        URL(setup.url).openStream().use { stream ->
            imp.run(stream, DataSetType.PROCESS)
        }
    }
}
