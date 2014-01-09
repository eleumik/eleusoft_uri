package org.eleusoft.uri;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * <b>Entry point</b> - This class offers static factory methods for creating an
 * URI.
 * 
 * @author Michele Vivoda
 */
public final class URIFactory
{
    private static final String UTF_8 = "UTF-8";
    public static void main(String[] args)
    {
        System.out.println(org.eleusoft.uri.URIFactory.class.getName() + ", provider is " + instance);
    } 
    
    private static final URIProvider instance;
    private static final URICodec codec;
    static
    {
        final String[] defaultProviders = new String[]{
          "org.eleusoft.uri.apache.ApacheURIProvider",
            "org.eleusoft.uri.java4.Java4URIProvider"};
        final String[] defaultCodecs = new String[]{
            "org.eleusoft.uri.apache.ApacheURICodec"};
        
        // providers.add("org.eleusoft.uri.java4.Java4URIProviderDefault");
        
        final ServiceLoader sl = new ServiceLoader();
        

        instance = (URIProvider) sl.getService(URIProvider.class, defaultProviders);

        codec = (URICodec) sl.getService(URICodec.class, defaultCodecs);
        // see URIFactoryTest
    }

    private URIFactory()
    {
    }
    
    static class ServiceLoader
    {

        public Object getService(Class serviceClass, String[] defaultProviders)
        {
            final ArrayList providers = new ArrayList();

            // Not needed anymore !
            // System.setProperty("org.apache.commons.logging.Log",
            // "org.apache.commons.logging.impl.SimpleLog");
            try
            {
                // for junit
                final String clazz = System.getProperty(serviceClass.getName());
                if (clazz != null) providers.add(clazz);
            }
            catch (SecurityException se)
            {
                // ignore
            }

            final InputStream is = URIFactory.class.getResourceAsStream("/META-INF/services/" + serviceClass.getName());
            if (is != null)
            {
                BufferedReader br = null;
                try
                {
                    br = new BufferedReader(new InputStreamReader(is));
                    String line = br.readLine();
                    while (line != null)
                    {
                        String tl = line.trim();
                        if (tl.length() == 0 || tl.startsWith("#"))
                        {
                            // skip comment or empty
                        }
                        else
                        {
                            providers.add(tl);
                        }
                        line = br.readLine();
                    }
                }
                catch (IOException e)
                {
                    // ignore
                    e.printStackTrace();
                }
                finally
                {
                    try
                    {
                        if (br != null)
                            br.close();
                        else
                            is.close();
                    }
                    catch (IOException e)
                    {
                        // ignore.
                    }
                }
            }
            for(int i=0,len=defaultProviders.length;i<len;i++)
            {
                providers.add(defaultProviders[i]);
            }
            
            final Iterator i = providers.iterator();
            Object temp = null;
            while (temp == null && i.hasNext())
            {
                String provider = (String) i.next();
                try
                {
                    temp = Class.forName(provider).newInstance();
                    if (serviceClass.isAssignableFrom(temp.getClass())==false)
                        throw new ClassCastException("Not a " + serviceClass + " but " + temp.getClass());
                    break;
                }
                catch (Throwable t)
                {
                    t.printStackTrace();

                }
            }

            if (temp == null)
                throw new RuntimeException("No URI provider available");

            return temp;
        }
    }

    


    public final static boolean isAbsoluteURI(final String uri) throws URIException
    {
        return parseScheme(uri)[0] != null;
    }

    /**
     * Parses an uri extracting the scheme when present. This method returns an
     * array of two strings. When there is a scheme the scheme is returned as
     * first item of the array and the remaining or whole part is in the second
     * item.
     * <p>
     * When there is no scheme the first item in the array is null and the
     * second contains the passed uri.
     * <p>
     * This method does not check if the scheme specific part respects the
     * generic uri syntax rules.
     * 
     * @throws URIException when the passed uri starts with colon char ':'
     */
    public final static String[] parseScheme(final String uri) throws URIException
    {
        // scheme: ALPHA *( ALPHA / DIGIT / "+" / "-" / "." )
        if (uri.length() == 0) return new String[] { null, uri };
        for (int i = 0, l = uri.length(); i < l; i++)
        {
            final char c = uri.charAt(i);
            if (c == ':')
            {
                if (i == 0)
                    throw new URIException("uri cannot start with ':'", uri);
                return new String[] { uri.substring(0, i), uri.substring(i + 1) };
            }
            else if (!isSchemeChar(c)) { return new String[] { null, uri }; }
        }
        return new String[] { null, uri };

    }

    /**
     * Parses an uri extracting the fragment when present. This method returns
     * an array of two strings.
     * <ol>
     * <li>The first item of the array contains the passed URI without the
     * fragment component, that means all the string content that preceeds the
     * first '#' symbol or all the content if there is no '#'. that is not a
     * fragment. The first item will never be null.
     * <li>The second item of the array contains the fragment component, if
     * present, otherwise null; when the fragment is present it might be also a
     * zero length string.
     * <p>
     * This method does not check if the URI specific part respects the generic
     * uri syntax rules.
     */
    public final static String[] extractFragment(final String uri)
    {
        if (uri.length() == 0) return new String[] { "", null }; // static value

        for (int i = 0, l = uri.length(); i < l; i++)
        {
            final char c = uri.charAt(i);
            if (c == '#')
            {
                if (i == 0)
                    return new String[] { "", uri.substring(1) }; // 1 == remove
                                                                  // #
                else
                    return new String[] { uri.substring(0, i),
                        uri.substring(i + 1) }; // +1 == remove #

            }

        }
        return new String[] { uri, null }; // no frag

    }

    /**
     * Returns the scheme and opaque part of an opaque URI, null if the URI is
     * not opaque..MAY BE NO MUCH SENSE IN THIS..
     */
    public final static String[] extractOpaquePart(final String uri) throws URIException
    {
        final String[] parts = parseScheme(uri);
        final String scheme = parts[0];
        final String schemeSpecific = parts[1];
        if (scheme != null && (schemeSpecific.length() == 0 || schemeSpecific.charAt(0) != '/'))
        {
            return new String[] { scheme, schemeSpecific };
        }
        else
        {
            return null;
        }
    }

    private static boolean isSchemeChar(char c)
    {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')
            || c == '+'
            || c == '-'
            || c == '.';
    }

    /**
     * Same as {@link createURI(String)} but throws IllegalArgumentException.
     * It's use is intended for static constants.
     * 
     * @throws IllegalArgumentException when calling the {@link
     *             createURI(String)} method passing the same uri as parameter
     *             throws URIException.
     */
    public final static URI uncheckedCreateURI(final String uri) throws IllegalArgumentException
    {
        try
        {
            return createURI(uri);
        }
        catch (URIException e)
        {
            e.printStackTrace();
            throw new IllegalArgumentException("Could not create uri [" + uri
                + "] Msg:"
                + e.getMessage());
        }
    }

    /**
     * Creates an {@link URI} instance from its scheme and escaped scheme
     * specific part.
     * 
     * @param scheme the scheme
     * @param schemeSpecificPart the (escaped) scheme specific part
     * @throws URIException when
     *             <ul>
     *             <li>The syntax of the scheme is wrong.
     *             <li>The syntax of the scheme specific part uri is wrong.
     *             </ul>
     * @return an URI instance, never <code>null</code>
     */
    public final static URI createURI(final String scheme,
        final String schemeSpecificPart) throws URIException
    {
        return instance.createURI(new StringBuffer().append(scheme)
            .append(':')
            .append(schemeSpecificPart)
            .toString());

    }

    /**
     * Creates an {@link URI} instance from its escaped string form.
     * 
     * @param escaped the escaped uri
     * @throws URIException when
     *             <ul>
     *             <li>The syntax of the escaped uri is wrong.
     *             </ul>
     * @return an URI instance, never <code>null</code>
     */
    public final static URI createURI(final String escaped) throws URIException
    {
        return instance.createURI(escaped);
    }

    /**
     * Creates an {@link URI} instance from the components of an hierarchical
     * uri. 
     * 
     * @param scheme the scheme component, might be null or not empty. Passing
     *            an empty string as scheme is an error and will cause an
     *            IllegalArgumentException.
     * @param auth the authority component, might be null or be non empty.
     *            Passing an empty authority must be interpreted by
     *            implementation as a null authority.
     * @param path the <em>unescaped</em> path. If a URI contains an authority component,
     *            then the path component must either be empty or begin with a
     *            slash ("/") character. The <b>path can never be null</b>.
     * @param query the percent escaped query
     * @param fragment the percent escaped fragment
     * 
     * @throws URIException when one of the components is wrong for its syntax
     *             or for the context, for example when the path is null or when
     *             <ul>
     *             <li>Scheme is an empty string.
     *             <li>Authority is an empty string.
     *             <li>Authority is null and path starts with "//"
     *             </ul>
     * @return an URI instance, never <code>null</code>
     */
    public final static URI createURI(final String scheme,
        final String auth,
        final String path,
        final String query,
        final String fragment) throws URIException
    {
        if (path == null)
            throw new URIException("Cannot pass a null path", null);
        return instance.createURI(scheme, auth, path, query, fragment);
    }

    /**
     * Decodes an percent-escaped string following URI rules and using the
     * passed encoding.
     * <p>
     * Note: <code>+</code> remains <code>+</code>, while following URL rules it
     * becomes a space.
     * 
     * @throws URIException when
     *             <ul>
     *             <li>The syntax of the escaped uri is wrong.
     *             </ul>
     * @return the decoded string, never <code>null</code>
     */
    public final static String decode(final String encoded,
        final String encoding) throws URIException
    {
        try
        {
            // Was using URLUtil..now checking for char + of lookup 200609
            // String u = org.apache.util.URLUtil.URLDecode(encoded, encoding);
            // Indeed URL makes + become space, URI not
            final String u2 = codec.decode(encoded,
                encoding);
            // if (!u.equals(u2)) throw new Error("apache.util:" + u +
            // " - httpclient URI:" + u2);
            return u2;
        }
        catch (Exception e)
        {
            throw new URIException("Could not URI-decode:" + encoded, encoded, e);
        }
    }

    /**
     * Decodes an percent-escaped string following URL rules and using the
     * passed encoding.
     * <p>
     * Note: '<code>+</code>' becomes space '<code> </code>', while following URI rules it
     * would stay plus '<code>+</code>'.
     * 
     * @throws URIException when
     *             <ul>
     *             <li>The syntax of the escaped uri is wrong.
     *             </ul>
     * @return the decoded string, never <code>null</code>
     */
    public final static String decodeURL(final String encoded,
        final String encoding) throws URIException
    {
        try
        {
            final String u2 = codec.decodeURL(encoded,
                encoding);
            return u2;
        }
        catch (Exception e)
        {
            throw new URIException("Could not URL-decode:" + encoded, encoded, e);
        }
    }
    public final static String decodeURL(final String encoded) throws URIException
    {
        return decodeURL(encoded, UTF_8);
    }
    /**
     * Decodes an percent-escaped string following URI rules and using the UTF-8
     * encoding.
     * <p>
     * Note: <code>+</code> remains <code>+</code>, while following URL rules it
     * becomes a space.
     * 
     * @throws URIException when
     *             <ul>
     *             <li>The syntax of the escaped uri is wrong.
     *             </ul>
     * @return the decoded string, never <code>null</code>
     */
    public final static String decode(final String encoded) throws URIException
    {
        return decode(encoded, UTF_8);
    }

    
    /**
     * Percent-escapes a string following URI rules and using the passed encoding.
     * 
     * @return the percent-escaped string, never <code>null</code>
     */
    public final static String encode(final String decoded,
        final String encoding)
    {
        return codec.encode(decoded, encoding);
    }
    /**
     * Like {@link #encode(String, String)} passing UTF-8 as encoding.
     * @param decoded A {@link String}, never <code>null</code>.
     * @return A {@link String}, never <code>null</code>.
     */
    public final static String encode(final String decoded)
    {
        return encode(decoded, UTF_8);
    }

    

    

}
