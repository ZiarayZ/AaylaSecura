import java.awt.Color;
import java.awt.Dimension;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.LayoutManager;
import java.awt.GridLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

public class UserInterface extends JFrame {
    private UserManagement user;
    private JPanel mainPane;
    private JButton loginBtn;
    private JButton usersBtn;
    private JButton taskBtn;
    private JButton editTaskBtn;
    private JButton logTaskBtn;
    private JFrame errorFrame = new JFrame("Error!");
    private JPanel errorPane = new JPanel();
    private CardLayout cardLayout;
    
    public UserInterface(database db, UserManagement newUser, LogTasks loggingTask, taskEntry myTE) {
        user = newUser;
        //define inside here, not used elsewhere
        LoginUI loginWindow;
        ManageUsersUI usersWindow;
        TaskLogUI logTasksWindow;
        TaskEntryUI enterTasksWindow;
        //TaskAssignUI assignTasksWindow;

        //setup errorPane
        LayoutManager layout = new FlowLayout();
        errorPane.setLayout(layout);
        errorFrame.getContentPane().add(errorPane, BorderLayout.CENTER);

        //setup accessPane
        JPanel accessPane = new JPanel();
        accessPane.setLayout(new GridLayout());
        JLabel accessMessage = new JLabel("You are not permitted to access this.");
        accessMessage.setForeground(new Color(105, 105, 105));
	    accessMessage.setOpaque(true);
		accessMessage.setFont(new Font("Verdana", Font.PLAIN, 28));
		accessMessage.setHorizontalAlignment(SwingConstants.CENTER);
		accessMessage.setBackground(new Color(224, 255, 255));
        accessPane.add(accessMessage);

        //create master pane and main menu pane
        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        setLayout(gbl);
		setBounds(100, 100, 1150, 775);
        setMinimumSize(new Dimension(1150, 775));
		mainPane = new JPanel(new CardLayout());
		JPanel contentPane = new JPanel();
        contentPane.setBackground(new Color(255, 255, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setSize(125, 686);
        contentPane.setPreferredSize(new Dimension(125, 686));
        contentPane.setMinimumSize(new Dimension(125, 686));
        contentPane.setMaximumSize(new Dimension(125, 686));
		contentPane.setLayout(null);

        //add buttons to access cards
        //Login
        loginBtn = new JButton("Login");
        loginBtn.setBounds(0, 0, 125, 50);
        loginBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayLogin();
            }
        });
        contentPane.add(loginBtn);

        //Allocate Tasks
        taskBtn = new JButton("Allocate Tasks");
        taskBtn.setBounds(0, 50, 125, 50);
        taskBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayAllocateTasks();
            }
        });
        contentPane.add(taskBtn);

        //Edit Tasks
        editTaskBtn = new JButton("Edit Tasks");
        editTaskBtn.setBounds(0, 100, 125, 50);
        editTaskBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayEditTasks();
            }
        });
        contentPane.add(editTaskBtn);

        //Log Tasks
        logTaskBtn = new JButton("Log Tasks");
        logTaskBtn.setBounds(0, 150, 125, 50);
        logTaskBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayLogTasks();
            }
        });
        contentPane.add(logTaskBtn);

        //Manage Users
        usersBtn = new JButton("Manage Users");
        usersBtn.setBounds(0, 200, 125, 50);
        usersBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayUsers();
            }
        });
        contentPane.add(usersBtn);
        
        //collect all JPanels together
        //empty card
        JPanel empty = new JPanel();
        empty.setBackground(new Color(255, 255, 255));
		empty.setBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(119, 136, 153), new Color(192, 192, 192)));
		empty.setLayout(null);
        mainPane.add(empty, "Main Menu");
        mainPane.add(accessPane, "Access");

        //create other JPanels
        loginWindow = new LoginUI(this, user);
        usersWindow = new ManageUsersUI(this, user, db);
        logTasksWindow = new TaskLogUI(this, loggingTask, db, user);
        enterTasksWindow = new TaskEntryUI(this, myTE, db);
        //allocateTasksWindow = new AllocateTaskUI(this, myTA, db);

        //add all other JPanels to main CardLayout JPanel
		mainPane.add(loginWindow, "Login");
		mainPane.add(usersWindow, "Users");
		mainPane.add(logTasksWindow, "LogTasks");
		mainPane.add(enterTasksWindow, "EditTasks");
        //mainPane.add(allocateTasksWindow, "AllocateTasks");

        //set constraints of GridBagLayout
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridheight = 1;
        gbc.gridwidth = 1;
        gbc.weighty = 1;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbl.setConstraints(contentPane, gbc);
        //assign to master Pane
        add(contentPane);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridheight = 1;
        gbc.gridwidth = 1;
        gbc.weighty = 1;
        gbc.weightx = 1;
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbl.setConstraints(mainPane, gbc);
        //assign to master Pane
        add(mainPane);

        //create CardLayout once done
        cardLayout = (CardLayout) mainPane.getLayout();

        //make sure main menu is displayed first (may change to login menu)
        displayMain();
    }

    //title of error box, message of error box
    public void displayError(String title, String message) {
        JOptionPane.showMessageDialog(errorFrame, message, title, JOptionPane.ERROR_MESSAGE);
    }
    public void show(String name) {
        cardLayout.show(mainPane, name);
    }

    public void displayLogin() {
        if (user.getLogin()) {
            user.logout();
            usersBtn.setEnabled(false);
            logTaskBtn.setEnabled(false);
            taskBtn.setEnabled(false);
            editTaskBtn.setEnabled(false);
            loginBtn.setText("Login");
        }
		setTitle("Login");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		cardLayout.show(mainPane, "Login");
    }
    public void displayUsers() {
		setTitle("Manage Users");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        if (user.getLogin() && user.accessLevel("MU") > 0) {
		    cardLayout.show(mainPane, "Users");
        } else {
            cardLayout.show(mainPane, "Access");
        }
    }
    public void displayLogTasks() {
		setTitle("Log Tasks");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        if (user.getLogin()) {
		    cardLayout.show(mainPane, "LogTasks");
        } else {
            cardLayout.show(mainPane, "Access");
        }
    }
    public void displayAllocateTasks() {
        setTitle("Allocate Tasks");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        if (user.getLogin()) {
		    cardLayout.show(mainPane, "Main Menu");//Dummy button "AssignTasks"
        } else {
            cardLayout.show(mainPane, "Access");
        }
    }
    public void displayEditTasks() {
		setTitle("Edit Tasks");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        if (user.getLogin()) {
		    cardLayout.show(mainPane, "EditTasks");
        } else {
            cardLayout.show(mainPane, "Access");
        }
		
    }
    public void displayMain() {
        setTitle("Main Menu");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        if (user.getLogin()) {
            usersBtn.setEnabled(true);
            logTaskBtn.setEnabled(true);
            taskBtn.setEnabled(true);
            editTaskBtn.setEnabled(true);
            loginBtn.setText("Logout");
        } else {
            usersBtn.setEnabled(false);
            logTaskBtn.setEnabled(false);
            taskBtn.setEnabled(false);
            editTaskBtn.setEnabled(false);
            loginBtn.setText("Login");
        }
		cardLayout.show(mainPane, "Main Menu");
    }
}
