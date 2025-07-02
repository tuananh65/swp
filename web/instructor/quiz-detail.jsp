<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!-- Nhúng header chung cho trang quản trị -->
<jsp:include page="/default/header.jsp">
    <jsp:param name="title" value="Quiz Detail"/>
</jsp:include>

<!-- Hiển thị banner tiêu đề trang -->
<jsp:include page="/default/banner.jsp">
    <jsp:param name="bannerTitle" value="Quiz Detail"/>
</jsp:include>

<!-- Nhúng sidebar quản trị -->
<jsp:include page="/default/sidebar.jsp" />

<body>
    <!-- Nhúng CSS riêng cho trang danh sách bài viết -->
    <style>
        @import url('${pageContext.request.contextPath}/css/.css');
    </style>

   <main class="container mt-4">
    <h2>${isCreate ? 'Create New Quiz' : 'Edit Quiz'}</h2>

    <form action="${pageContext.request.contextPath}/instructor/${isCreate ? 'quiz-create' : 'quiz'}" method="post">
        <c:if test="${not isCreate}">
            <input type="hidden" name="id" value="${quiz.testID}" />
        </c:if>
        <div class="row">
            <div class="col-md-6 mb-3">
                <label>Quiz Title</label>
                <input type="text" name="title" value="${quiz.title}" class="form-control" required />
            </div>
            <div class="col-md-3 mb-3">
                <label>Difficulty</label>
                <select name="difficulty" class="form-control">
                    <option ${quiz.difficulty=='Easy' ? 'selected' : ''}>Easy</option>
                    <option ${quiz.difficulty=='Medium' ? 'selected' : ''}>Medium</option>
                    <option ${quiz.difficulty=='Hard' ? 'selected' : ''}>Hard</option>
                </select>
            </div>
            <div class="col-md-3 mb-3">
                <label>Duration (minutes)</label>
                <input type="number" name="duration" value="${quiz.duration}" class="form-control" required />
            </div>
        </div>
        <div class="mb-3">
            <label>Description</label>
            <textarea name="description" class="form-control">${quiz.description}</textarea>
        </div>
        <div class="form-check mb-3">
            <input type="checkbox" name="active" class="form-check-input" ${quiz.active ? 'checked' : ''} />
            <label class="form-check-label">Active</label>
        </div>

        <button type="submit" class="btn btn-success">${isCreate ? 'Create' : 'Save Changes'}</button>
        <a href="${pageContext.request.contextPath}/instructor/quizzes" class="btn btn-secondary">Cancel</a>
    </form>
</main>



    <!-- Nhúng chatbot tương tác (nếu có) -->
    <jsp:include page="/chatbot/chatbot-widget.jsp" />

    <!-- Nhúng footer chung -->
    <jsp:include page="/default/footer.jsp" />
</body>

</html>
