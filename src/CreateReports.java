import java.awt.Desktop;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CreateReports {
    private static database tasksDB;
    private dbConnection dbConnect; //variable for report-specific queries
    private String taskStatus;
    private String caretakerPerformance;
    private String taskCompletionLevel;

    public CreateReports(database db) {
        tasksDB = db;
        dbConnect = new dbConnection();
        dbConnect.Connect("src\\groupDatabase.db");
    }

    public void createTaskStatusReport() throws IOException {
    	ResultSet taskCompletion = dbConnect.RunSQLQuery("SELECT task_id, task_name, completed FROM tasks");
    	taskStatus = taskCompletion.toString(); //turn db entries into string types
    	
    	File statusFile = new File("TaskStatus.txt"); //file to be written to
    	FileWriter fwrite = new FileWriter(statusFile, true);
    	PrintWriter pwrite = new PrintWriter(fwrite);
    	
    	try {
    		while (taskCompletion.next()) {
    			pwrite.println(taskStatus); //prints each task on a new line in text file    			
    	  }
    	} catch (SQLException e) {
    		System.out.println("Failed to connect "+ e);
    	}
    	pwrite.close();
    	
    	//display text file on click (could go here or in code for UI with filepath in .open)
    	Desktop desktop = Desktop.getDesktop(); 
    	desktop.open(statusFile);
    } //end of task status report
    
    public void createCaretakerReport() throws IOException {
    	//sql query to display user and number of logged tasks (not tested)
    	ResultSet caretakerDetails = dbConnect.RunSQLQuery("SELECT user_name, COUNT(logged_id) FROM users LEFT JOIN logged_tasks ON users.user_id = logged_tasks.user_id GROUP BY user_name, user_id");
    	caretakerPerformance = caretakerDetails.toString();
    	
    	File caretakerFile = new File("Caretaker.txt");
    	FileWriter fwrite = new FileWriter(caretakerFile, true);
    	PrintWriter pwrite = new PrintWriter(fwrite);
    	
    	try {
    		while (caretakerDetails.next()) {
    			pwrite.println(caretakerPerformance);
    		}
    	} catch (SQLException e) {
    		System.out.println("Failed to connect "+ e);
    	}
    	pwrite.close();
    	
    	//display text file on click
    	Desktop desktop = Desktop.getDesktop(); 
    	desktop.open(caretakerFile);
    } //end of caretaker report
    
    public void createCompletedTaskReport() throws IOException {
    	ResultSet completedTasks = tasksDB.getAllLoggedTasks(null); //may need non null argument
    	taskCompletionLevel = completedTasks.toString();
    	
    	File completedFile = new File("Completed.txt");
    	FileWriter fwrite = new FileWriter(completedFile, true);
    	PrintWriter pwrite = new PrintWriter(fwrite);
    	
    	try {
    		while (completedTasks.next()) {
    			pwrite.println(taskCompletionLevel);
    		}
    	} catch (SQLException e) {
    		System.out.println("Failed to connect " + e);
    	}
    	pwrite.close();
    	
    	Desktop desktop = Desktop.getDesktop(); 
    	desktop.open(completedFile);
    } //end of completed task report
}
