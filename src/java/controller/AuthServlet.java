package controller;

import dao.UserDAO;
import model.User;
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
import java.sql.Timestamp;
import javax.mail.*;
import javax.mail.internet.*;


public class AuthServlet extends HttpServlet {
    private UserDAO userDAO = new UserDAO();
    
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
            response.sendRedirect("student/dashboard.jsp");
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