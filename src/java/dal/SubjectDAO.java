package dal;

import model.Subject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level; // Import này quan trọng
import java.util.logging.Logger; // Import này quan trọng

public class SubjectDAO extends DBContext {
    private Connection conn = null;
    private PreparedStatement ps = null;
    private ResultSet rs = null;

    // Khai báo Logger để ghi log lỗi
    private static final Logger LOGGER = Logger.getLogger(SubjectDAO.class.getName());

    /**
     * Count the number of lessons for a given subject ID.
     * @param subjectId ID of the subject
     * @return Number of lessons associated with the subject
     */
    public int getLessonCountBySubjectId(int subjectId) {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM Lesson WHERE SubjectId = ?";
        try {
            conn = getConnection(); // Lấy kết nối từ DBContext
            if (conn == null) {
                LOGGER.log(Level.SEVERE, "Failed to get database connection in getLessonCountBySubjectId for subjectId: " + subjectId);
                return 0; // Trả về 0 nếu không có kết nối
            }
            ps = conn.prepareStatement(sql);
            ps.setInt(1, subjectId);
            rs = ps.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQL Error in getLessonCountBySubjectId for subjectId: " + subjectId + ". Query: " + sql, e);
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
        // Đảm bảo tên bảng và tên cột trùng khớp CHÍNH XÁC với database của bạn
        // Cần chú ý đến trường 'featured' - nếu trong CSDL là TINYINT(1) hoặc BIT, rs.getBoolean() vẫn hoạt động
        // Nếu nó là INT hoặc VARCHAR và chứa giá trị khác '0' hoặc '1', có thể gây lỗi.
        String sql = "SELECT subjectid, name, featured, thumbnail, description, ownerID, status, categoryName " +
                     "FROM Subject WHERE subjectid = ?";

        try {
            conn = getConnection(); // Lấy kết nối từ DBContext
            if (conn == null) {
                LOGGER.log(Level.SEVERE, "Failed to get database connection in getSubjectById for subjectId: " + subjectId);
                return null; // Trả về null nếu không có kết nối
            }
            ps = conn.prepareStatement(sql);
            ps.setInt(1, subjectId);
            rs = ps.executeQuery();

            if (rs.next()) {
                subject = new Subject();
                subject.setSubjectId(rs.getInt("subjectid"));
                subject.setName(rs.getString("name"));
                
                // --- Kiểm tra đặc biệt cho trường featured ---
                try {
                    // Cố gắng đọc dưới dạng boolean trước
                    subject.setFeatured(rs.getBoolean("featured"));
                } catch (SQLException e) {
                    // Nếu lỗi khi đọc boolean (ví dụ: cột không phải kiểu boolean), thử đọc dưới dạng int
                    // Đây là cách xử lý dự phòng nếu featured là INT (0/1) nhưng vẫn báo lỗi
                    LOGGER.log(Level.WARNING, "Could not read 'featured' as boolean for subjectId " + subjectId + ". Trying as int.", e);
                    try {
                        int featuredInt = rs.getInt("featured");
                        subject.setFeatured(featuredInt == 1); // 1 là true, 0 là false
                    } catch (SQLException e2) {
                        LOGGER.log(Level.SEVERE, "Failed to read 'featured' as both boolean and int for subjectId " + subjectId, e2);
                        // Đặt giá trị mặc định hoặc ném lại ngoại lệ nếu cần
                        subject.setFeatured(false); // Mặc định là false nếu không đọc được
                    }
                }
                // --- Kết thúc kiểm tra đặc biệt cho featured ---
                
                subject.setThumbnail(rs.getString("thumbnail"));
                subject.setDescription(rs.getString("description"));
                subject.setOwnerId(rs.getInt("ownerID"));
                subject.setStatus(rs.getString("status"));
                subject.setCategoryName(rs.getString("categoryName"));
            } else {
                // Ghi log nếu không tìm thấy môn học nào với ID này
                LOGGER.log(Level.INFO, "No subject found in database for subjectId: " + subjectId);
            }
        } catch (SQLException e) {
            // Đây là nơi quan trọng nhất để ghi log chi tiết lỗi SQL
            LOGGER.log(Level.SEVERE, "SQL Error in getSubjectById for subjectId: " + subjectId + ". Query: " + sql, e);
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
            if (conn == null) {
                LOGGER.log(Level.SEVERE, "Failed to get database connection in getAllCategories.");
                return categories;
            }
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                categories.add(rs.getString("categoryName"));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQL Error in getAllCategories. Query: " + sql, e);
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
            if (conn == null) {
                LOGGER.log(Level.SEVERE, "Failed to get database connection in getAllStatuses.");
                return statuses;
            }
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                statuses.add(rs.getString("status"));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQL Error in getAllStatuses. Query: " + sql, e);
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
            if (conn == null) {
                LOGGER.log(Level.SEVERE, "Failed to get database connection in updateSubject for subjectId: " + subject.getSubjectId());
                return false;
            }
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
            LOGGER.log(Level.SEVERE, "SQL Error in updateSubject for subjectId: " + subject.getSubjectId() + ". Query: " + sql, e);
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
            if (conn == null) {
                LOGGER.log(Level.SEVERE, "Failed to get database connection in deleteSubject for subjectId: " + subjectId);
                return false;
            }
            ps = conn.prepareStatement(sql);
            ps.setInt(1, subjectId);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQL Error in deleteSubject for subjectId: " + subjectId + ". Query: " + sql, e);
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
            if (conn == null) {
                LOGGER.log(Level.SEVERE, "Failed to get database connection in getAllSubjects.");
                return subjects;
            }
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
                
                // Xử lý featured tương tự như getSubjectById
                try {
                    subject.setFeatured(rs.getBoolean("featured"));
                } catch (SQLException e) {
                    LOGGER.log(Level.WARNING, "Could not read 'featured' as boolean in getAllSubjects. Trying as int.", e);
                    try {
                        int featuredInt = rs.getInt("featured");
                        subject.setFeatured(featuredInt == 1);
                    } catch (SQLException e2) {
                        LOGGER.log(Level.SEVERE, "Failed to read 'featured' as both boolean and int in getAllSubjects.", e2);
                        subject.setFeatured(false);
                    }
                }
                
                subject.setThumbnail(rs.getString("thumbnail"));
                subject.setDescription(rs.getString("description"));
                subject.setOwnerId(rs.getInt("ownerID"));
                subject.setStatus(rs.getString("status"));
                subject.setCategoryName(rs.getString("categoryName"));
                subjects.add(subject);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQL Error in getAllSubjects. Query: " + sql.toString(), e);
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
            LOGGER.log(Level.SEVERE, "Error closing database resources.", e);
        }
    }
}