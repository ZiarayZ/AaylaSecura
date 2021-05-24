import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class taskAssign {
	private database db;
	ArrayList<task> allTasks;
	
	public taskAssign(database db) {
		this.db=db;
	}
	
	public boolean allocateCaretaker(int task_id, int caretaker_id) {
		boolean result = db.assignCaretaker(task_id, caretaker_id);
		return result;
	}
	
	public boolean removeAllocatedCaretaker(int task_id) {
		boolean result = db.clearCaretaker(task_id);
		return result;
	}
	
	//get all tasks
	public ArrayList<task> getAllTasks() {
		ArrayList<task> myTasks = new ArrayList<task>();
		ResultSet rs = db.getAllTasks();
		try {
			while(rs.next()) {
				task tempTask = new task(rs.getInt(1),rs.getString(2),rs.getInt(3),rs.getInt(4),rs.getInt(5),rs.getInt(6),rs.getInt(7),rs.getString(8),rs.getInt(9),rs.getInt(10),rs.getInt(11));
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
	
	
	public ArrayList<task> sortUndoneTasksByPriority() {
		ArrayList<task> unsorted = getUndoneTasks();
		ArrayList<task> sorted = new ArrayList<task>();
		
		for(int maxPriority=3;maxPriority>0;maxPriority--) {//this orders it with a priority range of 1-3 with 3 being the highest priority
			for(int a=0;a<unsorted.size();a++) {
				if(unsorted.get(a).getPriority()==maxPriority) {
					sorted.add(unsorted.get(a));
				}
			}
		}
		return sorted;
	}
	
	public ArrayList<task> sortUndoneTasksByUnassigned() {
		ArrayList<task> unsorted = getUndoneTasks();
		ArrayList<task> sorted = new ArrayList<task>();
		
		for(int a=0;a<unsorted.size();a++) {
			if(unsorted.get(a).getAssignedCaretaker()==0) {
				sorted.add(unsorted.get(a));
			}
		}
		for(int a=0;a<unsorted.size();a++) {
			if(unsorted.get(a).getAssignedCaretaker()!=0) {
				sorted.add(unsorted.get(a));
			}
		}
		return sorted;
	}
	
	public ArrayList<task> sortUndoneTasksByAssigned() {
		ArrayList<task> unsorted = getUndoneTasks();
		ArrayList<task> sorted = new ArrayList<task>();
		
		for(int a=0;a<unsorted.size();a++) {
			if(unsorted.get(a).getAssignedCaretaker()!=0) {
				sorted.add(unsorted.get(a));
			}
		}
		for(int a=0;a<unsorted.size();a++) {
			if(unsorted.get(a).getAssignedCaretaker()==0) {
				sorted.add(unsorted.get(a));
			}
		}
		return sorted;
	}
	
	
	public task getTask(int id) {
		task result = null;
		ArrayList<task> taskList = getAllTasks();
		for(int a=0;a<taskList.size();a++) {
			if(taskList.get(a).getID()==id) {
				result = taskList.get(a);
			}
		}
		return result;
	}
	
	public ArrayList<task> sortUndoneTasksByDate(){
		ArrayList<task> taskList = getUndoneTasks();
		int sortedPoint=0;
		task task1;
		task task2;
		while(sortedPoint<taskList.size()-1) {
			task1=taskList.get(sortedPoint);
			task2=taskList.get(sortedPoint+1);
			if(task1.getConcDateTime()>task2.getConcDateTime()) {
				taskList.set(sortedPoint,task2);
				taskList.set(sortedPoint+1,task1);
				sortedPoint = 0;
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
	
	
	public ArrayList<task> filterToCaretaker (ArrayList<task> input, int caretaker_id){
		ArrayList<task> output = new ArrayList<task>();
		for(int a=0;a<input.size();a++) {
			if(input.get(a).getAssignedCaretaker()==caretaker_id) {
				output.add(input.get(a));
			}
		}
		return output;
	}
	
	public String getcaretakerNameFromID(int id) {
		String result = null;
		ResultSet rs = db.getUserFromID(id);
		try {
			while(rs.next()) {
				result = rs.getString(2);
			}
		}
		catch(SQLException e) {
			return null;
		}
		return result;
	}
	
	public ArrayList<String> getAllCaretakersNames() {
		ArrayList<String> result = new ArrayList<String>();
		ResultSet rs = db.getAllUsers();
		String tempName;
		int tempJobId;
		try {
			while(rs.next()) {
				tempName = rs.getString(2);
				tempJobId = rs.getInt(4);
				if(tempJobId==1) {
					result.add(tempName);
				}
			}
		}
		catch(SQLException e) {
			return null;
		}
		
		return result;
	}
	
	public ArrayList<Integer> getAllCaretakersIDs() {
		ArrayList<Integer> result = new ArrayList<Integer>();
		ResultSet rs = db.getAllUsers();
		int tempID;
		int tempJobId;
		try {
			while(rs.next()) {
				tempID = rs.getInt(1);
				tempJobId = rs.getInt(4);
				if(tempJobId==1) {
					result.add(tempID);
				}
			}
		}
		catch(SQLException e) {
			return null;
		}
		
		return result;
	}
	
	
}
