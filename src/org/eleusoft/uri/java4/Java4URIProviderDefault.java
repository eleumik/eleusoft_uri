package org.eleusoft.uri.java4;

import org.eleusoft.uri.URI;
import org.eleusoft.uri.URIException;
import org.eleusoft.uri.URIProvider;

/**
 * JAVA 4++ URI provider without corrections
 */
public class Java4URIProviderDefault implements URIProvider
{
    public Java4URIProviderDefault()
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
   public URI createURI(String scheme, String auth, 
        String path, String query, String fragment) throws URIException
    {
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
            return new JDK14URI(normjuri);
            
            
        }
        
        protected URI _resolve(URI relative) throws URIException
        {
            
            try
            {
                ///////////// FROM HERE FIRST PART IS THE SAME IN APACHE URI
                
                
                // Baseclass does not call this method 
                // when relative is absolute or this is opaque.
                //if (relative.isAbsolute()) return relative;
                
                 // Always only the frag of the rel
                 final String relativeURIFragment = relative.getFragment();
                    
                // Fix for 7 ?y http://a/b/c/d;p?y
	            // Java uri removes the name part
	            // and outputs http://a/b/c/?y
                final java.net.URI resolved = 
                    this.uri.resolve(new java.net.URI(relative.toString()));
                return new JDK14URI(resolved);
                
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
                if (getFragment()==null) return this;
                else return new JDK14URI(
                    new java.net.URI(uri.getScheme(), uri.getAuthority(), uri.getPath(),
                        uri.getQuery(), null));
            }
            catch(java.net.URISyntaxException use)
            {
                throw new URIException("Cannot remove fragment:" + toReadableURI(), toString(), use);
            }
        }
    

        
    
    
    }

    
    
                           
}
