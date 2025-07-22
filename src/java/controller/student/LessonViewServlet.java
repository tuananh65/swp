package controller.student;

import dal.CourseDAO;
import dal.LessonDAO;
import model.Course;
import model.Lesson;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class LessonViewServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Get courseId from request parameter
        String courseIdStr = request.getParameter("courseId");
        int courseId;
        try {
            courseId = Integer.parseInt(courseIdStr);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid course ID");
            return;
        }

        // Fetch the course to get the lessonID
        CourseDAO courseDAO = new CourseDAO();
        Course course = courseDAO.getCourseById(courseId);
        
        if (course == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Course not found");
            return;
        }

        // Get the lessonID from the course
        int lessonId = course.getLessonID();

        // Fetch the specific lesson using lessonID
        LessonDAO lessonDAO = new LessonDAO();
        Lesson lesson = lessonDAO.getLessonById(lessonId);

        if (lesson == null) {
            request.setAttribute("error", "No lesson found for Lesson ID: " + lessonId);
        }

        // Set lesson and courseId as request attributes
        request.setAttribute("lesson", lesson);
        request.setAttribute("courseId", courseId);

        // Forward to lessonView.jsp
        request.getRequestDispatcher("/student/lessonView.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}