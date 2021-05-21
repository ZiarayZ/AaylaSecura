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
	private JTextField nameField;
	private JPasswordField passwordField;
	private JPasswordField changePasswordField;
	private UserManagement User;
	private JLabel upperLbl;
	private JLabel lowerLbl;
	private JLabel numberLbl;
	private JLabel nonAlphanumericLbl;

	/**
	 * Create the Pane.
	 */
	public LoginUI(UserInterface UI, UserManagement verifyUser) {
		final Color red = new Color(236, 28, 36);
		final Color orange = new Color(248, 152, 29);
		final Color green = new Color(94, 186, 125);
		final Color grey = new Color(238, 238, 238);
		final Color white = new Color(255, 255, 255);
		User = verifyUser;
		window = UI;
		setBackground(white);
		setBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(119, 136, 153), new Color(192, 192, 192)));
		setLayout(new GridBagLayout());
		JPanel fixedPane = new JPanel();
		fixedPane.setLayout(null);
		fixedPane.setBackground(white);
		
		JLabel lblHeadingLabel = new JLabel("Please Login");
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
		nameField.setBackground(grey);
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
		passwordField.setBackground(grey);
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
		passStrength.setForeground(red);
		passStrength.setBounds(90, 293, 375, 25);

		changePasswordField = new JPasswordField();
		changePasswordField.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent event) {
				char key = event.getKeyChar();
				//check key
				if (key != KeyEvent.CHAR_UNDEFINED) {
					if (Character.isDigit(key)) {
						numberLbl.setForeground(green);
					}
					if (Character.isUpperCase(key)) {
						upperLbl.setForeground(green);
					}
					if (Character.isLowerCase(key)) {
						lowerLbl.setForeground(green);
					}
					if (!Character.isDigit(key) && !Character.isUpperCase(key) && !Character.isLowerCase(key)) {
						nonAlphanumericLbl.setForeground(green);
					}
				}
			}
			public void keyPressed(KeyEvent event) {
				//Press Enter
				if (event.getKeyCode() == KeyEvent.VK_ENTER) {
					attemptPassChange();
				}
			}
			//once a key is pressed then released will test the password's strength
			public void keyReleased(KeyEvent event) {
				char[] newPass = changePasswordField.getPassword();
				if (event.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
					String result = UserManagement.checkPassword(newPass);
					if (result.contains("U")) {
						upperLbl.setForeground(red);
					}
					if (result.contains("L")) {
						lowerLbl.setForeground(red);
					}
					if (result.contains("D")) {
						numberLbl.setForeground(red);
					}
					if (result.contains("N")) {
						nonAlphanumericLbl.setForeground(red);
					}
				}
				int strength = (int)UserManagement.passwordStrength(newPass);
				if (strength >= 100) {
					passStrength.setValue(100);
					passStrength.setForeground(green);
				} else if (strength >= 60) {
					passStrength.setValue(strength);
					passStrength.setForeground(green);
				} else if (strength >= 40) {
					passStrength.setValue(strength);
					passStrength.setForeground(orange);
				} else if (strength < 40) {
					passStrength.setValue(strength);
					passStrength.setForeground(red);
				}
			}
		});
		changePasswordField.setBackground(grey);
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

		//change password labels
		upperLbl = new JLabel("Upper case: A-Z");
		upperLbl.setForeground(red);
		upperLbl.setBounds(465, 101, 90, 14);
		lowerLbl = new JLabel("Lower case: a-z");
		lowerLbl.setForeground(red);
		lowerLbl.setBounds(465, 151, 90, 14);
		numberLbl = new JLabel("Numbers: 0-9");
		numberLbl.setForeground(red);
		numberLbl.setBounds(465, 201, 90, 14);
		nonAlphanumericLbl = new JLabel("Symbols: ~!@#$%^&*()_-");
		nonAlphanumericLbl.setForeground(red);
		nonAlphanumericLbl.setBounds(465, 251, 90, 14);
		fixedPane.add(upperLbl);
		fixedPane.add(lowerLbl);
		fixedPane.add(numberLbl);
		fixedPane.add(nonAlphanumericLbl);
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
