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
        String sql = "SELECT * FROM [User] WHERE Email = ? AND Password = ? AND is_activated = 1";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, email);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("UserID"));
                user.setUserName(rs.getString("UserName"));
                user.setPassword(rs.getString("Password"));
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
                user.setActivationToken(rs.getString("activation_token"));
                user.setActivated(rs.getBoolean("is_activated"));
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean register(User user, int roleId, String activationToken) {
        String sql = "INSERT INTO [User] (UserName, Password, RoleId, FullName, Gender, Email, Phone, Status, CreatedAt, activation_token, is_activated) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, user.getUserName());
            ps.setString(2, user.getPassword());
            ps.setInt(3, roleId);
            ps.setString(4, user.getFullName());
            ps.setString(5, user.getGender());
            ps.setString(6, user.getEmail());
            ps.setString(7, user.getPhone());
            ps.setString(8, "Active");
            ps.setTimestamp(9, new Timestamp(new Date().getTime()));
            ps.setString(10, activationToken);
            ps.setBoolean(11, false);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isEmailExists(String email) {
        String sql = "SELECT * FROM [User] WHERE Email = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean activateAccount(String token) {
        String sql = "UPDATE [User] SET is_activated = 1, activation_token = NULL WHERE activation_token = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, token);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean updatePassword(String email, String password) {
        String sql = "UPDATE [User] SET Password = ? WHERE Email = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, password);
            ps.setString(2, email);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}