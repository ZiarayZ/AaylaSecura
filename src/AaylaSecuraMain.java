import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.IOException;
public class AaylaSecuraMain {
	
	static database db = new database();
	//private static databaseReset dbReset = new databaseReset(db);
	private static UserManagement user;
	private static taskEntry myTE = new taskEntry(db);
	private static LogTasks logged_tasks;
	private static UserInterface window;
	private static CreateReports reportCreation;

	public static void main(String[] args) {
		//clear and repopulate database
		//dbReset.reset();
		//initialise classes
		logged_tasks = new LogTasks(db);
		user = new UserManagement(db);
		reportCreation = new CreateReports(db);
		window = new UserInterface(db, user, logged_tasks);
		//set window frame visible
		window.setVisible(true);

		
		//testing
		try {
			//System.out.println(db.addNewTask("Fix front door hinge room 7", 1, 15, 3, 0, 1, "2021-02-21 12:07:26", 0, 0));
			System.out.println(myTE.addTask("room 7", 1, 15, 3, 0, 1, "2021-02-21 12:07:26", 0, 0));
			//reportCreation.createTaskStatusReport();
			//reportCreation.createCaretakerReport();
			//reportCreation.createCompletedTaskReport();
		//catching exceptions to test other stuff
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
