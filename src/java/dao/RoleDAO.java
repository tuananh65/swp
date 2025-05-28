package dao;

import model.Role;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RoleDAO {
    private Connection conn;

    public RoleDAO() {
        conn = DBContext.getInstance().getConnection();
    }

    // Lấy RoleId dựa trên RoleName
    public int getRoleIdByName(String roleName) {
        String sql = "SELECT RoleId FROM [Role] WHERE RoleName = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, roleName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("RoleId");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Trả về -1 nếu không tìm thấy
    }
}