<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List, java.util.Map, java.sql.Date, java.math.BigDecimal"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <title>Admin Dashboard</title>

        <%-- Đảm bảo đường dẫn CSS của bạn đúng, blog.css có thể chứa các style chung khác --%>
        <link href="${pageContext.request.contextPath}/css/blog.css" rel="stylesheet">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dashboard.css" />

        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" />
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">

        <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

        </head>
    <body>
        <div class="wrapper">
            <nav>
                <jsp:include page="/default/header.jsp"/>
            </nav>

            <%-- Nút này được tạo bằng JS để đảm bảo vị trí và hành vi đúng --%>
            <%-- Không đặt button ở đây nữa, nó sẽ được JS thêm vào DOM --%>

            <div id="courseDropdownContainer">
                <div id="userSection">
                    <div>
                        <button class="dropdown-toggle">
                            <a href="${pageContext.request.contextPath}/view/changePassword.jsp">Change Password</a>
                        </button>
                    </div>
                    <div>
                        <button class="dropdown-toggle">
                            <a href="${pageContext.request.contextPath}/profile?id=${sessionScope.currentUser.userId}">Profile</a>
                        </button>
                    </div>
                    <div>
                        <button class="dropdown-toggle">
                            <a href="${pageContext.request.contextPath}/admin/dashboard">Dash Board</a>
                        </button>
                    </div>
                    <div>
                        <button class="dropdown-toggle">
                            <a href="${pageContext.request.contextPath}/sliderlist">Slider List</a>
                        </button>
                    </div>
                    <div>
                        <button class="dropdown-toggle">
                            <a href="${pageContext.request.contextPath}/userlist">User List</a>
                        </button>
                    </div>
                    <div>
                        <button class="dropdown-toggle">
                            <a href="${pageContext.request.contextPath}/admin/subjectList">Subject List</a>
                        </button>
                    </div>
                    <div>
                        <button class="dropdown-toggle">
                            <a href="${pageContext.request.contextPath}/admin/registrations">Registration List</a>
                        </button>
                    </div>
                    <div>
                        <button class="dropdown-toggle">
                            <a href="${pageContext.request.contextPath}/auth?action=logout">Sign out</a>
                        </button>
                    </div>
                </div>
            </div>
            <section class="hero-banner dashboard-banner">
                <div class="hero-image-wrapper">
                    <img class="hero-image" src="${pageContext.request.contextPath}/images/Banner.jpg" alt="Banner Dashboard">
                    <div class="hero-overlay"></div>
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
                        <p class="stat-detail">New Subjects</p>
                        <p class="stat-total">Total: <c:out value="${allSubjects}" default="0"/></p>
                    </div>

                    <%-- Thống kê Registrations --%>
                    <div class="card stat-card">
                        <div class="card-icon"><i class="fas fa-user-graduate"></i></div>
                        <h3>Enrollment Registrations</h3>
                        <p class="stat-value"><c:out value="${allRegistrations}" default="0"/></p>
                        <p class="stat-detail">All Registrations</p>
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
                        <p class="stat-detail">Based on submitted enrollments</p>
                    </div>

                    <%-- Thống kê khách hàng --%>
                    <div class="card stat-card">
                        <div class="card-icon"><i class="fas fa-users"></i></div>
                        <h3>Customer Statistics</h3>
                        <p class="stat-value"><c:out value="${newlyRegisteredCustomers}" default="0"/></p>
                        <p class="stat-detail">Newly Registered</p>
                        <c:if test="${newlyBoughtCustomers > 0}">
                            <p class="stat-total">Newly Bought: <c:out value="${newlyBoughtCustomers}" default="0"/></p>
                        </c:if>
                    </div>
                </div>

                <%-- Phần biểu đồ và thống kê chi tiết --%>
                <div class="dashboard-charts-section">
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

                    <div class="chart-container card">
                        <h3>Revenue by Subject Category</h3>
                        <c:choose>
                            <c:when test="${not empty revenueBySubjectCategoryJson && revenueBySubjectCategoryJson ne '[]'}">
                                <canvas id="revenueByCategoryChart"></canvas>
                            </c:when>
                            <c:otherwise>
                                <p class="no-data-message">No revenue by category data for this period.</p>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </main>

            <jsp:include page="/default/footer.jsp"/>
        </div>

        <script>
            // === JavaScript cho Sidebar Toggle ===
            // Tạo và thêm nút toggle vào body
            const toggleMenuBtn = document.createElement('button');
            toggleMenuBtn.id = 'toggleMenuBtn';
            toggleMenuBtn.innerHTML = '<i class="bi bi-list"></i>'; // Sử dụng icon Bootstrap Icons
            document.body.appendChild(toggleMenuBtn);

            const menuContainer = document.getElementById('courseDropdownContainer');

            toggleMenuBtn.addEventListener('click', () => {
                const isVisible = menuContainer.style.display === 'block';
                menuContainer.style.display = isVisible ? 'none' : 'block';
            });

            // Đóng sidebar khi click ra ngoài
            document.addEventListener('click', function(event) {
                const sidebar = document.getElementById('courseDropdownContainer');
                const toggleButton = document.getElementById('toggleMenuBtn');

                if (sidebar && !sidebar.contains(event.target) && (!toggleButton || !toggleButton.contains(event.target))) {
                    sidebar.style.display = 'none';
                }
            });

            // === JavaScript cho Chart.js ===
            document.addEventListener('DOMContentLoaded', function() {
                // Biểu đồ Order Trend
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
                    const submittedCounts = [];
                    const allCounts = [];

                    orderTrendData.forEach(function(trend) {
                        const dateObj = new Date(trend.date);
                        const options = { month: 'short', day: 'numeric' };
                        dates.push(dateObj.toLocaleDateString('en-US', options));
                        submittedCounts.push(trend.submittedOrders);
                        allCounts.push(trend.allOrders);
                    });

                    const orderCtx = document.getElementById('orderTrendChart');
                    if (orderCtx) {
                        new Chart(orderCtx.getContext('2d'), {
                            type: 'line',
                            data: {
                                labels: dates,
                                datasets: [{
                                    label: 'Submitted Orders',
                                    data: submittedCounts,
                                    borderColor: '#FFC107',
                                    backgroundColor: 'rgba(255, 193, 7, 0.2)',
                                    tension: 0.3,
                                    fill: false,
                                    pointBackgroundColor: '#FFC107',
                                    pointBorderColor: '#fff',
                                    pointRadius: 5,
                                    pointHoverRadius: 7
                                }, {
                                    label: 'All Orders',
                                    data: allCounts,
                                    borderColor: '#2196F3',
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
                                maintainAspectRatio: false,
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
                        console.warn("Canvas element 'orderTrendChart' not found. Chart will not be rendered.");
                    }
                } else {
                    console.warn("No order trend data available to display the chart.");
                }

                // Biểu đồ Revenue by Category
                const revenueByCategoryJson = '${revenueBySubjectCategoryJson}';
                let revenueByCategoryData = [];
                try {
                    revenueByCategoryData = JSON.parse(revenueByCategoryJson || '[]');
                } catch (e) {
                    console.error("Error parsing revenueByCategoryJson:", e);
                    revenueByCategoryData = [];
                }

                if (revenueByCategoryData.length > 0) {
                    const categoryLabels = revenueByCategoryData.map(item => item.categoryName);
                    const categoryRevenues = revenueByCategoryData.map(item => item.totalRevenue);

                    const revenueCtx = document.getElementById('revenueByCategoryChart');
                    if (revenueCtx) {
                        new Chart(revenueCtx.getContext('2d'), {
                            type: 'pie', // Hoặc 'bar' tùy theo sở thích
                            data: {
                                labels: categoryLabels,
                                datasets: [{
                                    data: categoryRevenues,
                                    backgroundColor: [
                                        'rgba(255, 99, 132, 0.7)', // Red
                                        'rgba(54, 162, 235, 0.7)', // Blue
                                        'rgba(255, 206, 86, 0.7)', // Yellow
                                        'rgba(75, 192, 192, 0.7)', // Green
                                        'rgba(153, 102, 255, 0.7)', // Purple
                                        'rgba(255, 159, 64, 0.7)'  // Orange
                                    ],
                                    borderColor: [
                                        'rgba(255, 99, 132, 1)',
                                        'rgba(54, 162, 235, 1)',
                                        'rgba(255, 206, 86, 1)',
                                        'rgba(75, 192, 192, 1)',
                                        'rgba(153, 102, 255, 1)',
                                        'rgba(255, 159, 64, 1)'
                                    ],
                                    borderWidth: 1
                                }]
                            },
                            options: {
                                responsive: true,
                                maintainAspectRatio: false,
                                plugins: {
                                    legend: {
                                        position: 'top',
                                    },
                                    tooltip: {
                                        callbacks: {
                                            label: function(context) {
                                                let label = context.label || '';
                                                if (label) {
                                                    label += ': ';
                                                }
                                                if (context.parsed !== null) {
                                                    label += new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(context.parsed); // Định dạng tiền tệ VNĐ
                                                }
                                                return label;
                                            }
                                        }
                                    }
                                }
                            }
                        });
                    } else {
                        console.warn("Canvas element 'revenueByCategoryChart' not found. Chart will not be rendered.");
                    }
                } else {
                    console.warn("No revenue by category data available to display the chart.");
                }

            }); // End DOMContentLoaded
        </script>
    </body>
</html>