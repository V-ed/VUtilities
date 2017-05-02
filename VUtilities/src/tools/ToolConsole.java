package tools;

import java.util.ArrayList;
import java.util.Arrays;

// TODO Javadoc

public class ToolConsole {
	
	private ToolConsole(){}
	
	public static void printTable(String[] columnTitles,
			ArrayList<Object[]> table, boolean hasBorders)
			throws IllegalArgumentException{
		
		int numberOfColumns = table.get(0).length;
		
		for(int i = 0; i < table.size(); i++){
			if(table.get(i).length != numberOfColumns){
				throw new IllegalArgumentException(
						"The table is not rectangular.");
			}
		}
		
		int[] biggestStringLengthInColumns = new int[numberOfColumns];
		
		if(columnTitles != null)
			for(int i = 0; i < numberOfColumns; i++){
				biggestStringLengthInColumns[i] = columnTitles[i].length();
			}
		
		for(int i = 0; i < numberOfColumns; i++){
			
			for(int j = 0; j < table.size(); j++){
				
				int stringLength = table.get(j)[i].toString().length();
				
				if(biggestStringLengthInColumns[i] < stringLength)
					biggestStringLengthInColumns[i] = stringLength;
				
			}
			
		}
		
		String separator = null;
		if(hasBorders)
			separator = createSeparatorLine(numberOfColumns,
					biggestStringLengthInColumns);
		
		// Print start horizontal border
		if(hasBorders)
			System.out.println(separator);
		// End start horizontal border
		
		if(columnTitles != null){
			// Print Column titles
			System.out.println(createRowLine(hasBorders, columnTitles,
					biggestStringLengthInColumns));
			// End column titles
			
			// Print middle horizontal line border
			if(hasBorders)
				System.out.println(separator);
			else System.out.println();
			// End middle horizontal border
		}
		
		// Print rows
		for(int i = 0; i < table.size(); i++){
			System.out.println(createRowLine(hasBorders, table.get(i),
					biggestStringLengthInColumns));
		}
		// End rows
		
		// Print last horizontal line border
		if(hasBorders)
			System.out.println(separator);
		// End last horizontal border
		
	}
	
	private static String createSeparatorLine(int numberOfColumns,
			int[] columnSizes){
		
		String line = "+-";
		for(int i = 0; i < numberOfColumns; i++){
			for(int j = 0; j < columnSizes[i]; j++){
				line += "-";
			}
			if(i < numberOfColumns - 1)
				line += "-+-";
		}
		line += "-+";
		return line;
		
	}
	
	private static String createRowLine(boolean hasBorders, Object[] values,
			int[] biggestStringLengthInColumns){
		
		String line = "";
		if(hasBorders){
			line = "| ";
			for(int i = 0; i < values.length; i++){
				line += values[i];
				int stringLength = String.valueOf(values[i]).length();
				for(int j = 0; j < biggestStringLengthInColumns[i]
						- stringLength; j++){
					line += " ";
				}
				if(i < values.length - 1)
					line += " | ";
			}
			line += " |";
		}
		else{
			for(int i = 0; i < values.length; i++){
				line += values[i];
				int stringLength = String.valueOf(values[i]).length();
				for(int j = 0; j < biggestStringLengthInColumns[i]
						- stringLength; j++){
					line += " ";
				}
				line += "\t";
			}
		}
		return line;
		
	}
	
	public static void printTable(String[] columnTitles, Object[][] table,
			boolean hasBorders){
		printTable(columnTitles, new ArrayList<Object[]>(Arrays.asList(table)),
				hasBorders);
	}
	
	public static void printTable(String[] columnTitles,
			ArrayList<Object[]> table){
		printTable(columnTitles, table, true);
	}
	
	public static void printTable(String[] columnTitles, Object[][] table){
		printTable(columnTitles, table, true);
	}
	
	public static void printTable(ArrayList<Object[]> table, boolean hasBorders){
		printTable(null, table, hasBorders);
	}
	
	public static void printTable(ArrayList<Object[]> table){
		printTable(null, table, true);
	}
	
	public static void printTable(Object[][] table, boolean hasBorders){
		printTable(null, table, hasBorders);
	}
	
	public static void printTable(Object[][] table){
		printTable(null, table, true);
	}
	
}
