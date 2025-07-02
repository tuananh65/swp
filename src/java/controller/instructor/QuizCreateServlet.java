package controller.instructor;

import dal.DBContext;
import dal.QuizDAO;
import model.Quiz;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.Connection;

@WebServlet("/instructor/quiz-create")
public class QuizCreateServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Tạo quiz trống để hiển thị form
        Quiz quiz = new Quiz(0, 0, "", "", "Easy", 30, true);
        request.setAttribute("quiz", quiz);
        request.setAttribute("isCreate", true);
        request.getRequestDispatcher("/instructor/quiz-detail.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String difficulty = request.getParameter("difficulty");
        int duration = Integer.parseInt(request.getParameter("duration"));
        boolean active = request.getParameter("active") != null;

        try (Connection conn = new DBContext().getConnection()) {
            QuizDAO dao = new QuizDAO(conn);
            Quiz quiz = new Quiz(0, 0, title, description, difficulty, duration, active);
            dao.createQuiz(quiz);

            response.sendRedirect(request.getContextPath() + "/instructor/quizzes");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
