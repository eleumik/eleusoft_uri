package junit.org.eleusoft.uri;

import org.eleusoft.uri.URI;

public class Test_URI30B_MartinDuerst extends Test_URI30_Abstract_MartinDuerst
{

	protected void doPercentEncodingTestFromProperty(String prop) throws Exception
	{
		prop = "e" + prop;
		String escapedURI = props.getProperty(prop + ".parse");
		String expectedresultURI = props.getProperty(prop + ".result");

		info("Parse URI with double byte chars check that percent encoding is as passed, Parsed URI, Expected URI",
			new String[]{escapedURI, expectedresultURI});

		URI uriObj = createURI(escapedURI);
		assertEqualsX("URI is different!!!!", expectedresultURI, uriObj.toString());
	}

}