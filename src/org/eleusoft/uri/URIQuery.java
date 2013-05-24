package org.eleusoft.uri;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;

import org.eleusoft.uri.apache.URIUtil;

public final class URIQuery
{
    public static final class Param
    {
        private final String name;
        private final String value;
        private final String decoded;
        Param(String name,
            String value, String valueDecoded)
        {
            super();
            this.name = name;
            this.value = value;
            this.decoded = valueDecoded;
        }
        public final String getName()
        {
            return name;
        }
        public final String getValue()
        {
            return value;
        }
        public String getValueDecoded()
        {
            return decoded;
        }
        
        
    }
    private final URI uri;
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
        if (separator==null) throw new IllegalArgumentException("null separator");
        if (valueSeparator==null) throw new IllegalArgumentException("null value separator");
        this.uri = uri;
        Exception exception = null;
        final String query = uri.getQuery();
        if (query!=null && query.length()!=0)
        {
            final StringTokenizer st = new StringTokenizer(query, separator);
            while (st.hasMoreElements())
            {
                String querySegment = st.nextToken();
                Param p;
                int i = querySegment.indexOf(valueSeparator);
                if (i==-1)
                {
                    p = new Param(querySegment, "", "");
                }
                else
                {
                    final String value = querySegment.substring(i+1);
                    String valueDecoded = value;
                    try
                    {
                        valueDecoded = URIUtil.decode(value);
                    }
                    catch (Exception e) {
                        // silent, mark error
                        exception = e;
                    }
                    p = new Param(querySegment.substring(0,i), value, valueDecoded);
                }
                params .addElement(p);
            }
        }
        this.exception = exception;
    }
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
     * Retrieves the first value, percent decoded,
     * or if is impossible to percent-decode the value, as found in
     * the request.
     * @param name
     * @return
     */
    public String getValue(final String name)
    {
        final Enumeration en = getParams();
        while (en.hasMoreElements())
        {
            final Param p = (Param) en.nextElement();
            if (p.getName().equals(name))
            {
                return p.getValue();
            }
        }
        return null;
        
    }
}
