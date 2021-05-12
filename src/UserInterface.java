import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class UserInterface extends JFrame {
    private LoginUI loginWindow;
    private ManageUsersUI usersWindow;
    private UserManagement user;
    private JPanel contentPane;
    private JButton usersBtn;
    
    public UserInterface(database db, UserManagement newUser) {
        user = newUser;
        loginWindow = new LoginUI(user);
        usersWindow = new ManageUsersUI(user, db);
        loginWindow.setWindow(this);
        usersWindow.setWindow(this);
		contentPane = new JPanel();
        contentPane.setBackground(new Color(255, 255, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
        JButton loginBtn = new JButton("Login/Update Password");
        loginBtn.setBounds(50, 100, 125, 50);
        loginBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayLogin();
            }
        });
        contentPane.add(loginBtn);
        usersBtn = new JButton("Manage Users");
        usersBtn.setBounds(225, 100, 125, 50);
        usersBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayUsers();
            }
        });
        contentPane.add(usersBtn);
        displayMain();
    }//craft a main menu to default to

    public void displayLogin() {
        loginWindow.displayWindow();
    }
    public void displayUsers() {
        usersWindow.displayWindow();
    }
    public void displayMain() {
        setTitle("Main Menu");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 400, 300);
		setContentPane(contentPane);
        if (user.getLogin()) {
            usersBtn.setEnabled(true);
        } else {
            usersBtn.setEnabled(false);
        }
    }
}
