package dao;

import model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

public class UserDAO {
    private Connection conn;

    public UserDAO() {
        conn = DBContext.getInstance().getConnection();
    }
    
    public User login(String email, String password) {
        String sql = "select * from [User] where Email = ? and Password = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, email);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("UserID"));
                user.setUserName(rs.getString("UserName"));
                user.setPassword(rs.getString("Password")); // Note: Case-sensitive column name
                user.setRoleId(rs.getInt("RoleId"));
                user.setFullName(rs.getString("FullName"));
                user.setGender(rs.getString("Gender"));
                user.setDateOfBirth(rs.getDate("DateOfBirth"));
                user.setAvatarUrl(rs.getString("AvatarUrl"));
                user.setEmail(rs.getString("Email"));
                user.setPhone(rs.getString("Phone"));
                user.setAddress(rs.getString("Address"));
                user.setStatus(rs.getString("Status"));
                user.setCreatedAt(rs.getDate("CreatedAt"));
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public boolean updatePassword(String email, String password) {
    String sql = "UPDATE [User] SET Password = ? WHERE Email = ?";
    try {
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, password); // Nên băm mật khẩu tại đây
        ps.setString(2, email);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected > 0;
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}
    
    public static void main(String[] args) {
        UserDAO dao = new UserDAO();
        User user = dao.login("admin1@example.com","admin@123");
        
        System.out.println(user);
    }
}