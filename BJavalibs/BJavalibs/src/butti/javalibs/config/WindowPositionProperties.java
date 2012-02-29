package butti.javalibs.config;

import java.awt.Window;
import java.util.Properties;

import javax.swing.JFrame;

public class WindowPositionProperties extends Properties {
	private static final long serialVersionUID = 1L;

	public void applay(Window window) {
		int x, y, width, height;

		x = window.getLocation().x;
		y = window.getLocation().y;
		width = window.getWidth();
		height = window.getHeight();

		x = Integer.parseInt(getProperty("x", "" + x));
		y = Integer.parseInt(getProperty("y", "" + y));
		width = Integer.parseInt(getProperty("width", "" + width));
		height = Integer.parseInt(getProperty("height", "" + height));

		window.setLocation(x, y);
		window.setSize(width, height);
		if (window instanceof JFrame) {
			if (Boolean.parseBoolean(getProperty("maximized"))) {
				((JFrame) window).setExtendedState(((JFrame) window).getExtendedState() | JFrame.MAXIMIZED_BOTH);
			}
		}
	}

	public void readWindowPos(Window window) {
		int x, y, width, height;

		x = window.getLocation().x;
		y = window.getLocation().y;
		width = window.getWidth();
		height = window.getHeight();

		setProperty("x", "" + x);
		setProperty("y", "" + y);
		setProperty("width", "" + width);
		setProperty("height", "" + height);
		if (window instanceof JFrame) {
			if ((((JFrame) window).getExtendedState() & JFrame.MAXIMIZED_BOTH) == JFrame.MAXIMIZED_BOTH) {
				setProperty("maximized", "true");
			} else {
				setProperty("maximized", "false");
			}
		}
	}

}
