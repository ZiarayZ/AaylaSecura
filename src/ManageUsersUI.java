import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableColumnModel;
import javax.swing.JButton;
import javax.swing.JTable;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.awt.event.ActionEvent;

public class ManageUsersUI {

	private JPanel contentPane;
	private JTable userTable;
	private database userDB;
	private UserManagement userModify;
	private UserInterface window;
	/**
	 * Launch the application.
	 */
	/*public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ManageUsersUI frame = new ManageUsersUI(null, new database());
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}*/

	/**
	 * Create the frame.
	 */
	public ManageUsersUI(UserManagement modifyUser, database db) {
		userModify = modifyUser;
		userDB = db;
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 255, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		
		JButton btnAddUserButton = new JButton("Add User");
		btnAddUserButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//add a user here
			}
		});
		btnAddUserButton.setBounds(99, 400, 134, 44);
		contentPane.add(btnAddUserButton);
		
		JButton btnRemoveUserButton = new JButton("Remove User");
		btnRemoveUserButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//delete a user here
			}
		});
		btnRemoveUserButton.setEnabled(false);
		btnRemoveUserButton.setBounds(327, 400, 134, 44);
		contentPane.add(btnRemoveUserButton);
		
		JButton btnEditUserButton = new JButton("Edit User");
		btnEditUserButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//edit a user here
			}
		});
		btnEditUserButton.setEnabled(false);
		btnEditUserButton.setBounds(561, 400, 124, 44);
		contentPane.add(btnEditUserButton);
		
		String[] colHeaders = {"ID", "Name", "Username", "Role", "Gender"};
		Object[][] data = populateTable();
		userTable = new JTable(data,colHeaders);
		//this removes the id column, but you should be able to call 'userTable.getModel().getValueAt(row, 0)' to get the id
		TableColumnModel tcm = userTable.getColumnModel();
		tcm.removeColumn(tcm.getColumn(0));
		//import table into a scroll pane so that the table headers are visible and other things
		JScrollPane scrollPane = new JScrollPane(userTable);
		scrollPane.setBackground(new Color(192, 192, 192));
		scrollPane.setBounds(35, 34, 685, 337);
		contentPane.add(scrollPane);
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
			System.out.println(e);//need to display error window instead
		} catch (NullPointerException e) {
			System.out.println(e);//need to display error window instead
		}

		Object[][] data = {{}};
		return data;
	}

	//sets window to have this contentPane
	public void setWindow(UserInterface wind) {
		window = wind;
	}
	public void displayWindow() {
		window.setTitle("Manage Users");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setBounds(100, 100, 771, 522);
		window.setContentPane(contentPane);
	}
}
