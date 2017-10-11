package org.openlca.conversion.service.resources;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.google.gson.Gson;
import org.openlca.conversion.service.Server;
import org.openlca.core.database.ActorDao;
import org.openlca.core.database.FlowDao;
import org.openlca.core.database.FlowPropertyDao;
import org.openlca.core.database.ProcessDao;
import org.openlca.core.database.RootEntityDao;
import org.openlca.core.database.SourceDao;
import org.openlca.core.database.UnitGroupDao;

@Path("database")
public class DatabaseResource {

	@GET
	@Path("actors")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getActors() {
		return getDescriptors(new ActorDao(Server.db));
	}

	@GET
	@Path("sources")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSources() {
		return getDescriptors(new SourceDao(Server.db));
	}

	@GET
	@Path("unitGroups")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUnitGroups() {
		return getDescriptors(new UnitGroupDao(Server.db));
	}

	@GET
	@Path("flowProperties")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getFlowProperties() {
		return getDescriptors(new FlowPropertyDao(Server.db));
	}

	@GET
	@Path("flows")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getFlows() {
		return getDescriptors(new FlowDao(Server.db));
	}

	@GET
	@Path("processes")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getProcesses() {
		return getDescriptors(new ProcessDao(Server.db));
	}

	private Response getDescriptors(RootEntityDao<?, ?> dao) {
		try {
			List<?> descriptors = dao.getDescriptors();
			Gson gson = new Gson();
			String json = gson.toJson(descriptors);
			return Response.ok(json).build();
		} catch (Exception e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR)
					.entity(e).build();
		}
	}

}
