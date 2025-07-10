package controller;

import dal.EnrollmentDAO;
import dal.UserDAO;
import dto.EnrollmentDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@WebServlet("/admin/registrations")
public class ListRegistrationServlet extends HttpServlet {

    private final int DEFAULT_PAGE_SIZE = 5;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("currentUser") == null) {
            response.sendRedirect(request.getContextPath() + "/view/SignIn.jsp");
            return;
        }

        Object currentUserObj = session.getAttribute("currentUser");
        if (!(currentUserObj instanceof User)) {
            response.sendRedirect(request.getContextPath() + "/view/SignIn.jsp");
            return;
        }

        User currentUser = (User) currentUserObj;
        System.out.println("[DEBUG] currentUser: " + currentUser);
        System.out.println("[DEBUG] currentUser.getRoleId(): " + (currentUser != null ? currentUser.getRoleId() : "null"));

        if (currentUser.getRoleId() != 3) {
            response.sendRedirect(request.getContextPath() + "/view/403.jsp"); // hoặc trang lỗi riêng
            return;
        }

        int userId = currentUser.getUserId();

        try {
            // Lấy các tham số lọc
            String subject = request.getParameter("subject");
            String status = request.getParameter("status");
            String from = request.getParameter("fromDate");
            String to = request.getParameter("toDate");
            String minPriceStr = request.getParameter("minPrice");
            String maxPriceStr = request.getParameter("maxPrice");

            //  Xử lý checkbox hiển thị cột
            String[] columnParams = request.getParameterValues("cols");
            List<String> displayColumns;
            if (request.getQueryString() != null && request.getQueryString().contains("cols")) {
                displayColumns = columnParams != null ? Arrays.asList(columnParams) : new ArrayList<>();
            } else {
                displayColumns = Arrays.asList("image", "package", "updatedBy"); // mặc định
            }

            BigDecimal minPrice = null, maxPrice = null;
            try {
                if (minPriceStr != null && !minPriceStr.isEmpty()) {
                    minPrice = new BigDecimal(minPriceStr);
                }
                if (maxPriceStr != null && !maxPriceStr.isEmpty()) {
                    maxPrice = new BigDecimal(maxPriceStr);
                }
            } catch (NumberFormatException e) {
                request.setAttribute("error", "Giá trị khoảng giá không hợp lệ.");
                forwardWithAttributes(request, response);
                return;
            }

            // Parse ngày
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date fromDate = null, toDate = null;
            try {
                if (from != null && !from.isEmpty()) {
                    fromDate = new Date(sdf.parse(from).getTime());
                }
                if (to != null && !to.isEmpty()) {
                    toDate = new Date(sdf.parse(to).getTime());
                }
            } catch (ParseException e) {
                request.setAttribute("error", "Định dạng ngày không hợp lệ.");
                forwardWithAttributes(request, response);
                return;
            }

            // Phân trang
            int page = 1;
            try {
                if (request.getParameter("page") != null) {
                    page = Integer.parseInt(request.getParameter("page"));
                }
            } catch (NumberFormatException e) {
                page = 1;
            }
            int limit = DEFAULT_PAGE_SIZE;
            int offset = (page - 1) * limit;

            // Sắp xếp
            String sortColumn = Optional.ofNullable(request.getParameter("sortColumn")).orElse("EnrollmentDate");
            String sortOrder = Optional.ofNullable(request.getParameter("sortOrder")).orElse("desc");

            System.out.println(">> Đang gọi DAO để lấy danh sách đăng ký...");
            System.out.println("subject=" + subject + ", status=" + status + ", fromDate=" + fromDate + ", toDate=" + toDate);

            // Lấy dữ liệu
            EnrollmentDAO dao = new EnrollmentDAO();
            List<EnrollmentDTO> enrollments = dao.getListEnrollmentsWithFilter(
                    subject, status, fromDate, toDate,
                    minPrice, maxPrice, sortColumn, sortOrder, offset, limit
            );

            int totalRecords = dao.getTotalEnrollmentsWithFilter(
                    subject, status, fromDate, toDate, minPrice, maxPrice
            );

            int totalPages = (int) Math.ceil((double) totalRecords / limit);

            // Set attribute trả về JSP
            request.setAttribute("enrollments", enrollments);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("totalRecords", totalRecords);

            // Filter giữ lại
            request.setAttribute("subject", subject);
            request.setAttribute("status", status);
            request.setAttribute("fromDate", from);
            request.setAttribute("toDate", to);
            request.setAttribute("minPrice", minPriceStr);
            request.setAttribute("maxPrice", maxPriceStr);
            request.setAttribute("sortColumn", sortColumn);
            request.setAttribute("sortOrder", sortOrder);
            request.setAttribute("displayColumns", displayColumns);

            request.getRequestDispatcher("/admin/registration-list.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace(); // log đầy đủ trong console
            request.setAttribute("error", "Lỗi: " + e.toString()); // hiện lỗi ra trang JSP
            forwardWithAttributes(request, response);
        }
    }

    private void forwardWithAttributes(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("enrollments", Collections.emptyList());
        request.setAttribute("currentPage", 1);
        request.setAttribute("totalPages", 0);
        request.setAttribute("totalRecords", 0);
        request.getRequestDispatcher("/admin/registration-list.jsp").forward(request, response);
    }
}
