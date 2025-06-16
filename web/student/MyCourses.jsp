<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Courses</title>
    <link rel="stylesheet" href="css/MyCourses.css">
</head>
<body>
    <section class="hero-banner">
            <div class="hero-content">
                <div class="hero-overlay"></div>
                <img class="hero-image" src="${pageContext.request.contextPath}/images/Banner.jpg" alt="Banner">
                <div class="hero-text">
                    <h1>MY COURSES</h1> <%-- Đã thay đổi từ "SLIDER LIST" thành "My Courses" --%>
                    <p><a href="${pageContext.request.contextPath}/home">Home</a> / My Courses</p> <%-- Đã thay đổi đường dẫn hiển thị --%>
                </div>
            </div>
    </section>
    <div class="controls">
        <label><input type="checkbox" checked> Thumbnail</label>
        <label><input type="checkbox" checked> Title</label>
        <label><input type="checkbox" checked> Tagline</label>
        <label><input type="checkbox" checked> Enroll Button</label>
        <label>Maximum courses per page: <select><option>9</option></select></label>
    </div>
    <div class="course-grid">
        <div class="course-card">
            <img src="https://via.placeholder.com/300x200" alt="Course Image">
            <div class="course-category">Digital Marketing</div>
            <div class="course-title">IT Statistics Data Science And Business Analysis</div>
            <div class="course-rating">★★★★☆ 4.5k</div>
            <div class="course-details">Lesson 10 • 19h 30m</div>
            <button class="enroll-btn">Enroll →</button>
        </div>
        <div class="course-card">
            <img src="https://via.placeholder.com/300x200" alt="Course Image">
            <div class="course-category">Digital Marketing</div>
            <div class="course-title">Bilginer Adobe Illustrator For Graphic Design</div>
            <div class="course-rating">★★★★☆ 4.5k</div>
            <div class="course-details">Lesson 10 • 19h 30m</div>
            <button class="enroll-btn">Enroll →</button>
        </div>
        <div class="course-card">
            <img src="https://via.placeholder.com/300x200" alt="Course Image">
            <div class="course-category">Digital Marketing</div>
            <div class="course-title">Starting SEO As Your Home Based Business</div>
            <div class="course-rating">★★★★☆ 4.5k</div>
            <div class="course-details">Lesson 10 • 19h 30m</div>
            <button class="enroll-btn">Enroll →</button>
        </div>
        <!-- Thêm các course khác tương tự nếu cần -->
    </div>
    <div class="pagination">
        <button>1</button>
        <button>2</button>
        <button>3</button>
        <button>...</button>
    </div>
    <jsp:include page="/default/footer.jsp"/> 
</body>
</html>