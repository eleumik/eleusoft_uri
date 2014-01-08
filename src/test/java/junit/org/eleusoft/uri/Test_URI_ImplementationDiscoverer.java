package junit.org.eleusoft.uri;

import org.eleusoft.uri.URIException;
import org.eleusoft.uri.URIFactory;


public class Test_URI_ImplementationDiscoverer extends BaseTestURI
{

	public Test_URI_ImplementationDiscoverer() {
		super(getImplName());
	}


	@Override
	public String getName()
	{
	    // TODO Auto-generated method stub
	    return getImplName();
	}

	//////////////////////////////////////////////////

	// BEGIN NORMALIZATION TESTS

	static final String IDIS = "Implementation discoverer,Implementation";
	public void testShowingImplementation() throws Exception
	{
		String clazz = getImplName();
		info(IDIS, new String[]{clazz});
		//fail("Implementation in use is :" + clazz);

	}
    protected static String getImplName() //throws URIException
    {
        try
        {
            return URIFactory.createURI("magic://abr.aca.dab/r.a").getClass().getName();
        }
        catch(URIException e)
        {
            return "unknown";
        }
    }

}