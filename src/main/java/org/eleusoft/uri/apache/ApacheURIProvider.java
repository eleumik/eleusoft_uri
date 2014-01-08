package org.eleusoft.uri.apache;

import org.eleusoft.uri.URI;
import org.eleusoft.uri.URIException;
import org.eleusoft.uri.URIProvider;
import org.eleusoft.uri.util.AbstractURI;

public class ApacheURIProvider implements URIProvider
{
    public ApacheURIProvider()
    {
        super();
    }
    public URI createURI(String escaped) throws URIException
    {
        return newURI(escaped);
    }
    public URI createURI(String scheme, String auth,
        String path, String query, String fragment) throws URIException
    {
        try
        {
            // Double encode and decode because apache uri does not
		    // understand for #.xml that # is part of the path and
		    // not fragment also if is passed in the path.
		    // java.net.URI works good..then return path
		    // unescaping it..
		    //URI uri = new URI(null, null, normalize(URLUtil.URLEncode(val, "UTF-8")), null, null);

            return newURI(scheme, auth, path, query, fragment);
        }
        catch(org.eleusoft.uri.apache.URIException use)
        {
            StringBuffer sb = new StringBuffer();
            sb.append("Scheme:").append(scheme).append("\nAuthority:").append(auth);
            sb.append("\nPath:").append(path).append("\nQuery:").append(auth);
            sb.append("\nFragment:").append(fragment);
            String msg = sb.toString();
            throw new URIException(use.getMessage(), msg, use); //rmvd from msg  + "\nparts:" + msg
        }
    }
    private static class ApacheURI extends AbstractURI
    {
        transient org.eleusoft.uri.apache.URI uri;



        ApacheURI(org.eleusoft.uri.apache.URI uri) 
            throws org.eleusoft.uri.apache.URIException
        {
            this.uri = uri;

            boolean isOpaque = uri.isOpaquePart(); //uri.isAbsoluteURI() && ApacheURIProvider.isOpaque(uri.toString());
            //if (uri.toString().indexOf("mail")!=-1)  throw new Error("op:" + isOpaque + " uri:" + uri.toString());
            if (isOpaque)
            {
                initOpaque(uri.getScheme(), uri.getPath(), 
                    uri.getFragment(), uri.getEscapedURIReference());
            }
            else
            {
                final String query = uri.getEscapedQuery();
                // getQuery to trigger decoding checking %-escape sequences
                uri.getQuery(); 
                
                init(uri.getScheme(), uri.getAuthority(), uri.getPath(),
                    query, uri.getFragment(), uri.getEscapedURIReference());
            }

        }
        /*
        public String getAuthority()
        {
            char[] userInfo = uri.getRawUserinfo();
            if (userInfo!=null) 
            {
                StringBuffer sb = new StringBuffer();
                sb.append(userInfo, 0,userInfo.length);
                sb.append('@');
                sb.append(super.getAuthority());
                return sb.toString();
            }
            else return super.getAuthority();
        }*/
        public String getPEPath()
        {
            return uri.getEscapedPath();
        }
        public String getPEFragment()
        {
            return uri.getEscapedFragment();
        }
    
     
        protected URI _normalize() throws URIException
        {
            // BASE CLASS does not call this method if this
            // is an opaque uri.


            try

            {
                // normalization doesn't change from abs to rel uri
                boolean isAbsolute = isAbsolute();
                // Fix: when normalizing the path should be null
                // if we go over the root.
                org.eleusoft.uri.apache.URI normapacheuri =
                    uri.normalize();


                // Fix: when normalizing the path should be null
                // if we go over the root.
                // Note: cannot test if returns the same instance
                // because need to fix special cases...
                boolean modified = false;
                //if (true) throw new Error();
                //if (true) return new ApacheURI(normapacheuri);

                String query = normapacheuri.getQuery();



                String path = normapacheuri.getPath();

                if (path==null)
                {
                    // This is the case of an URI over root
                    // apache handles in this way,
                    // so we must use empty string as path, as
                    // specified in URI javadoc.
                    if (isStrict())  throwExceptionForStrict("Normalization error, no path. Uri is: [" + toString() + "]");
                    // 200705 and so why '/' for root, changed.
                    //path = isAbsolute ? "/" : "";
                    path = "";
                    modified = true;
                }
                else if (path.equals("."))
                {
                    // After normalization remove context points
                    path = isAbsolute ? "" : "";
                    modified = true;
                }
                else if (path.startsWith("/../") || path.startsWith(".."))
                {
                    // This is the case gone over the root, return empty path
                    // java.net.URI handles in this way the paths over the root..
                    path = "";
                    modified = true;

                }
                else if (path.equals("/."))
                {
                    path = ".";
                    modified = true;
                }

                if (path.length()!=0)
                {
                    // If it is a non empty rel path
                    // make it absolute, normalize and remove front slash.
                    // This is out of spec. should not normalize relative....TODO setting.

                    // 6.1. Equvalence
                    // ...
                    // In testing for equivalence, applications should not directly
                    // compare relative references; the references should be converted
                    // to their respective target URIs before comparison.
                    // When URIs are compared to select (or avoid) a network action,
                    // such as retrieval of a representation, fragment components
                    // (if any) should be excluded from the comparison.


                    if (!path.startsWith("/"))
                    {
                        path = "/" + path;
                        org.eleusoft.uri.apache.URI temp =
                             new org.eleusoft.uri.apache.URI(null, null,
                                 path , null, null);
                        temp = temp.normalize();
                        path = temp.getPath();
                        int patLen = path.length();
                        path = patLen==0 ? "" : path.substring(1, patLen);
                        modified = true;
                    }
                }

                if (modified)
                {
                    String fragment =normapacheuri.getEscapedFragment(); 
                    try
                    {
                        return newURI(normapacheuri.getScheme(), 
                            normapacheuri.getAuthority(),
                            path, 
                            query, 
                            fragment);
                    }
                    catch(org.eleusoft.uri.apache.URIException use)
                    {
                        throw new IllegalStateException("Cannot apport modifications, scheme:[" + normapacheuri.getScheme()
                            + "] authority:[" + normapacheuri.getAuthority()
                            + "] path:[" + path + "] query:[" + query
                            + "] fragment:[" +  fragment
                            + "] msg:" + use.getMessage()
                            + "\nthis uri:" + toString());
                    }
                }
                else
                {
                    return new ApacheURI(normapacheuri);

                }
            }
            catch(org.eleusoft.uri.apache.URIException ue)
            {
                throw new Error("Cannot normalize");//, toString(), ue);
            }

        }
        public URI removeFragment() throws URIException
        {
            try
            {
                if (!hasFragment()) return this;
                else
                {
                    return newURI(buildURIString(uri.getScheme(), 
                        uri.getAuthority(),
                        uri.getEscapedPath(), 
                        uri.getEscapedQuery(), null));
                }
            }
            catch(org.eleusoft.uri.apache.URIException use)
            {
                throw new URIException("Cannot remove fragment:" + toReadableURI(), toString(), use);
            }
        }
        protected URI _relativize(URI passed) throws URIException
        {
            throw new URIException("relativize is still an unsupported operation", passed.toString());
        }

        protected URI _resolve(URI relative) throws URIException
        {
            try
            {
                // Baseclass does not call this method
                // when relative is absolute or this is opaque.
                // if (relative.isAbsolute()) return relative;

                // OK, changed code inside httpclient.URI !

                // Apache uri needs a scheme in front to
                // relativize ...
                org.eleusoft.uri.apache.URI baseAbsolutizedInCase = getAbsoluteURI();
                org.eleusoft.uri.apache.URI resolved =
                    new org.eleusoft.uri.apache.URI (baseAbsolutizedInCase, 
                        relative.toString());
                if (baseAbsolutizedInCase!=this.uri)
                {
                    return newURI(null, null, resolved.getPath(),
                        resolved.getQuery(), resolved.getEscapedFragment());
                }
                return new ApacheURI(resolved);
            }
            catch(org.eleusoft.uri.apache.URIException use)
            {
                throw new URIException("Cannot relativize [" +
                    toReadableURI() + "] with rel [" + relative.toReadableURI() +
                    "]\nMsg:" + use.getMessage(), relative.toString(), use);
            }
        }

        public final String toReadableURI()
        {
            return uri.toString();
        }

        private org.eleusoft.uri.apache.URI getAbsoluteURI() 
            throws org.eleusoft.uri.apache.URIException
        {
            return  (uri.isAbsoluteURI()) ? uri : 
                new org.eleusoft.uri.apache.URI (
                    "dummy", 
                    getAuthority(),
                    getPath(), 
                    getQuery(), 
                    getPEFragment());
        }
    }

    private static final URI newURI(String scheme, String auth, String path, String query, String fragment)
        throws org.eleusoft.uri.apache.URIException, URIException
    {
        return new ApacheURI(new org.eleusoft.uri.apache.URI (scheme,
            auth, path, query, fragment));
    }

    private static URI newURI(String escaped) throws URIException
    {
        try
        {
            final org.eleusoft.uri.apache.URI impl;
            // TODO200705 FINDBUGS not used isOpaque and getOpaqueParts..?
            if (true || !isOpaque(escaped))
            {
                // END - impl =  new org.eleusoft.uri.apache.URI (escaped, true);
                impl = new org.eleusoft.uri.apache.URI (escaped);
            }
            else
            {
                String[] parts = getOpaqueParts(escaped);
                impl = new org.eleusoft.uri.apache.URI (parts[0], parts[1], parts[2]);
                if (!impl.isOpaquePart()) throw new Error("caazzzz");
            }
            final ApacheURI result = new ApacheURI(impl);
            //if (!escaped.equals(impl.getEscapedURIReference()))
            // throw new RuntimeException("result is :" + impl.getEscapedURIReference() + " while source:" + escaped);
            return result;
        }
        catch(org.eleusoft.uri.apache.URIException use)
        {
            throw new URIException(use.getMessage(), escaped, use);
        }
    }

    /**
     * @return true if passed abs uri is opaque
     * @param uri an ESCAPED  absolute uri to be tested for opacity
     */
    private static boolean isOpaque(String uri) throws URIException
    {
        //System.out.println("Call is opaque:" + uri);
        int i = uri.indexOf(':');
        if (i==-1) return false;
        else if (i==0) throw new URIException("First character is a colon. If it is a relative path should be escaped, otherwise is an error", uri);
        else if (uri.length() <= i + 2)
        {
            // here can trap http:. and http:..
            //System.out.println("Not opaque, Exit for:" + uri);
            return false; // nothing or less than 2 chars after : of scheme
        }
        else
        {
            if (uri.substring(++i, i+1).equals("/"))
            {
                System.out.println("Not opaque");
                return false;
            }
            else
            {
                //System.out.println("Is opaque, Non / but:" + uri.substring(i, i+2));
                return true;
            }
        }
    }

    /**
     * @return true if passed abs uri is opaque
     * @param uri an ESCAPED absolute uri to be tested for opacity
     */
    private static String[] getOpaqueParts(String uri)
    {
        //System.out.println("Call getOpaqueParts:" + uri);
        int i = uri.indexOf(':');
        if (i==-1) throw new IllegalStateException("Is opaque and there is no ':' character ?");

        final String scheme = uri.substring(0, i);
        // Scheme specific part
        final String ssp;
        final String fragment;
        // increment to skip [:]
        ++i;
       int len = uri.length();
        int h = uri.lastIndexOf('#');
        if (h==-1)
        {
            ssp = uri.substring(i, len);
            fragment = null;
        }
        else
        {
            ssp = uri.substring(i,h);
            // increment to skip [#]
            h++;
            fragment = (h>len) ? "" : uri.substring(h, len);
        }
        //System.out.println("From uri:" + uri + "\nparts:" + scheme + "]\n" + ssp + "]\n" + fragment + "]");
        return new String[]{scheme, ssp, fragment};
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
