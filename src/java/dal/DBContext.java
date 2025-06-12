/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import dal.DatabaseInfo;
import java.sql.*;

public class DBContext implements DatabaseInfo {

    protected Connection connection;
    protected PreparedStatement preStatement;
    protected ResultSet resultSet;

    public DBContext() {
    }

    public Connection getConnection() {
        try {
            Class.forName(DatabaseInfo.DRIVERNAME);
        } catch (ClassNotFoundException e) {
            System.err.println("(At DBContext)_Error loading driver!" + e);
        }
        try {
            Connection con = DriverManager.getConnection(DatabaseInfo.DBURL, DatabaseInfo.USERDB, DatabaseInfo.PASSDB);
            return con;
        } catch (SQLException e) {
            System.err.println("(At DBContext)_Error: " + e);
        }
        return null;
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Error " + e.getMessage() + " at DBContext");
        }
    }
    //-------------------------------------------------------------------------------------------------//
    public static void main(String[] args) {
        DBContext db = new DBContext();
        Connection con = db.getConnection();
        System.out.println(con);
    }
}