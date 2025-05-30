package controller;

import dal.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.User;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@WebServlet("/updateUser")
public class UpdateUserServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        try {
            String userIDStr = request.getParameter("userID");
            System.out.println("Debug userID param: " + userIDStr);
            if (userIDStr == null || userIDStr.trim().isEmpty()) {
                request.setAttribute("errorMessage", "User ID không được gửi hoặc không hợp lệ.");
                request.getRequestDispatcher("/profile-edit.jsp").forward(request, response);
                return;
            }
            int userID = Integer.parseInt(userIDStr);

            User oldUser = userDAO.getUserById(userID); // Lấy user cũ từ DB

            // Lấy các trường được phép sửa từ request
            String fullName = request.getParameter("userFullName");
            String address = request.getParameter("userAddress");
            String password = request.getParameter("userPassword");
            String phone = request.getParameter("userPhone");
            String dobStr = request.getParameter("dateOfBirth");
            java.sql.Date dob = null;
            if (dobStr != null && !dobStr.isEmpty()) {
                try {
                    // HTML input type="date" gửi yyyy-MM-dd
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date parsed = sdf.parse(dobStr);
                    dob = new java.sql.Date(parsed.getTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            String gender = request.getParameter("userGender");

            // Tạo user mới và giữ nguyên các trường không thay đổi
            User user = new User();
            user.setUserID(userID);
            user.setUserName(oldUser.getUserName());
            user.setRoleID(oldUser.getRoleID());
            user.setUserAvatarUrl(oldUser.getUserAvatarUrl());
            user.setUserEmail(oldUser.getUserEmail());
            user.setUserStatus(oldUser.getUserStatus());
            user.setCreatedAt(oldUser.getCreatedAt());

            // Cập nhật các trường được sửa
            user.setUserFullName(fullName);
            user.setUserAddress(address);
            user.setUserPassword(password);
            user.setUserPhone(phone);
            user.setDateOfBirth(dob);
            user.setUserGender(gender);

            userDAO.updateUser(user);

            response.sendRedirect(request.getContextPath() + "/profile?id=" + userID);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Lỗi cập nhật dữ liệu!");
            request.getRequestDispatcher("/profile-edit.jsp").forward(request, response);
        }
    }
}
