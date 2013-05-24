package junit.org.eleusoft.uri;


public class Test_URI04_ResolveURI extends AbstractTestResolveURI
{

	public Test_URI04_ResolveURI() {
		super();
	}



	public void testResolvePath() throws Exception
	{
		resolveAndCheckURI("/", ".", "/");
		resolveAndCheckURI("/pop", ".", "/");
		resolveAndCheckURI("/pop/", ".", "/pop/");
		resolveAndCheckURI("/pop/a2", "..", "/");
		resolveAndCheckURI("/pop/a2", "./..", "/");
		resolveAndCheckURI("/pop/a2", "./../", "/");
		resolveAndCheckURI("/pop/a/a2", "..", "/pop/");
		resolveAndCheckURI("/pop/a/a2", "./..", "/pop/");
		resolveAndCheckURI("/pop/a/a2", "./../", "/pop/");
		resolveAndCheckURI("/pop/a2", "./../a", "/a");
		resolveAndCheckURI("/pop/a2", "./../a/../b", "/b");
		resolveAndCheckURI("/pop/a2", "./../a/../b/", "/b/");

	}

	public void testResolveCheckURI_AbsoluteWithAuthorityWithDoubleSlashPath() throws Exception
	{
		resolveAndCheckURI("foo://aa//b/c", "../d", "foo://aa//d");


	}



	public void testResolveCheckURI_AbsoluteWithAuthorityEmptyPathAddNetPath() throws Exception
	{
		resolveAndCheckURI("foo://aa", "//nn", "foo://nn");


	}

	public void testResolveCheckURI_AbsoluteWithAuthorityEmptyPathAddAbsPath() throws Exception
	{
		resolveAndCheckURI("foo://aa", "/nn", "foo://aa/nn");
	}

	public void testResolveCheckURI_AbsoluteWithAuthorityEmptyPathAddAbsPathWithEmptySegments() throws Exception
	{
		resolveAndCheckURI("foo://aa", "/nn///a", "foo://aa/nn///a");
	}

	public void testResolveCheckURI_AbsoluteWithAuthorityEmptyPathAddRelPath_Section5_2_3() throws Exception
	{
		resolveAndCheckURI("foo://aa","c", "foo://aa/c"); // if there is aut an no path return '/' + rel path
	}

	public void testResolveCheckURI_AbsoluteWithAuthorityEmptyPathAddRelPathWithEmptySegms_NotSure() throws Exception
	{
		resolveAndCheckURI("foo://aa","c//d", "foo://aa/c//d"); //not sure/
	}

	public void testResolveCheckURI_AbsoluteWithAuthorityEmptyPathAddNetPathWithDotInFront() throws Exception
	{
		resolveAndCheckURI("foo://aa",".//nn", "foo://aa/nn"); // aa/nn or aa//nn ?

	}
	/**
	 *
	 	if (R.path starts-with "/") then
		  T.path = remove_dot_segments(R.path);
	   else
		  T.path = merge(Base.path, R.path);
		  T.path = remove_dot_segments(T.path);
	   endif;

	//5.2.3 Merge Paths
	//If the base URI has a defined authority component and an empty path,
	//then return a string consisting of "/" concatenated with the reference's path; otherwise

*/
	public void testResolveCheckURI_AuthorityNoPath_ShouldBecomeRoot_RFCSection5_2_3() throws Exception
	{
		resolveAndCheckURI("foo://aa", "b", "foo://aa/b");

	}

	public void testResolveCheckURI_AbsoluteWithAuthorityWithDoubleSlashPath2() throws Exception
	{
		resolveAndCheckURI("foo://aa//b/c", "../../d", "foo://aa/d");

	}

	public void testResolveCheckURI_AbsoluteWithAuthorityWithDoubleSlashPath_Abnormal() throws Exception
	{
		resolveAndCheckURI("foo://aa//b/c", "../../../d", "foo://aa");

	}

	public void testResolveNormCheckURI_TryToResolveWithBaseRelativePath_ShouldError() throws Exception
	{
		resolveURIForError("ciao", "/");
	}



	public void testResolveCheckURI_AbsoluteURINoPath_AddRootPath() throws Exception
	{
		resolveAndCheckURI("http://www.example.org", "/", "http://www.example.org/");
	}
	public void testResolveCheckURI_AbsoluteURINoPath_AddDot() throws Exception
	{
		resolveAndCheckURI("http://www.example.org", ".", "http://www.example.org/");
	}
	public void testResolveCheckURI_AbsoluteURINoPath_AddRelPath() throws Exception
	{
		resolveAndCheckURI("http://www.example.org", "index.html", "http://www.example.org/index.html");
	}

	public void testResolveNormCheckURI_AbsoluteURINoPath_AddEmptyString_MAYBE_ShouldBeRootInstead() throws Exception
	{
		//hmm...may be root ?
		resolveAndCheckURI("http://www.example.org", "", "http://www.example.org");
	}

	////////////////////// DOWN HERE ALSO NORMALIZE


	public void testResolveNormCheckURI_AbsoluteURIRoot_AddQuery() throws Exception
	{
		resolveNormalizeAndCheckURI("http://www.example.org/", "?a=c%20b", "http://www.example.org/?a=c%20b");
	}

	public void testResolveNormCheckURI_AbsoluteURIRoot_AddQueryFragment() throws Exception
	{
		resolveNormalizeAndCheckURI("http://www.example.org/", "?a=cb#f%5fd", "http://www.example.org/?a=cb#f%5fd");
	}


	public void testResolveNormCheckURI_AbsoluteURIRoot_AddRelPathQueryFragment() throws Exception
	{
		resolveNormalizeAndCheckURI("http://www.example.org/", "s?a=cb#f%5fd", "http://www.example.org/s?a=cb#f%5fd");
	}


	public void testResolveNormCheckURI_AbsoluteURIRoot_AddAbsPathQueryFragment() throws Exception
	{
		resolveNormalizeAndCheckURI("http://www.example.org/", "/s?a=cb#f%5fd", "http://www.example.org/s?a=cb#f%5fd");
	}


	public void testResolveNormCheckURI_AbsoluteURIWithAllComponents_AddEmptyURI_FragmentMustGo() throws Exception
	{
		resolveNormalizeAndCheckURI("http://www.example.org/a?p=q#f", "", "http://www.example.org/a?p=q");
	}
	public void testResolveNormCheckURI_AbsoluteURIRoot_AddRootPath() throws Exception
	{
		resolveNormalizeAndCheckURI("http://www.example.org/", "/", "http://www.example.org/");
	}
	public void testResolveNormCheckURI_AbsoluteURIRoot_AddRootSubPath() throws Exception
	{
		resolveNormalizeAndCheckURI("http://www.example.org/", "/sub", "http://www.example.org/sub");
	}
	public void testResolveNormCheckURI_AbsoluteURIRoot_AddRelath() throws Exception
	{
		resolveNormalizeAndCheckURI("http://www.example.org/", "sub", "http://www.example.org/sub");
	}
	public void testResolveNormCheckURI_AbsoluteURIRootWithDot_AddRootPath() throws Exception
	{
		resolveNormalizeAndCheckURI("http://www.example.org/.", "/", "http://www.example.org/");
	}
	public void testResolveNormCheckURI_AbsoluteURIRootWithDot_AddRootSubPath() throws Exception
	{
		resolveNormalizeAndCheckURI("http://www.example.org/.", "/b", "http://www.example.org/b");
	}
	public void testResolveNormCheckURI_AbsoluteURIRootWithDot_AddRelPathFragment() throws Exception
	{
		// here the rel has root path and fragment
		resolveAndCheckURI("http://www.example.org/.", "p#fragment", "http://www.example.org/p#fragment");
	}
	public void testResolveNormCheckURI_AbsoluteURIRootWithDot_AddAbsPathFragment() throws Exception
	{
			// here the rel has root path and fragment
		resolveAndCheckURI("http://www.example.org/.", "/p#fragment", "http://www.example.org/p#fragment");
	}
	public void testResolveNormCheckURI_AbsoluteURIRootWithDot_AddRootPathFragment() throws Exception
	{
			// here the rel has root path and fragment
		resolveAndCheckURI("http://www.example.org/.", "/#fragment", "http://www.example.org/#fragment");
	}
	public void testResolveNormCheckURI_AbsoluteURIAddFragment() throws Exception
	{
			// here the rel has only fragment
		resolveNormalizeAndCheckURI("http://www.example.org/.", "#fragment", "http://www.example.org/#fragment");
	}
	public void testResolveNormCheckURI_AbsoluteURIAddFragmentWithSpace() throws Exception
	{
			// here the rel has only fragment
		resolveNormalizeAndCheckURI("http://www.example.org/", "#fra%20gment", "http://www.example.org/#fra%20gment");
	}
	public void testResolveNormCheckURI_AbsoluteURIAddFragmentWithDot() throws Exception
	{
			// here the rel has only fragment
		resolveNormalizeAndCheckURI("http://www.example.org/", "#frag/./ment", "http://www.example.org/#frag/./ment");
	}
	public void testResolveNormCheckURI_AbsoluteURIAddFragmentWithDoubleDot() throws Exception
	{
			// here the rel has only fragment
		resolveNormalizeAndCheckURI("http://www.example.org/", "#frag/../ment", "http://www.example.org/#frag/../ment");
	}
	public void testResolveNormCheckURI_AbsoluteURIRootSubPathAddEmptyFragment() throws Exception
	{
			// here the rel has only fragment
		resolveNormalizeAndCheckURI("http://www.example.org/pollo", "#fragment", "http://www.example.org/pollo#fragment");
	}
	public void testResolveNormCheckURI_AbsoluteURINoPathAddFragment_PathShouldStayEmpty() throws Exception
	{
		resolveNormalizeAndCheckURI("http://www.example.org", "#fragment", "http://www.example.org#fragment");

	}
	public void testResolveNormCheckURI_AbsoluteURIEmptyQueryShouldNOTDisappear() throws Exception
	{
		resolveNormalizeAndCheckURI("http://www.example.org?", "#fragment", "http://www.example.org?#fragment");

	}

	public void testResolveNormCheckURI_AbsoluteURIPathsWithColonInside() throws Exception
	{
		resolveNormalizeAndCheckURI("http://www.example.org/a:b/c/", "../c:d/e", "http://www.example.org/a:b/c:d/e");

	}

	public void testResolveNormCheckURI_AbsoluteURI_BaseURIFragmentMustDisappear() throws Exception
	{
		// Appendix D.2 Restored the behavior of [RFC1808] where, if the reference contains an empty path
		// and a defined query component, the target URI inherits the base URI's path component.
		resolveAndCheckURI("http://www.example.org/pollo#fff", "?q=r", "http://www.example.org/pollo?q=r");
	}





	//////////// MAUVE gnu.testlet.java.net.URI TESTS from http://sources.redhat.com/ml/mauve-patches/2005/txt00006.txt

	private static final String BASE_URI = "http://www.dcs.shef.ac.uk/com4280/";
	private static final String RELATIVE_URI = "special/../special/../artistdac1.html?id=32";
	private static final String CORRECT_URI = "http://www.dcs.shef.ac.uk/com4280/artistdac1.html?id=32";

	public void testGnuTestlet_Resolve() throws Exception
	{
		resolveNormalizeAndCheckURI(BASE_URI, RELATIVE_URI, CORRECT_URI);
	}
	public void testGnuTestlet_Normalize() throws Exception
	{
		createAndCheckToStringEqualsAfterNormalize(BASE_URI + RELATIVE_URI, CORRECT_URI);
	}


	private static final String TEST_URI_1 = "http://example.com/money/\uFFE5/file.html";


	public void testGnuTestlet_ToString() throws Exception
	{
		// normalize just because there is no test to check different from parsed..
		createAndCheckToStringEqualsAfterNormalize(TEST_URI_1, "http://example.com/money/%EF%BF%A5/file.html");
	}





}