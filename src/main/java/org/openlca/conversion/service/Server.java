package org.openlca.conversion.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import org.glassfish.grizzly.http.server.HttpServer;
import org.openlca.core.database.IDatabase;
import org.openlca.core.database.derby.DerbyDatabase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.sun.jersey.api.container.grizzly2.GrizzlyServerFactory;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.core.spi.component.ioc.IoCComponentProviderFactory;
import com.sun.jersey.guice.spi.container.GuiceComponentProviderFactory;

public class Server {

	private static Logger log = LoggerFactory.getLogger(Server.class);

	public static void main(String[] args) {
		try {
			Config config = getConfig(args);
			IDatabase db = config.initDB();
			HttpServer server = createServer(config, db);
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
				server.stop();
			} catch (Exception e) {
				log.error("failed to shutdown service", e);
			}
		}, "shutdownHook"));
	}

	private static HttpServer createServer(Config config, final IDatabase db)
			throws Exception {
		ResourceConfig resourceConfig = new PackagesResourceConfig(
				"org.openlca.conversion.service.resources");
		Injector injector = Guice.createInjector((binder) -> {
			binder.bind(IDatabase.class).toInstance(db);
			binder.bind(Config.class).toInstance(config);
		});
		IoCComponentProviderFactory ioc = new GuiceComponentProviderFactory(
				resourceConfig, injector);
		return GrizzlyServerFactory.createHttpServer(
				"http://localhost:" + config.port, resourceConfig, ioc);
	}
}
