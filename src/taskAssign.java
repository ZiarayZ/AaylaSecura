import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class taskAssign {
	private database db;
	ArrayList<task> allTasks;
	
	public taskAssign(database db) {
		this.db=db;
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
	
	
		
	
	
	
	
	
	
}
