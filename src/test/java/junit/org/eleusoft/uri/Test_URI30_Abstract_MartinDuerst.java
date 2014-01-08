package junit.org.eleusoft.uri;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public abstract class Test_URI30_Abstract_MartinDuerst extends BaseTestRFCExamples
{

	protected Properties props ;

	protected Test_URI30_Abstract_MartinDuerst() {
		super();
		InputStream is =
			getInputStreamFromFileName("MartinDuerstTest.properties");
	 	if (is == null)
	 	    throw new IllegalStateException("no file [MartinDuerstTest.properties], deploy error ?");
		props = new Properties();
	 	try
	 	{
	 		props.load(is);
		}
		catch(IOException ioe)
		{
			ioe.printStackTrace();
			throw new RuntimeException("Cannot load properties");
		}


	}

	protected abstract void doPercentEncodingTestFromProperty(String prop) throws Exception;




	/**

	from base uri:

	"http://a/b/c/d;p?q"


	81 ./g:h http://a/b/c/g:h

	**/

	public void testMartinDuerst81() throws Exception
	{
		doRFCExampleTest("./g:h", "http://a/b/c/g:h");
	}


	public void testMartinDuerst101() throws Exception
	{
		doPercentEncodingTestFromProperty("101");
	}

	public void testMartinDuerst111() throws Exception
	{
		doPercentEncodingTestFromProperty("111");
	}

	public void testMartinDuerst112() throws Exception
	{
		doPercentEncodingTestFromProperty("112");
	}

	public void testMartinDuerst121() throws Exception
	{
		doPercentEncodingTestFromProperty("121");
	}

	public void testMartinDuerst122() throws Exception
	{
		doPercentEncodingTestFromProperty("122");
	}
	public void testMartinDuerst_MV_HomePage() throws Exception
	{
		doPercentEncodingTestFromProperty("homepage"); //added m.v.
	}








}