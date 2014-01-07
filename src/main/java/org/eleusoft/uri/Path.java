package org.eleusoft.uri;	

// This class exploits the current implementation of URI

import java.util.StringTokenizer;
/**
 * Path class.
 * This class depends on the concept of path
 * as specified in URI RFC 3986,
 * the path has the following rules resumed
 * from RFC 3986 Section 3.3:
 * <ol>
 * <li>A path is made of segments, separated by 
 * the slash [/] character.
 * <li>A path is absolute when it starts with slash [/], 
 * otherwise is said to be a relative path.
 * <li>A path can also start with double slash [//] 
 * <li>When is a relative path, its first 
 * segment cannot contain a colon [:] character.
 * <blockquote><code>RFC 3986 4.2 </code>
 * <p>(..)A path segment that contains a colon character 
 * (e.g., "<code>this:that</code>")
 * cannot be used as the first segment of a relative-path reference, as
 * it would be mistaken for a scheme name.  Such a segment must be
 * preceded by a dot-segment (e.g., "<code>./this:that</code>") 
 * to make a relative-path reference.
 * </blockquote>
 * <p class=note>For 4.2. In a web application the first segment
 * of the path is the application context, that should not
 * contain [:]. Since a web app context name maps to a folder
 * and windows folders cannot contain <code>\ / ? : * &quot; &lt; | &gt; </code>
 * so having a [:] in first path is not possible for windows, 
 * I don't know if is possible in linux.</p>
 * <p class=note>For 4.2, remember that relative paths cannot be normalized
 * so there is no risk to remove the <code>./</code> in front 
 * of the <code>./this:that </code> relative path.
 * <li>Trimming - Delimiters
 * <li>From <cite>RFC 3986 Appendix C</cite>
 * Parsed uris are trimmed and two delimiters are removed:
 * angle-brackets and quotes.
 * <blockquote>URIs are often transmitted through formats that do not provide a 
 * clear context for their interpretation. For example, there are many 
 * occasions when a URI is included in plain text; examples include 
 * text sent in email, USENET news, and on printed paper. 
 * In such cases, it is important to be able to delimit 
 * the URI from the rest of the text, and in particular from punctuation 
 * marks that might be mistaken for part of the URI.
 * In practice, URIs are delimited in a variety of ways, but usually 
 * within double-quotes "http://example.com/", angle brackets 
 * <http://example.com/>, or just by using whitespace
 * <p>....
 * <p>For robustness, software that accepts user-typed URI should attempt 
 * to recognize and strip both delimiters and embedded whitespace.</blockquote>
 * </ol>
 * <p>A path is not an URI but a component of an URI.
 * <p>The string representation of a path 
 * as intended by this class is always unescaped,
 * the escaped form can be obtained with 
 * <code>Path.getURI().toString()</code>.
 * <p><b>Extensions</b>
 * <p>This class allows to split logically
 * a path into a 
 * <em>context</em> and a <em>name</em> part.
 * The <em>context</em> is what is before the last slash [/] char,
 * included the slash.
 * The <em>name</em> is what is after the last slash [/] char,
 * if there is something.
 * <p>For example:
 * <code class='code'>
 * /dir1/dir2/ciao.xml
 * context = /dir1/dir2/
 * name = ciao.xml
 *
 * /dir1/dir2/
 * context = /dir1/dir2/
 * name = [absent]
 * </code>.
 */
public final class Path
{
    //private static final Path EMPTY_PATH; // NO empty path..too strange behavior uri impls..
    
    /**
     * A common use Path represening
     * the parent path [..]
     */
    public static final Path PARENT_PATH;
    /**
     * A common use Path represening
     * the context path [.]
     */
    public static final Path CONTEXT_PATH;
    /**
     * A common use Path represening
     * the root path [/]
     */
    public static final Path ROOT_PATH;
    
    
    static
    {
        try
        {
            ROOT_PATH = new Path("/");
            PARENT_PATH = new Path("..");
            CONTEXT_PATH = new Path(".");
        }
        catch(URIException e)
        {
            e.printStackTrace();
            throw new Error("Impossible ? org.eleusoft.uri.Path 35324:" + e.getMessage());
        }
    }
    
    // The contained URI instance.
    private URI uri;
    
    /**
     * Public contructor.
     * @throws URIException if the passed path is not a valid path.
     */
	public Path(final String path) throws URIException
	{
	    this(URIFactory.createURI(null, null, path, null, null), true);
	}
	
	/**
     * Public contructor with URI configuration
     * @throws URIException if the uri is absolute 
     * and/or if it has a fragment and/or a query.
     */
	public Path(final URI uri) throws URIException
	{
	    this(uri, true);
	    if (uri.isAbsolute()) throw new URIException("Passed URI is an absolute URI", uri.toString());
	    else if (uri.hasFragment()) throw new URIException("Passed URI has fragment", uri.toString());
	    else if (uri.hasQuery()) throw new URIException("Passed URI has query", uri.toString());
	}
	
	// Fast lane constructor for private impl
	// called also by other constructors
	private Path(final URI uri, boolean dummy) throws URIException
	{
	    this.uri = uri;
	    if (getPath()==null) throw new URIException("Missing path in passed URI:" + uri.toString(),  uri.toString());
	}
	/**
     * Gets the path as an URI interface instance.
     * <p>Internally retrieves the 
     * unmodifiable wrapped URI interface.
     */
    public URI toURI()
    {
        return uri;
    }
	/**
	 * Retrieves the string 
	 * value of this path,
	 * in unescaped form;
	 * equivalent to {@link #getPath()}
	 */
	public String toString()
	{
	    return getPath();
	}
	/**
	 * Retrieves the string 
	 * value of this path,
	 * in unescaped form.
	 */
	public String getPath()
	{
	    return uri.getPDPath();
	}
	/**
	 * Returns the sequence of path
	 * segments in percent-decoded form
	 * as a String array.
	 * @return a String array, possibly empty, never null.
	 */
	public String[] getSegments()
	{
	    final StringTokenizer st = new StringTokenizer(getPath(), "/");
	    final int i = st.countTokens();
        final String[] s = new String[i];
        int z = 0;
        while(st.hasMoreElements())
        {
            s[z] = st.nextToken();
            z ++ ;
        }
        return s;
	}
	/**
	 * Signal whether this
	 * path is a relative path,
	 * eg: does not start with [/] slash.
	 */
	public boolean isRelative()
	{
	    return uri.isRelativePath();
	}
	/**
	 * Retrieves the name 
	 * part of this path.
	 * See class description
	 * for description of how 
	 * a path is split in a 
	 * <em>context</em> and a <em>name</em> part.
	 *
	 */
	public String getName()
	{
	    return uri.getName();
	}
		
    /**
	 * Creates a new path obtained
	 * from the context path.
	 * See class description
	 * for description of how 
	 * a path is split in a 
	 * <em>context</em> and a <em>name</em> part.
	 */
	public final Path getContextPath() throws URIException
	{
	    return resolve(CONTEXT_PATH);
	    // 201401 old, was disabled:
//	    final String name = getName();
//	    final String path = getPath();
//	    final String currHier = name==null ? path : path.substring(0, path.length() - name.length());
//	    //if (true) throw new Error("path:" + path + "] currHier:" + currHier+"[");
//	    return new Path(currHier);
	}
	
	/**
	 * Retrieves the parent path.
	 * Internally creates a new path obtained
	 * resolving this path using
	 * {@link resolve(Path)} and passing
	 * the {@link PARENT_PATH} instace.
	 */
	public Path getParentPath() throws URIException
	{
	    return resolve(PARENT_PATH) ;
	}
	/**
	 * Creates a new path obtained
	 * performing URI normalization
	 * on this path.
	 */
	public Path normalize() throws URIException
	{
	    URI n = uri.normalize();
	    if (n==uri) return this;
	    else return new Path(n);
	}
	
	/**
	 * Creates a new path obtained
	 * resolving this uri with the
	 * passed <em>relative</em> path.
	 * {@link resolve(Path)} and passing
	 * the {@link PARENT_PATH} instace.
	 * @param relPath a relative path 
	 * @throws URIException when the passed path is not relative.
	 */
	public Path resolve(Path relPath) throws URIException
	{
	    // 200706 added check for this.relative to fix test A389 | 279 
	    if(!relPath.isRelative()) throw new URIException(
	        "Not a relative path:" + relPath, relPath.toString());
		else if(isRelative()) throw new URIException(
	        "This is a relative path [ " + toString() + 
	        "], cannot resolve with rel path [" + relPath + "]", 
	        relPath.toString());
		
		final URI resolved = uri.resolve(relPath.uri).normalize();
		// Changed 2000705
		//final Path resolvedPath = resolved.getPath()==null ? 
		//    CONTEXT_PATH : new Path(resolved, true);
		final Path resolvedPath = resolved.isPathEmpty() ? 
		    ROOT_PATH : new Path(resolved, true);
		return resolvedPath;
	}
	/**
	 * Convenience method, like calling
	 * {@link resolve(Path)} but passing
	 * a string.
	 * @param relPath a relative path 
	 * @throws URIException when the passed path is not relative.
	 */
	public Path resolve(String relPath) throws URIException
	{
	    
		if(relPath==null) throw new IllegalArgumentException("null relative path");
		
		Path relPathObj = new Path(relPath);
		return resolve(relPathObj);
    }
    
    
	
}
    
