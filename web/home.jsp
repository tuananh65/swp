<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Soft Skills</title>
        <link href="${pageContext.request.contextPath}/css/styles.css" rel="stylesheet">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/SliderDetail.css" />
        <link href="${pageContext.request.contextPath}/css/home.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    </head>
    <body>
        <!-- Navigation Bar -->
        <nav>
            <jsp:include page="/default/header.jsp"/>
        </nav>
        
        <section class="slider-section">
            <iframe src="sliderdetail" width="100%" height="530" frameborder="0" scrolling="no"></iframe>
        </section>
        
        <!-- Main Content -->
        <div class="main-container">
            <!-- Content Grid -->
            <div class="blog-section">
                <div class="blog-container">
                    <!-- Recent Posts Sidebar (Left) -->
                    <div class="recent-posts-sidebar">
                        <h2 class="recent-posts-title">Recent Posts</h2>
                        <div class="recent-posts-list">
                            <c:forEach var="recentPost" items="${recentPosts}">
                                <div class="recent-post-item">
                                    <div class="recent-post-thumbnail">
                                        <img src="${pageContext.request.contextPath}/${recentPost.thumbnail}" alt="Recent Post Thumbnail">
                                    </div>
                                    <div class="recent-post-content">
                                        <div class="recent-post-date">
                                            <i class="bi bi-calendar"></i>
                                            <span>${recentPost.updatedDate}</span>
                                        </div>
                                        <h4 class="recent-post-title">
                                            <a href="${pageContext.request.contextPath}/post?id=${recentPost.id}">
                                                ${recentPost.title}
                                            </a>
                                        </h4>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </div>

                    <!-- Hot Posts Main Area (Right) -->
                    <div class="hot-posts-main">
                        <div class="hot-posts-header">
                            <h2 class="hot-posts-title">Hot Posts</h2>
                            <a href="${pageContext.request.contextPath}/blog" class="view-all-btn">VIEW ALL POST</a>
                        </div>

                        <div class="hot-posts-grid">
                            <c:forEach var="hotPost" items="${hotPosts}" end="1">
                                <article class="hot-post-card">
                                    <div class="hot-post-image">
                                        <img src="${pageContext.request.contextPath}/${hotPost.thumbnail}" alt="${hotPost.title}">
                                    </div>
                                    <div class="hot-post-content">
                                        <div class="hot-post-meta">
                                            <span class="post-author">
                                                <i class="bi bi-person"></i>
                                                ${hotPost.author}
                                            </span>
                                            <span class="post-date">
                                                <i class="bi bi-calendar"></i>
                                                ${hotPost.updatedDate}
                                            </span>
                                            <span class="post-category">
                                                <i class="bi bi-tag"></i>
                                                ${hotPost.category}
                                            </span>
                                        </div>
                                        <h3 class="hot-post-title">${hotPost.title}</h3>
                                        <a href="${pageContext.request.contextPath}/post?id=${hotPost.id}" class="read-more-btn">READ MORE</a>
                                    </div>
                                </article>
                            </c:forEach>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Featured Subjects Section -->
            <div class="featured-subjects">
                <h2 class="section-title">Featured Subjects</h2>
                <div class="subjects-container">
                    <c:forEach var="featuredCourse" items="${featuredCourses}" varStatus="status">
                        <div class="subject-card ${featuredCourse.featured ? 'featured' : 'dashed-border'}">
                            <div class="subject-image-container">
                                <img src="${pageContext.request.contextPath}/${featuredCourse.courseThumbnail}" alt="${featuredCourse.courseName}" class="subject-image">
                            </div>
                            <div class="subject-content">
                                <h3 class="subject-title">
                                    <a href="${pageContext.request.contextPath}/CourseDetailServlet?courseId=${featuredCourse.courseID}">
                                        ${featuredCourse.courseName}
                                    </a>
                                </h3>
                                <p class="subject-desc">${featuredCourse.tagLine}</p>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>

            <!-- Recent Course Section -->
            <div class="recent-course">
                <div class="course-header">
                    <h2 class="section-title">Recent Course</h2>
                </div>

                <!-- Filter Options -->
                <form id="filterForm" action="${pageContext.request.contextPath}/home" method="get">
                    <div class="filter-bar">
                        <div class="filter-checkboxes">
                            <label class="checkbox-item">
                                <input type="checkbox" name="showThumbnail" value="true" ${showThumbnail ? 'checked' : ''}>
                                <span class="checkmark"></span>
                                Thumbnail
                            </label>
                            <label class="checkbox-item">
                                <input type="checkbox" name="showPrice" value="true" ${showPrice ? 'checked' : ''}>
                                <span class="checkmark"></span>
                                Price
                            </label>
                            <label class="checkbox-item">
                                <input type="checkbox" name="showRegister" value="true" ${showRegister ? 'checked' : ''}>
                                <span class="checkmark"></span>
                                Register button
                            </label>
                            <label class="checkbox-item">
                                <input type="checkbox" name="showTagline" value="true" ${showTagline ? 'checked' : ''}>
                                <span class="checkmark"></span>
                                Tagline
                            </label>
                            <label class="checkbox-item">
                                <input type="checkbox" name="showTitle" value="true" ${showTitle ? 'checked' : ''}>
                                <span class="checkmark"></span>
                                Title
                            </label>
                            <span>Courses per page:</span>
                            <input type="number" name="pageSize" value="${pageSize}" min="1" max="100" class="page-size-input" onchange="this.form.submit()">
                            <span>Sort by:</span>
                            <select name="sort" onchange="this.form.submit()" class="sort-select">
                                <option value="latest" ${sortBy == 'latest' ? 'selected' : ''}>New</option>
                                <option value="price_low" ${sortBy == 'price_low' ? 'selected' : ''}>Low Price</option>
                                <option value="price_high" ${sortBy == 'price_high' ? 'selected' : ''}>High Price</option>
                                <option value="name" ${sortBy == 'name' ? 'selected' : ''}>Title</option>
                            </select>
                            <button type="submit" class="save-btn">SAVE</button>
                        </div>
                        <input type="hidden" name="search" value="${fn:escapeXml(searchTerm)}">
                    </div>
                </form>

                <!-- Courses Grid -->
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
                                            <h3 class="course-title">${fn:escapeXml(course.courseName)}</h3>
                                        </c:if>
                                        <p class="course-description">${fn:escapeXml(course.briefInfo)}</p>
                                        <c:if test="${showTagline}">
                                            <div class="course-tagline">${fn:escapeXml(course.tagLine)}</div>
                                        </c:if>
                                        <c:if test="${showPrice}">
                                            <div class="course-price">
                                                <c:if test="${course.salePrice < course.originalPrice}">
                                                    <span class="original-price"><fmt:formatNumber value="${course.originalPrice}" type="currency"/></span>
                                                    <span class="sale-price"><fmt:formatNumber value="${course.salePrice}" type="currency"/></span>
                                                </c:if>
                                                <c:if test="${course.salePrice >= course.originalPrice}">
                                                    <span class="current-price"><fmt:formatNumber value="${course.originalPrice}" type="currency"/></span>
                                                </c:if>
                                            </div>
                                        </c:if>
                                        <c:if test="${showRegister}">
                                            <a href="${pageContext.request.contextPath}/CourseDetailServlet?courseId=${course.courseID}" class="register-btn">Register Now</a>
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

                <!-- Pagination -->
                <div class="pagination">
                    <c:if test="${totalPages > 1}">
                        <c:if test="${currentPage > 1}">
                            <a class="page-btn" href="${pageContext.request.contextPath}/home?page=${currentPage - 1}&search=${fn:escapeXml(searchTerm)}&sort=${sortBy}&pageSize=${pageSize}&showThumbnail=${showThumbnail}&showPrice=${showPrice}&showRegister=${showRegister}&showTagline=${showTagline}&showTitle=${showTitle}">
                                <i class="fas fa-chevron-left"></i>
                            </a>
                        </c:if>
                        <c:forEach var="i" begin="1" end="${totalPages}">
                            <c:choose>
                                <c:when test="${i == currentPage}">
                                    <span class="page-btn active">${i}</span>
                                </c:when>
                                <c:when test="${i <= 2 || i > totalPages - 2 || (i >= currentPage - 1 && i <= currentPage + 1)}">
                                    <a class="page-btn" href="${pageContext.request.contextPath}/home?page=${i}&search=${fn:escapeXml(searchTerm)}&sort=${sortBy}&pageSize=${pageSize}&showThumbnail=${showThumbnail}&showPrice=${showPrice}&showRegister=${showRegister}&showTagline=${showTagline}&showTitle=${showTitle}">${i}</a>
                                </c:when>
                                <c:when test="${i == 3 && currentPage > 4 || i == totalPages - 2 && currentPage < totalPages - 3}">
                                    <span class="page-dots">...</span>
                                </c:when>
                            </c:choose>
                        </c:forEach>
                        <c:if test="${currentPage < totalPages}">
                            <a class="page-btn" href="${pageContext.request.contextPath}/home?page=${currentPage + 1}&search=${fn:escapeXml(searchTerm)}&sort=${sortBy}&pageSize=${pageSize}&showThumbnail=${showThumbnail}&showPrice=${showPrice}&showRegister=${showRegister}&showTagline=${showTagline}&showTitle=${showTitle}">
                                <i class="fas fa-chevron-right"></i>
                            </a>
                        </c:if>
                    </c:if>
                </div>
            </div>
        </div>

        <script>
            // Lưu trạng thái checkbox vào localStorage và gửi form
            document.querySelector('.save-btn').addEventListener('click', () => {
                const checkboxes = document.querySelectorAll('.filter-checkboxes input[type="checkbox"]');
                const filterState = {};
                checkboxes.forEach(cb => {
                    filterState[cb.name] = cb.checked;
                });
                localStorage.setItem('course_display_filters', JSON.stringify(filterState));
                document.getElementById('filterForm').submit(); // Gửi form sau khi lưu
            });

            // Áp dụng trạng thái từ localStorage hoặc đặt mặc định khi load trang
            window.addEventListener('DOMContentLoaded', () => {
                const savedState = localStorage.getItem('course_display_filters');
                const checkboxes = document.querySelectorAll('.filter-checkboxes input[type="checkbox"]');
                
                if (!savedState && !${showThumbnail || showPrice || showRegister || showTagline || showTitle}) {
                    // Nếu không có trạng thái đã lưu và không có tham số từ servlet, đặt mặc định tất cả đều checked
                    checkboxes.forEach(cb => {
                        cb.checked = true;
                    });
                } else if (savedState) {
                    // Áp dụng trạng thái từ localStorage
                    const filterState = JSON.parse(savedState);
                    checkboxes.forEach(cb => {
                        cb.checked = filterState[cb.name] !== undefined ? filterState[cb.name] : true;
                    });
                }
                // Nếu servlet cung cấp trạng thái (từ tham số URL), trạng thái từ servlet sẽ được giữ nguyên qua ${showX ? 'checked' : ''}
            });
        </script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script> 
        <jsp:include page="default/footer.jsp" />
    </body>
</html>
