package com.cdeledu.thread3.c10classloader.threadclassload;

import java.sql.Connection;
import java.sql.SQLException;

public class Test {

	//我们先来看平时是如何使用mysql获取数据库连接的：
	public static void main(String[] args) throws SQLException {
		// 加载Class到AppClassLoader（系统类加载器），然后注册驱动类
		// Class.forName("com.mysql.jdbc.Driver").newInstance(); 
		String url = "jdbc:mysql://localhost:3306/testdb";    
		// 通过java库获取数据库连接
		Connection conn = java.sql.DriverManager.getConnection(url, "name", "password"); 
	}

}
