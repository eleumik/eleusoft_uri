eleusoft_uri
============

An URI parsing Java package with multiple implementations and a complete test suite.

This library includes a parser for URI(s) and for some URI components like the path and the query string. It includes also some URI-scheme specific helpers, for example for the file: scheme and the jar: scheme.

This library includes a JUnit test suite with almost 500 tests, that have been collected from many sources like the URI RFC(s), the URI Mailing List (uri@w3.org) and some other test suites.  

This library has been started before 2005, when servers could run a version of Java < 1.4: only from Java 1.4 the java.net.URI class has been included in Java. There are two implementations for the URI parser: one based on a modified version of org.apache.commons.httpclient.URI and one based on java.net.URI, also adapted. Tests show less failures for the modified Apache implementation; for standard usage both versions are good enough.

build
-----

Both maven and ant are supported, a build.xml file is provided, use [ant jar] to create the jar, [ant test] to run the tests.

license
-------

Apache License 2.0



