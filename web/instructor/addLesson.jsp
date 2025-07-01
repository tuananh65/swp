<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Add Lesson</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/LessonDetail.css" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/froala-editor@latest/css/froala_editor.pkgd.min.css" rel="stylesheet" type="text/css" />
</head>
<body>
<div class="wrapper">
    <jsp:include page="/default/header.jsp"/>

    <section class="hero-banner">
        <div class="hero-content">
            <div class="hero-overlay"></div>
            <img class="hero-image" src="${pageContext.request.contextPath}/images/Banner.jpg" alt="Banner">
            <div class="hero-text">
                <h1>ADD NEW LESSON</h1>
            </div>
        </div>
    </section>

    <section class="content-section">
        <c:if test="${not empty message}">
            <div class="message-banner success-message">${message}</div>
        </c:if>
        <c:if test="${not empty error}">
            <div class="message-banner error-message">${error}</div>
        </c:if>

        <div class="lesson-detail-card">
            <form action="${pageContext.request.contextPath}/addLesson" method="post" class="lesson-form">
                <input type="hidden" name="subjectId" value="${subjectId}">

                <div class="form-group">
                    <label for="title">Title</label>
                    <input type="text" id="title" name="title" placeholder="Lesson Title" required>
                </div>
                <div class="form-group">
                    <label for="videoUrl">Video URL</label>
                    <input type="text" id="videoUrl" name="videoUrl" placeholder="Video URL">
                </div>
                <div class="form-group-inline">
                    <div class="form-group">
                        <label for="order">Order</label>
                        <input type="number" id="order" name="order" placeholder="Order" required min="1">
                    </div>
                    <div class="form-group">
                        <label for="type">Type</label>
                        <select id="type" name="type" required>
                            <option value="">Select Type</option>
                            <option value="Subject topic">Subject topic</option>
                            <option value="Lesson">Lesson</option>
                            <option value="Quiz">Quiz</option>
                            <option value="Assignment">Assignment</option>
                        </select>
                    </div>
                </div>
                <div class="form-group">
                    <label for="status">Status</label>
                    <select id="status" name="status">
                        <option value="Active">Active</option>
                        <option value="Inactive">Inactive</option>
                    </select>
                </div>
                <div class="form-group">
                    <label for="content">Content</label>
                    <textarea id="content" name="content" placeholder="Lesson Content"></textarea>
                </div>
                <div class="form-actions">
                    <button type="submit" class="btn btn-submit">SUBMIT</button>
                    <a href="${pageContext.request.contextPath}/subjectlesson?subjectId=${subjectId}" class="btn btn-cancel">CANCEL</a>
                </div>
            </form>
        </div>
    </section>

    <jsp:include page="/default/footer.jsp"/>
</div>

<script type="text/javascript" src="https://cdn.jsdelivr.net/npm/froala-editor@latest/js/froala_editor.pkgd.min.js"></script>
<script>
    new FroalaEditor('#content', {
        height: 300,
        imageUploadURL: '${pageContext.request.contextPath}/uploadImage',
        imageUploadParams: { id: 'my_editor' },
        imageUploadMethod: 'POST',
        imageMaxSize: 5 * 1024 * 1024,
        imageAllowedTypes: ['jpeg', 'jpg', 'png', 'gif'],

        videoUploadURL: '${pageContext.request.contextPath}/uploadVideo',
        videoUploadParams: { id: 'my_editor' },
        videoUploadMethod: 'POST',
        videoMaxSize: 50 * 1024 * 1024,
        videoAllowedTypes: ['mp4', 'webm', 'ogg']
    });
</script>
</body>
</html>
