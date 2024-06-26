package com.example.JavaWithSQL.ui;

import java.util.Arrays;
import java.util.Scanner;
import com.example.JavaWithSQL.logic.SQLInteractor;

public class UserInterface {
	
	final private Scanner scanner;
	
	public UserInterface() {
		this.scanner = new Scanner(System.in);
	}
	
	/**
	 * Asks the user for the server name and database name they want to access.
	 * @return String that will be used in connecting to the database
	 */
	public String getSQLURL() {
		// Ask for server and database name
		System.out.println("What is the SQL server you are trying to access?");
		String server = this.scanner.nextLine().trim();
		System.out.println("What is the database in the server you are trying to access?");
		String db = this.scanner.nextLine().trim();
		
		// String that is used to connect w/server and db
		String url = "jdbc:sqlserver://" + server + ";databaseName=" + db + ";encrypt=true;trustServerCertificate=true";
		
		return url;
	}
	
	/**
	 * Asks the user for the SQL credentials that will be used to connect to the server 
	 * and database.
	 * @return String[] containing the username and password credentials
	 */
	public String[] getCredentials() {
		// Asking for credentials to access server and db
		System.out.println("What is the username?");
		String user = this.scanner.nextLine().trim();
		System.out.println("What is the password?");
		String pass = this.scanner.nextLine().trim();
		
		return new String[] {user, pass};
	}
	
	/**
	 * Asks the user what they want to do in the database of the server.
	 * Calls different methods depending on what user selects
	 * Supposed to be used only after connection to database has been established.
	 * @param sqlInteractor sends queries SQL queries from java
	 */
	public boolean askMenuOptions(SQLInteractor sqlInteractor) {
		boolean keepAsking = true;
		
		// Asks and gives users options on what to do
		System.out.println("\n------------------------What would you like to do?------------------------");
		System.out.println("1. View tables in database "
				+ "\n2. Create table in database"
				+ "\n3. Delete table from database"
				+ "\n4. Create record for a table"
				+ "\n5. Delete record from a table"
				+ "\n6. View records of a a table"
				+ "\n7. Exit program");
		
		// Gets option user wants
		String optionSelected = scanner.nextLine().trim();
		
		if (optionSelected.matches("[1234567]")) {
			// Verifies a valid option was given
			switch (optionSelected) {
				case "1":
					// See what tables are in the database
					sqlInteractor.viewTablesQuery();
					break;
				case "2":
					// Creates a table
					createTable(sqlInteractor);
					break;
				case "3":
					// Delete a table
					deleteTable(sqlInteractor);
					break;
				case "4":
					// Add a record to a table
					addRecordToTable(sqlInteractor);
					break;
				case "5":
					// Delete a record from a table
					deleteRecordFromTable(sqlInteractor);
					break;
				case "6":
					// Shows records of a table
					viewRecordsOfTable(sqlInteractor);
					break;
				case "7":
					// End Program
					keepAsking = false;
					System.out.println("Ending program");
					break;
				default: 
					System.out.println("ERROR: Please give valid option");
			}
		} else {
			System.out.println("ERROR: Invalid option given!");
		}
		return keepAsking;
	}
	
	/**
	 * Creates a table in sql based off user input in java.
	 * @param sqlInteractor executes sql queries from java
	 */
	private void createTable(SQLInteractor sqlInteractor) {
		// Get new table's name
		System.out.println("What is the table's name?");
		String tableName = this.scanner.nextLine().trim();

		if (sqlInteractor.checkTableExists(tableName)) {
			// If table already exists stop creation
			System.out.println("ERROR: Table already exists.");
			return;
		}

		// Get number of columns
		System.out.println("How many columns will there be?");
		String numColumnsInputted = this.scanner.nextLine();
		
		if (!numColumnsInputted.matches("[0123456789]+")) {
			// If invalid input is given
			System.out.println("ERROR: Please give valid number of columns.");
			return;
		}
		
		// Converts string input of number of columns to an int
		int numOfColumns = Integer.parseInt(numColumnsInputted);
		// Array that will store the information of the columns
		String[] columns = new String[numOfColumns];
		
		for (int i = 0; i < numOfColumns; i++) {
			// Asks user for each column's information one at a time
			System.out.println("What is the column's information? Start with IDENTITY column."
					+ " Format it similar to SQL (ex: column_name datatype)");
			columns[i] = scanner.nextLine().trim();
		}
		
		// Attempts to create the table in SQL
		sqlInteractor.createTable(tableName, columns);
	}

	/**
	 * Deletes a table based off user input in java.
	 * @param sqlInteractor executes sql queries from java
	 */
	private void deleteTable(SQLInteractor sqlInteractor) {
		// Gets name of table user wants to delete
		System.out.println("What table would you like to delete?");
		String tableName = this.scanner.nextLine().trim();
	
		if (sqlInteractor.checkTableExists(tableName)) {
			// If table exists then delete it
			sqlInteractor.deleteTable(tableName);
		} else {
			System.out.println("ERROR: Table already does NOT exist");
		}
	}
	
	/**
	 * Adds records to a table based off user input in java.
	 * @param sqlInteractor executes sql queries from java
	 */
	private void addRecordToTable(SQLInteractor sqlInteractor) {
		// Get name of table user wants to delete
		System.out.println("What is the table's name?");
		String tableName  = this.scanner.nextLine().trim();
		
		// Stops trying to add a record if table does not exist
		if (!sqlInteractor.checkTableExists(tableName)) {
			// If table does NOT exist stop trying to add a record
			System.out.println("ERROR: Table does not exist.");
			return;
		}
		
		// Stores column names
		String[] columns = sqlInteractor.getColumnsOfTable(tableName);
		// Stores the values user wants to add for record
		String[] columnValues = new String[columns.length];
		
		// Gets the values user wants to add for each column of the record
		for (int i = 0; i < columns.length; i++) {
			System.out.println("For column: " + columns[i] + ", what do you want to add?");
			// Gets the value
			String columnValue = scanner.nextLine().trim();
			// Stores the value
			columnValues[i] = columnValue;
		}
		// Tries to add record to table
		sqlInteractor.addRecordToTable(tableName, columns, columnValues);
	}
	
	/**
	 * Deletes a record based off user input in java.
	 * @param sqlInteractor executes sql queries from java
	 */
	private void deleteRecordFromTable(SQLInteractor sqlInteractor) {
		// Get name of table user wants to delete record from
		System.out.println("What is the table the record is located in?");
		String tableName = this.scanner.nextLine();
		
		if (!sqlInteractor.checkTableExists(tableName)) {
			// If table does NOT exist trying to delete a record
			System.out.println("ERROR: Table does NOT exist!");
			return;
		}
		
		// Gets the columns and prints them out for user
		String[] columns = sqlInteractor.getColumnsOfTable(tableName);
		System.out.println("From which column do you want to search the record for?"
				+ "\nThese are the columns: " + Arrays.toString(columns));
		
		// Gets the column user wants to search by
		String criteria = scanner.nextLine().trim();
		boolean criteriaExists = false;
		// Checks that user gave a valid column
		for (int i = 0; i < columns.length; i++) {
			if (columns[i].equals(criteria)) {
				criteriaExists = true;
				break;
			}
		}
		
		if (!criteriaExists) {
			// If user gave an invalid column stop trying to delete a record
			System.out.println("ERROR: Column to search by does NOT exist!");
		} else {
			// If user gave a valid column try to delete a record
			System.out.println("What is the value you want to search for? NOTE: Multiple records with same value in column can be removed.");
			String valueOfColumn = scanner.nextLine().trim();
			sqlInteractor.deleteRecord(tableName, criteria, valueOfColumn);
		}
	}

	/**
	 * Views all records of a table given by user in java.
	 * @param sqlInteractor executes sql queries from java
	 */
	private void viewRecordsOfTable(SQLInteractor sqlInteractor) {
		// Gets the table name
		System.out.println("What is the table's name that you want to view the records of?");
		String tableName = scanner.nextLine();
		
		// Stop trying to view records if table does not exist
		if (!sqlInteractor.checkTableExists(tableName)) {
			System.out.println("ERROR: Table does NOT exist.");
			return;
		}
		sqlInteractor.viewRecords(tableName);
	}
}