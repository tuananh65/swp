<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List, java.util.Map, java.sql.Date, java.math.BigDecimal"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Admin Dashboard</title>
        <%-- Đảm bảo đường dẫn CSS của bạn đúng, blog.css có thể chứa các style chung khác --%>
        <link href="${pageContext.request.contextPath}/css/blog.css" rel="stylesheet">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dashboard.css" />
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
        <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
        <%-- Nếu bạn dùng Bootstrap, hãy bỏ comment dòng dưới --%>
        <%-- <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"> --%>
    </head>
    <body>
        <nav>
            <jsp:include page="/default/header.jsp"/>
        </nav>

        <section class="hero-banner dashboard-banner">
            <div class="hero-image-wrapper">
                <img class="hero-image" src="${pageContext.request.contextPath}/images/Banner.jpg" alt="Banner Dashboard">
                <div class="hero-overlay"></div> <%-- Lớp phủ đã bị ẩn hoàn toàn bằng CSS --%>
            </div>
            <div class="hero-text">
                <h1>ADMIN DASHBOARD</h1>
                <p><a href="${pageContext.request.contextPath}/home">Home</a> / Admin Dashboard</p>
            </div>
        </section>

        <main class="dashboard-main-content">
            <h2 class="section-title">Dashboard Overview</h2>

            <%-- Bộ lọc ngày tháng --%>
            <div class="date-filter-container card">
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
                <div class="card stat-card">
                    <div class="card-icon"><i class="fas fa-book-open"></i></div>
                    <h3>Subject Statistics</h3>
                    <p class="stat-value"><c:out value="${newSubjects}" default="0"/></p>
                    <p class="stat-detail">New Subjects <small>(selected period)</small></p>
                    <p class="stat-total">Total: <c:out value="${allSubjects}" default="0"/></p>
                </div>

                <%-- Thống kê Registrations --%>
                <div class="card stat-card">
                    <div class="card-icon"><i class="fas fa-user-graduate"></i></div>
                    <h3>Enrollment Registrations</h3>
                    <p class="stat-value"><c:out value="${allRegistrations}" default="0"/></p>
                    <p class="stat-detail">All Registrations <small>(selected period)</small></p>
                    <div class="stat-breakdown">
                        <span>Successful: <span class="stat-success"><c:out value="${successfulRegistrations}" default="0"/></span></span>
                        <span>Cancelled: <span class="stat-cancelled"><c:out value="${cancelledRegistrations}" default="0"/></span></span>
                        <span>Submitted: <span class="stat-submitted"><c:out value="${submittedRegistrations}" default="0"/></span></span>
                    </div>
                </div>

                <%-- Thống kê tổng doanh thu --%>
                <div class="card stat-card">
                    <div class="card-icon"><i class="fas fa-dollar-sign"></i></div>
                    <h3>Total Revenue</h3>
                    <p class="stat-value">
                        <fmt:formatNumber value="${totalRevenue}" type="currency" currencySymbol="₫" maxFractionDigits="0"/>
                    </p>
                    <p class="stat-detail">Based on successful enrollments <small>(selected period)</small></p>
                </div>

                <%-- Thống kê khách hàng --%>
                <div class="card stat-card">
                    <div class="card-icon"><i class="fas fa-users"></i></div>
                    <h3>Customer Statistics</h3>
                    <p class="stat-value"><c:out value="${newlyRegisteredCustomers}" default="0"/></p>
                    <p class="stat-detail">Newly Registered <small>(selected period)</small></p>
                    <%-- Chỉ hiển thị Newly Bought nếu có giá trị > 0 --%>
                    <c:if test="${newlyBoughtCustomers > 0}">
                        <p class="stat-total">Newly Bought: <c:out value="${newlyBoughtCustomers}" default="0"/></p>
                    </c:if>
                </div>
            </div>

            <%-- Phần biểu đồ và thống kê chi tiết --%>
            <div class="dashboard-charts-section">
                <%-- Doanh thu theo danh mục môn học (Biểu đồ tròn) --%>
                <div class="chart-container card">
                    <h3>Revenue by Subject Categories (Top 5)</h3>
                    <c:choose>
                        <c:when test="${not empty revenueByCategoriesJson && revenueByCategoriesJson ne '[]'}">
                            <canvas id="revenueByCategoryChart"></canvas>
                        </c:when>
                        <c:otherwise>
                            <p class="no-data-message">No revenue data for this period.</p>
                        </c:otherwise>
                    </c:choose>
                </div>

                <%-- Biểu đồ xu hướng đơn hàng --%>
                <div class="chart-container card">
                    <h3>Order Counts Trend (within selected period)</h3>
                    <c:choose>
                        <c:when test="${not empty orderTrendJson && orderTrendJson ne '[]'}">
                            <canvas id="orderTrendChart"></canvas>
                        </c:when>
                        <c:otherwise>
                            <p class="no-data-message">No order trend data for this period.</p>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </main>

        <jsp:include page="/default/footer.jsp"/>

        <script>
            document.addEventListener('DOMContentLoaded', function() {
                // Biểu đồ xu hướng đơn hàng
                const orderTrendJson = '${orderTrendJson}';
                let orderTrendData = [];
                try {
                    orderTrendData = JSON.parse(orderTrendJson || '[]');
                } catch (e) {
                    console.error("Error parsing orderTrendJson:", e);
                    orderTrendData = [];
                }

                if (orderTrendData.length > 0) {
                    const dates = [];
                    const successfulCounts = [];
                    const allCounts = [];

                    orderTrendData.forEach(function(trend) {
                        const dateObj = new Date(trend.date);
                        const options = { month: 'short', day: 'numeric' };
                        dates.push(dateObj.toLocaleDateString('en-US', options));
                        successfulCounts.push(trend.successfulOrders);
                        allCounts.push(trend.allOrders);
                    });

                    const orderCtx = document.getElementById('orderTrendChart');
                    if (orderCtx) {
                        new Chart(orderCtx.getContext('2d'), {
                            type: 'line',
                            data: {
                                labels: dates,
                                datasets: [{
                                    label: 'Successful Orders',
                                    data: successfulCounts,
                                    borderColor: '#4CAF50', // Màu xanh lá cây
                                    backgroundColor: 'rgba(76, 175, 80, 0.2)',
                                    tension: 0.3,
                                    fill: false,
                                    pointBackgroundColor: '#4CAF50',
                                    pointBorderColor: '#fff',
                                    pointRadius: 5,
                                    pointHoverRadius: 7
                                }, {
                                    label: 'All Orders',
                                    data: allCounts,
                                    borderColor: '#2196F3', // Màu xanh dương
                                    backgroundColor: 'rgba(33, 150, 243, 0.2)',
                                    tension: 0.3,
                                    fill: false,
                                    pointBackgroundColor: '#2196F3',
                                    pointBorderColor: '#fff',
                                    pointRadius: 5,
                                    pointHoverRadius: 7
                                }]
                            },
                            options: {
                                responsive: true,
                                maintainAspectRatio: false, // <-- Quan trọng!
                                scales: {
                                    y: {
                                        beginAtZero: true,
                                        title: {
                                            display: true,
                                            text: 'Number of Orders',
                                            color: '#555'
                                        },
                                        ticks: {
                                            precision: 0
                                        }
                                    },
                                    x: {
                                        title: {
                                            display: true,
                                            text: 'Date',
                                            color: '#555'
                                        }
                                    }
                                },
                                plugins: {
                                    title: {
                                        display: false,
                                    },
                                    legend: {
                                        position: 'bottom',
                                        labels: {
                                            boxWidth: 20,
                                            padding: 15,
                                            font: {
                                                size: 12
                                            }
                                        }
                                    },
                                    tooltip: {
                                        mode: 'index',
                                        intersect: false,
                                        callbacks: {
                                            label: function(context) {
                                                let label = context.dataset.label || '';
                                                if (label) {
                                                    label += ': ';
                                                }
                                                if (context.parsed.y !== null) {
                                                    label += context.parsed.y;
                                                }
                                                return label;
                                            }
                                        }
                                    }
                                }
                            }
                        });
                    } else {
                        console.warn("Canvas element 'orderTrendChart' not found or no data. Chart will not be rendered.");
                    }
                }

                // Biểu đồ doanh thu theo danh mục (Pie Chart)
                const revenueByCategoriesJson = JSON.parse('<c:out value="${revenueByCategoriesJson}" escapeXml="false"/>' || '[]');
                if (revenueByCategoriesJson.length > 0) {
                    const categoryNames = [];
                    const categoryRevenues = [];
                    const backgroundColors = [
                        '#FF6384', '#36A2EB', '#FFCE56', '#4BC0C0', '#9966FF',
                        '#FF9900', '#C9CBCF', '#8BC34A', '#FF5722', '#673AB7'
                    ];

                    revenueByCategoriesJson.forEach(function(item) {
                        categoryNames.push(item.categoryName);
                        categoryRevenues.push(item.totalRevenue);
                    });

                    const revenueCtx = document.getElementById('revenueByCategoryChart');
                    if (revenueCtx) {
                         new Chart(revenueCtx.getContext('2d'), {
                            type: 'pie',
                            data: {
                                labels: categoryNames,
                                datasets: [{
                                    data: categoryRevenues,
                                    backgroundColor: backgroundColors.slice(0, categoryNames.length),
                                    hoverOffset: 10
                                }]
                            },
                            options: {
                                responsive: true,
                                maintainAspectRatio: false, // <-- Quan trọng!
                                plugins: {
                                    title: {
                                        display: false,
                                    },
                                    legend: {
                                        position: 'right',
                                        labels: {
                                            boxWidth: 20,
                                            padding: 15,
                                            font: {
                                                size: 12
                                            }
                                        }
                                    },
                                    tooltip: {
                                        callbacks: {
                                            label: function(context) {
                                                let label = context.label || '';
                                                if (label) {
                                                    label += ': ';
                                                }
                                                if (context.parsed) {
                                                    label += new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(context.parsed);
                                                }
                                                return label;
                                            }
                                        }
                                    }
                                }
                            }
                        });
                    }
                } else {
                    console.warn("Canvas element 'revenueByCategoryChart' not found or no data. Chart will not be rendered.");
                }
            });
        </script>
    </body>
</html>