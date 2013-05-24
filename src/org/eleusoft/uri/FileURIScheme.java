package org.eleusoft.uri;

import java.io.File;

/**
 * Static helpers methods to work 
 * with the <code>file:</code>
 * scheme <em>URI</em>
 */
public class FileURIScheme
{
    private FileURIScheme(){;}
    /**
     * The <code>file</code> scheme string
     */
    public static final String SCHEME = "file";
    /**
     * Retrieves whether the passed scheme is a <code>file</code>
     * scheme, comparison is case unsensitive.
     * @return true if is a file scheme.
     * @param scheme the scheme to test, never null.
     * @throws IllegalArgumentException if scheme is null.
     */
    public static final boolean isFileScheme(String scheme)
    {
        if (scheme==null) throw new IllegalArgumentException("null scheme");
        return scheme.equalsIgnoreCase(SCHEME);
    }
    /**
     * Returns a file from a
     * <code>file:</code> scheme <em>URI</em> string.
     * <p>The passed uri might use the <code>file:</code>
     * scheme or a <code>null</code> or <em>empty</em>
     * scheme. Examples are:
     * <pre class=code>
     * file:/a/b/c.foo
     * file:/a/b%20c/c%20d.foo
     * file:/f:/docs/dox.xml
     * file:/f:/docs/dox.xml
     * /f:/docs%20forme.xml
     * :/f:/docs.xml (extreme case)
     * </pre>
     * <p>The passed uri must be correctly escaped,
     * so that it can be parsed by 
     * {@link URIFactory#createURI(String)}.
     * @param escapedURI a file uri 
     * @return a java.io.File for the passed uri
     */
    public static File fromURI(String escapedURI) throws URIException
    {
        URI uri = URIFactory.createURI(escapedURI);
        return fromURI(uri);
    }
    
    /**
     * Returns a file from a
     * <code>file:</code> scheme <em>{@link URI}</em> interface.
     * <p>The passed uri might use the <code>file:</code>
     * scheme or a <code>null</code> or <em>empty</em>
     * scheme. Examples are:
     * <pre class=code>
     * file:/a/b/c.foo
     * file:/a/b%20c/c%20d.foo
     * file:/f:/docs/dox.xml
     * file:/f:/docs/dox.xml
     * /f:/docs%20forme.xml
     * :/f:/docs.xml (extreme case)
     * </pre>
     * @param uri a file {@link URI} implementation
     * @return a java.io.File for the passed uri
     */
    public static File fromURI(URI uri) 
    {
        String scheme = uri.getScheme();
        if (scheme!=null && scheme.length()!=0 &&
            !isFileScheme(scheme))
        {
            throw new IllegalArgumentException("Passed uri is not a file or no-scheme uri:" + uri);
        }
        // File use percent decoded path
        String path = uri.getPDPath();
        String auth = uri.getAuthority();
        File file;
        if (auth!=null)
        {
            file = new File("\\\\" + auth + "\\" + path);
        }
        else
        {
            file = new File(path);
        }
        return file;
    }
    /**
     * Transforms a file to an URI.
     * The <code>file:</code> URI is local 
     * to the virtual machine. 
     * Corresponds to {@link java.io.File.toURI()}
     * available from JDK1.4
     */
    public static URI toURI(File file)
    {
        try
        {
            File absfile = file.getAbsoluteFile();
            String unescapedPath = slashify(absfile.getPath(), absfile.isDirectory());
            if (unescapedPath==null) throw new IllegalArgumentException("file has no path ?" + absfile);
            // Windows share - Eg: //mik2/dest 
            // java.net.URI makes from a windows share an uri with
            // no authority and path that starts with "//", Eg: file:////mik2/dest
            // This is wrong according to RFC3986 (path cannot start with '//' when
            // no authority)
            // Instead the URI should be file://mik2/dest
            if (unescapedPath.startsWith("//"))
            {
                String auth;
                String path;
                
                int i = unescapedPath.indexOf('/',2);
                if (i==-1) 
                {
                    auth = unescapedPath.substring(2);
                    path = "";
                }
                else 
                {
                    auth = unescapedPath.substring(2, i);
                    path = unescapedPath.substring(i, unescapedPath.length());
                }
                return URIFactory.createURI(SCHEME, auth, path, null, null);
                
            }
            else return URIFactory.createURI(SCHEME, null, unescapedPath, null, null);
        }
        catch(URIException u)
        {
            throw new Error("URI exception should not happen:" + u.getMessage());
        }
    }
    
    
    private static String slashify(String path, boolean isDirectory) {
	    String p = path;
	    if (File.separatorChar != '/')
	        p = p.replace(File.separatorChar, '/');
	    if (!p.startsWith("/"))
	        p = "/" + p;
	    if (!p.endsWith("/") && isDirectory)
	        p = p + "/";
	    return p;
    }

    
    
}
