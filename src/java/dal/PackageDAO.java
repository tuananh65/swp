package dal;

import model.Package;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PackageDAO extends DBContext {

    public List<Package> getAllPackages() {
        List<Package> list = new ArrayList<>();
        String sql = "SELECT * FROM Package";

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Package p = new Package();
                p.setPackageId(rs.getInt("PackageID"));

                p.setName(rs.getString("Name"));
                p.setDurationInDays(rs.getInt("DurationInDays"));
                p.setPriceModifier(rs.getDouble("PriceModifier"));
                list.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
    
    public Package getPackageById(int id) {
        String sql = "SELECT * FROM Package WHERE PackageID = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                Package p = new Package();
                p.setPackageId(rs.getInt("PackageID"));
                p.setName(rs.getString("Name"));
                p.setPriceModifier(rs.getDouble("PriceModifier"));
                p.setDurationInDays(rs.getInt("DurationInDays"));
                return p;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException ex) { ex.printStackTrace(); }
            try { if (ps != null) ps.close(); } catch (SQLException ex) { ex.printStackTrace(); }
            try { if (conn != null) conn.close(); } catch (SQLException ex) { ex.printStackTrace(); }
        }
        return null;
    }
    public void insertPackage(Package pkg) {
        String sql = "INSERT INTO Package (Name, DurationInDays, PriceModifier) VALUES (?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, pkg.getName());
            ps.setInt(2, pkg.getDurationInDays());
            ps.setDouble(3, pkg.getPriceModifier());

            int rows = ps.executeUpdate();
            System.out.println("Inserted " + rows + " row(s) into Package.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void deletePackage(int id) {
    String sql = "DELETE FROM Package WHERE PackageID = ?";
    try (Connection conn = getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, id);
        ps.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}


public void updatePackage(Package p) {
    String sql = "UPDATE Package SET Name = ?, DurationInDays = ?, PriceModifier = ? WHERE PackageID = ?";
    try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, p.getName());
        ps.setInt(2, p.getDurationInDays());
        ps.setDouble(3, p.getPriceModifier());
        ps.setInt(4, p.getPackageId());
        ps.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

}
