package junit.org.eleusoft.uri;

import java.util.Enumeration;

import org.eleusoft.uri.URI;
import org.eleusoft.uri.URIException;
import org.eleusoft.uri.URIFactory;
import org.eleusoft.uri.util.URIQuery;
import org.eleusoft.uri.util.URIQuery.Param;

public class Test_URI33_URIQuery extends BaseTestURI
{

    public Test_URI33_URIQuery()
    {
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

    public void testParse_WithValueDollarInName() throws Exception
    {
        final String u = "?$a=34";
        Enumeration e = doT(u);
        URIQuery.Param p = (Param) e.nextElement();
        assertEquals("$a", p.getName());
        assertEquals("34", p.getValue());
        assertFalse(e.hasMoreElements());
    }
    public void testParse_WithValuePlusInValueMustBecomeSpace() throws Exception
    {
        final String u = "?$a=3+4";
        Enumeration e = doT(u);
        URIQuery.Param p = (Param) e.nextElement();
        assertEquals("$a", p.getName());
        assertEquals("3+4", p.getValue());
        assertEquals("3 4", p.getValueDecoded());
        assertFalse(e.hasMoreElements());
    }

    public void testParse_WithValueDollarInName_GetValue() throws Exception
    {

        final String u = "?$a=34";
        URIQuery q = new URIQuery(u);
        assertEquals("34", q.getParameterValue("$a"));
        assertNull(q.getParameterValue("%24a"));

    }

    public void testParse_WithValueDollarInNameEscaped() throws Exception
    {
        final String u = "?%24a=34";
        Enumeration e = doT(u);
        URIQuery.Param p = (Param) e.nextElement();
        assertEquals("%24a", p.getName());
        assertEquals("$a", p.getNameDecoded());
        assertEquals("34", p.getValue());
        assertFalse(e.hasMoreElements());
    }

    public void testParse_WithValueDollarInNameEscaped_GetValue() throws Exception
    {
        final String u = "?%24a=34";
        URIQuery q = new URIQuery(u);
        assertEquals("34", q.getParameterValue("$a"));
        assertNull(q.getParameterValue("%24a"));

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

    public void testParse_WithValueEscaped_GetValue() throws Exception
    {
        URIQuery q = new URIQuery("?a=%20");
        assertEquals(" ", q.getParameterValue("a"));
    }

    public void testParse_WithNameEscaped() throws Exception
    {
        final String u = "?%20=x";
        Enumeration e = doT(u);
        URIQuery.Param p = (Param) e.nextElement();
        assertEquals("%20", p.getName());
        assertEquals("x", p.getValue());
        assertEquals("x", p.getValueDecoded());
        assertEquals(" ", p.getNameDecoded());
        assertFalse(e.hasMoreElements());

        URIQuery q = new URIQuery(u);
        assertEquals("x", q.getParameterValue(" "));
        assertNull(q.getParameterValue("%20"));
    }

    public void testParse_WithValueWRONGLYEscaped() throws Exception
    {
        // 201401 this fails, could not fail when using manual parsing..but is
        // useful? no also because now there is getURI ? 
        // so remove getURI (if one day want to support unparsable URI)
        // and here make ok to fail
        try
        {
            Enumeration e = doT("?a=%2%0");
            fail();
        }
        catch(URIException e)
        {
            // ok
        }
        // oLD:
//        URIQuery.Param p = (Param) e.nextElement();
//        assertEquals("a", p.getName());
//        assertEquals("%2%0", p.getValue());
//        assertEquals("%2%0", p.getValueDecoded());
//        assertFalse(e.hasMoreElements());
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

    public void testRemoveFragKeepQueryOk() throws Exception
    {
        URI u = URIFactory.createURI("editSocio.do?id=65861&back=%2flistContacts.do%3f#Note");
        URI u2 = u.removeFragment();
        assertEquals("id=65861&back=%2flistContacts.do%3f", u2.getQuery());

    }

}