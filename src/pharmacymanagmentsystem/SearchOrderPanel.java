/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package pharmacymanagmentsystem;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import pharmacymanagmentsystem.DBConnection;
/**
 *
 * @author mapple.pk
 */
public class SearchOrderPanel extends javax.swing.JPanel {

    /**
     * Creates new form SearchOrderPanel
     */
    public SearchOrderPanel() {
        initComponents();
    }
 // GUI Components
    private JTextField orderIdField;
    private JTextArea resultArea;
    private JButton searchBtn;

   private Manufacturer_Manager manufacturerManager;

public SearchOrderPanel(Order_Manager orderManager, Manufacturer_Manager manufacturerManager) {
    this.manufacturerManager = manufacturerManager;
// Constructor now has no parameters
        // this.orderManager = orderManager; // Remove this line
        setLayout(new GridLayout(3, 2, 10, 10)); // Layout for ID input and search button

        orderIdField = new JTextField();
        resultArea = new JTextArea(10, 30);
        resultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultArea);

        searchBtn = new JButton("Search Order");

        add(new JLabel("Order ID:"));
        add(orderIdField);
        add(new JLabel()); // empty space
        add(searchBtn);
        add(new JLabel("Result:"));
        add(scrollPane);

        searchBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String orderId = orderIdField.getText().trim();
                if (orderId.isEmpty()) {
                    JOptionPane.showMessageDialog(SearchOrderPanel.this, "Please enter an Order ID.", "Input Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                // Call the new method to search the database
                Order foundOrder = searchOrderFromDB(orderId);
                
                if (foundOrder != null) {
                    StringBuilder result = new StringBuilder();
                    result.append("Order ID: ").append(foundOrder.getOrderID()).append("\n");
                    result.append("Supplier: ").append(foundOrder.getSupplier().getName()).append(" (")
                          .append(foundOrder.getSupplier().getCompany()).append(")\n");
                    result.append("Status: ").append(foundOrder.getStatus()).append("\n");
                    result.append("Processed By: ").append(foundOrder.getProcessedBy()).append("\n"); // Use getProcessedBy
                    result.append("Total Order Amount: Rs. ").append(String.format("%.2f", foundOrder.calculateTotalAmount())).append("\n\n"); // Use calculateTotalAmount

                    result.append("Items:\n");

                    if (foundOrder.getOrderItems().isEmpty()) { // Use getOrderItems()
                        result.append("  (No items in this order)\n");
                    } else {
                        for (OrderItem item : foundOrder.getOrderItems()) { // Use getOrderItems()
                            result.append("- Medicine: ").append(item.getMedicine().getName()).append("\n");
                            result.append("  Quantity: ").append(item.getQuantity()).append("\n");
                            result.append("  Price per Unit: Rs. ").append(String.format("%.2f", item.getMedicine().getPrice())).append("\n");
                            result.append("  Total for Item: Rs. ").append(String.format("%.2f", item.calculateItemTotal())).append("\n\n");
                        }
                    }
                    resultArea.setText(result.toString());
                } else {
                    resultArea.setText("Order not found.");
                    JOptionPane.showMessageDialog(SearchOrderPanel.this, "Order not found.", "Search Result", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
    }

    // --- DATABASE INTERACTION METHODS ---

    private Order searchOrderFromDB(String orderId) {
        Connection conn = null;
        PreparedStatement pstmtOrder = null;
        PreparedStatement pstmtItems = null;
        ResultSet rsOrder = null;
        ResultSet rsItems = null;
        Order order = null;

        try {
            conn = DBConnection.getConnection();

            // 1. Fetch Order details
            String sqlOrder = "SELECT o.order_id, o.supplier_id, s.name AS supplier_name, s.company, s.contact, " +
                              "o.processed_by, o.total_amount, o.status " +
                              "FROM orders o JOIN suppliers s ON o.supplier_id = s.supplier_id " +
                              "WHERE o.order_id = ?";
            pstmtOrder = conn.prepareStatement(sqlOrder);
            pstmtOrder.setString(1, orderId);
            rsOrder = pstmtOrder.executeQuery();

            if (rsOrder.next()) {
                // Create Supplier object
                String supplierId = rsOrder.getString("supplier_id");
                String supplierName = rsOrder.getString("supplier_name");
                String supplierCompany = rsOrder.getString("company");
                String supplierContact = rsOrder.getString("contact");
                Supplier supplier = new Supplier(supplierId, supplierName, supplierCompany, supplierContact);

                // Create Order object (without items for now)
                String retrievedOrderId = rsOrder.getString("order_id");
                String processedBy = rsOrder.getString("processed_by");
                String status = rsOrder.getString("status");
                // The total_amount from DB is for display, calculateTotalAmount in Order calculates from items
                // double totalAmount = rsOrder.getDouble("total_amount"); // Not directly used in Order constructor, but can be for validation

                order = new Order(retrievedOrderId, supplier, processedBy, status);

                // 2. Fetch Order Items for this order
                List<OrderItem> orderItems = new ArrayList<>();
               String sqlItems = "SELECT oi.medicine_id, m.name AS medicine_name, m.price, oi.quantity, oi.unit_price, oi.item_total " +
                  "FROM order_items oi JOIN medicine m ON oi.medicine_id = m.medicine_id " +
                  "WHERE oi.order_id = ?";

                pstmtItems = conn.prepareStatement(sqlItems);
                pstmtItems.setString(1, orderId);
                rsItems = pstmtItems.executeQuery();

                while (rsItems.next()) {
                    String medicineId = rsItems.getString("medicine_id");
                    String medicineName = rsItems.getString("medicine_name");
                    double medicinePrice = rsItems.getDouble("price"); // Price from medicines table
                    int quantity = rsItems.getInt("quantity");

                    // To create a Medicine object, we also need its stock and supplier ID (can be null for searching)
                    // For displaying, name and price are sufficient, so we can simplify Medicine creation here.
                    // Or, we can fetch stock and supplier_id for completeness if needed elsewhere.
                    // For simplicity, we'll create Medicine with default stock/supplier_id if not fetched.
                    // A better approach would be to fetch medicine details for the medicine object.
                    // Let's assume we can create a Medicine object just with ID, Name, Price for display.
                    // Or, retrieve actual stock for consistency:
                    
                    // Option 1: Create a simple Medicine object just for display purposes
                    // Medicine medicine = new Medicine(medicineId, medicineName, medicinePrice, 0, null); // Stock and SupplierID might not be relevant here

                    // Option 2: If we need actual stock for a robust Medicine object:
                    Medicine medicine = getMedicineById(medicineId); // Helper to get full medicine details
                    if (medicine != null) {
                         // Use the fetched medicine object to create OrderItem
                         orderItems.add(new OrderItem(medicine, quantity));
                    } else {
                         System.err.println("Warning: Medicine " + medicineName + " (" + medicineId + ") not found in medicines table, skipping order item.");
                    }
                }
                order.setOrderItems(orderItems); // Set the fetched items into the Order object
            }
        } catch (SQLException e) {
            System.err.println("Database error searching for order " + orderId + ": " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error searching for order in DB. See console.", "DB Error", JOptionPane.ERROR_MESSAGE);
            order = null; // Ensure order is null on error
        } finally {
            try {
                if (rsItems != null) rsItems.close();
                if (pstmtItems != null) pstmtItems.close();
                if (rsOrder != null) rsOrder.close();
                if (pstmtOrder != null) pstmtOrder.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
        return order;
    }

    // Helper method to get a full Medicine object by ID from the database
   private Medicine getMedicineById(String medicineId) {
    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    Medicine medicine = null;
    try {
        conn = DBConnection.getConnection();
        String sql = "SELECT * FROM medicine WHERE medicine_id = ?";
        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, medicineId);
        rs = pstmt.executeQuery();
        if (rs.next()) {
            String manufacturerId = rs.getString("manufacturer_id");
            Manufacturer manufacturer = manufacturerManager.getManufacturerById(manufacturerId); // 👈

            medicine = new Medicine(
                rs.getString("medicine_id"),
                rs.getString("name"),
                rs.getString("type"),
                rs.getDouble("price"),
                rs.getInt("stock_quantity"),
                rs.getString("expiry_date"),
                rs.getInt("reorder_level"),
                rs.getBoolean("requires_prescription"),
                manufacturer
            );
        }
    } catch (SQLException e) {
        System.err.println("Database error fetching medicine by ID: " + e.getMessage());
        e.printStackTrace();
    } finally {
        try {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            System.err.println("Error closing resources: " + e.getMessage());
        }
    }
    return medicine;
}







    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
