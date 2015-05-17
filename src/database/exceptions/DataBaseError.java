package database.exceptions;

public class DataBaseError extends Exception
{
	public DataBaseError() 
	{
		super(); 
	}
	
	public DataBaseError(String arg0)
	{ 
		super(arg0); 
	}
	
	public DataBaseError(Throwable arg0) 
	{
		super(arg0); 
	}
	
	public DataBaseError(String arg0, Throwable arg1) 
	{
		super(arg0, arg1); 
	}
}


