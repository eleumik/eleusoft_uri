eleusoft_uri
============

An URI parsing Java package with multiple implementations and a complete test suite.

This library includes a parser for URI(s) and helpers to work with some URI components like the path and the query string. It includes also some URI-scheme specific helpers, for example for the `file:` scheme and the `jar:` scheme.

[JAVADOC](http://eleumik.github.io/eleusoft_uri/javadoc/)

Test Suite
----------

This library includes a JUnit test suite with almost 500 tests, many have been collected from many sources like the URI RFC 3986, the URI Mailing List (uri@w3.org) and other works, see [credits](#Credits). 

The test suite produces an HTML report at each run, these are the results in 2014:

[apache-uri-modified 496T 26F 2E](http://eleumik.github.io/eleusoft_uri/testresult/uriResults_T496_F26_E2_org.eleusoft.uri.apache.ApacheURIProvider$ApacheURI_2014-01-08T01-33.html)

[jdk-uri-modified 496T 53F 9E](http://eleumik.github.io/eleusoft_uri/testresult/uriResults_T496_F53_E9_org.eleusoft.uri.java4.Java4URIProvider$JDK14URI_2014-01-08T01-33.html)




Details
-------

This library has been started before 2005, when servers could run a version of Java < 1.4 (only from Java 1.4 the java.net.URI class has been included in Java). There are mainly two implementations for the URI parser: one based on a modified version of `org.apache.commons.httpclient.URI` and one based on `java.net.URI`, also adapted (and one without fixes, for test comparison). Tests show less failures for the modified Apache implementation; for standard usage both versions are good enough.

The library has been used mainly to be able to be independent from the underlying implementations. It has been also a way to correct some issues present in known implementations: cross testing of different implementations has been handy for finding and fixing the bugs.



Build
-----

Both Maven and Ant are supported, a `pom.xml` and a `build.xml` file are provided, 

Ant: use `ant -Dmaven=1 jaronly` to create the jar, `ant -Dmaven=1 test` to compile and run the tests. Junit must be in the `lib` dir or its position must be specified with `-Djar.junit=....` when running tests.

Maven: since tests have failures use option `-Dmaven.test.failure.ignore=true`

This package should be compiled with Java 4. Can be compiled in Java < 4 skipping the java4 package.


Dependencies
------------

No dependencies, Junit for testing. 

License
-------

Apache License 2.0

Credits
-------

Martin Duerst

http://lists.w3.org/Archives/Public/uri/2004Apr/0034.html

http://www.w3.org/2004/04/uri-rel-test.html

Graham Klyne

http://lists.w3.org/Archives/Public/uri/2004Feb/0108.html

Adam Costello

http://lists.w3.org/Archives/Public/uri/2004Feb/0114.html

Other

http://lists.w3.org/Archives/Public/uri/2005May/0036.html

http://lists.w3.org/Archives/Public/uri/2005May/0043.html

http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6345409

http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6338951

http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4479463

http://lists.fourthought.com/pipermail/4suite-dev/2006-June/002113.html

Author
------

Michele Vivoda






