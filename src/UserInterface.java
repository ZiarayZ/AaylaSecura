import java.awt.Color;
import java.awt.CardLayout;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class UserInterface extends JFrame {
    private LoginUI loginWindow;
    private ManageUsersUI usersWindow;
    private TaskLogUI logTasksWindow;
    private UserManagement user;
    private JPanel contentPane;
    private JPanel mainPane;
    private JButton usersBtn;
    private JButton logTaskBtn;
    private CardLayout cardLayout;
    
    public UserInterface(database db, UserManagement newUser, LogTasks loggingTask) {
        user = newUser;
		mainPane = new JPanel(new CardLayout());
		contentPane = new JPanel();
        contentPane.setBackground(new Color(255, 255, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
        JButton loginBtn = new JButton("Login/Update Password");
        loginBtn.setBounds(6, 100, 125, 50);
        loginBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayLogin();
            }
        });
        contentPane.add(loginBtn);
        usersBtn = new JButton("Manage Users");
        usersBtn.setBounds(137, 100, 125, 50);
        usersBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayUsers();
            }
        });
        contentPane.add(usersBtn);
        logTaskBtn = new JButton("Log Tasks");
        logTaskBtn.setBounds(268, 100, 125, 50);
        logTaskBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayLogTasks();
            }
        });
        contentPane.add(logTaskBtn);

        //collect all JPanels together
        mainPane.add(contentPane, "Main Menu");
        //create other JPanels
        loginWindow = new LoginUI(this, user);
        usersWindow = new ManageUsersUI(this, user, db);
        logTasksWindow = new TaskLogUI(this, loggingTask);
        //add all other JPanels to main CardLayout JPanel
		mainPane.add(loginWindow, "Login");
		mainPane.add(usersWindow, "Users");
		mainPane.add(logTasksWindow, "LogTasks");
        //assign to master Pane
        getContentPane().add(mainPane);
        //create CardLayout once done
        cardLayout = (CardLayout) mainPane.getLayout();
        //make sure main menu is displayed first (may change to login menu)
        displayMain();
    }//craft a main menu to default to

    public void displayLogin() {
		setTitle("Login");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//replace with "DO_NOTHING_ON_CLOSE" handle the operation in the "windowClosing" method of a registered "WindowListener" object
		setBounds(100, 100, 534, 363);
		cardLayout.show(mainPane, "Login");
    }
    public void displayUsers() {
		setTitle("Manage Users");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//replace with "DO_NOTHING_ON_CLOSE" handle the operation in the "windowClosing" method of a registered "WindowListener" object
		setBounds(100, 100, 771, 522);
		cardLayout.show(mainPane, "Users");
    }
    public void displayLogTasks() {
		setTitle("Log Tasks");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//replace with "DO_NOTHING_ON_CLOSE" handle the operation in the "windowClosing" method of a registered "WindowListener" object
		setBounds(100, 100, 976, 686);
		cardLayout.show(mainPane, "LogTasks");
    }
    public void displayMain() {
        setTitle("Main Menu");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 400, 300);
        if (user.getLogin()) {
            usersBtn.setEnabled(true);
        } else {
            usersBtn.setEnabled(false);
        }
		cardLayout.show(mainPane, "Main Menu");
    }
}
