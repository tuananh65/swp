<%-- 
    Document   : resetPassword
    Created on : May 29, 2025, 9:30:01 AM
    Author     : maitu
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Reset Password</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/login.css">
</head>
<body>
    <div class="wrapper">
        <%@ include file="Header.jsp" %>
        <div class="banner">
      <h1>RESET PASSWORD</h1>
      <p><a href="#">Forgot Password</a> / Reset Password</p>
    </div>
        
        <div class="main-content">
            <div class="form">
                <h2>RESET PASSWORD</h2>
                
                <c:if test="${not empty errorMessage}">
                    <div class="error-message">
                        ${errorMessage}
                    </div>
                </c:if>
                
                <form action="resetpassword" method="POST">
                    <input type="hidden" name="token" value="${param.token}">
                    
                    <div class="input-group">
                        <label>NEW PASSWORD *</label>
                        <input type="password" name="newPassword" class="input-field" required>
                    </div>
                    
                    <div class="input-group">
                        <label>CONFIRM PASSWORD *</label>
                        <input type="password" name="confirmPassword" class="input-field" required>
                    </div>
                    
                    <button type="submit">RESET PASSWORD</button>
                </form>
            </div>
        </div>
        
        <%@ include file="Footer.jsp" %>
    </div>
</body>
</html>