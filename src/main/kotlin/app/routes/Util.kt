package app.routes

import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import jakarta.ws.rs.core.Response.Status

internal object Util {

    fun unexpectedException(e: Exception): Response {
        return Response.serverError()
                .entity("Unexpected exception: " + e.message)
                .type(MediaType.TEXT_PLAIN)
                .build()
    }

    fun badRequest(message: String): Response {
        return Response.status(Status.BAD_REQUEST)
                .entity(message)
                .type(MediaType.TEXT_PLAIN)
                .build()
    }
}
