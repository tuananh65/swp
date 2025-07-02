<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!-- Nhúng header chung cho trang quản trị -->
<jsp:include page="/default/header.jsp">
    <jsp:param name="title" value="Quiz List"/>
</jsp:include>

<!-- Hiển thị banner tiêu đề trang -->
<jsp:include page="/default/banner.jsp">
    <jsp:param name="bannerTitle" value="Quiz List"/>
</jsp:include>

<!-- Nhúng sidebar quản trị -->
<jsp:include page="/default/sidebar.jsp" />

<body>
    <!-- Nhúng CSS riêng cho trang danh sách bài viết -->
    <style>
        @import url('${pageContext.request.contextPath}/css/.css');
    </style>
<main class="container mt-4">
    <h2>Quiz List</h2>

    <a href="${pageContext.request.contextPath}/instructor/quiz-create" class="btn btn-success mb-3">+ Create New Quiz</a>

    <table class="table table-bordered">
        <thead>
            <tr>
                <th>ID</th>
                <th>Title</th>
                <th>Difficulty</th>
                <th>Duration</th>
                <th>Active</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="q" items="${quizzes}">
                <tr>
                    <td>${q.testID}</td>
                    <td>${q.title}</td>
                    <td>${q.difficulty}</td>
                    <td>${q.duration}</td>
                    <td>${q.active ? 'Yes' : 'No'}</td>
                    <td>
                        <a href="${pageContext.request.contextPath}/instructor/quiz?id=${q.testID}" class="btn btn-sm btn-primary">Edit</a>
                        <a href="${pageContext.request.contextPath}/instructor/quiz-delete?id=${q.testID}" class="btn btn-sm btn-danger" onclick="return confirm('Delete this quiz?')">Delete</a>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</main>



    <!-- Nhúng chatbot tương tác (nếu có) -->
    <jsp:include page="/chatbot/chatbot-widget.jsp" />

    <!-- Nhúng footer chung -->
    <jsp:include page="/default/footer.jsp" />
</body>

</html>
