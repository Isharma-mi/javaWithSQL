package com.example.JavaWithSQL.logic;

import com.example.JavaWithSQL.ui.UserInterface;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;

public class Main {
	// TODO: Implement table creation from Java into SQL
	// TODO: Implement adding records from Java into SQL
	// TODO: Implement removing records from Java into SQL
	// TODO: Implement viewing of tables from SQL into Java
	public static void main(String[] args) {
		final UserInterface ui = new UserInterface();
		
		try {
			// Gets URL based off server and db from user
			String url = ui.getSQLURL();
			// Gets credentials from user
			String[] userAndPass = ui.getCredentials();
			
			// Establishes connection to database
			Connection connection = DriverManager.getConnection(url, userAndPass[0], userAndPass[1]);	
		} catch(SQLException e) {
			System.out.println("ERROR: Unable to connect to server or database");
			e.printStackTrace();
		}
	}
}
