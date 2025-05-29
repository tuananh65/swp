package controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Users;
import dal.UserDAO;
import dto.UserDetailDTO; // Import DTO

@WebServlet(name = "UserDetailServlet", urlPatterns = "/userdetail")
public class UserDetailServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userIdStr = request.getParameter("id");
        int userId;
        try {
            userId = Integer.parseInt(userIdStr);
        } catch (NumberFormatException e) {
            response.getWriter().println("Invalid User ID");
            return;
        }

        UserDAO userDao = new UserDAO();
        UserDetailDTO userDetailDTO = userDao.getUserByIdWithRoleName(userId);

        if (userDetailDTO != null) {
            request.setAttribute("loggedInUser", userDetailDTO.getUser()); // Đặt đối tượng Users
            request.setAttribute("roleName", userDetailDTO.getRoleName()); // Đặt RoleName
            request.getRequestDispatcher("js/UserDetail.jsp").forward(request, response);
        } else {
            response.getWriter().println("User not found with ID: " + userId);
        }
    }
}