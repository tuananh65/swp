package dal;

import model.Lesson;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LessonDAO extends DBContext {

    private static final Logger LOGGER = Logger.getLogger(LessonDAO.class.getName());

    public boolean deleteLesson(int lessonId) {
        String sql = "DELETE FROM Lesson WHERE LessonId = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, lessonId);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0; // Trả về true nếu có ít nhất 1 hàng bị ảnh hưởng (đã xóa)
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error deleting lesson with ID: " + lessonId + ". " + ex.getMessage(), ex);
            return false;
        }
    }
    // Phương thức đã sửa đổi của bạn
    public List<Lesson> getLessonsByCourseId(int subjectId, String lessonGroup, String status, String searchKeyword, int offset, int limit) {
        List<Lesson> lessons = new ArrayList<>();
        String sql = "SELECT LessonId, SubjectId, Title, Content, VideoUrl, SortOrder, Status, [Order], Type "
                + "FROM Lesson WHERE SubjectId = ?";

        List<Object> params = new ArrayList<>();
        params.add(subjectId);

        if (lessonGroup != null && !lessonGroup.isEmpty() && !lessonGroup.equals("All Lesson Groups")) {
            sql += " AND Type = ?";
            params.add(lessonGroup);
        }

        if (status != null && !status.isEmpty() && !status.equals("All Statuses")) {
            sql += " AND Status = ?";
            params.add(status);
        }

        if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
            sql += " AND Title LIKE ?";
            params.add("%" + searchKeyword.trim() + "%");
        }

        sql += " ORDER BY [Order] ASC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        params.add(offset);
        params.add(limit);

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Lesson lesson = new Lesson();
                lesson.setLessonId(rs.getInt("LessonId"));
                lesson.setCourseId(rs.getInt("SubjectId")); // Changed from CourseId to SubjectId based on your database schema
                lesson.setTitle(rs.getString("Title"));
                lesson.setContent(rs.getString("Content"));
                lesson.setVideoUrl(rs.getString("VideoUrl"));

                Integer sortOrder = rs.getInt("SortOrder");
                if (rs.wasNull()) {
                    sortOrder = null;
                }
                lesson.setSortOrder(sortOrder);

                lesson.setStatus(rs.getString("Status"));

                Integer order = rs.getInt("Order");
                if (rs.wasNull()) {
                    order = null;
                }
                lesson.setOrder(order);

                lesson.setType(rs.getString("Type"));
                lessons.add(lesson);
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error in getLessonsByCourseId: " + ex.getMessage(), ex);
        }
        return lessons;
    }

    // 1. Phương thức updateLessonStatus
    public boolean updateLessonStatus(int lessonId, String newStatus) {
        String sql = "UPDATE Lesson SET Status = ? WHERE LessonId = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newStatus);
            ps.setInt(2, lessonId);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error updating lesson status for Lesson ID " + lessonId + ": " + ex.getMessage(), ex);
            return false;
        }
    }

    // 2. Phương thức getTotalLessons
    public int getTotalLessons(int subjectId, String lessonGroup, String status, String searchKeyword) {
        int total = 0;
        String sql = "SELECT COUNT(*) FROM Lesson WHERE SubjectId = ?";
        List<Object> params = new ArrayList<>();
        params.add(subjectId);

        if (lessonGroup != null && !lessonGroup.isEmpty() && !lessonGroup.equals("All Lesson Groups")) {
            sql += " AND Type = ?";
            params.add(lessonGroup);
        }

        if (status != null && !status.isEmpty() && !status.equals("All Statuses")) {
            sql += " AND Status = ?";
            params.add(status);
        }

        if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
            sql += " AND Title LIKE ?";
            params.add("%" + searchKeyword.trim() + "%");
        }

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                total = rs.getInt(1);
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error getting total lessons for Subject ID " + subjectId + ": " + ex.getMessage(), ex);
        }
        return total;
    }

    // 3. Phương thức getUniqueLessonTypesByCourseId
    public Set<String> getUniqueLessonTypesByCourseId(int subjectId) {
        Set<String> uniqueTypes = new HashSet<>();
        String sql = "SELECT DISTINCT Type FROM Lesson WHERE SubjectId = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, subjectId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                uniqueTypes.add(rs.getString("Type"));
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error getting unique lesson types for Subject ID " + subjectId + ": " + ex.getMessage(), ex);
        }
        return uniqueTypes;
    }
}