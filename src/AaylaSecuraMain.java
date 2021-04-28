import java.sql.ResultSet;
import java.sql.SQLException;

public class AaylaSecuraMain {
	
	static database db;
	private UserManagement user;
	private LogTasks logged_tasks;

	public static void  main(String[] args) throws SQLException{
		db=new database();
		database.addNewJob("test job");
		ResultSet jobs = db.getAllJobs();
		while(jobs.next()) {
			System.out.println(jobs.getInt(1)+"|"+jobs.getString(2));
		}
		user = new UserManagement(db);
		logged_tasks = new LogTasks(db);
	}
	
	
}
