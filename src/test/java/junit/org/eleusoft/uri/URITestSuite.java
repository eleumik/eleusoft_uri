package junit.org.eleusoft.uri;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import junit.framework.Test;
import junit.framework.TestResult;
import junit.framework.TestSuite;

import org.eleusoft.uri.URIException;
import org.eleusoft.uri.URIFactory;

/**
 * 201307
 * Apache 490 3E 29F
 * JDK 490 10e 54f (this does not change for 1.4,1.5,1.6,1.7..)
 * JDK Default 10E 81F
 * 201401
 * Apache 496 2E 27F
 * JDK 496 9E 54F
 * JDK Default 496 9E 81F
 * @author mik
 *
 *
 */
public class URITestSuite extends TestSuite
{

	public static Test suite() {



	    final TestSuite suite= new URITestSuite();
	    //new org.eleusoft.ineo.report.TestSuiteIneoWrapper(new URITestSuite(), "URI Suite", "URISuiteResults");
		//report.addTest(suite);
	    // Basic
	    /**TestSuite b = new TestSuite("My Standard TestSuite");
	    suite.addTest(b);
	    b.addTestSuite(Test_URI_ImplementationDiscoverer.class);
	    b.addTestSuite(Test_URI00_ParseURI.class);
		b.addTestSuite(Test_URI00_ParseURICkComp.class);
		**/
	    // URI parsing
		suite.addTestSuite(Test_URI_ImplementationDiscoverer.class);
	    //if(true)return suite;

	    suite.addTestSuite(Test_URI00_ParseURI.class);
	    //suite.addTestSuite(Test_URI00_ParseURICkComp.class);
	    suite.addTestSuite(Test_URI00_ParseURICkComp_GeneralDelimiters.class);
	    suite.addTestSuite(Test_URI00_ParseURICkComp_SkAuthPathEmptyNull.class);


	    suite.addTestSuite(Test_URI01_ParseInvalidURI.class);




	    // URI normalization
	    suite.addTestSuite(Test_URI02_NormalizeURI.class);
	    // URI API consistency
	    suite.addTestSuite(Test_URI03_CreateURIFromComponents.class);
		// URI resolving
	    suite.addTestSuite(Test_URI04_ResolveURI.class);
	    // Opaque
	    suite.addTestSuite(Test_URI05_Opaque.class);
	    // Authority
		suite.addTestSuite(Test_URI06_Authority.class);

	    // Query
		suite.addTestSuite(Test_URI08_Query.class);
		// Path only URI
		suite.addTestSuite(Test_URI09_CreatePathComponentOnlyURI.class);
	    // Path class
		suite.addTestSuite(Test_URI10_Path.class);

	    // RFC EXAMPLES
	    suite.addTestSuite(Test_URI20_RFC3986_NormalExamples.class);
	    suite.addTestSuite(Test_URI21_RFC3986_AbnormalExamples.class);
	    suite.addTestSuite(Test_URI22_RFC3986_CompatibilitAbnormalExamples_OneSuccessOnly.class);

	    // Parse Scheme / Fragment SchemaFactory API
	    suite.addTestSuite(Test_URI23_ParseSchemeAPI.class);
	    suite.addTestSuite(Test_URI24_ExtractFragmentAPI.class);

		// Martin Duerst tests
	    suite.addTestSuite(Test_URI30_MartinDuerst.class);
	    suite.addTestSuite(Test_URI30B_MartinDuerst.class);

	    // GKlyine tests to be confirmed (corner cases)
	    suite.addTestSuite(Test_URI31_GKlyineMoreTestToBeConfirmed.class);

	    // Real world..
	    suite.addTestSuite(Test_URI32_RealWorldURI.class);
	    suite.addTestSuite(Test_URI33_URIQuery.class);




	    return suite;
	}

	public void runTest(Test test, TestResult result) {
		System.out.println("Test suite runs" + test);
		super.runTest(test, result);
	}

	java.text.SimpleDateFormat dateFormat =
	        new java.text.SimpleDateFormat("yyyy-MM-dd'T'hh-mm");


	public void run(TestResult tr){
		super.run(tr);
		boolean fixedName = Boolean.getBoolean("report-fixed-name");
		try
		{
			XReport xrep = BaseTestURI.getXReport();
			String provider = fixedName ? "fixed" : URIFactory.createURI("ciao:ciao").getClass().getName();
			String reportFileURI = "./uriResults_T" + xrep.count + "_F" + xrep.failures + "_E" + xrep.errors + "_" +  provider + "_" + dateFormat.format(new java.util.Date()) + ".html";

			File file = new File(reportFileURI );
			File total = new File("./URI_totals_" + provider + ".html");
			boolean totalExists = total.exists();
			Writer wthis = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
			Writer wtot = new OutputStreamWriter(new FileOutputStream(total, true), "UTF-8");
			// no multiple instances for these tests ;-)
			xrep.print(wthis, reportFileURI, wtot, !totalExists);
			// fuck the writers close()
			wthis.close();
			wtot.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		catch(URIException e)
		{
			throw new RuntimeException("Could not determine implementation");
		}
		System.out.println("FINISHED URI TEST SUITE");
	}

}



