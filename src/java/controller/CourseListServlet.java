package controller;

import dal.CourseDAO;
import model.Course;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "CourseListServlet", urlPatterns = {"/coursesList", "/courses"})
public class CourseListServlet extends HttpServlet {

    private static final int PAGE_SIZE = 9; // số khóa học mỗi trang
    private CourseDAO courseDAO;

    @Override
    public void init() throws ServletException {
        courseDAO = new CourseDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        // Lấy tham số
        String searchTerm = request.getParameter("search");
        if (searchTerm == null) searchTerm = "";
        
        String pageParam = request.getParameter("page");
        int page = 1;
        try {
            if (pageParam != null) page = Integer.parseInt(pageParam);
        } catch (NumberFormatException e) {
            page = 1;
        }

        // Lấy dữ liệu từ DB
        List<Course> courses = courseDAO.searchCourses(searchTerm, page, PAGE_SIZE);
        int totalCourses = courseDAO.countCourses(searchTerm);
        int totalPages = (int) Math.ceil((double) totalCourses / PAGE_SIZE);

        // Truyền về JSP
        request.setAttribute("courses", courses);
        request.setAttribute("searchTerm", searchTerm);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("totalCourses", totalCourses);

        request.getRequestDispatcher("/courselist.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
