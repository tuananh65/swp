package controller;

import dal.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import model.Users;

@WebServlet(name = "ResetPasswordController", urlPatterns = {"/resetpassword"})
public class ResetPasswordController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String token = request.getParameter("token");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");
        
        if (!newPassword.equals(confirmPassword)) {
            request.setAttribute("errorMessage", "Passwords do not match.");
            request.setAttribute("token", token);
            request.getRequestDispatcher("js/resetPassword.jsp").forward(request, response);
            return;
        }
        
        UserDAO userDAO = new UserDAO();
        Users user = userDAO.getUserByResetToken(token);
        
        if (user == null) {
            request.setAttribute("errorMessage", "Invalid or expired reset link. Please request a new one.");
            request.getRequestDispatcher("js/forgotPassword.jsp").forward(request, response);
            return;
        }
        
        boolean success = userDAO.updatePassword(user.getUserID(), newPassword);
        
        if (success) {
            // Clear the reset token after successful password change
            userDAO.clearResetToken(user.getUserID());
            
            request.setAttribute("successMessage", "Password has been reset successfully. You can now login with your new password.");
            request.getRequestDispatcher("js/SignIn.jsp").forward(request, response);
        } else {
            request.setAttribute("errorMessage", "Failed to reset password. Please try again.");
            request.setAttribute("token", token);
            request.getRequestDispatcher("js/resetPassword.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}