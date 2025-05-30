package dal;

import model.Course;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

public class CourseDAO {

    // Lấy course theo ID
    public Course getCourseById(int courseID) {
        String sql = "SELECT * FROM Course WHERE CourseID = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, courseID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extractCourseFromResultSet(rs);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Lấy tất cả course
    public List<Course> getAllCourses() {
        List<Course> list = new ArrayList<>();
        String sql = "SELECT * FROM Course";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(extractCourseFromResultSet(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Thêm course mới
    public void addCourse(Course course) {
        String sql = "INSERT INTO Course (CourseName, Description, Price, CreatedAt, UpdatedAt, UserID) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, course.getCourseName());
            ps.setString(2, course.getDescription());
            ps.setBigDecimal(3, course.getPrice());
            ps.setTimestamp(4, new Timestamp(course.getCreatedAt().getTime()));
            ps.setTimestamp(5, new Timestamp(course.getUpdatedAt().getTime()));
            ps.setInt(6, course.getUserID());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Cập nhật course
    public void updateCourse(Course course) {
        String sql = "UPDATE Course SET CourseName = ?, Description = ?, Price = ?, CreatedAt = ?, UpdatedAt = ?, UserID = ? WHERE CourseID = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, course.getCourseName());
            ps.setString(2, course.getDescription());
            ps.setBigDecimal(3, course.getPrice());
            ps.setTimestamp(4, new Timestamp(course.getCreatedAt().getTime()));
            ps.setTimestamp(5, new Timestamp(course.getUpdatedAt().getTime()));
            ps.setInt(6, course.getUserID());
            ps.setInt(7, course.getCourseID());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Xóa course theo ID
    public void deleteCourse(int courseID) {
        String sql = "DELETE FROM Course WHERE CourseID = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, courseID);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Helper: chuyển ResultSet thành Course
    private Course extractCourseFromResultSet(ResultSet rs) throws SQLException {
        Course course = new Course();
        course.setCourseID(rs.getInt("CourseID"));
        course.setCourseName(rs.getString("CourseName"));
        course.setDescription(rs.getString("Description"));
        course.setPrice(rs.getBigDecimal("Price"));
        course.setCreatedAt(rs.getTimestamp("CreatedAt"));
        course.setUpdatedAt(rs.getTimestamp("UpdatedAt"));
        course.setUserID(rs.getInt("UserID"));
        return course;
    }
    public List<Course> searchCourses(String keyword) {
    List<Course> list = new ArrayList<>();
    String sql = "SELECT * FROM Course WHERE CourseName LIKE ?";
    try (Connection conn = DBContext.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, "%" + keyword + "%");
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(extractCourseFromResultSet(rs));
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return list;
}

}
