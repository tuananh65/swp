<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Sign Up - Soft Skills</title>
        <link href="${pageContext.request.contextPath}/css/styles.css" rel="stylesheet">
    </head>
    <body>
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
            <div class="signup-form">
                <h2>SIGN UP</h2>
                <c:if test="${not empty errorMessage}">
                    <p style="color: red;">${errorMessage}</p>
                </c:if>
                <c:if test="${not empty successMessage}">
                    <p style="color: green;">${successMessage}</p>
                </c:if>
                <form action="${pageContext.request.contextPath}/auth" method="post">
                    <input type="hidden" name="action" value="register">
                    <input type="text" name="username" placeholder="User Name*" required><br>
                    <input type="text" name="fullName" placeholder="Full Name*" required><br>
                    <select name="gender" required>
                        <option value="">Gender*</option>
                        <option value="male">Male</option>
                        <option value="female">Female</option>
                    </select><br>
                    <input type="email" name="email" placeholder="Email*" required><br>
                    <input type="tel" name="phone" placeholder="Phone*" required><br>
                    <input type="password" name="password" placeholder="Password*" required><br>
                    <button type="submit">Sign Up <span class="arrow-circle">→</span></button>
                </form>
                <p>Already have an account? <a href="${pageContext.request.contextPath}/view/SignIn.jsp">Sign in</a></p>
            </div>
            <div class="signup-image">
                <img src="${pageContext.request.contextPath}/images/signup-image.jpg" alt="Sign Up Image">
            </div>
        </div>
            
     
        <!-- Footer -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
        <jsp:include page="/view/footer.jsp"/>
    </body>
</html>






