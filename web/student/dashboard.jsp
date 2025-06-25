<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%
    if (request.getAttribute("myEnrollments") == null) {
        response.sendRedirect(request.getContextPath() + "/student/dashboard");
        return;
    }
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard</title>
    <link href="${pageContext.request.contextPath}/css/styles.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/myRegistrations.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
</head>
<body>
    <!-- Navigation Bar -->
    <nav>
        <jsp:include page="/default/header.jsp"/>
    </nav>
        
    <div class="banner">
        <img src="${pageContext.request.contextPath}/images/Banner.jpg" alt="Banner">
        <div class="banner-text">
            <h1 class="banner-title">My Registrations</h1>
            <div class="banner-breadcrumb">
                <table>
                    <tr>
                        <td><a href="${pageContext.request.contextPath}/home">Student</a></td>
                        <td>Dashboard</td>
                    </tr>
                </table>
            </div>
        </div>
    </div>
   
                        
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
                <a href="${pageContext.request.contextPath}/mycourses">My Course</a>
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
                        
                        
    <!-- Main Content -->
    <main class="myRegistrations-main">
        <div class="myRegistrations-container">
            <div class="myRegistrations-content-wrapper">

                <!-- Sidebar -->
                <aside class="myRegistrations-sidebar">
                    <!-- Search Subject -->
                    <div class="myRegistrations-search-section">
                        <div class="myRegistrations-search-box">
                            <form action="${pageContext.request.contextPath}/student/dashboard" method="get" class="myRegistrations-search-box">
                                <input type="text" name="search" placeholder="Search Subject" class="myRegistrations-search-input"
                                       value="${fn:escapeXml(param.search)}" />
                                <button class="myRegistrations-search-submit" type="submit"><i class="fas fa-search"></i></button>
                            </form>
                        </div>
                    </div>
                    <!-- Subject Category -->
                    <div class="myRegistrations-category-section">
                        <h3 class="myRegistrations-category-title">SUBJECT CATEGORY</h3>
                        <ul class="myRegistrations-category-list">
                            <!-- Hiển thị tất cả category -->
                            <li class="myRegistrations-category-item ${empty param.category ? 'myRegistrations-active' : ''}">
                                <a href="${pageContext.request.contextPath}/student/dashboard" class="myRegistrations-category-link">
                                    All <i class="fas fa-chevron-right"></i>
                                </a>
                            </li>
                            <!-- Hiển thị danh sách category không trùng lặp -->
                            <c:set var="uniqueCourses" value="" />
                            <c:forEach var="e" items="${myEnrollments}">
                                <c:if test="${not fn:contains(uniqueCourses, e.courseName)}">
                                    <li class="myRegistrations-category-item ${param.category == e.courseName ? 'myRegistrations-active' : ''}">
                                        <a href="${pageContext.request.contextPath}/student/dashboard?category=${fn:escapeXml(e.courseName)}" class="myRegistrations-category-link">
                                            ${fn:escapeXml(e.courseName)} <i class="fas fa-chevron-right"></i>
                                        </a>
                                    </li>
                                    <c:set var="uniqueCourses" value="${uniqueCourses},${e.courseName}" />
                                </c:if>
                            </c:forEach>
                        </ul>
                    </div>
                </aside>

                <!-- Content Cards -->
                <div class="myRegistrations-cards-container">
                    <!-- Alerts -->
                    <c:if test="${not empty sessionScope.message}">
                        <div class="myRegistrations-alert myRegistrations-alert-success">${sessionScope.message}</div>
                        <c:remove var="message" scope="session" />
                    </c:if>
                    <c:if test="${not empty sessionScope.error}">
                        <div class="myRegistrations-alert myRegistrations-alert-danger">${sessionScope.error}</div>
                        <c:remove var="error" scope="session" />
                    </c:if>

                    <c:choose>
                        <c:when test="${not empty myEnrollments}">
                            <c:forEach var="e" items="${myEnrollments}">
                                <div class="myRegistrations-card">
                                    <div class="myRegistrations-card-content">
                                        <div class="myRegistrations-card-left">
                                            <div class="myRegistrations-card-info">
                                                <p><strong>ID:</strong> ${e.enrollmentId}</p>
                                                <p><strong>Subject:</strong> ${e.courseName}</p>
                                                <p><strong>Registration Time:</strong> <fmt:formatDate value="${e.enrollmentDate}" pattern="dd/MM/yyyy" /></p>
                                                <p><strong>Package:</strong> ${e.packageName}</p>
                                            </div>
                                        </div>
                                        <div class="myRegistrations-card-right">
                                            <div class="myRegistrations-card-details">
                                                <p><strong>Total Cost:</strong> <fmt:formatNumber value="${e.totalPrice}" type="number" groupingUsed="true"/>đ</p>
                                                <p><strong>Status:</strong>
                                                    <c:choose>
                                                        <c:when test="${e.status == 'Submitted'}">
                                                            <a href="dashboard?action=confirm&id=${e.enrollmentId}" class="myRegistrations-status myRegistrations-submitted"
                                                               onclick="return confirm('Are you sure you want to confirm this registration?');">
                                                               Submitted
                                                            </a>
                                                        </c:when>
                                                        <c:when test="${e.status == 'Confirmed'}">
                                                            <span class="myRegistrations-status myRegistrations-confirmed">Confirmed</span>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="myRegistrations-status">${e.status}</span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </p>
                                                <p><strong>Valid From:</strong> <fmt:formatDate value="${e.validFrom}" pattern="dd/MM/yyyy" /></p>
                                                <p><strong>Valid To:</strong> <fmt:formatDate value="${e.validTo}" pattern="dd/MM/yyyy" /></p>
                                            </div>
                                        </div>
                                    </div>
                                    <c:if test="${e.status == 'Submitted'}">
                                        <div class="myRegistrations-card-actions">
                                            <a href="edit-registration.jsp?id=${e.enrollmentId}" class="myRegistrations-btn myRegistrations-btn-edit">EDIT</a>
                                            <a href="dashboard?action=cancel&id=${e.enrollmentId}" class="myRegistrations-btn myRegistrations-btn-delete"
                                               onclick="return confirm('Are you sure you want to cancel this registration?');">CANCEL</a>
                                        </div>
                                    </c:if>
                                </div>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <div class="myRegistrations-empty-state">
                                <p>You have not registered for any course yet.</p>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </main>
    <!-- Footer -->
    <jsp:include page="/default/footer.jsp"/>            
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>