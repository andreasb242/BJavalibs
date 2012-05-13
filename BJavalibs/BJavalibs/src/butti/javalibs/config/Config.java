package butti.javalibs.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Main configuration of the Application
 * 
 * @author Andreas Butti
 */
public class Config {
	private static Properties config = new Properties();
	private static String applicationName;
	private static String applicationPath;
	private static String configFile;

	public static void initConfig(String applicationName, String applicationPath) throws IOException {
		initConifg(applicationName, applicationPath, "config/config.properties");
	}

	public static void initConifg(String applicationName, String applicationPath, String configFile) throws IOException {
		if (Config.applicationName != null) {
			throw new RuntimeException("Configuration already initialized!");
		}

		Config.applicationName = applicationName;
		Config.configFile = configFile;
		Config.applicationPath = applicationPath;

		if (applicationName == null) {
			throw new NullPointerException("applicationName == null");
		}

		FileInputStream in = new FileInputStream(configFile);
		config.load(in);
		in.close();
	}

	public static String getApplicationName() {
		if (applicationName == null) {
			throw new RuntimeException("Configuration not initialized, call initConifg first");
		}

		return applicationName;
	}

	public static String getApplicationPath() {
		if (applicationPath == null) {
			throw new RuntimeException("Configuration not initialized, call initConifg first");
		}

		return applicationPath;
	}
	
	public static String getConfigFile() {
		if (applicationName == null) {
			throw new RuntimeException("Configuration not initialized, call initConifg first");
		}

		return configFile;
	}

	public static String get(String key) {
		if (applicationName == null) {
			throw new RuntimeException("Configuration not initialized, call initConifg first");
		}

		return config.getProperty(key);
	}

	public static String[] getArray(String key) {
		if (applicationName == null) {
			throw new RuntimeException("Configuration not initialized, call initConifg first");
		}

		String s = get(key);
		if (s == null) {
			return new String[] {};
		}
		return s.split(";");
	}
}
