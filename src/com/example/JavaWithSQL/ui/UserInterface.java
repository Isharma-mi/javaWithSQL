package com.example.JavaWithSQL.ui;

import java.util.Scanner;

public class UserInterface {
	
	final private Scanner scanner;
	
	public UserInterface() {
		scanner = new Scanner(System.in);
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
}
