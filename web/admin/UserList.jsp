<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>

<%@ page import="java.util.*, model.User" %> <%-- Đảm bảo model.User là class chính xác --%>
<%@ page import="dto.UserWithRoleDTO" %> <%-- Đảm bảo dto.UserWithRoleDTO là class chính xác --%>
=======
<%@ page import="java.util.*, model.User" %>
<%@ page import="dto.UserWithRoleDTO" %>
<%@ page import="java.util.List" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Users List - Soft Skills</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" />

    <%-- Đảm bảo đường dẫn đến CSS là chính xác --%>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/UserList.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/header.css"> <%-- Nếu bạn có header.css riêng --%>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/footer.css"> <%-- Nếu bạn có footer.css riêng --%>
</head>
<body>
<div class="wrapper">
    <%-- Header (Đảm bảo đường dẫn include là chính xác) --%>
    <jsp:include page="/default/header.jsp"/>

    <%-- Banner Section - Đã cập nhật để khớp với cấu trúc của Setting List --%>
    <section class="hero-banner">
        <div class="hero-content">
            <%-- Đảm bảo đường dẫn ảnh là chính xác và hình ảnh phù hợp với banner --%>
            <img class="hero-image" src="${pageContext.request.contextPath}/images/nen.jpg" alt="Banner Image">
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
                <a href="${pageContext.request.contextPath}/addUser" class="add-user-btn"> <%-- Cập nhật class --%>
                    <i class="fas fa-plus-circle"></i> Add User <%-- Thêm icon --%>
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
                                <%-- You should dynamically populate these roles from your backend --%>
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
                                <option value="active" ${filterStatus eq 'active' ? 'selected' : ''}>Active</option>
                                <option value="inactive" ${filterStatus eq 'inactive' ? 'selected' : ''}>Inactive</option>
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
                                <span class="status ${dto.user.status eq 'active' ? 'status-active' : 'status-inactive'}">
                                    ${dto.user.status}
                                </span>
                            </td>
                            <td data-label="Email">${dto.user.email}</td>
                            <td data-label="Phone">${dto.user.phone}</td>
                            <td data-label="Address">${dto.user.address}</td>
                            <td data-label="Actions" class="action-buttons"> <%-- Cập nhật class --%>
                                <a href="${pageContext.request.contextPath}/userdetail?id=${dto.user.userId}" class="action-btn edit-btn" title="View/Edit User">
                                    <i class="fas fa-eye"></i> <%-- Thêm icon --%>
                                </a>
                                <form action="deleteUser" method="post" style="display:inline;" onsubmit="return confirm('Are you sure you want to delete user ID: ${dto.user.userId}?');">
                                    <input type="hidden" name="userID" value="${dto.user.userId}">
                                    <button type="submit" class="action-btn delete-btn" title="Delete User">
                                        <i class="fas fa-trash-alt"></i> <%-- Thêm icon --%>
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
=======
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/UserList.css" />

</head>
<body>
<div class="wrapper">
    <jsp:include page="/default/header.jsp"/>

    <div class="banner">
        <h1>USERS LIST</h1>
        <p><a href="#">Home</a> / Users</p>
    </div>

    <section class="content">
    <div class="search-container">
        <form action="userlist" method="get">
            <input type="text" class="search" name="search" placeholder="Search">
            <button type="submit">Tìm kiếm</button>
            <input type="hidden" name="page" value="1">
        </form>
        <a href="addUser" class="add-user-btn">Thêm người dùng</a>
    </div>

        <table>
            <thead>
                <tr>
                    <th>UserID</th>
                    <th>Avatar</th>
                    <th>Full Name</th>
                    <th>
                        <select id="filterRole">
                            <option value="">Role</option>
                            <option value="Instructor">Instructor</option>
                            <option value="Student">Student</option>
                        </select>
                    </th>
                    <th>
                        <select id="filterGender">
                            <option value="">Gender</option>
                            <option value="Male">Male</option>
                            <option value="Female">Female</option>
                        </select>
                    </th>
                    <th>
                        <select id="filterStatus">
                            <option value="">Status</option>
                            <option value="active">Active</option>
                            <option value="inactive">Inactive</option>
                        </select>
                    </th>
                    <th>Email</th>
                    <th>Phone</th>
                    <th>Address</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody id="userTableBody">
                <c:forEach var="dto" items="${userList}">
                    <tr>
                        <td>${dto.user.userId}</td>
                        <td><img src="${dto.user.avatarUrl}" style="width: 40px; height: 40px;"></td>
                        <td>${dto.user.fullName}</td>
                        <td class="role-cell">${dto.roleName}</td>
                        <td class="gender-cell">${dto.user.gender}</td>
                        <td class="status-cell">${dto.user.status}</td>
                        <td>${dto.user.email}</td>
                        <td>${dto.user.phone}</td>
                        <td>${dto.user.address}</td>
                        <td>
                            <button>Edit</button>
                            <form action="deleteUser" method="post" style="display:inline;">
                                <input type="hidden" name="userID" value="${dto.user.userId}">
                                <button type="submit" class="delete-btn">Delete</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty userList}">
                    <tr><td colspan="10">Không tìm thấy người dùng nào.</td></tr>
                </c:if>
            </tbody>
        </table>
        <div class="pagination">
            <c:if test="${currentPage > 1}">
                <a href="userlist?page=${currentPage - 1}${not empty search ? '&search=${search}' : ''}" class="page-btn">&laquo;</a>
            </c:if>

            <c:forEach var="i" begin="1" end="${totalPages}">
                <a href="userlist?page=${i}${not empty search ? '&search=${search}' : ''}" class="page-btn ${i == currentPage ? 'active' : ''}">${i}</a>
            </c:forEach>

            <c:if test="${currentPage < totalPages}">
                <a href="userlist?page=${currentPage + 1}${not empty search ? '&search=${search}' : ''}" class="page-btn">&raquo;</a>
            </c:if>
        </div>
    </section>

>>>>>>> 9f0d8d78ba253b25258b2d43672933c3caa423b4
    <jsp:include page="/default/footer.jsp"/>
</div>

<script>
<<<<<<< HEAD
    function applyFilters() {
        const searchInput = document.querySelector('.search-input').value;
        const filterRole = document.getElementById('filterRole').value;
        const filterGender = document.getElementById('filterGender').value;
        const filterStatus = document.getElementById('filterStatus').value;

        // Update hidden input fields for the form submission
        document.getElementById('hiddenFilterRole').value = filterRole;
        document.getElementById('hiddenFilterGender').value = filterGender;
        document.getElementById('hiddenFilterStatus').value = filterStatus;

        // Submit the search form to trigger a server-side filter
        document.querySelector('.search-form').submit();
    }

    // Initial setting of dropdown values based on server-side parameters (if any)
    document.addEventListener('DOMContentLoaded', function() {
        // Retrieve current filter values from the URL or request attributes if available
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
=======
    const filterRoleSelect = document.getElementById('filterRole');
    const filterGenderSelect = document.getElementById('filterGender');
    const filterStatusSelect = document.getElementById('filterStatus');
    const userTableBody = document.getElementById('userTableBody');
    const rows = userTableBody.querySelectorAll('tr');

    function filterTable() {
        const selectedRole = filterRoleSelect.value.toLowerCase();
        const selectedGender = filterGenderSelect.value.toLowerCase();
        const selectedStatus = filterStatusSelect.value.toLowerCase();

        rows.forEach(row => {
            const roleCell = row.querySelector('.role-cell').textContent.toLowerCase();
            const genderCell = row.querySelector('.gender-cell').textContent.toLowerCase();
            const statusCell = row.querySelector('.status-cell').textContent.toLowerCase();

            const roleMatch = selectedRole === '' || roleCell === selectedRole;
            const genderMatch = selectedGender === '' || genderCell === selectedGender;
            const statusMatch = selectedStatus === '' || statusCell === selectedStatus;

            if (roleMatch && genderMatch && statusMatch) {
                row.style.display = '';
            } else {
                row.style.display = 'none';
            }
        });
    }

    filterRoleSelect.addEventListener('change', filterTable);
    filterGenderSelect.addEventListener('change', filterTable);
    filterStatusSelect.addEventListener('change', filterTable);

    // Cập nhật giá trị mặc định của dropdown status dựa trên dữ liệu ban đầu
    document.addEventListener('DOMContentLoaded', function() {
        const firstRowStatus = document.querySelector('#userTableBody tr:first-child .status-cell');
        if (firstRowStatus) {
            const statusText = firstRowStatus.textContent.trim().toLowerCase();
            document.getElementById('filterStatus').value = statusText;
>>>>>>> 9f0d8d78ba253b25258b2d43672933c3caa423b4
        }
    });
</script>
</body>
</html>