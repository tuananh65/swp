package controller;

import dal.PostDAO;
import dal.CourseDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Post;
import model.Course;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@WebServlet(name = "HomeServlet", urlPatterns = {"/home"})
public class HomeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");

        PostDAO postDAO = new PostDAO();
        CourseDAO courseDAO = new CourseDAO();

        // Fetch recent posts (limited to 4)
        List<Post> recentPosts = postDAO.getRecentPosts(4);
        request.setAttribute("recentPosts", recentPosts);

        // Fetch hot posts (limited to 2)
        List<Post> hotPosts = postDAO.getRecentPosts(2);
        request.setAttribute("hotPosts", hotPosts);

        // Fetch featured courses (limited to 3)
        List<Course> featuredCourses = courseDAO.getFeaturedCourses();
        request.setAttribute("featuredCourses", featuredCourses);

        // Fetch recent courses with filtering and pagination
        String searchTerm = request.getParameter("search") != null ? request.getParameter("search").trim() : "";
        String sortBy = request.getParameter("sort") != null ? request.getParameter("sort") : "latest";
        int page = 1;
        try {
            page = Integer.parseInt(request.getParameter("page"));
            if (page < 1) page = 1;
        } catch (NumberFormatException ignored) {}

        int pageSize = 3;
        try {
            pageSize = Integer.parseInt(request.getParameter("pageSize"));
            if (pageSize < 1) pageSize = 3;
            if (pageSize > 100) pageSize = 100;
        } catch (NumberFormatException ignored) {}

        // Checkbox filters with default true for initial load
        boolean isInitialLoad = request.getParameterMap().isEmpty() || 
            (request.getParameter("search") == null && 
             request.getParameter("sort") == null && 
             request.getParameter("page") == null && 
             request.getParameter("pageSize") == null &&
             request.getParameter("showThumbnail") == null &&
             request.getParameter("showPrice") == null &&
             request.getParameter("showRegister") == null &&
             request.getParameter("showTagline") == null &&
             request.getParameter("showTitle") == null);

        boolean showThumbnail = isInitialLoad ? true : "true".equalsIgnoreCase(request.getParameter("showThumbnail"));
        boolean showPrice = isInitialLoad ? true : "true".equalsIgnoreCase(request.getParameter("showPrice"));
        boolean showRegister = isInitialLoad ? true : "true".equalsIgnoreCase(request.getParameter("showRegister"));
        boolean showTagline = isInitialLoad ? true : "true".equalsIgnoreCase(request.getParameter("showTagline"));
        boolean showTitle = isInitialLoad ? true : "true".equalsIgnoreCase(request.getParameter("showTitle"));

        // Set filter attributes
        request.setAttribute("showThumbnail", showThumbnail);
        request.setAttribute("showPrice", showPrice);
        request.setAttribute("showRegister", showRegister);
        request.setAttribute("showTagline", showTagline);
        request.setAttribute("showTitle", showTitle);

        // Calculate pagination
        int totalCourses = courseDAO.countCourses(searchTerm);
        int totalPages = (int) Math.ceil((double) totalCourses / pageSize);

        if (page > totalPages) {
            page = totalPages > 0 ? totalPages : 1;
        }

        int offset = (page - 1) * pageSize;
        List<Course> courseList = courseDAO.searchCourses(searchTerm, offset, pageSize, sortBy);

        // Set course list attributes
        request.setAttribute("courses", courseList != null ? courseList : Collections.emptyList());
        request.setAttribute("totalCourses", totalCourses);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("currentPage", page);
        request.setAttribute("pageSize", pageSize);
        request.setAttribute("searchTerm", searchTerm);
        request.setAttribute("sortBy", sortBy);

        // Forward to home.jsp
        request.getRequestDispatcher("/home.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response); // Handle POST requests the same way as GET for simplicity
    }

    @Override
    public String getServletInfo() {
        return "HomeServlet for Soft Skills Learning homepage";
    }
}
