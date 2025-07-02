<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Subject Management - Price Packages</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: "Inter", -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif;
            background-color: #f5f7fa;
            color: #1a202c;
            line-height: 1.7;
            font-size: 16px;
        }

        .container {
            max-width: 1280px;
            margin: 0 auto;
            padding: 0 24px;
        }

        .header {
            background: #ffffff;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
            position: fixed;
            top: 0;
            left: 0;
            right: 0;
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
            width: auto;
            transition: transform 200ms ease-in-out;
        }

        .logo:hover {
            transform: scale(1.08);
        }

        .brand-text {
            font-weight: 700;
            font-size: 20px;
            color: #2d3748;
            letter-spacing: 0.5px;
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
            transition: color 0.3s ease, transform 0.2s ease;
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

        .user-info {
            display: flex;
            align-items: center;
            gap: 12px;
        }

        .user-role {
            background: #5a67d8;
            color: #ffffff;
            padding: 6px 14px;
            border-radius: 9999px;
            font-size: 13px;
            font-weight: 600;
            text-transform: uppercase;
            letter-spacing: 0.5px;
        }

        .page-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 40px;
            flex-wrap: wrap;
            gap: 24px;
            padding-top: 120px;
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
            transition: all 0.3s ease;
        }

        .tab:hover {
            background-color: #e2e8f0;
            transform: translateY(-1px);
        }

        .tab.active {
            background-color: #5a67d8;
            color: #ffffff;
            font-weight: 700;
        }

        .main-content {
            padding: 20px;
        }

        .packages-section {
            background: white;
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

        .packages-header h3 {
            font-size: 20px;
            font-weight: 600;
        }

        .add-btn {
            background: #4caf50;
            color: white;
            border: none;
            padding: 10px 20px;
            border-radius: 6px;
            font-size: 14px;
            font-weight: 500;
            cursor: pointer;
            display: flex;
            align-items: center;
            gap: 8px;
        }

        .add-btn:hover {
            background: #45a049;
        }

        .error-message {
            background: #ffebee;
            color: #c62828;
            padding: 12px;
            border-radius: 6px;
            margin-bottom: 20px;
            border-left: 4px solid #c62828;
        }

        .packages-table {
            width: 100%;
            border-collapse: collapse;
            margin-bottom: 20px;
        }

        .packages-table th {
            background: #f5f5f5;
            padding: 15px 12px;
            text-align: left;
            font-weight: 600;
            font-size: 14px;
            color: #666;
            border-bottom: 1px solid #e0e0e0;
        }

        .packages-table td {
            padding: 15px 12px;
            border-bottom: 1px solid #f0f0f0;
            font-size: 14px;
        }

        .packages-table tr:hover {
            background: #f9f9f9;
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
            cursor: pointer;
            text-decoration: none;
            display: inline-flex;
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
            font-size: 14px;
            color: #666;
        }

        .pagination {
            display: flex;
            align-items: center;
            gap: 10px;
        }

        .no-data {
            text-align: center;
            padding: 40px;
            color: #666;
            font-style: italic;
        }

        @media (max-width: 768px) {
            .page-header {
                flex-direction: column;
                align-items: flex-start;
            }

            .header-tabs {
                width: 100%;
                justify-content: flex-start;
            }

            .header .container {
                padding: 12px 16px;
            }
        }

        @media (max-width: 480px) {
            .navbar ul {
                display: none;
            }

            .header .container {
                flex-direction: column;
                align-items: flex-start;
                gap: 12px;
            }

            .nav-brand {
                margin-bottom: 8px;
            }

            .header-actions {
                width: 100%;
                justify-content: flex-end;
            }

            .btn-primary {
                width: 100%;
                padding: 12px;
            }
        }

        @media print {
            .header,
            .btn-primary {
                display: none !important;
            }

            .page-header {
                padding-top: 0;
            }
        }
    </style>
</head>
<body>
    <header class="header">
        <div class="container">
            <div class="nav-brand">
                <img src="${pageContext.request.contextPath}/images/logo.jpg" alt="Soft Skills" class="logo">
                <span class="brand-text"></span>
            </div>
            <nav class="navbar">
                <ul class="nav-list">
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
        <div class="main-content">
            <div class="page-header">
                <div class="header-tabs">
                    <a href="${pageContext.request.contextPath}/subjectDetail?subjectId=${subject != null ? subject.subjectId : ''}" class="tab">Overview</a>
                    <a href="${pageContext.request.contextPath}/dimensionList?subjectId=${subject != null ? subject.subjectId : ''}" class="tab">Dimensions</a>
                    <a href="${pageContext.request.contextPath}/pricePackageList" class="tab active">Price Packages</a>
                </div>
            </div>

            <div class="packages-section">
                <div class="packages-header">
                    <h3>Price Packages</h3>
                    <button class="add-btn">
                        + Add New Package
                    </button>
                </div>

                <c:if test="${not empty errorMessage}">
                    <div class="error-message">${errorMessage}</div>
                </c:if>

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
                                        <a href="#" class="action-btn edit-action-btn">✏ Edit</a>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty packages}">
                            <tr>
                                <td colspan="5" class="no-data">No packages available.</td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>

                <div class="table-footer">
                    <div>
                        Showing <c:out value="${not empty packages ? packages.size() : 0}"/> packages
                    </div>
                    <div class="pagination">
                        <span>Page</span>
                        <strong>1</strong>
                        <span>of 1</span>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
