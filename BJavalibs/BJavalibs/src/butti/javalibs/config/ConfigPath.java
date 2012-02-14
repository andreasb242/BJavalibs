package butti.javalibs.config;

import java.io.File;

import javax.swing.JOptionPane;

public class ConfigPath {
	public static String getSettingsPath() {
		String path = Config.get("settingsPath");

		if (path == null) {
			// Nothing working because settings cannot be read, so show a
			// JOptionPane and exit..
			JOptionPane.showMessageDialog(null, "settingsPath in config/config.properties ist nicht gesetzt!\nApplikation kann nicht gestartet werden!", "Simulation", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}

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
