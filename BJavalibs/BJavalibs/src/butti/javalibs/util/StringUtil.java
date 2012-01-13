package butti.javalibs.util;

public class StringUtil {

	public static String replace(final String aInput, final String aOldPattern,
			final String aNewPattern) {
		if (aOldPattern.equals("")) {
			throw new IllegalArgumentException("Old pattern must have content.");
		}

		final StringBuffer result = new StringBuffer();
		// startIdx and idxOld delimit various chunks of aInput; these
		// chunks always end where aOldPattern begins
		int startIdx = 0;
		int idxOld = 0;
		while ((idxOld = aInput.indexOf(aOldPattern, startIdx)) >= 0) {
			// grab a part of aInput which does not include aOldPattern
			result.append(aInput.substring(startIdx, idxOld));
			// add aNewPattern to take place of aOldPattern
			result.append(aNewPattern);

			// reset the startIdx to just after the current match, to see
			// if there are any further matches
			startIdx = idxOld + aOldPattern.length();
		}
		// the final chunk will go to the end of aInput
		result.append(aInput.substring(startIdx));
		return result.toString();
	}

	public static String removeHtml(String htmlString) {
		if (htmlString == null) {
			return null;
		}
		return htmlString.replaceAll("\\<.*?\\>", "").trim();
	}

	public static boolean equals(String s1, String s2) {
		if (s1 == s2) {
			return true;
		}

		if (s1 == null) {
			return false;
		}

		return s1.equals(s2);
	}
}
