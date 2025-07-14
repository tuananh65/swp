package controller.instructor;

import dal.CourseDAO;
import dal.QuestionDAO;
import model.Course;
import model.Question;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/instructor/question-list")
public class QuestionListServlet extends HttpServlet {

    private QuestionDAO questionDAO;
    private CourseDAO courseDAO;

    @Override
    public void init() throws ServletException {
        questionDAO = new QuestionDAO();
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

        String keyword = request.getParameter("searchKeyword");
        if (keyword == null) {
            keyword = "";
        }

        // ✅ NEW: đọc courseID từ param
        String courseIDParam = request.getParameter("courseID");
        Integer courseID = (courseIDParam != null && !courseIDParam.isEmpty()) 
                ? Integer.parseInt(courseIDParam) 
                : null;

        // ✅ Tìm tất cả courses để đổ ra dropdown
        List<Course> courses = courseDAO.getAllCourses();

        // ✅ Query questions
        int totalQuestions = questionDAO.getTotalQuestions(courseID, keyword);
        int totalPages = (int) Math.ceil(totalQuestions * 1.0 / recordsPerPage);
        int offset = (page - 1) * recordsPerPage;

        List<Question> questions = questionDAO.getQuestionsByPage(courseID, offset, recordsPerPage, keyword);

        // ✅ Đưa attribute cho JSP
        request.setAttribute("courses", courses);
        request.setAttribute("selectedCourseID", courseID);
        request.setAttribute("questions", questions);
        request.setAttribute("totalPages", totalPages == 0 ? 1 : totalPages);
        request.setAttribute("currentPage", page);
        request.setAttribute("keyword", keyword);

        request.getRequestDispatcher("/instructor/question-list.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("delete".equals(action)) {
            int questionID = Integer.parseInt(request.getParameter("questionID"));
            boolean deleted = questionDAO.deleteQuestion(questionID);

            if (deleted) {
                response.sendRedirect("question-list");
            } else {
                request.setAttribute("errorMessage", "Failed to delete question.");
                doGet(request, response);
            }
        }
    }
}
