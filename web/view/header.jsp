<%-- 
    Document   : Header
    Created on : Jun 5, 2025, 1:03:37 PM
    Author     : Administrator
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<div class="navbar">
    <div class="logo">
        <img src="${pageContext.request.contextPath}/images/SoftSkill.png" alt="Soft Skills Logo">
        
    </div>
    <div class="nav-list">
        <a href="${pageContext.request.contextPath}/">Home</a>
        <a href="${pageContext.request.contextPath}/about">About Us</a>
        <a href="${pageContext.request.contextPath}/courseList">Courses</a>
        <a href="${pageContext.request.contextPath}/pages">Pages</a>
        <a href="${pageContext.request.contextPath}/blog">Blog</a>
        <a href="${pageContext.request.contextPath}/contact">Contact</a>
        <div class="search-icon">
            <span class="icon">🔍</span>
        </div>
    </div>
</div>