package com.example.JavaWithSQL.logic;

import com.example.JavaWithSQL.ui.UserInterface;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;

public class Main {
	// TODO: Implement adding records from Java into SQL
	// TODO: Implement removing records from Java into SQL
	public static void main(String[] args) {
		// TESTING BEGINS HERE
		TestingClass tester = new TestingClass();
		tester.testingStuff();
		// TESTING ENDS HERE
		
		// Create UI obj for user to interact with
		final UserInterface ui = new UserInterface();
		// Create SQLInteractor obj here so we can use it after try block
		SQLInteractor sqlInteractor = null;
		
		// Try to access server and database w/specific credentials
		try {
			String url = ui.getSQLURL();
			String[] userAndPass = ui.getCredentials();
			
			// Establishes connection to database
			Connection connection = DriverManager.getConnection(url, userAndPass[0], userAndPass[1]);
			// Setup SQLInteractor obj so we can send commands to SQL from Java
			sqlInteractor = new SQLInteractor(connection);
		} catch(SQLException e) {
			System.out.println("ERROR: Unable to connect to server or database");
			e.printStackTrace();
		}
		
		// For repeatedly asking user what they want to do
		while (true) {
			// Can pass in sqlInteractor safely since it can not be null at this pt
			ui.menuOptions(sqlInteractor);
		}
	
		

	}
}
