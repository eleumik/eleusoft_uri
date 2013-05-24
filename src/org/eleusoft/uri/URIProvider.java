package org.eleusoft.uri;

/**
 * <em>Providers only implementation interface.</em>
 * <p>This interface must be implemented by URI implementations
 * providers. 
 * <p>Its use is intended for internal and providers use, not for clients. 
 */
public interface URIProvider
{
    URI createURI(String escaped) throws URIException;
    
    URI createURI(String scheme, String auth, 
        String path, String query, String fragment) throws URIException;
    
       
}
