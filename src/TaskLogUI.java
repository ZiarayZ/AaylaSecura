import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.table.TableColumnModel;
import java.awt.Font;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TaskLogUI extends JPanel {

	private UserInterface window;
	private database taskDB;
	private JTable taskListTable;
	private JTextField caretakerNameField;
	private JTextField timeCompletedField;
	private LogTasks taskLog;

	/**
	 * Create the panel.
	 */
	public TaskLogUI(UserInterface UI, LogTasks loggingTask, database DB) {//, UserManagement User) {
		taskLog = loggingTask;
		taskDB = DB;
		//sets window to have this contentPane
		window = UI;
		setBackground(new Color(255, 255, 255));
		setBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(119, 136, 153), new Color(192, 192, 192)));
		setLayout(new GridBagLayout());
		JPanel fixedPane = new JPanel();
		fixedPane.setLayout(null);
		fixedPane.setBackground(new Color(255, 255, 255));
		
		JLabel lblHeadingLabel = new JLabel("Please Log a Completed Task");
		lblHeadingLabel.setForeground(new Color(105, 105, 105));
		lblHeadingLabel.setOpaque(true);
		lblHeadingLabel.setFont(new Font("Verdana", Font.PLAIN, 28));
		lblHeadingLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblHeadingLabel.setBackground(new Color(224, 255, 255));
		lblHeadingLabel.setBounds(256, 55, 444, 67);
		fixedPane.add(lblHeadingLabel);
		
		String[] colHeaders = {"Logged Task ID", "Task ID", "Task Name", "First User ID", "First User", "Second User ID", "Second User", "Date Completed"};
		Object[][] data = populateTable();
		taskListTable = new JTable(data,colHeaders);
		//this removes the id column, but you should be able to call 'userTable.getModel().getValueAt(row, 0)' to get the id
		TableColumnModel tcm = taskListTable.getColumnModel();
		tcm.removeColumn(tcm.getColumn(0));//Logged Task ID
		tcm.removeColumn(tcm.getColumn(0));//Task ID -1
		tcm.removeColumn(tcm.getColumn(1));//First User ID -2
		tcm.removeColumn(tcm.getColumn(2));//Second User ID -3
		//import table into a scroll pane so that the table headers are visible and other things
		JScrollPane scrollPane = new JScrollPane(taskListTable);
		scrollPane.setBackground(new Color(238, 238, 238));
		scrollPane.setBounds(68, 150, 820, 232);
		fixedPane.add(scrollPane);
		
		caretakerNameField = new JTextField();
		caretakerNameField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//record caretaker name here
			}
		});
		caretakerNameField.setBackground(new Color(238, 238, 238));
		caretakerNameField.setBounds(261, 427, 118, 20);
		fixedPane.add(caretakerNameField);
		caretakerNameField.setColumns(10);
		
		JLabel lblNameLabel = new JLabel("Completed By");
		lblNameLabel.setBounds(159, 430, 92, 14);
		fixedPane.add(lblNameLabel);
		
		timeCompletedField = new JTextField();
		timeCompletedField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//record when the task was completed
			}
		});
		timeCompletedField.setBackground(new Color(238, 238, 238));
		timeCompletedField.setBounds(261, 473, 118, 20);
		fixedPane.add(timeCompletedField);
		timeCompletedField.setColumns(10);
		
		JLabel lblTimeLabel = new JLabel("Time Completed");
		lblTimeLabel.setBounds(159, 476, 105, 14);
		fixedPane.add(lblTimeLabel);
		
		JTextArea addCommentsField = new JTextArea();
		//need to find out how to handle input in a text area
		addCommentsField.setBackground(new Color(238, 238, 238));
		addCommentsField.setBounds(641, 425, 213, 68);
		fixedPane.add(addCommentsField);
		
		JLabel lblNewLabel = new JLabel("Additional Comments");
		lblNewLabel.setBounds(493, 430, 138, 14);
		fixedPane.add(lblNewLabel);
		
		JButton sortByCaretakerButton = new JButton("<html><center>Sort By<br>Caretaker</center></html>");
		sortByCaretakerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//sort db by caretaker here
			}
		});
		sortByCaretakerButton.setBounds(78, 553, 125, 54);
		fixedPane.add(sortByCaretakerButton);
		
		JButton sortByDeadlineButton = new JButton("<html><center>Sort By<br>Deadline</center></html>");
		sortByDeadlineButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//sort db by deadline here
			}
		});
		sortByDeadlineButton.setBounds(213, 553, 132, 54);
		fixedPane.add(sortByDeadlineButton);
		
		JButton editCompletedButton = new JButton("<html><center>Edit<br>Completed Task</center></html>");
		editCompletedButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//detects a selected task to edit here
			}
		});
		editCompletedButton.setBounds(355, 553, 133, 54);
		fixedPane.add(editCompletedButton);
		
		JButton logTaskButton = new JButton("<html><center>Log<br>Task</center></html>");
		logTaskButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//logs the task
			}
		});
		logTaskButton.setBounds(498, 553, 118, 54);
		fixedPane.add(logTaskButton);
		
		JButton undoLoggedTaskButton = new JButton("<html><center>Undo<br>Changes</center></html>");
		undoLoggedTaskButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//undo functionality (optional)
			}
		});
		undoLoggedTaskButton.setBounds(626, 553, 124, 54);
		fixedPane.add(undoLoggedTaskButton);
		
		JButton createReportButton = new JButton("<html><center>Create<br>Report</center></html>");
		createReportButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//generates the report
			}
		});
		createReportButton.setBounds(760, 553, 128, 54);
		fixedPane.add(createReportButton);
		fixedPane.setPreferredSize(new Dimension(956,717));
		add(fixedPane);
	}
	
	public Object[][] populateTable() {
		ArrayList<Object[]> tempData = new ArrayList<Object[]>();

		//call for all logged tasks info
		try {
			taskLog.reloadLoggedTasks(taskDB.getAllLoggedTasks(" ORDER BY tasks.task_name"));
			LoggedTask task;
			for(int i = 0; i < taskLog.getLogged_Tasks().size(); i++) {
				task = taskLog.getTask(i);
				Object[] dataPoint = {
					task.getLogID(),
					task.getTaskID(),
					task.getTaskName(),
					task.getUser1ID(),
					task.getUser1Name(),
					task.getUser2ID(),
					task.getUser2Name(),
					task.getDateCompleted()
				};
				tempData.add(dataPoint);
			}
			Object[][] data = new Object[tempData.size()][8];
			return tempData.toArray(data);
		} catch (SQLException e) {
			window.displayError("Table Error!", e.toString());
		} catch (NullPointerException e) {
			window.displayError("Table Error!", e.toString());
		}

		Object[][] data = {{}};
		return data;
	}
}
