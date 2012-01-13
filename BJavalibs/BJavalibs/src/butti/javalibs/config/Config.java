package butti.javalibs.config;


import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import butti.javalibs.gui.messagebox.Messagebox;

public class Config {
	private static Properties config = new Properties();

	static {
		try {
			FileInputStream in = new FileInputStream(new File("config/config.properties"));
			config.load(in);
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			Messagebox.showError(null, "Konfiguration", "\"config/config.properties\" konnte nicht gelesen werden. Programm wird beendet.");
			System.exit(1);
		}
	}

	public static String get(String key) {
		return config.getProperty(key);
	}

	public static String[] getArray(String key) {
		String s = get(key);
		if (s == null) {
			return new String[] {};
		}
		return s.split(";");
	}
}
