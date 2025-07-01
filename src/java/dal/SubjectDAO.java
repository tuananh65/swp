package dal;

import model.Subject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SubjectDAO extends DBContext {
    private Connection conn = null;
    private PreparedStatement ps = null;
    private ResultSet rs = null;

    /**
     * Count the number of lessons for a given subject ID.
     * @param subjectId ID of the subject
     * @return Number of lessons associated with the subject
     */
    public int getLessonCountBySubjectId(int subjectId) {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM Lesson WHERE SubjectId = ?";
        try {
            conn = getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, subjectId);
            rs = ps.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            // Log error here
        } finally {
            closeResources();
        }
        return count;
    }

    /**
     * Get subject by ID.
     * @param subjectId ID of the subject
     * @return Subject object or null if not found
     */
    public Subject getSubjectById(int subjectId) {
        Subject subject = null;
        String sql = "SELECT subjectid, name, featured, thumbnail, description, ownerID, status, categoryName" +
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
                subject.setOwnerId(rs.getInt("ownerID"));
                subject.setStatus(rs.getString("status"));
                subject.setCategoryName(rs.getString("categoryName"));
            }
        } catch (SQLException e) {
            // Log error here
        } finally {
            closeResources();
        }
        return subject;
    }

    /**
     * Get all unique categories from the Subject table.
     * @return List of category names
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
            // Log error here
        } finally {
            closeResources();
        }
        return categories;
    }

    /**
     * Get all unique statuses from the Subject table.
     * @return List of statuses
     */
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
     * Update subject information.
     * @param subject Subject object with updated information
     * @return true if update successful, false otherwise
     */
    public boolean updateSubject(Subject subject) {
        String sql = "UPDATE Subject SET name = ?, featured = ?, thumbnail = ?, description = ?, " +
                     "ownerID = ?, status = ?, categoryName = ? WHERE subjectid = ?";
        try {
            conn = getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, subject.getName());
            ps.setBoolean(2, subject.isFeatured());
            ps.setString(3, subject.getThumbnail());
            ps.setString(4, subject.getDescription());
            ps.setInt(5, subject.getOwnerId());
            ps.setString(6, subject.getStatus());
            ps.setString(7, subject.getCategoryName());
            ps.setInt(8, subject.getSubjectId());
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            return false;
        } finally {
            closeResources();
        }
    }

    /**
     * Delete a subject by ID.
     * @param subjectId ID of the subject
     * @return true if deletion successful, false otherwise
     */
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

    /**
     * Get all subjects with optional filtering.
     * @param category Category filter
     * @param status Status filter
     * @param search Search keyword
     * @return List of subjects
     */
    public List<Subject> getAllSubjects(String category, String status, String search) {
        List<Subject> subjects = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT subjectid, name, featured, thumbnail, description, ownerID, status, categoryName FROM Subject WHERE 1=1");
        
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
     * Close database resources.
     */
    private void closeResources() {
        try {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            // Log error here
        }
    }
}