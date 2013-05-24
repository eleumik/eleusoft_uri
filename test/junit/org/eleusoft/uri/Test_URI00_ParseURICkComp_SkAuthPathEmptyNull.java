package junit.org.eleusoft.uri;

import org.eleusoft.uri.URI;

public class Test_URI00_ParseURICkComp_SkAuthPathEmptyNull extends BaseTestURI
{

	public Test_URI00_ParseURICkComp_SkAuthPathEmptyNull() {
		super();
	}


	// http: is a valid URI, also http://
	// SEE http://lists.w3.org/Archives/Public/uri/2005May/0036.html

	public void testParseAndCheckComp_SchemeOnly() throws Exception
	{
		parseURIAndCheckComponents("http:", "http", null, "", null, null);

	}
	// http://lists.w3.org/Archives/Public/uri/2005May/0043.html

	public void testParseAndCheckComp_SchemeNoAuthRootPath() throws Exception
	{
		parseURIAndCheckComponents("http:/", "http", null, "/", null, null);

	}
	public void testParseAndCheckComp_SchemeAndEmptyAuth() throws Exception
	{
		parseURIAndCheckComponents("http://", "http", "", "", null, null);

	}
	public void testParseAndCheckComp_SchemeAndEmptyAuthRootPath() throws Exception
	{
		parseURIAndCheckComponents("http:///", "http", "", "/", null, null);

	}
	public void testParseAndCheckComp_SchemeEmptyAuthAndNETRootPath() throws Exception
	{
		parseURIAndCheckComponents("http:////", "http", "", "//", null, null);

	}
	public void testParseAndCheckComp_SchemeEmptyAuthAndNETPath() throws Exception
	{
		parseURIAndCheckComponents("http:////a", "http", "", "//a", null, null);

	}
	public void testParseAndCheckComp_SchemeAuthAndNETPath() throws Exception
	{
		parseURIAndCheckComponents("http://a//b", "http", "a", "//b", null, null);

	}


	public void testParseAndCheckComp_Empty() throws Exception
	{
		parseURIAndCheckComponents("", null    , null, "", null, null);

	}

	public void testParseAndCheckComp_EmptySchemeOnly() throws Exception
	{
		parseURIForError(":");

	}

	public void testParseAndCheckComp_RELURI_NoAuthRootPath() throws Exception
	{
		parseURIAndCheckComponents("/", null, null, "/", null, null);

	}

	public void testParseAndCheckComp_RELURI_EmptyAuthorityOnly() throws Exception
	{
		parseURIAndCheckComponents("//", null, "", "", null, null);

	}public void testParseAndCheckComp_RELURI_AuthorityOnly() throws Exception
	{
		parseURIAndCheckComponents("//a", null, "a", "", null, null);

	}
	public void testParseAndCheckComp_RELURI_EmptyAuthorityRootPath() throws Exception
	{
		parseURIAndCheckComponents("///", null, "", "/", null, null);

	}
	public void testParseAndCheckComp_RELURI_AuthorityAndRootPath() throws Exception
	{
		parseURIAndCheckComponents("//a/", null, "a", "/", null, null);

	}


	public void testParseAndCheckComp_RELURI_EmptyAuthorityNETRootPath() throws Exception
	{
		parseURIAndCheckComponents("////", null, "", "//", null, null);

	}
	public void testParseAndCheckComp_RELURI_EmptyAuthorityNETPath() throws Exception
	{
		parseURIAndCheckComponents("////a", null, "", "//a", null, null);

	}



	public void testParseAndCheckComp_SchemeAndDot() throws Exception
	{
		// Path is always at least ""
		parseURIAndCheckComponents("http:.", "http", null, ".", null, null);
	}

	public void testParseAndCheckComp_SchemeAndDotDot() throws Exception
	{
		// Path is always at least ""
		parseURIAndCheckComponents("http:..", "http", null, "..", null, null);
	}

	public void testParseAndCheckComp_SchemeAndHash() throws Exception
	{
		parseURIAndCheckComponents("http:#", "http", null, "", null, "");
	}


	public void testParseAndCheckComp_SchemeEmptyAuthEmptyFrag() throws Exception
	{
		parseURIAndCheckComponents("http://#", "http", "", "", null, "");
	}

	public void testParseAndCheckComp_SchemeAndQuestionmark_isOpaque() throws Exception
	{
		info("Parse \"scheme:?\" uri, URI check is opaque, check scheme spec part", new String[]{"http:?", "?"});
		// Path is always at least ""
		URI uri = createURI("http:?");
		assertEquals("scheme must be http", "http", uri.getScheme());
		assertTrue("must be opaque", uri.isOpaque());
		assertEquals("scheme specific part should be a questionmark", "?", uri.getSchemeSpecificPart());
	}
	public void testParseAndCheckComp_SchemeAndAuth() throws Exception
	{
		// Path is always at least ""
		parseURIAndCheckComponents("http://a", "http", "a", "", null, null);
	}
	public void testParseAndCheckComp_AuthOnly() throws Exception
	{
		// Path is always at least ""
		parseURIAndCheckComponents("//a", null, "a", "", null, null);
	}
	public void testParseAndCheckComp_AuthAndPath() throws Exception
	{
		// Path is always at least ""
		parseURIAndCheckComponents("//a/b:c", null, "a", "/b:c", null, null);
	}

	public void testParseAndCheckComp_QueryOnly() throws Exception
	{
		// Path is always at least ""
		parseURIAndCheckComponents("?a=b", null, null, "", "a=b", null);
	}


	public void testParseAndCheckComp_FragmentOnly() throws Exception
	{
		// Path is always at least ""
		parseURIAndCheckComponents("#f", null, null, "", null, "f");
	}

	public void testParseAndCheckComp_OnlyQuestionMark() throws Exception
	{
		// Path is always at least ""
		parseURIAndCheckComponents("?", null, null, "", "", null);
	}
	public void testParseAndCheckComp_EmptyQueryAndFragment() throws Exception
	{
		// Path is always at least ""
		parseURIAndCheckComponents("?#f", null, null, "", "", "f");
	}
	public void testParseAndCheckComp_QuestionMarkHash() throws Exception
	{
		// Path is always at least ""
		parseURIAndCheckComponents("?#", null, null, "", "", "");
	}
	public void testParseAndCheckComp_SlashQuestionMarkHash() throws Exception
	{
		// Path is always at least ""
		parseURIAndCheckComponents("/?#", null, null, "/", "", "");
	}

	public void testParseAndCheckComp_SlashSlashQuestionMarkHash() throws Exception
	{
		// Path is always at least ""
		parseURIAndCheckComponents("//?#", null, "", "", "", "");
	}

	public void testParseAndCheckComp_SchemeSlashSlashQuestionMarkHash() throws Exception
	{
		// Path is always at least ""
		parseURIAndCheckComponents("a:///?#", "a", "", "/", "", "");
	}

	public void testParseAndCheckComp_SlashSlashSlashQuestionMarkHash() throws Exception
	{
		// Path is always at least ""
		parseURIAndCheckComponents("///?#", null, "", "/", "", "");
	}

	public void testParseAndCheckComp_SchemeSlashSlashSlashQuestionMarkHash() throws Exception
	{
		// Path is always at least ""
		parseURIAndCheckComponents("a:///?#", "a", "", "/", "", "");
	}






	public void testParseAndCheckComp_SchemeEmptyAuthEmptyQueryEmptyFrag() throws Exception
	{
		parseURIAndCheckComponents("http://?#", "http", "", "", "", "");
	}







	///////////////////////






}