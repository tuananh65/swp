package utils;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Utility class for password hashing and verification using BCrypt.
 */
public class PasswordUtils {

    /**
     * Hash a plaintext password using BCrypt with strength 12.
     *
     * @param plainTextPassword the password to hash
     * @return hashed password
     */
    public static String hashPassword(String plainTextPassword) {
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt(12));
    }

    /**
     * Verify a plaintext password against a hashed one.
     *
     * @param plainTextPassword user-provided password
     * @param hashedPassword    password stored in DB
     * @return true if match, false otherwise
     */
    public static boolean checkPassword(String plainTextPassword, String hashedPassword) {
        if (hashedPassword == null) return false;
        return BCrypt.checkpw(plainTextPassword, hashedPassword);
    }
}
