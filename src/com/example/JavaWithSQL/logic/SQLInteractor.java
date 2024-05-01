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
			System.out.println("ERROR: Unable to create table");
			e.printStackTrace();
		}
	}
	
	/**
	 * Adds a record to an existing table in SQL server's database.
	 */
	public void addRecordToTable(String tableName) {
		// TODO: Get table name and verify if it exists in UI
		// TODO: Get the column information
		// TODO: Get user to input info for each column

		StringBuilder cmd = new StringBuilder();
		
		cmd.append("SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = '");
			cmd.append(tableName);
			cmd.append("';");
		try {
			// Gets each column's name as sep rows under the column header COLUMN_NAME
			this.pstmnt = this.connection.prepareStatement(cmd.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			this.rs = this.pstmnt.executeQuery();

			// Used to get the total number of rows by pointing to the last row 
			this.rs.last();
			
			// Puts cursor right before the first row 
			// Done since .next() will push cursor to first row at beginning of loop 
			this.rs.beforeFirst();

			// Loop through each column's name ()
			while (rs.next()) {
				// Prints out the column's name
				System.out.println(this.rs.getString(1));
			}
		} catch (SQLException e) {
			System.out.println("ERROR: Unable to get column headers information from table");
			e.printStackTrace();
		}
		

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
