package pharmacymanagmentsystem;

import pharmacymanagmentsystem.DBConnection;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AddOrderPanel extends JPanel {

  // GUI components
    private JTextField orderIdField;
    private JComboBox<Supplier> supplierComboBox;
    private JComboBox<Medicine> medicineComboBox;
    private JTextField quantityField;
    private JTextField processedByField;

    private DefaultTableModel orderItemsTableModel;
    private JTable orderItemsTable;

    private List<OrderItem> currentOrderItems; // Temporary list for items in the current order
    private Manufacturer_Manager manufacturerManager;

    // Constructor no longer takes managers
    public AddOrderPanel(Order_Manager orderManager, MedicineManager medicineManager, Supplier_Manager supplierManager,Manufacturer_Manager manufacturerManager) {
        // You might pass null for managers or adjust MainFrame to not pass them
        // If your main frame explicitly passes managers, you might need a no-arg constructor
        // or just comment out the manager fields and their assignments.
        // this.orderManager = orderManager;
        // this.supplierManager = supplierManager;
        // this.medicineManager = medicineManager;
        this.manufacturerManager=manufacturerManager;
        this.currentOrderItems = new ArrayList<>(); // Initialize the temporary list

        setLayout(new BorderLayout(10, 10));

        // --- Input Panel (North) ---
        JPanel inputPanel = new JPanel(new GridLayout(6, 2, 10, 10));

        orderIdField = new JTextField();
        orderIdField.setEditable(false);
        orderIdField.setText(generateNextOrderID()); // Auto-generate initial ID from DB

        supplierComboBox = new JComboBox<>();
        populateSupplierComboBox(); // Populate the dropdown

        medicineComboBox = new JComboBox<>();
        populateMedicineComboBox(); // Populate the dropdown

        quantityField = new JTextField();
        processedByField = new JTextField();

        inputPanel.add(new JLabel("Order ID:"));
        inputPanel.add(orderIdField);
        inputPanel.add(new JLabel("Select Supplier:"));
        inputPanel.add(supplierComboBox);
        inputPanel.add(new JLabel("Select Medicine:"));
        inputPanel.add(medicineComboBox);
        inputPanel.add(new JLabel("Quantity:"));
        inputPanel.add(quantityField);
        inputPanel.add(new JLabel("Processed By (Name):"));
        inputPanel.add(processedByField);

        // --- Buttons Panel (Center/East) ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        JButton addItemBtn = new JButton("Add Item to Order");
        JButton placeOrderBtn = new JButton("Place Final Order");
        JButton clearOrderBtn = new JButton("Clear Current Order"); // To reset the items list

        buttonPanel.add(addItemBtn);
        buttonPanel.add(placeOrderBtn);
        buttonPanel.add(clearOrderBtn);

        // --- Order Items Table (Center) ---
        String[] columnNames = {"Medicine Name", "Quantity", "Price/Unit", "Total"};
        orderItemsTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
        };
        orderItemsTable = new JTable(orderItemsTableModel);
        JScrollPane scrollPane = new JScrollPane(orderItemsTable);

        // --- Add components to main panel ---
        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // --- Action Listeners ---
        addItemBtn.addActionListener(e -> addItemToCurrentOrder());
        placeOrderBtn.addActionListener(e -> placeFinalOrder());
        clearOrderBtn.addActionListener(e -> clearCurrentOrder());

        // Refresh ID and clear fields when panel is shown (useful if it's part of a tabbed pane)
        addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                refreshPanel();
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {}
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {}
        });
    }

    AddOrderPanel(Order_Manager orderManager, Supplier_Manager supplierManager, MedicineManager medicineManager) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    // --- DATABASE INTERACTION METHODS ---

    private String generateNextOrderID() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String lastId = null;
        try {
            conn = DBConnection.getConnection();
            String sql = "SELECT order_id FROM orders ORDER BY order_id DESC LIMIT 1";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                lastId = rs.getString("order_id");
            }
        } catch (SQLException e) {
            System.err.println("Database error generating order ID: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error generating Order ID. See console.", "DB Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }

        if (lastId == null || lastId.isEmpty()) {
            return "ORD001"; // First order ID
        } else {
            try {
                int num = Integer.parseInt(lastId.substring(3)); // Assumes format "ORDXXX"
                num++;
                return String.format("ORD%03d", num);
            } catch (NumberFormatException e) {
                System.err.println("Error parsing last order ID: " + lastId + ". Generating default ID.");
                return "ORD001"; // Fallback in case of parsing error
            }
        }
    }

    private boolean orderIdExists(String orderId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        boolean exists = false;
        try {
            conn = DBConnection.getConnection();
            String sql = "SELECT 1 FROM orders WHERE order_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, orderId);
            rs = pstmt.executeQuery();
            exists = rs.next(); // If rs.next() is true, the ID exists
        } catch (SQLException e) {
            System.err.println("Database error checking order ID existence: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error checking Order ID existence. See console.", "DB Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
        return exists;
    }


    private void populateSupplierComboBox() {
        supplierComboBox.removeAllItems();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            String sql = "SELECT supplier_id, name, company, contact FROM suppliers ORDER BY name";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                String id = rs.getString("supplier_id");
                String name = rs.getString("name");
                String company = rs.getString("company");
                String contact = rs.getString("contact");
                supplierComboBox.addItem(new Supplier(id, name, company, contact));
            }
            if (supplierComboBox.getItemCount() == 0) {
                 JOptionPane.showMessageDialog(this, "No suppliers found in the database. Please add suppliers first.", "No Data", JOptionPane.WARNING_MESSAGE);
            }
        } catch (SQLException e) {
            System.err.println("Database error fetching suppliers for combo box: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading suppliers. See console.", "DB Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
    }
//
  private void populateMedicineComboBox() {
    medicineComboBox.removeAllItems();
    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;

    try {
        conn = DBConnection.getConnection();
        String sql = "SELECT * FROM medicine ORDER BY name";
        pstmt = conn.prepareStatement(sql);
        rs = pstmt.executeQuery();

        while (rs.next()) {
            String id = rs.getString("medicine_id");
            String name = rs.getString("name");
            String type = rs.getString("type");
            double price = rs.getDouble("price");
            int stock = rs.getInt("stock_quantity");
            String expiryDate = rs.getString("expiry_date");
            int reorderLevel = rs.getInt("reorder_level");
            boolean requiresPrescription = rs.getBoolean("requires_prescription");
            String manufacturerId = rs.getString("manufacturer_id");

            Manufacturer manufacturer =  manufacturerManager.getManufacturerById(manufacturerId); // 👈 Helper below

            Medicine medicine = new Medicine(id, name, type, price, stock, expiryDate, reorderLevel, requiresPrescription, manufacturer);
            medicineComboBox.addItem(medicine);
        }

        if (medicineComboBox.getItemCount() == 0) {
            JOptionPane.showMessageDialog(this, "No medicines found in the database. Please add medicines first.", "No Data", JOptionPane.WARNING_MESSAGE);
        }

    } catch (SQLException e) {
        System.err.println("Database error fetching medicines for combo box: " + e.getMessage());
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error loading medicines. See console.", "DB Error", JOptionPane.ERROR_MESSAGE);
    } finally {
        try {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            System.err.println("Error closing resources: " + e.getMessage());
        }
    }
}

//   private void populateMedicineComboBox() {
//        medicineComboBox.removeAllItems();
//        Connection conn = null;
//        PreparedStatement pstmt = null;
//        ResultSet rs = null;
//        try {
//            conn = DBConnection.getConnection();
//            String sql = "SELECT medicine_id, name, price, stock_quantity, supplier_id FROM medicine ORDER BY name";
//            pstmt = conn.prepareStatement(sql);
//            rs = pstmt.executeQuery();
//
//            while (rs.next()) {
//                String id = rs.getString("medicine_id");
//                String name = rs.getString("name");
//                double price = rs.getDouble("price");
//                int stock = rs.getInt("stock");
//                String supplierId = rs.getString("supplier_id");                medicineComboBox.addItem(new Medicine(id, name, price, stock, supplierId));
//            }
//            if (medicineComboBox.getItemCount() == 0) {
//                JOptionPane.showMessageDialog(this, "No medicines found in the database. Please add medicines first.", "No Data", JOptionPane.WARNING_MESSAGE);
//            }
//        } catch (SQLException e) {
//            System.err.println("Database error fetching medicines for combo box: " + e.getMessage());
//            e.printStackTrace();
//            JOptionPane.showMessageDialog(this, "Error loading medicines. See console.", "DB Error", JOptionPane.ERROR_MESSAGE);
//        } finally {
//            try {
//                if (rs != null) rs.close();
//                if (pstmt != null) pstmt.close();
//                if (conn != null) conn.close();
//            } catch (SQLException e) {
//                System.err.println("Error closing resources: " + e.getMessage());
//            }
//        }
//    }
//    
    private boolean updateMedicineStock(String medicineId, int quantityDeducted, Connection conn) throws SQLException {
        // This method requires an existing connection to be passed for the transaction
        PreparedStatement pstmt = null;
        try {
            String sql = "UPDATE medicine SET stock_quantity = stock_quantity + ? WHERE medicine_id = ?";

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, quantityDeducted);
            pstmt.setString(2, medicineId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } finally {
            // Do NOT close the connection here, it's managed by the calling method (placeFinalOrder)
            if (pstmt != null) pstmt.close();
        }
    }

    // --- GUI LOGIC METHODS (Mostly unchanged, but now call internal DB methods) ---

    private void addItemToCurrentOrder() {
        Medicine selectedMedicine = (Medicine) medicineComboBox.getSelectedItem();
        String qtyText = quantityField.getText().trim();

        if (selectedMedicine == null) {
            JOptionPane.showMessageDialog(this, "Please select a medicine.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (qtyText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a quantity.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int quantity = Integer.parseInt(qtyText);
            if (quantity <= 0) {
                JOptionPane.showMessageDialog(this, "Quantity must be a positive number.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Check available stock before adding to order
            if (quantity > selectedMedicine.getStockQuantity()) {
                JOptionPane.showMessageDialog(this, "Not enough stock for " + selectedMedicine.getName() + ".\nAvailable: " + selectedMedicine.getStockQuantity(), "Stock Alert", JOptionPane.WARNING_MESSAGE);
                
            }

            // Check if item already exists in current order and update quantity
            boolean found = false;
            for (OrderItem item : currentOrderItems) {
                if (item.getMedicine().equals(selectedMedicine)) {
                    // Check stock for combined quantity
                    if (item.getQuantity() + quantity > selectedMedicine.getStockQuantity()) {
                        JOptionPane.showMessageDialog(this, "Adding " + quantity + " to existing " + item.getQuantity() + " exceeds available stock (" + selectedMedicine.getStockQuantity() + ") for " + selectedMedicine.getName() + ".", "Stock Alert", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    item.setQuantity(item.getQuantity() + quantity);
                    found = true;
                    break;
                }
            }
            if (!found) {
                currentOrderItems.add(new OrderItem(selectedMedicine, quantity));
            }

            updateOrderItemsTable();
            quantityField.setText("");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid quantity. Please enter a number.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateOrderItemsTable() {
        orderItemsTableModel.setRowCount(0); // Clear existing rows
        double totalOrderValue = 0; // To display overall total

        for (OrderItem item : currentOrderItems) {
            double itemTotal = item.calculateItemTotal();
            totalOrderValue += itemTotal;
            orderItemsTableModel.addRow(new Object[]{
                item.getMedicine().getName(),
                item.getQuantity(),
                String.format("%.2f", item.getMedicine().getPrice()),
                String.format("%.2f", itemTotal)
            });
        }
        // Add a total row at the end of the table
        if (!currentOrderItems.isEmpty()) {
            orderItemsTableModel.addRow(new Object[]{"", "", "Total:", String.format("%.2f", totalOrderValue)});
        }
    }

    private void placeFinalOrder() {
    String orderId = orderIdField.getText().trim();
    Supplier selectedSupplier = (Supplier) supplierComboBox.getSelectedItem();
    String processedBy = processedByField.getText().trim();

    if (selectedSupplier == null) {
        JOptionPane.showMessageDialog(this, "Please select a supplier.", "Input Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    if (currentOrderItems.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please add at least one item to the order.", "Input Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    if (processedBy.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please enter who processed the order.", "Input Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Check for duplicate Order ID
    if (orderIdExists(orderId)) {
        JOptionPane.showMessageDialog(this, "Order ID '" + orderId + "' already exists. Generating new ID.", "Duplicate ID", JOptionPane.ERROR_MESSAGE);
        orderIdField.setText(generateNextOrderID()); // Generate new ID
        return;
    }

    // Create the Order object
    Order newOrder = new Order(orderId, selectedSupplier, processedBy, currentOrderItems);

    Connection conn = null;
    PreparedStatement pstmtOrder = null;
    PreparedStatement pstmtOrderItem = null;
    boolean orderPlaced = false;

    try {
        conn = DBConnection.getConnection();
        conn.setAutoCommit(false); // Start transaction

        // ✅ 1. Insert into orders table with status
        String sqlOrder = "INSERT INTO orders (order_id, supplier_id, processed_by, total_amount, status) VALUES (?, ?, ?, ?, ?)";
        pstmtOrder = conn.prepareStatement(sqlOrder);
        pstmtOrder.setString(1, newOrder.getOrderID());
        pstmtOrder.setString(2, newOrder.getSupplier().getSupplierID());
        pstmtOrder.setString(3, newOrder.getProcessedBy());
        pstmtOrder.setDouble(4, newOrder.calculateTotalAmount());
        pstmtOrder.setString(5, "Pending"); // ✅ Set default status
        pstmtOrder.executeUpdate();

        // ✅ 2. Insert into order_items table
        String sqlOrderItem = "INSERT INTO order_items (order_id, medicine_id, quantity, unit_price, item_total) VALUES (?, ?, ?, ?, ?)";
        pstmtOrderItem = conn.prepareStatement(sqlOrderItem);

        for (OrderItem item : currentOrderItems) {
            pstmtOrderItem.setString(1, newOrder.getOrderID());
            pstmtOrderItem.setString(2, item.getMedicine().getMedicineID());
            pstmtOrderItem.setInt(3, item.getQuantity());
            pstmtOrderItem.setDouble(4, item.getMedicine().getPrice());
            pstmtOrderItem.setDouble(5, item.calculateItemTotal());
            pstmtOrderItem.addBatch();

            // Update medicine stock
            if (!updateMedicineStock(item.getMedicine().getMedicineID(), item.getQuantity(), conn)) {
                throw new SQLException("Failed to update stock for medicine: " + item.getMedicine().getName());
            }
        }

        pstmtOrderItem.executeBatch();
        conn.commit();
        orderPlaced = true;

        System.out.println("DEBUG: Order " + newOrder.getOrderID() + " and its items saved successfully.");

    } catch (SQLException e) {
        try {
            if (conn != null) conn.rollback();
        } catch (SQLException rbEx) {
            System.err.println("Error during rollback: " + rbEx.getMessage());
        }
        System.err.println("Database error placing order: " + e.getMessage());
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Failed to place order due to database error: " + e.getMessage(), "Order Save Error", JOptionPane.ERROR_MESSAGE);
    } finally {
        try {
            if (pstmtOrder != null) pstmtOrder.close();
            if (pstmtOrderItem != null) pstmtOrderItem.close();
            if (conn != null) conn.setAutoCommit(true);
            if (conn != null) conn.close();
        } catch (SQLException e) {
            System.err.println("Error closing resources: " + e.getMessage());
        }
    }

    if (orderPlaced) {
        JOptionPane.showMessageDialog(this, "Order " + newOrder.getOrderID() + " placed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        clearCurrentOrder();
        orderIdField.setText(generateNextOrderID());
    }
}


    private void clearCurrentOrder() {
        currentOrderItems.clear();
        orderItemsTableModel.setRowCount(0); // Clear table rows
        supplierComboBox.setSelectedIndex(-1); // Clear selection
        medicineComboBox.setSelectedIndex(-1); // Clear selection
        quantityField.setText("");
        processedByField.setText("");
        orderIdField.setText(generateNextOrderID()); // Generate new ID for next order
    }

    private void refreshPanel() {
        populateSupplierComboBox();
        populateMedicineComboBox();
        clearCurrentOrder(); // Resets the panel state
    }

  
   
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
