package controller;

import dal.CourseDAO;
import model.Course;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CourseListServlet extends HttpServlet {

    private static final int DEFAULT_PAGE_SIZE = 9;
    private CourseDAO courseDAO;

    @Override
    public void init() throws ServletException {
        courseDAO = new CourseDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");

            String searchTerm = request.getParameter("search") != null ? request.getParameter("search") : "";
            String sortBy = request.getParameter("sort") != null ? request.getParameter("sort") : "latest";

            int page = 1;
            try {
                page = Integer.parseInt(request.getParameter("page"));
            } catch (NumberFormatException ignored) {}

            int pageSize = DEFAULT_PAGE_SIZE;
            try {
                pageSize = Integer.parseInt(request.getParameter("pageSize"));
            } catch (NumberFormatException ignored) {}

            // Optional checkbox filters (can be extended if needed)
            String showThumbnail = request.getParameter("showThumbnail") != null ? "true" : "false";
            String showPrice = request.getParameter("showPrice") != null ? "true" : "false";
            String showTagline = request.getParameter("showTagline") != null ? "true" : "false";
            String showTitle = request.getParameter("showTitle") != null ? "true" : "false";
            String showRegister = request.getParameter("showRegister") != null ? "true" : "false";

            int totalCourses = courseDAO.countCourses(searchTerm);
            int totalPages = (int) Math.ceil((double) totalCourses / pageSize);

            if (page > totalPages) {
                page = totalPages > 0 ? totalPages : 1;
            } else if (page < 1) {
                page = 1;
            }

            int offset = (page - 1) * pageSize;
            List<Course> courseList = courseDAO.searchCourses(searchTerm, offset, pageSize, sortBy);

            // Set attributes
            request.setAttribute("courses", courseList);
            request.setAttribute("totalCourses", totalCourses);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("currentPage", page);
            request.setAttribute("pageSize", pageSize);
            request.setAttribute("searchTerm", searchTerm);
            request.setAttribute("sortBy", sortBy);

            request.setAttribute("showThumbnail", showThumbnail);
            request.setAttribute("showPrice", showPrice);
            request.setAttribute("showTagline", showTagline);
            request.setAttribute("showTitle", showTitle);
            request.setAttribute("showRegister", showRegister);

            // Forward to JSP (full or partial handled in JSP via isAjax check)
            request.getRequestDispatcher("/courselist.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Đã xảy ra lỗi khi tải danh sách khóa học");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }
}
