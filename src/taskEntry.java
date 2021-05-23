import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class taskEntry {
	
	private database db;
	
	public taskEntry(database db) {
		this.db = db;
		//create UI?
	}
	
	//add task
	public String addTask(String name, int type, int duration, int priority, int frequency, int need_logging, String date_created, int completed, int extra_sign_off) throws SQLException {
		String result;
		boolean boolResult = checkExistingTask(name, date_created);
		if(boolResult) {
			result = db.addNewTask(name, type, duration, priority, frequency, need_logging, date_created, completed, extra_sign_off);
		}else {result="Task Already Exists";}
		return result;
	}
	//edit task
	public String editTask(int task_ID, String name, int type, int duration, int priority, int frequency, int need_logging, String date_created, int completed, int extra_sign_off) throws SQLException {
		String result = db.updateTask(task_ID, name, type, duration, priority, frequency, need_logging, date_created, completed, extra_sign_off);
		return result;
	}
	//get all tasks
	public ArrayList<task> getAllTasks() {
		ArrayList<task> myTasks = new ArrayList<task>();
		ResultSet rs = db.getAllTasks();
		try {
			while(rs.next()) {
				task tempTask = new task(rs.getInt(1),rs.getString(2),rs.getInt(3),rs.getInt(4),rs.getInt(5),rs.getInt(6),rs.getInt(7),rs.getString(8),rs.getInt(9),rs.getInt(10));
				myTasks.add(tempTask);
			}
		}
		catch(SQLException e) {
			return null;
		}
		return myTasks;
	}
		
	//get all undone tasks
	public ArrayList<task> getUndoneTasks(){
		ArrayList<task> allTasks = getAllTasks();
		ArrayList<task> undoneTasks = new ArrayList<task>();
		for(int a=0;a<allTasks.size();a++) {
			if(allTasks.get(a).getCompleted()==0) {
				undoneTasks.add(allTasks.get(a));
			}
		}
		return undoneTasks;
	}
	
	//get all done tasks
	public ArrayList<task> getDoneTasks(){
		ArrayList<task> allTasks = getAllTasks();
		ArrayList<task> doneTasks = new ArrayList<task>();
		for(int a=0;a<allTasks.size();a++) {
			if(allTasks.get(a).getCompleted()==1) {
				doneTasks.add(allTasks.get(a));
			}
		}
		return doneTasks;
	}
	
	//other ways of sorting them
	
	//delete task
	public boolean deleteTask(int id) {
		boolean result = db.deleteTask(id); //INCLUDE FEEDBACK!!!
		return result;
	}
	
	
	private boolean checkDateTimeSameDay(String dateTime1, String dateTime2) {
		String day1 = dateTime1.substring(0,10);
		String day2 = dateTime2.substring(0,10);
		if(day1.equals(day2)) {
			return true;
		}
		else{
			return false;
		}
	}
	
	
	//check if task already exists
	private boolean checkExistingTask(String name, String date_created) { //checks if a task made the same day with the same name exists
		ArrayList<task> undoneTasks = getUndoneTasks();
		
		for(int a=0;a<undoneTasks.size();a++) {
			task tempTask = undoneTasks.get(a);
			if(name.equals(tempTask.getName())) {
				if(checkDateTimeSameDay(date_created,tempTask.getDateCreated())) {
					return true;
				}
			}
		}
				
		return false;
	}
	
	public ArrayList<task> sortUndoneTasksByPriority() {
		ArrayList<task> unsorted = getUndoneTasks();
		ArrayList<task> sorted = new ArrayList<task>();
		
		for(int maxPriority=3;maxPriority>0;maxPriority--) {//this orders it with a priority range of 1-3 with 3 being the highest priority
			for(int a=0;a<unsorted.size();a++) {
				if(unsorted.get(a).getPriority()==maxPriority) {
					sorted.add(unsorted.get(a));
					unsorted.remove(a);
				}
			}
		}
		return sorted;
	}
	
	public ArrayList<task> sortUndoneTasksByDate(){
		ArrayList<task> taskList = getUndoneTasks();
		int sortedPoint=0;
		task task1;
		task task2;
		while(sortedPoint<taskList.size()-1) {
			task1=taskList.get(sortedPoint);
			task2=taskList.get(sortedPoint+1);
			if(task1.getConcDateTime()<task2.getConcDateTime()) {
				taskList.set(sortedPoint,task2);
				taskList.set(sortedPoint+1,task1);
				sortedPoint = 0;
				//System.out.println("switched");
			}
			else {sortedPoint++;}
		}
		return taskList;
	}
	
	
	public ArrayList<task> filterToOneOffTasks (ArrayList<task> input){
		ArrayList<task> output = new ArrayList<task>();
		for(int a=0;a<input.size();a++) {
			if(input.get(a).getType()==1) {
				output.add(input.get(a));
			}
		}
		return output;
	}
	
	public ArrayList<task> filterToRepeatTasks (ArrayList<task> input){
		ArrayList<task> output = new ArrayList<task>();
		for(int a=0;a<input.size();a++) {
			if(input.get(a).getType()==0) {
				output.add(input.get(a));
			}
		}
		return output;
	}
	
}
