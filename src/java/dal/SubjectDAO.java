package dal;

import model.Subject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level; // Import này cần thiết
import java.util.logging.Logger; // Import này cần thiết

/**
 * Lớp DAO để quản lý chi tiết và cập nhật thông tin môn học trong cơ sở dữ liệu.
 */
public class SubjectDAO extends DBContext {

    // Thêm Logger để xử lý log lỗi nhất quán
    private static final Logger LOGGER = Logger.getLogger(SubjectDAO.class.getName());

    // Không cần khai báo conn, ps, rs ở đây nữa vì getConnection() sẽ cung cấp chúng trong try-with-resources
    // và chúng sẽ tự động đóng.
    // private Connection conn = null;
    // private PreparedStatement ps = null;
    // private ResultSet rs = null;

    /**
     * Lấy thông tin môn học theo ID.
     * @param subjectId ID của môn học
     * @return Đối tượng Subject hoặc null nếu không tìm thấy
     */
    public Subject getSubjectById(int subjectId) {
        Subject subject = null;
        String sql = "SELECT subjectid, name, featured, thumbnail, description, numberOfLesson, ownerID, status, categoryName " +
                     "FROM Subject WHERE subjectid = ?";

        // Sử dụng try-with-resources để đảm bảo Connection, PreparedStatement, ResultSet được đóng tự động
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, subjectId);
            try (ResultSet rs = ps.executeQuery()) {
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
            }
        } catch (SQLException e) {
            // Log lỗi chi tiết thay vì nuốt lỗi
            LOGGER.log(Level.SEVERE, "Error getting subject by ID: " + subjectId, e);
        } 
        // Không cần khối finally để đóng tài nguyên nữa vì đã dùng try-with-resources
        return subject;
    }

    /**
     * Lấy danh sách tất cả các category duy nhất từ bảng Subject.
     * @return Danh sách các categoryName
     */
    public List<String> getAllCategories() {
        List<String> categories = new ArrayList<>();
        String sql = "SELECT DISTINCT categoryName FROM Subject WHERE categoryName IS NOT NULL AND categoryName <> ''"; // Thêm điều kiện không rỗng
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) { // ResultSet cũng có thể trong try-with-resources nếu nó là kết quả của ps.executeQuery()

            while (rs.next()) {
                categories.add(rs.getString("categoryName"));
            }
        } catch (SQLException e) {
            // Log lỗi chi tiết
            LOGGER.log(Level.SEVERE, "Error getting all categories.", e);
        }
        return categories;
    }

    /**
     * Cập nhật thông tin môn học.
     * @param subject Đối tượng Subject chứa thông tin cần cập nhật
     * @return true nếu cập nhật thành công, false nếu thất bại
     */
    public boolean updateSubject(Subject subject) {
        String sql = "UPDATE Subject SET name = ?, featured = ?, thumbnail = ?, description = ?, numberOfLesson = ?, " +
                     "ownerID = ?, status = ?, categoryName = ? WHERE subjectid = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
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
            // Log lỗi chi tiết
            LOGGER.log(Level.SEVERE, "Error updating subject: " + subject.getSubjectId(), e);
            return false;
        } 
    }
}