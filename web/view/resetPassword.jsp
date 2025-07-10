<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Sign In - Soft Skills</title>

        <!-- Font Awesome (retained from Code 1) -->
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">

        <!-- Local CSS (from Code 2) -->
        <link href="${pageContext.request.contextPath}/css/styles.css" rel="stylesheet">
    </head>
    <body>
        <!-- Navigation Bar (from Code 2) -->
        <nav>
            <jsp:include page="/default/header.jsp"/>
        </nav>

    <!-- Banner (from Code 2) -->
        <div class="banner">
            <img src="${pageContext.request.contextPath}/images/Banner.jpg" alt="Banner">
            <div class="banner-text">
                <h1 class="banner-title">Sign In</h1>
                <div class="banner-breadcrumb">
                    <table>
                        <tr>
                            <td>Home</td>
                            <td>Sign In</td>
                        </tr>
                    </table>
                </div>
            </div>
        </div>

            <div class="main-content">
                <div class="password-form">
                    <h2>RESET PASSWORD</h2>
                    <c:if test="${not empty errorMessage}">
                        <p style="color: red;">${errorMessage}</p>
                    </c:if>

                    <form action="${pageContext.request.contextPath}/auth" method="post">
                        <input type="hidden" name="action" value="resetpassword">
                        <input type="hidden" name="token" value="${param.token}">
                        <input type="password" name="newPassword" placeholder="New Password*" required><br>
                        <input type="password" name="confirmPassword" placeholder="Re-enter Password*" required><br>
                        <button type="submit">CHANGE <span class="arrow-circle">→</span></button>
                    </form>
                </div>
            </div>

            <!-- Footer -->
        <jsp:include page="/default/footer.jsp"/>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>