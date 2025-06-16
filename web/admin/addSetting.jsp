<%-- addSetting.jsp --%>
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

</head>
<body>
    <div class="wrapper">
        <jsp:include page="/default/header.jsp"/>
        <div class="banner">
            <h1>ADD NEW SETTING</h1>
            <p><a href="settinglist">Settings</a> / Add Setting</p>
        </div>
        <div class="content-wrapper"> 
            <section class="content">
                <form action="addSetting" method="post">
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
                            <option value="active" ${status == 'active' ? 'selected' : ''}>Active</option>
                            <option value="inactive" ${status == 'inactive' ? 'selected' : ''}>Inactive</option>
                        </select>
                    </div>
                    <form action="addSetting" method="post">
                        </c:if>
                        <div class="button-group">
                            <button type="submit" class="add-user-btn">Save Setting</button>
                            <a href="settinglist" class="cancel-btn">Cancel</a>
                        </div>
                    </form>
                </form>
            </section>
        </div>
        <jsp:include page="/default/footer.jsp"/>
    </div>
</body>
</html>