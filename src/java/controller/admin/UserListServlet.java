package controller.admin;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.util.List;
import model.User;
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
        int page = 1;
        int recordsPerPage = 10;

        if (request.getParameter("page") != null && !request.getParameter("page").isEmpty()) {
            page = Integer.parseInt(request.getParameter("page"));
        }

        List<UserWithRoleDTO> userList;
        int totalRecords = 0; // Khởi tạo totalRecords với giá trị mặc định

        if (searchKeyword != null && !searchKeyword.isEmpty()) {
            // Thực hiện tìm kiếm
            userList = userDAO.searchUsers(searchKeyword, (page - 1) * recordsPerPage, recordsPerPage);
            totalRecords = userDAO.getTotalSearchResults(searchKeyword);
            request.setAttribute("search", searchKeyword);
        } else {
            // Lấy danh sách người dùng theo phân trang thông thường
            userList = userDAO.getUsersWithRoleByPage((page - 1) * recordsPerPage, recordsPerPage);
            totalRecords = userDAO.getTotalUserCount();
            request.removeAttribute("search");
        }

        int totalPages = (int) Math.ceil((double) totalRecords / recordsPerPage);

        request.setAttribute("userList", userList);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("currentPage", page);
        request.getRequestDispatcher("admin/UserList.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}