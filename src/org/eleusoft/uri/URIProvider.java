package org.eleusoft.uri;

/**
 * <em>Providers only implementation interface.</em>
 * <p>This interface must be implemented by URI implementations
 * providers. 
 * <p>Its use is intended for internal and providers use, not for clients. 
 * 
 * <p>To determine which URIProvider is loaded:
 * <ol>
 * <li>Set the value of the system property <code>org.eleusoft.uri.URIProvider</code> with value the implementation class  
 * <li>Create a file <code>META-INF/services/org.eleusoft.uri.URIProvider</code> in the class path 
 * containing a line with the implementation class.
 * </ol>
 */
public interface URIProvider
{
    URI createURI(String escaped) throws URIException;
    
    /**
     * 
     * @param scheme
     * @param auth 
     * @param path percent decoded path
     * @param query percent encoded query
     * @param fragment percent encoded fragment
     * @return
     * @throws URIException
     */
    URI createURI(String scheme, String auth, 
        String path, String query, String fragment) throws URIException;
    
       
}
