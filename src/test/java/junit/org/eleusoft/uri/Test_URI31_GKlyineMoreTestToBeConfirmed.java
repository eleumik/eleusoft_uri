package junit.org.eleusoft.uri;


public class Test_URI31_GKlyineMoreTestToBeConfirmed extends AbstractTestResolveURI
{

	public Test_URI31_GKlyineMoreTestToBeConfirmed() {
		super();
	}


	// http://lists.w3.org/Archives/Public/uri/2004Feb/0108.html
	// "More test cases to be confirmed"
	// Tests are not like in the email

	// perhaps related:
	/*1.2.3 Hierarchical identifiers
	All URI references are parsed by generic syntax parsers when used. However,
	because hierarchical processing has no effect on an absolute URI used
	in a reference unless it contains one or more dot-segments (complete path
	segments of "." or "..", as described in Section 3.3), URI scheme specifications
	can define opaque identifiers by disallowing use of slash characters, question
	mark characters, and the URIs "scheme:." and "scheme:..".
	*/
	public void testResolveNormCheckURI_MoreTestCasesToBeConfirmed_GKlyine_1() throws Exception
	{
		resolveAndCheckURI("http://example.com/path?query#frag", "", "http://example.com/path?query");
	}


	public void testResolveNormCheckURI_MoreTestCasesToBeConfirmed_GKlyine_2() throws Exception
	{
		resolveAndCheckURI("foo:a", "b/c", "foo:b/c"); // same for klyine
	}

	public void testResolveNormCheckURI_MoreTestCasesToBeConfirmed_GKlyine_3() throws Exception
	{
		resolveAndCheckURI("foo:a", "../b/c", "foo:"); // for klyine foo:/b/c
	}


	public void testResolveNormCheckURI_MoreTestCasesToBeConfirmed_GKlyine_4() throws Exception
	{
		resolveAndCheckURI("foo:a", "./b/c", "foo:a/b/c"); // for klyine foo:b/c
	}


	public void testResolveNormCheckURI_MoreTestCasesToBeConfirmed_GKlyine_5() throws Exception
	{
		resolveAndCheckURI("foo://a//b/c", "../../d", "foo://a/d"); // for klyine foo://a/d
	}

	public void testResolveNormCheckURI_MoreTestCasesToBeConfirmed_GKlyine_6() throws Exception
	{
		resolveAndCheckURI("http://a/b/c/d;p?q", "/../g", "http://a/g"); // for klyine http://a/../g for spec he says http://a/g
	}

	// Answer from Adam M. Costello
	// http://lists.w3.org/Archives/Public/uri/2004Feb/0114.html

	public void testResolveNormCheckURI_MoreTestCasesToBeConfirmed_Costello_1() throws Exception
	{
		resolveAndCheckURI("foo:a/b", "../c", "foo:/c");
	}

	public void testResolveNormCheckURI_MoreTestCasesToBeConfirmed_Costello_2() throws Exception
	{
		resolveAndCheckURI("foo:a", ".", "foo:.");
	}

	public void testResolveNormCheckURI_MoreTestCasesToBeConfirmed_Costello_3() throws Exception
	{
		resolveAndCheckURI("foo:a", "..", "foo:..");
	}



	//END TESTS "More test cases to be confirmed"




}