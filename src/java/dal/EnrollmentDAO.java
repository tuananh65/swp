package dal;

import dto.EnrollmentDTO;
import model.Enrollment;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import model.Course;

public class EnrollmentDAO extends DBContext {

    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    // Thêm mới một bản ghi Enrollment
    public boolean insertEnrollment(Enrollment e) {
        String sql = "INSERT INTO Enrollment (UserID, CourseID, EnrollmentDate, PackageID, TotalPrice, Status, ValidFrom, ValidTo, UpdatedByUserID, OrderID, Note) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            conn = getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, e.getUserId());
            ps.setInt(2, e.getCourseId());
            ps.setTimestamp(3, new Timestamp(e.getEnrollmentDate().getTime()));
            ps.setInt(4, e.getPackageId());
            ps.setBigDecimal(5, e.getTotalPrice());
            ps.setString(6, e.getStatus());
            ps.setDate(7, new java.sql.Date(e.getValidFrom().getTime()));
            ps.setDate(8, new java.sql.Date(e.getValidTo().getTime()));
            ps.setObject(9, e.getUpdatedByUserId(), Types.INTEGER);
            ps.setObject(10, e.getOrderId(), Types.INTEGER);
            ps.setString(11, e.getNote());

            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    // Lấy danh sách Enrollment của một User ( k có điều kiện )
    public List<Enrollment> getEnrollmentsByUserId(int userId) {
        List<Enrollment> list = new ArrayList<>();
        String sql = "SELECT * FROM Enrollment WHERE UserID = ?";
        try {
            conn = getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(extract(rs));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return list;
    }

    // Lấy tất cả lượt đăng ký
    public List<Enrollment> getAllEnrollments() {
        List<Enrollment> list = new ArrayList<>();
        String sql = "SELECT * FROM Enrollment";
        try {
            conn = getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(extract(rs));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return list;
    }

    // Tạo đối tượng Enrollment từ ResultSet
    private Enrollment extract(ResultSet rs) throws SQLException {
        Enrollment e = new Enrollment();
        e.setEnrollmentId(rs.getInt("EnrollmentID"));
        e.setUserId(rs.getInt("UserID"));
        e.setCourseId(rs.getInt("CourseID"));
        e.setEnrollmentDate(rs.getTimestamp("EnrollmentDate"));
        e.setPackageId(rs.getInt("PackageID"));
        e.setTotalPrice(rs.getBigDecimal("TotalPrice"));
        e.setStatus(rs.getString("Status"));
        e.setValidFrom(rs.getDate("ValidFrom"));
        e.setValidTo(rs.getDate("ValidTo"));
        e.setUpdatedByUserId((Integer) rs.getObject("UpdatedByUserID"));
        e.setOrderId((Integer) rs.getObject("OrderID"));
        e.setNote(rs.getString("Note"));
        return e;
    }

    public List<EnrollmentDTO> getEnrollmentDTOsByPage(int offset, int limit) {
        List<EnrollmentDTO> list = new ArrayList<>();
        String sql = "SELECT e.EnrollmentID, "
                + "       u.FullName AS UserFullName, "
                + "       u.Email AS UserEmail, "
                + "       c.CourseName, "
                + "       c.CourseThumbnail, "
                + "       p.Name AS PackageName, "
                + "       e.TotalPrice, "
                + "       e.Status, "
                + "       e.EnrollmentDate, "
                + "       e.ValidFrom, "
                + "       e.ValidTo, "
                + "       ub.FullName AS UpdatedByName, "
                + "       e.OrderID "
                + "FROM Enrollment e "
                + "JOIN [User] u ON e.UserID = u.UserID "
                + "JOIN Course c ON e.CourseID = c.CourseID "
                + "JOIN Package p ON e.PackageID = p.PackageID "
                + "LEFT JOIN [User] ub ON e.UpdatedByUserID = ub.UserID "
                + "ORDER BY e.EnrollmentDate DESC "
                + "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        try {
            conn = getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, offset);
            ps.setInt(2, limit);
            rs = ps.executeQuery();

            while (rs.next()) {
                EnrollmentDTO dto = new EnrollmentDTO();
                dto.setEnrollmentId(rs.getInt("EnrollmentID"));
                dto.setUserFullName(rs.getString("UserFullName"));
                dto.setUserEmail(rs.getString("UserEmail"));
                dto.setCourseName(rs.getString("CourseName"));
                dto.setCourseThumbnail(rs.getString("CourseThumbnail")); // 👈 thêm dòng này
                dto.setPackageName(rs.getString("PackageName"));
                dto.setTotalPrice(rs.getBigDecimal("TotalPrice"));
                dto.setStatus(rs.getString("Status"));
                dto.setEnrollmentDate(rs.getTimestamp("EnrollmentDate"));
                dto.setValidFrom(rs.getDate("ValidFrom"));
                dto.setValidTo(rs.getDate("ValidTo"));
                dto.setUpdatedByName(rs.getString("UpdatedByName"));
                dto.setOrderId(rs.getObject("OrderID") != null ? rs.getInt("OrderID") : null);

                list.add(dto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public int getTotalEnrollmentsWithFilter(
        String subject, String status,
        Date fromDate, Date toDate,
        BigDecimal minPrice, BigDecimal maxPrice) {

    StringBuilder sql = new StringBuilder(
        "SELECT COUNT(*) FROM Enrollment e "
      + "JOIN [User] u ON e.UserID = u.UserID "
      + "JOIN Course c ON e.CourseID = c.CourseID WHERE 1=1 "
    );

    List<Object> params = new ArrayList<>();

    if (subject != null && !subject.trim().isEmpty()) {
        sql.append("AND c.CourseName LIKE ? ");
        params.add("%" + subject.trim() + "%");
    }

    if (status != null && !status.trim().isEmpty()) {
        sql.append("AND e.Status = ? ");
        params.add(status.trim());
    }

    if (fromDate != null) {
        sql.append("AND e.EnrollmentDate >= ? ");
        params.add(new java.sql.Date(fromDate.getTime()));
    }

    if (toDate != null) {
        sql.append("AND e.EnrollmentDate <= ? ");
        params.add(new java.sql.Date(toDate.getTime()));
    }

    if (minPrice != null) {
        sql.append("AND e.TotalPrice >= ? ");
        params.add(minPrice);
    }

    if (maxPrice != null) {
        sql.append("AND e.TotalPrice <= ? ");
        params.add(maxPrice);
    }

    try (Connection conn = getConnection();
         PreparedStatement ps = conn.prepareStatement(sql.toString())) {

        for (int i = 0; i < params.size(); i++) {
            ps.setObject(i + 1, params.get(i));
        }

        ResultSet rs = ps.executeQuery();
        if (rs.next()) return rs.getInt(1);

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return 0;
}


// lấy danh sách có lọc 
    public List<EnrollmentDTO> getListEnrollmentsWithFilter(
        String subject, String status,
        Date fromDate, Date toDate,
        BigDecimal minPrice, BigDecimal maxPrice,
        String sortColumn, String sortOrder,
        int offset, int limit) {

    List<EnrollmentDTO> list = new ArrayList<>();

    StringBuilder sql = new StringBuilder(
        "SELECT e.EnrollmentID, "
      + "       u.FullName AS UserFullName, "
      + "       u.Email AS UserEmail, "
      + "       c.CourseName, "
      + "       c.CourseThumbnail, "
      + "       p.Name AS PackageName, "
      + "       e.TotalPrice, "
      + "       e.Status, "
      + "       e.EnrollmentDate, "
      + "       e.ValidFrom, "
      + "       e.ValidTo, "
      + "       ub.FullName AS UpdatedByName, "
      + "       e.OrderID "
      + "FROM Enrollment e "
      + "JOIN [User] u ON e.UserID = u.UserID "
      + "JOIN Course c ON e.CourseID = c.CourseID "
      + "JOIN Package p ON e.PackageID = p.PackageID "
      + "LEFT JOIN [User] ub ON e.UpdatedByUserID = ub.UserID "
      + "WHERE 1=1 "
    );

    List<Object> params = new ArrayList<>();

    if (subject != null && !subject.isEmpty()) {
        sql.append("AND c.CourseName LIKE ? ");
        params.add("%" + subject + "%");
    }

    if (status != null && !status.isEmpty()) {
        sql.append("AND e.Status = ? ");
        params.add(status);
    }

    if (fromDate != null) {
        sql.append("AND e.EnrollmentDate >= ? ");
        params.add(new java.sql.Date(fromDate.getTime()));
    }

    if (toDate != null) {
        sql.append("AND e.EnrollmentDate <= ? ");
        params.add(new java.sql.Date(toDate.getTime()));
    }

    if (minPrice != null) {
        sql.append("AND e.TotalPrice >= ? ");
        params.add(minPrice);
    }

    if (maxPrice != null) {
        sql.append("AND e.TotalPrice <= ? ");
        params.add(maxPrice);
    }

    sql.append("ORDER BY ").append(sortColumn).append(" ").append(sortOrder)
       .append(" OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");

    params.add(offset);
    params.add(limit);

    try (Connection conn = getConnection();
         PreparedStatement ps = conn.prepareStatement(sql.toString())) {

        for (int i = 0; i < params.size(); i++) {
            ps.setObject(i + 1, params.get(i));
        }

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            EnrollmentDTO dto = new EnrollmentDTO();
            dto.setEnrollmentId(rs.getInt("EnrollmentID"));
            dto.setUserFullName(rs.getString("UserFullName"));
            dto.setUserEmail(rs.getString("UserEmail"));
            dto.setCourseName(rs.getString("CourseName"));
            dto.setCourseThumbnail(rs.getString("CourseThumbnail"));
            dto.setPackageName(rs.getString("PackageName"));
            dto.setTotalPrice(rs.getBigDecimal("TotalPrice"));
            dto.setStatus(rs.getString("Status"));
            dto.setEnrollmentDate(rs.getTimestamp("EnrollmentDate"));
            dto.setValidFrom(rs.getDate("ValidFrom"));
            dto.setValidTo(rs.getDate("ValidTo"));
            dto.setUpdatedByName(rs.getString("UpdatedByName"));
            dto.setOrderId(rs.getObject("OrderID") != null ? rs.getInt("OrderID") : null);
            list.add(dto);
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return list;
}


    public Enrollment getEnrollmentById(int id) {
        String sql = "SELECT * FROM Enrollment WHERE EnrollmentID = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return extract(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateEnrollment(Enrollment e) {
    String sql = "UPDATE Enrollment SET Status = ?, PackageID = ?, ValidFrom = ?, ValidTo = ?, Note = ?, UpdatedByUserID = ? WHERE EnrollmentID = ?";
    try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, e.getStatus());
        ps.setInt(2, e.getPackageId());
        ps.setDate(3, new java.sql.Date(e.getValidFrom().getTime()));
        ps.setDate(4, new java.sql.Date(e.getValidTo().getTime()));
        ps.setString(5, e.getNote());
        ps.setObject(6, e.getUpdatedByUserId(), java.sql.Types.INTEGER); // 👈 mới
        ps.setInt(7, e.getEnrollmentId());

        return ps.executeUpdate() > 0;
    } catch (Exception ex) {
        ex.printStackTrace();
    }
    return false;
}
    
    public List<EnrollmentDTO> getEnrollmentDTOsByUserId(int userId) {
    List<EnrollmentDTO> list = new ArrayList<>();
    String sql = "SELECT e.EnrollmentID, "
            + "       u.FullName AS UserFullName, "
            + "       u.Email AS UserEmail, "
            + "       c.CourseName, "
            + "       c.CourseThumbnail, "
            + "       p.Name AS PackageName, "
            + "       e.TotalPrice, "
            + "       e.Status, "
            + "       e.EnrollmentDate, "
            + "       e.ValidFrom, "
            + "       e.ValidTo, "
            + "       ub.FullName AS UpdatedByName, "
            + "       e.OrderID "
            + "FROM Enrollment e "
            + "JOIN [User] u ON e.UserID = u.UserID "
            + "JOIN Course c ON e.CourseID = c.CourseID "
            + "JOIN Package p ON e.PackageID = p.PackageID "
            + "LEFT JOIN [User] ub ON e.UpdatedByUserID = ub.UserID "
            + "WHERE e.UserID = ? "
            + "ORDER BY e.EnrollmentDate DESC";

    try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, userId);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            EnrollmentDTO dto = new EnrollmentDTO();
            dto.setEnrollmentId(rs.getInt("EnrollmentID"));
            dto.setUserFullName(rs.getString("UserFullName"));
            dto.setUserEmail(rs.getString("UserEmail"));
            dto.setCourseName(rs.getString("CourseName"));
            dto.setCourseThumbnail(rs.getString("CourseThumbnail"));
            dto.setPackageName(rs.getString("PackageName"));
            dto.setTotalPrice(rs.getBigDecimal("TotalPrice"));
            dto.setStatus(rs.getString("Status"));
            dto.setEnrollmentDate(rs.getTimestamp("EnrollmentDate"));
            dto.setValidFrom(rs.getDate("ValidFrom"));
            dto.setValidTo(rs.getDate("ValidTo"));
            dto.setUpdatedByName(rs.getString("UpdatedByName"));
            dto.setOrderId(rs.getObject("OrderID") != null ? rs.getInt("OrderID") : null);
            list.add(dto);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return list;
}
    
    public List<EnrollmentDTO> getListEnrollmentsWithFilterByUserId(
        int userId, String category, String search,
        String subject, String status,
        Date fromDate, Date toDate,
        BigDecimal minPrice, BigDecimal maxPrice,
        String sortColumn, String sortOrder,
        int offset, int limit) {
    
    List<EnrollmentDTO> list = new ArrayList<>();
    StringBuilder sql = new StringBuilder(
            "SELECT e.EnrollmentID, " +
            "       u.FullName AS UserFullName, " +
            "       u.Email AS UserEmail, " +
            "       c.CourseName, " +
            "       c.CourseThumbnail, " +
            "       p.Name AS PackageName, " +
            "       e.TotalPrice, " +
            "       e.Status, " +
            "       e.EnrollmentDate, " +
            "       e.ValidFrom, " +
            "       e.ValidTo, " +
            "       ub.FullName AS UpdatedByName, " +
            "       e.OrderID " +
            "FROM Enrollment e " +
            "JOIN [User] u ON e.UserID = u.UserID " +
            "JOIN Course c ON e.CourseID = c.CourseID " +
            "JOIN Package p ON e.PackageID = p.PackageID " +
            "LEFT JOIN [User] ub ON e.UpdatedByUserID = ub.UserID " +
            "WHERE e.UserID = ? "
    );

    List<Object> params = new ArrayList<>();
    params.add(userId);

    if (search != null && !search.trim().isEmpty()) {
    String lowerSearch = "%" + search.trim().toLowerCase() + "%";
    sql.append("AND (")
       .append("LOWER(c.CourseName) LIKE ? OR ")
       .append("LOWER(e.Status) LIKE ? OR ")
       .append("CAST(e.EnrollmentID AS VARCHAR) LIKE ? OR ")
       .append("CAST(e.TotalPrice AS VARCHAR) LIKE ? OR ")
       .append("FORMAT(e.ValidFrom, 'yyyy-MM-dd') LIKE ? OR ")
       .append("FORMAT(e.ValidTo, 'yyyy-MM-dd') LIKE ? OR ")
       .append("LOWER(p.Name) LIKE ?")  // 👈 Thêm điều kiện tìm kiếm theo Package
       .append(") ");
    for (int i = 0; i < 7; i++) params.add(lowerSearch);
}


    if (category != null && !category.trim().isEmpty()) {
        sql.append("AND c.CourseName = ? ");
        params.add(category.trim());
    }

    if (status != null && !status.trim().isEmpty()) {
        sql.append("AND e.Status = ? ");
        params.add(status.trim());
    }

    if (fromDate != null) {
        sql.append("AND e.EnrollmentDate >= ? ");
        params.add(new java.sql.Date(fromDate.getTime()));
    }

    if (toDate != null) {
        sql.append("AND e.EnrollmentDate <= ? ");
        params.add(new java.sql.Date(toDate.getTime()));
    }

    if (minPrice != null) {
        sql.append("AND e.TotalPrice >= ? ");
        params.add(minPrice);
    }

    if (maxPrice != null) {
        sql.append("AND e.TotalPrice <= ? ");
        params.add(maxPrice);
    }

    sql.append("ORDER BY ").append(sortColumn).append(" ").append(sortOrder)
       .append(" OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");
    params.add(offset);
    params.add(limit);

    try (Connection conn = getConnection();
         PreparedStatement ps = conn.prepareStatement(sql.toString())) {

        for (int i = 0; i < params.size(); i++) {
            ps.setObject(i + 1, params.get(i));
        }

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            EnrollmentDTO dto = new EnrollmentDTO();
            dto.setEnrollmentId(rs.getInt("EnrollmentID"));
            dto.setUserFullName(rs.getString("UserFullName"));
            dto.setUserEmail(rs.getString("UserEmail"));
            dto.setCourseName(rs.getString("CourseName"));
            dto.setCourseThumbnail(rs.getString("CourseThumbnail"));
            dto.setPackageName(rs.getString("PackageName"));
            dto.setTotalPrice(rs.getBigDecimal("TotalPrice"));
            dto.setStatus(rs.getString("Status"));
            dto.setEnrollmentDate(rs.getTimestamp("EnrollmentDate"));
            dto.setValidFrom(rs.getDate("ValidFrom"));
            dto.setValidTo(rs.getDate("ValidTo"));
            dto.setUpdatedByName(rs.getString("UpdatedByName"));
            dto.setOrderId(rs.getObject("OrderID") != null ? rs.getInt("OrderID") : null);
            list.add(dto);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return list;
}
    
    public boolean deleteEnrollment(int enrollmentId, int userId) {
        String sql = "DELETE FROM Enrollment WHERE EnrollmentID = ? AND UserID = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, enrollmentId);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean updateStatusToConfirmed(int enrollmentId, int userId) {
        String sql = "UPDATE Enrollment SET Status = 'Confirmed' WHERE EnrollmentID = ? AND UserID = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, enrollmentId);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public List<Course> getConfirmedCoursesByUserId(int userId) {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT DISTINCT c.* FROM Enrollment e "
                   + "JOIN Course c ON e.CourseID = c.CourseID "
                   + "WHERE e.UserID = ? AND e.Status = 'Confirmed'";

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Course course = new CourseDAO().extractCourse(rs); // hoặc bạn tạo static extractCourse()
                courses.add(course);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return courses;
    }

}
