package dal;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Course;
import model.Question;
import model.Quiz;

public class QuizDAO {

    private Connection conn;

    public QuizDAO(Connection conn) {
        this.conn = conn;
    }
    /**
 * Tìm kiếm quiz theo tiêu đề (keyword), với phân trang
 *
 * @param keyword   Từ khóa tìm kiếm trong Title (có thể rỗng "")
 * @param pageNumber Trang hiện tại (bắt đầu từ 1)
 * @param pageSize  Số phần tử mỗi trang
 * @return Danh sách quiz theo trang
 */
public List<Quiz> searchQuizzes(String keyword, int pageNumber, int pageSize) throws SQLException {
    List<Quiz> list = new ArrayList<>();
    int offset = (pageNumber - 1) * pageSize;

    String sql = """
        SELECT q.*, c.CourseName
        FROM Quiz q
        JOIN Course c ON q.CourseID = c.CourseID
        WHERE q.Title LIKE ?
        ORDER BY q.TestID DESC
        OFFSET ? ROWS FETCH NEXT ? ROWS ONLY
        """;

    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, "%" + keyword + "%");
        ps.setInt(2, offset);
        ps.setInt(3, pageSize);

        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Quiz quiz = extractQuizFromResultSet(rs);
                quiz.setCourseName(rs.getString("CourseName"));
                list.add(quiz);
            }
        }
    }
    return list;
}
/**
 * Đếm số quiz có tiêu đề chứa keyword
 */
public int countQuizzes(String keyword) throws SQLException {
    String sql = "SELECT COUNT(*) FROM Quiz WHERE Title LIKE ?";
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, "%" + keyword + "%");
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        }
    }
    return 0;
}


    // ---------- UTIL ----------
    private Quiz extractQuizFromResultSet(ResultSet rs) throws SQLException {
        return new Quiz(
            rs.getInt("TestID"),
            rs.getInt("CourseID"),
            rs.getString("Title"),
            rs.getString("Description"),
            rs.getString("Difficulty"),
            rs.getInt("Duration"),
            rs.getBoolean("IsActive")
        );
    }

    private Question extractQuestionFromResultSet(ResultSet rs) throws SQLException {
    return new Question(
        rs.getInt("QuestionID"),
        rs.getInt("CourseID"),
        rs.getString("Content"),
        rs.getInt("Points")
    );
}


    // ---------- QUIZ ----------
    public Quiz getQuizById(int quizId) throws SQLException {
        String sql = "SELECT * FROM Quiz WHERE TestID = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, quizId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extractQuizFromResultSet(rs);
                }
            }
        }
        return null;
    }

    public List<Quiz> getAllQuizzes() throws SQLException {
        List<Quiz> list = new ArrayList<>();
        String sql = "SELECT * FROM Quiz";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(extractQuizFromResultSet(rs));
            }
        }
        return list;
    }

    public int createQuizAndReturnId(Quiz q) throws SQLException {
        String sql = "INSERT INTO Quiz (CourseID, Title, Description, Difficulty, Duration, IsActive) VALUES (?,?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, q.getCourseID());
            ps.setString(2, q.getTitle());
            ps.setString(3, q.getDescription());
            ps.setString(4, q.getDifficulty());
            ps.setInt(5, q.getDuration());
            ps.setBoolean(6, q.isActive());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return 0;
    }

    public void updateQuiz(Quiz q) throws SQLException {
        String sql = "UPDATE Quiz SET CourseID=?, Title=?, Description=?, Difficulty=?, Duration=?, IsActive=? WHERE TestID=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, q.getCourseID());
            ps.setString(2, q.getTitle());
            ps.setString(3, q.getDescription());
            ps.setString(4, q.getDifficulty());
            ps.setInt(5, q.getDuration());
            ps.setBoolean(6, q.isActive());
            ps.setInt(7, q.getTestID());
            ps.executeUpdate();
        }
    }

    public void deleteQuiz(int quizId) throws SQLException {
        // Xoá mapping trước
        String deleteMapping = "DELETE FROM Quiz_Question WHERE TestID=?";
        try (PreparedStatement ps = conn.prepareStatement(deleteMapping)) {
            ps.setInt(1, quizId);
            ps.executeUpdate();
        }

        // Xoá quiz
        String sql = "DELETE FROM Quiz WHERE TestID=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, quizId);
            ps.executeUpdate();
        }
    }

    // ---------- QUESTION ----------
    public List<Question> getBankQuestionsByCourse(int courseId) throws SQLException {
    List<Question> list = new ArrayList<>();
    String sql = "SELECT * FROM Question WHERE CourseID = ?";
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, courseId);
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(extractQuestionFromResultSet(rs));
            }
        }
    }
    return list;
}



    public void addQuestion(Question q) throws SQLException {
    String sql = "INSERT INTO Question (CourseID, Content, Points) VALUES (?,?,?)";
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, q.getCourseID());
        ps.setString(2, q.getContent());
        ps.setInt(3, q.getPoints());
        ps.executeUpdate();
    }
}


    public void updateQuestion(Question q) throws SQLException {
     String sql = "UPDATE Question SET content = ?, points = ? WHERE questionID = ?";
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, q.getContent());
        ps.setInt(2, q.getPoints());
        ps.setInt(3, q.getQuestionID());
        ps.executeUpdate();
    }
}


    public void deleteQuestion(int questionId) throws SQLException {
    String deleteMapping = "DELETE FROM Quiz_Question WHERE QuestionID=?";
    try (PreparedStatement ps = conn.prepareStatement(deleteMapping)) {
        ps.setInt(1, questionId);
        ps.executeUpdate();
    }

    String sql = "DELETE FROM Question WHERE QuestionID=?";
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, questionId);
        ps.executeUpdate();
    }
}


    // ---------- QUIZ_QUESTION Mapping ----------
   public void addQuestionToQuiz(int quizId, int questionId, int points) throws SQLException {
    String sql = "INSERT INTO Quiz_Question (TestID, QuestionID, Points) VALUES (?, ?, ?)";
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, quizId);
        ps.setInt(2, questionId);
        ps.setInt(3, points); // Lưu điểm vào cột Points
        ps.executeUpdate();
    }
}
public void updateQuestionPointsInQuiz(int quizID, int questionID, int points) throws SQLException {
    String sql = "UPDATE Quiz_Question SET Points = ? WHERE TestID = ? AND QuestionID = ?";
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, points);
        ps.setInt(2, quizID);
        ps.setInt(3, questionID);
        ps.executeUpdate();
    }
}


    public void clearQuizQuestions(int quizId) throws SQLException {
    String sql = "DELETE FROM Quiz_Question WHERE TestID=?";
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, quizId);
        ps.executeUpdate();
    }
}


    public List<Question> getQuestionsByQuiz(int quizId) throws SQLException {
    List<Question> list = new ArrayList<>();
    String sql = """
        SELECT q.QuestionID, q.CourseID, q.Content, qq.Points
        FROM Quiz_Question qq
        JOIN Question q ON q.QuestionID = qq.QuestionID
        WHERE qq.TestID = ?
        """;
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, quizId);
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Question(
                    rs.getInt("QuestionID"),
                    rs.getInt("CourseID"),
                    rs.getString("Content"),
                    rs.getInt("Points")
                ));
            }
        }
    }
    return list;
}


    public List<Question> getAvailableQuestionsForQuiz(int quizId, int courseId) throws SQLException {
    List<Question> list = new ArrayList<>();
    String sql = """
        SELECT * FROM Question 
        WHERE CourseID = ? 
          AND QuestionID NOT IN (
              SELECT QuestionID FROM Quiz_Question WHERE TestID = ?
          )
        """;
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, courseId);
        ps.setInt(2, quizId);
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(extractQuestionFromResultSet(rs));
            }
        }
    }
    return list;
}
    public void removeQuestionFromQuiz(int quizId, int questionId) throws SQLException {
    String sql = "DELETE FROM Quiz_Question WHERE TestID = ? AND QuestionID = ?";
    
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, quizId);
        ps.setInt(2, questionId);
        ps.executeUpdate();
    } catch (SQLException e) {
        throw new SQLException("Error removing question from quiz: " + e.getMessage(), e);
    }
}



    // ---------- COURSE ----------
    public List<Course> getAllCourses() throws SQLException {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT * FROM Course";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Course course = new Course(
                    rs.getInt("CourseID"),
                    rs.getString("CourseName"),
                    rs.getString("TagLine"),
                    rs.getString("BriefInfo"),
                    rs.getString("Description"),
                    rs.getDouble("OriginalPrice"),
                    rs.getDouble("SalePrice"),
                    rs.getString("CourseThumbnail"),
                    rs.getDate("CreatedAt"),
                    rs.getDate("UpdatedAt"),
                    rs.getInt("UserID"),
                    rs.getBoolean("Featured")
                );
                courses.add(course);
            }
        }
        return courses;
    }
    public List<Quiz> getAllQuizzesWithCourse() throws SQLException {
    List<Quiz> list = new ArrayList<>();
    String sql = """
        SELECT q.*, c.CourseName 
        FROM Quiz q
        JOIN Course c ON q.CourseID = c.CourseID
        """;
    try (PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
            Quiz q = extractQuizFromResultSet(rs);
            q.setCourseName(rs.getString("CourseName"));
            list.add(q);
        }
    }
    return list;
}

  /*  public static void main(String[] args) {
    try (Connection conn = new DBContext().getConnection()) {
        QuizDAO dao = new QuizDAO(conn);

        System.out.println("===== ALL QUIZZES =====");
        List<Quiz> quizzes = dao.getAllQuizzes();
        for (Quiz q : quizzes) {
            System.out.println(q);
        }

        System.out.println("\n===== ALL BANK QUESTIONS =====");
        List<Question> bankQuestions = dao.getAllBankQuestions();
        for (Question q : bankQuestions) {
            System.out.println(q);
        }

        // Giả sử quizID=1
        int quizId = 1;
        System.out.println("\n===== QUESTIONS IN QUIZ ID = 1 =====");
        List<Question> quizQuestions = dao.getQuestionsByQuiz(quizId);
        for (Question q : quizQuestions) {
            System.out.println(q);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
} */

}
