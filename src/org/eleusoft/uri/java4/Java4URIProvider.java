package org.eleusoft.uri.java4;

import org.eleusoft.uri.URI;
import org.eleusoft.uri.URIException;
import org.eleusoft.uri.URIProvider;

public class Java4URIProvider implements URIProvider
{
    public Java4URIProvider()
    {
        super();
    }
    public URI createURI(String escaped) throws URIException
    {
        try
        {
            
            return new JDK14URI(escaped);
        }
        catch(java.net.URISyntaxException use)
        {
            throw new URIException(use.getMessage(), escaped, use);
        }
    }
    
    private URI __createURI2(String scheme, String auth, 
        String path, String query, String fragment) throws URIException
    {
        try
        {
            return createURI(buildURIString(scheme, auth, 
                new java.net.URI(null, null, path, null, null).toString(), 
                query, 
                fragment));
        }
        catch(java.net.URISyntaxException use)
        {
            StringBuffer sb = new StringBuffer();
            sb.append("\nScheme:").append(scheme).append("\nAuthority:").append(auth);
            sb.append("\nPath:").append(path).append("\nQuery:").append(query);
            sb.append("\nFragment:").append(fragment);
            String msg = sb.toString();
            throw new URIException(use.getMessage(), msg, use);
        }
    }
    
    public URI createURI(String scheme, String auth, 
        String path, String query, String fragment) throws URIException
    {
        // RFC3986 3.3
        // (..) If a URI  does not contain an authority component, 
        // then the [path component] cannot begin
        // with two slash characters ("//").  
        
        if (auth==null && (path!=null && path.startsWith("//"))) 
        {
            throw new URIException("RFC3986 3.3 if an URI " 
                + "does not contain an authority component then "
                + "the path cannot begin with [//]", path);
        }
        else
        {
            if (true) // Trying..
                return __createURI2(scheme, auth, path, query, fragment);
                
            try
            {
                return new JDK14URI(new java.net.URI(scheme, auth, path, query, fragment));
            }
            catch(java.net.URISyntaxException use)
            {
                StringBuffer sb = new StringBuffer();
                sb.append("\nScheme:").append(scheme).append("\nAuthority:").append(auth);
                sb.append("\nPath:").append(path).append("\nQuery:").append(query);
                sb.append("\nFragment:").append(fragment);
                String msg = sb.toString();
                throw new URIException(use.getMessage(), msg, use);
            }
        }
    }
        
    private static class JDK14URI extends AbstractJDKURI 
    {
        
        JDK14URI(String escaped) throws java.net.URISyntaxException, URIException
        {
            super(new java.net.URI(removeContextDelimiters(escaped)));
        }
        JDK14URI(java.net.URI uri) throws URIException
        {
            super(uri);
        }
        
       
        protected URI _relativize(URI passed) throws URIException
        {
            java.net.URI relativized = this.uri.relativize(((JDK14URI)passed).uri);
            return new JDK14URI(relativized);
        }   
        protected URI _normalize() throws URIException
        {
            // BASE CLASS does not call this method if this
            // is an opaque uri.
            
            // Fix: when normalizing the path should be null
            // if we go over the root.
            // Note: cannot test if returns the same instance
            // because need to fix special cases...
            boolean modified = false;
            java.net.URI normjuri = uri.normalize();
            if (true) return new JDK14URI(normjuri);
            
            String query = normjuri.getQuery();;
            String path = normjuri.getRawPath();
            
            if (path==null)
            {
                // This is the case of an opaque uri
                // but for opaque uri this method 
                // should not have been called.
                throw new Error();
            }
            else if (path.startsWith("/..") || path.startsWith("../") || path.equals(".."))
            {
                // This is the case gone over the root, throw error or use an empty path
                // java.net.URI handles in this way the paths over the root..
                if (isStrict()) throwExceptionForStrict("Cannot normalize a path over the root: [" + path + "]");
                path = "";
                modified = true;
                
            }
            //if ((path.length()==0 || path.equals("."))
            else if (path.equals("/."))
            {
                 path = "/";
                modified = true;
               
            }
            
            if (modified)
            {
                try
                {
                    /**
                    return new JDK14URI(
                        new java.net.URI(normjuri.getScheme(), 
                            normjuri.getAuthority(), 
                            path,
                            query, 
                            normjuri.getFragment()));
                    **/
                    return new JDK14URI(
                        buildURIString(normjuri.getScheme(), 
                            normjuri.getAuthority(), 
                            path,
                            query, 
                            normjuri.getRawFragment()));
                }
                catch(java.net.URISyntaxException use)
                {
                    throw new IllegalStateException("Implementation error. Cannot apport modifications, path:[" + path + "] query:[" + query + "]");
                }
            }
            else 
            {
                return new JDK14URI(normjuri);
            }
        }
        
        protected URI _resolve(URI relative) throws URIException
        {
            // 200705 Fixes test A285 | 175 
            if(isRelativePath()) throw new URIException(
	            "This is a relative-path URI [ " + toString() + 
	                "], cannot resolve with rel URI [" + relative + "]", toString());
		
            try
            {
                ///////////// FROM HERE FIRST PART IS THE SAME IN APACHE URI
                
                
                // Baseclass does not call this method 
                // when relative is absolute or this is opaque.
                //if (relative.isAbsolute()) return relative;
                
                 // Always only the frag of the rel
                 final String relativeURIFragment = relative.getPEFragment();
                    
                // Fix for 7 ?y http://a/b/c/d;p?y
	            // Java uri removes the name part
	            // and outputs http://a/b/c/?y
                // Was checking for null..
                // now use isPathEmpty() 
                // String relPath = relative.getPEPath(); 
                if (relative.isPathEmpty() && relative.getAuthority()==null) 
                {
                    
                    // Relative URI
                    // No path
                    // --> Can have authority, query or fragment
                    // Do not check for empty query here, 
                    // only on normalize.
                    
                    final String relURIAuth = relative.getAuthority();
                    final String relURIQuery = relative.getQuery();
                    
                    final String toUSEAuth = relURIAuth == null ? 
                        getAuthority() : relURIAuth;
                    final String toUSEQuery;
                    if (relURIQuery == null) 
                    {
                        if (relURIAuth == null && relativeURIFragment == null)
                        {
                            // Rel URI, no auth, No path, no query, no fragment.
                            // Is an empty string. Nothing to do apart removing
                            // the fragment
                            return this.removeFragment();
                        }
                        else
                        {
                            toUSEQuery = this.getQuery();
                        }
                        
                    }
                    else
                    {
                        toUSEQuery = relURIQuery;
                    }
                    
                    
                    // Return this absolute uri with authority, query and fragment
                    // as determined
                    // System.out.println("NO AUTH ON REL RESOLVED PATH :" + getPath());       
                    // TODO : Return path "/" if this is absolute and has no path ??
                    return new JDK14URI(new java.net.URI(buildURIString(getScheme(), 
                        toUSEAuth,
                        getPEPath(), 
                        toUSEQuery, 
                        relativeURIFragment)));
                    
                       
                }
                else
                {
                    // FIX tragical error..
                    // <http://www.example.org> + <myrelpat>
                    // is resolved as
                    // http://www.example.orgmyrelpat
                    java.net.URI toBeUsedBaseURI;
                    if (isAbsolute() && getPath().length()==0)
                    {
                        toBeUsedBaseURI = new java.net.URI(buildURIString(getScheme(), 
                            getAuthority(), "/", getQuery(), getPEFragment()));
                    }
                    else
                    {
                        toBeUsedBaseURI = this.uri;
                    }
                    // Case 2: Path is present in relative uri
                    final java.net.URI resolved = 
                        toBeUsedBaseURI.resolve(new java.net.URI(relative.toString()));
                    String path = resolved.getPath();
                    // For "normal" test 15 "" http://a/b/c/d;p?q
               
                    // efficiency only test 
                    // ?? 200705 ?? what ??
                    if (path.indexOf("/.")!=-1) 
                    {
                        path = removePathDots(path);
                        
                       // return 
                        
                        //if (path==null ||  || path.startsWith(".."))
                        {
                            return new JDK14URI(buildURIString(resolved.getScheme(), 
                                resolved.getAuthority(), 
                                new java.net.URI(null, null, path, null).toString(),
                                resolved.getRawQuery(), 
                                relativeURIFragment));
                            //throw new URIException("Path error, over the root:" + path, path);
                        }
                        
                    }
                    return new JDK14URI(resolved);
                }
            }
            catch(java.net.URISyntaxException use)
            {
                throw new URIException("Cannot relativize:" + relative.toReadableURI(), relative.toString(), use);
            }
        }
        
        
        public final URI removeFragment() throws URIException
        {
            try
            {
                return !hasFragment() ? this :
                    new JDK14URI(
                        new java.net.URI(buildURIString(uri.getScheme(), 
                            uri.getAuthority(), 
                            uri.getRawPath(),
                            uri.getRawQuery(), 
                            null)));
            }
            catch(java.net.URISyntaxException use)
            {
                // TODO hmm..very strange if exception..should remove
                // URI exception from interface.
                throw new URIException("Cannot remove fragment:" + toReadableURI(), toString(), use);
            }
        }
    

        
    
    
    }

    
    // P-prot for faster access from inner class.
    static String buildURIString(String scheme, 
            String authority, 
            String path, 
            String query, 
            String fragment)
    {
        final StringBuffer sb = new StringBuffer();
        if (scheme!=null) sb.append(scheme).append(':');
        if (authority!=null) sb.append("//").append(authority);
        if (path!=null) sb.append(path);
        if (query!=null) sb.append('?').append(query);
        if (fragment!=null) sb.append('#').append(fragment);
        return sb.toString();
    }
        
        
                           
}
