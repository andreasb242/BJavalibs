package butti.javalibs.debugging;

import java.awt.Color;
import java.util.*;
import java.util.Map.Entry;
import javax.swing.UIManager;

/**
 * Generate a list of UIManager color keys.
 */
public class UIManagerColorKeys {
	public static void printColorKeys() {
		List<String> colorKeys = new ArrayList<String>();
		Set<Entry<Object, Object>> entries = UIManager.getLookAndFeelDefaults().entrySet();
		for (Entry<Object, Object> entry : entries) {
			if (entry.getValue() instanceof Color) {
				colorKeys.add((String) entry.getKey());
			}
		}

		// sort the color keys
		Collections.sort(colorKeys);

		// print the color keys
		for (String colorKey : colorKeys) {
			System.out.println(colorKey);
		}

	}
}
