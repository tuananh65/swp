package dal;

import model.Setting;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SettingListDAO {
    private Connection conn = null;
    private PreparedStatement ps = null;
    private ResultSet rs = null;
    private DBContext dbContext;

    public SettingListDAO() {
        dbContext = new DBContext();
    }

    // Helper method to close database resources
    private void closeResources() {
        try {
            if (rs != null) {
                rs.close();
                rs = null;
            }
            if (ps != null) {
                ps.close();
                ps = null;
            }
            if (conn != null) {
                conn.close();
                conn = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Setting> filterSettingsByStatus(String status, int offset, int limit) {
        List<Setting> list = new ArrayList<>();
        String sql = "SELECT SettingID, Type, [Key], Value, [Order], Status FROM Setting WHERE Status = ? ORDER BY SettingID OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, status);
            ps.setInt(2, offset);
            ps.setInt(3, limit);
            rs = ps.executeQuery();
            while (rs.next()) {
                Setting s = new Setting();
                s.setSettingID(rs.getInt("SettingID"));
                s.setType(rs.getString("Type"));
                s.setKey(rs.getString("Key"));
                s.setValue(rs.getString("Value"));
                s.setOrder(rs.getInt("Order"));
                s.setStatus(rs.getString("Status"));
                list.add(s);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return list;
    }

    public int getTotalFilteredResultsByStatus(String status) {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM Setting WHERE Status = ?";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, status);
            rs = ps.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return count;
    }

    public boolean deleteSetting(int settingID) {
        String sql = "DELETE FROM Setting WHERE SettingID = ?";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, settingID);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeResources();
        }
    }

    public int getTotalSettingCount() {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM Setting";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return count;
    }

    public List<Setting> getSettingsByPage(int offset, int limit) {
        List<Setting> list = new ArrayList<>();
        String sql = "SELECT SettingID, Type, [Key], Value, [Order], Status FROM Setting ORDER BY SettingID OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, offset);
            ps.setInt(2, limit);
            rs = ps.executeQuery();
            while (rs.next()) {
                Setting s = new Setting();
                s.setSettingID(rs.getInt("SettingID"));
                s.setType(rs.getString("Type"));
                s.setKey(rs.getString("Key"));
                s.setValue(rs.getString("Value"));
                s.setOrder(rs.getInt("Order"));
                s.setStatus(rs.getString("Status"));
                list.add(s);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return list;
    }

    public List<Setting> searchSettings(String keyword, int offset, int limit) {
        List<Setting> list = new ArrayList<>();
        String sql = "SELECT SettingID, Type, [Key], Value, [Order], Status FROM Setting WHERE SettingID LIKE ? OR Type LIKE ? OR [Key] LIKE ? OR Value LIKE ? ORDER BY SettingID OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");
            ps.setString(3, "%" + keyword + "%");
            ps.setString(4, "%" + keyword + "%");
            ps.setInt(5, offset);
            ps.setInt(6, limit);
            rs = ps.executeQuery();
            while (rs.next()) {
                Setting s = new Setting();
                s.setSettingID(rs.getInt("SettingID"));
                s.setType(rs.getString("Type"));
                s.setKey(rs.getString("Key"));
                s.setValue(rs.getString("Value"));
                s.setOrder(rs.getInt("Order"));
                s.setStatus(rs.getString("Status"));
                list.add(s);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return list;
    }

    public int getTotalSearchResults(String keyword) {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM Setting WHERE SettingID LIKE ? OR Type LIKE ? OR [Key] LIKE ? OR Value LIKE ?";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");
            ps.setString(3, "%" + keyword + "%");
            ps.setString(4, "%" + keyword + "%");
            rs = ps.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return count;
    }

    public boolean updateSettingStatus(int settingID, String status) {
        String sql = "UPDATE Setting SET Status = ? WHERE SettingID = ?";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, status);
            ps.setInt(2, settingID);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeResources();
        }
    }

    public boolean addSetting(Setting setting) {
        String sql = "INSERT INTO Setting (Type, [Key], Value, [Order], Status) VALUES (?, ?, ?, ?, ?)";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, setting.getType());
            ps.setString(2, setting.getKey());
            ps.setString(3, setting.getValue());
            if (setting.getOrder() != null) {
                ps.setInt(4, setting.getOrder());
            } else {
                ps.setNull(4, java.sql.Types.INTEGER);
            }
            ps.setString(5, setting.getStatus());
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeResources();
        }
    }

    public Setting getSettingById(int settingID) {
        String sql = "SELECT SettingID, Type, [Key], Value, [Order], Status FROM Setting WHERE SettingID = ?";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, settingID);
            rs = ps.executeQuery();
            if (rs.next()) {
                Setting setting = new Setting();
                setting.setSettingID(rs.getInt("SettingID"));
                setting.setType(rs.getString("Type"));
                setting.setKey(rs.getString("Key"));
                setting.setValue(rs.getString("Value"));
                setting.setOrder(rs.getInt("Order"));
                setting.setStatus(rs.getString("Status"));
                return setting;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return null;
    }

    public boolean updateSetting(Setting setting) {
        String sql = "UPDATE Setting SET Type = ?, [Key] = ?, Value = ?, [Order] = ?, Status = ? WHERE SettingID = ?";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, setting.getType());
            ps.setString(2, setting.getKey());
            ps.setString(3, setting.getValue());
            if (setting.getOrder() != null) {
                ps.setInt(4, setting.getOrder());
            } else {
                ps.setNull(4, java.sql.Types.INTEGER);
            }
            ps.setString(5, setting.getStatus());
            ps.setInt(6, setting.getSettingID());
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeResources();
        }
    }

    public List<Setting> filterSettingsByType(String type, int offset, int limit) {
        List<Setting> list = new ArrayList<>();
        String sql = "SELECT SettingID, Type, [Key], Value, [Order], Status FROM Setting WHERE Type = ? ORDER BY SettingID OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, type);
            ps.setInt(2, offset);
            ps.setInt(3, limit);
            rs = ps.executeQuery();
            while (rs.next()) {
                Setting s = new Setting();
                s.setSettingID(rs.getInt("SettingID"));
                s.setType(rs.getString("Type"));
                s.setKey(rs.getString("Key"));
                s.setValue(rs.getString("Value"));
                s.setOrder(rs.getInt("Order"));
                s.setStatus(rs.getString("Status"));
                list.add(s);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return list;
    }

    public int getTotalFilteredResultsByType(String type) {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM Setting WHERE Type = ?";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, type);
            rs = ps.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return count;
    }

    public static void main(String[] args) {
        SettingListDAO dao = new SettingListDAO();
        // Test example
        List<Setting> settings = dao.filterSettingsByStatus("Active", 0, 10);
        System.out.println("Settings: " + settings);
    }
}