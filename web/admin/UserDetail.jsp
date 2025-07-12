<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>User Detail - Soft Skills</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/login.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/UserDe.css" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" />
</head>
<body>
    <jsp:include page="/default/header.jsp"/>

    <%-- Banner Section - Đã cập nhật để khớp với cấu trúc của UserList --%>
    <section class="hero-banner">
        <div class="hero-content">
            <%-- Đảm bảo đường dẫn ảnh là chính xác và hình ảnh phù hợp với banner --%>
            <img class="hero-image" src="${pageContext.request.contextPath}/images/Banner.jpg" alt="Banner">
            <div class="hero-overlay"></div>
            <div class="hero-text">
                <h1>USER DETAILS</h1>
                <p><a href="${pageContext.request.contextPath}/home">Home</a> / User Detail</p>
            </div>
        </div>
    </section>

    <div class="user-details-container">
        <section class="user-details">
            <div class="user-card">
                <div class="user-details-wrapper">
                    <img src="${pageContext.request.contextPath}/image/${loggedInUser.avatarUrl}" alt="${loggedInUser.fullName}">
                    <div class="user-info">
                        <h2>${loggedInUser.fullName} <span class="status ${loggedInUser.status.toLowerCase()}">${loggedInUser.status}</span></h2>
                        <p class="role-display"><i class="fas fa-user-tag"></i> Role: <span id="currentRole">${roleName}</span></p>
                        <p class="status-display"><i class="fas fa-circle"></i> Status: <span id="currentStatus">${loggedInUser.status}</span></p>
                        <p class="gender"><i class="fas fa-venus-mars"></i> ${loggedInUser.gender}</p>
                        <p class="contact"><i class="fas fa-phone"></i> <a href="tel:${loggedInUser.phone}">${loggedInUser.phone}</a></p>
                        <p class="location"><i class="fas fa-map-marker-alt"></i> ${loggedInUser.address != null ? loggedInUser.address : "N/A"}</p>
                        <p class="email"><i class="fas fa-envelope"></i> <a href="mailto:${loggedInUser.email}">${loggedInUser.email}</a></p>
                    </div>
                </div>
                <div class="action-buttons">
                    <a href="${pageContext.request.contextPath}/addUser"><i class="fas fa-plus"></i> Add New</a>
                    <button id="editRoleStatusBtn"><i class="fas fa-edit"></i> Edit</button>
                </div>

                <div id="editRoleStatusForm" class="edit-role-status" style="display: none; margin-top: 15px;">
                    <h3>Edit Role and Status</h3>
                    <form action="${pageContext.request.contextPath}/updateUserRoleStatus" method="post">
                        <input type="hidden" name="userId" value="${loggedInUser.userId}">

                        <div class="form-group">
                            <label for="roleId">Role:</label>
                            <select id="roleId" name="roleId">
                                <option value="1">Student</option>
                                <option value="2">Instructor</option>
                                <option value="3">Admin</option>
                            </select>
                        </div>

                        <div class="form-group">
                            <label for="status">Status:</label>
                            <select id="status" name="status">
                                <option value="Active">Active</option>
                                <option value="Inactive">Inactive</option>
                            </select>
                        </div>

                        <button type="submit" class="save-button">Save Changes</button>
                    </form>
                </div>
            </div>
        </section>

        <c:if test="${not empty otherUsers}">
            <section class="other-users-container">
                <h3>Other Users</h3>
                <div class="four_user">
                    <%-- Thay đổi ở đây: Thêm 'end="2"' để chỉ hiển thị 3 user (index từ 0 đến 2) --%>
                    <c:forEach var="otherUser" items="${otherUsers}" begin="0" end="2">
                        <a href="${pageContext.request.contextPath}/userdetail?id=${otherUser.user.userId}" class="other-user-link">
                            <div class="other-user-card">
                                <img src="${pageContext.request.contextPath}/image/${otherUser.user.avatarUrl}" alt="${otherUser.user.fullName}">
                                <h4>${otherUser.user.fullName}</h4>
                                <p class="role">${otherUser.roleName}</p>
                            </div>
                        </a>
                    </c:forEach>
                </div>
            </section>
        </c:if>
    </div>
    <jsp:include page="/default/footer.jsp"/>
<script>
    document.addEventListener('DOMContentLoaded', function() {
        const editRoleStatusBtn = document.getElementById('editRoleStatusBtn');
        const editRoleStatusForm = document.getElementById('editRoleStatusForm');
        const roleSelect = document.getElementById('roleId');
        const statusSelect = document.getElementById('status');
        const currentRole = document.getElementById('currentRole').textContent.trim();
        const currentStatus = document.getElementById('currentStatus').textContent.trim();

        editRoleStatusBtn.addEventListener('click', function() {
            editRoleStatusForm.style.display = (editRoleStatusForm.style.display === 'none' || editRoleStatusForm.style.display === '') ? 'block' : 'none';

            // Set giá trị mặc định cho select boxes khi form hiển thị
            if (currentRole === 'Student') {
                roleSelect.value = '1';
            } else if (currentRole === 'Instructor') {
                roleSelect.value = '2';
            } else if (currentRole === 'Admin') {
                roleSelect.value = '3';
            } else {
                roleSelect.value = ''; // Default or handle other roles
            }

            statusSelect.value = currentStatus;
        });
    });
</script>
</body>
</html>