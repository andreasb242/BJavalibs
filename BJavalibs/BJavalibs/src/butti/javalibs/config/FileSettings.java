package butti.javalibs.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;
import java.util.Vector;

import butti.javalibs.errorhandler.Errorhandler;

/**
 * Settings class
 * 
 * @author Andreas Butti
 */
public class FileSettings implements Settings {

	/**
	 * The Properties backend to store / load the settings
	 */
	private Properties settings = new Properties();

	/**
	 * The filename for the settings
	 */
	private final String filename;

	/**
	 * Transaction running, do not save
	 * 
	 * This is because of performance reasons
	 */
	private boolean transaction = false;

	/**
	 * Ctor
	 * 
	 * @param filename
	 *            The filename to load / save the settings
	 */
	public FileSettings(String filename) {
		this.filename = filename;
		try {
			File file = new File(getSettingsFileName());
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

	/**
	 * Create Settings with default Filename "settings.properties"
	 */
	public FileSettings() {
		this("settings.properties");
	}

	/**
	 * @return The Settings name folder
	 */
	public String getSettingsFileName() {
		return ConfigPath.getSettingsPath() + filename;
	}

	/**
	 * Save the settings to a file
	 */
	private void save() {
		if (transaction) {
			return;
		}

		try {
			FileOutputStream out = new FileOutputStream(getSettingsFileName());
			settings.store(out, "Settings for " + Config.getApplicationName());
			out.close();
		} catch (Exception e) {
			Errorhandler.showError(e, "Einstellungen konnten nicht gespeichert werden!");
		}
	}

	@Override
	public boolean isSetting(String name) {
		return isSetting(name, false);
	}

	@Override
	public boolean isSetting(String name, boolean defaultValue) {
		String value = settings.getProperty(name);
		if (value == null) {
			return defaultValue;
		}
		return "true".equals(value);
	}

	@Override
	public void setSetting(String name, boolean value) {
		setSetting(name, Boolean.toString(value));
	}

	@Override
	public void setSetting(String name, double value) {
		setSetting(name, String.valueOf(value));
	}

	@Override
	public void setSetting(String name, String value) {
		settings.setProperty(name, value);
		save();
	}

	@Override
	public String getSetting(String name, String defaultValue) {
		String value = settings.getProperty(name);
		if (value == null) {
			return defaultValue;
		}
		return value;
	}

	@Override
	public double getSetting(String name, double defaultValue) {
		try {
			return Double.parseDouble(settings.getProperty(name));
		} catch (Exception e) {
		}
		return defaultValue;
	}

	@Override
	public String getSetting(String name) {
		return getSetting(name, null);
	}

	@Override
	public String[] getKeysStartingWith(String prefix) {
		Vector<String> keys = new Vector<String>();

		for (Object k : settings.keySet()) {
			String s = k.toString();
			if (s.startsWith(prefix)) {
				keys.add(s);
			}
		}
		return keys.toArray(new String[] {});
	}

	@Override
	public void removeSetting(String key) {
		settings.remove(key);
		save();
	}

	@Override
	public void startTransaction() {
		transaction = true;
	}

	@Override
	public void finishTransaction() {
		transaction = false;
		save();
	}
}
