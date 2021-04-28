
public class task {
	private int id;
	private String name;
	private int type;
	private int duration;
	private int priority;
	private int frequency;
	private int need_logging;
	private String date_created;
	private int completed;
	private int extra_sign_off;
	private int assigned_caretaker;
	
	public task(int id, String name, int type, int duration, int priority,	int frequency, int need_logging, String date_created, int completed, int extra_sign_off) { //create a task without a caretaker
		this.id = id;
		this.name = name;
		this.type = duration;
		this.priority = priority;
		this.frequency = frequency;
		this.need_logging = need_logging;
		this.date_created = date_created;
		this.completed = completed;
		this.extra_sign_off = extra_sign_off;
	}
	
	public task(int id, String name, int type, int duration, int priority,	int frequency, int need_logging, String date_created, int completed, int extra_sign_off, int assigned_caretaker) { //create a task with a caretaker
		this.id = id;
		this.name = name;
		this.type = duration;
		this.priority = priority;
		this.frequency = frequency;
		this.need_logging = need_logging;
		this.date_created = date_created;
		this.completed = completed;
		this.extra_sign_off = extra_sign_off;
		this.assigned_caretaker = assigned_caretaker;
	}
	public int getID() {
		return id;
	}
	public String getName() {
		return name;
	}
	public int getType() {
		return type;
	}
	public int getDuration() {
		return duration;
	}
	public int getPriority() {
		return priority;
	}
	public int getFrequency() {
		return frequency;
	}
	public int getNeedLogging() {
		return need_logging;
	}
	public String getDateCreated() {
		return date_created;
	}
	public int getCompleted() {
		return completed;
	}
	public int getExtraSignOff() {
		return extra_sign_off;
	}
	
	public int getAssignedCaretaker() {
		return assigned_caretaker;
	}
}
