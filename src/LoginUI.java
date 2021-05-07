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
	private JPasswordField changePasswordField;
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
				//i see no reason for this
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
				//i see no reason for this
			}
		});
		passwordField.setBackground(new Color(119, 136, 153));
		passwordField.setBounds(191, 148, 202, 20);
		contentPane.add(passwordField);
		
		JLabel lblPasswordLabel = new JLabel("Password");
		lblPasswordLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblPasswordLabel.setBounds(98, 151, 83, 14);
		contentPane.add(lblPasswordLabel);

		changePasswordField = new JPasswordField();
		changePasswordField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				//i see no reason for this
			}
		});
		changePasswordField.setBackground(new Color(119, 136, 153));
		changePasswordField.setBounds(191, 198, 202, 20);
		contentPane.add(changePasswordField);
		
		JLabel lblChangePasswordLabel = new JLabel("Password");
		lblChangePasswordLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblChangePasswordLabel.setBounds(98, 201, 83, 14);
		contentPane.add(lblChangePasswordLabel);
		
		JButton btnLoginButton = new JButton("Login");
		btnLoginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				//verify login with UserManagement
				try {
					//add grabbed username and password
					if (User.login(nameField.getText(), passwordField.getPassword())) {
						//handle successful login
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
		btnLoginButton.setBounds(98, 236, 117, 37);
		contentPane.add(btnLoginButton);
		
		JButton btnUpdatePasswordButton = new JButton("Update Password");
		btnUpdatePasswordButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				String username = nameField.getText();
				//verify password here
				if (User.getLogin()) {
					if (UserManagement.verifyPassword(changePasswordField.getPassword())) {
						//handle password change
						try {
							System.out.println(User.editPassword(changePasswordField.getPassword()));
							System.out.println("Password Update success.");
						} catch (SQLException e) {
							System.out.println("Password Update fail: SQLException.");
						} catch (NullPointerException e) {
							System.out.println("Password Update fail: NullPointerException.");
						}
					} else {
						//handle password fail
						System.out.println("Password Update fail: Password too weak.");
					}
				} else if (!username.equals(null) && !username.equals("")) {
					try {
						if (User.login(username, passwordField.getPassword())) {
							User.editPassword(changePasswordField.getPassword());
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
		btnUpdatePasswordButton.setBounds(300, 236, 117, 37);
		contentPane.add(btnUpdatePasswordButton);
	}
}
