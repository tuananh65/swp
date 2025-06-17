package controller;

import dal.CourseDAO;
import dal.PackageDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Course;
import model.Package;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "CourseDetailServlet", urlPatterns = {"/CourseDetailServlet"})
public class CourseDetailServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        CourseDAO dao = new CourseDAO();
        PackageDAO packageDAO = new PackageDAO();
List<Package> packageList = packageDAO.getAllPackages();
request.setAttribute("packageList", packageList);

        // Lấy danh sách tất cả khóa học để hiển thị dropdown
        List<Course> courseList = dao.getAllCourses();
        request.setAttribute("courseList", courseList);
        
        // Lấy danh sách featured course
        List<Course> featuredCourses = dao.getFeaturedCourses();
        request.setAttribute("featuredCourses", featuredCourses);


        String courseIdParam = request.getParameter("courseId");

        if (courseIdParam == null || courseIdParam.isEmpty()) {
            // Nếu chưa có courseId, không trả lỗi mà để course = null (hoặc có thể lấy khóa học đầu tiên làm mặc định)
            request.setAttribute("course", null);
        } else {
            try {
                int courseId = Integer.parseInt(courseIdParam);
                Course course = dao.getCourseById(courseId);

                if (course == null) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy khóa học");
                    return;
                }

                request.setAttribute("course", course);

                System.out.println("courseIdParam = " + courseIdParam);
                System.out.println("course = " + course);

            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "courseId không hợp lệ");
                return;
            }
        }

        request.getRequestDispatcher("coursedetail.jsp").forward(request, response);
    }
}
