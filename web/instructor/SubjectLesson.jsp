<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Subject Lesson</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/SubjectLesson.css" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
</head>
<body>
<div class="wrapper">
    <%-- Bao gồm Header. Đảm bảo file /default/header.jsp tồn tại. --%>
    <jsp:include page="/default/header.jsp"/>

    <section class="hero-banner">
        <div class="hero-content">
            <div class="hero-overlay"></div>
            <%-- Đảm bảo hình ảnh Banner.jpg có sẵn trong /images --%>
            <img class="hero-image" src="${pageContext.request.contextPath}/images/Banner.jpg" alt="Banner">
            <div class="hero-text">
                <h1>SUBJECT LESSON</h1>
                <div class="hero-subtitle">
                    <p>Subject / Lesson Page</p>
                </div>
            </div>
        </div>
    </section>

    <section class="content-section">
        <%-- Hiển thị thông báo thành công/lỗi từ Servlet (đọc từ request scope sau khi Servlet chuyển từ session) --%>
        <%-- Message container cho các thông báo --%>
        <div id="message-container">
            <c:if test="${not empty message}">
                <div class="message-banner success-message">${message}</div>
            </c:if>
            <c:if test="${not empty error}">
                <div class="message-banner error-message">${error}</div>
            </c:if>
        </div>

        <%-- Course Title Block --%>
        <div class="course-title-block">
            <i class="fas fa-star course-star-icon"></i>
            <h1 class="course-main-title"><c:out value="${subjectName}" /></h1>
        </div>

        <%-- New Filter and Add Section --%>
        <div class="filter-and-add-section">
            <%-- Form action sẽ gửi request đến /subjectlesson (Servlet URL) --%>
            <form action="${pageContext.request.contextPath}/subjectlesson" method="get" class="filter-form">
                <input type="hidden" name="subjectId" value="${subjectId}">
                <%-- Khi filter/search, luôn reset về trang 1 --%>
                <input type="hidden" name="page" value="1"> 

                <select id="typeFilter" name="filterType" class="filter-select" onchange="this.form.submit()">
                    <option value="">All Lesson Groups</option>
                    <c:forEach var="type" items="${uniqueTypes}">
                        <option value="${type}" ${type eq selectedType ? 'selected' : ''}>${type}</option>
                    </c:forEach>
                </select>

                <select id="statusFilterMain" name="filterStatus" class="filter-select" onchange="this.form.submit()">
                    <option value="">All Statuses</option>
                    <option value="Active" ${'Active' eq selectedStatus ? 'selected' : ''}>Active</option>
                    <option value="Inactive" ${'Inactive' eq selectedStatus ? 'selected' : ''}>Inactive</option>
                </select>

                <div class="search-input-group">
                    <input type="text" class="search-input-field" name="search" value="${search}" placeholder="Type lesson name to search">
                    <button type="submit" class="search-filter-button"><i class="fas fa-search"></i></button>
                </div>
            </form>
            <%-- Link đến trang thêm bài học, sử dụng LessonDetailServlet với subjectId và không có lessonId --%>
            <a href="${pageContext.request.contextPath}/instructor/lessondetail?subjectId=${subjectId}" class="add-lesson-btn">
                <i class="fas fa-plus-circle"></i> Add Lesson
            </a>
        </div>

        <div class="table-container">
            <table>
                <thead>
                    <tr>
                        <th>LessonID</th>
                        <th>Title</th>
                        <th>Order</th>
                        <th>Type</th>
                        <th>Status</th>
                        <th>FUNCTION</th>
                    </tr>
                </thead>
                <tbody id="lessonTableBody">
                <c:forEach var="lesson" items="${lessons}">
                    <tr>
                        <td data-label="LessonID">${lesson.lessonId}</td>
                        <td data-label="Title">${lesson.title}</td>
                        <td data-label="Order">${lesson.order}</td>
                        <td data-label="Type">${lesson.type}</td>
                        <td data-label="Status">
                            <%-- Thêm onclick để thay đổi trạng thái khi nhấp vào --%>
                            <%-- Không cần ID cho thẻ span nếu không dùng AJAX để cập nhật DOM --%>
                            <span class="status ${lesson.status == 'Active' ? 'status-active' : 'status-inactive'}"
                                  onclick="toggleLessonStatus(${lesson.lessonId}, ${subjectId}, '${lesson.status}', 
                                                              '${selectedType}', '${selectedStatus}', '${search}', ${currentPage})">
                                ${lesson.status}
                            </span>
                        </td>
                        <td data-label="FUNCTION" class="function-buttons">
                            <%-- Cập nhật action của form EDIT để trỏ đến LessonDetailServlet --%>
                            <form action="${pageContext.request.contextPath}/instructor/lessondetail" method="get" style="display:inline;">
                                <input type="hidden" name="lessonId" value="${lesson.lessonId}">
                                <input type="hidden" name="subjectId" value="${subjectId}"> <%-- Đảm bảo truyền subjectId --%>
                                <button type="submit" class="action-btn edit-btn">
                                    <i class="fas fa-edit"></i> EDIT
                                </button>
                            </form>
                            <%-- Form Xóa: Gửi POST đến cùng SubjectLessonServlet với action="delete" --%>
                            <form action="${pageContext.request.contextPath}/subjectlesson" method="post" style="display:inline;" 
                                  onsubmit="return confirm('Are you sure you want to delete lesson ID ${lesson.lessonId} - \'${lesson.title}\'?');">
                                <input type="hidden" name="action" value="delete"> 
                                <input type="hidden" name="lessonId" value="${lesson.lessonId}">
                                <input type="hidden" name="subjectId" value="${subjectId}">
                                <input type="hidden" name="filterType" value="${selectedType}">
                                <input type="hidden" name="filterStatus" value="${selectedStatus}">
                                <input type="hidden" name="search" value="${search}">
                                <input type="hidden" name="page" value="${currentPage}">
                                <button type="submit" class="action-btn delete-btn">
                                    <i class="fas fa-trash-alt"></i> DELETE
                                </button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty lessons}">
                    <tr><td colspan="6" class="no-lessons-found">No lessons found.</td></tr>
                </c:if>
                </tbody>
            </table>
        </div>

        <div class="pagination-container">
            <c:if test="${currentPage > 1}">
                <a href="${pageContext.request.contextPath}/subjectlesson?page=${currentPage - 1}&subjectId=${subjectId}<c:if test="${not empty search}">&search=<c:out value="${search}"/></c:if><c:if test="${not empty selectedType}">&filterType=<c:out value="${selectedType}"/></c:if><c:if test="${not empty selectedStatus}">&filterStatus=<c:out value="${selectedStatus}"/></c:if>"
                   class="page-btn page-nav-btn">&laquo;</a>
            </c:if>
            <c:forEach var="i" begin="1" end="${totalPages}">
                <a href="${pageContext.request.contextPath}/subjectlesson?page=${i}&subjectId=${subjectId}<c:if test="${not empty search}">&search=<c:out value="${search}"/></c:if><c:if test="${not empty selectedType}">&filterType=<c:out value="${selectedType}"/></c:if><c:if test="${not empty selectedStatus}">&filterStatus=<c:out value="${selectedStatus}"/></c:if>"
                   class="page-btn ${i == currentPage ? 'active' : ''}">${i}</a>
            </c:forEach>
            <c:if test="${currentPage < totalPages}">
                <a href="${pageContext.request.contextPath}/subjectlesson?page=${currentPage + 1}&subjectId=${subjectId}<c:if test="${not empty search}">&search=<c:out value="${search}"/></c:if><c:if test="${not empty selectedType}">&filterType=<c:out value="${selectedType}"/></c:if><c:if test="${not empty selectedStatus}">&filterStatus=<c:out value="${selectedStatus}"/></c:if>"
                   class="page-btn page-nav-btn">&raquo;</a>
            </c:if>
        </div>
    </section>

    <%-- Bao gồm Footer. Đảm bảo file /default/footer.jsp tồn tại. --%>
    <jsp:include page="/default/footer.jsp"/>
</div>

<script>
    function toggleLessonStatus(lessonId, subjectId, currentStatus, filterType, filterStatus, searchQuery, currentPage) {
        let newStatus = (currentStatus === 'Active') ? 'Inactive' : 'Active';
        let confirmMessage = (newStatus === 'Inactive') ? 'Bạn có chắc chắn muốn vô hiệu hóa bài học này không?' : 'Bạn có chắc chắn muốn kích hoạt bài học này không?';

        if (confirm(confirmMessage)) {
            let form = document.createElement('form');
            form.method = 'POST';
            form.action = '${pageContext.request.contextPath}/subjectlesson'; // Gửi đến servlet

            // Thêm các input ẩn vào form
            // Action để servlet biết đây là yêu cầu thay đổi trạng thái
            let actionInput = document.createElement('input');
            actionInput.type = 'hidden';
            actionInput.name = 'action';
            actionInput.value = 'toggleStatus'; // Giá trị này sẽ được servlet đọc
            form.appendChild(actionInput);

            // Lesson ID của bài học cần thay đổi
            let lessonIdInput = document.createElement('input');
            lessonIdInput.type = 'hidden';
            lessonIdInput.name = 'lessonId';
            lessonIdInput.value = lessonId;
            form.appendChild(lessonIdInput);

            // Trạng thái mới mà bạn muốn thiết lập
            let newStatusInput = document.createElement('input');
            newStatusInput.type = 'hidden';
            newStatusInput.name = 'newStatus';
            newStatusInput.value = newStatus;
            form.appendChild(newStatusInput);

            // Các tham số lọc, tìm kiếm, phân trang để duy trì trạng thái sau khi redirect
            let subjectIdInput = document.createElement('input');
            subjectIdInput.type = 'hidden';
            subjectIdInput.name = 'subjectId';
            subjectIdInput.value = subjectId;
            form.appendChild(subjectIdInput);

            let filterTypeInput = document.createElement('input');
            filterTypeInput.type = 'hidden';
            filterTypeInput.name = 'filterType';
            filterTypeInput.value = filterType;
            form.appendChild(filterTypeInput);

            let filterStatusInput = document.createElement('input');
            filterStatusInput.type = 'hidden';
            filterStatusInput.name = 'filterStatus';
            filterStatusInput.value = filterStatus;
            form.appendChild(filterStatusInput);

            let searchInput = document.createElement('input');
            searchInput.type = 'hidden';
            searchInput.name = 'search';
            searchInput.value = searchQuery;
            form.appendChild(searchInput);

            let pageInput = document.createElement('input');
            pageInput.type = 'hidden';
            pageInput.name = 'page';
            pageInput.value = currentPage;
            form.appendChild(pageInput);

            // Gắn form vào body và submit
            document.body.appendChild(form);
            form.submit();
        }
    }
</script>
</body>
</html>