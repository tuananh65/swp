<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<jsp:include page="/default/header.jsp">
    <jsp:param name="title" value="QUIZ DETAIL"/>
</jsp:include>
<jsp:include page="/default/banner.jsp">
    <jsp:param name="bannerTitle" value="QUIZ DETAIL"/>
</jsp:include>
<jsp:include page="/default/sidebar.jsp" />



<body>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/quiz-detail.css" />
    <main>
<c:set var="disabled" value="${quizIsActive ? 'disabled' : ''}" />

<h2 style="text-align:center;">${quiz != null ? 'Edit Quiz' : 'Create New Quiz'}</h2>

<c:if test="${not empty errorMessage}">
    <p class="error">${errorMessage}</p>
</c:if>

<c:if test="${not empty successMessage}">
    <p style="color: green; text-align: center;">${successMessage}</p>
</c:if>

<c:if test="${quizIsActive}">
    <p style="color: orange; font-weight: bold; text-align: center;">
        ⚠️ This quiz is currently <strong>active</strong>. Editing is disabled.
    </p>
</c:if>

<form action="quiz-detail" method="post">
    <c:if test="${quiz != null}">
        <input type="hidden" name="quizID" value="${quiz.quizID}" />
    </c:if>

    <label>Title:</label>
    <input type="text" name="title" required value="${quiz != null ? quiz.title : ''}" ${disabled} />

    <label>Description:</label>
    <textarea name="description" rows="4" ${disabled}>${quiz != null ? quiz.description : ''}</textarea>

    <label>Duration (minutes):</label>
    <input type="number" name="duration" required value="${quiz != null ? quiz.duration : 30}" ${disabled} />

    <label>Difficulty:</label>
    <select name="difficulty" ${disabled}>
        <option value="Easy" ${quiz != null && quiz.difficulty == 'Easy' ? 'selected' : ''}>Easy</option>
        <option value="Medium" ${quiz != null && quiz.difficulty == 'Medium' ? 'selected' : ''}>Medium</option>
        <option value="Hard" ${quiz != null && quiz.difficulty == 'Hard' ? 'selected' : ''}>Hard</option>
    </select>

    <label>Quiz Type:</label>
    <select name="quizType" required ${disabled}>
        <option value="Multiple Choice" ${quiz != null && quiz.quizType == 'Multiple Choice' ? 'selected' : ''}>Multiple Choice</option>
        <option value="Short Answer" ${quiz != null && quiz.quizType == 'Short Answer' ? 'selected' : ''}>Short Answer</option>
    </select>

    <label>Course:</label>
    <select name="courseID" required ${disabled}>
        <option value="">-- Select Course --</option>
        <c:forEach var="c" items="${courses}">
            <option value="${c.courseID}" <c:if test="${quiz != null && quiz.courseID == c.courseID}">selected</c:if>>
                ${c.courseName}
            </option>
        </c:forEach>
    </select>

    <label>
        <input type="checkbox" name="isActive" ${quiz != null && quiz.active ? 'checked' : ''} ${disabled}/>
        Active
    </label>

    <button type="submit" ${quizIsActive ? 'disabled' : ''}>${quiz != null ? 'Update' : 'Create'}</button>
    <a href="quiz-list">Cancel</a>
</form>

<c:if test="${quiz != null}">
    <hr/>
    <h3>Manage Questions in Quiz</h3>

    <h4>Questions in This Quiz:</h4>
    <table>
        <tr>
            <th>Question ID</th>
            <th>Content</th>
            <th>Points</th>
            <th>Actions</th>
        </tr>
        <c:forEach var="qq" items="${quizQuestions}">
            <tr>
                <td>${qq.questionID}</td>
                <td>
                    <c:forEach var="q" items="${availableQuestions}">
                        <c:if test="${q.questionID == qq.questionID}">${q.content}</c:if>
                    </c:forEach>
                </td>
                <td>${qq.points}</td>
                <td>
                    <c:if test="${!quizIsActive}">
                        <form action="quiz-detail" method="post" style="display:inline;">
                            <input type="hidden" name="manageAction" value="deleteQuestion"/>
                            <input type="hidden" name="quizID" value="${quiz.quizID}"/>
                            <input type="hidden" name="questionID" value="${qq.questionID}"/>
                            <button type="submit" style="margin-top:-3px">Remove</button>
                        </form>
                    </c:if>
                </td>
            </tr>
        </c:forEach>
    </table>

    <c:if test="${!quizIsActive}">
        <h4>Add Question to This Quiz:</h4>
        <form action="quiz-detail" method="post">
            <input type="hidden" name="manageAction" value="addQuestion"/>
            <input type="hidden" name="quizID" value="${quiz.quizID}"/>

            <label>Select Question:</label>
            <select name="questionID" required>
                <c:forEach var="q" items="${availableQuestions}">
                    <option value="${q.questionID}">${q.content}</option>
                </c:forEach>
            </select>

            <label>Points:</label>
            <input type="number" name="points" value="1" min="1" required/>

            <button type="submit">Add Question</button>
        </form>
    </c:if>
</c:if>
    </main>
</body>

<jsp:include page="/chatbot/chatbot-widget.jsp" />
<jsp:include page="/default/footer.jsp" />
</html>
