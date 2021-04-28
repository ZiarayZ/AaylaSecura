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
		String result = db.addNewTask(name, type, duration, priority, frequency, need_logging, date_created, completed, extra_sign_off);
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
	
}
