package dal;

import model.Course;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class CourseDAO extends DBContext {

    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    // Lấy tất cả khóa học
    public List<Course> getAllCourses() {
        List<Course> list = new ArrayList<>();
        String sql = "SELECT * FROM Course";

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
            e.printStackTrace();
        } 
        return list;
    }

    // Lấy khóa học theo ID
    public Course getCourseById(int courseId) {
        String sql = "SELECT * FROM Course WHERE CourseID = ?";

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
            e.printStackTrace();
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
            ps.setTimestamp(8, course.getCreatedAt() != null ? new Timestamp(course.getCreatedAt().getTime()) : null);
            ps.setTimestamp(9, course.getUpdatedAt() != null ? new Timestamp(course.getUpdatedAt().getTime()) : null);
            ps.setInt(10, course.getUserID());
            ps.setBoolean(11, course.isFeatured());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi trong insertCourse: " + e.getMessage());
            e.printStackTrace();
            return false;
        } 
    }

    // Lấy các khóa học nổi bật
    public List<Course> getFeaturedCourses() {
        List<Course> list = new ArrayList<>();
        String sql = "SELECT * FROM Course WHERE Featured = 1";

        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                list.add(extractCourse(rs));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi trong getFeaturedCourses: " + e.getMessage());
            e.printStackTrace();
        } 
        return list;
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
   

    // Main method for testing
    public static void main(String[] args) {
        CourseDAO dao = new CourseDAO();
        List<Course> list = dao.getAllCourses();

        for (Course c : list) {
            System.out.println("Course Name: " + c.toString());
        }
    }
}