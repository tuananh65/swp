package dal;

import model.Quiz;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuizDAO {
    private DBContext dbContext;

    public QuizDAO() {
        dbContext = new DBContext();
    }

    // 1. Get list of quizzes with pagination + search + filter
    public List<Quiz> getQuizzesByPage(Integer courseID, String keyword, String status, int offset, int limit) {
        List<Quiz> quizzes = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM Quiz WHERE Title LIKE ?");

        if (courseID != null) sql.append(" AND CourseID = ?");
        if (status != null && !status.isEmpty()) sql.append(" AND IsActive = ?");

        sql.append(" ORDER BY CreatedAt DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");

        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int paramIndex = 1;
            ps.setString(paramIndex++, "%" + keyword + "%");
            if (courseID != null) ps.setInt(paramIndex++, courseID);
            if (status != null && !status.isEmpty()) ps.setBoolean(paramIndex++, status.equalsIgnoreCase("Active"));
            ps.setInt(paramIndex++, offset);
            ps.setInt(paramIndex, limit);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Quiz q = mapQuiz(rs);
                quizzes.add(q);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return quizzes;
    }

    // 2. Count total quizzes for pagination
    public int getTotalQuizzes(Integer courseID, String keyword, String status) {
        int total = 0;
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM Quiz WHERE Title LIKE ?");

        if (courseID != null) sql.append(" AND CourseID = ?");
        if (status != null && !status.isEmpty()) sql.append(" AND IsActive = ?");

        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int paramIndex = 1;
            ps.setString(paramIndex++, "%" + keyword + "%");
            if (courseID != null) ps.setInt(paramIndex++, courseID);
            if (status != null && !status.isEmpty()) ps.setBoolean(paramIndex++, status.equalsIgnoreCase("Active"));

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                total = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }

    // 3. Get quiz by ID
    public Quiz getQuizByID(int quizID) {
        String sql = "SELECT * FROM Quiz WHERE QuizID = ?";
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

    // 4. Add quiz
    public boolean addQuiz(Quiz quiz) {
        String sql = "INSERT INTO Quiz (CourseID, Title, Description, Duration, Difficulty, IsActive) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, quiz.getCourseID());
            ps.setString(2, quiz.getTitle());
            ps.setString(3, quiz.getDescription());
            ps.setInt(4, quiz.getDuration());
            ps.setString(5, quiz.getDifficulty());
            ps.setBoolean(6, quiz.isActive());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 5. Update quiz
    public boolean updateQuiz(Quiz quiz) {
        String sql = "UPDATE Quiz SET CourseID = ?, Title = ?, Description = ?, Duration = ?, Difficulty = ?, IsActive = ? WHERE QuizID = ?";
        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, quiz.getCourseID());
            ps.setString(2, quiz.getTitle());
            ps.setString(3, quiz.getDescription());
            ps.setInt(4, quiz.getDuration());
            ps.setString(5, quiz.getDifficulty());
            ps.setBoolean(6, quiz.isActive());
            ps.setInt(7, quiz.getQuizID());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 6. Delete quiz
public boolean deleteQuiz(int quizID) {
    String sqlDeleteQuizQuestion = "DELETE FROM Quiz_Question WHERE QuizID = ?";
    String sqlDeleteQuiz = "DELETE FROM Quiz WHERE QuizID = ?";

    boolean result = false;

    try (Connection conn = dbContext.getConnection()) {
        conn.setAutoCommit(false);

        try (
            PreparedStatement psQuizQuestion = conn.prepareStatement(sqlDeleteQuizQuestion);
            PreparedStatement psQuiz = conn.prepareStatement(sqlDeleteQuiz)
        ) {
            // 1️⃣ Xoá tất cả câu hỏi gán cho quiz
            psQuizQuestion.setInt(1, quizID);
            psQuizQuestion.executeUpdate();

            // 2️⃣ Xoá quiz
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


    // Helper to map result set to Quiz
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
        return q;
    }

    // ⭐ Main method for debug
    public static void main(String[] args) {
        QuizDAO dao = new QuizDAO();

        System.out.println("=== TEST ADD QUIZ ===");
        Quiz newQuiz = new Quiz();
        newQuiz.setCourseID(1);
        newQuiz.setTitle("Sample Quiz");
        newQuiz.setDescription("Description here");
        newQuiz.setDuration(45);
        newQuiz.setDifficulty("Medium");
        newQuiz.setActive(true);
        boolean added = dao.addQuiz(newQuiz);
        System.out.println("Add result: " + added);

        System.out.println("\n=== TEST LIST QUIZ ===");
        List<Quiz> quizzes = dao.getQuizzesByPage(null, "", "", 0, 10);
        for (Quiz q : quizzes) {
            System.out.println("QuizID: " + q.getQuizID() + ", Title: " + q.getTitle());
        }

        System.out.println("\n=== TEST GET QUIZ BY ID ===");
        Quiz quiz = dao.getQuizByID(1);
        if (quiz != null) {
            System.out.println("Found: " + quiz.getTitle());
        }

        System.out.println("\n=== TEST UPDATE QUIZ ===");
        if (quiz != null) {
            quiz.setTitle("Updated Title");
            boolean updated = dao.updateQuiz(quiz);
            System.out.println("Update result: " + updated);
        }

        System.out.println("\n=== TEST DELETE QUIZ ===");
        boolean deleted = dao.deleteQuiz(5);
        System.out.println("Delete result: " + deleted);
    }
}
