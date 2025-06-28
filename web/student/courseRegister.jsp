<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!-- Modal Popup -->
<div id="modalOverlay" class="courseRegister-modal-overlay" onclick="closeModal(event)">
    <div class="courseRegister-modal-content" onclick="event.stopPropagation()">
        <div class="courseRegister-modal-header">
            <button class="courseRegister-close-btn" onclick="closeModal()">×</button>
            <h2 class="courseRegister-modal-title">COURSE REGISTER</h2>
        </div>
        <div class="courseRegister-modal-body">
            <c:if test="${not empty error}">
                <div class="courseRegister-error" style="color: #e17055; margin-bottom: 1rem; text-align: center;">
                    ${error}
                </div>
            </c:if>
            <form id="registrationForm" action="${pageContext.request.contextPath}/register-course" method="post">
                <input type="hidden" name="courseId" value="${course.courseID}" />
                <input type="hidden" name="basePrice" value="${course.salePrice}" />
                <div class="courseRegister-form-group">
                    <label for="subjectName">Subject Name <span class="courseRegister-required">*</span></label>
                    <input type="text" id="subjectName" name="subjectName" class="courseRegister-form-control" value="${course.courseName}" readonly required>
                </div>
                <div class="courseRegister-form-group">
                    <label for="packageId">Package <span class="courseRegister-required">*</span></label>
                    <select id="packageId" name="packageId" class="courseRegister-form-control" required>
                        <option value="">Select Package</option>
                        <c:forEach var="pkg" items="${packageList}">
                            <option value="${pkg.packageId}" data-modifier="${pkg.priceModifier}" data-duration="${pkg.durationInDays}">
                                ${pkg.name} (${pkg.durationInDays} days)
                            </option>
                        </c:forEach>
                    </select>
                </div>
                <div class="courseRegister-form-group">
                    <label for="price">Total Price</label>
                    <input type="text" id="price" name="price" class="courseRegister-form-control" readonly>
                </div>
                <div class="courseRegister-form-group">
                    <label for="fullName">Full Name <span class="courseRegister-required">*</span></label>
                    <input type="text" id="fullName" name="fullName" class="courseRegister-form-control" required>
                </div>
                <div class="courseRegister-form-group">
                    <label for="gender">Gender</label>
                    <select id="gender" name="gender" class="courseRegister-form-control">
                        <option value="">Select Gender</option>
                        <option value="male">Male</option>
                        <option value="female">Female</option>
                    </select>
                </div>
                <div class="courseRegister-form-group">
                    <label for="email">Email <span class="courseRegister-required">*</span></label>
                    <input type="email" id="email" name="email" class="courseRegister-form-control" required>
                </div>
                <div class="courseRegister-form-group">
                    <label for="phone">Phone</label>
                    <input type="tel" id="phone" name="phone" class="courseRegister-form-control">
                </div>
                <div class="courseRegister-form-buttons">
                    <button type="button" class="courseRegister-btn courseRegister-btn-cancel" onclick="resetAndCloseModal()">Cancel</button>
                    <button type="submit" class="courseRegister-btn courseRegister-btn-submit">Submit</button>
                </div>
            </form>
        </div>
    </div>
</div>