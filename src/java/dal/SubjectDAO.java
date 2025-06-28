package dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Subject;

public class SubjectDAO extends DBContext {

    public Subject getSubjectById(int subjectId) {
        Subject subject = null;
        System.out.println("Attempting to get subject with ID: " + subjectId);
        String sql = "SELECT SubjectId, Name, CategoryId, Featured, StatusId, Thumbnail, Description "
                   + "FROM Subject WHERE SubjectId = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, subjectId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                subject = new Subject(
                    rs.getInt("SubjectId"),
                    rs.getString("Name"),
                    // SỬA ĐỔI Ở ĐÂY: Dùng getInt() cho Integer và getBoolean() cho Boolean
                    // Xử lý null cho các cột có thể null (Integer, Boolean)
                    rs.getInt("CategoryId"), // Giả định CategoryId không null hoặc có giá trị mặc định là 0 nếu null
                    rs.getBoolean("Featured"), // Giả định Featured không null hoặc có giá trị mặc định là false nếu null
                    rs.getInt("StatusId"),   // Giả định StatusId không null hoặc có giá trị mặc định là 0 nếu null
                    rs.getString("Thumbnail"),
                    rs.getString("Description")
                );
            } else {
                // Có thể thêm log hoặc xử lý khi không tìm thấy Subject
            }
        } catch (SQLException ex) {
            Logger.getLogger(SubjectDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        // Xóa dòng closeConnection() ở đây nếu DBContext của bạn không yêu cầu hoặc getConnection() đã dùng connection pool
        return subject;
    }
}