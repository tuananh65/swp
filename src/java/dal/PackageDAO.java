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
}
