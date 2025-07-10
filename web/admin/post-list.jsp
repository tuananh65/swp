<!DOCTYPE html>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!-- Nhúng header chung cho trang quản trị -->
<jsp:include page="/default/header.jsp">
    <jsp:param name="title" value="POST LIST"/>
</jsp:include>

<!-- Hiển thị banner tiêu đề trang -->
<jsp:include page="/default/banner.jsp">
    <jsp:param name="bannerTitle" value="POST LIST"/>
</jsp:include>

<!-- Nhúng sidebar quản trị -->
<jsp:include page="/default/sidebar.jsp" />

<body>
    <!-- Nhúng CSS riêng cho trang danh sách bài viết -->
    <style>
        @import url('${pageContext.request.contextPath}/css/post-list.css');
    </style>

    <main class="post-list-main">

        <!-- Nút chuyển sang trang thêm mới bài viết -->
        <a href="edit-post" class="btn btn-success mb-3">Add New Post</a>

        <!-- Bảng hiển thị danh sách bài viết -->
        <table class="table table-bordered">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Title</th>
                    <th>Category</th>
                    <th>Author</th>
                    <th>Status</th>
                    <th>Featured</th>
                    <th>Updated Date</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <!-- Lặp qua danh sách bài viết và hiển thị từng dòng -->
                <c:forEach var="p" items="${posts}">
                    <tr>
                        <td>${p.id}</td>
                        <td>${p.title}</td>
                        <td>${p.category}</td>
                        <td>${p.author}</td>
                        <td>${p.status}</td>
                        <td>
                            <!-- Hiển thị trạng thái nổi bật: Yes / No -->
                            ${p.featured ? 'Yes' : 'No'}
                        </td>
                        <td>${p.updatedDate}</td>
                        <td>
                            <!-- Nút chuyển đến trang chỉnh sửa bài viết -->
                            <a href="edit-post?id=${p.id}" class="btn btn-sm btn-primary">Edit</a>
                            <!-- Có thể thêm nút Delete ở đây nếu muốn -->
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <!-- Phân trang -->
        <nav>
            <ul class="pagination">
                <!-- Lặp từ 1 đến tổng số trang để hiển thị số trang -->
                <c:forEach var="i" begin="1" end="${totalPages}">
                    <li class="page-item ${i == currentPage ? 'active' : ''}">
                        <a class="page-link" href="post-list?page=${i}">${i}</a>
                    </li>
                </c:forEach>
            </ul>
        </nav>

    </main>

    <!-- Nhúng chatbot tương tác (nếu có) -->
    <jsp:include page="/chatbot/chatbot-widget.jsp" />

    <!-- Nhúng footer chung -->
    <jsp:include page="/default/footer.jsp" />
</body>

</html>
