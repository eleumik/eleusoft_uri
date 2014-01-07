package junit.org.eleusoft.uri;

import org.eleusoft.uri.URI;
import org.eleusoft.uri.URIException;
import org.eleusoft.uri.URIFactory;

public final class Test_URI08_Query extends BaseTestURI
{

	public Test_URI08_Query() {
		super();
	}


	public void testCreateURIFromQueryComponent_CheckURI() throws Exception
	{
		createQueryAndCheckURIEquals("value=RD", "?value=RD");
	}

	public void testCreateURIFromQueryComponent_OneValueContainsAmpersand_CheckURI() throws Exception
	{
		// query semantics is lost...
		createQueryAndCheckURIEquals("value=R%26D", "?value=R%26D");
	}
	public void testCreateURIFromQueryComponent_OneValueContainsHash_CheckURI() throws Exception
	{
		// query semantics is lost...
		createQueryAndCheckURIEquals("value=R%23D", "?value=R%23D");
	}
	public void testCreateURIWithQuery_OneValueContainsAmpersand_checkQuery() throws Exception
	{
		// query semantics is lost...
		createAndCheckQueryEquals("http://www.aaa.bbb/?value=R%26D", "value=R%26D");
	}

	public void testCreateURIWithQuery_OneValueContainsHash_checkQuery() throws Exception
	{
		// query semantics is lost...
		createAndCheckQueryEquals("http://www.aaa.bbb/?value=R%23D", "value=R%23D");
	}

	public void testCreateURIWithQuery_OneValueContainsAmpersand_checkQuery_2() throws Exception
	{
		// query semantics is lost...
		createAndCheckQueryEquals("http://www.aaa.bbb/?value=R%26D&pop=3", "value=R%26D&pop=3");
	}

	public void testCreateURIWithQuery_OneValueContainsAmpersand_checkToString() throws Exception
	{
		// query semantics is lost...
		createAndCheckToStringEquals("http://www.aaa.bbb/?value=R%26D");
	}

	public void testCreateURIWithQuery_OneValueContainsAmpersand_checkToString_2 () throws Exception
	{
		// query semantics is lost...
		createAndCheckToStringEquals("http://www.aaa.bbb/?value=R%26D&pop=3");
	}

	////////////

	private final URI createQueryURI(String query) throws URIException
	{
		return URIFactory.createURI(null, null, "", query, null);
	}

	protected void createQueryAndCheckURIEquals(String query, String uri) throws URIException
	{
		info("Create empty path and query URI and check toString equals, Query, Expected URI",
				new String[]{query, uri});

		URI uriObj = createQueryURI(query);
		String test = uriObj.toString();
		assertEquals("createQueryAndCheckURIEquals: [" + uri+ "]<>[" + test  + "]  from  query[" + query + "]", uri, test);
	}

	protected void createAndCheckQueryEquals(String uri, String queryExpected) throws URIException
	{
		info("Create URI and check query equals expected, URI, Expected Query",
				new String[]{uri, queryExpected});

		URI uriObj = createURI(uri);
		String test = uriObj.getQuery();
		assertEquals("createAndCheckQueryEquals: [" + queryExpected+ "]<>[" + test
			+ "]  for uri [" + uri + "]", queryExpected, test);
	}




}