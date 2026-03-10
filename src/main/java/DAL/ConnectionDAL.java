package DAL;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionDAL {
    private final String url = "jdbc:sqlserver://localhost:1433;"
            + "databaseName=tour_management;"
            + "encrypt=true;"
            + "trustServerCertificate=true;"
            + "integratedSecurity=true;";

    public Connection getConnection() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            return DriverManager.getConnection(url);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}