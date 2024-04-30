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
					break;
				case "3":
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
}