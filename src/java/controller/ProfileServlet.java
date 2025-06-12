package controller;

import dal.UserDAO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import model.User;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@WebServlet(urlPatterns = {"/profile", "/updateUser", "/uploadAvatar"})
@MultipartConfig
public class ProfileServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String servletPath = request.getServletPath();

        if ("/profile".equals(servletPath)) {
            // Handle profile display
            String idParam = request.getParameter("id");
            if (idParam == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "User ID is required");
                return;
            }

            try {
                int userId = Integer.parseInt(idParam);
                User user = userDAO.getUserById(userId);

                if (user == null) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found");
                    return;
                }

                request.setAttribute("user", user);
                RequestDispatcher dispatcher = request.getRequestDispatcher("/profile.jsp");
                dispatcher.forward(request, response);
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid User ID");
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String servletPath = request.getServletPath();

        if ("/updateUser".equals(servletPath)) {
            // Handle user profile update
            request.setCharacterEncoding("UTF-8");

            try {
                String userIdStr = request.getParameter("userID");
                System.out.println("Debug userID param: " + userIdStr);
                if (userIdStr == null || userIdStr.trim().isEmpty()) {
                    request.setAttribute("errorMessage", "User ID không được gửi hoặc không hợp lệ.");
                    request.getRequestDispatcher("/profile.jsp").forward(request, response);
                    return;
                }
                int userId = Integer.parseInt(userIdStr);

                User oldUser = userDAO.getUserById(userId);

                String fullName = request.getParameter("userFullName");
                String address = request.getParameter("userAddress");
                String password = request.getParameter("userPassword");
                String phone = request.getParameter("userPhone");
                String dobStr = request.getParameter("dateOfBirth");
                java.sql.Date dob = null;
                if (dobStr != null && !dobStr.isEmpty()) {
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        Date parsed = sdf.parse(dobStr);
                        dob = new java.sql.Date(parsed.getTime());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                String gender = request.getParameter("userGender");

                User user = new User();
                user.setUserId(userId);
                user.setUserName(oldUser.getUserName());
                user.setRoleId(oldUser.getRoleId());
                user.setAvatarUrl(oldUser.getAvatarUrl());
                user.setEmail(oldUser.getEmail());
                user.setStatus(oldUser.getStatus());
                user.setCreatedAt(oldUser.getCreatedAt());
                user.setFullName(fullName);
                user.setAddress(address);
                user.setPassword(password != null && !password.isEmpty() ? password : oldUser.getPassword());
                user.setPhone(phone);
                user.setDateOfBirth(dob);
                user.setGender(gender);

                userDAO.updateUser(user);

                response.sendRedirect(request.getContextPath() + "/profile?id=" + userId);
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("errorMessage", "Lỗi cập nhật dữ liệu!");
                request.getRequestDispatcher("/profile.jsp").forward(request, response);
            }
        } else if ("/uploadAvatar".equals(servletPath)) {
            // Handle avatar upload
            int userId = Integer.parseInt(request.getParameter("userID"));
            Part avatarPart = request.getPart("avatar");

            String fileName = Paths.get(avatarPart.getSubmittedFileName()).getFileName().toString();
            String uploadPath = getServletContext().getRealPath("/images");
            File dir = new File(uploadPath);
            if (!dir.exists()) dir.mkdirs();

            String fullPath = uploadPath + File.separator + fileName;
            avatarPart.write(fullPath);

            User user = userDAO.getUserById(userId);
            user.setAvatarUrl("/images/" + fileName);
            userDAO.updateUser(user);

            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
        }
    }

    @Override
    public String getServletInfo() {
        return "Handles user profile display, updates, and avatar uploads";
    }
}