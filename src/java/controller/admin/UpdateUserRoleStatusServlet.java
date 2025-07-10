package controller.admin;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import dal.UserDAO;

@WebServlet(name = "UpdateUserRoleStatusServlet", urlPatterns = "/updateUserRoleStatus")
public class UpdateUserRoleStatusServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = Integer.parseInt(request.getParameter("userId"));
        int roleId = Integer.parseInt(request.getParameter("roleId"));
        String status = request.getParameter("status");

        UserDAO userDao = new UserDAO();
        boolean updated = userDao.updateUserRoleAndStatus(userId, roleId, status);

        if (updated) {
            // Chuyển hướng về trang chi tiết người dùng sau khi cập nhật thành công
            response.sendRedirect(request.getContextPath() + "/userdetail?id=" + userId);
        } else {
            // Xử lý lỗi nếu cập nhật không thành công
            response.getWriter().println("Failed to update user role and status.");
        }
    }
}