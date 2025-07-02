package dal;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Question;
import model.Quiz;

public class QuizDAO {

    private Connection conn;

    public QuizDAO(Connection conn) {
        this.conn = conn;
    }

    // ------------------- QUIZ ----------------------

    // 1. Get Quiz by ID
    public Quiz getQuizById(int quizId) throws SQLException {
        String sql = "SELECT * FROM Quiz WHERE TestID = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, quizId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
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
        }
        return null;
    }

    // 2. Create Quiz
    public void createQuiz(Quiz q) throws SQLException {
        String sql = "INSERT INTO Quiz (CourseID, Title, Description, Difficulty, Duration, IsActive) VALUES (?,?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, q.getCourseID());
            ps.setString(2, q.getTitle());
            ps.setString(3, q.getDescription());
            ps.setString(4, q.getDifficulty());
            ps.setInt(5, q.getDuration());
            ps.setBoolean(6, q.isActive());
            ps.executeUpdate();
        }
    }

    // 3. Update Quiz
    public void updateQuiz(Quiz q) throws SQLException {
        String sql = "UPDATE Quiz SET Title=?, Description=?, Difficulty=?, Duration=?, IsActive=? WHERE TestID=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, q.getTitle());
            ps.setString(2, q.getDescription());
            ps.setString(3, q.getDifficulty());
            ps.setInt(4, q.getDuration());
            ps.setBoolean(5, q.isActive());
            ps.setInt(6, q.getTestID());
            ps.executeUpdate();
        }
    }

    // 4. Delete Quiz
    public void deleteQuiz(int quizId) throws SQLException {
        // Xoá tất cả câu hỏi thuộc quiz trước
        String deleteQuestions = "DELETE FROM Question WHERE TestID=?";
        try (PreparedStatement ps = conn.prepareStatement(deleteQuestions)) {
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

    // ------------------- QUESTION ----------------------

    // 5. Get Questions by QuizID
    public List<Question> getQuestionsByQuiz(int quizId) throws SQLException {
        List<Question> list = new ArrayList<>();
        String sql = "SELECT * FROM Question WHERE TestID=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, quizId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Question q = new Question(
                    rs.getInt("QuestionID"),
                    rs.getInt("TestID"),
                    rs.getString("Content"),
                    rs.getInt("Points")
                );
                list.add(q);
            }
        }
        return list;
    }
    // Get all questions not already in this quiz
public List<Question> getAvailableQuestionsForQuiz(int quizId) throws SQLException {
    List<Question> list = new ArrayList<>();
    String sql = "SELECT * FROM Question WHERE TestID != ?";
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, quizId);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Question q = new Question(
                rs.getInt("QuestionID"),
                rs.getInt("TestID"),
                rs.getString("Content"),
                rs.getInt("Points")
            );
            list.add(q);
        }
    }
    return list;
}


    // 6. Add Question
    public void addQuestion(Question q) throws SQLException {
        String sql = "INSERT INTO Question (TestID, Content, Points) VALUES (?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, q.getTestID());
            ps.setString(2, q.getContent());
            ps.setInt(3, q.getPoints());
            ps.executeUpdate();
        }
    }

    // 7. Update Question
    public void updateQuestion(Question q) throws SQLException {
        String sql = "UPDATE Question SET Content=?, Points=? WHERE QuestionID=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, q.getContent());
            ps.setInt(2, q.getPoints());
            ps.setInt(3, q.getQuestionID());
            ps.executeUpdate();
        }
    }

    // 8. Delete Question
    public void deleteQuestion(int questionId) throws SQLException {
        String sql = "DELETE FROM Question WHERE QuestionID=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, questionId);
            ps.executeUpdate();
        }
    }
    public List<Quiz> getAllQuizzes() throws SQLException {
    List<Quiz> list = new ArrayList<>();
    String sql = "SELECT * FROM Quiz";
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Quiz q = new Quiz(
                rs.getInt("TestID"),
                rs.getInt("CourseID"),
                rs.getString("Title"),
                rs.getString("Description"),
                rs.getString("Difficulty"),
                rs.getInt("Duration"),
                rs.getBoolean("IsActive")
            );
            list.add(q);
        }
    }
    return list;
}

        // =================== MAIN FOR DEBUG ====================
    public static void main(String[] args) {
        try (Connection conn = new dal.DBContext().getConnection()) {
            QuizDAO dao = new QuizDAO(conn);

            System.out.println("=== Tạo Quiz mới ===");
            Quiz quiz = new Quiz(0, 1, "Test Quiz", "This is a test quiz.", "Easy", 30, true);
            dao.createQuiz(quiz);
            System.out.println("Đã tạo quiz.");

            // Lấy quiz mới nhất (lấy MAX TestID)
            String sql = "SELECT MAX(TestID) FROM Quiz";
            int testID = 0;
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    testID = rs.getInt(1);
                }
            }

            System.out.println("=== Lấy Quiz theo ID ===");
            Quiz createdQuiz = dao.getQuizById(testID);
            System.out.println(createdQuiz);

            System.out.println("=== Cập nhật Quiz ===");
            createdQuiz.setTitle("Updated Title");
            createdQuiz.setDescription("Updated Description");
            createdQuiz.setDifficulty("Medium");
            createdQuiz.setDuration(45);
            createdQuiz.setActive(false);
            dao.updateQuiz(createdQuiz);
            System.out.println("Quiz sau cập nhật: " + dao.getQuizById(testID));

            System.out.println("=== Thêm Question 1 ===");
            Question q1 = new Question(0, testID, "What is Java?", 5);
            dao.addQuestion(q1);

            System.out.println("=== Thêm Question 2 ===");
            Question q2 = new Question(0, testID, "What is SQL?", 10);
            dao.addQuestion(q2);

            System.out.println("=== Lấy danh sách Question ===");
            List<Question> questions = dao.getQuestionsByQuiz(testID);
            for (Question q : questions) {
                System.out.println(q);
            }

            System.out.println("=== Cập nhật Question đầu tiên ===");
            Question firstQ = questions.get(0);
            firstQ.setContent("Updated Question Content");
            firstQ.setPoints(15);
            dao.updateQuestion(firstQ);

            System.out.println("Question sau cập nhật:");
            System.out.println(dao.getQuestionsByQuiz(testID).get(0));

            System.out.println("=== Xoá Question thứ hai ===");
            dao.deleteQuestion(questions.get(1).getQuestionID());

            System.out.println("Danh sách Question sau xoá:");
            List<Question> afterDelete = dao.getQuestionsByQuiz(testID);
            for (Question q : afterDelete) {
                System.out.println(q);
            }

            System.out.println("=== Xoá Quiz và tất cả câu hỏi còn lại ===");
            dao.deleteQuiz(testID);

            System.out.println("Xong!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    

}
