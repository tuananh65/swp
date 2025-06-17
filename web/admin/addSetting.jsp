<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Add New Setting - Soft Skills</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/SettingLi.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/login.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/addSetting.css" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/MyCourses.css">

    <style>
        /* Đồng bộ với SettingDetail.jsp */
        .hero-text h1 {
            font-size: 3em;
        }
        .hero-text p {
            font-size: 1.2em;
        }
    </style>
</head>
<body>
    <div class="wrapper">
        <jsp:include page="/default/header.jsp"/>
        
        <section class="hero-banner">
            <div class="hero-content">
                <%-- Removed the hero-overlay div --%>
                <img class="hero-image" src="${pageContext.request.contextPath}/images/Banner.jpg" alt="Banner">
                <div class="hero-text">
                    <h1>ADD NEW SETTING</h1>
                    <p><a href="${pageContext.request.contextPath}/admin/settinglist">Settings</a> / Add Setting</p>
                </div>
            </div>
        </section>
        <div class="content-wrapper">
            <section class="content">
                <form action="${pageContext.request.contextPath}/addSetting" method="post">
                    <div>
                        <label for="type">Type:</label>
                        <input type="text" id="type" name="type" value="${type}" required>
                    </div>
                    <div>
                        <label for="key">Key:</label>
                        <input type="text" id="key" name="key" value="${key}" required>
                    </div>
                    <div>
                        <label for="value">Value:</label>
                        <input type="text" id="value" name="value" value="${value}" required>
                    </div>
                    <div>
                        <label for="order">Order:</label>
                        <input type="number" id="order" name="order" value="${order}">
                    </div>
                    <div>
                        <label for="status">Status:</label>
                        <select id="status" name="status" required>
                            <option value="Active" ${status == 'Active' ? 'selected' : ''}>Active</option>
                            <option value="Inactive" ${status == 'Inactive' ? 'selected' : ''}>Inactive</option>
                        </select>
                    </div>
                    <div class="button-group">
                        <button type="submit" class="add-user-btn">Save Setting</button>
                        <a href="${pageContext.request.contextPath}/settinglist" class="cancel-btn">Cancel</a>
                    </div>
                </form>
            </section>
        </div>
        <jsp:include page="/default/footer.jsp"/>
    </div>
</body>
</html>