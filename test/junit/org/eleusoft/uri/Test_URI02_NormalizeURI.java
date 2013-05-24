package junit.org.eleusoft.uri;

import org.eleusoft.uri.URI;
import org.eleusoft.uri.URIException;

public class Test_URI02_NormalizeURI extends BaseTestURI
{

	public Test_URI02_NormalizeURI() {
		super();
	}



	//////////////////////////////////////////////////

	// BEGIN NORMALIZATION TESTS

	public void testCreateURINormalizeRelURI() throws Exception
	{
		createAndCheckToStringEqualsAfterNormalize("/", "/");
		createAndCheckToStringEqualsAfterNormalize("/abs", "/abs");
		createAndCheckToStringEqualsAfterNormalize("rel", "rel");
		createAndCheckToStringEqualsAfterNormalize("/abs/2nd", "/abs/2nd");
		createAndCheckToStringEqualsAfterNormalize("#anchor", "#anchor");
		createAndCheckToStringEqualsAfterNormalize("?q=s", "?q=s");
		createAndCheckToStringEqualsAfterNormalize("?q=s#anchor", "?q=s#anchor");

	}
	public void testCreateURINormalize_ComplexAbsPath() throws Exception
	{
		createAndCheckToStringEqualsAfterNormalize("/a/b/c/./../../g", "/a/g");
	}
	public void testCreateURINormalizeAbsURI() throws Exception
	{

		createAndCheckToStringEqualsAfterNormalize("http://www.example.org/", "http://www.example.org/");
		// authoritity no path moved to path check
		createAndCheckToStringEqualsAfterNormalize("http://www.example.org/abs", "http://www.example.org/abs");
		createAndCheckToStringEqualsAfterNormalize("http://www.example.org/abs/2nd", "http://www.example.org/abs/2nd");
	}


	public void testCreateURINormalize_EmptyURI() throws Exception
	{
		createAndCheckToStringEqualsAfterNormalize("", "");

	}
	public void testCreateURINormalize_EmptyColonOfPortNumberShouldGo_NoPath() throws Exception
	{
		createAndCheckToStringEqualsAfterNormalize("http://www.example.org:", "http://www.example.org");

	}
	public void testCreateURINormalize_EmptyColonOfPortNumberShouldGo_RootPath() throws Exception
	{
		createAndCheckToStringEqualsAfterNormalize("http://www.example.org:/", "http://www.example.org/");

	}
	public void testCreateURINormalizeAbsURI_DoubleDotInside() throws Exception
	{
		createAndCheckToStringEqualsAfterNormalize("http://www.example.org/abs/pollo/../ciao/", "http://www.example.org/abs/ciao/");
	}
	public void testCreateURINormalizeAbsURI_SingleDotInside() throws Exception
	{
		createAndCheckToStringEqualsAfterNormalize("http://www.example.org/abs/pollo/./ciao/", "http://www.example.org/abs/pollo/ciao/");

	}
	public void testCreateURINormalize_QueryTest() throws Exception
	{
		createAndCheckToStringEqualsAfterNormalize("?param=value", "?param=value");
		createAndCheckToStringEqualsAfterNormalize("?param=R%26D", "?param=R%26D");
		createAndCheckToStringEqualsAfterNormalize("/?param=R%26D", "/?param=R%26D");
	}
	public void testCreateURINormalize_Abnormal1() throws Exception
	{
		createAndCheckToStringEqualsAfterNormalize("/pollo/..?param=R%26D", "/?param=R%26D");
	}
	public void testCreateURINormalize_Escaping() throws Exception
	{
		createAndCheckToStringEqualsAfterNormalize("http://www.example.org/ci%20ao/?b=c%20a#c%20a", "http://www.example.org/ci%20ao/?b=c%20a#c%20a");
	}

	public void testNormalize_DoNotRemoveEmptyQuery_RelPath() throws Exception
	{
		createAndCheckToStringEqualsAfterNormalize("?", "?");
	}
	public void testNormalize_DoNotRemoveEmptyQuery_AbsoluteURI() throws Exception
	{
		createAndCheckToStringEqualsAfterNormalize("http://www.ccc.xxx/ddd?", "http://www.ccc.xxx/ddd?");
	}
	public void testNormalize_DoNotRemoveEmptyQuery_AbsoluteURI_WithFragment() throws Exception
	{
		createAndCheckToStringEqualsAfterNormalize("http://www.ccc.xxx/ddd?#frag", "http://www.ccc.xxx/ddd?#frag");
	}

	public void testNormalize_SchemeToLowerCase() throws Exception
	{
			// http://www.apps.ietf.org/rfc/rfc3986.html#sec-6.2.2.1
		createAndCheckToStringEqualsAfterNormalize("HTTP://www.ccc.xxx", "http://www.ccc.xxx");
	}

	public void testNormalize_HostToLowerCase() throws Exception
	{
			// http://www.apps.ietf.org/rfc/rfc3986.html#sec-6.2.2.1
		createAndCheckToStringEqualsAfterNormalize("http://www.EXAMPLE.xxx", "http://www.example.xxx");
	}


	public void testNormalize_RemoveEmptySegment() throws Exception
	{
			// not sure if legal..java uri does it..I don't see it in normalizatoin
			//

			// instead from
			// http://www.apps.ietf.org/rfc/rfc3986.html#sec-6.2.2.1
			// is clear that segments can be empty..

			// then also
			// We don't do empty-segment normalization because the equivalence of empty and
			// missing segments is specific to OS paths on certain OSes.


			// http://lists.fourthought.com/pipermail/4suite-dev/2006-June/002113.html
			// /a/b/c on posix =  /a/b/c or file:///a/b/c
			// //a/b/c on posix = //a/b/c or file:////a/b/c
			// The fact that /a/b/c and //a/b/c are equivalent on posix does not make
			// their corresponding URI refs equivalent, unless the file scheme says so
			// (it doesn't).



		createAndCheckToStringEqualsAfterNormalize("http://www.example.com/com//com///com", "http://www.example.com/com//com///com");
	}




	//////////////// CHECK PATH

	public void testNormalize_PathOnlyURI_CheckPath() throws Exception
	{
		createURIAndCheckPathEqualsAfterNormalization("/", "/");
		createURIAndCheckPathEqualsAfterNormalization("/.", "/");


		// test rel
		createURIAndCheckPathEqualsAfterNormalization("rel", "rel");
		createURIAndCheckPathEqualsAfterNormalization("rel/2nd", "rel/2nd");
		createURIAndCheckPathEqualsAfterNormalization("rel/pollo/../ciao/", "rel/ciao/");
		createURIAndCheckPathEqualsAfterNormalization("rel/pollo/./ciao/", "rel/pollo/ciao/");
		// same but abs
		createURIAndCheckPathEqualsAfterNormalization("/abs", "/abs");
		createURIAndCheckPathEqualsAfterNormalization("/abs/2nd", "/abs/2nd");
		createURIAndCheckPathEqualsAfterNormalization("/abs/pollo/../ciao/", "/abs/ciao/");
		createURIAndCheckPathEqualsAfterNormalization("/abs/pollo/./ciao/", "/abs/pollo/ciao/");
		// same but abs URI
		createURIAndCheckPathEqualsAfterNormalization("http://www.example.org/abs", "/abs");
		createURIAndCheckPathEqualsAfterNormalization("http://www.example.org/abs/2nd", "/abs/2nd");
		createURIAndCheckPathEqualsAfterNormalization("http://www.example.org/abs/pollo/../ciao/", "/abs/ciao/");
		createURIAndCheckPathEqualsAfterNormalization("http://www.example.org/abs/pollo/./ciao/", "/abs/pollo/ciao/");


	}
	public void testURINoPath_CheckPathAfterNormalization() throws Exception
	{
		createURIAndCheckPathEqualsAfterNormalization("?param=value", "");
		createURIAndCheckPathEqualsAfterNormalization("#anchor", "");
	}
	public void testPathAfterNormalization_AuthorityNoPath() throws Exception
	{
		createURIAndCheckPathEqualsAfterNormalization("http://www.example.org", "");
		createAndCheckToStringEqualsAfterNormalize("http://www.example.org", "http://www.example.org");

	}


	public void testNormalize_SlashDoubleDotOnlyAbsPath_PathShouldBeEmptyStringOrError() throws Exception
	{
		createURIAndCheckPathEqualsAfterNormalization("/..", "");
	}

	public void testNormalize_SlashDoubleDotSlashOnlyAbsPath_PathShouldBeEmptyStringOrError() throws Exception
	{
		createURIAndCheckPathEqualsAfterNormalization("/../", "");
	}
	public void testNormalize_SlashDoubleDotSlashSubAbsPath_PathShouldBeEmptyStringOrError() throws Exception
	{
		createURIAndCheckPathEqualsAfterNormalization("/../pollo", "");
	}

	public void testNormalize_SlashSubSlashDoubleDotSlashDoubleDotAbsPath_PathShouldBeEmptyStringOrError() throws Exception
	{
		createURIAndCheckPathEqualsAfterNormalization("/pollo/../..", "");
	}



	public void testNormalize_DoubleDotOnlyRelPath_PathShouldBeEmptyStringOrError() throws Exception
	{
		createURIAndCheckPathEqualsAfterNormalization("..", "");
	}

	public void testNormalize_DotOnlyRelPath_PathShouldBeEmptyString() throws Exception
	{
		createURIAndCheckPathEqualsAfterNormalization(".", "");
	}

	public void testNormalize_DotOnlyRelPathWithQuery__PathShouldBeEmptyString() throws Exception
	{
		createURIAndCheckPathEqualsAfterNormalization(".?param=value", "");
	}
	public void testNormalize_DotOnlyRelPathWithAnchor_PathShouldBeEmptyString() throws Exception
	{
		createURIAndCheckPathEqualsAfterNormalization(".#anchor", "");
	}

	//////////////////////////////

	private static final String CUACPEAN = "Parse uri and check part equals after normalization, URI, Expected Path";
	protected void createURIAndCheckPathEqualsAfterNormalization(String uri, String path) throws URIException
	{
		info(CUACPEAN, new String[]{uri, path});

		URI uriObj = createURI(uri);
		uriObj = uriObj.normalize();
		String test = uriObj.getPDPath();
		assertEquals("createURIAndCheckPathEqualsAfterNormalization: [" + path + "]<>[" + test + "] for uri [" + uri + "]", path, test);

	}




}