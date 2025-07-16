package controller;

import dal.RoleDAO;
import dal.UserDAO;
import model.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;
import java.util.UUID;
import java.util.Date;
import java.util.Calendar;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import javax.mail.*;
import javax.mail.internet.*;

@WebServlet(name = "AuthServlet", urlPatterns = {"/auth", "/activate"})
public class AuthServlet extends HttpServlet {
    private UserDAO userDAO = new UserDAO();
    private RoleDAO roleDAO = new RoleDAO();

    // Email configuration (hard-coded for now - ideally move to config)
    private static final String SMTP_USERNAME = "tranchi18112004@gmail.com";
    private static final String SMTP_PASSWORD = "ihaq qwgd qalf jabs";

    /** Handle GET requests (e.g., logout, activate link) */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        String action = request.getParameter("action");
        System.out.println("[DEBUG] doGet action: " + action);

        if ("reset".equals(action)) {
            resetPasswordGet(request, response);
        } else if ("activate".equals(action) || request.getRequestURI().endsWith("/activate")) {
            activateAccount(request, response);
        } else if ("logout".equals(action)) {
            logout(request, response);
        } else {
            // Default handler
            request.setAttribute("errorMessage", "Invalid or missing action parameter.");
            request.getRequestDispatcher("index.jsp").forward(request, response);
        }
    }

    /** Handle POST requests (all form submits) */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8"); // ⭐ Always set encoding
        response.setContentType("text/html;charset=UTF-8");

        String action = request.getParameter("action");
        System.out.println("[DEBUG] doPost action: " + action);

        try {
            if ("register".equals(action)) {
                register(request, response);
            } else if ("login".equals(action)) {
                login(request, response);
            } else if ("forgotpassword".equals(action)) {
                sendResetLink(request, response);
            } else if ("resetpassword".equals(action)) {
                resetPasswordPost(request, response);
            } else if ("changePassword".equals(action)) {
                changePassword(request, response);
            } else {
                request.setAttribute("errorMessage", "Invalid action!");
                request.getRequestDispatcher("index.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error: " + e.getMessage());
            request.getRequestDispatcher("index.jsp").forward(request, response);
        }
    }

    /** ⭐ Handle user registration */
    private void register(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String username = request.getParameter("username");
        String fullName = request.getParameter("fullName");
        String gender = request.getParameter("gender");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String password = request.getParameter("password");

        // Trim inputs to avoid leading/trailing spaces
        if (email != null) email = email.trim();
        if (password != null) password = password.trim();

        // Check email duplication
        if (userDAO.isEmailExists(email)) {
            request.setAttribute("errorMessage", "Email already exists! Please use another email.");
            request.getRequestDispatcher("view/register.jsp").forward(request, response);
            return;
        }

        // Build User object
        User user = new User();
        user.setUserName(username);
        user.setFullName(fullName);
        user.setGender(gender);
        user.setEmail(email);
        user.setPhone(phone);
        user.setPassword(password);
        user.setStatus("Active");
        user.setCreatedAt(new Date());

        // Generate activation token
        String activationToken = UUID.randomUUID().toString();
        int roleId = roleDAO.getRoleIdByName("Student");

        if (roleId == -1) {
            request.setAttribute("errorMessage", "Role 'Student' not found! Please contact support.");
            request.getRequestDispatcher("view/register.jsp").forward(request, response);
            return;
        }

        boolean registered = userDAO.register(user, roleId, activationToken);

        if (registered) {
            sendActivationEmail(request, email, activationToken);
            request.setAttribute("successMessage", "Registration successful! Please check your email to activate your account.");
            request.getRequestDispatcher("view/register.jsp").forward(request, response);
        } else {
            request.setAttribute("errorMessage", "Failed to register. Please try again.");
            request.getRequestDispatcher("view/register.jsp").forward(request, response);
        }
    }

    /** ⭐ Send activation email after registration */
    private void sendActivationEmail(HttpServletRequest request, String recipientEmail, String token) {
        String activationLink = request.getScheme() + "://" + request.getServerName() + ":" +
                               request.getServerPort() + request.getContextPath() +
                               "/auth?action=activate&token=" + token;
        System.out.println("[DEBUG] Generated activation link: " + activationLink);

        sendEmail(recipientEmail, "Activate Account - Soft Skills",
                "Hello,\n\nPlease click on the following link to activate your account:\n" + activationLink + "\n\nThanks for signing up!");
    }

    /** ⭐ Handle clicking on activation link */
    private void activateAccount(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String token = request.getParameter("token");
        System.out.println("[DEBUG] Activation token: " + token);

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

    /** ⭐ Handle user login */
    private void login(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        System.out.println("[DEBUG] Login email: " + email);
        System.out.println("[DEBUG] Login password: " + password);

        if (email == null || password == null || email.isEmpty() || password.isEmpty()) {
            request.setAttribute("errorMessage", "Email and password are required!");
            request.getRequestDispatcher("view/SignIn.jsp").forward(request, response);
            return;
        }

        email = email.trim();
        password = password.trim();

        User user = userDAO.login(email, password);

        if (user == null) {
            request.setAttribute("errorMessage", "Invalid email or password! Please try again.");
            request.getRequestDispatcher("view/SignIn.jsp").forward(request, response);
        } else {
            request.getSession().setAttribute("currentUser", user);
            int role = user.getRoleId();

            switch (role) {
                case 3:
                    response.sendRedirect("/admin/dashboard.jsp");
                    break;
                case 1:
                    response.sendRedirect("/student/dashboard.jsp");
                    break;
                case 2:
                    response.sendRedirect("instructor/question-list");
                    break;
                default:
                    request.setAttribute("errorMessage", "Unknown role!");
                    request.getRequestDispatcher("home").forward(request, response);
            }
        }
    }

    /** ⭐ Send reset password link via email */
    private void sendResetLink(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String email = request.getParameter("email");
        System.out.println("[DEBUG] Forgot password email: " + email);

        User user = userDAO.getUserByEmail(email);

        if (user == null) {
            request.setAttribute("errorMessage", "Email does not exist in our system.");
            request.getRequestDispatcher("view/forgotPassword.jsp").forward(request, response);
            return;
        }

        String resetToken = UUID.randomUUID().toString();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR, 24);
        Date expiryDate = cal.getTime();

        boolean tokenStored = userDAO.storeResetToken(user.getUserId(), resetToken, expiryDate);

        if (!tokenStored) {
            request.setAttribute("errorMessage", "Failed to generate reset token. Please try again.");
            request.getRequestDispatcher("view/forgotPassword.jsp").forward(request, response);
            return;
        }

        String resetLink = request.getScheme() + "://" + request.getServerName() + ":" +
                          request.getServerPort() + request.getContextPath() +
                          "/auth?action=reset&token=" + resetToken;

        System.out.println("[DEBUG] Generated reset link: " + resetLink);

        String emailContent = "Please click the following link to reset your password: " + resetLink +
                             "\n\nThis link will expire in 24 hours.";

        sendEmail(email, "Password Reset Request - Soft Skills", emailContent);

        request.setAttribute("successMessage", "A password reset link has been sent to your email. It will expire in 24 hours.");
        request.getRequestDispatcher("view/forgotPassword.jsp").forward(request, response);
    }

    /** ⭐ Actually send an email */
    private void sendEmail(String to, String subject, String content) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SMTP_USERNAME, SMTP_PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(SMTP_USERNAME));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(content);

            Transport.send(message);

            System.out.println("[DEBUG] Email sent successfully to " + to);
        } catch (MessagingException e) {
            System.out.println("[ERROR] Sending email: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    // ⭐ ...resetPasswordGet, resetPasswordPost, changePassword, logout
    // ➜ Giữ nguyên như cũ nhưng nhớ thêm request.setCharacterEncoding("UTF-8"); đầu method
    private void resetPasswordGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String token = request.getParameter("token");
        System.out.println("Reset token: " + token); // Debug log
        User user = userDAO.getUserByResetToken(token);

        if (user == null) {
            request.setAttribute("errorMessage", "Invalid or expired reset link. Please request a new one.");
            request.getRequestDispatcher("view/forgotPassword.jsp").forward(request, response);
        } else {
            request.setAttribute("token", token);
            request.getRequestDispatcher("view/resetPassword.jsp").forward(request, response);
        }
    }

    private void resetPasswordPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String token = request.getParameter("token");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        if (!newPassword.equals(confirmPassword)) {
            request.setAttribute("errorMessage", "Passwords do not match.");
            request.setAttribute("token", token);
            request.getRequestDispatcher("view/resetPassword.jsp").forward(request, response);
            return;
        }

        if (newPassword.length() < 8) {
            request.setAttribute("errorMessage", "New password must be at least 8 characters long.");
            request.setAttribute("token", token);
            request.getRequestDispatcher("view/resetPassword.jsp").forward(request, response);
            return;
        }

        User user = userDAO.getUserByResetToken(token);

        if (user == null) {
            request.setAttribute("errorMessage", "Invalid or expired reset link. Please request a new one.");
            request.getRequestDispatcher("view/forgotPassword.jsp").forward(request, response);
            return;
        }

        boolean success = userDAO.updatePassword(user.getEmail(), newPassword); // Updated to use updatePassword

        if (success) {
            userDAO.clearResetToken(user.getUserId());
            request.setAttribute("successMessage", "Password has been reset successfully. You can now login with your new password.");
            request.getRequestDispatcher("view/SignIn.jsp").forward(request, response);
        } else {
            request.setAttribute("errorMessage", "Failed to reset password. Please try again.");
            request.setAttribute("token", token);
            request.getRequestDispatcher("view/resetPassword.jsp").forward(request, response);
        }
    }

    private void changePassword(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("currentUser");

        if (currentUser == null) {
            request.setAttribute("errorMessage", "Please login first!");
            request.getRequestDispatcher("view/SignIn.jsp").forward(request, response);
            return;
        }

        String oldPassword = request.getParameter("password");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        User user = userDAO.login(currentUser.getEmail(), oldPassword);

        if (user == null) {
            request.setAttribute("errorMessage", "Invalid old password! Please try again.");
            request.getRequestDispatcher("view/changePassword.jsp").forward(request, response);
            return;
        }

        if (newPassword.equals(oldPassword)) {
            request.setAttribute("errorMessage", "New password must be different from the old password!");
            request.getRequestDispatcher("view/changePassword.jsp").forward(request, response);
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            request.setAttribute("errorMessage", "New password and confirmation do not match!");
            request.getRequestDispatcher("view/changePassword.jsp").forward(request, response);
            return;
        }

        boolean updated = userDAO.updatePassword(currentUser.getEmail(), newPassword);

        if (updated) {
            response.sendRedirect("student/dashboard.jsp");
        } else {
            request.setAttribute("errorMessage", "Failed to change password. Please try again.");
            request.getRequestDispatcher("view/changePassword.jsp").forward(request, response);
        }
    }
    
    private void logout(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false);
        try (PrintWriter out = response.getWriter()) {
            if (session != null) {
                session.invalidate();
                response.sendRedirect("home");
            } else {
                response.sendRedirect("home");
            }
        }
    }

    @Override
    public String getServletInfo() {
        return "Handles user authentication, registration, account activation, password reset, and password change";
    }
}
