package org.eleusoft.uri;

/**
 * Exception for URI operations
 */
public class URIException extends Exception
{
    
    private Throwable root;
	
	protected String uri;
	
    public URIException(String message, String uri)
    {
        super(message);
        this.uri = uri;
    }
	public URIException(String message, String uri, Throwable t)
    {
        super(message + "\n- Exception URI:" + uri);
        this.uri = uri;
        this.root = t;
    }
    /**
     * The URI string that caused the exception.
     */
    public String getURI()
    {
		return uri;
    }
    /**
	 * For JDK 1.4
	 */
	public Throwable getCause()
	{
		return getRootCause();
	}
	/**
	 * Return the cause of the exception
	 */
	public Throwable getRootCause()
	{
		return root;
	}
	public void printStackTrace() 
	{ 
		synchronized (System.err) 
		{
			printStackTrace(System.err);
		}
    }
	public void printStackTrace(java.io.PrintStream s) 
	{ 
		super.printStackTrace(s);
		
		if (this.root!=null) 
		{
			s.println("Root cause:");
			this.root.printStackTrace(s);
		}
    }
	public void printStackTrace(java.io.PrintWriter writer)
	{
		super.printStackTrace(writer);
		
		if (this.root!=null) 
		{
			writer.println("Root cause:");
			this.root.printStackTrace(writer);
		}
	}

}
