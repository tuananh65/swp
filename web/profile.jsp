<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.User" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%
    model.User user = (model.User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect("login.jsp");
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
        <nav class="navbar" id="navbar">
            <div class="nav-container">
                <div class="logo">
                    <img src="images/logo.jpg" alt="Soft Skills Logo">
                </div>
                <div class="nav-menu" id="nav-menu">
                    <div class="nav-item dropdown">
                        <a href="#" class="nav-link"
                           >Trang chủ <i class="fas fa-chevron-down"></i
                            ></a>
                        <div class="dropdown-content">
                            <a href="#">Trang chủ 1</a>
                            <a href="#">Trang chủ 2</a>
                        </div>
                    </div>
                    <a href="#" class="nav-link">Về chúng tôi</a>
                    <div class="nav-item dropdown">
                        <a href="#" class="nav-link"
                           >Khóa học <i class="fas fa-chevron-down"></i
                            ></a>
                        <div class="dropdown-content">
                            <a href="#">Kỹ năng giao tiếp</a>
                            <a href="#">Quản lý thời gian</a>
                            <a href="#">Lãnh đạo</a>
                        </div>
                    </div>
                    <div class="nav-item dropdown">
                        <a href="#" class="nav-link"
                           >Trang <i class="fas fa-chevron-down"></i
                            ></a>
                        <div class="dropdown-content">
                            <a href="#">Hồ sơ</a>
                            <a href="#">Khóa học của tôi</a>
                        </div>
                    </div>
                    <a href="#" class="nav-link">Blog</a>
                    <a href="#" class="nav-link">Liên hệ</a>
                </div>
                <div class="nav-actions">
                    <button class="search-btn" aria-label="Tìm kiếm">
                        <i class="fas fa-search"></i>
                    </button>
                    <button class="menu-toggle" id="menu-toggle" aria-label="Menu">
                        <span></span>
                        <span></span>
                        <span></span>
                    </button>
                </div>
            </div>
        </nav>

        <!-- Hero Banner -->

        <section class="hero-banner">
            <div class="hero-content">
                <div class="hero-overlay"></div>
                <img class="hero-image" src="${pageContext.request.contextPath}/images/banner.jpg" alt="Banner">
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
                            <img src="${pageContext.request.contextPath}${user.userAvatarUrl}" alt="User Avatar" />
                            <div class="status-indicator"></div>
                        </div>
                            <div class="user-details">
                                <h3>${user.userFullName}</h3>
                                <p><i class="fas fa-phone"></i> ${user.userPhone}</p>
                                <p><i class="fas fa-map-marker-alt"></i> ${user.userAddress}</p>
                                <p><i class="fas fa-envelope"></i> ${user.userEmail}</p>

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
            src="${pageContext.request.contextPath}${user.userAvatarUrl}"
            alt="Avatar"
            id="avatar-preview"
        />
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
                                    <a
                                        href="#"
                                        class="social-icon facebook"
                                        aria-label="Facebook"
                                        >
                                        <i class="fab fa-facebook-f"></i>
                                    </a>
                                    <a
                                        href="#"
                                        class="social-icon instagram"
                                        aria-label="Instagram"
                                        >
                                        <i class="fab fa-instagram"></i>
                                    </a>
                                    <a
                                        href="#"
                                        class="social-icon linkedin"
                                        aria-label="LinkedIn"
                                        >
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
                                    <span>${user.userPhone}</span>
                                </div>
                                <div class="contact-item">
                                    <i class="fas fa-map-marker-alt"></i>
                                    <span>${user.userAddress}</span>
                                </div>
                                <div class="contact-item">
                                    <i class="fas fa-envelope"></i>
                                    <span>${user.userEmail}</span>
                                </div>

                            </div>
                        </div>

                        <div class="profile-right">
                           <form class="profile-form" id="profile-form" action="${pageContext.request.contextPath}/updateUser" method="post">
    <input type="hidden" name="userID" value="${user.userID}" />
    <!-- debug -->
    <!-- oninput="this.setCustomValidity('')" -->
    <h1>ID: ${user.userID}</h1> 
    
    <div class="form-row">
        <div class="form-group">
            <label for="userFullName">Full Name *</label>
            <input
                type="text"
                id="userFullName"
                name="userFullName"
                placeholder="Enter Full Name"
                required
                value="${user.userFullName}" />
        </div>
        <div class="form-group">
            <label for="userAddress">Address *</label>
            <input
                type="text"
                id="userAddress"
                name="userAddress"
                placeholder="Enter Address"
                required
                value="${user.userAddress}" />
        </div>
    </div>

    <div class="form-row">
        <div class="form-group">
            <label for="userPassword">Password *</label>
            <input
                type="password"
                id="userPassword"
                name="userPassword"
                placeholder="Enter password"
                required
                value="${user.userPassword}" />
        </div>

        <div class="form-group">
            <label for="userPhone">Phone *</label>
            <input
                
                type="tel"
                id="userPhone"
                name="userPhone"
                placeholder="Enter phone"
                required
                value="${user.userPhone}" />
        </div>
    </div>

    <div class="form-row">
        <div class="form-group">
            <label for="dateOfBirth">Ngày sinh *</label>
            <input
    type="date"
    id="dateOfBirth"
    name="dateOfBirth"
    required
    value="<fmt:formatDate value='${user.dateOfBirth}' pattern='yyyy-MM-dd' />" />
            
        </div>
        <div class="form-group">
            <label for="userGender">Giới tính</label>
            <select id="userGender" name="userGender">
                <option value="" ${empty user.userGender ? "selected" : ""}>Chọn giới tính</option>
                <option value="male" ${user.userGender == "male" ? "selected" : ""}>Nam</option>
                <option value="female" ${user.userGender == "female" ? "selected" : ""}>Nữ</option>
                <option value="other" ${user.userGender == "other" ? "selected" : ""}>Khác</option>
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

        <!-- Footer -->
        <footer class="footer">
            <div class="footer-top">
                <div class="container">
                    <div class="contact-info">
                        <div class="contact-item">
                            <div class="contact-icon">
                                <i class="fas fa-map-marker-alt"></i>
                            </div>
                            <div class="contact-details">
                                <h4>Địa chỉ</h4>
                                <p>Hudson, Wisconsin(WI), 54016</p>
                            </div>
                        </div>

                        <div class="contact-item">
                            <div class="contact-icon">
                                <i class="fas fa-phone"></i>
                            </div>
                            <div class="contact-details">
                                <h4>Điện thoại</h4>
                                <p>(568) 367-987-237</p>
                            </div>
                        </div>

                        <div class="contact-item">
                            <div class="contact-icon">
                                <i class="fas fa-envelope"></i>
                            </div>
                            <div class="contact-details">
                                <h4>Email</h4>
                                <p>govillage@gmail.com</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="footer-main">
                <div class="container">
                    <div class="footer-content">
                        <div class="footer-section">
                            <div class="footer-logo">
                                <img src="${pageContext.request.contextPath}/images/logo.jpg" alt="Logo" height="80" width="120" />
                            </div>
                            <p class="footer-description">
                                Nền tảng học trực tuyến giúp bạn phát triển kỹ năng mềm hiệu quả
                                với các khóa học chất lượng cao.
                            </p>
                            <div class="social-links">
                                <a href="#" class="social-link" aria-label="Facebook">
                                    <i class="fab fa-facebook-f"></i>
                                </a>
                                <a href="#" class="social-link" aria-label="Instagram">
                                    <i class="fab fa-instagram"></i>
                                </a>
                                <a href="#" class="social-link" aria-label="Twitter">
                                    <i class="fab fa-twitter"></i>
                                </a>
                                <a href="#" class="social-link" aria-label="LinkedIn">
                                    <i class="fab fa-linkedin-in"></i>
                                </a>
                            </div>
                        </div>

                        <div class="footer-section">
                            <h3>Dịch vụ của chúng tôi</h3>
                            <ul class="footer-links">
                                <li><a href="#">Tư vấn nghề nghiệp</a></li>
                                <li><a href="#">Đánh giá kỹ năng</a></li>
                                <li><a href="#">Workshop tùy chỉnh</a></li>
                                <li><a href="#">Phiên coaching</a></li>
                                <li><a href="#">Theo dõi tiến độ</a></li>
                            </ul>
                        </div>

                        <div class="footer-section">
                            <h3>Thư viện ảnh</h3>
                            <div class="gallery-grid">
                                
                                <div class="gallery-item">
                                    <img src="${pageContext.request.contextPath}/images/gallery1.jpg" alt="Gallery 1" height="80" width="80" />
                                </div>
                                <div class="gallery-item">
                                    <img src="${pageContext.request.contextPath}/images/gallery2.jpg" alt="Gallery 2" height="80" width="80" />
                                </div>
                                <div class="gallery-item">
                                    <img src="${pageContext.request.contextPath}/images/gallery3.jpg" alt="Gallery 3" height="80" width="80" />
                                </div>
                                <div class="gallery-item">
                                    <img src="${pageContext.request.contextPath}/images/gallery4.jpg" alt="Gallery 4" height="80" width="80" />
                                </div>
                                <div class="gallery-item">
                                    <img src="${pageContext.request.contextPath}/images/gallery5.jpg" alt="Gallery 5" height="80" width="80" />
                                </div>
                                <div class="gallery-item">
                                    <img src="${pageContext.request.contextPath}/images/gallery6.jpg" alt="Gallery 6" height="80" width="80" />
                                </div>

                            </div>
                        </div>

                        <div class="footer-section">
                            <h3>Đăng ký nhận tin</h3>
                            <p>Nhận thông tin về các khóa học mới và ưu đãi đặc biệt</p>
                            <form class="newsletter-form">
                                <div class="input-group">
                                    <input
                                        type="email"
                                        placeholder="Nhập email của bạn"
                                        required
                                        />
                                    <button type="submit">
                                        <i class="fas fa-paper-plane"></i>
                                    </button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>

            <div class="footer-bottom">
                <div class="container">
                    <p>
                        &copy; 2024 <strong>Soft Skills Learning</strong>. Tất cả quyền được
                        bảo lưu.
                    </p>
                </div>
            </div>
        </footer>

        <!-- Scripts -->
        <script src="${pageContext.request.contextPath}/js/profile.js" defer></script>
        
       <script>
    document.getElementById('avatar-input').addEventListener('change', function () {
        const file = this.files[0];
        if (!file) return;

        const formData = new FormData();
        formData.append("avatar", file);
        formData.append("userID", "${user.userID}");

        const reader = new FileReader();
        reader.onload = function (e) {
            document.getElementById("avatar-preview").src = e.target.result;
        };
        reader.readAsDataURL(file);

        fetch("${pageContext.request.contextPath}/uploadAvatar", {
            method: "POST",
            body: formData
        }).then(response => {
            if (!response.ok) alert("Upload thất bại!");
        });
    });
</script>
    </body>
</html>
