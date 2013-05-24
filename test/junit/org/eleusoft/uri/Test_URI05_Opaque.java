package junit.org.eleusoft.uri;

import org.eleusoft.uri.URI;
import org.eleusoft.uri.URIException;

public class Test_URI05_Opaque extends BaseTestRFCExamples
{

	public Test_URI05_Opaque() {
		super();

	}

	public void testToStringNews() throws Exception
	{
		createAndCheckToStringEquals("news:comp.lang.java");
	}
	public void testToStringISBN() throws Exception
	{
		createAndCheckToStringEquals("urn:isbn:096139210x");
	}

	public void testIsNotOpaque_Http() throws Exception
	{
		testSchemeSpecificPart("http://www.example.org/ciao", null);


	}
	public void testIsOpaque_NewsGroup() throws Exception
	{
		testSchemeSpecificPart("news:comp.lang.java", "comp.lang.java");


	}
	public void testIsOpaque_ISBN() throws Exception
	{
		testSchemeSpecificPart("urn:isbn:096139210x", "isbn:096139210x");

	}

	public void testIsOpaque_MailTO() throws Exception
	{
		testSchemeSpecificPart("mailto:pollo@pollo.org", "pollo@pollo.org");

	}
	public void testGetSchemeSpecificPart_NewsGroup() throws Exception
	{
		testSchemeSpecificPart("news:comp.lang.java", "comp.lang.java");

	}
	public void testGetSchemeSpecificPart_ISBN() throws Exception
	{
		testSchemeSpecificPart("urn:isbn:096139210x", "isbn:096139210x");

	}
	public void testGetSchemeSpecificPart_MailTo() throws Exception
	{
		testSchemeSpecificPart("mailto:pippo@topo.com", "pippo@topo.com");

	}

	public void testGetSchemeSpecificPart_MailToWithQuery() throws Exception
	{
		testSchemeSpecificPart("mailto:pippo@topo.com?subject=pippo", "pippo@topo.com?subject=pippo");

	}

	public void testGetSchemeSpecificPart_MailToWithFragment() throws Exception
	{
		testSchemeSpecificPart("mailto:pippo@topo.com#f", "pippo@topo.com");

	}



	public void testOpaqueNormalizeNews() throws Exception
	{
		createAndCheckToStringEqualsAfterNormalize("news:comp.lang.java", "news:comp.lang.java");
	}
	public void testOpaqueNormalizeISBN() throws Exception
	{
		createAndCheckToStringEqualsAfterNormalize("urn:isbn:096139210x", "urn:isbn:096139210x");
	}




	private void testSchemeSpecificPart(String puri, String expected) throws URIException
	{
		if (expected!=null)
			info("Parse opaque uri, URI, check is opaque check scheme specific part and check path is null",
				new String[]{puri, expected});
		else
			info("Parse uri and check is not opaque", new String[]{puri});

		URI uri = createURI(puri);

		if (expected!=null)
		{
			assertTrue("Failed, Uri is not opaque !!!", uri.isOpaque());
			String ssp = uri.getSchemeSpecificPart();
			assertEquals("scheme specific part differs", ssp, expected);
			if (!uri.getPDPath().equals(ssp)) fail ("Scheme specific part ok but path is not the same...");
		}
		else
		{
			assertFalse("Failed, Uri is opaque !!!", uri.isOpaque());
		}


	}

}