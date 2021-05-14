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
        mainPane.add(contentPane, "Main Menu");
        loginWindow.setWindow(this);
        usersWindow.setWindow(this);
        getContentPane().add(mainPane);
        cardLayout = (CardLayout) mainPane.getLayout();
        displayMain();
    }//craft a main menu to default to

    public JPanel getPane() {
        return mainPane;
    }
    public CardLayout getCardLayout() {
        return cardLayout;
    }
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
        if (user.getLogin()) {
            usersBtn.setEnabled(true);
        } else {
            usersBtn.setEnabled(false);
        }
		cardLayout.show(mainPane, "Main Menu");
    }
}
