package junit.org.eleusoft.uri;


public class Test_URI20_RFC3986_NormalExamples extends BaseTestRFCExamples
{

	public Test_URI20_RFC3986_NormalExamples() {
		super();
		//InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("junit/org/eleusoft/uri/tests.xml");

	}

	public 	void testExamples_Section_1_1_2_FTP() throws Exception
	{
		createAndCheckToStringEquals("ftp://ftp.is.co.za/rfc/rfc1808.txt");
	}
	public 	void testExamples_Section_1_1_2_HTTP() throws Exception
	{
		createAndCheckToStringEquals("http://www.ietf.org/rfc/rfc2396.txt");
	}

	public 	void testExamples_Section_1_1_2_LDAP() throws Exception
	{
		createAndCheckToStringEquals("ldap://[2001:db8::7]/c=GB?objectClass?one");
	}
	public 	void testExamples_Section_1_1_2_MAILTO() throws Exception
	{
		createAndCheckToStringEquals("mailto:John.Doe@example.com");
	}
	public 	void testExamples_Section_1_1_2_NEWS() throws Exception
	{
		createAndCheckToStringEquals("news:comp.infosystems.www.servers.unix");
	}
	public 	void testExamples_Section_1_1_2_TEL() throws Exception
	{
		createAndCheckToStringEquals("tel:+1-816-555-1212");
	}
	public 	void testExamples_Section_1_1_2_TELNET() throws Exception
	{
		createAndCheckToStringEquals("telnet://192.0.2.16:80/");
	}
	public 	void testExamples_Section_1_1_2_URN() throws Exception
	{
		createAndCheckToStringEquals("urn:oasis:names:specification:docbook:dtd:xml:4.1.2");
	}

	/**

	from base uri:

	"http://a/b/c/d;p?q"

	number relative absolute
	1 g:h g:h
	2 g http://a/b/c/g
	3 ./g http://a/b/c/g
	4 g/ http://a/b/c/g/
	5 /g http://a/g
	6 //g http://g
	7 ?y http://a/b/c/d;p?y
	8 g?y http://a/b/c/g?y
	9 #s http://a/b/c/d;p?q#s
	10 g#s http://a/b/c/g#s
	11 g?y#s http://a/b/c/g?y#s
	12 ;x http://a/b/c/;x
	13 g;x http://a/b/c/g;x
	14 g;x?y#s http://a/b/c/g;x?y#s
	15 "" http://a/b/c/d;p?q
	16 . http://a/b/c/
	17 ./ http://a/b/c/
	18 .. http://a/b/
	19 ../ http://a/b/
	20 ../g http://a/b/g
	21 ../.. http://a/
	22 ../../ http://a/
	23 ../../g http://a/g

	PLUS eXTENSIONS FOR 6

	doRFCExampleTest("//g/b", "http://g/b");
	doRFCExampleTest("//g/b?q", "http://g/b?q");
	doRFCExampleTest("//g/b?q#f", "http://g/b?q#f");
	doRFCExampleTest("//g/b#f", "http://g/b#f");
	**/

	public void testCheckBaseURIOK() throws Exception
	{
		createAndCheckToStringEquals(getBaseURI());
	}

	public void testNormal_1() throws Exception
	{
		doRFCExampleTest("g:h", "g:h");
	}
	public void testNormal_2() throws Exception
	{
			doRFCExampleTest("g", "http://a/b/c/g");

	}
	public void testNormal_3() throws Exception
	{
			doRFCExampleTest("./g", "http://a/b/c/g");

	}

	/**

	4 g/ http://a/b/c/g/
		5 /g http://a/g
		6 //g http://g
	**/

	public void testNormal_4() throws Exception
	{
			doRFCExampleTest("g/", "http://a/b/c/g/");

	}
	public void testNormal_5() throws Exception
	{
			doRFCExampleTest("/g", "http://a/g");

	}
	public void testNormal_6() throws Exception
	{
			doRFCExampleTest("//g", "http://g");

	}

	public void testNormal_6_Extension1() throws Exception
	{
			doRFCExampleTest("//g/b", "http://g/b");
			doRFCExampleTest("//g/b?q", "http://g/b?q");
			doRFCExampleTest("//g/b?q#f", "http://g/b?q#f");
			doRFCExampleTest("//g/b#f", "http://g/b#f");

	}


	/**

	 	7 ?y http://a/b/c/d;p?y
	 	8 g?y http://a/b/c/g?y
	 	9 #s http://a/b/c/d;p?q#s

	**/

	public void testNormal_7() throws Exception
	{
			doRFCExampleTest("?y", "http://a/b/c/d;p?y");

	}
	public void testNormal_8() throws Exception
	{
			doRFCExampleTest("g?y", "http://a/b/c/g?y");

	}
	public void testNormal_9() throws Exception
	{
			doRFCExampleTest("#s", "http://a/b/c/d;p?q#s");

	}

	/**

		 	10 g#s http://a/b/c/g#s
		 	11 g?y#s http://a/b/c/g?y#s
		 	12 ;x http://a/b/c/;x
		**/

	public void testNormal_10() throws Exception
	{
			doRFCExampleTest("g#s", "http://a/b/c/g#s");

	}
	public void testNormal_11() throws Exception
	{
			doRFCExampleTest("g?y#s", "http://a/b/c/g?y#s");

	}
	public void testNormal_12() throws Exception
	{
			doRFCExampleTest(";x", "http://a/b/c/;x");

	}


	// 13 g;x http://a/b/c/g;x
	// 14 g;x?y#s http://a/b/c/g;x?y#s



	public void testNormal_13() throws Exception
	{
			doRFCExampleTest("g;x", "http://a/b/c/g;x");

	}
	public void testNormal_14() throws Exception
	{
			doRFCExampleTest("g;x?y#s", "http://a/b/c/g;x?y#s");

	}

	// 15 "" http://a/b/c/d;p?q
	// 16 . http://a/b/c/

	public void testNormal_15() throws Exception
	{
			doRFCExampleTest("", "http://a/b/c/d;p?q");

	}
	public void testNormal_16() throws Exception
	{
			doRFCExampleTest(".", "http://a/b/c/");

	}

	// 17 ./ http://a/b/c/
	//  18 .. http://a/b/
	// 19 ../ http://a/b/

	public void testNormal_17() throws Exception
	{
			doRFCExampleTest("./", "http://a/b/c/");

	}
	public void testNormal_18() throws Exception
	{
			doRFCExampleTest("..", "http://a/b/");

	}
	public void testNormal_19() throws Exception
	{
			doRFCExampleTest("../", "http://a/b/");

	}

	// 20 ../g http://a/b/g
	// 21 ../.. http://a/
	// 22 ../../ http://a/
	// 23 ../../g http://a/g


	public void testNormal_20() throws Exception
	{
			doRFCExampleTest("../g", "http://a/b/g");

	}
	public void testNormal_21() throws Exception
	{
			doRFCExampleTest("../..", "http://a/");

	}
	public void testNormal_22() throws Exception
	{
				doRFCExampleTest("../../", "http://a/");

	}
	public void testNormal_23() throws Exception
	{
			doRFCExampleTest("../../g", "http://a/g");

	}




}