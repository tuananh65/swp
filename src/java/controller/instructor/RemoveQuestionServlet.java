package controller.instructor;

import dal.DBContext;
import dal.QuizDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.Connection;


@WebServlet("/instructor/remove-question")
public class RemoveQuestionServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String questionIdRaw = request.getParameter("questionID");
        String quizIdRaw = request.getParameter("quizID");

        if (questionIdRaw == null || quizIdRaw == null) {
            response.sendRedirect(request.getContextPath() + "/instructor/quiz-list");
            return;
        }

        int questionId = Integer.parseInt(questionIdRaw);
        int quizId = Integer.parseInt(quizIdRaw);

        try (Connection conn = new DBContext().getConnection()) {
            QuizDAO dao = new QuizDAO(conn);
            dao.removeQuestionFromQuiz(quizId, questionId);

            // Quay về trang quiz-detail
            response.sendRedirect(request.getContextPath() + "/instructor/quiz?id=" + quizId);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}


