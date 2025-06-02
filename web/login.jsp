<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Đăng nhập</title>
    <meta charset="UTF-8">
</head>
<body>
    <h2>Đăng nhập</h2>
    <form action="login" method="post">
        <label for="username">Tên đăng nhập:</label>
        <input type="text" id="username" name="username" required><br><br>

        <label for="password">Mật khẩu:</label>
        <input type="password" id="password" name="password" required><br><br>

        <input type="submit" value="Đăng nhập">
    </form>

    <p style="color:red;"><%= request.getAttribute("error") == null ? "" : request.getAttribute("error") %></p>
</body>
</html>
