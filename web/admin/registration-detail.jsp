<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<jsp:include page="/default/header.jsp">
    <jsp:param name="title" value="REGISTRATION LIST"/>
</jsp:include>

<jsp:include page="/default/banner.jsp">
    <jsp:param name="bannerTitle" value="REGISTRATION LIST"/>
</jsp:include>

<jsp:include page="/default/sidebar.jsp" />

<body class="bg-gradient-to-br from-gray-50 to-white font-inter text-gray-800 overflow-x-hidden">


    <style>
/*        @import url('https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css');*/

        /* Custom animations */
        @keyframes slideInFromLeft {
            from {
                transform: translateX(-100%);
                opacity: 0;
            }
            to {
                transform: translateX(0);
                opacity: 1;
            }
        }

        @keyframes fadeInUp {
            from {
                transform: translateY(20px);
                opacity: 0;
            }
            to {
                transform: translateY(0);
                opacity: 1;
            }
        }

        @keyframes pulseGlow {
            0% {
                box-shadow: 0 0 0 0 rgba(59, 130, 246, 0.4);
            }
            70% {
                box-shadow: 0 0 0 10px rgba(59, 130, 246, 0);
            }
            100% {
                box-shadow: 0 0 0 0 rgba(59, 130, 246, 0);
            }
        }

        .animate-slide-in {
            animation: slideInFromLeft 0.6s ease-out;
        }

        .animate-fade-in-up {
            animation: fadeInUp 0.5s ease-out;
        }

        .animate-pulse-glow {
            animation: pulseGlow 2s infinite;
        }

        /* Fine-tuned styles */
        main.container {
            border-color: rgba(209, 213, 219, 0.2);
        }

        main .form-input:focus,
        main .form-select:focus,
        main .form-textarea:focus {
            border-color: #3b82f6;
            box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.2);
        }

        main .btn-primary:hover {
            filter: brightness(1.1);
        }

        main .btn-secondary:hover {
            filter: brightness(1.1);
        }
    </style>

    <main class="container mx-auto mt-12 mb-12 p-8 max-w-5xl bg-white/95 rounded-2xl shadow-2xl border border-gray-100/50 transform transition-all duration-500 hover:scale-102 hover:shadow-blue-500/20">
        <c:if test="${empty enrollment}"><p class="text-red-600 font-medium animate-fade-in-up">enrollment is NULL</p></c:if>
        <c:if test="${empty user}"><p class="text-red-600 font-medium animate-fade-in-up">user is NULL</p></c:if>
        <c:if test="${empty packageList}"><p class="text-red-600 font-medium animate-fade-in-up">packageList is NULL</p></c:if>

        <c:if test="${not empty error}">
            <div class="bg-red-50/80 text-red-700 border-l-4 border-red-400 p-5 rounded-r-xl mb-8 shadow-lg animate-fade-in-up">
                ${error}
            </div>
        </c:if>

        <c:if test="${param.success eq 'true'}">
            <div class="bg-green-50/80 text-green-700 border-l-4 border-green-400 p-5 rounded-r-xl mb-8 shadow-lg animate-fade-in-up">
                ✅ Cập nhật thành công!
                <c:if test="${param.created eq 'true'}">
                    <br>🎉 Tài khoản học viên đã được tạo và email thông tin đăng nhập đã được gửi!
                </c:if>
            </div>
        </c:if>

        <h2 class="text-4xl font-extrabold text-gray-900 mb-10 pl-6 border-l-8 bg-gradient-to-r from-blue-600 to-purple-600 bg-clip-text text-transparent flex items-center gap-4 animate-slide-in">
            <i class="bi bi-pencil-square text-3xl"></i> Chỉnh sửa Đăng ký
        </h2>

        <form method="post" action="edit-registration" class="grid grid-cols-2 gap-8">
            <input type="hidden" name="enrollmentId" value="${enrollment.enrollmentId}" />

            <!-- Thông tin người dùng -->
            <div class="flex items-center gap-6 mb-8 animate-fade-in-up delay-100">
                <label class="w-1/3 text-blue-900 font-semibold text-lg leading-tight">Full Name: </label>
                <input type="text" class="flex-1 form-input border border-gray-300 rounded-2xl p-4 bg-gray-50 text-gray-800 focus:border-blue-500 focus:ring-2 focus:ring-blue-200 focus:bg-white transition-all duration-400" value="${user.fullName}" disabled />

            </div>
            <div class="flex items-center gap-6 mb-8 animate-fade-in-up delay-200">
                <label class="w-1/3 text-blue-900 font-semibold text-lg leading-tight">Email:</label>
                <input type="email" class="flex-1 form-input border border-gray-300 rounded-2xl p-4 bg-gray-50 text-gray-800 focus:border-blue-500 focus:ring-2 focus:ring-blue-200 focus:bg-white transition-all duration-400" value="${user.email}" disabled />

            </div>
            <div class="flex items-center gap-6 mb-8 animate-fade-in-up delay-300">
                <label class="w-1/3 text-blue-900 font-semibold text-lg leading-tight">Gender: </label>
                <select class="flex-1 form-select border border-gray-300 rounded-2xl p-4 bg-gray-50 text-gray-800 focus:border-blue-500 focus:ring-2 focus:ring-blue-200 focus:bg-white transition-all duration-400" name="gender">
                    <option value="Male" ${user.gender == 'Male' ? 'selected' : ''}>Male</option>
                    <option value="Female" ${user.gender == 'Female' ? 'selected' : ''}>Female</option>
                    <option value="Other" ${user.gender == 'Other' ? 'selected' : ''}>Other</option>
                </select>
            </div>
            <div class="flex items-center gap-6 mb-8 animate-fade-in-up delay-400">
                <label class="w-1/3 text-blue-900 font-semibold text-lg leading-tight">Phone:</label>
                <input type="text" class="flex-1 form-input border border-gray-300 rounded-2xl p-4 bg-gray-50 text-gray-800 focus:border-blue-500 focus:ring-2 focus:ring-blue-200 focus:bg-white transition-all duration-400" name="phone" value="${user.phone}" />
            </div>

            <!-- Thông tin đăng ký -->
            <div class="flex items-center gap-6 mb-8 animate-fade-in-up delay-500">
                <label class="w-1/3 text-blue-900 font-semibold text-lg leading-tight">Course:</label>
                <input type="text" class="flex-1 form-input border border-gray-300 rounded-2xl p-4 bg-gray-50 text-gray-800 focus:border-blue-500 focus:ring-2 focus:ring-blue-200 focus:bg-white transition-all duration-400" value="${course.courseName}" disabled />

            </div>
            <div class="flex items-center gap-6 mb-8 animate-fade-in-up delay-600">
                <label class="w-1/3 text-blue-900 font-semibold text-lg leading-tight">Status:</label>
                <select class="flex-1 form-select border border-gray-300 rounded-2xl p-4 bg-gray-50 text-gray-800 focus:border-blue-500 focus:ring-2 focus:ring-blue-200 focus:bg-white transition-all duration-400" name="status">
                    <option value="Pending" ${enrollment.status == 'Pending' ? 'selected' : ''}>Pending</option>
                    <option value="Approved" ${enrollment.status == 'Approved' ? 'selected' : ''}>Approved</option>
                    <option value="Rejected" ${enrollment.status == 'Rejected' ? 'selected' : ''}>Rejected</option>
                </select>
            </div>
            <div class="flex items-center gap-6 mb-8 animate-fade-in-up delay-700">
                <label class="w-1/3 text-blue-900 font-semibold text-lg leading-tight">Package:</label>
                <select class="flex-1 form-select border border-gray-300 rounded-2xl p-4 bg-gray-50 text-gray-800 focus:border-blue-500 focus:ring-2 focus:ring-blue-200 focus:bg-white transition-all duration-400" name="packageId" id="packageId">
                    <c:forEach var="pkg" items="${packageList}">
                        <option value="${pkg.packageId}"
                                data-duration="${pkg.durationInDays}"
                                ${pkg.packageId == enrollment.packageId ? 'selected' : ''}>
                            ${pkg.name} (${pkg.durationInDays} days)
                        </option>
                    </c:forEach>
                </select>
            </div>
            <div class="flex items-center gap-6 mb-8 animate-fade-in-up delay-800">
                <label class="w-1/3 text-blue-900 font-semibold text-lg leading-tight">From:</label>
                <input type="date" class="flex-1 form-input border border-gray-300 rounded-2xl p-4 bg-gray-50 text-gray-800 focus:border-blue-500 focus:ring-2 focus:ring-blue-200 focus:bg-white transition-all duration-400" name="validFrom" id="validFrom" value="<fmt:formatDate value='${enrollment.validFrom}' pattern='yyyy-MM-dd'/>" />
            </div>
            <div class="flex items-center gap-6 mb-8 animate-fade-in-up delay-900">
                <label class="w-1/3 text-blue-900 font-semibold text-lg leading-tight">To:</label>
                <input type="date" class="flex-1 form-input border border-gray-300 rounded-2xl p-4 bg-gray-50 text-gray-800 focus:border-blue-500 focus:ring-2 focus:ring-blue-200 focus:bg-white transition-all duration-400" name="validTo" id="validTo" value="<fmt:formatDate value='${enrollment.validTo}' pattern='yyyy-MM-dd'/>" />
            </div>
            <div class="col-span-2">
                <label class="text-blue-900 font-semibold text-lg mb-3 block">Note:</label>
                <textarea class="w-full form-textarea border border-gray-300 rounded-2xl p-5 bg-gray-50 text-gray-800 focus:border-blue-500 focus:ring-2 focus:ring-blue-200 focus:bg-white transition-all duration-400 h-40 resize-y animate-fade-in-up delay-1000" name="note" rows="3">${enrollment.note}</textarea>
            </div>

            <div class="col-span-2 flex justify-start gap-6 mt-10">
                <button type="submit" class="btn-primary bg-gradient-to-r from-blue-600 to-purple-600 text-white px-8 py-4 rounded-2xl hover:from-blue-700 hover:to-purple-700 hover:shadow-2xl hover:-translate-y-3 transition-all duration-500 flex items-center gap-4 animate-pulse-glow">
                    <span class="text-2xl">💾</span> Save
                </button>
                <a href="registrations" class="btn-secondary bg-gray-500 text-white px-8 py-4 rounded-2xl hover:bg-gray-600 hover:shadow-2xl hover:-translate-y-3 transition-all duration-500 flex items-center gap-4">
                    ← Back to List
                </a>
            </div>
        </form>
    </main>

    <script>
        document.getElementById("validFrom").addEventListener("change", function () {
            const fromDate = new Date(this.value);
            const selectedOption = document.getElementById("packageId").selectedOptions[0];
            const duration = parseInt(selectedOption.getAttribute("data-duration"));

            if (!isNaN(fromDate) && !isNaN(duration)) {
                const toDate = new Date(fromDate);
                toDate.setDate(toDate.getDate() + duration);

                const formatted = toDate.toISOString().split('T')[0];
                document.getElementById("validTo").value = formatted;
            }
        });

        document.querySelector("form").addEventListener("submit", function (e) {
            const validFrom = new Date(document.getElementById("validFrom").value);
            const validTo = new Date(document.getElementById("validTo").value);

            // Validate ngày
            if (validTo < validFrom) {
                alert("⚠️ Ngày 'Hiệu lực đến' không thể trước 'Hiệu lực từ'. Vui lòng kiểm tra lại.");
                e.preventDefault();
                return;
            }

            // Validate số điện thoại
            const phoneInput = document.querySelector('input[name="phone"]');
            const phone = phoneInput.value.trim();
            const phoneRegex = /^\d{10}$/;

            if (!phoneRegex.test(phone)) {
                alert("⚠️ Số điện thoại không hợp lệ! Vui lòng nhập đúng 10 chữ số và không chứa ký tự khác.");
                phoneInput.focus();
                e.preventDefault(); // chặn submit
            }
        });


    </script>


    <!-- Nhúng widget chatbot đã tối ưu -->
    <jsp:include page="/chatbot/chatbot-widget.jsp" />

    <!-- Footer -->
    <jsp:include page="/default/footer.jsp" />
</body>
</html>