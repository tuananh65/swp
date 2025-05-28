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
                <a href="#">Blog</a>
            </div>
            <a href="#">Contact</a>
        </div>
        <div class="icons">
            <span class="icon">🔍</span>
            <span class="icon">☰</span>
        </div>
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

    <!-- Main Content -->
    <div class="main-content">
        <div class="signup-form">
            <h2>SIGN IN</h2>
            
            <!-- Error Message (from Code 1) -->
            <c:if test="${not empty errorMessage}">
                <p style="color: red;">${errorMessage}</p>
            </c:if>
            
            <!-- Form (from Code 1, styled like Code 2) -->
            <form action="${pageContext.request.contextPath}/auth" method="post">
                <input type="hidden" name="action" value="login">
                <input type="email" name="email" placeholder="debra.burks@yahoo.com" required>
                <input type="password" name="password" placeholder="••••••••" required>
                <div style="display: flex; justify-content: space-between; align-items: center; margin: 10px 0;">
                    <a href="#" style="color: #ff0000; text-decoration: none; font-size: 13px;">FORGOT PASSWORD?</a>
                </div>
                <button type="submit">SIGN IN <span class="arrow-circle">→</span></button>
            </form>
            <p>Don't have an account? <a href="#" style="color: #6b48ff; text-decoration: none;">SIGN UP</a></p>
        </div>
        <div class="signup-image">
            <img src="${pageContext.request.contextPath}/images/signup-image.jpg" alt="Sign In Image">
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
        <jsp:include page="/view/footer.jsp"/>
</body>
</html>