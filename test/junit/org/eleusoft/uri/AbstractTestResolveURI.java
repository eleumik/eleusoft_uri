package junit.org.eleusoft.uri;

import org.eleusoft.uri.URI;
import org.eleusoft.uri.URIException;

public abstract class AbstractTestResolveURI extends BaseTestURI
{

	protected AbstractTestResolveURI() {
		super();
	}




	//////////////////////////////////////////////////////////////

	protected void resolveAndCheckURI(String uri, String rel, String expected) throws URIException
	{
		resolveAndCheckURIEx(uri, rel, expected, false);
	}
	protected void resolveNormalizeAndCheckURI(String uri, String rel, String expected) throws URIException
	{
		resolveAndCheckURIEx(uri, rel, expected, true);
	}
	private static final String RACUE = "Resolve and check uri (option normalize), URI, Relative REF, EXPECTED, NORMALIZED after resolution";
	protected void resolveAndCheckURIEx(String uri, String rel, String expected, boolean normalize) throws URIException
	{
		info(RACUE, new String[]{uri, rel, expected, String.valueOf(normalize)});

		URI result = resolveURI(uri, rel);
		if (normalize) result = result.normalize();
		String test = result.toString();
		if (!result.equals(createURI(expected)))
		{
			assertEquals("resolveAndCheckURIEx: [" + expected + "]<>[" + test +
				"]  from  uri [" + uri + "] and rel uri [" + rel + "]", expected, test);
		}
	}


	private static final String RACUERR = "Resolve two uris expecting error, URI, Relative REF";
	protected void resolveURIForError(String uri, String rel) throws URIException
	{
		info(RACUERR, new String[]{uri, rel});
		URI uriObj = null;
		try
		{
			uriObj = resolveURI(uri, rel);
		}
		catch(URIException e)
		{
			return;
		}
		fail("Should throw URIException, instead uri is :" + uriObj);
	}


	private URI resolveURI(String uri, String rel) throws URIException
	{
		URI uriObj = createURI(uri);
		URI relURI = createURI(rel);
		return uriObj.resolve(relURI);

	}

}