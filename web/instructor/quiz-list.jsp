<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Quiz List</title>
    <style>
        body { font-family: Arial, sans-serif; }
        table { border-collapse: collapse; width: 100%; margin-top: 20px; }
        th, td { border: 1px solid #ccc; padding: 8px; text-align: left; }
        th { background-color: #f2f2f2; }
        ul { list-style: none; padding: 0; }
        li { display: inline; margin-right: 5px; }
        strong { color: red; }
        .error { color: red; }
        form.inline { display: inline; }
    </style>
</head>
<body>
<h2>Quiz List</h2>

<!-- ✅ Error Message -->
<c:if test="${not empty errorMessage}">
    <p class="error">${errorMessage}</p>
</c:if>

<!-- ✅ Filter Form -->
<form action="quiz-list" method="get">
    <input type="text" name="searchKeyword" value="${keyword}" placeholder="Search by title" />

    <select name="courseID">
        <option value="">All Courses</option>
        <c:forEach var="c" items="${courses}">
            <option value="${c.courseID}"
                <c:if test="${selectedCourseID != null && selectedCourseID == c.courseID}">selected</c:if>
            >
                ${c.courseName}
            </option>
        </c:forEach>
    </select>

    <select name="status">
        <option value="">All Status</option>
        <option value="Active" ${statusFilter == 'Active' ? 'selected' : ''}>Active</option>
        <option value="Inactive" ${statusFilter == 'Inactive' ? 'selected' : ''}>Inactive</option>
    </select>

    <button type="submit">Search</button>
</form>

<!-- ✅ Create Quiz Button -->
<p>
    <a href="quiz-detail">➕ Create New Quiz</a>
</p>

<!-- ✅ Quiz Table -->
<table>
    <thead>
    <tr>
        <th>ID</th>
        <th>Title</th>
        <th>CourseID</th>
        <th>Duration (min)</th>
        <th>Difficulty</th>
        <th>Status</th>
        <th>Actions</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="quiz" items="${quizzes}">
        <tr>
            <td>${quiz.quizID}</td>
            <td>${quiz.title}</td>
            <td>${quiz.courseID}</td>
            <td>${quiz.duration}</td>
            <td>${quiz.difficulty}</td>
            <td>
                <c:choose>
                    <c:when test="${quiz.active}">Active</c:when>
                    <c:otherwise>Inactive</c:otherwise>
                </c:choose>
            </td>
            <td>
                <a href="quiz-detail?id=${quiz.quizID}">Edit</a> |
                <form action="quiz-list" method="post" class="inline">
                    <input type="hidden" name="action" value="delete" />
                    <input type="hidden" name="quizID" value="${quiz.quizID}" />
                    <button type="submit" onclick="return confirm('Are you sure?')">Delete</button>
                </form>
            </td>
        </tr>
    </c:forEach>
    <c:if test="${empty quizzes}">
        <tr>
            <td colspan="7" style="text-align: center;">No quizzes found.</td>
        </tr>
    </c:if>
    </tbody>
</table>

<!-- ✅ Pagination -->
<c:if test="${totalPages > 1}">
    <h3>Pages:</h3>
    <ul>
        <c:forEach var="i" begin="1" end="${totalPages}">
            <li>
                <c:choose>
                    <c:when test="${i == currentPage}">
                        <strong>${i}</strong>
                    </c:when>
                    <c:otherwise>
                        <a href="quiz-list?page=${i}&searchKeyword=${keyword}&courseID=${selectedCourseID}&status=${statusFilter}">${i}</a>
                    </c:otherwise>
                </c:choose>
            </li>
        </c:forEach>
    </ul>
</c:if>

</body>
</html>
