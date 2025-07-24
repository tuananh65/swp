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

        <%-- Nhúng thư viện Chart.js --%>
        <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
        <%-- Nhúng Chart.js adapter for date-fns để xử lý trục thời gian tốt hơn --%>
        <script src="https://cdn.jsdelivr.net/npm/chartjs-adapter-date-fns/dist/chartjs-adapter-date-fns.bundle.min.js"></script>

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

                    <%-- Biểu đồ Doanh thu theo danh mục môn học --%>
                    <div class="chart-container card">
                        <h3>Revenue by Subject Category (within selected period)</h3>
                        <c:choose>
                            <c:when test="${not empty revenueByCategoriesJson && revenueByCategoriesJson ne '[]'}">
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
            // --- JavaScript cho Sidebar Toggle ---
            // Tạo và thêm nút toggle vào body. Nút này sẽ hiển thị hoặc ẩn menu điều hướng.
            const toggleMenuBtn = document.createElement('button');
            toggleMenuBtn.id = 'toggleMenuBtn';
            toggleMenuBtn.innerHTML = '<i class="bi bi-list"></i>'; // Sử dụng icon Bootstrap Icons cho nút toggle
            document.body.appendChild(toggleMenuBtn); // Thêm nút vào cuối body

            // Lấy tham chiếu đến container chứa các mục dropdown của menu
            const menuContainer = document.getElementById('courseDropdownContainer');

            // Thêm sự kiện click cho nút toggle
            toggleMenuBtn.addEventListener('click', () => {
                // Kiểm tra trạng thái hiện tại của menu: nếu đang hiển thị thì ẩn đi, ngược lại thì hiển thị
                const isVisible = menuContainer.style.display === 'block';
                menuContainer.style.display = isVisible ? 'none' : 'block';
            });

            // Thêm sự kiện click toàn bộ tài liệu để đóng sidebar khi người dùng click ra ngoài
            document.addEventListener('click', function(event) {
                const sidebar = document.getElementById('courseDropdownContainer');
                const toggleButton = document.getElementById('toggleMenuBtn');

                // Nếu sidebar tồn tại VÀ click không phải trên sidebar VÀ click không phải trên nút toggle
                if (sidebar && !sidebar.contains(event.target) && (!toggleButton || !toggleButton.contains(event.target))) {
                    sidebar.style.display = 'none'; // Ẩn sidebar đi
                }
            });

            // --- JavaScript cho Chart.js ---
            // Đảm bảo rằng DOM đã được tải hoàn chỉnh trước khi chạy script vẽ biểu đồ
            document.addEventListener('DOMContentLoaded', function() {

                // --- Biểu đồ Order Trend (Xu hướng số lượng đơn hàng) ---
                // Lấy chuỗi JSON chứa dữ liệu xu hướng đơn hàng từ requestScope của JSP
                // Sử dụng || '[]' để đảm bảo nó là một chuỗi JSON hợp lệ (mảng rỗng) nếu biến không tồn tại
                const orderTrendJson = '${orderTrendJson}';
                let orderTrendData = [];
                try {
                    // Cố gắng parse chuỗi JSON thành đối tượng JavaScript
                    orderTrendData = JSON.parse(orderTrendJson || '[]');
                } catch (e) {
                    // Ghi lỗi vào console nếu có vấn đề khi parse JSON
                    console.error("Error parsing orderTrendJson:", e);
                    orderTrendData = []; // Đặt lại dữ liệu là mảng rỗng để tránh lỗi tiếp theo
                }

                // Chỉ vẽ biểu đồ nếu có dữ liệu
                if (orderTrendData.length > 0) {
                    const dates = []; // Mảng chứa các ngày cho trục X
                    const submittedCounts = []; // Mảng chứa số lượng đơn hàng "Submitted"
                    const allCounts = []; // Mảng chứa tổng số lượng đơn hàng

                    // Lặp qua dữ liệu nhận được và điền vào các mảng trên
                    orderTrendData.forEach(function(trend) {
                        // Chuyển đổi chuỗi ngày thành đối tượng Date của JavaScript
                        const dateObj = new Date(trend.date);
                        // Định dạng ngày để hiển thị trên nhãn trục (ví dụ: "Jul 24")
                        const options = { month: 'short', day: 'numeric' };
                        dates.push(dateObj.toLocaleDateString('en-US', options));
                        // Lấy số lượng đơn hàng đã gửi và tổng số đơn hàng
                        submittedCounts.push(trend.submittedOrders);
                        allCounts.push(trend.allOrders);
                    });

                    // Lấy tham chiếu đến phần tử canvas nơi biểu đồ Order Trend sẽ được vẽ
                    const orderCtx = document.getElementById('orderTrendChart');
                    if (orderCtx) {
                        // Tạo một biểu đồ mới sử dụng Chart.js
                        new Chart(orderCtx.getContext('2d'), {
                            type: 'line', // Loại biểu đồ: đường
                            data: {
                                labels: dates, // Nhãn cho trục X (các ngày)
                                datasets: [{
                                    label: 'Submitted Orders', // Tên bộ dữ liệu hiển thị trong chú giải
                                    data: submittedCounts, // Dữ liệu số lượng đơn hàng đã gửi
                                    borderColor: '#FFC107', // Màu đường (vàng/cam)
                                    backgroundColor: 'rgba(255, 193, 7, 0.2)', // Màu nền dưới đường (có độ trong suốt)
                                    tension: 0.3, // Độ cong của đường
                                    fill: false, // Không đổ màu bên dưới đường
                                    pointBackgroundColor: '#FFC107', // Màu của các điểm dữ liệu
                                    pointBorderColor: '#fff', // Màu viền của các điểm dữ liệu
                                    pointRadius: 5, // Bán kính của các điểm dữ liệu
                                    pointHoverRadius: 7 // Bán kính của các điểm khi di chuột qua
                                }, {
                                    label: 'All Orders', // Tên bộ dữ liệu cho tất cả đơn hàng
                                    data: allCounts, // Dữ liệu tổng số đơn hàng
                                    borderColor: '#2196F3', // Màu đường (xanh dương)
                                    backgroundColor: 'rgba(33, 150, 243, 0.2)', // Màu nền dưới đường (có độ trong suốt)
                                    tension: 0.3,
                                    fill: false,
                                    pointBackgroundColor: '#2196F3',
                                    pointBorderColor: '#fff',
                                    pointRadius: 5,
                                    pointHoverRadius: 7
                                }]
                            },
                            options: {
                                responsive: true, // Biểu đồ tự điều chỉnh kích thước
                                maintainAspectRatio: false, // Không giữ tỉ lệ khung hình mặc định
                                scales: {
                                    y: { // Cấu hình trục Y
                                        beginAtZero: true, // Bắt đầu từ 0
                                        title: {
                                            display: true,
                                            text: 'Number of Orders', // Tiêu đề trục Y
                                            color: '#555'
                                        },
                                        ticks: {
                                            precision: 0 // Đảm bảo các giá trị trên trục Y là số nguyên
                                        }
                                    },
                                    x: { // Cấu hình trục X
                                        title: {
                                            display: true,
                                            text: 'Date', // Tiêu đề trục X
                                            color: '#555'
                                        }
                                    }
                                },
                                plugins: {
                                    title: {
                                        display: false, // Ẩn tiêu đề biểu đồ (đã có H3 bên ngoài)
                                    },
                                    legend: { // Cấu hình chú giải (legend)
                                        position: 'bottom', // Đặt chú giải ở dưới cùng
                                        labels: {
                                            boxWidth: 20, // Chiều rộng của hộp màu trong chú giải
                                            padding: 15, // Khoảng cách giữa các mục chú giải
                                            font: {
                                                size: 12 // Kích thước font của chú giải
                                            }
                                        }
                                    },
                                    tooltip: { // Cấu hình tooltip (hiển thị khi di chuột)
                                        mode: 'index', // Hiển thị tooltip cho tất cả các đường tại cùng một điểm trên trục X
                                        intersect: false, // Tooltip sẽ hiển thị ngay cả khi con trỏ không trực tiếp nằm trên điểm dữ liệu
                                        callbacks: {
                                            label: function(context) {
                                                let label = context.dataset.label || '';
                                                if (label) {
                                                    label += ': ';
                                                }
                                                if (context.parsed.y !== null) {
                                                    label += context.parsed.y; // Hiển thị giá trị y
                                                }
                                                return label;
                                            }
                                        }
                                    }
                                }
                            }
                        });
                    } else {
                        // Ghi cảnh báo nếu không tìm thấy phần tử canvas
                        console.warn("Canvas element 'orderTrendChart' not found. Chart will not be rendered.");
                    }
                } else {
                    // Ghi cảnh báo nếu không có dữ liệu để vẽ biểu đồ
                    console.warn("No order trend data available to display the chart.");
                }

                // --- Biểu đồ Revenue by Category (Doanh thu theo danh mục) ---
                // Lấy chuỗi JSON chứa dữ liệu doanh thu theo danh mục từ requestScope của JSP
                const revenueByCategoryJson = '${revenueByCategoriesJson}'; // Lưu ý: đã sửa tên biến cho đúng với DashboardServlet
                let revenueByCategoryData = [];
                try {
                    revenueByCategoryData = JSON.parse(revenueByCategoryJson || '[]');
                } catch (e) {
                    console.error("Error parsing revenueByCategoryJson:", e);
                    revenueByCategoryData = [];
                }

                // Chỉ vẽ biểu đồ nếu có dữ liệu
                if (revenueByCategoryData.length > 0) {
                    const categoryLabels = revenueByCategoryData.map(item => item.categoryName); // Tên danh mục
                    const categoryRevenues = revenueByCategoryData.map(item => item.totalRevenue); // Doanh thu tương ứng

                    // Lấy tham chiếu đến phần tử canvas nơi biểu đồ Revenue by Category sẽ được vẽ
                    const revenueCtx = document.getElementById('revenueByCategoryChart');
                    if (revenueCtx) {
                        // Tạo một biểu đồ mới sử dụng Chart.js
                        new Chart(revenueCtx.getContext('2d'), {
                            type: 'pie', // Loại biểu đồ: hình tròn (có thể đổi thành 'bar' cho biểu đồ cột)
                            data: {
                                labels: categoryLabels, // Nhãn cho các lát cắt/cột
                                datasets: [{
                                    data: categoryRevenues, // Dữ liệu doanh thu
                                    backgroundColor: [ // Mảng các màu nền cho mỗi lát cắt/cột
                                        'rgba(255, 99, 132, 0.7)', // Red
                                        'rgba(54, 162, 235, 0.7)', // Blue
                                        'rgba(255, 206, 86, 0.7)', // Yellow
                                        'rgba(75, 192, 192, 0.7)', // Green
                                        'rgba(153, 102, 255, 0.7)', // Purple
                                        'rgba(255, 159, 64, 0.7)'  // Orange
                                    ],
                                    borderColor: [ // Mảng các màu viền
                                        'rgba(255, 99, 132, 1)',
                                        'rgba(54, 162, 235, 1)',
                                        'rgba(255, 206, 86, 1)',
                                        'rgba(75, 192, 192, 1)',
                                        'rgba(153, 102, 255, 1)',
                                        'rgba(255, 159, 64, 1)'
                                    ],
                                    borderWidth: 1 // Độ dày viền
                                }]
                            },
                            options: {
                                responsive: true,
                                maintainAspectRatio: false,
                                plugins: {
                                    legend: {
                                        position: 'top', // Đặt chú giải ở trên cùng
                                    },
                                    tooltip: {
                                        callbacks: {
                                            label: function(context) {
                                                let label = context.label || '';
                                                if (label) {
                                                    label += ': ';
                                                }
                                                if (context.parsed !== null) {
                                                    // Định dạng giá trị tiền tệ theo VNĐ
                                                    label += new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(context.parsed);
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

            }); // Kết thúc DOMContentLoaded event listener
        </script>
    </body>
</html>