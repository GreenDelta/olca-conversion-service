package org.openlca.conversion.service.resources;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.openlca.conversion.service.Config;
import org.openlca.core.database.IDatabase;
import org.openlca.core.database.ProcessDao;
import org.openlca.core.model.Process;
import org.openlca.ilcd.io.NetworkClient;
import org.openlca.io.ilcd.input.ProcessImport;

@Path("soda")
public class Soda {

	private final IDatabase database;
	private final Config config;

	@Inject
	public Soda(IDatabase database, Config config) {
		this.database = database;
		this.config = config;
	}

	@GET
	@Path("process")
	public Response getProcess(
			@QueryParam("baseUrl") String baseUrl,
			@QueryParam("uuid") String uuid,
			@QueryParam("format") String format,
			@QueryParam("mime") String mime) {
		try {
			ProcessDao dao = new ProcessDao(database);
			Process process = dao.getForRefId(uuid);
			if (process != null)
				return serveProcess(uuid, format, mime);
			NetworkClient client = new NetworkClient(baseUrl);
			ProcessImport processImport = new ProcessImport(client, database);
			processImport.run(uuid);
			return serveProcess(uuid, format, mime);
		} catch (Exception e) {
			return Util.unexpectedException(e);
		}

	}

	private Response serveProcess(String uuid, String format, String mime) {
		if (format == null)
			return Util.badRequest("no format specified");
		DatabaseProcess service = new DatabaseProcess(database, config);
		switch (format) {
		case "ecoSpold1":
			return service.getEcoSpold1(uuid, mime);
		case "ecoSpold2":
			return service.getEcoSpold2(uuid, mime);
		case "ilcd":
			return service.getILCD(uuid, mime);
		default:
			return Util.badRequest("unknown format: " + format
					+ ". Only the values ecoSpold1, "
					+ "ecoSpold2, and ilcd are allowed");
		}
	}

}
