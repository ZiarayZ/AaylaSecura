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
    private int perms;
    private String job;
    private String gender;
    private String notes;
    private boolean logged_in = false;

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

    //log in method
    public boolean login(String username, String password) {
        return false;
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
