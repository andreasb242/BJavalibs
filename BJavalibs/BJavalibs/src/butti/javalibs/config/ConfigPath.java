package butti.javalibs.config;

import java.io.File;


public class ConfigPath {
	public static String getConfigPath() {
		String path = Config.get("configPath");
		
		File f = new File(path);
		if(!f.exists()) {
			f.mkdirs();
		}
		
		return path;
	}
	
	public static String getProfilPreviewPath() {
		String path = getConfigPath();
		
		path += "preview/";
		
		File f = new File(path);
		if(!f.exists()) {
			f.mkdirs();
		}
		
		return path;
	}

	public static String getErrorLogPath() {
		return Config.get("errorlogPath");
	}
}
