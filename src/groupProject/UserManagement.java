package groupProject;

import java.sql.ResultSet;
import java.sql.SQLException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.SecureRandom;
import java.math.BigInteger;

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
    private database userDB;

    public UserManagement(database DB) {
        userDB = DB;
    }

    //to test passwords, and if the password methods work correctly
    public static void testPassword(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        if (verifyPassword(password)) {
            System.out.println("Verification success.");
            System.out.println(validatePassword(password, genPassHash(password)));
            System.out.println(passwordStrength(password));
        } else {
            System.out.println("Verification failed.");
            System.out.println(passwordStrength(password));
        }
    }

    //grab access level | 0=no access, 1=caretaker, 2=manager, 3=admin etc (plan to add these values into jobs table later)
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
    public String editPassword(String password) throws SQLException {
        if (password.equals(null) || password.equals("")) {
            return "empty/null";
        } else if (verifyPassword(password)) {
            try {
                String result = genPassHash(password);
                return userDB.editUser(user_id, "password", result);
            } catch (NoSuchAlgorithmException e) {
                System.out.println(e);
                return "NoSuchAlgorithmException";
            } catch (InvalidKeySpecException e) {
                System.out.println(e);
                return "InvalidKeySpecException";
            }
        } else {
            return "failed verification";
        }
    }
    //edit gender
    public String editGender(String gen) throws SQLException {
        if (gen.equals(null) || gen.equals("")) {
            return "empty/null";
        } else if (gen.matches("[MmFf]")) {
            return userDB.editUser(user_id, "m_f", gen.toUpperCase());
        } else {
            return "failed verification";
        }
    }

    //add user to database if manager/admin+
    public boolean addUser() {
        if (perms>=2) {
            return true;
        }
        return false;
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
    public boolean login(String user_name, String password) throws NoSuchAlgorithmException, InvalidKeySpecException, SQLException {
        //grab hashed password
        ResultSet query = userDB.getPassFromUsername(user_name);
        if (query.next()) {
            //check password with hashed password
            if (validatePassword(password, query.getString("password"))) {
                logged_in = true;
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
    private static boolean validatePassword(String original, String stored) throws NoSuchAlgorithmException, InvalidKeySpecException {
        //split stored hash apart
        String[] parts = stored.split(":");
        int iter = Integer.parseInt(parts[0]);
        byte[] salt = fromHex(parts[1]);
        byte[] hash = fromHex(parts[2]);

        //hash original using stored
        PBEKeySpec keySpec = new PBEKeySpec(original.toCharArray(), salt, iter, hash.length*8);
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
	private static String genPassHash(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        //use 1000 iterations
		int iter = 1000;
		char[] chars = password.toCharArray();
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
    private static boolean verifyPassword(String password) {
        //to do: add character requirements for password
        //make sure password is of valid strength
        if (passwordStrength(password)>=5) {
            return true;
        } else {
            return false;
        }
    }
    private static int passwordStrength(String password) {
        //strength of password on a scale of 0-10
        int score = 0;

        //total length 8-10 or 11+
        if (password.length()>10) {
            score += 2;
        } else if (password.length()>=8) {
            score += 1;
        } else {
            return 0;
        }

        //contains 1 or 2 lower chars
        if (password.matches("(?=.*[a-z].*[a-z]).*")) {
            score += 2;
        } else if (password.matches("(?=.*[a-z]).*")) {
            score += 1;
        }

        //contains 1 or 2 upper chars
        if (password.matches("(?=.*[A-Z].*[A-Z]).*")) {
            score += 2;
        } else if (password.matches("(?=.*[A-Z]).*")) {
            score += 1;
        }

        //contains 1 or 2 digit chars
        if (password.matches("(?=.*[0-9].*[0-9]).*")) {
            score += 2;
        } else if (password.matches("(?=.*[0-9]).*")) {
            score += 1;
        }

        //contains 1 or 2 special chars ~!@#$%^&*()_-
        if (password.matches("(?=.*[~!@#$%^&*()_-].*[~!@#$%^&*()_-]).*")) {
            score += 2;
        } else if (password.matches("(?=.*[~!@#$%^&*()_-]).*")) {
            score += 1;
        }

        return score;
    }
}
