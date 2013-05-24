package junit.org.eleusoft.uri;


public class Test_URI21_RFC3986_AbnormalExamples extends BaseTestRFCExamples
{

	public Test_URI21_RFC3986_AbnormalExamples() {
		super();
	}


	/**

	from base uri:

	"http://a/b/c/d;p?q"


	5.4.2 Abnormal Examples
	number relative absolute
	 Although the following abnormal examples are unlikely to occur in normal practice, all URI parsers should be capable of resolving them consistently. Each example uses the same base as above.
	 Parsers must be careful in handling cases where there are more relative path ".." segments than there are hierarchical levels in the base URI's path. Note that the ".." syntax cannot be used to change the authority component of a URI.
	31 ../../../g http://a/g
	32 ../../../../g http://a/g
	 Similarly, parsers must remove the dot-segments "." and ".." when they are complete components of a path, but not when they are only part of a segment.
	41 /./g http://a/g
	42 /../g http://a/g
	43 g. http://a/b/c/g.
	44 .g http://a/b/c/.g
	45 g.. http://a/b/c/g..
	46 ..g http://a/b/c/..g
	 Less likely are cases where the relative URI reference uses unnecessary or nonsensical forms of the "." and ".." complete path segments.
	51 ./../g http://a/b/g
	52 ./g/. http://a/b/c/g/
	53 g/./h http://a/b/c/g/h
	54 g/../h http://a/b/c/h
	55 g;x=1/./y http://a/b/c/g;x=1/y
	56 g;x=1/../y http://a/b/c/y
	 Some applications fail to separate the reference's query and/or fragment components from a relative path before merging it with the base path and removing dot-segments. This error is rarely noticed, since typical usage of a fragment never includes the hierarchy ("/") character, and the query component is not normally used within relative references.
	61 g?y/./x http://a/b/c/g?y/./x
	62 g?y/../x http://a/b/c/g?y/../x
	63 g#s/./x http://a/b/c/g#s/./x
	64 g#s/../x http://a/b/c/g#s/../x

	 -------------------
	 71a and 71b MOVED TO Test_URI04Compatibility_RFC3986_AbnormalExamples_OnlyOneCanBeGood.java

	 Some parsers allow the scheme name to be present in a relative URI reference
	 if it is the same as the base URI scheme. This is considered to be a loophole
	 in prior specifications of partial URI [RFC1630]. Its use should be avoided,
	 but is allowed for backward compatibility.

	 71a http:g http:g (for strict parsers)
	 71b http:g http://a/b/c/g (for backward compatibility)

	 --------------------

	Additional examples
	These examples don't appear in the URI spec, but work the same way as those above.

	number relative absolute
	81 ./g:h http://a/b/c/g:h


	**/



	public void testAbormal_31() throws Exception
	{
		doRFCExampleTest("../../../g", "http://a/g");
	}

	public void testAbormal_32() throws Exception
	{
		doRFCExampleTest("../../../../g", "http://a/g");
	}
	/**
	 	41 /./g http://a/g
	 	42 /../g http://a/g
	 	43 g. http://a/b/c/g.
	 	44 .g http://a/b/c/.g
	 	45 g.. http://a/b/c/g..
	 	46 ..g http://a/b/c/..g
	*/
	public void testAbormal_41() throws Exception
	{
		doRFCExampleTest("/./g", "http://a/g");
	}
	public void testAbormal_42() throws Exception
	{
		doRFCExampleTest("/../g", "http://a/g");
	}
	public void testAbormal_43() throws Exception
	{
		doRFCExampleTest("g.", "http://a/b/c/g.");
	}
	public void testAbormal_44() throws Exception
	{
		doRFCExampleTest(".g", "http://a/b/c/.g");
	}
	public void testAbormal_45() throws Exception
	{
		doRFCExampleTest("g..", "http://a/b/c/g..");
	}
	public void testAbormal_46() throws Exception
	{
		doRFCExampleTest("..g", "http://a/b/c/..g");
	}
	/**
		51 ./../g http://a/b/g
		52 ./g/. http://a/b/c/g/
		53 g/./h http://a/b/c/g/h
		54 g/../h http://a/b/c/h
		55 g;x=1/./y http://a/b/c/g;x=1/y
		56 g;x=1/../y http://a/b/c/y
	**/
	public void testAbormal_51() throws Exception
	{
		doRFCExampleTest("./../g", "http://a/b/g");
	}
	public void testAbormal_52() throws Exception
	{
		doRFCExampleTest("./g/.", "http://a/b/c/g/");
	}
	public void testAbormal_53() throws Exception
	{
		doRFCExampleTest("g/./h", "http://a/b/c/g/h");
	}
	public void testAbormal_54() throws Exception
	{
		doRFCExampleTest("g/../h", "http://a/b/c/h");
	}
	public void testAbormal_55() throws Exception
	{
		doRFCExampleTest("g;x=1/./y", "http://a/b/c/g;x=1/y");
	}
	public void testAbormal_56() throws Exception
	{
		doRFCExampleTest("g;x=1/../y", "http://a/b/c/y");
	}


	/**

	 Some applications fail to separate the reference's query and/or fragment
	 components from a relative path before merging it with the base path and
	 removing dot-segments. This error is rarely noticed, since typical usage
	 of a fragment never includes the hierarchy ("/") character, and the query
	 component is not normally used within relative references.


		61 g?y/./x http://a/b/c/g?y/./x
		62 g?y/../x http://a/b/c/g?y/../x
		63 g#s/./x http://a/b/c/g#s/./x
		64 g#s/../x http://a/b/c/g#s/../x
	**/

	public void testAbormal_61() throws Exception
	{
		doRFCExampleTest("g?y/./x", "http://a/b/c/g?y/./x");
	}

	public void testAbormal_62() throws Exception
	{
		doRFCExampleTest("g?y/../x", "http://a/b/c/g?y/../x");
	}

	public void testAbormal_63() throws Exception
	{
		doRFCExampleTest("g#s/./x", "http://a/b/c/g#s/./x");
	}

	public void testAbormal_64() throws Exception
	{
		doRFCExampleTest("g#s/../x", "http://a/b/c/g#s/../x");
	}

	public void testAbormal_81_Addition() throws Exception
	{
		doRFCExampleTest("./g:h", "http://a/b/c/g:h");
	}






}