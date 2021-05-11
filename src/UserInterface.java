import javax.swing.JFrame;
import javax.swing.JPanel;

public class UserInterface extends JFrame {
    private LoginUI loginWindow;
    private ManageUsersUI usersWindow;
    private JPanel contentPane;
    
    public UserInterface(database db, UserManagement user) {
        loginWindow = new LoginUI(user);
        usersWindow = new ManageUsersUI(user, db);
        setTitle("Main Menu");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 534, 363);
		setContentPane(contentPane);
    }//craft a main menu to default to

    public void displayLogin() {
        loginWindow.setWindow(this);
    }
    public void displayUsers() {
        usersWindow.setWindow(this);
    }
}
