//package dal;
//import dal.DBContext;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import model.Subject;
//
//public class SubjectDetailDAO extends DBContext {
//    private Connection conn = null;
//    private PreparedStatement ps = null;
//    private ResultSet rs = null;
//
//    public Subject getSubjectById(int subjectId) {
//        Subject subject = null;
//        String sql = "SELECT subjectid, name, categoryid, featured, thumbnail, description, numberOfLesson, owner, status FROM Subject WHERE subjectid = ?";
//
//        try {
//            conn = getConnection();
//            ps = conn.prepareStatement(sql);
//            ps.setInt(1, subjectId);
//            rs = ps.executeQuery();
//
//            if (rs.next()) {
//                subject = new Subject();
//                subject.setSubjectId(rs.getInt("subjectid"));
//                subject.setName(rs.getString("name"));
//                subject.setCategoryId(rs.getInt("categoryid"));
//                subject.setFeatured(rs.getBoolean("featured"));
//                subject.setThumbnail(rs.getString("thumbnail") != null ? rs.getString("thumbnail") : "");
//                subject.setDescription(rs.getString("description") != null ? rs.getString("description") : "");
//                subject.setNumberOfLesson(rs.getInt("numberOfLesson"));
//                subject.setOwner(rs.getString("owner"));
//                subject.setStatus(rs.getString("status"));
//            }
//        } catch (SQLException e) {
//            System.err.println("Lỗi trong getSubjectById: " + e.getMessage());
//            e.printStackTrace();
//        } finally {
//            closeResources();
//        }
//        return subject;
//    }
//
//    public boolean updateSubject(Subject subject) {
//        String sql = "UPDATE Subject SET name = ?, categoryid = ?, featured = ?, thumbnail = ?, description = ?, numberOfLesson = ?, owner = ?, status = ? WHERE subjectid = ?";
//        try {
//            conn = getConnection();
//            ps = conn.prepareStatement(sql);
//            ps.setString(1, subject.getName());
//            ps.setInt(2, subject.getCategoryId());
//            ps.setBoolean(3, subject.isFeatured());
//            ps.setString(4, subject.getThumbnail());
//            ps.setString(5, subject.getDescription());
//            ps.setInt(6, subject.getNumberOfLesson());
//            ps.setString(7, subject.getOwner());
//            ps.setString(8, subject.getStatus());
//            ps.setInt(9, subject.getSubjectId());
//
//            int rowsAffected = ps.executeUpdate();
//            if (rowsAffected > 0) {
//                System.out.println("Successfully updated Subject ID: " + subject.getSubjectId());
//                return true;
//            } else {
//                System.err.println("No rows updated for Subject ID: " + subject.getSubjectId() + ". Subject may not exist.");
//                return false;
//            }
//        } catch (SQLException e) {
//            System.err.println("SQL Error in updateSubject: " + e.getMessage());
//            e.printStackTrace();
//            return false;
//        } finally {
//            closeResources();
//        }
//    }
//
//    private void closeResources() {
//        try {
//            if (rs != null) rs.close();
//            if (ps != null) ps.close();
//            if (conn != null) conn.close();
//        } catch (SQLException e) {
//            System.err.println("Lỗi khi đóng tài nguyên: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }
//}
