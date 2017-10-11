package org.openlca.conversion.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import com.google.gson.Gson;
import org.openlca.core.database.IDatabase;
import org.openlca.core.database.derby.DerbyDatabase;

public class Config {

	public int port;
	public String workspace;
	public DerbyConfig derbyDB;

	public static class DerbyConfig {
		public String folder;
	}

	public static Config getDefault() {
		Config config = new Config();
		config.port = 8080;
		config.workspace = "workspace";
		config.derbyDB = new DerbyConfig();
		config.derbyDB.folder = "database";
		return config;
	}

	public static Config fromFile(File file) {
		Gson gson = new Gson();
		try (FileInputStream is = new FileInputStream(file);
		     InputStreamReader reader = new InputStreamReader(is, "utf-8");
		     BufferedReader buffer = new BufferedReader(reader)) {
			return gson.fromJson(buffer, Config.class);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public IDatabase initDB() {
		if (derbyDB != null && derbyDB.folder != null)
			return new DerbyDatabase(new File(derbyDB.folder));
		throw new RuntimeException("no database configuration found");
	}
}
