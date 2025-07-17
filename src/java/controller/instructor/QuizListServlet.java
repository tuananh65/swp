package controller.instructor;

import dal.CourseDAO;
import dal.QuizDAO;
import model.Course;
import model.Quiz;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
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
    int offset = (page - 1) * recordsPerPage;

    String keyword = request.getParameter("searchKeyword");
    if (keyword == null) keyword = "";

    String courseIDParam = request.getParameter("courseID");
    Integer courseID = (courseIDParam != null && !courseIDParam.isEmpty())
            ? Integer.parseInt(courseIDParam)
            : null;

    String statusFilter = request.getParameter("status");
    String difficultyFilter = request.getParameter("difficulty");
    String quizTypeFilter = request.getParameter("quizType");

    List<Course> courses = courseDAO.getAllCourses();

    List<Quiz> quizzes = quizDAO.getQuizzesByPage(
            courseID, keyword, statusFilter, difficultyFilter, quizTypeFilter, offset, recordsPerPage);

    int totalQuizzes = quizDAO.getTotalQuizzes(
            courseID, keyword, statusFilter, difficultyFilter, quizTypeFilter);
    int totalPages = (int) Math.ceil((double) totalQuizzes / recordsPerPage);
    if (totalPages == 0) totalPages = 1;

    request.setAttribute("courses", courses);
    request.setAttribute("quizzes", quizzes);
    request.setAttribute("selectedCourseID", courseID);
    request.setAttribute("statusFilter", statusFilter);
    request.setAttribute("difficultyFilter", difficultyFilter);
    request.setAttribute("quizTypeFilter", quizTypeFilter);
    request.setAttribute("keyword", keyword);
    request.setAttribute("currentPage", page);
    request.setAttribute("totalPages", totalPages);

    request.getRequestDispatcher("/instructor/quiz-list.jsp").forward(request, response);
}

}
