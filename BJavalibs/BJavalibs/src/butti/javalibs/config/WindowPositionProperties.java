package butti.javalibs.config;

import java.awt.Window;
import java.util.Properties;

import javax.swing.JFrame;

public class WindowPositionProperties extends Properties {
	private static final long serialVersionUID = 1L;

	public boolean apply(Window window) {
		try {
			int x = Integer.parseInt(getProperty("x"));
			int y = Integer.parseInt(getProperty("y"));
			int width = Integer.parseInt(getProperty("width"));
			int height = Integer.parseInt(getProperty("height"));

			System.out.println("width="+ width);
			
			window.setLocation(x, y);
			window.setSize(width, height);
			if (window instanceof JFrame) {
				if (Boolean.parseBoolean(getProperty("maximized"))) {
					((JFrame) window).setExtendedState(((JFrame) window).getExtendedState() | JFrame.MAXIMIZED_BOTH);
				}
			}
		} catch (Exception e) {
			return false;
		}
		return true;
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
