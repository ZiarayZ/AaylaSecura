import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.IOException;
public class AaylaSecuraMain {
	
	static database db = new database();
	private static databaseReset dbReset = new databaseReset(db);
	private static UserManagement user;
	private static LogTasks logged_tasks;
	private static UserInterface window;
	//private static CreateReports reportCreation;

	public static void main(String[] args) {
		//clear and repopulate database
		dbReset.reset();
		//initialise classes
		logged_tasks = new LogTasks(db);
		user = new UserManagement(db);
		//reportCreation = new CreateReports(db);
		window = new UserInterface(db, user, logged_tasks);
		//set window frame visible
		window.setVisible(true);

		//testing
		/*try {
			reportCreation.createTaskStatusReport();
			reportCreation.createCaretakerReport();
			reportCreation.createCompletedTaskReport();
		//catching exceptions to test other stuff
		} catch (IOException e) {
			System.out.println(e);
		}*/
	}
}
