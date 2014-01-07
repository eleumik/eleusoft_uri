package junit.org.eleusoft.uri;

import org.eleusoft.uri.URI;
import org.eleusoft.uri.URIException;

public class Test_URI32_RealWorldURI extends BaseTestURI
{

	public Test_URI32_RealWorldURI() {
		super();
	}

	public void testParse_USATODAY_NotRfCCompliant() throws Exception
	{
		doT("http://asp.usatoday.com/community/tags/topic.aspx?req=ssts&amp;tag=climate^weather");
	}
	public void testParse_EBAY_NotRfCCompliant() throws Exception
	{
		doT("http://ilapi.ebay.com/ws/eBayISAPI.dll?EKServer&ai=d{~u+(&bdrcolor=FFCC00&catid=1%20260&cid=0&endcolor=FF0000&endtime=n&fntcolor=000000&fs=3&hdrcolor=FFFFCC&height=height=250&img=n&maxprice=6000&minprice=50&num=3&numbid=n&popup=n&prvd=4&query=(a,%20e,%20i,%20o,%20u)&siteid=101&sort=MetaNewSort&sortby=endtime&sortdir=desc&srchdesc=n&tlecolor=FFCE63&track=1016485%20&width=250");
	}

	public void testParse_AMAZON_NotRfCCompliant() throws Exception
	{
		doT("http://www.amazon.com/gp/vote/ref=cm_r8n_yesno_submit/002-4803921-7160058?ie=UTF8&type=pipeline&2115|R22WM8TV6JM1KH.contentAssoc.1=1&2115|R22WM8TV6JM1KH.contentAssoc.2.id=6301884361&uid=2115R22WM8TV6JM1KHHelpfulReviews1&uri=/gp/product/6301884361&2115|R22WM8TV6JM1KH.contentAssoc.2.type=ProductSet&qv=1&contentId=2115|R22WM8TV6JM1KH&label=Helpful&qk=*Version*&2115|R22WM8TV6JM1KH.contentAssoc.2=1&2115|R22WM8TV6JM1KH.contentAssoc.1.id=A2Z8SIZVKESSJC&ifRes=showYesNoCommunityResponse&2115|R22WM8TV6JM1KH.contentAssoc.1.type=AmazonCustomer&context=Reviews&needsSignIn=1&voteValue=-1");
	}
	public void testParse_NYTIMES_NotRfCCompliant() throws Exception
	{
		doT("http://www.nytimes.com/adx/bin/adx_click.html?type=goto&page=movies2.nytimes.com/gst/movies/filmography.html&pos=Middle1C&camp=Etrade-Q2a-522884-nyt1&ad=etradeSearchSPonetradedotcomQ2c.html&goto=http://ad.doubleclick.net/click;h=v2|3815|0|0|*|e;94593879;0-0;0;16522891;31-1|1;20176941|20194835|1;;?https://us.etrade.com/e/t/jumppage/viewjumppage?PageName=CSAlanding&tb=3917&WT.mc_id=3917");
	}

	public void testParse_NYTIMES_NotRfCCompliant_NBSP_in_HTML() throws Exception
	{
			// orig link: http://jobs.nytimes.com/careers/rss/jobs/?view=2&nbsp;lookid=nyt&nbsp;tmpId=&nbsp;qHidden=lookiddiversity
			// nbsp !!! what the fuck..
		doT("http://jobs.nytimes.com/careers/rss/jobs/?view=2\u00a0lookid=nyt\u00a0tmpId=\u00A0qHidden=lookiddiversity");
	}

	public void testParse_DOUBLECLICK_NotRfCCompliant() throws Exception
	{
		doT("http://ad.doubleclick.net/imp;v1;f;94593879;0-0;0;16522891;1|1;20176941|20194835|1;;cs=t%3fhttp://m.doubleclick.net/dot.gif");
	}

	public void  testParse_W3C_NotRfCCompliant() throws Exception
	{
		// found on http://www.w3.org/Style/CSS/ June2007
		doT("http://www.awprofessional.com/catalog/product.asp?product_id={CD6CBE58-0498-4E80-8AE7-0A22DD13E2E5}");
	}

	private static final String CACPE = "Parse Real world URI invalid for RFC expecting error for the moment...";
	protected void doT(String uri) throws URIException
	{
		info(CACPE, new String[]{uri});
		try
		{
			URI uriObj = createURI(uri);
			fail("No error but uri:" + uriObj);
		}
		catch(URIException e)
		{
		}
		//String test = uriObj.toString();
		//assertEquals("realWorldURI - parsed [" + test + "]  different from uri [" + uri + "]", uri, test);
	}




	public void testParseRealWorldOK_Apple_SlashInFragment() throws Exception
	{
		parseURIAndCheckComponents("http://developer.apple.com/documentation/Carbon/Conceptual/understanding_utis/understand_utis_intro/chapter_1_section_1.html#//apple_ref/doc/uid/TP40001319-CH201-DontLinkElementID_19",
				"http",
				"developer.apple.com",
				"/documentation/Carbon/Conceptual/understanding_utis/understand_utis_intro/chapter_1_section_1.html",
				null,
				"//apple_ref/doc/uid/TP40001319-CH201-DontLinkElementID_19");

	}




}