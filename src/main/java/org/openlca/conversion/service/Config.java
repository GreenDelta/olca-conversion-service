package org.openlca.conversion.service;

import java.io.File;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Config {

	private boolean prettyPrinting;
	private String port;
	private File exportDir;
	private File databaseDir;

	public static Config getDefault() {
		Config config = new Config();
		config.setPrettyPrinting(true);
		config.exportDir = new File("exports");
		config.databaseDir = new File("database");
		config.port = "8080";
		return config;
	}

	public boolean isPrettyPrinting() {
		return prettyPrinting;
	}

	public void setPrettyPrinting(boolean prettyPrinting) {
		this.prettyPrinting = prettyPrinting;
	}

	public Gson getGson() {
		GsonBuilder builder = new GsonBuilder();
		if (prettyPrinting)
			builder.setPrettyPrinting();
		builder.setExclusionStrategies(new JsonExclusion());
		return builder.create();
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public File getDatabaseFolder() {
		return databaseDir;
	}

	public void setDatabaseFolder(File databaseDir) {
		this.databaseDir = databaseDir;
	}

	public void setExportFolder(File exportDir) {
		this.exportDir = exportDir;
	}

	public File getExportFolder() {
		return exportDir;
	}

	public File getEcoSpold01Folder() {
		return new File(exportDir, "EcoSpold01");
	}

	public File getEcoSpold02Folder() {
		return new File(exportDir, "EcoSpold02");
	}

	public File getILCDFolder() {
		return new File(exportDir, "ILCD");
	}
}
