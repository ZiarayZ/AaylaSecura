package groupProject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

public class LogTasks {
    private database taskLogDB = new database();
    private ResultSet taskLogQuery;
    private String taskCompletedBy;
    private String timeCompleted;
    private String comments;

    //sorting methods
    public ResultSet sortByCaretaker(boolean descending) {
        return null;
    }
    public ResultSet sortByDeadline(boolean descending) {
        return null;
    }
    public ResultSet sortByCompleted(boolean descending) {
        return null;
    }

    public boolean addLoggedTask() {
        return false;
    }

    public boolean editLoggedtask() {
        return false;
    }
}
