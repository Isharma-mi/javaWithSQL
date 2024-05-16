package com.example.JavaWithSQL.logic;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.ResultSetMetaData;

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
	public void createTableQuery(String tableName, String[] columns) {
		// Create obj that will store query to create table
		StringBuilder cmd = new StringBuilder();
		// Add CREATE to query
		cmd.append("CREATE TABLE ");
			// Add name of table to query
			cmd.append(tableName + " (");
			// Add each column to query
			for (int i = 0; i < columns.length; i++) {
				cmd.append(columns[i] + ", ");
			}
			// Add ending to query
			cmd.append(");");
			
		try {
			this.pstmnt = this.connection.prepareStatement(cmd.toString());
			this.pstmnt.execute();
		} catch (SQLException e) {
			System.out.println("ERROR: Unable to create table!");
			e.printStackTrace();
		}
	}
	
	/**
	 * Adds a record to an existing table in SQL server's database.
	 */
	public void addRecordToTable(String tableName, String[] columns, String[] columnValues) {
		StringBuilder cmd =  new StringBuilder();

		// Specifies which table we want to insert into
		cmd.append("INSERT INTO "); 
		cmd.append(tableName);
		cmd.append(" (");
		
		// Adds column names to cmd
		for (int i = 0; i < columns.length; i++) {
			cmd.append(columns[i]);
			
			// Checks that there are more elements to add
			if (!(i == columns.length-1)) {
				cmd.append(",");
			}
		}
		// Adds values to cmd
		cmd.append(") VALUES (");
		for (int i = 0; i < columnValues.length; i++) {
			// Used mainly for String values
			// Works with ints as well (do it for all types to reduce code w/o negative impact)
			cmd.append("'");
			cmd.append(columnValues[i]);
			cmd.append("'");
			
			// Checks that there are more elements to add
			if (!(i == columnValues.length - 1)) {
				cmd.append(",");
			}
		}
		cmd.append(")");
		
		try {
			this.pstmnt = this.connection.prepareStatement(cmd.toString());
			this.pstmnt.execute();
		} catch (SQLException e) {
			System.out.println("ERROR: Unable to add record with given information!");
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
		StringBuilder cmd = new StringBuilder();
		
		// SQL query that gets all the column names EXCEPT identity for specified table
		cmd.append("SELECT c.name FROM sys.columns AS C"
				+ "\n JOIN sys.tables AS t"
				+ "\n ON t.object_id = c.object_id"
				+ "\n WHERE c.name NOT IN(SELECT name FROM sys.identity_columns WHERE is_identity=1)"
				+ "\n AND t.name = '");
			cmd.append(tableName);
			cmd.append("';");
			
		try {
			// Loads query from above into PreparedStatement (Insensitive allows for us to move cursor)
			this.pstmnt = this.connection.prepareStatement(cmd.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
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

			// Used to store column names
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
	 * @return
	 */
	public boolean checkTableExists(String tableName) {
		boolean tableExists = false;
		try {
			// Used to get information about database
			DatabaseMetaData md = connection.getMetaData();
			// Checks if the table's name can be found in the database
			this.rs = md.getTables(null, null, tableName, null);

			if (this.rs.next()) {
				// Checks if the ResultSet has any values in it (will have one if table's name was found)
				tableExists = true;
			}
		} catch (SQLException e) {
			System.out.println("ERROR: Unable to check if table exists.");
			e.printStackTrace();
		}
		
		return tableExists;
	}

	
	
}
