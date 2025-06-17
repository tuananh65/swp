<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
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

    <jsp:include page="/default/footer.jsp"/>
</div>

<script>
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
        }
    });
</script>
</body>
</html>