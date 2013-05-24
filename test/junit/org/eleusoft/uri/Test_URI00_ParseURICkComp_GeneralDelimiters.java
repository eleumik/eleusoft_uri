package junit.org.eleusoft.uri;


public class Test_URI00_ParseURICkComp_GeneralDelimiters extends BaseTestURI
{

	public Test_URI00_ParseURICkComp_GeneralDelimiters() {
		super();
	}






	//////////////////

	// Test general delimiters ":" / "/" / "?" / "#" / "[" / "]" / "@"  in name

	/////////// [:] %3a

	public void testPChkComp_GeneralDelimiters_AbsPath_Colon() throws Exception
	{
		parseURIAndCheckComponents("/b/a%3ab", null, null, "/b/a%3ab", null, null);
	}

	public void testPChkComp_GeneralDelimiters_RelPath_Colon() throws Exception
	{
		parseURIAndCheckComponents("b/a%3ab", null, null, "b/a%3ab", null, null);
	}

	public void testPChkComp_GeneralDelimiters_Path_ColonInFirstSegmentWithDotSlashInFront() throws Exception
	{
		parseURIAndCheckComponents("./a:b", null, null, "./a:b", null, null);
	}


	public void testParseAndCheckComp_RelPathWithEscapedColonInside() throws Exception
	{
		parseURIAndCheckComponents("b/a%3ab", null, null, "b/a%3ab", null, null);
	}




	////////////// [/] %2f

	public void testPChkComp_GeneralDelimiters_Path_SlashEscaped_InconsistentForAPI() throws Exception
	{
		// We mean root segment "a/b" and not "b" next segment of root segment "a"
		// but using unescaped path the info is lost
		parseURIAndCheckComponents("/a%2fb", null, null, "/a%2fb", null, null);
	}
	public void testPChkComp_GeneralDelimiters_RelPath_SlashEscaped_InconsistentForAPI() throws Exception
	{
		// We mean root segment "a/b" and not "b" next segment of root segment "a"
		// but using unescaped path the info is lost
		parseURIAndCheckComponents("/a%2fb", null, null, "/a%2fb", null, null);
	}
	////////////// [?] %3f
	public void testPChkComp_GeneralDelimiters_Path_QuestionMark_Escaped() throws Exception
	{
		parseURIAndCheckComponents("/a%3fb", null, null, "/a%3fb", null, null);
	}
	public void testPChkComp_GeneralDelimiters_RelPath_QuestionMark_Escaped() throws Exception
	{
		parseURIAndCheckComponents("a%3fb", null, null, "a%3fb", null, null);
	}
	public void testPChkComp_GeneralDelimiters_Path_QuestionMark_Unescaped_IsQuery() throws Exception
	{
		parseURIAndCheckComponents("/a?b", null, null, "/a", "b", null);
	}
	public void testPChkComp_GeneralDelimiters_Query_QuestionMark_Unescaped_IsQuery() throws Exception
	{
		parseURIAndCheckComponents("?:/@?", null, null, "", ":/@?", null);
	}

	//////////////// [#] %23
	public void testPChkComp_GeneralDelimiters_Path_Hash_Escaped() throws Exception
	{
		parseURIAndCheckComponents("/a%23b", null, null, "/a%23b", null, null);
	}
	public void testPChkComp_GeneralDelimiters_RelPath_Hash_Escaped() throws Exception
	{
		parseURIAndCheckComponents("a%23b", null, null, "a%23b", null, null);
	}
	public void testPChkComp_GeneralDelimiters_Path_Hash_Unescaped_IsFragment() throws Exception
	{
		parseURIAndCheckComponents("/a#b", null, null, "/a", null, "b");
	}


	//////////////// [@] %40
	public void testPChkComp_GeneralDelimiters_Path_AT_40_Escaped() throws Exception
	{
		parseURIAndCheckComponents("/a%40b", null, null, "/a%40b", null, null);
	}
	public void testPChkComp_GeneralDelimiters_RelPath_AT_40_Escaped() throws Exception
	{
		parseURIAndCheckComponents("a%40b", null, null, "a%40b", null, null);
	}
	public void testPChkComp_GeneralDelimiters_Path_AT_40_Unescaped_IsOk() throws Exception
	{
		parseURIAndCheckComponents("/a@b", null, null, "/a@b", null, null);
	}

	//////////////// ([) %5B
	public void testPChkComp_GeneralDelimiters_Path_OpenSquareBracket_Escaped() throws Exception
	{
		parseURIAndCheckComponents("/a%5bb", null, null, "/a%5bb", null, null);
	}
	public void testPChkComp_GeneralDelimiters_RelPath_OpenSquareBracket_Escaped() throws Exception
	{
		parseURIAndCheckComponents("a%5bb", null, null, "a%5bb", null, null);
	}
	public void testPChkComp_GeneralDelimiters_Path_OpenSquareBracket_Unescaped_ShouldBeError() throws Exception
	{
		parseURIForError("/a[b");

	}
	public void testPChkComp_GeneralDelimiters_Path_OpenAngleBracket_Unescaped_ShouldBeError() throws Exception
	{
		parseURIForError("/a<b");

	}

	// Quote " %22

	public void testPChkComp_GeneralDelimiters_Path_Quote_Escaped() throws Exception
	{
		parseURIAndCheckComponents("/a%22b", null, null, "/a%22b", null, null);
	}
	public void testPChkComp_GeneralDelimiters_RelPath_Quote_Escaped() throws Exception
	{
		parseURIAndCheckComponents("a%22b", null, null, "a%22b", null, null);
	}
	public void testPChkComp_GeneralDelimiters_Path_Quote_Unescaped_ShouldBeError() throws Exception
	{
			parseURIForError("/a\"b");

	}




	///////////////////////






}