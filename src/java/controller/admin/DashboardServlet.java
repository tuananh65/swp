package controller.admin;

import dal.DashboardDAO;
import com.google.gson.Gson; // Đảm bảo bạn đã import Gson
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
        LocalDate defaultTrendStartDate = today.minusDays(6);
        LocalDate defaultStatsStartDate = today.minusDays(30);

        String startDateStr = request.getParameter("startDate");
        String endDateStr = request.getParameter("endDate");

        LocalDate actualTrendStartDate = defaultTrendStartDate;
        LocalDate actualStatsStartDate = defaultStatsStartDate;
        LocalDate actualEndDate = today;

        try {
            if (startDateStr != null && !startDateStr.isEmpty()) {
                actualStatsStartDate = LocalDate.parse(startDateStr);
                actualTrendStartDate = LocalDate.parse(startDateStr);
            }
            if (endDateStr != null && !endDateStr.isEmpty()) {
                actualEndDate = LocalDate.parse(endDateStr);
            }
        } catch (DateTimeParseException e) {
            System.err.println("Invalid date format. Using default dates. Error: " + e.getMessage());
        }

        Date sqlStatsStartDate = Date.valueOf(actualStatsStartDate);
        Date sqlTrendStartDate = Date.valueOf(actualTrendStartDate);
        Date sqlEndDate = Date.valueOf(actualEndDate);

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