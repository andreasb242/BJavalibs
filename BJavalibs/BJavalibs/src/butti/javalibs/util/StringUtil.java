package butti.javalibs.util;

public class StringUtil {

	public static String replace(final String aInput, final String aOldPattern, final String aNewPattern) {
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

	/**
	 * Replaces characters that may be confused by a HTML parser with their
	 * equivalent character entity references.
	 * 
	 * Any data that will appear as text on a web page should be be escaped.
	 * This is especially important for data that comes from untrusted sources
	 * such as Internet users. A common mistake in CGI programming is to ask a
	 * user for data and then put that data on a web page. For example:
	 * 
	 * <pre>
	 * Server: What is your name?
	 * User: &lt;b&gt;Joe&lt;b&gt;
	 * Server: Hello <b>Joe</b>, Welcome
	 * </pre>
	 * 
	 * If the name is put on the page without checking that it doesn't contain
	 * HTML code or without sanitizing that HTML code, the user could reformat
	 * the page, insert scripts, and control the the content on your web server.
	 * 
	 * This method will replace HTML characters such as &gt; with their HTML
	 * entity reference (&amp;gt;) so that the html parser will be sure to
	 * interpret them as plain text rather than HTML or script.
	 * 
	 * This method should be used for both data to be displayed in text in the
	 * html document, and data put in form elements. For example:<br>
	 * <code>&lt;html&gt;&lt;body&gt;<i>This in not a &amp;lt;tag&amp;gt;
	 * in HTML</i>&lt;/body&gt;&lt;/html&gt;</code><br>
	 * and<br>
	 * <code>&lt;form&gt;&lt;input type="hidden" name="date" value="<i>This data could
	 * be &amp;quot;malicious&amp;quot;</i>"&gt;&lt;/form&gt;</code><br>
	 * In the second example, the form data would be properly be resubmitted to
	 * your cgi script in the URLEncoded format:<br>
	 * <code><i>This data could be %22malicious%22</i></code>
	 * 
	 * @param s
	 *            String to be escaped
	 * @return escaped String
	 * @throws NullPointerException
	 *             if s is null.
	 * 
	 * @since ostermillerutils 1.00.00
	 */
	public static String escapeHTML(String s) {
		if(s == null) {
			return null;
		}
		
		int length = s.length();
		int newLength = length;
		boolean someCharacterEscaped = false;
		// first check for characters that might
		// be dangerous and calculate a length
		// of the string that has escapes.
		for (int i = 0; i < length; i++) {
			char c = s.charAt(i);
			int cint = 0xffff & c;
			if (cint < 32) {
				switch (c) {
				case '\r':
				case '\n':
				case '\t':
				case '\f': {
				}
					break;
				default: {
					newLength -= 1;
					someCharacterEscaped = true;
				}
				}
			} else {
				switch (c) {
				case '\"': {
					newLength += 5;
					someCharacterEscaped = true;
				}
					break;
				case '&':
				case '\'': {
					newLength += 4;
					someCharacterEscaped = true;
				}
					break;
				case '<':
				case '>': {
					newLength += 3;
					someCharacterEscaped = true;
				}
					break;
				}
			}
		}
		if (!someCharacterEscaped) {
			// nothing to escape in the string
			return s;
		}
		StringBuffer sb = new StringBuffer(newLength);
		for (int i = 0; i < length; i++) {
			char c = s.charAt(i);
			int cint = 0xffff & c;
			if (cint < 32) {
				switch (c) {
				case '\r':
				case '\n':
				case '\t':
				case '\f': {
					sb.append(c);
				}
					break;
				default: {
					// Remove this character
				}
				}
			} else {
				switch (c) {
				case '\"': {
					sb.append("&quot;");
				}
					break;
				case '\'': {
					sb.append("&#39;");
				}
					break;
				case '&': {
					sb.append("&amp;");
				}
					break;
				case '<': {
					sb.append("&lt;");
				}
					break;
				case '>': {
					sb.append("&gt;");
				}
					break;
				default: {
					sb.append(c);
				}
				}
			}
		}
		return sb.toString();
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
