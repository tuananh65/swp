<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="/default/header.jsp">
    <jsp:param name="title" value="Quiz List"/>
</jsp:include>

<jsp:include page="/default/banner.jsp">
    <jsp:param name="bannerTitle" value="Quiz List"/>
</jsp:include>

<jsp:include page="/default/sidebar.jsp" />

<body>
<main class="container mt-4">
    <h2 class="mb-3">Quiz List</h2>

    <div class="d-flex justify-content-between mb-3">
    <a href="${pageContext.request.contextPath}/instructor/quiz" class="btn btn-success mb-3">
    + Create New Quiz
</a>


    <form class="d-flex" method="get" action="${pageContext.request.contextPath}/instructor/quiz-list">
        <input class="form-control me-2" type="search" name="keyword" value="${keyword}" placeholder="Search by title" aria-label="Search">
        <button class="btn btn-outline-primary" type="submit">Search</button>
    </form>
</div>


    <c:if test="${empty quizzes}">
        <div class="alert alert-warning">No quizzes found.</div>
    </c:if>

    <c:if test="${not empty quizzes}">
        <div class="table-responsive">
            <table class="table table-striped table-bordered">
                <thead class="table-dark">
                    <tr>
                        <th>ID</th>
                        <th>Title</th>
                        <th>Course</th>
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
                            <td>${q.courseName}</td>
                            <td>${q.difficulty}</td>
                            <td>${q.duration} min</td>
                            <td>
                                <c:choose>
                                    <c:when test="${q.active}"><span class="badge bg-success">Yes</span></c:when>
                                    <c:otherwise><span class="badge bg-secondary">No</span></c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <a href="${pageContext.request.contextPath}/instructor/quiz?id=${q.testID}" class="btn btn-sm btn-primary">
                                    Edit
                                </a>
                                <a href="#" class="btn btn-sm btn-danger" onclick="confirmDelete(${q.testID})">
                                    Delete
                                </a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </c:if>
        <c:if test="${totalPage > 1}">
    <nav>
        <ul class="pagination justify-content-center">
            <c:if test="${currentPage > 1}">
                <li class="page-item">
                    <a class="page-link" href="?page=${currentPage - 1}&keyword=${keyword}">Previous</a>
                </li>
            </c:if>

            <c:forEach var="i" begin="1" end="${totalPage}">
                <li class="page-item ${i == currentPage ? 'active' : ''}">
                    <a class="page-link" href="?page=${i}&keyword=${keyword}">${i}</a>
                </li>
            </c:forEach>

            <c:if test="${currentPage < totalPage}">
                <li class="page-item">
                    <a class="page-link" href="?page=${currentPage + 1}&keyword=${keyword}">Next</a>
                </li>
            </c:if>
        </ul>
    </nav>
</c:if>

</main>

<script>
    function confirmDelete(id) {
        if (confirm("Are you sure you want to delete this quiz? This cannot be undone!")) {
            window.location.href = '${pageContext.request.contextPath}/instructor/quiz-delete?id=' + id;
        }
    }
</script>

<jsp:include page="/chatbot/chatbot-widget.jsp" />
<jsp:include page="/default/footer.jsp" />
</body>
</html>
