package butti.javalibs.config;

import java.io.File;

public class ConfigPath {
	public static String getSettingsPath() {
		String path = Config.get("settingsPath");

		File f = new File(path);
		if (!f.exists()) {
			f.mkdirs();
		}

		return path;
	}

	public static String getErrorLogPath() {
		return Config.get("errorlogPath");
	}
}
