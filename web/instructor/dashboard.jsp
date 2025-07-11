<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="model.User, java.util.List"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Admin Dashboard</title>
        <link href="${pageContext.request.contextPath}/css/blog.css" rel="stylesheet">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dashboard.css" /> <%-- Đảm bảo có file CSS này --%>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
        <script src="https://cdn.jsdelivr.net/npm/chart.js"></script> <%-- Thêm thư viện Chart.js --%>
        <%-- Nếu bạn dùng Bootstrap, hãy bỏ comment dòng dưới --%>
        <%-- <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"> --%>
        <style>
            /* Thêm CSS tùy chỉnh cho Dashboard nếu cần, hoặc đặt vào dashboard.css */
            .dashboard-main-content {
                padding: 20px;
                max-width: 1200px;
                margin: 0 auto;
            }
            .date-filter-container {
                margin-bottom: 30px;
                text-align: center;
                background-color: #f8f9fa;
                padding: 15px;
                border-radius: 8px;
                box-shadow: 0 2px 4px rgba(0,0,0,0.1);
            }
            .date-filter-container label {
                margin-right: 10px;
                font-weight: bold;
                color: #555;
            }
            .date-filter-container input[type="date"] {
                padding: 8px 12px;
                border: 1px solid #ccc;
                border-radius: 4px;
                margin-right: 15px;
            }
            .date-filter-container .filter-btn {
                padding: 8px 15px;
                background-color: #007bff;
                color: white;
                border: none;
                border-radius: 4px;
                cursor: pointer;
                transition: background-color 0.3s ease;
            }
            .date-filter-container .filter-btn:hover {
                background-color: #0056b3;
            }

            .dashboard-cards-grid {
                display: grid;
                grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
                gap: 20px;
                margin-bottom: 30px;
            }
            .dashboard-cards-grid .card {
                background-color: #fff;
                border-radius: 8px;
                box-shadow: 0 4px 8px rgba(0,0,0,0.1);
                padding: 25px;
                text-align: center;
                transition: transform 0.2s ease-in-out;
            }
            .dashboard-cards-grid .card:hover {
                transform: translateY(-5px);
            }
            .dashboard-cards-grid .card h3 {
                color: #3f51b5;
                margin-bottom: 15px;
                font-size: 1.4em;
            }
            .dashboard-cards-grid .card .stat-value {
                font-size: 2.5em;
                font-weight: bold;
                color: #007bff;
                margin-bottom: 10px;
            }
            .dashboard-cards-grid .card .stat-detail {
                font-size: 0.9em;
                color: #666;
                margin-bottom: 5px;
            }
            .stat-success { color: #28a745; font-weight: bold; }
            .stat-cancelled { color: #dc3545; font-weight: bold; }
            .stat-submitted { color: #ffc107; font-weight: bold; }

            .dashboard-charts-section {
                display: flex;
                flex-wrap: wrap;
                gap: 20px;
            }
            .chart-container {
                flex: 1; /* Cho phép các biểu đồ co giãn */
                min-width: 45%; /* Đảm bảo ít nhất 2 cột trên màn hình lớn */
                background-color: #fff;
                border-radius: 8px;
                box-shadow: 0 4px 8px rgba(0,0,0,0.1);
                padding: 25px;
            }
            @media (max-width: 768px) {
                .chart-container {
                    min-width: 100%; /* Trên màn hình nhỏ, mỗi biểu đồ chiếm 100% chiều rộng */
                }
            }

            .chart-container h3 {
                color: #3f51b5;
                margin-bottom: 20px;
                text-align: center;
                font-size: 1.3em;
            }
            .category-revenue-list {
                list-style: none;
                padding: 0;
            }
            .category-revenue-list li {
                display: flex;
                justify-content: space-between;
                padding: 8px 0;
                border-bottom: 1px dashed #eee;
                font-size: 1.05em;
            }
            .category-revenue-list li:last-child {
                border-bottom: none;
            }
            .category-revenue-list li span {
                color: #333;
            }
            .category-revenue-list li strong {
                color: #007bff;
            }

            /* CSS cho banner (từ code bạn cung cấp) */
            .hero-banner {
                position: relative;
                width: 100%;
                height: 300px; /* Chiều cao cố định cho banner */
                overflow: hidden;
            }

            .hero-content {
                position: relative;
                width: 100%;
                height: 100%;
            }

            .hero-image {
                width: 100%;
                height: 100%;
                object-fit: cover; /* Đảm bảo hình ảnh che phủ toàn bộ khu vực */
                position: absolute;
                top: 0;
                left: 0;
            }

            .hero-overlay {
                position: absolute;
                top: 0;
                left: 0;
                width: 100%;
                height: 100%;
                background-color: rgba(0, 0, 0, 0.5); /* Màu đen trong suốt */
                z-index: 1; /* Đảm bảo lớp phủ nằm trên ảnh */
            }

            .hero-text {
                position: absolute;
                top: 50%;
                left: 50%;
                transform: translate(-50%, -50%);
                color: white;
                text-align: center;
                z-index: 2; /* Đảm bảo văn bản nằm trên lớp phủ */
            }

            .hero-text h1 {
                font-size: 3em;
                margin-bottom: 10px;
                text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.7);
            }

            .hero-text p {
                font-size: 1.2em;
            }

            .hero-text p a {
                color: #007bff; /* Màu xanh nổi bật cho link */
                text-decoration: none;
            }
            .hero-text p a:hover {
                text-decoration: underline;
            }
        </style>
    </head>
    <body>
        <nav>
            <%-- Đảm bảo đường dẫn đến header.jsp là chính xác --%>
            <jsp:include page="/default/header.jsp"/>
        </nav>

        <section class="hero-banner dashboard-banner">
            <div class="hero-content">
                <div class="hero-overlay"></div>
                <img class="hero-image" src="${pageContext.request.contextPath}/images/Banner.jpg" alt="Banner Dashboard">
                <div class="hero-text">
                    <h1>ADMIN DASHBOARD</h1>
                    <p><a href="${pageContext.request.contextPath}/home">Home</a> / Admin Dashboard</p>
                </div>
            </div>
        </section>

        <main class="dashboard-main-content">
            <h2 style="text-align: center; margin-bottom: 30px; color: #3f51b5;">Dashboard Overview</h2>

            <%-- Bộ lọc ngày tháng --%>
            <div class="date-filter-container">
                <form action="${pageContext.request.contextPath}/admin/dashboard" method="GET">
                    <label for="startDate">Start Date:</label>
                    <input type="date" id="startDate" name="startDate" value="${currentStartDate}">
                    <label for="endDate">End Date:</label>
                    <input type="date" id="endDate" name="endDate" value="${currentEndDate}">
                    <button type="submit" class="filter-btn">Apply Filter</button>
                </form>
            </div>

            <%-- Các card thống kê --%>
            <div class="dashboard-cards-grid">
                <%-- Thống kê Subject --%>
                <div class="card">
                    <h3>Subject Statistics</h3>
                    <p class="stat-value"><c:out value="${subjectStats.newSubjects}" default="0"/></p>
                    <p class="stat-detail">New Subjects (within selected period)</p>
                    <p class="stat-detail">Total Subjects: <c:out value="${subjectStats.allSubjects}" default="0"/></p>
                </div>

                <%-- Thống kê Registrations --%>
                <div class="card">
                    <h3>Enrollment Registrations</h3>
                    <p class="stat-value"><c:out value="${enrollmentStats.allRegistrations}" default="0"/></p>
                    <p class="stat-detail">All Registrations (within selected period)</p>
                    <p class="stat-detail">Successful: <span class="stat-success"><c:out value="${enrollmentStats.successfulRegistrations}" default="0"/></span></p>
                    <p class="stat-detail">Cancelled: <span class="stat-cancelled"><c:out value="${enrollmentStats.cancelledRegistrations}" default="0"/></span></p>
                    <p class="stat-detail">Submitted: <span class="stat-submitted"><c:out value="${enrollmentStats.submittedRegistrations}" default="0"/></span></p>
                </div>

                <%-- Thống kê tổng doanh thu --%>
                <div class="card">
                    <h3>Total Revenue</h3>
                    <p class="stat-value">
                        <fmt:formatNumber value="${totalRevenue}" type="currency" currencySymbol="₫" maxFractionDigits="0"/>
                    </p>
                    <p class="stat-detail">Based on successful enrollments within selected period.</p>
                </div>

                <%-- Thống kê khách hàng --%>
                <div class="card">
                    <h3>Customer Statistics</h3>
                    <p class="stat-value"><c:out value="${customerStats.newlyRegisteredCustomers}" default="0"/></p>
                    <p class="stat-detail">Newly Registered Customers (within selected period)</p>
                    <p class="stat-detail">Newly Bought Customers: <c:out value="${customerStats.newlyBoughtCustomers}" default="0"/></p>
                </div>
            </div>

            <%-- Phần biểu đồ và thống kê chi tiết --%>
            <div class="dashboard-charts-section">
                <%-- Doanh thu theo danh mục môn học --%>
                <div class="chart-container card">
                    <h3>Revenue by Subject Categories (Top 5)</h3>
                    <ul class="category-revenue-list">
                        <c:forEach var="item" items="${revenueByCategories}" varStatus="loop">
                            <%-- Chỉ hiển thị top 5 hoặc tất cả nếu ít hơn 5 --%>
                            <c:if test="${loop.index < 5}">
                                <li>
                                    <span><c:out value="${item.categoryName}"/>:</span>
                                    <strong><fmt:formatNumber value="${item.totalRevenue}" type="currency" currencySymbol="₫" maxFractionDigits="0"/></strong>
                                </li>
                            </c:if>
                        </c:forEach>
                        <c:if test="${empty revenueByCategories}">
                            <li>No revenue data for this period.</li>
                        </c:if>
                    </ul>
                </div>

                <%-- Biểu đồ xu hướng đơn hàng --%>
                <div class="chart-container card">
                    <h3>Order Counts Trend (within selected period)</h3>
                    <canvas id="orderTrendChart"></canvas>
                </div>
            </div>
        </main>

        <%-- Đảm bảo đường dẫn đến footer.jsp là chính xác --%>
        <jsp:include page="/default/footer.jsp"/>
        <%-- Nếu bạn dùng Bootstrap, hãy bỏ comment dòng dưới --%>
        <%-- <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script> --%>

        <script>
            // Đảm bảo lớp phủ banner trong suốt nếu bạn muốn banner chỉ là ảnh nền
            document.addEventListener('DOMContentLoaded', function() {
                const heroOverlay = document.querySelector('.hero-banner .hero-overlay');
                if (heroOverlay) {
                    heroOverlay.style.backgroundColor = 'rgba(0, 0, 0, 0.3)'; // Hoặc 'transparent'
                }

                // Xử lý dữ liệu và vẽ biểu đồ Chart.js
                const orderTrendJson = '${orderTrendJson}';
                let orderTrendData = [];
                try {
                    orderTrendData = JSON.parse(orderTrendJson || '[]'); // Parse JSON, nếu rỗng thì dùng mảng rỗng
                } catch (e) {
                    console.error("Error parsing orderTrendJson:", e);
                    orderTrendData = [];
                }

                const dates = [];
                const successfulCounts = [];
                const allCounts = [];

                orderTrendData.forEach(function(trend) {
                    // Định dạng ngày (trend.date là java.sql.Date, Gson sẽ biến nó thành chuỗi ISO)
                    const dateObj = new Date(trend.date);
                    // Ví dụ: format thành "Jul 11"
                    const options = { month: 'short', day: 'numeric' };
                    dates.push(dateObj.toLocaleDateString('en-US', options));
                    successfulCounts.push(trend.successfulOrders);
                    allCounts.push(trend.allOrders);
                });

                const ctx = document.getElementById('orderTrendChart');
                if (ctx) { // Chỉ khởi tạo biểu đồ nếu phần tử canvas tồn tại
                    new Chart(ctx.getContext('2d'), {
                        type: 'line',
                        data: {
                            labels: dates,
                            datasets: [{
                                label: 'Successful Orders',
                                data: successfulCounts,
                                borderColor: 'rgb(75, 192, 192)',
                                backgroundColor: 'rgba(75, 192, 192, 0.2)',
                                tension: 0.3, // Làm đường cong mượt mà hơn
                                fill: false // Không tô màu dưới đường
                            }, {
                                label: 'All Orders',
                                data: allCounts,
                                borderColor: 'rgb(255, 99, 132)',
                                backgroundColor: 'rgba(255, 99, 132, 0.2)',
                                tension: 0.3,
                                fill: false
                            }]
                        },
                        options: {
                            responsive: true,
                            maintainAspectRatio: false, // Quan trọng để biểu đồ co giãn trong container
                            scales: {
                                y: {
                                    beginAtZero: true,
                                    title: {
                                        display: true,
                                        text: 'Number of Orders'
                                    }
                                }
                            },
                            plugins: {
                                title: {
                                    display: true,
                                    text: 'Daily Order Trend'
                                }
                            }
                        }
                    });
                } else {
                    console.warn("Canvas element 'orderTrendChart' not found. Chart will not be rendered.");
                }
            });
        </script>
    </body>
</html>