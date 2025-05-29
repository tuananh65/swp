<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>

<html lang="en">

<head>

    <meta charset="UTF-8">

    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Soft Skills</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/login.css" />

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/UserDe.css" />

    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" />

    <style>
        .user-card {
            position: relative; /* Để định vị tuyệt đối các nút bên trong */
        }

        .action-buttons {
            position: absolute;
            top: 10px; /* Điều chỉnh vị trí dọc tùy ý */
            right: 10px; /* Điều chỉnh vị trí ngang tùy ý */
            display: flex;
            flex-direction: column; /* Xếp các nút theo chiều dọc */
            gap: 5px; /* Khoảng cách giữa các nút */
        }

        .action-buttons a {
            display: block;
            padding: 8px 15px;
            text-decoration: none;
            border: 1px solid #ccc;
            border-radius: 5px;
            background-color: #f9f9f9;
            color: #333;
            font-size: 0.9em; /* Điều chỉnh kích thước chữ nếu cần */
            text-align: center;
        }

        .action-buttons a:hover {
            background-color: #eee;
        }
    </style>

</head>

<body>

    <%@ include file="Header.jsp" %>

    <div class="user-details-container">

        <div class="banner">

            <h1>USER DETAILS</h1>

            <p><a href="#">Home</a> / User Detail</p>

        </div>

        <section class="user-details">

            <div class="user-card">

                <div class="action-buttons">
                    <a href="${pageContext.request.contextPath}/addUser">Add New</a>
                    <a href="${pageContext.request.contextPath}/editUser?id=${loggedInUser.userID}">Edit</a>
                </div>

                <img src="${pageContext.request.contextPath}/image/${loggedInUser.avatarUrl}" alt="${loggedInUser.fullName}">

                <div class="user-info">

                    <h2>${loggedInUser.fullName} <span class="status ${loggedInUser.status.toLowerCase()}">${loggedInUser.status}</span></h2>

                    <p class="role">${roleName == '1' ? 'Admin' : (roleName == '2' ? 'Teacher' : roleName)}</p>

                    <p class="gender">${loggedInUser.gender}</p>

                    <p class="contact">📞 <a href="tel:${loggedInUser.phone}">${loggedInUser.phone}</a></p>

                    <p class="location">${loggedInUser.address != null ? loggedInUser.address : "N/A"}</p>

                    <p class="email">✉️ <a href="mailto:${loggedInUser.email}">${loggedInUser.email}</a></p>

                    <%-- Đã xóa div social-icons chứa các logo --%>

                </div>

            </div>

        </section>

    </div>

    <%@ include file="Footer.jsp" %>

</body>

</html>