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
		Config config = getConfig();
		try {
			IDatabase db = new DerbyDatabase(config.getDatabaseFolder());
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

	private static void addShutdownHook(final IDatabase db,
			final HttpServer server) {
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			public void run() {
				try {
					db.close();
					server.stop();
				} catch (Exception e) {
					log.error("failed to shutdown service", e);
				}
			}
		}, "shutdownHook"));
	}

	private static HttpServer createServer(Config config, final IDatabase db)
			throws Exception {
		ResourceConfig resourceConfig = new PackagesResourceConfig(
				"org.openlca.conversion.service.resources");
		Injector injector = Guice.createInjector(new Module() {
			public void configure(Binder binder) {
				binder.bind(IDatabase.class).toInstance(db);
				binder.bind(Config.class).toInstance(Config.getDefault());
			}
		});
		IoCComponentProviderFactory ioc = new GuiceComponentProviderFactory(
				resourceConfig, injector);
		final HttpServer server = GrizzlyServerFactory.createHttpServer(
				"http://localhost:" + config.getPort(), resourceConfig, ioc);
		return server;
	}

	private static Config getConfig() {
		File file = new File("config.ini");
		if (!file.exists()) {
			log.warn("config.ini not found; taking default configuration");
			return Config.getDefault();
		}
		try (InputStream is = new FileInputStream(file)) {
			Properties props = new Properties();
			props.load(is);
			return getConfig(props);
		} catch (Exception e) {
			log.error("failed to load config.ini", e);
			log.warn("taking default configuration");
			return Config.getDefault();
		}
	}

	private static Config getConfig(Properties props) {
		Config config = Config.getDefault();
		String port = props.getProperty("port");
		if (port != null)
			config.setPort(port);
		String exportPath = props.getProperty("exportFolder");
		if (exportPath != null)
			config.setExportFolder(new File(exportPath));
		String databasePath = props.getProperty("databaseFolder");
		if (databasePath != null)
			config.setDatabaseFolder(new File(databasePath));
		String prettyFormat = props.getProperty("prettyFormat");
		if (prettyFormat != null)
			config.setPrettyPrinting(Boolean.parseBoolean(prettyFormat));
		return config;
	}

}
