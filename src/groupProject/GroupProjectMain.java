package groupProject;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GroupProjectMain {
	
	static database db;


	public static void  main(String[] args) throws SQLException{
		db=new database();
		database.addNewJob("test job");
		ResultSet jobs = db.getAllJobs();
		while(jobs.next()) {
			System.out.println(jobs.getInt(1)+"|"+jobs.getString(2));
		}
		
	}
	
	
}
