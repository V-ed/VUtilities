package objects;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Object used to deal with a MySQL Database.
 * 
 * @version 1.7
 * @author Guillaume Marcoux
 */
public class MySQLDatabase {
	
	private Connection connection;
	private Statement objetRequetes;
	
	/**
	 * Constructor for the MySQLDatabase object. This object is never closing,
	 * which means you shouldn't need to open this object for the same database
	 * twice.
	 * 
	 * @param ip
	 *            Address required to access the MySQL database. <i>Ex.
	 *            <code>localhost</code></i>.
	 * @param databaseName
	 *            Name of the database itself.
	 * @param user
	 *            The user name required to access the database.
	 * @param password
	 *            The password used to access the database.
	 * @throws ClassNotFoundException
	 *             The drivers for MySQL are not installed. <a
	 *             href="https://dev.mysql.com/downloads/connector/j/">Click
	 *             here to go to the MySQL Connector download page</a> if this
	 *             error happens and install the connector.
	 * @throws SQLException
	 *             There was an SQL error, either by entering a wrong
	 *             <code>ip</code> or a non-existant <code>databaseName</code>.
	 */
	public MySQLDatabase(String ip, String databaseName, String user,
			String password) throws ClassNotFoundException, SQLException{
		
		Class.forName("com.mysql.jdbc.Driver");
		
		connection = DriverManager.getConnection("jdbc:mysql://" + ip + "/"
				+ databaseName, user, password);
		
		objetRequetes = connection.createStatement();
		
	}
	
	/**
	 * @param query
	 *            Query to execute.
	 * @return A <code>ResultSet</code> object containing the results of the
	 *         query passed in parameters, <code>null</code> if there's an SQL
	 *         error.
	 */
	public ResultSet executeQuery(String query){
		
		ResultSet results = null;
		
		try{
			results = objetRequetes.executeQuery(query);
		}
		catch(SQLException e){}
		
		return results;
		
	}
	
	/**
	 * Method to return a quick <code>SELECT * FROM <i>tableName</i>;</code>
	 * 
	 * @param tableName
	 *            Name of table to select everything from.
	 * @return A <code>ResultSet</code> object containing every items from the
	 *         table passed in parameters, <code>null</code> if there's an SQL
	 *         error.
	 */
	public ResultSet selectEverythingFrom(String tableName){
		return executeQuery("SELECT * FROM " + tableName);
	}
	
	/**
	 * Method to return a quick
	 * <code>SELECT * FROM <i>tableName</i> WHERE <i>columnName</i> = <i>textToMatch</i>;</code>
	 * 
	 * @param tableName
	 *            Name of table to select everything from.
	 * @param columnName
	 *            Column name to apply the <code>WHERE</code> condition.
	 * @param textToMatch
	 *            Text to search within the column of <code>columnName</code>.
	 * @return A <code>ResultSet</code> object containing every items where
	 *         <code>textToMatch</code>, inside of the column
	 *         <code>columnName</code>, matches in the object's database,
	 *         <code>null</code> if there's an SQL error.
	 */
	public ResultSet getAllContentWhere(String tableName, String columnName,
			Object textToMatch){
		
		String sqlRequest = "SELECT * FROM " + tableName;
		sqlRequest += " WHERE " + columnName + " = \"" + textToMatch + "\"";
		
		return executeQuery(sqlRequest);
		
	}
	
	/**
	 * Method to get all item's content of a specified column.
	 * 
	 * @param donnees
	 *            <code>ResultSet</code> object containing the items to search
	 *            for.
	 * @param columnName
	 *            Column name to get the content from the <code>ResultSet</code>
	 *            object.
	 * @return An <code>Object</code> array containing all item's content of the
	 *         specified column, <code>null</code> if the <code>ResultSet</code>
	 *         is empty or if there's an SQL error.
	 */
	public Object[] getAllContentOfColumn(ResultSet donnees, String columnName){
		
		Object[] results = null;
		
		ArrayList<Object> allContentList = new ArrayList<>();
		
		try{
			
			while(donnees.next()){
				
				allContentList.add(donnees.getString(columnName));
				
			}
			
			results = allContentList.toArray(new Object[allContentList.size()]);
			
		}
		catch(SQLException e){}
		
		return results;
		
	}
	
	/**
	 * Method to get all of an item's content of a row where a condition will be
	 * applied.
	 * 
	 * @param tableName
	 *            Name of the table in which the item is located.
	 * @param columnName
	 *            Name of the column that will be used for the
	 *            <code>WHERE</code> considition.
	 * @param textToMatch
	 *            Text to search in the <code>WHERE</code> condition.
	 * @return An <code>Object</code> array containing all of a row's content.
	 *         If multiple items pass through the condition, only the first item
	 *         will be returned. Returns <code>null</code> if there's an
	 *         <code>SQLException</code> error.
	 */
	public Object[] getRowContentOfTableWhere(String tableName,
			String columnName, String textToMatch){
		
		Object[] rowContent = null;
		
		try(ResultSet resultSet = getAllContentWhere(tableName, columnName,
				textToMatch)){
			
			resultSet.next();
			
			int numberOfColumns = resultSet.getMetaData().getColumnCount();
			
			rowContent = new Object[numberOfColumns];
			
			for(int iCol = 1; iCol <= numberOfColumns; iCol++){
				
				Object object = resultSet.getObject(iCol);
				
				if(object instanceof Boolean)
					object = (boolean)object ? 1 : 0;
				
				rowContent[iCol - 1] = (object == null) ? null : object;
				
			}
			
		}
		catch(SQLException e){}
		
		return rowContent;
		
	}
	
	/**
	 * Method to get everything, literally.
	 * 
	 * @param tableName
	 *            Name of the table of which to get every items inside.
	 * @return An <code>ArrayList</code> of <code>Object</code> arrays. Every
	 *         <code>Object</code> is an item in the table and everything inside
	 *         of it's array is the values of the columns. <code>null</code> if
	 *         there's an SQL error, by entering, for exemple, a table name that
	 *         doesn't exists.
	 */
	public ArrayList<Object[]> getAllContentofTable(String tableName){
		
		ResultSet queryResults = selectEverythingFrom(tableName);
		
		return convertResultSetToArraylistOfObjects(queryResults);
		
	}
	
	/**
	 * Method that returns Objects that matches certain conditions passed in
	 * parameters.
	 * 
	 * @param tableName
	 *            The name of the table in which reasearch will be made.
	 * @param conditions
	 *            A <code>String[]</code> array where all
	 * @return An <code>ArrayList<Object[]></code> object, each index of the
	 *         <code>ArrayList</code> represents a row of the results and each
	 *         column of the <code>Object[]</code>'s array is a column of the
	 *         table in the database. <code>null</code> if there is an
	 *         <code>SQLException</code> error.
	 * @see {@link #getAllContentWhere(String tableName, String[] columnsToSearch, Object[] valuesToSearch, boolean isValuesAbsolute)}
	 */
	public ArrayList<Object[]> getAllContentWhere(String tableName,
			Object... conditions){
		
		String sqlRequest = "SELECT * FROM " + tableName + " WHERE ";
		
		for(int i = 0; i < conditions.length; i++){
			
			sqlRequest += conditions[i];
			
			if(i < conditions.length - 1)
				sqlRequest += " AND ";
			
		}
		
		ResultSet queryResults = executeQuery(sqlRequest);
		
		return convertResultSetToArraylistOfObjects(queryResults);
		
	}
	
	/**
	 * Method that returns Objects based on conditions that will be created with
	 * the columns and values to search. It would make the same by using
	 * {@link #getAllContentWhere(String tableName, Object... conditions)}, but
	 * this current method creates the <code>conditions</code> parameter for
	 * you.<br>
	 * <br>
	 * Ex.:
	 * 
	 * <pre>
	 * String[] columns =
	 * {
	 * 	&quot;name&quot;, &quot;lastName&quot;
	 * };
	 * Object[] values =
	 * {
	 * 	&quot;Bob&quot;, &quot;Dishen&quot;
	 * };
	 * ArrayList&lt;Object[]&gt; ls = database.getAllContentWhere(&quot;persons&quot;, columns,
	 * 		values, false);
	 * </pre>
	 * 
	 * In this example, this SQL request will be created :
	 * 
	 * <pre>
	 * SELECT * FROM <i>persons</i> WHERE <i>name</i> = "<i>Bob</i>" AND <i>lastName</i> = "<i>Dishen</i>"
	 * </pre>
	 * 
	 * And this would be the return table, if the <i>persons</i> is structured
	 * with only three columns, <i>id, name, lastName</i> :
	 * <table border="1">
	 * <tr>
	 * <td></td>
	 * <td>ls.get(0);</td>
	 * <td>ls.get(1);</td>
	 * </tr>
	 * <tr>
	 * <td><i>ls.get(#)</i>[0]</td>
	 * <td>1</td>
	 * <td>2</td>
	 * </tr>
	 * <tr>
	 * <td><i>ls.get(#)</i>[1]</td>
	 * <td><b>Bob</b></td>
	 * <td>Robert</td>
	 * </tr>
	 * <tr>
	 * <td><i>ls.get(#)</i>[2]</td>
	 * <td>Portman</td>
	 * <td><b>Dishen</b></td>
	 * </tr>
	 * </table>
	 * 
	 * @param tableName
	 *            The name of the table in which research will be made.
	 * @param columnsToSearch
	 *            A <code>String</code> array that will be used as the searching
	 *            columns for matching values.
	 * @param valuesToSearch
	 *            An <code>Object</code> array that will be used as the values
	 *            for the condition(s).
	 * @param isValuesAbsolute
	 *            Boolean that determines whether values will be <i>absolute</i>
	 *            (<code>"<u><i>Bob</i></u>", "Under<i>Bob</i>"</code>) if
	 *            <code>true</code>, or inclusive (
	 *            <code>"<u><i>Bob</i></u>", "<u>Under<i>Bob</i></u>"</code>) if
	 *            <code>false</code>.
	 * @return An <code>ArrayList<Object[]></code> object, each index of the
	 *         <code>ArrayList</code> represents a row of the results and each
	 *         column of the <code>Object[]</code>'s array is a column of the
	 *         table in the database. <code>null</code> if there is an
	 *         <code>SQLException</code> error, if there's 0 columns / values or
	 *         the amount of columns doesn't match the number of values.
	 * @see {@link #getAllContentWhere(String tableName, Object... conditions)}
	 */
	public ArrayList<Object[]> getAllContentWhere(String tableName,
			String[] columnsToSearch, Object[] valuesToSearch,
			boolean isValuesAbsolute){
		
		ArrayList<Object[]> list = null;
		
		if(columnsToSearch.length == valuesToSearch.length
				&& columnsToSearch.length > 0){
			
			list = new ArrayList<>();
			
			ArrayList<String> conditions = new ArrayList<>();
			
			for(int i = 0; i < columnsToSearch.length; i++){
				
				if(valuesToSearch[i] instanceof Boolean)
					valuesToSearch[i] = (boolean)valuesToSearch[i] ? 1 : 0;
				
				if(!isValuesAbsolute){
					valuesToSearch[i] = "%" + valuesToSearch[i] + "%";
					conditions.add(columnsToSearch[i] + " LIKE \""
							+ valuesToSearch[i] + "\"");
				}
				else{
					conditions.add(columnsToSearch[i] + " = \""
							+ valuesToSearch[i] + "\"");
				}
				
			}
			
			list = getAllContentWhere(tableName, conditions.toArray());
			
		}
		
		return list;
		
	}
	
	/**
	 * <p>
	 * Method to add an item to the database.
	 * </p>
	 * <p>
	 * <b>IMPORTANT</b> : The number of columns must be the same of the number
	 * of values. Otherwise, the method will not even try to add the specified
	 * values.
	 * </p>
	 * 
	 * @param tableName
	 *            The name of the table of which the item is supposed to go
	 *            into.
	 * @param columnNames
	 *            The name of the columns of which the values will be added.
	 * @param values
	 *            The values which will be added to the columns specified.
	 * @return The id generated for the item created. If the
	 *         <i><b>important</b></i> notice haven't been followed or there is
	 *         an SQL error, the return value will be <code>-1</code>.
	 */
	public int addToTable(String tableName, String[] columnNames,
			Object[] values){
		
		int idCreated = -1;
		
		if(columnNames.length == values.length){
			
			String sqlRequest = "INSERT INTO ";
			
			sqlRequest += tableName;
			
			sqlRequest += "(";
			
			for(int i = 0; i < columnNames.length; i++){
				sqlRequest += columnNames[i];
				
				if(i != columnNames.length - 1)
					sqlRequest += ", ";
			}
			
			sqlRequest += ") VALUES (";
			
			for(int i = 0; i < values.length; i++){
				if(values[i] == null)
					sqlRequest += values[i];
				else
					sqlRequest += "\"" + values[i] + "\"";
				
				if(i != values.length - 1)
					sqlRequest += ", ";
			}
			
			sqlRequest += ")";
			
			try{
				
				objetRequetes.executeUpdate(sqlRequest,
						Statement.RETURN_GENERATED_KEYS);
				
				ResultSet rs = objetRequetes.getGeneratedKeys();
				
				if(rs.next()){
					idCreated = rs.getInt(1);
				}
				
				rs.close();
				
			}
			catch(SQLException e){}
			
		}
		
		return idCreated;
		
	}
	
	/**
	 * <p>
	 * Method which purpose is to modify an object inside this database.
	 * </p>
	 * <p>
	 * <b>IMPORTANT</b> : The values will be modified in the order of the
	 * columns, which means the <code>columnNames</code> and <code>values</code>
	 * ' content order is important.
	 * </p>
	 * 
	 * @param tableName
	 *            Name of the table which the object is located in.
	 * @param idColumnName
	 *            Name of the column where a condition will be applied in
	 *            conjunction with the parameter <code>id</code>.
	 * @param id
	 *            Value that will be applied in the condition <code>WHERE</code>
	 *            .
	 * @param columnNames
	 *            Columns that will be changed.
	 * @param values
	 *            Values of which will be applied.
	 * @return <code>true</code> if one or more rows have been modified
	 *         succesfully, <code>false</code> if there's an SQL error (ex.:
	 *         non-existant table name) or if no row have been modified.
	 */
	public boolean modifyObject(String tableName, String idColumnName,
			Object id, String[] columnNames, Object[] values){
		
		boolean success = false;
		
		String sqlRequest = "UPDATE " + tableName;
		
		sqlRequest += " SET ";
		
		for(int i = 0; i < columnNames.length; i++){
			
			sqlRequest += columnNames[i] + " = \"" + values[i] + "\"";
			
			if(i < columnNames.length - 1){
				sqlRequest += ", ";
			}
			
		}
		
		sqlRequest += " WHERE " + idColumnName + " = \"" + id + "\"";
		
		try{
			
			int numberOfRowsAffected = objetRequetes.executeUpdate(sqlRequest);
			
			if(numberOfRowsAffected != 0)
				success = true;
			
		}
		catch(SQLException e){}
		
		return success;
		
	}
	
	/**
	 * Method to execute a quick
	 * <code>DELETE FROM <i>tableName</i> WHERE <i>condition</i>;</code>
	 * 
	 * @param tableName
	 *            Name of the table of which the deletion will affect.
	 * @param condition
	 *            SQL condition that will be executed. <i>Ex. :
	 *            <code>idItem = 4</code></i>
	 * @return <code>true</code> if one or more rows have been deleted
	 *         succesfully, <code>false</code> if there's an SQL error (ex.:
	 *         non-existant table name) or if no row have been deleted.
	 */
	public boolean removeFromTable(String tableName, String condition){
		
		boolean success = false;
		
		String sqlRequest = "DELETE FROM " + tableName + " WHERE " + condition;
		
		try{
			
			int numberOfRowsAffected = objetRequetes.executeUpdate(sqlRequest);
			
			if(numberOfRowsAffected != 0)
				success = true;
			
		}
		catch(SQLException e){}
		
		return success;
		
	}
	
	/**
	 * Method that executes {@link #removeFromTable(String, String)} but with a
	 * pre-made condition using <code>idColumnName</code> and <code>id</code>.
	 * 
	 * @param tableName
	 *            Name of the table of which the deletion will affect.
	 * @param idColumnName
	 *            Name of the column which the condition will look upon, usually
	 *            the column with a unique ID.
	 * @param id
	 *            Value that will be applied in the condition.
	 * @return <code>true</code> if one or more rows have been deleted
	 *         succesfully, <code>false</code> if there's an SQL error (ex.:
	 *         non-existant table name) or if no row have been deleted.
	 */
	public boolean removeFromTable(String tableName, String idColumnName,
			Object id){
		
		String condition = idColumnName + " = \"" + id + "\"";
		
		return removeFromTable(tableName, condition);
		
	}
	
	/**
	 * Method that converts a ResultSet object into an ArrayList of objects.
	 * 
	 * @param queryResults
	 *            The <code>ResultSet</code> object to convert.
	 * @return An <code>ArrayList<Object[]></code> object, each index of the
	 *         <code>ArrayList</code> represents a row of the results and each
	 *         column of the <code>Object[]</code>'s array is a column of the
	 *         table in the database. <code>null</code> if there is an
	 *         <code>SQLException</code> error.
	 */
	private ArrayList<Object[]> convertResultSetToArraylistOfObjects(
			ResultSet queryResults){
		
		ArrayList<Object[]> table = null;
		
		if(queryResults != null){
			
			table = new ArrayList<>();
			
			try{
				
				int nCol = queryResults.getMetaData().getColumnCount();
				while(queryResults.next()){
					
					Object[] row = new Object[nCol];
					
					for(int iCol = 1; iCol <= nCol; iCol++){
						Object obj = queryResults.getObject(iCol);
						
						if(obj instanceof Boolean)
							obj = (boolean)obj ? 1 : 0;
						
						row[iCol - 1] = (obj == null) ? null : obj;
					}
					
					table.add(row);
					
				}
				
				queryResults.close();
				
			}
			catch(SQLException e){
				table = null;
			}
			
		}
		
		return table;
		
	}
	
	/**
	 * Closes the database.
	 */
	public void disconnect(){
		
		try{
			
			if(connection.isClosed())
				connection.close();
			
		}
		catch(SQLException e){
			// In theory, it should never go in here because this object does
			// not close if it already is, but the compiler doesn't care about
			// our feelings of handling ourselves the exceptions, kinda like
			// a mother. Compilers are mothers. Uh, interesting.
		}
		
	}
	
}
