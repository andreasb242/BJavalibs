package butti.javalibs.util;

import java.io.File;

import butti.javalibs.errorhandler.Errorhandler;
import butti.javalibs.gui.messagebox.Messagebox;

public class RestartUtil {
	public static boolean restartApplication(Object classInJarFile) {
		String javaBin = System.getProperty("java.home") + "/bin/java";
		File jarFile;
		try {
			jarFile = new File(classInJarFile.getClass().getProtectionDomain().getCodeSource().getLocation().toURI());
		} catch (Exception e) {
			Errorhandler.logError(e);
			
			Messagebox.showWarning(null, "Restart", "Die Applikation konnte nicht neu gestartet werden, bitte starten Sie die Applikation manuell neu");
			
			return false;
		}

		// is it a jar file?
		if (!jarFile.getName().endsWith(".jar")) {
			return false; // no, it's a .class probably
		}

		String toExec[] = new String[] { javaBin, "-jar", jarFile.getPath() };
		try {
			Runtime.getRuntime().exec(toExec);
		} catch (Exception e) {
			Errorhandler.logError(e);
			return false;
		}

		System.exit(0);

		return true;
	}
}
