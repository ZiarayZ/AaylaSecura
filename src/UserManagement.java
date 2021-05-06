import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.regex.Pattern;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.SecureRandom;
import java.math.BigInteger;
import java.nio.CharBuffer;

public class UserManagement {
    private int user_id;
    private String username;
    private String name;
    //access level 0 - can only login
    private int perms = 0;
    private String job;
    private String gender;
    //forces user to login
    private boolean logged_in = false;
    private static database userDB;
    //connect to login ui
    private LoginUI LoginWindow;
    //connect to manage users ui
    private ManageUsersUI ManageWindow;
    
    public UserManagement(database DB) {
        userDB = DB;
        LoginWindow = new LoginUI(this);
        ManageWindow = new ManageUsersUI(this);
    }
    public void listAll() {
        System.out.println(user_id + "| " + username + " " + name + " " + perms + " " + job + " " + gender + " | is logged in? " + logged_in);
    }
    public void displayLogin() {
        LoginWindow.setVisible(true);
    }
    public void displayUsers() {
        ManageWindow.setVisible(true);
    }

    //grab access level | 0=no access, 1=caretaker, 2=manager, 3=admin etc (plan to add these values into jobs table later)
    //may update to RBAC if i have the time and knowledge
    public int accessLevel() {
        return perms;
    }
    //getters
    public String getUsername() {
        return username;
    }
    public String getName() {
        return name;
    }
    public String getJob() {
        return job;
    }
    public boolean getLogin() {
        return logged_in;
    }
    public String getGender() {
        if (gender.toUpperCase().equals("M")) {
            return "Male";
        } else if (gender.toUpperCase().equals("F")) {
            return "Female";
        } else {
            return gender;
        }
    }

    //edit name
    public String editName(String user_name) throws SQLException {
        if (user_name.equals(null) || user_name.equals("")) {
            return "empty/null";
        } else {
            //i would add more validation but names can be ANYTHING
            return userDB.editUser(user_id, "user_name", user_name);
        }
    }
    //edit password
    public String editPassword(char[] password) throws SQLException {
        if (password.equals(null) || password.length == 0) {
            return "empty/null";
        } else {
            try {
                String result = genPassHash(password);
                return userDB.editUser(user_id, "hash_password", result);
            } catch (NoSuchAlgorithmException e) {
                System.out.println(e);
                return "NoSuchAlgorithmException";
            } catch (InvalidKeySpecException e) {
                System.out.println(e);
                return "InvalidKeySpecException";
            }
        }
    }
    //edit gender
    public String editGender(String gen) throws SQLException {
        if (gen.equals(null) || gen.equals("")) {
            return "empty/null";
        } else if (gen.matches("[MmFf]")) {
            return userDB.editUser(user_id, "M_F", gen.toUpperCase());
        } else {
            return "failed verification";
        }
    }

    //add user to database if manager/admin+
    public boolean addUser(String new_name, String new_username, int new_job, char[] password, String new_notes, String new_gender) throws SQLException {
        if (perms > 2) {
            String newPass = "";
            try {
                newPass = genPassHash(password);
            } catch (NoSuchAlgorithmException e) {
                System.out.println(e);//replace with error prompt in UI
                return false;
            } catch (InvalidKeySpecException e) {
                System.out.println(e);//replace with error prompt in UI
                return false;
            }
            if (!newPass.equals("")) {
                String problem = userDB.addNewUser(new_name, new_username, new_job, newPass, new_notes, new_gender);
                if (problem.equals("")) {
                    return true;
                } else {
                    System.out.println(problem);
                    return false;
                }
            } else {
                //just incase somehow
                return false;
            }
        } else {
            return false;
        }
    }
    //edit user in database if manager/admin+
    public boolean editUser() {
        if (perms>=2) {
            return true;
        }
        return false;
    }
    //delete user in database if manager/admin+
    public boolean deleteUser() {
        if (perms>=2) {
            return true;
        }
        return false;
    }

    //log in method
    public boolean login(String user_name, char[] password) throws SQLException {
        //grab hashed password
        ResultSet query = userDB.getPassFromUsername(user_name);
        if (query.next()) {
            //check password with hashed password
            try {
                if (validatePassword(password, query.getString("hash_password"))) {
                    logged_in = true;
                }
            } catch (NoSuchAlgorithmException e) {
                System.out.println(e);
                return false;
            } catch (InvalidKeySpecException e) {
                System.out.println(e);
                return false;
            }
        }
        //set up user attributes if successful
        if (logged_in) {
            query = userDB.getUserFromUsername(user_name);
            if (query.next()) {
                user_id = query.getInt("user_id");
                username = user_name;
                name = query.getString("user_name");
                gender = query.getString("m_f");
                job = query.getString("job_desc");
            }
            //login success
            return true;
        } else {
            //login fail
            return false;
        }
    }

    //password validation
    private static boolean validatePassword(char[] original, String stored) throws NoSuchAlgorithmException, InvalidKeySpecException {
        //split stored hash apart
        String[] parts = stored.split(":");
        int iter = Integer.parseInt(parts[0]);
        byte[] salt = fromHex(parts[1]);
        byte[] hash = fromHex(parts[2]);

        //hash original using stored
        PBEKeySpec keySpec = new PBEKeySpec(original, salt, iter, hash.length*8);
        SecretKeyFactory secKeyFact = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] testHash = secKeyFact.generateSecret(keySpec).getEncoded();
        
        //compare hashes
        int diff = hash.length ^ testHash.length;
        for (int i=0; i<hash.length && i<testHash.length; i++) {
            diff |= hash[i] ^ testHash[i];
        }
        return diff == 0;
    }
    private static byte[] fromHex(String hex) throws NoSuchAlgorithmException {
        //convert from hex
        byte[] bytes = new byte[hex.length()/2];
        for (int i=0; i<bytes.length; i++) {
            bytes[i] = (byte)Integer.parseInt(hex.substring(2*i, 2*i+2), 16);
        }
        return bytes;
    }

    //password hashing
	public static String genPassHash(char[] password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        //use 1000 iterations
		int iter = 1000;
		char[] chars = password;
		byte[] salt = genSalt();
        
        //create hashed password
		PBEKeySpec keySpec = new PBEKeySpec(chars, salt, iter, 64*8);
		SecretKeyFactory secKeyFact = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		byte[] hash = secKeyFact.generateSecret(keySpec).getEncoded();
		
        //combine these elements
		return iter+":"+toHex(salt)+":"+toHex(hash);
	}
	private static byte[] genSalt() throws NoSuchAlgorithmException {
        //create or generate a salt
		SecureRandom secRand = SecureRandom.getInstance("SHA1PRNG");
		byte[] salt = new byte[16];
		secRand.nextBytes(salt);
		return salt;
	}
	private static String toHex(byte[] array) throws NoSuchAlgorithmException {
        //convert to hex
        BigInteger bigInt = new BigInteger(1, array);
        String hex = bigInt.toString(16);
        int paddLength = (array.length*2)-hex.length();

        if (paddLength>0) {
            return String.format("%0" +paddLength+"d", 0)+hex;
        } else {
            return hex;
        }
	}

    //password verification
    public static boolean verifyPassword(char[] password) {
        //doesn't contain a single upper case: "(?=.*[A-Z]).*"
        if (!Pattern.matches("(?=.*[A-Z]).*", CharBuffer.wrap(password))) {
            return false;
        }
        //doesn't contain a single lower case: "(?=.*[a-z]).*"
        if (!Pattern.matches("(?=.*[a-z]).*", CharBuffer.wrap(password))) {
            return false;
        }
        //doesn't contain a single number: "(?=.*[0-9]).*"
        if (!Pattern.matches("(?=.*[0-9]).*", CharBuffer.wrap(password))) {
            return false;
        }
        //doesn't contain a single symbol: "(?=.*\\W).*"
        if (!Pattern.matches("(?=.*\\W).*", CharBuffer.wrap(password))) {
            return false;
        }
        //make sure password is of valid strength
        if (passwordStrength(password)<60) {
            return false;
        }
        return true;
    }
    private static float passwordStrength(char[] password) {
        float score = 0;

        //if no password return 0
        if (password.equals(null) || password.length == 0) {
            return score;
        }

        //record every character in password, repetitions diminish value from 5
        HashMap<Character,Integer> letters = new HashMap<Character,Integer>();
        char chr;
        for (int i=0; i<password.length; i++) {
            chr = password[i];
            if (letters.containsKey(chr)) {
                letters.put(chr, letters.get(chr)+1);
            } else {
                letters.put(chr, 1);
            }
            if (letters.get(chr) - password.length*30/100 > 1) {
                score += 5.0 / letters.get(chr) - password.length*30/100;
            } else {
                score += 5.0;
            }
        }

        //bonus for mixing things
        int varCount = 0;
        if (Pattern.matches("(?=.*[a-z]).*", CharBuffer.wrap(password))) {
            varCount += 1;
        }
        if (Pattern.matches("(?=.*[A-Z]).*", CharBuffer.wrap(password))) {
            varCount += 1;
        }
        if (Pattern.matches("(?=.*[0-9]).*", CharBuffer.wrap(password))) {
            varCount += 1;
        }
        if (Pattern.matches("(?=.*\\W).*", CharBuffer.wrap(password))) {
            varCount += 1;
        }
        score += (varCount - 1) * 10;

        //adding length into it
        score -= 16 - password.length;
        return score;
    }
}
