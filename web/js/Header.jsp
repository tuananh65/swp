<%-- 
    Document   : header
    Created on : May 25, 2025
    Author     : maitu
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!-- Navigation Bar -->
<nav class="navbar">
  <div class="logo">
    <img src="${pageContext.request.contextPath}/image/logoden.png" alt="Soft Skills Logo" />
  </div>
  <div class="nav-list">
    <div class="dropdown"><a href="#">Home</a></div>
    <a href="#">About Us</a>
    <div class="dropdown"><a href="#">Courses</a></div>
    <div class="dropdown"><a href="#">Pages</a></div>
    <div class="dropdown"><a href="#">Blog</a></div>
    <a href="#">Contact</a>
    <div class="icons">
      <span class="icon"><i class="fas fa-search"></i></span>
      <span class="icon"><i class="fas fa-bars"></i></span>
    </div>
  </div>
</nav>
