<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Add New User - Soft Skills</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" />
    <%-- Remove UserList.css and add addUser.css --%>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/addUser.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/header.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/footer.css">
    <%-- Removed inline <style> tag --%>
</head>
<body>
    <div class="wrapper">
        <jsp:include page="/default/header.jsp"/>

        <section class="hero-banner">
            <div class="hero-content">
                <img class="hero-image" src="${pageContext.request.contextPath}/images/Banner.jpg" alt="Banner Image">
                <div class="hero-text">
                    <h1>ADD NEW USER</h1>
                </div>
            </div>
        </section>

        <div class="content-wrapper">
            <section class="content">
                <div class="add-user-form-container">
                    <h2>Add New User</h2>
                    <%-- Display error message if any --%>
                    <c:if test="${not empty errorMessage}">
                        <div class="error-message">
                            <i class="fas fa-exclamation-circle"></i> ${errorMessage}
                        </div>
                    </c:if>
                    <form action="${pageContext.request.contextPath}/addUser" method="post">
                        <div class="form-group">
                            <label for="userName">Username:</label>
                            <input type="text" id="userName" name="userName" value="${not empty enteredUserName ? enteredUserName : param.userName}" required>
                        </div>
                        <div class="form-group">
                            <label for="fullName">Full Name:</label>
                            <input type="text" id="fullName" name="fullName" value="${not empty enteredFullName ? enteredFullName : param.fullName}" required>
                        </div>
                        <div class="form-group">
                            <label for="email">Email:</label>
                            <input type="email" id="email" name="email" value="${not empty enteredEmail ? enteredEmail : param.email}" required>
                        </div>
                        <div class="form-group">
                            <label for="password">Password:</label>
                            <input type="password" id="password" name="password" required>
                        </div>
<div class="form-group">
                            <label>Gender:</label>
                            <div class="radio-group">
                                <label><input type="radio" name="gender" value="Male" ${ (not empty enteredGender && enteredGender eq 'Male') || (empty enteredGender && param.gender eq 'Male') ? 'checked' : ''}> Male</label>
                                <label><input type="radio" name="gender" value="Female" ${ (not empty enteredGender && enteredGender eq 'Female') || (empty enteredGender && param.gender eq 'Female') ? 'checked' : ''}> Female</label>
                                <label><input type="radio" name="gender" value="Other" ${ (not empty enteredGender && enteredGender eq 'Other') || (empty enteredGender && param.gender eq 'Other') ? 'checked' : ''}> Other</label>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="phone">Phone:</label>
                            <input type="tel" id="phone" name="phone" value="${not empty enteredPhone ? enteredPhone : param.phone}">
                        </div>
                        <div class="form-group">
                            <label for="address">Address:</label>
                            <input type="text" id="address" name="address" value="${not empty enteredAddress ? enteredAddress : param.address}">
                        </div>
                        <%-- You can uncomment this if you want to add an avatar URL input field --%>
                        <%--
                        <div class="form-group">
                            <label for="avatarUrl">Avatar URL:</label>
                            <input type="text" id="avatarUrl" name="avatarUrl" value="${not empty enteredAvatarUrl ? enteredAvatarUrl : param.avatarUrl}">
                        </div>
                        --%>
                        <div class="form-group">
                            <label for="role">Role:</label>
                            <select id="role" name="role" required>
                                <option value="">Select Role</option>
                                <option value="Admin" ${ (not empty enteredRole && enteredRole eq 'Admin') || (empty enteredRole && param.role eq 'Admin') ? 'selected' : ''}>Admin</option>
                                <option value="Instructor" ${ (not empty enteredRole && enteredRole eq 'Instructor') || (empty enteredRole && param.role eq 'Instructor') ? 'selected' : ''}>Instructor</option>
                                <option value="Student" ${ (not empty enteredRole && enteredRole eq 'Student') || (empty enteredRole && param.role eq 'Student') ? 'selected' : ''}>Student</option>
                                <%-- Populate roles dynamically from DB if possible --%>
                            </select>
                        </div>
                        <div class="form-group">
<label for="status">Status:</label>
                            <select id="status" name="status" required>
    <option value="Active" ${enteredStatus eq 'Active' ? 'selected' : ''}>Active</option>
    <option value="Inactive" ${enteredStatus eq 'Inactive' ? 'selected' : ''}>Inactive</option>
</select>

                        </div>
                        <div class="form-actions">
                            <button type="submit"><i class="fas fa-plus-circle"></i> Add User</button>
                            <button type="button" class="cancel-btn" onclick="window.location.href='${pageContext.request.contextPath}/userlist'">
                                <i class="fas fa-times-circle"></i> Cancel
                            </button>
                        </div>
                    </form>
                </div>
            </section>
        </div>

        <jsp:include page="/default/footer.jsp"/>
    </div>
</body>
</html>