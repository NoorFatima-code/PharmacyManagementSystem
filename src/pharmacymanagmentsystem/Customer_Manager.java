/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pharmacymanagmentsystem;
import pharmacymanagmentsystem.DBConnection;
import java.util.ArrayList;
import java.util.List;

//forr sql///
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author mapple.pk
 */
public class Customer_Manager {
  private ArrayList<Customer> customerList = new ArrayList<>();

    private final String DB_URL = "jdbc:mysql://localhost:3306/pharmacymanagment_db"; // ✅ your DB
    private final String USER = "root";
    private final String PASS = "03216297157ALI@";

    public Customer_Manager() {
        loadCustomersFromDatabase(); // Load once during nitialization
    }

    public Customer_Manager(List<Customer> customerList) {
        this.customerList=(ArrayList<Customer>) customerList;
    }

    // ✅ Always use this for all panels
    public ArrayList<Customer> getCustomerList() {
        return customerList;
    }

    // ✅ Generate Customer ID based on max existing one in DB
    public String generateCustomerID() {
        String newID = "CUST1";
        int maxID = 0;

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            String sql = "SELECT customer_id FROM customer "; // ✅ match your table
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String id = rs.getString("customer_id"); // e.g. "CUST7"
                int num = Integer.parseInt(id.replace("CUST", ""));
                if (num > maxID) {
                    maxID = num;
                }
            }

            newID = "CUST" + (maxID + 1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return newID;
    }

    // ✅ Load all customers from DB into memory
    public void loadCustomersFromDatabase() {
        customerList.clear();

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            String sql = "SELECT * FROM customer WHERE is_active=TRUE"; // ✅ match your table
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String id = rs.getString("customer_id");
                String name = rs.getString("name");
                String contact = rs.getString("contact_number");

                customerList.add(new Customer(id, name, contact));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ✅ Add to database
    public boolean addCustomerToDatabase(String customerId, String name, String contactNumber) {
        String insertQuery = "INSERT INTO customer (customer_id, name, contact_number) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement(insertQuery)) {

            stmt.setString(1, customerId);
            stmt.setString(2, name);
            stmt.setString(3, contactNumber);

            int rowsInserted = stmt.executeUpdate();

            if (rowsInserted > 0) {
                customerList.add(new Customer(customerId, name, contactNumber)); // sync with memory
                return true;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error adding customer: " + e.getMessage());
        }
        return false;
    }

    // ✅ Update in database
    public boolean updateCustomerInDatabase(Customer updated) {
        String query = "UPDATE customer SET name = ?, contact_number = ? WHERE customer_id = ? AND is_active=TRUE";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, updated.getName());
            stmt.setString(2, updated.getContactNumber());
            stmt.setString(3, updated.getCustomerID());

            int updatedRows = stmt.executeUpdate();

            if (updatedRows > 0) {
                for (Customer c : customerList) {
                    if (c.getCustomerID().equals(updated.getCustomerID())) {
                        c.setName(updated.getName());
                        c.setContactNumber(updated.getContactNumber());
                        break;
                    }
                }
                return true;
            }

        } catch (SQLException e) {
            System.out.println("Error updating customer: " + e.getMessage());
        }
        return false;
    }

    // ✅ Optional: delete from database
   public boolean deleteCustomerFromDatabase(String customerId) {
    String query = "UPDATE customer SET is_active = FALSE WHERE customer_id = ?";

    try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
         PreparedStatement stmt = conn.prepareStatement(query)) {

        stmt.setString(1, customerId);
        int rowsUpdated = stmt.executeUpdate();

        if (rowsUpdated > 0) {
            customerList.removeIf(c -> c.getCustomerID().equals(customerId));
            return true;
        }

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error deactivating customer: " + e.getMessage());
    }

    return false;
}


    
    public Customer getCustomerByIDFromDatabase(String customerID) {
    String sql = "SELECT * FROM customer WHERE customer_id = ? AND is_active=TRUE";
    try (Connection con = DBConnection.getConnection();
         PreparedStatement stmt = con.prepareStatement(sql)) {
        stmt.setString(1, customerID);
        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return new Customer(
                    rs.getString("customer_id"),
                    rs.getString("name"),
                    rs.getString("contact_number")
                );
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null;
}
    public String getCustomerNameFromDatabase(String customerID) {
    String sql = "SELECT name FROM customer WHERE customer_id = ? AND is_active=TRUE";
    try (Connection con = DBConnection.getConnection();
         PreparedStatement stmt = con.prepareStatement(sql)) {

        stmt.setString(1, customerID);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return rs.getString("name");
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null;
}


    public List<Customer> getAllCustomersFromDatabase() {
    List<Customer> customerList = new ArrayList<>();
    String sql = "SELECT * FROM customer WHERE is_active=TRUE";

    try (Connection con = DBConnection.getConnection();
         PreparedStatement stmt = con.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {

        while (rs.next()) {
            Customer customer = new Customer(
                rs.getString("customer_id"),
                rs.getString("name"),
                rs.getString("contact_number")
            );
            customerList.add(customer);
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return customerList;
}

    
 
}
