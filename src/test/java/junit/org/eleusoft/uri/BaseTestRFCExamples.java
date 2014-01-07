package junit.org.eleusoft.uri;

import org.eleusoft.uri.URI;
import org.eleusoft.uri.URIException;

public class BaseTestRFCExamples extends BaseTestURI
{

	public BaseTestRFCExamples() {
		super();

	}

	protected String getBaseURI()
	{
		return "http://a/b/c/d;p?q";
	}




	//////////////////////////////////////////////////////////////

	protected void doRFCExampleTest(String rel, String expected) throws URIException
	{
		info("RFC Examples test: from base [http://a/b/c/d;p?q] resolve passed uri, REL REF, EXPECTED",
			new String[]{rel, expected});

		URI base = createURI(getBaseURI());
		URI relObj = createURI(rel);
		URI test = base.resolve(relObj);

		String testString = test.toString();

		assertEquals("RFC Example test from base [http://a/b/c/d;p?q] and rel uri [" + rel + "] expected [" + expected +
			"] but result is [" + testString + "] ", expected, testString);


	}




}