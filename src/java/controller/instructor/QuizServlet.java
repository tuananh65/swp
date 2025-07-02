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
import model.Question;

@WebServlet("/instructor/quiz")
public class QuizServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idRaw = request.getParameter("id");
        
        if (idRaw == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Quiz ID is required.");
            return;
        }
        int quizId;
        try {
            quizId = Integer.parseInt(idRaw);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Quiz ID.");
            return;
        }

        try (Connection conn = new DBContext().getConnection()) {
            QuizDAO dao = new QuizDAO(conn);
            Quiz quiz = dao.getQuizById(quizId);
            if (quiz == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Quiz not found.");
                return;
            }
            // Câu hỏi đã thuộc quiz
           List<Question> quizQuestions = dao.getQuestionsByQuiz(quizId);
           request.setAttribute("quizQuestions", quizQuestions);
           // Ngân hàng câu hỏi chưa gắn vào quiz
            List<Question> bankQuestions = dao.getAvailableQuestionsForQuiz(quizId);
            request.setAttribute("bankQuestions", bankQuestions);
     
            request.setAttribute("quiz", quiz);
            request.setAttribute("isCreate", false);
            request.getRequestDispatcher("/instructor/quiz-detail.jsp").forward(request, response);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idRaw = request.getParameter("id");
        String durationRaw = request.getParameter("duration");
        if (idRaw == null || durationRaw == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing required fields.");
            return;
        }
        int quizId;
        int duration;
        try {
            quizId = Integer.parseInt(idRaw);
            duration = Integer.parseInt(durationRaw);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid number format.");
            return;
        }

        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String difficulty = request.getParameter("difficulty");
        boolean active = request.getParameter("active") != null;

        try (Connection conn = new DBContext().getConnection()) {
            QuizDAO dao = new QuizDAO(conn);
            Quiz quiz = dao.getQuizById(quizId);
            if (quiz == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Quiz not found.");
                return;
            }
            quiz.setTitle(title);
            quiz.setDescription(description);
            quiz.setDifficulty(difficulty);
            quiz.setDuration(duration);
            quiz.setActive(active);

            dao.updateQuiz(quiz);

            response.sendRedirect(request.getContextPath() + "/instructor/quizzes");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
