import java.sql.SQLException;

public class databaseReset {

	public databaseReset() {};
	
	
	public void reset(database db) {
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
			
			//task - [type 1 is one off, type 0 is recurring, duration is in minutes, datetime in format yyyy-mm-dd hh:mm:ss,]
			db.addNewTask("Fix front door hinge room 7", 1, 15, 3, 0, 1, "2021-02-21 12:07:26", 0, 0);
			
			//logged task
			
		} catch (SQLException e) {
			System.out.println(e);
		}
		
	}
	
}
