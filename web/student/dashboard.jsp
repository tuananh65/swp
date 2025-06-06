<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="model.User, java.util.List"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Student - Dashboard</title>
        <link href="${pageContext.request.contextPath}/css/blog.css" rel="stylesheet">
    </head>
    <body>
        <!-- Navigation Bar -->
        <nav>
            <jsp:include page="/view/header.jsp"/>
            <div class="login-register-links" id="userSection">
                <%
                    User user = (User) session.getAttribute("currentUser");
                    if (user == null) {
                %>
                    
                <%
                    } else {
                %> 
                    <a href="${pageContext.request.contextPath}/view/changePassword.jsp" class="custom-btn">Change Password</a>
                    <a href="${pageContext.request.contextPath}/profile?id=<%= user.getUserId() %>" class="custom-btn">Profile</a>
                    <a href="${pageContext.request.contextPath}/logout" class="custom-btn">Đăng xuất</a>
                <%
                    }
                %>
            </div>
        </nav>
        <div class="banner">
            <img src="${pageContext.request.contextPath}/images/Banner.jpg" alt="Banner">
            <div class="banner-text">
                <h1 class="banner-title">Student</h1>
                <div class="banner-breadcrumb">
                    <table>
                        <tr>
                            <td>Home</td>
                            <td></td>
                        </tr>
                    </table>
                </div>
            </div>
        </div>
        <jsp:include page="/view/footer.jsp"/>            
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>