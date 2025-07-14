package dal;
import model.User;
import utils.PasswordUtils;
import java.util.List;

public class MigratePlaintextPasswords {
    public static void main(String[] args) {
        UserDAO dao = new UserDAO();

        // 1️⃣ Load tất cả user trong DB
        List<User> users = dao.getAllUsers();
        System.out.println("Total users found: " + users.size());

        int migratedCount = 0;

        // 2️⃣ Lặp qua từng user
        for (User u : users) {
            String currentPass = u.getPassword();

            // 3️⃣ Kiểm tra nếu password đã HASH chưa
            if (currentPass.startsWith("$2a$") || currentPass.startsWith("$2b$")) {
                // BCrypt hash rồi → bỏ qua
                System.out.println("✅ User " + u.getEmail() + " đã hash rồi. Bỏ qua.");
                continue;
            }

            // 4️⃣ Hash password plaintext
            String hashed = PasswordUtils.hashPassword(currentPass);

            // 5️⃣ Update vào DB
            boolean updated = dao.updatePassword(u.getEmail(), hashed);
            if (updated) {
                System.out.println("✅ Đã hash và update cho user: " + u.getEmail());
                migratedCount++;
            } else {
                System.out.println("❌ Update thất bại cho user: " + u.getEmail());
            }
        }

        System.out.println("\n=== TỔNG SỐ USER ĐÃ MIGRATE: " + migratedCount + " ===");
    }
}
