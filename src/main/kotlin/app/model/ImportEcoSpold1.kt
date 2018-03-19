package app.model

import org.openlca.core.database.IDatabase
import org.openlca.core.model.ModelType
import org.openlca.core.model.Process
import org.openlca.ecospold.io.DataSetType
import org.openlca.io.UnitMapping
import org.openlca.io.ecospold1.input.EcoSpold01Import
import java.net.URL

class ImportEcoSpold1 : Import {

    override fun doIt(url: String, db: IDatabase): Process {
        val imp = EcoSpold01Import(db, UnitMapping.createDefault(db))
        URL(url).openStream().use { stream ->
            imp.run(stream, DataSetType.PROCESS)
        }
        imp.infos.forEach { info ->
            if (info.entity.modelType == ModelType.PROCESS)
                return DB.get(info.entity.refId, db)!!
        }
        throw Exception("Did not found a process in the file")
    }
}