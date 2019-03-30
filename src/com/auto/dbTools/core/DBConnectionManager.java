package com.auto.dbTools.core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.auto.util.PropertiesUtil;

public class DBConnectionManager {
	public static Connection getConnection() {
		try {
			Class.forName(PropertiesUtil.getInstance().get("driverClass")
					.toString());
			return DriverManager.getConnection(PropertiesUtil.getInstance()
					.get("url").toString(),
					PropertiesUtil.getInstance().get("user").toString(),
					PropertiesUtil.getInstance().get("password").toString());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void close(PreparedStatement pstmt, ResultSet res) {
		if (res != null)
			try {
				res.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		if (pstmt != null)
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
	}

	public static void closeConn(Connection conn, PreparedStatement pstmt,
			ResultSet res) {
		if (res != null)
			try {
				res.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		if (pstmt != null)
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		if (conn != null)
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

	}
}
