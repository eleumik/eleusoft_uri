/**
 *
 */
package org.eleusoft.uri.util;

import org.eleusoft.uri.URI;
import org.eleusoft.uri.URIException;
import org.eleusoft.uri.URIFactory;


/**
 * A class to handle the <code>jar:</code> URI schema.
 * @author mik
 *
 */
public final class JARURIScheme
{

    private static final boolean debug = Boolean.getBoolean(JARURIScheme.class.getName()+".debug");
    private static final String JAR_SCHEME = "jar:";

    private final String jarURI;
    private final String jarEntryPath;

    private JARURIScheme(final String jarFilePath,
        final String jarEntry)
    {
        this.jarURI = jarFilePath;
        this.jarEntryPath = jarEntry;
    }
    /**
     * Returns the (percent-decoded) path of the jar entry.
     * <p><em>Example:</em>
     * <pre class=code>
     * /myjarfolder/my doc.xslt
     * </pre>
     * @return a string, never null.
     */
    public String getJarEntryPath()
    {
        try
        {
            URI uri = URIFactory.createURI(jarEntryPath);
            return uri.getPDPath();
        }
        catch(URIException e)
        {
            return jarEntryPath;
        }
    }
    /**
     * Returns the (percent-encoded) part of the
     * jar uri regarding the jar entry, the part
     * after the '!' symbol.
     * <p><em>Example:</em>
     * <pre class=code>
     * /myjarfolder/my%20doc.xslt
     * </pre>
     * @return a string, never null.
     */
    public String getJarEntryPart()
    {
        try
        {
            URI uri = URIFactory.createURI(jarEntryPath);
            return uri.getPDPath();
        }
        catch(URIException e)
        {
            return jarEntryPath;
        }
    }

    /**
     * Returns the (percent-encoded) part of the
     * jar uri regarding the jar file, without
     * the entry.
     * <p>Returns the part
     * after the <code>jar:</code> scheme and
     * before the <code>'!'</code> symbol.
     * prefix.
     * <p><em>Example:</em>
     * <pre class=code>
     * file:/f:/my%20folder/myjar.jar
     * </pre>
     * @return a string, never null.
     */
    public String getJarURI()
    {
        return jarURI;
    }



    /**
     * Returns the full URI of the jar file
     * and its entry.
     * <p><em>Example:</em>
     * <pre class=code>
     * file:/f:/my%20folder/myjar.jar!/myjarfolder/mydoc.xslt
     * </pre>
     * @return a string, never null.
     */
    public String toString()
    {
        return JAR_SCHEME + jarURI + "!" + jarEntryPath;
    }
    /**
     * Returns a {@link JARURIScheme} instance from the passed
     * uri string or null when is not a jar uri.
     * @param uri the jar uri.
     * @return a {@link JARURIScheme} or null.
     */
    public static final JARURIScheme getInstance(String uri)
    {
        final String jpath = uri.trim();
        if (!jpath.startsWith(JAR_SCHEME)) return null;
        if (debug) System.out.println("JARURIScheme ASKED " + uri);
        int index = jpath.indexOf(".jar!");
        if (index==-1)
        {
            index = jpath.indexOf('!');
            if (index==-1) return null;
        }
        else index +=4; // skip '.jar'

        final String jarRel = jpath.substring(index+1).replace('\\', '/');
        // remove jar:file: prefix (already checked is there)
        final String jar = jpath.substring(JAR_SCHEME.length(), index);
        if (debug)
        {
            System.out.println("JAR IS " + jar + "{END}");
            System.out.println("JARENTRY IS " + jarRel  + "{END}");
        }
        JARURIScheme result = new JARURIScheme(jar, jarRel);
        if (debug) System.out.println("RESULT IS " + result + "{END}");
        return result;

    }
    public static JARURIScheme getInstance(JARURIScheme jarUri, String href)
    throws URIException
    {
        Path path = new Path(jarUri.getJarEntryPath());
        Path hrefPath = new Path(href);
        Path resultPath = path.resolve(hrefPath);
        return new JARURIScheme(jarUri.getJarURI(), resultPath.toString());
    }

}