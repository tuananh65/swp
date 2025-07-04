<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<jsp:include page="/default/header.jsp">
    <jsp:param name="title" value="POST DETAIL"/>
</jsp:include>

<jsp:include page="/default/banner.jsp">
    <jsp:param name="bannerTitle" value="POST DETAIL"/>
</jsp:include>

<jsp:include page="/default/sidebar.jsp" />

<body>
    

    <h2 class="page-title-animate">${post != null ? 'Edit Post' : 'Add New Post'} </h2>


    <main class="post-detail-main">



        <!-- Messages -->
        <c:if test="${not empty message}">
            <div class="p-4 bg-green-50 text-green-700 rounded-lg border border-green-200 shadow-sm">${message}</div>
        </c:if>
        <c:if test="${not empty error}">
            <div class="p-4 bg-red-50 text-red-700 rounded-lg border border-red-200 shadow-sm">${error}</div>
        </c:if>

        <!-- Form + Preview -->


        <div class="post-detail-flex">

            <!-- LEFT FORM -->
            <form action="edit-post" method="post" enctype="multipart/form-data" class="post-detail-form">
                <input type="hidden" name="id" value="${post != null ? post.id : ''}" />
                <input type="hidden" name="existingThumbnail" value="${post != null ? post.thumbnail : ''}" />

                <!-- Title -->
                <div class="p-6 bg-gray-50 border border-gray-200 rounded-xl space-y-4 shadow-sm" >
                    <label class="block text-sm font-semibold text-gray-700">Title *</label>
                    <input type="text" name="title" required placeholder="Enter post title"
                           value="${post != null ? post.title : ''}"
                           class="w-full border border-gray-300 rounded-lg px-4 py-2.5 focus:ring-2 focus:ring-blue-500 focus:border-blue-500 bg-white shadow-sm">
                </div>

                <!-- Thumbnail -->
                <div class="p-6 bg-gray-50 border border-gray-200 rounded-xl space-y-4 shadow-sm" >
                    <label class="block text-sm font-semibold text-gray-700">Thumbnail (Upload Image)</label>
                    <input type="file" name="thumbnailFile" id="thumbnailFileInput" accept="image/*"
                           class="w-full border border-gray-300 rounded-lg px-4 py-2.5 bg-white shadow-sm file:mr-4 file:py-2 file:px-4 file:rounded-lg file:border-0 file:text-sm file:font-semibold file:bg-blue-50 file:text-blue-700 hover:file:bg-blue-100">
                    <img id="thumbnail-preview"
                         src="${post != null && post.thumbnail != '' ? pageContext.request.contextPath.concat('/').concat(post.thumbnail) : ''}"
                         alt="Thumbnail Preview"
                         class="rounded-lg border border-gray-200 max-h-64 object-cover ${post != null && post.thumbnail != '' ? '' : 'hidden'} shadow-sm">
                   

                </div>

                <!-- Category -->
                <div class="p-6 bg-gray-50 border border-gray-200 rounded-xl space-y-4 shadow-sm" >
                    <label class="block text-sm font-semibold text-gray-700">Category</label>
                    <input type="text" name="category" required placeholder="Enter category"
                           value="${post != null ? post.category : ''}"
                           class="w-full border border-gray-300 rounded-lg px-4 py-2.5 focus:ring-2 focus:ring-blue-500 focus:border-blue-500 bg-white shadow-sm">
                </div>

                <!-- Brief Info -->
                <div class="p-6 bg-gray-50 border border-gray-200 rounded-xl space-y-4 shadow-sm" >
                    <label class="block text-sm font-semibold text-gray-700">Brief Info</label>
                    <textarea name="briefInfo" rows="2" required placeholder="Short description"
                              class="w-full border border-gray-300 rounded-lg px-4 py-2.5 focus:ring-2 focus:ring-blue-500 bg-white shadow-sm">${post != null ? post.briefInfo : ''}</textarea>
                </div>

                <!-- Content -->
                <div class="p-6 bg-gray-50 border border-gray-200 rounded-xl space-y-4 shadow-sm" >
                    <label class="block text-sm font-semibold text-gray-700">Content</label>
                    <textarea name="content" id="editor-content" rows="10" placeholder="Post content here..."
                              class="w-full border border-gray-300 rounded-lg px-4 py-2.5 focus:ring-2 focus:ring-blue-500 bg-white shadow-sm">${post != null ? post.content : ''}</textarea>
                </div>

                <!-- Sidebar fields -->
                <div class="grid grid-cols-1 sm:grid-cols-2 gap-6">
                    <div class="p-6 bg-gray-50 border border-gray-200 rounded-xl space-y-4 shadow-sm" >
                        <label class="block text-sm font-semibold text-gray-700">Author</label>
                        <input type="text" name="author" required placeholder="Author name"
                               value="${post != null ? post.author : ''}"
                               class="w-full border border-gray-300 rounded-lg px-4 py-2.5 focus:ring-2 focus:ring-blue-500 bg-white shadow-sm">
                    </div>

                    <div class="p-6 bg-gray-50 border border-gray-200 rounded-xl space-y-4 shadow-sm">
                        <label class="block text-sm font-semibold text-gray-700">Status</label>
                        <select name="status" class="w-full border border-gray-300 rounded-lg px-4 py-2.5 focus:ring-2 focus:ring-blue-500 bg-white shadow-sm">
                            <option value="draft" ${post != null && post.status == 'draft' ? 'selected' : ''}>Draft</option>
                            <option value="published" ${post != null && post.status == 'published' ? 'selected' : ''}>Published</option>
                            <option value="hidden" ${post != null && post.status == 'hidden' ? 'selected' : ''}>Hidden</option>
                        </select>
                    </div>
                </div>

                <!-- Featured + Submit -->
                <div class="flex items-center gap-4">
                    <label class="flex items-center gap-2">
                        <input type="checkbox" name="featured" id="featured"
                               ${post != null && post.featured ? 'checked' : ''}
                               class="h-4 w-4 border-gray-300 rounded text-blue-600 focus:ring-blue-500">
                        <span class="text-sm font-semibold text-gray-700">Featured</span>
                    </label>
                    <button type="submit" class="bg-blue-600 text-white px-6 py-2.5 rounded-lg hover:bg-blue-700 transition-all shadow-sm">💾 Save</button>
                    <a href="post-list" class="bg-gray-200 text-gray-700 px-6 py-2.5 rounded-lg hover:bg-gray-300 transition-all shadow-sm">← Back to List</a>
                </div>

            </form>

            <!-- RIGHT PREVIEW -->
            <div id="preview-panel"
                 data-title="${post != null ? fn:escapeXml(post.title) : ''}"
                 data-category="${post != null ? fn:escapeXml(post.category) : ''}"
                 data-briefinfo="${post != null ? fn:escapeXml(post.briefInfo) : ''}"
                 data-content="${post != null ? fn:escapeXml(post.content) : ''}"
                 data-thumbnail="${post != null && post.thumbnail != '' ? pageContext.request.contextPath.concat('/').concat(post.thumbnail) : ''}"
                 class="post-detail-preview preview-panel">
                <h4>Preview</h4>
                <h4 id="preview-title">[Title Preview]</h4>
                <img id="preview-thumbnail" src="" alt="Thumbnail">
                <div id="preview-category">[Category]</div>
                <div id="preview-briefInfo">[Brief Info]</div>
                <div id="preview-content">[Content Preview]</div>
            </div>

        </div>

    </main>

    <jsp:include page="/chatbot/chatbot-widget.jsp" />
    <jsp:include page="/default/footer.jsp" />

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/edit-post.css" />

    <!-- TinyMCE -->
    <script src="https://cdn.tiny.cloud/1/36py1xd6bb37loxf3wign8es6k1h8p159yg8scey7ecq4rd8/tinymce/7/tinymce.min.js" referrerpolicy="origin"></script>

    <script>
       // ==========================
// 1. KHỞI TẠO TRÌNH SOẠN THẢO TINYMCE
// ==========================
tinymce.init({
    selector: '#editor-content', // chọn textarea có id này
    height: 500,
    plugins: 'print preview paste autolink save code visualblocks fullscreen image link media table charmap lists',
    toolbar: 'undo redo | bold italic underline | alignleft aligncenter alignright | bullist numlist | image media link | fullscreen code',
    
    // Khi nội dung thay đổi trong editor → cập nhật sang phần preview
    setup: function (editor) {
        editor.on('keyup change', function () {
            document.getElementById('preview-content').innerHTML = editor.getContent();
        });
    }
});


// ==========================
// 2. KHI TRANG ĐƯỢC TẢI HOÀN TOÀN
// ==========================
document.addEventListener('DOMContentLoaded', function () {

    // --- 2.1 Đồng bộ realtime khi người dùng nhập vào các input ---
    
    // Cập nhật title ngay khi người dùng gõ
    document.querySelector('input[name="title"]').addEventListener('input', function () {
        document.getElementById('preview-title').textContent = this.value;
    });

    // Cập nhật category
    document.querySelector('input[name="category"]').addEventListener('input', function () {
        document.getElementById('preview-category').textContent = this.value;
    });

    // Cập nhật brief info
    document.querySelector('textarea[name="briefInfo"]').addEventListener('input', function () {
        document.getElementById('preview-briefInfo').textContent = this.value;
    });


    // --- 2.2 Hiển thị dữ liệu bài viết sẵn có (dùng khi edit bài viết cũ) ---

    const panel = document.getElementById('preview-panel');

    // Đọc dữ liệu bài viết từ các thuộc tính data-* gán trong HTML
    const title = panel.dataset.title;
    const category = panel.dataset.category;
    const briefInfo = panel.dataset.briefinfo;
    const content = panel.dataset.content;
    const thumbnail = panel.dataset.thumbnail;

    // Gán vào khu vực preview nếu có
    if (title)
        document.getElementById('preview-title').textContent = title;

    if (category)
        document.getElementById('preview-category').textContent = category;

    if (briefInfo)
        document.getElementById('preview-briefInfo').textContent = briefInfo;

    if (content)
        document.getElementById('preview-content').innerHTML = content;

    if (thumbnail) {
        const img = document.getElementById('preview-thumbnail');
        img.src = thumbnail;
        img.classList.remove('hidden'); // nếu ảnh bị ẩn (hidden) thì hiện lên
    }


    // --- 2.3 Xử lý khi người dùng chọn ảnh thumbnail mới từ máy tính ---
    
    const fileInput = document.getElementById('thumbnailFileInput');
const previewThumbnail = document.querySelector('#preview-panel #preview-thumbnail'); // bên phải
const leftPreviewThumbnail = document.querySelector('form #thumbnail-preview'); // bên trái

fileInput.addEventListener('change', function () {
    const file = fileInput.files[0];
    if (file && file.type.startsWith('image/')) {
        const reader = new FileReader();
        reader.onload = function (e) {
            const imageUrl = e.target.result;

            // Preview bên phải
            previewThumbnail.src = imageUrl;
            previewThumbnail.classList.remove('hidden');

            // Preview bên trái
            leftPreviewThumbnail.src = imageUrl;
            leftPreviewThumbnail.style.display = 'block'; // đề phòng bị ẩn
            leftPreviewThumbnail.classList.remove('hidden');
        };
        reader.readAsDataURL(file);
    }
});

});




    </script>
</body>
</html>
