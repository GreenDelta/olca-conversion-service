package app.routes

import app.Server
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("refsystems")
class RefSystemRoute {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun get(): Response {
        val names = Server.refSystems.map { it.name }
        return Response.ok(names, MediaType.APPLICATION_JSON_TYPE).build()
    }

}
