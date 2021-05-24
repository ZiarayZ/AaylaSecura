public class AaylaSecuraMain {
	
	static database db = new database();
	private static UserManagement user;
	private static taskEntry myTE;
	private static taskAssign myTA;
	private static LogTasks logged_tasks;
	private static UserInterface window;

	public static void main(String[] args) {
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
