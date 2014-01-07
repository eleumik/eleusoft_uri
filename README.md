eleusoft_uri
============

An URI parsing Java package with multiple implementations and a complete test suite.

This library includes a parser for URI(s) and for some URI components like the path and the query string. It includes also some URI-scheme specific helpers, for example for the file: scheme and the jar: scheme.

This library includes a JUnit test suite with almost 500 tests, that have been collected from many sources like the URI RFC 3986, the URI Mailing List (uri@w3.org), some tests found in the web (thanks Martin Duerst, Graham Klyne) and many  other custom tests.  

This library has been started before 2005, when servers could run a version of Java < 1.4 (only from Java 1.4 the java.net.URI class has been included in Java). There are mainly two implementations for the URI parser: one based on a modified version of org.apache.commons.httpclient.URI and one based on java.net.URI, also adapted (and one without fixes, for test comparison). Tests show less failures for the modified Apache implementation; for standard usage both versions are good enough.

The library has been used mainly to be able to be independent from the underlying implementationas. It has been also a way to correct some issues present in known implementations: cross testing of different implementations has been handy for finding and fixing the major issues.

build
-----

Both Maven and Ant are supported, a pom.xml and a build.xml file are provided, use [ant jaronly] to create the jar, [ant test] to compile and run the tests. Can be compiled in Java < 4 skipping the java4 package.

dependencies
------------

No dependencies, Junit for testing. 

license
-------

Apache License 2.0



