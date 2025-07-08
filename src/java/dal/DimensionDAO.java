package dal;

import model.Dimension;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DimensionDAO extends DBContext {
    private Connection conn = null;
    private PreparedStatement ps = null;
    private ResultSet rs = null;

    private static final Logger LOGGER = Logger.getLogger(DimensionDAO.class.getName());

    // Lấy tất cả dimension theo subjectId
    public List<Dimension> getAllDimensionsBySubjectId(int subjectId) {
        List<Dimension> list = new ArrayList<>();
        String sql = "SELECT * FROM Dimension WHERE subjectId = ?";
        try {
            conn = getConnection();
            if (conn == null) {
                LOGGER.log(Level.SEVERE, "Không thể lấy connection khi gọi getAllDimensionsBySubjectId với subjectId: " + subjectId);
                return list;
            }
            ps = conn.prepareStatement(sql);
            ps.setInt(1, subjectId);
            rs = ps.executeQuery();
            while (rs.next()) {
                Dimension d = new Dimension();
                d.setDimensionId(rs.getInt("dimensionId"));
                d.setSubjectId(rs.getInt("subjectId"));
                d.setType(rs.getString("type"));
                d.setDimensionName(rs.getString("dimensionName"));
                list.add(d);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi SQL trong getAllDimensionsBySubjectId. Query: " + sql, e);
        } finally {
            closeResources();
        }
        return list;
    }

    // Lấy một dimension theo ID
    public Dimension getDimensionById(int id) {
        String sql = "SELECT * FROM Dimension WHERE dimensionId = ?";
        try {
            conn = getConnection();
            if (conn == null) {
                LOGGER.log(Level.SEVERE, "Không thể lấy connection khi gọi getDimensionById với id: " + id);
                return null;
            }
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                Dimension d = new Dimension();
                d.setDimensionId(rs.getInt("dimensionId"));
                d.setSubjectId(rs.getInt("subjectId"));
                d.setType(rs.getString("type"));
                d.setDimensionName(rs.getString("dimensionName"));
                return d;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi SQL trong getDimensionById. Query: " + sql, e);
        } finally {
            closeResources();
        }
        return null;
    }

    // Thêm mới dimension
    public boolean insertDimension(Dimension d) {
        String sql = "INSERT INTO Dimension (subjectId, type, dimensionName) VALUES (?, ?, ?)";
        try {
            conn = getConnection();
            if (conn == null) {
                LOGGER.log(Level.SEVERE, "Không thể lấy connection khi gọi insertDimension.");
                return false;
            }
            ps = conn.prepareStatement(sql);
            ps.setInt(1, d.getSubjectId());
            ps.setString(2, d.getType());
            ps.setString(3, d.getDimensionName());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi SQL trong insertDimension. Query: " + sql, e);
            return false;
        } finally {
            closeResources();
        }
    }

    // Cập nhật dimension
    public boolean updateDimension(Dimension d) {
        String sql = "UPDATE Dimension SET subjectId = ?, type = ?, dimensionName = ? WHERE dimensionId = ?";
        try {
            conn = getConnection();
            if (conn == null) {
                LOGGER.log(Level.SEVERE, "Không thể lấy connection khi gọi updateDimension với id: " + d.getDimensionId());
                return false;
            }
            ps = conn.prepareStatement(sql);
            ps.setInt(1, d.getSubjectId());
            ps.setString(2, d.getType());
            ps.setString(3, d.getDimensionName());
            ps.setInt(4, d.getDimensionId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi SQL trong updateDimension. Query: " + sql, e);
            return false;
        } finally {
            closeResources();
        }
    }

    // Xóa dimension theo ID
    public boolean deleteDimension(int id) {
        String sql = "DELETE FROM Dimension WHERE dimensionId = ?";
        try {
            conn = getConnection();
            if (conn == null) {
                LOGGER.log(Level.SEVERE, "Không thể lấy connection khi gọi deleteDimension với id: " + id);
                return false;
            }
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi SQL trong deleteDimension. Query: " + sql, e);
            return false;
        } finally {
            closeResources();
        }
    }

    // Đóng kết nối, statement, resultset
    private void closeResources() {
        try {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi đóng tài nguyên database.", e);
        }
    }
}
