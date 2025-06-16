<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.User" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%
    model.User user = (model.User) session.getAttribute("currentUser");
    if (user == null) {
        response.sendRedirect("view/SignIn.jsp");
        return;
    }
%>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <title>Soft Skills & Time Management - Nền tảng học kỹ năng mềm</title>
        <meta
            name="description"
            content="Nền tảng học trực tuyến giúp bạn phát triển kỹ năng mềm hiệu quả"
            />
        <link href="${pageContext.request.contextPath}/css/styles.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/css/profile.css" rel="stylesheet">

        <link rel="preconnect" href="https://fonts.googleapis.com" />
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
        <link
            href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap"
            rel="stylesheet"
            />
        <link
            rel="stylesheet"
            href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css"
            />
    </head>
    <body>
        <!-- Navigation Bar -->
        <nav>
            <jsp:include page="/default/header.jsp"/>
            <div class="login-register-links" id="userSection">
                <a href="${pageContext.request.contextPath}/view/changePassword.jsp" class="custom-btn">Change Password</a>
                <a href="${pageContext.request.contextPath}/profile?id=3" class="custom-btn">Profile</a>
                <a href="${pageContext.request.contextPath}/logout" class="custom-btn">Đăng xuất</a>
            </div>
        </nav>

        <!-- Hero Banner -->
        <section class="hero-banner">
            <div class="hero-content">
                <div class="hero-overlay"></div>
                <img class="hero-image" src="${pageContext.request.contextPath}/images/Banner.jpg" alt="Banner">
                <div class="hero-text">
                    <h1 style="color: #000000;">USER PROFILE</h1>
                </div>
            </div>
        </section>

        <!-- Sidebar Toggle -->
        <button class="sidebar-toggle" id="sidebar-toggle" aria-label="Mở sidebar">
            <i class="fas fa-bars"></i>
        </button>

        <!-- Main Content -->
        <main class="main-content">
            <!-- Sidebar -->
            <aside class="sidebar" id="sidebar">
                <div class="sidebar-content">
                    <div class="user-info">
                        <div class="user-avatar">
                            <img src="${pageContext.request.contextPath}${user.avatarUrl}" alt="User Avatar" />
                            <div class="status-indicator"></div>
                        </div>
                            <div class="user-details">
                                <h3>${user.fullName}</h3>
                                <p><i class="fas fa-phone"></i> ${user.phone}</p>
                                <p><i class="fas fa-map-marker-alt"></i> ${user.address}</p>
                                <p><i class="fas fa-envelope"></i> ${user.email}</p>

                        </div>
                    </div>

                    <nav class="sidebar-nav">
                        <div class="nav-section">
                            <div class="dropdown-item">
                                <button class="dropdown-btn">
                                    <i class="fas fa-user"></i>
                                    <span>Hồ sơ cá nhân</span>
                                    <i class="fas fa-chevron-right dropdown-arrow"></i>
                                </button>
                                <div class="dropdown-content">
                                    <a href="#">Thông tin cá nhân</a>
                                    <a href="#">Cài đặt tài khoản</a>
                                    <a href="#">Bảo mật</a>
                                </div>
                            </div>
                            <div class="dropdown-item">
                                <button class="dropdown-btn">
                                    <i class="fas fa-book"></i>
                                    <span>Khóa học của tôi</span>
                                    <i class="fas fa-chevron-right dropdown-arrow"></i>
                                </button>
                                <div class="dropdown-content">
                                    <a href="#">Đang học</a>
                                    <a href="#">Đã hoàn thành</a>
                                    <a href="#">Yêu thích</a>
                                </div>
                            </div>
                            <div class="dropdown-item">
                                <button class="dropdown-btn">
                                    <i class="fas fa-chart-line"></i>
                                    <span>Tiến độ học tập</span>
                                    <i class="fas fa-chevron-right dropdown-arrow"></i>
                                </button>
                                <div class="dropdown-content">
                                    <a href="#">Thống kê</a>
                                    <a href="#">Chứng chỉ</a>
                                    <a href="#">Thành tích</a>
                                </div>
                            </div>
                            <div class="dropdown-item">
                                <button class="dropdown-btn">
                                    <i class="fas fa-cog"></i>
                                    <span>Cài đặt</span>
                                    <i class="fas fa-chevron-right dropdown-arrow"></i>
                                </button>
                                <div class="dropdown-content">
                                    <a href="#">Thông báo</a>
                                    <a href="#">Ngôn ngữ</a>
                                    <a href="#">Giao diện</a>
                                </div>
                            </div>
                        </div>
                    </nav>
                </div>
            </aside>

            <!-- User Profile Section -->
            <section class="profile-section">
                <div class="profile-container">
                    <div class="profile-header">
                        <h2>Hồ sơ cá nhân</h2>
                        <p>Cập nhật thông tin cá nhân của bạn</p>
                    </div>

                    <div class="profile-content">
                        <div class="profile-left">
                            <!-- INPUT FILE và AVATAR -->
                            <div class="avatar-upload">
                                <div class="avatar-preview">
                                    <img
                                        src="${pageContext.request.contextPath}${user.avatarUrl}" alt="Avatar" id="avatar-preview"/>
                                    <div class="upload-overlay">
                                        <i class="fas fa-camera"></i>
                                    </div>
                                </div>
                                <input type="file" id="avatar-input" accept="image/*" hidden />
                                <button type="button" class="upload-btn" onclick="document.getElementById('avatar-input').click()">
                                    Tải ảnh lên
                                </button>
                            </div>

                            <div class="social-links" style="margin: 0 auto">
                                <div class="social-icons">
                                    <a href="#" class="social-icon facebook" aria-label="Facebook">
                                        <i class="fab fa-facebook-f"></i>
                                    </a>
                                    <a href="#" class="social-icon instagram" aria-label="Instagram">
                                        <i class="fab fa-instagram"></i>
                                    </a>
                                    <a href="#" class="social-icon linkedin" aria-label="LinkedIn">
                                        <i class="fab fa-linkedin-in"></i>
                                    </a>
                                    <a href="#" class="social-icon twitter" aria-label="Twitter">
                                        <i class="fab fa-twitter"></i>
                                    </a>
                                </div>
                            </div>

                            <div class="contact-card">
                                <h4>Thông tin liên hệ</h4>
                                <div class="contact-item">
                                    <i class="fas fa-phone"></i>
                                    <span><c:out value="${user.getPhone()}" /></span>
                                </div>
                                <div class="contact-item">
                                    <i class="fas fa-map-marker-alt"></i>
                                    <span><c:out value="${user.getAddress()}" /></span>
                                </div>
                                <div class="contact-item">
                                    <i class="fas fa-envelope"></i>
                                    <span><c:out value="${user.getEmail()}" /></span>
                                </div>
                            </div>
                        </div>

                        <div class="profile-right">
                            <form class="profile-form" id="profile-form" action="${pageContext.request.contextPath}/updateUser" method="post">
                                <input type="hidden" name="userID" value="${user.getUserId()}" />
                                <h1>ID: ${user.getUserId()}</h1>
                                <div class="form-row">
                                    <div class="form-group">
                                        <label for="userFullName">Full Name *</label>
                                        <input type="text" id="userFullName" name="userFullName" placeholder="Enter Full Name" required value="${user.getFullName()}" />
                                    </div>
                                    <div class="form-group">
                                        <label for="userAddress">Address *</label>
                                        <input type="text" id="userAddress" name="userAddress" placeholder="Enter Address" required value="${user.getAddress()}" />
                                    </div>
                                </div>
                                <div class="form-row">
                                    <div class="form-group">
                                        <label for="userPassword">Password *</label>
                                        <input type="password" id="userPassword" name="userPassword" placeholder="Enter password" required value="${user.getPassword()}" />
                                    </div>
                                    <div class="form-group">
                                        <label for="userPhone">Phone *</label>
                                        <input type="tel" id="userPhone" name="userPhone" placeholder="Enter phone" required value="${user.getPhone()}" />
                                    </div>
                                </div>
                                <div class="form-row">
                                    <div class="form-group">
                                        <label for="dateOfBirth">Ngày sinh *</label>
                                        <input type="date" id="dateOfBirth" name="dateOfBirth" required value="<fmt:formatDate value='${user.getDateOfBirth()}' pattern='yyyy-MM-dd' />" />
                                    </div>
                                    <div class="form-group">
                                        <label for="userGender">Giới tính</label>
                                        <select id="userGender" name="userGender">
                                            <option value="" ${empty user.getGender() ? "selected" : ""}>Chọn giới tính</option>
                                            <option value="male" ${user.getGender() == "male" ? "selected" : ""}>Nam</option>
                                            <option value="female" ${user.getGender() == "female" ? "selected" : ""}>Nữ</option>
                                            <option value="other" ${user.getGender() == "other" ? "selected" : ""}>Khác</option>
                                        </select>
                                    </div>
                                </div>
                                <div class="form-actions">
                                    <button type="button" class="btn-secondary" onclick="window.history.back()">Hủy</button>
                                    <button type="submit" class="btn-primary">
                                        <i class="fas fa-save"></i> Lưu thay đổi
                                    </button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </section>
        </main>

        <!-- Scripts -->
        <script src="${pageContext.request.contextPath}/js/profile.js" defer></script>
        <script>
            document.getElementById('avatar-input').addEventListener('change', function () {
                const file = this.files[0];
                if (!file) return;

                const formData = new FormData();
                formData.append("avatar", file);
                formData.append("userID", "${user.getUserId()}");

                const reader = new FileReader();
                reader.onload = function (e) {
                    document.getElementById("avatar-preview").src = e.target.result;
                };
                reader.readAsDataURL(file);

                fetch("${pageContext.request.contextPath}/uploadAvatar", {
                    method: "POST",
                    body: formData
                }).then(response => {
                    if (!response.ok) {
                        alert("Upload thất bại!");
                    } else {
                        window.location.reload(); // Reload to reflect updated avatar
                    }
                });
            });
        </script>
        <script>
            document.addEventListener('DOMContentLoaded', function () {
                const sidebar = document.getElementById('sidebar');
                const sidebarToggle = document.getElementById('sidebar-toggle');

                sidebarToggle.addEventListener('click', function () {
                    sidebar.classList.toggle('active');
                });
            });
        </script>
        <jsp:include page="/default/footer.jsp"/>            
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>