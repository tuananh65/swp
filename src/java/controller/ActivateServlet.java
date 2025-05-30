package controller;

import dao.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/activate")
public class ActivateServlet extends HttpServlet {
    private UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String token = request.getParameter("token");

        if (token == null || token.isEmpty()) {
            request.setAttribute("errorMessage", "Invalid activation link!");
            request.getRequestDispatcher("view/SignIn.jsp").forward(request, response);
            return;
        }

        boolean activated = userDAO.activateAccount(token);

        if (activated) {
            request.setAttribute("successMessage", "Account activated successfully! Please sign in.");
            request.getRequestDispatcher("view/SignIn.jsp").forward(request, response);
        } else {
            request.setAttribute("errorMessage", "Invalid or expired activation link!");
            request.getRequestDispatcher("view/SignIn.jsp").forward(request, response);
        }
    }
}