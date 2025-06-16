<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Danh Sách Đăng Ký - Soft Skills</title>
    <link href="${pageContext.request.contextPath}/css/styles.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/myRegistrations.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
</head>
<body>
    <!-- Navigation Bar -->
    <nav>
        <jsp:include page="/default/header.jsp"/>
        <div class="login-register-links" id="userSection">
            <a href="${pageContext.request.contextPath}/view/changePassword.jsp" class="custom-btn">Change Password</a>
            <a href="${pageContext.request.contextPath}/profile?id=3" class="custom-btn">Profile</a>
            <a href="${pageContext.request.contextPath}/logout" class="custom-btn">Đăng xuất</a>
        </div>
    </nav>
        
    <div class="banner">
        <img src="${pageContext.request.contextPath}/images/Banner.jpg" alt="Banner">
        <div class="banner-text">
            <h1 class="banner-title">My Registrations</h1>
            <div class="banner-breadcrumb">
                <table>
                    <tr>
                        <td>Home</td>
                        <td>My Registrations</td>
                    </tr>
                </table>
            </div>
        </div>
    </div>

    <div class="main-container">
        <aside class="sidebar">
            <form action="${pageContext.request.contextPath}/courseList" method="get">
                <div class="search-box">
                    <input type="text" name="search" placeholder="Tìm kiếm khóa học..." value="${fn:escapeXml(searchTerm)}">
                    <button type="submit" class="search-btn"><i class="fas fa-search"></i></button>
                    <input type="hidden" name="showThumbnail" value="${showThumbnail}">
                    <input type="hidden" name="showPrice" value="${showPrice}">
                    <input type="hidden" name="showRegister" value="${showRegister}">
                    <input type="hidden" name="showTagline" value="${showTagline}">
                    <input type="hidden" name="showTitle" value="${showTitle}">
                    <input type="hidden" name="sort" value="${sortBy}">
                    <input type="hidden" name="pageSize" value="${pageSize}">
                </div>
            </form>
            <div class="subject-categories">
                <h3>Subject category</h3>
                <ul>
                    <c:forEach var="category" items="${categories}">
                        <li><a href="${pageContext.request.contextPath}/courseList?category=${fn:escapeXml(category)}">${fn:escapeXml(category)}</a></li>
                    </c:forEach>
                </ul>
            </div>
            <div class="contact-info">
                <div class="contact-item"><i class="fas fa-phone"></i><span>(555) 567-8888</span></div>
                <div class="contact-item"><i class="fas fa-map-marker-alt"></i><span>Hudson, Wisconsin(WI), 54016</span></div>
                <div class="contact-item"><i class="fas fa-envelope"></i><span>contact@example.com</span></div>
            </div>
        </aside>

        <main class="main-content">
            <c:if test="${not empty message}">
                <div class="success-message">${fn:escapeXml(message)}</div>
            </c:if>
            <c:if test="${not empty error}">
                <div class="error-message">${fn:escapeXml(error)}</div>
            </c:if>
            <div class="registrations-grid">
                <c:choose>
                    <c:when test="${not empty registrations}">
                        <c:forEach var="reg" items="${registrations}">
                            <div class="registration-card">
                                <div class="registration-details">
                                    <h3><a href="${pageContext.request.contextPath}/CourseDetailServlet?courseId=${reg.courseID}">${fn:escapeXml(reg.getCourseName())}</a></h3>
                                    <p><strong>Course ID:</strong> ${reg.courseID}</p>
                                    <p><strong>Thời gian đăng ký:</strong> <fmt:formatDate value="${reg.registrationTime}" pattern="dd/MM/yyyy HH:mm"/></p>
                                    <p><strong>Gói:</strong> ${fn:escapeXml(reg.packageName)}</p>
                                    <p><strong>Price:</strong> <fmt:formatNumber value="${reg.totalCost}" type="currency"/></p>
                                    <p><strong>Status:</strong> ${fn:escapeXml(reg.status)}</p>
                                    <p><strong>Valid From:</strong> <fmt:formatDate value="${reg.validFrom}" pattern="dd/MM/yyyy"/></p>
                                    <p><strong>Valid To:</strong> <fmt:formatDate value="${reg.validTo}" pattern="dd/MM/yyyy"/></p>
                                    <c:if test="${reg.status == 'submitted'}">
                                        <div class="registration-actions">
                                            <a href="${pageContext.request.contextPath}/myRegistrations?action=cancel&registrationId=${reg.registrationID}" 
                                               class="action-btn cancel-btn" 
                                               onclick="return confirm('Bạn có chắc muốn hủy đăng ký này?')">Cancel</a>
                                            <a href="${pageContext.request.contextPath}/CourseDetailServlet?courseId=${reg.courseID}&editRegistration=${reg.registrationID}" 
                                               class="action-btn edit-btn">Edit</a>
                                        </div>
                                    </c:if>
                                </div>
                            </div>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <div class="no-registrations">
                            <p>Bạn chưa có đăng ký nào.</p>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>

            <!-- Pagination -->
            <div class="pagination">
                <c:if test="${totalPages > 1}">
                    <c:if test="${currentPage > 1}">
                        <a class="page-btn" href="${pageContext.request.contextPath}/myRegistrations?page=${currentPage - 1}">
                            <i class="fas fa-chevron-left"></i>
                        </a>
                    </c:if>
                    <c:forEach var="i" begin="1" end="${totalPages}">
                        <c:choose>
                            <c:when test="${i == currentPage}">
                                <span class="page-btn active">${i}</span>
                            </c:when>
                            <c:when test="${i <= 4 || i > totalPages - 4 || (i >= currentPage - 2 && i <= currentPage + 2)}">
                                <a class="page-btn" href="${pageContext.request.contextPath}/myRegistrations?page=${i}">${i}</a>
                            </c:when>
                            <c:when test="${i == 5 && currentPage > 6 || i == totalPages - 4 && currentPage < totalPages - 5}">
                                <span class="page-dots">...</span>
                            </c:when>
                        </c:choose>
                    </c:forEach>
                    <c:if test="${currentPage < totalPages}">
                        <a class="page-btn" href="${pageContext.request.contextPath}/myRegistrations?page=${currentPage + 1}">
                            <i class="fas fa-chevron-right"></i>
                        </a>
                    </c:if>
                </c:if>
            </div>
        </main>
    </div>

    <!-- Footer -->
        <jsp:include page="/default/footer.jsp"/>            
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>