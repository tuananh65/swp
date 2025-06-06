<%-- 
    Document   : Header
    Created on : Jun 5, 2025, 1:03:37 PM
    Author     : Administrator
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
        <div class="navbar">
            <div class="logo">
                <img src="${pageContext.request.contextPath}/images/SoftSkill.png" alt="Soft Skills Logo">
            </div>
            <div class="nav-list">
                <div class="dropdown home-link"> <!-- Add the home-link class here -->
                    <a href="#">Home</a>
                </div>
                <a href="#">About Us</a>
                <div class="dropdown">
                    <a href="#">Courses</a>
                    <div class="dropdown-content">
                        <c:forEach var="c" items="${courseList}">
                            <!-- Khi click vào sẽ gọi lại servlet và truyền courseId -->
                            <a href="${pageContext.request.contextPath}/CourseDetailServlet?courseId=${c.courseID}">
                                ${c.courseName}
                            </a>
                        </c:forEach>
                    </div>
                </div>
                <div class="dropdown">
                  <a href="#">Pages</a>
                </div>
                <div class="dropdown">
                  <a href="${pageContext.request.contextPath}/blog">Blog</a>
                </div>
                    <a href="#">Contact</a>
            <div class="icons">
                <span class="icon">🔍</span>
                <span class="icon">☰</span>
            </div>
        </div>