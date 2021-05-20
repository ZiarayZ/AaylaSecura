import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;

public class databaseReset {
	private database db;
	public databaseReset(database db) {
		this.db=db;
	}
	
	
	public void reset() {
		try {
			
			db.clearAllDatabase();
			//job
			database.addNewJob("Caretaker");//code for caretaker 1
			db.editJobPerms(1, "{Rank:3,MU:1}");
			database.addNewJob("Manager");//code for manager 2
			db.editJobPerms(2, "{Rank:2,MU:2}");
			database.addNewJob("Admin");//code for admin 3
			db.editJobPerms(3, "{Rank:1,AP:1,MU:3}");

			//perms - definitions
			db.addNewPerms("Rank", "Overall Access Level");//closer to 1 the higher their "rank", 0 is no rank at all
			db.addNewPerms("AP", "Administrator Permissions");//0 admin perms or 1 all admin perms
			db.addNewPerms("MU", "Manage Users");//0 no perms, higher number, higher perms

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
			System.out.println(db.addNewTask("replace fire alarm in room 26", 1, 10, 2, 0, 1, "2021-04-27 17:56:31", 0, 2));
			//logged task
			
		} catch (SQLException e) {
			System.out.println(e);
		}
		
	}
	
}
