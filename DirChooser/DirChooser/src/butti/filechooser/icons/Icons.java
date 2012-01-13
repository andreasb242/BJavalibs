package butti.filechooser.icons;

import java.net.URL;
import java.util.HashMap;

import javax.swing.ImageIcon;

/**
 * TreeIcon renderer
 * 
 * @author Andreas Butti
 * 
 */
public class Icons {
	/**
	 * All loaded images
	 */
	private static HashMap<String, ImageIcon> icons = new HashMap<String, ImageIcon>();

	/**
	 * Get an icon
	 * 
	 * @param file
	 *            The icon to load
	 * @return The loaded icon
	 */
	public static ImageIcon get(String file) {
		String path = getIconfolder() + "/" + file;

		ImageIcon icon = icons.get(path);
		if (icon == null) {
			URL imgURL = Icons.class.getResource(path);
			if (imgURL != null) {
				icon = new ImageIcon(imgURL);
				icons.put(path, icon);
			} else {
				throw (new RuntimeException("Could not load: " + path));
			}
		}
		return icon;
	}

	/**
	 * Returns the folder from with the icons should be loaded
	 * 
	 * @return the relative path
	 */
	private static String getIconfolder() {
		return "crystal";
	}
}
