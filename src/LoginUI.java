import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.UIManager;
import java.awt.Color;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import java.awt.SystemColor;
import java.awt.Window.Type;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.awt.event.ActionEvent;

public class LoginUI extends JFrame {

	private JPanel contentPane;
	private JLabel lblHeadingLabel;
	private JTextField nameField;
	private JPasswordField passwordField;
	private UserManagement User;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginUI frame = new LoginUI(null);
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
	public LoginUI(UserManagement verifyUser) {
		User = verifyUser;
		setTitle("Login");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 534, 363);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 255, 255));
		contentPane.setBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(119, 136, 153), new Color(192, 192, 192)));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		lblHeadingLabel = new JLabel("Please Login");
		lblHeadingLabel.setOpaque(true);
		lblHeadingLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblHeadingLabel.setFont(new Font("Verdana", Font.PLAIN, 28));
		lblHeadingLabel.setForeground(new Color(105, 105, 105));
		lblHeadingLabel.setBackground(new Color(224, 255, 255));
		lblHeadingLabel.setBounds(126, 11, 267, 37);
		contentPane.add(lblHeadingLabel);
		
		nameField = new JTextField();
		nameField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				//grab input username
			}
		});
		nameField.setBackground(new Color(119, 136, 153));
		nameField.setBounds(191, 98, 203, 20);
		contentPane.add(nameField);
		nameField.setColumns(10);
		
		JLabel lblUsernameLabel = new JLabel("Username");
		lblUsernameLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblUsernameLabel.setLabelFor(nameField);
		lblUsernameLabel.setBounds(98, 101, 83, 14);
		contentPane.add(lblUsernameLabel);
		
		passwordField = new JPasswordField();
		passwordField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				//grab input password
			}
		});
		passwordField.setBackground(new Color(119, 136, 153));
		passwordField.setBounds(191, 148, 202, 20);
		contentPane.add(passwordField);
		
		JLabel lblPasswordLabel = new JLabel("Password");
		lblPasswordLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblPasswordLabel.setBounds(98, 151, 83, 14);
		contentPane.add(lblPasswordLabel);
		
		JButton btnLoginButton = new JButton("Login");
		btnLoginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				//verify login with UserManagement
				try {
					//add grabbed username and password instead of ""
					if (User.login("","")) {
						//handle successful login
					} else {
						//handle failed login
					}
				} catch (SQLException e) {
					//handle sql exception
					//handle failed login
				}
			}
		});
		btnLoginButton.setBounds(98, 236, 117, 37);
		contentPane.add(btnLoginButton);
		
		JButton btnUpdatePasswordButton = new JButton("Update Password");
		btnUpdatePasswordButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				//verify password here
			}
		});
		btnUpdatePasswordButton.setBounds(300, 236, 117, 37);
		contentPane.add(btnUpdatePasswordButton);
	}
}
