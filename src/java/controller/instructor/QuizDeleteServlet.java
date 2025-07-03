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

        try {
            int quizId = Integer.parseInt(idRaw);

            try (Connection conn = new DBContext().getConnection()) {
                QuizDAO dao = new QuizDAO(conn);
                dao.deleteQuiz(quizId);
                // Sau khi xoá, quay lại list
                response.sendRedirect(request.getContextPath() + "/instructor/quiz-list");
            }

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Quiz ID.");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}

