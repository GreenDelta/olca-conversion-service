package org.openlca.conversion.service.resources;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.openlca.conversion.service.Config;
import org.openlca.core.database.ActorDao;
import org.openlca.core.database.FlowDao;
import org.openlca.core.database.FlowPropertyDao;
import org.openlca.core.database.IDatabase;
import org.openlca.core.database.ProcessDao;
import org.openlca.core.database.RootEntityDao;
import org.openlca.core.database.SourceDao;
import org.openlca.core.database.UnitGroupDao;

@Path("database")
public class DatabaseResource {

	private final IDatabase database;
	private final Config config;

	@Inject
	public DatabaseResource(IDatabase database, Config config) {
		this.database = database;
		this.config = config == null ? Config.getDefault() : config;
	}

	@GET
	@Path("actors")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getActors() {
		return getDescriptors(new ActorDao(database));
	}

	@GET
	@Path("sources")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSources() {
		return getDescriptors(new SourceDao(database));
	}

	@GET
	@Path("unitGroups")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUnitGroups() {
		return getDescriptors(new UnitGroupDao(database));
	}

	@GET
	@Path("flowProperties")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getFlowProperties() {
		return getDescriptors(new FlowPropertyDao(database));
	}

	@GET
	@Path("flows")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getFlows() {
		return getDescriptors(new FlowDao(database));
	}

	@GET
	@Path("processes")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getProcesses() {
		return getDescriptors(new ProcessDao(database));
	}

	private Response getDescriptors(RootEntityDao<?, ?> dao) {
		try {
			List<?> descriptors = dao.getDescriptors();
			String json = config.getGson().toJson(descriptors);
			return Response.ok(json).build();
		} catch (Exception e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR)
					.entity(e).build();
		}
	}

}
