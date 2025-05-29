<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, model.Users" %>
<%@ page import="dto.UserWithRoleDTO" %>
<%@ page import="java.util.List" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Setting List - Soft Skills</title>

    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" />

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/SettingList.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/login.css" />

</head>
<body>
<div class="wrapper">
    <%@ include file="Header.jsp" %>

    <div class="banner">
        <h1>SETTING LIST</h1>
        <p><a href="#">Home</a> / Setting List</p>
    </div>

    <section class="content">
        <div class="add-search-container">
            <div class="add-setting-container">
                <a href="${pageContext.request.contextPath}/addSetting" class="add-setting-btn">
                    <i class="fas fa-plus"></i> Add New Setting
                </a>
            </div>
            <form action="${pageContext.request.contextPath}/settingList" method="get" class="search-form">
                <input type="text" class="search" name="searchValue" placeholder="Search by Value" value="${searchValue}">
                <button type="submit" class="search-btn">Search</button>
            </form>
        </div>

        <table>
            <thead>
                <tr>
                    <th>Setting ID</th>
                    <th>
                        <form action="${pageContext.request.contextPath}/settingList" method="get">
                            <input type="hidden" name="searchValue" value="${searchValue}">
                            <select class="filter-select" name="type" onchange="this.form.submit()">
                                <option value="">Type</option>
                                <c:forEach var="type" items="${typeList}">
                                    <option value="${type}" ${selectedType == type ? 'selected' : ''}>${type}</option>
                                </c:forEach>
                            </select>
                            <input type="hidden" name="status" value="${selectedStatus}">
                        </form>
                    </th>
                    <th>Order</th>
                    <th>
                        Value
                    </th>
                    <th>
                        <form action="${pageContext.request.contextPath}/settingList" method="get">
                            <input type="hidden" name="searchValue" value="${searchValue}">
                            <input type="hidden" name="type" value="${selectedType}">
                            <select class="filter-select" name="status" onchange="this.form.submit()">
                                <option value="">Status</option>
                                <option value="active" ${selectedStatus == 'active' ? 'selected' : ''}>Active</option>
                                <option value="inactive" ${selectedStatus == 'inactive' ? 'selected' : ''}>Inactive</option>
                            </select>
                        </form>
                    </th>
                    </tr>
            </thead>
            <tbody>
                <c:forEach var="setting" items="${settingList}">
                    <tr>
                        <td>${setting.settingID}</td>
                        <td>${setting.type}</td>
                        <td>${setting.order}</td>
                        <td>${setting.value}</td>
                        <td>${setting.status}</td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        <div class="pagination">
            <c:if test="${totalPages > 0}">
                <c:if test="${currentPage > 1}">
                    <a href="${pageContext.request.contextPath}/settingList?page=${currentPage - 1}&searchValue=${searchValue}&type=${selectedType}&status=${selectedStatus}" class="prev-btn">&lt;</a>
                </c:if>
                <c:forEach var="i" begin="1" end="${totalPages}">
                    <c:choose>
                        <c:when test="${i == currentPage}">
                            <button class="current-page">${i}</button>
                        </c:when>
                        <c:otherwise>
                            <a href="${pageContext.request.contextPath}/settingList?page=${i}&searchValue=${searchValue}&type=${selectedType}&status=${selectedStatus}">${i}</a>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
                <c:if test="${currentPage < totalPages}">
                    <a href="${pageContext.request.contextPath}/settingList?page=${currentPage + 1}&searchValue=${searchValue}&type=${selectedType}&status=${selectedStatus}" class="next-btn">&gt;</a>
                </c:if>
            </c:if>
        </div>
    </section>

    <%@ include file="Footer.jsp" %>
</div>
</body>
</html>