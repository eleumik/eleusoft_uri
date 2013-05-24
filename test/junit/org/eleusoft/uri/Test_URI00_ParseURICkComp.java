package junit.org.eleusoft.uri;


public class Test_URI00_ParseURICkComp extends BaseTestURI
{

	public Test_URI00_ParseURICkComp() {
		super();
	}





	public void testParseAndCheckComp_FullURI() throws Exception
	{
		// Path is always at least ""
		parseURIAndCheckComponents("http://w.c.d/f/g?p=q#f", "http", "w.c.d", "/f/g", "p=q", "f");
	}
	public void testParseAndCheckComp_FullURI_BetweenQuotes_RFC3986_AppendixC() throws Exception
	{
		// Path is always at least ""
		// false means do not do toString check
		parseURIAndCheckComponents(false,"\"http://w.c.d/f/g?p=q#f\"", "http", "w.c.d", "/f/g", "p=q", "f");
	}

	public void testParseAndCheckComp_FullURI_BetweenAngleBrackets_RFC3986_AppendixC() throws Exception
	{
		// Path is always at least ""
		// false means do not do toString check
		parseURIAndCheckComponents(false,"<http://w.c.d/f/g?p=q#f>", "http", "w.c.d", "/f/g", "p=q", "f");
	}

	public void testParseAndCheckComp_FullURI_BetweenAngleBrackets_WrongTwoOpenBracket_RFC3986_AppendixC() throws Exception
	{
		parseURIForError("<http://w.c.d/f/g?p=q#f<");

	}



	public void testParseAndCheckComp_WindowsShareURI_5Slash() throws Exception
	{
		// This comes from windows \\mik\distribution\UI_Office\ path
		parseURIAndCheckComponents("file://///mik2/distribution/UI_Office/", "file", "", "///mik2/distribution/UI_Office/", null, null);
	}

	public void testParseAndCheckComp_WindowsShareURI_4Slash() throws Exception
	{
		// This comes from windows \\mik\distribution\UI_Office\ path
		parseURIAndCheckComponents("file:////mik2/distribution/UI_Office/", "file", "", "//mik2/distribution/UI_Office/", null, null);
	}

	public void testParseAndCheckComp_WindowsShareURI_3Slash() throws Exception
	{
		// This comes from windows \\mik\distribution\UI_Office\ path
		parseURIAndCheckComponents("file:///mik2/distribution/UI_Office/", "file", "", "/mik2/distribution/UI_Office/", null, null);
	}

	public void testParseAndCheckComp_WindowsShareURI_2Slash() throws Exception
	{
		// This comes from windows \\mik\distribution\UI_Office\ path
		parseURIAndCheckComponents("file://mik2/distribution/UI_Office/", "file", "mik2", "/distribution/UI_Office/", null, null);
	}



	public void testParseAndCheckComp_FileSchemeSlashSlashSlashFooSlashBar() throws Exception
	{
		parseURIAndCheckComponents("file:///foo/bar", "file", "", "/foo/bar", null, null);
	}
	public void testParseAndCheckComp_HostNameWithDotAtEnd_RFC3986_Sect_3_2_2() throws Exception
	{
		parseURIAndCheckComponents("http://www.example.", "http", "www.example.", "", null, null);
	}
	public void testParseAndCheckComp_HostNameWithDotAtEndAndRoot_RFC3986_Sect_3_2_2() throws Exception
	{
		parseURIAndCheckComponents("http://www.example./", "http", "www.example.", "/", null, null);
	}
	public void testParseAndCheckComp_HostNameWithDotAtEndAndRootSub_RFC3986_Sect_3_2_2() throws Exception
	{
		parseURIAndCheckComponents("http://www.example./root", "http", "www.example.", "/root", null, null);
	}
	public void testParseAndCheckComp_HostNameWithDotAtEndAndRootDot_RFC3986_Sect_3_2_2() throws Exception
	{
		parseURIAndCheckComponents("http://www.example./.", "http", "www.example.", "/.", null, null);
	}


	///////////// THESE THREE ARE EQUIVALENT FOR THE FILE: scheme. FOR URI they are different

	public void testParseAndCheckComp_File_NoAuthority() throws Exception
	{
		parseURIAndCheckComponents("file:/c:/temp", "file", null, "/c:/temp", null, null);
	}

	public void testParseAndCheckComp_File_EmptyAuthority() throws Exception
	{
		parseURIAndCheckComponents("file:///c:/temp", "file", "", "/c:/temp", null, null);
	}
	public void testParseAndCheckComp_File_Localhost() throws Exception
	{
		parseURIAndCheckComponents("file://localhost/c:/temp", "file", "localhost", "/c:/temp", null, null);
	}


	////////////////// the + problem


	public void testParseAndCheckComp_Plus_InPath() throws Exception
	{
		parseURIAndCheckComponents("http://localhost/test+test", "http", "localhost", "/test+test", null, null);
	}

	public void testParseAndCheckComp_Plus_InAuth_DomainNameCannotContainPlus_ButIsNotError() throws Exception
	{
		// Domain name cannot contain +, can contain minus - but plus not.
		// Anyway this is not an error, it depends on the scheme.
		parseURIAndCheckComponents("http://local+host/", "http", "local+host", "/", null, null);
	}
	public void testParseAndCheckComp_Minus_InAuth() throws Exception
	{
		parseURIAndCheckComponents("http://1-1", "http", "1-1", "", null, null);
	}
	public void testParseAndCheckComp_Plus_InQuery() throws Exception
	{
		parseURIAndCheckComponents("http://localhost/?c+a", "http", "localhost", "/", "c+a", null);
	}
	public void testParseAndCheckComp_Plus_InFrag() throws Exception
	{
		parseURIAndCheckComponents("http://localhost/#c+a", "http", "localhost", "/", null, "c+a");
	}

	///////////////// Empty path abs uri

	public void testParseAndCheckComp_QuestionMarkDecodedInFragment() throws Exception
	{
		parseURIAndCheckComponents("about:/W#ho?", "about", null, "/W", null, "ho?");
	}


	public void testParseAndCheckComp_OpaqueURI() throws Exception
	{
		parseURIAndCheckComponents("about:blank", "about", null, "blank", null, null);
	}

	///////////// SUN BUGS

	//http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4479463
	public void testParseURI_NoPath_Bug4479463() throws Exception
	{
		parseURIAndCheckComponents("http://sun.com?sid=130885#name", "http", "sun.com", "", "sid=130885", "name");
	}





	///////////////////////






}