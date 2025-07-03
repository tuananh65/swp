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

@WebServlet("/instructor/quiz-list")
public class QuizListServlet extends HttpServlet {

    private static final int PAGE_SIZE = 10;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pageRaw = request.getParameter("page");
        int page = 1;
        if (pageRaw != null) {
            try {
                page = Integer.parseInt(pageRaw);
            } catch (NumberFormatException ignored) {}
        }

        String keyword = request.getParameter("keyword");
        if (keyword == null) keyword = "";

        try (Connection conn = new DBContext().getConnection()) {
            QuizDAO dao = new QuizDAO(conn);

            // Danh sách quiz theo trang + tìm kiếm
            List<Quiz> quizzes = dao.searchQuizzes(keyword, page, PAGE_SIZE);

            // Tổng số quiz để tính tổng số trang
            int totalQuiz = dao.countQuizzes(keyword);
            int totalPage = (int) Math.ceil(totalQuiz * 1.0 / PAGE_SIZE);

            // Đẩy dữ liệu ra JSP
            request.setAttribute("quizzes", quizzes);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPage", totalPage);
            request.setAttribute("keyword", keyword);

            request.getRequestDispatcher("/instructor/quiz-list.jsp").forward(request, response);

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}

