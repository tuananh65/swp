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
    <jsp:include page="/view/header.jsp"/>

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

    <div class="main-container">
        <aside class="sidebar">
            <form action="${pageContext.request.contextPath}/courseList" method="get">
                <div class="search-box">
                    <input type="text" name="search" placeholder="Tìm kiếm khóa học..." value="${fn:escapeXml(searchTerm)}">
                    <button type="submit" class="search-btn"><i class="fas fa-search"></i></button>
                    <!-- Thêm hidden inputs để giữ trạng thái checkbox -->
                    <input type="hidden" name="showThumbnail" value="${showThumbnail}">
                    <input type="hidden" name="showPrice" value="${showPrice}">
                    <input type="hidden" name="showRegister" value="${showRegister}">
                    <input type="hidden" name="showTagline" value="${showTagline}">
                    <input type="hidden" name="showTitle" value="${showTitle}">
                    <input type="hidden" name="sort" value="${sortBy}">
                    <input type="hidden" name="pageSize" value="${pageSize}">
                </div>
            </form>
            <div class="contact-info">
                <div class="contact-item"><i class="fas fa-phone"></i><span>(555) 567-987-237</span></div>
                <div class="contact-item"><i class="fas fa-map-marker-alt"></i><span>Hudson, Wisconsin(WI), 54016</span></div>
                <div class="contact-item"><i class="fas fa-envelope"></i><span>contact@gmail.com</span></div>
            </div>
        </aside>

        <main class="main-content">
            <form id="filterForm" action="${pageContext.request.contextPath}/courseList" method="get">
                <div class="filter-options">
                    <div class="filter-checkboxes">
                        <label><input type="checkbox" name="showThumbnail" value="true" ${showThumbnail ? 'checked' : ''}> Thumbnail</label>
                        <label><input type="checkbox" name="showPrice" value="true" ${showPrice ? 'checked' : ''}> Price</label>
                        <label><input type="checkbox" name="showRegister" value="true" ${showRegister ? 'checked' : ''}> Register button</label>
                        <label><input type="checkbox" name="showTagline" value="true" ${showTagline ? 'checked' : ''}> Tagline</label>
                        <label><input type="checkbox" name="showTitle" value="true" ${showTitle ? 'checked' : ''}> Title</label>
                        <button type="submit" class="save-display-btn">Lưu</button>
                    </div>
                    <div class="filter-right">
                        <span>Số khóa học mỗi trang:</span>
                        <input type="number" name="pageSize" value="${pageSize}" min="1" max="100" class="page-size-input" onchange="this.form.submit()">
                        <span>Sắp xếp theo: 
                            <select name="sort" onchange="this.form.submit()">
                                <option value="latest" ${sortBy == 'latest' ? 'selected' : ''}>Mới nhất</option>
                                <option value="price_low" ${sortBy == 'price_low' ? 'selected' : ''}>Giá thấp</option>
                                <option value="price_high" ${sortBy == 'price_high' ? 'selected' : ''}>Giá cao</option>
                                <option value="name" ${sortBy == 'name' ? 'selected' : ''}>Tên</option>
                            </select>
                        </span>
                    </div>
                </div>
                <input type="hidden" name="search" value="${fn:escapeXml(searchTerm)}">
            </form>

            <div class="courses-grid">
                <c:choose>
                    <c:when test="${not empty courses}">
                        <c:forEach var="course" items="${courses}">
                            <div class="course-card">
                                <c:if test="${showThumbnail}">
                                    <div class="course-image">
                                        <img src="${pageContext.request.contextPath}/${not empty course.courseThumbnail ? course.courseThumbnail : 'images/default-course.jpg'}" 
                                             alt="${fn:escapeXml(course.courseName)}">
                                    </div>
                                </c:if>
                                <div class="course-content">
                                    <c:if test="${showTitle}">
                                        <h3>${fn:escapeXml(course.courseName)}</h3>
                                    </c:if>
                                    <p>${fn:escapeXml(course.briefInfo)}</p>
                                    <c:if test="${showTagline}">
                                        <div class="course-tags">
                                            <span class="tag">${fn:escapeXml(course.tagLine)}</span>
                                        </div>
                                    </c:if>
                                    <c:if test="${showPrice}">
                                        <div class="course-price">
                                            <c:if test="${course.salePrice < course.originalPrice}">
                                                <span class="original-price"><fmt:formatNumber value="${course.originalPrice}" type="currency"/></span>
                                                <span class="sale-price"><fmt:formatNumber value="${course.salePrice}" type="currency"/></span>
                                            </c:if>
                                            <c:if test="${course.salePrice >= course.originalPrice}">
                                                <span class="price"><fmt:formatNumber value="${course.originalPrice}" type="currency"/></span>
                                            </c:if>
                                        </div>
                                    </c:if>
                                    <c:if test="${showRegister}">
                                        <button class="register-btn">Đăng ký ngay</button>
                                    </c:if>
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

            <div class="pagination">
                <c:if test="${totalPages > 1}">
                    <c:if test="${currentPage > 1}">
                        <a class="page-btn" href="${pageContext.request.contextPath}/courseList?page=${currentPage - 1}&search=${fn:escapeXml(searchTerm)}&sort=${sortBy}&pageSize=${pageSize}&showThumbnail=${showThumbnail}&showPrice=${showPrice}&showRegister=${showRegister}&showTagline=${showTagline}&showTitle=${showTitle}">
                            <i class="fas fa-chevron-left"></i>
                        </a>
                    </c:if>
                    <c:forEach var="i" begin="1" end="${totalPages}">
                        <c:choose>
                            <c:when test="${i == currentPage}">
                                <span class="page-btn active">${i}</span>
                            </c:when>
                            <c:when test="${i <= 2 || i > totalPages - 2 || (i >= currentPage - 1 && i <= currentPage + 1)}">
                                <a class="page-btn" href="${pageContext.request.contextPath}/courseList?page=${i}&search=${fn:escapeXml(searchTerm)}&sort=${sortBy}&pageSize=${pageSize}&showThumbnail=${showThumbnail}&showPrice=${showPrice}&showRegister=${showRegister}&showTagline=${showTagline}&showTitle=${showTitle}">${i}</a>
                            </c:when>
                            <c:when test="${i == 3 && currentPage > 4 || i == totalPages - 2 && currentPage < totalPages - 3}">
                                <span class="page-dots">...</span>
                            </c:when>
                        </c:choose>
                    </c:forEach>
                    <c:if test="${currentPage < totalPages}">
                        <a class="page-btn" href="${pageContext.request.contextPath}/courseList?page=${currentPage + 1}&search=${fn:escapeXml(searchTerm)}&sort=${sortBy}&pageSize=${pageSize}&showThumbnail=${showThumbnail}&showPrice=${showPrice}&showRegister=${showRegister}&showTagline=${showTagline}&showTitle=${showTitle}">
                            <i class="fas fa-chevron-right"></i>
                        </a>
                    </c:if>
                </c:if>
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

    <script>
        const filterKey = 'course_display_filters';

        // Lưu trạng thái checkbox vào localStorage
        document.querySelector('.save-display-btn').addEventListener('click', () => {
            const checkboxes = document.querySelectorAll('.filter-checkboxes input[type="checkbox"]');
            const filterState = {};
            checkboxes.forEach(cb => {
                filterState[cb.name] = cb.checked;
            });
            localStorage.setItem(filterKey, JSON.stringify(filterState));
        });

        // Áp dụng trạng thái từ localStorage khi load trang
        window.addEventListener('DOMContentLoaded', () => {
            const savedState = localStorage.getItem(filterKey);
            if (savedState) {
                const filterState = JSON.parse(savedState);
                Object.entries(filterState).forEach(([name, checked]) => {
                    const checkbox = document.querySelector(`.filter-checkboxes input[name="${name}"]`);
                    if (checkbox) {
                        checkbox.checked = checked;
                    }
                });
            }
        });
    </script>
</body>
</html>