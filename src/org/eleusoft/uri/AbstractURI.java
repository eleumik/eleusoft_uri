package org.eleusoft.uri;

/**
 * Helper for implementing the {@link URI} interface.
 */
public abstract class AbstractURI implements URI, Comparable
{
    private String scheme;
    private String auth;
    private String path;
    private String fragment;
    private String query;
    private String toString;
    private boolean isOpaque;
    private int hashCode;
    
    private boolean strict = false;
    
   
    protected final void init(String scheme, String auth, 
        String path, String query, String fragment, String toString)
    {
        if (path==null) throw new IllegalStateException("Implementation error 8322482382 - passes null path");
        this.scheme = scheme==null ? null : scheme.toLowerCase();
        this.auth = auth;
        this.path = path;
        this.query = query;
        this.fragment = fragment;
        this.toString = toString;
        this.isOpaque = false;
        this.hashCode = toString.hashCode();
        //if (getPEFragment()!=null && getPEFragment().indexOf(' ')!=-1) throw new Error(toString + " ---- :??? " + getPEFragment());
    }
    protected static String removeContextDelimiters(String uri) throws URIException
    {
        if (uri==null) throw new IllegalArgumentException("Null uri passed");
        uri = uri.trim();
        if (uri.length()>2)
        {
            char first = uri.charAt(0);
            if (isOpenContextDelimiter(first))
            {
                final int ilast = uri.length() - 1;
                char clast = uri.charAt(ilast);
                if (!isCloseContextDelimiter(clast, first))
                    throw new URIException("First character is a context delimiter [" + 
                        first + "] but last not: [" + clast + "]", uri);
                return uri.substring(1, ilast); 
            }    
        }
        return uri;
    } 
    private static final boolean isOpenContextDelimiter(char c)
    {
        return c=='"' || c=='<';
    }
    private static final boolean isCloseContextDelimiter(char c, char o)
    {
        if (o=='"') return c == '"';
        else return  c=='>';
    }
    
    public String getPDPath()
    {
        return getPath();
    }
    
    public boolean isPathEmpty()
    {
        String p = getPath();
        return p==null || p.length()==0;
    }
    public boolean isPathRootless()
    {
        String p = getPath();
        return p!=null && p.length()!=0 &&
            p.charAt(0)!='/';
    }
    
    protected final boolean isStrict()
    {
        return strict;
    }
    public final int  hashCode()
    {
        return hashCode;
    }
    protected final String getNonEmptyStringOrNull(final String s)
    {
        // #NOTE: To Clean empty query 
        return (s!=null && s.trim().length()==0) ? null : s;
        
    }
    protected final void initOpaque(String scheme, String schemeSpecificPart, 
        String fragment, String toString)
    {
        this.scheme = scheme;
        this.fragment = fragment;
        this.path = schemeSpecificPart;
        this.toString = toString;
        isOpaque = true;
        this.hashCode = toString.hashCode();
    }
    
    public boolean isRelativePath()
    {
        return !isAbsolute() &&
            (path==null
             || path.length()==0
             || path.charAt(0)!='/');
    }
    /*
     * final..scheme cannot be empty string
     * and getScheme is also final.
     */
    public final boolean isAbsolute()
    {
        return scheme!=null;
    }
    public boolean isOpaque()
    {
        return isOpaque;
    }
    public String getName()
    {
        if (path==null) return null;
        int i = path.lastIndexOf('/');
        if (i==-1) return path;
        else return path.substring(i+1, path.length());
    }
    public final String getScheme()
    {
        return scheme;
    }
    
    public String getSchemeSpecificPart()
    {
        return isOpaque ? path : null;
    }
    
    public String getAuthority() //temp x apache
    {
        return auth;
    }
    public final String getPath()
    {//added temp...
        return path;
    }
    public final String getQuery()
    {
        return query;
    }
    public final boolean hasQuery()
    {
        return query != null;
    }
    
    public final String getFragment()
    {
        return fragment;
    }
    public final boolean hasFragment()
    {
        return fragment!=null;
    }
    
    public final String toString()
    {
        return toString;
    }
    
    public java.net.URL toURL() throws java.net.MalformedURLException
    {
        // important, use the accessor, 
        // not the member to allow other impls
        return new java.net.URL(toString());
    }

    final public URI normalize() throws URIException
    {
        if (isOpaque()) return this;
        else return _normalize();
    }
    
    abstract protected URI _normalize() throws URIException;
        
    final public URI resolve(URI relative) throws URIException
    {
        //todo missing case opaque but rel has frag only..
        // 200705 that means charAt(0)=='#'
        if (relative.isAbsolute()) return relative;
        else if (isOpaque()) return this;
        else return _resolve(relative);
    }
    
    /**
     * Relativizes the given URI against this URI. 
     * The relativization of the given URI against 
     * this URI is computed as follows: 
     * <ol><li>
     * If either this URI or the given URI are opaque, 
     * or if the scheme and authority components 
     * of the two URIs are not identical, 
     * or if the path of this URI is not a prefix 
     * of the path of the given URI, then the given URI is returned. 
     * <li>Otherwise a new relative hierarchical 
     * URI is constructed with query and fragment 
     * components taken from the given URI 
     * and with a path component computed by 
     * removing this URI's path from the 
     * beginning of the given URI's path. 
     * </ol>
     */
    public URI relativize(URI uri) throws URIException
    {
        if (isOpaque() 
            || uri.isOpaque() 
            || !schemeEquals(uri, this)
            || !authorityEquals(uri, this)) 
            return uri;
        
        
        /** Is not so easy..leave to impl..
        final String pathThis = getPath();
        final String pathPassed = uri.getPath();
        
        if (pathPassed==null
            || !pathPassed.startsWith(pathThis) )
            return uri;
        **/ 
        
        return _relativize(uri);
        
    }
    
    /**
     * Returns whether the passed URI instances
     * have the same scheme, as follows:
     * <ol>
     * <li>If one of the uri has <em>absent</em> scheme also the 
     * other must have <em>absent</em> scheme.
     * <li>Otherwise the scheme must be equal ignoring the
     * case.
     * </ol>
     */
    protected final boolean schemeEquals(URI one, URI two)
    {
        final String scheme1 = one.getScheme();
        final String scheme2 = two.getScheme();
        if (scheme1==null) return scheme2==null;
        else return scheme1.equalsIgnoreCase(scheme2);
        
    }

     /**
     * Returns whether the passed URI instances
     * have the same authority, as follows:
     * <ol>
     * <li>If one of the uri has <em>absent</em> authority also the 
     * other must have <em>absent</em> authority.
     * <li>Otherwise the authority components 
     * must be equal (ignore case?) .
     * </ol>
     */
    protected final boolean authorityEquals(URI one, URI two)
    {
        final String scheme1 = one.getAuthority();
        final String scheme2 = two.getAuthority();
        if (scheme1==null) return scheme2==null;
        else return scheme1.equals(scheme2);
        
    }

    
    abstract protected URI _resolve(URI relative) throws URIException;
    abstract protected URI _relativize(URI uri) throws URIException;
        
    
    protected String removePathDots(String path) throws URIException
    {
        boolean debug = false;
        try
        {
        if (path.equals("/.")) return "/";
        else if (path.equals("/..")) return "/"; // no strict
        
        int pathLen = path.length();
        int pathLastCharIndex = pathLen - 1 - 2 ;
        int i = path.indexOf("/.");
        if (i==-1) return path;
        int last = 0;
        if (debug) System.out.println("&&&** Path is:" + path + " size:" + path.length() + " max index:" + pathLastCharIndex);
            
        StringBuffer sb = new StringBuffer();
        while(i!=-1)
        {
            if (debug) System.out.println("FOund at:" + i);
            if (debug) System.out.println("Add From last (" + last + ") to i is [" + path.substring(last, i) + "]");
            
            sb.append(path.substring(last, i));
            last = i;
            
            if (i<=pathLastCharIndex)
            {
                char next = path.charAt(i+2);
                if (debug) System.out.println("next is:" + next);
                if (next=='/')
                {
                    // remove
                    if (debug) System.out.println("Found, remove");
                    last+=2;
                    
                    if (strict) throwExceptionForStrict("Resolving relative, new path is over the root: [" + path + "]");
                }
                else if (next=='.' && (i+3<pathLen && path.charAt(i+3)=='/'))
                {
                    //System.out.println("nextnext is:" + path.charAt(i+3 ));
                    // remove two
                    last+=3;
                    i++;
                    if (debug) System.out.println("Found, remove two");
                   if (strict)  throwExceptionForStrict("Resolving relative, new path is over the root: [" + path + "]");
                }
                else
                {
                    //sb.append("/.");
                    if (debug) System.out.println("Was not..add");
                    //last++;
                }
            }
            if (i < pathLastCharIndex)
            {
                if (debug) System.out.println("find from " + (i+2));
               
                i = path.indexOf("/.", i+2);
           }
            else 
            {
                if (debug)  System.out.println("last is:" + last + " exit.." + pathLastCharIndex);
               break;
             }
        }
        //if (last!=pathLastCharIndex+1)
        {   
            sb.append(path.substring(last, path.length() ));
            if (debug) System.out.println("At end add [" + path.substring(last, path.length() ) + "]");
            
            
        }
        if (debug) System.out.println("FINISHED. Path was:" + path + " now is:" + sb.toString());
         
        //if (true) throw new Error("path was:" + path + " now is:" + sb.toString());
        return sb.toString();
        }
        catch(RuntimeException e)
        {
            e.printStackTrace();
            throw e;
        }
    }
    
    protected final void  throwExceptionForStrict(String msg) throws URIException
    {
        throw new URIException("Strict mode error. " + msg, toString());
    }
    
    /**
     * Test an object if this URI is equal to another.
     *
     * @param obj an object to compare
     * @return true if two URI objects are equal
     */
    public boolean equals(Object obj) {

        // normalize and test each components
        if (obj == this) return true;
        else if (!(obj instanceof URI))  return false;
        
        URI another = (URI) obj;
        // scheme
        if (!equals(getScheme(), another.getScheme())) {
           // System.out.println("DIFF SKEME");
            return false;
        }
        // is_opaque_part or is_hier_part?  and opaque
        if (isOpaque() != another.isOpaque()) {
           // System.out.println("DIFF OPAQUE");
            return false;
        }
        // is_hier_part
        // has_authority
        if (!equals(getAuthority(), another.getAuthority())) {
           // System.out.println("DIFF AUTH");
            return false;
        }
        // path
        if (!equals(getPEPath(), another.getPEPath())) {
          // System.out.println("DIFF PATH");
             return false;
        }
        // has_query
        if (!equals(getQuery(), another.getQuery())) {
          //  System.out.println("DIFF QUERY");
            return false;
        }
        // has_fragment?  should be careful of the only fragment case.
        if (!equals(getFragment(), another.getFragment())) {
          //  System.out.println("DIFF FRAG");
            return false;
        }
        return true;
    }
    
    /**
     * Test if the first String is equal to the second String.
     */
    protected boolean equals(String first, String second) 
    {

        if (first == null && second == null) 
        {
            return true;
        }
        if (first == null || second == null) 
        {
            return false;
        }
        return first.equals(second);
   }

    /**
     * Compare this URI to another object.
     *
     * @param obj the object to be compared.
     * @return 0, if it's same,
     * -1, if failed, first being compared with in the authority component
     * @throws ClassCastException not URI argument
     */
    public int compareTo(Object obj) throws ClassCastException {

        URI another = (URI) obj;
        if (!equals(getAuthority(), another.getAuthority())) {
            return -1;
        }
        return toString().compareTo(another.toString());
    }


    
}
