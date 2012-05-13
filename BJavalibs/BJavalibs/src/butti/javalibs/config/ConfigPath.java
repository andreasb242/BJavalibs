package butti.javalibs.config;

import java.io.File;

import butti.javalibs.gui.messagebox.Messagebox;
import butti.javalibs.util.OS;

public class ConfigPath {
	private static String settingsPath = null;
	private static String errorLogPath = null;

	// utility class
	private ConfigPath() {
	}

	public static String getWriteablePathPrefix() {
		if (Boolean.valueOf(Config.get("portable")) == true) {
			return ""; // relative Path
		}

		String home = System.getProperty("user.home");

		if (OS.getOs() == OS.LINUX) {
			return home + "/.config/" + Config.getApplicationName();
		}
		if (OS.getOs() == OS.WINDOWS) {
			return System.getenv("APPDATA") + File.separator + Config.getApplicationName();
		}
		if (OS.getOs() == OS.MAC) {
			return home + "/Library/" + Config.getApplicationName();
		}

		return home;
	}

	public static String getSettingsPath() {
		if (settingsPath != null) {
			return settingsPath;
		}

		String path = Config.get("settingsPath");

		if (path == null) {
			// Nothing working because settings cannot be read, so show a
			// Messagebox and exit..
			Messagebox.showError(null, Config.getApplicationName(), "settingsPath in «" + Config.getConfigFile()
					+ "» ist nicht gesetzt!\nApplikation kann nicht gestartet werden!");
			System.exit(1);
		}

		try {
			File f = new File(getWriteablePathPrefix() + File.separator + path);
			if (!f.exists()) {
				f.mkdirs();
			}

			settingsPath = f.getCanonicalPath() + File.separator;
		} catch (Exception e) {
			Messagebox.showError(null, Config.getApplicationName(), "Konnte nicht auf «" + getWriteablePathPrefix() + File.separator + path
					+ "» zugreiffen\nApplikation wird beendet.");
			e.printStackTrace();

			System.exit(1);
		}

		return settingsPath;
	}

	public static String getErrorLogPath() {
		if (errorLogPath != null) {
			return errorLogPath;
		}
		try {
			File f = new File(getWriteablePathPrefix() + File.separator + Config.get("errorlogPath"));
			errorLogPath = f.getCanonicalPath() + File.separator;
		} catch (Exception e) {
			Messagebox.showError(null, Config.getApplicationName(), "Konnte nicht auf «" + getWriteablePathPrefix() + File.separator + Config.get("errorlogPath")
					+ "» zugreiffen\nApplikation wird beendet.");
			e.printStackTrace();
			System.exit(1);
		}

		return errorLogPath;
	}
}
