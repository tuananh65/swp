package dal;

import model.Subject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Lớp DAO để quản lý chi tiết và cập nhật thông tin môn học trong cơ sở dữ liệu.
 */
public class SubjectDAO extends DBContext {
    private Connection conn = null;
    private PreparedStatement ps = null;
    private ResultSet rs = null;

    /**
     * Lấy thông tin môn học theo ID.
     * @param subjectId ID của môn học
     * @return Đối tượng Subject hoặc null nếu không tìm thấy
     */
    public Subject getSubjectById(int subjectId) {
        Subject subject = null;
        String sql = "SELECT subjectid, name, featured, thumbnail, description, numberOfLesson, ownerID, status, categoryName " +
                     "FROM Subject WHERE subjectid = ?";

        try {
            conn = getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, subjectId);
            rs = ps.executeQuery();

            if (rs.next()) {
                subject = new Subject();
                subject.setSubjectId(rs.getInt("subjectid"));
                subject.setName(rs.getString("name"));
                subject.setFeatured(rs.getBoolean("featured"));
                subject.setThumbnail(rs.getString("thumbnail"));
                subject.setDescription(rs.getString("description"));
                subject.setNumberOfLesson(rs.getInt("numberOfLesson"));
                subject.setOwnerId(rs.getInt("ownerID"));
                subject.setStatus(rs.getString("status"));
                subject.setCategoryName(rs.getString("categoryName"));
            }
        } catch (SQLException e) {
            // Xử lý ngoại lệ (có thể log lỗi ở đây)
        } finally {
            closeResources();
        }
        return subject;
    }

    /**
     * Lấy danh sách tất cả các category duy nhất từ bảng Subject.
     * @return Danh sách các categoryName
     */
    public List<String> getAllCategories() {
        List<String> categories = new ArrayList<>();
        String sql = "SELECT DISTINCT categoryName FROM Subject WHERE categoryName IS NOT NULL";
        try {
            conn = getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                categories.add(rs.getString("categoryName"));
            }
        } catch (SQLException e) {
            // Xử lý ngoại lệ (có thể log lỗi ở đây)
        } finally {
            closeResources();
        }
        return categories;
    }
    
    public List<String> getAllStatuses() {
        List<String> statuses = new ArrayList<>();
        String sql = "SELECT DISTINCT status FROM Subject WHERE status IS NOT NULL";
        try {
            conn = getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                statuses.add(rs.getString("status"));
            }
        } catch (SQLException e) {
            // Log error here
        } finally {
            closeResources();
        }
        return statuses;
    }

    /**
     * Cập nhật thông tin môn học.
     * @param subject Đối tượng Subject chứa thông tin cần cập nhật
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    public boolean updateSubject(Subject subject) {
        String sql = "UPDATE Subject SET name = ?, featured = ?, thumbnail = ?, description = ?, numberOfLesson = ?, " +
                     "ownerID = ?, status = ?, categoryName = ? WHERE subjectid = ?";
        try {
            conn = getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, subject.getName());
            ps.setBoolean(2, subject.isFeatured());
            ps.setString(3, subject.getThumbnail());
            ps.setString(4, subject.getDescription());
            ps.setInt(5, subject.getNumberOfLesson());
            ps.setInt(6, subject.getOwnerId());
            ps.setString(7, subject.getStatus());
            ps.setString(8, subject.getCategoryName());
            ps.setInt(9, subject.getSubjectId());
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            return false;
        } finally {
            closeResources();
        }
    }
    
    public boolean deleteSubject(int subjectId) {
        String sql = "DELETE FROM Subject WHERE subjectid = ?";
        try {
            conn = getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, subjectId);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            // Log error here
            return false;
        } finally {
            closeResources();
        }
    }
    
    public List<Subject> getAllSubjects(String category, String status, String search) {
        List<Subject> subjects = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT subjectid, name, featured, thumbnail, description, numberOfLesson, ownerID, status, categoryName FROM Subject WHERE 1=1");
        
        if (category != null && !category.isEmpty()) {
            sql.append(" AND categoryName = ?");
        }
        if (status != null && !status.isEmpty()) {
            sql.append(" AND status = ?");
        }
        if (search != null && !search.isEmpty()) {
            sql.append(" AND name LIKE ?");
        }

        try {
            conn = getConnection();
            ps = conn.prepareStatement(sql.toString());
            int paramIndex = 1;
            
            if (category != null && !category.isEmpty()) {
                ps.setString(paramIndex++, category);
            }
            if (status != null && !status.isEmpty()) {
                ps.setString(paramIndex++, status);
            }
            if (search != null && !search.isEmpty()) {
                ps.setString(paramIndex, "%" + search + "%");
            }

            rs = ps.executeQuery();
            while (rs.next()) {
                Subject subject = new Subject();
                subject.setSubjectId(rs.getInt("subjectid"));
                subject.setName(rs.getString("name"));
                subject.setFeatured(rs.getBoolean("featured"));
                subject.setThumbnail(rs.getString("thumbnail"));
                subject.setDescription(rs.getString("description"));
                subject.setNumberOfLesson(rs.getInt("numberOfLesson"));
                subject.setOwnerId(rs.getInt("ownerID"));
                subject.setStatus(rs.getString("status"));
                subject.setCategoryName(rs.getString("categoryName"));
                subjects.add(subject);
            }
        } catch (SQLException e) {
            // Log error here
        } finally {
            closeResources();
        }
        return subjects;
    }
    

    /**
     * Đóng các tài nguyên kết nối cơ sở dữ liệu.
     */
    private void closeResources() {
        try {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            // Xử lý ngoại lệ (có thể log lỗi ở đây)
        }
    }
}