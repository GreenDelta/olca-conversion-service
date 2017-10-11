package app.routes

import app.model.ConversionInfo
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
        val msg = "the conversion from ${info.sourceFormat} " +
                "to ${info.targetFormat} is currently not implemented"
        return Response.status(Response.Status.NOT_IMPLEMENTED)
                .entity(msg)
                .type(MediaType.TEXT_PLAIN)
                .build()
    }

}
