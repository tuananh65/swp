package controller.admin;

import dal.PostDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Post;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Servlet xử lý yêu cầu hiển thị danh sách bài viết trong khu vực quản trị (/admin/post-list).
 * Sử dụng phân trang để tải danh sách bài viết từ cơ sở dữ liệu.
 */
@WebServlet("/admin/post-list")
public class PostListServlet extends HttpServlet {

    /** DAO để thao tác với bảng Post */
    private final PostDAO postDAO = new PostDAO();

    /**
     * Xử lý yêu cầu GET để hiển thị danh sách bài viết với phân trang.
     *
     * @param request  Yêu cầu HTTP từ phía client
     * @param response Phản hồi HTTP trả về cho client
     * @throws ServletException nếu có lỗi khi forward JSP
     * @throws IOException      nếu có lỗi khi xử lý luồng dữ liệu
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int page = 1;          // Mặc định là trang 1
        int pageSize = 10;     // Mỗi trang hiển thị 10 bài viết

        // Lấy tham số trang từ URL, nếu có
        String pageParam = request.getParameter("page");
        if (pageParam != null && !pageParam.isEmpty()) {
            try {
                page = Integer.parseInt(pageParam);
            } catch (NumberFormatException e) {
                // Nếu tham số không hợp lệ, mặc định giữ nguyên trang 1
                e.printStackTrace();
            }
        }

        // Lấy danh sách bài viết theo trang hiện tại
        List<Post> posts = postDAO.getPaginatedPosts(page, pageSize);

        // Tổng số bài viết trong DB
        int totalPosts = 0;
        try {
            totalPosts = postDAO.getTotalPosts();
        } catch (SQLException ex) {
            Logger.getLogger(PostListServlet.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Tính tổng số trang cần thiết
        int totalPages = (int) Math.ceil((double) totalPosts / pageSize);

        // Gửi dữ liệu ra view
        request.setAttribute("posts", posts);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);

        // Forward dữ liệu sang JSP để hiển thị
        request.getRequestDispatcher("post-list.jsp").forward(request, response);
    }
}
