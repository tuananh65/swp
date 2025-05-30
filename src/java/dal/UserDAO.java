package dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Date;
import model.Users;


public class UserDAO extends DBContext {

   Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    public Users login(String email, String password) {
        String query = "select* from [User]\n"
                + "where email = ? and password = ?";
        try {
            conn = new DBContext().getConnection();//mo ket noi voi sql
            ps = conn.prepareStatement(query);
            ps.setString(1, email);
            ps.setString(2, password);
            rs = ps.executeQuery();
            if (rs.next()) {
                return new Users(rs.getInt(1),          // UserId
    rs.getString(2),       // UserName
    rs.getString(3),       // Password
    rs.getInt(4),          // RoleId
    rs.getString(5),       // FullName
    rs.getDate(6),         // DateOfBirth
    rs.getString(7),       // AvatarUrl
    rs.getString(8),       // Email
    rs.getString(9),       // Phone
    rs.getString(10),      // Address
    rs.getString(11),      // Status
    rs.getTimestamp(12),   // CreatedAt
    rs.getString(13));       // Gender
   
            }
        } catch (Exception e) {
        }
        return null;
    }
    public Users getUserByUsernameAndEmail(String username, String email) {
    String query = "SELECT * FROM [User] WHERE UserName = ? AND Email = ?";
    try {
        conn = new DBContext().getConnection();
        ps = conn.prepareStatement(query);
        ps.setString(1, username);
        ps.setString(2, email);
        rs = ps.executeQuery();
        if (rs.next()) {
            return new Users(rs.getInt(1), rs.getString(2), rs.getString(3), 
                           rs.getInt(4), rs.getString(5), rs.getDate(6), 
                           rs.getString(7), rs.getString(8), rs.getString(9), 
                           rs.getString(10), rs.getString(11), rs.getTimestamp(12), 
                           rs.getString(13));
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return null;
}

public boolean updatePassword(int userId, String newPassword) {
    String query = "UPDATE [User] SET Password = ? WHERE UserID = ?";
    try {
        conn = new DBContext().getConnection();
        ps = conn.prepareStatement(query);
        ps.setString(1, newPassword);
        ps.setInt(2, userId);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected > 0;
    } catch (Exception e) {
        e.printStackTrace();
    }
    return false;
}
    
    public boolean storeResetToken(int userId, String resetToken, Date expiryDate) {
        String query = "UPDATE [User] SET reset_token = ?, token_expiry = ? WHERE UserID = ?";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, resetToken);
            ps.setTimestamp(2, new Timestamp(expiryDate.getTime()));
            ps.setInt(3, userId);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public Users getUserByResetToken(String resetToken) {
        String query = "SELECT * FROM [User] WHERE reset_token = ? AND token_expiry > GETDATE()";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, resetToken);
            rs = ps.executeQuery();
            if (rs.next()) {
                return new Users(rs.getInt(1), rs.getString(2), rs.getString(3), 
                               rs.getInt(4), rs.getString(5), rs.getDate(6), 
                               rs.getString(7), rs.getString(8), rs.getString(9), 
                               rs.getString(10), rs.getString(11), rs.getTimestamp(12), 
                               rs.getString(13));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean clearResetToken(int userId) {
        String query = "UPDATE [User] SET reset_token = NULL, token_expiry = NULL WHERE UserID = ?";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, userId);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

public static void main(String[] args) {
        UserDAO dao = new UserDAO();
        Users user = dao.login("admin1@example.com","admin@123");
        
        System.out.println(user);
    }
}