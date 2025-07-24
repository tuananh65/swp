package controller.admin;

import dal.DashboardDAO;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

@WebServlet(name = "DashboardServlet", urlPatterns = {"/admin/dashboard"})
public class DashboardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        LocalDate today = LocalDate.now();
        // Đặt ngày kết thúc mặc định là hôm nay
        LocalDate defaultEndDate = today;
        // Đặt ngày bắt đầu mặc định là 1 tháng trước ngày kết thúc mặc định
        LocalDate defaultStartDate = defaultEndDate.minusMonths(1);

        String startDateStr = request.getParameter("startDate");
        String endDateStr = request.getParameter("endDate");

        LocalDate actualStatsStartDate = defaultStartDate;
        LocalDate actualTrendStartDate = defaultStartDate;
        LocalDate actualEndDate = defaultEndDate;

        try {
            boolean useDefaultDates = false; // Cờ để kiểm soát việc sử dụng ngày mặc định

            if (startDateStr != null && !startDateStr.isEmpty()) {
                LocalDate parsedStartDate = LocalDate.parse(startDateStr);
                actualStatsStartDate = parsedStartDate;
                actualTrendStartDate = parsedStartDate;
            }

            if (endDateStr != null && !endDateStr.isEmpty()) {
                LocalDate parsedEndDate = LocalDate.parse(endDateStr);
                actualEndDate = parsedEndDate;
            }

            // Kiểm tra các điều kiện để reset về ngày mặc định
            // 1. Nếu endDate không phải là ngày hôm nay VÀ người dùng đã cung cấp endDate
            // HOẶC 2. Nếu startDate lớn hơn endDate
            if ((endDateStr != null && !endDateStr.isEmpty() && !actualEndDate.isEqual(today)) || actualStatsStartDate.isAfter(actualEndDate)) {
                useDefaultDates = true;
            }

            if (useDefaultDates) {
                actualStatsStartDate = defaultStartDate;
                actualTrendStartDate = defaultStartDate;
                actualEndDate = defaultEndDate;
            }

        } catch (DateTimeParseException e) {
            System.err.println("Invalid date format. Using default dates. Error: " + e.getMessage());
            // Nếu có lỗi parse, chắc chắn quay về dùng ngày mặc định
            actualStatsStartDate = defaultStartDate;
            actualTrendStartDate = defaultStartDate;
            actualEndDate = defaultEndDate;
        }

        // Chuyển đổi sang java.sql.Date
        Date sqlStatsStartDate = Date.valueOf(actualStatsStartDate);
        Date sqlTrendStartDate = Date.valueOf(actualTrendStartDate);
        Date sqlEndDate = Date.valueOf(actualEndDate);

        // Đặt lại các thuộc tính để hiển thị trên JSP
        request.setAttribute("currentStartDate", actualStatsStartDate.toString());
        request.setAttribute("currentEndDate", actualEndDate.toString());

        DashboardDAO dao = new DashboardDAO();
        Gson gson = new Gson();

        // --- Lấy dữ liệu và đặt vào request scope ---
        // Subject Stats
        Map<String, Integer> subjectStats = dao.getSubjectStats(sqlStatsStartDate, sqlEndDate);
        request.setAttribute("newSubjects", subjectStats.getOrDefault("newSubjects", 0));
        request.setAttribute("allSubjects", subjectStats.getOrDefault("allSubjects", 0));

        // Enrollment Stats
        Map<String, Integer> enrollmentStats = dao.getEnrollmentStats(sqlStatsStartDate, sqlEndDate);
        request.setAttribute("allRegistrations", enrollmentStats.getOrDefault("allRegistrations", 0));
        request.setAttribute("successfulRegistrations", enrollmentStats.getOrDefault("successfulRegistrations", 0));
        request.setAttribute("cancelledRegistrations", enrollmentStats.getOrDefault("cancelledRegistrations", 0));
        request.setAttribute("submittedRegistrations", enrollmentStats.getOrDefault("submittedRegistrations", 0));

        // Total Revenue
        BigDecimal totalRevenue = dao.getTotalRevenue(sqlStatsStartDate, sqlEndDate);
        request.setAttribute("totalRevenue", totalRevenue);

        // Revenue by Categories (chuyển sang JSON cho Chart.js)
        List<Map<String, Object>> revenueByCategories = dao.getRevenueBySubjectCategory(sqlStatsStartDate, sqlEndDate);
        String revenueByCategoriesJson = gson.toJson(revenueByCategories);
        request.setAttribute("revenueByCategoriesJson", revenueByCategoriesJson);

        // Customer Stats
        Map<String, Integer> customerStats = dao.getCustomerStats(sqlStatsStartDate, sqlEndDate);
        request.setAttribute("newlyRegisteredCustomers", customerStats.getOrDefault("newlyRegisteredCustomers", 0));
        request.setAttribute("newlyBoughtCustomers", customerStats.getOrDefault("newlyBoughtCustomers", 0));

        // Order Trend (for Chart.js)
        List<Map<String, Object>> orderTrend = dao.getOrderTrend(sqlTrendStartDate, sqlEndDate);
        String orderTrendJson = gson.toJson(orderTrend);
        request.setAttribute("orderTrendJson", orderTrendJson);

        request.getRequestDispatcher("/admin/dashboard.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Admin Dashboard Servlet";
    }
}