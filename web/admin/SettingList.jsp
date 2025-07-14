<%-- settinglist.jsp --%>
<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, model.Setting" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Settings List - Soft Skills</title>

    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" />
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/SettingLi.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/MyCourses.css"> <%-- Cho banner --%>

    <style>
        /* Các style riêng cho banner và khoảng cách nội dung */
        /* Đây là nơi để tùy chỉnh nhỏ mà không cần tạo file CSS mới */
        .hero-text h1 {
            font-size: 3em; /* Kích thước chữ lớn hơn một chút nếu muốn */
            text-shadow: 2px 2px 4px rgba(0,0,0,0.3); /* Thêm đổ bóng chữ cho tiêu đề */
        }
        .hero-text p {
            font-size: 1.2em; /* Kích thước chữ cho đường dẫn */
        }

        /* Đảm bảo content-wrapper không bị header che khuất nếu header cố định */
        .content-wrapper {
            display: flex;
            justify-content: center;
            align-items: flex-start;
            padding: 20px 0; /* Padding trên dưới 20px, hai bên 0 để nội dung có thể giãn ra */
            width: 100%; /* Đảm bảo wrapper chiếm toàn bộ chiều rộng */
        }

        /* Centering the content inside the wrapper */
        .content {
            width: 90%; /* Hoặc một max-width cố định như 1200px */
            max-width: 1200px; /* Giới hạn chiều rộng tối đa */
            margin: 0 auto; /* Căn giữa nội dung */
            padding: 0 20px; /* Padding hai bên cho nội dung */
            box-sizing: border-box; /* Đảm bảo padding không làm tăng chiều rộng */
        }
    </style>
</head>
<body>
    <div class="wrapper">
        <jsp:include page="/default/header.jsp"/>

        <%-- Cấu trúc banner giống hệt MyCourses.jsp và các trang admin khác --%>
        <section class="hero-banner">
            <div class="hero-content">
                <%-- Comment out or remove this line to remove the overlay --%>
                <%-- <div class="hero-overlay"></div> --%>
                <img class="hero-image" src="${pageContext.request.contextPath}/images/Banner.jpg" alt="Banner">
                <div class="hero-text">
                    <h1>SETTINGS LIST</h1>
                    <p><a href="${pageContext.request.contextPath}/home">Home</a> / Settings</p>
                </div>
            </div>
        </section>
        <%-- Hết cấu trúc banner --%>
        

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
                        <a href="${pageContext.request.contextPath}/sliderlist">Slider List</a>
                    </button>
                </div>
<<<<<<< HEAD
                    
                <div>
                    <button class="dropdown-toggle" onclick="toggleDropdown(this)">
                        <a href="${pageContext.request.contextPath}/admin/subjectList">Subject List</a>
                    </button>
                </div>  
                    
              
=======
                    <div>
                    <button class="dropdown-toggle" onclick="toggleDropdown(this)">
                        <a href="${pageContext.request.contextPath}/subjectDetail">Subject Detail</a>
                    </button>
                </div>
                    
>>>>>>> 9f0d8d78ba253b25258b2d43672933c3caa423b4
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

        <div class="content-wrapper"> <%-- Thêm content-wrapper ở đây để áp dụng margin-top và căn giữa --%>
            <section class="content">
                <c:if test="${not empty param.message}">
                    <div class="success-message">${param.message}</div>
                </c:if>
                <c:if test="${not empty param.error}">
                    <div class="error-message">${param.error}</div>
                </c:if>
                <div class="search-container">
                    <form action="${pageContext.request.contextPath}/settinglist" method="get" class="search-form"> <%-- Thêm class search-form --%>
                        <input type="text" class="search-input" name="search" value="${search}" placeholder="Search by type or value"> <%-- Đổi class --%>
                        <button type="submit" class="search-button"><i class="fas fa-search"></i></button> <%-- Đổi class và thêm icon --%>
                        <input type="hidden" name="page" value="1">
                         <input type="hidden" name="filterType" value="${selectedType}">
                         <input type="hidden" name="filterStatus" value="${selectedStatus}">
                    </form>
                    <a href="${pageContext.request.contextPath}/addSetting" class="add-btn"><i class="fas fa-plus-circle"></i> Add new Setting</a> <%-- Đổi class và thêm icon --%>
                </div>

                <form action="${pageContext.request.contextPath}/settinglist" method="get" id="filterForm"> <%-- Thêm ID cho form để submit dễ hơn --%>
                    <input type="hidden" name="search" value="${search}">
                    <input type="hidden" name="page" value="${currentPage}">
                    <table>
                        <thead>
                            <tr>
                                <th>Setting ID</th>
                                <th>
                                    <select id="filterType" name="filterType" onchange="document.getElementById('filterForm').submit()">
                                        <option value="">All Types</option> <%-- Đổi text --%>
                                        <c:forEach var="type" items="${uniqueTypes}">
                                            <option value="${type}" ${type == selectedType ? 'selected' : ''}>${type}</option>
                                        </c:forEach>
                                    </select>
                                </th>
                                <th>Value</th>
                                <th>Order</th>
                                <th>
                                    <select id="filterStatus" name="filterStatus" onchange="document.getElementById('filterForm').submit()">
                                        <option value="">All Statuses</option> <%-- Đổi text --%>
                                        <option value="Active" ${'Active' == selectedStatus ? 'selected' : ''}>Active</option>
                                        <option value="Inactive" ${'Inactive' == selectedStatus ? 'selected' : ''}>Inactive</option>
                                    </select>
                                </th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody id="settingTableBody">
                            <c:forEach var="setting" items="${settingList}">
                                <tr>
                                    <td>${setting.settingID}</td>
                                    <td class="type-cell">${setting.type}</td>
                                    <td class="value-cell">${setting.value}</td>
                                    <td class="order-cell">${setting.order}</td>
                                    <td class="status-cell">
                                        <span class="status ${setting.status eq 'Active' ? 'status-active' : 'status-inactive'}">
                                            ${setting.status}
                                        </span>
                                    </td>
                                    <td class="action-buttons">
                                        <a href="${pageContext.request.contextPath}/editSetting?settingID=${setting.settingID}" class="action-btn view-btn" title="View/Edit Setting"><i class="fas fa-eye"></i></a>
                                        <form action="${pageContext.request.contextPath}/deleteSetting" method="post" style="display:inline;" onsubmit="return confirm('Are you sure you want to delete setting ID: ${setting.settingID}?');">
                                            <input type="hidden" name="settingID" value="${setting.settingID}">
                                            <button type="submit" class="action-btn delete-btn" title="Delete Setting"><i class="fas fa-trash-alt"></i></button>
                                        </form>
                                    </td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty settingList}">
                                <tr><td colspan="6" class="no-results">No settings found matching your criteria.</td></tr> <%-- Thêm class --%>
                            </c:if>
                        </tbody>
                    </table>
                    <%-- Các hidden input cho form filter --%>
                </form>

                <div class="pagination">
                    <c:if test="${currentPage > 1}">
                        <a href="${pageContext.request.contextPath}/settinglist?page=${currentPage - 1}${not empty search ? '&search=${search}' : ''}${not empty selectedType ? '&filterType=${selectedType}' : ''}${not empty selectedStatus ? '&filterStatus=${selectedStatus}' : ''}" class="page-link">&laquo; Previous</a>
                    </c:if>

                    <c:forEach var="i" begin="1" end="${totalPages}">
                        <a href="${pageContext.request.contextPath}/settinglist?page=${i}${not empty search ? '&search=${search}' : ''}${not empty selectedType ? '&filterType=${selectedType}' : ''}${not empty selectedStatus ? '&filterStatus=${selectedStatus}' : ''}" class="page-link ${i == currentPage ? 'active' : ''}">${i}</a>
                    </c:forEach>

                    <c:if test="${currentPage < totalPages}">
                        <a href="${pageContext.request.contextPath}/settinglist?page=${currentPage + 1}${not empty search ? '&search=${search}' : ''}${not empty selectedType ? '&filterType=${selectedType}' : ''}${not empty selectedStatus ? '&filterStatus=${selectedStatus}' : ''}" class="page-link">Next &raquo;</a>
                    </c:if>
                </div>
            </section>
        </div>
        <jsp:include page="/default/footer.jsp"/>
        
    </div>
</body>
</html>