package junit.org.eleusoft.uri;

import org.eleusoft.uri.URIException;
import org.eleusoft.uri.URIFactory;

public class Test_URI23_ParseSchemeAPI extends BaseTestURI
{

	public Test_URI23_ParseSchemeAPI() {
		super();
	}

	private static final String PARSESCHEME = "Parse URI and check scheme and scheme specific part are the same as passed.,URI,SCHEME,SCHEME SPECIFIC PART";

	protected void parseSchemeAndCheckExpectedParts(String expectedScheme, String expectedSSPart, String uri) throws URIException
	{
		info(PARSESCHEME, new String[]{uri,expectedScheme, expectedSSPart});
		String[] s = URIFactory.parseScheme(uri);
		assertEquals("Scheme ["+ s[0] + "] differs!!" , expectedScheme, s[0]);
		assertEquals("Scheme specific part [" + s[1] + "] differs!!" , expectedSSPart, s[1]);

	}
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
	}



	public void testParseScheme_OnlyScheme() throws Exception
	{
		parseSchemeAndCheckExpectedParts("http", "", "http:");
	}

	public void testParseScheme_AbsURIWithFragment() throws Exception
	{
		parseSchemeAndCheckExpectedParts("http", "//www.example.com/foo/?p=q#ciao", "http://www.example.com/foo/?p=q#ciao");
	}


	public void testParseScheme_RelativeReference() throws Exception
	{
		parseSchemeAndCheckExpectedParts(null, "/foo/?p=q#ciao", "/foo/?p=q#ciao");
	}

	public void testParseScheme_RelativeReference_AbsPath() throws Exception
	{
		parseSchemeAndCheckExpectedParts(null, "/foo", "/foo");
	}

	public void testParseScheme_RelativeReference_AbsRootPath() throws Exception
	{
		parseSchemeAndCheckExpectedParts(null, "/", "/");
	}

	public void testParseScheme_RelativeReference_EmptyPath() throws Exception
	{
		parseSchemeAndCheckExpectedParts(null, "", "");
	}


	public void testParseScheme_RelativeReference_RelPath() throws Exception
	{
		parseSchemeAndCheckExpectedParts(null, "foo", "foo");
	}


	public void testParseScheme_RelativeReference_RelPathQueryFrag() throws Exception
		{
			parseSchemeAndCheckExpectedParts(null, "foo/?p=q#ciao", "foo/?p=q#ciao");
		}


	public void testParseScheme_RelativeReference_Query() throws Exception
	{
		parseSchemeAndCheckExpectedParts(null, "?p=q", "?p=q");
	}


	public void testParseScheme_RelativeReference_QueryEmpty() throws Exception
	{
		parseSchemeAndCheckExpectedParts(null, "?", "?");
	}


	public void testParseScheme_RelativeReference_Fragment() throws Exception
	{
		parseSchemeAndCheckExpectedParts(null, "#frag", "#frag");
	}


	public void testParseScheme_RelativeReference_FragmentEmpty() throws Exception
	{
		parseSchemeAndCheckExpectedParts(null, "#", "#");
	}


	public void testParseScheme_RelativeReference_WithColon_OK() throws Exception
	{
		parseSchemeAndCheckExpectedParts(null, "./a:b/d", "./a:b/d");
	}

	///////// TRICKY

	public void testParseScheme_Tricky_ColonAfterQuestionMark_IsRelRefWithQuery() throws Exception
	{
			// since can not be parsed as a scheme,
			// is a rel ref with query
		parseSchemeAndCheckExpectedParts(null, "a?a:b", "a?a:b");
	}


	public void testParseScheme_Tricky_ColonAfterHash_IsRelRefWithFrag() throws Exception
	{
			// since can not be parsed as a scheme,
			// is a rel ref with fragment
		parseSchemeAndCheckExpectedParts(null, "a#a:b", "a#a:b");
	}

	public void testParseScheme_Tricky_ColonAfterSlash_IsRelRefWithPathWithColon() throws Exception
	{
			// since can not be parsed as a scheme..
		parseSchemeAndCheckExpectedParts(null, "a/a:b", "a/a:b");
	}


	public void testParseScheme_Tricky_SchemeWithMinus() throws Exception
	{
			// since can not be parsed as a scheme..
		parseSchemeAndCheckExpectedParts("a-b", "ab", "a-b:ab");
	}


	public void testParseScheme_Tricky_PercentInPseudoScheme_IsRelRef() throws Exception
	{
			// since can not be parsed as a scheme..
		parseSchemeAndCheckExpectedParts(null, "a%55b:ab", "a%55b:ab");
	}



	/////////// ERRORS

	public void testParseSchemeForError_ColonInRelPath_InterpretedAsScheme() throws Exception
	{
		parseSchemeAndCheckExpectedParts("a", "b/d", "a:b/d");
	}



	public void testParseSchemeForError_OnlyColon() throws Exception
	{
		parseSchemeForError(":");
	}



	public void testParseSchemeForError_StartsWithColon() throws Exception
	{
		parseSchemeForError(":aaa");
	}






}