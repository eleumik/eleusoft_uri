package junit.org.eleusoft.uri;

import org.eleusoft.uri.URI;
import org.eleusoft.uri.URIException;

public class Test_URI00_ParseURI extends BaseTestURI
{

	public Test_URI00_ParseURI() {
		super();
	}

	// added 200705, also new method createAndCheckToStringEqualsExpected(
	//public void testSchemeLowerCase()  throws Exception
	//{
		// first param is expected
	//	createAndCheckToStringEqualsExpected("HTTP://example.com/", "HTTP://example.com/");
	//}


	public void testParseURI01() throws Exception
	{
		createAndCheckToStringEquals("");
	}
	public void testParseURI02() throws Exception
	{
		createAndCheckToStringEquals("");

	}
	public void testParseURI03() throws Exception
	{
		createAndCheckToStringEquals("rel");

	}
	public void testParseURI04() throws Exception
	{
		createAndCheckToStringEquals("/");

	}
	public void testParseURI05() throws Exception
	{
		createAndCheckToStringEquals("/abs");

	}
	public void testParseURI06() throws Exception
	{
		createAndCheckToStringEquals("/abs/2nd");

	}
	public void testParseURI07() throws Exception
	{
		createAndCheckToStringEquals("http://www.example.org/");

	}
	public void testParseURI08() throws Exception
	{
		createAndCheckToStringEquals("HTTP://www.example.org/"); // should not change it..

	}
	public void testParseURI09() throws Exception
	{
		createAndCheckToStringEquals("http://www.example.org");

	}
	public void testParseURI10() throws Exception
	{
		createAndCheckToStringEquals("file:///~calendar");

	}
	public void testParseURI11() throws Exception
	{
		createAndCheckToStringEquals("?param=value");

	}
	public void testParseURI12() throws Exception
	{
			createAndCheckToStringEquals("?param=R%26D");
	}
	public void testParseURI13() throws Exception
	{

		createAndCheckToStringEquals("#anchor");
	}

//////////////////////// FRAGMENT


	public void testParseURI_FragmentWith_AT_decoded() throws Exception
	{
		createAndCheckToStringEquals("#The%20@form%20attribute");
	}
	public void testParseURI_FragmentWith_AT_encoded() throws Exception
	{
		createAndCheckToStringEquals("#The%20%40form%20attribute");
	}
	public void testParseURI_FragmentWith_Hash_Decoded_IsError() throws Exception
	{
		// Is error my friend..createAndCheckToStringEquals("#The#Symbol");
		parseURIForError("#The#Symbol");
	}
	public void testParseURI_FragmentWith_Hash_Encoded() throws Exception
	{
		createAndCheckToStringEquals("#The%23Symbol");
	}
	public void testParseURI_FragmentWith_QuestionMark_decoded() throws Exception
	{
		createAndCheckToStringEquals("#Who?");
	}
	public void testParseURI_FragmentWith_QuestionMark_encoded() throws Exception
	{
		createAndCheckToStringEquals("#Who%3F");
	}
	/**
	URI         = scheme ":" hier-part [ "?" query ] [ "#" fragment ]

	      hier-part   = "//" authority path-abempty
	                  / path-absolute
	                  / path-rootless
	                  / path-empty
	**/
	public void testParseURI_FragmentWithScheme_EmptyPathIsLegal() throws Exception
	{
		createAndCheckToStringEquals("about:#Who"); // IS this perhaps an error ? think so..TODO
	}
	public void testParseURI_FragmentWithSchemeAndSSP_QuestionMark_decoded_Legal() throws Exception
	{
		createAndCheckToStringEquals("about:W#ho?");
	}
	public void testParseURI_FragmentWith_Slash_Decoded() throws Exception
	{
		createAndCheckToStringEquals("#I/O");
	}

	public void testParseURI_FragmentWith_SLASH_decoded() throws Exception
	{
		createAndCheckToStringEquals("#Goto%20home/mik");
	}

	public void testParseURI_FragmentWith_SLASH_encoded() throws Exception
	{
		createAndCheckToStringEquals("#Goto%20home%2fmik");
	}

	////////////////// TEST FRAGMENT DELIMITERS (FRAG AND QUERY HAVE THE SAME SYNTAX)

	public void testParseURI_FragmentWith_NOTALLOWED_AllRFCGenDelims_decoded_ERROR1() throws Exception
	{
		parseURIForError("#[");
	}
	public void testParseURI_FragmentWith_NOTALLOWED_AllRFCGenDelims_decoded_ERROR2() throws Exception
	{
		parseURIForError("#]");
	}

	public void testParseURI_FragmentWith_NOTALLOWED_AllRFCGenDelims_decoded_ERROR3() throws Exception
	{
		parseURIForError("##");
	}
	public void testParseURI_FragmentWith_Allowed_RFCGenDelims_decoded() throws Exception
	{
		// ALL: ":" / "/" / "?" / "#" / "[" / "]" / "@"
		// ALLOWED IN FRAG: :/@?
		createAndCheckToStringEquals("#:/@?");
	}

	public void testParseURI_FragmentWith_AllRFCSubDelims_decoded() throws Exception
	{
				// http://www.apps.ietf.org/rfc/rfc3986.html#sec-2.2
				// sub-delims
				// "!" / "$" / "&" / "'" / "(" / ")" / "*" / "+" / "," / ";" / "="

		createAndCheckToStringEquals("#!$&'()*+,;=");
	}



//////////////////////////// QUERY




	////////////////// TEST QUERY DELIMITERS  (FRAG AND QUERY HAVE THE SAME SYNTAX)

public void testParseURI_QueryWith_NOTALLOWED_AllRFCGenDelims_decoded_ERROR1() throws Exception
	{
		parseURIForError("?[");
	}
	public void testParseURI_QueryWith_NOTALLOWED_AllRFCGenDelims_decoded_ERROR2() throws Exception
	{
		parseURIForError("?]");
	}

	public void testParseURI_QueryWith_NOTALLOWED_AllRFCGenDelims_decoded_ERROR3() throws Exception
	{
		parseURIForError("?[]");
	}

	public void testParseURI_QueryWith_HashDecoded_ButIsFragment() throws Exception
	{
		createAndCheckToStringEquals("?#");
	}
	public void testParseURI_QueryWith_Allowed_RFCGenDelims_decoded() throws Exception
	{
		// ALL: ":" / "/" / "?" / "#" / "[" / "]" / "@"
		// ALLOWED IN FRAG or QUERY : :/@?
		createAndCheckToStringEquals("?:/@?");
	}




	public void testParseURI_QueryWith_RFCGenDelimsNoHashQuestionMarkSquareBrackets_decoded() throws Exception
	{
			// ":" / "/" / "?" / "#" / "[" / "]" / "@"
		createAndCheckToStringEquals("?:/@");
	}
	// Square brackets are officially not allowed - tests are up

	public void testParseURI_QueryWith_AllRFCGenDelims_decoded_HashQuestionMark_encoded_NoSquareBrackets() throws Exception
	{
			// ":" / "/" / "?" / "#" / "[" / "]" / "@"
		createAndCheckToStringEquals("?:/%23%3f@");
	}


	public void testParseURI_QueryWith_AllRFCSubDelims_decoded() throws Exception
	{
				// http://www.apps.ietf.org/rfc/rfc3986.html#sec-2.2
				// sub-delims
				// "!" / "$" / "&" / "'" / "(" / ")" / "*" / "+" / "," / ";" / "="

		createAndCheckToStringEquals("?!$&'()*+,;=");
	}

	public void testParseURI_SchemeCanContainNumber() throws Exception
	{
		createAndCheckToStringEquals("s1://ciao");
	}
	public void testParseURI_SchemeCanContainPlus() throws Exception
	{
		createAndCheckToStringEquals("s+1://ciao");
	}
	public void testParseURI_SchemeCanContainMinus() throws Exception
	{
		createAndCheckToStringEquals("s-1://ciao");
	}
	public void testParseURI_SchemeCanContainDot() throws Exception
	{
		createAndCheckToStringEquals("s.1://ciao");
	}

	public void testParseURI_PathFirstSegmentWithColonEscaped_Undetermined() throws Exception
	{
		// for RFC should fail but somebody parses
		// 200705 sure ?
		createAndCheckToStringEquals("a%3ab");
	}
	public void testParseURI_PathSegmentsEmpty() throws Exception
	{
		createAndCheckToStringEquals("////////////////");
	}
	public void testParseURI_Path_MultiDot() throws Exception
	{
		createAndCheckToStringEquals("/.../..../");
	}


	public void testParseURI_PathSecondSegmentWithColonButEscaped() throws Exception
	{
		createAndCheckToStringEquals("b/a%3ab");
	}

	public void testParseURICheckPath_OnlyColonButEscaped() throws Exception
	{
		createAndCheckPathEquals("%3a", ":");
	}

///////////////////// USERINFO / AUTHORITY / PORT


	public void testParseURI_UserInfo_Normal() throws Exception
	{
		createAndCheckToStringEquals("http://mimmo@www.example.com/");
	}

	public void testParseURI_UserInfo_With3F() throws Exception
	{
		createAndCheckToStringEquals("http://mi%3fmmo@www.example.com/");
	}

	public void testParseURI_UserInfo_WithATEscaped40() throws Exception
	{
		createAndCheckToStringEquals("http://B%40T@www.example.com/");
	}


	public void testParseURI_Authority_IPv6_WithPort() throws Exception
	{
			// note 0000 == :
		createAndCheckToStringEquals("http://[2001:0db8:85a3:::1319:8a2e:0370:7344]:443/");
	}

	// See http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6345409

	public void testParseURI_Authority_DoesNotRecognizeIPvFuture_1_BUG6345409() throws Exception
	{
		createAndCheckToStringEquals("s://@[vf0123456789aBcDe..::::a0-._~:!$&'()*+,;=]/path");
	}

	public void testParseURI_Authority_DoesNotRecognizeIPvFuture_2_BUG6345409() throws Exception
	{
		createAndCheckToStringEquals("s://@[v0.:]/path");
	}

	//http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6338951

	public void testParseURI_Authority_InvalidDNS_ButValidURI_Bug6338951() throws Exception
	{
		createAndCheckToStringEquals("http://-a");

	}

	//http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=5049974

	public void testParseURI_Authority_HostNameWithUnderscore_Bug5049974() throws Exception
	{
		createAndCheckToStringEquals("http://a_b/");
	}

	//http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6363889

	public void testParserURI_AuthorityAndUserInfoEmpty_Bug6363889() throws Exception
	{
		createAndCheckToStringEquals("s://@/path");
	}



	//http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6553128
	public void testParseURI_UnderscoreInAuth_Sun_Bug_6553128() throws Exception
	{
		 createAndCheckToStringEquals("http://jessicacook_1.tripod.com/");
	}
	public void testParseURIForError_BackSlash()throws Exception
	{
		 parseURIForError("http:\\\\www.sun.com\\index.html");
	}
	// http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6345551
	public void testParseURI_StrangeFromSun_Bug_6345551() throws Exception
	{
		 createAndCheckToStringEquals("s://ui@%5B::0000%5D/path'");
	}








//////////////////////// ERRORS

	public void testWrongURI1() throws Exception
	{
		parseURIForError(":");
	}

	public void testWrongURI_StartsWithColon() throws Exception
	{
		parseURIForError(":www");

	}
	public void testWrongURI_OnlyAngleBracketOpen() throws Exception
	{
		parseURIForError("<");

	}
	public void testWrongURI_OnlyQuote() throws Exception
	{
		parseURIForError("\"");

	}
	public void testWrongURI_PathWithSpace() throws Exception
	{
		parseURIForError("/a space s/");

	}

	public void testWrongURI_FragWithSpace() throws Exception
	{
		parseURIForError("/a/#a s");
	}
	public void testWrongURI_QueryWithSpace1() throws Exception
	{
		parseURIForError("/a/?a s");
	}

	public void testWrongURI_QueryWithSpace2() throws Exception
	{
		parseURIForError("/a/?a s#as");
	}


	public void testWrongURI_AuthorityWithSpace() throws Exception
	{
		parseURIForError("dum://mi k2/a/b.xml");
	}


	public void testWrongURI_SchemeWithSpace() throws Exception
	{
		parseURIForError("dum y://example.com");
	}


	public void testWrongURI_SchemeCannotStartWithNumber() throws Exception
	{
		parseURIForError("1scheme://ciao");

	}

	public void testWrongURI_SchemeCannotContainUnderscore() throws Exception
	{
		parseURIForError("sc_heme://ciao");

	}

	public void testWrongURI_OnlyPercentZeroZero() throws Exception
	{
		parseURIForError("%00");

	}
	public void testWrongURI_QueryWithPercent() throws Exception
	{
		parseURIForError("?%");
	}
	public void testWrongURI_QueryWithPercentWrong() throws Exception
	{
		parseURIForError("?%AZ");
	}


	public void testWrongURI_AbsPathWithPercent() throws Exception
	{
		parseURIForError("/%");
	}
	public void testWrongURI_AbsPathWithPercentWrong() throws Exception
	{
		parseURIForError("/%AZ");
	}

	public void testWrongURI_RelRefWithPercent() throws Exception
	{
		parseURIForError("%");
	}

	public void testWrongURI_RelRefWithPercentWrong() throws Exception
	{
		parseURIForError("%AZ");
	}

	public void testWrongURI_FragmentWithPercent() throws Exception
	{
		parseURIForError("#%");
	}

	public void testWrongURI_FragmentWithPercentWrong() throws Exception
	{
		parseURIForError("#%AZ");
	}

	//////////////////////////////////// PATH

	public void testParseURI_CheckPath_RelPathOnlyDot() throws Exception
	{
		createAndCheckPathEquals(".", ".");
	}

	public void testParseURI_CheckPath_RelPathOnlyDoubleDot() throws Exception
	{
		createAndCheckPathEquals("..", "..");
	}

	public void testParseURI_CheckPath_RelPath() throws Exception
	{
		createAndCheckPathEquals("rel", "rel");
		createAndCheckPathEquals("rel/pop", "rel/pop");
	}
	public void testParseURI_CheckPath_RelPath_WithDots() throws Exception
	{

		createAndCheckPathEquals("./rel/pop", "./rel/pop");
		createAndCheckPathEquals("../rel/pop", "../rel/pop");
	}

	public void testParseURI_CheckPath_RelPathWithQuery() throws Exception
	{
		createAndCheckPathEquals("?param=value", "");
		createAndCheckPathEquals("pop?param=value", "pop");
	}
	public void testParseURI_CheckPath_RelPathWithFragment() throws Exception
	{
		createAndCheckPathEquals("#anchor", "");
		createAndCheckPathEquals("pop#anchor", "pop");

	}

	public void testParseURI_CheckPath_RelPathWithQueryAndFragment() throws Exception
	{
		createAndCheckPathEquals("?q=r#anchor", "");
		createAndCheckPathEquals("pop?q=r#anchor", "pop");

	}

	public void testParseURI_CheckPath_RelPath_Ex() throws Exception
	{
		createAndCheckPathEquals(".?param=value", ".");
		createAndCheckPathEquals(".#anchor", ".");

	}


	public void testParseURI_CheckPath_AbsolutePath() throws Exception
	{
		createAndCheckPathEquals("/", "/");
		createAndCheckPathEquals("/abs", "/abs");
		createAndCheckPathEquals("/abs/2nd", "/abs/2nd");

	}
	public void testParseURI_CheckPath_AbsolutePath_WithDots() throws Exception
	{

		createAndCheckPathEquals("/.", "/.");
		createAndCheckPathEquals("/../", "/../");
	}

	public void testParseURI_CheckPath_AbsolutePath_WithColonInside() throws Exception
	{
		createAndCheckPathEquals("/a:b", "/a:b");
}




	public void testParseURI_CheckPath_AbsolutePath_WithSpaces() throws Exception
	{
		createAndCheckPathEquals("/with%20space", "/with space");
	}

	public void testParseURI_CheckPath_AbsolutePath_WithQueryAndOrFragment() throws Exception
	{

		createAndCheckPathEquals("/?param=value", "/");
		createAndCheckPathEquals("/.?param=value", "/.");
		createAndCheckPathEquals("/#anchor", "/");
		createAndCheckPathEquals("/.#anchor", "/.");

	}
	public void testParseURI_CheckPath_AbsoluteURI() throws Exception
	{
		createAndCheckPathEquals("http://www.example.org/", "/");
		createAndCheckPathEquals("http://www.example.org/.", "/.");
		createAndCheckPathEquals("http://www.example.org/../", "/../");
		createAndCheckPathEquals("http://www.example.org/with%20space", "/with space");
		createAndCheckPathEquals("http://www.example.org/abs/2nd", "/abs/2nd");
		createAndCheckPathEquals("http://www.example.org/?p=q", "/");
		createAndCheckPathEquals("http://www.example.org/#anchor", "/");
		createAndCheckPathEquals("http://www.example.org/?p=q#anchor", "/");
	}


	public void testParseURI_CheckPath_HttpAuthorityNoSlash() throws Exception
	{
		createAndCheckPathEquals("http://www.example.org", "");
	}


	public void testParseURI_CheckPath_AbsolutePathWithEncodedHash() throws Exception
	{
		createAndCheckPathEquals("/a%23.xml", "/a#.xml");
	}


	public void testParseURI_CheckPath_RelPathStartsWithEncodedHash() throws Exception
	{
		createAndCheckPathEquals("%23.xml", "#.xml");
	}

	// new 200705 + in path
	public void testParseURI_CheckPath_PlusInPath() throws Exception
	{
		createAndCheckPathEquals("http://localhost/test+t%20est", "/test+t est");
	}








///////////////////////




	///////////////////////




	private static final String CACPE = "Parse URI an check path equals expected, URI, expected path";
	protected void createAndCheckPathEquals(String uri, String path) throws URIException
	{
		info(CACPE, new String[]{uri, path});
		URI uriObj = createURI(uri);
		String test = uriObj.getPDPath();
		assertEquals("createAndCheckPathEquals: [" + path + "]<>[" + test + "]  for uri [" + uri + "]", path, test);
	}





}