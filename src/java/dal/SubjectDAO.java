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

        String sql = "SELECT SubjectId, Name, CategoryId, Featured, Thumbnail, Description, NumberOfLesson, Owner, Status "
                   + "FROM Subject WHERE SubjectId = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, subjectId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                // Xử lý nullable cho Integer và Boolean
                Integer categoryId = rs.getObject("CategoryId") != null ? rs.getInt("CategoryId") : null;
                Boolean featured = rs.getObject("Featured") != null ? rs.getBoolean("Featured") : null;
                Integer numberOfLesson = rs.getObject("NumberOfLesson") != null ? rs.getInt("NumberOfLesson") : null;
                String owner = rs.getString("Owner");
                String status = rs.getString("Status");

                subject = new Subject(
                    rs.getInt("SubjectId"),
                    rs.getString("Name"),
                    categoryId,
                    featured,
                    rs.getString("Thumbnail"),
                    rs.getString("Description"),
                    numberOfLesson,
                    owner,
                    status
                );
            }
        } catch (SQLException ex) {
            Logger.getLogger(SubjectDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return subject;
    }
}
