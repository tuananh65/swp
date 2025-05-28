<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="model.User"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Soft Skills</title>
        <link href="${pageContext.request.contextPath}/css/styles.css" rel="stylesheet">
    </head>
    <body>
        <!-- Navigation Bar -->
        <nav class="navbar">
                <div class="logo">
                    <img src="${pageContext.request.contextPath}/images/SoftSkill.png" alt="Soft Skills Logo">
                </div>
                <div class="nav-list">
                    <div class="dropdown">
                      <a href="#">Home</a>
                    </div>
                    <a href="#">About Us</a>
                    <div class="dropdown">
                      <a href="#">Courses</a>
                    </div>
                    <div class="dropdown">
                      <a href="#">Pages</a>
                    </div>
                    <div class="dropdown">
                      <a href="${pageContext.request.contextPath}/blog">Blog</a>
                    </div>
                    <a href="#">Contact</a>
                <div class="login-register-links" id="userSection">
                    <%
                        User user = (User) session.getAttribute("user");
                        if (user == null) {
                    %>
                        <a href="${pageContext.request.contextPath}/view/login.jsp" class="custom-btn">Đăng nhập</a>
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
            <div class="icons">
                <span class="icon">🔍</span>
                <span class="icon">☰</span>
            </div>
        </nav>
    
        <div class="banner">
            <img src="${pageContext.request.contextPath}/images/Banner.jpg" alt="Banner">
            <div class="banner-text">
                <h1 class="banner-title">Sign Up</h1>
                <div class="banner-breadcrumb">
                    <table>
                        <tr>
                            <td>Home</td>
                            <td>Sign Up</td>
                        </tr>
                    </table>
                </div>
            </div>
        </div>        

    <!-- Rest of your JSP content -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script> 
        <jsp:include page="view/footer.jsp" />
    </body>
</html>





