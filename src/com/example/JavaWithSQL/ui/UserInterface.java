package com.example.JavaWithSQL.ui;

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
		// Ask for server and database until authentication done
		System.out.println("What is the SQL server you are trying to access?");
		String server = this.scanner.nextLine().trim();
		System.out.println("What is the database in the server you are trying to access?");
		String db = this.scanner.nextLine().trim();
		
		// URL used to connect w/server and db
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
	public void menuOptions(SQLInteractor sqlInteractor) {
		// Asks and gives users options on what to do
		System.out.println("\n------------------------What would you like to do?------------------------");
		System.out.println("1. View tables in database "
				+ "\n2. Create table in database"
				+ "\n3. Create record for a table"
				+ "\n4. Delete record from a table");
		
		// Gets option user wants
		String optionSelected = scanner.nextLine().trim();
		
		if (optionSelected.matches("[1234]")) {
			// Verifies a valid option was given
			switch (optionSelected) {
				case "1":
					// See what tables are in the database
					sqlInteractor.viewTablesQuery();
					break;
				case "2":					
					createTable(sqlInteractor);
					break;
				case "3":
					addRecordToTable(sqlInteractor);
					break;
				case "4":
					break;
				default: 
					System.out.println("ERROR: Please give valid option");
			}
		} else {
			System.out.println("ERROR: Invalid option given!");
		}
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
			// Return to menu if table already exists
			System.out.println("ERROR: Table already exists.");
			return;
		}

		// Get number of columns
		System.out.println("How many columns will there be?");
		String numColumnsInputted = this.scanner.nextLine();
		
		if (!numColumnsInputted.matches("[0123456789]+")) {
			// Return to menu if invalid num of columns given
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
		sqlInteractor.createTableQuery(tableName, columns);
	}

	/**
	 * Adds records to a table based off user input in java
	 * @param sqlInteractor executes sql queries from java
	 */
	private void addRecordToTable(SQLInteractor sqlInteractor) {
		// Adds a record to a specific table
		System.out.println("What is the table's name?");
		String tableToAddTo = this.scanner.nextLine().trim();
		
		// Stops trying to add a record if table does not exist
		if (!sqlInteractor.checkTableExists(tableToAddTo)) {
			System.out.println("ERROR: Table does not exist.");
			return;
		}
		String[] columns = sqlInteractor.getColumnsOfTable(tableToAddTo);
	}
}