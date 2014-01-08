package org.eleusoft.uri;

/**
 * <em>Providers Only</em>
 * @author mik
 *
 */
public interface URICodec
{
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
    public String decode(final String encoded,
        final String encoding) throws URIException;
    
    /**
     * Percent-escapes a string following rules for single param name or param
     * value of a URI query component and using the passed encoding.
     * 
     * @return the percent-escaped string, never <code>null</code>
     */
    public String encode(final String decoded,
        final String encoding);

    

}
