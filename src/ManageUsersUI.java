import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JTable;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ManageUsersUI extends JFrame {

	private JPanel contentPane;
	private JTable userTable;
	private UserManagement userModify;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ManageUsersUI frame = new ManageUsersUI(null);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ManageUsersUI(UserManagement modifyUser) {
		userModify = modifyUser;
		setTitle("Manage Users");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 771, 522);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 255, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
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
		btnRemoveUserButton.setBounds(327, 400, 134, 44);
		contentPane.add(btnRemoveUserButton);
		
		JButton btnEditUserButton = new JButton("Edit User");
		btnEditUserButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//edit a user here
			}
		});
		btnEditUserButton.setBounds(561, 400, 124, 44);
		contentPane.add(btnEditUserButton);
		
		userTable = new JTable();
		userTable.setBackground(new Color(192, 192, 192));
		userTable.setBounds(35, 34, 685, 337);
		contentPane.add(userTable);
	}
}
