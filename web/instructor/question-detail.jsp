<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Question Detail</title>
        <style>
            .answer-row {
                margin-bottom: 5px;
            }
        </style>
    </head>
    <body>
        <h2>${question != null ? 'Edit Question' : 'Create New Question'}</h2>

        <c:if test="${not empty errorMessage}">
            <p style="color:red;">${errorMessage}</p>
        </c:if>

        <form action="question-detail" method="post">
            <input type="hidden" name="action" value="save">
            <c:if test="${question != null}">
                <input type="hidden" name="questionID" value="${question.questionID}">
            </c:if>

            <label>Course:</label>
            <select name="courseID" required>
                <option value="">--Select Course--</option>
                <c:forEach var="c" items="${courseList}">
                    <option value="${c.courseID}"
                            <c:if test="${question != null && question.courseID == c.courseID}">selected</c:if>>
                        ${c.courseName}
                    </option>
                </c:forEach>
            </select>
            <br><br>


            <label>Content:</label><br>
            <textarea name="content" rows="4" cols="50" required>${question != null ? question.content : ''}</textarea><br><br>

            <label>Points:</label>
            <input type="number" name="points" required
                   value="${question != null ? question.points : '0'}"><br><br>

            <label>Question Type:</label>
            <select name="questionType" id="questionType" onchange="toggleAnswers()" required>
                <option value="">--Select--</option>
                <option value="Short Answer"
                        <c:if test="${question != null && question.questionType == 'Short Answer'}">selected</c:if>>
                            Short Answer
                        </option>
                        <option value="Multiple Choice"
                        <c:if test="${question != null && question.questionType == 'Multiple Choice'}">selected</c:if>>
                            Multiple Choice
                        </option>
                </select><br><br>

                <!-- Short Answer Section -->
                <div id="shortAnswerSection" style="display:none;">
                    <label>Answer:</label><br>
                    <input type="text" name="shortAnswerContent"
                           value="<c:forEach var='a' items='${question.answers}'>
                        <c:if test='${a.correct}'>${a.content}</c:if>
                    </c:forEach>"><br><br>
            </div>

            <!-- Multiple Choice Section -->
            <div id="multipleChoiceSection" style="display:none;">
                <h4>Answers for Multiple Choice</h4>
                <div id="answersContainer">
                    <c:choose>
                        <c:when test="${not empty question && question.questionType == 'Multiple Choice'}">
                            <c:forEach var="a" items="${question.answers}" varStatus="status">
                                <div class="answer-row">
                                    <input type="text" name="answerContent" value="${a.content}" required>
                                    <label>
                                        <input type="checkbox" name="isCorrect" value="${status.index}"
                                               <c:if test="${a.correct}">checked</c:if>> Correct
                                        </label>
                                    </div>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <!-- Default 3 empty inputs for new -->
                            <div class="answer-row">
                                <input type="text" name="answerContent" required>
                                <label><input type="checkbox" name="isCorrect" value="0"> Correct</label>
                            </div>
                            <div class="answer-row">
                                <input type="text" name="answerContent" required>
                                <label><input type="checkbox" name="isCorrect" value="1"> Correct</label>
                            </div>
                            <div class="answer-row">
                                <input type="text" name="answerContent" required>
                                <label><input type="checkbox" name="isCorrect" value="2"> Correct</label>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>

            <button type="submit">Save</button>
            <a href="question-list">Cancel</a>
        </form>

        <script>
            function toggleAnswers() {
                const type = document.getElementById('questionType').value;
                document.getElementById('multipleChoiceSection').style.display = (type === 'Multiple Choice') ? 'block' : 'none';
                document.getElementById('shortAnswerSection').style.display = (type === 'Short Answer') ? 'block' : 'none';
            }

            // Auto toggle on page load (for edit)
            window.onload = toggleAnswers;
        </script>
    </body>
</html>
