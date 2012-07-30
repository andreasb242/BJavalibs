package butti.javalibs.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;

import butti.javalibs.errorhandler.Errorhandler;
import butti.javalibs.gui.messagebox.Messagebox;

/**
 * Öffnet den System File Browser
 * 
 * @author Andreas Butti
 * 
 */
public class OpenFileBrowser {
	/**
	 * Führt ein Programm aus
	 * 
	 * @param exec
	 *            Der String der ausgeführt wird
	 */
	public static boolean exec(String exec) {
		System.out.println("B-OpenFileBrowser: Exce: " + exec);
		try {
			String line;
			Process p = Runtime.getRuntime().exec(exec);
			BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
			while ((line = input.readLine()) != null) {
				System.out.println(line);
			}
			input.close();
			return true;
		} catch (Exception err) {
			err.printStackTrace();
			return false;
		}
	}

	/**
	 * Öffnet den Pfad im System Browser
	 * 
	 * @param path
	 *            Der Pfad der geöffnet werden soll
	 */
	public static void openPath(String path) {
		File file = new File(path);

		path = file.getAbsolutePath();

		System.out.println("Open Path: " + path);

		try {
			switch (OS.getOs()) {
			case WINDOWS:
				path = StringUtil.replace(path, "/", "\\");
				path = path.substring(0, 2) + StringUtil.replace(path.substring(2), "\\\\", "\\");

				if (exec("explorer.exe /n,/e,\"" + path + "\"")) {
					return;
				}
				break;
			case LINUX:
				String pathLinux = path.replaceAll(" ", "\\ ");
				if (exec("nautilus file://" + pathLinux)) {
					return;
				}
				if (exec("konqueror file://" + pathLinux)) {
					return;
				}
				break;
			case MAC:
				if (exec("open \"" + path + "\"")) {
					return;
				}
				break;

			default:
				break;
			}

			Messagebox.showWarning(null, "Ordner öffnen", "Der Pfad \"" + path + "\" konnte nicht geöffnet werden.");
		} catch (Exception e) {
			Errorhandler.showError(e, "Der Pfad \"" + path + "\" konnte nicht im Filebrowser geöffnet werden!");
		}

	}

	/**
	 * Runns an Process
	 * 
	 * @param commands
	 *            The commands to run
	 */
	public static void runProcessAsync(String[] commands) {
		switch (OS.getOs()) {
		case WINDOWS: {
			StringBuffer cmd = new StringBuffer("cmd /c \"" + commands[0]);
			boolean first = true;
			for (String p : commands) {
				if (first) {
					first = false;
					continue;
				}
				cmd.append(" ");
				cmd.append(p);
			}
			cmd.append("\"");
			exec(cmd.toString());
			return;
		}
		case LINUX: {
			Vector<String> cmd = new Vector<String>();
			cmd.add("nohup");
			for (String c : commands) {
				cmd.add(c);
			}
			try {
				Runtime.getRuntime().exec(cmd.toArray(new String[] {}));
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}
		case MAC:
			Vector<String> cmd = new Vector<String>();
			cmd.add("open");
			for (String c : commands) {
				cmd.add(c);
			}
			try {
				Runtime.getRuntime().exec(cmd.toArray(new String[] {}));
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;

		default:
			break;
		}
	}
}
