<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>403 - Access Denied</title>
</head>
<body>
    <h1>🚫 403 - Access Denied</h1>
    <p>You don't have permission to access this page.</p>
    <a href="<%= request.getContextPath() %>/home">← Back to Home</a>
</body>
</html>
