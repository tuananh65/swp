package dal;

import model.Post;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
/**
 * Lớp PostDAO chịu trách nhiệm truy xuất dữ liệu bài viết từ cơ sở dữ liệu.
 * Thực hiện các thao tác CRUD và truy vấn lọc, tìm kiếm, phân trang cho bảng Post.
 * Kế thừa từ DBContext để sử dụng kết nối cơ sở dữ liệu.
 */
public class PostDAO extends DBContext {

    /**
     * Lấy danh sách bài viết có phân trang.
     *
     * @param page     Trang hiện tại (bắt đầu từ 1)
     * @param pageSize Số bài viết trên mỗi trang
     * @return Danh sách bài viết theo trang yêu cầu
     */
        private static final Logger logger = LogManager.getLogger(PostDAO.class);


    public List<Post> getPaginatedPosts(int page, int pageSize) {
        List<Post> posts = new ArrayList<>();
        String query = "SELECT id, title, thumbnail, category, briefInfo, content, author, updatedDate, featured, status " +
                       "FROM Post ORDER BY updatedDate DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            int safeOffset = Math.max(0, (page - 1) * pageSize);
            int safePageSize = (pageSize <= 0) ? 10 : pageSize;

            ps.setInt(1, safeOffset);
            ps.setInt(2, safePageSize);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    posts.add(mapResultSetToPost(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return posts;
    }
public int getTotalPosts() throws SQLException {
    String query = "SELECT COUNT(*) FROM Post";
    
    logger.info("Executing getTotalPosts()...");  // ✔️ Bắt đầu thực thi
    
    try (Connection conn = getConnection();
         PreparedStatement ps = conn.prepareStatement(query);
         ResultSet rs = ps.executeQuery()) {
        
        if (rs.next()) {
            int total = rs.getInt(1);
            logger.debug("Total posts retrieved: " + total);  // ✔️ Log giá trị lấy được
            return total;
        }

    } catch (SQLException e) {
        logger.error("Error executing getTotalPosts()", e);  // ✔️ Log lỗi + stack trace
        throw e;
    }

    return 0;
}

    /**
     * Lấy tổng số bài viết trong bảng Post.
     *
     * @return Tổng số bài viết
     */
   /* public int getTotalPosts() {
        String query = "SELECT COUNT(*) FROM Post";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    */

    /**
     * Lấy danh sách bài viết mới nhất theo số lượng giới hạn.
     *
     * @param limit Số lượng bài viết muốn lấy
     * @return Danh sách bài viết mới nhất
     */
    public List<Post> getRecentPosts(int limit) {
        List<Post> posts = new ArrayList<>();
        String query = "SELECT id, title, thumbnail, category, briefInfo, content, author, updatedDate, featured, status " +
                       "FROM Post ORDER BY updatedDate DESC OFFSET 0 ROWS FETCH NEXT ? ROWS ONLY";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            int safeLimit = (limit <= 0) ? 10 : limit;
            ps.setInt(1, safeLimit);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    posts.add(mapResultSetToPost(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return posts;
    }

    /**
     * Lấy danh sách bài viết theo chuyên mục có phân trang.
     *
     * @param category Tên chuyên mục
     * @param page     Trang hiện tại
     * @param pageSize Số bài viết mỗi trang
     * @return Danh sách bài viết theo chuyên mục
     */
    public List<Post> getPostsByCategory(String category, int page, int pageSize) {
        List<Post> posts = new ArrayList<>();
        String query = "SELECT id, title, thumbnail, category, briefInfo, content, author, updatedDate, featured, status " +
                       "FROM Post WHERE category = ? ORDER BY updatedDate DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, category);
            int safeOffset = Math.max(0, (page - 1) * pageSize);
            int safePageSize = (pageSize <= 0) ? 10 : pageSize;

            ps.setInt(2, safeOffset);
            ps.setInt(3, safePageSize);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    posts.add(mapResultSetToPost(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return posts;
    }

    /**
     * Lấy tổng số bài viết trong một chuyên mục.
     *
     * @param category Chuyên mục
     * @return Số bài viết trong chuyên mục
     */
    public int getTotalPostsByCategory(String category) throws SQLException {
    String query = "SELECT COUNT(*) FROM Post WHERE category = ?";
    logger.info("Executing getTotalPostsByCategory() with category: " + category);

    try (Connection conn = getConnection();
         PreparedStatement ps = conn.prepareStatement(query)) {

        ps.setString(1, category);

        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                int total = rs.getInt(1);
                logger.debug("Total posts in category [" + category + "]: " + total);
                return total;
            }
        }

    } catch (SQLException e) {
        logger.error("Error executing getTotalPostsByCategory() with category: " + category, e);
        throw e;
    }

    return 0;
}

    /**
     * Tìm kiếm bài viết theo từ khóa (tiêu đề hoặc mô tả ngắn), có phân trang.
     *
     * @param keyword  Từ khóa tìm kiếm
     * @param page     Trang hiện tại
     * @param pageSize Số bài mỗi trang
     * @return Danh sách bài viết phù hợp
     */
    public List<Post> searchPosts(String keyword, int page, int pageSize) {
        List<Post> posts = new ArrayList<>();
        String query = "SELECT id, title, thumbnail, category, briefInfo, content, author, updatedDate, featured, status " +
                       "FROM Post WHERE title LIKE ? OR briefInfo LIKE ? " +
                       "ORDER BY updatedDate DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            String searchTerm = "%" + keyword + "%";
            ps.setString(1, searchTerm);
            ps.setString(2, searchTerm);

            int safeOffset = Math.max(0, (page - 1) * pageSize);
            int safePageSize = (pageSize <= 0) ? 10 : pageSize;

            ps.setInt(3, safeOffset);
            ps.setInt(4, safePageSize);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    posts.add(mapResultSetToPost(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return posts;
    }

    /**
     * Lấy tổng số bài viết khớp với từ khóa tìm kiếm.
     *
     * @param keyword Từ khóa tìm kiếm
     * @return Số lượng bài viết phù hợp
     */
    public int getTotalSearchPosts(String keyword) {
        String query = "SELECT COUNT(*) FROM Post WHERE title LIKE ? OR briefInfo LIKE ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            String searchTerm = "%" + keyword + "%";
            ps.setString(1, searchTerm);
            ps.setString(2, searchTerm);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Lấy chi tiết bài viết theo ID.
     *
     * @param id ID bài viết
     * @return Đối tượng Post tương ứng hoặc null nếu không tìm thấy
     */
    public Post getPostById(int id) {
        String query = "SELECT id, title, thumbnail, category, briefInfo, content, author, updatedDate, featured, status " +
                       "FROM Post WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToPost(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Thêm bài viết mới vào cơ sở dữ liệu.
     *
     * @param post Đối tượng bài viết cần thêm
     * @return ID của bài viết vừa được thêm
     */
    public int insertPost(Post post) throws SQLException {
    String query = "INSERT INTO Post (title, thumbnail, category, briefInfo, content, author, updatedDate, featured, status) " +
                   "OUTPUT INSERTED.Id VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    logger.info("Executing insertPost()...");

    try (Connection conn = getConnection();
         PreparedStatement ps = conn.prepareStatement(query)) {

        ps.setString(1, post.getTitle());
        ps.setString(2, post.getThumbnail());
        ps.setString(3, post.getCategory());
        ps.setString(4, post.getBriefInfo());
        ps.setString(5, post.getContent());
        ps.setString(6, post.getAuthor());
        ps.setTimestamp(7, post.getUpdatedDate());
        ps.setBoolean(8, post.isFeatured());
        ps.setString(9, post.getStatus());

        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                int newId = rs.getInt(1);
                post.setId(newId);
                logger.debug("New post inserted with ID: " + newId);
                return newId;
            }
        }
    } catch (SQLException e) {
        logger.error("Error executing insertPost()", e);
        throw e;
    }
    return 0;
}


    /**
     * Cập nhật thông tin bài viết hiện có.
     *
     * @param post Bài viết cần cập nhật
     */
    public void updatePost(Post post) throws SQLException {
    String query = "UPDATE Post SET title = ?, thumbnail = ?, category = ?, briefInfo = ?, content = ?, author = ?, " +
                   "updatedDate = ?, featured = ?, status = ? WHERE id = ?";

    logger.info("Executing updatePost() for post ID: " + post.getId());

    try (Connection conn = getConnection();
         PreparedStatement ps = conn.prepareStatement(query)) {

        ps.setString(1, post.getTitle());
        ps.setString(2, post.getThumbnail());
        ps.setString(3, post.getCategory());
        ps.setString(4, post.getBriefInfo());
        ps.setString(5, post.getContent());
        ps.setString(6, post.getAuthor());
        ps.setTimestamp(7, post.getUpdatedDate());
        ps.setBoolean(8, post.isFeatured());
        ps.setString(9, post.getStatus());
        ps.setInt(10, post.getId());

        int rowsAffected = ps.executeUpdate();
        logger.debug("Updated rows: " + rowsAffected);
    } catch (SQLException e) {
        logger.error("Error executing updatePost()", e);
        throw e;
    }
}

    /**
     * Xóa bài viết theo ID.
     *
     * @param id ID bài viết cần xóa
     */
    public void deletePost(int id) {
        String query = "DELETE FROM Post WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Hàm trợ giúp: ánh xạ dữ liệu từ ResultSet thành đối tượng Post.
     *
     * @param rs ResultSet trả về từ truy vấn
     * @return Đối tượng Post
     * @throws SQLException Nếu có lỗi truy cập dữ liệu
     */
    private Post mapResultSetToPost(ResultSet rs) throws SQLException {
        return new Post(
            rs.getInt("id"),
            rs.getString("title"),
            rs.getString("thumbnail"),
            rs.getString("category"),
            rs.getString("briefInfo"),
            rs.getString("content"),
            rs.getString("author"),
            rs.getTimestamp("updatedDate"),
            rs.getBoolean("featured"),
            rs.getString("status")
        );
    }
    public static void main(String[] args) {
    Logger logger = LogManager.getLogger(PostDAO.class);

    PostDAO dao = new PostDAO();

    try {
        logger.info("Testing getTotalPosts()");
        int totalPosts = dao.getTotalPosts();
        System.out.println("Total Posts: " + totalPosts);

        logger.info("Testing getPaginatedPosts()");
        List<Post> paginatedPosts = dao.getPaginatedPosts(1, 5);
        for (Post p : paginatedPosts) {
            System.out.println("Post: " + p.getId() + " - " + p.getTitle());
        }

        logger.info("Testing getRecentPosts()");
        List<Post> recentPosts = dao.getRecentPosts(3);
        for (Post p : recentPosts) {
            System.out.println("Recent Post: " + p.getId() + " - " + p.getTitle());
        }

        logger.info("Testing searchPosts()");
        List<Post> searchedPosts = dao.searchPosts("test", 1, 5);
        for (Post p : searchedPosts) {
            System.out.println("Searched Post: " + p.getId() + " - " + p.getTitle());
        }

        logger.info("Testing insertPost()");
        Post newPost = new Post();
        newPost.setTitle("Test Insert");
        newPost.setThumbnail("thumbnail.jpg");
        newPost.setCategory("Test");
        newPost.setBriefInfo("This is a brief info");
        newPost.setContent("This is content");
        newPost.setAuthor("Tester");
        newPost.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
        newPost.setFeatured(false);
        newPost.setStatus("Draft");
        int newId = dao.insertPost(newPost);
        System.out.println("Inserted Post ID: " + newId);

        logger.info("Testing getPostById()");
        Post fetchedPost = dao.getPostById(newId);
        if (fetchedPost != null) {
            System.out.println("Fetched Post: " + fetchedPost.getId() + " - " + fetchedPost.getTitle());
        }

        logger.info("Testing updatePost()");
        fetchedPost.setTitle("Updated Title");
        dao.updatePost(fetchedPost);
        System.out.println("Post updated.");

        logger.info("Testing deletePost()");
        dao.deletePost(newId);
        System.out.println("Post deleted.");

    } catch (SQLException e) {
        logger.error("Error in main()", e);
        e.printStackTrace();
    }
}

}
    


