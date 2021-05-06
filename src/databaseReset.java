import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;

public class databaseReset {
	private database db;
	public databaseReset(database db) {
		this.db=db;
	};
	
	
	public void reset() {
		try {
			
			db.clearAllDatabase();
			//job
			database.addNewJob("Caretaker");//code for caretaker 1
			database.addNewJob("Manager");//code for manager 2
			database.addNewJob("Admin");//code for admin 3
			//user
			//name, username, job_id, hashed_password, notes, M_F(gender)
			try {
				db.addNewUser("Jeremy Strong", "Strong123", 3, UserManagement.genPassHash("temp123".toCharArray()), "", "M");
				db.addNewUser("Ed" , "Ed123", 2, UserManagement.genPassHash("hash password".toCharArray()), "", "M");
			} catch (NoSuchAlgorithmException e) {
				System.out.println(e);
			} catch (InvalidKeySpecException e) {
				System.out.println(e);
			}

			//task - [type 1 is one off, type 0 is recurring, duration is in minutes,frequency is in days, datetime in format yyyy-mm-dd hh:mm:ss,]
			db.addNewTask("Fix front door hinge room 7", 1, 15, 3, 0, 1, "2021-02-21 12:07:26", 0, 0);
			db.addNewTask("take out bins on floor 1", 0, 20, 1, 7, 1, "2020-05-14 09:24:50", 0, 0);
			db.addNewTask("replace fire alarm in room 26", 1, 10, 2, 0, 1, "2021-04-27 17:56:31", 0, 2);
			//logged task
			
		} catch (SQLException e) {
			System.out.println(e);
		}
		
	}
	
}
