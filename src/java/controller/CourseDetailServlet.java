package controller;

import dal.CourseDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Course;

import java.io.IOException;

@WebServlet(name = "CourseDetailServlet", urlPatterns = {"/coursedetail"})
public class CourseDetailServlet extends HttpServlet {

    private final CourseDAO courseDAO = new CourseDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String courseIdStr = request.getParameter("courseId");

        if (courseIdStr == null || courseIdStr.isEmpty()) {
            // Nếu không có courseId, chuyển hướng về danh sách khóa học hoặc trang khác
            response.sendRedirect("courses");
            return;
        }

        try {
            int courseId = Integer.parseInt(courseIdStr);
            Course course = courseDAO.getCourseById(courseId);

            if (course == null) {
                // Nếu không tìm thấy khóa học, cũng chuyển hướng hoặc báo lỗi
                response.sendRedirect("courses");
                return;
            }

            // Đưa đối tượng course vào request để JSP lấy ra hiển thị
            request.setAttribute("course", course);
            request.getRequestDispatcher("coursedetail.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            // Nếu courseId không phải số hợp lệ, chuyển hướng về danh sách khóa học
            response.sendRedirect("courses");
        }
    }
}
