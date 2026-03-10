package DAL;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionDAL {
    private static Connection conn;
    private static String url = "jdbc:sqlserver://localhost:1433;"
            + "databaseName=tour_management;"
            + "encrypt=true;"
            + "trustServerCertificate=true;"
            + "integratedSecurity=true;";

    public Connection getConnection() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            conn = DriverManager.getConnection(url);
            System.out.println("connect successfully!");
        } catch (Exception ex) {
            System.out.println("connect failure!");
            ex.printStackTrace();
        }
        return conn;
    }
}
