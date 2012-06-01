package butti.javalibs.config;

public interface Settings {

	/**
	 * Gibt eine Einstellung zurück
	 * 
	 * @param name
	 *            Der name der Einstellung
	 * @return Der Wert der Einstellung
	 */
	public abstract boolean isSetting(String name);

	/**
	 * Gibt eine Einstellung zurück
	 * 
	 * @param name
	 *            Der name der Einstellung
	 * @param defaultValue
	 *            Der Defaultwert
	 * @return Der Wert der Einstellung
	 */
	public abstract boolean isSetting(String name, boolean defaultValue);

	/**
	 * Setzt eine Einstellung
	 * 
	 * @param name
	 *            Der Name der Einstellung
	 * @param value
	 *            Der Wert der Einstellung
	 */
	public abstract void setSetting(String name, boolean value);

	/**
	 * Setzt eine Einstellung
	 * 
	 * @param name
	 *            Der Name der Einstellung
	 * @param value
	 *            Der Wert der Einstellung
	 */
	public abstract void setSetting(String name, double value);

	/**
	 * Setzt eine Einstellung
	 * 
	 * @param name
	 *            Der Name der Einstellung
	 * @param value
	 *            Der Wert der Einstellung
	 */
	public abstract void setSetting(String name, String value);

	/**
	 * Gibt eine Einstellung zurück
	 * 
	 * @param name
	 *            Der name der Einstellung
	 * @param defaultValue
	 *            Der Defaultwert
	 * @return Der Wert der Einstellung
	 */
	public abstract String getSetting(String name, String defaultValue);

	/**
	 * Gibt eine Einstellung zurück
	 * 
	 * @param name
	 *            Der name der Einstellung
	 * @param defaultValue
	 *            Der Defaultwert
	 * @return Der Wert der Einstellung
	 */
	public abstract double getSetting(String name, double defaultValue);

	/**
	 * Gibt eine Einstellung zurück
	 * 
	 * @param name
	 *            Der name der Einstellung
	 * @return Der Wert der Einstellung oder NULL wenn nicht gefunden
	 */
	public abstract String getSetting(String name);

	/**
	 * Gets all property keys starting with this prefix
	 */
	public abstract String[] getKeysStartingWith(String prefix);

	/**
	 * Delte a settings entry
	 * 
	 * @param key
	 *            The key to be remove
	 */
	public abstract void removeSetting(String key);

}