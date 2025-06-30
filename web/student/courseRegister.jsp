<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!-- Modal Popup -->
<div id="modalOverlay" class="courseRegister-modal-overlay" onclick="closeModal(event)">
    <div class="courseRegister-modal-content" onclick="event.stopPropagation()">
        <div class="courseRegister-modal-header">
            <button class="courseRegister-close-btn" onclick="closeModal()">×</button>
            <h2 class="courseRegister-modal-title" id="modalTitle">COURSE REGISTER</h2>
        </div>
        <div class="courseRegister-modal-body">
            <c:if test="${not empty error}">
                <div class="courseRegister-error" style="color: #e17055; margin-bottom: 1rem; text-align: center;">
                    ${error}
                </div>
            </c:if>
            <form id="registrationForm" action="${pageContext.request.contextPath}/register-course" method="post">
                <input type="hidden" name="courseId" id="courseId" value="${course.courseID}" />
                <input type="hidden" name="basePrice" id="basePrice" value="${course.salePrice}" />
                <input type="hidden" name="enrollmentId" id="enrollmentId">
                <div class="courseRegister-form-group">
                    <label for="subjectName">Subject Name <span class="courseRegister-required">*</span></label>
                    <input type="text" id="subjectName" name="subjectName" class="courseRegister-form-control" value="${course.courseName}" readonly required>
                </div>
                <div class="courseRegister-form-group">
                    <label for="packageId">Package <span class="courseRegister-required">*</span></label>
                    <select id="packageId" name="packageId" class="courseRegister-form-control" onchange="updateTotalPrice()" required>
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
                    <input type="text" id="fullName" name="fullName" class="courseRegister-form-control" value="${currentUser.fullName}" ${not empty currentUser ? 'readonly' : ''} required>
                </div>
                <div class="courseRegister-form-group">
                    <label for="gender">Gender</label>
                    <select id="gender" name="gender" class="courseRegister-form-control" ${not empty currentUser ? 'disabled' : ''}>
                        <option value="">Select Gender</option>
                        <option value="male" ${currentUser.gender == 'male' ? 'selected' : ''}>Male</option>
                        <option value="female" ${currentUser.gender == 'female' ? 'selected' : ''}>Female</option>
                    </select>
                </div>
                <div class="courseRegister-form-group">
                    <label for="email">Email <span class="courseRegister-required">*</span></label>
                    <input type="email" id="email" name="email" class="courseRegister-form-control" value="${currentUser.email}" ${not empty currentUser ? 'readonly' : ''} required>
                </div>
                <div class="courseRegister-form-group">
                    <label for="phone">Phone</label>
                    <input type="tel" id="phone" name="phone" class="courseRegister-form-control" value="${currentUser.phone}" ${not empty currentUser ? 'readonly' : ''}>
                </div>
                <div class="courseRegister-form-buttons">
                    <button type="button" class="courseRegister-btn courseRegister-btn-cancel" onclick="resetAndCloseModal()">Cancel</button>
                    <button type="submit" class="courseRegister-btn courseRegister-btn-submit" id="submitButton">Submit</button>
                </div>
            </form>
        </div>
    </div>
</div>


<!-- JavaScript for modal -->
<script>
    // Modal Functions
    function openModal(enrollmentId = null) {
        const isEditMode = enrollmentId !== null;
        const modal = document.getElementById('modalOverlay');
        modal.classList.add('active');
        document.body.style.overflow = 'hidden';

        const form = document.getElementById('registrationForm');
        const title = document.getElementById('modalTitle');
        const submitButton = document.getElementById('submitButton');
        const packageSelect = document.getElementById('packageId');

        if (isEditMode) {
            title.textContent = 'Edit Registration';
            form.action = '${pageContext.request.contextPath}/student/editRegistration';
            submitButton.textContent = 'Update';

            fetch('${pageContext.request.contextPath}/student/dashboard?action=edit&id=' + enrollmentId)
                .then(response => {
                    if (!response.ok) throw new Error('Network response was not ok');
                    return response.json();
                })
                .then(data => {
                    if (data.error) {
                        alert(data.error);
                        return;
                    }
                    document.getElementById('enrollmentId').value = data.enrollmentId || '';
                    document.getElementById('courseId').value = data.courseId || '';
                    document.getElementById('basePrice').value = data.basePrice || '';
                    document.getElementById('subjectName').value = data.courseName || '';
                    document.getElementById('price').value = data.totalPrice ? data.totalPrice.toLocaleString('vi-VN') + ' VND' : '';
                    packageSelect.value = data.packageId || '';
                    ['fullName', 'gender', 'email', 'phone'].forEach(id => {
                        document.getElementById(id).setAttribute('readonly', true);
                    });
                    document.getElementById('gender').setAttribute('disabled', true);
                })
                .catch(error => {
                    console.error('Error fetching enrollment data:', error);
                    alert('Failed to load enrollment data: ' + error.message);
                });
        } else {
            title.textContent = 'COURSE REGISTER';
            form.action = '${pageContext.request.contextPath}/register-course';
            submitButton.textContent = 'Submit';
            form.reset();
            document.getElementById('price').value = '';
            ['fullName', 'gender', 'email', 'phone'].forEach(id => {
                document.getElementById(id).removeAttribute('readonly');
            });
            document.getElementById('gender').removeAttribute('disabled');
            if (document.getElementById('mainPackageId')) {
                document.getElementById('mainPackageId').value = '';
                document.getElementById('mainTotalPrice').textContent = '';
            }
            packageSelect.value = ''; // Reset package selection
        }
    }

    function closeModal(event) {
        if (event && event.target !== event.currentTarget) return;
        document.getElementById('modalOverlay').classList.remove('active');
        document.body.style.overflow = 'auto';
    }

    function resetAndCloseModal() {
        document.getElementById('registrationForm').reset();
        document.getElementById('price').value = '';
        closeModal();
    }

    function updateTotalPrice() {
        const basePriceInput = document.getElementById('basePrice');
        const packageSelect = document.getElementById('packageId');
        if (!basePriceInput || !packageSelect) return;
        const basePrice = parseFloat(basePriceInput.value);
        const selectedOption = packageSelect.options[packageSelect.selectedIndex];
        const priceModifier = parseFloat(selectedOption.getAttribute('data-modifier'));
        const priceField = document.getElementById('price');
        if (!isNaN(basePrice) && !isNaN(priceModifier)) {
            priceField.value = (basePrice * priceModifier).toLocaleString('vi-VN') + ' VND';
        } else {
            priceField.value = '';
        }
    }

    document.getElementById('packageId')?.addEventListener('change', updateTotalPrice);

    document.getElementById('registrationForm')?.addEventListener('submit', function(e) {
        const requiredFields = ['subjectName', 'packageId', 'fullName', 'email'];
        let isValid = true;
        requiredFields.forEach(field => {
            const input = document.getElementById(field);
            if (!input.value.trim() && !input.getAttribute('readonly')) {
                input.style.borderColor = '#e17055';
                isValid = false;
            } else {
                input.style.borderColor = '#e9ecef';
            }
        });
        if (!isValid) {
            e.preventDefault();
            alert('Vui lòng điền đầy đủ các trường bắt buộc!');
        }
    });

    document.addEventListener('keydown', function(e) {
        if (e.key === 'Escape') closeModal();
    });
</script>
