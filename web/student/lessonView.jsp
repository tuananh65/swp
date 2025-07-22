<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Lesson Details</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/LessonDetail.css">
</head>
<body>
    <nav>
        <jsp:include page="/default/header.jsp"/>
    </nav>
    <section class="hero-banner">
        <div class="hero-content">
            <div class="hero-overlay"></div>
            <img class="hero-image" src="${pageContext.request.contextPath}/images/Banner.jpg" alt="Banner">
            <div class="hero-text">
                <h1>View Lesson</h1>
            </div>
        </div>
    </section>

    <div class="lesson-container">
        <h2>Lesson for Course ID: ${courseId}</h2>
        <c:choose>
            <c:when test="${not empty lesson}">
                <div class="lesson-card">
                    <h3>Lesson ID: ${lesson.lessonId}</h3>
                    <p><strong>Title:</strong> ${lesson.title}</p>
                    <p><strong>Content:</strong> ${lesson.content}</p>
                    <c:if test="${not empty lesson.videoUrl}">
                        <p><strong>Video:</strong></p>
                        <video controls>
                            <source src="${lesson.videoUrl}" type="video/mp4">
                            Your browser does not support the video tag.
                        </video>
                    </c:if>
                    <c:if test="${empty lesson.videoUrl}">
                        <p><strong>Video:</strong> No video available</p>
                    </c:if>
                </div>
            </c:when>
            <c:otherwise>
                <p class="no-lesson">${error != null ? error : "No lesson found for this course."}</p>
            </c:otherwise>
        </c:choose>
    </div>

    <jsp:include page="/default/footer.jsp"/>
</body>
</html>