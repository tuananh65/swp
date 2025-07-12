package controller.admin;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.util.List;
// import model.User; // Không cần thiết nếu bạn chỉ dùng UserWithRoleDTO trực tiếp
import dal.UserDAO;
import dto.UserWithRoleDTO;

@WebServlet(name ="UserListServlet", urlPatterns = "/userlist")
public class UserListServlet extends HttpServlet {
    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        userDAO = new UserDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String searchKeyword = request.getParameter("search");
        String filterRole = request.getParameter("role"); // Lấy tham số filterRole
        String filterGender = request.getParameter("gender"); // Lấy tham số filterGender
        String filterStatus = request.getParameter("status"); // Lấy tham số filterStatus

        int page = 1;
        int recordsPerPage = 10;

        if (request.getParameter("page") != null && !request.getParameter("page").isEmpty()) {
            try {
                page = Integer.parseInt(request.getParameter("page"));
            } catch (NumberFormatException e) {
                // Xử lý nếu page không phải số hợp lệ, ví dụ: set lại về 1
                page = 1;
            }
        }

        List<UserWithRoleDTO> userList;
        int totalRecords = 0;

        // **Cập nhật logic để bao gồm cả tìm kiếm và lọc**
        userList = userDAO.getFilteredUsers(searchKeyword, filterRole, filterGender, filterStatus,
                                            (page - 1) * recordsPerPage, recordsPerPage);
        totalRecords = userDAO.getTotalFilteredUserCount(searchKeyword, filterRole, filterGender, filterStatus);


        int totalPages = (int) Math.ceil((double) totalRecords / recordsPerPage);

        request.setAttribute("userList", userList);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("currentPage", page);
        // **Đặt lại các tham số tìm kiếm và lọc vào request để JSP có thể giữ trạng thái**
        request.setAttribute("search", searchKeyword != null ? searchKeyword : "");
        request.setAttribute("filterRole", filterRole != null ? filterRole : "");
        request.setAttribute("filterGender", filterGender != null ? filterGender : "");
        request.setAttribute("filterStatus", filterStatus != null ? filterStatus : "");

        request.getRequestDispatcher("admin/UserList.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Trong trường hợp này, doPost cũng gọi doGet để xử lý các request POST như GET
        doGet(request, response);
    }
}