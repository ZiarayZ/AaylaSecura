import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.awt.event.ActionEvent;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ManageUsersUI extends JPanel {

	private JTable userTable;
	private UserManagement user;
	private UserInterface window;
	private EditUser tempUser;

	/**
	 * Create the frame.
	 */
	public ManageUsersUI(UserInterface UI, UserManagement modifyUser, database db) {
		user = modifyUser;
		window = UI;
		setBackground(new Color(255, 255, 255));
		setBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(119, 136, 153), new Color(192, 192, 192)));
		setLayout(new GridBagLayout());
		JPanel fixedPane = new JPanel();
		fixedPane.setLayout(null);
		fixedPane.setBackground(new Color(255, 255, 255));
		
		JButton btnAddUserButton = new JButton("Add User");
		btnAddUserButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (user.accessLevel("MU") >= 1 || user.accessLevel("AP") == 1) {
					createAddUser();
				} else {
					window.displayError("Restricted Access", "You don't have access to do this.");
				}
			}
		});
		btnAddUserButton.setBounds(99, 400, 134, 44);
		fixedPane.add(btnAddUserButton);
		
		JButton btnRemoveUserButton = new JButton("Delete User");
		btnRemoveUserButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (user.accessLevel("MU") >= 3 || user.accessLevel("AP") == 1) {
					JPanel panel = new JPanel();
					int userID = (int) userTable.getModel().getValueAt(userTable.getSelectedRow(), 0);
					panel.add(new JLabel("Delete User Permenantly: " + ((String) userTable.getModel().getValueAt(userTable.getSelectedRow(), 2))));
					//Dialog output
					int result = JOptionPane.showConfirmDialog(null, panel, "Delete User",
					JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
					if (result == JOptionPane.OK_OPTION) {
						boolean sqlResult = user.deleteUser(userID);
						window.displayError("Delete User", "User Deleted: " + sqlResult);
					}
				} else {
					window.displayError("Delete User", "You do not have access to this feature.");
				}
			}
		});
		btnRemoveUserButton.setEnabled(false);
		btnRemoveUserButton.setBounds(561, 400, 134, 44);
		fixedPane.add(btnRemoveUserButton);

		//define new panel
		JPanel editPanel = new JPanel();
		JPanel fixedEditPanel = new JPanel();
		editPanel.setBackground(new Color(255, 255, 255));
		editPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(119, 136, 153), new Color(192, 192, 192)));
		editPanel.setLayout(new GridBagLayout());
		GridLayout editPanelLayout = new GridLayout(0, 2, 100, 25); //2 columns, infinite rows
		fixedEditPanel.setLayout(editPanelLayout);
		fixedEditPanel.setBackground(new Color(255, 255, 255));

		//add content to panel
		//user's ID
		JTextField editID = new JTextField("-1");
		//user's name
		fixedEditPanel.add(new JLabel("User's Name:"));
		JTextField editName = new JTextField();
		fixedEditPanel.add(editName);
		//user's job
		fixedEditPanel.add(new JLabel("User's Role:"));
		JComboBox<Job> editJob = new JComboBox<Job>();
		try {
			ResultSet jobs = user.getJobs();
			while (jobs.next()) {
				editJob.addItem(new Job(jobs.getInt(1), jobs.getString(2)));
			}
		} catch (SQLException e) {
			window.displayError("Database Error!", e.toString());
		} catch (NullPointerException e) {
			window.displayError("Database Error!", e.toString());
		}
		fixedEditPanel.add(editJob);
		//user's notes
		fixedEditPanel.add(new JLabel("User's Notes:"));
		JTextArea editNotes = new JTextArea();
		fixedEditPanel.add(editNotes);
		//user's gender
		fixedEditPanel.add(new JLabel("User's Gender:"));
		Job[] genders = {new Job('M', "Male"), new Job('F', "Female")};
		JComboBox<Job> editGender = new JComboBox<Job>(genders);
		fixedEditPanel.add(editGender);
		//add hidden user's ID
		fixedEditPanel.add(editID);
		editID.setVisible(false);

		//accept edit
		JButton acceptEdit = new JButton("Edit");
		acceptEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				if (Integer.parseInt(editID.getText()) != -1) {
					try {
						user.editUser(
							Integer.parseInt(editID.getText()),//get ID stored
							editName.getText(),
							((Job) editJob.getSelectedItem()).getID(),
							editNotes.getText(),
							Character.toString((char) ((Job) editGender.getSelectedItem()).getID())
						);
					} catch (SQLException e) {
						window.displayError("Database Error!", e.toString());
					}
				} else {
					window.displayError("Edit Error!", "No user Selected.");
				}
			}
		});
		fixedEditPanel.add(acceptEdit);

		//finish panel creation
		fixedEditPanel.setPreferredSize(new Dimension(555, 329));
		editPanel.add(fixedEditPanel);
		window.addCard(editPanel, "EditUser");
		
		JButton btnEditUserButton = new JButton("Edit User");
		btnEditUserButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				try {
					ResultSet editUser = user.getUserFromID((int) userTable.getModel().getValueAt(userTable.getSelectedRow(), 0));
					if (editUser.next()) {
						tempUser = new EditUser(Integer.toString(
							editUser.getInt(1)),
							editUser.getString(2),
							editUser.getString(4),
							editUser.getString(6),
							editUser.getString(5)
						);
						editID.setText(tempUser.getID());
						editName.setText(tempUser.getName());
						editJob.setSelectedItem(tempUser.getJob());
						editGender.setSelectedItem(tempUser.getGender());
						editNotes.setText(tempUser.getNotes());
					}
				} catch (SQLException e) {
					window.displayError("Database Error!", e.toString());
					editID.setText(Integer.toString((int) userTable.getModel().getValueAt(userTable.getSelectedRow(), 0)));
					editName.setText(userTable.getModel().getValueAt(userTable.getSelectedRow(), 1).toString());
					editJob.setSelectedItem(userTable.getModel().getValueAt(userTable.getSelectedRow(), 3).toString());
					editGender.setSelectedItem(userTable.getModel().getValueAt(userTable.getSelectedRow(), 4).toString());
				} catch (NullPointerException e) {
					window.displayError("Database Error!", e.toString());
					editID.setText(Integer.toString((int) userTable.getModel().getValueAt(userTable.getSelectedRow(), 0)));
					editName.setText(userTable.getModel().getValueAt(userTable.getSelectedRow(), 1).toString());
					editJob.setSelectedItem(userTable.getModel().getValueAt(userTable.getSelectedRow(), 3).toString());
					editGender.setSelectedItem(userTable.getModel().getValueAt(userTable.getSelectedRow(), 4).toString());
				}
				window.show("EditUser");
			}
		});
		btnEditUserButton.setEnabled(false);
		btnEditUserButton.setBounds(327, 400, 124, 44);
		fixedPane.add(btnEditUserButton);
		
		String[] colHeaders = {"User ID", "Name", "Username", "Role ID", "Role", "Gender"};
		Object[][] data = populateTable();
		TableModel tableModel = new DefaultTableModel(colHeaders, 0);
		userTable = new JTable(tableModel);
		DefaultTableModel DTM = (DefaultTableModel) userTable.getModel();
		for (int i = 0; i < data.length; i++) {
			DTM.addRow(data[i]);
		}
		userTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		ListSelectionModel selectionModel = userTable.getSelectionModel();
		selectionModel.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				//when selecting a row in the manage users table, restrict button access the user can access
				if (userTable.getSelectedRow() != -1) {

					//admin permission
					if (user.accessLevel("AP") == 1) {
						btnAddUserButton.setEnabled(true);
						btnEditUserButton.setEnabled(true);
						btnRemoveUserButton.setEnabled(true);
					} else {
						btnAddUserButton.setEnabled(false);
						btnEditUserButton.setEnabled(false);
						btnRemoveUserButton.setEnabled(false);
					}

					//manage users
					int accessLevel = user.accessLevel("MU");
					if (accessLevel >= 1) {
						btnAddUserButton.setEnabled(true);
					} else {
						btnAddUserButton.setEnabled(false);
					}
					if (accessLevel >= 2) {
						btnEditUserButton.setEnabled(true);
					} else {
						btnEditUserButton.setEnabled(false);
					}
					if (accessLevel >= 3) {
						btnRemoveUserButton.setEnabled(true);
					} else {
						btnRemoveUserButton.setEnabled(false);
					}
				} else {
					if (user.accessLevel("MU") == 0) {
						btnAddUserButton.setEnabled(false);
					}
					btnEditUserButton.setEnabled(false);
					btnRemoveUserButton.setEnabled(false);
				}
			}
		});

		//this removes the id column, but you should be able to call 'userTable.getModel().getValueAt(row, 0)' to get the id
		TableColumnModel tcm = userTable.getColumnModel();
		tcm.removeColumn(tcm.getColumn(0));//user ID, index 0
		tcm.removeColumn(tcm.getColumn(2));//role ID, index 3
		//import table into a scroll pane so that the table headers are visible and other things
		JScrollPane scrollPane = new JScrollPane(userTable);
		scrollPane.setBackground(new Color(192, 192, 192));
		scrollPane.setBounds(35, 34, 685, 337);
		fixedPane.add(scrollPane);
		fixedPane.setPreferredSize(new Dimension(755, 512));
		add(fixedPane);
	}

	public void refreshTable() {
		DefaultTableModel DTM = (DefaultTableModel) userTable.getModel();
		DTM.setRowCount(0);
		Object[][] data = populateTable();
		for (int i = 0; i < data.length; i++) {
			DTM.addRow(data[i]);
		}
	}
	private Object[][] populateTable() {
		ArrayList<Object[]> tempData = new ArrayList<Object[]>();

		//call for all users info
		try {
			ResultSet sql = user.getUsers();
			String gender;
			while (sql.next()) {
				gender = sql.getString(6);
				if (gender.equals("M")) {
					gender = "Male";
				} else {
					gender = "Female";
				}
				Object[] dataPoint = {sql.getInt(1), sql.getString(2), sql.getString(3), sql.getInt(4), sql.getString(5), gender};
				tempData.add(dataPoint);
			}
			Object[][] data = new Object[tempData.size()][6];
			return tempData.toArray(data);
		} catch (SQLException e) {
			window.displayError("Table Error!", e.toString());
		} catch (NullPointerException e) {
			window.displayError("Table Error!", e.toString());
		}

		Object[][] data = {{}};
		return data;
	}

	//add new user form
	private void createAddUser() {
		try {
			ResultSet jobs = user.getJobs();
			JComboBox<Job> jobCombo = new JComboBox<Job>();
			while (jobs.next()) {
				jobCombo.addItem(new Job(jobs.getInt(1), jobs.getString(2)));
			}
			Job[] genders = {new Job('M', "Male"), new Job('F', "Female")};
			JComboBox<Job> genderCombo = new JComboBox<Job>(genders);
			JTextField name = new JTextField();
			JTextField username = new JTextField();
			JPasswordField password = new JPasswordField();
			JTextField notes = new JTextField();
			JPanel addUserPanel = new JPanel(new GridLayout(0, 1));
			addUserPanel.add(new JLabel("User's Name:"));
			addUserPanel.add(name);
			addUserPanel.add(new JLabel("Username:"));
			addUserPanel.add(username);
			addUserPanel.add(new JLabel("Job:"));
			addUserPanel.add(jobCombo);
			addUserPanel.add(new JLabel("Password:"));
			addUserPanel.add(password);
			addUserPanel.add(new JLabel("Notes:"));
			addUserPanel.add(notes);
			addUserPanel.add(new JLabel("Gender:"));
			addUserPanel.add(genderCombo);

			//Dialog output
			int result = JOptionPane.showConfirmDialog(null, addUserPanel, "Add User",
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
			if (result == JOptionPane.OK_OPTION) {
				String addName = name.getText();
				String addUsername = username.getText();
				if (password.getPassword().length >= 8 && !(addName.length() == 0 || addUsername.length() == 0)) {
					try {
						String newResult = user.addUser(
							addName,
							addUsername,
							((Job) jobCombo.getSelectedItem()).getID(),
							UserManagement.genPassHash(password.getPassword()),
							notes.getText(),
							Character.toString((char) ((Job) genderCombo.getSelectedItem()).getID())
						);
						if (!newResult.equals("")) {
							window.displayError("Add New User Failed", "Error: " + newResult);
						} else {
							refreshTable();
							window.displayError("Add New User Success", "Successfully added new user: " + addUsername);
						}
					} catch (NoSuchAlgorithmException e) {
						window.displayError("Add New User Failed", e.toString());
					} catch (InvalidKeySpecException e) {
						window.displayError("Add New User Failed", e.toString());
					}
				} else if (password.getPassword().length < 8) {
					window.displayError("Add New User Failed", "Error: Password is too short. Must be at least 8 characters.");
				} else {
					window.displayError("Add New User Failed", "Error: User's Name or Username fields are empty.");
				}
			}
		} catch (SQLException e) {
			window.displayError("Database Error!", "Cannot retrieve jobs from database.");
		}
	}
}

//combo item
class Job {
	private int ID;//will be char for gender
	private String name;
	public Job(int jobID, String jobName) {
		ID = jobID;
		name = jobName;
	}

	public int getID() {
		return ID;
	}
	public String toString() {
		return name;
	}
}
class EditUser {
	private String ID;
	private String name;
	private String job;
	private String gender;
	private String notes;
	public EditUser(String newID, String newName, String newJob, String newGender, String newNotes) {
		ID = newID;
		name = newName;
		job = newJob;
		gender = newGender;
		notes = newNotes;
	}
	public String getID() {
		return ID;
	}
	public String getName() {
		return name;
	}
	public String getJob() {
		return job;
	}
	public String getGender() {
		return gender;
	}
	public String getNotes() {
		return notes;
	}
}