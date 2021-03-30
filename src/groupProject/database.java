package groupProject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
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
	
	public static void  main(String[] args) throws SQLException{
		addNewJob("test job");
	}
	
	
	private boolean checkValidDateTime(final String dateTime) { //returns true if given date time is valid
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
	
	//~~~~~~~~~~~~~[tasks]~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//used to encapsulate the validation of all the variables for tasks
	public String taskValidate(String name, int type, int duration, int priority, int frequency, int need_logging, String date_created, int completed, int extra_sign_off) {
		String result="";
		
		if(name.replaceAll("\\s+", "").equals("")) {result+="Name is empty\n";}//Check name isn't empty even once whitespace is removed
		if(checkValidBoolean(type)) {result+="Non-valid Type";}
		if(duration>=0) {result+="Non-valid Duration";}
		if(priority<=0||priority>3) {result+="Non-valid Priority";}
		if(frequency<0) {result+="Non-valid Frequency";}
		if(checkValidBoolean(need_logging)) {result+="Non-valid need_logging";}
		if(checkValidDateTime(date_created)){result+="Non-valid date created";}
		if(checkValidBoolean(completed)) {result+="Non-valid completed";}
		if(checkValidBoolean(extra_sign_off)) {result+="Non-valid extra_sign_off";}
		
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
		}
		
		return result;
	}//end addNewTask()
	
	public ResultSet getTaskFromID(int ID) {
		String sqlString = "Select task_name, type, duration, priority, frequency, need_logging, date_created, completed, extra_sign_off FROM tasks WHERE task_id='"+ID+"'";
		ResultSet result = myDB.RunSQLQuery(sqlString);
		return result;
	}//end getTaskFromID()
	
	public ResultSet getAllTasks() {
		String getSQL="SELECT task_id, task_name, type, duration, priority, frequency, need_logging, date_created, completed, extra_sign_off FROM tasks";
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
				String updateTaskSQL = "UPDATE tasks SET task_name='"+name+"',type='"+type+"',duration='"+duration+"',priority='"+priority+"',frequency='"+frequency+"',need_logging='"+need_logging+"',date_created'"+date_created+"',completed='"+completed+"', extra_sign_off='"+extra_sign_off+"' WHERE task_ID='"+task_ID+"'";
				boolean updateResult = myDB.RunSQL(updateTaskSQL);
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
	
	public boolean deleteJob(int ID) throws SQLException {
		
		String deleteJobSQL = "DELETE FROM job WHERE job_id="+ID;
		boolean result = myDB.RunSQL(deleteJobSQL);
		return result;
	}

	public String updateJob(int job_id, String name) throws SQLException {
		//check task_ID exists
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
			}
		} else {result+="Invalid ID";}
		
		return result;
	}
	
	public ResultSet getAllJobs() {
		String getSQL="SELECT job_id, job_desc FROM job";
		ResultSet result = myDB.RunSQLQuery(getSQL);
		return result;
	}
	//~~~~~~~~~~~~~[users]~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	//~~~~~~~~~~~~~[logged_tasks]~~~~~~~~~~~~~~~~~~~~~~~~~~

}//end class database
