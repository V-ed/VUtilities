package vutils.mysql;

import vutils.tools.ToolCode;

// TODO Javadoc

public abstract class TableItem {
	
	protected MySQLDatabase database;
	protected String tableName;
	
	protected String idColumnName;
	protected int idItem = -1;
	
	protected String[] columnNames;
	protected Object[] values;
	
	protected TableItem(MySQLDatabase database, String tableName,
			String idColumnName, String[] columnNames, boolean isFirstValueID,
			Object... values){
		
		// Testing for illegal arguments
		ToolCode.testNullOrEmptyArg(database, "database object");
		ToolCode.testNullOrEmptyArg(tableName, "table's name");
		ToolCode.testNullOrEmptyArg(idColumnName,
				"column name of this object's id");
		ToolCode.testNullOrEmptyArg(columnNames, "column names array");
		ToolCode.testNullOrEmptyArg(values, "values array");
		// Arguments good, proceed.
		
		this.database = database;
		
		this.tableName = tableName;
		this.idColumnName = idColumnName;
		
		if(isFirstValueID)
			this.idItem = (int)values[0];
		
		this.columnNames = columnNames;
		this.values = values;
		
	}
	
	public String getTableName(){
		return tableName;
	}
	
	public String getIDColumnName(){
		return idColumnName;
	}
	
	public int getID(){
		return idItem;
	}
	
	public void setID(int idItem){
		
		if(idItem < 0)
			throw new IllegalArgumentException(
					"The id cannot be a negative number.");
		else
			this.idItem = idItem;
		
	}
	
	public String[] getColumnNames(){
		return columnNames;
	}
	
	public String[] getColumnNamesWithoutID(){
		
		String[] columnsWithoutID = new String[columnNames.length - 1];
		
		for(int i = 0; i < columnsWithoutID.length; i++){
			columnsWithoutID[i] = columnNames[i + 1];
		}
		
		return columnsWithoutID;
	}
	
	public Object[] getValues(){
		return values;
	}
	
	public Object[] getValuesWithoutID(){
		
		Object[] values = new Object[this.values.length - 1];
		
		for(int i = 0; i < values.length; i++){
			values[i] = this.values[i + 1];
		}
		
		return values;
		
	}
	
	public void addToDatabase(){
		
		idItem = database.addToTable(tableName, getColumnNamesWithoutID(),
				getValuesWithoutID());
		
	}
	
	public void modifyItem(Object... values){
		
		ToolCode.testNullOrEmptyArg(values, "values array");
		
		if(idItem == -1)
			throw new IllegalStateException(
					"The id of this item must be set, acheivable by adding this item to the database (with element.addToDatabase()).");
		else{
			
			Object[] valuesWithID = new Object[values.length + 1];
			
			valuesWithID[0] = getID();
			
			for(int i = 1; i <= values.length; i++){
				valuesWithID[i] = values[i - 1];
			}
			
			this.values = valuesWithID;
			
			database.modifyObject(tableName, idColumnName, idItem,
					getColumnNamesWithoutID(), getValuesWithoutID());
			
		}
		
	}
	
	public void modifyItem(TableItem modifiedItem){
		
		if(modifiedItem == null)
			throw new IllegalArgumentException(
					"The modified item cannot be null.");
		
	}
	
	public void removeFromDatabase(){
		
		if(idItem == -1)
			throw new IllegalStateException(
					"The id of this item must be set, acheivable by adding this item to the database (with element.addToDatabase()).");
		else
			database.removeFromTable(tableName, idColumnName, idItem);
		
	}
	
	@Override
	public abstract String toString();
	
}
