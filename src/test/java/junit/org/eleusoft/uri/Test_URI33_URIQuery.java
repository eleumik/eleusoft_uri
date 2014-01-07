package junit.org.eleusoft.uri;

import java.util.Enumeration;

import org.eleusoft.uri.URI;
import org.eleusoft.uri.URIException;
import org.eleusoft.uri.URIQuery;
import org.eleusoft.uri.URIQuery.Param;

public class Test_URI33_URIQuery extends BaseTestURI
{

	public Test_URI33_URIQuery() {
		super();
	}

	public void testParse_WithValue() throws Exception
    {
        Enumeration e = doT("?a=34");
        URIQuery.Param p = (Param) e.nextElement();
        assertEquals("a", p.getName());
        assertEquals("34", p.getValue());
        assertFalse(e.hasMoreElements());
    }
	public void testParse_WithValueEscaped() throws Exception
    {
        Enumeration e = doT("?a=%20");
        URIQuery.Param p = (Param) e.nextElement();
        assertEquals("a", p.getName());
        assertEquals("%20", p.getValue());
        assertEquals(" ", p.getValueDecoded());
        assertFalse(e.hasMoreElements());
    }
	public void testParse_WithValueWRONGLYEscaped() throws Exception
    {
        Enumeration e = doT("?a=%2%0");
        URIQuery.Param p = (Param) e.nextElement();
        assertEquals("a", p.getName());
        assertEquals("%2%0", p.getValue());
        assertEquals("%2%0", p.getValueDecoded());
        assertFalse(e.hasMoreElements());
    }

	public void testParse_NoValue() throws Exception
	{
	  	Enumeration e = doT("?a");
	  	URIQuery.Param p = (Param) e.nextElement();
	  	assertEquals("a", p.getName());
	  	assertEquals("", p.getValue());
        assertFalse(e.hasMoreElements());
	}
	public void testParse_TwoValues() throws Exception
    {
        Enumeration e = doT("?a=34&b=23");
        URIQuery.Param p = (Param) e.nextElement();
        assertEquals("a", p.getName());
        assertEquals("34", p.getValue());
        p = (Param) e.nextElement();
        assertEquals("b", p.getName());
        assertEquals("23", p.getValue());
        assertFalse(e.hasMoreElements());
    }

    private Enumeration doT(String uri) throws URIException
    {
        URIQuery q = new URIQuery(uri);
        return q.getParams();
    }



}