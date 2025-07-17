/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pharmacymanagmentsystem;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;
/**
 *
 * @author mapple.pk
 */
public class DBConnection {
     private static final String DB_URL = "jdbc:mysql://localhost:3306/pharmacymanagment_db";
    private static final String DB_USER = "root"; // e.g., "root"
    private static final String DB_PASSWORD = "03216297157ALI@"; // e.g., "password"

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "MySQL JDBC Driver not found. Make sure it's in your project's library.", "Database Error", JOptionPane.ERROR_MESSAGE);
            throw new SQLException("JDBC Driver not found", e);
        }
    }

}
