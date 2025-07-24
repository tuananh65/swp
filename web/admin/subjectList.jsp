<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
    <html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Dashboard</title>
        <link href="${pageContext.request.contextPath}/css/styles.css" rel="stylesheet">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/myRegistrations.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/SubjectList.css">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    </head>
    <body>
        <!-- Navigation Bar -->
        <nav>
            <jsp:include page="/default/header.jsp"/>
        </nav>

        <div class="banner">
            <img src="${pageContext.request.contextPath}/images/Banner.jpg" alt="Banner">
            <div class="banner-text">
                <h1 class="banner-title">Subject List</h1>
                <div class="banner-breadcrumb">
                    <table>
                        <tr>
                            <td>Admin</td>
                            <td>Dashboard</td>
                        </tr>
                    </table>
                </div>
            </div>
        </div>


            <!-- Nút toggle bên cạnh màn hình -->
        <!-- Nút tròn cố định bên trái giữa màn hình -->
        <button id="toggleMenuBtn" style="
            position: fixed;
            top: 50%;
            left: 0;
            transform: translateY(-50%);
            z-index: 1000;
            width: 50px;
            height: 50px;
            background-color: #6366f1;
            color: #ffffff;
            border: none;
            border-radius: 50%;
            font-size: 1.5rem;
            cursor: pointer;
            box-shadow: 0 4px 6px rgba(0,0,0,0.1);
            display: flex;
            align-items: center;
            justify-content: center;
        "> <i class="bi bi-list"></i>
        </button>

        <!-- Container menu -->
        <div id="courseDropdownContainer" style="
            display: none;
            position: fixed;
            top: 50%;
            left: 60px;
            transform: translateY(-50%);
            background-color: #ffffff;
            box-shadow: 0 10px 15px -3px rgba(0,0,0,0.1), 0 4px 6px -4px rgba(0,0,0,0.1);
            padding: 1rem;
            border-radius: 1rem;
            z-index: 999;
            width: 280px;
            max-height: 80vh;
            overflow-y: auto;
            font-family: 'Inter', sans-serif;
        ">

            <div id="userSection" style="display: flex; flex-direction: column; gap: 1.5rem;">
                <div>
                    <button class="dropdown-toggle" onclick="toggleDropdown(this)">
                        <a href="${pageContext.request.contextPath}/view/changePassword.jsp">Change Password</a>
                    </button>
                </div>
                <div>
                    <button class="dropdown-toggle" onclick="toggleDropdown(this)">
                        <a href="${pageContext.request.contextPath}/profile?id=${sessionScope.currentUser.userId}">Profile</a>
                    </button>
                </div>
                <div>
                    <button class="dropdown-toggle" onclick="toggleDropdown(this)">
                        <a href="${pageContext.request.contextPath}/sliderlist">Slider List</a>
                    </button>
                </div>

                <div>
                    <button class="dropdown-toggle" onclick="toggleDropdown(this)">
                        <a href="${pageContext.request.contextPath}/settinglist">Setting List</a>
                    </button>
                </div>  

                <div>
                    <button class="dropdown-toggle" onclick="toggleDropdown(this)">
                        <a href="${pageContext.request.contextPath}/admin/registrations">Registation List</a>
                    </button>
                </div> 

                <div>
                    <button class="dropdown-toggle" onclick="toggleDropdown(this)">
                        <a href="${pageContext.request.contextPath}/auth?action=logout">Sign out</a>
                    </button>
                </div> 
            </div>
        </div>

        <!-- CSS -->
        <style>
            .dropdown-toggle {
                background-color: #6366f1;
                color: #111827;
                border: none;
                padding: 0.75rem 1rem;
                border-radius: 0.75rem;
                cursor: pointer;
                font-size: 1rem;
                width: 100%;
                text-align: left;
                transition: background-color 250ms;
                box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
            }

            .dropdown-toggle:hover {
                background-color: #f3f4f6;
            }
            .subjectListViewBtn {
    background-color: #10b981; /* xanh ngọc */
    color: white;
    border: none;
    padding: 0.5rem 0.75rem;
    border-radius: 0.375rem;
    font-size: 0.875rem;
    cursor: pointer;
    margin-left: 0.25rem;
    transition: background-color 0.2s ease-in-out;
}

.subjectListViewBtn:hover {
    background-color: #059669;
}

        </style>

        <!-- JavaScript -->
        <script>
            const toggleMenuBtn = document.getElementById('toggleMenuBtn');
            const menuContainer = document.getElementById('courseDropdownContainer');
            const searchInput = document.getElementById('searchInput');
            const searchResult = document.getElementById('searchResult');
            const manualDropdowns = document.getElementById('manualDropdowns');
            const noResultsMsg = document.getElementById('noResultsMsg');

            toggleMenuBtn.addEventListener('click', () => {
                const isVisible = menuContainer.style.display === 'block';
                menuContainer.style.display = isVisible ? 'none' : 'block';

                if (isVisible) {
                    searchInput.value = '';
                    searchResult.style.display = 'none';
                    manualDropdowns.style.display = 'flex';
                    noResultsMsg.style.display = 'none';

                    const dropdowns = menuContainer.querySelectorAll('.dropdown-list');
                    dropdowns.forEach(d => d.style.display = 'none');
                }
            });

            function toggleDropdown(button) {
                const dropdown = button.nextElementSibling;
                const isVisible = dropdown.style.display === 'flex';
                dropdown.style.display = isVisible ? 'none' : 'flex';
            }

            searchInput.addEventListener('input', function () {
                const keyword = this.value.toLowerCase().trim();
                const allCourses = document.querySelectorAll('.all-course-item');
                searchResult.innerHTML = '';

                if (keyword === '') {
                    searchResult.style.display = 'none';
                    noResultsMsg.style.display = 'none';
                    manualDropdowns.style.display = 'flex';
                    return;
                }

                manualDropdowns.style.display = 'none';
                searchResult.style.display = 'flex';

                let foundCount = 0;
                allCourses.forEach(item => {
                    const text = item.textContent.toLowerCase();
                    if (text.includes(keyword)) {
                        const cloned = item.cloneNode(true);
                        cloned.style.display = 'block';
                        searchResult.appendChild(cloned);
                        foundCount++;
                    }
                });

                // Hiển thị thông báo nếu không tìm thấy kết quả
                noResultsMsg.style.display = foundCount === 0 ? 'block' : 'none';
            });
        </script> 

        <!-- Main Content -->
        <main class="subjectListMain">
            <div class="subjectListContainer">
                <!-- Filter and Search Section -->
                <div class="subjectListFilterSection">
                    <form action="subjectList" method="get" class="subjectListFilterForm">
                        <input type="hidden" name="action" value="list">
                        <div class="subjectListFilterRow">
                            <div class="subjectListInputGroup">
                                <input type="text" name="search" placeholder="Subject name" class="subjectListInput" value="${param.search}">
                            </div>
                            <div class="subjectListSelectGroup">
                                <select name="category" class="subjectListSelect">
                                    <option value="">All Categories</option>
                                    <c:forEach items="${categories}" var="category">
                                        <option value="${category}" ${category == param.category ? 'selected' : ''}>${category}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="subjectListSelectGroup">
                                <select name="status" class="subjectListSelect">
                                    <option value="">All Statuses</option>
                                    <c:forEach items="${statuses}" var="status">
                                        <option value="${status}" ${status == param.status ? 'selected' : ''}>${status}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <button type="submit" class="subjectListSearchBtn">SEARCH</button>
                            <a href="${pageContext.request.contextPath}/NewSubjectServlet" style="text-decoration: none;">
    <button type="button" class="subjectListNewBtn">NEW SUBJECT</button>
</a>
                        </div>
                    </form>
                </div>

                <!-- Subject List Table -->
                <div class="subjectListTableContainer">
                    <table class="subjectListTable">
                        <thead class="subjectListTableHeader">
                            <tr>
                                <th>SubjectID</th>
                                <th>Subject Name</th>
                                <th>Category</th>
                                <th>Number of Lessons</th>
                                <th>Owner</th>
                                <th>Status</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody class="subjectListTableBody">
                            <c:forEach items="${subjects}" var="subject">
                                <tr>
                                    <td>${subject.subjectId}</td>
                                    <td>${subject.name}</td>
                                    <td>${subject.categoryName}</td>
                                    <td>
                                        <a href="${pageContext.request.contextPath}/subjectlesson?subjectId=${subject.subjectId}" style="text-decoration: none;">
                                            ${subject.lessonCount}
                                        </a>
                                    </td>
                                    <td>${subject.ownerId}</td>
                                    <td>${subject.status}</td>
                                    <td class="subjectListActions">
                                        <a href="${pageContext.request.contextPath}/subjectDetail?subjectId=${subject.subjectId}" style="text-decoration: none;">
                                                   <button class="subjectListEditBtn">EDIT</button>
                                        </a>

                                        <a href="subjectList?action=delete&id=${subject.subjectId}" 
                                           style="text-decoration: none;"
                                           onclick="return confirm('Are you sure you want to delete this subject?')">
                                            <button class="subjectListDeleteBtn">DELETE</button>
                                        </a>
                                         <a href="${pageContext.request.contextPath}/courses-of-subject?subjectId=${subject.subjectId}" style="text-decoration: none;">
    <button class="subjectListViewBtn">VIEW COURSE</button>
</a>



                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>

                <!-- Pagination -->
                <div class="subjectListPagination">
                    <c:if test="${currentPage > 1}">
                        <a href="subjectList?action=list&page=${currentPage - 1}&category=${param.category}&status=${param.status}&search=${param.search}">
                            <button class="subjectListPageBtn">Previous</button>
                        </a>
                    </c:if>
                    <c:forEach begin="1" end="${totalPages}" var="i">
                        <a href="subjectList?action=list&page=${i}&category=${param.category}&status=${param.status}&search=${param.search}">
                            <button class="subjectListPageBtn ${i == currentPage ? 'subjectListPageActive' : ''}">${i}</button>
                        </a>
                    </c:forEach>
                    <c:if test="${currentPage < totalPages}">
                        <a href="subjectList?action=list&page=${currentPage + 1}&category=${param.category}&status=${param.status}&search=${param.search}">
                            <button class="subjectListPageBtn">Next</button>
                        </a>
                    </c:if>
                </div>
            </div>
        </main>
        <jsp:include page="/default/footer.jsp"/>            
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>