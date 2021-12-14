package app.routes

import app.Server
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response

@Path("result")
class Result {

    @GET
    @Path("{name}")
    fun get(@PathParam("name") name: String): Response {
        val file = Server.cache!!.getFile(name)
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