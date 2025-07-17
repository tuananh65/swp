<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>

<jsp:include page="/default/header.jsp">
    <jsp:param name="title" value="QUIZ LIST"/>
</jsp:include>
<jsp:include page="/default/banner.jsp">
    <jsp:param name="bannerTitle" value="QUIZ LIST"/>
</jsp:include>
<jsp:include page="/default/sidebar.jsp" />
<body>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/quiz-list.css" />
<div class="quiz-page">
  <div class="container">

    <h2>📋 Quiz List</h2>

    <c:if test="${not empty errorMessage}">
        <p class="error">${errorMessage}</p>
    </c:if>

    <!-- ✅ Filter Form -->
    <form class="filter-form" action="quiz-list" method="get">
        <input type="text" name="searchKeyword" value="${keyword}" placeholder="🔍 Search by title" />

        <select name="courseID">
            <option value="">All Courses</option>
            <c:forEach var="c" items="${courses}">
                <option value="${c.courseID}" <c:if test="${selectedCourseID == c.courseID}">selected</c:if>>
                    ${c.courseName}
                </option>
            </c:forEach>
        </select>

        <select name="status">
            <option value="">All Status</option>
            <option value="Active" ${statusFilter == 'Active' ? 'selected' : ''}>Active</option>
            <option value="Inactive" ${statusFilter == 'Inactive' ? 'selected' : ''}>Inactive</option>
        </select>

        <select name="difficulty">
            <option value="">All Difficulty</option>
            <option value="Easy" ${difficultyFilter == 'Easy' ? 'selected' : ''}>Easy</option>
            <option value="Medium" ${difficultyFilter == 'Medium' ? 'selected' : ''}>Medium</option>
            <option value="Hard" ${difficultyFilter == 'Hard' ? 'selected' : ''}>Hard</option>
        </select>
        
        <select name="quizType">
    <option value="">All Types</option>
    <option value="Multiple Choice" ${quizTypeFilter == 'Multiple Choice' ? 'selected' : ''}>Multiple Choice</option>
    <option value="Short Answer" ${quizTypeFilter == 'Short Answer' ? 'selected' : ''}>Short Answer</option>
</select>


        <button type="submit">🔎 Search</button>
    </form>

    <!-- ✅ Create New -->
    <p class="create-btn">
        <a href="quiz-detail">➕ Create New Quiz</a>
    </p>

    <!-- ✅ Quiz Table -->
    <table>
        <thead>
        <tr>
            <th>ID</th>
            <th>Title</th>
            <th>Course</th>
            <th>Difficulty</th>
            <th>Quiz Type</th>
            <th>Duration (min)</th>
            <th>Status</th>
            <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="quiz" items="${quizzes}">
            <tr>
                <td>${quiz.quizID}</td>
                <td>${quiz.title}</td>
                <td>${quiz.courseName}</td>
                <td>${quiz.difficulty}</td>
                <td>${quiz.quizType}</td>
                <td>${quiz.duration}</td>
                <td>
                    <c:choose>
                        <c:when test="${quiz.active}">Active</c:when>
                        <c:otherwise>Inactive</c:otherwise>
                    </c:choose>
                </td>
                <td class="actions">
                    <a href="quiz-detail?id=${quiz.quizID}">Edit</a>
                    <form action="quiz-list" method="post" class="inline" style="display:inline;">
                        <input type="hidden" name="action" value="delete" />
                        <input type="hidden" name="quizID" value="${quiz.quizID}" />
                        <button type="submit" onclick="return confirm('Are you sure?')">Delete</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
        <c:if test="${empty quizzes}">
            <tr>
                <td colspan="8" class="no-data">No quizzes found.</td>
            </tr>
        </c:if>
        </tbody>
    </table>

    <!-- ✅ Pagination -->
    <c:if test="${totalPages > 1}">
        <div class="pagination">
            <strong>Pages:</strong>
            <c:forEach var="i" begin="1" end="${totalPages}">
                <c:choose>
                    <c:when test="${i == currentPage}">
                        <strong>${i}</strong>
                    </c:when>
                    <c:otherwise>
                        <a href="quiz-list?page=${i}&searchKeyword=${keyword}&courseID=${selectedCourseID}&status=${statusFilter}&difficulty=${difficultyFilter}">${i}</a>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </div>
    </c:if>
  </div>
</div>
</body>
<!-- Nhúng widget chatbot đã tối ưu -->
<jsp:include page="/chatbot/chatbot-widget.jsp" />

<!-- Footer -->
<jsp:include page="/default/footer.jsp" />
</html>
