// UserDetailServlet.java
package controller.admin;

import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import dal.UserDAO;
import dto.UserDetailDTO;
import dto.UserWithRoleDTO; // Import DTO này

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
            request.setAttribute("loggedInUser", userDetailDTO.getUser());
            request.setAttribute("roleName", userDetailDTO.getRoleName());

            // Lấy thêm danh sách người dùng khác (ví dụ: 4 người)
            List<UserWithRoleDTO> otherUsers = userDao.getOtherUsersWithRole(userId, 4);
            request.setAttribute("otherUsers", otherUsers);

            request.getRequestDispatcher("admin/UserDetail.jsp").forward(request, response);
        } else {
            response.getWriter().println("User not found with ID: " + userId);
        }
    }
}