package controller.student;

import dal.CourseDAO;
import model.Course;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Date;
import java.sql.Timestamp;

@WebServlet(name = "MyRegistrationsServlet", urlPatterns = {"/myRegistrations"})
public class MyRegistrationsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        // Default userID = 3
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response); // Handle POST requests the same way as GET
    }

    @Override
    public String getServletInfo() {
        return "MyRegistrationsServlet for displaying and managing user registrations";
    }
}