import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumnModel;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.DefaultComboBoxModel;
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
import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Dimension;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ManageUsersUI extends JPanel {

	private JTable userTable;
	private database userDB;
	private UserManagement user;
	private UserInterface window;

	/**
	 * Create the frame.
	 */
	public ManageUsersUI(UserInterface UI, UserManagement modifyUser, database db) {
		user = modifyUser;
		userDB = db;
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
		
		JButton btnRemoveUserButton = new JButton("Remove User");
		btnRemoveUserButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//create or show JDialog box to confirm
			}
		});
		btnRemoveUserButton.setEnabled(false);
		btnRemoveUserButton.setBounds(327, 400, 134, 44);
		fixedPane.add(btnRemoveUserButton);
		
		JButton btnEditUserButton = new JButton("Edit User");
		btnEditUserButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//create or show form to edit user
			}
		});
		btnEditUserButton.setEnabled(false);
		btnEditUserButton.setBounds(561, 400, 124, 44);
		fixedPane.add(btnEditUserButton);
		
		String[] colHeaders = {"ID", "Name", "Username", "Role", "Gender"};
		Object[][] data = populateTable();
		userTable = new JTable(data,colHeaders);
		userTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		ListSelectionModel selectionModel = userTable.getSelectionModel();
		selectionModel.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				//grabbing selected row index in JTable
				if (userTable.getSelectedRow() != -1) {
					btnEditUserButton.setEnabled(true);
					btnRemoveUserButton.setEnabled(true);
				} else {
					btnEditUserButton.setEnabled(false);
					btnRemoveUserButton.setEnabled(false);
				}
			}
		});

		//this removes the id column, but you should be able to call 'userTable.getModel().getValueAt(row, 0)' to get the id
		TableColumnModel tcm = userTable.getColumnModel();
		tcm.removeColumn(tcm.getColumn(0));
		//import table into a scroll pane so that the table headers are visible and other things
		JScrollPane scrollPane = new JScrollPane(userTable);
		scrollPane.setBackground(new Color(192, 192, 192));
		scrollPane.setBounds(35, 34, 685, 337);
		fixedPane.add(scrollPane);
		fixedPane.setPreferredSize(new Dimension(755,512));
		add(fixedPane);
	}

	public Object[][] populateTable() {
		ArrayList<Object[]> tempData = new ArrayList<Object[]>();

		//call for all users info
		ResultSet sql = userDB.getAllUsers();
		String gender;
		try {
			while (sql.next()) {
				gender = sql.getString(5);
				if (gender.equals("M")) {
					gender = "Male";
				} else {
					gender = "Female";
				}
				Object[] dataPoint = {sql.getInt(1), sql.getString(2), sql.getString(3), sql.getString(4), gender};
				tempData.add(dataPoint);
			}
			Object[][] data = new Object[tempData.size()][5];
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
			ResultSet jobs = userDB.getAllJobs();
			JComboBox<Job> jobCombo = new JComboBox<Job>();
			while (jobs.next()) {
				jobCombo.addItem(new Job(jobs.getInt(1), jobs.getString(2)));
			}
			Job[] genders = {new Job('M', "Male"), new Job('F', "Female")};
			JComboBox<Job> genderCombo = new JComboBox<Job>(genders);
			JTextField name = new JTextField();
			JTextField username = new JTextField();
			JPasswordField password = new JPasswordField();
			JTextArea notes = new JTextArea();
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
				if (password.getPassword().length >= 8 && !(name.getText().length() == 0 || username.getText().length() == 0)) {
					try {
						String newResult = userDB.addNewUser(
							name.getText(),
							username.getText(),
							((Job) jobCombo.getSelectedItem()).getID(),
							UserManagement.genPassHash(password.getPassword()),
							notes.getText(),
							Character.toString((char) ((Job) genderCombo.getSelectedItem()).getID())
						);
						if (!newResult.equals("")) {
							window.displayError("Add New User Failed", "Error: " + newResult);
						} else {
							window.displayError("Add New User Success", "Successfully added new user: " + username.getText());
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