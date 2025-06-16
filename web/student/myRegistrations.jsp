<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Registrations - Soft Skills</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/myRegistrations.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
</head>
<body>
    <!-- Navigation Bar -->
    <nav>
        <jsp:include page="/view/header.jsp"/>
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
                        <td>Student Dashboard</td>
                    </tr>
                </table>
            </div>
        </div>
    </div>

    <!-- Main Content -->
    <div class="main-container">
        <!-- Sidebar -->
        <aside class="sidebar">
            <form action="${pageContext.request.contextPath}/myRegistrations" method="get" id="searchForm">
                <div class="search-box">
                    <input type="text" name="searchSubject" placeholder="Search Subject..." value="${param.searchSubject}">
                    <button type="submit" class="search-btn"><i class="fas fa-search"></i></button>
                </div>
            </form>
            <div class="subject-categories">
                <h3>SUBJECT CATEGORY</h3>
                <ul>
                    <li><a href="?searchSubject=Leadership">Leadership</a></li>
                    <li><a href="?searchSubject=Communication">Communication</a></li>
                    <li><a href="?searchSubject=Time Management">Time Management</a></li>
                    <li><a href="?searchSubject=Teamwork">Teamwork</a></li>
                    <li><a href="?searchSubject=Art & Humanities">Art & Humanities</a></li>
                    <li><a href="?searchSubject=Mobile Application">Mobile Application</a></li>
                </ul>
            </div>
            <div class="contact-info">
                <div class="contact-item"><i class="fas fa-phone"></i><span>(555) 567-987-237</span></div>
                <div class="contact-item"><i class="fas fa-map-marker-alt"></i><span>Hudson, Wisconsin(WI), 54016</span></div>
                <div class="contact-item"><i class="fas fa-envelope"></i><span>contact@gmail.com</span></div>
            </div>
        </aside>

        <!-- Registration List -->
        <main class="main-content">
            <c:if test="${not empty registrations}">
                <c:forEach var="reg" items="${registrations}">
                    <div class="registration-card">
                        <div class="registration-details">
                            <p><strong>ID:</strong> ${reg.enrollmentId}</p>
                            <p><strong>Subject:</strong> ${reg.courseName}</p>
                            <p><strong>Registration Time:</strong> <fmt:formatDate value="${reg.enrollmentDate}" pattern="dd/MM/yyyy HH:mm:ss" /></p>
                            <p><strong>Package:</strong> ${reg.packageName}</p>
                            <p><strong>Total Cost:</strong> $<fmt:formatNumber value="${reg.totalPrice}" type="currency" currencySymbol="$"/></p>
                            <p><strong>Status:</strong> ${reg.status}</p>
                            <p><strong>Valid From:</strong> <fmt:formatDate value="${reg.validFrom}" pattern="dd/MM/yyyy" /></p>
                            <p><strong>Valid To:</strong> <fmt:formatDate value="${reg.validTo}" pattern="dd/MM/yyyy" /></p>
                        </div>
                        <c:if test="${reg.status == 'submitted'}">
                            <div class="registration-actions">
                                <form action="${pageContext.request.contextPath}/myRegistrations?action=cancel" method="post" style="display:inline;">
                                    <input type="hidden" name="enrollmentId" value="${reg.enrollmentId}">
                                    <button type="submit" class="delete-btn">Cancel</button>
                                </form>
                                <a href="${pageContext.request.contextPath}/CourseDetailServlet?courseId=${reg.courseId}&action=edit&enrollmentId=${reg.enrollmentId}" class="edit-btn">Edit</a>
                            </div>
                        </c:if>
                    </div>
                </c:forEach>
            </c:if>
            <c:if test="${empty registrations}">
                <p>No registrations found.</p>
            </c:if>
        </main>
    </div>

    <!-- Footer -->
    <jsp:include page="/view/footer.jsp"/>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>