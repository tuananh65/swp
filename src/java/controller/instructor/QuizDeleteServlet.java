package controller.instructor;

import dal.DBContext;
import dal.QuizDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.Connection;

@WebServlet("/instructor/quiz-delete")
public class QuizDeleteServlet extends HttpServlet {
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
            dao.deleteQuiz(quizId);
            response.sendRedirect(request.getContextPath() + "/instructor/quizzes");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
