import java.sql.ResultSet;
public class AaylaSecuraMain {
	
	static database db = new database();
	private static databaseReset dbReset = new databaseReset(db);
	private static UserManagement user;
	private static taskEntry myTE;
	private static LogTasks logged_tasks;
	private static UserInterface window;
	private static CreateReports reportCreation;

	public static void main(String[] args) {
		
		//clear and repopulate database
		dbReset.reset();
		//initialise classes		
		myTE = new taskEntry(db);
		logged_tasks = new LogTasks(db);
		user = new UserManagement(db);
		reportCreation = new CreateReports(db);
		//initialises window and sets the frame visible
		window = new UserInterface(db, user, logged_tasks, myTE);
		
		//testing
		try {
			//reportCreation.createTaskStatusReport();
			//reportCreation.createCaretakerReport();
			//reportCreation.createCompletedTaskReport();
		//catching exceptions to test other stuff
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
