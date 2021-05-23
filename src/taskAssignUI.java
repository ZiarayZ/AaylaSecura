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

public class taskAssignUI extends JPanel {

	private UserInterface window;
	private database db;
	private JTable taskListTable;
	private JTextField taskNameField;
	private JTextField timeCompletedField;
	private taskAssign myTA;
	private int sort;
	private int filter;
	private JPanel addTaskPanel;
	private JPanel editTaskPanel;
	private JPanel feedbackPanel;
	private JButton addButton1;
	private JButton editButton1;
	private JButton editButton2;
	private JComboBox caretakerInput;

	/**
	 * Create the panel.
	 */
	public taskAssignUI(UserInterface UI, taskAssign myTA, database db) {// , UserManagement User, database db) {
		this.myTA = myTA;
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

		JLabel lblHeadingLabel = new JLabel("Task Allocation");
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
		
		JButton sortByUnassignedButton = new JButton("<html><center>Sort by<br>Unassigned</center></html>");
		sortByUnassignedButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sort = 2;
				refreshTable(sort, filter);
			}
		});
		sortByUnassignedButton.setBounds(78, 618, 125, 54);
		fixedPane.add(sortByUnassignedButton);
		
		JButton sortByAssignedButton = new JButton("<html><center>Sort by<br>Assigned</center></html>");
		sortByAssignedButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sort = 3;
				refreshTable(sort, filter);
			}
		});
		sortByAssignedButton.setBounds(213, 618, 132, 54);
		fixedPane.add(sortByAssignedButton);
		
		
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
		
		JButton addTaskButton = new JButton("<html><center>Sort by<br></center></html>");
		addTaskButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				;
			}
		});
		addTaskButton.setBounds(355, 618, 133, 54);
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
	
	
	private void setEditTaskPanel(int task_id,int sort,int filter) {
		task currentTask = myTA.getTask(task_id);
		
		editTaskPanel = new JPanel();
		JLabel message = new JLabel("<html><br>Enter Details:<br><br></html>");
		message.setHorizontalAlignment(SwingConstants.CENTER);
		editTaskPanel.add(message);
		
		//Input creation
		
		JLabel nameLabel = new JLabel("Name:");
		editTaskPanel.add(nameLabel);
		
		JLabel typeLabel = new JLabel("Type:");
		editTaskPanel.add(typeLabel);
		
		JLabel durationLabel = new JLabel("Duration:");
		editTaskPanel.add(durationLabel);
		
		JLabel priorityLabel = new JLabel("Prioirty:");
		editTaskPanel.add(priorityLabel);
		
		JLabel frequencyLabel = new JLabel("Frequency:");
		editTaskPanel.add(frequencyLabel);
		
		JLabel needLoggingLabel = new JLabel("Need Logging:");
		editTaskPanel.add(needLoggingLabel);
		
		JLabel extraSignOffLabel = new JLabel("Extra Sign Off By:");
		editTaskPanel.add(extraSignOffLabel);
		
		
		String[] caretakerNames = genCNames();
		caretakerInput = new JComboBox();
		caretakerInput.setModel(new DefaultComboBoxModel(caretakerNames));
		editTaskPanel.add(caretakerInput);
		String currentCaretakerName = myTA.getcaretakerNameFromID(currentTask.getAssignedCaretaker());
		caretakerInput.setSelectedItem(currentCaretakerName);
		JLabel caretakerLabel = new JLabel("-Assigned Caretaker");
		editTaskPanel.add(caretakerLabel);
		
		
		//button creation
		editButton1 = new JButton("Confirm");
		editButton1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				String errorMessages = "";
				int caretaker_id;
				int[] caretakerIDs = genCIDs();
				int chosenC = caretakerInput.getSelectedIndex();
				if(chosenC==0) {
					caretaker_id=0;
				}
				else {caretaker_id=caretakerIDs[chosenC-1];}
				boolean result = myTA.allocateCaretaker(task_id, caretaker_id);
				if(!result) {
					setFeedbackPanel(errorMessages);
					JOptionPane.showConfirmDialog(null, feedbackPanel, "Feedback", JOptionPane.PLAIN_MESSAGE);
				}
				else {
					setFeedbackPanel("Allocation successful");
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
	
	
	public void refreshTable(int sort, int filter) {
		DefaultTableModel DTM = (DefaultTableModel) taskListTable.getModel();
		DTM.setRowCount(0);
		Object[][] data = populateTable(sort,filter);
		for (int i = 0; i < data.length; i++) {
			DTM.addRow(data[i]);
		}
	}
	
	private void setFeedbackPanel(String feedback) {
		feedbackPanel = new JPanel();
		
		JLabel feedbackLabel = new JLabel(feedback);
		feedbackPanel.add(feedbackLabel);
		
	}
	
	private JScrollPane createTable(int sort, int filter) {
		String[] colHeaders = { "Task ID", "Task Name", "Task Type", "Task Duration", "Task Priority", "Task Frequency",
				"need logging", "Date Created", "extra sign off", "Caretaker"};
		Object[][] data = populateTable(sort, filter);
		TableModel tableModel = new DefaultTableModel(data, colHeaders);
		taskListTable = new JTable(tableModel);
		// this removes the id column, but you should be able to call
		// 'userTable.getModel().getValueAt(row, 0)' to get the id
		TableColumnModel tcm = taskListTable.getColumnModel();
		tcm.removeColumn(tcm.getColumn(0));// Task ID
		tcm.removeColumn(tcm.getColumn(4));// Frequency
		tcm.removeColumn(tcm.getColumn(4));// Need Logging
		tcm.removeColumn(tcm.getColumn(4));// Completed
		tcm.removeColumn(tcm.getColumn(4));// Extra Sign off
		// import table into a scroll pane so that the table headers are visible and
		// other things
		JScrollPane scrollPane = new JScrollPane(taskListTable);
		scrollPane.setBackground(new Color(238, 238, 238));
		scrollPane.setBounds(68, 150, 820, 232);
		return scrollPane;
	}
	public Object[][] populateTable(int sort, int filter) {
		ArrayList<Object[]> tempData = new ArrayList<Object[]>();

		// call for all tasks info
		ArrayList<task> tasks = new ArrayList<task>();
		switch (sort) {
		case 0:
			tasks = myTA.sortUndoneTasksByDate();
			break;
		case 1:
			tasks = myTA.sortUndoneTasksByUnassigned();
			break;
		case 2:
			tasks = myTA.sortUndoneTasksByUnassigned();
			break;
		case 3:
			tasks = myTA.sortUndoneTasksByAssigned();
			break;
		}
		switch (filter) {
		case 0:
			tasks = myTA.filterToRepeatTasks(tasks);
			break;
		case 1:
			tasks = myTA.filterToOneOffTasks(tasks);
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
						tempTask.getDateCreated(), tempTask.getExtraSignOff(), myTA.getcaretakerNameFromID(tempTask.getAssignedCaretaker())};
				tempData.add(dataPoint);
			}
			Object[][] data = new Object[tempData.size()][10];
			return tempData.toArray(data);
		} catch (NullPointerException e) {
			window.displayError("Table Error!", e.toString());
		}

		Object[][] data = { {} };
		return data;
	}
	
	public String[] genCNames() {
		ArrayList<String> caretakerNamesArrayList = myTA.getAllCaretakersNames();
		String[] caretakerNames = new String[caretakerNamesArrayList.size()+1];
		caretakerNames[0]="None";
		for(int a=0;a<caretakerNamesArrayList.size();a++) {
			caretakerNames[a+1]=caretakerNamesArrayList.get(a);
		}
		return caretakerNames;
	}
	
	public int[] genCIDs() {
		ArrayList<Integer> caretakerIDsArrayList = myTA.getAllCaretakersIDs();
		int[] caretakerIDs = new int[caretakerIDsArrayList.size()];
		for(int a=0;a<caretakerIDsArrayList.size();a++) {
			caretakerIDs[a]=caretakerIDsArrayList.get(a);
		}
		return caretakerIDs;
	}
}
