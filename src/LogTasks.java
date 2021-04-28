import java.sql.ResultSet;
import java.sql.SQLException;

public class LogTasks {
    private static database taskLogDB;
    private ResultSet taskLogQuery;
    private String taskCompletedBy;
    private String timeCompleted;
    private String comments;

    public LogTasks(database DB) {
        taskLogDB = DB;
        taskLogQuery = taskLogDB.getAllLoggedTasks("");
    }

    //getters
    public ResultSet getLogged_Tasks() {
        return taskLogQuery; //plan to iterate through ResultSet and create a table here later
    }

    //sorting methods
    public void sortByCaretaker(boolean descending) throws SQLException {
        String desc = orderBy(descending);
        taskLogQuery = taskLogDB.getAllLoggedTasks(" ORDER BY users1.user_name "+desc);
    }
    public void sortByDeadline(boolean descending) throws SQLException {
        String desc = orderBy(descending);
        //there is no deadline field to sort by, unless we use the tasks table and calculate it using duration and date_created
        //taskLogQuery = taskLogDB.getAllLoggedTasks(" ORDER BY deadline? "+desc);
    }
    public void sortByCompleted(boolean descending) throws SQLException {
        String desc = orderBy(descending);
        taskLogQuery = taskLogDB.getAllLoggedTasks(" ORDER BY logged_tasks.date_completed "+desc);
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
