package DAL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class ConnectionDAL {
    private static Connection conn;
    private static String url = "jdbc:sqlserver://localhost:1433;"
<<<<<<< HEAD
            + "databaseName=tour_management;"
            + "integratedSecurity=true;"
            + "encrypt=true;" 
            + "trustServerCertificate=true;";
    private static String user = "";
    private static String password = "";
    
    public static Connection getConnection() {
=======
            + "databaseName = tour_management;"
            + "encrypt = true;"
            + "trustServerCertificate = true;";
    private static String user = "sa";
    private static String password = "Mysqlserver02";

    public Connection getConnection() {
>>>>>>> 379838c5d2def71baad191839bfd9123657069d3
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
