<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Contact Us - Soft Skills Learning</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css" />
    </head>
    <body>
        <nav>
            <jsp:include page="/default/header.jsp"/>
        </nav>
        <div class="container content-section">
        <!-- Contact Section -->
        <div class="section">
            <h2>Contact Soft Skills Learning</h2>
            <p>We're here to support your learning journey. Reach out to us through any of the channels below.</p>
        </div>
        <div class="grid">
            <div class="item contact-info">
                <h3>Contact Information</h3>
                <div>
                    <div><strong>Address:</strong> 123 Skill Street, District 1, Ho Chi Minh City</div>
                    <div><strong>Hotline:</strong> 0909 123 456</div>
                    <div><strong>Email:</strong> support@softskillslearning.com</div>
                    <div><strong>Working Hours:</strong> 8:00 AM - 5:00 PM (Mon - Sat)</div>
                </div>
                <iframe src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d3919.669676!2d106.698!3d10.7769!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x0%3A0x0!2zMTDCsDQ2JzM2LjgiTiAxMDbCsDQxJzUyLjgiRQ!5e0!3m2!1sen!2s!4v1634567890123" width="100%" height="400" allowfullscreen="" loading="lazy"></iframe>
            </div>
        </div>
        
        <!-- Branches -->
        <div class="section">
            <h2>Our Locations</h2>
            <div class="grid">
                <div class="item contact-info">
                    <h4>Hanoi Branch</h4>
                    <p>456 Education Avenue, Hai Ba Trung, Hanoi<br>Hotline: 0988 456 789</p>
                </div>
                <div class="item contact-info">
                    <h4>Da Nang Branch</h4>
                    <p>789 Success Road, Hai Chau, Da Nang<br>Hotline: 0911 789 123</p>
                </div>
            </div>
        </div>
    </div>
    </body>
</html>
 <jsp:include page="default/footer.jsp" />
