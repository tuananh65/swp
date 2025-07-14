<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Question List</title>
    <style>
        body { font-family: Arial, sans-serif; }
        table { border-collapse: collapse; width: 100%; margin-top: 20px; }
        th, td { border: 1px solid #ccc; padding: 8px; text-align: left; }
        th { background-color: #f2f2f2; }
        ul { list-style: none; padding: 0; }
        li { display: inline; margin-right: 5px; }
        strong { color: red; }
        .debug { font-size: 0.9em; color: gray; }
        .error { color: red; }
    </style>
</head>
<body>
<h2>Question List</h2>

<!-- ✅ Debug Log Section -->
<c:if test="${not empty questions}">
    <p class="debug">[DEBUG] Number of questions fetched from DB: ${fn:length(questions)}</p>
</c:if>
<c:if test="${empty questions}">
    <p class="debug">[DEBUG] No questions fetched from DB.</p>
</c:if>

<!-- ✅ Error Message -->
<c:if test="${not empty errorMessage}">
    <p class="error">${errorMessage}</p>
</c:if>

<!-- ✅ Search Form with Course Dropdown -->
<form action="question-list" method="get">
    <input type="text" name="searchKeyword" value="${keyword}" placeholder="Search for questions" />

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

    <button type="submit">Search</button>
</form>

<!-- ✅ Create / Import Buttons -->
<p>
    <a href="question-detail">➕ Create New Question</a> |
    <a href="question-import">📥 Import Questions</a>
</p>

<!-- ✅ Question Table -->
<table>
    <thead>
    <tr>
        <th>ID</th>
        <th>Content</th>
        <th>Points</th>
        <th>Type</th>
        <th>Course ID</th>
        <th>Actions</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="question" items="${questions}">
        <tr>
            <td>${question.questionID}</td>
            <td>${question.content}</td>
            <td>${question.points}</td>
            <td>${question.questionType}</td>
            <td>${question.courseID}</td>
            <td>
                <a href="question-detail?id=${question.questionID}">Edit</a> |
                <form action="question-list" method="post" style="display:inline;">
                    <input type="hidden" name="action" value="delete" />
                    <input type="hidden" name="questionID" value="${question.questionID}" />
                    <button type="submit" onclick="return confirm('Are you sure?')">Delete</button>
                </form>
            </td>
        </tr>
    </c:forEach>
    <c:if test="${empty questions}">
        <tr>
            <td colspan="6" style="text-align: center;">No questions found.</td>
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
                        <a href="question-list?page=${i}&searchKeyword=${keyword}&courseID=${selectedCourseID}">${i}</a>
                    </c:otherwise>
                </c:choose>
            </li>
        </c:forEach>
    </ul>
</c:if>

</body>
</html>
