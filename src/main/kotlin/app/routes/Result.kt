package app.routes

import app.Server
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("result")
class Result {

    @GET
    @Path("{name}")
    fun get(@PathParam("name") name: String): Response {
        val file = Server.cache!!.file(name)
        if (!file.exists()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .type(MediaType.TEXT_PLAIN)
                    .entity("File $name does not exist")
                    .build()
        }
        return Response.ok(file as Any).type("application/zip")
                .header("Content-Disposition","attachment; " +
                        "filename=\"${file.name}\"").build()
    }
}