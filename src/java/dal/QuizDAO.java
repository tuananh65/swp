package dal;

import model.Quiz;
import java.sql.*;
import java.util.*;

public class QuizDAO {
    private DBContext dbContext;

    public QuizDAO() {
        dbContext = new DBContext();
    }

    public List<Quiz> getQuizzesByPage(Integer courseID, String keyword, String status, String difficulty, String quizType, int offset, int limit) {
    List<Quiz> quizzes = new ArrayList<>();
    StringBuilder sql = new StringBuilder(
        "SELECT q.*, c.CourseName " +
        "FROM Quiz q JOIN Course c ON q.CourseID = c.CourseID " +
        "WHERE q.Title LIKE ?"
    );

    if (courseID != null) sql.append(" AND q.CourseID = ?");
    if (status != null && !status.isEmpty()) sql.append(" AND q.IsActive = ?");
    if (difficulty != null && !difficulty.isEmpty()) sql.append(" AND q.Difficulty = ?");
    if (quizType != null && !quizType.isEmpty()) sql.append(" AND q.QuizType = ?");

    sql.append(" ORDER BY q.CreatedAt DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");

    try (Connection conn = dbContext.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql.toString())) {

        int paramIndex = 1;
        ps.setString(paramIndex++, "%" + keyword + "%");
        if (courseID != null) ps.setInt(paramIndex++, courseID);
        if (status != null && !status.isEmpty()) ps.setBoolean(paramIndex++, status.equalsIgnoreCase("Active"));
        if (difficulty != null && !difficulty.isEmpty()) ps.setString(paramIndex++, difficulty);
        if (quizType != null && !quizType.isEmpty()) ps.setString(paramIndex++, quizType);
        ps.setInt(paramIndex++, offset);
        ps.setInt(paramIndex, limit);

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            quizzes.add(mapQuiz(rs));
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return quizzes;
}

public int getTotalQuizzes(Integer courseID, String keyword, String status, String difficulty, String quizType) {
    int total = 0;
    StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM Quiz WHERE Title LIKE ?");

    if (courseID != null) sql.append(" AND CourseID = ?");
    if (status != null && !status.isEmpty()) sql.append(" AND IsActive = ?");
    if (difficulty != null && !difficulty.isEmpty()) sql.append(" AND Difficulty = ?");
    if (quizType != null && !quizType.isEmpty()) sql.append(" AND QuizType = ?");

    try (Connection conn = dbContext.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql.toString())) {

        int paramIndex = 1;
        ps.setString(paramIndex++, "%" + keyword + "%");
        if (courseID != null) ps.setInt(paramIndex++, courseID);
        if (status != null && !status.isEmpty()) ps.setBoolean(paramIndex++, status.equalsIgnoreCase("Active"));
        if (difficulty != null && !difficulty.isEmpty()) ps.setString(paramIndex++, difficulty);
        if (quizType != null && !quizType.isEmpty()) ps.setString(paramIndex++, quizType);

        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            total = rs.getInt(1);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return total;
}

// mapQuiz: cập nhật lại, không random nữa
private Quiz mapQuiz(ResultSet rs) throws SQLException {
    Quiz q = new Quiz();
    q.setQuizID(rs.getInt("QuizID"));
    q.setCourseID(rs.getInt("CourseID"));
    q.setTitle(rs.getString("Title"));
    q.setDescription(rs.getString("Description"));
    q.setDuration(rs.getInt("Duration"));
    q.setDifficulty(rs.getString("Difficulty"));
    q.setActive(rs.getBoolean("IsActive"));
    q.setCreatedAt(rs.getTimestamp("CreatedAt"));
    q.setUpdatedAt(rs.getTimestamp("UpdatedAt"));
    q.setCourseName(rs.getString("CourseName"));
    q.setQuizType(rs.getString("QuizType")); // lấy từ DB
    return q;
}



    /** ⭐ 3️⃣ Get single quiz by ID (JOIN for CourseName) */
    public Quiz getQuizByID(int quizID) {
        String sql = 
            "SELECT q.*, c.CourseName " +
            "FROM Quiz q JOIN Course c ON q.CourseID = c.CourseID " +
            "WHERE q.QuizID = ?";
        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, quizID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapQuiz(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /** ⭐ 4️⃣ Add new quiz */
    /** ⭐ 4️⃣ Add new quiz */
public boolean addQuiz(Quiz quiz) {
    String sql = "INSERT INTO Quiz (CourseID, Title, Description, Duration, Difficulty, IsActive, QuizType) " +
                 "VALUES (?, ?, ?, ?, ?, ?, ?)";
    try (Connection conn = dbContext.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setInt(1, quiz.getCourseID());
        ps.setString(2, quiz.getTitle());
        ps.setString(3, quiz.getDescription());
        ps.setInt(4, quiz.getDuration());
        ps.setString(5, quiz.getDifficulty());
        ps.setBoolean(6, quiz.isActive());
        ps.setString(7, quiz.getQuizType()); // ⭐ NEW

        return ps.executeUpdate() > 0;
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}


    /** ⭐ 5️⃣ Update quiz */
    /** ⭐ 5️⃣ Update quiz */
public boolean updateQuiz(Quiz quiz) {
    String sql = "UPDATE Quiz SET CourseID = ?, Title = ?, Description = ?, Duration = ?, Difficulty = ?, IsActive = ?, QuizType = ? WHERE QuizID = ?";
    try (Connection conn = dbContext.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setInt(1, quiz.getCourseID());
        ps.setString(2, quiz.getTitle());
        ps.setString(3, quiz.getDescription());
        ps.setInt(4, quiz.getDuration());
        ps.setString(5, quiz.getDifficulty());
        ps.setBoolean(6, quiz.isActive());
        ps.setString(7, quiz.getQuizType()); // ⭐ NEW
        ps.setInt(8, quiz.getQuizID());

        return ps.executeUpdate() > 0;
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}


    /** ⭐ 6️⃣ Delete quiz (with child relation) */
    public boolean deleteQuiz(int quizID) {
        String sqlDeleteQuizQuestion = "DELETE FROM Quiz_Question WHERE QuizID = ?";
        String sqlDeleteQuiz = "DELETE FROM Quiz WHERE QuizID = ?";
        boolean result = false;

        try (Connection conn = dbContext.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement psQuizQuestion = conn.prepareStatement(sqlDeleteQuizQuestion);
                 PreparedStatement psQuiz = conn.prepareStatement(sqlDeleteQuiz)) {

                psQuizQuestion.setInt(1, quizID);
                psQuizQuestion.executeUpdate();

                psQuiz.setInt(1, quizID);
                int rowsAffected = psQuiz.executeUpdate();

                conn.commit();
                result = rowsAffected > 0;
            } catch (SQLException e) {
                e.printStackTrace();
                conn.rollback();
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }



 
}
