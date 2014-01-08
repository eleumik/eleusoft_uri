package org.eleusoft.uri.apache;

import org.eleusoft.uri.URICodec;
import org.eleusoft.uri.URIException;

/**
 * {@link URICodec} implementation based on Apache package.
 * @author mik
 *
 */
public class ApacheURICodec implements URICodec
{

    public String decode(String encoded, String encoding) throws URIException
    {
        try
        {
            return org.eleusoft.uri.apache.URIUtil.decode(encoded, encoding);
        }
        catch (org.eleusoft.uri.apache.URIException e)
        {
            throw new URIException(encoding, encoding, e);
        }
    }

    public String encodeQueryPart(String decoded, String encoding)
    {
        return org.eleusoft.uri.apache.URIUtil.encodeWithinQuery(decoded,
            encoding);
    }

    

}
