package dal;

import model.Answer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Question;

public class AnswerDAO {

    private DBContext dbContext;

    public AnswerDAO() {
        dbContext = new DBContext();
    }

    /**
     * Thêm 1 đáp án cho câu hỏi
     */
    public boolean addAnswer(int questionID, String content, boolean isCorrect) {
        String sql = "INSERT INTO Answer (QuestionID, Content, IsCorrect) VALUES (?, ?, ?)";
        try (Connection conn = dbContext.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, questionID);
            ps.setString(2, content);
            ps.setBoolean(3, isCorrect);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Answer> getAnswersByQuestionID(int questionID) {
        List<Answer> answers = new ArrayList<>();
        String sql = "SELECT * FROM Answer WHERE QuestionID = ?";

        try (Connection conn = dbContext.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, questionID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Answer answer = new Answer();
                answer.setAnswerID(rs.getInt("AnswerID"));
                answer.setQuestionID(rs.getInt("QuestionID"));
                answer.setContent(rs.getString("Content"));
                answer.setCorrect(rs.getBoolean("isCorrect"));
                answers.add(answer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return answers;
    }

    public boolean addAnswer(Answer answer) {
        return addAnswer(answer.getQuestionID(), answer.getContent(), answer.isCorrect());
    }

    public boolean addAnswersBatch(List<Answer> answers) {
    String sql = "INSERT INTO Answer (QuestionID, Content, IsCorrect) VALUES (?, ?, ?)";
    try (Connection conn = dbContext.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
        for (Answer answer : answers) {
            ps.setInt(1, answer.getQuestionID());
            ps.setString(2, answer.getContent());
            ps.setBoolean(3, answer.isCorrect());
            ps.addBatch();
        }
        int[] results = ps.executeBatch();
        System.out.println("Inserted " + results.length + " answers."); // Log số câu trả lời được insert
        return true;
    } catch (SQLException e) {
        e.printStackTrace();
        System.out.println("Error inserting answers: " + e.getMessage());
    }
    return false;
}



    public boolean deleteAnswersByQuestionID(int questionID) {
        String sql = "DELETE FROM Answer WHERE QuestionID = ?";
        try (Connection conn = dbContext.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, questionID);
            return ps.executeUpdate() >= 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void main(String[] args) {
        QuestionDAO questionDAO = new QuestionDAO();
        Question newQuestion = new Question();
        newQuestion.setCourseID(1);
        newQuestion.setContent("Test question for AnswerDAO");
        newQuestion.setPoints(5);
        newQuestion.setQuestionType("Multiple Choice");

        int newQID = questionDAO.addQuestionAndGetID(newQuestion);
        System.out.println("Created question ID: " + newQID);

        int testQuestionID = newQID;

        AnswerDAO answerDAO = new AnswerDAO();

        System.out.println("=== Test addAnswer ===");
        boolean added = answerDAO.addAnswer(testQuestionID, "This is a test answer", true);
        System.out.println("Add single answer result: " + added);

        System.out.println("\n=== Test addAnswersBatch ===");
        List<Answer> batch = new ArrayList<>();
        batch.add(new Answer(0, testQuestionID, "Batch Answer 1", true));
        batch.add(new Answer(0, testQuestionID, "Batch Answer 2", false));
        batch.add(new Answer(0, testQuestionID, "Batch Answer 3", false));
        boolean batchResult = answerDAO.addAnswersBatch(batch);
        System.out.println("Add batch result: " + batchResult);

        System.out.println("\n=== Test getAnswersByQuestionID ===");
        List<Answer> answers = answerDAO.getAnswersByQuestionID(testQuestionID);
        for (Answer a : answers) {
            System.out.println("AnswerID: " + a.getAnswerID()
                    + ", Content: " + a.getContent()
                    + ", isCorrect: " + a.isCorrect());
        }

        System.out.println("\n=== Test deleteAnswersByQuestionID ===");
        boolean deleted = answerDAO.deleteAnswersByQuestionID(testQuestionID);
        System.out.println("Delete result: " + deleted);

        System.out.println("\n=== Verify delete by fetching again ===");
        answers = answerDAO.getAnswersByQuestionID(testQuestionID);
        if (answers.isEmpty()) {
            System.out.println("✅ All answers deleted successfully!");
        } else {
            System.out.println("❌ Still has answers: " + answers.size());
        }
    }

}
