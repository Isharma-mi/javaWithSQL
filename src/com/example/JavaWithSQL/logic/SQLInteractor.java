package com.example.JavaWithSQL.logic;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLInteractor {
	private final Connection connection;
	private PreparedStatement pstmnt;
	private ResultSet rs;
	
	public SQLInteractor(Connection connection) {
		this.connection = connection;
	}
	
	/**
	 * Prints out the names of the tables located in the database.
	 */
	public void viewTablesQuery() {
		try {
			// SQL query to return table w/header "TABLE_NAME" and name of each table in DB
			this.pstmnt = this.connection.prepareStatement("SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES;");
			// Executes query and stores result of it
			this.rs = this.pstmnt.executeQuery();
			
			System.out.println("Table names:");
			// Loops thru row of the table data 
			while (this.rs.next()) {
				// Prints out each tables name
				System.out.println("\t" + rs.getString("TABLE_NAME"));
			}
		} catch (SQLException e) {
			System.out.println("ERROR: Unable to view tables");
			e.printStackTrace();
		}
	}
	
	/**
	 * Creates a table in SQL when it does not exist already.
	 * @param tableName used to set the name of the table in SQL
	 * @param columns contains Strings that each have the column_name and 
	 * datatype associated with it
	 */
	public void createTable(String tableName, String[] columns) {
		// Create obj that will store query to create table
		StringBuilder createTableQuery = new StringBuilder();
		
		createTableQuery.append("CREATE TABLE ");
		// Add name of table to query
		createTableQuery.append(tableName + " (");
		// Add each column to query
		for (int i = 0; i < columns.length; i++) {
			createTableQuery.append(columns[i] + ", ");
		}
		// Add ending to query
		createTableQuery.append(");");
			
		try {
			// Executes query in SQL
			this.pstmnt = this.connection.prepareStatement(createTableQuery.toString());
			this.pstmnt.execute();
			System.out.println(tableName + " table was created successfully");
		} catch (SQLException e) {
			System.out.println("ERROR: Unable to create table!");
			e.printStackTrace();
		}
	}
	
	/**
	 * Deletes a table specified by the user from the SQL server's database.
	 * @param tableName used to find table to delete
	 */
	public void deleteTable(String tableName) {
		// Create obj that will store query to delete table
		StringBuilder deleteTableQuery = new StringBuilder();
		// Adds table's name to query
		deleteTableQuery.append("DROP TABLE " + tableName);
		
		try {
			// Executes query in SQL
			this.pstmnt = this.connection.prepareStatement(deleteTableQuery.toString());
			this.pstmnt.execute();
			System.out.println(tableName + " table was deleted succesfully!");
		} catch (SQLException e) {
			System.out.println("ERROR: Unable to delete table!");
			e.printStackTrace();
		}
	} 
	
	
	/**
	 * Adds a record to an existing table in SQL server's database.
	 * @param tableName used to find table to add record to
	 * @param columns used to tell user what columns values will need to be added to
	 * @param columnValues used to populate the columns
	 */
	public void addRecordToTable(String tableName, String[] columns, String[] columnValues) {
		// Create obj that will store query to create table
		StringBuilder addRecordQuery =  new StringBuilder();

		// Specifies which table we want to insert into
		addRecordQuery.append("INSERT INTO "); 
		addRecordQuery.append(tableName);
		addRecordQuery.append(" (");
		
		// Adds column names to query
		for (int i = 0; i < columns.length; i++) {
			addRecordQuery.append(columns[i]);
			
			// Checks if there are more elements to add
			if (!(i == columns.length-1)) {
				addRecordQuery.append(",");
			}
		}
		
		// Adds values to query
		addRecordQuery.append(") VALUES (");
		for (int i = 0; i < columnValues.length; i++) {
			// Single quotes work with all data types in SQL
			addRecordQuery.append("'");
			addRecordQuery.append(columnValues[i]);
			addRecordQuery.append("'");
			
			// Checks if there are more elements to add
			if (!(i == columnValues.length - 1)) {
				addRecordQuery.append(",");
			}
		}
		addRecordQuery.append(")");
		
		try {
			//Executes query in SQL
			this.pstmnt = this.connection.prepareStatement(addRecordQuery.toString());
			this.pstmnt.execute();
			System.out.println("Record was added to " + tableName + " successfully!");
		} catch (SQLException e) {
			System.out.println("ERROR: Unable to add record with given information!");
			e.printStackTrace();
		}
	}
	
	/**
	 * Deletes a record from an existing table in SQL server's database.
	 * @param tableName used to find table to delete record from
	 * @param column used to see what column to search record from
	 * @param valueOfColumn used to find the record(s)
	 */
	public void deleteRecord(String tableName, String column, String valueOfColumn) {
		// Creates obj that will store query to verify value exists
		// Checks that value exists (done so we can let user know if record could NOT be found)
		StringBuilder checkValueExistsQuery = new StringBuilder();
		checkValueExistsQuery.append("SELECT * FROM ");
			checkValueExistsQuery.append(tableName);
		checkValueExistsQuery.append(" WHERE ");
			checkValueExistsQuery.append(column);
			checkValueExistsQuery.append(" = '");
			checkValueExistsQuery.append(valueOfColumn);
			checkValueExistsQuery.append("';");

		// Creates obj that will store query to delete a record
		StringBuilder deleteRecordQuery = new StringBuilder();
		deleteRecordQuery.append("DELETE FROM ");
			deleteRecordQuery.append(tableName);
		deleteRecordQuery.append(" WHERE ");
			deleteRecordQuery.append(column);
			deleteRecordQuery.append(" = '");
			deleteRecordQuery.append(valueOfColumn);
			deleteRecordQuery.append("';");
		
		try {
			// Executes value check query in SQL (Insensitive allows for us to move cursor) and stores query's output
			this.pstmnt = this.connection.prepareStatement(checkValueExistsQuery.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			this.rs = this.pstmnt.executeQuery();
			if (!this.rs.isBeforeFirst()) {
				// If ResultSet is empty (value user wants to delete could not be found)
				System.out.println("ERROR: Unable to find record(s)");
			} else {
				// If ResultSet is not empty (value user wants to delete was found)
				this.pstmnt = this.connection.prepareStatement(deleteRecordQuery.toString());
				// Executes value check query in SQL
				this.pstmnt.execute();
				System.out.println("Record(s) were deleted successfully!");
			}
		} catch (SQLException e) {
			System.out.println("ERROR: Unable to delete record(s)");
			e.printStackTrace();
		}
	}

	
	/**
	 * Finds the column names of a given table.
	 * Separate method since need to ask user about each column individually they will add values to
	 * @param tableName specifies from which table user wants the columns
	 * @return array containing the table's columns
	 */
	public String[] getColumnsOfTable(String tableName) {
		String[] columns = null;
		// Create obj that will store query to get column all the column names EXCEPT identity for specified table
		StringBuilder getColumnsQuery = new StringBuilder();
		
		getColumnsQuery.append("SELECT c.name FROM sys.columns AS C"
				+ "\n JOIN sys.tables AS t"
				+ "\n ON t.object_id = c.object_id"
				+ "\n WHERE c.name NOT IN(SELECT name FROM sys.identity_columns WHERE is_identity=1)"
				+ "\n AND t.name = '");
			getColumnsQuery.append(tableName);
			getColumnsQuery.append("';");
			
		try {
			// Executes query in SQL (Insensitive allows for us to move cursor) and stores query's output
			this.pstmnt = this.connection.prepareStatement(getColumnsQuery.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			this.rs = this.pstmnt.executeQuery();

			// Puts cursor at last row of result set 
			this.rs.last();
			// Gets the id of last row -> Can be used to check number of columns in table
			int numColumns= this.rs.getRow();
			// Creates array to store column names
			columns = new String[numColumns];
			
			// Puts cursor right before the first row 
			// Done since .next() will push cursor to first row at beginning of loop 
			this.rs.beforeFirst();

			// Used to store column names in order
			int tracker = 0;
			// Loop through each column's name
			while (rs.next()) {
				// Adds column name to array
				columns[tracker++] = this.rs.getString(1);
			}
		} catch (SQLException e) {
			System.out.println("ERROR: Unable to get column headers information from table");
			e.printStackTrace();
		}
		return columns;
	}
	
	/**
	 * Checks if a given table name is already in the SQL server's database.
	 * @param tableName table whose existence is checked for
	 * @return boolean letting user know if table exists
	 */
	public boolean checkTableExists(String tableName) {
		boolean tableExists = false;
		
		try {
			// Used to get information about database
			DatabaseMetaData md = connection.getMetaData();
			
			// Used to check if the table's name can be found in the database (ResultSet will have values if table was found)
			this.rs = md.getTables(null, null, tableName, null);

			if (this.rs.next()) {
				// If the ResultSet is able to move its cursor (i.e. there are values in it)
				tableExists = true;
			}
		} catch (SQLException e) {
			System.out.println("ERROR: Unable to check if table exists.");
			e.printStackTrace();
		}
		return tableExists;
	}

	
	
}
