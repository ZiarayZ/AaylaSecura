import javax.swing.JFrame;

public class UserInterface extends JFrame {
    private LoginUI loginWindow;
    private ManageUsersUI usersWindow;
    
    public UserInterface(database db, UserManagement user) {
        loginWindow = new LoginUI(user);
        usersWindow = new ManageUsersUI(user, db);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }//craft a main menu to default to

    public void displayLogin() {
        loginWindow.setWindow(this);
    }
    public void displayUsers() {
        usersWindow.setWindow(this);
    }
}
