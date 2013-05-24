package junit.org.eleusoft.uri;


public class Test_URI06_Authority extends BaseTestRFCExamples
{

	public Test_URI06_Authority() {
		super();

	}

// http://www.ninebynine.org/Software/HaskellUtils/Network/UriTest.csv
/*
	Syntax021,uri:AbsId,http://[FEDC:BA98:7654:3210:FEDC:BA98:7654:3210]:80/index.html,,,IPv6 tests
	Syntax022,uri:AbsId,http://[1080:0:0:0:8:800:200C:417A]/index.html,,,
	Syntax023,uri:AbsId,http://[3ffe:2a00:100:7031::1],,,
	Syntax024,uri:AbsId,http://[1080::8:800:200C:417A]/foo,,,
	Syntax025,uri:AbsId,http://[::192.9.5.5]/ipng,,,
	Syntax026,uri:AbsId,http://[::FFFF:129.144.52.38]:80/index.html,,,
	Syntax027,uri:AbsId,http://[2010:836B:4179::836B:4179],,,
	Syntax028,uri:RelRf,//[2010:836B:4179::836B:4179],,,
	Syntax029,uri:InvRf,[2010:836B:4179::836B:4179],,,

*/

	// abc://me@[0001:ABCD::0001]/home/root?a=b;x=y
	// abc://[::0001]/home/root#X

	public void testCreateURI_Authority() throws Exception
	{
		parseURIAndCheckComponents("ftp://[1234:2345::ABCD]:1234/dir/file#ABC",
			"ftp", "[1234:2345::ABCD]:1234",  "/dir/file", null, "ABC");
	}

	//http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4671369
	public void testParseAndCheckComp_HostStartsWithNumber_BUG4671369() throws Exception
	{
		parseURIAndCheckComponents("http://7in1web.com/", "http", "7in1web.com", "/", null, null);
	}


	public void testParseAndCheckComp_HostStartsWithNumber2() throws Exception
	{
		parseURIAndCheckComponents("http://www.3com.com/", "http", "www.3com.com", "/", null, null);
	}




	//  foo://%31.2.3.4/   // must see that is an ip and not a registry-based..

}