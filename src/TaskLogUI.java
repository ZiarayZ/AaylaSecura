import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.Font;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TaskLogUI extends JPanel {
	private UserInterface window;
	private UserManagement user;
	private JTable taskListTable;
	private JTable logTaskListTable;
	private JComboBox<EditUser> caretakerNameField;
	private JTextField timeCompletedField;
	private LogTasks taskLog;
	private JPanel reportPanel;
	private CreateReports reportCreation;
	private JButton reportButt1;
	private JButton reportButt2;
	private JButton reportButt3;

	/**
	 * Create the panel.
	 */
	public TaskLogUI(UserInterface UI, LogTasks loggingTask, UserManagement User, CreateReports Report) {
		taskLog = loggingTask;
		reportCreation = Report;
		user = User;
		//define colors
		final Color red = new Color(236, 28, 36);
		final Color green = new Color(94, 186, 125);
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

		//create tasks table
		String[] colHeaders = {"Task ID", "Task Name", "Task Priority",
								"Completed", "Deadline", "Extra Sign Off", "Second Sign Off", "Assigned ID", "Assigned Caretaker"};
		Object[][] data = populateTaskTable();
		TableModel tableModel = new DefaultTableModel(colHeaders, 0);
		taskListTable = new JTable(tableModel);
		TableRowSorter<TableModel> tableSorter = new TableRowSorter<TableModel>(taskListTable.getModel());
		taskListTable.setRowSorter(tableSorter);
		DefaultTableModel DTM = (DefaultTableModel) taskListTable.getModel();
		for (int i = 0; i < data.length; i++) {
			DTM.addRow(data[i]);
		}
		//creating new log tasks panel
		JPanel logTasks = new JPanel();
		JPanel fixedLogTasks = new JPanel();
		logTasks.setBackground(new Color(255, 255, 255));
		logTasks.setBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(119, 136, 153), new Color(192, 192, 192)));
		logTasks.setLayout(new GridBagLayout());
		fixedLogTasks.setLayout(null);
		fixedLogTasks.setBackground(new Color(255, 255, 255));
		createLoggedTasksPanel(fixedLogTasks);

		caretakerNameField = new JComboBox<EditUser>();
		try {
			ResultSet allUsers = user.getUsers();
			caretakerNameField.addItem(null);
			while (allUsers.next()) {
				caretakerNameField.addItem(new EditUser(allUsers.getInt(1), allUsers.getString(2)));
			}
		} catch (SQLException e) {
			window.displayError("Database Error!", e.toString());
		} catch (NullPointerException e) {
			window.displayError("Database Error!", e.toString());
		}
		caretakerNameField.setBackground(new Color(238, 238, 238));
		caretakerNameField.setBounds(261, 427, 118, 20);
		fixedPane.add(caretakerNameField);
		
		JLabel lblNameLabel = new JLabel("Completed By");
		lblNameLabel.setBounds(159, 430, 92, 14);
		fixedPane.add(lblNameLabel);
		JLabel lblExample = new JLabel("Example Format:");
		lblExample.setBounds(159, 453, 250, 20);
		fixedPane.add(lblExample);
		JLabel lblformat = new JLabel("HH:mm:ss");
		lblformat.setBounds(261, 453, 250, 20);
		fixedPane.add(lblformat);
		timeCompletedField = new JTextField();
		timeCompletedField.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent event) {
				//Required method
			}
			public void keyPressed(KeyEvent event) {
				//Required method
			}
			//once a key is pressed then released will test the password's strength
			public void keyReleased(KeyEvent event) {
				//validate date time from DB method
				boolean valid = false;
				try {
					//dummy date for checking correct time
					LocalDate.parse("2000-06-10 " + timeCompletedField.getText(), DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss").withResolverStyle(ResolverStyle.STRICT));
					valid = true;
				} catch (DateTimeParseException e) {
					valid = false;
				}
				if (valid) {
					timeCompletedField.setForeground(green);
				} else {
					timeCompletedField.setForeground(red);
				}
			}
		});
		timeCompletedField.setBackground(new Color(238, 238, 238));
		timeCompletedField.setBounds(261, 473, 118, 20);
		fixedPane.add(timeCompletedField);
		timeCompletedField.setColumns(10);
		
		JLabel lblTimeLabel = new JLabel("Time Completed");
		lblTimeLabel.setBounds(159, 476, 105, 14);
		fixedPane.add(lblTimeLabel);
		
		JButton sortByCaretakerButton = new JButton("<html><center>Sort By<br>Caretaker</center></html>");
		sortByCaretakerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refreshTaskTable();
				List<RowSorter.SortKey> sortKeys = new ArrayList<>(25);
				sortKeys.add(new RowSorter.SortKey(8, SortOrder.ASCENDING));//sort by user 1
				sortKeys.add(new RowSorter.SortKey(5, SortOrder.ASCENDING));//sort by user 2 after user 1
				sortKeys.add(new RowSorter.SortKey(4, SortOrder.ASCENDING));//sort by date completed after user 2 after user 1
				tableSorter.setSortKeys(sortKeys);
			}
		});
		sortByCaretakerButton.setBounds(78, 553, 125, 54);
		fixedPane.add(sortByCaretakerButton);
		
		JButton sortByDeadlineButton = new JButton("<html><center>Sort By<br>Deadline</center></html>");
		sortByDeadlineButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refreshTaskTable();
				List<RowSorter.SortKey> sortKeys = new ArrayList<>(25);
				sortKeys.add(new RowSorter.SortKey(4, SortOrder.ASCENDING));
				sortKeys.add(new RowSorter.SortKey(2, SortOrder.ASCENDING));
				tableSorter.setSortKeys(sortKeys);
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
			public void actionPerformed(ActionEvent event) {
				//logs the task
				if (taskListTable.getSelectedRow() != -1) {
					int taskID = (int) taskListTable.getModel().getValueAt(taskListTable.getSelectedRow(), 0);
					int userID = 0;
					int secondID = 0;
					//no extra sign off
					if ((caretakerNameField.getSelectedItem().toString().equals("")) && (taskListTable.getModel().getValueAt(taskListTable.getSelectedRow(), 6).toString().equals(null) || taskListTable.getModel().getValueAt(taskListTable.getSelectedRow(), 6).toString().equals(""))) {
						if ((int) taskListTable.getModel().getValueAt(taskListTable.getSelectedRow(), 7) != 0 && (int) taskListTable.getModel().getValueAt(taskListTable.getSelectedRow(), 7) == user.getID()) {
							userID = user.getID();
						}
					//extra sign off no assigned caretaker
					} else if (!(caretakerNameField.getSelectedItem().toString().equals("")) && (taskListTable.getModel().getValueAt(taskListTable.getSelectedRow(), 8).toString().equals(null) || taskListTable.getModel().getValueAt(taskListTable.getSelectedRow(), 8).toString().equals(""))) {
						userID = user.getID();
					} else if (!(caretakerNameField.getSelectedItem().toString().equals("")) && (taskListTable.getModel().getValueAt(taskListTable.getSelectedRow(), 6).toString().equals(null) || taskListTable.getModel().getValueAt(taskListTable.getSelectedRow(), 6).toString().equals(""))) {
						userID = ((EditUser) caretakerNameField.getSelectedItem()).getIntID();
						secondID = user.getID();
					}
					boolean valid = false;
					try {
						//dummy date for checking correct time
						LocalDate.parse("2000-06-10 " + timeCompletedField.getText(), DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss").withResolverStyle(ResolverStyle.STRICT));
						valid = true;
					} catch (DateTimeParseException e) {
						valid = false;
					}
					if (valid) {
						if (userID != 0) {
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
							String date = sdf.format(new Date())+" "+timeCompletedField.getText();
							boolean result = false;
							try {
								result = loggingTask.addLoggedTask(userID, secondID, taskID, date);
							} catch (SQLException e) {
								window.displayError("Database Error!", e.toString());
							} catch (NullPointerException e) {
								window.displayError("Database Error!", e.toString());
							}
							window.displayError("Log Task Attempt.", "Logging Task: " + result);
						} else {
							
						}
					} else {
						window.displayError("Log Task Failed.", "Time Format is Incorrect.");
					}
				}
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
		
		setReportPanel();
		JButton createReportButton = new JButton("<html><center>Create<br>Report</center></html>");
		createReportButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//restrict report buttons to Create Report access level
				if (user.accessLevel("CR") >= 2 || user.accessLevel("AP") == 1) {
					reportButt1.setEnabled(true);
					reportButt1.setEnabled(true);
					reportButt1.setEnabled(true);
				} else if (user.accessLevel("CR") == 1) {
					reportButt1.setEnabled(true);
					reportButt2.setEnabled(true);
					reportButt3.setEnabled(false);
				} else {
					reportButt1.setEnabled(false);
					reportButt2.setEnabled(false);
					reportButt3.setEnabled(false);
				}
				JOptionPane.showConfirmDialog(null, reportPanel, "Create Report",
				JOptionPane.CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
			}
		});
		createReportButton.setBounds(760, 553, 128, 54);
		fixedPane.add(createReportButton);

		//set table listener
		ListSelectionModel selectionModel = taskListTable.getSelectionModel();
		selectionModel.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				//when selecting a row in the logging task table
				if (taskListTable.getSelectedRow() != -1) {
					if (user.accessLevel("LT") >= 1 || user.accessLevel("AP") == 1) {
						logTaskButton.setEnabled(true);
						editCompletedButton.setEnabled(true);
					} else {
						editCompletedButton.setEnabled(false);
						logTaskButton.setEnabled(false);
					}
				} else {
					if (user.accessLevel("LT") >= 1 || user.accessLevel("AP") == 1) {
						logTaskButton.setEnabled(true);
					} else {
						logTaskButton.setEnabled(false);
					}
					editCompletedButton.setEnabled(false);
				}
			}
		});
		//this removes the id column, but you should be able to call 'taskListTable.getModel().getValueAt(row, 0)' to get the id
		//task_id, task_name, priority, date_created, completed, extra_sign_off, user1.user_name, assigned_caretaker, user2.user_name
		TableColumnModel tcm = taskListTable.getColumnModel();
		tcm.removeColumn(tcm.getColumn(0));//Task ID: 0
		tcm.removeColumn(tcm.getColumn(4));//Extra Sign Off: 5
		tcm.removeColumn(tcm.getColumn(5));//Extra Sign Off: 7

		//import table into a scroll pane so that the table headers are visible and other things
		JScrollPane taskListPane = new JScrollPane(taskListTable);
		taskListPane.setBackground(new Color(238, 238, 238));
		taskListPane.setBounds(68, 150, 820, 232);
		fixedPane.add(taskListPane);

		//set pane size and add to main pane
		fixedPane.setPreferredSize(new Dimension(956,717));
		add(fixedPane);
	}
	private void createLoggedTasksPanel(JPanel fixedLogTasks) {
		//create main label
		JLabel lblHeadingLabel = new JLabel("Please Pick a Completed Task");
		lblHeadingLabel.setForeground(new Color(105, 105, 105));
		lblHeadingLabel.setOpaque(true);
		lblHeadingLabel.setFont(new Font("Verdana", Font.PLAIN, 28));
		lblHeadingLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblHeadingLabel.setBackground(new Color(224, 255, 255));
		lblHeadingLabel.setBounds(256, 55, 444, 67);
		fixedLogTasks.add(lblHeadingLabel);

		//create log tasks table
		String[] newColHeaders = {"Logged Task ID", "Task ID", "Task Name", "First User ID", "First User", "Second User ID", "Second User", "Date Completed"};
		Object[][] newData = populateLogTaskTable();
		TableModel newTableModel = new DefaultTableModel(newColHeaders, 0);
		logTaskListTable = new JTable(newTableModel);
		TableRowSorter<TableModel> newTableSorter = new TableRowSorter<TableModel>(logTaskListTable.getModel());
		logTaskListTable.setRowSorter(newTableSorter);
		DefaultTableModel newDTM = (DefaultTableModel) logTaskListTable.getModel();
		for (int i = 0; i < newData.length; i++) {
			newDTM.addRow(newData[i]);
		}
		//set table listener
		ListSelectionModel selectionModel = taskListTable.getSelectionModel();
		selectionModel.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				//when selecting a row in the logging task table
			}
		});

		TableColumnModel newtcm = logTaskListTable.getColumnModel();
		newtcm.removeColumn(newtcm.getColumn(0));//Logged Task ID: 0
		newtcm.removeColumn(newtcm.getColumn(0));//Task ID: 1
		newtcm.removeColumn(newtcm.getColumn(1));//First User ID: 3
		newtcm.removeColumn(newtcm.getColumn(2));//Second User ID: 5
		//import table into a scroll pane so that the table headers are visible and other things
		JScrollPane taskListPane = new JScrollPane(taskListTable);
		taskListPane.setBackground(new Color(238, 238, 238));
		taskListPane.setBounds(68, 150, 820, 232);
		fixedLogTasks.add(taskListPane);
	}

	public void refreshLogTaskTable() {
		DefaultTableModel newDTM = (DefaultTableModel) logTaskListTable.getModel();
		newDTM.setRowCount(0);
		Object[][] newData = populateLogTaskTable();
		for (int i = 0; i < newData.length; i++) {
			newDTM.addRow(newData[i]);
		}
	}
	public void refreshTaskTable() {
		DefaultTableModel DTM = (DefaultTableModel) taskListTable.getModel();
		DTM.setRowCount(0);
		Object[][] data = populateTaskTable();
		for (int i = 0; i < data.length; i++) {
			DTM.addRow(data[i]);
		}
	}
	private Object[][] populateLogTaskTable() {
		ArrayList<Object[]> tempData = new ArrayList<Object[]>();

		//call for all logged tasks info
		try {
			taskLog.reloadLoggedTasks(" ORDER BY tasks.task_name");
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
	private Object[][] populateTaskTable() {
		ArrayList<Object[]> tempData = new ArrayList<Object[]>();

		//call for all logged tasks info
		try {
			ResultSet rs = taskLog.reloadTasks();
			while(rs.next()) {
				boolean completed;
				if (rs.getInt(9)==0) {
					completed = false;
				} else {
					completed = true;
				}
				String deadline = "";
				try {
					//parsing to datetime, adding a week to be the deadline as there is no deadline to grab from tasks table...
					deadline = LocalDateTime.parse(rs.getString(4), DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss").withResolverStyle(ResolverStyle.STRICT)).plusWeeks(1).format(DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss").withResolverStyle(ResolverStyle.STRICT));
				} catch (DateTimeParseException e) {
					System.out.println(e);
				}
				Object[] dataPoint = {//task_id, task_name, priority, date_created, completed, extra_sign_off, user1.user_name, assigned_caretaker, user2.user_name
						rs.getInt(1), rs.getString(2), rs.getInt(3),
						completed, deadline,
						rs.getInt(6), rs.getString(7),
						rs.getInt(8), rs.getString(9)
				};
				tempData.add(dataPoint);
			}
			Object[][] data = new Object[tempData.size()][9];
			return tempData.toArray(data);
		}
		catch(SQLException e) {
			return null;
		} catch (NullPointerException e) {
			window.displayError("Table Error!", e.toString());
		}

		Object[][] data = { {} };
		return data;
	}

	private void setReportPanel() {
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		reportPanel = new JPanel(gbl);
		//label spanning whole panel width
		JLabel message = new JLabel("<html><br>Choose a Report to Create.<br><br></html>");
		message.setHorizontalAlignment(SwingConstants.CENTER);
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.gridheight = 1;
		gbc.gridwidth = 3;
		gbc.weighty = 1;
		gbc.weightx = 1;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbl.setConstraints(message, gbc);
		reportPanel.add(message);
		//button creation
		reportButt1 = new JButton("Create Task Status Report");
		reportButt1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				try {
					reportCreation.createTaskStatusReport();
				} catch (IOException e) {
					window.displayError("Report Error!", e.toString());
				}
			}
		});
		reportButt2 = new JButton("Create Completed Task Report");
		reportButt2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				try {
					reportCreation.createCompletedTaskReport();
				} catch (IOException e) {
					window.displayError("Report Error!", e.toString());
				}
			}
		});
		reportButt3 = new JButton("Create Caretaker Report");
		reportButt3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				try {
					reportCreation.createCaretakerReport();
				} catch (IOException e) {
					window.displayError("Report Error!", e.toString());
				}
			}
		});
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		gbc.weighty = 1;
		gbc.weightx = 1;
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbl.setConstraints(reportButt1, gbc);
		reportPanel.add(reportButt1);
		gbc.gridx = 1;
		gbl.setConstraints(reportButt2, gbc);
		reportPanel.add(reportButt2);
		gbc.gridx = 2;
		gbl.setConstraints(reportButt3, gbc);
		reportPanel.add(reportButt3);
	}
}
