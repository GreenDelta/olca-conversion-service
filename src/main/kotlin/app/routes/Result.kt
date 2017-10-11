package app.routes

import app.Server
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.Response

@Path("result")
class Result {

    @GET
    @Produces("application/zip")
    @Path("{name}")
    fun get(@PathParam("name") name: String): Response {
        val file = Server.workspace!!.file(name)
        return Response.ok(file as Any).type("application/zip")
                .header("Content-Disposition","attachment; " +
                        "filename=\"${file.name}\"").build()
    }

}