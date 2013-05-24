package junit.org.eleusoft.uri;

import org.eleusoft.uri.URI;
import org.eleusoft.uri.URIException;

public class Test_URI09_CreatePathComponentOnlyURI extends BaseTestURI
{
	// These tests use the components-based constructor
	// to build a path only URI...TODO change the testmethod
	// to abstract to test paths and URI

	public Test_URI09_CreatePathComponentOnlyURI() {
		super();
	}



	/////////////////////////////////////////////

	/// REL PATH VERSION

	public void testURIFrom_PathStandardRelAndAbsPath() throws Exception
	{
		createPathAndCheckURIEquals("/", "/");
		createPathAndCheckURIEquals("/file.xml", "/file.xml");
		createPathAndCheckURIEquals(".", ".");
		createPathAndCheckURIEquals("file.xml", "file.xml");
	}
	public void testURIFrom_RelPathWithSpaceInside() throws Exception
	{
		createPathAndCheckURIEquals("file %20.xml", "file%20%2520.xml");

	}
	public void testURIFrom_RelPathWithHashSymbolInside() throws Exception
	{
		createPathAndCheckURIEquals("file #.xml", "file%20%23.xml");
	}

	public void testURIFrom_RelPathWithPercentSymbolAtBegin() throws Exception
	{
		createPathAndCheckURIEquals("%ciao%", "%25ciao%25");
	}
	public void testURIFrom_RelPathWithPercentSymbolInsideAndEnd() throws Exception
	{
		createPathAndCheckURIEquals("a%ciao%", "a%25ciao%25");
	}
	public void testURIFrom_RelPathWithOnlyAPercentSymbol() throws Exception
	{
		createPathAndCheckURIEquals("%", "%25");
	}
	public void testURIFrom_RelPathWithSpaceAndSemicolon() throws Exception
	{
		createPathAndCheckURIEquals("The ; URI", "The%20;%20URI");
	}
	public void testURIFrom_RelPath2ndLevelWithSpaceAndColon() throws Exception
	{
		createPathAndCheckURIEquals("foo/The : URI", "foo/The%20:%20URI");
	}
	public void testURIFrom_RelPathWithSpaceAndColon_SEEDOC() throws Exception
	{
		//with escaping fix no : in path first segment
		// as path should work but depends on the scheme if [:] means
		// something and so it must be transformed in "./a:b" or if means
		// the literal and so it becomes "a%2ab"
		//createPathForError("The : URI", "I am not sure if we can do this here..is scheme specific..");
		//createPathAndCheckURIEquals("The : URI", "The%20%2A%20URI");

		// 200705
		// IS OK "a%2ab", ':' is a general delimiter that can appear in path,
		// it does not have anymore a  meaning, the same happens to '@' that from
		// path are allowed in all the components.
		// Additionally the same happens for '/' and '?' for both query  and fragment,
		// they are allowed to represent data, as from:

		// 3.4 Query
		// The characters slash ("/") and question mark ("?") may represent data within the query component.
		// 3.5 Fragment
		// ..
		// The characters slash ("/") and question mark ("?") are allowed to represent data within the fragment identifier
		//  Beware that some older, erroneous implementations may not handle this data correctly when it
		// is used as the base URI for relative references (Section 5.1).

		// hm... it could be both valid :
		// 1) "The%20%2A%20URI"
		// 2) "./The%20:%20URI"

		//try this..
		//String test = createPathAndReturnURIStringWithInfoOnPath("The : URI", "[The%20%2A%20URI] or [./The%20:%20URI]");
		//assertTrue("result must be or [The%20%2A%20URI] or [./The%20:%20URI] instead is [" + test + "]",
		//	test.equals("The%20%2A%20URI") || test.equals("The%20:%20URI"));

		// no no ... is like said at begin..
		// '/' and '?' They remain reserved characters, only that can be used in some components,
		// same for ':'
		// These re not the same:
		// 1) "The%20%2A%20URI"
		// 2) "./The%20:%20URI"

		createPathForError("The : URI", "Is this scheme specific ?");


	}
	public void testURIFrom_RelPathWithAmpersand() throws Exception
	{
		createPathAndCheckURIEquals("&URI", "&URI");
	}

	public void testURIFrom_RelPathOnlyAmpersand() throws Exception
	{
		createPathAndCheckURIEquals("&", "&");
	}


	public void testURIFrom_RelPathContainsQuestionMark() throws Exception
	{
		createPathAndCheckURIEquals("a?ds", "a%3Fds");
	}
	public void testURIFrom_RelPathStartsWithQuestionMark() throws Exception
	{
		createPathAndCheckURIEquals("?ds", "%3Fds");
	}

	public void testURIFrom_RelPath_StrangeLooksLikeScheme() throws Exception
	{
		createPathAndCheckURIEquals("sc/heme://ciao", "sc/heme://ciao");
	}



	// RFC 3986
	// 3.3
	// (..) In addition, a URI reference
   	// (Section 4.1) may be a relative-path reference, in which case the
    // first path segment cannot contain a colon (":") character.


	// a rel path with colon of course
	// might be interpreted as an absolute uri with scheme
	// when starting from string representation,
	// so also component is not allowed..

	// but might be uesd escaped?

	public void testURIFrom_Invalid_RelPathOnlyColon() throws Exception
	{

		doInvalid_ColonInFirstSegment(":");
	}

	public void testURIFrom_Invalid_RelPathOneSegmentWithInsideColon() throws Exception
	{
		doInvalid_ColonInFirstSegment("a:d");
	}

	public void testURIFrom_Invalid_RelPathStartsWithColon() throws Exception
	{
		doInvalid_ColonInFirstSegment(":s/p");
	}

	public void testURIFrom_RelPath2ndSegmentHasColon() throws Exception
	{
		doInvalid_ColonInFirstSegment(":s/p");
	}

	private void doInvalid_ColonInFirstSegment(String path) throws Exception
	{
		createPathForError(path, "Create URI from unescaped path and check is invalid because is relative "
			+ " and has colon [:] in first segment [RFC3986 3.3 ]");


	}

		////////////////////



		public void testURIFrom_InvalidFixedWithDotSlashInFront_RelPathOnlyColon() throws Exception
		{

			createPathAndCheckURIEquals("./:", "./:");
		}

		public void testURIFrom_InvalidFixedWithDotSlashInFront_RelPathOneSegmentWithInsideColon() throws Exception
		{
			createPathAndCheckURIEquals("./a:d", "./a:d");
		}

		public void testURIFrom_InvalidFixedWithDotSlashInFront_RelPathStartsWithColon() throws Exception
		{
			createPathAndCheckURIEquals("./:s/p", "./:s/p");
		}

		public void testURIFrom_InvalidFixedWithDotSlashInFront_RelPath2ndSegmentHasColon() throws Exception
		{
			createPathAndCheckURIEquals("./:s/p", "./:s/p");
		}




/// ABS PATH VERSION



	public void testURIFrom_AbsPathWithSpaceInside() throws Exception
	{
		createPathAndCheckURIEquals("/file %20.xml", "/file%20%2520.xml");

	}
	public void testURIFrom_AbsPathWithHashSymbolInside() throws Exception
	{
		createPathAndCheckURIEquals("/file #.xml", "/file%20%23.xml");
	}

	public void testURIFrom_AbsPathWithPercentSymbolAtBegin() throws Exception
	{
		createPathAndCheckURIEquals("/%ciao%", "/%25ciao%25");
	}
	public void testURIFrom_AbsPathWithPercentSymbolInsideAndEnd() throws Exception
	{
		createPathAndCheckURIEquals("/a%ciao%", "/a%25ciao%25");
	}
	public void testURIFrom_AbsPathWithOnlyAPercentSymbol() throws Exception
	{
		createPathAndCheckURIEquals("/%", "/%25");
	}
	public void testURIFrom_AbsPathWithSpaceAndSemicolon() throws Exception
	{
		createPathAndCheckURIEquals("/The ; URI", "/The%20;%20URI");
	}
	public void testURIFrom_AbsPathWithAmpersand() throws Exception
	{
		createPathAndCheckURIEquals("/&URI", "/&URI");
	}

	public void testURIFrom_AbsPathOnlyAmpersand() throws Exception
	{
		createPathAndCheckURIEquals("/&", "/&");
	}

	public void testURIFrom_AbsPathOnlyColon() throws Exception
	{
		createPathAndCheckURIEquals("/:", "/:");
	}
	public void testURIFrom_AbsPathWithInsideColon() throws Exception
	{
		createPathAndCheckURIEquals("/a:d", "/a:d");
	}

	public void testURIFrom_AbsPathStartsWithColon() throws Exception
	{
		createPathAndCheckURIEquals("/:s", "/:s");
	}

	public void testURIFrom_AbsPathStartsWithQuestionMark() throws Exception
	{
		createPathAndCheckURIEquals("/?ds", "/%3Fds");
	}
	public void testURIFrom_AbsPathContainsQuestionMark() throws Exception
	{
		createPathAndCheckURIEquals("/a?ds", "/a%3Fds");
	}


	// If a URI contains an authority component, then the path component
	// must either be empty or begin with a slash ("/") character.  If a URI
	// does not contain an authority component, then the path cannot begin
    // with two slash characters ("//").

	public void testPathComponentCannotStartWithDoubleSlashIfNoAuthority3986_3_3() throws Exception
	{
		createPathForError("//pathstartswithdoubleslash",
			"Test RFC3986 3.3: if an URI does " +
			"not contain an authority component then the path cannot begin with [//]");


	}


/////////////////////////////////////////

	protected void infoCreatePathAndCheckURIEquals(String path, String uri) throws URIException
	{
		info("Create path only URI and check URI equals to expected, PATH, EXPECTED URI", new String[]{path, uri});
	}
	protected String createPathAndReturnURIStringWithInfoOnPath(String path, String uri) throws URIException
	{
		infoCreatePathAndCheckURIEquals(path, uri);
		URI uriObj = createPathURI(path);
		return uriObj.toString();
}
	protected void createPathAndCheckURIEquals(String path, String uri) throws URIException
	{
		String test = createPathAndReturnURIStringWithInfoOnPath(path, uri);
		assertEquals("createPathAndCheckURIEquals: [" + uri + "]<>[" + test + "]  from  path[" + path + "]", uri, test);
	}

	protected void createPathForError(String path, String extraMsg) throws URIException
	{
		info("Create invalid path only URI and check exception is thrown. Extra info:[" + extraMsg + "], PATH", new String[]{path});
		try
		{
			URI uriObj = createPathURI(path);

		}
		catch(URIException e)
		{
			return;
		}
		fail("Should throw URI exception:" + extraMsg);
	}





}