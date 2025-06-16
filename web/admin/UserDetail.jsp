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
    <div class="user-details-container">
        <div class="banner">
            <h1>USER DETAILS</h1>
            <p><a href="#">Home</a> / User Detail</p>
        </div>
    <section class="user-details">
        <div class="user-card">
            <div class="user-details-wrapper">
                <img src="${pageContext.request.contextPath}/image/${loggedInUser.avatarUrl}" alt="${loggedInUser.fullName}">
                <div class="user-info">
                    <h2>${loggedInUser.fullName} <span class="status ${loggedInUser.status.toLowerCase()}">${loggedInUser.status}</span></h2>
                    <p class="role-display">Role: <span id="currentRole">${roleName == '1' ? 'Student' : (roleName == '2' ? 'Teacher' : roleName)}</span></p>
                    <p class="status-display">Status: <span id="currentStatus">${loggedInUser.status}</span></p>
                    <p class="gender">${loggedInUser.gender}</p>
                    <p class="contact">📞 <a href="tel:${loggedInUser.phone}">${loggedInUser.phone}</a></p>
                    <p class="location">${loggedInUser.address != null ? loggedInUser.address : "N/A"}</p>
                    <p class="email">✉️ <a href="mailto:${loggedInUser.email}">${loggedInUser.email}</a></p>
                </div>
            </div>
            <div class="action-buttons">
                <a href="${pageContext.request.contextPath}/addUser">Add New</a>
                <button id="editRoleStatusBtn">Edit</button>
            </div>

            <div id="editRoleStatusForm" class="edit-role-status" style="display: none; margin-top: 15px;">
                <h3>Edit Role and Status</h3>
                <form action="${pageContext.request.contextPath}/updateUserRoleStatus" method="post">
                    <input type="hidden" name="userId" value="${loggedInUser.userID}">

                    <div class="form-group">
                        <label for="roleId">Role:</label>
                        <select id="roleId" name="roleId">
                            <option value="1">Student</option>
                            <option value="2">Instructor</option>
                        </select>
                    </div>

                    <div class="form-group">
                        <label for="status">Status:</label>
                        <select id="status" name="status">
                            <option value="Active">Active</option>
                            <option value="Inactive">Inactive</option>
                        </select>
                    </div>

                    <button type="submit" class="save-button">Save change</button>
                </form>
            </div>
        </div>
    </section>


            <c:if test="${not empty otherUsers}">
                <section class="other-users-container">
                    <h3>Other Users</h3>
                    <div class="four_user">
                        <c:forEach var="otherUser" items="${otherUsers}">
                            <a href="${pageContext.request.contextPath}/userdetail?id=${otherUser.user.userID}" class="other-user-link">
                                <div class="other-user-card">
                                    <img src="${pageContext.request.contextPath}/image/${otherUser.user.avatarUrl}" alt="${otherUser.user.fullName}">
                                    <h4>${otherUser.user.fullName}</h4>
                                    <p class="role">${otherUser.roleName == '1' ? 'Admin' : (otherUser.roleName == '2' ? 'Teacher' : otherUser.roleName)}</p>
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
            } else if (currentRole === 'Teacher') {
                roleSelect.value = '2';
            }

            statusSelect.value = currentStatus;
        });
    });
</script>
</body>
</html>