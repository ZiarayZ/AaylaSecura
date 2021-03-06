import java.sql.SQLException;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;


public class database {
	private static dbConnection myDB;
	
	public database() {
		myDB = new dbConnection();
		myDB.Connect("src\\groupDatabase.db");
	}
	
	private boolean checkValidDateTime(final String dateTime) {//returns true if given date time is valid
        boolean valid = false;
        try {
            LocalDate.parse(dateTime, DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss").withResolverStyle(ResolverStyle.STRICT));
            valid = true;
        } catch (DateTimeParseException e) {
        	System.out.println(e);
            valid = false;
        }
        return valid;
    }
	
	private boolean checkValidBoolean(final int input) { //returns true if given int is a valid boolean
		boolean valid = false;
		if(input<0||input>1) {valid=false;}
		else {valid=true;}
		return valid;
	}
	
	public boolean clearAllDatabase() {
		
		String deleteTaskSQL = "DELETE FROM tasks";
		String deleteJobSQL = "DELETE FROM job";
		String deleteUserSQL = "DELETE FROM users";
		String deleteloggedSQL = "DELETE FROM logged_tasks";
		String deletePermsSQL = "DELETE FROM perms";
		
		boolean result1=myDB.RunSQL(deleteloggedSQL);
		boolean result2=myDB.RunSQL(deleteTaskSQL);
		boolean result3=myDB.RunSQL(deleteUserSQL);
		boolean result4=myDB.RunSQL(deleteJobSQL);
		boolean result5=myDB.RunSQL(deletePermsSQL);
		
		
		if(result1&&result2&&result3&&result4&&result5) {return true;}
		else {return false;}
	}
	
	//~~~~~~~~~~~~~[tasks]~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//used to encapsulate the validation of all the variables for tasks
	private String taskValidate(String name, int type, int duration, int priority, int frequency, int need_logging, String date_created, int completed, int extra_sign_off) {
		String result="";
		
		if(name.replaceAll("\\s+", "").equals("")) {result+="Name is empty\n";}//Check name isn't empty even once whitespace is removed
		if(!checkValidBoolean(type)) {result+="Non-valid Type";}
		if(duration<=0) {result+="Non-valid Duration";}
		if(priority<=0||priority>3) {result+="Non-valid Priority";}
		if(frequency<0) {result+="Non-valid Frequency";}
		if(!checkValidBoolean(need_logging)) {result+="Non-valid need_logging";}
		if(!checkValidDateTime(date_created)){result+="Non-valid date created";}
		if(!checkValidBoolean(completed)) {result+="Non-valid completed";}
		if(extra_sign_off<0) {result+="Non-valid extra_sign_off";}
		
		return result;
	}//end taskValidate()
	
	public String addNewTask(String name, int type, int duration, int priority, int frequency, int need_logging, String date_created, int completed, int extra_sign_off) throws SQLException {
		String result="";
		
		//Find the next ID to use-------------------------
		String findNextIDsql = "SELECT COUNT(task_id)+1 from tasks";
		ResultSet nextIDResult = myDB.RunSQLQuery(findNextIDsql);
		int nextID=0;
		while(nextIDResult.next()) {
			nextID = nextIDResult.getInt(1);
		}
		//------------------------------------------------

		result=taskValidate(name,type,duration,priority,frequency,need_logging,date_created,completed,extra_sign_off);
		
		if(result.equals("")){ //if no problems then run SQL
			String addTaskSQL = "INSERT INTO tasks (task_id, task_name, type, duration, priority, frequency, need_logging, date_created, completed, extra_sign_off) VALUES ('"+nextID+"','"+name+"','"+type+"','"+duration+"','"+priority+"','"+frequency+"','"+need_logging+"','"+date_created+"','"+completed+"','"+extra_sign_off+"')";
			boolean addResult = myDB.RunSQL(addTaskSQL);
			if (!addResult) {
				result = "Insert failed.";
			}
		}
		
		return result;
	}//end addNewTask()
	
	
	public boolean assignCaretaker(int task_id, int user_id) {
		String updateCaretaker = "UPDATE tasks SET assigned_caretaker = '"+user_id+"' where task_id = '"+task_id+"'";
		//include check that user_id exists
		boolean result = myDB.RunSQL(updateCaretaker);
		return result;
	}//end assignCaretaker
	
	public boolean clearCaretaker(int task_id) {
		String sqlString = "UPDATE tasks SET assigned_caretaker = 'null' where task_id = '"+task_id+"'";
		boolean result = myDB.RunSQL(sqlString);
		return result;
	}//end clearCaretaker
	
	
	public ResultSet getTaskFromID(int ID) {
		String sqlString = "Select task_name, type, duration, priority, frequency, need_logging, date_created, completed, extra_sign_off FROM tasks WHERE task_id='"+ID+"'";
		ResultSet result = myDB.RunSQLQuery(sqlString);
		return result;
	}//end getTaskFromID()
	
	public ResultSet getAllTasks() {
		String getSQL="SELECT task_id, task_name, type, duration, priority, frequency, need_logging, date_created, completed, extra_sign_off, assigned_caretaker FROM tasks";
		ResultSet result = myDB.RunSQLQuery(getSQL);
		return result;
	}
	public ResultSet getAllCompletedTasks() {
		String getSQL="SELECT task_id, task_name, priority, date_created, completed, extra_sign_off, user1.user_name, assigned_caretaker, user2.user_name FROM tasks LEFT JOIN users AS user1 ON extra_sign_off=user1.user_id LEFT JOIN users AS user2 ON assigned_caretaker=user2.user_id WHERE need_logging=1";
		ResultSet result = myDB.RunSQLQuery(getSQL);
		return result;
	}
	
	
	public boolean deleteTask(int ID) {
		String sqlString ="DELETE FROM tasks WHERE task_id="+ID;
		boolean result = myDB.RunSQL(sqlString);
		return result;
	}//end deleteTask()
	
	public String updateTask(int task_ID, String name, int type, int duration, int priority, int frequency, int need_logging, String date_created, int completed, int extra_sign_off) throws SQLException {
		//check task_ID exists
		String checkIDsql = "SELECT task_name FROM tasks WHERE task_ID="+task_ID;
		ResultSet checkIDResult = myDB.RunSQLQuery(checkIDsql);
		boolean validID = false;
		if(checkIDResult.next()) {validID=true;}
		
		String result="";
		if(validID) {
			result=taskValidate(name,type,duration,priority,frequency,need_logging,date_created,completed,extra_sign_off);
			if(result.equals("")){
				String updateTaskSQL = "UPDATE tasks SET task_name='"+name+"',type='"+type+"',duration='"+duration+"',priority='"+priority+"',frequency='"+frequency+"',need_logging='"+need_logging+"',date_created='"+date_created+"',completed='"+completed+"', extra_sign_off='"+extra_sign_off+"' WHERE task_ID='"+task_ID+"'";
				boolean updateResult = myDB.RunSQL(updateTaskSQL);
				if (!updateResult) {
					result = "Update failed.";
				}
			}
		} else {result+="Invalid ID";}
				
		return result;
	}//end updateTask()
	//~~~~~~~~~~~~~[job]~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	public static String addNewJob(String name) throws SQLException {
		String findNextIDsql = new String("SELECT COUNT(job_id)+1 from job");
		ResultSet nextIDResult = myDB.RunSQLQuery(findNextIDsql);
		int nextID=0;
		while(nextIDResult.next()) {
			nextID = nextIDResult.getInt(1);
		}
		String result="";
		if(name.replaceAll("\\s+", "").equals("")) {result+="Name is empty\n";}//Check name isn't empty even once whitespace is removed
		
		
		if(result.equals("")) {
			String addJobSQL= new String("INSERT INTO job (job_id, job_desc) VALUES ('"+nextID+"','"+name+"')");
			boolean addResult = myDB.RunSQL(addJobSQL);
			if(!addResult) {result+="SQL Failed";}
		}
		
		
		return result;
	}
	
	public String getJobFromID(int ID) throws SQLException {
		String jobName="";
		
		String getJobSQL="SELECT job_desc FROM job WHERE job_id="+ID;
		ResultSet result = myDB.RunSQLQuery(getJobSQL);
		
		while(result.next()) {
			jobName = result.getString(0);
		}
		
		return jobName;
	}

	public String getPermsFromJob(String job) throws SQLException {
		String jobPerms = "";

		String getPermsSQL = "SELECT job_perms FROM job WHERE job_desc='"+job+"'";
		ResultSet result = myDB.RunSQLQuery(getPermsSQL);

		if (result.next()) {
			jobPerms = result.getString(1);
		}
		
		return jobPerms;
	}
	
	public boolean deleteJob(int ID) throws SQLException {
		
		String deleteJobSQL = "DELETE FROM job WHERE job_id="+ID;
		boolean result = myDB.RunSQL(deleteJobSQL);
		return result;
	}

	public String updateJob(int job_id, String name) throws SQLException {
		//check job_ID exists
		String checkIDsql = "SELECT job_desc FROM job WHERE job_ID="+job_id;
		ResultSet checkIDResult = myDB.RunSQLQuery(checkIDsql);
		boolean validID = false;
		if(checkIDResult.next()) {validID=true;}
		
		String result="";
		if(validID) {
			if(name.replaceAll("\\s+", "").equals("")) {result+="Name is empty\n";}//Check name isn't empty even once whitespace is removed
			if(result.equals("")) {
				String updateJobSQL="UPDATE job SET job_desc='"+name+"' WHERE job_id="+job_id;
				boolean addResult = myDB.RunSQL(updateJobSQL);
				if (!addResult) {
					result = "Insert failed.";
				}
			}
		} else {result+="Invalid ID";}
		
		return result;
	}

	//update adds perms, edit overwrites perms
	public String editJobPerms(int job_id, String perms) throws SQLException {
		//check job_ID exists
		String checkIDsql = "SELECT job_desc FROM job WHERE job_id="+job_id;
		ResultSet checkIDResult = myDB.RunSQLQuery(checkIDsql);
		boolean validID = false;
		if (checkIDResult.next()) {validID = true;}

		String result = "";
		if (validID) {
			//check {} format
			if (!(perms.charAt(0) == '{' && perms.charAt(perms.length()-1) == '}')) {result += "One of '{}' missing.";}
			//check {perm_name:perm_value,perm_name:perm_value}
			else if (!perms.matches("\\{(([a-zA-Z]+:[0-9]+,)+)?[a-zA-Z]+:[0-9]+\\}")) {result += "Format: '{MU:1,A:0}' failed.";}
			if (result.equals("")) {
				String updateJobSQL = "UPDATE job SET job_perms='" + perms + "' WHERE job_id=" + job_id;
				boolean addResult = myDB.RunSQL(updateJobSQL);
				if (!addResult) {
					result = "Insert failed.";
				}
			}
		} else {result+="Invalid ID.";}
		
		return result;
	}
	public String updateJobPerms(int job_id, String perms) throws SQLException {
		//check job_ID exists
		String checkIDsql = "SELECT job_perms FROM job WHERE job_id="+job_id;
		ResultSet checkIDResult = myDB.RunSQLQuery(checkIDsql);
		boolean validID = false;
		String jobPerms = "";
		if (checkIDResult.next()) {
			validID = true;
			jobPerms = checkIDResult.getString(1);//get perms
			if (!jobPerms.equals("{}")) {
				jobPerms = jobPerms.substring(1, jobPerms.length()-1);//remove {}
			}
		}

		String result = "";
		if (validID) {
			//check perm_name:perm_value,perm_name:perm_value
			if (!perms.matches("(([a-zA-Z]+:[0-9]+,)+)?[a-zA-Z]+:[0-9]+")) {result += "Format: 'MU:1,A:0' failed.";}
			if (result.equals("")) {
				jobPerms = "{" + jobPerms + "," + perms + "}";
				String updateJobSQL = "UPDATE job SET job_perms='" + jobPerms + "' WHERE job_id=" + job_id;
				boolean addResult = myDB.RunSQL(updateJobSQL);
				if (!addResult) {
					result = "Insert failed.";
				}
			}
		} else {result+="Invalid ID.";}
		
		return result;
	}
	
	public ResultSet getAllJobs() {
		String getSQL="SELECT job_id, job_desc, job_perms FROM job";
		ResultSet result = myDB.RunSQLQuery(getSQL);
		return result;
	}
	//~~~~~~~~~~~~~[perms]~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	public String permsValidate(String name, String desc) {
		String result = "";
		if (name.replaceAll("\\s+", "").equals("")) {result += "Name is empty\n";}//Check name isn't empty even once whitespace is removed
		if (desc.replaceAll("\\s+", "").equals("")) {result += "Desc is empty\n";}//Check desc isn't empty even once whitespace is removed
		return result;
	}

	//these perms methods are for testing and populating tables atm
	public String addNewPerms(String name, String desc) throws SQLException {
		String findNextIDsql = new String("SELECT COUNT(perm_id)+1 from perms");
		ResultSet nextIDResult = myDB.RunSQLQuery(findNextIDsql);
		int nextID = 0;
		while (nextIDResult.next()) {
			nextID = nextIDResult.getInt(1);
		}
		String result = "";
		result += permsValidate(name, desc);

		if (result.equals("")) {
			String addPermsSQL= new String("INSERT INTO perms (perm_id, perm_name, perm_desc) VALUES ('"+nextID+"','"+name+"','"+desc+"')");
			boolean addResult = myDB.RunSQL(addPermsSQL);
			if (!addResult) {result += "SQL Failed";}
		}

		return result;
	}
	//~~~~~~~~~~~~~[users]~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	public String userValidate(String name, int job, String notes, String mf) {
		//editing username or password should be handled alone/with care
		String result = "";
		
		if (name.replaceAll("\\s+", "").equals("")) {result += "Name is empty\n";}//Check name isn't empty even once whitespace is removed
		//if (notes.replaceAll("\\s+", "").equals("")) {result += "Notes is empty\n";}//notes can be empty
		if (mf.replaceAll("\\s+", "").equals("")) {result += "M/F is empty\n";}
		//no point checking if it's valid type if empty
		else if (!(mf.equals("M")||mf.equals("F"))) {result += "M/F is invalid\n";}
		//job>3 should be removed? limits number of jobs to 4
		if (job<0||job>3) {result += "Non-valid Job";}
		
		return result;
	}
	public String userValidate(String name, String username, int job, String password, String notes, String mf) {
		String result = userValidate(name, job, notes, mf);
		
		if (username.replaceAll("\\s+", "").equals("")) {result += "Username is empty\n";}
		if (password.replaceAll("\\s+", "").equals("")) {result += "Password is empty\n";}//password already hashed
		
		return result;
	}//end userValidate()

	public String addNewUser(String name, String username, int job, String password, String notes, String mf) throws SQLException{
		String result = "";
		
		//Find the next ID to use-------------------------
		String findNextIDsql = "SELECT COUNT(user_id)+1 from users";
		ResultSet nextIDResult = myDB.RunSQLQuery(findNextIDsql);
		int nextID = 0;
		while (nextIDResult.next()) {
			nextID = nextIDResult.getInt(1);
		}
		//------------------------------------------------

		//validate then insert record
		result = userValidate(name, username, job, password, notes, mf);
		
		if (result.equals("")) { //if no problems then run SQL
			String addUserSQL = "INSERT INTO users (user_id, user_name, username, job_id, hash_password, notes, M_F) VALUES ('"+nextID+"','"+name+"','"+username+"','"+job+"','"+password+"','"+notes+"','"+mf+"')";
			boolean addResult = myDB.RunSQL(addUserSQL);
			if (!addResult) {
				result = "Insert failed.";
			}
		}
		
		return result;
	}//end addNewUser()

	public ResultSet getUserFromUsername(String username) {
		String sqlString = "Select user_id, user_name, job.job_id, job.job_desc, job.job_perms, user_perms, notes, M_F FROM users INNER JOIN job ON users.job_id=job.job_id WHERE username='"+username+"'";
		ResultSet result = myDB.RunSQLQuery(sqlString);
		return result;
	}//end getUserFromID()

	//when a user attempts to login, also the only time we have to get it
	public ResultSet getPassFromUsername(String username) {
		String sqlString = "Select hash_password FROM users WHERE username='"+username+"'";
		ResultSet result = myDB.RunSQLQuery(sqlString);
		return result;
	}//end getPassFromUsername()

	public ResultSet getAllUsers() {
		String getSQL = "SELECT user_id, user_name, username, job.job_id, job.job_desc, M_F FROM users INNER JOIN job ON users.job_id=job.job_id ORDER BY user_name";
		ResultSet result = myDB.RunSQLQuery(getSQL);
		return result;
	}
	public ResultSet getUserFromID(int id) {
		String getSQL = "SELECT user_id, user_name, username, job.job_desc, notes, M_F FROM users INNER JOIN job ON users.job_id=job.job_id WHERE user_id = "+id;
		ResultSet result = myDB.RunSQLQuery(getSQL);
		return result;
	}
	public boolean deleteUser(int ID) {
		String sqlString = "DELETE FROM users WHERE user_id="+ID;
		boolean result = myDB.RunSQL(sqlString);
		return result;
	}//end deleteUser()

	public String updateUser(int user_ID, String name, int job, String notes, String mf) throws SQLException {
		//check user_ID exists
		String checkIDsql = "SELECT username FROM users WHERE user_id="+user_ID;
		ResultSet checkIDResult = myDB.RunSQLQuery(checkIDsql);
		boolean validID = false;
		if (checkIDResult.next()) {
			validID = true;
		}
		
		String result = "";
		if (validID) {
			result = userValidate(name, job, notes, mf);
			if (result.equals("")){
				String updateUserSQL = "UPDATE users SET user_name='"+name+"',job_id='"+job+"',notes='"+notes+"',M_F='"+mf+"' WHERE user_id='"+user_ID+"'";
				boolean updateResult = myDB.RunSQL(updateUserSQL);
				if (!updateResult) {
					result = "Update failed.";
				}
			}
		} else {
			result += "Invalid ID";
		}
				
		return result;
	}//end updateUser()

	//edit specific fields(condition) of the user
	public String editUser(int user_ID, String condition, String value) throws SQLException {
		//checking id is a valid id
		String checkIDsql = "SELECT username FROM users WHERE user_id="+user_ID;
		ResultSet checkIDResult = myDB.RunSQLQuery(checkIDsql);
		boolean validID = false;
		if (checkIDResult.next()) {
			validID = true;
		}

		//checking condition is valid
		if (validID) {
			boolean updateResult = false;
			if (condition.equals("user_name")) {
				String updateUserSQL = "UPDATE users SET user_name='"+value+"' WHERE user_id='"+user_ID+"'";
				updateResult = myDB.RunSQL(updateUserSQL);
			} else if (condition.equals("hash_password")) {
				String updateUserSQL = "UPDATE users SET hash_password='"+value+"' WHERE user_id='"+user_ID+"'";
				updateResult = myDB.RunSQL(updateUserSQL);
			} else if (condition.equals("M_F")) {
				String updateUserSQL = "UPDATE users SET M_F='"+value+"' WHERE user_id='"+user_ID+"'";
				updateResult = myDB.RunSQL(updateUserSQL);
			} else {
				return "Invalid: column type.";
			}
			if (!updateResult) {
				return "Invalid: Failed to update.";
			}
		} else {
			return "Invalid: user_ID.";
		}
		//successfully edited
		return "";
	}//end editUser()

	//update adds perms, edit overwrites perms
	public String editUserPerms(int user_id, String perms) throws SQLException {
		//check user_ID exists
		String checkIDsql = "SELECT username FROM users WHERE user_id="+user_id;
		ResultSet checkIDResult = myDB.RunSQLQuery(checkIDsql);
		boolean validID = false;
		if (checkIDResult.next()) {validID = true;}

		String result = "";
		if (validID) {
			//check {} format
			if (!(perms.charAt(0) == '{' && perms.charAt(perms.length()-1) == '}')) {result += "One of '{}' missing.";}
			//check {perm_name:perm_value,perm_name:perm_value}
			else if (!perms.matches("\\{(([a-zA-Z]+:[0-9]+,)+)?[a-zA-Z]+:[0-9]+\\}")) {result += "Format: '{MU:1,A:0}' failed.";}
			if (result.equals("")) {
				String updateUserSQL = "UPDATE users SET user_perms='" + perms + "' WHERE user_id=" + user_id;
				boolean addResult = myDB.RunSQL(updateUserSQL);
				if (!addResult) {
					result = "Insert failed.";
				}
			}
		} else {result+="Invalid ID.";}
		
		return result;
	}
	public String updateUserPerms(int user_id, String perms) throws SQLException {
		//check user_ID exists
		String checkIDsql = "SELECT user_perms FROM users WHERE user_id="+user_id;
		ResultSet checkIDResult = myDB.RunSQLQuery(checkIDsql);
		boolean validID = false;
		String userPerms = "";
		if (checkIDResult.next()) {
			validID = true;
			userPerms = checkIDResult.getString(1);//get perms
			if (!userPerms.equals("{}")) {
				userPerms = userPerms.substring(1, userPerms.length()-1);//remove {}
			}
		}

		String result = "";
		if (validID) {
			//check perm_name:perm_value,perm_name:perm_value
			if (!perms.matches("(([a-zA-Z]+:[0-9]+,)+)?[a-zA-Z]+:[0-9]+")) {result += "Format: 'MU:1,A:0' failed.";}
			if (result.equals("")) {
				userPerms = "{" + userPerms + "," + perms + "}";
				String updateUserSQL = "UPDATE users SET user_perms='" + userPerms + "' WHERE user_id=" + user_id;
				boolean addResult = myDB.RunSQL(updateUserSQL);
				if (!addResult) {
					result = "Insert failed.";
				}
			}
		} else {result+="Invalid ID.";}
		
		return result;
	}
	//~~~~~~~~~~~~~[logged_tasks]~~~~~~~~~~~~~~~~~~~~~~~~~~
	public String loggedTaskValidate(int task, int user, int user2, String dateCompleted) {
		String result = "";
		
		if (task<0) {result += "Non-valid Task\n";}
		if (user<0) {result += "Non-valid User\n";}
		if (user2<0) {result += "Non-valid Second User\n";}

		return result;
	}//end loggedTaskValidate()

	public String addNewLoggedTask(int task, int user, int user2, String dateCompleted) throws SQLException{
		String result = "";
		
		//Find the next ID to use-------------------------
		String findNextIDsql = "SELECT COUNT(logged_id)+1 from logged_tasks";
		ResultSet nextIDResult = myDB.RunSQLQuery(findNextIDsql);
		int nextID = 0;
		while (nextIDResult.next()) {
			nextID = nextIDResult.getInt(1);
		}
		//------------------------------------------------

		result = loggedTaskValidate(task, user, user2, dateCompleted);
		
		if (result.equals("")) { //if no problems then run SQL
			String addLoggedTaskSQL = "INSERT INTO logged_tasks (logged_id, task_id, user_id, second_user_id, date_completed) VALUES ('"+nextID+"','"+task+"','"+user+"','"+user2+"','"+dateCompleted+"')";
			boolean addResult = myDB.RunSQL(addLoggedTaskSQL);
			if (!addResult) {
				result = "Insert failed.";
			}
		}
	
		return result;
	}//end addNewLoggedTask()

	public ResultSet getLoggedTaskFromID(int ID) {
		String sqlString = "Select task_id, user_id, second_user_id, date_completed FROM logged_tasks WHERE logged_id='"+ID+"'";
		ResultSet result = myDB.RunSQLQuery(sqlString);
		return result;
	}//end getLoggedTaskFromID()

	//3 inner joins to get task name and 2 different users' names
	public ResultSet getAllLoggedTasks(String condition) {
		//double check the sql statement is correct later
		String getSQL = "SELECT logged_id, tasks.task_id, tasks.task_name, users1.user_id, users1.user_name, users2.user_id, users2.user_name, date_completed FROM logged_tasks LEFT JOIN tasks ON logged_tasks.task_id=tasks.task_id LEFT JOIN users AS users1 ON logged_tasks.user_id=users1.user_id LEFT JOIN users AS users2 ON logged_tasks.second_user_id=users2.user_id"+condition;
		ResultSet result = myDB.RunSQLQuery(getSQL);
		return result;
	}

	public boolean deleteLoggedTask(int ID) {
		String sqlString = "DELETE FROM logged_tasks WHERE logged_id="+ID;
		boolean result = myDB.RunSQL(sqlString);
		return result;
	}//end deleteLoggedTask()

	public String updateLoggedTask(int logged_ID, int user, int user2, String dateCompleted) throws SQLException {
		//check user_ID exists
		String checkIDsql = "SELECT task_id FROM logged_tasks WHERE logged_ID="+logged_ID;
		ResultSet checkIDResult = myDB.RunSQLQuery(checkIDsql);
		boolean validID = false;
		if (checkIDResult.next()) {validID=true;}
		
		String result = "";
		if (validID) {
			result = loggedTaskValidate(1, user, user2, dateCompleted);
			if (result.equals("")){
				String updateLoggedTaskSQL = "UPDATE logged_tasks SET user_id='"+user+"',second_user_id='"+user2+"',date_completed='"+dateCompleted+"' WHERE logged_ID='"+logged_ID+"'";
				boolean updateResult = myDB.RunSQL(updateLoggedTaskSQL);
				if (!updateResult) {
					result = "Update failed.";
				}
			}
		} else {result += "Invalid ID";}
				
		return result;
	}//end updateLoggedTask()

}//end class database
