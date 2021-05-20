import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumnModel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.JPanel;
import javax.swing.JDialog;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ManageUsersUI extends JPanel {

	private JTable userTable;
	private JDialog enterInfo;
	private database userDB;
	private UserManagement User;
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
	public ManageUsersUI(UserInterface UI, UserManagement modifyUser, database db) {
		User = modifyUser;
		userDB = db;
		window = UI;
		setBackground(new Color(255, 255, 255));
		setBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(119, 136, 153), new Color(192, 192, 192)));
		setLayout(null);
		
		JButton btnAddUserButton = new JButton("Add User");
		btnAddUserButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//create or show form to add user
			}
		});
		btnAddUserButton.setBounds(201, 482, 134, 44);
		add(btnAddUserButton);
		
		JButton btnRemoveUserButton = new JButton("Remove User");
		btnRemoveUserButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//create or show JDialog box to confirm
			}
		});
		btnRemoveUserButton.setEnabled(false);
		btnRemoveUserButton.setBounds(429, 482, 134, 44);
		add(btnRemoveUserButton);
		
		JButton btnEditUserButton = new JButton("Edit User");
		btnEditUserButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//create or show form to edit user
			}
		});
		btnEditUserButton.setEnabled(false);
		btnEditUserButton.setBounds(663, 482, 124, 44);
		add(btnEditUserButton);
		
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
		scrollPane.setBounds(137, 116, 685, 337);
		add(scrollPane);
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
}
