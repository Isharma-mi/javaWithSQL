package com.example.JavaWithSQL.logic;

import java.sql.Connection;

public class SQLInteractor {
	private final Connection connection;
	
	public SQLInteractor(Connection connection) {
		this.connection = connection;
	}
}
