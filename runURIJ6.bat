REM SET PROPS=-Djavax.xml.parsers.DocumentBuilderFactory=org.apache.xerces.jaxp.DocumentBuilderFactoryImpl
SET PROPS=-Dorg.eleusoft.uri.URIProvider=org.eleusoft.uri.java4.Java4URIProvider
call java5
call java -version
java %PROPS% -classpath F:\JUnit\junit3.8.1\junit.jar;..\classes;.\lib\apacheutils.jar;.; junit.swingui.TestRunner junit.org.eleusoft.uri.URITestSuite
