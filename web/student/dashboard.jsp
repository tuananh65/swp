<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>My Registrations</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/myRegistrations.css">
</head>
<body>
    <jsp:include page="/view/header.jsp"/>

    <section class="banner">
        <div class="container">
            <h1>MY REGISTRATIONS</h1>
        </div>
    </section>

    

    <jsp:include page="/view/footer.jsp"/>
</body>
</html>
