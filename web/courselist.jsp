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
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
</head>
<body>
    <!-- Header -->
    <header class="header">
        <div class="container">
            <div class="nav-brand">
                <img src="${pageContext.request.contextPath}/images/SoftSkillWhite.png" alt="Soft Skills" class="logo">
                <span class="brand-text">SOFT SKILLS</span>
            </div>
            <nav class="navbar">
                <ul class="nav-list">
                    <li><a href="${pageContext.request.contextPath}/" class="nav-link">Home</a></li>
                    <li><a href="#" class="nav-link">About Us</a></li>
                    <li><a href="${pageContext.request.contextPath}/courses" class="nav-link active">Courses</a></li>
                    <li><a href="#" class="nav-link">Pages</a></li>
                    <li><a href="#" class="nav-link">Blog</a></li>
                    <li><a href="#" class="nav-link">Contact</a></li>
                </ul>
            </nav>
            <div class="header-actions">
                <i class="fas fa-search"></i>
                <i class="fas fa-user"></i>
                <i class="fas fa-bars mobile-menu"></i>
            </div>
        </div>
    </header>

    <!-- Banner -->
    <section class="banner" style="background-image: url('${pageContext.request.contextPath}/images/Banner.jpg');">
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
    <body>
    <div class="main-container">
        <!-- Sidebar -->
        <aside class="sidebar">
            <!-- Search Box -->
            <div class="search-box">
                <input type="text" placeholder="Search" id="searchInput">
                <button type="button" class="search-btn"><i class="fas fa-search"></i></button>
            </div>
            
            <!-- Service Categories -->
            <div class="service-category">
                <h3>SERVICE CATEGORY</h3>
                <ul class="category-list">
                    <li class="category-item">
                        <div class="category-header">
                            <span>Graphic Design</span>
                            <i class="fas fa-chevron-right"></i>
                        </div>
                    </li>
                    <li class="category-item">
                        <div class="category-header">
                            <span>Web Design</span>
                            <i class="fas fa-chevron-right"></i>
                        </div>
                    </li>
                    <li class="category-item">
                        <div class="category-header">
                            <span>IT And Software</span>
                            <i class="fas fa-chevron-right"></i>
                        </div>
                    </li>
                    <li class="category-item">
                        <div class="category-header">
                            <span>Sales Marketing</span>
                            <i class="fas fa-chevron-right"></i>
                        </div>
                    </li>
                    <li class="category-item">
                        <div class="category-header">
                            <span>Art & Humanities</span>
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
                <input type="number" id="pageSizeInput" value="9" min="1" class="page-size-input" placeholder="Enter number">
            </div>
            <div class="sort-options">
                <span>Sorted by latest updates</span>
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
                    <div class="course-card featured">
                        <div class="course-image">
                            <img src="${pageContext.request.contextPath}/images/goal-setting.jpg" alt="Goal Setting">
                        </div>
                        <div class="course-content">
                            <h3>Goal Setting</h3>
                            <p>Master your day with smart time strategies. This course boosts productivity, cuts stress, and helps you build a balanced, successful life.</p>
                            <div class="course-tags">
                                <span class="tag">Tag: #goalsetting</span>
                                <span class="tag">#softskills</span>
                            </div>
                            <div class="course-price">
                                250.000 VND
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
                    <button class="page-btn active">1</button>
                    <button class="page-btn">2</button>
                    <button class="page-btn">3</button>
                    <button class="page-btn next"><i class="fas fa-chevron-right"></i></button>
                </div>
            </main>
        </div>
    </section>

   <!-- Footer -->
<footer class="footer">
    <div class="footer-top">
        <div class="footer-block">
            <span class="footer-icon">📍</span>
            <span class="footer-label">Address:</span>
            <span class="footer-info">1925 Bogness Street</span>
        </div>
        <div class="footer-block">
            <span class="footer-icon">📞</span>
            <span class="footer-label">Phone:</span>
            <span class="footer-info">(00) 875 784 568</span>
        </div>
        <div class="footer-block">
            <span class="footer-icon">📧</span>
            <span class="footer-label">Email:</span>
            <span class="footer-info">info@gmail.com</span>
        </div>
    </div>
    <div class="footer-content">
        <div class="footer-section logo-section">
            <div class="footer-logo">
                <img src="${pageContext.request.contextPath}/images/SoftSkillWhite.png" alt="Soft Skills Logo">
                <p>Interdum velit laoreet id donec ultrices tincidunt arcu tortor aliqua mi facilisi cras fermentum odio eu.</p>
                <div class="social-icons">
                        <a href="#"><i class="icon">F</i></a>
                        <a href="#"><i class="icon">T</i></a>
                        <a href="#"><i class="icon">I</i></a>
                        <a href="#"><i class="icon">L</i></a>
                    </div>
            </div>
        </div>
        <div class="footer-section">
            <h4>Our Services:</h4>
            <ul>
                <li><a href="#">Web Design</a></li>
                <li><a href="#">UI/UX Design</a></li>
                <li><a href="#">Development</a></li>
                <li><a href="#">Marketing</a></li>
                <li><a href="#">Digital Marketing</a></li>
                <li><a href="#">Blog News</a></li>
            </ul>
        </div>
        <div class="footer-section">
            <h4>Gallery</h4>
            <div class="gallery">
                <img src="${pageContext.request.contextPath}/images/recent-post-thumbnail.jpg" alt="Gallery Image">
                <img src="${pageContext.request.contextPath}/images/recent-post-thumbnail-2.jpg" alt="Gallery Image">
                <img src="${pageContext.request.contextPath}/images/recent-post-thumbnail-3.jpg" alt="Gallery Image">
                <img src="${pageContext.request.contextPath}/images/recent-post-thumbnail-4.jpg" alt="Gallery Image">
            </div>
        </div>
        <div class="footer-section">
            <h4>Subscribe</h4>
            <input type="email" placeholder="Enter your email">
            <button class="subscribe-btn">SUBSCRIBE NOW</button>
        </div>
    </div>
    
</footer>
<jsp:include page="/view/footer.jsp"/>              
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
    document.addEventListener('DOMContentLoaded', function() {
        const categoryItems = document.querySelectorAll('.category-item');
        categoryItems.forEach(item => {
            item.addEventListener('click', function() {
                // Xóa lớp active khỏi tất cả các mục
                categoryItems.forEach(i => i.classList.remove('active'));
                // Thêm lớp active cho mục được click
                this.classList.add('active');
            });
        });
    });
</script>
</body>
</html>