package dal;

import model.QuizQuestion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuizQuestionDAO {
    private final DBContext dbContext;

    public QuizQuestionDAO() {
        dbContext = new DBContext();
    }

    // Add question to quiz
    public boolean addQuizQuestion(QuizQuestion qq) {
        String sql = "INSERT INTO Quiz_Question (QuizID, QuestionID, Points) VALUES (?, ?, ?)";
        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, qq.getQuizID());
            ps.setInt(2, qq.getQuestionID());
            ps.setInt(3, qq.getPoints());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Delete all questions in quiz
    public boolean deleteAllQuizQuestions(int quizID) {
        String sql = "DELETE FROM Quiz_Question WHERE QuizID = ?";
        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, quizID);
            return ps.executeUpdate() >= 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Get all questions in quiz
    public List<QuizQuestion> getQuestionsByQuizID(int quizID) {
        List<QuizQuestion> list = new ArrayList<>();
        String sql = "SELECT * FROM Quiz_Question WHERE QuizID = ? ORDER BY QuestionID";

        try (Connection conn = dbContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, quizID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                QuizQuestion qq = new QuizQuestion();
                qq.setQuizID(rs.getInt("QuizID"));
                qq.setQuestionID(rs.getInt("QuestionID"));
                qq.setPoints(rs.getInt("Points"));
                list.add(qq);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
public boolean deleteQuizQuestion(int quizID, int questionID) {
    String sql = "DELETE FROM Quiz_Question WHERE QuizID = ? AND QuestionID = ?";
    try (Connection conn = dbContext.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, quizID);
        ps.setInt(2, questionID);
        return ps.executeUpdate() > 0;
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}

    // ⭐ Main method for debug
  public static void main(String[] args) {
    QuizQuestionDAO dao = new QuizQuestionDAO();

    // ✅ Điều chỉnh ID này theo Quiz & Question đang có thật trong DB
    int testQuizID = 2;        // EXISTING QuizID
    int testQuestionID = 4;    // EXISTING QuestionID

    System.out.println("[DEBUG] Make sure QuizID=" + testQuizID + " and QuestionID=" + testQuestionID + " exist in DB!");

    System.out.println("\n=== TEST ADD QUIZ_QUESTION ===");
    QuizQuestion qq = new QuizQuestion();
    qq.setQuizID(testQuizID);
    qq.setQuestionID(testQuestionID);
    qq.setPoints(5);
    boolean added = dao.addQuizQuestion(qq);
    System.out.println("Add result: " + added);

    System.out.println("\n=== TEST GET BY QUIZID ===");
    List<QuizQuestion> list = dao.getQuestionsByQuizID(testQuizID);
    for (QuizQuestion q : list) {
        System.out.println("QuizID: " + q.getQuizID() + ", QuestionID: " + q.getQuestionID() + ", Points: " + q.getPoints());
    }

    System.out.println("\n=== TEST DELETE ALL ===");
    boolean deleted = dao.deleteAllQuizQuestions(testQuizID);
    System.out.println("Delete all result: " + deleted);

    System.out.println("\n=== VERIFY DELETE ===");
    List<QuizQuestion> verifyList = dao.getQuestionsByQuizID(testQuizID);
    if (verifyList.isEmpty()) {
        System.out.println("✅ All questions successfully deleted from quiz!");
    } else {
        System.out.println("❌ Still found questions in quiz!");
    }
}


}
