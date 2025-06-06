<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="model.User, model.Post, java.util.List"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Soft Skills - Blog</title>
        <link href="${pageContext.request.contextPath}/css/blog.css" rel="stylesheet">
    </head>
    <body>
        <!-- Navigation Bar -->
        <nav>
            <jsp:include page="/view/header.jsp"/>
            <div class="login-register-links" id="userSection">
                <%
                    User user = (User) session.getAttribute("user");
                    if (user == null) {
                %>
                    <a href="${pageContext.request.contextPath}/view/SignIn.jsp" class="custom-btn">Đăng nhập</a>
                    <a href="${pageContext.request.contextPath}/view/register.jsp" class="custom-btn">Đăng ký</a>
                <%
                    } else {
                %>
                    <span>Welcome, <%= user.getFullName() %></span>
                    <a href="${pageContext.request.contextPath}/logout" class="custom-btn">Đăng xuất</a>
                <%
                    }
                %>
            </div>
        </nav>
        <!-- Banner -->
        <div class="banner">
            <img src="${pageContext.request.contextPath}/images/Banner.jpg" alt="Banner">
            <div class="banner-text">
                <h1 class="banner-title">Blog</h1>
                <div class="banner-breadcrumb">
                    <table>
                        <tr>
                            <td>Home</td>
                            <td>Blog</td>
                        </tr>
                    </table>
                </div>
            </div>
        </div>

        <!-- Main Content -->
        <section class="main-content blog-page">
            <!-- Sidebar -->
            <aside class="sidebar">
                <!-- Search Bar -->
                <div class="search-section">
                    <form action="${pageContext.request.contextPath}/blog" method="get">
                        <input type="text" class="search-box" name="search" placeholder="Search..." value="${search}">
                        <button type="submit" class="search-icon">🔍</button>
                    </form>
                </div>

                <!-- Service Category -->
                <div class="category-section">
                    <h4>Service Category</h4>
                    <ul class="category-list" id="categoryList">
                        <%
                            String[] categories = {"Communication", "Emotional Intelligence", "Time Management", "Leadership", "Teamwork", "Problem Solving", "Adaptability"};
                            String selectedCategory = (String) request.getAttribute("selectedCategory");
                            for (String category : categories) {
                                String activeClass = category.equals(selectedCategory) ? "active" : "";
                        %>
                            <li class="category-item <%= activeClass %>">
                                <a href="${pageContext.request.contextPath}/blog?category=<%= category %>"><%= category %></a>
                                <span class="category-arrow">▶</span>
                            </li>
                        <%
                            }
                        %>
                    </ul>
                </div>

                <!-- Recent Posts -->
                <div class="recent-posts-section">
                    <h4>Recent Post</h4>
                    <%
                        List<Post> recentPosts = (List<Post>) request.getAttribute("recentPosts");
                        if (recentPosts != null) {
                            for (Post recentPost : recentPosts) {
                    %>
                        <div class="recent-post">
                            <img src="${pageContext.request.contextPath}/<%= recentPost.getThumbnail() %>" alt="Recent Post Thumbnail" class="recent-post-thumbnail">
                            <div class="recent-post-info">
                                <h5><a href="${pageContext.request.contextPath}/post?id=<%= recentPost.getId() %>"><%= recentPost.getTitle() %></a></h5>
                                <p><%= recentPost.getUpdatedDate() %></p>
                            </div>
                        </div>
                    <%
                            }
                        }
                    %>
                </div>

                <!-- Popular Tags -->
                <div class="tags-section">
                    <h4>Popular Tag:</h4>
                    <div class="tags">
                        <span class="tag">Balance</span>
                        <span class="tag">Coaching</span>
                        <span class="tag">Motivation</span>
                        <span class="tag">Courses</span>
                        <span class="tag">Life Guide</span>
                        <span class="tag">Strategy</span>
                        <span class="tag">Education</span>
                        <span class="tag">Coach</span>
                    </div>
                </div>
            </aside>

            <!-- Posts Section -->
            <div class="posts-container">
                <%
                    List<Post> posts = (List<Post>) request.getAttribute("posts");
                    if (posts != null && !posts.isEmpty()) {
                        for (Post post : posts) {
                %>
                    <article class="post">
                        <a href="${pageContext.request.contextPath}/post?id=<%= post.getId() %>">
                            <img src="${pageContext.request.contextPath}/<%= post.getThumbnail() %>" alt="Post Thumbnail" class="post-thumbnail">
                            <div class="post-meta">
                                <span class="post-date"><%= post.getUpdatedDate() %></span>
                                <span class="post-author"><%= post.getAuthor() %></span>
                                <span class="post-category"><%= post.getCategory() %></span>
                            </div>
                            <h3 class="post-title"><%= post.getTitle() %></h3>
                            <p class="post-brief"><%= post.getBriefInfo() %></p>
                            <button class="read-more">Read More</button>
                        </a>
                    </article>
                <%
                        }
                    } else {
                %>
                    <p>No posts found.</p>
                <%
                    }
                %>

                <!-- Pagination -->
                <%
                    Integer currentPage = (Integer) request.getAttribute("currentPage");
                    Integer totalPages = (Integer) request.getAttribute("totalPages");
                    String search = (String) request.getAttribute("search");
                    String category = (String) request.getAttribute("selectedCategory");
                    StringBuilder baseUrl = new StringBuilder("/blog");
                    if (search != null && !search.trim().isEmpty()) {
                        baseUrl.append("?search=").append(java.net.URLEncoder.encode(search, "UTF-8"));
                    } else if (category != null && !category.isEmpty()) {
                        baseUrl.append("?category=").append(java.net.URLEncoder.encode(category, "UTF-8"));
                    }
                    if (totalPages != null && totalPages > 1) {
                %>
                    <div class="pagination">
                        <%
                            for (int i = 1; i <= totalPages; i++) {
                                String activeClass = (i == currentPage) ? "active" : "";
                                String pageUrl = baseUrl.toString();
                                if (baseUrl.indexOf("?") > -1) {
                                    pageUrl += "&page=" + i;
                                } else {
                                    pageUrl += "?page=" + i;
                                }
                        %>
                            <a href="${pageContext.request.contextPath}<%= pageUrl %>">
                                <button class="page-btn <%= activeClass %>"><%= i %></button>
                            </a>
                        <%
                            }
                        %>
                    </div>
                <%
                    }
                %>
            </div>
        </section>

        <!-- Footer -->
        <footer class="footer">
            <div class="footer-top">
                <div class="footer-block">
                    <span class="footer-icon">📍</span>
                    <span class="footer-label">Address:</span>
                    <span class="footer-info">1925 Bogness Street</span>
                </div>
                <div class="footer-block">
                    <span class="footer-icon">📞</span>
                    <span class="footer-label">Phone:</span>
                    <span class="footer-info">(00) 875 784 568</span>
                </div>
                <div class="footer-block">
                    <span class="footer-icon">📧</span>
                    <span class="footer-label">Email:</span>
                    <span class="footer-info">info@gmail.com</span>
                </div>
            </div>
            <div class="footer-content">
                <div class="footer-section">
                    <div class="logo">
                        <img src="${pageContext.request.contextPath}/images/SoftSkillWhite.png" alt="Soft Skills Logo">
                    </div>
                    <p>Interdum velit laoreet id donec ultrices tincidunt arcu tortor aliqua mi facilisi cras fermentum odio eu.</p>
                    <div class="social-icons">
                        <a href="#"><i class="icon">F</i></a>
                        <a href="#"><i class="icon">T</i></a>
                        <a href="#"><i class="icon">I</i></a>
                        <a href="#"><i class="icon">L</i></a>
                    </div>
                </div>
                <div class="footer-section">
                    <h4>Our Services:</h4>
                    <ul>
                        <li><a href="#">Web Design</a></li>
                        <li><a href="#">UI/UX Design</a></li>
                        <li><a href="#">Development</a></li>
                        <li><a href="#">Marketing</a></li>
                        <li><a href="#">Digital Marketing</a></li>
                        <li><a href="#">Blog News</a></li>
                    </ul>
                </div>
                <div class="footer-section">
                    <h4>Gallery</h4>
                    <div class="gallery">
                        <img src="${pageContext.request.contextPath}/images/recent-post-thumbnail.jpg" alt="Gallery Image">
                        <img src="${pageContext.request.contextPath}/images/recent-post-thumbnail-2.jpg" alt="Gallery Image">
                        <img src="${pageContext.request.contextPath}/images/recent-post-thumbnail-3.jpg" alt="Gallery Image">
                        <img src="${pageContext.request.contextPath}/images/recent-post-thumbnail-4.jpg" alt="Gallery Image">
                    </div>
                </div>
                <div class="footer-section">
                    <h4>Subscribe</h4>
                    <input type="email" placeholder="Enter your email">
                    <button class="subscribe-btn">SUBSCRIBE NOW</button>
                </div>
            </div>
        </footer>
        <jsp:include page="/view/footer.jsp"/>            
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>