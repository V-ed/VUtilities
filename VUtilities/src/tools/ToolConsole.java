package tools;

import java.util.ArrayList;
import java.util.Arrays;

// TODO Javadoc

public class ToolConsole {
	
	private ToolConsole(){}
	
	public static void printTable(ArrayList<Object[]> table, boolean hasBorders){
		
		// TODO for in for loop
		
	}
	
	public static void printTable(Object[][] table, boolean hasBorders){
		printTable(new ArrayList<Object[]>(Arrays.asList(table)), hasBorders);
	}
	
	public static void printTable(ArrayList<Object[]> table){
		printTable(table, true);
	}
	
	public static void printTable(Object[][] table){
		printTable(table, true);
	}
	
}
