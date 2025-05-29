package dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import model.Role;

public class RoleDAO extends DBContext {

    public Role getRoleByID(int roleID) {
        String sql = "SELECT * FROM Role WHERE RoleID = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
             
            ps.setInt(1, roleID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Role(rs.getInt("RoleID"), rs.getString("RoleName"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // Không tìm thấy hoặc lỗi
    }
}
