package dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import model.Role;
import model.User;


public class RoleDAO extends DBContext {

    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    public int getRoleIdByName(String roleName) {
        String sql = "SELECT RoleId FROM [Role] WHERE RoleName = ?";
        try {conn = new DBContext().getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, roleName);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("RoleId");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Trả về -1 nếu không tìm thấy
    }
    
    public Role getRoleByID(int roleID) {
        String sql = "SELECT * FROM Role WHERE RoleID = ?";
        try {conn = new DBContext().getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, roleID);
            rs = ps.executeQuery();
            if (rs.next()) {
                return new Role(rs.getInt("RoleID"), rs.getString("RoleName"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // Không tìm thấy hoặc lỗi
    }

    public static void main(String[] args) {
    RoleDAO dao = new RoleDAO();

    // Test 1: Lấy RoleId theo tên
    String roleName = "User";  // Bạn có thể thay bằng "Admin", "Instructor" nếu schema có
    int roleId = dao.getRoleIdByName(roleName);
    System.out.println("Role name: " + roleName);
    System.out.println("→ Role ID: " + (roleId != -1 ? roleId : "❌ Không tìm thấy"));

    // Test 2: Lấy Role theo ID
    if (roleId != -1) {
        Role role = dao.getRoleByID(roleId);
        System.out.println("→ Role object: " + (role != null ? role : "❌ Không tìm thấy"));
    }

    // Test 3: Lấy role với ID không tồn tại
    Role notFound = dao.getRoleByID(999); // Giả định 999 không tồn tại
    System.out.println("\nTest ID không tồn tại (999): " + (notFound != null ? notFound : "✅ Không có Role với ID 999"));
}

    }