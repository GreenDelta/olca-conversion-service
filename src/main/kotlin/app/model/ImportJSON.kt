package app.model

import org.openlca.core.model.ModelType
import org.openlca.core.model.Process
import org.openlca.jsonld.input.JsonImport

class ImportJSON : Import {

    override fun doIt(url: String): Process {
        if (!url.contains("/PROCESS/"))
            throw Exception("Invalid URL: $url")
        val parts = url.split("/PROCESS/")
        val id = parts[1]
        val p = DB.get(id)
        if (p != null)
            return p
        val imp = JsonImport(LcaRepo(parts[0]), DB.db)
        imp.run(ModelType.PROCESS, id)
        return DB.get(id)!!
    }
}