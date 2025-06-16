<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>All Sliders - Soft Skills</title>

    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" />

    <%-- Liên kết đến các file CSS bên ngoài --%>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/SliderDetail.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/login.css" /> <%-- Sử dụng chung CSS cơ bản của bạn --%>

</head>
<body>
    <div class="wrapper">
        <section class="content">

            <%-- Hiển thị thông báo lỗi từ Servlet (errorMessage) --%>
            <c:if test="${not empty errorMessage}">
                <div class="error-message-display">
                    <strong>Error:</strong> ${errorMessage}
                </div>
            </c:if>

            <%-- Message display (optional) from request parameters --%>
            <c:if test="${not empty param.message}">
                <div class="success-message">${param.message}</div>
            </c:if>
            <c:if test="${not empty param.error}">
                <div class="error-message">${param.error}</div>
            </c:if>

            <div class="slider-carousel-container">
                <c:choose>
                    <c:when test="${not empty sliders}">
                        <c:forEach var="s" items="${sliders}" varStatus="loop">
                            <div class="carousel-slide ${loop.index == 0 ? 'active' : ''}">
                                <img src="${pageContext.request.contextPath}/${s.image}" alt="${s.title}">
                                <div class="carousel-caption">
                                    <p class="welcome-text-carousel">Welcome Soft Skill Learning courses</p>
                                    <h1>${s.title}</h1>
                                    <p class="description-text-carousel">${s.note}</p>
                                    <a href="${s.backlink}" class="discover-more-btn-carousel" target="_blank">DISCOVER MORE</a>

                                </div>
                            </div>
                        </c:forEach>

                        <a class="prev-btn" onclick="plusSlides(-1)"><i class="fas fa-arrow-left"></i></a>
                        <a class="next-btn" onclick="plusSlides(1)"><i class="fas fa-arrow-right"></i></a>

                        <div class="carousel-dots">
                            <c:forEach var="s" items="${sliders}" varStatus="loop">
                                <span class="dot ${loop.index == 0 ? 'active' : ''}" onclick="currentSlide(${loop.index + 1})"></span>
                            </c:forEach>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <%-- Hiển thị thông báo khi không có slider nào, chỉ khi không có lỗi hệ thống --%>
                        <c:if test="${empty errorMessage}">
                            <p style="text-align: center; padding: 50px;">No sliders available to display.</p>
                        </c:if>
                    </c:otherwise>
                </c:choose>
            </div>

        </section>
    </div>

    <script>
        let slideIndex = 1; // slide đang hiển thị
        let autoSlideInterval; // Khai báo biến này ở phạm vi toàn cục

        document.addEventListener('DOMContentLoaded', function() {
            let slides = document.getElementsByClassName("carousel-slide");
            if (slides.length > 0) {
                // Khởi tạo tất cả slide có display: flex nhưng opacity: 0 (CSS sẽ ẩn ban đầu)
                // Javascript chỉ cần đảm bảo chúng có display phù hợp để transition hoạt động
                for (let i = 0; i < slides.length; i++) {
                    slides[i].style.display = "flex"; // Đảm bảo tất cả đều là flex
                }
                showSlides(slideIndex); // Gọi để thiết lập slide đầu tiên
            }
            startAutoSlide(); // Bắt đầu tự động chuyển khi tải trang
        });

        function plusSlides(n) {
            showSlides(slideIndex += n);
        }

        function currentSlide(n) {
            showSlides(slideIndex = n);
        }

        function updateDots() {
            let dots = document.getElementsByClassName("dot");
            for (let i = 0; i < dots.length; i++) {
                // Xóa chữ active của classnaem từng có chấm sáng để nó không sáng nữa
                dots[i].className = dots[i].className.replace(" active", "");
            }
            if (dots.length > 0) { // Đảm bảo có chấm tròn trước khi truy cập
                // thêm active vào đúng chấm trùng với số slide để nó sáng
                 dots[slideIndex - 1].className += " active";
            }
        }

        function showSlides(n) {
            let slides = document.getElementsByClassName("carousel-slide");
            
            if (slides.length === 0) {
                return;
            }

            if (n > slides.length) {slideIndex = 1}
            if (n < 1) {slideIndex = slides.length}
            
            // Ẩn class 'active' khỏi tất cả các slide (để chúng mờ dần)
            for (let i = 0; i < slides.length; i++) {
                slides[i].classList.remove('active');
            }

            // Hiện class 'active' vào slide hiện tại để nó hiển thị (fade-in)
            slides[slideIndex-1].classList.add('active');
            
            // Cập nhật các chấm chỉ báo
            updateDots();
        }

        function startAutoSlide() {
            clearInterval(autoSlideInterval); // Xóa interval cũ trước khi tạo mới
            let slides = document.getElementsByClassName("carousel-slide");
            if (slides.length > 1) {
                autoSlideInterval = setInterval(() => plusSlides(1), 5000); // Chuyển sau 5 giây
            }
        }

        // Dừng tự động chuyển khi hover và tiếp tục khi rời chuột
        const carouselContainer = document.querySelector('.slider-carousel-container');
        if (carouselContainer) {
            carouselContainer.addEventListener('mouseover', () => clearInterval(autoSlideInterval));
            carouselContainer.addEventListener('mouseout', () => startAutoSlide());
        }
    </script>
</body>
</html>