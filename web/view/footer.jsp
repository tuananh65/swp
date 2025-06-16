<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!-- footer.jsp -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/footer.css" />

<footer class="footer">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;600&display=swap" rel="stylesheet">

    <div class="footer-top">
        <div class="container">
            <div class="contact-info" style="margin-right:120px">
                <div class="contact-item">
                    <div class="contact-icon">
                        <i class="fas fa-map-marker-alt"></i>
                    </div>
                    <div class="contact-details">
                        <h4>Address</h4>
                        <p>FPT University</p>
                    </div>
                </div>

                <div class="contact-item">
                    <div class="contact-icon">
                        <i class="fas fa-phone"></i>
                    </div>
                    <div class="contact-details">
                        <h4>Phone</h4>
                        <p>(568) 367-987-237</p>
                    </div>
                </div>

                <div class="contact-item">
                    <div class="contact-icon">
                        <i class="fas fa-envelope"></i>
                    </div>
                    <div class="contact-details">
                        <h4>Email</h4>
                        <p>globalhelcurt14092005@gmail.com</p>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="footer-main">
        <div class="container" style="margin-left:100px">
            <div class="footer-content">
                <div class="footer-section">
                    <div class="footer-logo">
                        <img src="${pageContext.request.contextPath}/images/SoftSkillWhite.png" alt="Logo" />
                    </div>
                    <p class="footer-description">
                        An online learning platform that empowers you to develop essential soft skills through high-quality, expertly crafted courses
                    </p>
                    <div class="social-links">
                        <a href="#" class="social-link" aria-label="Facebook">
                            <i class="fab fa-facebook-f"></i>
                        </a>
                        <a href="#" class="social-link" aria-label="Instagram">
                            <i class="fab fa-instagram"></i>
                        </a>
                        <a href="#" class="social-link" aria-label="Twitter">
                            <i class="fab fa-twitter"></i>
                        </a>
                        <a href="#" class="social-link" aria-label="LinkedIn">
<i class="fab fa-linkedin-in"></i>
                        </a>
                    </div>
                </div>

                <div class="footer-section" style="padding-left: 70px">
                    <h3>Our Services</h3>
                    <ul class="footer-links">
                        <li><a href="#">Consulting</a></li>
                        <li><a href="#">Rating</a></li>
                        <li><a href="#">Workshop</a></li>
                        <li><a href="#">Coaching</a></li>
                        <li><a href="#">Tracking</a></li>
                    </ul>
                </div>

                <div class="footer-section" style="">
                    <h3>Gallery</h3>
                    <div class="gallery-grid">
                        <c:forEach var="img" begin="1" end="6">
                            <div class="gallery-item">
                                <img src="${pageContext.request.contextPath}/images/gallery${img}.jpg" alt="Gallery ${img}" height="80" width="80" />
                            </div>
                        </c:forEach>
                    </div>
                </div>

                <div class="footer-section"  style="padding-left: 70px">
                    <h3>Contact Us</h3>
                    <p>Enter your email</p>
                    <form class="newsletter-form">
                        <div class="input-group">
                            <input type="email" placeholder="Email" required />
                            <button type="submit">
                                <i class="fas fa-paper-plane"></i>
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <div class="footer-bottom">
        <div class="container">
            <p>
                &copy; 2024 <strong style="color: orange;">Soft Skills Learning</strong>.All Right Reserved.
            </p>
        </div>
    </div>
</footer>