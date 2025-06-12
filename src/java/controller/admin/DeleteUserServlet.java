// DeleteUserServlet.java
package controller.admin;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;

import dal.UserDAO;

@WebServlet(name = "DeleteUserServlet", urlPatterns = "/deleteUser")
public class DeleteUserServlet extends HttpServlet {
    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        userDAO = new UserDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String userIDToDelete = request.getParameter("userID");

        if (userIDToDelete != null && !userIDToDelete.isEmpty()) {
            try {
                int userId = Integer.parseInt(userIDToDelete);
                boolean isDeleted = userDAO.deleteUser(userId);

                if (isDeleted) {
                    // Xóa thành công, bạn có thể gửi một thông báo thành công
                    // và chuyển hướng người dùng trở lại trang danh sách người dùng.
                    response.sendRedirect(request.getContextPath() + "/userlist?message=delete_success");
                } else {
                    // Xóa không thành công, gửi thông báo lỗi (tùy chọn)
                    response.sendRedirect(request.getContextPath() + "/userlist?message=delete_failed");
                }
            } catch (NumberFormatException e) {
                // Xử lý trường hợp userID không phải là số
                response.sendRedirect(request.getContextPath() + "/userlist?message=invalid_userid");
            }
        } else {
            // Xử lý trường hợp không có userID được cung cấp
            response.sendRedirect(request.getContextPath() + "/userlist?message=userid_missing");
        }
    }
}