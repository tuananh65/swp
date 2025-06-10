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
    <!-- Include Header -->
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
            <div class="search-box">
                <input type="text" placeholder="Search" id="searchInput" value="${searchTerm}">
                <button type="button" class="search-btn" onclick="searchCourses()"><i class="fas fa-search"></i></button>
            </div>

            <!-- Service Categories -->
            <div class="service-category">
                <h3>SERVICE CATEGORY</h3>
                <ul class="category-list">
                    <li class="category-item ${categoryFilter == 'Communication' ? 'active' : ''}">
                        <div class="category-header" onclick="filterByCategory('Communication')">
                            <span>Communication</span>
                            <i class="fas fa-chevron-right"></i>
                        </div>
                    </li>
                    <li class="category-item ${categoryFilter == 'Emotional Intelligence' ? 'active' : ''}">
                        <div class="category-header" onclick="filterByCategory('Emotional Intelligence')">
                            <span>Emotional Intelligence</span>
                            <i class="fas fa-chevron-right"></i>
                        </div>
                    </li>
                    <li class="category-item ${categoryFilter == 'Time Management' ? 'active' : ''}">
                        <div class="category-header" onclick="filterByCategory('Time Management')">
                            <span>Time Management</span>
                            <i class="fas fa-chevron-right"></i>
                        </div>
                    </li>
                    <li class="category-item ${categoryFilter == 'Leadership' ? 'active' : ''}">
                        <div class="category-header" onclick="filterByCategory('Leadership')">
                            <span>Leadership</span>
                            <i class="fas fa-chevron-right"></i>
                        </div>
                    </li>
                    <li class="category-item ${categoryFilter == 'Teamwork' ? 'active' : ''}">
                        <div class="category-header" onclick="filterByCategory('Teamwork')">
                            <span>Teamwork</span>
                            <i class="fas fa-chevron-right"></i>
                        </div>
                    </li>
                </ul>
            </div>

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
                        <input type="number" id="pageSizeInput" value="${pageSize}" min="1" class="page-size-input" placeholder="Enter number" onchange="updatePageSize()">
                    </div>
                    <div class="sort-options">
                        <span>Sorted by 
                            <select name="sort" onchange="sortCourses()">
                                <option value="latest" ${sortBy == 'latest' ? 'selected' : ''}>Latest</option>
                                <option value="price_low" ${sortBy == 'price_low' ? 'selected' : ''}>Price Low</option>
                                <option value="price_high" ${sortBy == 'price_high' ? 'selected' : ''}>Price High</option>
                                <option value="name" ${sortBy == 'name' ? 'selected' : ''}>Name</option>
                            </select>
                        </span>
                    </div>
                </div>
            </div>
                
                <!-- Courses Grid -->
                <div class="courses-grid">
                    <!-- Course 1 -->
                    <div class="course-card">
                        <div class="course-image">
                            <img src="${pageContext.request.contextPath}/images/time-management.jpg" alt="Time Management">
                        </div>
                        <div class="course-content">
                            <h3>Time Management</h3>
                            <p>Master your day with smart time strategies. This course boosts productivity, cuts stress, and helps you build a balanced, successful life.</p>
                            <div class="course-tags">
                                <span class="tag">Tag: #timemanagement</span>
                                <span class="tag">#softskills</span>
                            </div>
                            <div class="course-price">
                                200.000 VND
                            </div>
                            <button class="register-btn">Register Now</button>
                        </div>
                    </div>
                    
                    <!-- Course 2 -->
                    <div class="course-card">
                        <div class="course-image">
                            <img src="${pageContext.request.contextPath}/images/time-management.jpg" alt="Time Management">
                        </div>
                        <div class="course-content">
                            <h3>Time Management</h3>
                            <p>Master your day with smart time strategies. This course boosts productivity, cuts stress, and helps you build a balanced, successful life.</p>
                            <div class="course-tags">
                                <span class="tag">Tag: #timemanagement</span>
                                <span class="tag">#softskills</span>
                            </div>
                            <div class="course-price">
                                200.000 VND
                            </div>
                            <button class="register-btn">Register Now</button>
                        </div>
                    </div>
                    
                    <!-- Course 3 -->
                    <div class="course-card">
                        <div class="course-image">
                            <img src="${pageContext.request.contextPath}/images/time-management.jpg" alt="Time Management">
                        </div>
                        <div class="course-content">
                            <h3>Time Management</h3>
                            <p>Master your day with smart time strategies. This course boosts productivity, cuts stress, and helps you build a balanced, successful life.</p>
                            <div class="course-tags">
                                <span class="tag">Tag: #timemanagement</span>
                                <span class="tag">#softskills</span>
                            </div>
                            <div class="course-price">
                                200.000 VND
                            </div>
                            <button class="register-btn">Register Now</button>
                        </div>
                    </div>
                    
                    <!-- Course 4 -->
                    <div class="course-card">
                        <div class="course-image">
                            <img src="${pageContext.request.contextPath}/images/time-management.jpg" alt="Time Management">
                        </div>
                        <div class="course-content">
                            <h3>Time Management</h3>
                            <p>Master your day with smart time strategies. This course boosts productivity, cuts stress, and helps you build a balanced, successful life.</p>
                            <div class="course-tags">
                                <span class="tag">Tag: #timemanagement</span>
                                <span class="tag">#softskills</span>
                            </div>
                            <div class="course-price">
                                200.000 VND
                            </div>
                            <button class="register-btn">Register Now</button>
                        </div>
                    </div>
                    
                    <!-- Course 5 -->
                    <div class="course-card">
                        <div class="course-image">
                            <img src="${pageContext.request.contextPath}/images/time-management.jpg" alt="Time Management">
                        </div>
                        <div class="course-content">
                            <h3>Time Management</h3>
                            <p>Master your day with smart time strategies. This course boosts productivity, cuts stress, and helps you build a balanced, successful life.</p>
                            <div class="course-tags">
                                <span class="tag">Tag: #timemanagement</span>
                                <span class="tag">#softskills</span>
                            </div>
                            <div class="course-price">
                                200.000 VND
                            </div>
                            <button class="register-btn">Register Now</button>
                        </div>
                    </div>
                    
                    <!-- Course 6 -->
                    <div class="course-card">
                        <div class="course-image">
                            <img src="${pageContext.request.contextPath}/images/time-management.jpg" alt="Time Management">
                        </div>
                        <div class="course-content">
                            <h3>Time Management</h3>
                            <p>Master your day with smart time strategies. This course boosts productivity, cuts stress, and helps you build a balanced, successful life.</p>
                            <div class="course-tags">
                                <span class="tag">Tag: #timemanagement</span>
                                <span class="tag">#softskills</span>
                            </div>
                            <div class="course-price">
                                200.000 VND
                            </div>
                            <button class="register-btn">Register Now</button>
                        </div>
                    </div>
                    
                    <!-- Course 7 -->
                    <div class="course-card">
                        <div class="course-image">
                            <img src="${pageContext.request.contextPath}/images/time-management.jpg" alt="Time Management">
                        </div>
                        <div class="course-content">
                            <h3>Time Management</h3>
                            <p>Master your day with smart time strategies. This course boosts productivity, cuts stress, and helps you build a balanced, successful life.</p>
                            <div class="course-tags">
                                <span class="tag">Tag: #timemanagement</span>
                                <span class="tag">#softskills</span>
                            </div>
                            <div class="course-price">
                                200.000 VND
                            </div>
                            <button class="register-btn">Register Now</button>
                        </div>
                    </div>
                    
                    <!-- Course 8 -->
                    <div class="course-card">
                        <div class="course-image">
                            <img src="${pageContext.request.contextPath}/images/time-management.jpg" alt="Time Management">
                        </div>
                        <div class="course-content">
                            <h3>Time Management</h3>
                            <p>Master your day with smart time strategies. This course boosts productivity, cuts stress, and helps you build a balanced, successful life.</p>
                            <div class="course-tags">
                                <span class="tag">Tag: #timemanagement</span>
                                <span class="tag">#softskills</span>
                            </div>
                            <div class="course-price">
                                200.000 VND
                            </div>
                            <button class="register-btn">Register Now</button>
                        </div>
                    </div>
                    
                    <!-- Course 9 -->
                    <div class="course-card">
                        <div class="course-image">
                            <img src="${pageContext.request.contextPath}/images/time-management.jpg" alt="Time Management">
                        </div>
                        <div class="course-content">
                            <h3>Time Management</h3>
                            <p>Master your day with smart time strategies. This course boosts productivity, cuts stress, and helps you build a balanced, successful life.</p>
                            <div class="course-tags">
                                <span class="tag">Tag: #timemanagement</span>
                                <span class="tag">#softskills</span>
                            </div>
                            <div class="course-price">
                                200.000 VND
                            </div>
                            <button class="register-btn">Register Now</button>
                        </div>
                    </div>
                </div>
                
               <!-- Pagination -->
            <div class="pagination">
                <c:if test="${totalPages > 1}">
                    <c:forEach var="i" begin="1" end="${totalPages}">
                        <button class="page-btn ${currentPage == i ? 'active' : ''}" onclick="goToPage(${i})">${i}</button>
                    </c:forEach>
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
        

   

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        function goToPage(page) {
            const searchTerm = "${searchTerm}";
            const categoryFilter = "${categoryFilter}";
            const sortBy = "${sortBy}";
            const pageSize = document.getElementById("pageSizeInput").value;
            window.location.href = `${pageContext.request.contextPath}/coursesList?page=${page}&pageSize=${pageSize}&search=${searchTerm}&category=${categoryFilter}&sort=${sortBy}`;
        }

        function searchCourses() {
            const searchTerm = document.getElementById("searchInput").value;
            const categoryFilter = "${categoryFilter}";
            const sortBy = "${sortBy}";
            const pageSize = document.getElementById("pageSizeInput").value;
            window.location.href = `${pageContext.request.contextPath}/coursesList?page=1&pageSize=${pageSize}&search=${searchTerm}&category=${categoryFilter}&sort=${sortBy}`;
        }

        function filterByCategory(category) {
            const searchTerm = "${searchTerm}";
            const sortBy = "${sortBy}";
            const pageSize = document.getElementById("pageSizeInput").value;
            window.location.href = `${pageContext.request.contextPath}/coursesList?page=1&pageSize=${pageSize}&search=${searchTerm}&category=${category}&sort=${sortBy}`;
        }

        function updatePageSize() {
            const searchTerm = "${searchTerm}";
            const categoryFilter = "${categoryFilter}";
            const sortBy = "${sortBy}";
            const pageSize = document.getElementById("pageSizeInput").value;
            window.location.href = `${pageContext.request.contextPath}/coursesList?page=1&pageSize=${pageSize}&search=${searchTerm}&category=${categoryFilter}&sort=${sortBy}`;
        }

        function sortCourses() {
            const searchTerm = "${searchTerm}";
            const categoryFilter = "${categoryFilter}";
            const sortBy = document.querySelector('select[name="sort"]').value;
            const pageSize = document.getElementById("pageSizeInput").value;
            window.location.href = `${pageContext.request.contextPath}/coursesList?page=1&pageSize=${pageSize}&search=${searchTerm}&category=${categoryFilter}&sort=${sortBy}`;
        }

        document.getElementById('searchInput').addEventListener('keypress', function(e) {
            if (e.key === 'Enter') {
                searchCourses();
            }
        });

        document.addEventListener('DOMContentLoaded', function() {
            const categoryItems = document.querySelectorAll('.category-item');
            categoryItems.forEach(item => {
                item.addEventListener('click', function() {
                    categoryItems.forEach(i => i.classList.remove('active'));
                    this.classList.add('active');
                });
            });
        });
    </script>
    
</body>
</html>