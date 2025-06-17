package controller;

import dal.CourseDAO;
import dal.EnrollmentDAO;
import model.Course;
import model.User;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class MyCoursesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("currentUser") == null) {
            response.sendRedirect(request.getContextPath() + "/view/SignIn.jsp");
            return;
        }

        User currentUser = (User) session.getAttribute("currentUser");
        int userId = currentUser.getUserId();

        EnrollmentDAO enrollmentDAO = new EnrollmentDAO();

        // Lấy tất cả các khóa học đã được đăng ký và có trạng thái Confirmed của user
        List<Course> courses = enrollmentDAO.getConfirmedCoursesByUserId(userId);

        // Xử lý phân trang như ban đầu
        String pageIndexStr = request.getParameter("page");
        int pageIndex = 1;
        if (pageIndexStr != null && !pageIndexStr.isEmpty()) {
            try {
                pageIndex = Integer.parseInt(pageIndexStr);
            } catch (NumberFormatException e) {
                pageIndex = 1;
            }
        }

        String pageSizeStr = request.getParameter("pageSize");
        int pageSize = 20; // mặc định
        if (pageSizeStr != null && !pageSizeStr.isEmpty()) {
            try {
                pageSize = Integer.parseInt(pageSizeStr);
                if (pageSize <= 0) pageSize = 9;
            } catch (NumberFormatException e) {
                pageSize = 20;
            }
        }

        int totalCourses = courses.size();
        int totalPages = (int) Math.ceil((double) totalCourses / pageSize);

        int fromIndex = Math.min((pageIndex - 1) * pageSize, totalCourses);
        int toIndex = Math.min(fromIndex + pageSize, totalCourses);
        List<Course> pagedCourses = courses.subList(fromIndex, toIndex);

        request.setAttribute("courses", pagedCourses);
        request.setAttribute("currentPage", pageIndex);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("pageSize", pageSize);

        request.getRequestDispatcher("/student/MyCourses.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
