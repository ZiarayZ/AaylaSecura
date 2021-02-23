package protoGroupProject;

public class task {
	private String name;//Job Name
	private int type; //
	private int duration; //Days till due
	private int importance; //weighted importance
	private int frequency;
	
	//-----------[Constructor]----------------------------------
	public task(String name, int type, int duration, int importance, int frequency){
		this.name=name;
		this.type=type;
		this.duration=duration;
		this.importance=importance;
		this.frequency=frequency;
	}
	
	//-----------[Getters and Setters]-----------------------------
	public String getName() {return name;}
	public void setName(String name) {this.name=name;}
	public int getType() {return type;}
	public void setType(int type) {this.type=type;}
	public int getDuration() {return duration;}
	public void setDuration(int duration) {this.duration=duration;}
	public int getImportance() {return importance;}
	public void setImportance(int importance) {this.importance=importance;}
	public int getFrequency() {return frequency;}
	public void setFrequency(int frequency) {this.frequency=frequency;}
	
	//-------------------------------------------------------
	

}
