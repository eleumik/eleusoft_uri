package junit.org.eleusoft.uri;

import org.eleusoft.uri.Path;
import org.eleusoft.uri.URIException;

public class Test_URI10_Path extends BaseTestURI
{

	public Test_URI10_Path() {
		super();
	}


	public void testCreatePath() throws Exception
	{
		createAndCheckToStringEquals("/");
		createAndCheckToStringEquals("/abs");
		createAndCheckToStringEquals("/abs/2nd");
		createAndCheckToStringEquals("/p/f/g");
		createAndCheckToStringEquals("/p/f/g/");
		createAndCheckToStringEquals("/p");
		createAndCheckToStringEquals("a");
		createAndCheckToStringEquals("rel");

	}
	public void testWrongPath1() throws Exception
	{
		try
		{
			createPath(":");
		}
		catch(URIException e)
		{
			return;
		}
		fail("Should throw URIException");
	}

	public void testCreatePathNormalize() throws Exception
	{
		createAndCheckToStringEqualsAfterNormalize("/", "/");
		createAndCheckToStringEqualsAfterNormalize("/abs", "/abs");
		createAndCheckToStringEqualsAfterNormalize("rel", "rel");
		createAndCheckToStringEqualsAfterNormalize("/abs/2nd", "/abs/2nd");
		createAndCheckToStringEqualsAfterNormalize("/abs/../2nd", "/2nd");
		createAndCheckToStringEqualsAfterNormalize("/abs/./2nd", "/abs/2nd");
		createAndCheckToStringEqualsAfterNormalize("/abs/../", "/");
		createAndCheckToStringEqualsAfterNormalize("/abs/..", "/");

	}
	public void testPath() throws Exception
	{
		createAndCheckPathEquals("/", "/");
		createAndCheckPathEquals("/../", "/../");
		createAndCheckPathEquals("rel", "rel");
		createAndCheckPathEquals("/abs", "/abs");

	}
	public void testPath_LeaveControlDotsIfNotNormalized() throws Exception
	{
			createAndCheckPathEquals("/./p1", "/./p1");
	}
	public void testPath_SlashDot() throws Exception
	{
			createAndCheckPathEquals("/.", "/.");
	}
	public void testPath_DoubleDot() throws Exception
	{
			createAndCheckPathEquals("..", "..");
	}

	public void testPath_OnlyDot() throws Exception
	{
		createAndCheckPathEquals(".", ".");

	}

	// "normal" normalization examples
		public void testBaseNormalization() throws Exception
		{
			createAndCheckPathEqualsAfterNormalization("/", "/");
			createAndCheckPathEqualsAfterNormalization("/ciao/../pollo", "/pollo");
			createAndCheckPathEqualsAfterNormalization("rel", "rel");
			createAndCheckPathEqualsAfterNormalization("rel/pollo/../ciao/", "rel/ciao/");
			createAndCheckPathEqualsAfterNormalization("/abs", "/abs");
	}

	public void testPath_OnlyDot_Normalize_ShouldBeEmptyString() throws Exception
	{
		createAndCheckPathEqualsAfterNormalization(".", "");

	}


	public void testPath_SlashDot_Normalize_ShouldBeSlashOnly() throws Exception
	{
			createAndCheckPathEqualsAfterNormalization("/.", "/");
	}
	public void testPath_DoubleDot_Normalize_ShouldBeErrEmptyString() throws Exception
	{
			createAndCheckPathEqualsAfterNormalization("..", "");
	}
	public void testPath_SlashDoubleDotSlash_Normalize_ShouldBeErrEmptyString() throws Exception
	{
			createAndCheckPathEqualsAfterNormalization("/../", "");
	}
	public void testPath_SlashDoubleDotSlashSub_Normalize_ShouldBeErrEmptyString() throws Exception
	{
			createAndCheckPathEqualsAfterNormalization("/../sub", "");
	}
	public void testPathRel_Sub_2DoubleDots_Normalize_ShouldBeErrEmptyString() throws Exception
	{
			createAndCheckPathEqualsAfterNormalization("up/../..", "");
	}
	public void testPathRel_Sub_2DoubleDotsSlash_Normalize_ShouldBeErrEmptyString() throws Exception
	{
			createAndCheckPathEqualsAfterNormalization("up/../../", "");
	}
	public void testPath_SlashDoubleDot_Normalize_ShouldBeErrEmptyString() throws Exception
	{
			createAndCheckPathEqualsAfterNormalization("/..", "");
	}
	public void testPath_Root_Sub_2DoubleDots_Normalize_ShouldBeErrEmptyString() throws Exception
	{
			createAndCheckPathEqualsAfterNormalization("/up/../..", "");
	}
	public void testPath_Root_Sub_2DoubleDotsSlah_Normalize_ShouldBeErrEmptyString() throws Exception
	{
			createAndCheckPathEqualsAfterNormalization("/up/../../", "");
	}



	public void testDoubleDotOnlyPathAfterNormalizationEmptyStringOrError() throws Exception
	{
		createAndCheckPathEqualsAfterNormalization("..", "");
	}

	public void testDotOnlyPathAfterNormalizationEmptyString() throws Exception
	{
		createAndCheckPathEqualsAfterNormalization(".", "");
	}


	public void testResolvePath() throws Exception
	{
		resolveAndCheckPath("/", ".", "/");
		resolveAndCheckPath("/a", "b", "/b");
		resolveAndCheckPath("/a/", "b", "/a/b");
		resolveAndCheckPath("/pop", ".", "/");
		resolveAndCheckPath("/pop/", ".", "/pop/");
		resolveAndCheckPath("/pop/a2", "..", "/");
		resolveAndCheckPath("/pop/a2", "./..", "/");
		resolveAndCheckPath("/pop/a2", "./../a", "/a");
		resolveAndCheckPath("/pop/a2", "./../a/../b", "/b");

	}



	public void testResolvePath_WithBaseARelatviePath_ShouldError() throws Exception
	{

		resolvePathForError("z/dd", "b");
	}


	public void testResolvePath_AbsBaseWithColonInside() throws Exception
	{
		resolveAndCheckPath("/a:d", "b", "/b");
	}

	public void testResolvePath_AbsBaseWithColonInside2() throws Exception
	{
		resolveAndCheckPath("/a:d/", "b", "/a:d/b");
	}

	public void testContextPath() throws Exception
	{
		testContextPath("/", "/");
		testContextPath("/pollo", "/");
		testContextPath("/pol/sd", "/pol/");
		testContextPath("/pol/d.x", "/pol/");




	}

	public void testContextPathEx() throws Exception
	{

		testContextPath("/./", "/");



	}








	//////////////////////////////////////////////////////////////

	protected void resolveAndCheckPath(String uri, String rel, String expected) throws URIException
	{
		resolveAndCheckPathEx(uri, rel, expected, false);
	}
	protected void relativizeNormalizeAndCheckPath(String uri, String rel, String expected) throws URIException
	{
		resolveAndCheckPathEx(uri, rel, expected, true);
	}
	private void resolveAndCheckPathEx(String base, String rel, String expected, boolean normalize) throws URIException
	{
		info("Resolve uri.Paths {option normalize:" + normalize + "} and check result path," +
			"BASE, REL REF,EXPECTED PATH, NORMALIZE", new String[]{base, rel, expected, String.valueOf(normalize)});
		Path result = resolvePath(base, rel);
		if (normalize) result = result.normalize();
		String test = result.toString();
		assertEquals("resolveAndCheckPathEx : [" + test + "]<>[" + expected +
			"]  from  base [" + base + "] and rel uri [" + rel + "]", expected, test);
	}
	private Path resolvePath(String base, String rel) throws URIException
	{
		Path uriObj = createPath(base);
		Path relPath = createPath(rel);
		return uriObj.resolve(relPath);
	}

	private void resolvePathForError(String base, String rel) throws URIException
	{
		info("Resolve paths expecting error, BASE, REL REF", new String[]{base, rel});
		Path path = null;
		try
		{
			path = resolvePath(base, rel);
		}
		catch(URIException e)
		{
			return;
		}
		fail("Expected URIException instead path is " + path);
	}




	protected void testContextPath(String path, String expected) throws URIException
		{
			Path uriObj = createPath(path);
			String test = uriObj.getContextPath().toString();
			assertEquals("testContextPath: [" + expected + "]<>[" + test + "]  from  path[" + path + "]", expected, test);
		}

	protected void createPathAndCheckPathEquals(String path, String expected) throws URIException
	{
		Path uriObj = createPath(path);
		String test = uriObj.toString();
		assertEquals("createPathAndCheckPathEquals: [" + expected + "]<>[" + test + "]  from  path[" + path + "]", expected, test);
	}




	protected void createAndCheckToStringEquals(String path) throws URIException
	{
		createAndCheckToStringEqualsEx(path, path, true);
	}
	protected void createAndCheckToStringEqualsAfterNormalize(String path, String uriExpected) throws URIException
	{
		createAndCheckToStringEqualsEx(path, uriExpected, true);
	}
	protected void createAndCheckToStringEqualsEx(String path, String expected, boolean normalize) throws URIException
	{

		info("Create Path class and check toString equals, Unescaped Path, Expected, Normalize",
			new String[]{path, expected, String.valueOf(normalize)});


		Path uriObj = createPath(path);
		if (normalize) uriObj = uriObj.normalize();
		String test = uriObj.toString();
		assertEquals("createAndCheckToStringEqualsEx {normalize:" + normalize +
			"}  expected [" + expected + "]<>[" +test +
			"]  for path [" + path + "]", expected, test);
	}
	protected void createAndCheckPathEquals(String path, String expected) throws URIException
	{
		createAndCheckPathEqualsEx(path, expected, false);
	}
	protected void createAndCheckPathEqualsAfterNormalization(String path, String uriExpected) throws URIException
	{
		createAndCheckPathEqualsEx(path, uriExpected, true);
	}

	protected void createAndCheckPathEqualsEx(String path, String expected, boolean normalize) throws URIException
	{
		info("Create Path class and check getPath equals, Unescaped Path, Expected, Normalize",
			new String[]{path, expected, String.valueOf(normalize)});
		Path uriObj = createPath(path);
		if (normalize)uriObj = uriObj.normalize();
		String test = uriObj.getPath();
		assertEquals("createAndCheckPathEqualsEx  {normalize:" + normalize +
			"} expected [" + expected + "]<>[" + test + "] for path [" + path + "]", expected, test);
	}


}