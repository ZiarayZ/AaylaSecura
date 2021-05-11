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
			ResultSet users = db.getAllUsers();
			System.out.println("Jobs:");
			while(jobs.next()) {
				System.out.println(jobs.getInt(1)+"| "+jobs.getString(2)+" "+jobs.getString(3));
			}
			System.out.println("Users:");
			while(users.next()) {
				System.out.println(users.getInt(1)+"| "+users.getString(2)+" "+users.getString(3)+" "+users.getString(4)+" "+users.getString(5));
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
