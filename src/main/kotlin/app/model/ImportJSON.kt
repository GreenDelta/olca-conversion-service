package app.model

import org.openlca.core.database.IDatabase
import org.openlca.core.model.ModelType
import org.openlca.core.model.Process
import org.openlca.jsonld.input.JsonImport

class ImportJSON : Import {

    override fun doIt(setup: ConversionSetup, db: IDatabase): Process {
        val url = setup.url
        if (!url.contains("/PROCESS/"))
            throw Exception("Invalid URL: $url")
        val parts = url.split("/PROCESS/")
        val id = parts[1]
        val p = DB.get(id, db)
        if (p != null)
            return p
        val imp = JsonImport(LcaRepo(parts[0]), db)
        imp.run(ModelType.PROCESS, id)
        return DB.get(id, db)!!
    }
}