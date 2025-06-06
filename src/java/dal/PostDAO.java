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
        String query = "SELECT id, title, thumbnail, updatedDate FROM Post ORDER BY updatedDate DESC OFFSET 0 ROWS FETCH NEXT ? ROWS ONLY";
        try {conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, limit);
            rs = ps.executeQuery();
            while (rs.next()) {
                Post post = new Post(
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getString("thumbnail"),
                    null, null, null, null,
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
            
        }
    }