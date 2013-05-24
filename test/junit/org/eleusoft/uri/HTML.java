package junit.org.eleusoft.uri;


/**
 * HTML-escape helper
 */
public abstract class HTML
{

	// HTML ESCAPE - Copied from org.eleusoft.htmlprint HTMLEncodeUtil.java

	private static final char[] htmlControlChars = new char[]{'&', '\'', '\"', '<', '>'};
	private static final String[] htmlControlCharsEscaped = new String[]{"&amp;", "&#39;", "&quot;", "&lt;", "&gt;"};
	public static String htmlEscape(String s1)
	{
		return charEscape(s1, htmlControlChars, htmlControlCharsEscaped);
	}
	private static String charEscape(String s1, char[] toBeEscaped, String[] escapedEquivalents)
	{
		if (s1==null) throw new IllegalArgumentException("null string to escape passed");
		return charEscape(s1.toCharArray(), 0, s1.length(), toBeEscaped, escapedEquivalents);
	}

	private static String charEscape(char[] s1, int off, int len, char[] toBeEscaped, String[] escapedEquivalents)
	{
		final int toBeEscapedLen	= toBeEscaped.length;

		StringBuffer buf	= new StringBuffer((int)(len*1.1));

		boolean found;
		char ch;

		for (int i=off; i<len; ++i) {
			ch = s1[i];
			found = false;
			for (int index=0;index<toBeEscapedLen && !found;++index)
			{
				if (ch==toBeEscaped[index])
				{
					buf.append(escapedEquivalents[index]);
					found = true;
				}
			}
			if (!found) buf.append(ch);
		}
		//System.out.println("buf.toString()"+buf.toString()+"\nwas:" + s1);
		return buf.toString();
	}


}
