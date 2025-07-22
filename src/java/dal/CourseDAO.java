package dal;

import java.math.BigDecimal;
import model.Course;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Date; // <-- Thêm import cho java.util.Date

public class CourseDAO extends DBContext {

    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    // Lấy tất cả khóa học
    public List<Course> getAllCourses() {
        List<Course> list = new ArrayList<>();
        String sql = "SELECT CourseID, CourseName, TagLine, BriefInfo, Description, OriginalPrice, SalePrice, CourseThumbnail, CreatedAt, UpdatedAt, UserID, Featured FROM Course";

        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Course course = extractCourse(rs);
                list.add(course);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi trong getAllCourses: " + e.getMessage());
        } finally {
            closeResources();
        }
        return list;
    }

    // Lấy khóa học theo ID
    public Course getCourseById(int courseId) {
        String sql = "SELECT CourseID, CourseName, TagLine, BriefInfo, Description, OriginalPrice, SalePrice, CourseThumbnail, CreatedAt, UpdatedAt, UserID, Featured FROM Course WHERE CourseID = ?";

        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, courseId);
            rs = ps.executeQuery();

            if (rs.next()) {
                return extractCourse(rs);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi trong getCourseById: " + e.getMessage());
        } finally {
            closeResources();
        }
        return null;
    }

    // Thêm khóa học mới
    public boolean insertCourse(Course course) {
        String sql = "INSERT INTO Course (CourseName, TagLine, BriefInfo, Description, OriginalPrice, SalePrice, CourseThumbnail, CreatedAt, UpdatedAt, UserID, Featured) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(sql);

            ps.setString(1, course.getCourseName());
            ps.setString(2, course.getTagLine());
            ps.setString(3, course.getBriefInfo());
            ps.setString(4, course.getDescription());
            ps.setDouble(5, course.getOriginalPrice());
            ps.setDouble(6, course.getSalePrice());
            ps.setString(7, course.getCourseThumbnail());
            // Chuyển đổi java.util.Date sang java.sql.Date khi set vào PreparedStatement
            ps.setDate(8, course.getCreatedAt() != null ? new java.sql.Date(course.getCreatedAt().getTime()) : null);
            ps.setDate(9, course.getUpdatedAt() != null ? new java.sql.Date(course.getUpdatedAt().getTime()) : null);
            ps.setInt(10, course.getUserID());
            ps.setBoolean(11, course.isFeatured());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi trong insertCourse: " + e.getMessage());
            return false;
        } finally {
            closeResources();
        }
    }

    // Lấy 3 khóa học nổi bật
    public List<Course> getFeaturedCourses() {
        List<Course> list = new ArrayList<>();
        String sql = "SELECT CourseID, CourseName, TagLine, BriefInfo, Description, OriginalPrice, SalePrice, CourseThumbnail, CreatedAt, UpdatedAt, UserID, Featured FROM Course WHERE Featured = 1 ORDER BY CreatedAt DESC OFFSET 0 ROWS FETCH NEXT 3 ROWS ONLY";

        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                list.add(extractCourse(rs));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi trong getFeaturedCourses: " + e.getMessage());
        } finally {
            closeResources();
        }
        return list;
    }

    // ✅ Tìm kiếm, phân trang, sắp xếp
    public List<Course> searchCourses(String keyword, int offset, int pageSize, String sortBy) {
        List<Course> courses = new ArrayList<>();
        String orderBy = "CourseID"; // fallback

        switch (sortBy) {
            case "latest":
                orderBy = "CreatedAt DESC";
                break;
            case "price_low":
                orderBy = "SalePrice ASC";
                break;
            case "price_high":
                orderBy = "SalePrice DESC";
                break;
            case "name":
                orderBy = "CourseName ASC";
                break;
        }

        String sql = "SELECT CourseID, CourseName, TagLine, BriefInfo, Description, OriginalPrice, SalePrice, CourseThumbnail, CreatedAt, UpdatedAt, UserID, Featured FROM Course WHERE CourseName LIKE ? "
                   + "ORDER BY " + orderBy + " OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, "%" + keyword + "%");
            ps.setInt(2, offset);
            ps.setInt(3, pageSize);
            rs = ps.executeQuery();

            while (rs.next()) {
                courses.add(extractCourse(rs));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi trong searchCourses: " + e.getMessage());
        } finally {
            closeResources();
        }

        return courses;
    }

    // ✅ Đếm số khóa học khớp với keyword
    public int countCourses(String keyword) {
        String sql = "SELECT COUNT(*) FROM Course WHERE CourseName LIKE ?";

        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, "%" + keyword + "%");
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi trong countCourses: " + e.getMessage());
        } finally {
            closeResources();
        }
        return 0;
    }

    public Integer getCourseCreatorId(int courseId) {
        String sql = "SELECT UserID FROM Course WHERE CourseID = ?";

        // Sử dụng try-with-resources để tự động đóng tài nguyên
        try (Connection currentConn = getConnection(); // Đổi tên biến để tránh trùng với conn toàn cục
             PreparedStatement currentPs = currentConn.prepareStatement(sql)) {

            currentPs.setInt(1, courseId);
            try (ResultSet currentRs = currentPs.executeQuery()) { // Sử dụng try-with-resources cho ResultSet
                if (currentRs.next()) {
                    return currentRs.getInt("UserID");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public BigDecimal getCourseBasePrice(int courseId) {
        String sql = "SELECT SalePrice FROM Course WHERE CourseID = ?";
        // Sử dụng try-with-resources để tự động đóng tài nguyên
        try (Connection currentConn = getConnection(); // Đổi tên biến để tránh trùng với conn toàn cục
             PreparedStatement currentPs = currentConn.prepareStatement(sql)) {

            currentPs.setInt(1, courseId);
            try (ResultSet currentRs = currentPs.executeQuery()) { // Sử dụng try-with-resources cho ResultSet
                if (currentRs.next()) {
                    return currentRs.getBigDecimal("SalePrice");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return BigDecimal.ZERO;
    }

    // Tạo Course từ ResultSet
    public static Course extractCourse(ResultSet rs) throws SQLException {
        Course c = new Course();
        c.setCourseID(rs.getInt("CourseID"));
        c.setCourseName(rs.getString("CourseName"));
        c.setTagLine(rs.getString("TagLine"));
        c.setBriefInfo(rs.getString("BriefInfo"));
        c.setDescription(rs.getString("Description"));
        c.setOriginalPrice(rs.getDouble("OriginalPrice"));
        c.setSalePrice(rs.getDouble("SalePrice"));
        c.setCourseThumbnail(rs.getString("CourseThumbnail"));
        c.setLessonID(rs.getInt("LessonID"));
        return c;
    }

    // Đóng tài nguyên
    // Phương thức closeResources này sẽ không cần thiết cho các hàm dùng try-with-resources
    // Nhưng vẫn giữ lại cho các hàm cũ hơn hoặc nếu bạn muốn quản lý đóng thủ công
    private void closeResources() {
        try {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            System.err.println("Lỗi khi đóng tài nguyên: " + e.getMessage());
        }
    }

    // Main test
    public static void main(String[] args) {
        CourseDAO dao = new CourseDAO();
        List<Course> list = dao.searchCourses("java", 0, 5, "price_low");
        // Test insertCourse
        System.out.println("\nTesting insertCourse:");
        Course newCourse = new Course();
        newCourse.setCourseName("New Test Course");
        newCourse.setTagLine("A test tagline");
        newCourse.setBriefInfo("Brief info for test");
        newCourse.setDescription("Detailed description for test course.");
        newCourse.setOriginalPrice(100.0);
        newCourse.setSalePrice(50.0);
        newCourse.setCourseThumbnail("test_thumb.jpg");
        newCourse.setCreatedAt(new Date()); // Current date
        newCourse.setUpdatedAt(new Date()); // Current date
        newCourse.setUserID(1); // Assuming user ID 1 exists
        newCourse.setFeatured(false);

        boolean inserted = dao.insertCourse(newCourse);
        if (inserted) {
            System.out.println("Course inserted successfully!");
        } else {
            System.out.println("Failed to insert course.");
        }
    }
}
