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
    private JButton loginBtn;
    private JButton usersBtn;
    private JButton logTaskBtn;
    private JButton backBtn;
    private CardLayout cardLayout;
    
    public UserInterface(database db, UserManagement newUser, LogTasks loggingTask) {
        user = newUser;
        //create master pane and main menu pane
		mainPane = new JPanel(new CardLayout());
		contentPane = new JPanel();
        contentPane.setBackground(new Color(255, 255, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
        //add buttons to access cards
        loginBtn = new JButton("Login");
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
        logTasksWindow = new TaskLogUI(this, loggingTask, db);
        //create back button to navigate back to main menu
        backBtn = new JButton("<");
        backBtn.setBounds(0, 0, 50, 30);
        backBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayMain();
            }
        });
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
    }

    public void displayBackBtn(JPanel window) {
        //add back button to selected JPanel
        window.add(backBtn);
    }

    public void displayLogin() {
        if (user.getLogin()) {
            user.logout();
        }
		setTitle("Login");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//replace with "DO_NOTHING_ON_CLOSE" handle the operation in the "windowClosing" method of a registered "WindowListener" object
		setBounds(100, 100, 534, 363);
        displayBackBtn(loginWindow);
		cardLayout.show(mainPane, "Login");
    }
    public void displayUsers() {
		setTitle("Manage Users");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//replace with "DO_NOTHING_ON_CLOSE" handle the operation in the "windowClosing" method of a registered "WindowListener" object
		setBounds(100, 100, 771, 522);
        displayBackBtn(usersWindow);
		cardLayout.show(mainPane, "Users");
    }
    public void displayLogTasks() {
		setTitle("Log Tasks");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//replace with "DO_NOTHING_ON_CLOSE" handle the operation in the "windowClosing" method of a registered "WindowListener" object
		setBounds(100, 100, 976, 686);
        displayBackBtn(logTasksWindow);
		cardLayout.show(mainPane, "LogTasks");
    }
    public void displayMain() {
        setTitle("Main Menu");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 400, 300);
        if (user.getLogin()) {
            usersBtn.setEnabled(true);
            logTaskBtn.setEnabled(true);
            loginBtn.setText("Logout");
        } else {
            usersBtn.setEnabled(false);
            logTaskBtn.setEnabled(false);
            loginBtn.setText("Login");
        }
		cardLayout.show(mainPane, "Main Menu");
    }
}
