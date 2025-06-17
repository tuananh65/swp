<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@page import="model.User"%>

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
        <nav>
            <jsp:include page="/default/header.jsp"/>
        </nav>

        <!-- Hero Banner -->
        <section class="hero-banner">
            <div class="hero-content">
                <div class="hero-overlay"></div>
                <img class="hero-image" src="${pageContext.request.contextPath}/images/Banner.jpg" alt="Banner">
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
                    <a href="${pageContext.request.contextPath}/CourseDetailServlet?courseId=${c.courseID}" class="dropdown-item all-course-item">
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
                    <a href="${pageContext.request.contextPath}/CourseDetailServlet?courseId=${fc.courseID}" class="dropdown-item all-course-item">
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

                        <form action="${pageContext.request.contextPath}/register-course" method="post" class="enroll-form">
                            <input type="hidden" name="courseId" value="${course.courseID}" />
                            <input type="hidden" name="basePrice" value="${course.salePrice}" />

                            <div class="package-selection">
                                <label for="packageId"><strong>Chọn gói học:</strong></label>
                                <select name="packageId" id="packageId" onchange="updatePrice()">
                                    <c:forEach var="pkg" items="${packageList}">
                                        <option value="${pkg.packageId}" data-modifier="${pkg.priceModifier}">
                                            ${pkg.name} (${pkg.durationInDays} ngày)
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>

                            <button type="submit" class="enroll-button">Register Now</button>
                        </form>

                        <div class="extra-info">
                            <p><strong>Ngày tạo:</strong> <fmt:formatDate value="${course.createdAt}" pattern="dd/MM/yyyy" /></p>
                            <p><strong>Ngày cập nhật:</strong> <fmt:formatDate value="${course.updatedAt}" pattern="dd/MM/yyyy" /></p>
                            <p><strong>Người tạo khóa học:</strong> ${course.userID}</p>
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
        <script>
function updatePrice() {
    const select = document.getElementById('packageId');
    const modifier = parseFloat(select.options[select.selectedIndex].dataset.modifier);
    const originalPrice = parseFloat(${course.salePrice});
    const totalPriceInput = document.querySelector('input[name="totalPrice"]');
    totalPriceInput.value = (originalPrice * modifier).toFixed(2);
}
</script>
        <!-- Footer -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script> 
        <jsp:include page="default/footer.jsp" />
    </body>
</html>
