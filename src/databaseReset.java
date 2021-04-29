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
			//String hashed_password = UserManagement.genPassHash("temp123"); //gives a long hashed_password like the one below
			//name, username, job_id, hashed_password, notes, M_F(gender)
			db.addNewUser("Jeremy Strong", "Strong123", 3, "1000:2fd7b8d6c5b20733f2ec2a056f1d54d9:47eb672a2d95c5b559ebee1891df982c2b1ba3f24f8a172859297f975f66170f5392b5da8a77fbc6a78dd454c8ca007bc9499cf21266b926e01e149efcc75db5", "", "M");
			db.addNewUser("Ed" , "Ed123", 2, "password hash", "", "M");
			
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
