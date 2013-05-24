package junit.org.eleusoft.uri;

import org.eleusoft.uri.URI;
import org.eleusoft.uri.URIException;

public class Test_URI03_CreateURIFromComponents extends BaseTestURI
{

	public Test_URI03_CreateURIFromComponents() {
		super();
	}


	public void testCreateFromComp_Default() throws Exception
	{
		createAndCheckComponents("http", "www.example.com", "", null, null);
		createAndCheckComponents("http", "www.example.com", "/", null, null);
		createAndCheckComponents("http", "www.example.com", "/", "", null);
		createAndCheckComponents("http", "www.example.com", "", "", null);
		createAndCheckComponents("http", "www.example.com", "/", "", "");
		createAndCheckComponents("http", "www.example.com", "/", null, "");
		createAndCheckComponents("http", "www.example.com", "", "", "");
		createAndCheckComponents("http", "www.example.com", "", null, "");
	}
	public void testCreateFromComp_OnlyPathEmpty() throws Exception
	{
			createAndCheckComponents(null, null, "mypath", null, null);
	}
	public void testCreateFromComp_OnlyPathEmpty_Tricky() throws Exception
	{
			createAndCheckComponents(null, null, "/m:/?;[]{}()", null, null);
	}
	public void testCreateFromComp_PathEmpty_Authority() throws Exception
	{
			createAndCheckComponents(null, "myauth", "", null, null);
	}

	public void testCreateFromComp_PathEmpty_Fragment() throws Exception
	{
			createAndCheckComponents(null, null, "", null, "myfrag");
	}
	public void testCreateFromComp_PathEmpty_FragmentWithSpace() throws Exception
	{
			createAndCheckComponents(null, null, "", null, "myfrag%20ment");
	}

	public void testCreateFromComp_PathEmpty_Fragment_Tricky() throws Exception
	{
			createAndCheckComponents(null, null, "", null, "my%23fr?a/g@:.ment");
	}

	public void testCreateFromComp_PathEmpty_Query() throws Exception
	{
			createAndCheckComponents(null, null, "", "myquery=d", null);
	}

	public void testCreateFromComp_PathEmpty_Query_Tricky() throws Exception
	{
			createAndCheckComponents(null, null, "", "myquery=R%26D", null);
	}
	public void testCreateFromComp_AuthorityEmptyAndPathBeginsWithSlashSlash_ShouldBeOK() throws Exception
	{
			createAndCheckComponents(null, "", "//net", null, null);

	}
	public void testCreateFromComp_SchemeCanContainPlus() throws Exception
	{
		createAndCheckComponents("s+1", "b", "", "" ,"");
	}
	public void testCreateFromComp_SchemeCanContainMinus() throws Exception
	{
		createAndCheckComponents("s-1", "b", "", "", "");
	}
	public void testCreateFromComp_SchemeCanContainDot() throws Exception
	{
		createAndCheckComponents("s.1", "b", "", "", "");
	}

	////////////////////// ERROR FROM COMPONENTS

	//When authority is present, the path must either be empty or begin with a slash ("/") character.
	//When authority is not present, the path cannot begin with two slash characters ("//").

	public void testCreateFromComp_PathCannotBeNull_CaseAllNull() throws Exception
	{
			createFromComponentsForError("Path cannot be null, all components are null", null, null, null, null, null);
	}
	public void testCreateFromComp_PathCannotBeNull_CasePassedAuthority() throws Exception
	{
			createFromComponentsForError("Path cannot be null, auth is present", null, "mik2", null, null, null);
	}
	public void testCreateFromComp_PathCannotBeNull_CasePassedAuthorityAndScheme() throws Exception
	{
			createFromComponentsForError("Path cannot be null, auth and scheme is present", "http", "mik2", null, null, null);
	}
	public void testCreateFromComp_AuthorityNullAndPathBeginsWithSlashSlash_ShouldBeError() throws Exception
	{
			createFromComponentsForError("When auth is null path cannot begin with //", null, null, "//net", null, null);

	}
	public void testCreateFromCompForError_AuthPresent_PathNotBeginWithSlash() throws Exception
	{
			createFromComponentsForError("When authority is present, the path must either be " +
				"empty or begin with a slash character.",
				"s", "aut", "wrongnoslash", null, null);
	}

	public void testCreateFromCompForError_AuthNotPresent_PathNotBeginWithTwoSlash() throws Exception
	{
			createFromComponentsForError("When authority is not present, the path must not begin " +
				"with two slash characters.",
				null, null, "//wrong-TWO-slash", null, null);
	}

	public void testCreateFromCompForError_AuthNotPresent_PathNotBeginWithTwoSlash_HasScheme() throws Exception
	{
			createFromComponentsForError("When authority is not present, the path must not begin " +
				"with two slash characters.",
				"s", null, "//wrong-TWO-slash", null, null);
	}

	public void testCreateFromCompForError_SchemeCannotStartWithNumber() throws Exception
	{
			createFromComponentsForError("Scheme cannot start with number", "1s", "", "", "", "");
	}
	public void testCreateFromCompForError_Query_EscapedQueryContainsPercent() throws Exception
	{
			createFromComponentsForError("EscapedQueryContainsPercent",
				null, null, null, "%", null);
	}

	//////////////////
	// Create From Components And Check URI
	///////////////////

	public void testCreateFromCompCheckURI_UnreservedCharacters_MustNeverBeEscaped() throws Exception
	{
			// This is a test for unreserved chars, that must not be escaped.
			createFromComponentsAndCheckURI("SCH-EME://AUT-._~/PA-._~TH?QU-._~ERY#FRA-._~GMENT",
				"SCH-EME", "AUT-._~", "/PA-._~TH", "QU-._~ERY", "FRA-._~GMENT");
	}

	public void testCreateFromCompCheckURI_ReservedCharacters_Authority_MustAlwaysBeEscaped() throws Exception
	{
			// Test for reserved delimiters ":" / "/" / "?" / "#" / "[" / "]" / "@"

			// : / ? # [ ] @


			// authority
			createFromComponentsAndCheckURI("SCHEME://AUTOB%55HN@pollo.com/",
				"SCHEME", "AUTOB%55HN@pollo", "/", null, null);
	}


	public void testCreateFromCompCheckURI_ReservedCharacters_Path_MustAlwaysBeEscaped() throws Exception
	{
			// Test for reserved delimiters ":" / "/" / "?" / "#" / "[" / "]" / "@"

			// in path are not reserved @ and :
			createFromComponentsAndCheckURI("SCHEME://AUT/PA%3FTH/%3F%23%5Ban%5D",
				"SCHEME", "AUT", "/PA?TH/?#[an]", null, null);
	}

	public void testCreateFromCompCheckURI_ReservedCharacters_PathNotReservedChars_MustNotBeEscaped() throws Exception
	{
			// Test for unreserved delimiters for path '@' ':'

			// in path are not reserved @ and :
			createFromComponentsAndCheckURI("SCHEME://AUT/PA@:@:s",
				"SCHEME", "AUT", "/PA@:@:s", null, null);
	}


	// http://www.apps.ietf.org/rfc/rfc3986.html#sec-2.2
	// sub-delims
	// "!" / "$" / "&" / "'" / "(" / ")" / "*" / "+" / "," / ";" / "="




	///////////////////////



	private static final String CACCE = "Create uri from unescaped components to generate an error, Scheme, Auth, Path, Query, Fragment";

	protected void createFromComponentsForError(String msg, String s, String a, String p, String q, String f) throws URIException
	{
		info(CACCE, new String[]{s,a,p,q,f});
		try
		{

			URI u = createURI(s, a, p, q, f);
		}
		catch(URIException e)
		{
			return;
		}
		catch(Exception e)
		{
			fail("Should throw URIException instead throwed " + e.getClass().getName() + " Msg:" + e.getMessage());
			return;
		}
		fail("Should throw URIException:" + msg);
	}

	private static final String CACC = "Create uri from unescaped components and check they have the passed value, Scheme, Auth, Path, Query, Fragment";

	private static final String CACCURI = "Create uri from unescaped components and check the result URI, Expected URI,Scheme, Auth, Path, Query, Fragment";

	protected void createFromComponentsAndCheckURI(String uri, String s, String a, String p, String q, String f) throws URIException
	{
		info(CACCURI, new String[]{uri, s,a,p,q,f});


		URI u = createURI(s, a, p, q, f);
		assertEquals("URI is different:" + u.toString(), uri, u.toString());
	}

	protected void createAndCheckComponents(String s, String a, String p, String q, String f) throws URIException
	{
		info(CACC, new String[]{s,a,p,q,f});


		URI u = createURI(s, a, p, q, f);
		assertEqualsX("scheme", s, u.getScheme());
		assertEqualsX("authority", a, u.getAuthority());
		assertEqualsX("path", p, u.getPDPath());
		assertEqualsX("query", q, u.getQuery());
		assertEqualsX("fragment", f, u.getPEFragment());



	}




}