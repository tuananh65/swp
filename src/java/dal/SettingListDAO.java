// SettingListDAO.java
package dal;

import model.Setting;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SettingListDAO {
    private Connection connection;
    private DBContext dbContext; // Thêm một instance của DBContext

    public SettingListDAO() {
        dbContext = new DBContext(); // Khởi tạo DBContext
        connection = dbContext.getConnection(); // Sử dụng DBContext của bạn để lấy connection
    }

    public List<Setting> getSettings(String searchValue, String typeFilter, String statusFilter, int page, int recordsPerPage) {
        List<Setting> settings = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM setting WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (searchValue != null && !searchValue.trim().isEmpty()) {
            sql.append(" AND value LIKE ?");
            params.add("%" + searchValue + "%");
        }
        if (typeFilter != null && !typeFilter.isEmpty() && !typeFilter.equals("Type")) {
            sql.append(" AND type = ?");
            params.add(typeFilter);
        }
        if (statusFilter != null && !statusFilter.isEmpty() && !statusFilter.equals("Status")) {
            sql.append(" AND status = ?");
            params.add(statusFilter);
        }

        sql.append(" ORDER BY setting_id LIMIT ?, ?");
        params.add((page - 1) * recordsPerPage);
        params.add(recordsPerPage);

        // Thêm dòng log này để in ra câu lệnh SQL
        System.out.println("Executing SQL in DAO: " + sql.toString());
        try (PreparedStatement statement = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                statement.setObject(i + 1, params.get(i));
            }
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Setting setting = new Setting();
                setting.setSettingID(resultSet.getInt("setting_id"));
                setting.setType(resultSet.getString("type"));
                setting.setOrder(resultSet.getInt("order"));
                setting.setValue(resultSet.getString("value"));
                setting.setStatus(resultSet.getString("status"));
                settings.add(setting);
            }
            // Thêm dòng log này để kiểm tra kích thước của settings trong DAO
            System.out.println("Size of settings in DAO: " + settings.size());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return settings;
    }

    public int getTotalSettings(String searchValue, String typeFilter, String statusFilter) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM setting WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (searchValue != null && !searchValue.trim().isEmpty()) {
            sql.append(" AND value LIKE ?");
            params.add("%" + searchValue + "%");
        }
        if (typeFilter != null && !typeFilter.isEmpty() && !typeFilter.equals("Type")) {
            sql.append(" AND type = ?");
            params.add(typeFilter);
        }
        if (statusFilter != null && !statusFilter.isEmpty() && !statusFilter.equals("Status")) {
            sql.append(" AND status = ?");
            params.add(statusFilter);
        }

        try (PreparedStatement statement = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                statement.setObject(i + 1, params.get(i));
            }
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<String> getAllTypes() {
        List<String> types = new ArrayList<>();
        String sql = "SELECT DISTINCT type FROM setting ORDER BY type";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                types.add(resultSet.getString("type"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return types;
    }

    public void deactivateSetting(int id) {
        String sql = "UPDATE setting SET status = 'inactive' WHERE setting_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbContext.closeConnection(); // Cân nhắc thời điểm đóng kết nối
        }
    }

    public void activateSetting(int id) {
        String sql = "UPDATE setting SET status = 'active' WHERE setting_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbContext.closeConnection(); // Cân nhắc thời điểm đóng kết nối
        }
    }

    public Setting getSettingById(int id) {
        Setting setting = null;
        String sql = "SELECT * FROM setting WHERE setting_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                setting = new Setting();
                setting.setSettingID(resultSet.getInt("setting_id"));
                setting.setType(resultSet.getString("type"));
                setting.setOrder(resultSet.getInt("order"));
                setting.setValue(resultSet.getString("value"));
                setting.setStatus(resultSet.getString("status"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbContext.closeConnection(); // Cân nhắc thời điểm đóng kết nối
        }
        return setting;
    }

    public void updateSetting(Setting setting) {
        String sql = "UPDATE setting SET type = ?, `order` = ?, value = ?, status = ? WHERE setting_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, setting.getType());
            statement.setInt(2, setting.getOrder());
            statement.setString(3, setting.getValue());
            statement.setString(4, setting.getStatus());
            statement.setInt(5, setting.getSettingID());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbContext.closeConnection(); // Cân nhắc thời điểm đóng kết nối
        }
    }

    public void addSetting(Setting setting) {
        String sql = "INSERT INTO setting (type, `order`, value, status) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, setting.getType());
            statement.setInt(2, setting.getOrder());
            statement.setString(3, setting.getValue());
            statement.setString(4, setting.getStatus());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbContext.closeConnection(); // Cân nhắc thời điểm đóng kết nối
        }
    }
}