package DAL;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionDAL {
    private static final String URL = "jdbc:sqlserver://localhost:1433;"
            + "databaseName = tour_management;"
            + "encrypt = true;"
            + "trustServerCertificate = true;";
    private static final String USER = "sa";
    private static final String PASSWORD = "letaiken9a4";

    public Connection getConnection() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("connect successfully!");
            if (conn == null || conn.isClosed()) {
                throw new IllegalStateException("Connection không hợp lệ hoặc đã đóng.");
            }
            return conn;
        } catch (Exception ex) {
            System.out.println("connect failure!");
            throw new IllegalStateException("Không thể kết nối SQL Server.", ex);
        }
    }
}