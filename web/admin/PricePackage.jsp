<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Quản lý môn học - Gói học phí</title>

    <!-- Font Awesome để hiển thị icon -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">

    <style>
        /* Reset CSS cơ bản */
        * { margin: 0; padding: 0; box-sizing: border-box; }

        /* Cấu hình body */
        body {
            font-family: "Inter", sans-serif;
            background-color: #f5f7fa;
            color: #1a202c;
            font-size: 16px;
            line-height: 1.7;
        }

        /* Container chính giữa màn hình */
        .container {
            max-width: 1280px;
            margin: 0 auto;
            padding: 0 24px;
        }

        /* Thanh header cố định trên cùng */
        .header {
            background: #fff;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
            position: fixed;
            top: 0; left: 0; right: 0;
            z-index: 1000;
        }

        .header .container {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 16px 24px;
        }

        /* Logo và tên thương hiệu */
        .nav-brand {
            display: flex;
            align-items: center;
            gap: 12px;
        }

        .logo {
            height: 52px;
            transition: transform 0.2s ease-in-out;
        }

        .logo:hover { transform: scale(1.08); }

        .brand-text {
            font-weight: 700;
            font-size: 20px;
            color: #2d3748;
        }

        /* Menu điều hướng */
        .navbar ul {
            display: flex;
            list-style: none;
            gap: 32px;
        }

        .nav-link {
            text-decoration: none;
            color: #2d3748;
            font-weight: 600;
            font-size: 15px;
        }

        .nav-link:hover,
        .nav-link.active {
            color: #5a67d8;
            transform: translateY(-1px);
        }

        /* Hiển thị người dùng */
        .header-actions {
            display: flex;
            align-items: center;
            gap: 16px;
        }

        .user-info {
            display: flex;
            align-items: center;
            gap: 12px;
        }

        .user-role {
            background: #5a67d8;
            color: #fff;
            padding: 6px 14px;
            border-radius: 9999px;
            font-size: 13px;
            font-weight: 600;
            text-transform: uppercase;
        }

        /* Phần tab điều hướng chi tiết môn học */
        .main-content { padding: 20px; }

        .page-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            flex-wrap: wrap;
            gap: 24px;
            padding-top: 120px;
            margin-bottom: 40px;
        }

        .header-tabs {
            display: flex;
            gap: 12px;
        }

        .tab {
            padding: 12px 24px;
            background-color: #edf2f7;
            border-radius: 8px;
            text-decoration: none;
            color: #2d3748;
            font-weight: 600;
            font-size: 14px;
        }

        .tab:hover {
            background-color: #e2e8f0;
        }

        .tab.active {
            background-color: #5a67d8;
            color: #fff;
        }

        /* Bảng hiển thị danh sách gói */
        .packages-section {
            background: #fff;
            border-radius: 8px;
            padding: 25px;
            box-shadow: 0 1px 3px rgba(0,0,0,0.1);
        }

        .packages-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 25px;
        }

        .add-btn {
            background: #4caf50;
            color: white;
            padding: 10px 20px;
            border-radius: 6px;
            border: none;
            font-weight: 500;
            cursor: pointer;
        }

        .add-btn:hover { background: #45a049; }

        .error-message {
            background: #ffebee;
            color: #c62828;
            padding: 12px;
            border-left: 4px solid #c62828;
            border-radius: 6px;
            margin-bottom: 20px;
        }

        table.packages-table {
            width: 100%;
            border-collapse: collapse;
            margin-bottom: 20px;
        }

        .packages-table th, .packages-table td {
            padding: 15px 12px;
            font-size: 14px;
        }

        .packages-table th {
            background: #f5f5f5;
            color: #666;
            font-weight: 600;
        }

        .packages-table td {
            border-bottom: 1px solid #f0f0f0;
        }

        .package-name {
            color: #1976d2;
            font-weight: 500;
        }

        .actions {
            display: flex;
            gap: 8px;
        }

        .action-btn {
            padding: 6px 12px;
            border: 1px solid;
            border-radius: 4px;
            font-size: 12px;
            text-decoration: none;
            display: flex;
            align-items: center;
            gap: 4px;
        }

        .edit-action-btn {
            color: #1976d2;
            border-color: #1976d2;
            background: white;
        }

        .edit-action-btn:hover {
            background: #e3f2fd;
        }

        .table-footer {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding-top: 15px;
            color: #666;
        }

        .no-data {
            text-align: center;
            padding: 40px;
            color: #666;
            font-style: italic;
        }

        /* Responsive cho điện thoại */
        @media (max-width: 768px) {
            .page-header { flex-direction: column; }
        }

        @media (max-width: 480px) {
            .navbar ul { display: none; }
            .header .container {
                flex-direction: column;
                gap: 12px;
            }
        }

        /* Ẩn header khi in */
        @media print {
            .header, .btn-primary {
                display: none !important;
            }
        }

        /* Form popup thêm/sửa gói */
        .form-popup {
            display: none;
            background: #fff;
            padding: 20px;
            max-width: 500px;
            margin: 0 auto;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
        }

        .form-popup input[type="text"],
        .form-popup input[type="number"] {
            width: 100%;
            padding: 8px;
            margin: 8px 0;
        }

        .form-popup button {
            padding: 8px 16px;
            background-color: #4caf50;
            color: white;
            border: none;
            cursor: pointer;
        }

        .form-popup .cancel-btn {
            background-color: #ccc;
            margin-left: 10px;
        }
    </style>
</head>
<body>

<!-- Phần Header -->
<jsp:include page="/default/header.jsp"/>

<!-- Nội dung chính -->
<div class="container">
    <div class="main-content">
        <!-- Tabs điều hướng -->
        <div class="page-header">
            <div class="header-tabs">
                <a href="${pageContext.request.contextPath}/subjectDetail?subjectId=${subject != null ? subject.subjectId : ''}" class="tab">Overview</a>
                <a href="${pageContext.request.contextPath}/dimensionList?subjectId=${subject != null ? subject.subjectId : ''}" class="tab">Dimensions</a>
                <a href="${pageContext.request.contextPath}/pricePackageList" class="tab active">Price Packages</a>
            </div>
        </div>

        <!-- Bảng danh sách gói -->
        <div class="packages-section">
            <div class="packages-header">
                <h3>Price Packages</h3>
                <button class="add-btn" onclick="openForm()">+ Thêm gói mới</button>
            </div>

            <!-- Thông báo lỗi -->
            <c:if test="${not empty errorMessage}">
                <div class="error-message">${errorMessage}</div>
            </c:if>

            <!-- Bảng hiển thị dữ liệu -->
            <table class="packages-table">
                <thead>
                    <tr>
                        <th>#</th>
                        <th>Package</th>
                        <th>Duration</th>
                        <th>Price Modifier</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="pkg" items="${packages}" varStatus="status">
                        <tr>
                            <td>${status.index + 1}</td>
                            <td class="package-name">${pkg.name}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${pkg.durationInDays == 0}">Unlimited</c:when>
                                    <c:when test="${pkg.durationInDays == 90}">3 months</c:when>
                                    <c:when test="${pkg.durationInDays == 180}">6 months</c:when>
                                    <c:otherwise>${pkg.durationInDays} days</c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${pkg.priceModifier > 0}">${pkg.priceModifier}</c:when>
                                    <c:otherwise>N/A</c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <div class="actions">
                                    <a href="javascript:void(0)" class="action-btn edit-action-btn"
                                       onclick="openForm('${pkg.packageId}', '${pkg.name}', '${pkg.durationInDays}', '${pkg.priceModifier}')">✏ Edit</a>
                                    <a href="pricePackageList?deleteId=${pkg.packageId}" class="action-btn edit-action-btn"
                                       style="color: red; border-color: red;"
                                       onclick="return confirm('Bạn có chắc chắn muốn xoá gói này không?')">🗑 Delete</a>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty packages}">
                        <tr>
                            <td colspan="5" class="no-data">Không có gói nào được hiển thị.</td>
                        </tr>
                    </c:if>
                </tbody>
            </table>

            <!-- Footer bảng -->
            <div class="table-footer">
                <div>Hiển thị <c:out value="${not empty packages ? packages.size() : 0}"/> gói</div>
                <div class="pagination"><span>Page</span> <strong>1</strong> <span>of 1</span></div>
            </div>
        </div>
    </div>
</div>

<!-- Form thêm/sửa gói -->
<div class="form-popup" id="packageForm">
    <form action="pricePackageList" method="post">
        <input type="hidden" id="packageId" name="packageId">
        <label for="name">Tên Package</label>
        <input type="text" id="name" name="name" required>
        <label for="durationInDays">Thời lượng (ngày)</label>
        <input type="number" id="durationInDays" name="durationInDays" required>
        <label for="priceModifier">Hệ số giá</label>
        <input type="number" step="0.01" id="priceModifier" name="priceModifier" required>
        <button type="submit">💾 Lưu</button>
        <button type="button" class="cancel-btn" onclick="closeForm()">❌ Hủy</button>
    </form>
</div>

<!-- JavaScript để xử lý hiển thị form -->
<script>
    function openForm(packageId = '', name = '', duration = '', price = '') {
        document.getElementById('packageId').value = packageId;
        document.getElementById('name').value = name;
        document.getElementById('durationInDays').value = duration;
        document.getElementById('priceModifier').value = price;
        document.getElementById('packageForm').style.display = 'block';
    }

    function closeForm() {
        document.getElementById('packageForm').style.display = 'none';
    }
</script>

<!-- Nếu đang chỉnh sửa, tự động mở form với dữ liệu đã có -->
<c:if test="${not empty editPackage}">
    <script>
        openForm('${editPackage.packageId}', '${editPackage.name}', '${editPackage.durationInDays}', '${editPackage.priceModifier}');
    </script>
</c:if>

</body>
</html>
