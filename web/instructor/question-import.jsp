<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Import Questions</title>
</head>
<body>
    <h2>Import Questions from CSV</h2>

    <c:if test="${not empty errorMessage}">
        <p style="color:red;">${errorMessage}</p>
    </c:if>

    <!-- STEP 1: Upload Form -->
    <c:if test="${empty parsedQuestions && empty importResults}">
        <form action="question-import" method="post" enctype="multipart/form-data">
            <label>Choose CSV File:</label>
            <input type="file" name="importFile" accept=".csv" required>
            <button type="submit">Upload</button>
        </form>

        <p><b>Expected CSV Format:</b></p>
        <pre>
CourseID,Content,Points,QuestionType,Answer1,IsCorrect1,Answer2,IsCorrect2,Answer3,IsCorrect3
1,What is Java?,5,Multiple Choice,Programming language,true,Database,false,Operating System,false
2,Define Communication.,5,Short Answer,Exchange of information,true
        </pre>
    </c:if>

    <!-- STEP 2: Preview Uploaded Questions -->
<c:if test="${not empty parsedQuestions && empty importResults}">
    <h3>📋 Preview Parsed Questions</h3>
    <table border="1" cellpadding="5">
        <tr>
            <th>#</th>
            <th>CourseID</th>
            <th>Content</th>
            <th>Type</th>
            <th>Points</th>
            <th>Answers</th>
            <th>Status</th>
        </tr>
        <c:forEach var="q" items="${parsedQuestions}" varStatus="loop">
            <tr>
                <td>${loop.index + 1}</td>
                <td>${q.courseID}</td>
                <td>${q.content}</td>
                <td>${q.questionType}</td>
                <td>${q.points}</td>
                <td>
                    <c:forEach var="a" items="${q.answers}">
                        ${a.content} [${a.correct}]<br/>
                    </c:forEach>
                </td>
                <td>
                    <c:forEach var="r" items="${validationResults}" varStatus="status">
                        <c:if test="${status.index == loop.index}">
                            <c:choose>
                                <c:when test="${r.success}">✅ ${r.message}</c:when>
                                <c:otherwise>❌ ${r.message}</c:otherwise>
                            </c:choose>
                        </c:if>
                    </c:forEach>
                </td>
            </tr>
        </c:forEach>
    </table>

    <br>
<form action="question-import" method="post" style="display:inline;">
    <input type="hidden" name="step" value="confirm">
    <button type="submit">✅ Confirm Import</button>
</form>
<form action="question-import" method="get" style="display:inline;">
    <button type="submit">❌ Cancel</button>
</form>

</c:if>



    <!-- STEP 3: Report Result -->
<c:if test="${not empty importResults}">
    <h3>✅ Import Report</h3>
    <table border="1" cellpadding="5">
        <tr>
            <th>Row</th>
            <th>Status</th>
        </tr>
        <c:forEach var="r" items="${importResults}">
            <tr>
                <td>${r.rowNumber}</td>
                <td>
                    <c:choose>
                        <c:when test="${r.success}">
                            ✅ ${r.message}
                        </c:when>
                        <c:otherwise>
                            ❌ ${r.message}
                        </c:otherwise>
                    </c:choose>
                </td>
            </tr>
        </c:forEach>
    </table>
    <br>
    <p><a href="question-list">⬅️ Back to Question List</a></p>
    <p><a href="question-import">🔄 New Import</a></p>
</c:if>

</body>
</html>
