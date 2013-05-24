package junit.org.eleusoft.uri;

import org.eleusoft.uri.URIException;
import org.eleusoft.uri.URIFactory;

public class Test_URI24_ExtractFragmentAPI extends BaseTestURI
{

	public Test_URI24_ExtractFragmentAPI() {
		super();
	}



	private static final String EXTRACTFRAGMENT = "Parse URI and check URI without fragment and fragment are the same as passed.,URI,URI-NO-FRAGMENT,FRAGMENT";

	protected void extractFragmentAndCheckExpectedParts(String expectedNF, String expectedFrag, String uri) throws URIException
	{
		info(EXTRACTFRAGMENT, new String[]{uri,expectedNF, expectedFrag});
		String[] s = URIFactory.extractFragment(uri);
		assertEquals("NO-FRAGMENT-URI ["+ s[0] + "] differs!!" , expectedNF, s[0]);
		assertEquals("FRAGMENT ["+ s[1] + "] differs!!" , expectedFrag, s[1]);

	}
	/**
	protected void parseSchemeForError(String uri) throws URIException
	{
		info(PARSESCHEME, new String[]{uri, "[error expected]", "[error expected]"});
		try
		{
			String[] s = URIFactory.parseScheme(uri);
			fail("Should have thrown URIException for " + uri);

		}
		catch(URIException e)
		{
			return;
		}
	}**/



	public void testExtractFragment_AbsURI() throws Exception
	{
		extractFragmentAndCheckExpectedParts("http://www.example.com/foo/?p=q", "ciao", "http://www.example.com/foo/?p=q#ciao");
	}


	public void testExtractFragment_AbsPath() throws Exception
	{
		extractFragmentAndCheckExpectedParts("/foo/?p=q", "ciao", "/foo/?p=q#ciao");
	}


	public void testExtractFragment_RelPathQueryFrag() throws Exception
	{
		extractFragmentAndCheckExpectedParts("foo/?p=q" ,"ciao", "foo/?p=q#ciao");
	}


	public void testExtractFragment_Fragment() throws Exception
	{
		extractFragmentAndCheckExpectedParts("", "frag", "#frag");
	}


	public void testExtractFragment_FragmentEmpty() throws Exception
	{
		extractFragmentAndCheckExpectedParts("", "", "#");
	}
	public void testExtractFragment_NoFragment() throws Exception
	{
		extractFragmentAndCheckExpectedParts("http://www.example.com/foo/", null, "http://www.example.com/foo/");
	}


	public void testExtractFragment_AllEmpty() throws Exception
	{
		extractFragmentAndCheckExpectedParts("", null, "");
	}

	public void testExtractFragment_DoubleHash() throws Exception
	{
		extractFragmentAndCheckExpectedParts("foo/?p=q" , "ciao#ciao", "foo/?p=q#ciao#ciao");
	}


}