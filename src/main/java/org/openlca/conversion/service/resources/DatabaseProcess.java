package org.openlca.conversion.service.resources;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.openlca.conversion.service.Config;
import org.openlca.core.database.IDatabase;
import org.openlca.core.database.ProcessDao;
import org.openlca.core.model.Process;
import org.openlca.core.model.descriptors.Descriptors;
import org.openlca.core.model.descriptors.ProcessDescriptor;
import org.openlca.ilcd.io.FileStore;
import org.openlca.io.ecospold1.exporter.EcoSpold01Outputter;
import org.openlca.io.ecospold2.output.EcoSpold2Export;
import org.openlca.io.ilcd.output.ProcessExport;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

@Path("database/process")
public class DatabaseProcess {

	private final IDatabase database;
	private final Config config;

	@Inject
	public DatabaseProcess(IDatabase database, Config config) {
		this.database = database;
		this.config = config;
	}

	@GET
	@Path("ecoSpold1")
	public Response getEcoSpold1(
			@QueryParam("uuid") String uuid,
			@QueryParam("mime") String mime) {
		try {
			File rootFolder = config.getEcoSpold01Folder();
			File processFolder = new File(rootFolder, "EcoSpold01");
			String fileName = "process_" + uuid + ".xml";
			File file = new File(processFolder, fileName);
			if (file.exists())
				return serveFile(file, "EcoSpold01", mime);
			ProcessDao dao = new ProcessDao(database);
			Process process = dao.getForRefId(uuid);
			if (process == null)
				return noProcessFoundError(uuid);
			EcoSpold01Outputter outputter = new EcoSpold01Outputter(
					rootFolder);
			outputter.exportProcess(process);
			return serveFile(file, "EcoSpold01", mime);
		} catch (Exception e) {
			return Util.unexpectedException(e);
		}
	}

	@GET
	@Path("ecoSpold2")
	public Response getEcoSpold2(
			@QueryParam("uuid") String uuid,
			@QueryParam("mime") String mime) {
		try {
			File rootFolder = config.getEcoSpold02Folder();
			File processFolder = new File(rootFolder, "Activities");
			String fileName = uuid + ".spold";
			File file = new File(processFolder, fileName);
			if (file.exists())
				return serveFile(file, "EcoSpold02", mime);
			ProcessDao dao = new ProcessDao(database);
			Process process = dao.getForRefId(uuid);
			if (process == null)
				return noProcessFoundError(uuid);
			ProcessDescriptor descriptor = Descriptors.toDescriptor(process);
			List<ProcessDescriptor> descriptors = Arrays.asList(descriptor);
			EcoSpold2Export export = new EcoSpold2Export(rootFolder, database,
					descriptors);
			export.run();
			return serveFile(file, "EcoSpold02", mime);
		} catch (Exception e) {
			return Util.unexpectedException(e);
		}
	}

	@GET
	@Path("ilcd")
	public Response getILCD(
			@QueryParam("uuid") String uuid,
			@QueryParam("mime") String mime) {
		try {
			File rootFolder = config.getILCDFolder();
			File processFolder = new File(rootFolder, "ILCD/processes");
			String fileName = uuid + ".xml";
			File file = new File(processFolder, fileName);
			if (file.exists())
				return serveFile(file, "ILCD", mime);
			ProcessDao dao = new ProcessDao(database);
			Process process = dao.getForRefId(uuid);
			if (process == null)
				return noProcessFoundError(uuid);
			FileStore fileStore = new FileStore(rootFolder);
			ProcessExport export = new ProcessExport(database, fileStore);
			export.run(process);
			return serveFile(file, "ILCD", mime);
		} catch (Exception e) {
			return Util.unexpectedException(e);
		}
	}

	private Response serveFile(File file, String type, String mime) {
		if (!file.exists())
			return fileNotFoundError(file);
		if ("json".equalsIgnoreCase(mime))
			return serveJson(file, type);
		else
			return Response.ok(file).type(MediaType.APPLICATION_XML).build();
	}

	private Response serveJson(File file, String format) {
		try {
			String xml = FileUtils.readFileToString(file, "utf-8");
			String escapedXml = StringEscapeUtils.escapeHtml4(xml);
			JsonObject object = new JsonObject();
			object.addProperty("format", format);
			object.addProperty("xml", escapedXml);
			String json = new Gson().toJson(object);
			return Response.ok(json).type(MediaType.APPLICATION_JSON).build();
		} catch (Exception e) {
			return Util.unexpectedException(e);
		}
	}

	private Response fileNotFoundError(File file) {
		return Response.status(Status.NOT_FOUND)
				.entity("could not find file: " + file.getAbsolutePath())
				.type(MediaType.TEXT_PLAIN)
				.build();
	}

	private Response noProcessFoundError(String uuid) {
		return Response.status(Status.NOT_FOUND)
				.entity("no process with uuid=" + uuid + " in database")
				.type(MediaType.TEXT_PLAIN)
				.build();
	}

}
