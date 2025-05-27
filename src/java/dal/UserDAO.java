package dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import model.Users;


public class UserDAO extends DBContext {

   Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    public Users login(String email, String password) {
        String query = "select* from [User]\n"
                + "where email = ? and password = ?";
        try {
            conn = new DBContext().getConnection();//mo ket noi voi sql
            ps = conn.prepareStatement(query);
            ps.setString(1, email);
            ps.setString(2, password);
            rs = ps.executeQuery();
            if (rs.next()) {
                return new Users(rs.getInt(1),          // UserId
    rs.getString(2),       // UserName
    rs.getString(3),       // Password
    rs.getInt(4),          // RoleId
    rs.getString(5),       // FullName
    rs.getDate(6),         // DateOfBirth
    rs.getString(7),       // AvatarUrl
    rs.getString(8),       // Email
    rs.getString(9),       // Phone
    rs.getString(10),      // Address
    rs.getString(11),      // Status
    rs.getTimestamp(12),   // CreatedAt
    rs.getString(13));       // Gender
   
            }
        } catch (Exception e) {
        }
        return null;
    }
    public static void main(String[] args) {
        UserDAO dao = new UserDAO();
        Users user = dao.login("admin1@example.com","admin@123");
        
        System.out.println(user);
    }
}