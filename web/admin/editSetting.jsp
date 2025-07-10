<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Setting Detail - Soft Skills</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" />

    <%-- Các file CSS đã tối ưu: --%>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/MyCourses.css"> <%-- Cho banner --%>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/SettingLi.css" /> <%-- Các style chung cho admin --%>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/editSetting.css" /> <%-- Các style riêng cho form này --%>

    <style>
        /* Các style riêng cho SettingDetail, ghi đè hoặc bổ sung */
        .hero-text h1 {
            font-size: 3.5em; /* Lớn hơn một chút để nhất quán với SettingList */
            text-shadow: 2px 2px 4px rgba(0,0,0,0.4);
        }
        .hero-text p {
            font-size: 1.1em;
        }

        /* Đảm bảo content-wrapper không bị header che khuất nếu header cố định */
        .content-wrapper {
            margin-top: 88px; /* Giả sử header cao 88px */
            display: flex;
            justify-content: center;
            align-items: flex-start; /* Căn trên đầu container */
            padding: 30px 0; /* Tăng padding trên/dưới một chút */
            width: 100%;
            box-sizing: border-box;
        }

        /* Thêm style cho thẻ p trong hero-text để link home không bị gạch chân */
        .hero-text p a {
            color: #fff;
            text-decoration: none; /* Bỏ gạch chân */
            transition: color 0.3s ease;
        }
        .hero-text p a:hover {
            color: #ffc107; /* Màu vàng khi hover */
        }
    </style>
</head>
<body>
    <div class="wrapper">
        <jsp:include page="/default/header.jsp"/>

        <%-- Cấu trúc banner giống hệt MyCourses.jsp và SettingList.jsp --%>
        <section class="hero-banner">
            <div class="hero-content">
                <%-- Removed the hero-overlay div --%>
                <img class="hero-image" src="${pageContext.request.contextPath}/images/Banner.jpg" alt="Banner">
                <div class="hero-text">
                    <h1>SETTING DETAIL</h1>
                    <p><a href="${pageContext.request.contextPath}/settinglist">Settings</a> / Setting Detail</p>
                </div>
            </div>
        </section>
        <%-- Hết cấu trúc banner --%>

        <div class="content-wrapper">
            <section class="content">
                <c:if test="${not empty errorMessage}">
                    <div class="error-message">${errorMessage}</div>
                </c:if>
                <c:if test="${not empty setting}">
                    <form action="editSetting" method="post" class="setting-form"> <%-- Thêm class setting-form --%>
                        <input type="hidden" name="settingID" value="${setting.settingID}">

                        <div class="form-group">
                            <label for="type">Type:</label>
                            <input type="text" id="type" name="type" value="${setting.type}" required class="form-control">
                        </div>
                        <div class="form-group">
                            <label for="key">Key:</label>
                            <input type="text" id="key" name="key" value="${setting.key}" required class="form-control">
                        </div>
                        <div class="form-group">
                            <label for="value">Value:</label>
                            <input type="text" id="value" name="value" value="${setting.value}" required class="form-control">
                        </div>
                        <div class="form-group">
                            <label for="order">Order:</label>
                            <input type="number" id="order" name="order" value="${setting.order}" class="form-control">
                        </div>
                        <div class="form-group">
                            <label for="status">Status:</label>
                            <select id="status" name="status" required class="form-control">
                                <option value="Active" ${setting.status == 'Active' ? 'selected' : ''}>Active</option>
                                <option value="Inactive" ${setting.status == 'Inactive' ? 'selected' : ''}>Inactive</option>
                            </select>
                        </div>

                        <div class="button-group">
                            <button type="submit" class="btn btn-primary"><i class="fas fa-save"></i> Update Setting</button>
                            <a href="${pageContext.request.contextPath}/settinglist" class="btn btn-secondary"><i class="fas fa-times-circle"></i> Cancel</a>
                            <a href="${pageContext.request.contextPath}/addSetting" class="btn btn-success"><i class="fas fa-plus-circle"></i> Add new</a>
                        </div>
                    </form>
                </c:if>
                <c:if test="${empty setting}">
                    <div class="button-group">
                        <a href="${pageContext.request.contextPath}/addSetting" class="btn btn-success"><i class="fas fa-plus-circle"></i> Add new</a>
                        <a href="${pageContext.request.contextPath}/settinglist" class="btn btn-secondary"><i class="fas fa-arrow-alt-circle-left"></i> Back to List</a>
                    </div>
                    <p class="no-setting-found-message">Setting not found or invalid ID provided.</p> <%-- Class mới --%>
                </c:if>
            </section>
        </div>
        <jsp:include page="/default/footer.jsp"/>
    </div>
</body>
</html>