
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Subject Detail - <c:out value="${subject.name != null ? subject.name : 'New Subject'}" /></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/subject-detail.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <script src="https://cdn.tiny.cloud/1/no-api-key/tinymce/6/tinymce.min.js" referrerpolicy="origin"></script>
    <style>
        .header-tabs {
            display: flex;
            gap: 10px;
            margin-bottom: 20px;
        }
        .tab {
            padding: 10px 20px;
            background-color: #f0f2f5;
            border-radius: 5px;
            text-decoration: none;
            color: #333;
        }
        .tab.active {
            background-color: #e1e3e6;
            font-weight: bold;
        }
        .form-row {
            display: flex;
            gap: 20px;
            margin-bottom: 20px;
        }
        .form-group {
            flex: 1;
        }
        .post-detail-flex {
            display: flex;
            gap: 20px;
        }
        .post-detail-form {
            flex: 1;
        }
        .post-detail-preview {
            flex: 1;
            padding: 20px;
            background: #f9f9f9;
            border: 1px solid #ddd;
            border-radius: 5px;
        }
        .hidden {
            display: none;
        }
        .error-message {
            color: red;
            margin-bottom: 20px;
            padding: 10px;
            border: 1px solid red;
            border-radius: 5px;
        }
        .btn-edit {
            background-color: #007bff;
            color: white;
            padding: 8px 16px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
        }
        .btn-edit:hover {
            background-color: #0056b3;
        }
    </style>
</head>
<body>
    <header class="header">
        <div class="container">
            <div class="nav-brand">
                <img src="${pageContext.request.contextPath}/images/logo.jpg" alt="Soft Skills" class="logo">
                <span class="brand-text"></span>
            </div>
            <nav class="navbar">
                <ul class="nav-list">
                    <li><a href="${pageContext.request.contextPath}/" class="nav-link">Dashboard</a></li>
                    <li><a href="${pageContext.request.contextPath}/subjects" class="nav-link">Subjects</a></li>
                    <li><a href="#" class="nav-link active">Subject Detail</a></li>
                    <li><a href="#" class="nav-link">Settings</a></li>
                </ul>
            </nav>
            <div class="header-actions">
                <div class="user-info">
                    <span class="user-role">Admin</span>
                    <i class="fas fa-user-circle"></i>
                </div>
            </div>
        </div>
    </header>

    <div class="main-container">
        <div class="page-header">
            <div class="header-tabs">
                <a href="#" class="tab active">Overview</a>
                <a href="#" class="tab">Dimension</a>
                <a href="#" class="tab">Price Package</a>
            </div>
            <div class="header-right">
                <div class="status-badge">
                    <span class="badge status-${subject.status}">${subject.status}</span>

                </div>
                <button class="btn-primary" id="saveBtn">
                    <i class="fas fa-save"></i>
                    Save
                </button>
                <button class="btn-edit" id="editBtn" style="display: none;">
                    <i class="fas fa-edit"></i>
                    Edit
                </button>
            </div>
        </div>

        <div class="content-grid">
            <div class="main-content-area">
                <c:if test="${not empty errorMessage}">
                    <div class="error-message">${errorMessage}</div>
                </c:if>
                <div class="card">
                    <div class="card-header">
                        <h3><i class="fas fa-cog"></i> Subject Information</h3>
                    </div>
                    <div class="card-body">
                        <div class="post-detail-flex">
                            <form id="subjectForm" action="${pageContext.request.contextPath}/subjectDetail" method="post" enctype="multipart/form-data" class="post-detail-form">
                                <input type="hidden" name="id" value="${subject.subjectId}" />
                                <input type="hidden" name="existingThumbnail" value="${subject.thumbnail}" />

                                <div class="p-6 bg-gray-50 border border-gray-200 rounded-xl space-y-4 shadow-sm">
                                    <label class="block text-sm font-semibold text-gray-700">Subject Name *</label>
                                    <input type="text" name="name" required placeholder="Enter subject name"
                                           value="${subject.name != null ? subject.name : ''}"
                                           class="w-full border border-gray-300 rounded-lg px-4 py-2.5 focus:ring-2 focus:ring-blue-500 focus:border-blue-500 bg-white shadow-sm">
                                </div>

                                <div class="p-6 bg-gray-50 border border-gray-200 rounded-xl space-y-4 shadow-sm">
                                    <label class="block text-sm font-semibold text-gray-700">Thumbnail (Upload Image)</label>
                                    <input type="file" name="thumbnailFile" id="thumbnailFileInput" accept="image/*"
                                           class="w-full border border-gray-300 rounded-lg px-4 py-2.5 bg-white shadow-sm file:mr-4 file:py-2 file:px-4 file:rounded-lg file:border-0 file:text-sm file:font-semibold file:bg-blue-50 file:text-blue-700 hover:file:bg-blue-100">
                                    <img id="thumbnail-preview"
                                         src="${subject.thumbnail != '' ? pageContext.request.contextPath.concat(subject.thumbnail) : ''}"
                                         alt="Thumbnail Preview"
                                         class="rounded-lg border border-gray-200 max-h-64 object-cover ${subject.thumbnail != '' ? '' : 'hidden'} shadow-sm">
                                </div>

                                <div class="p-6 bg-gray-50 border border-gray-200 rounded-xl space-y-4 shadow-sm">
                                    <label class="block text-sm font-semibold text-gray-700">Category</label>
                                    <select name="categoryId" class="w-full border border-gray-300 rounded-lg px-4 py-2.5 focus:ring-2 focus:ring-blue-500 focus:border-blue-500 bg-white shadow-sm">
                                        <option value="1" ${subject.categoryId == 1 ? 'selected' : ''}>Programming</option>
                                        <option value="2" ${subject.categoryId == 2 ? 'selected' : ''}>Design</option>
                                        <option value="3" ${subject.categoryId == 3 ? 'selected' : ''}>Marketing</option>
                                        <option value="4" ${subject.categoryId == 4 ? 'selected' : ''}>Business</option>
                                    </select>
                                </div>

                                <div class="p-6 bg-gray-50 border border-gray-200 rounded-xl space-y-4 shadow-sm">
                                    <label class="block text-sm font-semibold text-gray-700">Description</label>
                                    <textarea name="description" id="editor-content" rows="10" placeholder="Subject description here..."
                                              class="w-full border border-gray-300 rounded-lg px-4 py-2.5 focus:ring-2 focus:ring-blue-500 bg-white shadow-sm">${subject.description != null ? subject.description : ''}</textarea>
                                </div>

                                <div class="p-6 bg-gray-50 border border-gray-200 rounded-xl space-y-4 shadow-sm">
                                    <label class="block text-sm font-semibold text-gray-700">Status</label>
                                   <select name="status" id="formStatusSelect"
        class="w-full border border-gray-300 rounded-lg px-4 py-2.5 focus:ring-2 focus:ring-blue-500 bg-white shadow-sm">
    <option value="Published" ${subject.status == 'Published' ? 'selected' : ''}>Published</option>
    <option value="Draft" ${subject.status == 'Draft' ? 'selected' : ''}>Draft</option>
</select>
                                </div>

                                <div class="flex items-center gap-4">
                                    <label class="flex items-center gap-2">
                                        <input type="checkbox" name="featured" id="featured"
                                               ${subject.featured ? 'checked' : ''}
                                               class="h-4 w-4 border-gray-300 rounded text-blue-600 focus:ring-blue-500">
                                        <span class="text-sm font-semibold text-gray-700">Featured</span>
                                    </label>
                                </div>
                            </form>

                            <div id="preview-panel"
                                 data-title="${subject.name != null ? subject.name : ''}"
                                 data-category="${subject.categoryId == 1 ? 'Programming' : subject.categoryId == 2 ? 'Design' : subject.categoryId == 3 ? 'Marketing' : subject.categoryId == 4 ? 'Business' : ''}"
                                 data-content="${subject.description != null ? subject.description : ''}"
                                 data-thumbnail="${subject.thumbnail != '' ? pageContext.request.contextPath.concat(subject.thumbnail) : ''}"
                                 class="post-detail-preview preview-panel">
                                <h4>Preview</h4>
                                <h4 id="preview-title">[Subject Name Preview]</h4>
                                <img id="preview-thumbnail" src="" alt="Thumbnail">
                                <div id="preview-category">[Category]</div>
                                <div id="preview-content">[Content Preview]</div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <jsp:include page="/default/footer.jsp"/>

    <script>
        document.addEventListener('DOMContentLoaded', () => {
            let isEditing = true;
            const saveBtn = document.getElementById('saveBtn');
            const editBtn = document.getElementById('editBtn');
            const formStatusSelect = document.getElementById('formStatusSelect');
            const subjectForm = document.getElementById('subjectForm');

            // Bật lại nút khi trang tải
            saveBtn.disabled = false;

            saveBtn.addEventListener('click', () => {
                if (isEditing) {
                    saveBtn.disabled = true; // Ngăn nhấn nhiều lần
                    subjectForm.submit();
                }
            });

            editBtn.addEventListener('click', () => {
                isEditing = true;
                toggleEditMode();
                saveBtn.style.display = 'inline-block';
                editBtn.style.display = 'none';
            });

            function toggleEditMode() {
                const formInputs = document.querySelectorAll('input:not([type="hidden"]), select, textarea');
                formInputs.forEach(input => {
                    input.disabled = !isEditing;
                });

                if (isEditing) {
                    saveBtn.querySelector('i').className = 'fas fa-save';
                    saveBtn.childNodes[2].textContent = ' Save';
                    saveBtn.classList.remove('btn-success');
                } else {
                    saveBtn.querySelector('i').className = 'fas fa-check';
                    saveBtn.childNodes[2].textContent = ' Saved Changes';
                    saveBtn.classList.add('btn-success');
                    saveBtn.style.display = 'none';
                    editBtn.style.display = 'inline-block';
                }
            }

            toggleEditMode();

            formStatusSelect.addEventListener('change', function() {
                const badge = document.querySelector('.status-badge .badge');
                badge.className = 'badge status-' + this.value;
                badge.textContent = this.value == 1 ? 'Published' : 'Draft';
            });

            tinymce.init({
                selector: '#editor-content',
                height: 500,
                plugins: 'print preview paste autolink save code visualblocks fullscreen image link media table charmap lists',
                toolbar: 'undo redo | bold italic underline | alignleft aligncenter alignright | bullist numlist | image media link | fullscreen code',
                setup: function (editor) {
                    editor.on('keyup change', function () {
                        document.getElementById('preview-content').innerHTML = editor.getContent();
                    });
                }
            });

            document.querySelector('input[name="name"]').addEventListener('input', function () {
                document.getElementById('preview-title').textContent = this.value;
            });

            document.querySelector('select[name="categoryId"]').addEventListener('change', function () {
                const categories = {1: 'Programming', 2: 'Design', 3: 'Marketing', 4: 'Business'};
                document.getElementById('preview-category').textContent = categories[this.value] || '';
            });

            document.querySelector('textarea[name="description"]').addEventListener('input', function () {
                document.getElementById('preview-content').innerHTML = tinymce.get('editor-content').getContent();
            });

            const fileInput = document.getElementById('thumbnailFileInput');
            const previewThumbnail = document.getElementById('preview-thumbnail');
            const leftPreviewThumbnail = document.querySelector('form #thumbnail-preview');

            fileInput.addEventListener('change', function () {
                const file = fileInput.files[0];
                if (file && file.type.startsWith('image/')) {
                    const reader = new FileReader();
                    reader.onload = function (e) {
                        const imageUrl = e.target.result;
                        previewThumbnail.src = imageUrl;
                        previewThumbnail.classList.remove('hidden');
                        leftPreviewThumbnail.src = imageUrl;
                        leftPreviewThumbnail.classList.remove('hidden');
                    };
                    reader.readAsDataURL(file);
                }
            });

            const panel = document.getElementById('preview-panel');
            if (panel.dataset.title) document.getElementById('preview-title').textContent = panel.dataset.title;
            if (panel.dataset.category) document.getElementById('preview-category').textContent = panel.dataset.category;
            if (panel.dataset.content) document.getElementById('preview-content').innerHTML = panel.dataset.content;
            if (panel.dataset.thumbnail) {
                previewThumbnail.src = panel.dataset.thumbnail;
                previewThumbnail.classList.remove('hidden');
            }

            const urlParams = new URLSearchParams(window.location.search);
            if (urlParams.get('saved') === 'true') {
                isEditing = false;
                toggleEditMode();
            }
        });
    </script>
</body>
</html>
