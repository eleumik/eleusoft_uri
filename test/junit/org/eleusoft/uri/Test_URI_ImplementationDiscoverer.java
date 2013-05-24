package junit.org.eleusoft.uri;


public class Test_URI_ImplementationDiscoverer extends BaseTestURI
{

	public Test_URI_ImplementationDiscoverer() {
		super();
	}



	//////////////////////////////////////////////////

	// BEGIN NORMALIZATION TESTS

	static final String IDIS = "Implementation discoverer,Implementation";
	public void testFailShowingImplementation() throws Exception
	{
		String clazz = createURI("magic://abr.aca.dab/r.a").getClass().getName();
		info(IDIS, new String[]{clazz});
		fail("Implementation in use is :" + clazz);

	}

}