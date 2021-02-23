package protoGroupProject;

import java.util.ArrayList;

public class taskList {
	private ArrayList<task> listOfTasks;
	private int numOfTasks;
	//-----------[Constructor]----------------------------------
	public taskList(){
		listOfTasks = new ArrayList<task>();
		numOfTasks=listOfTasks.size();
	}
	
	//-----------[Getters and Setters]-----------------------------
	public ArrayList<task> getListOfTasks(){return listOfTasks;}
	public void setListOfTasks(ArrayList<task> listOfTasks){this.listOfTasks=listOfTasks;}
	public int getNumOfTasks(){return numOfTasks;}
	
	//------------[update numOfTasks]---------------------------
	public void updateNumOfTasks(){numOfTasks=listOfTasks.size();}
	
	//------------[Loads tasks from the database]--------------
	public void loadFromDB(){
		//do loading from DB
		return;
	}
	
	//-------------[Create a task]-------------------------------
	public void createTask(String name, int type, int duration, int importance, int frequency){
		task newTask = new task(name,type,duration,importance,frequency);
		listOfTasks.add(newTask);
	}
	
	//-----------------------------------------------------------

}
