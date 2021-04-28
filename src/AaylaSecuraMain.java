import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AaylaSecuraMain {
	
	static database db = new database();
	private UserManagement user = new UserManagement(db);
	private LogTasks logged_tasks = new LogTasks(db);

	public static void  main(String[] args) {
		try {
			database.addNewJob("test job");
			ResultSet jobs = db.getAllJobs();
			while(jobs.next()) {
				System.out.println(jobs.getInt(1)+"|"+jobs.getString(2));
			}
		//catching exceptions to test other stuff
		} catch (SQLException e) {
			System.out.println(e);
		} catch (NullPointerException e) {
			System.out.println(e);
		}
	}
	
	
}
