<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Dimension Management</title>

    <!-- Font Awesome for icons -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">

    <style>
        /* ======= Reset and Basic Layout ======= */
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body {
            font-family: "Inter", sans-serif;
            background-color: #f5f7fa;
            color: #1a202c;
            font-size: 16px;
            line-height: 1.7;
        }

        .container {
            max-width: 1280px;
            margin: 0 auto;
            padding: 100px 24px 0;
        }

        /* ======= Header ======= */
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

        .header-actions {
            display: flex;
            align-items: center;
            gap: 16px;
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

        /* ======= Table ======= */
        table {
            width: 100%;
            border-collapse: collapse;
            margin-bottom: 20px;
        }

        th, td {
            padding: 12px 15px;
            border-bottom: 1px solid #e0e0e0;
            font-size: 14px;
        }

        th {
            background-color: #f5f5f5;
            font-weight: 600;
            color: #666;
            text-align: left;
        }

        tr:hover { background-color: #f9f9f9; }

        .action-btn {
            padding: 6px 12px;
            border: 1px solid;
            border-radius: 4px;
            font-size: 12px;
            text-decoration: none;
            display: inline-block;
            margin-right: 4px;
        }

        .edit-btn {
            color: #1976d2;
            border-color: #1976d2;
            background: #fff;
        }

        .edit-btn:hover { background: #e3f2fd; }

        .delete-btn {
            color: red;
            border-color: red;
            background: #fff;
        }

        .delete-btn:hover { background: #ffe6e6; }

        .add-btn {
            background: #4caf50;
            color: #fff;
            padding: 10px 20px;
            border: none;
            border-radius: 6px;
            font-size: 14px;
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

        /* ======= Popup Form ======= */
        .form-popup {
            display: none;
            background: #fff;
            padding: 20px;
            max-width: 500px;
            margin: 20px auto;
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
            color: #fff;
            border: none;
            cursor: pointer;
        }

        .form-popup button:hover {
            background-color: #45a049;
        }

        .form-popup .cancel-btn {
            background-color: #ccc;
            margin-left: 10px;
        }

        /* ======= Responsive ======= */
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

        @media print {
            .header, .btn-primary {
                display: none !important;
            }
        }

        .btn-back {
            display: inline-block;
            padding: 8px 14px;
            background-color: #3498db;
            color: white;
            text-decoration: none;
            border-radius: 5px;
            margin-bottom: 10px;
            font-weight: bold;
            transition: background-color 0.3s ease;
        }

        .btn-back:hover {
            background-color: #2980b9;
        }

        .btn-back i {
            margin-right: 5px;
        }
    </style>
</head>
<body>
    <!-- Phần header cố định -->
    <header class="header">
        <div class="container">
            <div class="nav-brand">
                <img src="${pageContext.request.contextPath}/images/logo.jpg" alt="Soft Skills" class="logo">
                <span class="brand-text"></span>
            </div>
            <nav class="navbar">
                <ul>
                    <li><a href="${pageContext.request.contextPath}/" class="nav-link">Dashboard</a></li>
                    <li><a href="${pageContext.request.contextPath}/subjects" class="nav-link">Subjects</a></li>
                    <li><a href="${pageContext.request.contextPath}/subjectDetail?subjectId=${subject != null ? subject.subjectId : ''}" class="nav-link active">Subject Details</a></li>
                    <li><a href="#" class="nav-link">Settings</a></li>
                </ul>
            </nav>
            <div class="header-actions">
                <div class="user-info">
                    <span class="user-role">Admin</span>
                    <i class="fas fa-user-circle"></i>
                </div>
            </div>
        </div>
    </header>

    <div class="container">
        <!-- Nút quay về Subject -->
        <div style="margin-bottom: 20px;">
            <a href="${pageContext.request.contextPath}/subjectDetail?subjectId=${subjectId}" class="btn-back">
                <i class="fas fa-arrow-left"></i> Back to Subject
            </a>
        </div>

        <!-- Hiển thị lỗi nếu có -->
        <c:if test="${not empty errorMessage}">
            <div class="error-message">${errorMessage}</div>
        </c:if>

        <!-- Bảng danh sách Dimension -->
        <table>
            <thead>
                <tr>
                    <th>#</th>
                    <th>Subject ID</th>
                    <th>Type</th>
                    <th>Dimension Name</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="dim" items="${dimensionList}" varStatus="status">
                    <tr>
                        <td>${status.index + 1}</td>
                        <td>${dim.subjectId}</td>
                        <td>${dim.type}</td>
                        <td>${dim.dimensionName}</td>
                        <td>
                            <a href="dimensionList?subjectId=${subjectId}&editId=${dim.dimensionId}" class="action-btn edit-btn">
                                <i class="fas fa-edit"></i> Edit
                            </a>
                            <a href="dimensionList?subjectId=${subjectId}&deleteId=${dim.dimensionId}" class="action-btn delete-btn"
                               onclick="return confirm('Are you sure you want to delete this dimension?');">
                                <i class="fas fa-trash"></i> Delete
                            </a>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty dimensionList}">
                    <tr>
                        <td colspan="5" style="text-align: center; padding: 20px; font-style: italic;">No dimensions found.</td>
                    </tr>
                </c:if>
            </tbody>
        </table>

        <!-- Nút thêm Dimension mới -->
        <button class="add-btn" onclick="openForm()">+ Add New Dimension</button>

        <!-- Form popup -->
        <div class="form-popup" id="dimensionForm">
            <form action="dimensionList" method="post">
                <input type="hidden" id="dimensionId" name="dimensionId">
                <input type="hidden" id="subjectId" name="subjectId" value="${subjectId}">

                <label for="type">Type</label>
                <input type="text" id="type" name="type" placeholder="Enter type" required>

                <label for="dimensionName">Dimension Name</label>
                <input type="text" id="dimensionName" name="dimensionName" placeholder="Enter dimension name" required>

                <button type="submit">💾 Save</button>
                <button type="button" class="cancel-btn" onclick="closeForm()">❌ Cancel</button>
            </form>
        </div>
    </div>

    <!-- JavaScript xử lý form popup -->
    <script>
        // Mở popup để thêm/sửa Dimension
        function openForm(dimensionId = '', type = '', dimensionName = '') {
            document.getElementById('dimensionId').value = dimensionId;
            document.getElementById('type').value = type;
            document.getElementById('dimensionName').value = dimensionName;
            document.getElementById('dimensionForm').style.display = 'block';
        }

        // Đóng popup
        function closeForm() {
            document.getElementById('dimensionForm').style.display = 'none';
        }

        // Tự động mở popup nếu có editDimension
        <c:if test="${not empty editDimension}">
            openForm('${editDimension.dimensionId}', '${editDimension.type}', '${editDimension.dimensionName}');
        </c:if>
    </script>
</body>
</html>
