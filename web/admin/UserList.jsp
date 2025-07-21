<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, model.User" %> <%-- Đảm bảo model.User là class chính xác --%>
<%@ page import="dto.UserWithRoleDTO" %> <%-- Đảm bảo dto.UserWithRoleDTO là class chính xác --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Users List - Soft Skills</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" />
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">

    <%-- Đảm bảo đường dẫn đến CSS là chính xác --%>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/UserList.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/header.css"> <%-- Nếu bạn có header.css riêng --%>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/footer.css"> <%-- Nếu bạn có footer.css riêng --%>
</head>
<body>
<div class="wrapper">
    <%-- Header (Đảm bảo đường dẫn include là chính xác) --%>
    <jsp:include page="/default/header.jsp"/>

    <%-- Nút này được tạo bằng JS để đảm bảo vị trí và hành vi đúng --%>
    <%-- KHÔNG CẦN ĐẶT BUTTON Ở ĐÂY NỮA, NÓ SẼ ĐƯỢC JS THÊM VÀO DOM SAU NÀY --%>

    <div id="courseDropdownContainer">
        <div id="userSection">
            <div>
                <button class="dropdown-toggle">
                    <a href="${pageContext.request.contextPath}/view/changePassword.jsp">Change Password</a>
                </button>
            </div>
            <div>
                <button class="dropdown-toggle">
                    <a href="${pageContext.request.contextPath}/profile?id=${sessionScope.currentUser.userId}">Profile</a>
                </button>
            </div>
            <div>
                <button class="dropdown-toggle">
                    <a href="${pageContext.request.contextPath}/admin/dashboard">Dash Board</a>
                </button>
            </div>
            <div>
                <button class="dropdown-toggle">
                    <a href="${pageContext.request.contextPath}/sliderlist">Slider List</a>
                </button>
            </div>
            <div>
                <button class="dropdown-toggle">
                    <a href="${pageContext.request.contextPath}/userlist">User List</a>
                </button>
            </div>
            <div>
                <button class="dropdown-toggle">
                    <a href="${pageContext.request.contextPath}/admin/subjectList">Subject List</a>
                </button>
            </div>
            <div>
                <button class="dropdown-toggle">
                    <a href="${pageContext.request.contextPath}/admin/registrations">Registration List</a>
                </button>
            </div>
            <div>
                <button class="dropdown-toggle">
                    <a href="${pageContext.request.contextPath}/auth?action=logout">Sign out</a>
                </button>
            </div>
        </div>
    </div>
    <%-- Banner Section - Đã cập nhật để khớp với cấu trúc của Setting List --%>
    <section class="hero-banner">
        <div class="hero-content">
            <%-- Đảm bảo đường dẫn ảnh là chính xác và hình ảnh phù hợp với banner --%>
            <img class="hero-image" src="${pageContext.request.contextPath}/images/Banner.jpg" alt="Banner">
            <div class="hero-overlay"></div> <%-- ĐÃ THÊM LỚP PHỦ NÀY ĐỂ KHỚP VỚI SETTING LIST --%>
            <div class="hero-text">
                <h1>USERS LIST</h1>
                <p><a href="${pageContext.request.contextPath}/home">Home</a> / Users</p>
            </div>
        </div>
    </section>

    <div class="content-wrapper"> <%-- Thêm div này để áp dụng styles content-wrapper --%>
        <section class="content">

            <%-- Success/Error Messages --%>
            <c:if test="${not empty successMessage}">
                <div class="success-message">
                    <i class="fas fa-check-circle"></i> ${successMessage}
                </div>
            </c:if>
            <c:if test="${not empty errorMessage}">
                <div class="error-message">
                    <i class="fas fa-times-circle"></i> ${errorMessage}
                </div>
            </c:if>

            <div class="search-container">
                <form action="userlist" method="get" class="search-form"> <%-- Cập nhật class --%>
                    <input type="text" class="search-input" name="search" value="${search}" placeholder="Search by name, email, or phone..."> <%-- Cập nhật class và placeholder --%>
                    <button type="submit" class="search-button"><i class="fas fa-search"></i></button> <%-- Cập nhật class và thêm icon --%>
                    <input type="hidden" name="page" value="1">
                    <%-- Các hidden input này sẽ được cập nhật bởi JS khi applyFilters() --%>
                    <input type="hidden" name="role" id="hiddenFilterRole" value="${filterRole}">
                    <input type="hidden" name="gender" id="hiddenFilterGender" value="${filterGender}">
                    <input type="hidden" name="status" id="hiddenFilterStatus" value="${filterStatus}">
                </form>
                <a href="${pageContext.request.contextPath}/addUser" class="add-user-btn">
                    <i class="fas fa-plus-circle"></i> Add User
                </a>
            </div>

            <table>
                <thead>
                    <tr>
                        <th>UserID</th>
                        <th>Avatar</th>
                        <th>Full Name</th>
                        <th>
                            <%-- Select lọc Role --%>
                            <select id="filterRole" onchange="applyFilters()">
                                <option value="">Role</option>
                                <option value="Admin" ${filterRole eq 'Admin' ? 'selected' : ''}>Admin</option>
                                <option value="Instructor" ${filterRole eq 'Instructor' ? 'selected' : ''}>Instructor</option>
                                <option value="Student" ${filterRole eq 'Student' ? 'selected' : ''}>Student</option>
                                <%-- Bạn nên động popuplate các vai trò này từ backend của mình nếu có thể --%>
                            </select>
                        </th>
                        <th>
                            <%-- Select lọc Gender --%>
                            <select id="filterGender" onchange="applyFilters()">
                                <option value="">Gender</option>
                                <option value="Male" ${filterGender eq 'Male' ? 'selected' : ''}>Male</option>
                                <option value="Female" ${filterGender eq 'Female' ? 'selected' : ''}>Female</option>
                                <option value="Other" ${filterGender eq 'Other' ? 'selected' : ''}>Other</option>
                            </select>
                        </th>
                        <th>
                            <%-- Select lọc Status --%>
                            <select id="filterStatus" onchange="applyFilters()">
                                <option value="">Status</option>
                                <option value="Active" ${filterStatus eq 'Active' ? 'selected' : ''}>Active</option>
                                <option value="Inactive" ${filterStatus eq 'Inactive' ? 'selected' : ''}>Inactive</option>
                            </select>
                        </th>
                        <th>Email</th>
                        <th>Phone</th>
                        <th>Address</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody id="userTableBody">
                    <c:if test="${empty userList}">
                        <tr>
                            <td colspan="10" class="no-results">No users found.</td>
                        </tr>
                    </c:if>
                    <c:forEach var="dto" items="${userList}">
                        <tr>
                            <td data-label="UserID">${dto.user.userId}</td>
                            <td data-label="Avatar">
                                <img src="${dto.user.avatarUrl}" alt="Avatar" style="width: 40px; height: 40px; border-radius: 50%; object-fit: cover;">
                            </td>
                            <td data-label="Full Name">${dto.user.fullName}</td>
                            <td data-label="Role">${dto.roleName}</td>
                            <td data-label="Gender">${dto.user.gender}</td>
                            <td data-label="Status" class="status-cell">
                                <span class="status ${dto.user.status eq 'Active' ? 'status-active' : 'status-inactive'}">
                                    ${dto.user.status}
                                </span>
                            </td>
                            <td data-label="Email">${dto.user.email}</td>
                            <td data-label="Phone">${dto.user.phone}</td>
                            <td data-label="Address">${dto.user.address}</td>
                            <td data-label="Actions" class="action-buttons">
                                <%-- ĐÃ THAY ĐỔI ĐƯỜNG DẪN TẠI ĐÂY TỪ "/profile" SANG "/userdetail" --%>
                                <a href="${pageContext.request.contextPath}/userdetail?id=${dto.user.userId}" class="action-btn edit-btn" title="View/Edit User">
                                    <i class="fas fa-eye"></i>
                                </a>
                                <form action="deleteUser" method="post" style="display:inline;" onsubmit="return confirm('Are you sure you want to delete user ID: ${dto.user.userId}?');">
                                    <input type="hidden" name="userID" value="${dto.user.userId}">
                                    <button type="submit" class="action-btn delete-btn" title="Delete User">
                                        <i class="fas fa-trash-alt"></i>
                                    </button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
            <div class="pagination">
                <c:if test="${currentPage > 1}">
                    <a href="userlist?page=${currentPage - 1}&search=${search}&role=${filterRole}&gender=${filterGender}&status=${filterStatus}" class="page-btn">&laquo; Previous</a>
                </c:if>

                <c:forEach var="i" begin="1" end="${totalPages}">
                    <a href="userlist?page=${i}&search=${search}&role=${filterRole}&gender=${filterGender}&status=${filterStatus}" class="page-btn ${i == currentPage ? 'active' : ''}">${i}</a>
                </c:forEach>

                <c:if test="${currentPage < totalPages}">
                    <a href="userlist?page=${currentPage + 1}&search=${search}&role=${filterRole}&gender=${filterGender}&status=${filterStatus}" class="page-btn">Next &raquo;</a>
                </c:if>
            </div>
        </section>
    </div> <%-- End .content-wrapper --%>

    <%-- Footer (Đảm bảo đường dẫn include là chính xác) --%>
    <jsp:include page="/default/footer.jsp"/>
</div>

<script>
    function applyFilters() {
        // Lấy giá trị từ ô tìm kiếm
        const searchInput = document.querySelector('.search-input').value;
        // Lấy giá trị từ các dropdown lọc
        const filterRole = document.getElementById('filterRole').value;
        const filterGender = document.getElementById('filterGender').value;
        const filterStatus = document.getElementById('filterStatus').value;

        // Cập nhật các trường input ẩn trong form tìm kiếm
        document.getElementById('hiddenFilterRole').value = filterRole;
        document.getElementById('hiddenFilterGender').value = filterGender;
        document.getElementById('hiddenFilterStatus').value = filterStatus;

        // Gửi form tìm kiếm để áp dụng các bộ lọc mới
        document.querySelector('.search-form').submit();
    }

    // Thiết lập giá trị ban đầu cho các dropdown dựa trên tham số từ server khi trang tải
    document.addEventListener('DOMContentLoaded', function() {
        const currentRole = "${filterRole}";
        const currentGender = "${filterGender}";
        const currentStatus = "${filterStatus}";

        if (currentRole) {
            document.getElementById('filterRole').value = currentRole;
        }
        if (currentGender) {
            document.getElementById('filterGender').value = currentGender;
        }
        if (currentStatus) {
            document.getElementById('filterStatus').value = currentStatus;
        }

        // === JavaScript cho Sidebar Toggle (Đã copy từ dashboard.jsp) ===
        // Tạo và thêm nút toggle vào body
        const toggleMenuBtn = document.createElement('button');
        toggleMenuBtn.id = 'toggleMenuBtn';
        toggleMenuBtn.innerHTML = '<i class="bi bi-list"></i>'; // Sử dụng icon Bootstrap Icons
        document.body.appendChild(toggleMenuBtn);

        const menuContainer = document.getElementById('courseDropdownContainer');

        toggleMenuBtn.addEventListener('click', () => {
            const isVisible = menuContainer.style.display === 'block';
            menuContainer.style.display = isVisible ? 'none' : 'block';
        });

        // Đóng sidebar khi click ra ngoài
        document.addEventListener('click', function(event) {
            const sidebar = document.getElementById('courseDropdownContainer');
            const toggleButton = document.getElementById('toggleMenuBtn');

            // Nếu click không phải trên sidebar và không phải trên nút toggle
            if (sidebar && !sidebar.contains(event.target) && (!toggleButton || !toggleButton.contains(event.target))) {
                sidebar.style.display = 'none';
            }
        });
    });
</script>
</body>
</html>