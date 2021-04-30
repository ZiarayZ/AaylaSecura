

public class LoggedTask {
    private int logged_id;
    private int task_id;
    private String task_name;
    private int fir_user_id;
    private String fir_user_name;
    private int sec_user_id;
    private String sec_user_name;
    private String date_completed;

    //constructor
    public LoggedTask(int logged, int task, String task1, int user1, String user11, int user2, String user22, String date) {
        logged_id = logged;
        task_id = task;
        task_name = task1;
        fir_user_id = user1;
        fir_user_name = user11;
        sec_user_id = user2;
        sec_user_name = user22;
        date_completed = date;
    }

    //getters
    public int getLogID() {
        return logged_id;
    }
    public int getTaskID() {
        return task_id;
    }
    public int getUser1ID() {
        return fir_user_id;
    }
    public int getUser2ID() {
        return sec_user_id;
    }
    public String getTaskName() {
        return task_name;
    }
    public String getUser1Name() {
        return fir_user_name;
    }
    public String getUser2Name() {
        return sec_user_name;
    }
    public String getDateCompleted() {
        return date_completed;
    }
}
