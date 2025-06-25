<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Courses</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/MyCourses.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
</head>
<body>
    <nav>
        <jsp:include page="/default/header.jsp"/>
    </nav>
    <section class="hero-banner">
        <div class="hero-content">
            <div class="hero-overlay"></div>
            <img class="hero-image" src="${pageContext.request.contextPath}/images/Banner.jpg" alt="Banner">
            <div class="hero-text">
                <h1>MY COURSES</h1>
                <p><a href="${pageContext.request.contextPath}/home">Home</a> / My Courses</p>
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
"> <i class="bi bi-list"></i>
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

    <div id="userSection" style="display: flex; flex-direction: column; gap: 1.5rem;">
        <div>
            <button class="dropdown-toggle" onclick="toggleDropdown(this)">
                <a href="${pageContext.request.contextPath}/view/changePassword.jsp">Change Password</a>
            </button>
        </div>
        <div>
            <button class="dropdown-toggle" onclick="toggleDropdown(this)">
                <a href="${pageContext.request.contextPath}/profile?id=${sessionScope.currentUser.userId}">Profile</a>
            </button>
        </div>
        <div>
            <button class="dropdown-toggle" onclick="toggleDropdown(this)">
                <a href="${pageContext.request.contextPath}/student/dashboard">My Registation</a>
            </button>
        </div>    
        <div>
            <button class="dropdown-toggle" onclick="toggleDropdown(this)">
                <a href="${pageContext.request.contextPath}/auth?action=logout">Sign out</a>
            </button>
        </div> 
    </div>
</div>

<!-- CSS -->
<style>
    .dropdown-toggle {
        background-color: #6366f1;
        color: #111827;
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
        background-color: #f3f4f6;
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

    <div class="controls">
        <label>
            <input type="checkbox" id="toggleThumbnail" checked> Thumbnail
        </label>
        <label>
            <input type="checkbox" id="toggleTitle" checked> Title
        </label>
        <label>
            <input type="checkbox" id="toggleTagline" checked> Tagline
        </label>
        <label>
            <input type="checkbox" id="toggleEnrollButton" checked> Enroll Button
        </label>
        <label>
            Maximum courses per page:
            <input type="number" id="coursesPerPageInput" value="9" min="1" max="999">
        </label>
    </div>

    <div class="course-grid" id="courseGrid">
        <c:forEach var="course" items="${courses}">
            <div class="course-card">
                <img class="course-thumbnail" src="${course.courseThumbnail}" alt="Course Image">
                <div class="course-category">${course.briefInfo}</div>
                <div class="course-title">${course.courseName}</div>
                <div class="course-tagline">${course.tagLine}</div>
                <div class="course-rating">
                    <i class="fa-solid fa-star"></i><i class="fa-solid fa-star"></i><i class="fa-solid fa-star"></i><i class="fa-solid fa-star"></i><i class="fa-regular fa-star"></i> 4.5k
                </div>
                <div class="course-details">Lesson 10 • 19h 30m</div>
                <button class="enroll-btn">Enroll <i class="fa-solid fa-arrow-right"></i></button>
            </div>
        </c:forEach>
        <c:if test="${empty courses}">
            <p style="text-align: center; color: #6c757d; padding: 20px;">No courses found.</p>
        </c:if>
    </div>

    <div class="pagination" id="paginationControls">
        <%-- Phần này sẽ được tạo động bằng JavaScript --%>
    </div>

    <jsp:include page="/default/footer.jsp"/>

    <script>
        $(document).ready(function() {
            const courseCards = $('.course-card');
            const coursesPerPageInput = $('#coursesPerPageInput');
            const paginationControls = $('#paginationControls');

            let currentPage = 1;
            let coursesPerPage = parseInt(coursesPerPageInput.val());

            function applyFilters() {
                $('.course-thumbnail').toggle($('#toggleThumbnail').is(':checked'));
                $('.course-title').toggle($('#toggleTitle').is(':checked'));
                $('.course-tagline').toggle($('#toggleTagline').is(':checked'));
                
                // Logic cho course-category vẫn theo tagline
                if ($('#toggleTagline').is(':checked')) {
                    $('.course-category').show();
                } else {
                    $('.course-category').hide();
                }
                $('.enroll-btn').toggle($('#toggleEnrollButton').is(':checked'));

                updatePaginationAndDisplayCourses();
            }
            
            // Phân chia trang sau khi nhập số trang 
            function updatePaginationAndDisplayCourses() {
                let enteredValue = parseInt(coursesPerPageInput.val());

                if (isNaN(enteredValue) || enteredValue <= 0) {
                    coursesPerPage = 9;
                } else {
                    coursesPerPage = enteredValue;
                }

                const totalPages = Math.ceil(courseCards.length / coursesPerPage);
                // Không cho currentPage > TotalPage và <1
                currentPage = Math.min(currentPage, totalPages > 0 ? totalPages : 1);
                currentPage = Math.max(1, currentPage);

                courseCards.hide();
                // Tính vị trí bắt đầu và kết thúc
                const startIndex = (currentPage - 1) * coursesPerPage;
                // (1-1)*9 = 0
                const endIndex = startIndex + coursesPerPage;
                // 0 + 9 = 9
                courseCards.slice(startIndex, endIndex).show();

                renderPaginationButtons(totalPages);
            }
            
            // Tạo các nút phân trang
            function renderPaginationButtons(totalPages) {
                paginationControls.empty();

                if (totalPages > 1) {
                    const prevButton = $('<button>').html('&laquo;').prop('disabled', currentPage === 1);
                    prevButton.on('click', function() {
                        if (currentPage > 1) {
                            currentPage--;
                            updatePaginationAndDisplayCourses();
                        }
                    });
                    paginationControls.append(prevButton);

                    for (let i = 1; i <= totalPages; i++) {
                        // Thêm class active để tô màu cũ
                        const pageButton = $('<button>').text(i).addClass(i === currentPage ? 'active' : '');
                        pageButton.on('click', function() {
                            currentPage = i;
                            updatePaginationAndDisplayCourses();
                        });
                        // Thêm nút trang cho giao diện
                        paginationControls.append(pageButton);
                    }
                    // nút >> khi ở trang cuối sẽ bị ẩn
                    const nextButton = $('<button>').html('&raquo;').prop('disabled', currentPage === totalPages);
                    nextButton.on('click', function() {
                        if (currentPage < totalPages) {
                            currentPage++;
                            updatePaginationAndDisplayCourses();
                        }
                    });
                    // Thêm nút >> vào phân trang
                    paginationControls.append(nextButton);
                }
            }
            
            // Sau khi tick hoặc bỏ thì applyFilter ẩn hiện đúng
            $('#toggleThumbnail').on('change', applyFilters);
            $('#toggleTitle').on('change', applyFilters);
            $('#toggleTagline').on('change', applyFilters);
            $('#toggleEnrollButton').on('change', applyFilters);

            coursesPerPageInput.on('input', function() {
                updatePaginationAndDisplayCourses();
            });

            coursesPerPageInput.on('blur', function() {
                let enteredValue = parseInt(coursesPerPageInput.val());
                if (isNaN(enteredValue) || enteredValue <= 0) {
                    coursesPerPageInput.val(9);
                    updatePaginationAndDisplayCourses();
                }
            });

            // Nếu không có khóa học nào thì báo không có
            if (courseCards.length === 0) {
                 $('#courseGrid').html('<p style="text-align: center; color: #6c757d; padding: 20px;">No courses found.</p>');
                 paginationControls.empty(); // No pagination if no courses
            } else {
                applyFilters(); // Initial render if courses exist
            }
        });
    </script>
</body>
</html>