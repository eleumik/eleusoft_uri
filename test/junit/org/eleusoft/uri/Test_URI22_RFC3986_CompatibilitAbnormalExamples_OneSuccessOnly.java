package junit.org.eleusoft.uri;


public class Test_URI22_RFC3986_CompatibilitAbnormalExamples_OneSuccessOnly extends BaseTestRFCExamples
{

	public Test_URI22_RFC3986_CompatibilitAbnormalExamples_OneSuccessOnly() {
		super();
	}


	/**
	 -------------------

	 Some parsers allow the scheme name to be present in a relative URI reference
	 if it is the same as the base URI scheme. This is considered to be a loophole
	 in prior specifications of partial URI [RFC1630]. Its use should be avoided,
	 but is allowed for backward compatibility.

	 71a http:g http:g (for strict parsers)
	 71b http:g http://a/b/c/g (for backward compatibility)

	 --------------------
	**/


	public void testAbormal_71a_Strict() throws Exception
	{
		doRFCExampleTest("http:g", "http:g");
	}

	public void testAbormal_71b_Compatibility() throws Exception
	{
		doRFCExampleTest("http:g", "http://a/b/c/g");
	}


}