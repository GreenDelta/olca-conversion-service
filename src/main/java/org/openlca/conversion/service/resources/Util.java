package org.openlca.conversion.service.resources;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

class Util {

	private Util() {
	}

	static Response unexpectedException(Exception e) {
		return Response.serverError()
				.entity("Unexpected exception: " + e.getMessage())
				.type(MediaType.TEXT_PLAIN)
				.build();
	}

	static Response badRequest(String message) {
		return Response.status(Status.BAD_REQUEST)
				.entity(message)
				.type(MediaType.TEXT_PLAIN)
				.build();
	}
}
