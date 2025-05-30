<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>


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
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/cousrsedetail.css" />
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
                    <img src="${pageContext.request.contextPath}/images/logo.jpg" alt="Soft Skills Logo" />
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
                        <a href="#" class="nav-link">
                            Khóa học <i class="fas fa-chevron-down"></i>
                        </a>
                        <div class="dropdown-content">
                            <c:forEach var="c" items="${courseList}">
                                <!-- Khi click vào sẽ gọi lại servlet và truyền courseId -->
                                <a href="${pageContext.request.contextPath}/coursedetail?courseId=${c.courseID}">
                                    ${c.courseName}
                                </a>
                            </c:forEach>
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
                    <h1>COURSE DETAIL</h1>
                </div>
            </div>
        </section>

       
    <!-- Nút toggle bên cạnh màn hình -->
 <!-- Nút tròn cố định bên trái giữa màn hình -->
<button id="toggleMenuBtn" style="
    position: fixed;
    top: 50%;
    left: 0;
    transform: translateY(-50%);
    z-index: 1000;
    width: 50px;
    height: 50px;
    background-color: #6366f1;
    color: #ffffff;
    border: none;
    border-radius: 50%;
    font-size: 1.5rem;
    cursor: pointer;
    box-shadow: 0 4px 6px rgba(0,0,0,0.1);
    display: flex;
    align-items: center;
    justify-content: center;
">
    ☰
</button>

<!-- Container menu -->
<div id="courseDropdownContainer" style="
    display: none;
    position: fixed;
    top: 50%;
    left: 60px;
    transform: translateY(-50%);
    background-color: #ffffff;
    box-shadow: 0 10px 15px -3px rgba(0,0,0,0.1), 0 4px 6px -4px rgba(0,0,0,0.1);
    padding: 1rem;
    border-radius: 1rem;
    z-index: 999;
    width: 280px;
    max-height: 80vh;
    overflow-y: auto;
    font-family: 'Inter', sans-serif;
">
    <!-- Ô tìm kiếm -->
    <input type="text" id="searchInput" placeholder="Tìm kiếm khóa học..." style="
        width: 100%;
        padding: 0.6rem 1rem;
        margin-bottom: 1rem;
        border: 1px solid #d1d5db;
        border-radius: 0.75rem;
        font-size: 1rem;
        transition: box-shadow 0.2s;
    " onfocus="this.style.boxShadow='0 0 0 2px #6366f1'" onblur="this.style.boxShadow='none'" />

    <!-- Danh sách kết quả tìm kiếm -->
    <div id="searchResult" style="display: none; flex-direction: column; gap: 0.5rem;"></div>

    <!-- Thông báo khi không tìm thấy -->
    <div id="noResultsMsg" style="display:none; color:#ef4444; font-weight:600; text-align:center; margin-top: 0.5rem;">
        Không tìm thấy khóa học nào.
    </div>

    <!-- Dropdown thủ công -->
    <div id="manualDropdowns" style="display: flex; flex-direction: column; gap: 1.5rem;">
        <!-- Khóa học -->
        <div>
            <button class="dropdown-toggle" onclick="toggleDropdown(this)">
                Khóa học <i class="fas fa-chevron-down" style="float: right;"></i>
            </button>
            <div class="dropdown-list" style="display: none;">
                <c:forEach var="c" items="${courseList}">
                    <a href="${pageContext.request.contextPath}/coursedetail?courseId=${c.courseID}" class="dropdown-item all-course-item">
                        ${c.courseName}
                    </a>
                </c:forEach>
            </div>
        </div>

        <!-- Featured -->
        <div>
            <button class="dropdown-toggle" onclick="toggleDropdown(this)">
                Featured Courses <i class="fas fa-chevron-down" style="float: right;"></i>
            </button>
            <div class="dropdown-list" style="display: none;">
                <c:forEach var="fc" items="${featuredCourses}">
                    <a href="${pageContext.request.contextPath}/coursedetail?courseId=${fc.courseID}" class="dropdown-item all-course-item">
                        ${fc.courseName}
                    </a>
                </c:forEach>
            </div>
        </div>
    </div>
</div>

<!-- CSS -->
<style>
    .dropdown-toggle {
        background-color: #6366f1;
        color: white;
        border: none;
        padding: 0.75rem 1rem;
        border-radius: 0.75rem;
        cursor: pointer;
        font-size: 1rem;
        width: 100%;
        text-align: left;
        transition: background-color 250ms;
        box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
    }

    .dropdown-toggle:hover {
        background-color: #4f46e5;
    }

    .dropdown-list {
        margin-top: 0.5rem;
        display: flex;
        flex-direction: column;
        gap: 0.25rem;
        max-height: 200px;
        overflow-y: auto;
    }

    .dropdown-item {
        padding: 0.6rem 0.75rem;
        border-radius: 0.5rem;
        text-decoration: none;
        color: #111827;
        background-color: #f3f4f6;
        display: block;
        transition: background-color 200ms;
        font-size: 0.95rem;
    }

    .dropdown-item:hover {
        background-color: #e5e7eb;
    }
</style>

<!-- JavaScript -->
<script>
    const toggleMenuBtn = document.getElementById('toggleMenuBtn');
    const menuContainer = document.getElementById('courseDropdownContainer');
    const searchInput = document.getElementById('searchInput');
    const searchResult = document.getElementById('searchResult');
    const manualDropdowns = document.getElementById('manualDropdowns');
    const noResultsMsg = document.getElementById('noResultsMsg');

    toggleMenuBtn.addEventListener('click', () => {
        const isVisible = menuContainer.style.display === 'block';
        menuContainer.style.display = isVisible ? 'none' : 'block';

        if (isVisible) {
            searchInput.value = '';
            searchResult.style.display = 'none';
            manualDropdowns.style.display = 'flex';
            noResultsMsg.style.display = 'none';

            const dropdowns = menuContainer.querySelectorAll('.dropdown-list');
            dropdowns.forEach(d => d.style.display = 'none');
        }
    });

    function toggleDropdown(button) {
        const dropdown = button.nextElementSibling;
        const isVisible = dropdown.style.display === 'flex';
        dropdown.style.display = isVisible ? 'none' : 'flex';
    }

    searchInput.addEventListener('input', function () {
        const keyword = this.value.toLowerCase().trim();
        const allCourses = document.querySelectorAll('.all-course-item');
        searchResult.innerHTML = '';

        if (keyword === '') {
            searchResult.style.display = 'none';
            noResultsMsg.style.display = 'none';
            manualDropdowns.style.display = 'flex';
            return;
        }

        manualDropdowns.style.display = 'none';
        searchResult.style.display = 'flex';

        let foundCount = 0;
        allCourses.forEach(item => {
            const text = item.textContent.toLowerCase();
            if (text.includes(keyword)) {
                const cloned = item.cloneNode(true);
                cloned.style.display = 'block';
                searchResult.appendChild(cloned);
                foundCount++;
            }
        });

        // Hiển thị thông báo nếu không tìm thấy kết quả
        noResultsMsg.style.display = foundCount === 0 ? 'block' : 'none';
    });
</script>



  

        <!-- Main Content -->
                    
        <main class="main-content">
           
            <c:if test="${not empty course}">
                <div class="course-layout">
                    <div class="course-thumbnail">
                        <img src="${pageContext.request.contextPath}/${course.courseThumbnail}" alt="${course.courseName}" />
                    </div>
                    <div class="course-info">
                        <h1>${course.courseName}</h1>
                        <p class="tagline">${course.tagLine}</p>

                        <div class="rating">
                            <span>★★★★★</span>
                            <span class="review-count">(124 đánh giá)</span>
                        </div>

                        <div class="desc-block">
                            <h3>Mô tả ngắn:</h3>
                            <p>${course.briefInfo}</p>
                        </div>

                        <div class="desc-block">
                            <h3>Mô tả chi tiết:</h3>
                            <p>${course.description}</p>
                        </div>

                        <div class="price-block">
                            <p><strong>Giá gốc:</strong> <span class="original-price">${course.originalPrice} VND</span></p>
                            <p><strong>Giá khuyến mãi:</strong> <span class="sale-price">${course.salePrice} VND</span></p>
                        </div>

                        <a href="#" class="enroll-button">Đăng ký ngay</a>

                        <div class="extra-info">
                            <p><strong>Ngày tạo:</strong> <fmt:formatDate value="${course.createdAt}" pattern="dd/MM/yyyy" /></p>
                            <p><strong>Ngày cập nhật:</strong> <fmt:formatDate value="${course.updatedAt}" pattern="dd/MM/yyyy" /></p>
                            <p><strong>Người tạo khóa học (UserID):</strong> ${course.userID}</p>
                            <p><strong>Đặc biệt:</strong>
                                <c:choose>
                                    <c:when test="${course.featured}">Có</c:when>
                                    <c:otherwise>Không</c:otherwise>
                                </c:choose>
                            </p>
                        </div>
                    </div>
                </div>
            </c:if>
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
                                <img src="${pageContext.request.contextPath}/images/logo.jpg" alt="Logo" />
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
                        &copy; 2024 <strong style="color: orange;">Soft Skills Learning</strong>. Tất cả quyền được
                        bảo lưu.
                    </p>
                </div>
            </div>
        </footer>
        

    </body>
</html>
