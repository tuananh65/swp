package controller;

import dal.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;
import java.util.Calendar;
import model.Users;
import util.EmailSender;

@WebServlet(name = "ForgotPasswordController", urlPatterns = {"/forgotpassword"})
public class ForgotPasswordController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        
        if (action == null) {
            request.getRequestDispatcher("js/forgotPassword.jsp").forward(request, response);
        } else if (action.equals("sendlink")) {
            String username = request.getParameter("username");
            String email = request.getParameter("email");
            
            UserDAO userDAO = new UserDAO();
            Users user = userDAO.getUserByUsernameAndEmail(username, email);
            
            if (user == null) {
                request.setAttribute("errorMessage", "Username or email does not exist in our system.");
                request.getRequestDispatcher("js/forgotPassword.jsp").forward(request, response);
            } else {
                // Generate reset token
                String resetToken = UUID.randomUUID().toString();
                
                // Calculate expiry date (24 hours from now)
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.HOUR, 24);
                Date expiryDate = cal.getTime();
                
                // Store token and expiry date in database
                boolean tokenStored = userDAO.storeResetToken(user.getUserID(), resetToken, expiryDate);
                
                if (!tokenStored) {
                    request.setAttribute("errorMessage", "Failed to generate reset token. Please try again.");
                    request.getRequestDispatcher("js/forgotPassword.jsp").forward(request, response);
                    return;
                }
                
                // Send email with reset link
                String resetLink = request.getRequestURL().toString() 
                        + "?action=reset&token=" + resetToken;
                
                String emailContent = "Please click the following link to reset your password: " + resetLink 
                        + "\n\nThis link will expire in 24 hours.";
                
                EmailSender.sendEmail(email, "Password Reset Request", emailContent);
                
                request.setAttribute("successMessage", "A password reset link has been sent to your email. It will expire in 24 hours.");
                request.getRequestDispatcher("js/forgotPassword.jsp").forward(request, response);
            }
        } else if (action.equals("reset")) {
            String token = request.getParameter("token");
            
            UserDAO userDAO = new UserDAO();
            Users user = userDAO.getUserByResetToken(token);
            
            if (user == null) {
                request.setAttribute("errorMessage", "Invalid or expired reset link. Please request a new one.");
                request.getRequestDispatcher("js/forgotPassword.jsp").forward(request, response);
            } else {
                request.setAttribute("token", token);
                request.getRequestDispatcher("js/resetPassword.jsp").forward(request, response);
            }
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