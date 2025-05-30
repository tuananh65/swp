package controller;
import dal.CourseDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import model.Course;

@WebServlet(name = "CourseServlet", urlPatterns = {"/courses"})
public class CourseServlet extends HttpServlet {

    private final CourseDAO courseDAO = new CourseDAO();

    /**
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    String keyword = request.getParameter("keyword");
    List<Course> courseList;

    if (keyword != null && !keyword.trim().isEmpty()) {
        courseList = courseDAO.searchCourses(keyword.trim());
    } else {
        courseList = courseDAO.getAllCourses();
    }

    request.setAttribute("courses", courseList);
    request.setAttribute("keyword", keyword);
    request.getRequestDispatcher("coursedetail.jsp").forward(request, response);
}


}
