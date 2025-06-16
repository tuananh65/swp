package dal;

import dto.UserWithRoleDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import model.User;


public class UserDAO extends DBContext {

    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    public User login(String email, String password) {
        String sql = "SELECT * FROM [User] WHERE Email = ? AND Password = ? AND is_activated = 1";
        try {conn = new DBContext().getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, email);
            ps.setString(2, password);
            rs = ps.executeQuery();
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
                user.setResetToken(rs.getString("reset_token"));
                user.setTokenExpiry(rs.getTimestamp("token_expiry"));
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
        try {conn = new DBContext().getConnection();
            ps = conn.prepareStatement(sql);
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
        try {conn = new DBContext().getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, email);
            rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean activateAccount(String token) {
        String sql = "UPDATE [User] SET is_activated = 1, activation_token = NULL WHERE activation_token = ?";
        try {conn = new DBContext().getConnection();
            ps = conn.prepareStatement(sql);
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
        try {conn = new DBContext().getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, password);
            ps.setString(2, email);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public User getUserByEmail(String email) {
        String query = "SELECT * FROM [User] WHERE  Email = ?";
        try {conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new User(rs.getInt(1), rs.getString(2), rs.getString(3), 
                                rs.getInt(4), rs.getString(5), rs.getString(6), 
                                rs.getDate(7), rs.getString(8), rs.getString(9), 
                                rs.getString(10), rs.getString(11), rs.getString(12), 
                                rs.getDate(13), rs.getString(14), rs.getBoolean(15),
                                rs.getString(16), rs.getTimestamp(17));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public boolean storeResetToken(int userId, String resetToken, Date expiryDate) {
        String query = "UPDATE [User] SET reset_token = ?, token_expiry = ? WHERE UserID = ?";
        try {conn = new DBContext().getConnection();
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

    public User getUserByResetToken(String resetToken) {
        String query = "SELECT * FROM [User] WHERE reset_token = ? AND token_expiry > GETDATE()";
        try {conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, resetToken);
            rs = ps.executeQuery();
            if (rs.next()) {
                return new User(rs.getInt(1), rs.getString(2), rs.getString(3), 
                                rs.getInt(4), rs.getString(5), rs.getString(6), 
                                rs.getDate(7), rs.getString(8), rs.getString(9), 
                                rs.getString(10), rs.getString(11), rs.getString(12), 
                                rs.getDate(13), rs.getString(14), rs.getBoolean(15),
                                rs.getString(16), rs.getTimestamp(17));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean clearResetToken(int userId) {
        String query = "UPDATE [User] SET reset_token = NULL, token_expiry = NULL WHERE UserID = ?";
        try {conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, userId);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean resetPassword(int userId, String newPassword) {
        String query = "UPDATE [User] SET Password = ? WHERE UserID = ?";
        try {conn = new DBContext().getConnection();
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
    
    public User getUserById(int userID) {
        String sql = "SELECT * FROM [User] WHERE UserId = ?";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, userID);
            rs = ps.executeQuery();
            if (rs.next()) {
                return extractUserFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Lấy tất cả user
    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
        String sql = "SELECT * FROM [User]";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(extractUserFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Cập nhật user
    public boolean updateUser(User user) {
        String sql = "UPDATE [User] SET UserName = ?, Password = ?, RoleId = ?, FullName = ?, Gender = ?, DateOfBirth = ?, AvatarUrl = ?, Email = ?, Phone = ?, Address = ?, Status = ?, CreatedAt = ? WHERE UserId = ?";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, user.getUserName());
            ps.setString(2, user.getPassword());
            ps.setInt(3, user.getRoleId());
            ps.setString(4, user.getFullName());
            ps.setString(5, user.getGender());
            if (user.getDateOfBirth() != null) {
                ps.setDate(6, new java.sql.Date(user.getDateOfBirth().getTime()));
            } else {
                ps.setNull(6, java.sql.Types.DATE);
            }
            ps.setString(7, user.getAvatarUrl());
            ps.setString(8, user.getEmail());
            ps.setString(9, user.getPhone());
            ps.setString(10, user.getAddress());
            ps.setString(11, user.getStatus());
            if (user.getCreatedAt() != null) {
                ps.setTimestamp(12, new java.sql.Timestamp(user.getCreatedAt().getTime()));
            } else {
                ps.setNull(12, java.sql.Types.TIMESTAMP);
            }
            ps.setInt(13, user.getUserId());
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Xóa user theo ID
    public boolean deleteUser(int userID) {
        String sql = "DELETE FROM [User] WHERE UserId = ?";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, userID);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Lấy user theo username và password (để login)
    public User getUserByUsernameAndPassword(String username, String password) {
        String sql = "SELECT * FROM [User] WHERE UserName = ? AND Password = ?";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            rs = ps.executeQuery();
            if (rs.next()) {
                return extractUserFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Helper: chuyển ResultSet thành User
    private User extractUserFromResultSet(ResultSet rs) throws SQLException {
        User user = new User();
        user.setUserId(rs.getInt("UserId"));
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
        user.setCreatedAt(rs.getTimestamp("CreatedAt"));
        user.setActivationToken(rs.getString("activation_token"));
        user.setActivated(rs.getBoolean("is_activated"));
        user.setResetToken(rs.getString("reset_token"));
        user.setTokenExpiry(rs.getTimestamp("token_expiry"));
        return user;
    }

    public boolean updateUserRoleAndStatus(int userId, int roleId, String status) {
        String sql = "UPDATE [User] SET RoleID = ?, Status = ? WHERE UserID = ?";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, roleId);
            ps.setString(2, status);
            ps.setInt(3, userId);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public int getTotalUserCount() {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM [User]";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } 
        return count;
    }
    
    public List<UserWithRoleDTO> searchUsers(String keyword, int offset, int limit) {
        List<UserWithRoleDTO> list = new ArrayList<>();
        String sql = "SELECT u.UserID, u.AvatarURL, u.FullName, u.Gender, u.Email, u.PhoneNumber, u.[Address], " +
                     "r.RoleName, u.Username, u.Password, u.DateOfBirth, u.Status, u.CreateDate, u.RoleID " +
                     "FROM [User] u JOIN Role r ON u.RoleID = r.RoleID " +
                     "WHERE u.UserID LIKE ? OR u.FullName LIKE ? OR u.PhoneNumber LIKE ? " +
                     "ORDER BY u.UserID " +
                     "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");
            ps.setString(3, "%" + keyword + "%");
            ps.setInt(4, offset);
            ps.setInt(5, limit);
            rs = ps.executeQuery();
            while (rs.next()) {
                User u = new User();
                u.setUserId(rs.getInt("UserID"));
                u.setUserName(rs.getString("Username"));
                u.setPassword(rs.getString("Password"));
                u.setRoleId(rs.getInt("RoleID"));
                u.setFullName(rs.getString("FullName"));
                u.setDateOfBirth(rs.getDate("DateOfBirth"));
                u.setAvatarUrl(rs.getString("AvatarURL"));
                u.setEmail(rs.getString("Email"));
                u.setPhone(rs.getString("PhoneNumber"));
                u.setAddress(rs.getString("Address"));
                u.setStatus(rs.getString("Status"));
                u.setCreatedAt(rs.getTimestamp("CreateDate"));
                u.setGender(rs.getString("Gender"));
                String roleName = rs.getString("RoleName");
                list.add(new UserWithRoleDTO(u, roleName));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } 
        return list;
    }
    
    public List<UserWithRoleDTO> getUsersWithRoleByPage(int offset, int limit) {
        List<UserWithRoleDTO> list = new ArrayList<>();
        String sql = "SELECT u.UserID, u.AvatarURL, u.FullName, u.Gender, u.Email, u.PhoneNumber, u.[Address], " +
                     "r.RoleName, u.Username, u.Password, u.DateOfBirth, u.Status, u.CreateDate, u.RoleID " +
                     "FROM [User] u JOIN Role r ON u.RoleID = r.RoleID " +
                     "ORDER BY u.UserID " +
                     "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, offset);
            ps.setInt(2, limit);
            rs = ps.executeQuery();
            while (rs.next()) {
                User u = new User();
                u.setUserId(rs.getInt("UserID"));
                u.setUserName(rs.getString("Username"));
                u.setPassword(rs.getString("Password"));
                u.setRoleId(rs.getInt("RoleID"));
                u.setFullName(rs.getString("FullName"));
                u.setDateOfBirth(rs.getDate("DateOfBirth"));
                u.setAvatarUrl(rs.getString("AvatarURL"));
                u.setEmail(rs.getString("Email"));
                u.setPhone(rs.getString("PhoneNumber"));
                u.setAddress(rs.getString("Address"));
                u.setStatus(rs.getString("Status"));
                u.setCreatedAt(rs.getTimestamp("CreateDate"));
                u.setGender(rs.getString("Gender"));
                String roleName = rs.getString("RoleName");
                list.add(new UserWithRoleDTO(u, roleName));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } 
        return list;
    }
    
    public List<UserWithRoleDTO> getOtherUsersWithRole(int currentUserId, int limit) {
        List<UserWithRoleDTO> list = new ArrayList<>();
        String sql = "SELECT TOP (?) u.UserID, u.AvatarURL, u.FullName, r.RoleName " +
                     "FROM [User] u JOIN Role r ON u.RoleID = r.RoleID " +
                     "WHERE u.UserID <> ? " +
                     "ORDER BY NEWID()";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, limit);
            ps.setInt(2, currentUserId);
            rs = ps.executeQuery();
            while (rs.next()) {
                User u = new User();
                u.setUserId(rs.getInt("UserID"));
                u.setAvatarUrl(rs.getString("AvatarURL"));
                u.setFullName(rs.getString("FullName"));
                String roleName = rs.getString("RoleName");
                list.add(new UserWithRoleDTO(u, roleName));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } 
        return list;
    }
    
    public dto.UserDetailDTO getUserByIdWithRoleName(int userId) {
        String sql = "SELECT u.UserID, u.AvatarURL, u.FullName, u.Gender, u.Email, u.PhoneNumber, u.[Address], " +
                     "r.RoleName, u.Status, u.RoleID, u.Username, u.Password, u.DateOfBirth, u.CreateDate " +
                     "FROM [User] u JOIN Role r ON u.RoleID = r.RoleID " +
                     "WHERE u.UserID = ?";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            rs = ps.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("UserID"));
                user.setUserName(rs.getString("Username"));
                user.setPassword(rs.getString("Password"));
                user.setRoleId(rs.getInt("RoleID"));
                user.setFullName(rs.getString("FullName"));
                user.setDateOfBirth(rs.getDate("DateOfBirth"));
                user.setAvatarUrl(rs.getString("AvatarURL"));
                user.setEmail(rs.getString("Email"));
                user.setPhone(rs.getString("PhoneNumber"));
                user.setAddress(rs.getString("Address"));
                user.setStatus(rs.getString("Status"));
                user.setCreatedAt(rs.getTimestamp("CreateDate"));
                user.setGender(rs.getString("Gender"));
                String roleName = rs.getString("RoleName");
                return new dto.UserDetailDTO(user, roleName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public int getTotalSearchResults(String keyword) {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM [User] WHERE UserID LIKE ? OR FullName LIKE ? OR PhoneNumber LIKE ?";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");
            ps.setString(3, "%" + keyword + "%");
            rs = ps.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } 
        return count;
    }
    
    public static void main(String[] args) {
        UserDAO dao = new UserDAO();
        // Test login
        User user = dao.login("test@example.com", "password");
        System.out.println("Logged in user: " + user);
        // Test user with role
        List<UserWithRoleDTO> users = dao.getUsersWithRoleByPage(0, 5);
        System.out.println("Users with role: " + users);
    }
}