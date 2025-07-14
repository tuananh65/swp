package controller.instructor;

import dal.CourseDAO;
import dal.QuizDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Course;
import model.Quiz;

import java.io.IOException;
import java.util.List;

@WebServlet("/instructor/quiz-list")
public class QuizListServlet extends HttpServlet {

    private QuizDAO quizDAO;
    private CourseDAO courseDAO;

    @Override
    public void init() throws ServletException {
        quizDAO = new QuizDAO();
        courseDAO = new CourseDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int page = 1;
        int recordsPerPage = 5;

        if (request.getParameter("page") != null) {
            page = Integer.parseInt(request.getParameter("page"));
        }

        // Keyword search
        String keyword = request.getParameter("searchKeyword");
        if (keyword == null) {
            keyword = "";
        }

        // Course filter
        String courseIDParam = request.getParameter("courseID");
        Integer courseID = (courseIDParam != null && !courseIDParam.isEmpty()) ? Integer.parseInt(courseIDParam) : null;

        // Status filter
        String status = request.getParameter("status");  // Expected values: "Active", "Inactive", null

        // List all courses for filter dropdown
        List<Course> courses = courseDAO.getAllCourses();

        // Fetch quiz list
        int totalQuizzes = quizDAO.getTotalQuizzes(courseID, keyword, status);
        int totalPages = (int) Math.ceil(totalQuizzes * 1.0 / recordsPerPage);
        int offset = (page - 1) * recordsPerPage;

        List<Quiz> quizzes = quizDAO.getQuizzesByPage(courseID, keyword, status, offset, recordsPerPage);

        // Pass data to JSP
        request.setAttribute("courses", courses);
        request.setAttribute("selectedCourseID", courseID);
        request.setAttribute("statusFilter", status);
        request.setAttribute("keyword", keyword);
        request.setAttribute("quizzes", quizzes);
        request.setAttribute("totalPages", totalPages == 0 ? 1 : totalPages);
        request.setAttribute("currentPage", page);

        request.getRequestDispatcher("/instructor/quiz-list.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("delete".equals(action)) {
            int quizID = Integer.parseInt(request.getParameter("quizID"));
            boolean deleted = quizDAO.deleteQuiz(quizID);

            if (deleted) {
                response.sendRedirect("quiz-list");
            } else {
                request.setAttribute("errorMessage", "Failed to delete quiz.");
                doGet(request, response);
            }
        }
    }
}
