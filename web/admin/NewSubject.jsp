<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>

    <meta charset="UTF-8">
    <title>Add New Subject</title>
   <style>
/* =================== GLOBAL =================== */
body {
    background: linear-gradient(135deg, #f2f7ff, #fef6ff);
    font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
    margin: 0;
    padding: 0;
}

/* =================== CONTAINER =================== */
.form-container {
    max-width: 720px;
    background-color: white;
    margin: 60px auto;
    padding: 40px;
    border-radius: 16px;
    box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
    animation: fadeIn 0.5s ease-in-out;
}

/* =================== TITLES =================== */
.form-container h2 {
    text-align: center;
    margin-bottom: 30px;
    color: #333;
    font-weight: 700;
}

/* =================== FORM INPUTS =================== */
.form-group {
    margin-bottom: 20px;
}

.form-group label {
    font-weight: 600;
    display: block;
    margin-bottom: 8px;
    color: #555;
}

.form-group input[type="text"],
.form-group input[type="number"],
.form-group select,
.form-group textarea,
.form-group input[type="file"] {
    width: 100%;
    padding: 12px 16px;
    border: 1px solid #ddd;
    border-radius: 12px;
    background-color: #fdfdfd;
    font-size: 15px;
    transition: all 0.3s ease;
}

.form-group input:focus,
.form-group textarea:focus,
.form-group select:focus {
    border-color: #8e44ad;
    box-shadow: 0 0 0 3px rgba(142, 68, 173, 0.1);
    outline: none;
}

/* =================== CHECKBOX =================== */
.form-group input[type="checkbox"] {
    transform: scale(1.2);
    margin-right: 8px;
}

/* =================== BUTTONS =================== */
.button-group {
    display: flex;
    justify-content: space-between;
    margin-top: 30px;
    flex-wrap: wrap;
    gap: 12px;
}

.create-btn,
.back-btn {
    padding: 12px 28px;
    font-weight: 600;
    border-radius: 10px;
    font-size: 15px;
    cursor: pointer;
    transition: all 0.3s ease-in-out;
    text-decoration: none;
    border: none;
}

.create-btn {
    background: linear-gradient(135deg, #9b59b6, #8e44ad);
    color: white;
}

.create-btn:hover {
    background: linear-gradient(135deg, #8e44ad, #6c3483);
    transform: scale(1.03);
}

.back-btn {
    background-color: #bdc3c7;
    color: #333;
}

.back-btn:hover {
    background-color: #aeb6bf;
    transform: scale(1.03);
}

/* =================== MESSAGES =================== */
.success-message,
.error-message {
    text-align: center;
    padding: 12px;
    border-radius: 10px;
    margin-bottom: 20px;
    font-weight: 600;
}

.success-message {
    background-color: #eafaf1;
    color: #27ae60;
    border: 1px solid #27ae60;
}

.error-message {
    background-color: #fdecea;
    color: #e74c3c;
    border: 1px solid #e74c3c;
}

/* =================== IMAGE PREVIEW =================== */
.image-preview img {
    display: block;
    margin-top: 10px;
    max-width: 100%;
    height: auto;
    border-radius: 10px;
    border: 1px solid #ccc;
}

/* =================== ANIMATIONS =================== */
@keyframes fadeIn {
    from {opacity: 0; transform: translateY(20px);}
    to {opacity: 1; transform: translateY(0);}
}
select.custom-select {
    all: unset; /* Reset lại kiểu mặc định */
    display: block;
    width: 100%;
    padding: 12px 16px;
    font-size: 15px;
    color: #333;
    background-color: #fdfdfd;
    border: 1px solid #ddd;
    border-radius: 12px;
    box-sizing: border-box;

    /* Mũi tên custom */
    background-image: url('data:image/svg+xml,%3Csvg width="14" height="10" viewBox="0 0 14 10" xmlns="http://www.w3.org/2000/svg"%3E%3Cpath fill="%23888888" d="M7 10L0 0h14z"/%3E%3C/svg%3E');
    background-repeat: no-repeat;
    background-position: right 16px center;
    background-size: 12px;
    appearance: none;
    -webkit-appearance: none;
    -moz-appearance: none;
}

select.custom-select:focus {
    border-color: #8e44ad;
    box-shadow: 0 0 0 3px rgba(142, 68, 173, 0.1);
    outline: none;
}

</style>

</head>
<body>
    <nav>
            <jsp:include page="/default/header.jsp"/>
        </nav>
   <div class="form-container">
    <h2>Create New Subject</h2>

    <%-- Thông báo --%>
<% if (request.getAttribute("success") != null) { %>
    <div class="success-message"><%= request.getAttribute("success") %></div>
<% } %>

<% if (request.getAttribute("error") != null) { %>
    <div class="error-message"><%= request.getAttribute("error") %></div>
<% } %>


    <form method="post" action="${pageContext.request.contextPath}/NewSubjectServlet" enctype="multipart/form-data">
        <div class="button-group">
        <a href="${pageContext.request.contextPath}/admin/subjectList" class="back-btn">← Back to Subject List</a>
        </div>
        <div class="form-group">
            <label for="name">Subject Name</label>
            <input type="text" id="name" name="name" required />
        </div>

        <div class="form-group">
            <label for="thumbnail">Thumbnail Image</label>
            <input type="file" id="thumbnail" name="thumbnail" accept="image/*" onchange="previewImage(event)" required />
            <div class="image-preview">
                <img id="thumbnailPreview" src="#" style="display: none;" />
            </div>
        </div>

        <div class="form-group">
            <label for="category">Category</label>
            <input type="text" id="category" name="category" required />
        </div>

        <div class="form-group">
            <label for="status">Status</label>
            <select id="status" name="status" required>
                <option value="Published">Published</option>
                <option value="Daft">Daft</option>
            </select>
        </div>

       <div class="form-group">
    <label for="ownerId">Owner (Admin)</label>
    <select id="ownerId" name="ownerId" class="custom-select" required>
        <c:forEach var="admin" items="${adminList}">
            <option value="${admin.userId}">
                ${admin.userId} - ${admin.userName}
            </option>
        </c:forEach>
    </select>
</div>



        <div class="form-group">
            <label>
                <input type="checkbox" name="featured" /> Featured
            </label>
        </div>

        <div class="form-group">
            <label for="description">Description</label>
            <textarea id="description" name="description" rows="4" required></textarea>
        </div>

        <div class="button-group">
            <button type="submit" class="create-btn">Create Subject</button>
            
        </div>
    </form>
</div>
        <jsp:include page="/default/footer.jsp"/>

      <script>
function previewImage(event) {
    var input = event.target;
    var preview = document.getElementById("thumbnailPreview");
    if (input.files && input.files[0]) {
        var reader = new FileReader();
        reader.onload = function (e) {
            preview.src = e.target.result;
            preview.style.display = "block";
        };
        reader.readAsDataURL(input.files[0]);
    }
}
</script>

</body>
</html>
