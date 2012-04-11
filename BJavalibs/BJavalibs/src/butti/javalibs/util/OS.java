package butti.javalibs.util;

/**
 * The OS
 */
public enum OS {
	WINDOWS, LINUX, MAC, OTHER;

	/**
	 * 
	 * @return The running OS
	 */
	public static OS getOs() {
		if (System.getProperty("os.name").toLowerCase().indexOf("windows") > -1) {
			return OS.WINDOWS;
		} else if (System.getProperty("os.name").toLowerCase().indexOf("linux") > -1) {
			return OS.LINUX;
		} else if (System.getProperty("os.name").toLowerCase().indexOf("mac") > -1) {
			return OS.MAC;
		}

		return OS.OTHER;
	}
}
