package junit.org.eleusoft.uri;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

import junit.framework.AssertionFailedError;
import junit.framework.Test;
import junit.framework.TestListener;

/**
 * Report class that implements TestListener
 */
public class XReport  implements TestListener
{

	int count = 0;
	int runned = 0;
	int errors= 0;
	int failures= 0;

	private Map results = new HashMap();
	private List resultOrder = new ArrayList();

	private XResult currentXResult;
	private String currentName;

	static class XResult
	{
		String[] params;
		String name;
		String failed;
		// THe Test instance for err and failure;
		String error;
		String failure;
		Object object;

		String anchor;
		int index;


	}
	protected final void info(String name, String[] params)
	{
		List list = (List)results.get(name);
		if (list ==null)
		{
			list = new ArrayList();
			results.put(name, list);
			resultOrder.add(name);

		}
		currentXResult = new XResult();
		currentXResult.name = currentName;
		currentXResult.params = params;
		currentXResult.index = count;
		list.add(currentXResult);
	}




	///////////////


	static class URIAndAnchors implements Comparable
	{
		String uri;
		ArrayList anchors = new ArrayList();
		ArrayList xresults = new ArrayList();
		public int compareTo(Object o)
		{
			return uri.compareTo(((URIAndAnchors)o).uri);
		}
	}

	/////////// TestListener METHODS

	public void addError(Test test, java.lang.Throwable t){
	 errors++;
	 // When there is an unhandled exception in the test,
	 // I think endTest is called before...
	 // might happen when running one
	 // !!! and when one test does not call info !!!!
	 if (currentXResult==null)  info("[Test DOES NOT CALL info()] - Error for: " + test.toString(), new String[]{"[Test DOES NOT CALL info()] "} );
	 currentXResult.error = getErrorMsg(test, t);


	}
	public void addFailure(Test test, AssertionFailedError t)
	{
		failures++;
		// See addError
		if (currentXResult==null)  info("[Test DOES NOT CALL info()] - Failure for: " + test.toString(), new String[]{"[Test DOES NOT CALL info()] "} );
		else currentXResult.failure = getErrorMsg(test, t);
	}
	public void endTest(Test test)
	{
		currentXResult = null;
	}
	public void startTest(Test test)
	{
		//System.out.println("Start :" + test);
		runned ++ ;
		//if (currentXResult!=null) throw new Error("already in:" + currentXResult);
		currentName = test.toString();
	}

	///////// PRINT


	void print(Writer out, String baseURI, Writer totalsReport, boolean firstTime) throws IOException
	{
		// This for xml <!DOCTYPE html [ <!ENTITY nbsp "&#160;"> ]>
		// 200705 ? why not directly use numeric character entity ??

		out.write("<html><head><title>Results for org.eleusoft.uri</title>\n<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />");
		out.write("\n<link rel='stylesheet' type='text/css' href='uriResults.css'/></head><body>");

		out.write("\n<div class='menu'>");
		out.write("\n<h1>junit.TestSuite results for package <code>org.eleusoft.uri</code></h1>");
		out.write("Tests runned:" + runned);

		out.write("&#160;<a href='#uriindex'>URI Index</a> ");
		if (errors> 0) out.write("&#160;<a href='#errors'>Errors</a> (" + errors + ")");
		if (failures > 0)  out.write("&#160;<a href='#failures'>Failures</a> (" + failures + ")");
		out.write("&#160; Finished at " + new Date());
		out.write("\n</div>");
		out.write("\n<results>");




		//if (true)return;
		int printed = 0;
		ArrayList failuresList = new ArrayList();
		ArrayList errorList = new ArrayList();
		Map uriAnchorsMap = new TreeMap(); // map from uri to uri+test anchors
		HashMap uriXResultMap = new HashMap ();
		out.write("<ul>");
		int index =0;
		Iterator iter = resultOrder.iterator();
		while(iter.hasNext())
		{
			index++;
			String test = (String)iter.next();
			StringTokenizer st = new StringTokenizer(test, ",",false);
			String name = (String)st.nextElement();
			out.write("<li>");
			out.write("<a href='#test" + index + "'>");
			out.write(name);
			out.write("</a></li>");
		}
		out.write("</ul>");
		index =0;
		iter = resultOrder.iterator();

		// init first time totals report
		if (firstTime)
		{
			totalsReport.write("\n<style>");
			totalsReport.write("\n.e{background-color:red;color:white}");
			totalsReport.write("\n.f{background-color:#ffa0a0;color:black}");
			totalsReport.write("\n.t{background-color:#80ff80;color:black}");
			totalsReport.write("\n.linetable{font-size:0.7em;}");
			totalsReport.write("\n.linetable TD{padding:0.1em;}");
			totalsReport.write("\n.plus{font-size:1.2em;font-weight:bolder;cursor:hand;background-color:#99ccff;color:black}");

			totalsReport.write("\n</style>\n");

		}

		String sID = "ID" + Integer.toString(baseURI.hashCode() * baseURI.hashCode(),16);

		//totalsReport.write("\n\n\n<table cellspacing=0 class=linetable><tr>");
	//	totalsReport.write("\n<th><span class=plus onclick='document.getElementById(\"" + sID + "\").style.display=\"block\";'>+</span></th>");
		totalsReport.write("\n<div><span class=plus onclick='document.getElementById(\"" + sID + "\").style.display=\"block\";'>+</span>");

		while(iter.hasNext())
		{
			index++;
			String test = (String)iter.next();
			StringTokenizer st = new StringTokenizer(test, ",",false);
			String name = (String)st.nextElement();
			out.write("<a name='test" + index + "'><!-- --></a>");
			out.write("<table border='1'>");
			out.write("<test><tr class='header'>");
			out.write("<th colspan='99'><name>");
			out.write(name);
			out.write("</name></th></tr><tr><th>Index</th><th>Name</th>");
			while (st.hasMoreElements())
			{
				String header = (String)st.nextElement();
				out.write("<th>");
				out.write(header);
				out.write("</th>");

			}
			out.write("<th>Result</th></tr>");


			List testResults = (List)results.get(test);
			for (int i=0,len=testResults.size();i<len;i++)
			{
				printed++;
				XResult xs = (XResult)testResults.get(i);

				String uri = xs.params.length ==0 ? null : xs.params[0];
				if (uri==null) uri = "NULL URI";// just to see it...and then fix..
				URIAndAnchors uaf = (URIAndAnchors )uriAnchorsMap.get(uri);
				if (uaf==null)
				{
					uaf = new URIAndAnchors();
					uaf.uri =  uri;
					uriAnchorsMap.put(uri, uaf);
				}

				String anchor = "A" + printed;
				xs.anchor = anchor;
				uaf.anchors.add(anchor);
				uaf.xresults.add(xs);



				writeXResult(out, xs, anchor);
				if (xs.failure!=null)
				{
					failuresList.add(xs);
					totalsReport.write("<span class='f'>");
					totalsReport.write("<a title='" + htmlEscape(xs.name + " - " + xs.failure) + "' href='"  + baseURI + '#' + xs.anchor + "'>F</a>");
					totalsReport.write("</span>");

				}
				else if (xs.error!=null)
				{
					errorList.add(xs);
					totalsReport.write("<span class='e'>");
					totalsReport.write("<a title='" + htmlEscape(xs.name + " - " + xs.failure) + "' href='"  + baseURI + '#' + xs.anchor + "'>E</a>");
					totalsReport.write("</span>");

				}
				else
				{
					totalsReport.write("<span class='t'>");
					totalsReport.write("<a title='" + htmlEscape(xs.name) + "' href='"  + baseURI + '#' + xs.anchor + "'>T</a>");
					totalsReport.write("</span>");
				}


			}
			out.write("</test>");

			out.write("</table>");

		}
		totalsReport.write("</div>");


		Iterator it = failuresList.iterator();
		if (it.hasNext())
		{
			out.write("<a name='failures'><!-- --></a>");
			out.write("<ul class='listerrors'><p>Failures list</p>");
			while(it.hasNext())
			{
				out.write("<li><span class='fail'>");
				XResult xs = (XResult)it.next();
				out.write("<a href='#"  +xs.anchor + "'>" + xs.anchor + "</a>");
				if (xs.failure!=null)
				{
					out.write(" | ");
					out.write(htmlEscape(xs.failure));
				}
				else
				{
					throw new Error();
				}
				out.write("</span></li>");
			}
			out.write("</ul>");
		}

		// added also append to global report totalsReport
		totalsReport.write("<div style='display:none' id='" + sID + "' class='item'>");
		totalsReport.write("\n<span class='failuresh'>FAILURES: </span><span class='failures'>");

		it = failuresList.iterator();
		if (it.hasNext())
		{
			out.write("<table border='1'><caption>Failures (" + failuresList.size() + ") </caption>");
			while(it.hasNext())
			{
				out.write("<tr><td class='fail'><error>");
				XResult xs = (XResult)it.next();
				writeXResult(out, xs, null);
				out.write("</error></td></tr>");


				totalsReport.write("<span class='fail'>");
				totalsReport.write("<a title='" + htmlEscape(xs.name + " - " + xs.failure) + "' href='"  + baseURI + '#' + xs.anchor + "'>" + xs.anchor + "</a>");
				totalsReport.write("</span><span class='nocss'> | </span>");

			}
			out.write("</table>");


		}

		// end div failures totals report
		totalsReport.write("\n</span>");
		totalsReport.write("\n<span class='errorsh'>ERRORS: </span><span class='errors'>");

		it = errorList.iterator();
		if (it.hasNext())
		{
			out.write("<a name='errors'><!-- --></a>");
			out.write("<ul class='listerrors'><p>Errors list</p>");
			while(it.hasNext())
			{
				out.write("<li><span class='fail'>");
				XResult xs = (XResult)it.next();

				out.write("<a href='#"  +xs.anchor + "'>" + xs.anchor + "</a>");
				if (xs.error!=null)
				{
					out.write(" | ");
					out.write(htmlEscape(xs.error));
				}
				else
				{
					throw new Error();
				}
				out.write("</span></li>");
			}
			out.write("</ul>");
		}

		it = errorList.iterator();
		if (it.hasNext())
		{
			out.write("<table border='1'><caption>ERRORS (" + errorList.size() + ") </caption>");
			while(it.hasNext())
			{
				out.write("<tr><td class='err'><error>");
				XResult xs = (XResult)it.next();
				writeXResult(out, xs, null);
				out.write("</error></td></tr>");

				totalsReport.write("<span class='err'>");
				totalsReport.write("<a title='" + htmlEscape(xs.name + " - " + xs.error) + "' href='"  + baseURI + '#' + xs.anchor + "'>" + xs.anchor + "</a>");
				totalsReport.write("</span><span class='nocss'> | </span>");

			}
			out.write("</table>");


		}

		// end div errors totals report
		totalsReport.write("\n</span>");
		// end totals report
		totalsReport.write("\n</div>\n\n");



		out.write("<info>TestCase runned: " + runned + ", Results printed:" + printed + " , Run method calls:" + count);
		out.write("</info>");

		out.write("<uriindex>");
		out.write("<a name='uriindex'><!-- --></a>");
		out.write("<table border='1'><caption>URI Alphabetical Index</caption>");
		iter = uriAnchorsMap.keySet().iterator();
		while(iter.hasNext())
		{
			out.write("<tr><td class='uindx'>");
			String uri = (String)iter.next();
			URIAndAnchors uaf =
				(URIAndAnchors )uriAnchorsMap.get(uri);
			String urilabel = uri.length() > 80 ? uri.substring(0,80) + "(too long, see test)" : uri;
			out.write(htmlEscape(urilabel));
			out.write("</td><td class='uanchors'>");
			for (int x=0,len=uaf.anchors.size();x<len;x++)
			{
				String anchor = (String)uaf.anchors.get(x);
				XResult xresult = (XResult) uaf.xresults.get(x);
				String cssClass = xresult.failure!=null  ? "faillink" : xresult.error!=null ? "errlink" : "normlink";
				out.write("<span class='" + cssClass  + "'>");
				out.write("<A HREF='#");
				out.write(anchor);
				out.write("'>" + anchor + "</A> ");
				out.write("</span>");

			}
			out.write("</td></tr>");

		}
		out.write("</table>");
		out.write("</uriindex>");
		out.write("</results>");
		out.write("</body></html>");

	}

	private static void writeXResult( Writer out, XResult xs, String anchor) throws IOException
	{

		String[] s = xs.params;
		out.write("\n<tr class='xresult'>");
		if (anchor!=null)
		{
			out.write("<A NAME='");
			out.write(anchor);
			out.write("'><!-- --></A>");
		}
		out.write("<td class='tdid'>");
		if (anchor==null)
		{
			out.write("<A HREF='#");
			out.write(xs.anchor);
			out.write("'>");
		}
		out.write(xs.anchor);
		if (anchor==null)
		{
			out.write("</A>");
		}
		if (anchor != null) out.write("&#160;-&#160;" + xs.index);

		out.write("</td>");




		out.write("\n<td class='tdname'>" + xs.name + "</td>");

		for (int z=0;z<s.length;z++)
		{
			out.write("\n<td>");
			String zz = s[z];
			out.write(zz==null ? "&lt;null>" : zz.length()==0 ? "&lt;emptystring>" : formatHTMLValue(htmlEscape(zz)));
			out.write("</td>");

		}
		if (xs.error!=null)
		{
			out.write("\n<td class='error'>ERROR</td></tr>");
			out.write("\n<tr><td colspan='99' class='emsg'><pre>");
			out.write(htmlEscape(xs.error));
			out.write("</pre></td>");
		}
		else if (xs.failure!=null)
		{
			out.write("\n<td class='failure'>FAILED</td></tr>");
			out.write("\n<tr><td colspan='99' class='fmsg'><pre>");
			out.write(htmlEscape(xs.failure));
			out.write("</pre></td>");
		}
		else
		{
			out.write("\n<td class='ok'>OK</td>");
		}
		out.write("\n</tr>");
	}

	private static String formatHTMLValue(String s)
	{
		return s;
		/** this is shit
		int len = s.length();
		if (len<40) return s;
		StringBuffer sb = new StringBuffer ();
		for (int i=0;i<len;i+=40)
		{
			if (i+40<len) sb.append(s.substring(i, i+40));
			else sb.append(s.substring(i));
			sb.append("<br/>");

		}
		return sb.toString();
		**/
	}


	public  String getErrorMsg(Object t ,Throwable e)
	{
		if (true)
		{
			return runned + " " +  e.getMessage();
		}
		StringWriter sw = new StringWriter();
		PrintWriter pw  = new PrintWriter(sw);
		e.printStackTrace(pw);
		return sw.toString();
	}

	// HTML ESCAPE

	private static String htmlEscape(String s1)
	{
		return HTML.htmlEscape(s1);
	}

} //END XReport CLASS


