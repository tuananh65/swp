package dal;

import static dal.DBContext.getConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.User;

public class UserDAO {

    // Lấy user theo ID
    public User getUserById(int userID) {
        String sql = "SELECT * FROM [User] WHERE UserId = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return extractUserFromResultSet(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Lấy tất cả user
    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
        String sql = "SELECT * FROM [User]";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(extractUserFromResultSet(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Cập nhật user
    public void updateUser(User user) {
        String sql = "UPDATE [User] SET UserName = ?, Password = ?, RoleId = ?, FullName = ?, Gender = ?, DateOfBirth = ?, AvatarUrl = ?, Email = ?, Phone = ?, Address = ?, Status = ?, CreatedAt = ? WHERE UserId = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getUserName());
            ps.setString(2, user.getUserPassword());
            ps.setInt(3, user.getRoleID());
            ps.setString(4, user.getUserFullName());
            ps.setString(5, user.getUserGender());

            // Với Date, dùng setDate hoặc setTimestamp tùy kiểu cột trong DB
            if (user.getDateOfBirth() != null) {
                ps.setDate(6, new java.sql.Date(user.getDateOfBirth().getTime()));
            } else {
                ps.setNull(6, java.sql.Types.DATE);
            }

            ps.setString(7, user.getUserAvatarUrl());
            ps.setString(8, user.getUserEmail());
            ps.setString(9, user.getUserPhone());
            ps.setString(10, user.getUserAddress());
            ps.setString(11, user.getUserStatus());

            if (user.getCreatedAt() != null) {
                ps.setTimestamp(12, new java.sql.Timestamp(user.getCreatedAt().getTime()));
            } else {
                ps.setNull(12, java.sql.Types.TIMESTAMP);
            }

            ps.setInt(13, user.getUserID());

            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Xóa user theo ID
    public void deleteUser(int userID) {
        String sql = "DELETE FROM [User] WHERE UserId = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userID);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Helper: chuyển ResultSet thành User
    private User extractUserFromResultSet(ResultSet rs) throws SQLException {
   

        User user = new User();
        user.setUserID(rs.getInt("UserId"));
        user.setUserName(rs.getString("UserName"));
        user.setUserPassword(rs.getString("Password"));
        user.setRoleID(rs.getInt("RoleId"));
        user.setUserFullName(rs.getString("FullName"));
        user.setUserGender(rs.getString("Gender"));
        user.setDateOfBirth(rs.getDate("DateOfBirth")); // dùng getDate() trả về java.sql.Date kế thừa java.util.Date
        user.setUserAvatarUrl(rs.getString("AvatarUrl"));
        user.setUserEmail(rs.getString("Email"));
        user.setUserPhone(rs.getString("Phone"));
        user.setUserAddress(rs.getString("Address"));
        user.setUserStatus(rs.getString("Status"));
        user.setCreatedAt(rs.getTimestamp("CreatedAt")); // dùng getTimestamp cho trường datetime/timestamp
        return user;
    }

    // Lấy user theo username và password (để login)
    public User getUserByUsernameAndPassword(String username, String password) {
        String sql = "SELECT * FROM [User] WHERE UserName = ? AND Password = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extractUserFromResultSet(rs);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
}
