package dal;

import dal.DBContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

// Đảm bảo các import này khớp với vị trí thực tế của các lớp model của bạn
import model.User;
import dto.UserWithRoleDTO; // Giả sử UserWithRoleDTO nằm trong package dto
import dto.UserDetailDTO;   // Giả sử UserDetailDTO nằm trong package dto


public class UserDAO {

    private DBContext dbContext;

    public UserDAO() {
        dbContext = new DBContext();
    }

    private Connection getConnection() throws SQLException {
        return dbContext.getConnection();
    }

    private User extractUserFromResultSet(ResultSet rs) throws SQLException {
        User user = new User();
        user.setUserId(rs.getInt("UserID"));
        user.setUserName(rs.getString("UserName"));
        user.setPassword(rs.getString("Password"));
        user.setRoleId(rs.getInt("RoleId"));
        user.setFullName(rs.getString("FullName"));
        user.setGender(rs.getString("Gender"));

        java.sql.Date dob = rs.getDate("DateOfBirth");
        if (dob != null) {
            user.setDateOfBirth(new java.util.Date(dob.getTime()));
        } else {
            user.setDateOfBirth(null);
        }

        user.setAvatarUrl(rs.getString("AvatarUrl"));
        user.setEmail(rs.getString("Email"));
        user.setPhone(rs.getString("Phone"));
        user.setAddress(rs.getString("Address"));
        user.setStatus(rs.getString("Status"));

        Timestamp createdAt = rs.getTimestamp("CreatedAt");
        if (createdAt != null) {
            user.setCreatedAt(new java.util.Date(createdAt.getTime()));
        } else {
            user.setCreatedAt(null);
        }

        try {
            user.setActivationToken(rs.getString("activation_token"));
        } catch (SQLException e) { /* Column not found or null */ }
        try {
            user.setActivated(rs.getBoolean("is_activated"));
        } catch (SQLException e) { /* Column not found or null */ }
        try {
            user.setResetToken(rs.getString("reset_token"));
        } catch (SQLException e) { /* Column not found or null */ }
        try {
            Timestamp tokenExpiry = rs.getTimestamp("token_expiry");
            if (tokenExpiry != null) {
                user.setTokenExpiry(tokenExpiry);
            } else {
                user.setTokenExpiry(null);
            }
        } catch (SQLException e) { /* Column not found or null */ }
        return user;
    }

    public User login(String email, String password) {
        String sql = "SELECT * FROM [User] WHERE Email = ? AND is_activated = 1 AND Status = 'Active'";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User user = extractUserFromResultSet(rs);
                    // TODO: So sánh mật khẩu đã băm (hash)
                    if (user.getPassword().equals(password)) { // Tạm thời để test
                        return user;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean register(User user, int defaultRoleId, String activationToken) {
        String sql = "INSERT INTO [User] (UserName, Password, RoleId, FullName, Gender, Email, Phone, CreatedAt, AvatarUrl, Address, Status, is_activated, activation_token) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getUserName());
            // TODO: Băm mật khẩu (Password Hashing) trước khi lưu
            ps.setString(2, user.getPassword());
            ps.setInt(3, defaultRoleId);
            ps.setString(4, user.getFullName());
            ps.setString(5, user.getGender());
            ps.setString(6, user.getEmail());
            ps.setString(7, user.getPhone());
            ps.setTimestamp(8, new Timestamp(new Date().getTime()));
            ps.setString(9, user.getAvatarUrl());
            ps.setString(10, user.getAddress());
            ps.setString(11, "Pending");
            ps.setBoolean(12, false);
            ps.setString(13, activationToken);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean addUser(User user, String roleName) {
        int roleId = getRoleIdByName(roleName);
        if (roleId == -1) {
            System.err.println("Role '" + roleName + "' not found.");
            return false;
        }
        user.setRoleId(roleId);
        return createUserByAdmin(user);
    }

    public boolean createUserByAdmin(User user) {
        String sql = "INSERT INTO [User] (UserName, Password, RoleId, FullName, Gender, Email, Phone, Status, CreatedAt, is_activated, AvatarUrl, Address) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getUserName());
            // TODO: Băm mật khẩu (Password Hashing) trước khi lưu
            ps.setString(2, user.getPassword());
            ps.setInt(3, user.getRoleId());
            ps.setString(4, user.getFullName());
            ps.setString(5, user.getGender());
            ps.setString(6, user.getEmail());
            ps.setString(7, user.getPhone());
            ps.setString(8, "Active");
            ps.setTimestamp(9, new Timestamp(new java.util.Date().getTime()));
            ps.setBoolean(10, true);
            ps.setString(11, user.getAvatarUrl());
            ps.setString(12, user.getAddress());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isEmailExists(String email) {
        String sql = "SELECT COUNT(*) FROM [User] WHERE Email = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean activateAccount(String token) {
        String sql = "UPDATE [User] SET is_activated = 1, Status = 'Active', activation_token = NULL WHERE activation_token = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, token);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updatePassword(String email, String password) {
        String sql = "UPDATE [User] SET Password = ? WHERE Email = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            // TODO: Băm mật khẩu (Password Hashing) trước khi lưu
            ps.setString(1, password);
            ps.setString(2, email);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public User getUserByEmail(String email) {
        String query = "SELECT * FROM [User] WHERE Email = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extractUserFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Phương thức storeResetToken (Đã thêm vào)
    public boolean storeResetToken(int userId, String resetToken, Date expiryDate) {
        String query = "UPDATE [User] SET reset_token = ?, token_expiry = ? WHERE UserID = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, resetToken);
            ps.setTimestamp(2, new Timestamp(expiryDate.getTime()));
            ps.setInt(3, userId);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Phương thức getUserByResetToken (Đã thêm vào)
    public User getUserByResetToken(String resetToken) {
        String query = "SELECT * FROM [User] WHERE reset_token = ? AND token_expiry > GETDATE()";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, resetToken);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extractUserFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Phương thức clearResetToken (Đã thêm vào)
    public boolean clearResetToken(int userId) {
        String query = "UPDATE [User] SET reset_token = NULL, token_expiry = NULL WHERE UserID = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, userId);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean resetPassword(int userId, String newPassword) {
        String query = "UPDATE [User] SET Password = ? WHERE UserID = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            // TODO: Băm newPassword trước khi lưu
            ps.setString(1, newPassword);
            ps.setInt(2, userId);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public User getUserById(int userID) {
        String sql = "SELECT * FROM [User] WHERE UserID = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extractUserFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
        String sql = "SELECT * FROM [User]";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(extractUserFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean updateUser(User user) {
        String sql = "UPDATE [User] SET UserName = ?, FullName = ?, Gender = ?, Email = ?, Phone = ?, Address = ?, AvatarUrl = ? WHERE UserID = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getUserName());
            ps.setString(2, user.getFullName());
            ps.setString(3, user.getGender());
            ps.setString(4, user.getEmail());
            ps.setString(5, user.getPhone());
            ps.setString(6, user.getAddress());
            ps.setString(7, user.getAvatarUrl());
            ps.setInt(8, user.getUserId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Helper: Lấy RoleID từ RoleName
    public int getRoleIdByName(String roleName) {
        String sql = "SELECT RoleID FROM [Role] WHERE RoleName = ?";
        try (Connection conn = getConnection(); // Gọi getConnection() thông qua instance
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, roleName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("RoleID");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Trả về -1 nếu không tìm thấy RoleName
    }

    public boolean deleteUser(int userID) {
        String sql = "DELETE FROM [User] WHERE UserID = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userID);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateUserGenderAndPhone(int userId, String gender, String phone) {
        String sql = "UPDATE [User] SET Gender = ?, Phone = ? WHERE UserID = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, gender);
            ps.setString(2, phone);
            ps.setInt(3, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public boolean updateUserRoleAndStatus(int userId, int roleId, String status) {
        String sql = "UPDATE [User] SET RoleID = ?, Status = ? WHERE UserID = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
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
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
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
        String sql = "SELECT u.UserID, u.AvatarURL, u.FullName, u.Gender, u.Email, u.Phone, u.[Address], "
                + "r.RoleName, u.Username, u.Password, u.DateOfBirth, u.Status, u.CreatedAt, u.RoleID, u.is_activated, u.activation_token, u.reset_token, u.token_expiry "
                + "FROM [User] u JOIN [Role] r ON u.RoleID = r.RoleID "
                + "WHERE u.UserID LIKE ? OR u.FullName LIKE ? OR u.Phone LIKE ? OR u.Email LIKE ? OR r.RoleName LIKE ? "
                + "ORDER BY u.UserID "
                + "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");
            ps.setString(3, "%" + keyword + "%");
            ps.setString(4, "%" + keyword + "%");
            ps.setString(5, "%" + keyword + "%");
            ps.setInt(6, offset);
            ps.setInt(7, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    User u = extractUserFromResultSet(rs);
                    String roleName = rs.getString("RoleName");
                    list.add(new UserWithRoleDTO(u, roleName));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<UserWithRoleDTO> searchUsersWithFilters(String keyword, String roleFilter, String genderFilter, String statusFilter, int offset, int limit) {
        List<UserWithRoleDTO> list = new ArrayList<>();
        StringBuilder sqlBuilder = new StringBuilder("SELECT u.UserID, u.AvatarURL, u.FullName, u.Gender, u.Email, u.Phone, u.[Address], ")
                .append("r.RoleName, u.Username, u.Password, u.DateOfBirth, u.Status, u.CreatedAt, u.RoleID, u.is_activated, u.activation_token, u.reset_token, u.token_expiry ")
                .append("FROM [User] u JOIN [Role] r ON u.RoleID = r.RoleID WHERE 1=1 ");

        List<Object> params = new ArrayList<>();

        if (keyword != null && !keyword.trim().isEmpty()) {
            sqlBuilder.append("AND (u.UserID LIKE ? OR u.FullName LIKE ? OR u.Phone LIKE ? OR u.Email LIKE ? OR r.RoleName LIKE ?) ");
            for (int i = 0; i < 5; i++) {
                params.add("%" + keyword + "%");
            }
        }
        if (roleFilter != null && !roleFilter.trim().isEmpty()) {
            sqlBuilder.append("AND r.RoleName = ? ");
            params.add(roleFilter);
        }
        if (genderFilter != null && !genderFilter.trim().isEmpty()) {
            sqlBuilder.append("AND u.Gender = ? ");
            params.add(genderFilter);
        }
        if (statusFilter != null && !statusFilter.trim().isEmpty()) {
            sqlBuilder.append("AND u.Status = ? ");
            params.add(statusFilter);
        }

        sqlBuilder.append("ORDER BY u.UserID OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");
        params.add(offset);
        params.add(limit);

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sqlBuilder.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    User u = extractUserFromResultSet(rs);
                    String roleName = rs.getString("RoleName");
                    list.add(new UserWithRoleDTO(u, roleName));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public int getTotalSearchResultsWithFilters(String keyword, String roleFilter, String genderFilter, String statusFilter) {
        int count = 0;
        StringBuilder sqlBuilder = new StringBuilder("SELECT COUNT(*) FROM [User] u JOIN [Role] r ON u.RoleID = r.RoleID WHERE 1=1 ");

        List<Object> params = new ArrayList<>();

        if (keyword != null && !keyword.trim().isEmpty()) {
            sqlBuilder.append("AND (u.UserID LIKE ? OR u.FullName LIKE ? OR u.Email LIKE ? OR u.Phone LIKE ? OR r.RoleName LIKE ?) ");
            for (int i = 0; i < 5; i++) {
                params.add("%" + keyword + "%");
            }
        }
        if (roleFilter != null && !roleFilter.trim().isEmpty()) {
            sqlBuilder.append("AND r.RoleName = ? ");
            params.add(roleFilter);
        }
        if (genderFilter != null && !genderFilter.trim().isEmpty()) {
            sqlBuilder.append("AND u.Gender = ? ");
            params.add(genderFilter);
        }
        if (statusFilter != null && !statusFilter.trim().isEmpty()) {
            sqlBuilder.append("AND u.Status = ? ");
            params.add(statusFilter);
        }

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sqlBuilder.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    public List<UserWithRoleDTO> getUsersWithRoleByPage(int offset, int limit) {
        List<UserWithRoleDTO> list = new ArrayList<>();
        String sql = "SELECT u.UserID, u.AvatarURL, u.FullName, u.Gender, u.Email, u.Phone, u.[Address], "
                + "r.RoleName, u.Username, u.Password, u.DateOfBirth, u.Status, u.CreatedAt, u.RoleID, u.is_activated, u.activation_token, u.reset_token, u.token_expiry "
                + "FROM [User] u JOIN [Role] r ON u.RoleID = r.RoleID "
                + "ORDER BY u.UserID "
                + "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, offset);
            ps.setInt(2, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    User u = extractUserFromResultSet(rs);
                    String roleName = rs.getString("RoleName");
                    list.add(new UserWithRoleDTO(u, roleName));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<UserWithRoleDTO> getOtherUsersWithRole(int currentUserId, int limit) {
        List<UserWithRoleDTO> list = new ArrayList<>();
        String sql = "SELECT TOP (?) u.UserID, u.AvatarURL, u.FullName, r.RoleName, u.Username, u.Password, u.DateOfBirth, u.Gender, u.Email, u.Phone, u.[Address], u.Status, u.CreatedAt, u.RoleID, u.is_activated, u.activation_token, u.reset_token, u.token_expiry "
                + "FROM [User] u JOIN [Role] r ON u.RoleID = r.RoleID "
                + "WHERE u.UserID <> ? "
                + "ORDER BY NEWID()";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limit);
            ps.setInt(2, currentUserId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    User u = extractUserFromResultSet(rs);
                    String roleName = rs.getString("RoleName");
                    list.add(new UserWithRoleDTO(u, roleName));
                }
            }
            // No explicit close for ps and rs here as try-with-resources handles it
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public UserDetailDTO getUserByIdWithRoleName(int userId) {
        String sql = "SELECT u.UserID, u.AvatarURL, u.FullName, u.Gender, u.Email, u.Phone, u.[Address], "
                + "r.RoleName, u.Status, u.RoleID, u.Username, u.Password, u.DateOfBirth, u.CreatedAt, u.is_activated, u.activation_token, u.reset_token, u.token_expiry "
                + "FROM [User] u JOIN [Role] r ON u.RoleID = r.RoleID "
                + "WHERE u.UserID = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User user = extractUserFromResultSet(rs);
                    String roleName = rs.getString("RoleName");
                    return new UserDetailDTO(user, roleName);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getTotalSearchResults(String keyword) {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM [User] u JOIN [Role] r ON u.RoleID = r.RoleID WHERE u.UserID LIKE ? OR u.FullName LIKE ? OR u.Phone LIKE ? OR u.Email LIKE ? OR r.RoleName LIKE ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");
            ps.setString(3, "%" + keyword + "%");
            ps.setString(4, "%" + keyword + "%");
            ps.setString(5, "%" + keyword + "%");
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }
    
    // Phương thức để lấy danh sách người dùng có kèm theo tìm kiếm và lọc
    public List<UserWithRoleDTO> getFilteredUsers(String searchKeyword, String role, String gender, String status, int offset, int limit) {
        List<UserWithRoleDTO> userList = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT u.*, r.RoleName FROM [User] u JOIN [Role] r ON u.RoleID = r.RoleID WHERE 1=1");

        // Sử dụng % để tìm kiếm linh hoạt hơn
        String likeSearchKeyword = (searchKeyword != null && !searchKeyword.isEmpty()) ? "%" + searchKeyword + "%" : null;

        if (likeSearchKeyword != null) {
            sql.append(" AND (u.FullName LIKE ? OR u.Email LIKE ? OR u.Phone LIKE ?)");
        }
        if (role != null && !role.isEmpty()) {
            sql.append(" AND r.RoleName = ?");
        }
        if (gender != null && !gender.isEmpty()) {
            sql.append(" AND u.Gender = ?");
        }
        if (status != null && !status.isEmpty()) {
            sql.append(" AND u.Status = ?");
        }

        sql.append(" ORDER BY u.UserID ASC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            int paramIndex = 1;
            if (likeSearchKeyword != null) {
                ps.setString(paramIndex++, likeSearchKeyword);
                ps.setString(paramIndex++, likeSearchKeyword);
                ps.setString(paramIndex++, likeSearchKeyword);
            }
            if (role != null && !role.isEmpty()) {
                ps.setString(paramIndex++, role);
            }
            if (gender != null && !gender.isEmpty()) {
                ps.setString(paramIndex++, gender);
            }
            if (status != null && !status.isEmpty()) {
                ps.setString(paramIndex++, status);
            }

            ps.setInt(paramIndex++, offset);
            ps.setInt(paramIndex++, limit);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    User user = extractUserFromResultSet(rs);
                    String roleName = rs.getString("RoleName");
                    userList.add(new UserWithRoleDTO(user, roleName));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching filtered users: " + e.getMessage());
            e.printStackTrace();
        }
        return userList;
    }

    // Phương thức để lấy tổng số bản ghi đã được lọc và tìm kiếm
    public int getTotalFilteredUserCount(String searchKeyword, String role, String gender, String status) {
        int count = 0;
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM [User] u JOIN [Role] r ON u.RoleID = r.RoleID WHERE 1=1");

        // Sử dụng % để tìm kiếm linh hoạt hơn
        String likeSearchKeyword = (searchKeyword != null && !searchKeyword.isEmpty()) ? "%" + searchKeyword + "%" : null;

        if (likeSearchKeyword != null) {
            sql.append(" AND (u.FullName LIKE ? OR u.Email LIKE ? OR u.Phone LIKE ?)");
        }
        if (role != null && !role.isEmpty()) {
            sql.append(" AND r.RoleName = ?");
        }
        if (gender != null && !gender.isEmpty()) {
            sql.append(" AND u.Gender = ?");
        }
        if (status != null && !status.isEmpty()) {
            sql.append(" AND u.Status = ?");
        }

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            int paramIndex = 1;
            if (likeSearchKeyword != null) {
                ps.setString(paramIndex++, likeSearchKeyword);
                ps.setString(paramIndex++, likeSearchKeyword);
                ps.setString(paramIndex++, likeSearchKeyword);
            }
            if (role != null && !role.isEmpty()) {
                ps.setString(paramIndex++, role);
            }
            if (gender != null && !gender.isEmpty()) {
                ps.setString(paramIndex++, gender);
            }
            if (status != null && !status.isEmpty()) {
                ps.setString(paramIndex++, status);
            }

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting total filtered user count: " + e.getMessage());
            e.printStackTrace();
        }
        return count;
    }
    
    // Trong file dal/UserDAO.java của bạn, thêm phương thức này:

    public boolean isUsernameExists(String username) {
        String sql = "SELECT COUNT(*) FROM [User] WHERE UserName = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error checking if username exists: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public static void main(String[] args) {
        UserDAO dao = new UserDAO(); // Tạo một instance của UserDAO

        // 1. Test đăng ký
        System.out.println("=== TEST ĐĂNG KÝ (register method) ===");
        User newUserRegister = new User();
        newUserRegister.setUserName("testuser_reg_1"); // Make unique for testing
        newUserRegister.setPassword("password123"); // SẼ CẦN BĂM
        newUserRegister.setFullName("Test User Registered 1");
        newUserRegister.setGender("Male");
        newUserRegister.setEmail("testuser_reg_1@example.com"); // Make unique for testing
        newUserRegister.setPhone("0123456780");
        newUserRegister.setAddress("123 Register St"); // Added address for testing
        newUserRegister.setAvatarUrl("https://example.com/default_avatar.jpg"); // Add default avatar
        // Assume roleId 2 exists (e.g., "Student" role)
        boolean registerSuccess = dao.register(newUserRegister, 2, "random_activation_token_register_1");
        System.out.println("Register success: " + registerSuccess);

        // Get the newly registered user for subsequent tests
        User registeredUser = dao.getUserByEmail("testuser_reg_1@example.com");
        int registeredUserId = -1;
        if (registeredUser != null) {
            registeredUserId = registeredUser.getUserId();
            System.out.println("Registered User ID: " + registeredUserId);

            // Kích hoạt tài khoản để có thể login
            System.out.println("\n=== TEST ACTIVATE ACCOUNT for registered user ===");
            boolean activateResult = dao.activateAccount("random_activation_token_register_1");
            System.out.println("Activate account result: " + activateResult);
        }

        // --- TEST NEW addUser method ---
        System.out.println("\n=== TEST ADD USER (addUser method) ===");
        User newUserAdd = new User();
        newUserAdd.setUserName("admin_added_user_1");
        newUserAdd.setPassword("hashed_pass_123_admin"); // SẼ CẦN BĂM
        newUserAdd.setFullName("Admin Added User 1");
        newUserAdd.setGender("Female");
        newUserAdd.setEmail("admin.added.user1@example.com");
        newUserAdd.setPhone("0987654322");
        newUserAdd.setAddress("456 Admin Ave");
        newUserAdd.setAvatarUrl("https://example.com/avatar.jpg");

        boolean addUserSuccess = dao.addUser(newUserAdd, "Admin");
        System.out.println("Add User (admin) success: " + addUserSuccess);


        // Test login
        System.out.println("\n=== TEST LOGIN ===");
        User loggedInUser = dao.login("testuser_reg_1@example.com", "password123");
        if (loggedInUser != null) {
            System.out.println("Login successful for: " + loggedInUser.getFullName());
        } else {
            System.out.println("Login failed for testuser_reg_1@example.com");
        }

        loggedInUser = dao.login("admin.added.user1@example.com", "hashed_pass_123_admin");
        if (loggedInUser != null) {
            System.out.println("Login successful for: " + loggedInUser.getFullName());
        } else {
            System.out.println("Login failed for admin.added.user1@example.com");
        }


        // Test isEmailExists
        System.out.println("\n=== TEST IS EMAIL EXISTS ===");
        System.out.println("Is 'testuser_reg_1@example.com' exists: " + dao.isEmailExists("testuser_reg_1@example.com"));
        System.out.println("Is 'nonexistent@example.com' exists: " + dao.isEmailExists("nonexistent@example.com"));

        // Test updatePassword
        System.out.println("\n=== TEST UPDATE PASSWORD ===");
        boolean updatePassSuccess = dao.updatePassword("testuser_reg_1@example.com", "new_password_hashed");
        System.out.println("Update password success: " + updatePassSuccess);
        loggedInUser = dao.login("testuser_reg_1@example.com", "new_password_hashed");
        if (loggedInUser != null) {
            System.out.println("Login with new password successful for: " + loggedInUser.getFullName());
        } else {
            System.out.println("Login with new password failed.");
        }

        // Test getUserById
        System.out.println("\n=== TEST GET USER BY ID ===");
        if (registeredUserId != -1) {
            User userById = dao.getUserById(registeredUserId);
            if (userById != null) {
                System.out.println("User by ID " + registeredUserId + ": " + userById.getFullName());
            } else {
                System.out.println("User by ID " + registeredUserId + " not found.");
            }
        }

        // Test getAllUsers
        System.out.println("\n=== TEST GET ALL USERS ===");
        List<User> allUsers = dao.getAllUsers();
        allUsers.forEach(user -> System.out.println("- " + user.getFullName()));

        // Test updateUser
        System.out.println("\n=== TEST UPDATE USER ===");
        if (registeredUser != null) {
            registeredUser.setFullName("Updated Name for Register User (Full)");
            registeredUser.setPhone("999888777");
            registeredUser.setAddress("456 Updated Address");
            registeredUser.setAvatarUrl("https://example.com/updated_avatar.jpg");
            boolean updateSuccess = dao.updateUser(registeredUser);
            System.out.println("Update user success: " + updateSuccess);
            User updatedUserCheck = dao.getUserById(registeredUserId);
            if(updatedUserCheck != null) {
                System.out.println("Updated User Full Info: " + updatedUserCheck.getFullName() + ", Phone: " + updatedUserCheck.getPhone() + ", Address: " + updatedUserCheck.getAddress());
            }
        }

        System.out.println("\n=== TEST UPDATE USER GENDER AND PHONE ===");
        if (registeredUserId != -1) {
            boolean updateInfoSuccess = dao.updateUserGenderAndPhone(registeredUserId, "Female", "111222333");
            System.out.println("Update gender and phone success: " + updateInfoSuccess);
            User updatedUser = dao.getUserById(registeredUserId);
            if (updatedUser != null) {
                System.out.println("Updated User Info: Gender=" + updatedUser.getGender() + ", Phone=" + updatedUser.getPhone());
            }
        }

        System.out.println("\n=== TEST UPDATE USER ROLE AND STATUS ===");
        if (registeredUserId != -1) {
            boolean updateRoleStatusSuccess = dao.updateUserRoleAndStatus(registeredUserId, 1, "Inactive");
            System.out.println("Update role and status success: " + updateRoleStatusSuccess);
            UserDetailDTO updatedUserDetail = dao.getUserByIdWithRoleName(registeredUserId);
            if (updatedUserDetail != null) {
                System.out.println("Updated User Role/Status: Role=" + updatedUserDetail.getRoleName() + ", Status=" + updatedUserDetail.getUser().getStatus());
            }
        }

        System.out.println("\n=== TEST GET TOTAL USER COUNT ===");
        int totalCount = dao.getTotalUserCount();
        System.out.println("Total user count: " + totalCount);

        System.out.println("\n=== TEST SEARCH USERS (pagination) ===");
        List<UserWithRoleDTO> searchResults = dao.searchUsers("User", 0, 5);
        searchResults.forEach(userDto -> System.out.println("- Search Result: " + userDto.getUser().getFullName() + " (Role: " + userDto.getRoleName() + ")"));

        System.out.println("\n=== TEST SEARCH USERS WITH FILTERS ===");
        List<UserWithRoleDTO> filteredUsers = dao.searchUsersWithFilters("test", "Student", "Male", "Active", 0, 10);
        System.out.println("Filtered Search Results:");
        filteredUsers.forEach(userDto -> System.out.println("- " + userDto.getUser().getFullName() + " (Role: " + userDto.getRoleName() + ", Gender: " + userDto.getUser().getGender() + ", Status: " + userDto.getUser().getStatus() + ")"));

        System.out.println("\n=== TEST GET TOTAL SEARCH RESULTS WITH FILTERS ===");
        int totalFiltered = dao.getTotalSearchResultsWithFilters("test", "Student", "Male", "Active");
        System.out.println("Total filtered results: " + totalFiltered);

        System.out.println("\n=== TEST GET USERS WITH ROLE BY PAGE ===");
        List<UserWithRoleDTO> usersByPage = dao.getUsersWithRoleByPage(0, 3);
        usersByPage.forEach(userDto -> System.out.println("- Page User: " + userDto.getUser().getFullName() + " (Role: " + userDto.getRoleName() + ")"));

        System.out.println("\n=== TEST GET OTHER USERS WITH ROLE ===");
        List<UserWithRoleDTO> otherUsers = dao.getOtherUsersWithRole(1, 2);
        System.out.println("Other Users:");
        otherUsers.forEach(userDto -> System.out.println("- " + userDto.getUser().getFullName() + " (Role: " + userDto.getRoleName() + ")"));

        System.out.println("\n=== TEST GET USER BY ID WITH ROLE NAME ===");
        if (registeredUserId != -1) {
            UserDetailDTO userDetail = dao.getUserByIdWithRoleName(registeredUserId);
            if (userDetail != null) {
                System.out.println("User Detail for ID " + registeredUserId + ": " + userDetail.getUser().getFullName() + " (Role: " + userDetail.getRoleName() + ")");
            } else {
                System.out.println("User Detail for ID " + registeredUserId + " not found.");
            }
        }

        // Test deleteUser (DANGER: USE WITH CAUTION IN PROD)
        System.out.println("\n=== TEST DELETE USER ===");
         if (registeredUserId != -1) {
            boolean deleteSuccess = dao.deleteUser(registeredUserId);
            System.out.println("Delete registered user success: " + deleteSuccess);
         }
         User adminAddedUserToDelete = dao.getUserByEmail("admin.added.user1@example.com");
         if (adminAddedUserToDelete != null) {
            boolean deleteAdminAddedUser = dao.deleteUser(adminAddedUserToDelete.getUserId());
            System.out.println("Delete admin added user success: " + deleteAdminAddedUser);
         }
         System.out.println("Total user count after deletion: " + dao.getTotalUserCount());
    }
}