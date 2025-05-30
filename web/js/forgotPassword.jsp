<%-- 
    Document   : forgotPassword
    Created on : May 29, 2025, 9:29:38 AM
    Author     : maitu
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Forgot Password</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/login.css">
</head>
<body>
    <div class="wrapper">
        <%@ include file="Header.jsp" %>
         <!-- Banner -->
    <div class="banner">
      <h1>FORGOT PASSWORD</h1>
      <p><a href="#">Sign In</a> / Forgot Password</p>
    </div>
        
        <div class="main-content">
            <div class="form">
                <h2>FORGOT PASSWORD</h2>
                
                <c:if test="${not empty errorMessage}">
                    <div class="error-message">
                        ${errorMessage}
                    </div>
                </c:if>
                
                <c:if test="${not empty successMessage}">
                    <div class="success-message">
                        ${successMessage}
                    </div>
                </c:if>
                
                <form action="forgotpassword?action=sendlink" method="POST">
                    <div class="input-group">
                        <label>USERNAME *</label>
                        <input type="text" name="username" class="input-field" required>
                    </div>
                    
                    <div class="input-group">
                        <label>EMAIL *</label>
                        <input type="email" name="email" class="input-field" required>
                    </div>
                    
                    <button type="submit">SEND RESET LINK</button>
                </form>
                
                <div class="links">
                    <a href="login" class="link">Back to Sign In</a>
                </div>
            </div>
        </div>
        <%@ include file="Footer.jsp" %>
    </div>
</body>
</html>