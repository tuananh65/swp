<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.Course" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Courses of Subject</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700;800&display=swap" rel="stylesheet">
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: 'Inter', -apple-system, BlinkMacSystemFont, sans-serif;
            background: #ffffff;
            min-height: 100vh;
            color: #333;
        }

        .container {
            padding: 40px 20px;
            max-width: 1400px;
            margin: 0 auto;
        }

        /* Header Styling */
        .header {
            text-align: center;
            margin-bottom: 60px;
            position: relative;
        }

        .header h2 {
            font-size: clamp(2rem, 5vw, 3.5rem);
            font-weight: 800;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 50%, #f093fb 100%);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            background-clip: text;
            margin-bottom: 15px;
            letter-spacing: -0.02em;
            line-height: 1.2;
        }

        .subtitle {
            color: #666;
            font-size: 1.2rem;
            font-weight: 400;
            margin-top: 10px;
        }

        /* No Courses Message */
        .no-courses {
            text-align: center;
            background: #f8f9fa;
            border: 1px solid #e9ecef;
            padding: 60px 40px;
            border-radius: 24px;
            max-width: 600px;
            margin: 0 auto;
            box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
        }

        .no-courses p {
            font-size: 1.3rem;
            color: #666;
            font-weight: 500;
        }

        /* Course Container */
        .course-container {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(380px, 1fr));
            gap: 40px;
            margin-top: 40px;
        }

        /* Course Card */
        .course-card {
            background: #ffffff;
            border: 1px solid #e9ecef;
            border-radius: 24px;
            overflow: hidden;
            transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
            position: relative;
            transform: translateY(0);
            box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
        }

        .course-card::before {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            right: 0;
            height: 3px;
            background: linear-gradient(90deg, #667eea, #764ba2, #f093fb);
            opacity: 0;
            transition: opacity 0.3s ease;
        }

        .course-card:hover {
            transform: translateY(-12px) scale(1.02);
            box-shadow: 0 20px 40px rgba(0, 0, 0, 0.15);
            border-color: #d6d9dc;
        }

        .course-card:hover::before {
            opacity: 1;
        }

        /* Thumbnail */
        .thumbnail-container {
            position: relative;
            overflow: hidden;
            height: 240px;
        }

        .course-thumbnail {
            width: 100%;
            height: 100%;
            object-fit: cover;
            transition: transform 0.6s cubic-bezier(0.4, 0, 0.2, 1);
        }

        .course-card:hover .course-thumbnail {
            transform: scale(1.1);
        }

        .thumbnail-overlay {
            position: absolute;
            top: 0;
            left: 0;
            right: 0;
            bottom: 0;
            background: linear-gradient(180deg, transparent 0%, rgba(0, 0, 0, 0.3) 100%);
            opacity: 0;
            transition: opacity 0.3s ease;
        }

        .course-card:hover .thumbnail-overlay {
            opacity: 1;
        }

        /* Course Content */
        .course-content {
            padding: 32px;
        }

        .course-title {
            font-size: 1.5rem;
            font-weight: 700;
            color: #2c3e50;
            margin-bottom: 12px;
            line-height: 1.3;
            display: -webkit-box;
            -webkit-line-clamp: 2;
            -webkit-box-orient: vertical;
            overflow: hidden;
            letter-spacing: -0.01em;
        }

        .course-tagline {
            color: #7f8c8d;
            font-size: 1rem;
            font-weight: 400;
            margin-bottom: 20px;
            line-height: 1.5;
            font-style: italic;
        }

        /* Price Section */
        .price-section {
            display: flex;
            align-items: center;
            gap: 16px;
            margin-bottom: 20px;
            flex-wrap: wrap;
        }

        .price-container {
            display: flex;
            align-items: center;
            gap: 12px;
        }

        .original-price {
            text-decoration: line-through;
            color: #bdc3c7;
            font-size: 1.1rem;
            font-weight: 500;
        }

        .sale-price {
            color: #27ae60;
            font-weight: 700;
            font-size: 1.6rem;
        }

        .discount-badge {
            background: linear-gradient(135deg, #e74c3c, #c0392b);
            color: white;
            padding: 6px 12px;
            border-radius: 20px;
            font-size: 0.85rem;
            font-weight: 600;
            box-shadow: 0 4px 12px rgba(231, 76, 60, 0.3);
            animation: pulse 2s infinite;
        }

        @keyframes pulse {
            0%, 100% { transform: scale(1); }
            50% { transform: scale(1.05); }
        }

        /* Brief Info */
        .brief-info {
            color: #5d6d7e;
            line-height: 1.6;
            margin-bottom: 28px;
            font-size: 0.95rem;
            display: -webkit-box;
            -webkit-line-clamp: 3;
            -webkit-box-orient: vertical;
            overflow: hidden;
        }

        /* View Details Button */
        .view-details-btn {
            display: inline-flex;
            align-items: center;
            justify-content: center;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            text-decoration: none;
            padding: 14px 28px;
            border-radius: 50px;
            font-weight: 600;
            font-size: 0.95rem;
            transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
            position: relative;
            overflow: hidden;
            box-shadow: 0 8px 24px rgba(102, 126, 234, 0.3);
            letter-spacing: 0.5px;
        }

        .view-details-btn::before {
            content: '';
            position: absolute;
            top: 0;
            left: -100%;
            width: 100%;
            height: 100%;
            background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.2), transparent);
            transition: left 0.5s;
        }

        .view-details-btn:hover {
            transform: translateY(-2px);
            box-shadow: 0 12px 32px rgba(102, 126, 234, 0.5);
            text-decoration: none;
            color: white;
        }

        .view-details-btn:hover::before {
            left: 100%;
        }

        .view-details-btn:active {
            transform: translateY(0);
        }

        /* Responsive Design */
        @media (max-width: 768px) {
            .container {
                padding: 30px 15px;
            }

            .header {
                margin-bottom: 40px;
            }

            .course-container {
                grid-template-columns: 1fr;
                gap: 30px;
            }

            .course-content {
                padding: 24px;
            }

            .thumbnail-container {
                height: 200px;
            }

            .course-title {
                font-size: 1.3rem;
            }

            .sale-price {
                font-size: 1.4rem;
            }
        }

        @media (max-width: 480px) {
            .course-container {
                grid-template-columns: 1fr;
                gap: 20px;
            }

            .course-content {
                padding: 20px;
            }

            .price-section {
                flex-direction: column;
                align-items: flex-start;
                gap: 12px;
            }
        }

        /* Loading Animation */
        .course-card {
            animation: slideInUp 0.8s cubic-bezier(0.4, 0, 0.2, 1) forwards;
            opacity: 0;
            transform: translateY(60px);
        }

        .course-card:nth-child(1) { animation-delay: 0.1s; }
        .course-card:nth-child(2) { animation-delay: 0.2s; }
        .course-card:nth-child(3) { animation-delay: 0.3s; }
        .course-card:nth-child(4) { animation-delay: 0.4s; }
        .course-card:nth-child(5) { animation-delay: 0.5s; }
        .course-card:nth-child(6) { animation-delay: 0.6s; }

        @keyframes slideInUp {
            to {
                opacity: 1;
                transform: translateY(0);
            }
        }

        /* Scrollbar Styling */
        ::-webkit-scrollbar {
            width: 8px;
        }

        ::-webkit-scrollbar-track {
            background: #f1f1f1;
        }

        ::-webkit-scrollbar-thumb {
            background: linear-gradient(135deg, #667eea, #764ba2);
            border-radius: 4px;
        }

        ::-webkit-scrollbar-thumb:hover {
            background: linear-gradient(135deg, #764ba2, #667eea);
        }
        .back-button {
    display: inline-block;
    padding: 10px 20px;
    background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
    color: white;
    font-weight: 600;
    text-decoration: none;
    border-radius: 8px;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
    transition: all 0.3s ease;
    margin-bottom: 20px;
    font-size: 15px;
}

.back-button:hover {
    background: linear-gradient(135deg, #00c6ff 0%, #0072ff 100%);
    transform: translateY(-2px);
    box-shadow: 0 6px 12px rgba(0, 0, 0, 0.15);
}
.register-btn {
    display: inline-block;
    background: linear-gradient(135deg, #00c6ff, #0072ff);
    color: white;
    padding: 10px 18px;
    border-radius: 8px;
    font-weight: 600;
    text-decoration: none;
    transition: all 0.3s ease;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
    font-size: 15px;
}

.register-btn:hover {
    background: linear-gradient(135deg, #0072ff, #0049b7);
    transform: translateY(-2px);
    box-shadow: 0 6px 12px rgba(0, 0, 0, 0.15);
}



    </style>
</head>
<body>
        <jsp:include page="/default/header.jsp"/>
      <a href="${pageContext.request.contextPath}/admin/subjectList" style="text-decoration: none;">
    <button class="back-button">← Back to Subject List</button>
</a>


    <div class="container">
        <div class="header">
            <h2>Premium Courses Collection</h2>
            <p class="subtitle">Subject ID: ${subjectId}</p>
        </div>
        
        <c:if test="${empty courses}">
            <div class="no-courses">
                <p>✨ No courses found for this subject yet.</p>
            </div>
        </c:if>
        
        <div class="course-container">
            <c:forEach var="course" items="${courses}">
                <div class="course-card">
                    <div class="thumbnail-container">
                        <img class="course-thumbnail" src="${course.courseThumbnail}" alt="${course.courseName}" />
                        <div class="thumbnail-overlay"></div>
                    </div>
                    <div class="course-content">
                        <h3 class="course-title">${course.courseName}</h3>
                        <p class="course-tagline">${course.tagLine}</p>
                        
                        <div class="price-section">
                            <div class="price-container">
                                <span class="original-price">$${course.originalPrice}</span>
                                <span class="sale-price">$${course.salePrice}</span>
                            </div>
                            <c:if test="${course.originalPrice > course.salePrice}">
                                <span class="discount-badge">
                                    ${Math.round((1 - course.salePrice/course.originalPrice) * 100)}% OFF
                                </span>
                            </c:if>
                        </div>
                        
                        <p class="brief-info">${course.briefInfo}</p>
                        
                        <a class="register-btn" href="${pageContext.request.contextPath}/CourseDetailServlet?courseId=${course.courseID}">
  View Course Detail
</a>

                    </div>
                </div>
            </c:forEach>
        </div>
    </div>
        <jsp:include page="/default/footer.jsp"/>
</body>
</html>