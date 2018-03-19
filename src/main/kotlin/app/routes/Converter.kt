package app.routes

import app.Server
import app.model.*
import com.google.gson.Gson
import org.openlca.core.database.IDatabase
import javax.ws.rs.Consumes
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response
import javax.ws.rs.core.Response.Status

@Path("convert")
class Converter {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    fun convert(body: String): Response {
        val info = Gson().fromJson(body, ConversionInfo::class.java)
        val imp = getImport(info)
        if (imp == null) {
            val msg = "unsupported source format: ${info.sourceFormat}"
            return respond(msg, Status.NOT_IMPLEMENTED)
        }
        val exp = getExport(info)
        if (exp == null) {
            val msg = "unsupported target format: ${info.targetFormat}"
            return respond(msg, Status.NOT_IMPLEMENTED)
        }
        return doIt(info, imp, exp)
    }

    private fun doIt(info: ConversionInfo, imp: Import, exp: Export): Response {
        var db: IDatabase? = null
        return try {
            val refSystem = Server.getRefSystem(info.refSystem)
            db = refSystem.db()
            val p = imp.doIt(info.url, db)
            val file = exp.doIt(p, db)
            val result = ConversionResult()
            result.zipFile = file.name
            result.process = Server.cache!!.process(p.refId, exp.format)
            result.format = exp.format.label
            Response.ok(result, MediaType.APPLICATION_JSON_TYPE).build()
        } catch (e: Exception) {
            val msg = "Conversion failed: ${e.message}"
            respond(msg, Status.INTERNAL_SERVER_ERROR)
        } finally {
            db?.close()
        }
    }

    private fun respond(msg: String, stat: Status): Response {
        return Response.status(stat)
                .entity(msg)
                .type(MediaType.TEXT_PLAIN)
                .build()
    }

    private fun getImport(info: ConversionInfo): Import? {
        val format = Format.get(info.sourceFormat)
        return when(format) {
            Format.ILCD -> ImportILCD()
            Format.ECOSPOLD_1 -> ImportEcoSpold1()
            Format.JSON_LD -> ImportJSON()
            else -> null
        }
    }

    private fun getExport(info: ConversionInfo): Export? {
        val format = Format.get(info.targetFormat)
        return when(format) {
            Format.JSON_LD -> ExportJSON()
            Format.ILCD -> ExportILCD()
            Format.ECOSPOLD_1 -> ExportEcoSpold1()
            else -> null
        }
    }
}
