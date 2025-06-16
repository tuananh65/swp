<%-- settinglist.jsp --%>
<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, model.Setting" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Settings List - Soft Skills</title>

    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" />

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/SettingLi.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/login.css" />
</head>
<body>
    <div class="wrapper">
        <jsp:include page="/default/header.jsp"/>

        <div class="banner">
            <h1>SETTINGS LIST</h1>
            <p><a href="#">Home</a> / Settings</p>
        </div>

        <section class="content">
            <c:if test="${not empty param.message}">
                <div class="success-message">${param.message}</div>
            </c:if>
            <c:if test="${not empty param.error}">
                <div class="error-message">${param.error}</div>
            </c:if>
            <div class="search-container">
                <form action="settinglist" method="get">
                    <input type="text" class="search" name="search" value="${search}" placeholder="Search">
                    <button type="submit">Search</button>
                    <input type="hidden" name="page" value="1">
                </form>
                <a href="addSetting" class="add-user-btn">Add new Setting</a>
            </div>

            <form action="settinglist" method="get">
                <table>
                    <thead>
                        <tr>
                            <th>SettingID</th>
                            <th>
                                <select id="filterType" name="filterType" onchange="this.form.submit()">
                                    <option value="">Type</option>
                                    <c:forEach var="type" items="${uniqueTypes}">
                                        <option value="${type}" ${type == selectedType ? 'selected' : ''}>${type}</option>
                                    </c:forEach>
                                </select>
                            </th>
                            <th>Value</th>
                            <th>Order</th>
                            <th>
                                <select id="filterStatus" name="filterStatus" onchange="this.form.submit()">
                                    <option value="">Status</option>
                                    <option value="active" ${'active' == selectedStatus ? 'selected' : ''}>Active</option>
                                    <option value="inactive" ${'inactive' == selectedStatus ? 'selected' : ''}>Inactive</option>
                                </select>
                            </th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody id="settingTableBody">
                        <c:forEach var="setting" items="${settingList}">
                            <tr>
                                <td>${setting.settingID}</td>
                                <td class="type-cell">${setting.type}</td>
                                <td class="value-cell">${setting.value}</td>
                                <td class="order-cell">${setting.order}</td>
                                <td class="status-cell">${setting.status}</td>
                                <td>
                                    <a href="editSetting?settingID=${setting.settingID}">View</a>
                                    <form action="deleteSetting" method="post" style="display:inline;">
                                        <input type="hidden" name="settingID" value="${setting.settingID}">
                                        <button type="submit" class="delete-btn">Delete</button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty settingList}">
                            <tr><td colspan="6">Can not find.</td></tr>
                        </c:if>
                    </tbody>
                </table>
                <div class="pagination">
                    <c:if test="${currentPage > 1}">
                        <a href="settinglist?page=${currentPage - 1}${not empty search ? '&search=${search}' : ''}${not empty selectedType ? '&filterType=${selectedType}' : ''}${not empty selectedStatus ? '&filterStatus=${selectedStatus}' : ''}" class="page-btn">&laquo;</a>
                    </c:if>

                    <c:forEach var="i" begin="1" end="${totalPages}">
                        <a href="settinglist?page=${i}${not empty search ? '&search=${search}' : ''}${not empty selectedType ? '&filterType=${selectedType}' : ''}${not empty selectedStatus ? '&filterStatus=${selectedStatus}' : ''}" class="page-btn ${i == currentPage ? 'active' : ''}">${i}</a>
                    </c:forEach>

                    <c:if test="${currentPage < totalPages}">
                        <a href="settinglist?page=${currentPage + 1}${not empty search ? '&search=${search}' : ''}${not empty selectedType ? '&filterType=${selectedType}' : ''}${not empty selectedStatus ? '&filterStatus=${selectedStatus}' : ''}" class="page-btn">&raquo;</a>
                    </c:if>
                </div>
                <input type="hidden" name="page" value="${currentPage}">
            </form>
        </section>

        <jsp:include page="/default/footer.jsp"/>
    </div>

    <script>
        // Bỏ đoạn JavaScript lọc trên client-side vì chúng ta đã chuyển sang lọc ở server-side
    </script>
</body>
</html>