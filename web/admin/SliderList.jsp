<%-- slider-list.jsp --%>
<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, model.Slider" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Sliders List - Soft Skills</title>

    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" />
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
    <%-- Đường dẫn đến file CSS cho Slider List --%>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/SliderLi.css" />

    <%-- Nếu bạn có CSS chung cho login hoặc các phần khác, giữ nguyên --%>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/login.css" /> 

</head>
<body>
    <div class="wrapper">
        <%-- Bao gồm header chung của bạn --%>
        <jsp:include page="/default/header.jsp"/> 

        <section class="hero-banner">
            <div class="hero-content">
                <div class="hero-overlay"></div>
                <img class="hero-image" src="${pageContext.request.contextPath}/images/Banner.jpg" alt="Banner">
                <div class="hero-text">
                    <h1>SLIDER LIST</h1>
                    <%-- Nếu bạn muốn thêm dòng "Home / Slider List" vào đây, hãy thêm vào như sau: --%>
                    <p><a href="${pageContext.request.contextPath}/home">Home</a> / Slider List</p>
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
                        <a href="${pageContext.request.contextPath}/settinglist">Setting List</a>
                    </button>
                </div>    
                    
                <div>
                    <button class="dropdown-toggle" onclick="toggleDropdown(this)">
                        <a href="${pageContext.request.contextPath}/admin/registrations">Registation List</a>
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

        <section class="content">
            <%-- Hiển thị thông báo thành công/lỗi --%>
            <c:if test="${not empty param.message}">
                <div class="success-message">${param.message}</div>
            </c:if>
            <c:if test="${not empty param.error}">
                <div class="error-message">${param.error}</div>
            </c:if>

            <div class="search-container">
                <form action="sliderlist" method="get">
                    <input type="text" class="search" name="search" value="${search}" placeholder="Search by Title">
                    <button type="submit">Search</button>
                    <input type="hidden" name="page" value="1">
                    <%-- Giữ lại các filter đã chọn nếu có (sử dụng filterStatus) --%>
                    <c:if test="${not empty filterStatus}"><input type="hidden" name="filterStatus" value="${filterStatus}"></c:if>
                </form>
            </div>

            <%-- Form bao quanh bảng và phân trang để giữ các tham số tìm kiếm/lọc khi phân trang --%>
            <form action="sliderlist" method="get">
                <input type="hidden" name="search" value="${search}"> 
                <table>
                    <thead>
                        <tr>
                            <th>SliderId</th>
                            <th>Title</th>
                            <th>Image</th>
                            <th>Backlink</th>
                            <th>
                                <select id="filterStatus" name="filterStatus" onchange="this.form.submit()">
                                    <option value="">Status</option>
                                    <option value="active" ${'active' eq filterStatus ? 'selected' : ''}>Active</option>
                                    <option value="inactive" ${'inactive' eq filterStatus ? 'selected' : ''}>Inactive</option>
                                </select>
                            </th>
                            <th>Functions</th>
                        </tr>
                    </thead>
                    <tbody id="sliderTableBody">
                        <c:forEach var="slider" items="${sliders}">
                            <tr>
                                <td data-label="SliderId">${slider.sliderId}</td>
                                <td data-label="Title" class="title-cell">${slider.title}</td>
                                <td data-label="Image" class="image-cell">
                                    <%-- Sử dụng contextPath cho đường dẫn ảnh --%>
                                    <img src="${pageContext.request.contextPath}/${slider.image}" alt="Slider Image" style="width: 100px; height: auto; object-fit: contain;">
                                </td>
                                <td data-label="Backlink" class="backlink-cell"><a href="${slider.backlink}" target="_blank">${slider.backlink}</a></td>
                                <td data-label="Status" class="status-cell">
                                    <c:choose>
                                        <c:when test="${slider.status eq 'active' or slider.status eq 'Active'}">Active</c:when>
                                        <c:otherwise>Inactive</c:otherwise>
                                    </c:choose>
                                </td>
                                <td data-label="Functions" class="function-buttons">
                                    <%-- Nút Hide/Show --%>
                                    <c:choose>
                                        <c:when test="${slider.status eq 'active' or slider.status eq 'Active'}">
                                            <form action="hideSlider" method="post" style="display:inline;">
                                                <input type="hidden" name="sliderId" value="${slider.sliderId}">
                                                <button type="submit" class="action-btn hide-btn">Hide</button>
                                            </form>
                                        </c:when>
                                        <c:otherwise>
                                            <form action="showSlider" method="post" style="display:inline;">
                                                <input type="hidden" name="sliderId" value="${slider.sliderId}">
                                                <button type="submit" class="action-btn show-btn">Show</button>
                                            </form>
                                        </c:otherwise>
                                    </c:choose>

                                    <%-- Nút Delete --%>
                                    <form action="deleteSlider" method="post" style="display:inline;" onsubmit="return confirm('Are you sure you want to delete this slider? This action cannot be undone.');">
                                        <input type="hidden" name="sliderId" value="${slider.sliderId}">
                                        <button type="submit" class="action-btn delete-btn">Delete</button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty sliders}">
                            <tr><td colspan="6">No sliders found.</td></tr>
                        </c:if>
                    </tbody>
                </table>
                <%-- Phần phân trang --%>
                <div class="pagination">
                    <c:if test="${currentPage > 1}">
                        <a href="sliderlist?page=${currentPage - 1}<c:if test="${not empty search}">&search=${search}</c:if><c:if test="${not empty filterStatus}">&filterStatus=${filterStatus}</c:if>" class="page-btn">&laquo;</a>
                    </c:if>

                    <c:forEach var="i" begin="1" end="${totalPages}">
                        <a href="sliderlist?page=${i}<c:if test="${not empty search}">&search=${search}</c:if><c:if test="${not empty filterStatus}">&filterStatus=${filterStatus}</c:if>"
                           class="page-btn ${i == currentPage ? 'active' : ''}">${i}</a>
                    </c:forEach>

                    <c:if test="${currentPage < totalPages}">
                        <a href="sliderlist?page=${currentPage + 1}<c:if test="${not empty search}">&search=${search}</c:if><c:if test="${not empty filterStatus}">&filterStatus=${filterStatus}</c:if>" class="page-btn">&raquo;</a>
                    </c:if>
                </div>
                <input type="hidden" name="page" value="${currentPage}">
            </form>
        </section>

        <%-- Bao gồm footer chung của bạn --%>
        <jsp:include page="/default/footer.jsp"/>
    </div>

    <script>
        // Có thể thêm JavaScript nếu cần cho các chức năng tương tác (ví dụ: xác nhận xóa)
    </script>
</body>
</html>