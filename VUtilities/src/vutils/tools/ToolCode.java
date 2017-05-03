package vutils.tools;

// TODO Javadoc

public class ToolCode {
	
	private ToolCode(){}
	
	public static void testNullOrEmptyArg(Object arg, String nameOfElement){
		
		if(arg == null)
			throw new IllegalArgumentException("The " + nameOfElement
					+ " cannot be null.");
		
		if(arg instanceof String)
			if(((String)arg).isEmpty())
				throw new IllegalArgumentException("The " + nameOfElement
						+ " cannot be empty.");
		
		if(arg instanceof Object[])
			if(((Object[])arg).length == 0)
				throw new IllegalArgumentException("The size of the "
						+ nameOfElement + " cannot be 0.");
		
	}
	
}
