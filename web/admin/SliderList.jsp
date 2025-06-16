<%-- slider-list.jsp --%>
<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, model.Slider" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Sliders List - Soft Skills</title>

    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" />

    <%-- Đường dẫn đến file CSS cho Slider List --%>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/SliderLi.css" />

    <%-- Nếu bạn có CSS chung cho login hoặc các phần khác, giữ nguyên --%>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/login.css" /> 

</head>
<body>
    <div class="wrapper">
        <%-- Bao gồm header chung của bạn --%>
        <jsp:include page="/view/header.jsp"/> 

        <section class="hero-banner">
            <div class="hero-content">
                <div class="hero-overlay"></div>
                <img class="hero-image" src="${pageContext.request.contextPath}/images/Banner.jpg" alt="Banner">
                <div class="hero-text">
                    <h1>SLIDER LIST</h1>
                    <%-- Nếu bạn muốn thêm dòng "Home / Slider List" vào đây, hãy thêm vào như sau: --%>
                    <p><a href="${pageContext.request.contextPath}/home">Home</a> / Slider List</p>
                </div>
            </div>
        </section>

        <section class="content">
            <%-- Hiển thị thông báo thành công/lỗi --%>
            <c:if test="${not empty param.message}">
                <div class="success-message">${param.message}</div>
            </c:if>
            <c:if test="${not empty param.error}">
                <div class="error-message">${param.error}</div>
            </c:if>

            <div class="search-container">
                <form action="sliderlist" method="get">
                    <input type="text" class="search" name="search" value="${search}" placeholder="Search by Title">
                    <button type="submit">Search</button>
                    <input type="hidden" name="page" value="1">
                    <%-- Giữ lại các filter đã chọn nếu có (sử dụng filterStatus) --%>
                    <c:if test="${not empty filterStatus}"><input type="hidden" name="filterStatus" value="${filterStatus}"></c:if>
                </form>
                <%-- Nút "Add new Slider" --%>
                <a href="addSlider" class="add-user-btn">Add new Slider</a>
            </div>

            <%-- Form bao quanh bảng và phân trang để giữ các tham số tìm kiếm/lọc khi phân trang --%>
            <form action="sliderlist" method="get">
                <input type="hidden" name="search" value="${search}"> 
                <table>
                    <thead>
                        <tr>
                            <th>SliderId</th>
                            <th>Title</th>
                            <th>Image</th>
                            <th>Backlink</th>
                            <th>
                                <select id="filterStatus" name="filterStatus" onchange="this.form.submit()">
                                    <option value="">Status</option>
                                    <option value="active" ${'active' eq filterStatus ? 'selected' : ''}>Active</option>
                                    <option value="inactive" ${'inactive' eq filterStatus ? 'selected' : ''}>Inactive</option>
                                </select>
                            </th>
                            <th>Functions</th>
                        </tr>
                    </thead>
                    <tbody id="sliderTableBody">
                        <c:forEach var="slider" items="${sliders}">
                            <tr>
                                <td data-label="SliderId">${slider.sliderId}</td>
                                <td data-label="Title" class="title-cell">${slider.title}</td>
                                <td data-label="Image" class="image-cell">
                                    <%-- Sử dụng contextPath cho đường dẫn ảnh --%>
                                    <img src="${pageContext.request.contextPath}/${slider.image}" alt="Slider Image" style="width: 100px; height: auto; object-fit: contain;">
                                </td>
                                <td data-label="Backlink" class="backlink-cell"><a href="${slider.backlink}" target="_blank">${slider.backlink}</a></td>
                                <td data-label="Status" class="status-cell">
                                    <c:choose>
                                        <c:when test="${slider.status eq 'active' or slider.status eq 'Active'}">Active</c:when>
                                        <c:otherwise>Inactive</c:otherwise>
                                    </c:choose>
                                </td>
                                <td data-label="Functions" class="function-buttons">
                                    <%-- Nút Hide/Show --%>
                                    <c:choose>
                                        <c:when test="${slider.status eq 'active' or slider.status eq 'Active'}">
                                            <form action="hideSlider" method="post" style="display:inline;">
                                                <input type="hidden" name="sliderId" value="${slider.sliderId}">
                                                <button type="submit" class="action-btn hide-btn">Hide</button>
                                            </form>
                                        </c:when>
                                        <c:otherwise>
                                            <form action="showSlider" method="post" style="display:inline;">
                                                <input type="hidden" name="sliderId" value="${slider.sliderId}">
                                                <button type="submit" class="action-btn show-btn">Show</button>
                                            </form>
                                        </c:otherwise>
                                    </c:choose>

                                    <%-- Nút Delete --%>
                                    <form action="deleteSlider" method="post" style="display:inline;" onsubmit="return confirm('Are you sure you want to delete this slider? This action cannot be undone.');">
                                        <input type="hidden" name="sliderId" value="${slider.sliderId}">
                                        <button type="submit" class="action-btn delete-btn">Delete</button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty sliders}">
                            <tr><td colspan="6">No sliders found.</td></tr>
                        </c:if>
                    </tbody>
                </table>
                <%-- Phần phân trang --%>
                <div class="pagination">
                    <c:if test="${currentPage > 1}">
                        <a href="sliderlist?page=${currentPage - 1}<c:if test="${not empty search}">&search=${search}</c:if><c:if test="${not empty filterStatus}">&filterStatus=${filterStatus}</c:if>" class="page-btn">&laquo;</a>
                    </c:if>

                    <c:forEach var="i" begin="1" end="${totalPages}">
                        <a href="sliderlist?page=${i}<c:if test="${not empty search}">&search=${search}</c:if><c:if test="${not empty filterStatus}">&filterStatus=${filterStatus}</c:if>"
                           class="page-btn ${i == currentPage ? 'active' : ''}">${i}</a>
                    </c:forEach>

                    <c:if test="${currentPage < totalPages}">
                        <a href="sliderlist?page=${currentPage + 1}<c:if test="${not empty search}">&search=${search}</c:if><c:if test="${not empty filterStatus}">&filterStatus=${filterStatus}</c:if>" class="page-btn">&raquo;</a>
                    </c:if>
                </div>
                <input type="hidden" name="page" value="${currentPage}">
            </form>
        </section>

        <%-- Bao gồm footer chung của bạn --%>
        <jsp:include page="/view/footer.jsp"/>
    </div>

    <script>
        // Có thể thêm JavaScript nếu cần cho các chức năng tương tác (ví dụ: xác nhận xóa)
    </script>
</body>
</html>