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
    try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, courseId);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            Course course = new Course();
            course.setCourseID(rs.getInt("CourseID"));
            course.setCourseName(rs.getString("CourseName"));
            course.setTagLine(rs.getString("TagLine"));
            course.setBriefInfo(rs.getString("BriefInfo"));
            course.setDescription(rs.getString("Description"));
            course.setOriginalPrice(rs.getDouble("OriginalPrice"));
            course.setSalePrice(rs.getDouble("SalePrice"));
            course.setCourseThumbnail(rs.getString("CourseThumbnail"));
            course.setCreatedAt(rs.getTimestamp("CreatedAt"));
            course.setUpdatedAt(rs.getTimestamp("UpdatedAt"));
            course.setUserID(rs.getInt("UserID"));
            course.setFeatured(rs.getBoolean("Featured"));
            return course;
        }
    } catch (SQLException e) {
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

        String sql = "SELECT * FROM Course WHERE CourseName LIKE ? "
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
    
    public List<String> getCourseCategories() {
        List<String> categories = new ArrayList<>();
        String sql = "SELECT DISTINCT TagLine FROM Course WHERE TagLine IS NOT NULL";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                categories.add(rs.getString("TagLine"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
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
    
    private void closeResources() {
        try {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            System.err.println("Lỗi khi đóng tài nguyên: " + e.getMessage());
        }
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