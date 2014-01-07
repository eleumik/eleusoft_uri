package junit.org.eleusoft.uri;

import java.io.InputStream;

import junit.framework.TestCase;
import junit.framework.TestResult;

import org.eleusoft.uri.Path;
import org.eleusoft.uri.URI;
import org.eleusoft.uri.URIException;
import org.eleusoft.uri.URIFactory;

/**
 *  Base abstract class for URI tests.
 * To run with different providers
 * the System.property <code>org.eleusoft.uri.URIProvider=org.eleusoft.uri.java4.Java4URIProvider</code>
 * can be used.
 * <p>Windows example":
 * <pre>
 * SET PROPS=-Dorg.eleusoft.uri.URIProvider=org.eleusoft.uri.java4.Java4URIProvider
 * java %PROPS% -classpath [my claspath] junit.swingui.TestRunner junit.org.eleusoft.uri.uriAnchorsMapuite
 *</pre>
 */
public abstract class BaseTestURI extends TestCase
{


	public BaseTestURI() {
		super();
	}




	// URI - PATH FACTORY


	protected final URI createURI(String uri) throws URIException
	{
		return URIFactory.createURI(uri);
	}

	protected final URI createURI(String s, String a, String p, String q, String f) throws URIException
	{
		return URIFactory.createURI(s, a, p, q, f);
	}

	protected Path createPath(String path) throws URIException
	{
		Path uriObj = new Path(path);
		return uriObj;
	}

	protected URI createPathURI(String path) throws URIException
	{
		return URIFactory.createURI(null, null, path, null, null);
	}


	/////////////TESTING HELPERS

	protected void resolveAndCheckURI(String uri, String rel, String expected) throws URIException
	{
		resolveAndCheckURIEx(uri, rel, expected, false);
	}
	protected void resolveNormalizeAndCheckURI(String uri, String rel, String expected) throws URIException
	{
		resolveAndCheckURIEx(uri, rel, expected, true);
	}

	private static final String RACUE = "Resolve and check uri object,URI,REL,RESULT,NORMALIZE";
	private void resolveAndCheckURIEx(String uri, String rel, String expected, boolean normalize) throws URIException
	{
		info(RACUE, new String[]{uri, rel, expected, String.valueOf(normalize)});

		URI uriObj = createURI(uri);
		URI relURI = createURI(rel);
		URI result = uriObj.resolve(relURI);
		if (normalize) result = result.normalize();
		String test = result.toString();
		if (!result.equals(createURI(expected)))
		{
			assertEquals("resolveAndCheckURIEx: [" + expected + "]<>[" + test +
				"]  from  uri [" + uri + "] and rel uri [" + rel + "]", expected, test);
		}
	}

	private static final String CACTSE = "Parse and check toString is the same,URI";
	private static final String CACTSE_PASSED = "Parse and check toString is the same as passed URI";

	protected void createAndCheckToStringEqualsExpected(String expected, String uri) throws URIException
	{
		info(CACTSE_PASSED, new String[]{uri,expected});
		assertEquals(expected, createURI(uri).toString());

	}
	protected void createAndCheckToStringEquals(String uri) throws URIException
	{
		info(CACTSE, new String[]{uri});
		assertEquals(uri, createURI(uri).toString());

	}




	private static final String CUFE = "Parse URI expecting an error, URI";
	protected void parseURIForError(String uri) throws URIException
	{
		info(CUFE, new String[]{uri});

		URI u;
		try
		{
			u = createURI(uri);
		}
		catch(URIException e)
		{
			return;
		}
		String dump = "\nscheme:[" + u.getScheme()
			+ "] authority:[" + u.getAuthority()
			+ "] pe-path:[" + u.getPEPath() + "] pe-query:[" + u.getQuery()
			+ "] fragment:[" +  u.getFragment()
			+ "]\ntoString:" + u.toString();
		fail("Should throw URIException instead created uri, dump:" + dump);


	}



	private static final String CACTSEAN = "Parse and check toString after normalize,URI,EXPECTED";
	protected void createAndCheckToStringEqualsAfterNormalize(String uri, String uriExpected) throws URIException
	{

		info(CACTSEAN, new String[]{uri, uriExpected});

		URI uriObj = createURI(uri).normalize();
		String test = uriObj.toString();
		assertEquals("createAndCheckToStringEqualsAfterNormalize: [" + uriExpected+ "]<>[" + test
			+ "]  for uri [" + uri + "]", uriExpected, test);

	}

	static final String PUCC = "Parse uri and check components,URI,SCHEME,AUTH,PATH,QUERY,FRAGMENT" ;

	protected void parseURIAndCheckComponents(boolean doToStringTest, String uri, String s, String a, String p, String q, String f) throws URIException
	{
		info(PUCC, new String[]{uri, s, a, p, q, f});

		URI u = createURI(uri);

		if (doToStringTest) assertEqualsX(uri + " URI.toString", uri, u.toString()); //200705

		assertEqualsX(uri + " scheme", s, u.getScheme());
		assertEqualsX(uri + " authority", a, u.getAuthority());
		assertEqualsX(uri + " path", p, u.getPEPath());
		assertEqualsX(uri + " query", q, u.getQuery());
		assertEqualsX(uri + " fragment", f, u.getPEFragment());

	}

	protected void parseURIAndCheckComponents(String uri, String s, String a, String p, String q, String f) throws URIException
	{
			parseURIAndCheckComponents(true, uri, s,a,p,q,f);
	}

	protected void assertEqualsX(String d, String e, String v)
	{
		assertEquals(d + "\nexpected:" + formatValue(e) + "\nfound:" + formatValue(v) + "\n", e,v);
	}

	private String formatValue(String s)
	{
		if (s==null) return null;
		int len = s.length();
		if (len < 40) return s;
		else
		{
			return s.substring(0,40) + "...(omitted)...";
		}
	}
	/////////// I/O

	protected InputStream getInputStreamFromFileName(String name)
	{
		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("junit/org/eleusoft/uri/" + name);
		return is;

	}



	//////////////// REPORTING

	/////////// Test METHODS

	public synchronized void run(TestResult tr)
	{
		getXReport().count++;
		try
		{
			tr.addListener(getXReport());
			System.out.println("--" + getXReport().count);
			super.run(tr);
		}
		finally
		{
			tr.removeListener(getXReport());
		}

	}

	/// Test Reporting
	protected final void info(String name, String[] params)
	{
		getXReport().info(name, params);
	}

	static XReport xrep;
	static XReport getXReport()
	{
		if ( xrep == null ) xrep = new XReport();
		return xrep;
	}






}