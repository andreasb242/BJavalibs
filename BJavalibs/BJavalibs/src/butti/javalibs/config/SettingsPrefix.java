package butti.javalibs.config;

/**
 * Settings adapter with saves and reads all settings with a prefix
 * 
 * @author Andreas Butti
 */
public class SettingsPrefix implements Settings {

	/**
	 * Settings to write / read
	 */
	private Settings settings;

	/**
	 * The prefix
	 */
	private String prefix;

	/**
	 * Ctor
	 * 
	 * @param settings
	 *            The Settings backend
	 * @param prefix
	 *            The prefix, without a tailing point
	 */
	public SettingsPrefix(Settings settings, String prefix) {
		this.settings = settings;
		this.prefix = prefix + ".";

		if (this.settings == null) {
			throw new NullPointerException("this.settings == null");
		}
	}

	@Override
	public boolean isSetting(String name) {
		return settings.isSetting(prefix + name);
	}

	@Override
	public boolean isSetting(String name, boolean defaultValue) {
		return settings.isSetting(prefix + name, defaultValue);
	}

	@Override
	public void setSetting(String name, boolean value) {
		settings.setSetting(prefix + name, value);
	}

	@Override
	public void setSetting(String name, double value) {
		settings.setSetting(prefix + name, value);
	}

	@Override
	public void setSetting(String name, String value) {
		settings.setSetting(prefix + name, value);
	}

	@Override
	public String getSetting(String name, String defaultValue) {
		return settings.getSetting(prefix + name, defaultValue);
	}

	@Override
	public double getSetting(String name, double defaultValue) {
		return settings.getSetting(prefix + name, defaultValue);
	}

	@Override
	public String getSetting(String name) {
		return settings.getSetting(prefix + name);
	}

	@Override
	public String[] getKeysStartingWith(String prefix) {
		String[] keys = settings.getKeysStartingWith(this.prefix + prefix);

		int len = this.prefix.length();

		for (int i = 0; i < len; i++) {
			keys[i] = keys[i].substring(len);
		}

		return keys;
	}

	@Override
	public void removeSetting(String key) {
		settings.removeSetting(prefix + key);
	}

}
