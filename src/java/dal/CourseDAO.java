package dal;

import model.Course;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseDAO {

    // Lấy tất cả khóa học
    public List<Course> getAllCourses() {
        List<Course> list = new ArrayList<>();
        String sql = "SELECT * FROM Course";

        try (Connection conn = DBContext.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Course course = extractCourse(rs);
                list.add(course);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi trong getAllCourses: " + e.getMessage());
            e.printStackTrace();
        }

        return list;
    }

    // Lấy khóa học theo ID
    public Course getCourseById(int courseId) {
        String sql = "SELECT * FROM Course WHERE CourseID = ?";
        try (Connection conn = DBContext.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, courseId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extractCourse(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Lỗi trong getCourseById: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // Thêm khóa học mới
    public boolean insertCourse(Course course) {
        String sql = "INSERT INTO Course (CourseName, TagLine, BriefInfo, Description, OriginalPrice, SalePrice, CourseThumbnail, CreatedAt, UpdatedAt, UserID, Featured) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBContext.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, course.getCourseName());
            ps.setString(2, course.getTagLine());
            ps.setString(3, course.getBriefInfo());
            ps.setString(4, course.getDescription());
            ps.setDouble(5, course.getOriginalPrice());
            ps.setDouble(6, course.getSalePrice());
            ps.setString(7, course.getCourseThumbnail());
            if (course.getCreatedAt() != null) {
                ps.setTimestamp(8, new Timestamp(course.getCreatedAt().getTime()));
            } else {
                ps.setTimestamp(8, null);
            }

            if (course.getUpdatedAt() != null) {
                ps.setTimestamp(8, new Timestamp(course.getUpdatedAt().getTime()));
            } else {
                ps.setTimestamp(8, null);
            }

            ps.setInt(10, course.getUserID());
            ps.setBoolean(11, course.isFeatured());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Hàm hỗ trợ: Tạo đối tượng Course từ ResultSet
    private Course extractCourse(ResultSet rs) throws SQLException {
        Course c = new Course();
        c.setCourseID(rs.getInt("CourseID"));
        c.setCourseName(rs.getString("CourseName"));
        c.setTagLine(rs.getString("TagLine"));
        c.setBriefInfo(rs.getString("BriefInfo"));
        c.setDescription(rs.getString("Description"));
        c.setOriginalPrice(rs.getDouble("OriginalPrice"));
        c.setSalePrice(rs.getDouble("SalePrice"));
        c.setCourseThumbnail(rs.getString("CourseThumbnail"));
        c.setCreatedAt(rs.getTimestamp("CreatedAt"));
        c.setUpdatedAt(rs.getTimestamp("UpdatedAt"));
        c.setUserID(rs.getInt("UserID"));
        c.setFeatured(rs.getBoolean("Featured"));
        return c;
    }
    
    // Hàm lấy featured course
    public List<Course> getFeaturedCourses() {
    List<Course> list = new ArrayList<>();
    String sql = "SELECT * FROM Course WHERE Featured = 1";

    try (Connection conn = DBContext.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
            list.add(extractCourse(rs));
        }
    } catch (SQLException e) {
        System.err.println("Lỗi trong getFeaturedCourses: " + e.getMessage());
        e.printStackTrace();
    }

    return list;
}

     public static void main(String[] args) {
        CourseDAO dao = new CourseDAO();
        List<Course> list = dao.getAllCourses();

        for (Course c : list) {
            System.out.println("Course Name: " + c.toString());
        }
    }
     
}
