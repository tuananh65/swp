<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Đã hủy thanh toán</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="${pageContext.request.contextPath}/css/payment-result.css" rel="stylesheet">
</head>
<body>
    <div class="payment-container">
        <h2>Đã hủy thanh toán</h2>
        
        <div class="status-indicator status-error">⚠</div>
        <div class="error-message">
            <div class="message-text">
                Bạn đã hủy giao dịch thanh toán.
            </div>
            <div class="message-text">
                Giao dịch chưa được hoàn thành.
            </div>
        </div>
        <a href="${pageContext.request.contextPath}/student/dashboard.jsp">Quay lại Dashboard</a>
    </div>
</body>
</html>
