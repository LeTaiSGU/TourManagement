/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author letan
 */
public class ConnectionDAL {
    private static Connection conn;
    private static String url = "jdbc:sqlserver://localhost:1433;"
            + "databaseName=QLSV;"
            + "encrypt=true;" 
            + "trustServerCertificate=true;";
    private static String user = "sa";
    private static String password = "letaiken9a4";
    
    public static Connection getConnection() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("connect successfully!");
        } catch (Exception ex) {
            System.out.println("connect failure!");
            ex.printStackTrace();
        }
        return conn;
    }  
}
