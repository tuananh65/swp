<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="model.User, model.Post, java.util.List"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Soft Skills - <%= ((Post) request.getAttribute("post")).getTitle() %></title>
        <link href="${pageContext.request.contextPath}/css/blog.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
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
                <h1 class="banner-title">Post Details</h1>
                <div class="banner-breadcrumb">
                    <table>
                        <tr>
                            <td>Home</td>
                            <td>Post Details</td>
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
                        <button type="submit" class="search-icon"><i class="bi bi-search"></i></button>
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
                                <span class="category-arrow"><i class="bi bi-caret-right-fill"></i></span>
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
                                <p><i class="bi bi-calendar-week"></i><%= recentPost.getUpdatedDate() %></p>
                                <h5><a href="${pageContext.request.contextPath}/post?id=<%= recentPost.getId() %>"><%= recentPost.getTitle() %></a></h5>
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
            
            <div class="posts-container">    
                <%
                    Post post = (Post) request.getAttribute("post");
                    if (post != null) {
                %>
                    <article class="post">
                        <img src="${pageContext.request.contextPath}/<%= post.getThumbnail() %>" alt="Post Thumbnail" class="post-thumbnail">
                        <div class="post-meta">
                            <span class="post-date"><i class="bi bi-calendar-week"></i><%= post.getUpdatedDate() %></span>
                            <span class="post-author"><i class="bi bi-person-fill"></i><%= post.getAuthor() %></span>
                            <span class="post-category"><i class="bi bi-tag"></i><%= post.getCategory() %></span>
                        </div>
                        <h3 class="post-title"><%= post.getTitle() %></h3>
                        <div class="post-content"><%= post.getContent() %></div>
                    </article>
                <%
                    } else {
                %>
                    <p>Post not found.</p>
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
                        <img src="${pageContext.request.contextPath}/images/gallery-img-1.jpg" alt="Gallery Image">
                        <img src="${pageContext.request.contextPath}/images/gallery-img-2.jpg" alt="Gallery Image">
                        <img src="${pageContext.request.contextPath}/images/gallery-img-3.jpg" alt="Gallery Image">
                        <img src="${pageContext.request.contextPath}/images/gallery-img-4.jpg" alt="Gallery Image">
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