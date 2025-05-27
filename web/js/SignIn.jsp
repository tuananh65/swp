<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Sign In - Soft Skills</title>
    
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" />
    
   <!-- Local CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/login.css" />

</head>
<body>
    <div class="wrapper">
    <!-- Include Header -->
    <%@ include file="Header.jsp" %>

    <!-- Banner -->
    <div class="banner">
      <h1>SIGN IN</h1>
      <p><a href="#">Home</a> / Sign In</p>
    </div>

    <!-- Main Content -->
    <div class="main-content">
        <!-- Form -->
        <div class="form">
            <h2>SIGN IN</h2>

            
            <!-- Error Message -->
            <c:if test="${not empty errorMessage}">
                <div class="error-message">
                    ${errorMessage}
                </div>
            </c:if>
            
            <!-- Form -->
            <form action="login" method="POST">
                <!-- Email Input -->
                <div class="input-group">
                    <label>EMAIL *</label>
                    <input type="email" name="email" class="input-field" placeholder="debra.burks@yahoo.com" required>
                </div>
                
                <!-- Password Input -->
                <div class="input-group">
                    <label>PASSWORD *</label>
                    <input type="password" name="password" class="input-field" placeholder="••••••••" required>
                </div>
                
                <!-- Links -->
                <div class="links">
                    <a href="#" class="link">Don't have an account? <strong>SIGN UP</strong></a>
                    <a href="#" class="link">FORGOT PASSWORD?</a>
                </div>
                
                <!-- Login Button -->
                <button type="submit">SIGN IN</button>
            </form>
        </div>

        
    </div>

    <!-- Include Footer -->
    <%@ include file="Footer.jsp" %>
    </div>
</body>
</html>