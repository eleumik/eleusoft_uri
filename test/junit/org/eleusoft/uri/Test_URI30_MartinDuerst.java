package junit.org.eleusoft.uri;

import org.eleusoft.uri.URI;

public class Test_URI30_MartinDuerst extends Test_URI30_Abstract_MartinDuerst
{

	protected void doPercentEncodingTestFromProperty(String prop) throws Exception
	{
		prop = "e" + prop;
		String escapedURI = props.getProperty(prop + ".source");
		String unescAuth = props.getProperty(prop + ".auth");
		String unescPath= props.getProperty(prop + ".path");
		info("Check percent encoding, Escaped URI, Expected unescaped auth, Expected unescaped path",
			new String[]{escapedURI, unescAuth, unescPath});

		URI uriObj = createURI(escapedURI);
		assertEquals("path is different", unescPath, uriObj.getPDPath());
		assertEquals("authority is different", unescAuth, uriObj.getAuthority());
	}

}