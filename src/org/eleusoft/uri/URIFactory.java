package org.eleusoft.uri;


/**
 * <b>Entry point</b> - This class offers static factory methods  for
 * creating an URI.
 */
public final class URIFactory
{
    private URIFactory()
    {
    }
    private static  URIProvider instance;
    static
    {
		// Not needed anymore !
        //System.setProperty("org.apache.commons.logging.Log",
        //    "org.apache.commons.logging.impl.SimpleLog");
        String clazz;
        try
        {
           clazz = System.getProperty(URIProvider.class.getName());
        }
        catch(SecurityException se)
        {
            clazz = null;
        }
        URIProvider temp;
        if (clazz!=null)
        {
            // very raw...just for junit
            try
            {
                temp = (URIProvider)Class.forName(clazz).newInstance();
                System.out.println("URIProvider loaded from System property:" + clazz);
            }
            catch(Throwable t)
            {
                t.printStackTrace();
                temp = null;
            }

        }
        else temp = null;

        if (temp==null)
        try
        {
            //temp = new  org.eleusoft.uri.java4.Java4URIProviderDefault();
            temp = new org.eleusoft.uri.apache.ApacheURIProvider();
            temp = new  org.eleusoft.uri.java4.Java4URIProvider();
            //temp = new org.eleusoft.uri.apache.ApacheURIProvider();

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        catch(Error e)
        {
            throw e;
        }
        if (temp==null)
        try
        {
            temp = new org.eleusoft.uri.apache.ApacheURIProvider();
        }
        catch(Error e2)
        {

        }
        if (temp==null) throw new RuntimeException("No URI provider available");
        instance = temp;
    }
    public static void main(String[] args)
    {
        System.out.println("---");
        System.out.println("Eleusoft URI Factory");
        System.out.println("Current provider:");
        System.out.println(instance.getClass().getName());
        System.out.println("---");
    }



    //static int i;
    private static void rand()
    {
        return;
        /**
        i++;
        if (true) return;
        if (i % 16 > 8)
            instance = new org.eleusoft.uri.apache.ApacheURIProvider();
         else
            instance = new org.eleusoft.uri.java4.Java4URIProvider();
        System.out.println(i + "provider is :" + instance.getClass().getName());
        **/
    }

    public final static boolean isAbsoluteURI(final String uri)
        throws URIException
    {
        return parseScheme(uri)[0]!=null;
    }
    /**
     * Parses an uri extracting the scheme when present.
     * This method returns an array of two strings.
     * When there is a scheme the scheme is returned
     * as first item of the array and
     * the remaining or whole part is in the second item.
     * <p>When there is no scheme the first item in the
     * array is null and the second contains the passed uri.
     * <p>This method does not check if the scheme
     * specific part respects the generi uri syntax rules.
     * @throws URIException when the passed uri starts with
     *  colon char ':'
     */
    public final static String[] parseScheme(final String uri)
        throws URIException
    {
        // scheme: ALPHA *( ALPHA / DIGIT / "+" / "-" / "." )
        if (uri.length()==0) return new String[]{null, uri};
        for(int i=0,l=uri.length();i<l;i++)
        {
            final char c = uri.charAt(i);
            if (c==':')
            {
                if (i==0) throw new URIException(
                    "uri cannot start with ':'",
                    uri);
                return new String[]{
                    uri.substring(0, i),
                    uri.substring(i+1)};
            }
            else if (!isSchemeChar(c))
            {
                return new String[]{
                    null,
                    uri};
            }
        }
        return new String[]{
                    null,
                    uri};

    }
    /**
     * Parses an uri extracting the fragment when present.
     * This method returns an array of two strings.
     * <ol>
     * <li>The first item of the array contains the passed
     * URI without the fragment component, that means
     * all the string content that preceeds the first '#' symbol
     * or all the content if there is no '#'.
     * that is not a fragment. The first item will never be null.
     * <li>The second item of the array
     * contains the fragment component, if present,
     * otherwise null; when the fragment is present
     * it might be also a zero length string.
     * <p>This method does not check if the URI
     * specific part respects the generi uri syntax rules.
     */
    public final static String[] extractFragment(final String uri)
    {
        if (uri.length()==0) return new String[]{"", null}; // static value

        for(int i=0,l=uri.length();i<l;i++)
        {
            final char c = uri.charAt(i);
            if (c=='#')
            {
                if (i==0)
                    return new String[]{
                        "",
                        uri.substring(1)}; // 1 == remove #
                else
                    return new String[]{
                        uri.substring(0, i),
                        uri.substring(i+1)}; // +1 == remove #

            }

        }
        return new String[]{
                    uri,
                    null}; // no frag

    }

    /**
     * Returns the scheme and opaque part of an opaque URI,
     * null if the URI is not opaque..MAY BE NO MUCH SENSE IN THIS..
     */
    public final static String[] extractOpaquePart(final String uri)
        throws URIException
    {
        final String[] parts = parseScheme(uri);
        final String scheme = parts[0];
        final String schemeSpecific = parts[1];
        if (scheme!=null &&
            (schemeSpecific.length()==0 || schemeSpecific.charAt(0)!='/'))
        {
            return new String[]{scheme, schemeSpecific};
        }
        else
        {
            return null;
        }
    }


    private static boolean isSchemeChar(char c)
    {
        return (c >='a' && c <= 'z') ||
            (c >= 'A' && c <= 'Z') ||
             c=='+' ||
             c=='-' ||
             c=='.';
    }
    /**
     * Same as {@link createURI(String)} but throws
     * IllegalArgumentException. It's use is intended
     * for static constants.
     * @throws IllegalArgumentException when calling
     * the {@link createURI(String)} method passing
     * the same uri as parameter throws URIException.
     */
    public final static URI uncheckedCreateURI(
        final String uri) throws IllegalArgumentException
    {
        try
        {
            return createURI(uri);
        }
        catch(URIException e)
        {
            e.printStackTrace();
            throw new IllegalArgumentException(
                "Could not create uri [" + uri + "] Msg:" + e.getMessage());
        }
    }

    /**
     * Creates an {@link URI} instance
     * from its scheme and escaped
     * scheme specific part.
     *
     * @param scheme the scheme
     * @param schemeSpecificPart the (escaped) scheme specific part
     * @throws URIException when
     *  <ul>
     *  <li>The syntax of the scheme is wrong.
     *  <li>The syntax of the scheme specific part uri is wrong.
     * </ul>
     * @return an URI instance, never <code>null</code>
     */
    public final static URI createURI(
        final String scheme, final String schemeSpecificPart) throws URIException
    {
        rand();
        return instance.createURI(
            new StringBuffer()
                .append(scheme)
                .append(':')
                .append(schemeSpecificPart)
                .toString());

    }
    /**
     * Creates an {@link URI} instance
     * from its escaped string form.
     *
     * @param escaped the escaped uri
     * @throws URIException when
     *  <ul>
     *  <li>The syntax of the escaped uri is wrong.
     * </ul>
     * @return an URI instance, never <code>null</code>
     */
    public final static URI createURI(final String escaped) throws URIException
    {
        rand();
        return instance.createURI(escaped);
    }
    /**
     * Creates an {@link URI} instance
     * from the components of an hierarchical uri .
     *
     * @param scheme the scheme component, might be null or not empty.
     *  Passing an empty string as scheme is an error and will cause an IllegalArgumentException.
     * @param auth the authority component, might be null or be non empty.
     *  Passing an empty authority must be interpreted by implementation
     *  as a null authority.
     * @param path the unescaped path
     *  If a URI contains an authority component,
     *  then the path component must either be empty
     *  or begin with a slash ("/") character. Cannot be null.
     * @param query the escaped query
     * @param fragment the un-escaped fragment
     *
     * @throws URIException when one of the components is
     * wrong for its syntax or for the context, for example
     * when the path is null or when
     *  <ul>
     *  <li>Scheme is an empty string.
     *  <li>Authority is an empty string.
     *  <li>Authority is null and path starts with "//"
     * </ul>
     * @return an URI instance, never <code>null</code>
     */
    public final static URI createURI(final String scheme, final String auth,
        final String path, final String query, final String fragment) throws URIException
    {
        rand();
        if (path==null) throw new URIException("Cannot pass a null path", null);
        return instance.createURI(scheme, auth, path, query, fragment);
    }

    /**
     * Decodes an percent-escaped string following URI rules
     * and using the passed encoding.
     * <p>Note: <code>+</code> remains <code>+</code>, while
     * following URL rules it becomes a space.
     * @throws URIException when
     *  <ul>
     *  <li>The syntax of the escaped uri is wrong.
     * </ul>
     * @return the decoded string, never <code>null</code>
     */
    public final static  String decode(final String encoded, final String encoding)
           throws URIException
    {
        try
        {
            // Was using URLUtil..now checking for char + of lookup 200609
            //String u = org.apache.util.URLUtil.URLDecode(encoded, encoding);
            // Infact URL makes + become space, URI not
            String u2 = org.eleusoft.uri.apache.URIUtil.decode(encoded, encoding);
            //if (!u.equals(u2)) throw new Error("apache.util:" + u + " - httpclient URI:" + u2);
            return u2;
        }
        catch(Exception e)
        {
            throw new URIException("Could not decode:" + encoded, encoded,e);
        }
    }
    /**
     * Decodes an percent-escaped string following URI rules
     * and using the UTF-8 encoding.
     * <p>Note: <code>+</code> remains <code>+</code>, while
     * following URL rules it becomes a space.
     * @throws URIException when
     *  <ul>
     *  <li>The syntax of the escaped uri is wrong.
     * </ul>
     * @return the decoded string, never <code>null</code>
     */
    public final static String decode(final String encoded)
           throws URIException
    {
        return decode(encoded, "UTF-8");
    }
    /**
     * Percent-escapes a string following URI rules
     * and using the passed encoding.
     * @return the percent-escaped string, never <code>null</code>
    public final static  String encode(final String decoded, final String encoding)
    {
        return org.eleusoft.uri.apache.URIUtil.encode(decoded, encoding);
    }
     */
    /**
     * Percent-escapes a string following rules for single param name or param value of a URI query component
     * and using the passed encoding.
     * @return the percent-escaped string, never <code>null</code>
     */
    public final static  String encodeQueryPart(final String decoded, final String encoding)
    {
       return org.eleusoft.uri.apache.URIUtil.encodeWithinQuery(decoded, encoding);
    }
    /**
     * Percent-escapes a string following rules for fragment of a URI query component
     * and using the passed encoding.
     * @return the percent-escaped string, never <code>null</code>
     */
    public final static  String encodeFragment(final String decoded, final String encoding)
    {
		// From RFC fragment and query are the same
       	return encodeQueryPart(decoded, encoding);
    }


    /**
     * Percent-escapes a string following URI rules
     * and using UTF-8 encoding.
     * @return the percent-escaped string, never <code>null</code>
    public final static String encode(final String decoded)
    {
        return encode(decoded, "UTF-8");
    }
 */


}
