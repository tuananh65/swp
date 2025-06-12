<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="model.User, model.Post"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Soft Skills - <%= ((Post) request.getAttribute("post")).getTitle() %></title>
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
            <%
                Post post = (Post) request.getAttribute("post");
                if (post != null) {
            %>
                <article class="post">
                    <img src="${pageContext.request.contextPath}/<%= post.getThumbnail() %>" alt="Post Thumbnail" class="post-thumbnail">
                    <div class="post-meta">
                        <span class="post-date"><%= post.getUpdatedDate() %></span>
                        <span class="post-author"><%= post.getAuthor() %></span>
                        <span class="post-category"><%= post.getCategory() %></span>
                    </div>
                    <h3 class="post-title"><%= post.getTitle() %></h3>
                    <p class="post-brief"><%= post.getBriefInfo() %></p>
                    <div class="post-content">
                        <%= post.getContent() %>
                    </div>
                </article>
            <%
                } else {
            %>
                <p>Post not found.</p>
            <%
                }
            %>
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