<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>

<jsp:include page="/default/header.jsp">
    <jsp:param name="title" value="QUESTION IMPORT"/>
</jsp:include>
<jsp:include page="/default/banner.jsp">
    <jsp:param name="bannerTitle" value="QUESTION IMPORT"/>
</jsp:include>
<jsp:include page="/default/sidebar.jsp" />
<body>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/question-import.css" />



    <div class="import-page">
        <div class="container">
            <h2>📥 Import Questions from CSV</h2>

            <c:if test="${not empty errorMessage}">
                <p class="error-message">${errorMessage}</p>
            </c:if>

            <!-- STEP 1: Upload Form -->
            <c:if test="${empty parsedQuestions && empty importResults}">
                <form action="question-import" method="post" enctype="multipart/form-data">
                    <label>Choose CSV File:</label><br>
                    <input type="file" name="importFile" accept=".csv" required>
                    <button type="submit">Upload</button>
                </form>

                <form action="question-import" method="get" style="display:inline;">
                    <input type="hidden" name="action" value="downloadTemplate">
                    <button type="submit">📥 Download CSV Template</button>
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
                <table>
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
                                            <c:when test="${r.success}">
                                                <span class="status-success">✅ ${r.message}</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="status-error">❌ ${r.message}</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:if>
                                </c:forEach>
                            </td>
                        </tr>
                    </c:forEach>
                </table>

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
                <table>
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
                                        <span class="status-success">✅ ${r.message}</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="status-error">❌ ${r.message}</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </c:forEach>
                </table>

                <p style="text-align:center; margin-top:20px;">
                    <a href="question-list">⬅️ Back to Question List</a> | 
                    <a href="question-import">🔄 New Import</a>
                </p>
            </c:if>
        </div>
    </div>

</body>
<!-- Nhúng widget chatbot đã tối ưu -->
<jsp:include page="/chatbot/chatbot-widget.jsp" />

<!-- Footer -->
<jsp:include page="/default/footer.jsp" />
</html>
