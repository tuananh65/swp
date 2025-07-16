package dal;

import model.Lesson;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LessonDAO extends DBContext {

    private static final Logger LOGGER = Logger.getLogger(LessonDAO.class.getName());

    public Lesson getLessonById(int lessonId) {
        Lesson lesson = null;
        String sql = "SELECT LessonId, SubjectId, Title, Content, VideoUrl, Status, [Order], Type "
                   + "FROM Lesson WHERE LessonId = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, lessonId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    lesson = new Lesson();
                    lesson.setLessonId(rs.getInt("LessonId"));
                    lesson.setSubjectId(rs.getInt("SubjectId"));
                    lesson.setTitle(rs.getString("Title"));
                    lesson.setContent(rs.getString("Content"));
                    lesson.setVideoUrl(rs.getString("VideoUrl"));
                    lesson.setStatus(rs.getString("Status"));
                    // Lấy giá trị Integer cho trường "Order"
                    lesson.setOrder(rs.getObject("Order") != null ? rs.getInt("Order") : null);
                    lesson.setType(rs.getString("Type"));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting lesson by ID: " + lessonId, e);
        }
        return lesson;
    }

    public List<Lesson> getLessonsByCourseId(int subjectId, String lessonType, String status, String searchKeyword, int offset, int limit) {
        List<Lesson> lessons = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT LessonId, SubjectId, Title, Content, VideoUrl, Status, [Order], Type FROM Lesson WHERE SubjectId = ?");

        List<Object> params = new ArrayList<>();
        params.add(subjectId);

        if (lessonType != null && !lessonType.isEmpty()) {
            sql.append(" AND Type = ?");
            params.add(lessonType);
        }
        if (status != null && !status.isEmpty()) {
            sql.append(" AND Status = ?");
            params.add(status);
        }
        if (searchKeyword != null && !searchKeyword.isEmpty()) {
            sql.append(" AND Title LIKE ?");
            params.add("%" + searchKeyword + "%");
        }

        sql.append(" ORDER BY [Order] ASC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");
        params.add(offset);
        params.add(limit);

        LOGGER.log(Level.INFO, "Executing SQL for getLessonsByCourseId: {0}", sql.toString());
        LOGGER.log(Level.INFO, "Parameters: {0}", params);

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Lesson lesson = new Lesson();
                    lesson.setLessonId(rs.getInt("LessonId"));
                    lesson.setSubjectId(rs.getInt("SubjectId"));
                    lesson.setTitle(rs.getString("Title"));
                    lesson.setContent(rs.getString("Content"));
                    lesson.setVideoUrl(rs.getString("VideoUrl"));
                    lesson.setStatus(rs.getString("Status"));
                    lesson.setOrder(rs.getObject("Order") != null ? rs.getInt("Order") : null);
                    lesson.setType(rs.getString("Type"));
                    lessons.add(lesson);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting lessons for subject ID: " + subjectId, e);
        }
        return lessons;
    }

    public int getTotalLessons(int subjectId, String lessonType, String status, String searchKeyword) {
        int total = 0;
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM Lesson WHERE SubjectId = ?");

        List<Object> params = new ArrayList<>();
        params.add(subjectId);

        if (lessonType != null && !lessonType.isEmpty()) {
            sql.append(" AND Type = ?");
            params.add(lessonType);
        }
        if (status != null && !status.isEmpty()) {
            sql.append(" AND Status = ?");
            params.add(status);
        }
        if (searchKeyword != null && !searchKeyword.isEmpty()) {
            sql.append(" AND Title LIKE ?");
            params.add("%" + searchKeyword + "%");
        }

        LOGGER.log(Level.INFO, "Executing SQL for getTotalLessons: {0}", sql.toString());
        LOGGER.log(Level.INFO, "Parameters: {0}", params);

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    total = rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting total lessons for subject ID: " + subjectId, e);
        }
        return total;
    }

    public Set<String> getUniqueLessonTypesByCourseId(int subjectId) {
        Set<String> uniqueTypes = new HashSet<>();
        String sql = "SELECT DISTINCT Type FROM Lesson WHERE SubjectId = ? AND Type IS NOT NULL AND Type <> ''";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, subjectId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    uniqueTypes.add(rs.getString("Type"));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting unique lesson types for subject ID: " + subjectId, e);
        }
        return uniqueTypes;
    }

    public List<String> getAllLessonTypes() {
        List<String> types = new ArrayList<>();
        String sql = "SELECT DISTINCT Type FROM Lesson WHERE Type IS NOT NULL AND Type <> '' ORDER BY Type";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                types.add(rs.getString("Type"));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting all unique lesson types", e);
        }
        // Thêm các loại mặc định nếu muốn đảm bảo có đủ các option trong dropdown
        // Đây là cách bạn có thể xử lý việc có các loại mặc định không phụ thuộc vào DB
        // Sắp xếp lại danh sách để đảm bảo thứ tự nhất quán
        Set<String> defaultTypes = new HashSet<>(Arrays.asList("Subject topic", "Lesson", "Quiz", "Assignment"));
        for (String defaultType : defaultTypes) {
            if (!types.contains(defaultType)) {
                types.add(defaultType);
            }
        }
        types.sort(String::compareTo); // Sắp xếp theo alphabet
        return types;
    }

    public boolean updateLessonStatus(int lessonId, String newStatus) {
        String sql = "UPDATE Lesson SET Status = ? WHERE LessonId = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newStatus);
            ps.setInt(2, lessonId);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating lesson status for ID: " + lessonId + " to " + newStatus, e);
            return false;
        }
    }

    public boolean deleteLesson(int lessonId) {
        String sql = "DELETE FROM Lesson WHERE LessonId = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, lessonId);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting lesson with ID: " + lessonId, e);
            return false;
        }
    }

    // Phương thức thêm mới bài học (ADD)
    public boolean addLesson(Lesson lesson) {
        // --- THÊM KIỂM TRA NULL Ở ĐÂY ---
        if (lesson == null) {
            LOGGER.log(Level.WARNING, "Attempted to add a null lesson object.");
            return false;
        }
        String sql = "INSERT INTO Lesson (SubjectId, Title, Content, VideoUrl, Status, [Order], Type) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, lesson.getSubjectId());
            ps.setString(2, lesson.getTitle());
            ps.setString(3, lesson.getContent());
            ps.setString(4, lesson.getVideoUrl());
            ps.setString(5, lesson.getStatus());
            ps.setObject(6, lesson.getOrder()); // Sử dụng setObject cho Integer để tránh lỗi nếu null
            ps.setString(7, lesson.getType());
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error adding new lesson: " + lesson.getTitle(), e);
            return false;
        }
    }

    // Phương thức cập nhật bài học (UPDATE)
    public boolean updateLesson(Lesson lesson) {
        // --- THÊM KIỂM TRA NULL Ở ĐÂY ---
        if (lesson == null) {
            LOGGER.log(Level.WARNING, "Attempted to update with a null lesson object.");
            return false;
        }
        String sql = "UPDATE Lesson SET SubjectId = ?, Title = ?, Content = ?, VideoUrl = ?, Status = ?, [Order] = ?, Type = ? WHERE LessonId = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, lesson.getSubjectId());
            ps.setString(2, lesson.getTitle());
            ps.setString(3, lesson.getContent());
            ps.setString(4, lesson.getVideoUrl());
            ps.setString(5, lesson.getStatus());
            ps.setObject(6, lesson.getOrder()); // Sử dụng setObject cho Integer để tránh lỗi nếu null
            ps.setString(7, lesson.getType());
            ps.setInt(8, lesson.getLessonId());
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating lesson: " + lesson.getLessonId(), e);
            return false;
        }
    }

    public int getNextOrderForSubject(int subjectId) {
        int maxOrder = 0;
        String sql = "SELECT ISNULL(MAX([order]), 0) FROM [Lesson] WHERE subjectid = ?";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, subjectId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    maxOrder = rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            Logger.getLogger(LessonDAO.class.getName()).log(Level.SEVERE, "Error getting max order for subject " + subjectId, e);
        } catch (Exception e) {
            Logger.getLogger(LessonDAO.class.getName()).log(Level.SEVERE, "DB connection error for max order", e);
        }
        return maxOrder + 1;
    }
}