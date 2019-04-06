package com.shopnail.api.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionDB {
	private final String className = "com.mysql.jdbc.Driver";// Dùng cho Linux
	private Connection connection;
	private String url = "jdbc:mysql://localhost:3306/";// URL kết nối
	private String dbname = "nail";// Database name
	private String user = "root";// Tên User MySQL Server
	private String pw = "password";// Mật khẩu User MySQL Server

	public Connection connect() {
		try {
			Class.forName(className);
			connection = DriverManager.getConnection(url + dbname, user, pw);
			System.out.println("Kết nối Database thành công");
		} catch (ClassNotFoundException ex) {
			System.out.println("Kết nối Database thất bại 1: ClassNotFoundException" );
		} catch (SQLException ex) {
			System.out.println("Kết nối Database thất bại 2: SQLException");
		}
		return connection;
	}
}
