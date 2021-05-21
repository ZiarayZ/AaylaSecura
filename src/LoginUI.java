import javax.swing.border.EtchedBorder;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JProgressBar;
import javax.swing.JPasswordField;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.GridBagLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.sql.SQLException;

public class LoginUI extends JPanel {

	private UserInterface window;
	private JLabel lblHeadingLabel;
	private JTextField nameField;
	private JPasswordField passwordField;
	private JPasswordField changePasswordField;
	private UserManagement User;

	/**
	 * Create the Pane.
	 */
	public LoginUI(UserInterface UI, UserManagement verifyUser) {
		User = verifyUser;
		window = UI;
		setBackground(new Color(255, 255, 255));
		setBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(119, 136, 153), new Color(192, 192, 192)));
		setLayout(new GridBagLayout());
		JPanel fixedPane = new JPanel();
		fixedPane.setLayout(null);
		fixedPane.setBackground(new Color(255, 255, 255));
		
		lblHeadingLabel = new JLabel("Please Login");
		lblHeadingLabel.setOpaque(true);
		lblHeadingLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblHeadingLabel.setFont(new Font("Verdana", Font.PLAIN, 28));
		lblHeadingLabel.setForeground(new Color(105, 105, 105));
		lblHeadingLabel.setBackground(new Color(224, 255, 255));
		lblHeadingLabel.setBounds(126, 11, 267, 37);
		fixedPane.add(lblHeadingLabel);
		
		nameField = new JTextField();
		nameField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				//press enter
				attemptLogin();
			}
		});
		nameField.setBackground(new Color(238, 238, 238));
		nameField.setBounds(191, 98, 203, 20);
		fixedPane.add(nameField);
		nameField.setColumns(10);
		
		JLabel lblUsernameLabel = new JLabel("Username");
		lblUsernameLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblUsernameLabel.setLabelFor(nameField);
		lblUsernameLabel.setBounds(90, 101, 90, 14);
		fixedPane.add(lblUsernameLabel);
		
		passwordField = new JPasswordField();
		passwordField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				//press enter
				attemptLogin();
			}
		});
		passwordField.setBackground(new Color(238, 238, 238));
		passwordField.setBounds(191, 148, 202, 20);
		fixedPane.add(passwordField);
		
		JLabel lblPasswordLabel = new JLabel("Password");
		lblPasswordLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblPasswordLabel.setBounds(90, 151, 90, 14);
		fixedPane.add(lblPasswordLabel);

		//creating a Progress Bar to display New Password's strength, 40 is a pass, 60 is a strong
		JProgressBar passStrength = new JProgressBar(0, 100);
		passStrength.setValue(0);
		passStrength.setBackground(new Color(140, 255, 251));
		passStrength.setForeground(new Color(236, 28, 36));
		passStrength.setBounds(90, 293, 375, 25);

		changePasswordField = new JPasswordField();
		changePasswordField.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent event) {
				//Do Nothing
			}
			public void keyPressed(KeyEvent event) {
				//Press Enter
				int key = event.getKeyCode();
				if (key == KeyEvent.VK_ENTER) {
					attemptPassChange();
				}
			}
			//once a key is pressed then released will test the password's strength
			public void keyReleased(KeyEvent event) {
				int strength = (int)UserManagement.passwordStrength(changePasswordField.getPassword());
				if (strength >= 100) {
					passStrength.setValue(100);
					passStrength.setForeground(new Color(94, 186, 125));
				} else if (strength >= 60) {
					passStrength.setValue(strength);
					passStrength.setForeground(new Color(94, 186, 125));
				} else if (strength >= 40) {
					passStrength.setValue(strength);
					passStrength.setForeground(new Color(248, 152, 29));
				} else if (strength < 40) {
					passStrength.setValue(strength);
					passStrength.setForeground(new Color(236, 28, 36));
				}
			}
		});
		changePasswordField.setBackground(new Color(238, 238, 238));
		changePasswordField.setBounds(191, 198, 202, 20);
		fixedPane.add(changePasswordField);
		
		JLabel lblChangePasswordLabel = new JLabel("New Password");
		lblChangePasswordLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblChangePasswordLabel.setBounds(90, 201, 90, 14);
		fixedPane.add(lblChangePasswordLabel);
		
		JButton btnLoginButton = new JButton("Login");
		btnLoginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				attemptLogin();
			}
		});
		btnLoginButton.setBounds(98, 236, 117, 37);
		fixedPane.add(btnLoginButton);
		
		JButton btnUpdatePasswordButton = new JButton("Update Password");
		btnUpdatePasswordButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				attemptPassChange();
			}
		});
		btnUpdatePasswordButton.setBounds(300, 236, 157, 37);
		fixedPane.add(btnUpdatePasswordButton);
		fixedPane.add(passStrength);
		fixedPane.setPreferredSize(new Dimension(555, 329));
		add(fixedPane);
	}

	private void attemptLogin() {
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
			} else {
				//handle failed login
				window.displayError("Login Failed!", "Invalid Username or Password.");
			}
		} catch (SQLException e) {
			//handle sql exception
			//handle failed login
			window.displayError("Login Failed!", e.toString());
		} catch (NullPointerException e) {
			//handle null result from sql error
			//handle failed login
			window.displayError("Login Failed!", e.toString());
		}
	}
	private void attemptPassChange() {
		String username = nameField.getText();
		//verify password here
		if (!username.equals(null) && !username.equals("")) {
			try {
				if (User.login(username, passwordField.getPassword())) {
					String attempt = User.editPassword(changePasswordField.getPassword());
					if (attempt.equals("")) {
						changePasswordField.setText("");
						passwordField.setText("");
						nameField.setText("");
						window.displayError("Password Updated!", "Password update successful!");
					} else {
						window.displayError("Password Update Failed!", attempt);
					}
				} else {
					//handle login fail
					window.displayError("Password Update Failed!", "Login Failed.");
				}
			} catch (SQLException e) {
				//handle sql exception
				//handle failed Password Update
				window.displayError("Password Update Failed!", e.toString());
			} catch (NullPointerException e) {
				//handle null result from sql error
				//handle failed Password Update
				window.displayError("Password Update Failed!", e.toString());
			}
		} else {
			//handle final password update fails
			//this should theoretically never happen, as you are forcefully logged out when this pane is opened
			window.displayError("Password Update Failed!", "Not Logged In.");
		}
	}
}
