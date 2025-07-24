<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="model.User"%>
<html lang="vi">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>${title != null ? title : "Soft Skills Portal"}</title>

    <!-- Font & Icons -->
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" />

    <!-- CSS riêng cho header -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/header.css" />
</head>
<body>

<nav class="navbar" id="navbar">
    <div class="nav-container">
        <div class="logo">
            <img src="${pageContext.request.contextPath}/images/SoftSkill.png" alt="Soft Skills Logo" />
        </div>
        <div class="nav-menu">
            <a href="${pageContext.request.contextPath}/home" class="nav-link">Home</a>
            <a href="${pageContext.request.contextPath}/aboutUs.jsp" class="nav-link">About us</a>
            <a href="${pageContext.request.contextPath}/courseList" class="nav-link">Courses</a>                           
            <a href="${pageContext.request.contextPath}/blog" class="nav-link">Blog</a>
            <a href="${pageContext.request.contextPath}/contact.jsp" class="nav-link">Contact</a>
        </div>
    
        <div class="login-register-links" id="userSection">
            <%
                User user = (User) session.getAttribute("currentUser");
                if (user == null) {
            %>
                <a href="${pageContext.request.contextPath}/view/SignIn.jsp" class="custom-btn btn btn-outline-light me-2">Sign In</a>
                <a href="${pageContext.request.contextPath}/view/register.jsp" class="custom-btn btn btn-primary">Sign Up</a>
            <%
                } else {
            %>
                <div class="dropdown">
                    <span class="user-fullname" onclick="toggleDropdown(this)">
                        <%= user.getFullName() != null ? user.getFullName() : user.getEmail() != null ? user.getEmail() : "User" %>
                    </span>
                    <div class="dropdown-menu">
                        <div><a class="dropdown-item" href="${pageContext.request.contextPath}/view/changePassword.jsp">Change Password</a></div>
                        <div><a class="dropdown-item" href="${pageContext.request.contextPath}/auth?action=logout">Sign out</a></div>
                    </div>
                </div>
            <%
                }
            %>
        </div>
    </div>
</nav>
        
<script>
    function toggleDropdown(element) {
        const dropdownMenu = element.nextElementSibling;
        dropdownMenu.classList.toggle('show');
        document.addEventListener('click', function closeDropdown(event) {
            if (!element.contains(event.target) && !dropdownMenu.contains(event.target)) {
                dropdownMenu.classList.remove('show');
                document.removeEventListener('click', closeDropdown);
            }
        });
    }
</script>
