package app.routes

import app.model.*
import com.google.gson.Gson
import java.nio.file.Files
import javax.ws.rs.Consumes
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("convert")
class Converter {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    fun convert(body: String): Response {
        val info = Gson().fromJson(body, ConversionInfo::class.java)
        val imp = getImport(info)
        if (imp == null) {
            val msg = "unsupported source format: ${info.sourceFormat}"
            return Response.status(Response.Status.NOT_IMPLEMENTED)
                    .entity(msg).type(MediaType.TEXT_PLAIN).build()
        }
        val exp = getExport(info)
        if (exp == null) {
            val msg = "unsupported target format: ${info.targetFormat}"
            return Response.status(Response.Status.NOT_IMPLEMENTED)
                    .entity(msg).type(MediaType.TEXT_PLAIN).build()
        }
        try {
            val processID = imp.doIt(info.url)
            val file = exp.doIt(processID)
            val bytes = Files.readAllBytes(file.toPath())
            return Response.ok(bytes).type("application/zip")
                    .header("Content-Disposition","attachment; " +
                    "filename=\"${file.name}\"").build()
        } catch (e: Exception) {
            val msg = "Conversion failed: ${e.message}"
            return Response.status(Response.Status.NOT_IMPLEMENTED)
                    .entity(msg).type(MediaType.TEXT_PLAIN).build()
        }
    }

    private fun getImport(info: ConversionInfo): Import? {
        val format = Format.get(info.sourceFormat)
        return when(format) {
            Format.ILCD -> ImportILCD()
            else -> null
        }
    }

    private fun getExport(info: ConversionInfo): Export? {
        val format = Format.get(info.targetFormat)
        return when(format) {
            Format.JSON_LD -> ExportJSON()
            else -> null
        }
    }
}
