package pharmacymanagmentsystem;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import pharmacymanagmentsystem.DBConnection;

public class UpdateOrderPanel extends JPanel {

   private Manufacturer_Manager manufacturerManager;

  private JTextField orderIdField;
    private JComboBox<String> statusBox;
    private JTextField processedByField;
    private JTextArea currentOrderInfoArea; // To display current order info

    public UpdateOrderPanel(Order_Manager orderManager, MedicineManager medicineManager, Supplier_Manager supplierManager,Manufacturer_Manager manufacturerManager
) { // Constructor now has no parameters
        // this.orderManager = orderManager; // Remove this line
        setLayout(new BorderLayout(10, 10)); // Use BorderLayout
this.manufacturerManager=manufacturerManager;
        // --- Input Panel (North) ---
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 10));

        orderIdField = new JTextField();
        statusBox = new JComboBox<>(new String[]{"Pending", "Received"});
        processedByField = new JTextField();

        JButton searchBtn = new JButton("Load Order"); // New button to load order info
        JButton updateBtn = new JButton("Update Order");

        inputPanel.add(new JLabel("Order ID:"));
        inputPanel.add(orderIdField);
        inputPanel.add(new JLabel("New Status:"));
        inputPanel.add(statusBox);
        inputPanel.add(new JLabel("Processed By:"));
        inputPanel.add(processedByField);
        inputPanel.add(searchBtn); // Add search button
        inputPanel.add(updateBtn);

        // --- Display Area (Center) ---
        currentOrderInfoArea = new JTextArea(10, 30);
        currentOrderInfoArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(currentOrderInfoArea);

        add(inputPanel, BorderLayout.NORTH);
        add(new JLabel("Current Order Details:", SwingConstants.CENTER), BorderLayout.WEST); // Label for info area
        add(scrollPane, BorderLayout.CENTER);

        // --- Action Listeners ---
        searchBtn.addActionListener(e -> loadOrderDetailsForUpdate());
// Inside UpdateOrderPanel.java

// ... other code ...

        updateBtn.addActionListener(e -> {
            String orderId = orderIdField.getText().trim();
            String newStatus = (String) statusBox.getSelectedItem(); // Get selected status
            String newProcessedBy = processedByField.getText().trim();

            if (orderId.isEmpty() || newProcessedBy.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter Order ID and Processed By.", "Input Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // The 'updateOrderInDB' method handles the actual database update for status and processed_by
            // We do NOT need to load the order into memory and manipulate its item list here.
            boolean updated = updateOrderInDB(orderId, newStatus, newProcessedBy);

            if (updated) {
                JOptionPane.showMessageDialog(this, "Order updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                clearFields();
                currentOrderInfoArea.setText(""); // Clear info
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update order. Check console for details.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });


        
    }

    // --- DATABASE INTERACTION METHODS ---

    private void loadOrderDetailsForUpdate() {
        String orderId = orderIdField.getText().trim();
        if (orderId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter an Order ID to load.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Use the search method (similar to SearchOrderPanel)
        Order foundOrder = searchOrderFromDB(orderId); // Call internal DB search method
        
        if (foundOrder != null) {
            StringBuilder info = new StringBuilder();
            info.append("Order ID: ").append(foundOrder.getOrderID()).append("\n");
            info.append("Supplier: ").append(foundOrder.getSupplier().getName()).append(" (")
                .append(foundOrder.getSupplier().getCompany()).append(")\n");
            info.append("Current Status: ").append(foundOrder.getStatus()).append("\n");
            info.append("Processed By: ").append(foundOrder.getProcessedBy()).append("\n");
            info.append("Total Price: Rs. ").append(String.format("%.2f", foundOrder.calculateTotalAmount())).append("\n"); // Use calculateTotalAmount
            info.append("\nItems:\n");
            
            // Check if items list is null or empty before iterating
            if (foundOrder.getOrderItems() != null && !foundOrder.getOrderItems().isEmpty()) {
                for (OrderItem item : foundOrder.getOrderItems()) { // Use getOrderItems()
                    info.append("  - ").append(item.getMedicine().getName())
                        .append(" (Qty: ").append(item.getQuantity())
                        .append(", Unit Price: ").append(String.format("%.2f", item.getMedicine().getPrice()))
                        .append(")\n");
                }
            } else {
                info.append("  (No items found for this order)\n");
            }

            currentOrderInfoArea.setText(info.toString());
            statusBox.setSelectedItem(foundOrder.getStatus());
            processedByField.setText(foundOrder.getProcessedBy()); // Pre-fill with existing "processed by"
        } else {
            currentOrderInfoArea.setText("Order not found with ID: " + orderId);
            JOptionPane.showMessageDialog(this, "Order not found.", "Search Result", JOptionPane.INFORMATION_MESSAGE);
            clearFields();
        }
    }
    
//    private Order searchOrderFromDB(String orderId) {
//        Connection conn = null;
//        PreparedStatement pstmtOrder = null;
//        PreparedStatement pstmtItems = null;
//        ResultSet rsOrder = null;
//        ResultSet rsItems = null;
//        Order order = null;
//
//        try {
//            conn = DBConnection.getConnection();
//
//            // 1. Fetch Order details
//            String sqlOrder = "SELECT o.order_id, o.supplier_id, s.name AS supplier_name, s.company, s.contact, " +
//                              "o.processed_by, o.total_amount, o.status " +
//                              "FROM orders o JOIN suppliers s ON o.supplier_id = s.supplier_id " +
//                              "WHERE o.order_id = ?";
//            pstmtOrder = conn.prepareStatement(sqlOrder);
//            pstmtOrder.setString(1, orderId);
//            rsOrder = pstmtOrder.executeQuery();
//
//            if (rsOrder.next()) {
//                // Create Supplier object
//                String supplierId = rsOrder.getString("supplier_id");
//                String supplierName = rsOrder.getString("supplier_name");
//                String supplierCompany = rsOrder.getString("company");
//                String supplierContact = rsOrder.getString("contact");
//                Supplier supplier = new Supplier(supplierId, supplierName, supplierCompany, supplierContact);
//
//                // Create Order object (without items for now)
//                String retrievedOrderId = rsOrder.getString("order_id");
//                String processedBy = rsOrder.getString("processed_by");
//                String status = rsOrder.getString("status");
//                
//                order = new Order(retrievedOrderId, supplier, processedBy, status);
//
//                // 2. Fetch Order Items for this order
//                List<OrderItem> orderItems = new ArrayList<>();
//                String sqlItems = "SELECT oi.medicine_id, m.name AS medicine_name, m.price, m.stock, m.supplier_id, oi.quantity, oi.unit_price, oi.item_total " + // Include stock and supplier_id for full Medicine object
//                                  "FROM order_items oi JOIN medicines m ON oi.medicine_id = m.medicine_id " +
//                                  "WHERE oi.order_id = ?";
//                pstmtItems = conn.prepareStatement(sqlItems);
//                pstmtItems.setString(1, orderId);
//                rsItems = pstmtItems.executeQuery();
//
//                while (rsItems.next()) {
//                    // Create a full Medicine object from fetched data
//                    Medicine medicine = new Medicine(
//                        rsItems.getString("medicine_id"),
//                        rsItems.getString("medicine_name"),
//                        rsItems.getDouble("price"),
//                        rsItems.getInt("stock"), // Get stock from medicines table
//                        rsItems.getString("supplier_id") // Get supplier_id from medicines table
//                    );
//                    int quantity = rsItems.getInt("quantity");
//                    orderItems.add(new OrderItem(medicine, quantity));
//                }
//                order.setOrderItems(orderItems); // Set the fetched items into the Order object
//            }
//        } catch (SQLException e) {
//            System.err.println("Database error searching for order " + orderId + ": " + e.getMessage());
//            e.printStackTrace();
//            // Don't show JOptionPane here, let loadOrderDetailsForUpdate handle "not found"
//            order = null; // Ensure order is null on error
//        } finally {
//            try {
//                if (rsItems != null) rsItems.close();
//                if (pstmtItems != null) pstmtItems.close();
//                if (rsOrder != null) rsOrder.close();
//                if (pstmtOrder != null) pstmtOrder.close();
//                if (conn != null) conn.close();
//            } catch (SQLException e) {
//                System.err.println("Error closing resources: " + e.getMessage());
//            }
//        }
//        return order;
//    }

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
            // Supplier info
            String supplierId = rsOrder.getString("supplier_id");
            String supplierName = rsOrder.getString("supplier_name");
            String supplierCompany = rsOrder.getString("company");
            String supplierContact = rsOrder.getString("contact");
            Supplier supplier = new Supplier(supplierId, supplierName, supplierCompany, supplierContact);

            // Order object
            String processedBy = rsOrder.getString("processed_by");
            String status = rsOrder.getString("status");
            order = new Order(orderId, supplier, processedBy, status);

            // 2. Fetch Order Items (medicine JOIN manufacturer)
            List<OrderItem> orderItems = new ArrayList<>();
            String sqlItems = "SELECT oi.medicine_id, m.name AS medicine_name, m.type, m.price, m.stock_quantity, m.expiry_date, m.reorder_level, m.requires_prescription, m.manufacturer_id, oi.quantity " +
                              "FROM order_items oi JOIN medicine m ON oi.medicine_id = m.medicine_id " +
                              "WHERE oi.order_id = ?";
            pstmtItems = conn.prepareStatement(sqlItems);
            pstmtItems.setString(1, orderId);
            rsItems = pstmtItems.executeQuery();

            while (rsItems.next()) {
                String manufacturerId = rsItems.getString("manufacturer_id");
                Manufacturer manufacturer = manufacturerManager.getManufacturerById(manufacturerId);

                Medicine medicine = new Medicine(
                    rsItems.getString("medicine_id"),
                    rsItems.getString("medicine_name"),
                    rsItems.getString("type"),
                    rsItems.getDouble("price"),
                    rsItems.getInt("stock_quantity"),
                    rsItems.getString("expiry_date"),
                    rsItems.getInt("reorder_level"),
                    rsItems.getBoolean("requires_prescription"),
                    manufacturer
                );

                int quantity = rsItems.getInt("quantity");
                orderItems.add(new OrderItem(medicine, quantity));
            }

            order.setOrderItems(orderItems);
        }
    } catch (SQLException e) {
        System.err.println("Database error searching for order " + orderId + ": " + e.getMessage());
        e.printStackTrace();
        order = null;
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


    private boolean updateOrderInDB(String orderId, String newStatus, String newProcessedBy) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBConnection.getConnection();
            String sql = "UPDATE orders SET status = ?, processed_by = ? WHERE order_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, newStatus);
            pstmt.setString(2, newProcessedBy);
            pstmt.setString(3, orderId);
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("DEBUG: Order " + orderId + " updated successfully. Rows affected: " + rowsAffected);
                return true;
            } else {
                System.out.println("DEBUG: No order found with ID " + orderId + " for update.");
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Database error updating order " + orderId + ": " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error during update: " + e.getMessage(), "DB Error", JOptionPane.ERROR_MESSAGE);
            return false;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
    }


    private void clearFields() {
        orderIdField.setText("");
        processedByField.setText("");
        statusBox.setSelectedIndex(0); // Reset to "Pending"
        currentOrderInfoArea.setText(""); // Clear the info area as well
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
