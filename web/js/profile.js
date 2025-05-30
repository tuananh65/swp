// Utility functions
const $ = (selector) => document.querySelector(selector)
const $$ = (selector) => document.querySelectorAll(selector)

// DOM elements
const navbar = $("#navbar")
const sidebarToggle = $("#sidebar-toggle")
const sidebar = $("#sidebar")

// Navbar scroll effect
let lastScrollY = window.scrollY

window.addEventListener("scroll", () => {
  const currentScrollY = window.scrollY

  if (currentScrollY > 100) {
    navbar.classList.add("scrolled")
  } else {
    navbar.classList.remove("scrolled")
  }

  lastScrollY = currentScrollY
})

// Sidebar functionality
class SidebarManager {
  constructor() {
    this.isOpen = false
    this.init()
  }

  init() {
    // Toggle sidebar
    sidebarToggle?.addEventListener("click", (e) => {
      e.stopPropagation()
      this.toggle()
    })

    // Close sidebar when clicking outside
    document.addEventListener("click", (e) => {
      if (this.isOpen && !sidebar?.contains(e.target) && e.target !== sidebarToggle) {
        this.close()
      }
    })

    // Prevent sidebar from closing when clicking inside
    sidebar?.addEventListener("click", (e) => {
      e.stopPropagation()
    })

    // Handle dropdown buttons
    this.initDropdowns()

    // Handle escape key
    document.addEventListener("keydown", (e) => {
      if (e.key === "Escape" && this.isOpen) {
        this.close()
      }
    })
  }

  toggle() {
    this.isOpen ? this.close() : this.open()
  }

  open() {
    sidebar?.classList.add("active")
    sidebarToggle?.classList.add("active")
    this.isOpen = true

    // Add overlay for mobile
    if (window.innerWidth <= 768) {
      this.createOverlay()
    }
  }

  close() {
    sidebar?.classList.remove("active")
    sidebarToggle?.classList.remove("active")
    this.isOpen = false
    this.removeOverlay()
  }

  createOverlay() {
    if ($(".sidebar-overlay")) return

    const overlay = document.createElement("div")
    overlay.className = "sidebar-overlay"
    overlay.style.cssText = `
            position: fixed;
            top: 0;
            left: 0;
            right: 0;
            bottom: 0;
            background: rgba(0, 0, 0, 0.5);
            z-index: 997;
            backdrop-filter: blur(4px);
        `

    overlay.addEventListener("click", () => this.close())
    document.body.appendChild(overlay)
  }

  removeOverlay() {
    const overlay = $(".sidebar-overlay")
    if (overlay) {
      overlay.remove()
    }
  }

  initDropdowns() {
    const dropdownBtns = $$(".sidebar .dropdown-btn")

    dropdownBtns.forEach((btn) => {
      btn.addEventListener("click", (e) => {
        e.stopPropagation()
        const dropdownItem = btn.closest(".dropdown-item")
        const isActive = dropdownItem.classList.contains("active")

        // Close all other dropdowns
        $$(".sidebar .dropdown-item").forEach((item) => {
          if (item !== dropdownItem) {
            item.classList.remove("active")
          }
        })

        // Toggle current dropdown
        dropdownItem.classList.toggle("active", !isActive)
      })
    })
  }
}

// Initialize sidebar when DOM is ready
document.addEventListener("DOMContentLoaded", () => {
  new SidebarManager()

  // Add custom CSS animations for sidebar overlay if needed
  const style = document.createElement("style")
  style.textContent = `
        @keyframes slideInRight {
            from {
                opacity: 0;
                transform: translateX(100%);
            }
            to {
                opacity: 1;
                transform: translateX(0);
            }
        }
        
        @keyframes slideOutRight {
            from {
                opacity: 1;
                transform: translateX(0);
            }
            to {
                opacity: 0;
                transform: translateX(100%);
            }
        }
    `
  document.head.appendChild(style)
})


