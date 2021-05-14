import java.awt.Color;
import java.awt.CardLayout;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class UserInterface extends JFrame {
    private LoginUI loginWindow;
    private ManageUsersUI usersWindow;
    private UserManagement user;
    private JPanel contentPane;
    private JPanel mainPane;
    private JButton usersBtn;
    private CardLayout cardLayout;
    
    public UserInterface(database db, UserManagement newUser) {
        user = newUser;
        loginWindow = new LoginUI(user);
        usersWindow = new ManageUsersUI(user, db);
		mainPane = new JPanel(new CardLayout());
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
        //add all JPanels to main CardLayout JPanel
        mainPane.add(contentPane, "Main Menu");
        loginWindow.setWindow(this);
        usersWindow.setWindow(this);
		mainPane.add(loginWindow, "Login");
		mainPane.add(usersWindow, "Users");
        //assign master Pane
        getContentPane().add(mainPane);
        cardLayout = (CardLayout) mainPane.getLayout();
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
