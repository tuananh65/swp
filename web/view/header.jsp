<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

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
            <a href="#" class="nav-link">About us</a>
            <div class="nav-item dropdown">
                <a href="${pageContext.request.contextPath}/courseList" class="nav-link">Courses<i class="fas fa-chevron-down"></i></a>
                <div class="dropdown-content">
                    <c:forEach var="c" items="${courseList}">
                        <a href="${pageContext.request.contextPath}/CourseDetailServlet?courseId=${course.courseID}">${c.courseName}</a>
                    </c:forEach>
                </div>
            </div>
            <a href="${pageContext.request.contextPath}/blog" class="nav-link">Blog</a>
            <a href="#" class="nav-link">Contact</a>
        </div>
        <div class="nav-actions">
            <button class="search-btn" aria-label="Tìm ki?m"><i class="fas fa-search"></i></button>
        </div>
    </div>
