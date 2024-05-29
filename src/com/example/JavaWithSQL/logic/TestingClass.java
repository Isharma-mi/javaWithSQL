package com.example.JavaWithSQL.logic;

import java.sql.Connection;
import java.sql.DriverManager;

public class TestingClass {
	public void testingStuff() {
		Connection connection;
		SQLInteractor sqlInteractor = null;
		// Insert URl here
		String url = "jdbc:sqlserver://ISHARMA;databaseName=ishaan;encrypt=true;trustServerCertificate=true";

		// Insert credentials here
		String user = "isharma4";
		String pass = "test";

		try {
			connection = DriverManager.getConnection(url, user, pass);
			sqlInteractor = new SQLInteractor(connection);
		} catch(Exception e) {
			System.out.println("TESTING ERROR: Unable to make test connection");
		}
		sqlInteractor.viewRecords("star_wars");
		
	}
}
