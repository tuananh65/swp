package dal;

import model.Question;
import java.sql.*;
import java.util.*;
import java.sql.Timestamp;  // Nếu bạn cần sử dụng Timestamp cho trường CreatedAt hoặc UpdatedAt
import model.Answer;


public class QuestionDAO {
    private DBContext dbContext;

    public QuestionDAO() {
        dbContext = new DBContext();
    }

    // Lấy danh sách câu hỏi theo phân trang và tìm kiếm
   public List<Question> getQuestionsByPage(Integer courseID, int offset, int limit, String keyword) {
    List<Question> questions = new ArrayList<>();
    StringBuilder sql = new StringBuilder("SELECT * FROM Question WHERE Content LIKE ?");

    if (courseID != null) {
        sql.append(" AND CourseID = ?");
    }

    sql.append(" ORDER BY CreatedAt DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");

    try (Connection conn = dbContext.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql.toString())) {

        int paramIndex = 1;
        ps.setString(paramIndex++, "%" + keyword + "%");

        if (courseID != null) {
            ps.setInt(paramIndex++, courseID);
        }

        ps.setInt(paramIndex++, offset);
        ps.setInt(paramIndex, limit);

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Question question = new Question();
            question.setQuestionID(rs.getInt("QuestionID"));
            question.setCourseID(rs.getInt("CourseID"));
            question.setContent(rs.getString("Content"));
            question.setPoints(rs.getInt("Points"));
            question.setQuestionType(rs.getString("QuestionType"));
            question.setCreatedAt(rs.getDate("CreatedAt"));
            question.setUpdatedAt(rs.getDate("UpdatedAt"));
            questions.add(question);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return questions;
}






   public int getTotalQuestions(Integer courseID, String keyword) {
    StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM Question WHERE Content LIKE ?");
    int totalQuestions = 0;

    if (courseID != null) {
        sql.append(" AND CourseID = ?");
    }

    try (Connection conn = dbContext.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql.toString())) {

        int paramIndex = 1;
        ps.setString(paramIndex++, "%" + keyword + "%");

        if (courseID != null) {
            ps.setInt(paramIndex, courseID);
        }

        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            totalQuestions = rs.getInt(1);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return totalQuestions;
}

    



    // Lấy câu hỏi theo ID
    public Question getQuestionByID(int questionID) {
        String sql = "SELECT * FROM Question WHERE QuestionID = ?";
        Question question = null;

        try (Connection conn = dbContext.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, questionID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                question = new Question();
                question.setQuestionID(rs.getInt("QuestionID"));
                question.setCourseID(rs.getInt("CourseID"));
                question.setContent(rs.getString("Content"));
                question.setPoints(rs.getInt("Points"));
                question.setQuestionType(rs.getString("QuestionType"));
                question.setCreatedAt(rs.getDate("CreatedAt"));
                question.setUpdatedAt(rs.getDate("UpdatedAt"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return question;
    }

    // Thêm câu hỏi mới
    public boolean addQuestion(Question question) {
        String sql = "INSERT INTO Question (CourseID, Content, Points, QuestionType) VALUES (?, ?, ?, ?)";
        boolean result = false;

        try (Connection conn = dbContext.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, question.getCourseID());
            ps.setString(2, question.getContent());
            ps.setInt(3, question.getPoints());
            ps.setString(4, question.getQuestionType());
            int rowsAffected = ps.executeUpdate();
            result = rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    // Cập nhật câu hỏi
    public boolean updateQuestion(Question question) {
        String sql = "UPDATE Question SET Content = ?, Points = ?, QuestionType = ?, UpdatedAt = ? WHERE QuestionID = ?";
        boolean result = false;

        try (Connection conn = dbContext.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, question.getContent());
            ps.setInt(2, question.getPoints());
            ps.setString(3, question.getQuestionType());
            ps.setTimestamp(4, new Timestamp(System.currentTimeMillis()));  // Sử dụng Timestamp thay vì java.util.Date
            ps.setInt(5, question.getQuestionID());
            int rowsAffected = ps.executeUpdate();
            result = rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    // Xóa câu hỏi
public boolean deleteQuestion(int questionID) {
    String sqlDeleteAnswer = "DELETE FROM Answer WHERE QuestionID = ?";
    String sqlDeleteQuestion = "DELETE FROM Question WHERE QuestionID = ?";
    boolean result = false;

    try (Connection conn = dbContext.getConnection();
         PreparedStatement psAnswer = conn.prepareStatement(sqlDeleteAnswer);
         PreparedStatement psQuestion = conn.prepareStatement(sqlDeleteQuestion)) {

        // Xóa hết Answer liên quan trước (dù có hay không cũng ko lỗi)
        psAnswer.setInt(1, questionID);
        psAnswer.executeUpdate();

        // Luôn thử xóa Question
        psQuestion.setInt(1, questionID);
        int rowsAffectedQuestion = psQuestion.executeUpdate();
        result = rowsAffectedQuestion > 0;

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return result;
}


/**
 * Thêm câu hỏi và trả về ID mới (dùng OUTPUT INSERTED)
 */
public int addQuestionAndGetID(Question question) {
    String sql = "INSERT INTO Question (CourseID, Content, Points, QuestionType) OUTPUT INSERTED.QuestionID VALUES (?, ?, ?, ?)";
    try (Connection conn = dbContext.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, question.getCourseID());
        ps.setString(2, question.getContent());
        ps.setInt(3, question.getPoints());
        ps.setString(4, question.getQuestionType());
        
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            int insertedQuestionID = rs.getInt(1);
            System.out.println("Inserted question with ID: " + insertedQuestionID);  // Log ID inserted
            return insertedQuestionID;
        }
    } catch (SQLException e) {
        e.printStackTrace();
        System.out.println("Error inserting question: " + e.getMessage());
    }
    return -1;
}


public Question getQuestionWithAnswers(int questionID) {
    Question question = getQuestionByID(questionID);
    if (question != null) {
        AnswerDAO answerDAO = new AnswerDAO();
        List<Answer> answers = answerDAO.getAnswersByQuestionID(questionID);
        question.setAnswers(answers);
    }
    return question;
}

public List<Question> getQuestionsByCourseID(int courseID) {
    List<Question> questions = new ArrayList<>();
    String sql = "SELECT * FROM Question WHERE CourseID = ?";
    try (Connection conn = dbContext.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, courseID);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Question question = new Question();
            question.setQuestionID(rs.getInt("QuestionID"));
            question.setCourseID(rs.getInt("CourseID"));
            question.setContent(rs.getString("Content"));
            question.setPoints(rs.getInt("Points"));
            question.setQuestionType(rs.getString("QuestionType"));
            question.setCreatedAt(rs.getDate("CreatedAt"));
            question.setUpdatedAt(rs.getDate("UpdatedAt"));
            questions.add(question);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return questions;
}
public List<Question> getQuestionsByCourseAndType(int courseID, String questionType) {
    List<Question> questions = new ArrayList<>();
    String sql = "SELECT * FROM Question WHERE CourseID = ? AND QuestionType = ?";
    try (Connection conn = dbContext.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, courseID);
        ps.setString(2, questionType);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Question question = new Question();
            question.setQuestionID(rs.getInt("QuestionID"));
            question.setCourseID(rs.getInt("CourseID"));
            question.setContent(rs.getString("Content"));
            question.setPoints(rs.getInt("Points"));
            question.setQuestionType(rs.getString("QuestionType"));
            question.setCreatedAt(rs.getDate("CreatedAt"));
            question.setUpdatedAt(rs.getDate("UpdatedAt"));
            questions.add(question);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return questions;
}



    public static void main(String[] args) {
        // Tạo đối tượng QuestionDAO để truy cập cơ sở dữ liệu
        QuestionDAO questionDAO = new QuestionDAO();

        // Kiểm tra thêm câu hỏi mới
        System.out.println("=== TEST ADD QUESTION ===");
        Question newQuestion = new Question();
        newQuestion.setCourseID(1); // Chọn khóa học với ID 1
        newQuestion.setContent("What is Java?");
        newQuestion.setPoints(5);
        newQuestion.setQuestionType("Multiple Choice");
        boolean addResult = questionDAO.addQuestion(newQuestion);
        System.out.println("Add question success: " + addResult);

        // Kiểm tra lấy câu hỏi theo ID
        System.out.println("\n=== TEST GET QUESTION BY ID ===");
        Question questionByID = questionDAO.getQuestionByID(1);  // Lấy câu hỏi có ID = 1
        if (questionByID != null) {
            System.out.println("QuestionID: " + questionByID.getQuestionID());
            System.out.println("Content: " + questionByID.getContent());
            System.out.println("Points: " + questionByID.getPoints());
            System.out.println("QuestionType: " + questionByID.getQuestionType());
        } else {
            System.out.println("No question found with the given ID.");
        }

        // Kiểm tra cập nhật câu hỏi
        System.out.println("\n=== TEST UPDATE QUESTION ===");
        Question updatedQuestion = new Question();
        updatedQuestion.setQuestionID(1); // Chỉnh sửa câu hỏi có ID = 1
        updatedQuestion.setContent("What is the JVM?");
        updatedQuestion.setPoints(10);
        updatedQuestion.setQuestionType("Multiple Choice");
        boolean updateResult = questionDAO.updateQuestion(updatedQuestion);
        System.out.println("Update question success: " + updateResult);

        // Kiểm tra lấy danh sách câu hỏi theo khóa học (dùng phân trang)
        System.out.println("\n=== TEST GET QUESTIONS BY PAGE ===");
        List<Question> questions = questionDAO.getQuestionsByPage(1, 0, 10, "Java");
        for (Question q : questions) {
            System.out.println("QuestionID: " + q.getQuestionID() + ", Content: " + q.getContent());
        }

        // Kiểm tra xóa câu hỏi
        System.out.println("\n=== TEST DELETE QUESTION ===");
        boolean deleteResult = questionDAO.deleteQuestion(1);  // Xóa câu hỏi có ID = 1
        System.out.println("Delete question success: " + deleteResult);

        // Kiểm tra tổng số câu hỏi (dùng cho phân trang)
        System.out.println("\n=== TEST GET TOTAL QUESTIONS ===");
        int totalQuestions = questionDAO.getTotalQuestions(1, "Java");
        System.out.println("Total questions in course 1: " + totalQuestions);
    }
}
