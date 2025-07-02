package controller.instructor;

import dal.DBContext;
import dal.QuizDAO;
import model.Quiz;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;

@WebServlet("/instructor/quizzes")
public class QuizListServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try (Connection conn = new DBContext().getConnection()) {
            QuizDAO dao = new QuizDAO(conn);
            List<Quiz> quizzes = dao.getAllQuizzes();
            request.setAttribute("quizzes", quizzes);
            request.getRequestDispatcher("/instructor/quiz-list.jsp").forward(request, response);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
