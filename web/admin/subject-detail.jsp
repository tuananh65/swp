<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Subject Details - <c:out value="${subject.name}" default="Subject Not Found" /></title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/subject-detail.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <script src="https://cdn.tiny.cloud/1/no-api-key/tinymce/6/tinymce.min.js" referrerpolicy="origin"></script>
</head>
<body>

  <jsp:include page="/default/header.jsp"/>
  <a href="${pageContext.request.contextPath}/admin/subjectList" style="text-decoration: none;">
    <button class="back-button">← Back to Subject List</button>
</a>

<div class="main-container">
    <div class="page-header">
        <div class="header-tabs">
            <a href="${pageContext.request.contextPath}/subjectDetail?subjectId=${subject != null ? subject.subjectId : ''}" class="tab active">Overview</a>
            <a href="${pageContext.request.contextPath}/dimensionList?subjectId=${subject != null ? subject.subjectId : ''}" class="tab">Dimensions</a>
            <a href="${pageContext.request.contextPath}/pricePackageList" class="tab">Price Packages</a>
        </div>
        <div class="header-right">
            <c:choose>
                <c:when test="${not empty subject.status}">
                    <span class="badge status-${subject.status}">${subject.status}</span>
                </c:when>
                <c:otherwise>
                    <span class="badge status-Draft">Draft</span>
                </c:otherwise>
            </c:choose>
            <button class="btn-primary" id="saveBtn" ${subject == null ? 'disabled' : ''}>
                <i class="fas fa-save"></i> Save
            </button>
        </div>
    </div>

    <div class="main-content-area">
        <c:if test="${not empty errorMessage}">
            <div class="error-message">${errorMessage}</div>
        </c:if>
        <c:if test="${subject == null}">
            <div class="error-message">Subject not found with the provided ID.</div>
        </c:if>

        <form id="subjectForm" action="${pageContext.request.contextPath}/subjectDetail" method="post" enctype="multipart/form-data">
            <input type="hidden" name="id" value="${subject.subjectId}" />
            <input type="hidden" name="existingThumbnail" value="${subject.thumbnail}" />
            <input type="hidden" name="ownerId" value="<c:out value='${subject.ownerId}' default='1' />" />

            <div class="card">
                <div class="card-body">
                    <label>Subject Name *</label>
                    <input type="text" name="name" required value="${subject.name}" placeholder="Enter subject name">

                    <label>Thumbnail Image</label>
                    <input type="file" name="thumbnailFile" id="thumbnailFileInput" accept="image/*">
                    <c:if test="${not empty subject.thumbnail}">
                        <img id="thumbnail-preview" src="${pageContext.request.contextPath}${subject.thumbnail}" 
                             alt="Thumbnail Preview" style="max-height: 200px;">
                    </c:if>
                    <c:if test="${empty subject.thumbnail}">
                        <img id="thumbnail-preview" class="hidden" style="max-height: 200px;">
                    </c:if>

                    <label>Category</label>
                    <select name="categoryName">
                        <c:forEach var="category" items="${categoryList}">
                            <option value="${category}" <c:if test="${subject.categoryName == category}">selected</c:if>>${category}</option>
                        </c:forEach>
                    </select>

                    <label>Description</label>
                    <textarea name="description" id="editor-content">${subject.description}</textarea>

                    <label>Status</label>
                    <select name="status" id="formStatusSelect">
                        <option value="Published" <c:if test="${subject.status == 'Published'}">selected</c:if>>Published</option>
                        <option value="Draft" <c:if test="${subject.status == 'Draft'}">selected</c:if>>Draft</option>
                    </select>

                    <label>
                        <input type="checkbox" name="featured" <c:if test="${subject.featured}">checked</c:if>> Featured
                    </label>
                </div>
            </div>
        </form>
    </div>
</div>

<jsp:include page="/default/footer.jsp"/>

<script>
    tinymce.init({
        selector: '#editor-content',
        height: 400,
        plugins: 'link image media code',
        toolbar: 'undo redo | bold italic | alignleft aligncenter | bullist numlist | link image media | code'
    });

    document.getElementById('thumbnailFileInput').addEventListener('change', function () {
        const file = this.files[0];
        if (file && file.type.startsWith('image/')) {
            const reader = new FileReader();
            reader.onload = e => {
                const preview = document.getElementById('thumbnail-preview');
                preview.src = e.target.result;
                preview.classList.remove('hidden');
            };
            reader.readAsDataURL(file);
        }
    });

    document.getElementById('saveBtn').addEventListener('click', function (e) {
        e.preventDefault();
        tinymce.get('editor-content').save();

        const name = document.querySelector('input[name="name"]').value.trim();
        if (!name) {
            alert('Subject name is required.');
            return;
        }

        this.disabled = true;
        this.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Saving...';
        document.getElementById('subjectForm').submit();
    });

    document.getElementById('formStatusSelect').addEventListener('change', function () {
        const badge = document.querySelector('.badge');
        badge.className = 'badge status-' + this.value;
        badge.textContent = this.value;
    });
</script>
</body>
</html>
