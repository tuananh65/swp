<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!-- Header -->
<jsp:include page="/default/header.jsp">
    <jsp:param name="title" value="Quiz Detail"/>
</jsp:include>

<!-- Banner -->
<jsp:include page="/default/banner.jsp">
    <jsp:param name="bannerTitle" value="Quiz Detail"/>
</jsp:include>

<!-- Sidebar -->
<jsp:include page="/default/sidebar.jsp" />

<body>


    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/quiz-detail.css" />



    <main class="container mt-4">
        <h2>${isCreate ? 'Create New Quiz' : 'Edit Quiz'}</h2>

        <!-- ✅ FORM GET để chọn Course và load bank question -->
        <c:if test="${isCreate}">
            <form method="get" action="${pageContext.request.contextPath}/instructor/quiz" class="mb-4">
                <input type="hidden" name="title" value="${quiz.title}" />
                <input type="hidden" name="description" value="${quiz.description}" />
                <input type="hidden" name="difficulty" value="${quiz.difficulty}" />
                <input type="hidden" name="duration" value="${quiz.duration}" />
                <input type="hidden" name="active" value="${quiz.active}" />

                <label for="courseID" class="form-label">Select Course</label>
                <select name="courseID" id="courseID" class="form-control" required onchange="this.form.submit();">
                    <option value="">-- Choose Course --</option>
                    <c:forEach var="course" items="${courses}">
                        <option value="${course.courseID}" ${quiz.courseID == course.courseID ? 'selected' : ''}>
                            ${course.courseName}
                        </option>
                    </c:forEach>
                </select>
            </form>
        </c:if>

        <!-- ✅ FORM POST để SAVE quiz -->
        <!-- ✅ FORM POST để SAVE quiz -->
<form method="post" action="${pageContext.request.contextPath}/instructor/quiz" class="mt-4">

    <!-- ✅ Hidden fields -->
    <c:if test="${not isCreate}">
        <input type="hidden" name="id" value="${quiz.testID}" />
    </c:if>
    <input type="hidden" name="courseID" value="${quiz.courseID}" />

    <!-- ✅ Quiz Info Section -->
    <div class="card mb-4 shadow-sm">
        <div class="card-body">
            <h4 class="card-title mb-3">Quiz Information</h4>
            <div class="row">
                <div class="col-md-6 mb-3">
                    <label class="form-label">Quiz Title</label>
                    <input type="text" name="title" value="${quiz.title}" class="form-control" required />
                </div>
                <div class="col-md-3 mb-3">
                    <label class="form-label">Difficulty</label>
                    <select name="difficulty" class="form-control">
                        <option ${quiz.difficulty == 'Easy' ? 'selected' : ''}>Easy</option>
                        <option ${quiz.difficulty == 'Medium' ? 'selected' : ''}>Medium</option>
                        <option ${quiz.difficulty == 'Hard' ? 'selected' : ''}>Hard</option>
                    </select>
                </div>
                <div class="col-md-3 mb-3">
                    <label class="form-label">Duration (minutes)</label>
                    <input type="number" name="duration" value="${quiz.duration}" class="form-control" required />
                </div>
            </div>

            <div class="mb-3">
                <label class="form-label">Description</label>
                <textarea name="description" class="form-control">${quiz.description}</textarea>
            </div>

            <div class="form-check mb-3">
                <input type="checkbox" name="active" class="form-check-input" ${quiz.active ? 'checked' : ''} />
                <label class="form-check-label">Active</label>
            </div>
        </div>
    </div>

    <!-- ✅ Assigned Questions (Editable) -->
<c:if test="${not empty quizQuestions}">
    <div class="card mb-4 shadow-sm">
        <div class="card-body">
            <h4 class="card-title mb-3">Assigned Questions</h4>
            <ul class="list-group">
                <c:forEach var="q" items="${quizQuestions}">
                    <li class="list-group-item">
                        <input type="hidden" name="editQuestionID[]" value="${q.questionID}" />

                        <div class="mb-2">
                            <label class="form-label">Question Content</label>
                            <p class="form-control-plaintext">${q.content}</p>  <!-- Show content without editing -->
                        </div>

                        <div class="mb-2">
                            <label class="form-label">Points</label>
                            <p class="form-control-plaintext">${q.points}</p>  <!-- Show points without editing -->
                        </div>

                        <a href="${pageContext.request.contextPath}/instructor/quiz?id=${quiz.testID}&removeQuestion=${q.questionID}"
                           class="btn btn-danger btn-sm mt-2">
                           Remove
                        </a>
                    </li>
                </c:forEach>
            </ul>
        </div>
    </div>
</c:if>





    <!-- ✅ Bank Questions to Add -->
    <div class="card mb-4 shadow-sm">
        <div class="card-body">
            <h4 class="card-title mb-3">Add Questions from Bank</h4>
            <c:if test="${empty bankQuestions}">
                <div class="alert alert-warning">No available questions in bank.</div>
            </c:if>
            <c:if test="${not empty bankQuestions}">
                <input type="text" id="searchQuestions" class="form-control mb-3" placeholder="Search questions" />

                <c:forEach var="q" items="${bankQuestions}">
                    <div class="form-check mb-2 d-flex align-items-center">
                        <input class="form-check-input me-2" type="checkbox"
                               name="selectedQuestions[]"
                               value="${q.questionID}" id="q_${q.questionID}">

                        <label class="form-check-label me-3 flex-grow-1" for="q_${q.questionID}">
                            ${q.content}
                        </label>

                        <input type="number"
                               class="form-control form-control-sm"
                               style="width:80px;"
                               min="1"
                               max="100"
                               value="5"
                               name="points_${q.questionID}" />
                    </div>
                </c:forEach>
            </c:if>
        </div>
    </div>

    <!-- ✅ Buttons -->
    <div class="d-flex justify-content-center">
        <button type="submit" class="btn btn-success me-2">
            ${isCreate ? 'Create' : 'Save Changes'}
        </button>
        <a href="${pageContext.request.contextPath}/instructor/quiz-list" class="btn btn-secondary">Cancel</a>
    </div>
</form>


    </main>

    <!-- JavaScript to bind checkbox value with points -->
    <script>
        document.getElementById("searchQuestions").addEventListener("input", (e) => {
            const searchText = e.target.value.toLowerCase();
            document.querySelectorAll('.form-check').forEach(item => {
                const content = item.querySelector('label').textContent.toLowerCase();
                item.style.display = content.includes(searchText) ? 'block' : 'none';
            });
        });

        document.querySelectorAll('input[type=checkbox][name=selectedQuestions[]]').forEach(cb => {
            const qid = cb.getAttribute('id').split('_')[1];
            const pointsInput = document.getElementById('points_' + qid);

            const updateValue = () => {
                cb.value = qid + "_" + pointsInput.value;
            };

            cb.addEventListener('change', updateValue);
            pointsInput.addEventListener('input', updateValue);
        });
    </script>

    <jsp:include page="/chatbot/chatbot-widget.jsp" />
    <jsp:include page="/default/footer.jsp" />
</body>
</html>
