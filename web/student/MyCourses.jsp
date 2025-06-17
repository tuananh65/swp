<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Courses</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/MyCourses.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
    <%-- Xóa block <style> ở đây, tất cả CSS sẽ nằm trong MyCourses.css --%>
</head>
<body>
    <jsp:include page="/default/header.jsp"/>
    <section class="hero-banner">
        <div class="hero-content">
            <div class="hero-overlay"></div>
            <img class="hero-image" src="${pageContext.request.contextPath}/images/Banner.jpg" alt="Banner">
            <div class="hero-text">
                <h1>MY COURSES</h1>
                <p><a href="${pageContext.request.contextPath}/home">Home</a> / My Courses</p>
            </div>
        </div>
    </section>

    <div class="controls">
        <label>
            <input type="checkbox" id="toggleThumbnail" checked> Thumbnail
        </label>
        <label>
            <input type="checkbox" id="toggleTitle" checked> Title
        </label>
        <label>
            <input type="checkbox" id="toggleTagline" checked> Tagline
        </label>
        <label>
            <input type="checkbox" id="toggleEnrollButton" checked> Enroll Button
        </label>
        <label>
            Maximum courses per page:
            <input type="number" id="coursesPerPageInput" value="9" min="1" max="999">
        </label>
    </div>

    <div class="course-grid" id="courseGrid">
        <c:forEach var="course" items="${courses}">
            <div class="course-card">
                <img class="course-thumbnail" src="${course.courseThumbnail}" alt="Course Image">
                <div class="course-category">${course.briefInfo}</div>
                <div class="course-title">${course.courseName}</div>
                <div class="course-tagline">${course.tagLine}</div>
                <div class="course-rating">
                    <i class="fa-solid fa-star"></i><i class="fa-solid fa-star"></i><i class="fa-solid fa-star"></i><i class="fa-solid fa-star"></i><i class="fa-regular fa-star"></i> 4.5k
                </div>
                <div class="course-details">Lesson 10 • 19h 30m</div>
                <button class="enroll-btn">Enroll <i class="fa-solid fa-arrow-right"></i></button>
            </div>
        </c:forEach>
        <c:if test="${empty courses}">
            <p style="text-align: center; color: #6c757d; padding: 20px;">No courses found.</p>
        </c:if>
    </div>

    <div class="pagination" id="paginationControls">
        <%-- Phần này sẽ được tạo động bằng JavaScript --%>
    </div>

    <jsp:include page="/default/footer.jsp"/>

    <script>
        $(document).ready(function() {
            const courseCards = $('.course-card');
            const coursesPerPageInput = $('#coursesPerPageInput');
            const paginationControls = $('#paginationControls');

            let currentPage = 1;
            let coursesPerPage = parseInt(coursesPerPageInput.val());

            function applyFilters() {
                $('.course-thumbnail').toggle($('#toggleThumbnail').is(':checked'));
                $('.course-title').toggle($('#toggleTitle').is(':checked'));
                $('.course-tagline').toggle($('#toggleTagline').is(':checked'));
                
                // Logic cho course-category vẫn theo tagline
                if ($('#toggleTagline').is(':checked')) {
                    $('.course-category').show();
                } else {
                    $('.course-category').hide();
                }
                $('.enroll-btn').toggle($('#toggleEnrollButton').is(':checked'));

                updatePaginationAndDisplayCourses();
            }

            function updatePaginationAndDisplayCourses() {
                let enteredValue = parseInt(coursesPerPageInput.val());

                if (isNaN(enteredValue) || enteredValue <= 0) {
                    coursesPerPage = 9;
                } else {
                    coursesPerPage = enteredValue;
                }

                const totalPages = Math.ceil(courseCards.length / coursesPerPage);
                currentPage = Math.min(currentPage, totalPages > 0 ? totalPages : 1);
                currentPage = Math.max(1, currentPage);

                courseCards.hide();
                const startIndex = (currentPage - 1) * coursesPerPage;
                const endIndex = startIndex + coursesPerPage;
                courseCards.slice(startIndex, endIndex).show();

                renderPaginationButtons(totalPages);
            }

            function renderPaginationButtons(totalPages) {
                paginationControls.empty();

                if (totalPages > 1) {
                    const prevButton = $('<button>').html('&laquo;').prop('disabled', currentPage === 1);
                    prevButton.on('click', function() {
                        if (currentPage > 1) {
                            currentPage--;
                            updatePaginationAndDisplayCourses();
                        }
                    });
                    paginationControls.append(prevButton);

                    for (let i = 1; i <= totalPages; i++) {
                        const pageButton = $('<button>').text(i).addClass(i === currentPage ? 'active' : '');
                        pageButton.on('click', function() {
                            currentPage = i;
                            updatePaginationAndDisplayCourses();
                        });
                        paginationControls.append(pageButton);
                    }

                    const nextButton = $('<button>').html('&raquo;').prop('disabled', currentPage === totalPages);
                    nextButton.on('click', function() {
                        if (currentPage < totalPages) {
                            currentPage++;
                            updatePaginationAndDisplayCourses();
                        }
                    });
                    paginationControls.append(nextButton);
                }
            }

            $('#toggleThumbnail').on('change', applyFilters);
            $('#toggleTitle').on('change', applyFilters);
            $('#toggleTagline').on('change', applyFilters);
            $('#toggleEnrollButton').on('change', applyFilters);

            coursesPerPageInput.on('input', function() {
                updatePaginationAndDisplayCourses();
            });

            coursesPerPageInput.on('blur', function() {
                let enteredValue = parseInt(coursesPerPageInput.val());
                if (isNaN(enteredValue) || enteredValue <= 0) {
                    coursesPerPageInput.val(9);
                    updatePaginationAndDisplayCourses();
                }
            });

            // Add an initial check for 'No courses found' display
            if (courseCards.length === 0) {
                 $('#courseGrid').html('<p style="text-align: center; color: #6c757d; padding: 20px;">No courses found.</p>');
                 paginationControls.empty(); // No pagination if no courses
            } else {
                applyFilters(); // Initial render if courses exist
            }
        });
    </script>
</body>
</html>