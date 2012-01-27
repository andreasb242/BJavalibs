package butti.javalibs.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

import butti.javalibs.errorhandler.Errorhandler;


public class Settings {
	private Properties settings = new Properties();

	private final String filename;

	public Settings(String filename) {
		this.filename = filename;
		try {
			File file = new File(ConfigPath.getSettingsPath() + filename);
			if (file.exists()) {
				FileInputStream in = new FileInputStream(file);
				settings.load(in);
				in.close();
			}
		} catch (Exception e) {
			// Wenn die Settings nicht geladen werden können einfach
			// Defaultwerte benutzen (z.B. bei erstem Start)
			e.printStackTrace();
		}
	}

	public Settings() {
		this("settings.properties");
	}

	private void save() {
		try {
			FileOutputStream out = new FileOutputStream(new File(ConfigPath.getSettingsPath() + filename));
			settings.store(out, "MaskDocu Benutzereinstellungen");
			out.close();
		} catch (Exception e) {
			Errorhandler.showError(e, "Einstellungen konnten nicht gespeichert werden!");
		}
	}

	/**
	 * Gibt eine Einstellung zurück
	 * 
	 * @param name
	 *            Der name der Einstellung
	 * @return Der Wert der Einstellung
	 */
	public boolean isSetting(String name) {
		return isSetting(name, false);
	}

	/**
	 * Gibt eine Einstellung zurück
	 * 
	 * @param name
	 *            Der name der Einstellung
	 * @param defaultValue
	 *            Der Defaultwert
	 * @return Der Wert der Einstellung
	 */
	public boolean isSetting(String name, boolean defaultValue) {
		String value = settings.getProperty(name);
		if (value == null) {
			return defaultValue;
		}
		return "true".equals(value);
	}

	/**
	 * Setzt eine Einstellung
	 * 
	 * @param name
	 *            Der Name der Einstellung
	 * @param value
	 *            Der Wert der Einstellung
	 */
	public void setSetting(String name, boolean value) {
		setSetting(name, Boolean.toString(value));
	}

	/**
	 * Setzt eine Einstellung
	 * 
	 * @param name
	 *            Der Name der Einstellung
	 * @param value
	 *            Der Wert der Einstellung
	 */
	public void setSetting(String name, String value) {
		settings.setProperty(name, value);
		save();
	}

	/**
	 * Gibt eine Einstellung zurück
	 * 
	 * @param name
	 *            Der name der Einstellung
	 * @param defaultValue
	 *            Der Defaultwert
	 * @return Der Wert der Einstellung
	 */
	public String getSetting(String name, String defaultValue) {
		String value = settings.getProperty(name);
		if (value == null) {
			return defaultValue;
		}
		return value;
	}

	/**
	 * Gibt eine Einstellung zurück
	 * 
	 * @param name
	 *            Der name der Einstellung
	 * @return Der Wert der Einstellung oder NULL wenn nicht gefunden
	 */
	public String getSetting(String name) {
		return getSetting(name, null);
	}
}
