package dal;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashboardDAO extends DBContext {

    // Lấy số lượng Subject mới trong khoảng ngày và tổng số Subject
    // Trả về Map: {"newSubjects": int, "allSubjects": int}
    public Map<String, Integer> getSubjectStats(Date startDate, Date endDate) {
        Map<String, Integer> stats = new HashMap<>();
        String sql = "SELECT "
                + "COUNT(CASE WHEN CreatedAt >= ? AND CreatedAt <= ? THEN 1 END) AS NewSubjects, "
                + "COUNT(*) AS AllSubjects "
                + "FROM Subject";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, startDate);
            ps.setDate(2, endDate);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    stats.put("newSubjects", rs.getInt("NewSubjects"));
                    stats.put("allSubjects", rs.getInt("AllSubjects"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting SubjectStats: " + e.getMessage());
            stats.put("newSubjects", 0);
            stats.put("allSubjects", 0);
        }
        return stats;
    }

    // Lấy thống kê Enrollment
    // Trả về Map: {"allRegistrations": int, "successfulRegistrations": int, "cancelledRegistrations": int, "submittedRegistrations": int}
    public Map<String, Integer> getEnrollmentStats(Date startDate, Date endDate) {
        Map<String, Integer> stats = new HashMap<>();
        String sql = "SELECT "
                + "COUNT(*) AS AllRegistrations, "
                + "COUNT(CASE WHEN Status = 'Confirmed' THEN 1 END) AS SuccessfulRegistrations, " // ĐÃ SỬA ĐỔI ĐỂ BAO GỒM CONFIRMED
                + "COUNT(CASE WHEN Status = 'Cancelled' THEN 1 END) AS CancelledRegistrations, "
                + "COUNT(CASE WHEN Status = 'Submitted' THEN 1 END) AS SubmittedRegistrations "
                + "FROM Enrollment "
                + "WHERE EnrollmentDate >= ? AND EnrollmentDate <= ?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, startDate);
            ps.setDate(2, endDate);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    stats.put("allRegistrations", rs.getInt("AllRegistrations"));
                    stats.put("successfulRegistrations", rs.getInt("SuccessfulRegistrations"));
                    stats.put("cancelledRegistrations", rs.getInt("CancelledRegistrations"));
                    stats.put("submittedRegistrations", rs.getInt("SubmittedRegistrations"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting EnrollmentStats: " + e.getMessage());
            stats.put("allRegistrations", 0);
            stats.put("successfulRegistrations", 0);
            stats.put("cancelledRegistrations", 0);
            stats.put("submittedRegistrations", 0);
        }
        return stats;
    }

    // Lấy tổng doanh thu (đã bao gồm cả 'Submitted' và 'Successful')
    public BigDecimal getTotalRevenue(Date startDate, Date endDate) {
        BigDecimal totalRevenue = BigDecimal.ZERO;
        String sql = "SELECT SUM(TotalPrice) AS TotalRevenue "
                + "FROM Enrollment "
                + "WHERE EnrollmentDate >= ? AND EnrollmentDate <= ? AND (Status = 'Successful' OR Status = 'Submitted')";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, startDate);
            ps.setDate(2, endDate);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    totalRevenue = rs.getBigDecimal("TotalRevenue");
                    if (totalRevenue == null) {
                        totalRevenue = BigDecimal.ZERO;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting TotalRevenue: " + e.getMessage());
        }
        return totalRevenue;
    }

    // Lấy doanh thu theo danh mục môn học (đã bao gồm cả 'Submitted' và 'Successful')
    public List<Map<String, Object>> getRevenueBySubjectCategory(Date startDate, Date endDate) {
        List<Map<String, Object>> revenues = new ArrayList<>();
        String sql = "SELECT s.CategoryName, SUM(e.TotalPrice) AS TotalRevenue "
                + "FROM Enrollment e "
                + "JOIN Course c ON e.CourseId = c.CourseID "
                + "JOIN Subject s ON c.SubjectId = s.SubjectId "
                + "WHERE e.EnrollmentDate >= ? AND e.EnrollmentDate <= ? AND (e.Status = 'Successful' OR e.Status = 'Submitted') "
                + "GROUP BY s.CategoryName "
                + "ORDER BY TotalRevenue DESC";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, startDate);
            ps.setDate(2, endDate);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> item = new HashMap<>();
                    item.put("categoryName", rs.getString("CategoryName"));
                    item.put("totalRevenue", rs.getBigDecimal("TotalRevenue") != null ? rs.getBigDecimal("TotalRevenue") : BigDecimal.ZERO);
                    revenues.add(item);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting RevenueBySubjectCategory: " + e.getMessage());
        }
        return revenues;
    }

    // Lấy thống kê khách hàng
    public Map<String, Integer> getCustomerStats(Date startDate, Date endDate) {
        Map<String, Integer> stats = new HashMap<>();
        String newlyRegisteredSql = "SELECT COUNT(*) FROM [User] WHERE CreatedAt >= ? AND CreatedAt <= ?";
        String newlyBoughtSql = "SELECT COUNT(DISTINCT UserId) FROM Enrollment WHERE EnrollmentDate >= ? AND EnrollmentDate <= ? AND Status = 'Successful'"; // Giữ nguyên Status = 'Successful' cho Newly Bought

        try (Connection con = getConnection();
             PreparedStatement ps1 = con.prepareStatement(newlyRegisteredSql);
             PreparedStatement ps2 = con.prepareStatement(newlyBoughtSql)) {

            // For newly registered users
            ps1.setDate(1, startDate);
            ps1.setDate(2, endDate);
            try (ResultSet rs1 = ps1.executeQuery()) {
                if (rs1.next()) {
                    stats.put("newlyRegisteredCustomers", rs1.getInt(1));
                }
            }

            // For newly bought customers
            ps2.setDate(1, startDate);
            ps2.setDate(2, endDate);
            try (ResultSet rs2 = ps2.executeQuery()) {
                if (rs2.next()) {
                    stats.put("newlyBoughtCustomers", rs2.getInt(1));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting CustomerStats: " + e.getMessage());
            stats.put("newlyRegisteredCustomers", 0);
            stats.put("newlyBoughtCustomers", 0);
        }
        return stats;
    }

    // Lấy xu hướng đơn hàng theo ngày
    public List<Map<String, Object>> getOrderTrend(Date startDate, Date endDate) {
        List<Map<String, Object>> trends = new ArrayList<>();
        String sql = "WITH Dates AS (\n" +
                     "    SELECT CAST(? AS DATE) AS TrendDate\n" +
                     "    UNION ALL\n" +
                     "    SELECT DATEADD(day, 1, TrendDate)\n" +
                     "    FROM Dates\n" +
                     "    WHERE TrendDate < CAST(? AS DATE)\n" +
                     ")\n" +
                     "SELECT d.TrendDate AS Date, \n" +
                     "        COUNT(e.EnrollmentId) AS AllOrders, \n" +
                     "        COUNT(CASE WHEN e.Status = 'Submitted' THEN 1 END) AS SubmittedOrdersCount\n" + // ĐÃ THAY ĐỔI Ở ĐÂY để đếm Submitted
                     "FROM Dates d\n" +
                     "LEFT JOIN Enrollment e ON d.TrendDate = CAST(e.EnrollmentDate AS DATE)\n" +
                     "GROUP BY d.TrendDate\n" +
                     "ORDER BY d.TrendDate\n" +
                     "OPTION (MAXRECURSION 0);";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, startDate);
            ps.setDate(2, endDate);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> item = new HashMap<>();
                    item.put("date", rs.getDate("Date"));
                    item.put("allOrders", rs.getInt("AllOrders"));
                    // Lấy giá trị từ cột mới "SubmittedOrdersCount" và đặt vào key "submittedOrders"
                    item.put("submittedOrders", rs.getInt("SubmittedOrdersCount"));
                    trends.add(item);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting OrderTrend: " + e.getMessage());
        }
        return trends;
    }

    public static void main(String[] args) {
        DashboardDAO dao = new DashboardDAO();
        LocalDate today = LocalDate.now(); //
        LocalDate sevenDaysAgo = today.minusDays(7); //
        LocalDate thirtyDaysAgo = today.minusDays(30); //

        java.sql.Date sqlToday = java.sql.Date.valueOf(today); //
        java.sql.Date sqlSevenDaysAgo = java.sql.Date.valueOf(sevenDaysAgo); //
        java.sql.Date sqlThirtyDaysAgo = java.sql.Date.valueOf(thirtyDaysAgo); //

        System.out.println("--- Subject Stats (Last 30 Days) ---"); //
        Map<String, Integer> ss = dao.getSubjectStats(sqlThirtyDaysAgo, sqlToday); //
        System.out.println("New Subjects: " + ss.get("newSubjects")); //
        System.out.println("All Subjects: " + ss.get("allSubjects")); //

        System.out.println("\n--- Enrollment Stats (Last 30 Days) ---"); //
        Map<String, Integer> es = dao.getEnrollmentStats(sqlThirtyDaysAgo, sqlToday); //
        System.out.println("All Registrations: " + es.get("allRegistrations")); //
        System.out.println("Successful: " + es.get("successfulRegistrations")); //
        System.out.println("Cancelled: " + es.get("cancelledRegistrations")); //
        System.out.println("Submitted: " + es.get("submittedRegistrations")); //

        System.out.println("\n--- Total Revenue (Last 30 Days) ---"); //
        BigDecimal tr = dao.getTotalRevenue(sqlThirtyDaysAgo, sqlToday); //
        System.out.println("Total Revenue: " + tr); //

        System.out.println("\n--- Revenue By Category (Last 30 Days) ---"); //
        List<Map<String, Object>> rbc = dao.getRevenueBySubjectCategory(sqlThirtyDaysAgo, sqlToday); //
        rbc.forEach(item -> System.out.println(item.get("categoryName") + ": " + item.get("totalRevenue"))); //

        System.out.println("\n--- Customer Stats (Last 30 Days) ---"); //
        Map<String, Integer> cs = dao.getCustomerStats(sqlThirtyDaysAgo, sqlToday); //
        System.out.println("Newly Registered: " + cs.get("newlyRegisteredCustomers")); //
        System.out.println("Newly Bought: " + cs.get("newlyBoughtCustomers")); //

        System.out.println("\n--- Order Trend (Last 7 Days) ---"); //
        List<Map<String, Object>> ot = dao.getOrderTrend(sqlSevenDaysAgo, sqlToday); //
        ot.forEach(item -> System.out.println(item.get("date") + ": All=" + item.get("allOrders") + ", Submitted=" + item.get("submittedOrders"))); //
    }
}