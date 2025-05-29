package dal;

import model.Users;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    private Connection connection;
    private DBContext dbContext;

    public UserDAO() {
        dbContext = new DBContext();
        connection = dbContext.getConnection();
    }

    public Users login(String email, String password) {
        String query = "SELECT UserID, Username, Password, RoleID, FullName, DateOfBirth, AvatarURL, Email, PhoneNumber, Address, Status, CreateDate, Gender " +
                "FROM [User] WHERE Email = ? AND Password = ?";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, email);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Users(rs.getInt("UserID"),
                            rs.getString("Username"),
                            rs.getString("Password"),
                            rs.getInt("RoleID"),
                            rs.getString("FullName"),
                            rs.getDate("DateOfBirth"),
                            rs.getString("AvatarURL"),
                            rs.getString("Email"),
                            rs.getString("PhoneNumber"),
                            rs.getString("Address"),
                            rs.getString("Status"),
                            rs.getTimestamp("CreateDate"),
                            rs.getString("Gender"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getTotalUserCount() {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM [User]";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    public List<dto.UserWithRoleDTO> getUsersWithRoleByPage(int offset, int limit) {
        List<dto.UserWithRoleDTO> list = new ArrayList<>();
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT u.UserID, u.AvatarURL, u.FullName, u.Gender, u.Email, u.PhoneNumber, u.[Address], " +
                             "r.RoleName, u.Username, u.Password, u.DateOfBirth, u.Status, u.CreateDate, u.Gender AS UserGender, u.RoleID AS UserRoleID " +
                             "FROM [User] u JOIN Role r ON u.RoleID = r.RoleID " +
                             "ORDER BY u.UserID " +
                             "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY")) {
            stmt.setInt(1, offset);
            stmt.setInt(2, limit);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Users u = new Users();
                    u.setUserID(rs.getInt("UserID"));
                    u.setUserName(rs.getString("Username"));
                    u.setPassword(rs.getString("Password"));
                    u.setRoleId(rs.getInt("UserRoleID"));
                    u.setFullName(rs.getString("FullName"));
                    u.setDateOfBirth(rs.getDate("DateOfBirth"));
                    u.setAvatarUrl(rs.getString("AvatarURL"));
                    u.setEmail(rs.getString("Email"));
                    u.setPhone(rs.getString("PhoneNumber"));
                    u.setAddress(rs.getString("Address"));
                    u.setStatus(rs.getString("Status"));
                    u.setCreatedAt(rs.getTimestamp("CreateDate"));
                    u.setGender(rs.getString("UserGender"));

                    String roleName = rs.getString("RoleName");
                    dto.UserWithRoleDTO dto = new dto.UserWithRoleDTO(u, roleName);
                    list.add(dto);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<dto.UserWithRoleDTO> searchUsers(String keyword, int offset, int limit) {
        List<dto.UserWithRoleDTO> list = new ArrayList<>();
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT u.UserID, u.AvatarURL, u.FullName, u.Gender, u.Email, u.PhoneNumber, u.[Address], " +
                             "r.RoleName, u.Username, u.Password, u.DateOfBirth, u.Status, u.CreateDate, u.Gender AS UserGender, u.RoleID AS UserRoleID " +
                             "FROM [User] u JOIN Role r ON u.RoleID = r.RoleID " +
                             "WHERE u.UserID LIKE ? OR u.FullName LIKE ? OR u.PhoneNumber LIKE ? " +
                             "ORDER BY u.UserID " +
                             "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY")) {
            stmt.setString(1, "%" + keyword + "%");
            stmt.setString(2, "%" + keyword + "%");
            stmt.setString(3, "%" + keyword + "%");
            stmt.setInt(4, offset);
            stmt.setInt(5, limit);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Users u = new Users();
                    u.setUserID(rs.getInt("UserID"));
                    u.setUserName(rs.getString("Username"));
                    u.setPassword(rs.getString("Password"));
                    u.setRoleId(rs.getInt("UserRoleID"));
                    u.setFullName(rs.getString("FullName"));
                    u.setDateOfBirth(rs.getDate("DateOfBirth"));
                    u.setAvatarUrl(rs.getString("AvatarURL"));
                    u.setEmail(rs.getString("Email"));
                    u.setPhone(rs.getString("PhoneNumber"));
                    u.setAddress(rs.getString("Address"));
                    u.setStatus(rs.getString("Status"));
                    u.setCreatedAt(rs.getTimestamp("CreateDate"));
                    u.setGender(rs.getString("UserGender"));

                    String roleName = rs.getString("RoleName");
                    dto.UserWithRoleDTO dto = new dto.UserWithRoleDTO(u, roleName);
                    list.add(dto);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public int getTotalSearchResults(String keyword) {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM [User] WHERE UserID LIKE ? OR FullName LIKE ? OR PhoneNumber LIKE ?";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + keyword + "%");
            stmt.setString(2, "%" + keyword + "%");
            stmt.setString(3, "%" + keyword + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    public dto.UserDetailDTO getUserByIdWithRoleName(int userId) {
        String sql = "SELECT u.UserID, u.AvatarURL, u.FullName, u.Gender, u.Email, u.PhoneNumber, u.[Address], " +
                "r.RoleName, u.Status, u.RoleID, u.UserName, u.Password, u.DateOfBirth, u.CreateDate, u.Gender AS UserGender " +
                "FROM [User] u JOIN Role r ON u.RoleID = r.RoleID " +
                "WHERE u.UserID = ?";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Users user = new Users(
                            rs.getInt("UserID"),
                            rs.getString("UserName"),
                            rs.getString("Password"),
                            rs.getInt("RoleID"),
                            rs.getString("FullName"),
                            rs.getDate("DateOfBirth"),
                            rs.getString("AvatarURL"),
                            rs.getString("Email"),
                            rs.getString("PhoneNumber"),
                            rs.getString("Address"),
                            rs.getString("Status"),
                            rs.getTimestamp("CreateDate"),
                            rs.getString("UserGender")
                    );
                    String roleName = rs.getString("RoleName");
                    return new dto.UserDetailDTO(user, roleName);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}