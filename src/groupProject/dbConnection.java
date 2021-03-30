package groupProject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

public class dbConnection {
	private Connection conn;
	
	public dbConnection() {
		conn=null;
	}
	
	public boolean Connect(String filename) {
		try {
			String url="jdbc:sqlite:"+filename;
			conn = DriverManager.getConnection(url);
			System.out.println("Connection to "+filename+" has been established.");
		}
		catch(SQLException e) {
			System.out.println("Failed to connect to "+filename);
			System.out.println(e.getMessage());
			return false;
		}
				
		return true;
	}//end Connect

	public boolean RunSQL(String sql) {
		if(conn==null) {
			System.out.println("There is no datase loaded. Cannot run SQL.");
			return false;
		}
		try {
			Statement sqlStatement = conn.createStatement();
			sqlStatement.execute(sql);
		}
		catch(SQLException e) {
			System.out.println("Failed to execute "+sql);
			System.out.println(e.getMessage());
			return false;
		}
		return true;
	}//end RUNSQL

	public ResultSet RunSQLQuery(String sql) {
		ResultSet result;
		if(conn==null) {
			System.out.println("There is no datase loaded. Cannot run SQL.");
			return null;
		}
		try {
			Statement sqlStatement = conn.createStatement();
			result = sqlStatement.executeQuery(sql);
		}
		catch(SQLException e) {
			System.out.println("Failed to execute "+sql);
			System.out.println(e.getMessage());
			return null;
		}
		return result;
	}//end RunSQLQuery
	
}//end class dbConnection

