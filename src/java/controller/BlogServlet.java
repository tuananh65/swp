package controller;

import dal.PostDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Post;
import model.User;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BlogServlet extends HttpServlet {
    private static final int PAGE_SIZE = 3; // 3 posts per page

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostDAO postDAO = new PostDAO();
        String pageStr = request.getParameter("page");
        String category = request.getParameter("category");
        String search = request.getParameter("search");

        int page = 1;
        if (pageStr != null && !pageStr.isEmpty()) {
            try {
                page = Integer.parseInt(pageStr);
            } catch (NumberFormatException e) {
                page = 1;
            }
        }

        List<Post> posts = null;
        int totalPosts = 0;
        if (search != null && !search.trim().isEmpty()) {
            // Handle search
            posts = postDAO.searchPosts(search, page, PAGE_SIZE);
            totalPosts = postDAO.getTotalSearchPosts(search);
            request.setAttribute("search", search);
        } else if (category != null && !category.isEmpty()) {
            try {
                // Handle category filter
                posts = postDAO.getPostsByCategory(category, page, PAGE_SIZE);
                totalPosts = postDAO.getTotalPostsByCategory(category);
                request.setAttribute("selectedCategory", category);
            } catch (SQLException ex) {
                Logger.getLogger(BlogServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            // Default: all posts
            posts = postDAO.getPaginatedPosts(page, PAGE_SIZE);
            try {
                totalPosts = postDAO.getTotalPosts();
            } catch (SQLException ex) {
                Logger.getLogger(BlogServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        // Get recent posts for sidebar
        List<Post> recentPosts = postDAO.getRecentPosts(4);

        // Calculate total pages
        int totalPages = (int) Math.ceil((double) totalPosts / PAGE_SIZE);

        // Set attributes for JSP
        request.setAttribute("posts", posts);
        request.setAttribute("recentPosts", recentPosts);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);

        // Forward to JSP
        request.getRequestDispatcher("/blog.jsp").forward(request, response);
        
    }
}