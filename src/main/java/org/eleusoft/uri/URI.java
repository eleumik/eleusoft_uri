/*
 * $Header: /cvsrep/fd/WEB-INF/classes/org/eleusoft/uri/apache/URI.java,v 1.1 2007/07/10 18:19:30 mik Exp $
 * $Revision: 1.1 $
 * $Date: 2007/07/10 18:19:30 $
 *
 * ====================================================================
 *
 *  Copyright 2002-2004 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

//package org.apache.commons.httpclient;
// http://svn.apache.org/viewvc/jakarta/commons/proper/httpclient/trunk/
// http://svn.apache.org/viewvc/jakarta/commons/proper/httpclient/trunk/src/java/org/apache/commons/httpclient/
package org.eleusoft.uri.apache;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.BitSet;
import java.util.Hashtable;
import java.util.Locale;

//import org.apache.commons.codec.DecoderException;
//import org.apache.commons.codec.net.URLCodec;
//import org.apache.commons.httpclient.util.EncodingUtil;

/**
 * The interface for the URI(Uniform Resource Identifiers) version of RFC 2396.
 * This class has the purpose of supportting of parsing a URI reference to
 * extend any specific protocols, the character encoding of the protocol to
 * be transported and the charset of the document.
 * <p>
 * A URI is always in an "escaped" form, since escaping or unescaping a
 * completed URI might change its semantics.
 * <p>
 * Implementers should be careful not to escape or unescape the same string
 * more than once, since unescaping an already unescaped string might lead to
 * misinterpreting a percent data character as another escaped character,
 * or vice versa in the case of escaping an already escaped string.
 * <p>
 * In order to avoid these problems, data types used as follows:
 * <p><blockquote><pre>
 *   URI character sequence: char
 *   octet sequence: byte
 *   original character sequence: String
 * </pre></blockquote><p>
 *
 * So, a URI is a sequence of characters as an array of a char type, which
 * is not always represented as a sequence of octets as an array of byte.
 * <p>
 *
 * URI Syntactic Components
 * <p><blockquote><pre>
 * - In general, written as follows:
 *   Absolute URI = &lt;scheme&gt:&lt;scheme-specific-part&gt;
 *   Generic URI = &lt;scheme&gt;://&lt;authority&gt;&lt;path&gt;?&lt;query&gt;
 *
 * - Syntax
 *   absoluteURI   = scheme ":" ( hier_part | opaque_part )
 *   hier_part     = ( net_path | abs_path ) [ "?" query ]
 *   net_path      = "//" authority [ abs_path ]
 *   abs_path      = "/"  path_segments
 * </pre></blockquote><p>
 *
 * The following examples illustrate URI that are in common use.
 * <pre>
 * ftp://ftp.is.co.za/rfc/rfc1808.txt
 *    -- ftp scheme for File Transfer Protocol services
 * gopher://spinaltap.micro.umn.edu/00/Weather/California/Los%20Angeles
 *    -- gopher scheme for Gopher and Gopher+ Protocol services
 * http://www.math.uio.no/faq/compression-faq/part1.html
 *    -- http scheme for Hypertext Transfer Protocol services
 * mailto:mduerst@ifi.unizh.ch
 *    -- mailto scheme for electronic mail addresses
 * news:comp.infosystems.www.servers.unix
 *    -- news scheme for USENET news groups and articles
 * telnet://melvyl.ucop.edu/
 *    -- telnet scheme for interactive services via the TELNET Protocol
 * </pre>
 * Please, notice that there are many modifications from URL(RFC 1738) and
 * relative URL(RFC 1808).
 * <p>
 * <b>The expressions for a URI</b>
 * <p><pre>
 * For escaped URI forms
 *  - URI(char[]) // constructor
 *  - char[] getRawXxx() // method
 *  - String getEscapedXxx() // method
 *  - String toString() // method
 * <p>
 * For unescaped URI forms
 *  - URI(String) // constructor
 *  - String getXXX() // method
 * </pre><p>
 *
 * @author <a href="mailto:jericho@apache.org">Sung-Gu</a>
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @version $Revision: 1.1 $ $Date: 2002/03/14 15:14:01
 */
 class URI implements Comparable, Serializable {

	private static boolean MVSURE = true;
	private static boolean MV = true;

    // ----------------------------------------------------------- Constructors

    /** Create an instance as an internal use */
    protected URI() {
    }

    
    /**
     * Construct a URI from the given string.
     * <p><blockquote><pre>
     *   URI-reference =  URI / relative-ref
     * </pre></blockquote><p>
     * An URI can be placed within double-quotes or angle brackets like
     * "http://test.com/" and &lt;http://test.com/&gt;
     *
     * @param original the URI string representation,
     *  in percent escaped form.
     * @throws URIException If the URI cannot be created.
     */
    public URI(String original) throws URIException {
        parseUriReference(original);
    }


        /**
     * Construct a general URI from the given components.
     * <p><blockquote><pre>
     *   URI-reference = [ absoluteURI | relativeURI ] [ "#" fragment ]
     *   absoluteURI   = scheme ":" ( hier_part | opaque_part )
     *   opaque_part   = uric_no_slash *uric
     * </pre></blockquote><p>
     * It's for absolute URI = &lt;scheme&gt;:&lt;scheme-specific-part&gt;#
     * &lt;fragment&gt;.
     *
     * @param scheme the scheme string
     * @param schemeSpecificPart scheme_specific_part
     * @param fragment the fragment string
     * @throws URIException If the URI cannot be created.
     * @see #getDefaultProtocolCharset
     */
    public URI(String scheme, String schemeSpecificPart, String fragment)
        throws URIException {

        // validate and contruct the URI character sequence
        if (scheme == null) {
           throw new URIException(URIException.PARSING, "scheme required");
        }
        _scheme = validateScheme(scheme);
        
        _opaque = encode(schemeSpecificPart, allowed_opaque_part,
                getProtocolCharset());
        // Set flag
        _is_opaque_part = true;
        
        _fragment = fragment==null ? null : validateFragment(fragment); 

        setURI();
    }
    
    /**
     * Construct a general URI from the given components.
     * <p><blockquote><pre>
     *   URI-reference = [ absoluteURI | relativeURI ] [ "#" fragment ]
     *   absoluteURI   = scheme ":" ( hier_part | opaque_part )
     *   relativeURI   = ( net_path | abs_path | rel_path ) [ "?" query ]
     *   hier_part     = ( net_path | abs_path ) [ "?" query ]
     * </pre></blockquote><p>
     * It's for absolute URI = &lt;scheme&gt;:&lt;path&gt;?&lt;query&gt;#&lt;
     * fragment&gt; and relative URI = &lt;path&gt;?&lt;query&gt;#&lt;fragment
     * &gt;.
     *
     * @param scheme the scheme string
     * @param authority the authority string
     * @param path the path string
     * @param query the query string
     * @param fragment the fragment string
     * @throws URIException If the new URI cannot be created.
     * @see #getDefaultProtocolCharset
     */
    public URI(String scheme, String authority, String path, String query,
               String fragment) throws URIException {

		if (path == null) throw new IllegalArgumentException("Null path passed. " +
			"\nRFC 3986 3.3 A path is always defined for a URI, " +
			"though the defined path may be empty (zero length).");

		final char[] schemeChars;
		if (scheme!=null) {
			schemeChars = validateScheme(scheme);
	    }
	    else schemeChars = null;


		// MV: here must first escape the components
		// and then pass to parseURIReference(String,true)
		// otherwise path with %23 [#] is lost....
        // NEW: query and frag must be already escaped
        
        // validate and contruct the URI character sequence
        StringBuffer buff = new StringBuffer();
        if (schemeChars != null) {
            buff.append(schemeChars);
            buff.append(':');
        }
        if (authority != null) {
            buff.append("//");
            buff.append(authority);
        }
        if (path.length()!=0) {  // MV DO NOT check for empty path, was:  (path!=null) //accept empty path
            if ((scheme != null || authority != null)
                    && isPathARelativePath(path)) {
                throw new URIException(URIException.PARSING,
                        "abs_path requested");
            } else {


				final String escapedPath = escapePathAsString(path);
				checkNotEmptyEscapedPathAndAuthority(escapedPath, authority);
				buff.append(escapedPath);
			}


        }
        if (query != null) {
            buff.append('?');
            buff.append(query);
        }
        if (fragment != null) {
            buff.append('#');
            //buff.append(escapeFragmentAsString(fragment));
            buff.append(fragment); //2007remove
        }
        parseUriReference(buff.toString());
    }

    private char[] validateFragment(String fragment)
        throws URIException
    {
        char[] arr = fragment.toCharArray();
        if (arr.length==0) return EMPTYCHARS;
        else
        {
            if (!validate(arr, URI.ABNF_fragment)) {
		    	throw new URIException(URIException.PARSING, 
		    	    "incorrect fragment: [" + fragment + "]");
		    }
		    else return arr;
            
        }
    }

    private char[] validateQuery(String query)
        throws URIException
    {
        char[] arr = query.toCharArray();
        if (arr.length==0) return EMPTYCHARS;
        else
        {
            // remove the fragment identifier
            // OLD not needed
            // escapedQuery = removeFragmentIdentifier(escapedQuery);
            if (!validate(arr, URI.ABNF_query)) {
		    	throw new URIException(URIException.PARSING, 
		    	    "incorrect query: [" + query + "]");
		    }
		    else return arr;
        }
    }
    
        

    /**
     * Scheme validation
     */
	
	/**
     * Scheme validation 
     */
	private char[] validateScheme(String scheme) 
	    throws URIException 
	{
		
		final char[] schemeChars = scheme.toCharArray();
		if (schemeChars.length==0) {
			throw new URIException(URIException.PARSING, 
			    "scheme cannot be an empty string " + 
			    "(URI cannot start with colon ':').");
		}
		if (!ALPHA.get(schemeChars[0])) {
			throw new URIException(URIException.PARSING, "scheme must start with letter (a-z A-Z) : [" + new String(schemeChars) + "]");
		}
		else if (!validate(schemeChars, URI.ABNF_scheme)) {
			throw new URIException(URIException.PARSING, "incorrect scheme: [" + new String(schemeChars) + "]");
		}
		else return schemeChars;
	}

	private void checkNotEmptyEscapedPathAndAuthority(String path, String auth) throws URIException {

		// RFC3986 3.3
		// (..) If a URI  does not contain an authority component, then the path cannot begin
		// with two slash characters ("//").
		// NOTE: empty string is a present, empty authority component

		if (auth==null && (path.startsWith("//")))
		{
			throw new URIException("RFC3986 3.3 if an URI "
				+ "does not contain an authority component "
				+ "then the path cannot begin with [//]. Escaped path: [" + path + "]");
		}

		if (isPathARelativePath(path)) {
			// RFC 3986 3.3
			// If the path is a relative path,
			// and so the uri is a relative path reference,
			// it cannot contain a colon in its first segment.
			// RFC 3986 4.2
			// (..)A path segment that contains a colon character (e.g., "this:that")
			// cannot be used as the first segment of a relative-path reference, as
			// it would be mistaken for a scheme name.  Such a segment must be
			// preceded by a dot-segment (e.g., "./this:that") to make a relative-
 			// path reference.
 			// -------------
 			// Yes but after normalization ?
 			// Relative paths cannot be normalized !!!
 			// Found a good reason for not normalizing.
			final int indxColon = path.indexOf(":");
			final int indxSlash = path.indexOf("/");
			if (indxColon >= 0 && (indxSlash==-1 || indxColon < indxSlash)) {
				// If there is a colon in the rel path and
				// (the first slash is not present or occurs after the colon),
				// then it MUST start with ./
				if (!path.startsWith("./")) throw new URIException("RFC3986 3.3  a URI reference"
					+ " may be a relative-path reference, in which case the "
					+ " first path segment cannot contain a colon (\":\") character. Escaped path:" + path);

			}
		}

	}
	/**
	 * @path NON NULL path
	 * @returt true if relative path
	 */
	private static boolean isPathARelativePath(String path)
	{
		return !path.startsWith("/");
	}

	private char[] escapePath(final String path) throws URIException
	{
		if (path == null ) return null;
		else if (path.length() == 0) return EMPTYCHARS;
		else return _escapePathAsString(path).toCharArray();
	}
	private String escapePathAsString(final String path) throws URIException
	{
		if (path == null || path.length() == 0) {
			return path;
		}
		else return _escapePathAsString(path);
	}

	/**
	 * @param NON NULL and NON EMPTY path
	 */
	private String _escapePathAsString(final String path) throws URIException
	{
		// set the charset to do escape encoding
		final String charset = getProtocolCharset();

		final boolean isRelativePath = isPathARelativePath(path);
		final String result;
		if (!isRelativePath) {
			result = new String(encode(path, allowed_abs_path, charset));
			//System.out.println("MV: abs or net path escaped is " + result);
		} else { //////////if (_is_rel_path) {
			StringBuffer buff = new StringBuffer(path.length());
			int at = path.indexOf('/');
			if (at == 0) { // never 0
				throw new URIException(URIException.PARSING,
						"incorrect relative path");
			}
			if (at > 0) {
				buff.append(encode(path.substring(0, at), allowed_rel_path,
							charset));
				buff.append(encode(path.substring(at), allowed_abs_path,
							charset));
			} else {
				buff.append(encode(path, allowed_rel_path, charset));
			}
			result = buff.toString(); //.toCharArray();
			//System.out.println("MV: rel path escaped is " + result);

		} /** else if (_is_opaque_part) {
			StringBuffer buf = new StringBuffer();
			buf.insert(0, encode(path.substring(0, 1), uric_no_slash, charset));
			buf.insert(1, encode(path.substring(1), uric, charset));
			_opaque = buf.toString().toCharArray();
		} else {
			throw new URIException(URIException.PARSING, "incorrect path");
        } **/
        return result;
	}





    /**
     * Construct a general URI with the given relative URI string.
     *
     * @param base the base URI
     * @param relative the relative URI string, 
     *      in percent escaped form.
     * @throws URIException If the new URI cannot be created.
     *
     */
    public URI(URI base, String relative) throws URIException {
        this(base, new URI(relative));
    }


    /**
     * Construct a general URI with the given relative URI.
     * <p><blockquote><pre>
     *   URI-reference = [ absoluteURI | relativeURI ] [ "#" fragment ]
     *   relativeURI   = ( net_path | abs_path | rel_path ) [ "?" query ]
     * </pre></blockquote><p>
     * Resolving Relative References to Absolute Form.
     *
     * <strong>Examples of Resolving Relative URI References</strong>
     *
     * Within an object with a well-defined base URI of
     * <p><blockquote><pre>
     *   http://a/b/c/d;p?q
     * </pre></blockquote><p>
     * the relative URI would be resolved as follows:
     *
     * Normal Examples
     *
     * <p><blockquote><pre>
     *   g:h           =  g:h
     *   g             =  http://a/b/c/g
     *   ./g           =  http://a/b/c/g
     *   g/            =  http://a/b/c/g/
     *   /g            =  http://a/g
     *   //g           =  http://g
     *   ?y            =  http://a/b/c/?y
     *   g?y           =  http://a/b/c/g?y
     *   #s            =  (current document)#s
     *   g#s           =  http://a/b/c/g#s
     *   g?y#s         =  http://a/b/c/g?y#s
     *   ;x            =  http://a/b/c/;x
     *   g;x           =  http://a/b/c/g;x
     *   g;x?y#s       =  http://a/b/c/g;x?y#s
     *   .             =  http://a/b/c/
     *   ./            =  http://a/b/c/
     *   ..            =  http://a/b/
     *   ../           =  http://a/b/
     *   ../g          =  http://a/b/g
     *   ../..         =  http://a/
     *   ../../        =  http://a/
     *   ../../g       =  http://a/g
     * </pre></blockquote><p>
     *
     * Some URI schemes do not allow a hierarchical syntax matching the
     * <hier_part> syntax, and thus cannot use relative references.
     *
     * @param base the base URI
     * @param relative the relative URI
     * @throws URIException If the new URI cannot be created.
     */
    public URI(URI base, URI relative) throws URIException {

		// RFC 3986 5.2.1 says no scheme, no resolving
		// TODO add to "strict" option
        if (base._scheme == null) {
            throw new URIException(URIException.PARSING, "base URI required");
        }
        else if (base._is_opaque_part || relative._is_opaque_part) {
			// Fix one is opaque case
			// use copyOpaqueAllButScheme
			base.copyContext(this);
            this._scheme = base._scheme;
            this._is_opaque_part = base._is_opaque_part
                || relative._is_opaque_part;
            this._opaque = relative._opaque; // ??? TODO
            this._path = this._opaque; // ??? TODO
            this._fragment = relative._fragment;
            // Always call setURI before exiting
            this.setURI();
            return;
        }

		// Here base is absolute uri,
		// none of the two is opaque


		// Fix cases relative is absolute and relative
		// with authority

        if (relative._scheme != null) {
			// Case relative is absolute URI
			// clone the relative apart fragment
            this._scheme = relative._scheme;
            relative.copyNotOpaqueAllButScheme(this, false);
        } else if (relative._authority != null) {
			// Case network path //myhost[..]
			// Clone all but scheme and fragment of the base
            this._scheme = base._scheme;
			relative.copyNotOpaqueAllButScheme(this, false);
        } else {

			// Here relative has no scheme and no authority,
			// copy the scheme and authority part from the base to the
			// result uri (this for the moment)
			this._scheme = base._scheme;
			base.copyContext(this);

			if (base._authority != null ) {
				base.copyAuthority(this, false);
            }
            // Here remains to solve path, query and fragment.
            // If the relative has no path bu query and/or fragment
            // the base path must be used.

			if (relative._path.length == 0) {
				// Remember: Resolved fragment, if present, is ALWAYS fragment of relative
				if (relative._query==null) {
					if (relative._fragment==null) {
						// Relative has no scheme, auth, path, query and fragm
						// so is the empty uri, clone the base uri query (path done later)
						this._query = base._query;
					} else {
						this._fragment = relative._fragment;
						this._query = base._query;
					}
				} else {
					// Rel (that is rel uri, empty auth/path)
					// has query: copy query and possible fragment
					relative.copyQueryFragment(this);
				}
				base.copyPath(this);

			} else {
				// Relative has path, resolve base path with rel
				// and use query and frag from rel
				this._path = resolvePath(base._path, relative._path);
				relative.copyQueryFragment(this);

			}
		}
		this.setURI();
	}




    // --------------------------------------------------- Instance Variables


    /** Version ID for serialization */
    static final long serialVersionUID = 604752400577948726L;


    /**
     * Cache the hash code for this URI.
     */
    protected int hash = 0;


    /**
     * This Uniform Resource Identifier (URI).
     * The URI is always in an "escaped" form, since escaping or unescaping
     * a completed URI might change its semantics.
     */
    protected char[] _uri = null;


    /**
     * The charset of the protocol used by this URI instance.
     */
    protected String protocolCharset = null;


    /**
     * The default charset of the protocol.  RFC 2277, 2396
     */
    protected static String defaultProtocolCharset = "UTF-8";


    /**
     * The default charset of the document.  RFC 2277, 2396
     * The platform's charset is used for the document by default.
     */
    protected static String defaultDocumentCharset;
    
    protected static final String defaultDocumentCharsetByLocale;
    protected static final String defaultDocumentCharsetByPlatform;
    // Static initializer for defaultDocumentCharset
    static {
        Locale locale = Locale.getDefault();
        // in order to support backward compatiblity
        String locDefaultDocumentCharset;
        if (locale != null) {
            defaultDocumentCharsetByLocale =
                LocaleToCharsetMap.getCharset(locale);
            // set the default document charset
            locDefaultDocumentCharset = defaultDocumentCharsetByLocale;
        } else {
            locDefaultDocumentCharset = null;
            defaultDocumentCharsetByLocale  = null;
        }
        // in order to support platform encoding
        String locDefaultDocumentCharsetByPlatform;
        try {
            locDefaultDocumentCharsetByPlatform = System.getProperty("file.encoding");
        } catch (SecurityException ignore) {
            locDefaultDocumentCharsetByPlatform = null;
        }
        defaultDocumentCharsetByPlatform = locDefaultDocumentCharsetByPlatform;
        
        if (locDefaultDocumentCharset == null) {
            // set the default document charset
            defaultDocumentCharset = defaultDocumentCharsetByPlatform;
        } else {
            defaultDocumentCharset = locDefaultDocumentCharset;
        }
    }


    /**
     * The scheme.
     */
    protected char[] _scheme = null;


    /**
     * The opaque.
     */
    protected char[] _opaque = null;


    /**
     * The authority.
     */
    protected char[] _authority = null;


    /**
     * The userinfo.
     */
    protected char[] _userinfo = null;


    /**
     * The host.
     */
    protected char[] _host = null;


    /**
     * The port.
     */
    protected int _port = -1;


    /**
     * The path.
     */
    protected char[] _path = null;


    /**
     * The query.
     */
    protected char[] _query = null;


    /**
     * The fragment.
     */
    protected char[] _fragment = null;


    /**
     * The root path.
     */
    protected static char[] rootPath = { '/' };

    // ---------------------- Bitsets for components validation

    /**
     * The percent "%" character always has the reserved purpose of being the
     * escape indicator, it must be escaped as "%25" in order to be used as
     * data within a URI.
     */
    protected static final BitSet PERCENT = new BitSet(256);
    static {
        PERCENT.set('%');
    }


    /**
     * BitSet for DIGIT,
     * specified in RFC 4234,
     * <a href='http://ietfreport.isoc.org/rfc/rfc4234.txt'>
     * <em>Augmented BNF for Syntax Specifications: ABNF</em></a>.
     * <p><blockquote><pre>
     * DIIGT =  0 / 9
     * </pre></blockquote><p>
     */
    protected static final BitSet DIGIT = new BitSet(256);
    static {
        for (int i = '0'; i <= '9'; i++) {
            DIGIT.set(i);
        }
    }


    /**
     * BitSet for ALPHA, specified in RFC 4234,
     * <a href='http://ietfreport.isoc.org/rfc/rfc4234.txt'>
     * <em>Augmented BNF for Syntax Specifications: ABNF</em></a>.
     * <p><blockquote><pre>
     * ALPHA =  A-Z / a-z
     * </pre></blockquote><p>
     */
    protected static final BitSet ALPHA = new BitSet(256);
    static {
        for (int i = 'a'; i <= 'z'; i++) {
            ALPHA.set(i);
        }
        for (int i = 'A'; i <= 'Z'; i++) {
            ALPHA.set(i);
        }
    }



    /**
     * BitSet for HEXDIG,
     * specified in RFC 4234,
     * <a href='http://ietfreport.isoc.org/rfc/rfc4234.txt'>
     * <em>Augmented BNF for Syntax Specifications: ABNF</em></a>.
     * <p><blockquote><pre>
     * HEXDIG=  DIGIT / "A" / "B" / "C" / "D" / "E" / "F"
     *
     * Note: ABNF Strings are case unsensitive.
     * </pre></blockquote><p>
     */
    protected static final BitSet HEXDIG = new BitSet(256);
    static {
        HEXDIG.or(DIGIT);
        for (int i = 'a'; i <= 'f'; i++) {
            HEXDIG.set(i);
        }
        for (int i = 'A'; i <= 'F'; i++) {
            HEXDIG.set(i);
        }
    }


	/**
	 * BitSet for ALPHADIGIT 
	 * (join of {@link #ALPHA} &amp; {@link #DIGIT}).
	 * Implementation helper.
	 * <p><blockquote><pre>
	 *  ALPHADIGIT      = ALPHA / DIGIT
	 * </pre></blockquote><p>
	 */
	protected static final BitSet ALPHADIGIT = new BitSet(256);
	static {
		ALPHADIGIT.or(ALPHA);
		ALPHADIGIT.or(DIGIT);
	}



    /**
     * BitSet for <code>pct-encoded</code>,
     * specified as an ABNF in 
     * <a href='http://www.apps.ietf.org/rfc/rfc3986.html#sec-2.1'>
     * <em>RFC3986 Section 2.1</em></a>.
     * <p><blockquote>
     * <p>A percent-encoding mechanism is used to represent 
     * a data octet in a component when that octet's 
     * corresponding character is outside the allowed 
     * set or is being used as a delimiter of, or within, 
     * the component. A percent-encoded octet is encoded 
     * as a character triplet, consisting of the percent 
     * character "%" followed by the two hexadecimal digits 
     * representing that octet's numeric value. 
     * <p><pre>
     * pct_encoded       = "%" HEXDIG HEXDIG
     * </pre></blockquote><p>
     */
    protected static final BitSet ABNF_pct_encoded = new BitSet(256);
    static {
        ABNF_pct_encoded.or(PERCENT);
        ABNF_pct_encoded.or(HEXDIG);
    }

	/**
     * BitSet for <code>scheme</code>,
     * specified as an ABNF in 
     * <a href='http://www.apps.ietf.org/rfc/rfc3986.html#sec-3.1'>
     * <em>RFC3986 Section 3.1</em></a>.
     * <p><blockquote>
     *
     * <p>Each URI begins with a scheme name that refers to 
     * a specification for assigning identifiers within that 
     * scheme. As such, the URI syntax is a federated and 
     * extensible naming system wherein each scheme's specification 
     * may further restrict the syntax and semantics of 
     * identifiers using that scheme
     *
     * <p>Scheme names consist of a sequence of characters 
     * beginning with a letter and followed by any combination 
     * of letters, digits, plus ("+"), period ("."), or hyphen ("-"). 
     * Although schemes are case- insensitive, the canonical form 
     * is lowercase and documents that specify schemes must do so 
     * with lowercase letters. An implementation should accept 
     * uppercase letters as equivalent to lowercase in 
     * scheme names (e.g., allow "HTTP" as well as "http") for 
     * the sake of robustness but should only produce lowercase 
     * scheme names for consistency
     * <pre>
     * scheme        = ALPHA *( ALPHA / DIGIT / "+" / "-" / "." )
     * </pre></blockquote><p>
     */
    protected static final BitSet ABNF_scheme = new BitSet(256);
    // Static initializer for scheme
    static {
        ABNF_scheme.or(ALPHA);
        ABNF_scheme.or(DIGIT);
        ABNF_scheme.set('+');
        ABNF_scheme.set('-');
        ABNF_scheme.set('.');
    }







	/**
     * BitSet for <code>gen-delims</code>,
     * specified as an ABNF in 
     * <a href='http://www.apps.ietf.org/rfc/rfc3986.html#sec-2.2'>
     * <em>RFC3986 Section 2.2</em></a>.
     * <p><blockquote>
     * <p><pre>
     * gen-delims  = ":" / "/" / "?" / "#" / "[" / "]" / "@"
     * </pre></blockquote><p>
     * @see #ABNF_sub_delims
     * @see #ABNF_reserved
     */
    protected static final BitSet ABNF_gen_delims = new BitSet(256);
	static {
		ABNF_gen_delims.set(':');
		ABNF_gen_delims.set('/');
		ABNF_gen_delims.set('?');
		ABNF_gen_delims.set('#');
		ABNF_gen_delims.set('[');
		ABNF_gen_delims.set(']');
		ABNF_gen_delims.set('@');
	}

	/**
     * BitSet for <code>sub-delims</code>,
     * specified as an ABNF in 
     * <a href='http://www.apps.ietf.org/rfc/rfc3986.html#sec-2.2'>
     * <em>RFC3986 Section 2.2</em></a>.
     * <p><blockquote>
     * <p><pre>
     * sub-delims  = "!" / "$" / "&amp;" / "'" / "(" / ")"
     *             / "*" / "+" / "," / ";" / "="
     *
     * </pre></blockquote><p>
     * @see #ABNF_gen_delims
     * @see #ABNF_reserved
     */
	protected static final BitSet ABNF_sub_delims = new BitSet(256);
	static {
		ABNF_sub_delims.set('!');
		ABNF_sub_delims.set('$');
		ABNF_sub_delims.set('&');
		ABNF_sub_delims.set('\'');
		ABNF_sub_delims.set('(');
		ABNF_sub_delims.set(')');
		ABNF_sub_delims.set('*');
		ABNF_sub_delims.set('+');
		ABNF_sub_delims.set(',');
		ABNF_sub_delims.set(';');
		ABNF_sub_delims.set('=');

	}

    /**
     * BitSet for <code>reserved</code>,
     * specified as an ABNF in 
     * <a href='http://www.apps.ietf.org/rfc/rfc3986.html#sec-2.2'>
     * <em>RFC3986 Section 2.2</em></a>.
     * <p><blockquote>
     * <p>URIs include components and subcomponents that 
     * are delimited by characters in the "reserved" set. 
     * These characters are called "reserved" because 
     * they may (or may not) be defined as delimiters 
     * by the generic syntax, by each scheme-specific 
     * syntax, or by the implementation-specific 
     * syntax of a URI's dereferencing algorithm. 
     * If data for a URI component would conflict 
     * with a reserved character's purpose as a delimiter, 
     * then the conflicting data must be 
     * percent-encoded before the URI is formed.
     *
     * <p><pre>
     * reserved    = gen-delims / sub-delims
     * </pre></blockquote><p>
     * @see #ABNF_gen_delims
     * @see #ABNF_sub_delims
     */
	protected static final BitSet ABNF_reserved = new BitSet(256);
	static {
		ABNF_reserved.or(ABNF_gen_delims);
		ABNF_reserved.or(ABNF_sub_delims);
	}
    /**
     * BitSet for <code>unreserved</code>,
     * specified as an ABNF in 
     * <a href='http://www.apps.ietf.org/rfc/rfc3986.html#sec-2.3'>
     * <em>RFC3986 Section 2.3</em></a>.
     * <p><blockquote>
     * <p>Characters that are allowed in a URI but 
     * do not have a reserved purpose are called unreserved. 
     * These include uppercase and lowercase letters, decimal 
     * digits, hyphen, period, underscore, and tilde. 
     * <p><pre>
     * unreserved =  ALPHA / DIGIT / "-" / "." / "_" / "~"
     * </pre></blockquote><p>
     */
    protected static final BitSet ABNF_unreserved = new BitSet(256);
    static {
        ABNF_unreserved.or(ALPHADIGIT);
        ABNF_unreserved.set('-');
        ABNF_unreserved.set('.');
        ABNF_unreserved.set('_');
        ABNF_unreserved.set('~');
    }

	
	/**
	 * BitSet for pchar.
	 * <p><blockquote><pre>
	 * uric          = unreserved / pct-encoded / sub-delims / ":" / "@"
	 * </pre></blockquote><p>
	 */
	protected static final BitSet pchar = new BitSet(256);
	static {
		pchar.or(ABNF_unreserved);
		pchar.or(ABNF_pct_encoded);
		pchar.or(ABNF_sub_delims);
		pchar.set(':');
		pchar.set('@');
	}

	/**
	 * Helper BitSet common to fragment and query.
	 * <p><blockquote><pre>
	 * query       = *( pchar / "/" / "?" )
	 * </pre></blockquote><p>
	 */
	private static final BitSet ABNFHELPER_fragment_query = new BitSet(256);
	static {
		ABNFHELPER_fragment_query.or(pchar);
		ABNFHELPER_fragment_query.set('/');
		ABNFHELPER_fragment_query.set('?');
	}



	/**
     * BitSet for <code>query</code>,
     * specified as an ABNF in 
     * <a href='http://www.apps.ietf.org/rfc/rfc3986.html#sec-3.4'>
     * <em>RFC3986 Section 3.4</em></a>.
     * <p><blockquote>
     * <p>The query component contains non-hierarchical 
     * data that, along with data in the path component 
     * (Section 3.3), serves to identify a resource 
     * within the scope of the URI's scheme and naming 
     * authority (if any). The query component is indicated 
     * by the first question mark ("?") character and 
     * terminated by a number sign ("#") character or 
     * by the end of the URI. 
     * <p><blockquote><pre>
	 * query         = *( pchar / "/" / "?" )
	 * </pre></blockquote><p>
	 * @see ABNF_fragment They have the same bit set.
	 */
	protected static final BitSet ABNF_query = ABNFHELPER_fragment_query;

	/**
	 * BitSet for fragment.
	 * <p><blockquote><pre>
	 * fragment         = *( pchar / "/" / "?" )
	 * </pre></blockquote><p>
	 */
	protected static final BitSet ABNF_fragment = ABNFHELPER_fragment_query;



	//////////////////// MV up to here ok with RFC3986

    
	/**
     * BitSet for uric.
     * <p><blockquote><pre>
     * uric          = reserved | unreserved | pct_encoded
     * </pre></blockquote><p>
    protected static final BitSet uric = new BitSet(256);
    // Static initializer for uric
    static {
        uric.or(ABNF_reserved);
        uric.or(ABNF_unreserved);
        uric.or(ABNF_pct_encoded);
    }

 */

    /**
     * BitSet for segment.
     * <p><blockquote><pre>
     * segment       = *pchar
     * </pre></blockquote><p>
     */
    protected static final BitSet segment = new BitSet(256);
    // Static initializer for segment
    static {
        segment.or(pchar);
    }


    /**
     * BitSet for path segments.
     * <p><blockquote><pre>
     * path_segments = segment *( "/" segment )
     * </pre></blockquote><p>
     */
    protected static final BitSet path_segments = new BitSet(256);
    // Static initializer for path_segments
    static {
        path_segments.set('/');
        path_segments.or(segment);
    }


    /**
     * URI absolute path.
     * <p><blockquote><pre>
     * abs_path      = "/"  path_segments
     * </pre></blockquote><p>
     */
    protected static final BitSet abs_path = new BitSet(256);
    // Static initializer for abs_path
    static {
        abs_path.set('/');
        abs_path.or(path_segments);
    }


    /**
     * URI bitset for encoding typical non-slash characters.
     * <p><blockquote><pre>
     * uric_no_slash = unreserved | pct_encoded | ";" | "?" | ":" | "@" |
     *                 "&amp;" | "=" | "+" | "$" | ","
     * </pre></blockquote><p>
     */
    protected static final BitSet uric_no_slash = new BitSet(256);
    // Static initializer for uric_no_slash
    static {
        uric_no_slash.or(ABNF_unreserved);
        uric_no_slash.or(ABNF_pct_encoded);
        uric_no_slash.set(';');
        uric_no_slash.set('?');
        uric_no_slash.set(':'); //MV was duplicate ; ... is :
        uric_no_slash.set('@');
        uric_no_slash.set('&');
        uric_no_slash.set('=');
        uric_no_slash.set('+');
        uric_no_slash.set('$');
        uric_no_slash.set(',');
    }


	private static final BitSet uric = pchar;// TEMP UNTIL FIX PATH

    /**
     * URI bitset that combines uric_no_slash and uric.
     * <p><blockquote><pre>
     * opaque_part   = uric_no_slash *uric
     * </pre></blockquote><p>
     */
    protected static final BitSet opaque_part = new BitSet(256);
    // Static initializer for opaque_part
    static {
        // it's generous. because first character must not include a slash
        opaque_part.or(uric_no_slash);
        opaque_part.or(uric);
        // added MV TODO rationalize
        opaque_part.set('/');
        
    }


    /**
     * URI bitset that combines absolute path and opaque part.
     * <p><blockquote><pre>
     * path          = [ abs_path | opaque_part ]
     * </pre></blockquote><p>
     */
    protected static final BitSet path = new BitSet(256);
    // Static initializer for path
    static {
        path.or(abs_path);
        path.or(opaque_part);
    }


    /**
     * Port, a logical alias for DIGIT.
     */
    protected static final BitSet ABNF_port = DIGIT;


    /**
     * Bitset that combines DIGIT and dot fo IPv$address.
     * <p><blockquote><pre>
     * IPv4address   = 1*DIGIT "." 1*DIGIT "." 1*DIGIT "." 1*DIGIT
     * </pre></blockquote><p>
     */
    protected static final BitSet IPv4address = new BitSet(256);
    // Static initializer for IPv4address
    static {
        IPv4address.or(DIGIT);
        IPv4address.set('.');
    }


    /**
     * RFC 2373.
     * <p><blockquote><pre>
     * IPv6address = hexpart [ ":" IPv4address ]
     * </pre></blockquote><p>
     */
    protected static final BitSet IPv6address = new BitSet(256);
    // Static initializer for IPv6address reference
    static {
        IPv6address.or(HEXDIG); // hexpart
        IPv6address.set(':');
        IPv6address.or(IPv4address);
    }


    /**
     * RFC 2732, 2373.
     * <p><blockquote><pre>
     * IPv6reference   = "[" IPv6address "]"
     * </pre></blockquote><p>
     */
    protected static final BitSet IPv6reference = new BitSet(256);
    // Static initializer for IPv6reference
    static {
        IPv6reference.set('[');
        IPv6reference.or(IPv6address);
        IPv6reference.set(']');
    }


    /**
     * BitSet for toplabel.
     * <p><blockquote><pre>
     * toplabel      = ALPHA | ALPHA *( ALPHADIGIT | "-" ) ALPHADIGIT
     * </pre></blockquote><p>
     */
    protected static final BitSet toplabel = new BitSet(256);
    // Static initializer for toplabel
    static {
        toplabel.or(ALPHADIGIT);
        toplabel.set('-');
    }


    /**
     * BitSet for domainlabel.
     * <p><blockquote><pre>
     * domainlabel   = ALPHADIGIT | ALPHADIGIT *( ALPHADIGIT | "-" ) ALPHADIGIT
     * </pre></blockquote><p>
     */
    protected static final BitSet domainlabel = toplabel;

	/**
	authority     = [ userinfo "@" ] host [ ":" port ]
	 userinfo      = *( unreserved / pct-encoded / sub-delims / ":" )
	 host          = IP-literal / IPv4address / reg-name
	 port          = *DIGIT

**/

    /**
     * BitSet for hostname.
     * <p><blockquote><pre>
     * hostname      = *( domainlabel "." ) toplabel [ "." ]
     * </pre></blockquote><p>
     */
    protected static final BitSet hostname = new BitSet(256);
    // Static initializer for hostname
    static {
        hostname.or(toplabel);
        // hostname.or(domainlabel);
        hostname.set('.');
    }


    /**
     * BitSet for host.
     * <p><blockquote><pre>
     * host          = hostname | IPv4address | IPv6reference
     * </pre></blockquote><p>
     */
    protected static final BitSet host = new BitSet(256);
    // Static initializer for host
    static {
        host.or(hostname);
        // host.or(IPv4address);
        host.or(IPv6reference); // IPv4address
    }


    /**
     * BitSet for hostport.
     * <p><blockquote><pre>
     * hostport      = host [ ":" port ]
     * </pre></blockquote><p>
     */
    protected static final BitSet hostport = new BitSet(256);
    // Static initializer for hostport
    static {
        hostport.or(host);
        hostport.set(':');
        hostport.or(ABNF_port);
    }


    /**
     * Bitset for userinfo.
     * <p><blockquote><pre>
	 * userinfo      = *( unreserved / pct-encoded / sub-delims / ":" )
     * </pre></blockquote><p>
     */
    protected static final BitSet userinfo = new BitSet(256);
    // Static initializer for userinfo
    static {
        userinfo.or(ABNF_unreserved);
        userinfo.or(ABNF_pct_encoded);
        userinfo.or(ABNF_sub_delims);
        userinfo.set(':');
    }


    /**
     * BitSet for within the userinfo component like user and password.
     */
    public static final BitSet within_userinfo = new BitSet(256);
    // Static initializer for within_userinfo
    static {
        within_userinfo.or(userinfo);
        within_userinfo.clear(';'); // reserved within authority
        within_userinfo.clear(':');
        within_userinfo.clear('@');
        within_userinfo.clear('?');
        within_userinfo.clear('/');
    }


    /**
     * Bitset for server.
     * <p><blockquote><pre>
     * server        = [ [ userinfo "@" ] hostport ]
     * </pre></blockquote><p>
     */
    protected static final BitSet ABNF_authority = new BitSet(256);
    // Static initializer for ABNF_authority
    static {
        ABNF_authority.or(userinfo);
        ABNF_authority.set('@');
        ABNF_authority.or(hostport);
    }


    /**
     * BitSet for reg_name.
     * <p><blockquote><pre>
     * reg_name      = 1*( unreserved | pct_encoded | "$" | "," |
     *                     ";" | ":" | "@" | "&amp;" | "=" | "+" )
     * </pre></blockquote><p>
     */
    protected static final BitSet reg_name = new BitSet(256);
    // Static initializer for reg_name
    static {
        reg_name.or(ABNF_unreserved);
        reg_name.or(ABNF_pct_encoded);
        reg_name.set('$');
        reg_name.set(',');
        reg_name.set(';');
        reg_name.set(':');
        reg_name.set('@');
        reg_name.set('&');
        reg_name.set('=');
        reg_name.set('+');
    }


    /**
     * BitSet for authority.
     * <p><blockquote><pre>
     * authority     = ABNF_authority | reg_name
     * </pre></blockquote><p>
     */
    protected static final BitSet authority = new BitSet(256);
    // Static initializer for authority
    static {
        authority.or(ABNF_authority);
        authority.or(reg_name);
    }


    

    /**
     * BitSet for rel_segment.
     * <p><blockquote><pre>
     * rel_segment   = 1*( unreserved | pct_encoded |
     *                     ";" | "@" | "&amp;" | "=" | "+" | "$" | "," )
     * </pre></blockquote><p>
     */
    protected static final BitSet rel_segment = new BitSet(256);
    // Static initializer for rel_segment
    static {
        rel_segment.or(ABNF_unreserved);
        rel_segment.or(ABNF_pct_encoded);
        rel_segment.set(';');
        rel_segment.set('@');
        rel_segment.set('&');
        rel_segment.set('=');
        rel_segment.set('+');
        rel_segment.set('$');
        // MOVED TO RESERVED if (MV) rel_segment.set('#'); // MV ADDED 200601
        if (MV) rel_segment.clear('#'); // MV ADDED 200601
    	if (MV) rel_segment.clear('?'); // MV ADDED 200601

        rel_segment.set(',');
    }


    /**
     * BitSet for rel_path.
     * <p><blockquote><pre>
     * rel_path      = rel_segment [ abs_path ]
     * </pre></blockquote><p>
     */
    protected static final BitSet rel_path = new BitSet(256);
    // Static initializer for rel_path
    static {
        rel_path.or(rel_segment);
        rel_path.or(abs_path);
    }


    /**
     * BitSet for net_path.
     * <p><blockquote><pre>
     * net_path      = "//" authority [ abs_path ]
     * </pre></blockquote><p>
     */
    protected static final BitSet net_path = new BitSet(256);
    // Static initializer for net_path
    static {
        net_path.set('/');
        net_path.or(authority);
        net_path.or(abs_path);
    }


    /**
     * BitSet for hier_part.
     * <p><blockquote><pre>
     * hier_part     = ( net_path | abs_path ) [ "?" query ]
     * </pre></blockquote><p>
     */
    protected static final BitSet hier_part = new BitSet(256);
    // Static initializer for hier_part
    static {
        hier_part.or(net_path);
        hier_part.or(abs_path);
        // hier_part.set('?'); aleady included
        hier_part.or(ABNF_query);
    }


    /**
     * BitSet for relativeURI.
     * <p><blockquote><pre>
     * relativeURI   = ( net_path | abs_path | rel_path ) [ "?" query ]
     * </pre></blockquote><p>
     */
    protected static final BitSet relativeURI = new BitSet(256);
    // Static initializer for relativeURI
    static {
        relativeURI.or(net_path);
        relativeURI.or(abs_path);
        relativeURI.or(rel_path);
        // relativeURI.set('?'); aleady included
        relativeURI.or(ABNF_query);
    }


    /**
     * BitSet for absoluteURI.
     * <p><blockquote><pre>
     * absoluteURI   = scheme ":" ( hier_part | opaque_part )
     * </pre></blockquote><p>
     */
    protected static final BitSet absoluteURI = new BitSet(256);
    // Static initializer for absoluteURI
    static {
        absoluteURI.or(ABNF_scheme);
        absoluteURI.set(':');
        absoluteURI.or(hier_part);
        absoluteURI.or(opaque_part);
    }


    /**
     * BitSet for URI-reference.
     * <p><blockquote><pre>
     * URI-reference = [ absoluteURI | relativeURI ] [ "#" fragment ]
     * </pre></blockquote><p>
     */
    protected static final BitSet URI_reference = new BitSet(256);
    // Static initializer for URI_reference
    static {
        URI_reference.or(absoluteURI);
        URI_reference.or(relativeURI);
        URI_reference.set('#');
        URI_reference.or(ABNF_fragment);
    }

    // ---------------------------- Characters disallowed within the URI syntax
    // Excluded US-ASCII Characters are like control, space, delims and unwise

    /**
     * BitSet for control.
     */
    public static final BitSet control = new BitSet(256);
    // Static initializer for control
    static {
        for (int i = 0; i <= 0x1F; i++) {
            control.set(i);
        }
        control.set(0x7F);
    }

    /**
     * BitSet for space.
     */
    public static final BitSet space = new BitSet(256);
    // Static initializer for space
    static {
        space.set(0x20);
    }

	/**
     * BitSet for delims.
     */
    public static final BitSet context_delims = new BitSet(256);
    // Static initializer for delims
    static {
        context_delims.set('<');
        context_delims.set('>');
        context_delims.set('"');
    }


    /**
     * BitSet for delims.
     */
    public static final BitSet delims = new BitSet(256);
    // Static initializer for delims
    static {
        delims.set('<');
        delims.set('>');
        delims.set('#');
        delims.set('%');
        delims.set('"');
    }


    /**
     * BitSet for unwise.
     */
    public static final BitSet unwise = new BitSet(256);
    // Static initializer for unwise
    static {
        unwise.set('{');
        unwise.set('}');
        unwise.set('|');
        unwise.set('\\');
        unwise.set('^');
        unwise.set('[');
        unwise.set(']');
        unwise.set('`');
    }


    // ----------------------- Characters allowed within and for each component

    /**
     * Those characters that are allowed for the authority component.
     */
    public static final BitSet allowed_authority = new BitSet(256);
    // Static initializer for allowed_authority
    static {
        allowed_authority.or(ABNF_authority);
        allowed_authority.clear('%');
    }


    /**
     * Those characters that are allowed for the opaque_part.
     */
    public static final BitSet allowed_opaque_part = new BitSet(256);
    // Static initializer for allowed_opaque_part
    static {
        allowed_opaque_part.or(opaque_part);
        allowed_opaque_part.clear('%');
        
    }


    /**
     * Those characters that are allowed for the reg_name.
     */
    public static final BitSet allowed_reg_name = new BitSet(256);
    // Static initializer for allowed_reg_name
    static {
        allowed_reg_name.or(reg_name);
        // allowed_reg_name.andNot(PERCENT);
        allowed_reg_name.clear('%');
    }


    /**
     * Those characters that are allowed for the userinfo component.
     */
    public static final BitSet allowed_userinfo = new BitSet(256);
    // Static initializer for allowed_userinfo
    static {
        allowed_userinfo.or(userinfo);
        // allowed_userinfo.andNot(PERCENT);
        allowed_userinfo.clear('%');
    }


    /**
     * Those characters that are allowed for within the userinfo component.
     */
    public static final BitSet allowed_within_userinfo = new BitSet(256);
    // Static initializer for allowed_within_userinfo
    static {
        allowed_within_userinfo.or(within_userinfo);
        allowed_within_userinfo.clear('%');
    }


    /**
     * Those characters that are allowed for the IPv6reference component.
     * The characters '[', ']' in IPv6reference should be excluded.
     */
    public static final BitSet allowed_IPv6reference = new BitSet(256);
    // Static initializer for allowed_IPv6reference
    static {
        allowed_IPv6reference.or(IPv6reference);
        // allowed_IPv6reference.andNot(unwise);
        allowed_IPv6reference.clear('[');
        allowed_IPv6reference.clear(']');
    }


    /**
     * Those characters that are allowed for the host component.
     * The characters '[', ']' in IPv6reference should be excluded.
     */
    public static final BitSet allowed_host = new BitSet(256);
    // Static initializer for allowed_host
    static {
        allowed_host.or(hostname);
        allowed_host.or(allowed_IPv6reference);
    }


    /**
     * Those characters that are allowed for the authority component.
     */
    public static final BitSet allowed_within_authority = new BitSet(256);
    // Static initializer for allowed_within_authority
    static {
        allowed_within_authority.or(ABNF_authority);
        allowed_within_authority.or(reg_name);
        allowed_within_authority.clear(';');
        allowed_within_authority.clear(':');
        allowed_within_authority.clear('@');
        allowed_within_authority.clear('?');
        allowed_within_authority.clear('/');
    }


    /**
     * Those characters that are allowed for the abs_path.
     */
    public static final BitSet allowed_abs_path = new BitSet(256);
    // Static initializer for allowed_abs_path
    static {
        allowed_abs_path.or(abs_path);
        // allowed_abs_path.set('/');  // aleady included
        allowed_abs_path.andNot(PERCENT);
        // 200705 added to update with
        // Fix for HTTPCLIENT-578: literal plus (+) 
        // character in path components of HttpURL is not preserved
        // http://svn.apache.org/viewvc/jakarta/commons/proper/httpclient/trunk/src/java/org/apache/commons/httpclient/URI.java?r1=372560&r2=410367&diff_format=h
        // there were no tests for this
        // but../a+v/ ? what does it mean ?
        // clearing the bit the behavior becomes
        // different from java URI and after 
        // hmm..when cleared the lookup works with /a+b/
        // and also java uri..should be right..
        // only that now the problem is that when creating
        // from unescaped components the uri becomes /a%20b/ ??!?
        
        // The right behavior should be to see /a+b/ as an URI
        // then the receiver that uses form url encoded 
        // may replace furtherly the + with a space when 
        // this is allowed...
        // In any case still don't know what to do of this bit
        
        allowed_abs_path.clear('+');
    }


    /**
     * Those characters that are allowed for the rel_path.
     */
    public static final BitSet allowed_rel_path = new BitSet(256);
    // Static initializer for allowed_rel_path
    static {
        allowed_rel_path.or(rel_path);
        allowed_rel_path.clear('%');
        // 200705  same as for rel path
        allowed_rel_path.clear('+');
    }


    /**
     * Those characters that are allowed within the path.
     */
    public static final BitSet allowed_within_path = new BitSet(256);
    // Static initializer for allowed_within_path
    static {
        allowed_within_path.or(abs_path);
        allowed_within_path.clear('/');
        allowed_within_path.clear(';');
        allowed_within_path.clear('=');
        allowed_within_path.clear('?');
        //if (MV) allowed_within_path.clear('#');
    }


    /**
     * Those characters that are allowed for the query component.
     */
    public static final BitSet allowed_query = new BitSet(256);
    // Static initializer for allowed_query
    static {
        allowed_query.or(uric);
        allowed_query.clear('%');
    }


    /**
     * Those characters that are allowed within the query component.
     */
    public static final BitSet allowed_within_query = new BitSet(256);
    // Static initializer for allowed_within_query
    static {
        allowed_within_query.or(allowed_query);
        allowed_within_query.andNot(ABNF_reserved); // excluded 'reserved'
    }


    /**
     * Those characters that are allowed for the fragment component.
     */
    public static final BitSet allowed_fragment = new BitSet(256);
    // Static initializer for allowed_fragment
    static {
        allowed_fragment.or(uric);
        allowed_fragment.clear('%');
    }

    // ------------------------------------------- Flags for this URI-reference

    // TODO: Figure out what all these variables are for and provide javadoc

    // URI-reference = [ absoluteURI | relativeURI ] [ "#" fragment ]
    // absoluteURI   = scheme ":" ( hier_part | opaque_part )
    protected boolean _is_hier_part;
    protected boolean _is_opaque_part;
    // relativeURI   = ( net_path | abs_path | rel_path ) [ "?" query ]
    // hier_part     = ( net_path | abs_path ) [ "?" query ]
    protected boolean _is_net_path;
    protected boolean _is_abs_path;
    protected boolean _is_rel_path;
    // net_path      = "//" authority [ abs_path ]
    // authority     = server | reg_name
    protected boolean _is_reg_name;
    protected boolean _is_server;  // = _has_server
    // server        = [ [ userinfo "@" ] hostport ]
    // host          = hostname | IPv4address | IPv6reference
    protected boolean _is_hostname;
    protected boolean _is_IPv4address;
    protected boolean _is_IPv6reference;

    // ------------------------------------------ Character and escape encoding

    /**
     * Encodes URI string.
     *
     * This is a two mapping, one from original characters to octets, and
     * subsequently a second from octets to URI characters:
     * <p><blockquote><pre>
     *   original character sequence->octet sequence->URI character sequence
     * </pre></blockquote><p>
     *
     * An escaped octet is encoded as a character triplet, consisting of the
     * percent character "%" followed by the two hexadecimal digits
     * representing the octet code. For example, "%20" is the escaped
     * encoding for the US-ASCII space character.
     * <p>
     * Conversion from the local filesystem character set to UTF-8 will
     * normally involve a two step process. First convert the local character
     * set to the UCS; then convert the UCS to UTF-8.
     * The first step in the process can be performed by maintaining a mapping
     * table that includes the local character set code and the corresponding
     * UCS code.
     * The next step is to convert the UCS character code to the UTF-8 encoding.
     * <p>
     * Mapping between vendor codepages can be done in a very similar manner
     * as described above.
     * <p>
     * The only time escape encodings can allowedly be made is when a URI is
     * being created from its component parts.  The escape and validate methods
     * are internally performed within this method.
     *
     * @param original the original character sequence
     * @param allowed those characters that are allowed within a component
     * @param charset the protocol charset
     * @return URI character sequence
     * @throws URIException null component or unsupported character encoding
     */

    protected static char[] encode(String original, BitSet allowed,
            String charset) throws URIException    {  
       
        if (original == null) {
            throw new IllegalArgumentException("Original string may not be null");
        }
        if (allowed == null) {
            throw new IllegalArgumentException("Allowed bitset may not be null");
        }
        byte[] rawdata = URLCodec.encodeUrl(allowed, EncodingUtil.getBytes(original, charset));
        //if (MV || true) System.out.println("Encode: [" + original + "] result:[" + EncodingUtil.getAsciiString(rawdata) + "]");
        return EncodingUtil.getAsciiString(rawdata).toCharArray();
    }

    /**
     * Decodes URI encoded string.
     *
     * This is a two mapping, one from URI characters to octets, and
     * subsequently a second from octets to original characters:
     * <p><blockquote><pre>
     *   URI character sequence->octet sequence->original character sequence
     * </pre></blockquote><p>
     *
     * A URI must be separated into its components before the escaped
     * characters within those components can be allowedly decoded.
     * <p>
     * Notice that there is a chance that URI characters that are non UTF-8
     * may be parsed as valid UTF-8.  A recent non-scientific analysis found
     * that EUC encoded Japanese words had a 2.7% false reading; SJIS had a
     * 0.0005% false reading; other encoding such as ASCII or KOI-8 have a 0%
     * false reading.
     * <p>
     * The percent "%" character always has the reserved purpose of being
     * the escape indicator, it must be escaped as "%25" in order to be used
     * as data within a URI.
     * <p>
     * The unescape method is internally performed within this method.
     *
     * @param component the URI character sequence
     * @param charset the protocol charset
     * @return original character sequence
     * @throws URIException incomplete trailing escape pattern or unsupported
     * character encoding
     */
    protected static String decode(char[] component, String charset)
        throws URIException {
        if (component == null) {
            throw new IllegalArgumentException("Component array of chars may not be null");
        }
        return decode(new String(component), charset);
    }

    /**
     * Decodes URI encoded string.
     *
     * This is a two mapping, one from URI characters to octets, and
     * subsequently a second from octets to original characters:
     * <p><blockquote><pre>
     *   URI character sequence->octet sequence->original character sequence
     * </pre></blockquote><p>
     *
     * A URI must be separated into its components before the escaped
     * characters within those components can be allowedly decoded.
     * <p>
     * Notice that there is a chance that URI characters that are non UTF-8
     * may be parsed as valid UTF-8.  A recent non-scientific analysis found
     * that EUC encoded Japanese words had a 2.7% false reading; SJIS had a
     * 0.0005% false reading; other encoding such as ASCII or KOI-8 have a 0%
     * false reading.
     * <p>
     * The percent "%" character always has the reserved purpose of being
     * the escape indicator, it must be escaped as "%25" in order to be used
     * as data within a URI.
     * <p>
     * The unescape method is internally performed within this method.
     *
     * @param component the URI character sequence
     * @param charset the protocol charset
     * @return original character sequence
     * @throws URIException incomplete trailing escape pattern or unsupported
     * character encoding
     *
     * @since 3.0
     */
    protected static String decode(String component, String charset)
        throws URIException {
        if (component == null) {
            throw new IllegalArgumentException("Component array of chars may not be null");
        }
        byte[] rawdata = null;
        try {
            rawdata = URLCodec.decodeUrl(EncodingUtil.getAsciiBytes(component), true);
        } catch (CodecException e) {
            throw new URIException(e.getMessage());
        }
        return EncodingUtil.getString(rawdata, charset);
    }
    /**
     * Pre-validate the unescaped URI string within a specific component.
     *
     * @param component the component string within the component
     * @param disallowed those characters disallowed within the component
     * @return if true, it doesn't have the disallowed characters
     * if false, the component is undefined or an incorrect one
     */
    protected boolean prevalidate(String component, BitSet disallowed) {
        // prevalidate the given component by disallowed characters
        if (component == null) {
            return false; // undefined
        }
        char[] target = component.toCharArray();
        for (int i = 0; i < target.length; i++) {
            if (disallowed.get(target[i])) {
                return false;
            }
        }
        return true;
    }


    /**
     * Validate the URI characters within a specific component.
     * The component must be performed after escape encoding. Or it doesn't
     * include escaped characters.
     *
     * @param component the characters sequence within the component
     * @param generous those characters that are allowed within a component
     * @return if true, it's the correct URI character sequence
     */
    protected boolean validate(char[] component, BitSet generous) {
        // validate each component by generous characters
        return validate(component, 0, -1, generous);
    }


    /**
     * Validate the URI characters within a specific component.
     * The component must be performed after escape encoding. Or it doesn't
     * include escaped characters.
     * <p>
     * It's not that much strict, generous.  The strict validation might be
     * performed before being called this method.
     *
     * @param component the characters sequence within the component
     * @param soffset the starting offset of the given component
     * @param eoffset the ending offset of the given component
     * if -1, it means the length of the component
     * @param generous those characters that are allowed within a component
     * @return if true, it's the correct URI character sequence
     */
    protected boolean validate(char[] component, int soffset, int eoffset,
            BitSet generous) {
        // validate each component by generous characters
        if (eoffset == -1) {
            eoffset = component.length - 1;
        }
        for (int i = soffset; i <= eoffset; i++) {
            if (!generous.get(component[i])) {
                return false;
            }
        }
        return true;
    }



	private static final boolean isOpenContextDelimiter(char c) {
		return c=='"' || c=='<';
	}
	private static final boolean isCloseContextDelimiter(char c, char o){
		if (o=='"') return c == '"';
		else return  c=='>';
	}
    /**
     * In order to avoid any possilbity of conflict with non-ASCII characters,
     * Parse a URI reference as a <code>String</code> with the character
     * encoding of the local system or the document.
     * <p>
     * The following line is the regular expression for breaking-down a URI
     * reference into its components.
     * <p><blockquote><pre>
     *   ^(([^:/?#]+):)?(//([^/?#]*))?([^?#]*)(\?([^#]*))?(#(.*))?
     *    12            3  4          5       6  7        8 9
     * </pre></blockquote><p>
     * For example, matching the above expression to
     *   http://jakarta.apache.org/ietf/uri/#Related
     * results in the following subexpression matches:
     * <p><blockquote><pre>
     *               $1 = http:
     *  scheme    =  $2 = http
     *               $3 = //jakarta.apache.org
     *  authority =  $4 = jakarta.apache.org
     *  path      =  $5 = /ietf/uri/
     *               $6 = <undefined>
     *  query     =  $7 = <undefined>
     *               $8 = #Related
     *  fragment  =  $9 = Related
     * </pre></blockquote><p>
     *
     * @param original the original character sequence
     * @param escaped <code>true</code> if <code>original</code> is escaped
     * @throws URIException If an error occurs.
     */
    protected void parseUriReference(final String original)
        throws URIException {

		// Note: always called by contstructor on empty instance
		// validate and contruct the URI character sequence
        if (original == null) {
            throw new URIException("Null URI String passed");
        }
       // System.out.println("BEGIN PARSE:" + original);


        /* @
         *  ^(([^:/?#]+):)?(//([^/?#]*))?([^?#]*)(\?([^#]*))?(#(.*))?
         */
        String tmp = original.trim();

        /*
         * The length of the string sequence of characters.
         * It may not be equal to the length of the byte array.
         */
        int length = tmp.length();

		/*
         * Remove the delimiters like angle brackets around an URI.
         */
		if (length > 0) {
			final char first = tmp.charAt(0);
			if (isOpenContextDelimiter(first))	{
				if (length==1)
					throw new URIException("First and only character is a context delimiter [" +
						first + "] :" + original);
				final int ilast = length - 1;
				char clast = tmp.charAt(ilast);
				if (!isCloseContextDelimiter(clast, first))
					throw new URIException("First character is a context delimiter [" +
						first + "] but last not: [" + clast + "]");
				tmp = tmp.substring(1, ilast);
				length-=2;
			}
		}
		// Note: length might change in delimiters removal
		// so order is important
		// Handle case "empty string" and ":" after delimiters removal
		if (length==0) {
			_path = EMPTYCHARS;
			_is_rel_path = true;
			setURI();
			return;
		} else if (tmp.charAt(0)==':') {
			throw new URIException("Invalid uri [:] if it is a rel path use [./:]");
		}


		// A URI-reference is either a URI or a relative reference.
		// If the URI-reference's prefix does not match the syntax of
		// a scheme followed by its colon separator, then the
		// URI-reference is a relative reference.

		/*
         * The starting index
         */
        int from = 0;
		//System.out.println("BEGINSKEME");

		// Here lenght is >0 and [:] only is already gone
		_scheme = null;
		if (tmp.charAt(0)!='/' && !tmp.startsWith("."))
		for(int i=0;i<length;i++){
			char c = tmp.charAt(i);
			if (c==':') {
				//System.out.println("SKEME:" + tmp.substring(0, i));
				_scheme = validateScheme(tmp.substring(0, i));
            	from = ++i;
				break;
			} else if (!URI.ABNF_scheme.get(c)) {
			    //System.out.println("FOUND NON SKEME CHAR at " + i + ":	" + c);
    			// 200507 changed Was:
				//if (c=='/') break;
				//int at = indexFirstOf(tmp, ":/?#", i + 1); //tmp.indexOf(':');
				// 200507 now:
				// We check also for the char itself, not only for the next
				int at = indexFirstOf(tmp, ":/?#", i); 
				
				if (at!=-1 && tmp.charAt(at)==':') {
					//System.out.println("SCHEME ERROR:" + at + " for " + tmp);
					throw new URIException("The first part of the uri contains a colon [:] " +
						"It is not a scheme name because contains char [" + c + "] so it" +
						" is a relative reference relative path that contains colon [:] in the" +
						" first segment, this is not allowed, See RFC3986 Section 3.3. " +
						"\nFull URI:" + tmp);
				} else {
					//System.out.println("HAS NO SCHEME:" + tmp);
					break;
				}
			} else {
				// Case good char for scheme
				//System.out.println("GOOD SKEME CHAR:" + c);
			}
		}
		//System.out.println("FINALSCHEME:" + (_scheme==null ? " [null]" : new String(_scheme)));

		// Note must be careful because [scheme] is the BitSet while
		// _scheme is the char array....EASY BUG
		// 200705 changed, BitSet is now (URI.)ABNF_scheme

		final boolean isOpaque;
		if (_scheme!=null ) {
			if (from==length) {
				// case <http:> is synctactically correct
				isOpaque = true;
			} else if (from < length && tmp.charAt(from) == '/') {
				isOpaque = false;
			} else {
				isOpaque = true;
			}
		} else {
			isOpaque = false;
		}

		_is_opaque_part = isOpaque;

		//System.out.println("Is opaque:" + isOpaque + "," + tmp);

		if (isOpaque)
		{
			if (from==length) {
				// Case http:
				_is_abs_path = true;
				_path = EMPTYCHARS;
				// to see here..setting opaque then relativ?
			} else {
				int at = tmp.indexOf('#', from);
				if (at!=-1) {
					if (at==length) _fragment = EMPTYCHARS;
					else _fragment = validateFragment(tmp.substring(at+1));
					_path = tmp.substring(from, at).toCharArray();
				}
				else _path = tmp.substring(from).toCharArray();
			}
			if (!validate(_path, opaque_part)) {
				throw new URIException("Invalid scheme specific part:  "+ new String(_path));
			}
			_opaque = _path;
			//System.out.println("Is opaque:" + isOpaque + "," + tmp + " SSP:" + new String(_opaque));

			setURI();
			//System.out.println("toString:" + toString());
			return;
		}



        /*
         * <p><blockquote><pre>
         *     @@@@@@@@
         *  ^(([^:/?#]+):)?(//([^/?#]*))?([^?#]*)(\?([^#]*))?(#(.*))?
         * </pre></blockquote><p>
         */
        int at = indexFirstOf(tmp, "/?#", from);
        if (at == -1) {
            at = 0;
        }



        //System.out.println("HERE:" + tmp.toString());
        //System.out.println("HERE:" + tmp.substring(from, length));


        /*
         * Parse the authority component.
         * <p><blockquote><pre>
         *  authority =  $4 = jakarta.apache.org
         *                  @@
         *  ^(([^:/?#]+):)?(//([^/?#]*))?([^?#]*)(\?([^#]*))?(#(.*))?
         * </pre></blockquote><p>
         */
        // Reset flags
        _is_net_path = _is_abs_path = _is_rel_path = _is_hier_part = false;
        if (0 <= at && at < length && tmp.charAt(at) == '/') {
            // Set flag
            _is_hier_part = true;
            if (at + 2 <= length && tmp.charAt(at + 1) == '/') {
                // the temporary index to start the search from
                int next = indexFirstOf(tmp, "/?#", at + 2);
                if (next == -1) {
                    next = (tmp.substring(at + 2).length() == 0) ? at + 2
                        : tmp.length();
                }
                parseAuthority(tmp.substring(at + 2, next));
                from = at = next;
                // Set flag
                _is_net_path = true;
            }
            if (from == at) {
                // Set flag
                _is_abs_path = true;
            }
        }

        /*
         * Parse the path component.
         * <p><blockquote><pre>
         *  path      =  $5 = /ietf/uri/
         *                                @@@@@@
         *  ^(([^:/?#]+):)?(//([^/?#]*))?([^?#]*)(\?([^#]*))?(#(.*))?
         * </pre></blockquote><p>
         */
        if (from < length) {
            // rel_path = rel_segment [ abs_path ]
            int next = indexFirstOf(tmp, "?#", from);
            if (next == -1) {
                next = tmp.length();
            }
            if (!_is_abs_path) {
                if (validate(tmp.substring(from, next).toCharArray(), rel_path)) {
                    // Set flag
                    _is_rel_path = true;
                } else if (validate(tmp.substring(from, next).toCharArray(), opaque_part)) {
                    // Set flag
                    _is_opaque_part = true;
                } else {
                    // the path component may be empty
                    // MV 200601 but not null..
                    _path = EMPTYCHARS;
                    // ya TODO bu then setPath is called..
                }
            }
            setRawPath(tmp.substring(from, next).toCharArray());
            at = next;
        }
        else setRawPath(EMPTYCHARS);



        /*
         * Parse the query component.
         * <p><blockquote><pre>
         *  query     =  $7 = <undefined>
         *                                        @@@@@@@@@
         *  ^(([^:/?#]+):)?(//([^/?#]*))?([^?#]*)(\?([^#]*))?(#(.*))?
         * </pre></blockquote><p>
         */
        if (0 <= at && at  < length && tmp.charAt(at) == '?') {
			if (at == length) {
				_query = EMPTYCHARS; // MV Changed mantain empty string (only ? present)
				at = length;
			}
			else {
				int next = tmp.indexOf('#', at + 1);
				if (next == -1) {
					next = tmp.length();
				}
				_query = validateQuery(tmp.substring(at + 1, next));
				at = next;
				
			}
        }

        /*
         * Parse the fragment component.
         * <p><blockquote><pre>
         *  fragment  =  $9 = Related
         *                                                   @@@@@@@@
         *  ^(([^:/?#]+):)?(//([^/?#]*))?([^?#]*)(\?([^#]*))?(#(.*))?
         * </pre></blockquote><p>
         */
        if (0 <= at && at + 1 <= length && tmp.charAt(at) == '#') {
            if (at + 1 == length) { // empty fragment
                _fragment = EMPTYCHARS;
            } else {
                _fragment = validateFragment(tmp.substring(at + 1));
            }
        }

        // set this URI.
        setURI();
    }
    
    private static final char[] EMPTYCHARS = new char[]{};


    /**
     * Get the earlier index that to be searched for the first occurrance in
     * one of any of the given string.
     *
     * @param s the string to be indexed
     * @param delims the delimiters used to index
     * @return the earlier index if there are delimiters
     */
    protected int indexFirstOf(String s, String delims) {
        return indexFirstOf(s, delims, -1);
    }


    /**
     * Get the earlier index that to be searched for the first occurrance in
     * one of any of the given string.
     *
     * @param s the string to be indexed
     * @param delims the delimiters used to index
     * @param offset the from index
     * @return the earlier index if there are delimiters
     */
    protected int indexFirstOf(String s, String delims, int offset) {
        if (s == null || s.length() == 0) {
            return -1;
        }
        if (delims == null || delims.length() == 0) {
            return -1;
        }
        // check boundaries
        if (offset < 0) {
            offset = 0;
        } else if (offset > s.length()) {
            return -1;
        }
        // s is never null
        int min = s.length();
        char[] delim = delims.toCharArray();
        for (int i = 0; i < delim.length; i++) {
            int at = s.indexOf(delim[i], offset);
            if (at >= 0 && at < min) {
                min = at;
            }
        }
        return (min == s.length()) ? -1 : min;
    }


    /**
     * Get the earlier index that to be searched for the first occurrance in
     * one of any of the given array.
     *
     * @param s the character array to be indexed
     * @param delim the delimiter used to index
     * @return the ealier index if there are a delimiter
     */
    protected int indexFirstOf(char[] s, char delim) {
        return indexFirstOf(s, delim, 0);
    }


    /**
     * Get the earlier index that to be searched for the first occurrance in
     * one of any of the given array.
     *
     * @param s the character array to be indexed
     * @param delim the delimiter used to index
     * @param offset The offset.
     * @return the ealier index if there is a delimiter
     */
    protected int indexFirstOf(char[] s, char delim, int offset) {
        if (s == null || s.length == 0) {
            return -1;
        }
        // check boundaries
        if (offset < 0) {
            offset = 0;
        } else if (offset > s.length) {
            return -1;
        }
        for (int i = offset; i < s.length; i++) {
            if (s[i] == delim) {
                return i;
            }
        }
        return -1;
    }


    /**
     * Parse the (percent encoded) authority component.
     *
     * @param original the percent encoded character 
     *  sequence of authority component
     * @throws URIException If an error occurs.
     */
    protected void parseAuthority(String original)
        throws URIException {

        // Reset flags
        _is_reg_name = _is_server =
        _is_hostname = _is_IPv4address = _is_IPv6reference = false;

        boolean hasPort = true;
        int from = 0;
        int next = original.indexOf('@');
        if (next != -1) { // neither -1 and 0
            // each protocol extented from URI supports the specific userinfo
            _userinfo = original.substring(0, next).toCharArray();
            from = next + 1;
        }
        next = original.indexOf('[', from);
        if (next >= from) {
            next = original.indexOf(']', from);
            if (next == -1) {
                throw new URIException(URIException.PARSING, "IPv6reference");
            } else {
                next++;
            }
            // In IPv6reference, '[', ']' should be excluded
            _host = original.substring(from, next).toCharArray();
            // Set flag
            _is_IPv6reference = true;
        } else { // only for !_is_IPv6reference
            next = original.indexOf(':', from);
            if (next == -1) {
                next = original.length();
                hasPort = false;
            }
            // REMINDME: it doesn't need the pre-validation
            _host = original.substring(from, next).toCharArray();
            if (validate(_host, IPv4address)) {
                // Set flag
                _is_IPv4address = true;
            } else if (validate(_host, hostname)) {
                // Set flag
                _is_hostname = true;
            } else {
                // Set flag
                _is_reg_name = true;
            }
        }
        if (_is_reg_name) {
            // Reset flags for a server-based naming authority
            _is_server = _is_hostname = _is_IPv4address =
            _is_IPv6reference = false;
            // set a registry-based naming authority
            _authority = original.toCharArray();
        } else {
            if (original.length() - 1 > next && hasPort
                && original.charAt(next) == ':') { // not empty
                from = next + 1;
                try {
					//System.out.println("PARSEPORT [" + original.substring(from) + "]");
                    _port = Integer.parseInt(original.substring(from));
                } catch (NumberFormatException error) {
                    throw new URIException(URIException.PARSING,
                            "invalid port number");
                }
            }
            // set a server-based naming authority
            StringBuffer buf = new StringBuffer();
            if (_userinfo != null) { // has_userinfo
                buf.append(_userinfo);
                buf.append('@');
            }
            if (_host != null) {
                buf.append(_host);
                if (_port != -1) {
                    buf.append(':');
                    buf.append(_port);
                }
            }
            _authority = buf.toString().toCharArray();
            // Set flag
            _is_server = true;
        }
    }


    /**
     * Once it's parsed successfully, set this URI.
     *
     * See 5.3 Component Recomposition
     * http://www.apps.ietf.org/rfc/rfc3986.html#sec-5.3
     * 
     * @see #getRawURI
     */
    protected void setURI() {
        
        StringBuffer buf = new StringBuffer();
        if (_scheme != null) {
            buf.append(_scheme);
            buf.append(':');
        }
        
        if (_is_net_path) {
            buf.append("//");
            if (_authority != null) { // has_authority
                
                buf.append(_authority);
            }
        }
        
        if (_opaque != null && _is_opaque_part) {
            buf.append(_opaque);
        } else if (_path != null) {
            // _is_hier_part or _is_relativeURI
            if (_path.length != 0) {
                buf.append(_path);
            }
        }
        if (_query != null) { // has_query
            buf.append('?');
            buf.append(_query);
        }
        // ignore the fragment identifier
        _uri = buf.toString().toCharArray();
        hash = 0;
    }

    // ----------------------------------------------------------- Test methods


    /**
     * Tell whether or not this URI is absolute.
     *
     * @return true iif this URI is absoluteURI
     */
    public boolean isAbsoluteURI() {
        // in rfc3986 absolute uri has also no fragment..
        return (_scheme != null);
    }


    /**
     * Teldl whether or not this URI is relative.
     *
     * @return true iif this URI is relativeURI
     */
    public boolean isRelativeURI() {
        return (_scheme == null);
    }


    /**
     * Tell whether or not the absoluteURI of this URI is hier_part.
     *
     * @return true iif the absoluteURI is hier_part
     */
    public boolean isHierPart() {
        return _is_hier_part;
    }


    /**
     * Tell whether or not the absoluteURI of this URI is opaque_part.
     *
     * @return true iif the absoluteURI is opaque_part
     */
    public boolean isOpaquePart() {
        return _is_opaque_part;
    }


    /**
     * Tell whether or not the relativeURI or heir_part of this URI is net_path.
     * It's the same function as the has_authority() method.
     *
     * @return true iif the relativeURI or heir_part is net_path
     * @see #hasAuthority
     */
    public boolean isNetPath() {
        return _is_net_path || (_authority != null);
    }


    /**
     * Tell whether or not the relativeURI or hier_part of this URI is abs_path.
     *
     * @return true iif the relativeURI or hier_part is abs_path
     */
    public boolean isAbsPath() {
        return _is_abs_path;
    }


    /**
     * Tell whether or not the relativeURI of this URI is rel_path.
     *
     * @return true iif the relativeURI is rel_path
     */
    public boolean isRelPath() {
        return _is_rel_path;
    }


    /**
     * Tell whether or not this URI has authority.
     * It's the same function as the is_net_path() method.
     *
     * @return true iif this URI has authority
     * @see #isNetPath
     */
    public boolean hasAuthority() {
        return (_authority != null) || _is_net_path;
    }

    /**
     * Tell whether or not the authority component of this URI is reg_name.
     *
     * @return true iif the authority component is reg_name
     */
    public boolean isRegName() {
        return _is_reg_name;
    }


    /**
     * Tell whether or not the authority component of this URI is server.
     *
     * @return true iif the authority component is server
     */
    public boolean isServer() {
        return _is_server;
    }


    /**
     * Tell whether or not this URI has userinfo.
     *
     * @return true iif this URI has userinfo
     */
    public boolean hasUserinfo() {
        return (_userinfo != null);
    }


    /**
     * Tell whether or not the host part of this URI is hostname.
     *
     * @return true iif the host part is hostname
     */
    public boolean isHostname() {
        return _is_hostname;
    }


    /**
     * Tell whether or not the host part of this URI is IPv4address.
     *
     * @return true iif the host part is IPv4address
     */
    public boolean isIPv4address() {
        return _is_IPv4address;
    }


    /**
     * Tell whether or not the host part of this URI is IPv6reference.
     *
     * @return true iif the host part is IPv6reference
     */
    public boolean isIPv6reference() {
        return _is_IPv6reference;
    }


    /**
     * Tell whether or not this URI has query.
     *
     * @return true iif this URI has query
     */
    public boolean hasQuery() {
        return (_query != null);
    }


    /**
     * Tell whether or not this URI has fragment.
     *
     * @return true iif this URI has fragment
     */
    public boolean hasFragment() {
        return (_fragment != null);
    }


    // ---------------------------------------------------------------- Charset


    /**
     * Set the default charset of the protocol.
     * <p>
     * The character set used to store files SHALL remain a local decision and
     * MAY depend on the capability of local operating systems. Prior to the
     * exchange of URIs they SHOULD be converted into a ISO/IEC 10646 format
     * and UTF-8 encoded. This approach, while allowing international exchange
     * of URIs, will still allow backward compatibility with older systems
     * because the code set positions for ASCII characters are identical to the
     * one byte sequence in UTF-8.
     * <p>
     * An individual URI scheme may require a single charset, define a default
     * charset, or provide a way to indicate the charset used.
     *
     * <p>
     * Always all the time, the setter method is always succeeded and throws
     * <code>DefaultCharsetChanged</code> exception.
     *
     * So API programmer must follow the following way:
     * <code><pre>
     *  import org.apache.util.URI$DefaultCharsetChanged;
     *      .
     *      .
     *      .
     *  try {
     *      URI.setDefaultProtocolCharset("UTF-8");
     *  } catch (DefaultCharsetChanged cc) {
     *      // CASE 1: the exception could be ignored, when it is set by user
     *      if (cc.getReasonCode() == DefaultCharsetChanged.PROTOCOL_CHARSET) {
     *      // CASE 2: let user know the default protocol charset changed
     *      } else {
     *      // CASE 2: let user know the default document charset changed
     *      }
     *  }
     *  </pre></code>
     *
     * The API programmer is responsible to set the correct charset.
     * And each application should remember its own charset to support.
     *
     * @param charset the default charset for each protocol
     * @throws DefaultCharsetChanged default charset changed
     */
    public static void setDefaultProtocolCharset(String charset)
        throws DefaultCharsetChanged {

        defaultProtocolCharset = charset;
        throw new DefaultCharsetChanged(DefaultCharsetChanged.PROTOCOL_CHARSET,
                "the default protocol charset changed");
    }


    /**
     * Get the default charset of the protocol.
     * <p>
     * An individual URI scheme may require a single charset, define a default
     * charset, or provide a way to indicate the charset used.
     * <p>
     * To work globally either requires support of a number of character sets
     * and to be able to convert between them, or the use of a single preferred
     * character set.
     * For support of global compatibility it is STRONGLY RECOMMENDED that
     * clients and servers use UTF-8 encoding when exchanging URIs.
     *
     * @return the default charset string
     */
    public static String getDefaultProtocolCharset() {
        return defaultProtocolCharset;
    }


    /**
     * Get the protocol charset used by this current URI instance.
     * It was set by the constructor for this instance. If it was not set by
     * contructor, it will return the default protocol charset.
     *
     * @return the protocol charset string
     * @see #getDefaultProtocolCharset
     */
    public String getProtocolCharset() {
        return (protocolCharset != null)
            ? protocolCharset
            : defaultProtocolCharset;
    }


    /**
     * Set the default charset of the document.
     * @param charset the default charset for the document
     */
    public static void setDefaultDocumentCharset(String charset) {
        defaultDocumentCharset = charset;
    }


    /**
     * Get the recommended default charset of the document.
     *
     * @return the default charset string
     */
    public static String getDefaultDocumentCharset() {
        return defaultDocumentCharset;
    }


    /**
     * Get the default charset of the document by locale.
     *
     * @return the default charset string by locale
     */
    public static String getDefaultDocumentCharsetByLocale() {
        return defaultDocumentCharsetByLocale;
    }


    /**
     * Get the default charset of the document by platform.
     *
     * @return the default charset string by platform
     */
    public static String getDefaultDocumentCharsetByPlatform() {
        return defaultDocumentCharsetByPlatform;
    }

    // ------------------------------------------------------------- The scheme

    /**
     * Get the scheme.
     *
     * @return the scheme
     */
    protected char[] getRawScheme() {
        return _scheme;
    }


    /**
     * Get the scheme.
     *
     * @return the scheme
     * null if undefined scheme
     */
    public String getScheme() {
        return (_scheme == null) ? null : new String(_scheme);
    }

    // ---------------------------------------------------------- The authority

    
    /**
     * Get the raw-escaped authority.
     *
     * @return the raw-escaped authority
     */
    protected char[] getRawAuthority() {
        return _authority;
    }


    /**
     * Get the escaped authority.
     *
     * @return the escaped authority
     */
    public String getEscapedAuthority() {
        return (_authority == null) ? null : new String(_authority);
    }


    /**
     * Get the authority.
     *
     * @return the authority
     * @throws URIException If {@link #decode} fails
     */
    public String getAuthority() throws URIException {
        return (_authority == null) ? null : decode(_authority,
                getProtocolCharset());
    }

    // ----------------------------------------------------------- The userinfo

    /**
     * Get the raw-escaped userinfo.
     *
     * @return the raw-escaped userinfo
     * @see #getAuthority
     */
    protected char[] getRawUserinfo() {
        return _userinfo;
    }


    /**
     * Get the escaped userinfo.
     *
     * @return the escaped userinfo
     * @see #getAuthority
     */
    public String getEscapedUserinfo() {
        return (_userinfo == null) ? null : new String(_userinfo);
    }


    /**
     * Get the userinfo.
     *
     * @return the userinfo
     * @throws URIException If {@link #decode} fails
     * @see #getAuthority
     */
    public String getUserinfo() throws URIException {
        return (_userinfo == null) ? null : decode(_userinfo,
                getProtocolCharset());
    }

    // --------------------------------------------------------------- The host

    /**
     * Get the host.
     * <p><blockquote><pre>
     *   host          = hostname | IPv4address | IPv6reference
     * </pre></blockquote><p>
     *
     * @return the host
     * @see #getAuthority
     */
    protected char[] getRawHost() {
        return _host;
    }


    /**
     * Get the host.
     * <p><blockquote><pre>
     *   host          = hostname | IPv4address | IPv6reference
     * </pre></blockquote><p>
     *
     * @return the host
     * @throws URIException If {@link #decode} fails
     * @see #getAuthority
     */
    public String getHost() throws URIException {
        if (_host != null) {
            return decode(_host, getProtocolCharset());
        } else {
            return null;
        }
    }

    // --------------------------------------------------------------- The port

    /**
     * Get the port.  In order to get the specfic default port, the specific
     * protocol-supported class extended from the URI class should be used.
     * It has the server-based naming authority.
     *
     * @return the port
     * if -1, it has the default port for the scheme or the server-based
     * naming authority is not supported in the specific URI.
     */
    public int getPort() {
        return _port;
    }

    // --------------------------------------------------------------- The path

    /**
     * Set the raw-escaped path.
     *
     * @param escapedPath the path character sequence
     * @throws URIException encoding error or not proper for initial instance
     * @see #encode
     */
    private void setRawPath(char[] escapedPath) throws URIException {
        if (escapedPath == null) throw new IllegalArgumentException("Null path passed. " +
			"\nRFC 3986 3.3 A path is always defined for a URI, " +
			"though the defined path may be empty (zero length).");

		if (escapedPath.length == 0) {
            _path = _opaque = escapedPath;
            setURI();
            return;
        }

        // remove the fragment identifier
        escapedPath = removeFragmentIdentifier(escapedPath);
        if (_is_net_path || _is_abs_path) {
            if (escapedPath[0] != '/') {
                throw new URIException(URIException.PARSING,
                        "not absolute path");
            }
            if (!validate(escapedPath, abs_path)) {
                throw new URIException(URIException.ESCAPING,
                        "escaped absolute path not valid");
            }
            _opaque = EMPTYCHARS; // MV added
            _path = escapedPath;
        } else if (_is_rel_path) {
            int at = indexFirstOf(escapedPath, '/');
            if (at == 0) {
                throw new URIException(URIException.PARSING, "incorrect path");
            }
            if (at > 0 && !validate(escapedPath, 0, at - 1, rel_segment)
                && !validate(escapedPath, at, -1, abs_path)
                || at < 0 && !validate(escapedPath, 0, -1, rel_segment)) {

                throw new URIException(URIException.ESCAPING,
                        "escaped relative path not valid");
            }
            _opaque = EMPTYCHARS; // MV added
           	_path = escapedPath;
        } else if (_is_opaque_part) {
            if (!uric_no_slash.get(escapedPath[0])
                && !validate(escapedPath, 1, -1, uric)) {
                throw new URIException(URIException.ESCAPING,
                    "escaped opaque part not valid");
            }
            _opaque = escapedPath;
            // here TODO ?? put _path empty ?? exist in bot setPath and setRawPath
			// MV to add _path = EMPTYCHARS;
			_path = _opaque;

        } else {
            throw new URIException(URIException.PARSING, "incorrect path: " + new String(escapedPath));
        }
        setURI();
    }


    
    /**
     * Resolve the base and relative path.
     *
     * @param basePath a character array of the basePath
     * @param relPath a character array of the relPath
     * @return the resolved path
     * @throws URIException no more higher path level to be resolved
     */
    protected static char[] resolvePath(char[] basePath, char[] relPath)
        throws URIException {

        // REMINDME: paths are never null
        String base = (basePath == null) ? "" : new String(basePath);
        //System.out.println("Resolve path:" + base + " with " + new String(relPath));
        int at = base.lastIndexOf('/');
        if (at != -1) {
            basePath = base.substring(0, at + 1).toCharArray();
        }
        // _path could be empty
        if (relPath == null || relPath.length == 0) {
            return normalize(basePath);
        } else if (relPath[0] == '/') {
            return normalize(relPath);
        } else {
            StringBuffer buff = new StringBuffer(base.length()
                + relPath.length);
            buff.append((at != -1) ? base.substring(0, at + 1) : "/");
            buff.append(relPath);
            return normalize(buff.toString().toCharArray());
        }
    }


    /**
     * Get the raw-escaped current hierarchy level in the given path.
     * If the last namespace is a collection, the slash mark ('/') should be
     * ended with at the last character of the path string.
     *
     * @param path the path
     * @return the current hierarchy level
     * @throws URIException no hierarchy level
     */
    protected char[] getRawCurrentHierPath(char[] path) throws URIException {

        if (_is_opaque_part) {
            throw new URIException(URIException.PARSING, "no hierarchy level");
        }
        if (path == null) {
            throw new URIException(URIException.PARSING, "empty path");
        }
        String buff = new String(path);
        int first = buff.indexOf('/');
        int last = buff.lastIndexOf('/');
        if (last == 0) {
            return rootPath;
        } else if (first != last && last != -1) {
            return buff.substring(0, last).toCharArray();
        }
        // FIXME: it could be a document on the server side
        return path;
    }


    /**
     * Get the raw-escaped current hierarchy level.
     *
     * @return the raw-escaped current hierarchy level
     * @throws URIException If {@link #getRawCurrentHierPath(char[])} fails.
     */
    protected char[] getRawCurrentHierPath() throws URIException {
        return (_path.length == 0) ? EMPTYCHARS : getRawCurrentHierPath(_path);
    }


    /**
     * Get the escaped current hierarchy level.
     *
     * @return the escaped current hierarchy level
     * @throws URIException If {@link #getRawCurrentHierPath(char[])} fails.
     */
    private String getEscapedCurrentHierPath() throws URIException {
        char[] path = getRawCurrentHierPath();
        return (path == null) ? null : new String(path);
    }


    /**
     * Get the current hierarchy level.
     *
     * @return the current hierarchy level
     * @throws URIException If {@link #getRawCurrentHierPath(char[])} fails.
     * @see #decode
     */
    public String getCurrentHierPath() throws URIException {
        char[] path = getRawCurrentHierPath();
        return (path == null) ? null : decode(path, getProtocolCharset());
    }


    /**
     * Get the level above the this hierarchy level.
     *
     * @return the raw above hierarchy level
     * @throws URIException If {@link #getRawCurrentHierPath(char[])} fails.
     */
    private char[] getRawAboveHierPath() throws URIException {
        char[] path = getRawCurrentHierPath();
        return (path == null) ? null : getRawCurrentHierPath(path);
    }


    /**
     * Get the level above the this hierarchy level.
     *
     * @return the raw above hierarchy level
     * @throws URIException If {@link #getRawCurrentHierPath(char[])} fails.
     */
    public String getEscapedAboveHierPath() throws URIException {
        char[] path = getRawAboveHierPath();
        return (path == null) ? null : new String(path);
    }




    /**
     * Get the escaped path.
     * @return the escaped path string
     */
    private char[] getRawPath() {
        return _is_opaque_part ? _opaque : _path;
    }



    /**
     * Get the escaped path.
     * @return the escaped path string
     */
    public String getEscapedPath() {
        char[] path = getRawPath();
        return (path == null) ? null : new String(path);
    }


    /**
     * Get the path.
     * <p><blockquote><pre>
     *   path          = [ abs_path | opaque_part ]
     * </pre></blockquote><p>
     * @return the path string
     * @throws URIException If {@link #decode} fails.
     * @see #decode
     */
    public String getPath() throws URIException {
        char[] path =  getRawPath();
        return (path == null) ? null : decode(path, getProtocolCharset());
    }


    /**
     * Get the raw-escaped basename of the path.
     *
     * @return the raw-escaped basename
     */
    private char[] getRawName() {
        if (_path.length == 0) {
            return EMPTYCHARS;
        }

        int at = 0;
        // TODO Should be == to
        // for (int i = _path.length; --i >= 0; ) {

        for (int i = _path.length - 1; i >= 0; i--) {
            if (_path[i] == '/') {
                at = i + 1;
                break;
            }
        }
        int len = _path.length - at;
        char[] basename =  new char[len];
        System.arraycopy(_path, at, basename, 0, len);
        return basename;
    }


    /**
     * Get the escaped basename of the path.
     *
     * @return the escaped basename string
     */
    public String getEscapedName() {
        char[] basename = getRawName();
        return (basename == null) ? null : new String(basename);
    }


    /**
     * Get the basename of the path.
     *
     * @return the basename string
     * @throws URIException incomplete trailing escape pattern or unsupported
     * character encoding
     * @see #decode
     */
    public String getName() throws URIException {
        char[] basename = getRawName();
        return (basename == null) ? null : decode(getRawName(),
                getProtocolCharset());
    }

    // ----------------------------------------------------- The path and query

    /**
     * Get the raw-escaped path and query.
     *
     * @return the raw-escaped path and query
     */
    private char[] getRawPathQuery() {

        if (_path.length > 0 && _query == null) {
            return null;
        }
        StringBuffer buff = new StringBuffer();
        buff.append(_path);
        if (_query != null) {
            buff.append('?');
            buff.append(_query);
        }
        return buff.toString().toCharArray();
    }


    /**
     * Get the escaped query.
     *
     * @return the escaped path and query string
     */
    public String getEscapedPathQuery() {
        char[] rawPathQuery = getRawPathQuery();
        return (rawPathQuery == null) ? null : new String(rawPathQuery);
    }


    
    // -------------------------------------------------------------- The query

    
    /**
     * Get the escaped query.
     *
     * @return the escaped query string
     */
    public String getEscapedQuery() {
        return (_query == null) ? null : new String(_query);
    }


    /**
     * Decodes the query for validation purposes
     * Package-friend for Apache URI Provider
     */
    String getQuery() throws URIException {
        return (_query == null) ? null : decode(_query, getProtocolCharset());
    }

    // ----------------------------------------------------------- The fragment

    /**
	 * @param non null fragment
	private char[] escapeFragment(String fragment)  throws URIException {
		return encode(fragment, allowed_fragment, getProtocolCharset());
	}
 */
	
    /**
	 * @param non null fragment
	private String escapeFragmentAsString(String fragment)  throws URIException {
		return new String(escapeFragment(fragment));
	}
 */
	
   
    /**
     * Get the escaped fragment.
     * <p>
     * The optional fragment identifier is not part of a URI, but is often used
     * in conjunction with a URI.
     * <p>
     * The format and interpretation of fragment identifiers is dependent on
     * the media type [RFC2046] of the retrieval result.
     * <p>
     * A fragment identifier is only meaningful when a URI reference is
     * intended for retrieval and the result of that retrieval is a document
     * for which the identified fragment is consistently defined.
     *
     * @return the escaped fragment string
     */
    public String getEscapedFragment() {
        return (_fragment == null) ? null : new String(_fragment);
    }


    /**
     * Decodes the fragment for validation purposes
     * Package-friend for Apache URI Provider
     */
    String getFragment() throws URIException {
//        return (_fragment == null) ? null : decode(_fragment,
//            getProtocolCharset());
        try
        {
            return (_fragment == null) ? null : EncodingUtil.getString(URLCodec.decodeUrl(EncodingUtil.getAsciiBytes(
                new String(_fragment)),
                false), getProtocolCharset());
        }
        catch (CodecException e)
        {
            throw new URIException("Could not decode fragment: " +  e.getMessage());
        }
    }

    // ------------------------------------------------------------- Utilities

    /**
     * Remove the fragment identifier of the given component.
     *
     * @param component the component that a fragment may be included
     * @return the component that the fragment identifier is removed
     */
    protected char[] removeFragmentIdentifier(char[] component) {
        if (component == null) {
            return null;
        }
        int lastIndex = new String(component).indexOf('#');
        if (lastIndex != -1) {
            // TODO optimize
            component = new String(component).substring(0,
                    lastIndex).toCharArray();
        }
        return component;
    }


    /**
     * Normalize the given hier path part.
     *
     * <p>Algorithm taken from URI reference parser at
     * http://www.apache.org/~fielding/uri/rev-2002/issues.html.
     *
     * @param path the path to normalize
     * @return the normalized path
     * @throws URIException no more higher path level to be normalized
     */
    protected static char[] normalize(char[] path) throws URIException {

        //System.out.println("Normalize [" + new String(path) + "]");

        if (path.length==0) {
            return path;
        }



        String normalized = new String(path);

        // If the buffer begins with "./" or "../", the "." or ".." is removed.
        if (normalized.startsWith("./")) {
            normalized = normalized.substring(1);
        } else if (normalized.startsWith("../")) {
            normalized = normalized.substring(2);
        } else if (normalized.startsWith("..")) {
            normalized = normalized.substring(2);
        }

        // All occurrences of "/./" in the buffer are replaced with "/"
        int index = -1;
        while ((index = normalized.indexOf("/./")) != -1) {
            normalized = normalized.substring(0, index) + normalized.substring(index + 2);
        }

        // If the buffer ends with "/.", the "." is removed.
        if (normalized.endsWith("/.")) {
            normalized = normalized.substring(0, normalized.length() - 1);
        }

        int startIndex = 0;

        // All occurrences of "/<segment>/../" in the buffer, where ".."
        // and <segment> are complete path segments, are iteratively replaced
        // with "/" in order from left to right until no matching pattern remains.
        // If the buffer ends with "/<segment>/..", that is also replaced
        // with "/".  Note that <segment> may be empty.
        while ((index = normalized.indexOf("/../", startIndex)) != -1) {
            int slashIndex = normalized.lastIndexOf('/', index - 1);
            if (slashIndex >= 0) {
                normalized = normalized.substring(0, slashIndex) + normalized.substring(index + 3);
            } else {
                startIndex = index + 3;
            }
        }
        if (normalized.endsWith("/..")) {
            int slashIndex = normalized.lastIndexOf('/', normalized.length() - 4);
            if (slashIndex >= 0) {
                normalized = normalized.substring(0, slashIndex + 1);
            }
        }

        // All prefixes of "<segment>/../" in the buffer, where ".."
        // and <segment> are complete path segments, are iteratively replaced
        // with "/" in order from left to right until no matching pattern remains.
        // If the buffer ends with "<segment>/..", that is also replaced
        // with "/".  Note that <segment> may be empty.
        while ((index = normalized.indexOf("/../")) != -1) {
            int slashIndex = normalized.lastIndexOf('/', index - 1);
            if (slashIndex >= 0) {
                break;
            } else {
                normalized = normalized.substring(index + 3);
            }
        }
        if (normalized.endsWith("/..")) {
            int slashIndex = normalized.lastIndexOf('/', normalized.length() - 4);
            if (slashIndex < 0) {
                normalized = "";
            }
        }
		//System.out.println("Normalize result [" + normalized + "]");

        return normalized.toCharArray();
    }


    /**
     * Creates a new URI obtained
     * normalizing the path part of this URI.  
     * <p>Normalization is only meant to be performed on
     * URIs with an absolute path.  
     * Calling this method on a relative path URI will have no
     * effect.
     * @return a normalized uri or the same uri if is not an absolute path.
     * @throws URIException no more higher path level to be normalized
     * 
     * @see #isAbsPath()
     */
    public URI normalize() throws URIException {
        if (isAbsPath()) {
            char[] npath = normalize(_path);
            URI uri = new URI();
            copy(uri, true);
            uri._path = npath;
            uri.setURI();
            return uri;
        } else {
            return this;
        }
    }


    /**
     * Test if the first array is equal to the second array.
     *
     * @param first the first character array
     * @param second the second character array
     * @return true if they're equal
     */
    protected boolean equals(char[] first, char[] second) {

        if (first == null && second == null) {
            return true;
        }
        if (first == null || second == null) {
            return false;
        }
        if (first.length != second.length) {
            return false;
        }
        for (int i = 0; i < first.length; i++) {
            if (first[i] != second[i]) {
                return false;
            }
        }
        return true;
    }


    /**
     * Test an object if this URI is equal to another.
     *
     * @param obj an object to compare
     * @return true if two URI objects are equal
     */
    public boolean equals(Object obj) {

        // normalize and test each components
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof URI)) {
            return false;
        }
        URI another = (URI) obj;
        // scheme
        if (!equals(_scheme, another._scheme)) {
            //System.out.println("DIFF SKEME");
            return false;
        }
        // is_opaque_part or is_hier_part?  and opaque
        if (!equals(_opaque, another._opaque)) {
            //System.out.println("DIFF OPAQUE");
            return false;
        }
        // is_hier_part
        // has_authority
        if (!equals(_authority, another._authority)) {
            //System.out.println("DIFF AUTH");
            return false;
        }
        // path
        if (!equals(_path, another._path)) {
            //System.out.println("DIFF PATH");
            return false;
        }
        // has_query
        if (!equals(_query, another._query)) {
            //System.out.println("DIFF QUERY");
            return false;
        }
        // has_fragment?  should be careful of the only fragment case.
        if (!equals(_fragment, another._fragment)) {
            //System.out.println("DIFF FRAG");
            return false;
        }
        return true;
    }

    // ---------------------------------------------------------- Serialization

    /**
     * Write the content of this URI.
     *
     * @param oos the object-output stream
     * @throws IOException If an IO problem occurs.
     */
    private void writeObject(ObjectOutputStream oos)
        throws IOException {
        // TODO here shoul write only the string...
        oos.defaultWriteObject();
    }
    

    /**
     * Read a URI.
     *
     * @param ois the object-input stream
     * @throws ClassNotFoundException If one of the classes specified in the
     * input stream cannot be found.
     * @throws IOException If an IO problem occurs.
     */
    private void readObject(ObjectInputStream ois)
        throws ClassNotFoundException, IOException {

        ois.defaultReadObject();
    }
   
    // -------------------------------------------------------------- Hash code

    /**
     * Return a hash code for this URI.
     *
     * @return a has code value for this URI
     */
    public int hashCode() {
        // here is implemented like in the String class,
        // http://www.cs.umd.edu/~pugh/java/memoryModel/archive/2178.html
        int t = hash;
        if (t == 0) {
            char[] c = _uri;
            if (c != null) {
                for (int i = 0, len = c.length; i < len; i++) {
                    t = 31 * t + c[i];
                }
            }
            c = _fragment;
            if (c != null) {
                for (int i = 0, len = c.length; i < len; i++) {
                    t = 31 * t + c[i];
                }
            }
            this.hash = t;
        }
        return hash;
    }

    // ------------------------------------------------------------- Comparison

    /**
     * Compare this URI to another object.
     *
     * @param obj the object to be compared.
     * @return 0, if it's same,
     * -1, if failed, first being compared with in the authority component
     * @throws ClassCastException not URI argument
     */
    public int compareTo(Object obj) throws ClassCastException {

        URI another = (URI) obj;
        if (!equals(_authority, another.getRawAuthority())) {
            return -1;
        }
        return toString().compareTo(another.toString());
    }

    // ------------------------------------------------------------------ Copy Implementation


			// This is set calling setURI
			// _uri;

			// Copy context
			// protocolCharset;

	        // Copy
	        // _scheme;
	        //   ..then..
	        //   --- if opaque copyNotOpaqueAllButScheme
	        //   ----else 	   copyOpaqueAllButScheme = CopyAuthority, CopyPath, CopyQueryFragment



			// copyNotOpaqueAllButScheme
			// _is_opaque_part;
			// _opaque = _path; // ?? impl
			// _fragment


	        // Copy Authority (not opaque)
	        // _authority;
	        // _userinfo;
	        // _is_reg_name;
	        // _is_server;
	        // _host;
	        // _port;
	        // _is_hostname;
			// _is_IPv4address;
			// _is_IPv6reference;
			// _is_hier_part;
	        // _is_net_path;

	        // Copy Path
	        // _path;
	        // _is_abs_path;
	        // _is_rel_path;

			// Copy QueryFragment
			//  _query;
			//  _fragment;


			// NOTE: all implementations of copy accept a NEW uri,
			// not an already filled, existing URI.

		private void copy(final URI out, boolean normalize) {
		    //normalize = false;
			out._scheme = normalize ? toLowerCase(this._scheme) : this._scheme;
			if (this._is_opaque_part) {
				copyOpaqueAllButScheme(out);
			} else {

				copyNotOpaqueAllButScheme(out, normalize);
			}
            out._uri = normalize ? this.toString().toCharArray() : this._uri;
		}
		private char[] toLowerCase(char[] a)
        {
            return a == null ? null : new String(a).toLowerCase().toCharArray();
        }


        private void copyNotOpaqueAllButScheme(final URI out, boolean normalize) {
			if (_is_opaque_part) throw new IllegalStateException();
	       	copyContext(out);
	        copyAuthority(out, normalize);
			copyPath(out);
			copyQueryFragment(out);
		}
		private void copyOpaqueAllButScheme(final URI out) {
			if (!_is_opaque_part) throw new IllegalStateException();
	        copyContext(out);
			out._is_opaque_part = true;
			out._opaque = this._opaque;
			out._path = this._path;
			copyFragment(out);
		}
		private final void copyContext(final URI out) {
			out.protocolCharset = this.protocolCharset;
		}

	    private void copyAuthority(final URI out, boolean normalize) {
			out._is_hostname = this._is_hostname;
			out._is_IPv4address = this._is_IPv4address;
			out._is_IPv6reference = this._is_IPv6reference;

			out._is_net_path = this._is_net_path;
			out._authority = normalize ? toLowerCase(this._authority) : this._authority;
			if (this._is_server) {
				out._is_server = this._is_server;
				out._userinfo = this._userinfo;
				out._host = normalize ? toLowerCase(this._host) : this._host;
				out._port = this._port;
			} else if (this._is_reg_name) {
				out._is_reg_name = this._is_reg_name;
			}
			out._opaque = this._opaque;

		}
		private final void copyPath(final URI out) {
			out._is_abs_path = this._is_abs_path;
	        out._is_rel_path = this._is_rel_path;
	        out._path = this._path;
		}

	    private final void copyQueryFragment(final URI out) {
			out._query = this._query;
			copyFragment(out);
		}

		private final void copyFragment(final URI out) {
			out._fragment= this._fragment;
		}



    // ------------------------------------------------------------------ Clone

    /*** JUST FOR REF, no sense in cloning an immutable

        URI instance = new URI();
        //this.copy(instance);
        instance._uri = _uri;
		instance._scheme = _scheme;
		instance._opaque = _opaque;
		instance._authority = _authority;
		instance._userinfo = _userinfo;
		instance._host = _host;
		instance._port = _port;
		instance._path = _path;
		instance._query = _query;
		instance._fragment = _fragment;
		// the charset to do escape encoding for this instance
		instance.protocolCharset = protocolCharset;
		// flags
		instance._is_hier_part = _is_hier_part;
		instance._is_opaque_part = _is_opaque_part;
		instance._is_net_path = _is_net_path;
		instance._is_abs_path = _is_abs_path;
		instance._is_rel_path = _is_rel_path;
		instance._is_reg_name = _is_reg_name;
		instance._is_server = _is_server;
		instance._is_hostname = _is_hostname;
		instance._is_IPv4address = _is_IPv4address;
		instance._is_IPv6reference = _is_IPv6reference;
		// mv test
        URI zz = new URI();
        copy(zz);
        if (!zz.equals(instance)) throw new Error("different, copy is:" + zz + "\nold is:" + instance);

            return zz;
        }
        
    ******/
    


    /**
     * Retrieves the percent encoded URI string. 
     * @return the percent encoded URI string
     */
    public String getEscapedURI() {
        return (_uri == null) ? null : new String(_uri);
    }


    /**
     * Get the escaped URI reference string.
     *
     * @return the escaped URI reference string
     */
    public String getEscapedURIReference() {
        if (_fragment == null) {
            return getEscapedURI();
        }
        if (_uri == null) {
            return getEscapedFragment();
        }
        // if _uri != null &&  _fragment != null
        StringBuffer uriReference = new StringBuffer()
            .append(_uri)
            .append('#')
            .append(_fragment);
        return uriReference.toString();
    }


    /**
     * Get the escaped URI string.
     * @return the escaped URI string
     */
    public String toString() {
        return getEscapedURI();
    }


    // ------------------------------------------------------------ Inner class

    /**
     * The charset-changed normal operation to represent to be required to
     * alert to user the fact the default charset is changed.
     */
    public static class DefaultCharsetChanged extends RuntimeException {

        // ------------------------------------------------------- constructors

        /**
         * The constructor with a reason string and its code arguments.
         *
         * @param reasonCode the reason code
         * @param reason the reason
         */
        public DefaultCharsetChanged(int reasonCode, String reason) {
            super(reason);
            this.reason = reason;
            this.reasonCode = reasonCode;
        }

        // ---------------------------------------------------------- constants

        /** No specified reason code. */
        public static final int UNKNOWN = 0;

        /** Protocol charset changed. */
        public static final int PROTOCOL_CHARSET = 1;

        /** Document charset changed. */
        public static final int DOCUMENT_CHARSET = 2;

        // ------------------------------------------------- instance variables

        /** The reason code. */
        private int reasonCode;

        /** The reason message. */
        private String reason;

        // ------------------------------------------------------------ methods

        /**
         * Get the reason code.
         *
         * @return the reason code
         */
        public int getReasonCode() {
            return reasonCode;
        }

        /**
         * Get the reason message.
         *
         * @return the reason message
         */
        public String getReason() {
            return reason;
        }

    }


    /**
     * A mapping to determine the (somewhat arbitrarily) preferred charset for a
     * given locale.  Supports all locales recognized in JDK 1.1.
     * <p>
     * The distribution of this class is Servlets.com.    It was originally
     * written by Jason Hunter [jhunter at acm.org] and used by with permission.
     */
    public static class LocaleToCharsetMap {

        /** A mapping of language code to charset */
        private static final Hashtable LOCALE_TO_CHARSET_MAP;
        static {
            LOCALE_TO_CHARSET_MAP = new Hashtable();
            LOCALE_TO_CHARSET_MAP.put("ar", "ISO-8859-6");
            LOCALE_TO_CHARSET_MAP.put("be", "ISO-8859-5");
            LOCALE_TO_CHARSET_MAP.put("bg", "ISO-8859-5");
            LOCALE_TO_CHARSET_MAP.put("ca", "ISO-8859-1");
            LOCALE_TO_CHARSET_MAP.put("cs", "ISO-8859-2");
            LOCALE_TO_CHARSET_MAP.put("da", "ISO-8859-1");
            LOCALE_TO_CHARSET_MAP.put("de", "ISO-8859-1");
            LOCALE_TO_CHARSET_MAP.put("el", "ISO-8859-7");
            LOCALE_TO_CHARSET_MAP.put("en", "ISO-8859-1");
            LOCALE_TO_CHARSET_MAP.put("es", "ISO-8859-1");
            LOCALE_TO_CHARSET_MAP.put("et", "ISO-8859-1");
            LOCALE_TO_CHARSET_MAP.put("fi", "ISO-8859-1");
            LOCALE_TO_CHARSET_MAP.put("fr", "ISO-8859-1");
            LOCALE_TO_CHARSET_MAP.put("hr", "ISO-8859-2");
            LOCALE_TO_CHARSET_MAP.put("hu", "ISO-8859-2");
            LOCALE_TO_CHARSET_MAP.put("is", "ISO-8859-1");
            LOCALE_TO_CHARSET_MAP.put("it", "ISO-8859-1");
            LOCALE_TO_CHARSET_MAP.put("iw", "ISO-8859-8");
            LOCALE_TO_CHARSET_MAP.put("ja", "Shift_JIS");
            LOCALE_TO_CHARSET_MAP.put("ko", "EUC-KR");
            LOCALE_TO_CHARSET_MAP.put("lt", "ISO-8859-2");
            LOCALE_TO_CHARSET_MAP.put("lv", "ISO-8859-2");
            LOCALE_TO_CHARSET_MAP.put("mk", "ISO-8859-5");
            LOCALE_TO_CHARSET_MAP.put("nl", "ISO-8859-1");
            LOCALE_TO_CHARSET_MAP.put("no", "ISO-8859-1");
            LOCALE_TO_CHARSET_MAP.put("pl", "ISO-8859-2");
            LOCALE_TO_CHARSET_MAP.put("pt", "ISO-8859-1");
            LOCALE_TO_CHARSET_MAP.put("ro", "ISO-8859-2");
            LOCALE_TO_CHARSET_MAP.put("ru", "ISO-8859-5");
            LOCALE_TO_CHARSET_MAP.put("sh", "ISO-8859-5");
            LOCALE_TO_CHARSET_MAP.put("sk", "ISO-8859-2");
            LOCALE_TO_CHARSET_MAP.put("sl", "ISO-8859-2");
            LOCALE_TO_CHARSET_MAP.put("sq", "ISO-8859-2");
            LOCALE_TO_CHARSET_MAP.put("sr", "ISO-8859-5");
            LOCALE_TO_CHARSET_MAP.put("sv", "ISO-8859-1");
            LOCALE_TO_CHARSET_MAP.put("tr", "ISO-8859-9");
            LOCALE_TO_CHARSET_MAP.put("uk", "ISO-8859-5");
            LOCALE_TO_CHARSET_MAP.put("zh", "GB2312");
            LOCALE_TO_CHARSET_MAP.put("zh_TW", "Big5");
        }

        /**
         * Get the preferred charset for the given locale.
         *
         * @param locale the locale
         * @return the preferred charset or null if the locale is not
         * recognized.
         */
        public static String getCharset(Locale locale) {
            // try for an full name match (may include country)
            String charset =
                (String) LOCALE_TO_CHARSET_MAP.get(locale.toString());
            if (charset != null) {
                return charset;
            }

            // if a full name didn't match, try just the language
            charset = (String) LOCALE_TO_CHARSET_MAP.get(locale.getLanguage());
            return charset;  // may be null
        }

    }

}

