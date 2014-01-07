package org.eleusoft.uri;

import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.Vector;


/**
 * A class to parse the parameters
 * present in the Query String 
 * of an URI, supports different separators.
 * <p>The default separator is <code>&amp;</code>
 * and the default value-separator is <code>=</code>,
 * the default encoding is <code>UTF-8</code>.
 * 
 * @author mik
 *
 */
public final class URIQuery
{
    /**
     * Value object that represents
     * a parameter of the query string and its value.
     * @author mik
     *
     */
    public static final class Param
    {
        private final String name;
        private final String value;
        private final String decoded;
        private final String nameDecoded;
        Param(String name, String nameDecoded,
            String value, String valueDecoded)
        {
            super();
            this.nameDecoded = nameDecoded;
            this.name = name;
            this.value = value;
            this.decoded = valueDecoded;
        }
        /**
         * Returns the percent escaped
         * name of the parameter
         * @return
         */
        public final String getName()
        {
            return name;
        }
        /**
         * Returns the percent escaped
         * value of the parameter
         * @return
         */
        public final String getValue()
        {
            return value;
        }
        public String getValueDecoded()
        {
            return decoded;
        }
        public String getNameDecoded()
        {
            return nameDecoded;
        }
        
        
    }
    //private final URI uri;
    private final Vector params = new Vector();
    private final Exception exception;
    
    
    /**
     * 
     * @param uri
     * @throws URIException 
     */
    public URIQuery(String uri) throws URIException
    {
        this(URIFactory.createURI(uri));
    }

    public URIQuery(URI uri)
    {
        this(uri, "&", "=");
    }
    public URIQuery(URI uri, String separator, String valueSeparator)
    {
        this(uri, separator, valueSeparator, "UTF-8");
    }
    /**
     * Most complete constructor, specifies 
     * separators and encoding.
     * @param uri A {@link URI}, never <code>null</code>.
     * @param separator A {@link String}, never <code>null</code>.
     * @param valueSeparator A {@link String}, never <code>null</code>.
     * @param charset A {@link String} or <code>null</code>.
     */
    public URIQuery(URI uri, String separator, String valueSeparator, String encoding)
    {
        if (separator==null) throw new IllegalArgumentException("null separator");
        if (valueSeparator==null) throw new IllegalArgumentException("null value separator");
        if (encoding==null) throw new IllegalArgumentException("null encoding");
        //this.uri = uri;
        Exception exception = null;
        final String query = uri.getQuery();
        if (query!=null && query.length()!=0)
        {
            final StringTokenizer st = new StringTokenizer(query, separator);
            while (st.hasMoreElements())
            {
                final String querySegment = st.nextToken();
                final Param p;
                final int i = querySegment.indexOf(valueSeparator);
                if (i==-1)
                {
                    String nameDecoded = querySegment;
                    try
                    {
                        nameDecoded = URIFactory.decode(querySegment, encoding);
                    }
                    catch (Exception e) {
                        // silent, mark error
                        exception = e;
                    }
                    
                    p = new Param(querySegment,  nameDecoded, "", "");
                }
                else
                {
                    final String value = querySegment.substring(i+1);
                    final String name = querySegment.substring(0,i);
                    String valueDecoded = value;
                    String nameDecoded = name;
                    try
                    {
                        valueDecoded = URIFactory.decode(value, encoding );
                    }
                    catch (Exception e) {
                        // silent, mark error
                        exception = e;
                    }
                    try
                    {
                        nameDecoded = URIFactory.decode(name, encoding);
                    }
                    catch (Exception e) {
                        // silent, mark error
                        exception = e;
                    }
                    p = new Param(name, nameDecoded, value, valueDecoded);
                }
                params .addElement(p);
            }
        }
        this.exception = exception;
    }
    
    /**
     * The URI passed in constructor
     * or constructed from the string passed
     * in constructor, may include other components
     * than the query string.
     * @return A {@link URI}, never <code>null</code>.
     */
    // Removed since it may remove the possibility to load unparsable URI(s)
//    public URI getUri()
//    {
//        return uri;
//    }
    /**
     * Retrieves the last, if any, exception
     * catched parsing values. Values are parsed
     * all also if one is invalid, the invalid param
     * gets the invalid value not percent decoded.
     */
    public Exception getException()
    {
        return exception;
    }
    /**
     * Retrieves an enumeration of {@link Param}
     * @return A {@link Enumeration}, never <code>null</code>.
     */
    public Enumeration getParams()
    {
        return params.elements();
    }
    /**
     * Retrieves the first value of the parameter
     * with decoded name as passed, the value is returned percent decoded,
     * or if is impossible to percent-decode the value, as found in
     * the request.
     * @param name A {@link String}, never <code>null</code>.
     * @return A {@link String} or <code>null</code>.
     */
    public String getParameterValue(final String name)
    {
        final Enumeration en = getParams();
        while (en.hasMoreElements())
        {
            final Param p = (Param) en.nextElement();
            if (p.getNameDecoded().equals(name))
            {
                return p.getValueDecoded();
            }
        }
        return null;
        
    }
    /**
     * Static helper to perform a very common
     * operation: get a single parameter value
     * in a single shot from a string URI.
     * @param uri A {@link String}, never <code>null</code>.
     * @param name A {@link String}, never <code>null</code>.
     * @return A {@link String} or <code>null</code>.
     * @throws URIException
     */
    public static String getParameterValue(String uri, String name) throws URIException
    {
        return new URIQuery(uri).getParameterValue(name);
    }
}
