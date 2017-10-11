package app.routes

import app.model.ConversionInfo
import app.model.Format
import app.model.ImportILCD
import app.model.Import
import com.google.gson.Gson
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
        imp.doIt(info.url)
        val msg = "the conversion from ${info.sourceFormat} " +
                "to ${info.targetFormat} is currently not implemented"
        return Response.status(Response.Status.NOT_IMPLEMENTED)
                .entity(msg).type(MediaType.TEXT_PLAIN).build()
    }

    private fun getImport(info: ConversionInfo): Import? {
        val format = Format.get(info.sourceFormat)
        return when(format) {
            Format.ILCD -> ImportILCD()
            else -> null
        }
    }

}
