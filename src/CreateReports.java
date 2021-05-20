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
    private ResultSet taskStatus;
    private ResultSet caretakerPerformance;
    private ResultSet taskCompletionLevel;

    public CreateReports(database db) {
        tasksDB = db;
        dbConnect = new dbConnection();
    }

    public void createTaskStatusReport() throws IOException {
    	taskStatus = dbConnect.RunSQLQuery("SELECT task_id, task_name, completed FROM tasks");
    	
    	File statusFile = new File("TaskStatus.txt"); //file to be written to
    	FileWriter fwrite = new FileWriter(statusFile, false);
    	PrintWriter pwrite = new PrintWriter(fwrite);
    	
    	try {
    		while (taskStatus.next()) {
    			pwrite.println("Task ID: " +taskStatus.getString(1)); //print each sql column to file as string
    			pwrite.println("Task name: "+taskStatus.getString(2));
    			pwrite.println("Completion Status: "+taskStatus.getString(3));
    			pwrite.println(" "); 			
    	  }
    	} catch (SQLException e) {
    		System.out.println("Failed to connect "+ e);
    	}
    	pwrite.close();
    	
    	//display text file on click
    	Desktop desktop = Desktop.getDesktop(); 
    	desktop.open(statusFile);
    } //end of task status report
    
    public void createCaretakerReport() throws IOException {
    	//sql query to display user and number of logged tasks
    	caretakerPerformance = dbConnect.RunSQLQuery("SELECT user_name, COUNT(logged_id) FROM users LEFT JOIN logged_tasks ON users.user_id = logged_tasks.user_id GROUP BY user_name, logged_tasks.user_id");

    	
    	File caretakerFile = new File("Caretaker.txt");
    	FileWriter fwrite = new FileWriter(caretakerFile, false);
    	PrintWriter pwrite = new PrintWriter(fwrite);
    	
    	try {
    		while (caretakerPerformance.next()) {
    			pwrite.println("Caretaker name: " + caretakerPerformance.getString(1)+" "+ " No. of Logged Tasks: "+ caretakerPerformance.getInt(2));
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
    	taskCompletionLevel = tasksDB.getAllLoggedTasks(" ORDER BY date_completed ");//show logged tasks in date order 
    	
    	File completedFile = new File("Completed.txt");
    	FileWriter fwrite = new FileWriter(completedFile, false);
    	PrintWriter pwrite = new PrintWriter(fwrite);
    	
    	try {
    		while (taskCompletionLevel.next()) {
    			//print only relevant columns
    			pwrite.println(taskCompletionLevel.getString(3) + " with an ID no. of " + taskCompletionLevel.getInt(2) + " was completed by " + 
    							taskCompletionLevel.getString(5) + " on " + taskCompletionLevel.getString(8));
    		}
    	} catch (SQLException e) {
    		System.out.println("Failed to connect " + e);
    	}
    	pwrite.close();
    	
    	Desktop desktop = Desktop.getDesktop(); 
    	desktop.open(completedFile);
    } //end of completed task report
}
