package dal;

import java.math.BigDecimal;
import model.Course;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class CourseDAO extends DBContext {

    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    // Lấy tất cả khóa học
    public List<Course> getAllCourses() {
        List<Course> list = new ArrayList<>();
        String sql = "SELECT CourseID, CourseName, TagLine, BriefInfo, Description, OriginalPrice, SalePrice, CourseThumbnail, CreatedAt, UpdatedAt, UserID, Featured, SubjectID, LessonID FROM Course";

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
        String sql = "SELECT CourseID, CourseName, TagLine, BriefInfo, Description, OriginalPrice, SalePrice, CourseThumbnail, CreatedAt, UpdatedAt, UserID, Featured, SubjectID, LessonID FROM Course WHERE CourseID = ?";

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
        String sql = "INSERT INTO Course (CourseName, TagLine, BriefInfo, Description, OriginalPrice, SalePrice, CourseThumbnail, CreatedAt, UpdatedAt, UserID, Featured, SubjectID, LessonID) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

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
            ps.setDate(8, course.getCreatedAt() != null ? new java.sql.Date(course.getCreatedAt().getTime()) : null);
            ps.setDate(9, course.getUpdatedAt() != null ? new java.sql.Date(course.getUpdatedAt().getTime()) : null);
            ps.setInt(10, course.getUserID());
            ps.setBoolean(11, course.isFeatured());
            ps.setInt(12, course.getSubjectID());
            if (course.getLessonID() != null) {
                ps.setInt(13, course.getLessonID());
            } else {
                ps.setNull(13, java.sql.Types.INTEGER);
            }

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi trong insertCourse: " + e.getMessage());
            return false;
        } finally {
            closeResources();
        }
    }

    // Lấy khóa học nổi bật
    public List<Course> getFeaturedCourses() {
        List<Course> list = new ArrayList<>();
        String sql = "SELECT CourseID, CourseName, TagLine, BriefInfo, Description, OriginalPrice, SalePrice, CourseThumbnail, CreatedAt, UpdatedAt, UserID, Featured, SubjectID, LessonID FROM Course WHERE Featured = 1 ORDER BY CreatedAt DESC OFFSET 0 ROWS FETCH NEXT 3 ROWS ONLY";

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

    // Tìm kiếm, phân trang, sắp xếp
    public List<Course> searchCourses(String keyword, int offset, int pageSize, String sortBy) {
        List<Course> courses = new ArrayList<>();
        String orderBy = "CourseID";

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

        String sql = "SELECT CourseID, CourseName, TagLine, BriefInfo, Description, OriginalPrice, SalePrice, CourseThumbnail, CreatedAt, UpdatedAt, UserID, Featured, SubjectID, LessonID FROM Course WHERE CourseName LIKE ? "
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

        try (Connection currentConn = getConnection();
             PreparedStatement currentPs = currentConn.prepareStatement(sql)) {
            currentPs.setInt(1, courseId);
            try (ResultSet currentRs = currentPs.executeQuery()) {
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

        try (Connection currentConn = getConnection();
             PreparedStatement currentPs = currentConn.prepareStatement(sql)) {
            currentPs.setInt(1, courseId);
            try (ResultSet currentRs = currentPs.executeQuery()) {
                if (currentRs.next()) {
                    return currentRs.getBigDecimal("SalePrice");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return BigDecimal.ZERO;
    }
    // Lấy danh sách các khóa học theo SubjectID
public List<Course> getCoursesBySubjectId(int subjectId) {
    List<Course> list = new ArrayList<>();
    String sql = "SELECT CourseID, CourseName, TagLine, BriefInfo, Description, OriginalPrice, SalePrice, CourseThumbnail, CreatedAt, UpdatedAt, UserID, Featured, SubjectID, LessonID "
               + "FROM Course WHERE SubjectID = ?";

    try {
        conn = new DBContext().getConnection();
        ps = conn.prepareStatement(sql);
        ps.setInt(1, subjectId);
        rs = ps.executeQuery();

        while (rs.next()) {
            Course course = extractCourse(rs);
            list.add(course);
        }
    } catch (SQLException e) {
        System.err.println("Lỗi trong getCoursesBySubjectId: " + e.getMessage());
    } finally {
        closeResources();
    }

    return list;
}





    // ✅ Extract Course từ ResultSet
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
        c.setCreatedAt(rs.getDate("CreatedAt"));
        c.setUpdatedAt(rs.getDate("UpdatedAt"));
        c.setUserID(rs.getInt("UserID"));
        c.setFeatured(rs.getBoolean("Featured"));
        c.setSubjectID(rs.getInt("SubjectID"));
        c.setSubjectID(rs.getInt("LessonID"));
        int lessonId = rs.getInt("LessonID");
        if (rs.wasNull()) {
            c.setLessonID(null);
        } else {
            c.setLessonID(lessonId);
        }

        return c;
    }

    private void closeResources() {
        try {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            System.err.println("Lỗi khi đóng tài nguyên: " + e.getMessage());
        }
    }

    // Test insert
    public static void main(String[] args) {
        CourseDAO dao = new CourseDAO();
        Course newCourse = new Course();
        newCourse.setCourseName("New Test Course");
        newCourse.setTagLine("A test tagline");
        newCourse.setBriefInfo("Brief info for test");
        newCourse.setDescription("Detailed description for test course.");
        newCourse.setOriginalPrice(100.0);
        newCourse.setSalePrice(50.0);
        newCourse.setCourseThumbnail("test_thumb.jpg");
        newCourse.setCreatedAt(new Date());
        newCourse.setUpdatedAt(new Date());
        newCourse.setUserID(1);
        newCourse.setFeatured(false);
        newCourse.setSubjectID(1);
        newCourse.setLessonID(null); // Có thể là null

        boolean inserted = dao.insertCourse(newCourse);
        System.out.println(inserted ? "Insert thành công!" : "Insert thất bại.");
    }
}
