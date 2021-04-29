import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AaylaSecuraMain {
	
	static database db = new database();
	private static UserManagement user = new UserManagement(db);
	private LogTasks logged_tasks = new LogTasks(db);
	private static databaseReset dbReset = new databaseReset();

	public static void main(String[] args) {
		dbReset.reset(db);
		try {
			ResultSet jobs = db.getAllJobs();
			ResultSet users = db.getAllUsers();
			while(jobs.next()) {
				System.out.println(jobs.getInt(1)+"|"+jobs.getString(2));
			}
			while(users.next()) {
				System.out.println(users.getInt(1)+"|"+users.getString(2)+" "+users.getString(3)+" "+users.getString(4)+" "+users.getString(5)+" "+users.getString(6)+" "+users.getString(7));
			}
			//testing login functions correctly
			System.out.println(user.login("Strong123", "temp123"));
			user.listAll();
		//catching exceptions to test other stuff
		} catch (SQLException e) {
			System.out.println(e);
		} catch (NullPointerException e) {
			System.out.println(e);
		}
	}
	
	
}
