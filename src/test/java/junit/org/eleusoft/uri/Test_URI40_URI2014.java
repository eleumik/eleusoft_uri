package junit.org.eleusoft.uri;

import java.net.URLDecoder;
import java.net.URLEncoder;

import org.eleusoft.uri.URI;
import org.eleusoft.uri.URIException;
import org.eleusoft.uri.URIFactory;

public class Test_URI40_URI2014 extends BaseTestURI
{

    public Test_URI40_URI2014()
    {
        super();
    }

    final String stack20941947 = "xxxxx://xx730xxxxxxx005.xxxx.xxx:8443/xxx/xxxxxxxxx.xxxx#xxxxxx=xxxxxxx2&&xxxxxxxxx=1388782204000&&xxxxxxx=1388785804000&&xxxxx=xxxxxxxxxxx='xxx-xx730xxxxx10'%20xxx%20xxxxxxxx='__xxx__xxx_xxx_xxxxxxx_xxxx_xxxxxxxxx_#2_xxx-xx__xxx_xxx_xxxxxxx_______xxxx'";

    // http://stackoverflow.com/questions/20941947/java-net-uri-illegal-character-in-fragment-at-index-xxx-due-to-character
    public void testStack20941947() throws Exception
    {

        try
        {
            URI u = URIFactory.createURI(stack20941947);
            fail();
        }
        catch (URIException e)
        {
            // ..ok
        }

    }

    public void testStack20941947Frag() throws Exception
    {
        String[] f = URIFactory.extractFragment(stack20941947);
        final String fragment = f[1];
        assertEquals("xxxxxx=xxxxxxx2&&xxxxxxxxx=1388782204000&&xxxxxxx=1388785804000&&xxxxx=xxxxxxxxxxx='xxx-xx730xxxxx10'%20xxx%20xxxxxxxx='__xxx__xxx_xxx_xxxxxxx_xxxx_xxxxxxxxx_#2_xxx-xx__xxx_xxx_xxxxxxx_______xxxx'",
            fragment);
        final String frDec = URIFactory.decode(fragment);
        final String frEnc = URIFactory.encode(frDec);
        assertFalse(frEnc.equals(fragment));
        URI ur2 = URIFactory.createURI("#" + frEnc);
        String frag2 = ur2.getFragment();
        final String frDec2 = URIFactory.decode(frag2);
        assertEquals(frDec2, frDec);

    }

    public void testStack20941947Frag_URLEncoder() throws Exception
    {
        String[] f = URIFactory.extractFragment(stack20941947);
        final String fragment = f[1];
        assertEquals("xxxxxx=xxxxxxx2&&xxxxxxxxx=1388782204000&&xxxxxxx=1388785804000&&xxxxx=xxxxxxxxxxx='xxx-xx730xxxxx10'%20xxx%20xxxxxxxx='__xxx__xxx_xxx_xxxxxxx_xxxx_xxxxxxxxx_#2_xxx-xx__xxx_xxx_xxxxxxx_______xxxx'",
            fragment);
        final String frEnc = URLEncoder.encode(fragment, "UTF-8");
        assertFalse(frEnc.equals(fragment));
        URI ur2 = URIFactory.createURI("#" + frEnc);
        String frag2 = ur2.getFragment();
        final String frDec2 = URIFactory.decode(frag2);
        final String frDec = URLDecoder.decode(fragment, "UTF-8");
        assertEquals(frDec2, frDec);

    }

    public void testStack20941947Frag_AllJava() throws Exception
    {
        String[] f = URIFactory.extractFragment(stack20941947);
        final String fragment = f[1];
        final String uriNoFrag = f[0];
        assertEquals("xxxxxx=xxxxxxx2&&xxxxxxxxx=1388782204000&&xxxxxxx=1388785804000&&xxxxx=xxxxxxxxxxx='xxx-xx730xxxxx10'%20xxx%20xxxxxxxx='__xxx__xxx_xxx_xxxxxxx_xxxx_xxxxxxxxx_#2_xxx-xx__xxx_xxx_xxxxxxx_______xxxx'",
            fragment);
        // Encode fragment
        final String frEnc = URLEncoder.encode(fragment, "UTF-8");
        assertFalse(frEnc.equals(fragment));
        // Append to first part with '#'
        java.net.URI ur2 = new java.net.URI(uriNoFrag + "#" + frEnc);
        // get unescaped fragment
        String frag2 = ur2.getFragment();
        // is same as original
        assertEquals(fragment, frag2);
        // Decode both and check.. (why ? mah..)
        final String frDec2 = URIFactory.decode(frag2);
        final String frDec = URLDecoder.decode(fragment, "UTF-8");
        assertEquals(frDec2, frDec);

    }

    public void testStack20941947Frag_AllJava_DecodeFirst() throws Exception
    {
        String[] f = URIFactory.extractFragment(stack20941947);
        final String fragment = f[1];
        final String uriNoFrag = f[0];
        assertEquals("xxxxxx=xxxxxxx2&&xxxxxxxxx=1388782204000&&xxxxxxx=1388785804000&&xxxxx=xxxxxxxxxxx='xxx-xx730xxxxx10'%20xxx%20xxxxxxxx='__xxx__xxx_xxx_xxxxxxx_xxxx_xxxxxxxxx_#2_xxx-xx__xxx_xxx_xxxxxxx_______xxxx'",
            fragment);
        // Encode fragment (HERE using decoded)
        final String frDec = URLDecoder.decode(fragment, "UTF-8");
        final String frEnc = URLEncoder.encode(frDec, "UTF-8");
        assertFalse(frEnc.equals(fragment));
        // Append to first part with '#'
        java.net.URI ur2 = new java.net.URI(uriNoFrag + "#" + frEnc);
        // get unescaped fragment
        String frag2 = ur2.getFragment();
        // is same as original
        // NO because of + (from escaper) and %20 (in orig)
        // assertEquals(fragment, frag2);
        assertEquals(fragment, frag2.replaceAll("\\+", "%20"));
        // Decode both and check.. (why ? mah..)
        final String frDec2 = URIFactory.decodeURL(frag2);
        assertEquals(frDec2, frDec);

    }

    // ///////////////////////////////

    public void testPlusInFragment() throws URIException
    {
        // + in fragment is allowed, meaning is scheme
        // dependent, it could be special or not so:
        // 1) it should not be converted to space when parsing
        // 2) is scheme dep if it should be escaped or not

        { // 1) check
            URI u = createFragment("A+B");
            assertEquals("A+B", u.getPEFragment());
            // here apache fails
            assertEquals("A+B", u.getFragment());
        }
        { // 1) check with components
            URI u = URIFactory.createURI(null, null, "", null, "A+B");
            assertEquals("A+B", u.getPEFragment());
            // here apache fails
            assertEquals("A+B", u.getFragment());
        }

    }

    protected URI createFragment(String f) throws URIException
    {
        // return URIFactory.createURI(null, null, "", null, f);

        return URIFactory.createURI("#" + f);
    }

    public void testPlus() throws URIException
    {
        {
            URI u = URIFactory.createURI("http", "auth", "/A+B", "A+B", "A+B");
            // BUG ? Java 4 uri emits A+B for path, not %2B
            // assertEquals("http://auth/A%2BB?A+B#A+B", u.toString());
            assertEquals("/A+B", u.getPDPath());
            // In fragment '+' has not special meaning
            assertEquals("A+B", u.getPEFragment());
            // java 4 gives right result 'A+B'
            assertEquals("A+B", u.getFragment());
        }
        // Fragment must be escaped, "A B" is not valid
        {
            // Test without fragment is ok
            URI u1 = URIFactory.createURI("http", "auth", "/xxx", "xxx", null);
            try
            {
                // Frag with spaces not ok
                URI u = URIFactory.createURI("http",
                    "auth",
                    "/xxx",
                    "xxx",
                    "A B");
                fail();
            }
            catch (URIException e)
            {
                // incorrect fragment
            }
        }
        {
            URI u = URIFactory.createURI("http",
                "auth",
                "/A B",
                "A%20B",
                "A%20B");
            assertEquals("http://auth/A%20B?A%20B#A%20B", u.toString());
            assertEquals("/A B", u.getPDPath());
            assertEquals("A%20B", u.getPEFragment());
            assertEquals("A B", u.getFragment());

        }
    }

    public void testDecodeSpace() throws URIException
    {
        assertEquals(" ", URIFactory.decode("%20"));
    }
    public void testDecodeSpaceAsURL() throws URIException
    {
        assertEquals(" ", URIFactory.decodeURL("%20"));
    }
    
    public void testDecodePlus() throws URIException
    {
        assertEquals("+", URIFactory.decode("+"));
    }
    public void testDecodePlusAsURL() throws URIException
    {
        assertEquals(" ", URIFactory.decodeURL("+"));
    }
    

}