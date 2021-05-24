public class AaylaSecuraMain {
	
	static database db = new database();
	private static databaseReset dbReset = new databaseReset(db);//for testing
	private static UserManagement user;
	private static taskEntry myTE;
	private static taskAssign myTA;
	private static LogTasks logged_tasks;
	private static UserInterface window;

	public static void main(String[] args) {
		//clear and repopulate database
		//dbReset.reset();//resetting database to factory settings for testing
		//initialise classes
		myTE = new taskEntry(db);
		myTA = new taskAssign(db);
		logged_tasks = new LogTasks(db);
		user = new UserManagement(db);
		//initialises window and sets the frame visible
		window = new UserInterface(db, user, logged_tasks, myTE, myTA);
        window.displayLogin();
        window.setVisible(true);
	}
}
