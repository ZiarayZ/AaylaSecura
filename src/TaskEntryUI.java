import java.awt.BorderLayout;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Color;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;

import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.AncestorEvent;
import javax.swing.event.CaretListener;
import javax.swing.table.TableColumnModel;
import javax.swing.event.CaretEvent;

public class TaskEntryUI extends JPanel {

	private UserInterface window;
	private database db;
	private JTable taskListTable;
	private JTextField taskNameField;
	private JTextField timeCompletedField;
	private taskEntry myTE;
	private int sort;
	private int filter;
	private JPanel addTaskPanel;
	private JButton addButton1;
	private JButton addButton2;
	private JButton addButton3;
	private JTextField nameInput;
	private JCheckBox typeInput;
	private JComboBox durationInput;
	private JComboBox priorityInput;
	private JTextField frequncyInput;
	private JCheckBox needLoggingInput;
	private JTextField extraSignoffInput;//not right yet

	/**
	 * Create the panel.
	 */
	public TaskEntryUI(UserInterface UI, taskEntry myTE, database db) {// , UserManagement User, database db) {
		this.myTE = myTE;
		this.db = db;
		sort = 0;
		filter = 2;
		// sets window to have this contentPane
		window = UI;
		setBackground(new Color(255, 255, 255));
		setBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(119, 136, 153), new Color(192, 192, 192)));
		setLayout(new GridBagLayout());
		JPanel fixedPane = new JPanel();
		fixedPane.setLayout(null);
		fixedPane.setBackground(new Color(255, 255, 255));

		JLabel lblHeadingLabel = new JLabel("Please enter, edit or delete a task");
		lblHeadingLabel.setForeground(new Color(105, 105, 105));
		lblHeadingLabel.setOpaque(true);
		lblHeadingLabel.setFont(new Font("Verdana", Font.PLAIN, 28));
		lblHeadingLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblHeadingLabel.setBackground(new Color(224, 255, 255));
		lblHeadingLabel.setBounds(256, 55, 444, 67);
		fixedPane.add(lblHeadingLabel);

		JScrollPane scrollPane = updateTable(sort, filter);
		fixedPane.add(scrollPane);

		/*
		 * caretakerNameField = new JTextField();
		 * caretakerNameField.addActionListener(new ActionListener() { public void
		 * actionPerformed(ActionEvent e) { //record caretaker name here } });
		 * caretakerNameField.setBackground(new Color(238, 238, 238));
		 * caretakerNameField.setBounds(261, 427, 118, 20); add(caretakerNameField);
		 * caretakerNameField.setColumns(10);
		 * 
		 * JLabel lblNameLabel = new JLabel("Completed By"); lblNameLabel.setBounds(159,
		 * 430, 92, 14); add(lblNameLabel);
		 * 
		 * timeCompletedField = new JTextField();
		 * timeCompletedField.addActionListener(new ActionListener() { public void
		 * actionPerformed(ActionEvent e) { //record when the task was completed } });
		 * timeCompletedField.setBackground(new Color(238, 238, 238));
		 * timeCompletedField.setBounds(261, 473, 118, 20); add(timeCompletedField);
		 * timeCompletedField.setColumns(10);
		 * 
		 * JLabel lblTimeLabel = new JLabel("Time Completed");
		 * lblTimeLabel.setBounds(159, 476, 105, 14); add(lblTimeLabel);
		 * 
		 * JTextArea addCommentsField = new JTextArea(); //need to find out how to
		 * handle input in a text area addCommentsField.setBackground(new Color(238,
		 * 238, 238)); addCommentsField.setBounds(641, 425, 213, 68);
		 * add(addCommentsField);
		 * 
		 * JLabel lblNewLabel = new JLabel("Additional Comments");
		 * lblNewLabel.setBounds(493, 430, 138, 14); add(lblNewLabel);
		 */

		JButton sortByDateButton = new JButton("<html><center>Sort By<br>Date created</center></html>");
		sortByDateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sort = 0;
				fixedPane.remove(scrollPane);
				JScrollPane scrollPane = updateTable(sort, filter);
				fixedPane.add(scrollPane);
			}
		});
		sortByDateButton.setBounds(78, 553, 125, 54);
		fixedPane.add(sortByDateButton);

		JButton sortByPriorityButton = new JButton("<html><center>Sort By<br>Priority</center></html>");
		sortByPriorityButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sort = 1;
				fixedPane.remove(scrollPane);
				JScrollPane scrollPane = updateTable(sort, filter);
				fixedPane.add(scrollPane);
			}
		});
		sortByPriorityButton.setBounds(213, 553, 132, 54);
		fixedPane.add(sortByPriorityButton);

		JButton filterOneOffButton = new JButton("<html><center>One Off<br>Tasks</center></html>");
		filterOneOffButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				filter = 1;
				fixedPane.remove(scrollPane);
				JScrollPane scrollPane = updateTable(sort, filter);
				fixedPane.add(scrollPane);
			}
		});
		filterOneOffButton.setBounds(355, 553, 133, 54);
		fixedPane.add(filterOneOffButton);

		JButton filterRepeatButton = new JButton("<html><center>Repeat<br>Tasks</center></html>");
		filterRepeatButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				filter = 0;
				fixedPane.remove(scrollPane);
				JScrollPane scrollPane = updateTable(sort, filter);
				fixedPane.add(scrollPane);
			}
		});
		filterRepeatButton.setBounds(498, 553, 118, 54);
		fixedPane.add(filterRepeatButton);

		JButton filterBothButton = new JButton("<html><center>One Off and<br>Repeat Tasks</center></html>");
		filterBothButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				filter = 2;
				fixedPane.remove(scrollPane);
				JScrollPane scrollPane = updateTable(sort, filter);
				fixedPane.add(scrollPane);
			}
		});
		filterBothButton.setBounds(626, 553, 124, 54);
		fixedPane.add(filterBothButton);

		JButton createReportButton = new JButton("<html><center>Add New<br>Task</center></html>");
		createReportButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// generates the report
			}
		});
		createReportButton.setBounds(760, 553, 128, 54);
		fixedPane.add(createReportButton);
		
		
		
		setAddTaskPanel();
		JButton addTaskButton = new JButton("<html><center>Add New<br>Task</center></html>");
		addTaskButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addButton1.setEnabled(true);
				addButton2.setEnabled(true);
				JOptionPane.showConfirmDialog(null, addTaskPanel, "Create Report",
				JOptionPane.CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
			}
		});
		addTaskButton.setBounds(355, 607, 133, 54);
		fixedPane.add(addTaskButton);
		JButton editTaskButton = new JButton("<html><center>Edit<br>Task</center></html>");
		editTaskButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// edits the task
			}
		});
		editTaskButton.setBounds(498, 607, 118, 54);
		fixedPane.add(editTaskButton);
		fixedPane.setPreferredSize(new Dimension(956, 717));
		add(fixedPane);
	}

	public Object[][] populateTable(int sort, int filter) {
		ArrayList<Object[]> tempData = new ArrayList<Object[]>();

		// call for all tasks info
		ArrayList<task> tasks = new ArrayList<task>();
		switch (sort) {
		case 0:
			tasks = myTE.sortUndoneTasksByDate();
			break;
		case 1:
			tasks = myTE.sortUndoneTasksByPriority();
			break;
		}
		switch (filter) {
		case 0:
			tasks = myTE.filterToRepeatTasks(tasks);
			break;
		case 1:
			tasks = myTE.filterToOneOffTasks(tasks);
			break;
		case 2:
			;
			break;
		}

		task tempTask;
		try {
			for (int a = 0; a < tasks.size(); a++) {
				tempTask = tasks.get(a);
				Object[] dataPoint = { tempTask.getID(), tempTask.getName(), tempTask.getType(), tempTask.getDuration(),
						tempTask.getPriority(), tempTask.getFrequency(), tempTask.getNeedLogging(),
						tempTask.getDateCreated(), tempTask.getExtraSignOff() };
				tempData.add(dataPoint);
			}
			Object[][] data = new Object[tempData.size()][8];
			return tempData.toArray(data);
		} catch (NullPointerException e) {
			window.displayError("Table Error!", e.toString());
		}

		Object[][] data = { {} };
		return data;
	}

	private JScrollPane updateTable(int sort, int filter) {
		String[] colHeaders = { "Task ID", "Task Name", "Task Type", "Task Duration", "Task Priority", "Task Frequency",
				"need logging", "Date Created", "completed", "extra sign off" };
		Object[][] data = populateTable(sort, filter);
		taskListTable = new JTable(data, colHeaders);
		// this removes the id column, but you should be able to call
		// 'userTable.getModel().getValueAt(row, 0)' to get the id
		TableColumnModel tcm = taskListTable.getColumnModel();
		tcm.removeColumn(tcm.getColumn(0));// Task ID
		tcm.removeColumn(tcm.getColumn(4));// Frequency
		tcm.removeColumn(tcm.getColumn(4));// Need Logging
		tcm.removeColumn(tcm.getColumn(5));// Completed
		tcm.removeColumn(tcm.getColumn(5));// Extra Sign off
		// import table into a scroll pane so that the table headers are visible and
		// other things
		JScrollPane scrollPane = new JScrollPane(taskListTable);
		scrollPane.setBackground(new Color(238, 238, 238));
		scrollPane.setBounds(68, 150, 820, 232);
		return scrollPane;
	}

	private JPanel addTask() {
		/*
		 * JPanel entryBox = new JPanel(); System.out.println("entryBox"); taskNameField
		 * = new JTextField(); taskNameField.addActionListener(new ActionListener() {
		 * public void actionPerformed(ActionEvent e) { record caretaker name here } });
		 * taskNameField.setBackground(new Color(238, 238, 238));
		 * taskNameField.setBounds(261, 427, 118, 20); add(taskNameField);
		 * taskNameField.setColumns(10); entryBox.add(taskNameField); JLabel
		 * lblNameLabel = new JLabel("Completed By"); lblNameLabel.setBounds(159, 430,
		 * 92, 14); add(lblNameLabel);
		 * 
		 * timeCompletedField = new JTextField();
		 * timeCompletedField.addActionListener(new ActionListener() { public void
		 * actionPerformed(ActionEvent e) { record when the task was completed } });
		 * timeCompletedField.setBackground(new Color(238, 238, 238));
		 * timeCompletedField.setBounds(261, 473, 118, 20); add(timeCompletedField);
		 * timeCompletedField.setColumns(10);
		 * 
		 * JLabel lblTimeLabel = new JLabel("Time Completed");
		 * lblTimeLabel.setBounds(159, 476, 105, 14); add(lblTimeLabel);
		 * 
		 * JTextArea addCommentsField = new JTextArea(); //need to find out how to
		 * handle input in a text area addCommentsField.setBackground(new Color(238,
		 * 238, 238)); addCommentsField.setBounds(641, 425, 213, 68);
		 * add(addCommentsField);
		 * 
		 * JLabel lblNewLabel = new JLabel("Additional Comments");
		 * lblNewLabel.setBounds(493, 430, 138, 14); add(lblNewLabel);
		 * 
		 * 
		 * JScrollPane scrollPane = new JScrollPane(entryBox);
		 */
		JPanel panel = new JPanel();
				
		JTextField txtName = new JTextField();
		
		txtName.setText("name");
		panel.add(txtName);
		txtName.setColumns(10);
		
		JTextField textField_1 = new JTextField();
		panel.add(textField_1);
		textField_1.setColumns(10);
		
		JTextField textField = new JTextField();
		panel.add(textField);
		textField.setColumns(10);
		panel.setBackground(new Color(238, 238, 238));
		panel.setBounds(68, 150, 820, 232);
		return panel;
	}
	
	private void setAddTaskPanel() {
		addTaskPanel = new JPanel();
		JLabel message = new JLabel("<html><br>Enter Details<br><br></html>");
		message.setHorizontalAlignment(SwingConstants.CENTER);
		addTaskPanel.add(message);
		
		//Inputcreation
		
		nameInput = new JTextField();
		addTaskPanel.add(nameInput);
		nameInput.setColumns(10);
		JLabel nameLabel = new JLabel("-Name");
		addTaskPanel.add(nameLabel);
		
		typeInput = new JCheckBox("-Reccurring");
		addTaskPanel.add(typeInput);
		
		durationInput = new JComboBox();
		durationInput.setModel(new DefaultComboBoxModel(new String[] {"5", "10", "15","20","25","30","45","60"}));
		addTaskPanel.add(durationInput);
		JLabel durationLabel = new JLabel("-Duration");
		addTaskPanel.add(durationLabel);
		
		priorityInput = new JComboBox();
		priorityInput.setModel(new DefaultComboBoxModel(new String[] {"1", "2", "3"}));
		addTaskPanel.add(priorityInput);
		JLabel priorityLabel = new JLabel("-Priority");
		addTaskPanel.add(priorityLabel);
		
		
		frequncyInput = new JTextField();
		addTaskPanel.add(frequncyInput);
		frequncyInput.setColumns(5);
		JLabel freqLabel = new JLabel("-Frequency (Days)");
		addTaskPanel.add(freqLabel);
		
		needLoggingInput = new JCheckBox("-Need Logging");
		addTaskPanel.add(needLoggingInput);
		
		extraSignoffInput = new JTextField();
		addTaskPanel.add(extraSignoffInput);
		extraSignoffInput.setColumns(5);
		JLabel eSLabel = new JLabel("-Extra Signoff?");
		addTaskPanel.add(eSLabel);
		
		
		//button creation
		addButton1 = new JButton("Confirm");
		addButton1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				
			}
		});
		addButton2 = new JButton("Cancel");
		addButton2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				
			}
		});
		addTaskPanel.add(addButton1);
		//saddTaskPanel.add(addButton2);
	}
}
