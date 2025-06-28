package dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import model.Slider;

public class SliderDAO extends DBContext {

    private Connection conn = null;
    private PreparedStatement ps = null;
    private ResultSet rs = null;

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

    // Phương thức để lấy TẤT CẢ sliders (cho trang quản lý)
    public List<Slider> getAllSliders() {
        List<Slider> sliders = new ArrayList<>();
        String query = "SELECT sliderId, title, image, backlink, status, note, createdBy, createdAt FROM Slider ORDER BY sliderId ASC";

        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                Slider slider = new Slider(
                    rs.getInt("sliderId"),
                    rs.getString("title"),
                    rs.getString("image"),
                    rs.getString("backlink"),
                    rs.getString("status"),
                    rs.getString("note"),
                    rs.getString("createdBy"),
                    rs.getTimestamp("createdAt")
                );
                sliders.add(slider);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return sliders;
    }

    // Phương thức để lấy CHỈ các sliders đang ACTIVE (cho trang người dùng)
    public List<Slider> getAllActiveSliders() {
        List<Slider> activeSliders = new ArrayList<>();
        // CHỈ LẤY NHỮNG SLIDER CÓ STATUS LÀ 'active'
        String query = "SELECT sliderId, title, image, backlink, status, note, createdBy, createdAt FROM Slider WHERE status = 'active' ORDER BY sliderId ASC";

        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                Slider slider = new Slider(
                    rs.getInt("sliderId"),
                    rs.getString("title"),
                    rs.getString("image"),
                    rs.getString("backlink"),
                    rs.getString("status"),
                    rs.getString("note"),
                    rs.getString("createdBy"),
                    rs.getTimestamp("createdAt")
                );
                activeSliders.add(slider);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return activeSliders;
    }

    public List<Slider> getPaginatedSliders(int page, int pageSize, String search, String filterStatus) {
        List<Slider> sliders = new ArrayList<>();
        // Thêm createdBy, createdAt nếu bạn muốn hiển thị chúng trên bảng quản lý
        StringBuilder query = new StringBuilder("SELECT sliderId, title, image, backlink, status, note, createdBy, createdAt FROM Slider WHERE 1=1 ");

        if (search != null && !search.trim().isEmpty()) {
            query.append(" AND (title LIKE ? OR note LIKE ?) "); // Tìm kiếm cả trong title và note
        }
        if (filterStatus != null && !filterStatus.trim().isEmpty()) {
            query.append(" AND status = ? ");
        }

        query.append("ORDER BY sliderId ASC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");

        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query.toString());

            int paramIndex = 1;
            if (search != null && !search.trim().isEmpty()) {
                ps.setString(paramIndex++, "%" + search + "%");
                ps.setString(paramIndex++, "%" + search + "%"); // cho note
            }
            if (filterStatus != null && !filterStatus.trim().isEmpty()) {
                ps.setString(paramIndex++, filterStatus);
            }

            int offset = (page - 1) * pageSize;
            ps.setInt(paramIndex++, offset);
            ps.setInt(paramIndex++, pageSize);

            rs = ps.executeQuery();
            while (rs.next()) {
                Slider slider = new Slider(
                    rs.getInt("sliderId"),
                    rs.getString("title"),
                    rs.getString("image"),
                    rs.getString("backlink"),
                    rs.getString("status"),
                    rs.getString("note"),
                    rs.getString("createdBy"),
                    rs.getTimestamp("createdAt")
                );
                sliders.add(slider);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return sliders;
    }

    public int getTotalSliders(String search, String filterStatus) {
        String query = "SELECT COUNT(*) FROM Slider WHERE 1=1 ";
        StringBuilder finalQuery = new StringBuilder(query);

        if (search != null && !search.trim().isEmpty()) {
            finalQuery.append(" AND (title LIKE ? OR note LIKE ?) "); // Tìm kiếm cả trong title và note
        }
        if (filterStatus != null && !filterStatus.trim().isEmpty()) {
            finalQuery.append(" AND status = ? ");
        }

        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(finalQuery.toString());

            int paramIndex = 1;
            if (search != null && !search.trim().isEmpty()) {
                ps.setString(paramIndex++, "%" + search + "%");
                ps.setString(paramIndex++, "%" + search + "%"); // cho note
            }
            if (filterStatus != null && !filterStatus.trim().isEmpty()) {
                ps.setString(paramIndex++, filterStatus);
            }

            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return 0;
    }

    public List<Slider> filterSlidersByStatus(String status, int offset, int limit) {
        List<Slider> list = new ArrayList<>();
        String sql = "SELECT sliderId, title, image, backlink, status, note, createdBy, createdAt FROM Slider WHERE status = ? ORDER BY sliderId OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, status);
            ps.setInt(2, offset);
            ps.setInt(3, limit);
            rs = ps.executeQuery();
            while (rs.next()) {
                Slider s = new Slider();
                s.setSliderId(rs.getInt("sliderId"));
                s.setTitle(rs.getString("title"));
                s.setImage(rs.getString("image"));
                s.setBacklink(rs.getString("backlink"));
                s.setStatus(rs.getString("status"));
                s.setNote(rs.getString("note"));
                s.setCreatedBy(rs.getString("createdBy"));
                s.setCreatedAt(rs.getTimestamp("createdAt"));
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
        String sql = "SELECT COUNT(*) FROM Slider WHERE status = ?";
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

    public List<Slider> searchSliders(String keyword, int offset, int limit) {
        List<Slider> list = new ArrayList<>();
        // Tìm kiếm trên nhiều cột, bao gồm note, createdBy, createdAt
        String sql = "SELECT sliderId, title, image, backlink, status, note, createdBy, createdAt FROM Slider WHERE sliderId LIKE ? OR title LIKE ? OR image LIKE ? OR backlink LIKE ? OR note LIKE ? OR createdBy LIKE ? ORDER BY sliderId OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(sql);
            String searchKeyword = "%" + keyword + "%";
            ps.setString(1, searchKeyword);
            ps.setString(2, searchKeyword);
            ps.setString(3, searchKeyword);
            ps.setString(4, searchKeyword);
            ps.setString(5, searchKeyword); // Cho note
            ps.setString(6, searchKeyword); // Cho createdBy
            ps.setInt(7, offset);
            ps.setInt(8, limit);
            rs = ps.executeQuery();
            while (rs.next()) {
                Slider s = new Slider();
                s.setSliderId(rs.getInt("sliderId"));
                s.setTitle(rs.getString("title"));
                s.setImage(rs.getString("image"));
                s.setBacklink(rs.getString("backlink"));
                s.setStatus(rs.getString("status"));
                s.setNote(rs.getString("note"));
                s.setCreatedBy(rs.getString("createdBy"));
                s.setCreatedAt(rs.getTimestamp("createdAt"));
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
        // Tìm kiếm trên nhiều cột, bao gồm note, createdBy
        String sql = "SELECT COUNT(*) FROM Slider WHERE sliderId LIKE ? OR title LIKE ? OR image LIKE ? OR backlink LIKE ? OR note LIKE ? OR createdBy LIKE ?";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(sql);
            String searchKeyword = "%" + keyword + "%";
            ps.setString(1, searchKeyword);
            ps.setString(2, searchKeyword);
            ps.setString(3, searchKeyword);
            ps.setString(4, searchKeyword);
            ps.setString(5, searchKeyword); // Cho note
            ps.setString(6, searchKeyword); // Cho createdBy
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

    public boolean addSlider(Slider slider) {
        // Thêm cột 'note', 'createdBy', 'createdAt' vào câu lệnh INSERT
        String sql = "INSERT INTO Slider (title, image, backlink, status, note, createdBy, createdAt) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, slider.getTitle());
            ps.setString(2, slider.getImage());
            ps.setString(3, slider.getBacklink());
            ps.setString(4, slider.getStatus());
            ps.setString(5, slider.getNote());
            ps.setString(6, slider.getCreatedBy()); // Đảm bảo bạn set createdBy
            ps.setTimestamp(7, slider.getCreatedAt()); // Đảm bảo bạn set createdAt
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeResources();
        }
    }

    public Slider getSliderById(int sliderId) {
        // Đảm bảo bạn lấy tất cả các cột cần thiết
        String query = "SELECT sliderId, title, image, backlink, status, note, createdBy, createdAt FROM Slider WHERE sliderId = ?";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, sliderId);
            rs = ps.executeQuery();
            if (rs.next()) {
                return new Slider(
                    rs.getInt("sliderId"),
                    rs.getString("title"),
                    rs.getString("image"),
                    rs.getString("backlink"),
                    rs.getString("status"),
                    rs.getString("note"),
                    rs.getString("createdBy"),
                    rs.getTimestamp("createdAt")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return null;
    }

    public boolean updateSlider(Slider slider) {
        // Thêm cột 'note' vào câu lệnh UPDATE nếu bạn muốn cập nhật nó.
        // CreatedBy và createdAt thường không được cập nhật qua hàm này.
        String sql = "UPDATE Slider SET title = ?, image = ?, backlink = ?, status = ?, note = ? WHERE sliderId = ?";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, slider.getTitle());
            ps.setString(2, slider.getImage());
            ps.setString(3, slider.getBacklink());
            ps.setString(4, slider.getStatus());
            ps.setString(5, slider.getNote());
            ps.setInt(6, slider.getSliderId());
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeResources();
        }
    }

    public boolean updateSliderStatus(int sliderId, String status) {
        String query = "UPDATE Slider SET status = ? WHERE sliderId = ?";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, status);
            ps.setInt(2, sliderId);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return false;
    }

    public boolean deleteSlider(int sliderId) {
        String query = "DELETE FROM Slider WHERE sliderId = ?";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, sliderId);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return false;
    }

    public int getTotalSliderCount() {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM Slider";
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

    public List<Slider> getSlidersByPage(int offset, int limit) {
        List<Slider> list = new ArrayList<>();
        String sql = "SELECT sliderId, title, image, backlink, status, note, createdBy, createdAt FROM Slider ORDER BY sliderId OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, offset);
            ps.setInt(2, limit);
            rs = ps.executeQuery();
            while (rs.next()) {
                Slider s = new Slider();
                s.setSliderId(rs.getInt("sliderId"));
                s.setTitle(rs.getString("title"));
                s.setImage(rs.getString("image"));
                s.setBacklink(rs.getString("backlink"));
                s.setStatus(rs.getString("status"));
                s.setNote(rs.getString("note"));
                s.setCreatedBy(rs.getString("createdBy"));
                s.setCreatedAt(rs.getTimestamp("createdAt"));
                list.add(s);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return list;
    }

    public List<Slider> filterSlidersByType(String type, int offset, int limit) {
        List<Slider> list = new ArrayList<>();
        String sql = "SELECT sliderId, title, image, backlink, status, note, createdBy, createdAt FROM Slider WHERE title LIKE ? ORDER BY sliderId OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, "%" + type + "%");
            ps.setInt(2, offset);
            ps.setInt(3, limit);
            rs = ps.executeQuery();
            while (rs.next()) {
                Slider s = new Slider();
                s.setSliderId(rs.getInt("sliderId"));
                s.setTitle(rs.getString("title"));
                s.setImage(rs.getString("image"));
                s.setBacklink(rs.getString("backlink"));
                s.setStatus(rs.getString("status"));
                s.setNote(rs.getString("note"));
                s.setCreatedBy(rs.getString("createdBy"));
                s.setCreatedAt(rs.getTimestamp("createdAt"));
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
        String sql = "SELECT COUNT(*) FROM Slider WHERE title LIKE ?";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, "%" + type + "%");
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
        SliderDAO dao = new SliderDAO();

        System.out.println("--- Test getAllActiveSliders() ---");
        List<Slider> activeSlidersTest = dao.getAllActiveSliders();
        for (Slider slider : activeSlidersTest) {
            System.out.println("Active Slider: ID=" + slider.getSliderId() + ", Title=" + slider.getTitle() + ", Status=" + slider.getStatus());
        }

        System.out.println("\n--- Test getPaginatedSliders(1, 3, null, null) ---");
        List<Slider> paginatedSliders = dao.getPaginatedSliders(1, 3, null, null);
        for (Slider slider : paginatedSliders) {
            System.out.println("Paginated Slider: ID=" + slider.getSliderId() + ", Title=" + slider.getTitle() + ", Status=" + slider.getStatus() + ", Note=" + slider.getNote() + ", CreatedBy=" + slider.getCreatedBy() + ", CreatedAt=" + slider.getCreatedAt());
        }

        System.out.println("\n--- Test filterSlidersByStatus(\"active\", 0, 10) ---");
        List<Slider> filteredActiveSliders = dao.filterSlidersByStatus("active", 0, 10);
        for (Slider slider : filteredActiveSliders) {
            System.out.println("Filtered Active Slider: ID=" + slider.getSliderId() + ", Title=" + slider.getTitle() + ", Status=" + slider.getStatus() + ", Note=" + slider.getNote());
        }
        System.out.println("Total active sliders: " + dao.getTotalFilteredResultsByStatus("active"));

        System.out.println("\n--- Test searchSliders(\"Test\", 0, 10) ---");
        List<Slider> searchResult = dao.searchSliders("Test", 0, 10);
        for (Slider slider : searchResult) {
            System.out.println("Searched Slider: ID=" + slider.getSliderId() + ", Title=" + slider.getTitle() + ", Note=" + slider.getNote() + ", CreatedBy=" + slider.getCreatedBy());
        }
        System.out.println("Total search results for 'Test': " + dao.getTotalSearchResults("Test"));

        // Test addSlider (chú ý cung cấp giá trị cho createdBy và createdAt)
        System.out.println("\n--- Test addSlider ---");
        // Đảm bảo class Slider có constructor và setters cho createdBy, createdAt
        // Timestamp now = new Timestamp(System.currentTimeMillis());
        // Slider newSlider = new Slider(0, "New Test Slider from Main", "image_new_main.jpg", "link_new_main.com", "inactive", "This is a new note from main test.", "admin", now);
        // boolean added = dao.addSlider(newSlider);
        // System.out.println("Added new slider: " + added);

        // Test getSliderById
        // System.out.println("\n--- Test getSliderById(1) ---");
        // Slider retrievedSlider = dao.getSliderById(1);
        // if (retrievedSlider != null) {
        //     System.out.println("Retrieved Slider by ID 1: " + retrievedSlider.getTitle() + ", Status: " + retrievedSlider.getStatus() + ", Note: " + retrievedSlider.getNote() + ", CreatedBy: " + retrievedSlider.getCreatedBy() + ", CreatedAt: " + retrievedSlider.getCreatedAt());
        // } else {
        //     System.out.println("Slider with ID 1 not found.");
        // }

        // Test updateSlider
        // if (retrievedSlider != null) {
        //     System.out.println("\n--- Test updateSlider ---");
        //     retrievedSlider.setTitle("Updated Title from Main");
        //     retrievedSlider.setNote("Updated Note for slider 1 from main test.");
        //     boolean updated = dao.updateSlider(retrievedSlider);
        //     System.out.println("Updated slider 1: " + updated);
        // }

         //Test updateSliderStatus
         System.out.println("\n--- Test updateSliderStatus ---");
         boolean updatedStatus = dao.updateSliderStatus(1, "active"); // Change status of slider 1 to active
         System.out.println("Slider 1 status updated to active: " + updatedStatus);
    }
}