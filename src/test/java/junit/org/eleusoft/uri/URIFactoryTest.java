package junit.org.eleusoft.uri;

import org.eleusoft.uri.URIException;
import org.eleusoft.uri.URIFactory;
import org.eleusoft.uri.URIProvider;

public class URIFactoryTest
{

    public static void main(String[] args) throws URIException
    {
        //System.setProperty(URIProvider.class.getName(), "org.eleusoft.uri.apache.ApacheURIProvider");
        System.out.println("---");
URIFactory.isAbsoluteURI("");
        System.out.println("Eleusoft URI Factory");
        System.out.println("Current provider:");
        //System.out.println(URIFactory..getClass().getName());
        System.out.println("---");
    }
}
