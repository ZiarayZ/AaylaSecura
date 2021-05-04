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
				System.out.println(jobs.getInt(1)+"| "+jobs.getString(2));
			}
			System.out.println("Users:");
			while(users.next()) {
				System.out.println(users.getInt(1)+"| "+users.getString(2)+" "+users.getString(3)+" "+users.getString(4)+" "+users.getString(5)+" "+users.getString(6)+" "+users.getString(7));
			}
		//catching exceptions to test other stuff
		} catch (SQLException e) {
			System.out.println(e);
		} catch (NullPointerException e) {
			System.out.println(e);
		}
	}
	
	
}
