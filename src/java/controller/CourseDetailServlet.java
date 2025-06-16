package controller;

import dal.CourseDAO;
import dal.EnrollmentDAO;
import dal.PackageDAO;
import dto.EnrollmentDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Course;

import java.io.IOException;
import java.util.List;
import model.Enrollment;

@WebServlet(name = "CourseDetailServlet", urlPatterns = {"/CourseDetailServlet"})
public class CourseDetailServlet extends HttpServlet {
    
    private CourseDAO courseDAO = new CourseDAO();
    private EnrollmentDAO enrollmentDAO = new EnrollmentDAO();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        CourseDAO dao = new CourseDAO();
        PackageDAO packageDAO = new PackageDAO();
request.setAttribute("packageList", packageDAO.getAllPackages());


        // Lấy danh sách tất cả khóa học để hiển thị dropdown
        List<Course> courseList = dao.getAllCourses();
        request.setAttribute("courseList", courseList);
        
        // Lấy danh sách featured course
        List<Course> featuredCourses = dao.getFeaturedCourses();
request.setAttribute("featuredCourses", featuredCourses);


        String courseIdParam = request.getParameter("courseId");
        String action = request.getParameter("action");
        String enrollmentIdParam = request.getParameter("enrollmentId");

        if ("edit".equals(action) && enrollmentIdParam != null) {
            try {
                int enrollmentId = Integer.parseInt(enrollmentIdParam);
                Enrollment enrollment = enrollmentDAO.getEnrollmentById(enrollmentId);
                if (enrollment != null) {
                    request.setAttribute("enrollment", enrollment);
                }
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid enrollment ID");
                return;
            }
        } else if (courseIdParam == null || courseIdParam.isEmpty()) {
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
