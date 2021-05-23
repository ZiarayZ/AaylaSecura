
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
	private long concDateTime;
	
	public task(int id, String name, int type, int duration, int priority,	int frequency, int need_logging, String date_created, int completed, int extra_sign_off) { //create a task without a caretaker
		this.id = id;
		this.name = name;
		this.type = type;
		this.duration = duration;
		this.priority = priority;
		this.frequency = frequency;
		this.need_logging = need_logging;
		this.date_created = date_created;
		this.completed = completed;
		this.extra_sign_off = extra_sign_off;
		genDate();
	}
	
	public task(int id, String name, int type, int duration, int priority,	int frequency, int need_logging, String date_created, int completed, int extra_sign_off, int assigned_caretaker) { //create a task with a caretaker
		this.id = id;
		this.name = name;
		this.type = type;
		this.duration = duration;
		this.priority = priority;
		this.frequency = frequency;
		this.need_logging = need_logging;
		this.date_created = date_created;
		this.completed = completed;
		this.extra_sign_off = extra_sign_off;
		this.assigned_caretaker = assigned_caretaker;
		genDate();
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
	public long getConcDateTime() {
		return concDateTime;
	}
	public void genDate() {
		int year = Integer.parseInt(date_created.substring(0, 4));
		int month = Integer.parseInt(date_created.substring(5, 7));
		int day = Integer.parseInt(date_created.substring(8, 10));
		int hour = Integer.parseInt(date_created.substring(11, 13));
		int min = Integer.parseInt(date_created.substring(14, 16));
		int sec = Integer.parseInt(date_created.substring(17, 19));
		concDateTime = (year*10000000000L)+(month*100000000)+(day*1000000)+(hour*10000)+(min*100)+sec;
	}
}
