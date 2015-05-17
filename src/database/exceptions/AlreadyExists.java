package database.exceptions;
/**
 * 
 * @author Jes�s V�zquez
 *
 */
public class AlreadyExists extends Exception
{
	public AlreadyExists() 
	{
		super(); 
	}
	
	public AlreadyExists(String arg0)
	{ 
		super(arg0); 
	}
	
	public AlreadyExists(Throwable arg0) 
	{
		super(arg0); 
	}
	
	public AlreadyExists(String arg0, Throwable arg1) 
	{
		super(arg0, arg1); 
	}

}
