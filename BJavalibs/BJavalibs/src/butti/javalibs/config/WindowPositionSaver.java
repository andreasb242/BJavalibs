package butti.javalibs.config;

import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

import butti.javalibs.errorhandler.Errorhandler;

public class WindowPositionSaver {
	private String name;
	private Window window;
	private AdditionalWindowPositions add;

	public WindowPositionSaver(Window window) {
		this(window, null);
	}

	public WindowPositionSaver(Window window, AdditionalWindowPositions add) {
		this.window = window;
		this.name = window.getClass().getName();
		this.add = add;

		load();

		window.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				save();
			}

		});
	}

	private File getFile() {
		return new File(ConfigPath.getSettingsPath() + name + ".windowPos");
	}

	public void load() {
		try {
			File f = getFile();

			if (!f.exists()) {
				return;
			}

			WindowPositionProperties p = new WindowPositionProperties();
			p.load(new FileInputStream(f));

			p.applay(window);

			if (add != null) {
				add.load(p);
			}
		} catch (Exception e) {
			Errorhandler.showError(e, "Fensterposition für das Fenster \"" + window.getName() + "\" vom Typ \"" + name + "\" konnte nicht geladen werden!");
		}
	}

	public void save() {
		try {
			File f = getFile();

			if (!f.exists()) {
				f.createNewFile();
			}

			WindowPositionProperties p = new WindowPositionProperties();

			p.readWindowPos(window);

			if (add != null) {
				add.save(p);
			}

			p.store(new FileOutputStream(f), "Fensterposition des Fenstertypes \"" + name + "\"");
		} catch (Exception e) {
			Errorhandler.showError(e, "Fensterposition für das Fenster \"" + window.getName() + "\" vom Typ \"" + name + "\" konnte nicht gespeichert werden!");
		}
	}

	public static abstract class AdditionalWindowPositions {
		public void load(Properties p) {
		}

		public void save(Properties p) {
		}
	}
}
