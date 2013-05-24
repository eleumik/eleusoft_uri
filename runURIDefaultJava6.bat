REM HERE WITH DEFAULT PROVIDER see runURIJ4 and runURIApache

call java -version
pause
java -classpath F:\JUnit\junit3.8.1\junit.jar;.\bin;.\lib\apacheutils.jar;.; junit.swingui.TestRunner junit.org.eleusoft.uri.URITestSuite
call c:\java5.bat
call java -version
pause
java -classpath F:\JUnit\junit3.8.1\junit.jar;.\bin;.\lib\apacheutils.jar;.; junit.swingui.TestRunner junit.org.eleusoft.uri.URITestSuite
