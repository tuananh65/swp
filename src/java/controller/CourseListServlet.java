package controller;

import dal.CourseDAO;
import model.Course;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Collections;

public class CourseListServlet extends HttpServlet {

    private static final int DEFAULT_PAGE_SIZE = 9;
    private static final int MAX_PAGE_SIZE = 100; // Giới hạn pageSize để tránh lạm dụng
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

            // Lấy các tham số tìm kiếm, phân trang, sắp xếp
            String searchTerm = request.getParameter("search") != null ? request.getParameter("search").trim() : "";
            String sortBy = request.getParameter("sort") != null ? request.getParameter("sort") : "latest";

            int page = 1;
            try {
                page = Integer.parseInt(request.getParameter("page"));
                if (page < 1) page = 1;
            } catch (NumberFormatException ignored) {}

            int pageSize = DEFAULT_PAGE_SIZE;
            try {
                pageSize = Integer.parseInt(request.getParameter("pageSize"));
                if (pageSize < 1) pageSize = DEFAULT_PAGE_SIZE;
                if (pageSize > MAX_PAGE_SIZE) pageSize = MAX_PAGE_SIZE;
            } catch (NumberFormatException ignored) {}

            // Xử lý checkbox: kiểm tra giá trị thực tế
            boolean showThumbnail = "true".equalsIgnoreCase(request.getParameter("showThumbnail"));
            boolean showPrice = "true".equalsIgnoreCase(request.getParameter("showPrice"));
            boolean showRegister = "true".equalsIgnoreCase(request.getParameter("showRegister"));
            boolean showTagline = "true".equalsIgnoreCase(request.getParameter("showTagline"));
            boolean showTitle = "true".equalsIgnoreCase(request.getParameter("showTitle"));

            // Gửi lại các checkbox hiển thị về JSP
            request.setAttribute("showThumbnail", showThumbnail);
            request.setAttribute("showPrice", showPrice);
            request.setAttribute("showRegister", showRegister);
            request.setAttribute("showTagline", showTagline);
            request.setAttribute("showTitle", showTitle);

            // Tính tổng số khóa học & tổng số trang
            int totalCourses = courseDAO.countCourses(searchTerm);
            int totalPages = (int) Math.ceil((double) totalCourses / pageSize);

            if (page > totalPages) {
                page = totalPages > 0 ? totalPages : 1;
            }

            int offset = (page - 1) * pageSize;
            List<Course> courseList = courseDAO.searchCourses(searchTerm, offset, pageSize, sortBy);

            // Gửi dữ liệu về JSP
            request.setAttribute("courses", courseList != null ? courseList : Collections.emptyList());
            request.setAttribute("totalCourses", totalCourses);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("currentPage", page);
            request.setAttribute("pageSize", pageSize);
            request.setAttribute("searchTerm", searchTerm);
            request.setAttribute("sortBy", sortBy);

            // Chuyển tiếp đến trang JSP
            request.getRequestDispatcher("/courselist.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Đã xảy ra lỗi khi tải danh sách khóa học");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }
}