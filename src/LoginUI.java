import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.Window.Type;
import javax.swing.border.EtchedBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.SQLException;

public class LoginUI extends JPanel {

	private UserInterface window;
	private JLabel lblHeadingLabel;
	private JTextField nameField;
	private JPasswordField passwordField;
	private JPasswordField changePasswordField;
	private UserManagement User;
	/**
	 * Launch the application.
	 */
	/*public static void main(String[] args) {
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
	}*/

	/**
	 * Create the frame.
	 */
	public LoginUI(UserInterface UI, UserManagement verifyUser) {
		User = verifyUser;
		window = UI;
		setBackground(new Color(255, 255, 255));
		setBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(119, 136, 153), new Color(192, 192, 192)));
		setLayout(null);
		
		lblHeadingLabel = new JLabel("Please Login");
		lblHeadingLabel.setOpaque(true);
		lblHeadingLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblHeadingLabel.setFont(new Font("Verdana", Font.PLAIN, 28));
		lblHeadingLabel.setForeground(new Color(105, 105, 105));
		lblHeadingLabel.setBackground(new Color(224, 255, 255));
		lblHeadingLabel.setBounds(347, 173, 267, 37);
		add(lblHeadingLabel);
		
		nameField = new JTextField();
		nameField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				//i see no reason for this
			}
		});
		nameField.setBackground(new Color(119, 136, 153));
		nameField.setBounds(412, 260, 203, 20);
		add(nameField);
		nameField.setColumns(10);
		
		JLabel lblUsernameLabel = new JLabel("Username");
		lblUsernameLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblUsernameLabel.setLabelFor(nameField);
		lblUsernameLabel.setBounds(311, 263, 90, 14);
		add(lblUsernameLabel);
		
		passwordField = new JPasswordField();
		passwordField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				//i see no reason for this
			}
		});
		passwordField.setBackground(new Color(119, 136, 153));
		passwordField.setBounds(412, 310, 202, 20);
		add(passwordField);
		
		JLabel lblPasswordLabel = new JLabel("Password");
		lblPasswordLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblPasswordLabel.setBounds(311, 313, 90, 14);
		add(lblPasswordLabel);

		changePasswordField = new JPasswordField();
		changePasswordField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				//i see no reason for this
			}
		});
		changePasswordField.setBackground(new Color(119, 136, 153));
		changePasswordField.setBounds(412, 360, 202, 20);
		add(changePasswordField);
		
		JLabel lblChangePasswordLabel = new JLabel("New Password");
		lblChangePasswordLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblChangePasswordLabel.setBounds(311, 363, 90, 14);
		add(lblChangePasswordLabel);
		
		JButton btnLoginButton = new JButton("Login");
		btnLoginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				//verify login with UserManagement
				try {
					//add grabbed username and password
					if (User.login(nameField.getText(), passwordField.getPassword())) {
						changePasswordField.setText("");
						passwordField.setText("");
						nameField.setText("");
						//handle successful login
						changePasswordField.setText("");
						passwordField.setText("");
						nameField.setText("");
						window.displayMain();
						System.out.println("Login success.");
					} else {
						//handle failed login
						System.out.println("Login fail: Invalid Username or Password.");
					}
				} catch (SQLException e) {
					//handle sql exception
					//handle failed login
					System.out.println("Login fail: SQLException.");
				} catch (NullPointerException e) {
					//handle null result from sql error
					//handle failed login
					System.out.println("Login fail: NullPointerException.");
				}
			}
		});
		btnLoginButton.setBounds(319, 398, 117, 37);
		add(btnLoginButton);
		
		JButton btnUpdatePasswordButton = new JButton("Update Password");
		btnUpdatePasswordButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				String username = nameField.getText();
				//verify password here
				if (!username.equals(null) && !username.equals("")) {
					try {
						if (User.login(username, passwordField.getPassword())) {
							User.editPassword(changePasswordField.getPassword());
							changePasswordField.setText("");
							passwordField.setText("");
							nameField.setText("");
							window.displayMain();
							System.out.println("Password Update success.");
						} else {
							//handle login fail
							System.out.println("Password Update fail: Failed Login.");
						}
					} catch (SQLException e) {
						//handle sql exception
						//handle failed Password Update
						System.out.println("Password Update fail: SQLException.");
					} catch (NullPointerException e) {
						//handle null result from sql error
						//handle failed Password Update
						System.out.println("Password Update fail: NullPointerException.");
					}
				} else {
						//handle final password update fails
						System.out.println("Password Update fail: Not Logged in.");
				}
			}
		});
		btnUpdatePasswordButton.setBounds(521, 398, 157, 37);
		add(btnUpdatePasswordButton);
	}
}
