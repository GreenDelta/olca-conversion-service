package app.routes

import app.Server
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response

@Path("refsystems")
class RefSystemRoute {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun get(): Response {
        val names = Server.refSystems.map { it.name }
        return Response.ok(names, MediaType.APPLICATION_JSON_TYPE).build()
    }

}
