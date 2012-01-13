package butti.javalibs.errorhandler;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.SwingUtilities;

import butti.javalibs.config.ConfigPath;
import butti.javalibs.gui.messagebox.Messagebox;
import butti.javalibs.util.OpenFileBrowser;



/**
 * Zeigt dem User einen unerwarteten Error an.
 * 
 * @author Andreas Butti
 */
public class Errorhandler {
	/**
	 * Unser logfile
	 */
	private static OutputStreamWriter logFile;

	/**
	 * Name des Logfiles
	 */
	private static String logfilename;

	/**
	 * Logfile öffnen
	 */
	static {
		Date dt = new Date();
		// Festlegung des Formats:
		SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");

		File folder = new File(ConfigPath.getErrorLogPath());
		if (folder.exists()) {
			System.out.println("Folder \"" + ConfigPath.getErrorLogPath() + "\" exists");
			if(!folder.isDirectory()) {
				System.out.println("Logfolder is not a folder!");
			}
		} else {
			System.out.println("Folder \"" + ConfigPath.getErrorLogPath() + "\" don't exist");
			if (!folder.mkdirs()) {
				Messagebox.showError(null, "Logfile", "Der Ordner \"" + ConfigPath.getErrorLogPath() + "\" konnte nicht erstellt werden!");
			}
		}

		logfilename = "logs" + df.format(dt) + ".log";

		File outputFile = new File(ConfigPath.getErrorLogPath() + logfilename);
		try {
			if(!outputFile.exists()) {
				System.out.println("create logfile: " + outputFile);
				if(!outputFile.createNewFile()) {
					System.out.println("could not create logfile: " + outputFile);
				}
			}
			logFile = new OutputStreamWriter(new FileOutputStream(outputFile, true), "UTF-8");
		} catch (IOException e) {
			Messagebox.showError(null, "Logfile", "Das Logfile konnte nicht geöffnet werden. Prüfen Sie die Berechtigugen auf den Ordner \"" + outputFile
					+ "\"");
			e.printStackTrace();
		}
	}

	private Errorhandler() {
	}

	/**
	 * Loggt den Text
	 * 
	 * @param log
	 *            Der Text der geloggt werden soll
	 */
	public static synchronized void log(String log) {
		try {
			if (logFile != null) {
				logFile.write(log);
				logFile.flush();
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * Zeigt eine Error an und logt diesen in das Logfile
	 * 
	 * @param e
	 *            Die Exception
	 * @param msg
	 *            Die Meldung die angezeigt wird
	 */
	public static void showError(final Throwable e, final String msg) {
		logError(e, msg);
		if (msg != null) {
			System.err.println("Fehler: " + msg);
		}
		e.printStackTrace();

		SwingUtilities.invokeLater(new Runnable() {

			public void run() {
				String message;

				if (msg == null) {
					message = "<html><b>Es ist ein Fehler aufgetreten,</b><br>" + "für den keine Beschreibung vorliegt.<br>";
				} else {
					message = "<html>Es ist ein Fehler aufgetreten:<br><b>" + msg + "</b><br>";
				}

				if (e.getMessage() != null) {
					String msg = e.getMessage();
					if (msg.length() > 100) {
						msg = msg.substring(0, 100) + "...";
					}

					message += "<i>" + msg + "</i>";
				}

				message += "Der Fehler wurde geloggt. Bitte senden Sie das Logfile an den Entwickler.<br>" + "Logfile: " + logfilename + "</html>";

				Messagebox msg = new Messagebox(null, "Fehler", message, Messagebox.ERROR);
				msg.addButton("Schliessen", 0);

				msg.addButton("Logfile öffnen", 1, true);

				if (msg.display() == 1) {
					OpenFileBrowser.openPath(ConfigPath.getErrorLogPath());
				}
			}

		});

		e.printStackTrace();
	}

	/**
	 * Zeigt eine Error an und logt diesen in das Logfile
	 * 
	 * @param e
	 *            Die Exception
	 */
	public static void showError(Throwable e) {
		showError(e, null);
	}

	/**
	 * Logt eine Error in das Logfile
	 * 
	 * @param e
	 *            Die Exception
	 */
	public static void logError(Throwable e, String message) {
		StringBuilder error = new StringBuilder();

		if (message != null) {
			error.append("Msg: " + message);
		}

		error.append(e.getMessage());
		error.append("\n----\n");
		error.append(e.getClass());
		error.append("\n\n");
		for (Object o : e.getStackTrace()) {
			error.append(o.toString());
			error.append("\n");
		}
		error.append("\n");

		// Für die Entwicklung auch alles auf Konsole ausgeben
		e.printStackTrace();

		logError(error.toString());
	}

	/**
	 * Logt eine Error in das Logfile
	 * 
	 * @param message
	 *            Der Fehler
	 */
	public static void logError(String message) {
		Date dt = new Date();
		// Festlegung des Formats:
		SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss.S");
		log("->" + df.format(dt) + "\n");
		log(message);
	}

	public static void registerAwtErrorHandler() {
		System.setProperty("sun.awt.exception.handler", AwtHandler.class.getName());
	}

	public static void main(String[] args) {
		Errorhandler.showError(new Exception());
	}
}