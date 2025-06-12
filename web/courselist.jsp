<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Danh Sách Khóa Học - Soft Skills</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/courselist.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/header.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/footer.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
</head>
<body>

<!-- Header -->
<jsp:include page="/view/header.jsp"/>

<!-- Banner -->
<section class="banner">
    <div class="container">
        <div class="banner-content">
            <h1>COURSES LIST</h1>
            <div class="breadcrumb">
                <a href="${pageContext.request.contextPath}/">Home</a> > <span>Courses List</span>
            </div>
        </div>
    </div>
</section>

<!-- Main Content -->
<div class="main-container">
    <!-- Sidebar -->
    <aside class="sidebar">
        <!-- Search Box -->
        <form action="${pageContext.request.contextPath}/courseList" method="get">
            <div class="search-box">
                <input type="text" name="search" placeholder="Search" id="searchInput" value="${fn:escapeXml(searchTerm)}">
                <button type="submit" class="search-btn"><i class="fas fa-search"></i></button>
            </div>
        </form>

        <!-- Contact Info -->
        <div class="contact-info">
            <div class="contact-item">
                <i class="fas fa-phone"></i>
                <span>(555) 567-987-237</span>
            </div>
            <div class="contact-item">
                <i class="fas fa-map-marker-alt"></i>
                <span>Hudson, Wisconsin(WI), 54016</span>
            </div>
            <div class="contact-item">
                <i class="fas fa-envelope"></i>
                <span>contact@gmail.com</span>
            </div>
        </div>
    </aside>

    <!-- Main Content -->
    <main class="main-content">
        <!-- Filter Options -->
        <form id="filterForm" action="${pageContext.request.contextPath}/courseList" method="get">
            <div class="filter-options">
                <div class="filter-checkboxes">
                    <label><input type="checkbox" checked> Thumbnail</label>
                    <label><input type="checkbox" checked> Price</label>
                    <label><input type="checkbox" checked> Register button</label>
                    <label><input type="checkbox" checked> Tagline</label>
                    <label><input type="checkbox" checked> Title</label>
                </div>
                <div class="filter-right">
                    <span>Maximum courses per page:</span>
                    <div class="page-size-selector">
                        <input type="number" id="pageSizeInput" name="pageSize" value="${pageSize}" min="1" class="page-size-input" placeholder="Enter number" onchange="this.form.submit()">
                    </div>
                    <div class="sort-options">
                        <span>Sorted by 
                            <select name="sort" onchange="this.form.submit()">
                                <option value="latest" ${sortBy == 'latest' ? 'selected' : ''}>Latest</option>
                                <option value="price_low" ${sortBy == 'price_low' ? 'selected' : ''}>Price Low</option>
                                <option value="price_high" ${sortBy == 'price_high' ? 'selected' : ''}>Price High</option>
                                <option value="name" ${sortBy == 'name' ? 'selected' : ''}>Name</option>
                            </select>
                        </span>
                    </div>
                </div>
            </div>
            <input type="hidden" name="search" value="${fn:escapeXml(searchTerm)}">
        </form>

        <!-- Course List -->
        <div id="courseListContainer">
            <div class="courses-grid">
                <c:choose>
                    <c:when test="${not empty courses}">
                        <c:forEach var="course" items="${courses}">
                            <div class="course-card">
                                <div class="course-image">
                                    <c:choose>
                                        <c:when test="${not empty course.courseThumbnail}">
                                            <img src="${pageContext.request.contextPath}/${course.courseThumbnail}" alt="${fn:escapeXml(course.courseName)}">
                                        </c:when>
                                        <c:otherwise>
                                            <img src="${pageContext.request.contextPath}/images/default-course.jpg" alt="Default course image">
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                                <div class="course-content">
                                    <h3>${fn:escapeXml(course.courseName)}</h3>
                                    <p>${fn:escapeXml(course.briefInfo)}</p>
                                    <div class="course-tags">
                                        <span class="tag">${fn:escapeXml(course.tagLine)}</span>
                                    </div>
                                    <div class="course-price">
                                        <c:if test="${course.salePrice < course.originalPrice}">
                                            <span class="original-price"><fmt:formatNumber value="${course.originalPrice}" type="currency"/></span>
                                            <span class="sale-price"><fmt:formatNumber value="${course.salePrice}" type="currency"/></span>
                                        </c:if>
                                        <c:if test="${course.salePrice >= course.originalPrice}">
                                            <span class="price"><fmt:formatNumber value="${course.originalPrice}" type="currency"/></span>
                                        </c:if>
                                    </div>
                                    <button class="register-btn">Register Now</button>
                                </div>
                            </div>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <div class="no-courses">
                            <p>Không tìm thấy khóa học nào. Vui lòng thử từ khóa khác.</p>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>

            <!-- Pagination -->
            <div class="pagination">
                <c:if test="${totalPages > 1}">
                    <c:if test="${currentPage > 1}">
                        <a class="page-btn" href="${pageContext.request.contextPath}/courseList?page=${currentPage - 1}&search=${searchTerm}&sort=${sortBy}&pageSize=${pageSize}">
                            <i class="fas fa-chevron-left"></i>
                        </a>
                    </c:if>

                    <c:forEach var="i" begin="1" end="${totalPages}">
                        <c:choose>
                            <c:when test="${i == currentPage}">
                                <span class="page-btn active">${i}</span>
                            </c:when>
                            <c:when test="${i <= 2 || i > totalPages - 2 || (i >= currentPage - 1 && i <= currentPage + 1)}">
                                <a class="page-btn" href="${pageContext.request.contextPath}/courseList?page=${i}&search=${searchTerm}&sort=${sortBy}&pageSize=${pageSize}">${i}</a>
                            </c:when>
                            <c:when test="${i == 3 && currentPage > 4}">
                                <span class="page-dots">...</span>
                            </c:when>
                            <c:when test="${i == totalPages - 2 && currentPage < totalPages - 3}">
                                <span class="page-dots">...</span>
                            </c:when>
                        </c:choose>
                    </c:forEach>

                    <c:if test="${currentPage < totalPages}">
                        <a class="page-btn" href="${pageContext.request.contextPath}/courseList?page=${currentPage + 1}&search=${searchTerm}&sort=${sortBy}&pageSize=${pageSize}">
                            <i class="fas fa-chevron-right"></i>
                        </a>
                    </c:if>
                </c:if>
            </div>
        </div>
    </main>
</div>

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
                                <img src="${pageContext.request.contextPath}/images/SoftSkillWhite.png" alt="Soft Skills Logo">
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
                                    <img
                                        src="/placeholder.svg?height=80&width=80"
                                        alt="Gallery 1"
                                        />
                                </div>
                                <div class="gallery-item">
                                    <img
                                        src="/placeholder.svg?height=80&width=80"
                                        alt="Gallery 2"
                                        />
                                </div>
                                <div class="gallery-item">
                                    <img
                                        src="/placeholder.svg?height=80&width=80"
                                        alt="Gallery 3"
                                        />
                                </div>
                                <div class="gallery-item">
                                    <img
                                        src="/placeholder.svg?height=80&width=80"
                                        alt="Gallery 4"
                                        />
                                </div>
                                <div class="gallery-item">
                                    <img
                                        src="/placeholder.svg?height=80&width=80"
                                        alt="Gallery 5"
                                        />
                                </div>
                                <div class="gallery-item">
                                    <img
                                        src="/placeholder.svg?height=80&width=80"
                                        alt="Gallery 6"
                                        />
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
        

   


</body>
</html>
