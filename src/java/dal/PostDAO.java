package dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import model.Post;
import model.User;


public class PostDAO extends DBContext {

    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    // Get paginated posts sorted by updated date
    public List<Post> getPaginatedPosts(int page, int pageSize) {
        List<Post> posts = new ArrayList<>();
        String query = "SELECT id, title, thumbnail, category, briefInfo, content, author, updatedDate " +
                      "FROM Post ORDER BY updatedDate DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        try {conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            int offset = (page - 1) * pageSize;
            ps.setInt(1, offset);
            ps.setInt(2, pageSize);
            rs = ps.executeQuery();
            while (rs.next()) {
                Post post = new Post(
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getString("thumbnail"),
                    rs.getString("category"),
                    rs.getString("briefInfo"),
                    rs.getString("content"),
                    rs.getString("author"),
                    rs.getString("updatedDate")
                );
                posts.add(post);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return posts;
    }

    // Get total number of posts for pagination
    public int getTotalPosts() {
        String query = "SELECT COUNT(*) FROM Post";
        try {conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Get recent posts for sidebar (e.g., latest 4 posts)
    public List<Post> getRecentPosts(int limit) {
        List<Post> posts = new ArrayList<>();
        String query = "SELECT id, title, thumbnail, category, author, updatedDate FROM Post ORDER BY updatedDate DESC OFFSET 0 ROWS FETCH NEXT ? ROWS ONLY";
        try {conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, limit);
            rs = ps.executeQuery();
            while (rs.next()) {
                Post post = new Post(
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getString("thumbnail"),
                    rs.getString("category"),
                    null, null, 
                    rs.getString("author"),    
                    rs.getString("updatedDate")
                );
                posts.add(post);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return posts;
    }

    // Get posts by category
    public List<Post> getPostsByCategory(String category, int page, int pageSize) {
        List<Post> posts = new ArrayList<>();
        String query = "SELECT id, title, thumbnail, category, briefInfo, content, author, updatedDate " +
                      "FROM Post WHERE category = ? ORDER BY updatedDate DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        try {conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, category);
            int offset = (page - 1) * pageSize;
            ps.setInt(2, offset);
            ps.setInt(3, pageSize);
            rs = ps.executeQuery();
            while (rs.next()) {
                Post post = new Post(
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getString("thumbnail"),
                    rs.getString("category"),
                    rs.getString("briefInfo"),
                    rs.getString("content"),
                    rs.getString("author"),
                    rs.getString("updatedDate")
                );
                posts.add(post);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return posts;
    }

    // Get total posts by category
    public int getTotalPostsByCategory(String category) {
        String query = "SELECT COUNT(*) FROM Post WHERE category = ?";
        try {conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, category);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Search posts by title or brief info
    public List<Post> searchPosts(String keyword, int page, int pageSize) {
        List<Post> posts = new ArrayList<>();
        String query = "SELECT id, title, thumbnail, category, briefInfo, content, author, updatedDate " +
                      "FROM Post WHERE title LIKE ? OR briefInfo LIKE ? " +
                      "ORDER BY updatedDate DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        try {conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            String searchTerm = "%" + keyword + "%";
            ps.setString(1, searchTerm);
            ps.setString(2, searchTerm);
            int offset = (page - 1) * pageSize;
            ps.setInt(3, offset);
            ps.setInt(4, pageSize);
            rs = ps.executeQuery();
            while (rs.next()) {
                Post post = new Post(
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getString("thumbnail"),
                    rs.getString("category"),
                    rs.getString("briefInfo"),
                    rs.getString("content"),
                    rs.getString("author"),
                    rs.getString("updatedDate")
                );
                posts.add(post);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return posts;
    }

    // Get total posts for search
    public int getTotalSearchPosts(String keyword) {
        String query = "SELECT COUNT(*) FROM Post WHERE title LIKE ? OR briefInfo LIKE ?";
        try {conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            String searchTerm = "%" + keyword + "%";
            ps.setString(1, searchTerm);
            ps.setString(2, searchTerm);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Get post by ID for post details page
    public Post getPostById(int id) {
        String query = "SELECT id, title, thumbnail, category, briefInfo, content, author, updatedDate FROM Post WHERE id = ?";
        try {conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                return new Post(
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getString("thumbnail"),
                    rs.getString("category"),
                    rs.getString("briefInfo"),
                    rs.getString("content"),
                    rs.getString("author"),
                    rs.getString("updatedDate")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static void main(String[] args) {
    PostDAO dao = new PostDAO();

    // 1. Test phân trang bài viết
    System.out.println("=== 📄 PHÂN TRANG BÀI VIẾT ===");
    List<Post> paginated = dao.getPaginatedPosts(1, 5);
    paginated.forEach(System.out::println);
    System.out.println("→ Tổng bài viết: " + dao.getTotalPosts());

    // 2. Test lấy bài viết mới nhất (sidebar)
    System.out.println("\n=== 🆕 BÀI VIẾT MỚI NHẤT ===");
    List<Post> recent = dao.getRecentPosts(4);
    recent.forEach(System.out::println);

    // 3. Test bài viết theo danh mục
    String category = "Technology"; // đảm bảo có category này trong DB
    System.out.println("\n=== 📚 BÀI VIẾT THEO DANH MỤC: " + category + " ===");
    List<Post> byCategory = dao.getPostsByCategory(category, 1, 3);
    byCategory.forEach(System.out::println);
    System.out.println("→ Tổng bài viết theo danh mục: " + dao.getTotalPostsByCategory(category));

    // 4. Test tìm kiếm bài viết
    String keyword = "AI";  // chỉnh tùy DB
    System.out.println("\n=== 🔍 TÌM KIẾM BÀI VIẾT VỚI TỪ KHÓA: \"" + keyword + "\" ===");
    List<Post> searchResults = dao.searchPosts(keyword, 1, 5);
    searchResults.forEach(System.out::println);
    System.out.println("→ Tổng kết quả tìm kiếm: " + dao.getTotalSearchPosts(keyword));

    // 5. Test lấy bài viết chi tiết theo ID
    int postIdToFind = 1; // đảm bảo có bài viết có ID = 1
    System.out.println("\n=== 📰 CHI TIẾT BÀI VIẾT CÓ ID = " + postIdToFind + " ===");
    Post post = dao.getPostById(postIdToFind);
    System.out.println(post != null ? post : "❌ Không tìm thấy bài viết");
}

    }