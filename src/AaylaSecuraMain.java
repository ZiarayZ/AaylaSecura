import java.sql.ResultSet;
import java.sql.SQLException;

public class AaylaSecuraMain {
	
	static database db = new database();
	private static databaseReset dbReset = new databaseReset(db);
	private static UserManagement user;
	private static LogTasks logged_tasks;

	public static void main(String[] args) {
		dbReset.reset();
		logged_tasks = new LogTasks(db);
		user = new UserManagement(db);
		try {
			ResultSet jobs = db.getAllJobs();
			System.out.println("Jobs:");
			while(jobs.next()) {
				System.out.println(jobs.getInt(1)+"| "+jobs.getString(2)+" "+jobs.getString(3));
			}
			//somehow set the main menu visible once login succeeds
			//this sets both the login menu and manage users menu visible at the same time
			user.displayLogin();
			user.displayUsers();
		//catching exceptions to test other stuff
		} catch (SQLException e) {
			System.out.println(e);
		} catch (NullPointerException e) {
			System.out.println(e);
		}
	}
	
	
}
