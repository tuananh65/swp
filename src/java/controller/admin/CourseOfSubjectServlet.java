package controller.admin;

import dal.CourseDAO;
import model.Course;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/courses-of-subject")
public class CourseOfSubjectServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String sid = request.getParameter("subjectId");

        try {
            int subjectId = Integer.parseInt(sid);

            CourseDAO dao = new CourseDAO();
            List<Course> courseList = dao.getCoursesBySubjectId(subjectId);

            request.setAttribute("courses", courseList);
            request.setAttribute("subjectId", subjectId); // nếu cần dùng lại ở JSP
            request.getRequestDispatcher("admin/courseofsubject.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid subject ID");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server error");
        }
    }
}
