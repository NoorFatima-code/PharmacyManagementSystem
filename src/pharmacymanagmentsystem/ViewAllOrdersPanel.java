/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package pharmacymanagmentsystem;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
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
public class ViewAllOrdersPanel extends javax.swing.JPanel {

    /**
     * Creates new form ViewAllOrdersPanel
     */
    public ViewAllOrdersPanel() {
        initComponents();
    }
private Manufacturer_Manager manufacturerManager;

    private JTable table;
    private JTextField searchField;
    private JComboBox<String> searchTypeBox;
    private JButton refreshBtn;

    public ViewAllOrdersPanel(Order_Manager orderManager, MedicineManager medicineManager, Supplier_Manager supplierManager,Manufacturer_Manager manufacturerManager) { // Constructor now has no parameters
        // this.orderManager = orderManager; // Remove this line
        this.manufacturerManager=manufacturerManager;
        setLayout(new BorderLayout(10, 10));

        // Top Search Panel
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        searchField = new JTextField(15);
        searchTypeBox = new JComboBox<>(new String[]{"Order ID", "Supplier Name"});
        JButton searchBtn = new JButton("Search");
        refreshBtn = new JButton("Refresh All");

        topPanel.add(new JLabel("Search:"));
        topPanel.add(searchField);
        topPanel.add(searchTypeBox);
        topPanel.add(searchBtn);
        topPanel.add(refreshBtn);

        // Table and Scroll
        table = new JTable();
        // Make table non-editable by default
        DefaultTableModel nonEditableTableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table.setModel(nonEditableTableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        // Load All Orders initially
        loadAllOrders();

        // Action listeners
        refreshBtn.addActionListener(e -> {
            searchField.setText(""); // Clear search field on refresh
            loadAllOrders();
        });

        searchBtn.addActionListener(e -> {
            String query = searchField.getText().trim();
            String selectedSearchType = (String) searchTypeBox.getSelectedItem();
            
            if (query.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a search term.", "Input Error", JOptionPane.WARNING_MESSAGE);
                loadAllOrders(); // Show all if search field is empty
                return;
            }
            
            // Call the database search method
            List<Order> matchedOrders = searchOrdersFromDB(query, selectedSearchType);

            if (!matchedOrders.isEmpty()) {
                setTableData(matchedOrders);
            } else {
                JOptionPane.showMessageDialog(this, "No matching orders found.", "Search Result", JOptionPane.INFORMATION_MESSAGE);
                setTableData(new ArrayList<>()); // Clear table if no results
            }
        });

        // Add to main panel
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Refresh panel when it becomes visible (e.g., when switching tabs)
        addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                loadAllOrders(); // Refresh data when panel becomes visible
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {}
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {}
        });
    }

    // --- DATABASE INTERACTION METHODS ---

    private void loadAllOrders() {
        Connection conn = null;
        PreparedStatement pstmtOrders = null;
        ResultSet rsOrders = null;
        List<Order> allOrders = new ArrayList<>();

        try {
            conn = DBConnection.getConnection();
            // Fetch all orders with supplier details
            String sql = "SELECT o.order_id, o.supplier_id, s.name AS supplier_name, s.company, s.contact, " +
                         "o.processed_by, o.total_amount, o.status " +
                         "FROM orders o JOIN suppliers s ON o.supplier_id = s.supplier_id ORDER BY o.order_id DESC";
            pstmtOrders = conn.prepareStatement(sql);
            rsOrders = pstmtOrders.executeQuery();

            while (rsOrders.next()) {
                String orderId = rsOrders.getString("order_id");
                String supplierId = rsOrders.getString("supplier_id");
                String supplierName = rsOrders.getString("supplier_name");
                String supplierCompany = rsOrders.getString("company");
                String supplierContact = rsOrders.getString("contact");
                String processedBy = rsOrders.getString("processed_by");
                String status = rsOrders.getString("status");

                Supplier supplier = new Supplier(supplierId, supplierName, supplierCompany, supplierContact);
                Order order = new Order(orderId, supplier, processedBy, status);

                // Fetch items for each order
                List<OrderItem> items = getOrderItemsForOrder(orderId, conn); // Pass connection to reuse
                order.setOrderItems(items);
                allOrders.add(order);
            }
        } catch (SQLException e) {
            System.err.println("Database error loading all orders: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading orders from DB. See console.", "DB Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (rsOrders != null) rsOrders.close();
                if (pstmtOrders != null) pstmtOrders.close();
                if (conn != null) conn.close(); // Close connection here
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
        setTableData(allOrders);
    }

    private List<Order> searchOrdersFromDB(String query, String searchType) {
        Connection conn = null;
        PreparedStatement pstmtOrders = null;
        ResultSet rsOrders = null;
        List<Order> matchedOrders = new ArrayList<>();

        try {
            conn = DBConnection.getConnection();
            String sql;
            if ("Order ID".equals(searchType)) {
                sql = "SELECT o.order_id, o.supplier_id, s.name AS supplier_name, s.company, s.contact, " +
                      "o.processed_by, o.total_amount, o.status " +
                      "FROM orders o JOIN suppliers s ON o.supplier_id = s.supplier_id " +
                      "WHERE o.order_id LIKE ? ORDER BY o.order_id DESC"; // Use LIKE for partial matching
            } else { // Supplier Name
                sql = "SELECT o.order_id, o.supplier_id, s.name AS supplier_name, s.company, s.contact, " +
                      "o.processed_by, o.total_amount, o.status " +
                      "FROM orders o JOIN suppliers s ON o.supplier_id = s.supplier_id " +
                      "WHERE s.name LIKE ? ORDER BY o.order_id DESC"; // Use LIKE for partial matching
            }
            pstmtOrders = conn.prepareStatement(sql);
            pstmtOrders.setString(1, "%" + query + "%"); // Wildcard for partial match
            rsOrders = pstmtOrders.executeQuery();

            while (rsOrders.next()) {
                String orderId = rsOrders.getString("order_id");
                String supplierId = rsOrders.getString("supplier_id");
                String supplierName = rsOrders.getString("supplier_name");
                String supplierCompany = rsOrders.getString("company");
                String supplierContact = rsOrders.getString("contact");
                String processedBy = rsOrders.getString("processed_by");
                String status = rsOrders.getString("status");

                Supplier supplier = new Supplier(supplierId, supplierName, supplierCompany, supplierContact);
                Order order = new Order(orderId, supplier, processedBy, status);

                // Fetch items for each order
                List<OrderItem> items = getOrderItemsForOrder(orderId, conn); // Pass connection
                order.setOrderItems(items);
                matchedOrders.add(order);
            }
        } catch (SQLException e) {
            System.err.println("Database error searching orders: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error searching orders in DB. See console.", "DB Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (rsOrders != null) rsOrders.close();
                if (pstmtOrders != null) pstmtOrders.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
        return matchedOrders;
    }

    // Helper method to fetch order items for a given order ID
    // Accepts a Connection object to avoid opening/closing multiple times
//    private List<OrderItem> getOrderItemsForOrder(String orderId, Connection existingConn) throws SQLException {
//        PreparedStatement pstmtItems = null;
//        ResultSet rsItems = null;
//        List<OrderItem> orderItems = new ArrayList<>();
//        try {
//            String sqlItems = "SELECT oi.medicine_id, m.name AS medicine_name, m.price, m.stock_quantity,  m.quantity " +
//                              "FROM order_items oi JOIN medicine m ON oi.medicine_id = m.medicine_id " +
//                              "WHERE oi.order_id = ?";
//            pstmtItems = existingConn.prepareStatement(sqlItems);
//            pstmtItems.setString(1, orderId);
//            rsItems = pstmtItems.executeQuery();
//
//            while (rsItems.next()) {
//                Medicine medicine = new Medicine(
//                    rsItems.getString("medicine_id"),
//                    rsItems.getString("medicine_name"),
//                    rsItems.getDouble("price"),
//                    rsItems.getInt("stock_quantity"),
//                    rsItems.getString("supplier_id")
//                );
//                int quantity = rsItems.getInt("quantity");
//                orderItems.add(new OrderItem(medicine, quantity));
//            }
//        } finally {
//            if (rsItems != null) rsItems.close();
//            if (pstmtItems != null) pstmtItems.close();
//        }
//        return orderItems;
//    }

    private List<OrderItem> getOrderItemsForOrder(String orderId, Connection existingConn) throws SQLException {
    PreparedStatement pstmtItems = null;
    ResultSet rsItems = null;
    List<OrderItem> orderItems = new ArrayList<>();

    try {
        String sqlItems = "SELECT oi.medicine_id, m.name AS medicine_name, m.price, m.stock_quantity, " +
                          "m.expiry_date, m.type, m.reorder_level, m.requires_prescription, m.manufacturer_id, oi.quantity " +
                          "FROM order_items oi JOIN medicine m ON oi.medicine_id = m.medicine_id " +
                          "WHERE oi.order_id = ?";
        pstmtItems = existingConn.prepareStatement(sqlItems);
        pstmtItems.setString(1, orderId);
        rsItems = pstmtItems.executeQuery();

        while (rsItems.next()) {
            String manufacturerId = rsItems.getString("manufacturer_id");

            // 👇 Get Manufacturer object using manager
            Manufacturer manufacturer = manufacturerManager.getManufacturerById(manufacturerId);

            // 👇 Create Medicine with full data
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
    } finally {
        if (rsItems != null) rsItems.close();
        if (pstmtItems != null) pstmtItems.close();
    }
    return orderItems;
}

    private void setTableData(List<Order> orders) {
        String[] columns = {"Order ID", "Supplier", "Status", "Processed By", "Total Amount", "Number of Items"}; // Consistent column names

        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setColumnIdentifiers(columns);
        model.setRowCount(0); // Clear existing rows

        if (orders.isEmpty()) {
            // Optionally, display a message in the table or just leave it empty
            // model.addRow(new Object[]{"", "No orders found.", "", "", "", ""});
            return;
        }

        for (Order o : orders) {
            model.addRow(new Object[]{
                o.getOrderID(),
                o.getSupplier().getName(),
                o.getStatus(),
                o.getProcessedBy(), // Use getProcessedBy
                String.format("Rs. %.2f", o.calculateTotalAmount()), // Use calculateTotalAmount
                o.getOrderItems() != null ? o.getOrderItems().size() : 0 // Use getOrderItems(), handle null
            });
        }
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
