<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Quiz Details</title>
    <style>
        body { font-family: Arial, sans-serif; }
        form { max-width: 600px; margin: auto; }
        label { display: block; margin-top: 10px; }
        input[type="text"], textarea, select, input[type="number"] {
            width: 100%; padding: 8px; margin-top: 5px;
        }
        button { margin-top: 15px; padding: 8px 12px; }
        .error { color: red; text-align: center; }
        h2 { text-align: center; }
    </style>
</head>
<body>
<h2>${quiz != null ? 'Edit Quiz' : 'Create New Quiz'}</h2>

<c:if test="${not empty errorMessage}">
    <p class="error">${errorMessage}</p>
</c:if>

<form action="quiz-detail" method="post">
    <c:if test="${quiz != null}">
        <input type="hidden" name="quizID" value="${quiz.quizID}" />
    </c:if>

    <label>Title:</label>
    <input type="text" name="title" required value="${quiz != null ? quiz.title : ''}" />

    <label>Description:</label>
    <textarea name="description" rows="4">${quiz != null ? quiz.description : ''}</textarea>

    <label>Duration (minutes):</label>
    <input type="number" name="duration" required value="${quiz != null ? quiz.duration : 30}" />

    <label>Difficulty:</label>
    <select name="difficulty">
        <option value="Easy" ${quiz != null && quiz.difficulty == 'Easy' ? 'selected' : ''}>Easy</option>
        <option value="Medium" ${quiz != null && quiz.difficulty == 'Medium' ? 'selected' : ''}>Medium</option>
        <option value="Hard" ${quiz != null && quiz.difficulty == 'Hard' ? 'selected' : ''}>Hard</option>
    </select>

    <label>Course:</label>
    <select name="courseID" required>
        <option value="">-- Select Course --</option>
        <c:forEach var="c" items="${courses}">
            <option value="${c.courseID}"
                    <c:if test="${quiz != null && quiz.courseID == c.courseID}">selected</c:if>
            >
                ${c.courseName}
            </option>
        </c:forEach>
    </select>

    <label>
        <input type="checkbox" name="isActive" ${quiz != null && quiz.active ? 'checked' : ''} />
        Active
    </label>

    <button type="submit">${quiz != null ? 'Update' : 'Create'}</button>
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
                    <form action="quiz-detail" method="post" style="display:inline;">
                        <input type="hidden" name="manageAction" value="deleteQuestion"/>
                        <input type="hidden" name="quizID" value="${quiz.quizID}"/>
                        <input type="hidden" name="questionID" value="${qq.questionID}"/>
                        <button type="submit">Remove</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
    </table>

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


</body>
</html>
