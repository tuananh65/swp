package dal;

import model.User;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import utils.PasswordUtils;

public class UserDAOFullTest {

    public static void main(String[] args) {
        UserDAO dao = new UserDAO();

        System.out.println("\n=== ✅ TEST REGISTER ===");
        String uniqueEmail = "test" + System.currentTimeMillis() + "@example.com";
        User newUser = new User();
        newUser.setUserName("testuser");
        newUser.setPassword("MySecurePass123"); // Plain text
        newUser.setFullName("Test User");
        newUser.setGender("Male");
        newUser.setEmail(uniqueEmail);
        newUser.setPhone("0123456789");

        boolean registerSuccess = dao.register(newUser, 1, UUID.randomUUID().toString());
        
        System.out.println("✅ Register success: " + registerSuccess);

        // NOTE: User will be is_activated = false until activate
        System.out.println("\n=== ✅ GET ACTIVATION TOKEN FROM DB ===");
User registeredUser = dao.getUserByEmail(uniqueEmail);
if (registeredUser == null) {
    System.out.println("❌ Không tìm thấy user sau khi register!");
    return;
}
String activationToken = registeredUser.getActivationToken();
System.out.println("✅ Activation token: " + activationToken);

System.out.println("\n=== ✅ TEST ACTIVATE ===");
boolean activated = dao.activateAccount(activationToken);
System.out.println("✅ Activate account: " + activated);


        System.out.println("\n=== ✅ TEST LOGIN (should succeed) ===");
        User loggedIn = dao.login(uniqueEmail, "MySecurePass123");
        if (loggedIn != null) {
            System.out.println("✅ Login success: " + loggedIn.getFullName() + " | Email: " + loggedIn.getEmail());
        } else {
            System.out.println("❌ Login failed!");
        }

        System.out.println("\n=== ✅ TEST UPDATE PASSWORD ===");
        String newPassword = "MyNewSecurePass456";
        boolean passwordUpdated = dao.updatePassword(uniqueEmail, newPassword);
        System.out.println("✅ Password updated: " + passwordUpdated);

        System.out.println("\n=== ✅ TEST LOGIN WITH NEW PASSWORD ===");
        User loggedInNew = dao.login(uniqueEmail, newPassword);
        if (loggedInNew != null) {
            System.out.println("✅ Login with new password success: " + loggedInNew.getFullName());
        } else {
            System.out.println("❌ Login with new password failed!");
        }

        System.out.println("\n=== ✅ TEST STORE RESET TOKEN ===");
        String resetToken = UUID.randomUUID().toString();
        Date expiry = new Date(System.currentTimeMillis() + (24 * 60 * 60 * 1000)); // 24h
        boolean tokenStored = dao.storeResetToken(loggedInNew.getUserId(), resetToken, expiry);
        System.out.println("✅ Reset token stored: " + tokenStored);

        System.out.println("\n=== ✅ TEST GET USER BY RESET TOKEN ===");
        User resetUser = dao.getUserByResetToken(resetToken);
        if (resetUser != null) {
            System.out.println("✅ Found user by reset token: " + resetUser.getEmail());
        } else {
            System.out.println("❌ User not found by reset token!");
        }

        System.out.println("\n=== ✅ TEST RESET PASSWORD ===");
        String resetNewPassword = "FinalPassword789";
        boolean resetPass = dao.resetPassword(loggedInNew.getUserId(), resetNewPassword);
        System.out.println("✅ Reset password success: " + resetPass);

        System.out.println("\n=== ✅ TEST LOGIN AFTER RESET PASSWORD ===");
        User resetLoggedIn = dao.login(uniqueEmail, resetNewPassword);
        if (resetLoggedIn != null) {
            System.out.println("✅ Login with reset password success: " + resetLoggedIn.getFullName());
        } else {
            System.out.println("❌ Login with reset password failed!");
        }

        System.out.println("\n=== ✅ TEST CLEAR RESET TOKEN ===");
        boolean clearToken = dao.clearResetToken(loggedInNew.getUserId());
        System.out.println("✅ Clear reset token: " + clearToken);

        System.out.println("\n=== ✅ TEST GET ALL USERS ===");
        List<User> allUsers = dao.getAllUsers();
        System.out.println("Total users in system: " + allUsers.size());
        for (User u : allUsers) {
            System.out.println("- " + u.getUserId() + ": " + u.getEmail());
        }

        System.out.println("\n✅✅✅ ALL TESTS COMPLETED ✅✅✅");
        String userInput = "Minhdz14092005@";
String hashInDB = "$2a$12$VJ7FSAoz2otxFSN6DnpGm.Xjk1ebo2WsDZySKq.SyKFx66TqVpBTW";

boolean check = PasswordUtils.checkPassword(userInput, hashInDB);
System.out.println("✅ Check result: " + check);

    }
}
