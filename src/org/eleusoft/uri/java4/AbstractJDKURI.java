package org.eleusoft.uri.java4;

import org.eleusoft.uri.AbstractURI;

/**
 * Baseclass for implementing JDK URIs
 */
abstract class AbstractJDKURI extends AbstractURI
{

    protected java.net.URI uri;
        
    
    
    protected AbstractJDKURI(java.net.URI uri)
    {
        this.uri = uri;
        if (uri.isOpaque())
        {
            initOpaque(uri.getScheme(), 
                uri.getSchemeSpecificPart(), 
                uri.getFragment(), uri.toASCIIString());
        }
        else 
        {
            uri.getQuery(); // to decode
            init(uri.getScheme(), uri.getAuthority(), 
                uri.getPath(), uri.getRawQuery(), 
                uri.getFragment(), uri.toASCIIString());
        }    
    }
        
    public String getPEFragment()
    {
        return uri.getRawFragment();
    }
    
    public String getPEPath()
    {
        String s = uri.getRawPath();
        if (s==null) return uri.getRawSchemeSpecificPart();
        else return s;
    }
     
    public final String toReadableURI()
    {
        return uri.toString();
    }
    
    
    
                           
}
