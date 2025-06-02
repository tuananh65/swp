package controller;import dao.RoleDAO;
import dao.UserDAO;
import model.User;
import model.Role;
import java.io.IOException;
import java.util.Properties;
import java.util.UUID;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.PrintWriter;
import static java.lang.System.out;
import java.sql.Timestamp;
import java.util.Date;
import javax.mail.*;
import javax.mail.internet.*;

public class AuthServlet extends HttpServlet {
    private UserDAO userDAO = new UserDAO();
    private RoleDAO roleDAO = new RoleDAO();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet AuthServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet AuthServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String action = request.getParameter("action");
    } 

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String action = request.getParameter("action");
        try (PrintWriter out = response.getWriter()) {
            if ("login".equals(action)) {
                login(request, response);
            } else if ("register".equals(action)) {
                register(request, response);
            } else if ("changePassword".equals(action)) {
                changePassword(request, response);
            } else {
                out.println("<h3>Invalid action!</h3>");
                out.println("<a href='index.jsp'>Quay lại trang chủ</a>");
            }
        } catch (Exception e) {
            try (PrintWriter out = response.getWriter()) {
                out.println("<h3>Error: " + e.getMessage() + "</h3>");
                out.println("<a href='index.jsp'>Quay lại trang chủ</a>");
            }
            e.printStackTrace();
        }
    }

    private void login(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        UserDAO dao = new UserDAO();
        User user = dao.login(email, password); // Chú ý: nếu class là "Users"

        if (user == null) {
            request.setAttribute("errorMessage", "Invalid email or password! Please try again.");
            request.getRequestDispatcher("view/SignIn.jsp").forward(request, response);
        } else {
            // Có thể lưu session nếu muốn
            request.getSession().setAttribute("currentUser", user);
            int role = user.getRoleId();
                switch (role) {
                    case 1:
                        response.sendRedirect("admin/dashboard.jsp");
                        break;
                    case 2:
                        response.sendRedirect("student/dashboard.jsp");
                        break;
                    case 3:
                        response.sendRedirect("instructor/dashboard.jsp");
                        break;
                    default:
                        out.println("<h3>Unknown role!</h3>");
                        out.println("<a href='index.jsp'>Quay lại trang chủ</a>");
                }
        }
    }

    private void register(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String username = request.getParameter("username");
        String fullName = request.getParameter("fullName");
        String gender = request.getParameter("gender");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String password = request.getParameter("password");

        // Kiểm tra email đã tồn tại
        UserDAO userDAO = new UserDAO();
        if (userDAO.isEmailExists(email)) {
            request.setAttribute("errorMessage", "Email already exists! Please use another email.");
            request.getRequestDispatcher("view/register.jsp").forward(request, response);
            return;
        }

        // Tạo đối tượng User
        User user = new User();
        user.setUserName(username);
        user.setFullName(fullName);
        user.setGender(gender);
        user.setEmail(email);
        user.setPhone(phone);
        user.setPassword(password); // Nên băm mật khẩu trong thực tế
        user.setStatus("Active");
        user.setCreatedAt(new Date());

        // Tạo token kích hoạt
        String activationToken = UUID.randomUUID().toString();

        // Lấy RoleId của Student
        int roleId = roleDAO.getRoleIdByName("Student");
        if (roleId == -1) {
            request.setAttribute("errorMessage", "Role 'Student' not found! Please contact support.");
            request.getRequestDispatcher("view/register.jsp").forward(request, response);
            return;
        }

        // Lưu người dùng vào cơ sở dữ liệu
        boolean registered = userDAO.register(user, roleId, activationToken);

        if (registered) {
            // Gửi email kích hoạt
            sendActivationEmail(email, activationToken);
            request.setAttribute("successMessage", "Registration successful! Please check your email to activate your account.");
            request.getRequestDispatcher("view/register.jsp").forward(request, response);
        } else {
            request.setAttribute("errorMessage", "Failed to register. Please try again.");
            request.getRequestDispatcher("view/register.jsp").forward(request, response);
        }
    }

    private void sendActivationEmail(String recipientEmail, String token) {
        final String username = "tranchi18112004@gmail.com"; // Thay bằng email của bạn
        final String password = "ihaq qwgd qalf jabs";   // Thay bằng App Password của Gmail
        String activationLink = "http://localhost:8080/SoftSkill16/activate?token=" + token;

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("Activate Account Soft Skills");
            message.setText("Hello,\n\nPlease click on the following link to activate your account:\n" 
                            + activationLink + "\n\nThanks for signing up!");
            Transport.send(message);
            System.out.println("Activation link sent to " + recipientEmail);
        } catch (MessagingException e) {
            System.out.println("Error sending email " + e.getMessage());
        }
    }
    
    private void changePassword(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("currentUser");

        // Kiểm tra xem người dùng đã đăng nhập chưa
        if (currentUser == null) {
            request.setAttribute("errorMessage", "Please login first!");
            request.getRequestDispatcher("view/SignIn.jsp").forward(request, response);
            return;
        }

        // Lấy thông tin từ form
        String oldPassword = request.getParameter("password");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        // Xác minh mật khẩu cũ
        UserDAO dao = new UserDAO();
        User user = dao.login(currentUser.getEmail(), oldPassword);

        if (user == null) {
            request.setAttribute("errorMessage", "Invalid old password! Please try again.");
            request.getRequestDispatcher("view/changePassword.jsp").forward(request, response);
            return;
        }

        // Kiểm tra mật khẩu mới và xác nhận mật khẩu
        if (!newPassword.equals(confirmPassword)) {
            request.setAttribute("errorMessage", "New password and confirmation do not match!");
            request.getRequestDispatcher("view/changePassword.jsp").forward(request, response);
            return;
        }

        // Cập nhật mật khẩu trong cơ sở dữ liệu
        boolean updated = dao.updatePassword(currentUser.getEmail(), newPassword);

        if (updated) {
            response.sendRedirect("student/dashboard.jsp");
        } else {
            request.setAttribute("errorMessage", "Failed to change password. Please try again.");
            request.getRequestDispatcher("view/changePassword.jsp").forward(request, response);
        }
    }
    @Override
    public String getServletInfo() {
        return "Handles user login authentication";
    }

}   

