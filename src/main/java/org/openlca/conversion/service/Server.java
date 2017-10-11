package org.openlca.conversion.service;

import java.io.File;
import java.net.URI;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.openlca.core.database.IDatabase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Server {

	private static Logger log = LoggerFactory.getLogger(Server.class);

	public static IDatabase db;

	public static void main(String[] args) {
		try {
			Config config = getConfig(args);
			db = config.initDB();
			HttpServer server = createServer(config);
			addShutdownHook(db, server);
			log.info("starting server");
			server.start();
			log.info("Press CTRL^C to exit..");
			Thread.currentThread().join();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static Config getConfig(String[] args) {
		File file = new File("config.json");
		if (file.exists())
			return Config.fromFile(file);
		if (args != null && args.length > 0)
			return Config.fromFile(new File(args[0]));
		return Config.getDefault();
	}

	private static void addShutdownHook(IDatabase db, HttpServer server) {
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			try {
				db.close();
				server.shutdownNow();
			} catch (Exception e) {
				log.error("failed to shutdown service", e);
			}
		}, "shutdownHook"));
	}

	private static HttpServer createServer(Config config)
			throws Exception {
		ResourceConfig resourceConfig = new ResourceConfig().packages(
				"org.openlca.conversion.service.resources");
		return GrizzlyHttpServerFactory.createHttpServer(URI.create(
				"http://localhost:" + config.port), resourceConfig);
	}
}
