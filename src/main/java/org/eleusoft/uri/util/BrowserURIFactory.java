package org.eleusoft.uri.util;

import org.eleusoft.uri.URI;
import org.eleusoft.uri.URIException;
import org.eleusoft.uri.URIFactory;

/**
 * A class for handling real world
 * URIs as found inside HTML pages.
 * <p>These "real wold URIS" are called
 * also <em>link text</em>.
 * <p><b>Rules:</b>
 * <ol>
 * <li>Trim link text using {@link String#trim()}.
 * <li>Remove all TAB (#9), CR (#10) and LF (#13) instances from the link text.
 * <li>Replace all SPACE (#32) instances with %20 in the link text.
 * </ol>
 * <p>Notes:
 * <li>There are uris with '^' inside query, like
 * <a href='http://asp.usatoday.com/community/tags/topic.aspx?req=ssts&amp;tag=climate^weather'>
 * http://asp.usatoday.com/community/tags/topic.aspx?req=ssts&amp;tag=climate^weather</a>
 * <li>Some have '{' like this ebay link (that BTW, if you remove the '{' looks like to work..)
 * <a href='http://ilapi.ebay.com/ws/eBayISAPI.dll?EKServer&ai=d{~u+(&bdrcolor=FFCC00&catid=1%20260&cid=0&endcolor=FF0000&endtime=n&fntcolor=000000&fs=3&hdrcolor=FFFFCC&height=height=250&img=n&maxprice=6000&minprice=50&num=3&numbid=n&popup=n&prvd=4&query=(a,%20e,%20i,%20o,%20u)&siteid=101&sort=MetaNewSort&sortby=endtime&sortdir=desc&srchdesc=n&tlecolor=FFCE63&track=1016485%20&width=250'>
 * http://ilapi.ebay.com/ws/eBayISAPI.dll?EKServer&ai=d{~u+(&bdrcolor=FFCC00&catid=1%20260&cid=0&endcolor=FF0000&endtime=n&fntcolor=000000&fs=3&hdrcolor=FFFFCC&height=height=250&img=n&maxprice=6000&minprice=50&num=3&numbid=n&popup=n&prvd=4&query=(a,%20e,%20i,%20o,%20u)&siteid=101&sort=MetaNewSort&sortby=endtime&sortdir=desc&srchdesc=n&tlecolor=FFCE63&track=1016485%20&width=250</a>
 * <li>one with '|'
 * <a href='http://www.amazon.com/gp/vote/ref=cm_r8n_yesno_submit/002-4803921-7160058?ie=UTF8&type=pipeline&2115|R22WM8TV6JM1KH.contentAssoc.1=1&2115|R22WM8TV6JM1KH.contentAssoc.2.id=6301884361&uid=2115R22WM8TV6JM1KHHelpfulReviews1&uri=/gp/product/6301884361&2115|R22WM8TV6JM1KH.contentAssoc.2.type=ProductSet&qv=1&contentId=2115|R22WM8TV6JM1KH&label=Helpful&qk=*Version*&2115|R22WM8TV6JM1KH.contentAssoc.2=1&2115|R22WM8TV6JM1KH.contentAssoc.1.id=A2Z8SIZVKESSJC&ifRes=showYesNoCommunityResponse&2115|R22WM8TV6JM1KH.contentAssoc.1.type=AmazonCustomer&context=Reviews&needsSignIn=1&voteValue=-1'>
 * http://www.amazon.com/gp/vote/ref=cm_r8n_yesno_submit/002-4803921-7160058?ie=UTF8&type=pipeline&2115|R22WM8TV6JM1KH.contentAssoc.1=1&2115|R22WM8TV6JM1KH.contentAssoc.2.id=6301884361&uid=2115R22WM8TV6JM1KHHelpfulReviews1&uri=/gp/product/6301884361&2115|R22WM8TV6JM1KH.contentAssoc.2.type=ProductSet&qv=1&contentId=2115|R22WM8TV6JM1KH&label=Helpful&qk=*Version*&2115|R22WM8TV6JM1KH.contentAssoc.2=1&2115|R22WM8TV6JM1KH.contentAssoc.1.id=A2Z8SIZVKESSJC&ifRes=showYesNoCommunityResponse&2115|R22WM8TV6JM1KH.contentAssoc.1.type=AmazonCustomer&context=Reviews&needsSignIn=1&voteValue=-1</a>
 * <li>Look at this ..
 * <a href='http://www.nytimes.com/adx/bin/adx_click.html?type=goto&page=movies2.nytimes.com/gst/movies/filmography.html&pos=Middle1C&camp=Etrade-Q2a-522884-nyt1&ad=etradeSearchSPonetradedotcomQ2c.html&goto=http://ad.doubleclick.net/click;h=v2|3815|0|0|*|e;94593879;0-0;0;16522891;31-1|1;20176941|20194835|1;;?https://us.etrade.com/e/t/jumppage/viewjumppage?PageName=CSAlanding&tb=3917&WT.mc_id=3917'>
 * http://www.nytimes.com/adx/bin/adx_click.html?type=goto&page=movies2.nytimes.com/gst/movies/filmography.html&pos=Middle1C&camp=Etrade-Q2a-522884-nyt1&ad=etradeSearchSPonetradedotcomQ2c.html&goto=http://ad.doubleclick.net/click;h=v2|3815|0|0|*|e;94593879;0-0;0;16522891;31-1|1;20176941|20194835|1;;?https://us.etrade.com/e/t/jumppage/viewjumppage?PageName=CSAlanding&tb=3917&WT.mc_id=3917</a>

 */
public class BrowserURIFactory
{
	private BrowserURIFactory(){}

	public static URI createURI(String browserURI)
		throws URIException
	{
		return URIFactory.createURI(interpretURI(browserURI));
	}
	public static String interpretURI(String browserURI)
	{
		StringBuffer sb = new StringBuffer();
		char[] arr = browserURI.trim().toCharArray();
		for(int i=0,l=arr.length;i<l;i++)
		{
			char c = arr[i];
			if (c!=10 && c!=13 && c!=9)
			{
				if (c==32)
			    {
			    	sb.append("%20");
			    }
			    else sb.append(c);
			}
		}
		return sb.toString();
	}
}
