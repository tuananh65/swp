<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.User" %>

<%
    // Lấy user từ session
    User user = (User) session.getAttribute("user");
    if (user == null) {
        out.println("<!-- Không tìm thấy người dùng. Hãy đăng nhập trước. -->");
        return;
    }

    // Cho phép dùng EL như ${user.fullName}
    request.setAttribute("user", user);
%>

<!-- Link CSS -->
<link href="${pageContext.request.contextPath}/css/sidebar.css" rel="stylesheet">

<!-- Nút toggle sidebar -->
<button class="sidebar-toggle" id="sidebar-toggle" aria-label="Mở sidebar">
    <i class="fas fa-bars"></i>
</button>

<!-- Sidebar -->
<aside class="sidebar" id="sidebar">
    <div class="sidebar-content">
        <div class="user-info">
            <div class="user-avatar">
                <img src="${pageContext.request.contextPath}/${user.avatarUrl}" alt="User Avatar" />
                <div class="status-indicator"></div>
            </div>
            <div class="user-details">
                <h3>${user.fullName}</h3>
                <p><i class="fas fa-phone"></i> ${user.phone}</p>
                <p><i class="fas fa-map-marker-alt"></i> ${user.address}</p>
                <p><i class="fas fa-envelope"></i> ${user.email}</p>
            </div>
        </div>

        <nav class="sidebar-nav">
            <div class="nav-section">

                <div class="dropdown-item">
                    <button class="dropdown-btn">
                        <i class="fas fa-user"></i>
                        <span>Hồ sơ cá nhân</span>
                        <i class="fas fa-chevron-right dropdown-arrow"></i>
                    </button>
                    <div class="dropdown-content">
                        <a href="#">Thông tin cá nhân</a>
                        <a href="#">Cài đặt tài khoản</a>
                        <a href="#">Bảo mật</a>
                    </div>
                </div>

                <div class="dropdown-item">
                    <button class="dropdown-btn">
                        <i class="fas fa-book"></i>
                        <span>Khóa học của tôi</span>
                        <i class="fas fa-chevron-right dropdown-arrow"></i>
                    </button>
                    <div class="dropdown-content">
                        <a href="#">Đang học</a>
                        <a href="#">Đã hoàn thành</a>
                        <a href="#">Yêu thích</a>
                    </div>
                </div>

                <div class="dropdown-item">
                    <button class="dropdown-btn">
                        <i class="fas fa-chart-line"></i>
                        <span>Tiến độ học tập</span>
                        <i class="fas fa-chevron-right dropdown-arrow"></i>
                    </button>
                    <div class="dropdown-content">
                        <a href="#">Thống kê</a>
                        <a href="#">Chứng chỉ</a>
                        <a href="#">Thành tích</a>
                    </div>
                </div>

                <div class="dropdown-item">
                    <button class="dropdown-btn">
                        <i class="fas fa-cog"></i>
                        <span>Cài đặt</span>
                        <i class="fas fa-chevron-right dropdown-arrow"></i>
                    </button>
                    <div class="dropdown-content">
                        <a href="#">Thông báo</a>
                        <a href="#">Ngôn ngữ</a>
                        <a href="#">Giao diện</a>
                    </div>
                </div>

            </div>
        </nav>
    </div>
</aside>

<!-- Script Sidebar -->
<script>
    const sidebar = document.getElementById("sidebar");
const sidebarToggle = document.getElementById("sidebar-toggle");
const $ = (selector) => document.querySelector(selector);
const $$ = (selector) => document.querySelectorAll(selector);

class SidebarManager {
  constructor() {
    this.isOpen = false;
    this.init();
  }

  init() {
    sidebarToggle?.addEventListener("click", (e) => {
      e.stopPropagation();
      this.toggle();
    });

    document.addEventListener("click", (e) => {
      if (this.isOpen && !sidebar.contains(e.target) && e.target !== sidebarToggle) {
        this.close();
      }
    });

    sidebar.addEventListener("click", (e) => e.stopPropagation());

    document.addEventListener("keydown", (e) => {
      if (e.key === "Escape" && this.isOpen) this.close();
    });

    this.initDropdowns();
  }

  toggle() {
    this.isOpen ? this.close() : this.open();
  }

  open() {
    sidebar.classList.add("active");
    sidebarToggle.classList.add("active");
    this.isOpen = true;
    if (window.innerWidth <= 768) this.createOverlay();
  }

  close() {
    sidebar.classList.remove("active");
    sidebarToggle.classList.remove("active");
    this.isOpen = false;
    this.removeOverlay();
  }

  createOverlay() {
    if (document.querySelector(".sidebar-overlay")) return;
    const overlay = document.createElement("div");
    overlay.className = "sidebar-overlay";
    overlay.style.cssText = `
      position: fixed;
      top: 0; left: 0; right: 0; bottom: 0;
      background: rgba(0,0,0,0.5);
      z-index: 997;
      backdrop-filter: blur(4px);
    `;
    overlay.addEventListener("click", () => this.close());
    document.body.appendChild(overlay);
  }

  removeOverlay() {
    const overlay = document.querySelector(".sidebar-overlay");
    if (overlay) overlay.remove();
  }

  initDropdowns() {
    const buttons = $$(".sidebar .dropdown-btn");
    buttons.forEach((btn) => {
      btn.addEventListener("click", (e) => {
        e.stopPropagation();
        const parent = btn.closest(".dropdown-item");
        const isActive = parent.classList.contains("active");

        $$(".sidebar .dropdown-item").forEach((item) => {
          if (item !== parent) item.classList.remove("active");
        });

        parent.classList.toggle("active", !isActive);
      });
    });
  }
}

document.addEventListener("DOMContentLoaded", () => {
  new SidebarManager();
});


</script>

