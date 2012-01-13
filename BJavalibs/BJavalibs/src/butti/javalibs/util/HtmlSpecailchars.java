package butti.javalibs.util;

import java.util.HashMap;

public class HtmlSpecailchars {
	private static HashMap<String, String> specialchars = new HashMap<String, String>();

	static {
		specialchars.put("&#228;", "ä");
		specialchars.put("&#246;", "ö");
		specialchars.put("&#252;", "ü");
		specialchars.put("&#196;", "Ä");
		specialchars.put("&#214;", "Ö");
		specialchars.put("&#220;", "Ü");
	}

	private HtmlSpecailchars() {
	}

	/**
	 * Replates the HTML Specialchars
	 * 
	 * @param text
	 *            The HTML String
	 * @return The HTML String without specialchars
	 */
	public static String unescape(String text) {
		for (Object spec : specialchars.keySet()) {
			text = text.replaceAll(spec.toString(), specialchars.get(spec)
					.toString());
		}

		return text;
	}
}
