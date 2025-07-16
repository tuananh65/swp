<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Lesson Details</title>
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
                <h1>LESSON DETAILS</h1>
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
            <div class="card-header">
                <h2 class="lesson-title"><c:out value="${lesson.title != null && !lesson.title.isEmpty() ? lesson.title : 'New Lesson'}" /></h2> <%-- Tiêu đề lesson --%>
            </div>
            <%-- ĐÃ THÊM enctype="multipart/form-data" VÀO ĐÂY --%>
            <form action="${pageContext.request.contextPath}/updateLesson" method="post" class="lesson-form" enctype="multipart/form-data">
                <input type="hidden" name="lessonId" value="${lesson.lessonId}">
                <input type="hidden" name="subjectId" value="${lesson.subjectId}">
                
                <div class="form-group">
                    <label for="title">Title</label>
                    <input type="text" id="title" name="title" value="${lesson.title}" placeholder="Lesson Title" required>
                </div>
                <div class="form-group">
                    <label for="videoUrl">Video URL</label>
                    <input type="text" id="videoUrl" name="videoUrl" value="${lesson.videoUrl}" placeholder="Video URL">
                </div>
                <div class="form-group-inline">
                    <div class="form-group">
                        <label for="order">Order</label>
                        <input type="number" id="order" name="order" value="${lesson.order}" placeholder="Order" required min="1">
                    </div>
                    <div class="form-group">
                        <label for="type">Type</label>
                        <select id="type" name="type" required>
                            <option value="">Select Type</option>
                            <option value="Subject topic" ${lesson.type eq 'Subject topic' ? 'selected' : ''}>Subject topic</option>
                            <option value="Lesson" ${lesson.type eq 'Lesson' ? 'selected' : ''}>Lesson</option>
                            <option value="Quiz" ${lesson.type eq 'Quiz' ? 'selected' : ''}>Quiz</option>
                            <option value="Assignment" ${lesson.type eq 'Assignment' ? 'selected' : ''}>Assignment</option>
                        </select>
                    </div>
                </div>
                <div class="form-group">
                    <label for="status">Status</label>
                    <select id="status" name="status">
                        <option value="Active" ${lesson.status eq 'Active' ? 'selected' : ''}>Active</option>
                        <option value="Inactive" ${lesson.status eq 'Inactive' ? 'selected' : ''}>Inactive</option>
                    </select>
                </div>
                <div class="form-group">
                    <label for="content">Content</label>
                    <textarea id="content" name="content" placeholder="Lesson Content">${lesson.content}</textarea>
                </div>
                <div class="form-actions">
                    <button type="submit" class="btn btn-submit">SUBMIT</button>
                    <a href="${pageContext.request.contextPath}/subjectlesson?subjectId=${lesson.subjectId}" class="btn btn-cancel">CANCEL</a>
                    
                    <c:if test="${lesson.lessonId != 0}">
                        <button type="button" class="btn btn-delete" onclick="confirmDelete(${lesson.lessonId}, ${lesson.subjectId})">DELETE</button>
                    </c:if>
                </div>
            </form>
        </div>
    </section>

    <jsp:include page="/default/footer.jsp"/>
</div>

<script type="text/javascript" src="https://cdn.jsdelivr.net/npm/froala-editor@latest/js/froala_editor.pkgd.min.js"></script>
<script>
    // Khởi tạo Froala Editor
    new FroalaEditor('#content', {
        height: 300,
        toolbarButtons: [['bold', 'italic', 'underline', 'strikeThrough', 'subscript', 'superscript'], ['fontFamily', 'fontSize', 'textColor', 'backgroundColor'], ['paragraphFormat', 'align', 'quote', 'formatOL', 'formatUL', 'outdent', 'indent'], ['insertLink', 'insertImage', 'insertVideo', 'insertTable', 'emoticons', 'specialCharacters'], ['undo', 'redo'], ['fullscreen', 'clearFormatting', 'html']],
        
        // Cấu hình upload ảnh
        // URL này sẽ trỏ đến cùng LessonDetailServlet, nhưng với pattern /uploadImage
        imageUploadURL: '${pageContext.request.contextPath}/uploadImage', 
        imageUploadParams: { id: 'my_editor' },
        imageUploadMethod: 'POST',
        imageMaxSize: 5 * 1024 * 1024, // 5MB
        imageAllowedTypes: ['jpeg', 'jpg', 'png', 'gif'],

        // Cấu hình upload video (nếu bạn muốn cho phép upload video lên server)
        // Bạn sẽ cần thêm logic xử lý video tương tự trong LessonDetailServlet
        videoUploadURL: '${pageContext.request.contextPath}/uploadVideo', 
        videoUploadParams: { id: 'my_editor' },
        videoUploadMethod: 'POST',
        videoMaxSize: 50 * 1024 * 1024, // 50MB
        videoAllowedTypes: ['mp4', 'webm', 'ogg']
    });

    function confirmDelete(lessonId, subjectId) {
        if (confirm('Are you sure you want to delete this lesson? This action cannot be undone.')) {
            let form = document.createElement('form');
            form.method = 'POST';
            form.action = '${pageContext.request.contextPath}/subjectlesson'; // Gửi về SubjectLessonServlet để xóa

            let actionInput = document.createElement('input');
            actionInput.type = 'hidden';
            actionInput.name = 'action';
            actionInput.value = 'delete';
            form.appendChild(actionInput);

            let lessonIdInput = document.createElement('input');
            lessonIdInput.type = 'hidden';
            lessonIdInput.name = 'lessonId';
            lessonIdInput.value = lessonId;
            form.appendChild(lessonIdInput);

            let subjectIdInput = document.createElement('input');
            subjectIdInput.type = 'hidden';
            subjectIdInput.name = 'subjectId';
            subjectIdInput.value = subjectId;
            form.appendChild(subjectIdInput);

            document.body.appendChild(form);
            form.submit();
        }
    }
</script>
</body>
</html>