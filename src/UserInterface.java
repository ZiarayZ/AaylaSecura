import java.awt.Color;
import java.awt.CardLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

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
    private CardLayout cardLayout;
    
    public UserInterface(database db, UserManagement newUser, LogTasks loggingTask) {
        user = newUser;
        //create master pane and main menu pane
        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        setLayout(gbl);
		setBounds(100, 100, 1100, 686);
		mainPane = new JPanel(new CardLayout());
		contentPane = new JPanel();
        contentPane.setBackground(new Color(255, 255, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setSize(125, 686);
		contentPane.setLayout(null);
        //add buttons to access cards
        loginBtn = new JButton("Login");
        loginBtn.setBounds(0, 0, 125, 50);
        loginBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayLogin();
            }
        });
        contentPane.add(loginBtn);
        usersBtn = new JButton("Manage Users");
        usersBtn.setBounds(0, 100, 125, 50);
        usersBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayUsers();
            }
        });
        contentPane.add(usersBtn);
        logTaskBtn = new JButton("Log Tasks");
        logTaskBtn.setBounds(0, 50, 125, 50);
        logTaskBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayLogTasks();
            }
        });
        contentPane.add(logTaskBtn);

        //collect all JPanels together
        //empty card
        JPanel empty = new JPanel();
        empty.setBackground(new Color(255, 255, 255));
		empty.setBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(119, 136, 153), new Color(192, 192, 192)));
		empty.setLayout(null);
        mainPane.add(empty, "Main Menu");
        //create other JPanels
        loginWindow = new LoginUI(this, user);
        usersWindow = new ManageUsersUI(this, user, db);
        logTasksWindow = new TaskLogUI(this, loggingTask, db);
        //add all other JPanels to main CardLayout JPanel
		mainPane.add(loginWindow, "Login");
		mainPane.add(usersWindow, "Users");
		mainPane.add(logTasksWindow, "LogTasks");
        //assign to master Pane
        //set constraints of GridBagLayout
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1;
        gbc.weightx = 0.11;
        gbc.gridwidth = 5;
        gbc.gridheight = 1;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbl.setConstraints(contentPane, gbc);
        add(contentPane);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1;
        gbc.weightx = 0.89;
        gbc.gridwidth = 39;
        gbc.gridheight = 1;
        gbc.gridx = 5;
        gbc.gridy = 0;
        gbl.setConstraints(mainPane, gbc);
        add(mainPane);
        //create CardLayout once done
        cardLayout = (CardLayout) mainPane.getLayout();
        //make sure main menu is displayed first (may change to login menu)
        displayMain();
        displayLogin();
    }

    public void displayLogin() {
        if (user.getLogin()) {
            user.logout();
            usersBtn.setEnabled(false);
            logTaskBtn.setEnabled(false);
            loginBtn.setText("Login");
        }
		setTitle("Login");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		cardLayout.show(mainPane, "Login");
    }
    public void displayUsers() {
		setTitle("Manage Users");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		cardLayout.show(mainPane, "Users");
    }
    public void displayLogTasks() {
		setTitle("Log Tasks");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		cardLayout.show(mainPane, "LogTasks");
    }
    public void displayMain() {
        setTitle("Main Menu");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
