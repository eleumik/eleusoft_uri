package org.eleusoft.uri;

import java.net.MalformedURLException;

/**
 * <em>U</em>niform <em>R</em>esource <em>I</em>dentifier Reference interface.
 * <p>This interface has been thought to provide a consistent
 * interface for parsing and representing the components
 * of an URI Reference as specified by
 * <cite>RFC3986 - Uniform Resource Identifier (URI): Generic Syntax</cite>,
 * available at:<ul>
 * <li><a href='http://www.ietf.org/rfc/rfc3986.txt'>
 * http://www.ietf.org/rfc/rfc3986.txt</a> (text)
 * <a target='_blank' href='http://www.ietf.org/rfc/rfc3986.txt'>
 * [new window]</a>
 * <li><a href='http://www.apps.ietf.org/rfc/rfc3986.html'>
 * http://www.apps.ietf.org/rfc/rfc3986.html</a> (html)
 * <a target='_blank' href='http://www.apps.ietf.org/rfc/rfc3986.html'>
 * [new window]</a>
 * </ul>
 * <blockquote>
 * <p>From RFC3986 - Abstract
 * <p><cite>
 * A Uniform Resource Identifier (URI) is a compact sequence of
 * characters that identifies an abstract or physical resource.
 * </cite>
 * </blockquote>
 * <p>An URI is way to <strong>identify</strong> a resource,
 * not to locate it.
 * <blockquote>
 * <p>From RFC3986 Section 1.2.2 - Separating Identification from Interaction
 * <p><cite>
  Although many URI schemes are named after protocols, this does not
   imply that use of these URIs will result in access to the resource
   via the named protocol.  URIs are often used simply for the sake of
   identification.</cite>
 * </p>
 * </blockquote>
 * <p>This interface represents an URI reference.
 * <blockquote>
 * <p>From RFC3986 Section 4.1 - URI Reference
 * <p><cite>
 * A URI-reference is either a URI or a relative reference.  If the
 * URI-reference's prefix does not match the syntax of a scheme followed
 * by its colon separator, then the URI-reference is a relative
 * reference.</cite>
 * </blockquote>
 * <p>Both Section 4.3 and Section 1.2.3 help in defininig
 * a <em>relative reference</em>.
 * <blockquote>
  * <p>From RFC3986 Section 4.3 - Relative Reference
  * <p><cite>
  * A relative reference takes advantage of the hierarchical syntax
  * (Section 1.2.3) to express a URI reference relative
  * to the name space of another hierarchical URI.</cite>
  * <p>From RFC3986 Section 1.2.3 - Hierarchical identifiers
  * <p><cite>
  * A relative reference (Section 4.2) refers to a resource by describing
  * the difference within a hierarchical name space between
  * the reference context and the target URI.</cite>
  * </blockquote>


 * <p><strong>URI Components</strong>
 * <p>The generic URI syntax consists of a hierarchical sequence of components referred to as:
 * <ul>
 *  <li>Scheme
 *  <li>Authority
 *  <li>Path
 *  <li>Query
 *  <li>Fragment
 * </ul>
 * <a name='emptyabsent'><!-- --></a>
 * <p><strong>Empty and absent components</strong>
 * <p>
 * <table class='Bordered' cellpadding=4 border=1>
 * <tr>
 * <th>Component</th>
 * <th>Methods</th>
 * <th>MayBe Empty</th>
 * <th>MayBe Absent</th>
 * </tr>
 * <tr>
 * <th><code>scheme</code></td>
 * <td>{@link #getScheme()}, {@link #isAbsolute()}</td>
 * <td align=center class=error>no</td>
 * <td align=center>yes</td>
 * </tr>
 * <tr>
 * <th><code>authority</code></td>
 * <td>{@link #getAuthority()}</td>
 * <td align=center>yes</td>
 * <td align=center>yes</td>
 * </tr>
 * <tr>
 * <th><code>path</code></td>
 * <td>{@link #getPEPath()}, {@link #isPathEmpty()}, {@link #getPDPath()}</td>
 * <td align=center>yes</td>
 * <td align=center class=error>no</td>
 * </tr>
 * <tr>
 * <th><code>query</code></td>
 * <td >{@link #getQuery()}</td>
 * <td align=center>yes</td>
 * <td align=center>yes</td>
 * </tr>
 * <tr>
 * <th><code>fragment</code></td>
 * <td>{@link #getFragment()}, {@link #hasFragment()}</td>
 * <td align=center>yes</td>
 * <td align=center>yes</td>
 * </tr>
 * </table>
 *
 *
 * <p><strong>Percent Encoding - "URI Escaping"</strong>
 * <p>An URI is <em>always in an percent encoded form</em>.
 * URI escaping is the old term for <em>percent encoding</em>.
 * <p>
 * <blockquote>
 *  <p>From RFC3986 Section 2.1 - Percent Encoding
 * <p><cite>
   A percent-encoding mechanism is used to represent a data octet in a
   component when that octet's corresponding character is outside the
   allowed set or is being used as a delimiter of, or within, the
   component.  A percent-encoded octet is encoded as a character
   triplet, consisting of the percent character "%" followed by the two
   hexadecimal digits representing that octet's numeric value.  For
   example, "%20" is the percent-encoding for the binary octet
   "00100000" (ABNF: %x20), which in US-ASCII corresponds to the space
   character (SP).</cite>
 * </blockquote>
 * Being alwyays in escaped form means that
 * <em>decoding the percent escaped sequences</em>
 * of an URI
 * <em>may not preserve the information present in the URI</em>.

 * <p>RFC 3986 allows percent-encoded octets to appear in
 * the user-info, path, query, and fragment components.
 * Escaping serves two purposes in URIs: to insert
 * non-ASCII characters in the URI and to insert
 * URI-reserved characters inside the URI avoiding that
 * their interpretation as delimiters
 * for URI components and sub components. Not all the
 * URI-reserved characters are actually used as delimiters,
 * a basic set is always reserved for each component,
 * while some are free for use by URI scheme implementations
 * (for designers of an URI scheme).
 * <p>
 * <blockquote>
 *  <p>From RFC3986 2.2 -  Reserved Characters
 * <p><cite>
   URIs include components and subcomponents that are delimited by
   characters in the "reserved" set.  These characters are called
   "reserved" because they may (or may not) be defined as delimiters by
   the generic syntax, by each scheme-specific syntax, or by the
   implementation-specific syntax of a URI's dereferencing algorithm.</cite>
 * </blockquote>
 * <p>Reserved characters are divided in general delimiters and
 * sub delimiters.
 *
 * <pre class=code>
 *
 * reserved    = gen-delims / sub-delims
 *
 * gen-delims  = ":" / "/" / "?" / "#" / "[" / "]" / "@"
 *
 * sub-delims  = "!" / "$" / "&amp;" / "'" / "(" / ")"
 *                  / "*" / "+" / "," / ";" / "="
 *
 * unreserved  = ALPHA / DIGIT / "-" / "." / "_" / "~"
 * pct-encoded = "%" HEXDIG HEXDIG
 * </pre>
 *
 * <p>General delimiters in the first component that
 * support percent escaped octets (user-info) must always
 * be escaped. In the following percent-escaping-supporting
 * component, path, all general delimiters
 * but ':' and '@' must be escaped (':' has an exception [**] when
 * is in the first segment of a relative reference),
 * in the other components also '?' and '/' can be present.
 * This, together with most of the other rules defined in the RFC lead to the following table:
 * <p>
 * <a name='chars'><!-- --></a>
 * <table class='Bordered' cellpadding=4 border=1 style='font-size:0.8em'>
 * <tr >
 * <td colspan=1 rowspan='1' style='border:0px'></td>
 * <th rowspan='2' colspan=1>percent-encoded</th>
 * <th colspan=14>UNICODE</th>
 * <tr >
 * <td rowspan='4' colspan='2' style='border:0px'></td>
 * <th rowspan='2'>DIGIT</th>
 * <th rowspan='2'>'.'</th>
 * <th rowspan='1'>a-f A-F</th>
 * <th rowspan='1'>g-z G-Z</th>
 * <th rowspan='2'>'-'</th>
 * <th rowspan='2'>'_' '~'</th>
 * <th rowspan='2'>'+'</th>
 * <th rowspan='2'>! $ &amp; ' ( ) * , ; =</th>
 * <th rowspan='2'>':'</th>
 * <th rowspan='2'>'@'</th>
 * <th rowspan='2'>'?' '/'</th>
 * <th rowspan='2'>'#'</th>
 * <th rowspan='2'>'[' ']'</th>
 * <th rowspan='4' colspan=1>All other unicode characters</th>
 * </tr>
 * <tr >
 * <th colspan=2>LITERAL</th>
 * <tr >
 * <tr >
 * <th rowspan='1' colspan=6>unreserved</th>
 * <th rowspan='1' colspan=2>sub-delims</th>
 * <th rowspan='1' colspan=5>gen-delims</th>
 * </tr>
 * <tr>
 * <th><code>scheme</code></td>
 * <td style='background-color:#ffe0ff'>NO</td>
 * <td colspan=5 style='background-color:#60aa60;color:white;'>PDEC-ONLY</td>
 * <td colspan=1 style='background-color:#ffe0ff'>[not-allowed]</td>
 * <td  style='background-color:#60aa60;color:white;'>PDEC-ONLY</td>
 * <td colspan=7 style='background-color:#ffe0ff'>[not-allowed]</td>
 * </tr>
 * <tr>
 * <th><code>user-info</code></td>
 * <td>YES</td>
 * <td colspan=6>ANY-MAY-NORMALIZE</td>
 * <td colspan=2>SCHEME-DEPENDENT</td>
 * <td>ANY</td>
 * <td colspan=4 style='background-color:#a0a0ff'>PENC</td>
 * <td colspan=1 style='background-color:#a0a0ff'>PENC</td>
 * </tr>
 * <tr>
 * <th><code>host.registry-name</code></td>
 * <td>Yes</td>
 * <td colspan='6'>ANY-MAY-NORMALIZE</td>
 * <td colspan=2>SCHEME-DEPENDENT</td>
 * <td colspan=1 style='background-color:#a0a0ff'>PENC</td>
 * <td colspan=4 style='background-color:#a0a0ff'>PENC</td>
 * <td colspan=1 style='background-color:#a0a0ff'>PENC</td>
 * </tr>

 * <tr>
 * <th><code>host.IPv6Future</code></td>
 * <td style='background-color:#ffe0ff'>NO</td>
 * <td colspan='9' style='background-color:#60aa60;color:white;'>PDEC-ONLY</td>
 * <td colspan=3 style='background-color:#ffe0ff'>[not-allowed]</td>
 * <td colspan='1' style='background-color:#60aa60;color:white;'>PDEC-ONLY</td>
 * <td colspan=1 style='background-color:#ffe0ff'>[not-allowed]</td>
 * </tr>

 * <tr>
 * <th><code>host.IPv6address</code></td>
 * <td style='background-color:#ffe0ff'>NO</td>
 * <td colspan='3' style='background-color:#60aa60;color:white;'>PDEC-ONLY HEXDIGIT '.'</td>
 * <td colspan=5 style='background-color:#ffe0ff'>[not-allowed]</td>
 * <td colspan='1' style='background-color:#60aa60;color:white;'>PDEC-ONLY</td>
 * <td colspan=3 style='background-color:#ffe0ff'>[not-allowed]</td>
 * <td colspan='1' style='background-color:#60aa60;color:white;'>PDEC-ONLY</td>
 * <td colspan=3 style='background-color:#ffe0ff'>[not-allowed]</td>
 * </tr>

 * <tr>
 * <th><code>host.IPv4address</code></td>
 * <td style='background-color:#ffe0ff'>NO</td>
 * <td colspan='2' style='background-color:#60aa60;color:white;'>PDEC-ONLY DIGIT  '.'</td>
 * <td colspan=12 style='background-color:#ffe0ff'>[not-allowed]</td>
 * </tr>

 * <tr>
 * <th><code>port</code></td>
 * <td style='background-color:#ffe0ff'>NO</td>
 * <td colspan='1' style='background-color:#60aa60;color:white;'>PDEC-ONLY</td>
 * <td colspan=15 style='background-color:#ffe0ff'>[not-allowed]</td>
 * </tr>

 * <tr>
 * <th><code>path</code></td>
 * <td>YES</td>
 * <td colspan=6>ANY-MAY-NORMALIZE</td>
 * <td colspan=2>SCHEME-DEPENDENT</td>
 * <td colspan=2>ANY [1]</td>
 * <td colspan=3 style='background-color:#a0a0ff'>PENC</td>
 * <td colspan=1 style='background-color:#a0a0ff'>PENC</td>
 * </tr>
 * <tr>
 * <th><code>query</code></td>
 * <td>YES</td>
 * <td colspan=6>ANY-MAY-NORMALIZE</td>
 * <td colspan=2>SCHEME-DEPENDENT</td>
 * <td colspan=3>ANY</td>
 * <td colspan='2' style='background-color:#a0a0ff'>PENC</td>
 * <td colspan=1 style='background-color:#a0a0ff'>PENC</td>
 * </tr>
 * <tr>
 * <th><code>fragment</code></td>
 * <td>YES</td>
 * <td colspan=6>ANY-MAY-NORMALIZE</td>
 * <td colspan=2>SCHEME-DEPENDENT</td>
 * <td colspan=3>ANY</td>
 * <td colspan='2' style='background-color:#a0a0ff'>PENC</td>
 * <td colspan=1 style='background-color:#a0a0ff'>PENC</td>
 * </tr>
 * </table>

 * <p><ul>
  <li>[1] Exception ':' cannot stay in first segment of a rel path
 * <li>[2] About '[' and ']' -
  * <a href='http://lists.w3.org/Archives/Public/uri/2004Jun/0011.html'>
  * http://lists.w3.org/Archives/Public/uri/2004Jun/0011.html</a> |
 * <a href='http://www.nabble.com/Accepting-square-brackets-(--)-in-Zend_Uri_Http-t3675869s16154.html'>
 * http://www.nabble.com/Accepting-square-brackets-(--)-in-Zend_Uri_Http-t3675869s16154.html</a>
 * 	<p>Note that at section [3.2.2 Host] of RFC 3986, talking about IPV6 hosts they say:
 * <blockquote>
 * This is the only place where
 * square bracket characters are allowed in the URI syntax.
 * </blockquote>
 * <p>And also D.1 Additions
 * <blockquote>
 *  Square brackets are now specified as reserved within the authority component
 * and are not allowed outside their use as delimiters for an IP literal within host
 * </blockquote>
 * <p>So they are not allowed, but as noted in the ZendUri message, there
 * are uris like that..
 * </li>
 * </ul>
 *
 * <p>The number of general delimiter characters that must
 * be escaped to represent its value in each component decrements when you move
 * from the first (user-info) to the last (fragment).
 * <blockquote>
 *  <p>From RFC3986 Section 2.4 - When to Encode or Decode
 *  <p><cite>
 *   Under normal circumstances, the only time when octets within a URI
 *  are percent-encoded is during the process of producing the URI from
 * its component parts.  This is when an implementation determines which
 * of the reserved characters are to be used as subcomponent delimiters
 * and which can be safely used as data.  Once produced, a URI is always
 * in its percent-encoded form</cite>
 * </blockquote>
 * <p><b>URI percent encoding example</b>
 *  <p>This is example may help in understanding
 * why is necessary to escape an URI.
 * Suppose we receive two URIs like these:
 * <ol>
 * <li> &lt;&lt;<code>http://www.example.org/Number#A1.xml</code>&gt;&gt;
 * <li> &lt;&lt;<code>http://www.example.org/Number<em>%23</em>A1.xml</code>&gt;&gt;
 * </ol>
 * <p>While both have the same scheme <code>http</code>,
 * the same authority <code>www.example.org</code>,
 * and absent query, the first URI has path
 * <code>&lt;&lt;/Number>></code>
 * and fragment <code>&lt;&lt;A1.xml>></code> while
 * the second one has path
 * <code>&lt;&lt;/Number<em>%23</em>A1.xml>></code>
 * and <em>absent</em> fragment.
 * In the first case an URI parser will interpret
 * the hash '#' symbol as the URI-reserved-control-char
 * that marks the begin of an URI fragment component,
 * In the latter case the '#' is encoded as '%23' so
 * that will be interpreted as part of the path;
 * in this second case it might be possible for a web server
 * to map the URI to a file with <em>file</em>-name
 * <code>Number#A1.xml</code> on the web server machine,
 * also if whether this actually happens will
 * depend on how the web server is configured.
 * In the first case instead the
 * file-name would have been <code>Number</code>.
 *
 * <p><strong>URI Encoding character set (browsers)</strong>
 * <p>Some browsers URLs to be written inside HTML documents
 * using characters belonging to the HTML document charset
 * without requiring percent encoding.
 * The result is a more readable page and the automatic correction
 * of wrongly typed URIs. It is instead necessary to
 * always encode the url using <emASCII</em>,
 * percent encoding the non-ASCII characters.
 * In this way  we can insert in an HTML page
 * encoded with standard 8 bit western european encoding (iso-8859-1)
 * an HTML link containing and URL that contains japanese characters.
 * Cfr. <a href='http://www.w3.org/TR/html401/appendix/notes.html#non-ascii-chars'>
 * http://www.w3.org/TR/html401/appendix/notes.html#non-ascii-chars</a>.
 * <p>Browsers in general are very forgiving about URI syntax.

 * <p><a name='scheme'><strong><code>scheme</code></strong></a>
 * <p><cite>
 * Scheme names consist of a sequence of characters beginning with a
   letter and followed by any combination of letters, digits, plus
   ("+"), period ("."), or hyphen ("-").  Although schemes are case-
   insensitive, the canonical form is lowercase and documents that
   specify schemes must do so with lowercase letters.  An implementation
   should accept uppercase letters as equivalent to lowercase in scheme
   names (e.g., allow "HTTP" as well as "http") for the sake of
   robustness but should only produce lowercase scheme names for
   consistency.</cite>


 * <div style='margin-left:2em'>
 *
 *      <p><strong>URI and PATH</strong>
 *      <p>A path is always defined for a URI, though the
 *      defined path may be empty (zero length?).
 *      <p>Path is a common concept for file systems.
 *      File system paths are not normally percent-encoded and they simply
 *      do not allow some characters inside path expressions.
 *      <p class='note'>Windows/DOS file/folder names do not allow the following
 *      9 characters:<code>\ / ? : * &quot; &lt; | &gt; </code>
 *      in windows explorer names that start or end with the char <code>.</code> are not allowed,
 *		[msg:You must type a file name] in command line they are allowed.
 *      For LINUX all characters are allowed apart [.] [..] [ ] and [/]
 *      <p>This is almost the same of what happens for the URI path
 *      component also if the URI path theoretically allows more characters
 *      (that are anyway better not to use since they cannot find place on file systems).
 *		This interface exposes the path also in unescaped form, this might be
 *		wrong but is what most of the API do.
 *      <p><strong>query component</strong>
 *
 *      <p>Some implementations of URI make available the query in unescaped form.
 *      This for me is wrong since the query might loose of meaning in the process of
 *      unescaping. See an example in the {@link #getQuery()} method.
 *      <p>See also discussion <a href='http://lists.w3.org/Archives/Public/uri/2006Jan/0025.html'>
 *      http://lists.w3.org/Archives/Public/uri/2006Jan/0025.html</a>.
 *      <p>Query must be opaque since its meaning depends on the scheme.<ul>
 *		<li>There is no standard about query internal structure,
 *		<li>separator for parameters might be [&amp;] or [;].
 *      <li>Parameters could be specified also using application/x-www-form-urlencoded
 *      so [+] in place of [%20] etc.
 *		</ul>
 * 		<p>Note on + and %20 : When sent in an HTTP GET request,
 *		application/x-www-form-urlencoded data is included
 *		in the query component of the request URI
 *		(When sent in an HTTP POST request or via email,
 *		the data is placed in the body of the message,
 *		and the name of the media type is included in the message's Content-Type header),
 *		since the x-www-form-urlencoded  format allows + as placeholder
 *		(this should be syntactically ok since + is reserved in the query),
 *		for spaces inside the query it is only a x-www-form-urlencoded format parser that
 *		provided of the query component (percent encoded of course),
 *		will be able to decode the + as a space.


 *      <p><strong>URI path resolving / normalization - meaningless path</strong>
 *      <p>Some implementations of URI (java.net.URI) resolve and normalize paths that attempt
 *      to reach files before the root making available in the resolved or normalized
 *      URI a path that does not   mean anything like "/../../".
 *      <p>In this cases the beahvior of implementations of this interface must be to return
 *      empty string as path after normalization.
 *      <p>In general, after that resolution brings an empty path,
 *      checking that the path component of the relative URI passed
 *      to {@link #resolve(URI)} method is not empty is enough to determine that
 *      resolution brought an invalid result.
 *      <p><strong>URI null and empty string path.</strong>
 *      <p>An empty string path can be present because an  uri has been created with no path
 *       <ul><p><em>Example</em>:
 *      <li><code>http://www.example.org</code>
 *      <li><code>http://www.example.org#fds</code>
 *      <li><code>#fds</code>
 *      <li><code>?param=value</code></ul>
 *      <p>But also because the URI after normalization contains an meaningless path:
 *       <ul><p><em>Example</em>:
 *      <li><code>http://www.example.org/../../file.xml</code>
 *      <li><code>http://www.example.org/s/../../../p.xml#fds</code></ul>
 *      <p>In my opinion after normalization a meaningless path might
 *      in some cases become the root path (if the URI is not opaque and is absolute),
 *      since <code>http://www.example.org/</code>
 *      and <code>http://www.example.org</code> are commonly considered the same,
 *		but again this depends on the uri schema.
 *      <p><strong>TO DETERMINE</strong>: An empty path for a relative URI should become '.' or [emptystring] ?
 *      <p>A meaningless path should instead remain an empty path after normalization
 *      to signal its error state. Comments on this point ?
 *      <p>Not sure anymore if path empty is good...empty path has different
 *       meaning if relative or absolute URI. When absolute uri the empty path
 *      is also an absolute path. For sure strict mode with error if goes over.
 *      Could use the RFC algorithm for abnormal...
 *      <p><b>Windows network share </b>
 *		<p><a href='http://lists.xml.org/archives/xml-dev/200005/msg00046.html'>
 *		http://lists.xml.org/archives/xml-dev/200005/msg00046.html</a>
 *      <p>Windows shell accepts from file://mik2/dest/pollo.xml to file:///////mik2/distribution/
 *      <p>java.io.File.toURL() gives file://mik2/dest/pollo.xml
 *      <p>java.io.File load and saves from:<pre>
 *          //mik2/dest/pollo.xml  to //////(as many as you want)///////mik2/dest/pollo.xml
 *          \\mik2\dest\pollo.xml  to \\\\\\(as many as you want)\\\\\\\mik2/dest/pollo.xml</pre>
 *      <p>...probably because for java.io.File is ok also /dest/////////pollo.xml
 *      for windows is not ok, must be dest\pollo.xml
 *      <p>Placing a link in IExplorer pointing to [\\mik2\dest] creates a [file://mik2/dest] url
 *      <p>Firefox gives  [file://///mik2/dest/pollo.xml]
  *      <p><strong>Opaque uris</strong><p>Opaque uris are those absolute URIs
  *		whose scheme-specific part does not begin with a slash character ('/').
  *     <p>getSchemeSpecificPart() and getPath()
  *     Can be used with the same effect on an opaque URI.
  *     <p class=note> remains to determine what returns getSchemeSpecificPart()
  *     when is not opaque. For the moment null.
        <p> Should create a getRFCPath() that returns the escaped
        path or the (escaped) scheme specific part,
        there should be no unescaped schemespecific ?
 * </div>
 *
 * <p><strong>Threading</strong>
 * <p>This URI interface is immutable and so it might
 * be shared between threads.
 *
 * <p><strong>Implementations</strong>
 * <p>One of the scopes of this package is to
 * provide an URI implementation of not
 * based on the JDK(1.4+) <a href='http://java.sun.com/j2se/1.4.2/docs/api/java/net/URI.html'>
 * <code>java.net.URI</code></a> implementation, for dependency
 * reasons and because that class has been created before RFC3986
 * and has some inconsistent behavior respect to that specification.
 * At the moment exist two implementations, one based on JDK1.4+
 * <code>java.net.URI</code> and an other one based on
 * the <code>org.apache.util.URI</code> class, both wrapped by
 * a 'correction' adaptor.
 * <p>Test cases have been produced using some uri tests and examples available
 * on the web. They are available here [link to be added TODO].
 *
 * <p>References
 * <ol>
* <li><a href='http://www.ietf.org/rfc/rfc3986.txt'>
 * http://www.ietf.org/rfc/rfc3986.txt</a> (text)
 * <a target='_blank' href='http://www.ietf.org/rfc/rfc3986.txt'>
 * [new window]</a>
 * <li><a href='http://www.apps.ietf.org/rfc/rfc3986.html'>
 * http://www.apps.ietf.org/rfc/rfc3986.html</a> (html)
 * <a target='_blank' href='http://www.apps.ietf.org/rfc/rfc3986.html'>
 * [new window]</a>
 * <li><a href='http://jakarta.apache.org/commons/httpclient/'>
 * http://jakarta.apache.org/commons/httpclient/ - Jakarta HTTP Commons</a>
  * <li><a href='http://www.haskell.org/ghc/docs/latest/html/libraries/network/Network-URI.html'>
 * http://www.haskell.org/ghc/docs/latest/html/libraries/network/Network-URI.html</a>
 * <li><a href='http://www.w3.org/Addressing/URL/4_URI_Recommentations.html'>
 * http://www.w3.org/Addressing/URL/4_URI_Recommentations.html</a>
 * </ol>
 *
 */
public interface URI
{

    /**
     * <em>Convenience method</em> that
     * returns whether the URI is
     * absolute (has a scheme component).
     * <p>Since an implementation of this
     * interface cannot return an empty
     * string from {@link #getScheme()),
     * this method is equivalent to:
     * <pre class=code>
     *     return scheme!=null;
     * </pre>
     * <p>Note that in RFC Section 4.3
     * <a href='http://www.apps.ietf.org/rfc/rfc3986.html#sec-4.3'>
     * http://www.apps.ietf.org/rfc/rfc3986.html#sec-4.3</a>
     * an absolute uri is specified as also
     * having no fragment.
     * <blockquote>
     * Some protocol elements allow only the absolute form of a URI without
     * a fragment identifier. For example, defining a base URI for later
     * use by relative references calls for an absolute-URI syntax rule
     * that does not allow a fragment.
     * </blockquote>
     * <p>Here instead only the scheme presence is tested.
     */
    boolean isAbsolute();

    /**
     * <em>Convenience method</em> that
     * returns whether the URI is a relative URI
     * and has as path component a relative
     * path, that is a path that is empty
     * or does not start with the char <code>/</code>.
     * <p>This method is equivalent to:
     * <pre class=code>
     * !uri.isAbsolute() &&
     *      (uri.isPathEmpty() || uri.getPEPath().charAt(0)!='/')
     * </pre>
     */
    boolean isRelativePath();


    /**
     * Returns the <code>scheme</code>
     * component of the URI.
     * <p>
     * <pre class=code>
     * scheme      = ALPHA *( ALPHA / DIGIT / "+" / "-" / "." )
     * </pre>
     * <p>The URI <code>scheme</code>
     * cannot be empty, it is either absent
     * or with a non-empty value
     * so this method must never return an empty
     * string.
     * <p>The URI <code>scheme</code> cannot
     * contain percent encoded characters
     * so there is no difference between the
     * two forms.
     * <p>The URI <code>scheme</code> is case - unsensitive,
     * upper case scheme are accepted
     * but this method returns always a lowercase
     * string.
     * <blockquote>
     * FROM RFC3986 - Section 3.1 - Scheme
     * <p><cite>Although schemes are case-
     *  insensitive, the canonical form is lowercase
     * and documents that  specify schemes must do so
     * with lowercase letters.  An implementation
     * should accept uppercase letters as equivalent
     * to lowercase in scheme names
     * (e.g., allow "HTTP" as well as "http")
     * for the sake of  robustness but should
     * only produce lowercase scheme names for
     * consistency.</cite>
     * </blockquote>
     * @return the lower case value of the scheme
     * component or null when the scheme is absent,
     * never empty string.
     */
    String getScheme();

    /**
     * Tells whether or not this URI is opaque.
     * An opaque URI is not parsed beyond the scheme component.
     * When the uri is opaque its scheme specific part
     * can be retrieved using {@link #getSchemeSpecificPart()}
     */

    public boolean isOpaque();

    /**
     * Returns the decoded scheme-specific
     * part of this URI when opaque.
     */
    public String getSchemeSpecificPart();



    /**
     * Returns the <code>authority</code>
     * component of the URI.
     * The authority value returned by
     * this method is unescaped.
     */
    String getAuthority();

	//hm...2 cases empty and absent boolean hasAuthority();
    /**
	 * Retrieves the <b>p</b>ercent-<b>d</b>ecoded
	 * path component of the uri.
	 */
	String getPDPath();
	/**
	 * Retrieves the <b>p</b>ercent <b>e</b>ncoded
	 * path component of the uri.
	 */
	String getPEPath();
	/**
	 * Signals whether the path component
	 * is empty.
	 * <p>
	 * <blockquote class=code>
	 * <p>RFC3986 Section 3.3 - Path
	 * <p><cite>
	 * <pre>A path is always defined for a URI, though the
     * defined path may be empty (zero length).
     * Use of the slash character
     * to indicate hierarchy is only required when a URI will be used as the
     * context for relative references.  For example, the URI
     * &lt;mailto:fred@example.com> has a path of "fred@example.com", whereas
     * the URI &lt;foo://info.example.com?fred> has an empty path.</cite>
     * </blockquote>
     */
    boolean isPathEmpty();

	/**
	 * Signals whether the path component
	 * does not start with slash '/' character.
	 * <p>
	 * <blockquote class=code>
	 * <p>RFC3986 Section 3.3 - Path
	 * <p><cite>
	 * <pre>path-rootless = segment-nz *( "/" segment )</pre></cite>
     * </blockquote>
     * <p>For example the URI
     * &lt;mailto:fred@example.com> has a path of "fred@example.com"
     * and so is root less.
     * <p>When the result of this method is anded together
     * with the result of {@link #isAbsolute()},
     * it generate the result of {@link #isOpaque()}
     */
    boolean isPathRootless();

    /**
     * Returns the percent encoded <code>query</code>
     * component of the URI.
     * <pre class=code>
	 * query = *( pchar / "/" / "?" )
     * </pre>
     * <p>The query is always percent encoded,
     * since subsequent decoding and encoding
     * might cause data lost.
     * <p>Example of data loss (param value containing & symbol):
     * <p><pre class=code>
     * URI: http://127.0.0.1:8080/Test.jsp?current=R%26D
     * QUERY param name: current
     * QUERY param value: R&D
     * UNESCAPED QUERY: current=R&D
     * ESCAPED QUERY: current=R&D
     *		!!! error:
     *		[char '&' is interpreted as a query param delimiter]
     * </pre>
     * @return the percent encoded query string value
     * or null when no query component.
     * Might return the empty string.
     */
    String getQuery();

    /**
     * <em>Convenience method</em> that
     * returns whether the
     * <code>query</code> is <em>absent</em>.
     * <p>Equivalent to:
     * <pre class=code>
     * uri.getQuery()!=null
     * </pre>
     * <p>When the query is empty
     * (a zero length string)
     * this method returns true.
     * <p>An empty query is built for
     * example from an uri like the following:
     * &lt;&lt;<code>http://www.example.com/index.jsp?</code>&gt;&gt;
     *
     */
    public boolean hasQuery();

    /**
     * Returns the <code>fragment</code>
     * component of the URI.
     * The path value returned by
     * this method is unescaped.
     */
    String getFragment();

	/**
	 * Returns the percent encoded fragment.
	 * <pre class=code>
	 * fragment    = *( pchar / "/" / "?" )
     * </pre>
	 * <blockquote class=code>
	 * <p>RFC3986 Section 3.5 - Fragment
	 * <p><cite>
     * The fragment identifier component of a URI allows indirect
     * identification of a secondary resource by reference to a
     * primary resource and additional identifying information.
     * The identified secondary resource may be some portion or subset
     * of the primary resource, some view on representations of
     * the primary resource, or some other resource defined or
     * described by those representations. A fragment identifier
     * component is indicated by the presence of a number sign
     * ("#") character and terminated by the end of the URI.
     * @return the percent encoded fragment or null when no fragment.
     * Might return the empty string.
	 */
    String getPEFragment();
    /**
     * <em>Convenience method</em> that
     * returns whether the
     * fragment is <em>absent</em>.
     * <p>Equivalent to:
     * <pre class=code>
     * uri.getFragment()!=null
     * </pre>
     * <p>When the fragment is empty
     * (a zero length string)
     * this method returns true.
     * <p>An empty fragment is built for
     * example from an uri like the following:
     * &lt;&lt;<code>http://www.example.com/index.jsp#</code>&gt;&gt;
     */
    boolean hasFragment();
    /**
     * Returns a readable URI.
     * The returned string
     * can be only used
     * for presentation scopes
     * since it is possible that
     * is not anymore parsable
     * by a {@link URIFactory}.
     */
    String toReadableURI();

    /**
     * Returns the string representation
     * of an URI in percent encoded form.
     */
    String toString();

    /**
     * Resolves the given URI against this URI.
     */
    public URI resolve(URI relative) throws URIException;

    /**
     * Relativizes the given URI against this URI.
     * The relativization of the given URI against
     * this URI is computed as follows:
     * <ol><li>
     * If either this URI or the given URI are opaque,
     * or if the scheme and authority components
     * of the two URIs are not identical,
     * or if the path of this URI is not a prefix
     * of the path of the given URI, then the given URI is returned.
     * <li>Otherwise a new relative hierarchical
     * URI is constructed with query and fragment
     * components taken from the given URI
     * and with a path component computed by
     * removing this URI's path from the
     * beginning of the given URI's path.
     * </ol>
     */
    //public URI relativize(URI uri) throws URIException;

    /**
     * Retrieves an uri that is the copy of this
     * URI with no fragment.
     */
    public URI removeFragment() throws URIException;

    /**
     * Normalizes this URI's path.
     * <p>When in the relative path there are more
     *  relative path ".." segments than there are hierarchical levels in the
     *  base URI's path this method returns an URI with <em>empty path</em>.
     * <p>See  RFC 2396, section 5.2, step 6, sub-steps c through f;
     * <blockquote><code>from Appendix C.2. "Abnormal Examples"</code>
     *  <p>Parsers must be careful in handling the case where there are more
     *  relative path ".." segments than there are hierarchical levels in the
     *  base URI's path.  Note that the ".." syntax cannot be used to change
     *  the authority component of a URI.
     *  <pre class=code>
     *  BASE URI: http://a/b/c/d;p?q
     *
     *  ../../../g    =  http://a/../g
     *  ../../../../g =  http://a/../../g
     *  </pre>
     *  <p>In practice, some implementations strip leading relative symbolic
     * elements (".", "..") after applying a relative URI calculation, based
     * on the theory that compensating for obvious author errors is better
     * than allowing the request to fail.  Thus, the above two references
     * will be interpreted as "http://a/g" by some implementations.
     * </blockquote>
     * <p>Implementations of this interface won't return <code>http://a/g</code>
     * in this case but <code>http://a</code>
     * <blockquote>
     * <p>Continuing from  RFC 2396
     * <p>Similarly, parsers must avoid treating "." and ".." as special when
     *  they are not complete components of a relative path.
     * <pre class=code>
     *  /./g          =  http://a/./g
     *  /../g         =  http://a/../g
     *  g.            =  http://a/b/c/g.
     *  .g            =  http://a/b/c/.g
     *  g..           =  http://a/b/c/g..
     *  ..g           =  http://a/b/c/..g
     * </pre>
     * </blockquote>
     * <p>Implementations of this interface must give results that conform
     * to this last list of examples.
     * <p>See also class description.
     */
    URI normalize() throws URIException;

    /**
     * Retrieves this URI as a java.net.URL
     * @throws IllegalArgumentException - If this URL is not absolute
     * @throws MalformedURLException - If a protocol handler for the URL
     *      could not be found, or if some other error occurred
     *      while constructing the URL
     * @return the URL instance, never null.
     */
    java.net.URL toURL() throws java.net.MalformedURLException;

///////////////// OUTSIDE RFC - EXTENSIONS

    /**
     * <em>Not standard</em> -
     * Returns the percent decoded
     * name part of the path
     * component. The name is the
     * value of the last segment of the
     * path.
     * <p><table class='Bordered' cellpadding=4 border=1>
     * <tr><th>path</th><th>name</th></tr>
     * <tr><td>/a/b.xml</td><td>b.xml</td></tr>
     * <tr><td>/a/</td><td>[empty string]</td></tr>
     * <tr><td>/</td><td>[empty string]</td></tr>
     * </table>
     */
    String getName();
}
