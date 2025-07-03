package dal;

import model.Answer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO cho bảng Answer
 */
public class AnswerDAO {

    private Connection conn;

    public AnswerDAO(Connection conn) {
        this.conn = conn;
    }

    // ==== Extract Answer from ResultSet ====
    private Answer extractAnswer(ResultSet rs) throws SQLException {
        return new Answer(
                rs.getInt("AnswerID"),
                rs.getInt("QuestionID"),
                rs.getString("Content"),
                rs.getBoolean("IsCorrect")
        );
    }

    // ==== Get all answers for a question ====
    public List<Answer> getAnswersByQuestion(int questionId) throws SQLException {
        List<Answer> list = new ArrayList<>();
        String sql = "SELECT * FROM Answer WHERE QuestionID=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, questionId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(extractAnswer(rs));
                }
            }
        }
        return list;
    }

    // ==== Add Answer ====
    public void addAnswer(Answer a) throws SQLException {
        String sql = "INSERT INTO Answer (QuestionID, Content, IsCorrect) VALUES (?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, a.getQuestionID());
            ps.setString(2, a.getContent());
            ps.setBoolean(3, a.isCorrect());
            ps.executeUpdate();
        }
    }

    // ==== Update Answer ====
    public void updateAnswer(Answer a) throws SQLException {
        String sql = "UPDATE Answer SET Content=?, IsCorrect=? WHERE AnswerID=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, a.getContent());
            ps.setBoolean(2, a.isCorrect());
            ps.setInt(3, a.getAnswerID());
            ps.executeUpdate();
        }
    }

    // ==== Delete Answer ====
    public void deleteAnswer(int answerId) throws SQLException {
        String sql = "DELETE FROM Answer WHERE AnswerID=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, answerId);
            ps.executeUpdate();
        }
    }

    // ==== MAIN TEST ====
    public static void main(String[] args) {
        try (Connection conn = new DBContext().getConnection()) {
            AnswerDAO dao = new AnswerDAO(conn);

            System.out.println("=== ADD ===");
            dao.addAnswer(new Answer(0, 1, "New Answer Example", false));

            System.out.println("=== GET ===");
            List<Answer> answers = dao.getAnswersByQuestion(1);
            for (Answer a : answers) {
                System.out.println(a);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

