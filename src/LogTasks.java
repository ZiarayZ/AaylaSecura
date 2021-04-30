import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class LogTasks {
    private static database taskLogDB;
    private ArrayList<LoggedTask> logged_tasks;
    private LoggedTask current;

    public LogTasks(database DB) {
        taskLogDB = DB;
        try {
            reloadLoggedTasks(taskLogDB.getAllLoggedTasks(""));
        } catch (SQLException e) {
            System.out.println(e);//change to give error code window
        }
    }

    //getters
    public ArrayList<LoggedTask> getLogged_Tasks() {
        return logged_tasks; //plan to iterate through ResultSet and create a table here later
    }
    public LoggedTask getCurrentTask() {
        return current;
    }

    //sorting methods
    public void sortByCaretaker(boolean descending) throws SQLException {
        String desc = orderBy(descending);
        reloadLoggedTasks(taskLogDB.getAllLoggedTasks(" ORDER BY users1.user_name "+desc));
    }
    public void sortByDeadline(boolean descending) throws SQLException {
        //there is no deadline field to sort by, unless we use the tasks table and calculate it using duration and date_created
        /*String desc = orderBy(descending);
        reloadLoggedTasks(taskLogDB.getAllLoggedTasks(" ORDER BY deadline? "+desc));*/
    }
    public void sortByCompleted(boolean descending) throws SQLException {
        String desc = orderBy(descending);
        reloadLoggedTasks(taskLogDB.getAllLoggedTasks(" ORDER BY logged_tasks.date_completed "+desc));
    }
    private void reloadLoggedTasks(ResultSet taskLogQuery) throws SQLException {
        while (taskLogQuery.next()) {
            logged_tasks.add(new LoggedTask(taskLogQuery.getInt(1),//logged_id
                                            taskLogQuery.getInt(2),//task_id
                                            taskLogQuery.getString(3),//task_name
                                            taskLogQuery.getInt(4),//user1_id
                                            taskLogQuery.getString(5),//user1_name
                                            taskLogQuery.getInt(6),//user2_id = maybe null
                                            taskLogQuery.getString(7),//user2_name = maybe null or ""
                                            taskLogQuery.getString(8)));//date_completed = maybe null or ""
        }
    }
    private String orderBy(boolean result) {
        if (result) {
            return "DESC";
        } else {
            return "ASC";
        }
    }

    public boolean addLoggedTask() {
        return false;
    }

    public boolean editLoggedtask() {
        return false;
    }
}
