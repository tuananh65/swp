package dao;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBContext {
    private static final DBContext instance = new DBContext();
    private Connection connection;

    public static DBContext getInstance() {
        return instance;
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                String user = "sa";
                String password = "123";
                String url = "jdbc:sqlserver://DESKTOP-98TGIBL\\KTEAM:1433;databaseName=SoftSkillLearning3;TrustServerCertificate=true;";
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                connection = DriverManager.getConnection(url, user, password);
            }
        } catch (Exception e) {
            e.printStackTrace();
            connection = null;
        }
        return connection;
    }

    private DBContext() {}
}