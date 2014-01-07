package junit.org.eleusoft.uri;


public class Test_URI01_ParseInvalidURI extends BaseTestURI
{

	public Test_URI01_ParseInvalidURI() {
		super();
	}


	public void testParseInvalid_EmptyScheme() throws Exception
	{
		parseURIForError("://example.com/examples");
	}

	public void testParseInvalid78() throws Exception
	{
		parseURIForError("http://foo.org:80Path/More");
	}
	public void testParseInvalid81() throws Exception
	{
		parseURIForError("::");
	}

	public void testParseInvalid83() throws Exception
	{
		parseURIForError("%");
	}

	public void testParseInvalid84() throws Exception
	{
		parseURIForError("A%Z");
	}

	public void testParseInvalid85() throws Exception
	{
		parseURIForError("%ZZ");
	}

	public void testParseInvalid86() throws Exception
	{
		parseURIForError("%AZ");
	}

	public void testParseInvalid87() throws Exception
	{
		parseURIForError("A C");
	}

	public void testParseInvalid90() throws Exception
		{
			parseURIForError("\"A\\C\"\"\"");
		}


	public void testParseInvalid90_MV() throws Exception // to check only \
	{
		parseURIForError("A\\C");
	}



	public void testParseInvalid91() throws Exception
	{
		parseURIForError("A`C");
	}

	public void testParseInvalid92() throws Exception
	{
		parseURIForError("A<C");
	}

	public void testParseInvalid93() throws Exception
	{
		parseURIForError("A>C");
	}

	public void testParseInvalid94() throws Exception
	{
		parseURIForError("A^C");
	}

	public void testParseInvalid95() throws Exception
	{
		parseURIForError("A\\\\C");
	}

	public void testParseInvalid96() throws Exception
	{
		parseURIForError("A{C");
	}

	public void testParseInvalid97() throws Exception
	{
		parseURIForError("A|C");
	}

	public void testParseInvalid98() throws Exception
	{
		parseURIForError("A}C");
	}

	/**
	 Syntax101,uri:InvRf,A[C,,,Disallowed use of '[' amd ']'
	 	Syntax102,uri:InvRf,A]C,,,
	 	Syntax103,uri:InvRf,A[**]C,,,
	 	Syntax104,uri:InvRf,http://[xyz]/,,,
	 	Syntax105,uri:InvRf,http://]/,,,
	 	Syntax106,uri:InvRf,http://example.org/[2010:836B:4179::836B:4179],,,
	 	Syntax107,uri:InvRf,http://example.org/abc#[2010:836B:4179::836B:4179],,,
	 	Syntax108,uri:InvRf,http://example.org/xxx/[qwerty]#a[b],,,

	**/
	public void testParseInvalid101() throws Exception
	{
		parseURIForError("A[C");
	}

	public void testParseInvalid102() throws Exception
	{
		parseURIForError("A]C");
	}

	public void testParseInvalid103() throws Exception
	{
		parseURIForError("A[**]C");
	}

	public void testParseInvalid104() throws Exception
	{
		parseURIForError("http://[xyx]/");
	}

	public void testParseInvalid105() throws Exception
	{
		parseURIForError("http://]/");
	}

	public void testParseInvalid106() throws Exception
	{
		parseURIForError("http://example.org/[2010:836B:4179::836B:4179]");
	}

	// Not  VALID because  the '[' is a gen-delim and
	// since not all gen-delims are in fargment ('[' is not) it must be escaped
	public void testParseInvalid107() throws Exception
	{
		parseURIForError("http://example.org/abc#[2010:836B:4179::836B:4179]");
	}
	public void testParseInvalid107B() throws Exception
	{
		parseURIForError("http://example.org/abc#YEAR{2010}");
	}

	public void testParseInvalid108() throws Exception
	{
		parseURIForError("http://example.org/xxx/[qwerty]#a[b]");
	}

	/*
	Syntax078,uri:InvRf,http://foo.org:80Path/More,,,Not valid URIrefs
	Syntax081,uri:InvRf,::,,,
	Syntax082,uri:InvRf, ,,,Empty
	Syntax083,uri:InvRf,%,,,
	Syntax084,uri:InvRf,A%Z,,,
	Syntax085,uri:InvRf,%ZZ,,,
	Syntax086,uri:InvRf,%AZ,,,
	Syntax087,uri:InvRf,A C,,,
	Syntax090,uri:InvRf,"A\C""",,,
	Syntax091,uri:InvRf,A`C,,,
	Syntax092,uri:InvRf,A<C,,,
	Syntax093,uri:InvRf,A>C,,,
	Syntax094,uri:InvRf,A^C,,,
	Syntax095,uri:InvRf,A\\C,,,
	Syntax096,uri:InvRf,A{C,,,
	Syntax097,uri:InvRf,A|C,,,
	Syntax098,uri:InvRf,A}C,,,

	Syntax101,uri:InvRf,A[C,,,Disallowed use of '[' amd ']'
	Syntax102,uri:InvRf,A]C,,,
	Syntax103,uri:InvRf,A[**]C,,,
	Syntax104,uri:InvRf,http://[xyz]/,,,
	Syntax105,uri:InvRf,http://]/,,,
	Syntax106,uri:InvRf,http://example.org/[2010:836B:4179::836B:4179],,,
	Syntax107,uri:InvRf,http://example.org/abc#[2010:836B:4179::836B:4179],,,
	Syntax108,uri:InvRf,http://example.org/xxx/[qwerty]#a[b],,,

	Syntax111,uri:AbsRf,http://example/Andr&#567;,,,Random other things,,,,,,,,,
	Syntax112,uri:AbsId,file:///C:/DEV/Haskell/lib/HXmlToolbox-3.01/examples/,,,,,,,,,,,,
	Syntax113,uri:AbsId,http://46229EFFE16A9BD60B9F1BE88B2DB047ADDED785/demo.mp3,,,,,,,,,,,,
	Syntax114,uri:InvRf,http://example.org/xxx/qwerty#a#b,,,

*/

		//  why is this not valid ??200705..

	public void testParseInvalid113() throws Exception
	{
		parseURIForError("http://46229EFFE16A9BD60B9F1BE88B2DB047ADDED785/demo.mp3");
	}
	public void testParseInvalid112B() throws Exception
	{
		parseURIForError("c:\\dev\\tools");
	}

	/** MV. THESE TWO ARE OK
	public void testParseInvalid111() throws Exception
	{
		parseURIForError("http://example/Andr&#567;");
	}
	**/

	public void testParseInvalid111_IsActuallyValid_MV() throws Exception
	{
		createAndCheckToStringEquals("http://example/Andr&#567;");
	}
/////////////////////////TEST THAT I THINK ARE NOT INVALID URIs

	/**
	public void testParseInvalid112() throws Exception
	{
		parseURIForError("file:///C:/DEV/Haskell/lib/HXmlToolbox-3.01/examples/");
	}
	**/

	public void testParseInvalid112_IsActuallyValid_MV() throws Exception
	{
		createAndCheckToStringEquals("file:///C:/DEV/Haskell/lib/HXmlToolbox-3.01/examples/");
	}


	// This is invalid because  '#' is not allowed in fragment

	public void testParseInvalid114() throws Exception
	{
		parseURIForError("http://example.org/xxx/qwerty#a#b");
	}






}