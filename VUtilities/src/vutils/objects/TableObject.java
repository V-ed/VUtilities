package vutils.objects;

// TODO Javadoc

public abstract class TableObject {
	
	protected MySQLDatabase database;
	protected String tableName;
	
	protected String idColumnName;
	protected int idObject = -1;
	
	protected String[] columnNames;
	protected Object[] values;
	
	protected TableObject(MySQLDatabase database, String tableName,
			String idColumnName, String[] columnNames, boolean isFirstValueID,
			Object... values){
		
		this.database = database;
		
		this.tableName = tableName;
		this.idColumnName = idColumnName;
		
		if(isFirstValueID)
			this.idObject = (int)values[0];
		
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
		return idObject;
	}
	
	public void setID(int idObject){
		this.idObject = idObject;
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
		
		idObject = database.addToTable(tableName, getColumnNamesWithoutID(),
				getValuesWithoutID());
		
	}
	
	public void modifyItem(Object... values){
		
		if(idObject != -1){
			
			Object[] valuesWithID = new Object[values.length + 1];
			
			valuesWithID[0] = getID();
			
			for(int i = 1; i <= values.length; i++){
				valuesWithID[i] = values[i - 1];
			}
			
			this.values = valuesWithID;
			
			database.modifyObject(tableName, idColumnName, idObject,
					getColumnNamesWithoutID(), getValuesWithoutID());
			
		}
		
	}
	
	public abstract void modifyItem(TableObject modifiedObject);
	
	public void removeFromDatabase(){
		
		if(idObject != -1){
			
			database.removeFromTable(tableName, idColumnName, idObject);
			
		}
		
	}
	
	@Override
	public abstract String toString();
	
}
