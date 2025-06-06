<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Set Password - Soft Skills</title>
        <link href="${pageContext.request.contextPath}/css/styles.css" rel="stylesheet">
    </head>
    <body>
    <!-- Navigation Bar -->
        <nav>
            <jsp:include page="/view/header.jsp"/>
        </nav>

        <!-- Banner -->
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

        <!-- Main Content -->
        <div class="main-content">
            <div class="password-form">
                <h2>Change PASSWORD</h2>
                <c:if test="${not empty errorMessage}">
                        <p style="color: red;">${errorMessage}</p>
                </c:if>
                <form action="${pageContext.request.contextPath}/auth" method="post">
                    <input type="hidden" name="action" value="changePassword">
                    <input type="password" name="password" placeholder="Old Password*" required><br>
                    <input type="password" name="newPassword" placeholder="New Password*" required><br>
                    <input type="password" name="confirmPassword" placeholder="Re-enter Password*" required><br>
                    <button type="submit">Change <span class="arrow-circle">→</span></button>
                </form>
            </div>
        </div>

        <!-- Footer -->
        <jsp:include page="/view/footer.jsp"/>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>