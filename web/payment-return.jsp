<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page pageEncoding="UTF-8" %>
<%@ page import="dal.EnrollmentDAO" %>
<!DOCTYPE html>
<html>
<head>
    <title>Kết quả thanh toán</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="${pageContext.request.contextPath}/css/payment-result.css" rel="stylesheet">
</head>
<body>
    <div class="payment-container">
        <h2>Payment results</h2>
        
        <c:choose>
            <c:when test="${param.status == 'PAID'}">
                <div class="status-indicator status-success">✓</div>
                <div class="success-message">
                    <div class="message-text">
                        <%
                            String enrollmentIdStr = request.getParameter("orderCode");
                            try {
                                int enrollmentId = Integer.parseInt(enrollmentIdStr);
                                EnrollmentDAO dao = new EnrollmentDAO();
                                boolean updated = dao.updateStatus(enrollmentId, "Confirmed");
                                if (updated) {
                                    out.println("Payment successful! Registration status has been updated to Confirmed.");
                                } else {
                                    out.println("Payment successful but application status cannot be updated.");
                                }
                            } catch (NumberFormatException e) {
                                out.println("Error: Invalid application ID.");
                            }
                        %>
                    </div>
                </div>
                <a href="student/dashboard.jsp">Go Back</a>
            </c:when>
            <c:otherwise>
                <div class="status-indicator status-error">✗</div>
                <div class="error-message">
                    <div class="message-text">
                        Payment failed or was canceled.
                    </div>
                </div>
                <a href="student/dashboard.jsp">Go Back</a>
            </c:otherwise>
        </c:choose>
    </div>
</body>
</html>
