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
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
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
	private JPanel editTaskPanel;
	private JPanel feedbackPanel;
	private JButton addButton1;
	private JButton editButton1;
	private JButton editButton2;
	private JTextField nameInput;
	private JCheckBox typeInput;
	private JTextField durationInput;
	private JComboBox priorityInput;
	private JTextField frequencyInput;
	private JCheckBox needLoggingInput;
	private JComboBox extraSignoffInput;//not right yet

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

		JLabel lblHeadingLabel = new JLabel("Task Entry");
		lblHeadingLabel.setForeground(new Color(105, 105, 105));
		lblHeadingLabel.setOpaque(true);
		lblHeadingLabel.setFont(new Font("Verdana", Font.PLAIN, 28));
		lblHeadingLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblHeadingLabel.setBackground(new Color(224, 255, 255));
		lblHeadingLabel.setBounds(256, 55, 444, 67);
		fixedPane.add(lblHeadingLabel);

		JScrollPane scrollPane = createTable(sort, filter);
		fixedPane.add(scrollPane);

		JButton sortByDateButton = new JButton("<html><center>Sort By<br>Date created</center></html>");
		sortByDateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sort = 0;
				refreshTable(sort, filter);
			}
		});
		sortByDateButton.setBounds(78, 553, 125, 54);
		fixedPane.add(sortByDateButton);

		JButton sortByPriorityButton = new JButton("<html><center>Sort By<br>Priority</center></html>");
		sortByPriorityButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sort = 1;
				refreshTable(sort, filter);
			}
		});
		sortByPriorityButton.setBounds(213, 553, 132, 54);
		fixedPane.add(sortByPriorityButton);

		JButton filterOneOffButton = new JButton("<html><center>One Off<br>Tasks</center></html>");
		filterOneOffButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				filter = 1;
				refreshTable(sort, filter);
			}
		});
		filterOneOffButton.setBounds(355, 553, 133, 54);
		fixedPane.add(filterOneOffButton);

		JButton filterRepeatButton = new JButton("<html><center>Repeat<br>Tasks</center></html>");
		filterRepeatButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				filter = 0;
				refreshTable(sort, filter);
			}
		});
		filterRepeatButton.setBounds(498, 553, 118, 54);
		fixedPane.add(filterRepeatButton);

		JButton filterBothButton = new JButton("<html><center>One Off and<br>Repeat Tasks</center></html>");
		filterBothButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				filter = 2;
				refreshTable(sort, filter);
			}
		});
		filterBothButton.setBounds(626, 553, 124, 54);
		fixedPane.add(filterBothButton);

		JButton RefreshTableButton = new JButton("<html><center>Refresh<br>Table</center></html>");
		RefreshTableButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refreshTable(sort, filter);
			}
		});
		RefreshTableButton.setBounds(760, 553, 128, 54);
		fixedPane.add(RefreshTableButton);
		
		
		
		
		JButton addTaskButton = new JButton("<html><center>Add New<br>Task</center></html>");
		addTaskButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setAddTaskPanel(sort,filter);
				addButton1.setEnabled(true);
				JOptionPane.showConfirmDialog(null, addTaskPanel, "Create Report", JOptionPane.CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
			}
		});
		addTaskButton.setBounds(355, 488, 133, 54);
		fixedPane.add(addTaskButton);
		JButton editTaskButton = new JButton("<html><center>Edit<br>Task</center></html>");
		editTaskButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int row = taskListTable.getSelectedRow();
				if(row==-1) {
					setFeedbackPanel("Please select a task to edit");
					JOptionPane.showConfirmDialog(null, feedbackPanel, "Message", JOptionPane.PLAIN_MESSAGE);}
				else{
					int task_id = (int)taskListTable.getModel().getValueAt(row, 0);
					setEditTaskPanel(task_id,sort,filter);
					editButton1.setEnabled(true);
					JOptionPane.showConfirmDialog(null, editTaskPanel, "Create Report", JOptionPane.CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
				}
			}
		});
		editTaskButton.setBounds(498, 488, 118, 54);
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
				String type;
				if(tempTask.getType()==1) {type="One-Off";}
				else {type="Recurring";}
				Object[] dataPoint = { tempTask.getID(), tempTask.getName(), type, tempTask.getDuration(),
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

	private JScrollPane createTable(int sort, int filter) {
		String[] colHeaders = { "Task ID", "Task Name", "Task Type", "Task Duration", "Task Priority", "Task Frequency",
				"need logging", "Date Created", "completed", "extra sign off" };
		Object[][] data = populateTable(sort, filter);
		TableModel tableModel = new DefaultTableModel(data, colHeaders);
		taskListTable = new JTable(tableModel);
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
	
	private void setAddTaskPanel(int sort, int filter) {
		addTaskPanel = new JPanel();
		JLabel message = new JLabel("<html><br>Enter Details:<br><br></html>");
		message.setHorizontalAlignment(SwingConstants.CENTER);
		addTaskPanel.add(message);
		
		//Inputcreation
		
		nameInput = new JTextField();
		addTaskPanel.add(nameInput);
		nameInput.setColumns(20);
		JLabel nameLabel = new JLabel("-Name");
		addTaskPanel.add(nameLabel);
		
		typeInput = new JCheckBox("-Reccurring");
		addTaskPanel.add(typeInput);
		
		durationInput = new JTextField();
		addTaskPanel.add(durationInput);
		durationInput.setColumns(5);
		JLabel durationLabel = new JLabel("-Duration(Mins)");
		addTaskPanel.add(durationLabel);
		
		priorityInput = new JComboBox();
		priorityInput.setModel(new DefaultComboBoxModel(new String[] {"1", "2", "3"}));
		addTaskPanel.add(priorityInput);
		JLabel priorityLabel = new JLabel("-Priority");
		addTaskPanel.add(priorityLabel);
		
		
		frequencyInput = new JTextField();
		addTaskPanel.add(frequencyInput);
		frequencyInput.setColumns(5);
		JLabel freqLabel = new JLabel("-Frequency (Days)");
		addTaskPanel.add(freqLabel);
		
		needLoggingInput = new JCheckBox("-Need Logging");
		addTaskPanel.add(needLoggingInput);
		
		String[] caretakerNames = genCNames();
		extraSignoffInput = new JComboBox();
		extraSignoffInput.setModel(new DefaultComboBoxModel(caretakerNames));
		addTaskPanel.add(extraSignoffInput);
		
		JLabel eSLabel = new JLabel("-Extra Signoff?");
		addTaskPanel.add(eSLabel);
		
		
		//button creation
		addButton1 = new JButton("Confirm");
		addButton1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				String errorMessages = "";
				String name = nameInput.getText();
				int type;
				if(typeInput.isSelected()) {type=0;}
				else {type=1;}
				int duration;
				if(durationInput.getText().equals("")) {duration = 0;}
				else{duration = Integer.parseInt(durationInput.getText());}
				int priority = Integer.parseInt((String)priorityInput.getSelectedItem());
				int frequency;
				if(frequencyInput.getText().equals("")) {frequency = 0;}
				else {frequency = Integer.parseInt(frequencyInput.getText());}
				int need_logging;
				if(needLoggingInput.isSelected()) {need_logging=1;}
				else {need_logging=0;}
				String date_created = "1999-03-27 15:07:43";//get date
				int completed = 0;
				int extra_sign_off;
				int[] caretakerIDs = genCIDs();
				int chosenES = extraSignoffInput.getSelectedIndex();
				if(chosenES==0) {
					extra_sign_off=0;
				}
				else {extra_sign_off=caretakerIDs[chosenES-1];}
				try {
					errorMessages = myTE.addTask(name, type, duration, priority, frequency, need_logging, date_created, completed, extra_sign_off);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				if(!errorMessages.equals("")) {
					setFeedbackPanel(errorMessages);
					JOptionPane.showConfirmDialog(null, feedbackPanel, "Feedback", JOptionPane.PLAIN_MESSAGE);
				}
				else {
					setFeedbackPanel("Creation successful");
					JOptionPane.showConfirmDialog(null, feedbackPanel, "Feedback", JOptionPane.PLAIN_MESSAGE);
				}
				refreshTable(sort,filter);
			}
		});
		
		addTaskPanel.add(addButton1);
		
	}
	
	private void setEditTaskPanel(int task_id,int sort,int filter) {
		task currentTask = myTE.getTask(task_id);
		
		editTaskPanel = new JPanel();
		JLabel message = new JLabel("<html><br>Enter Details:<br><br></html>");
		message.setHorizontalAlignment(SwingConstants.CENTER);
		editTaskPanel.add(message);
		
		//Input creation
		
		nameInput = new JTextField();
		editTaskPanel.add(nameInput);
		nameInput.setColumns(20);
		nameInput.setText(currentTask.getName());
		JLabel nameLabel = new JLabel("-Name");
		editTaskPanel.add(nameLabel);
		
		typeInput = new JCheckBox("-Reccurring");
		editTaskPanel.add(typeInput);
		if(currentTask.getType()==0) {
			typeInput.setSelected(true);
		}
		else{typeInput.setSelected(false);}
		
		durationInput = new JTextField();
		editTaskPanel.add(durationInput);
		durationInput.setColumns(5);
		durationInput.setText(String.valueOf(currentTask.getDuration()));
		JLabel durationLabel = new JLabel("-Duration(Mins)");
		editTaskPanel.add(durationLabel);
		
		priorityInput = new JComboBox();
		priorityInput.setModel(new DefaultComboBoxModel(new String[] {"1", "2", "3"}));
		editTaskPanel.add(priorityInput);
		priorityInput.setSelectedItem(String.valueOf(currentTask.getPriority()));
		JLabel priorityLabel = new JLabel("-Priority");
		editTaskPanel.add(priorityLabel);
		
		
		frequencyInput = new JTextField();
		editTaskPanel.add(frequencyInput);
		frequencyInput.setColumns(5);
		frequencyInput.setText(String.valueOf(currentTask.getFrequency()));
		JLabel freqLabel = new JLabel("-Frequency (Days)");
		editTaskPanel.add(freqLabel);
		
		needLoggingInput = new JCheckBox("-Need Logging");
		editTaskPanel.add(needLoggingInput);
		if(currentTask.getNeedLogging()==1) {
			needLoggingInput.setSelected(true);
		}
		else{needLoggingInput.setSelected(false);}
		
		String[] caretakerNames = genCNames();
		extraSignoffInput = new JComboBox();
		extraSignoffInput.setModel(new DefaultComboBoxModel(caretakerNames));
		editTaskPanel.add(extraSignoffInput);
		String es;
		if(currentTask.getExtraSignOff()==0) {es="0";}
		else {es = myTE.getcaretakerNameFromID(currentTask.getExtraSignOff());}
		JLabel eSLabel = new JLabel("-Extra Signoff?");
		editTaskPanel.add(eSLabel);
		
		
		//button creation
		editButton1 = new JButton("Confirm");
		editButton1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				String errorMessages = "";
				String name = nameInput.getText();
				int type;
				if(typeInput.isSelected()) {type=0;}
				else {type=1;}
				int duration;
				if(durationInput.getText().equals("")) {duration = 0;}
				else{duration = Integer.parseInt(durationInput.getText());}
				int priority = Integer.parseInt((String)priorityInput.getSelectedItem());
				int frequency;
				if(frequencyInput.getText().equals("")) {frequency = 0;}
				else {frequency = Integer.parseInt(frequencyInput.getText());}
				int need_logging;
				if(needLoggingInput.isSelected()) {need_logging=1;}
				else {need_logging=0;}
				String date_created = currentTask.getDateCreated();//get date
				int completed = 0;
				int[] caretakerIDs = genCIDs();
				int extra_sign_off;
				int chosenES = extraSignoffInput.getSelectedIndex();
				if(chosenES==0) {
					extra_sign_off=0;
				}
				else {extra_sign_off=caretakerIDs[chosenES-1];}
				try {
					errorMessages = myTE.editTask(task_id, name, type, duration, priority, frequency, need_logging, date_created, completed, extra_sign_off);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				if(!errorMessages.equals("")) {
					setFeedbackPanel(errorMessages);
					JOptionPane.showConfirmDialog(null, feedbackPanel, "Feedback", JOptionPane.PLAIN_MESSAGE);
				}
				else {
					setFeedbackPanel("Edit successful");
					JOptionPane.showConfirmDialog(null, feedbackPanel, "Feedback", JOptionPane.PLAIN_MESSAGE);
				}
				refreshTable(sort,filter);
			}
		});
		
		editTaskPanel.add(editButton1);
		
		editButton2 = new JButton("Delete");
		editButton2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				refreshTable(sort,filter);
			}
		});
		
		editTaskPanel.add(editButton2);
	}
	
	private void setFeedbackPanel(String feedback) {
		feedbackPanel = new JPanel();
		
		JLabel feedbackLabel = new JLabel(feedback);
		feedbackPanel.add(feedbackLabel);
		
	}
	
	
	public void refreshTable(int sort, int filter) {
		DefaultTableModel DTM = (DefaultTableModel) taskListTable.getModel();
		DTM.setRowCount(0);
		Object[][] data = populateTable(sort,filter);
		for (int i = 0; i < data.length; i++) {
			DTM.addRow(data[i]);
		}
	}
	
	public String[] genCNames() {
		ArrayList<String> caretakerNamesArrayList = myTE.getAllCaretakersNames();
		String[] caretakerNames = new String[caretakerNamesArrayList.size()+1];
		caretakerNames[0]="None";
		for(int a=0;a<caretakerNamesArrayList.size();a++) {
			caretakerNames[a+1]=caretakerNamesArrayList.get(a);
		}
		return caretakerNames;
	}
	
	public int[] genCIDs() {
		ArrayList<Integer> caretakerIDsArrayList = myTE.getAllCaretakersIDs();
		int[] caretakerIDs = new int[caretakerIDsArrayList.size()];
		for(int a=0;a<caretakerIDsArrayList.size();a++) {
			caretakerIDs[a]=caretakerIDsArrayList.get(a);
		}
		return caretakerIDs;
	}
}
