package pharmacymanagmentsystem;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import pharmacymanagmentsystem.DBConnection;

public class DeleteOrderPanel extends JPanel {

    public DeleteOrderPanel(Order_Manager orderManager) {
        setLayout(new GridLayout(3, 2, 10, 10));

        JTextField orderIdField = new JTextField();

        JButton deleteBtn = new JButton("Delete Order");

        add(new JLabel("Order ID:"));
        add(orderIdField);
        add(new JLabel());
        add(deleteBtn);

        deleteBtn.addActionListener(e -> {
            String orderId = orderIdField.getText().trim();
            if (orderId.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter an Order ID.", "Input Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Order orderToDelete = searchOrderFromDB(orderId);
            if (orderToDelete == null) {
                JOptionPane.showMessageDialog(this, "Order ID not found.", "Deletion Failed", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!orderToDelete.getStatus().equalsIgnoreCase("Pending")) {
                JOptionPane.showMessageDialog(this, "Order cannot be deleted because its status is '" + orderToDelete.getStatus() + "'. Only 'Pending' orders can be deleted.", "Deletion Denied", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete order " + orderId + "?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                boolean deleted = deleteOrderFromDB(orderId);
                if (deleted) {
                    JOptionPane.showMessageDialog(this, "Order deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    orderIdField.setText("");
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to delete order. Check console for details.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    // --- DATABASE INTERACTION METHODS ---

    private Order searchOrderFromDB(String orderId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Order order = null;
        try {
            conn = DBConnection.getConnection();
            // Removed order_date from SELECT query
            String sql = "SELECT o.order_id, o.supplier_id, s.name AS supplier_name, s.company, s.contact, " +
                         "o.processed_by, o.total_amount, o.status " + // No order_date here
                         "FROM orders o JOIN suppliers s ON o.supplier_id = s.supplier_id " +
                         "WHERE o.order_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, orderId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                String supplierId = rs.getString("supplier_id");
                String supplierName = rs.getString("supplier_name");
                String supplierCompany = rs.getString("company");
                String supplierContact = rs.getString("contact");
                Supplier supplier = new Supplier(supplierId, supplierName, supplierCompany, supplierContact);

                String retrievedOrderId = rs.getString("order_id");
                String processedBy = rs.getString("processed_by");
                String status = rs.getString("status");

                // Updated constructor call (no orderDate parameter)
                order = new Order(retrievedOrderId, supplier, processedBy, status);
            }
        } catch (SQLException e) {
            System.err.println("Database error searching for order " + orderId + ": " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error searching for order in DB. See console.", "DB Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
        return order;
    }

    private boolean deleteOrderFromDB(String orderId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBConnection.getConnection();
            String sql = "DELETE FROM orders WHERE order_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, orderId);
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("DEBUG: Order " + orderId + " deleted successfully. Rows affected: " + rowsAffected);
                return true;
            } else {
                System.out.println("DEBUG: No order found with ID " + orderId + " for deletion.");
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Database error deleting order " + orderId + ": " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error during deletion: " + e.getMessage(), "DB Error", JOptionPane.ERROR_MESSAGE);
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
