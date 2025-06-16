<%-- SettingDetail.jsp --%>
<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Setting Detail - Soft Skills</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/SettingLi.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/login.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/editSetting.css" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" />

</head>
<body>
    <div class="wrapper">
        <jsp:include page="/view/header.jsp"/>
        <div class="banner">
            <h1>SETTING DETAIL</h1>
            <p><a href="settinglist">Settings</a> / Setting Detail</p>
        </div>
        <div class="content-wrapper"> 
            <section class="content">
                 <c:if test="${not empty errorMessage}">
                     <div class="error-message">${errorMessage}</div>
                 </c:if>
                 <c:if test="${not empty setting}">
                     <form action="editSetting" method="post"> <input type="hidden" name="settingID" value="${setting.settingID}">
                         <div>
                             <label for="type">Type:</label>
                             <input type="text" id="type" name="type" value="${setting.type}" required>
                         </div>
                         <div>
                             <label for="key">Key:</label>
                             <input type="text" id="key" name="key" value="${setting.key}" required>
                         </div>
                         <div>
                             <label for="value">Value:</label>
                             <input type="text" id="value" name="value" value="${setting.value}" required>
                         </div>
                         <div>
                             <label for="order">Order:</label>
                             <input type="number" id="order" name="order" value="${setting.order}">
                         </div>
                         <div>
                             <label for="status">Status:</label>
                             <select id="status" name="status" required>
                                 <option value="active" ${setting.status == 'active' ? 'selected' : ''}>Active</option>
                                 <option value="inactive" ${setting.status == 'inactive' ? 'selected' : ''}>Inactive</option>
                             </select>
                         </div>
                         <div class="button-group">
                             <button type="submit" class="add-user-btn">Update Setting</button>
                             <a href="settinglist" class="cancel-btn">Cancel</a>
                             <a href="addSetting" class="add-new-btn">Add new</a>
                         </div>
                 </form>
                 </c:if>
                 <c:if test="${empty setting}">
                     <div class="button-group">
                         <a href="addSetting" class="add-new-btn">Add new</a>
                         <a href="settinglist" class="cancel-btn">Back to List</a>
                     </div>
                     <p class="error-message">Setting not found.</p>
                 </c:if>
             </section>       
        </div>
        <jsp:include page="/view/footer.jsp"/>
    </div>
</body>
</html>