<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
<jsp:include page="/default/header.jsp">
    <jsp:param name="title" value="REGISTRATION LIST"/>
</jsp:include>

<jsp:include page="/default/banner.jsp">
    <jsp:param name="bannerTitle" value="REGISTRATION LIST"/>
</jsp:include>

<jsp:include page="/default/sidebar.jsp" />
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
                <a href="${pageContext.request.contextPath}/sliderlist">Slider List</a>
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
<body>
    
    <style>
        @import url('${pageContext.request.contextPath}/css/registration-list.css');
    </style>

    <main class="container mt-5 mb-5">
       <p>Current RoleID: ${currentUser.roleId}</p>

        <c:if test="${not empty error}">
            <div class="alert alert-danger">${error}</div>
        </c:if>

        <h2 class="mb-4"><i class="bi bi-clipboard-data"></i> Registration List</h2>

        <!-- Bộ lọc -->
        <form method="get" action="registrations" class="form-filter">
            <div class="form-group">
                <label class="form-label fw-bold">Min cost: (₫):</label>
                <input type="number" name="minPrice" class="form-control" value="${minPrice}">
            </div>
            <div class="form-group">
                <label class="form-label fw-bold">Max cost: (₫):</label>
                <input type="number" name="maxPrice" class="form-control" value="${maxPrice}">
            </div>
            <div class="form-group">
                <label class="form-label fw-bold">Course:</label>
                <input type="text" name="subject" class="form-control" value="${subject}">
            </div>
            <div class="form-group">
                <label class="form-label fw-bold">From Date:</label>
                <input type="date" name="fromDate" class="form-control" value="${fromDate}">
            </div>
            <div class="form-group">
                <label class="form-label fw-bold">To Date:</label>
                <input type="date" name="toDate" class="form-control" value="${toDate}">
            </div>
            <div style="position: relative; display: inline-block; margin: 1.5rem 0;padding-top:40px;margin-bottom:-1px">
                <!-- Toggle Button -->
                <button type="button" onclick="toggleCustomMenu()" style="
                       
                        background: #f59e0b;
                        color: white;
                        border: none;
                        padding: 0.5rem 1rem;
                        font-size: 0.95rem;
                        border-radius: 0.375rem;
                        cursor: pointer;
                        font-family: 'Inter', sans-serif;
                        ">
                    Custom
                </button>

                <!-- Hidden Options Panel -->
                <div id="customMenu" style="
                     display: none;
                     position: absolute;
                     top: 110%;
                     left: 0;
                     background: #f9fafb;
                     border: 1px solid #d1d5db;
                     padding: 1rem;
                     border-radius: 0.5rem;
                     box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
                     z-index: 999;
                     min-width: 200px;
                     ">
                    <c:forEach var="col" items="${['image', 'package', 'updatedBy']}">
                        <label style="display: block; margin-bottom: 0.75rem; color: #1f2937; font-size: 0.95rem;">
                            <input type="checkbox" name="cols" value="${col}"
                                   <c:if test="${not empty displayColumns and fn:contains(displayColumns, col)}">checked</c:if> />
                            <c:choose>
                                <c:when test="${col == 'image'}">Images</c:when>
                                <c:when test="${col == 'package'}">Package</c:when>
                                <c:when test="${col == 'updatedBy'}">Instructor</c:when>
                            </c:choose>
                        </label>
                    </c:forEach>
                </div>
            </div>

            <!-- Tùy chọn hiển thị cột -->


            <div class="form-buttons">
                <button type="submit" class="btn btn-primary">Apply</button>
                <a href="registrations" class="btn btn-secondary">Remove</a>
            </div>
        </form>
        <script>
// Khi submit form, nếu không có checkbox "cols" nào được tick, thêm input ẩn để giữ trạng thái
            document.querySelector("form.form-filter").addEventListener("submit", function (e) {
                const checkboxes = document.querySelectorAll("input[name='cols']:checked");
                if (checkboxes.length === 0) {
                    const hidden = document.createElement("input");
                    hidden.type = "hidden";
                    hidden.name = "cols";
                    hidden.value = "";
                    this.appendChild(hidden);
                }
            });
            function toggleCustomMenu() {
                const menu = document.getElementById('customMenu');
                menu.style.display = (menu.style.display === 'block') ? 'none' : 'block';
            }

// Optional: Click outside to close
            document.addEventListener('click', function (e) {
                const button = e.target.closest('button');
                const menu = document.getElementById('customMenu');
                if (!e.target.closest('#customMenu') && (!button || button.textContent.trim() !== 'Custom')) {
                    menu.style.display = 'none';
                }
            });
        </script>


        <!-- Bảng danh sách -->
        <div class="table-responsive mt-4">
            <table class="table table-bordered align-middle">
                <thead class="table-light">
                    <tr>
                        <th>ID</th>
                        <th>Email</th>
                        <th>Course</th>
                            <c:if test="${fn:contains(displayColumns, 'image')}">
                            <th>Image</th>
                            </c:if>
                            <c:if test="${fn:contains(displayColumns, 'package')}">
                            <th>Package</th>
                            </c:if>
                        <th>Cost</th>
                        <th>Status</th>
                        <th>From</th>
                        <th>To</th>
                            <c:if test="${fn:contains(displayColumns, 'updatedBy')}">
                            <th>Instructor</th>
                            </c:if>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="e" items="${enrollments}" varStatus="loop">
                        <tr>
                            <td>${e.enrollmentId}</td>
                            <td>${e.userEmail}</td>
                            <td>${e.courseName}</td>
                            <c:if test="${fn:contains(displayColumns, 'image')}">
                                <td>
                                    <c:choose>
                                        <c:when test="${not empty e.courseThumbnail}">
                                            <img src="${e.courseThumbnail}" class="table-img" style="width:60px"/>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="text-muted fst-italic">None</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                            </c:if>
                            <c:if test="${fn:contains(displayColumns, 'package')}">
                                <td>${e.packageName}</td>
                            </c:if>
                            <td><fmt:formatNumber value="${e.totalPrice}" type="number" groupingUsed="true"/>₫</td>
                            <td>
                                <c:choose>
                                    <c:when test="${e.status == 'Pending'}">
                                        <span class="badge bg-warning text-dark">Pending</span>
                                    </c:when>
                                    <c:when test="${e.status == 'Approved'}">
                                        <span class="badge bg-success">Approved</span>
                                    </c:when>
                                    <c:when test="${e.status == 'Rejected'}">
                                        <span class="badge bg-danger">Rejected</span>
                                    </c:when>
                                    <c:otherwise>
                                        ${e.status}
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td><fmt:formatDate value="${e.validFrom}" pattern="yyyy-MM-dd"/></td>
                            <td><fmt:formatDate value="${e.validTo}" pattern="yyyy-MM-dd"/></td>
                            <c:if test="${fn:contains(displayColumns, 'updatedBy')}">
                                <td>${e.updatedByName}</td>
                            </c:if>
                            <td>
                               <a href="edit-registration?id=${e.enrollmentId}" class="btn btn-warning btn-sm">Modify</a>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty enrollments}">
                        <tr>
                            <td colspan="11" class="text-center text-muted">Empty</td>
                        </tr>
                    </c:if>
                </tbody>
            </table>

        </div>

        <!-- Phân trang đầy đủ -->
        <c:if test="${totalPages >= 1}">
            <nav aria-label="Pagination">
                <ul class="pagination">

                    <!-- ≪ Trang đầu -->
                    <c:if test="${currentPage > 1}">
                        <c:url var="firstPageUrl" value="registrations">
                            <c:param name="page" value="1" />
                            <c:param name="subject" value="${subject}" />
                            <c:param name="status" value="${status}" />
                            <c:param name="fromDate" value="${fromDate}" />
                            <c:param name="toDate" value="${toDate}" />
                            <c:param name="minPrice" value="${minPrice}" />
                            <c:param name="maxPrice" value="${maxPrice}" />
                            <c:forEach var="col" items="${displayColumns}">
                                <c:param name="cols" value="${col}" />
                            </c:forEach>
                        </c:url>
                        <li class="page-item">
                            <a class="page-link" href="${firstPageUrl}">&laquo;</a>
                        </li>
                    </c:if>

                    <!-- Số trang -->
                    <c:forEach begin="1" end="${totalPages}" var="i">
                        <c:url var="pageUrl" value="registrations">
                            <c:param name="page" value="${i}" />
                            <c:param name="subject" value="${subject}" />
                            <c:param name="status" value="${status}" />
                            <c:param name="fromDate" value="${fromDate}" />
                            <c:param name="toDate" value="${toDate}" />
                            <c:param name="minPrice" value="${minPrice}" />
                            <c:param name="maxPrice" value="${maxPrice}" />
                            <c:forEach var="col" items="${displayColumns}">
                                <c:param name="cols" value="${col}" />
                            </c:forEach>
                        </c:url>
                        <li class="page-item ${i == currentPage ? 'active' : ''}">
                            <a class="page-link" href="${pageUrl}">${i}</a>
                        </li>
                    </c:forEach>

                    <!-- » Trang kế -->
                    <c:if test="${currentPage < totalPages}">
                        <c:url var="nextPageUrl" value="registrations">
                            <c:param name="page" value="${currentPage + 1}" />
                            <c:param name="subject" value="${subject}" />
                            <c:param name="status" value="${status}" />
                            <c:param name="fromDate" value="${fromDate}" />
                            <c:param name="toDate" value="${toDate}" />
                            <c:param name="minPrice" value="${minPrice}" />
                            <c:param name="maxPrice" value="${maxPrice}" />
                            <c:forEach var="col" items="${displayColumns}">
                                <c:param name="cols" value="${col}" />
                            </c:forEach>
                        </c:url>
                        <li class="page-item">
                            <a class="page-link" href="${nextPageUrl}">&gt;</a>
                        </li>
                    </c:if>

                    <!-- ≫ Trang cuối -->
                    <c:if test="${currentPage < totalPages}">
                        <c:url var="lastPageUrl" value="registrations">
                            <c:param name="page" value="${totalPages}" />
                            <c:param name="subject" value="${subject}" />
                            <c:param name="status" value="${status}" />
                            <c:param name="fromDate" value="${fromDate}" />
                            <c:param name="toDate" value="${toDate}" />
                            <c:param name="minPrice" value="${minPrice}" />
                            <c:param name="maxPrice" value="${maxPrice}" />
                            <c:forEach var="col" items="${displayColumns}">
                                <c:param name="cols" value="${col}" />
                            </c:forEach>
                        </c:url>
                        <li class="page-item">
                            <a class="page-link" href="${lastPageUrl}">&raquo;</a>
                        </li>
                    </c:if>

                </ul>
            </nav>
        </c:if>


        <!-- Nút thêm mới -->
        <div class="text-start mt-3">
            <a href="create-registration.jsp" class="btn btn-success">+ New </a>
        </div>
    </main>







    <!-- Nhúng widget chatbot đã tối ưu -->
    <jsp:include page="/chatbot/chatbot-widget.jsp" />

    <!-- Footer -->
    <jsp:include page="/default/footer.jsp" />
</body>
</html>
