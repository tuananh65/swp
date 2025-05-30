package controller;

import dal.UserDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.User;

import java.io.IOException;

@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
    System.out.println(">>> Debug: Received login attempt");
    System.out.println(">>> Username: " + username);
    System.out.println(">>> Password: " + password);

        UserDAO userDAO = new UserDAO();
        User user = userDAO.getUserByUsernameAndPassword(username, password);

        if (user != null) {
    HttpSession session = request.getSession();
    session.setAttribute("user", user);

    // Lấy userID từ user
    int userID = user.getUserID();

    response.sendRedirect(request.getContextPath() + "/profile?id=" + userID);
} else {
    request.setAttribute("error", "Tên đăng nhập hoặc mật khẩu không đúng");
    request.getRequestDispatcher("login.jsp").forward(request, response);
}
    }
}

