import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
					unsorted.remove(a);
				}
			}
		}
		return sorted;
	}
	
	private String getYear(String dateTime) {
		String result = dateTime.substring(0, 4);
		return result;
	}
	
	private String getMonth(String dateTime) {
		String result = dateTime.substring(5, 7);
		return result;
	}
	
	private String getDay(String dateTime) {
		String result = dateTime.substring(8, 10);
		return result;
	}
	
	public ArrayList<task> sortUndoneTasksByDate(){
		ArrayList<task> unsorted = getUndoneTasks();
		ArrayList<task> sorted = new ArrayList<task>();
		
		
		
	    
	    
		
		return sorted;
	}
	
	
	
	
	
	
}
