<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="model.Users"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Admin Dashboard</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/css/styles.css" rel="stylesheet">
    </head>
    <body>
        <nav class="navbar navbar-expand-lg navbar-dark">
            <div class="container-fluid">
                <a class="navbar-brand" href="#">
                    <img src="${pageContext.request.contextPath}/images/logo1.png" alt="Driving Skills Logo" class="img-fluid">
                </a>
                <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
                    <span class="navbar-toggler-icon"></span>
                </button>
                <div class="collapse navbar-collapse" id="navbarNav">
                    <ul class="navbar-nav mx-auto">
                        <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/index.jsp">Trang chủ</a></li>
                        <li class="nav-item dropdown">
                            <a class="nav-link dropdown-toggle" href="#" id="navbarDropdownCourses" role="button" data-bs-toggle="dropdown" aria-expanded="false">Khóa học</a>
                            <ul class="dropdown-menu" aria-labelledby="navbarDropdownCourses">
                                <li><a class="dropdown-item" href="${pageContext.request.contextPath}/student?action=viewCourses">Đăng ký khóa học</a></li>
                                <li><a class="dropdown-item" href="${pageContext.request.contextPath}/student?action=viewRegisteredCourses">Khóa học đã đăng ký</a></li>
                            </ul>
                        </li>
                        <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/student?action=schedule">Lịch học</a></li>
                        <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/student?action=registerExam">Lịch thi</a></li>
                        <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/student?action=viewResults">Xem Kết quả</a></li>
                        <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/student?action=viewCertificates">Xem Chứng chỉ</a></li>
                        <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/student?action=viewNotifications">Thông báo</a></li>
                    </ul>
                    <div class="dropdown">
                        <%
                            Users user = (Users) session.getAttribute("user");
                            if (user != null) {
                        %>
                        <span style="color: white; cursor: pointer;"><%= user.getEmail() %></span>
                        <div class="dropdown-menu">
                            <a href="${pageContext.request.contextPath}/auth?action=updateUserInfo">Cập nhật thông tin</a>
                            <a href="${pageContext.request.contextPath}/view/resetPassword.jsp?email=<%= user.getEmail() %>">Cập nhật mật khẩu</a>
                            <a href="${pageContext.request.contextPath}/auth?action=logout">Đăng xuất</a>
                        </div>
                        <%
                            } else {
                                response.sendRedirect("../view/login.jsp");
                            }
                        %>
                    </div>
                </div>
            </div>
        </nav>
            <div class="container content-section">
                <div class="login-form">
                    <h2>Cập nhật thông tin cá nhân</h2>
                    <form action="${pageContext.request.contextPath}/auth" method="post">
                        <input type="hidden" name="action" value="updateUserInfo">
                        <%
                            if (user != null) {
                        %>
                        <div>
                            <label for="fullName">Họ và Tên</label>
                            <input type="text" id="fullName" name="fullName" value="<%= user.getFullName() %>" required>
                        </div>
                        <div>
                            <label for="className">Tên Lớp</label>
                            <input type="text" id="className" name="className" value="<%= user.getClassName() %>">
                        </div>
                        <div>
                            <label for="school">Trường Học</label>
                            <input type="text" id="school" name="school" value="<%= user.getSchool() %>">
                        </div>
                        <div>
                            <label for="phone">Số Điện Thoại</label>
                            <input type="text" id="phone" name="phone" value="<%= user.getPhone() %>" required>
                        </div>
                        <button type="submit">Cập nhật</button>
                        <div class="link-container">
                            <a href="${pageContext.request.contextPath}/index.jsp">Quay lại trang chủ</a>
                        </div>
                        <%
                            } else {
                        %>
                        <p>Bạn chưa đăng nhập! Vui lòng <a href="../view/login.jsp">đăng nhập</a>.</p>
                        <%
                            }
                        %>
                    </form>
                </div>
            </div>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>