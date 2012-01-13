package butti.javalibs.config;

import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

import javax.swing.JFrame;

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
		return new File(ConfigPath.getConfigPath() + name + ".windowPos");
	}

	public void load() {
		try {
			File f = getFile();

			if (!f.exists()) {
				return;
			}

			Properties p = new Properties();
			p.load(new FileInputStream(f));

			int x, y, width, height;

			x = window.getLocation().x;
			y = window.getLocation().y;
			width = window.getWidth();
			height = window.getHeight();

			x = Integer.parseInt(p.getProperty("x", "" + x));
			y = Integer.parseInt(p.getProperty("y", "" + y));
			width = Integer.parseInt(p.getProperty("width", "" + width));
			height = Integer.parseInt(p.getProperty("height", "" + height));

			window.setLocation(x, y);
			window.setSize(width, height);
			if (window instanceof JFrame) {
				if (Boolean.parseBoolean(p.getProperty("maximized"))) {
					((JFrame) window).setExtendedState(((JFrame) window)
							.getExtendedState()
							| JFrame.MAXIMIZED_BOTH);
				}
			}

			if (add != null) {
				add.load(p);
			}
		} catch (Exception e) {
			Errorhandler.showError(e, "Fensterposition für das Fenster \""
					+ window.getName() + "\" vom Typ \"" + name
					+ "\" konnte nicht geladen werden!");
		}
	}

	public void save() {
		try {
			File f = getFile();

			if (!f.exists()) {
				f.createNewFile();
			}

			Properties p = new Properties();

			int x, y, width, height;

			x = window.getLocation().x;
			y = window.getLocation().y;
			width = window.getWidth();
			height = window.getHeight();

			p.setProperty("x", "" + x);
			p.setProperty("y", "" + y);
			p.setProperty("width", "" + width);
			p.setProperty("height", "" + height);
			if (window instanceof JFrame) {
				if ((((JFrame) window).getExtendedState() & JFrame.MAXIMIZED_BOTH) == JFrame.MAXIMIZED_BOTH) {
					p.setProperty("maximized", "true");
				} else {
					p.setProperty("maximized", "false");
				}
			}

			if (add != null) {
				add.save(p);
			}

			p.store(new FileOutputStream(f),
					"Fensterposition des Fenstertypes \"" + name + "\"");
		} catch (Exception e) {
			Errorhandler.showError(e, "Fensterposition für das Fenster \""
					+ window.getName() + "\" vom Typ \"" + name
					+ "\" konnte nicht gespeichert werden!");
		}
	}

	public static abstract class AdditionalWindowPositions {
		public void load(Properties p) {
		}

		public void save(Properties p) {
		}
	}
}
