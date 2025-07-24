<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>About Us - Soft Skills Learning</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css" />
    </head>
    <body>
        <nav>
            <jsp:include page="/default/header.jsp"/>
        </nav>
        <div class="container content-section">
        <!-- About Section -->
        <div class="section">
            <h2>Leading Soft Skills Learning Platform</h2>
            <p>Soft Skills Learning is a premier training center offering a wide range of professional courses designed to equip individuals with essential interpersonal and communication skills. Our mission is to empower learners with skills that matter in the real world—teamwork, problem-solving, leadership, time management, and emotional intelligence.</p>
            <p>We believe soft skills are just as important as technical knowledge. That's why our platform offers expertly designed content, real-life simulations, and interactive learning experiences that help learners grow personally and professionally.</p>
        </div>
        
        <!-- Course Offerings -->
        <div class="section">
            <h2>Popular Courses We Offer</h2>
            <p>We provide a variety of soft skill courses suitable for students, professionals, and organizations, including:</p>
            <ul>
                <li>Effective Communication</li>
                <li>Public Speaking and Presentation Skills</li>
                <li>Emotional Intelligence</li>
                <li>Time Management and Productivity</li>
                <li>Teamwork and Collaboration</li>
                <li>Conflict Resolution</li>
                <li>Critical Thinking & Problem Solving</li>
                <li>Leadership Development</li>
            </ul>
            <div class="grid">
                <div class="item">
                    <img src="/placeholder.svg?height=200&width=300" alt="Communication Skills">
                    <h3>Master the Art of Communication</h3>
                </div>
                <div class="item">
                    <img src="/placeholder.svg?height=200&width=300" alt="Leadership Skills">
                    <h3>Leadership Excellence</h3>
                </div>
                <div class="item">
                    <img src="/placeholder.svg?height=200&width=300" alt="Teamwork Skills">
                    <h3>Team Collaboration</h3>
                </div>
            </div>
        </div>
        
        <!-- Why Choose Us -->
        <div class="section">
            <h2>Why Learners Trust Us</h2>
            <p>We have successfully trained over 50,000 learners globally and work closely with corporations and universities to integrate soft skills into everyday professional life.</p>
            <p>Our courses are built by certified coaches and experienced facilitators, ensuring both practical and theoretical value. We offer self-paced online learning, live workshops, and blended learning solutions.</p>
        </div>
        
        <!-- Flexible Learning -->
        <div class="section">
            <h2>Flexible Learning That Fits Your Schedule</h2>
            <p>Whether you're a student, a working professional, or a full-time parent, our learning model is built to adapt to your schedule:</p>
            <ul>
                <li><strong>Live sessions:</strong> Join our weekend or evening classes from anywhere.</li>
                <li><strong>Self-paced modules:</strong> Study anytime at your convenience with our online platform.</li>
                <li><strong>Mobile-friendly:</strong> Learn on the go through your tablet or smartphone.</li>
            </ul>
            <p>Need help deciding the right course? Contact our support team for 1-on-1 career guidance and personalized recommendations.</p>
        </div>
        
        <!-- Contact -->
        <div class="section">
            <h2>Get in Touch</h2>
            <p>Have questions about courses, pricing, or certification? Our team is here to help 24/7.</p>
            <p><strong>Email:</strong> support@softskillslearning.com</p>
            <p><strong>Hotline:</strong> 1900 123 456</p>
        </div>
    </div>
    </body>
</html>
<jsp:include page="default/footer.jsp" />
