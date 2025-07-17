/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package pharmacymanagmentsystem; 
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import pharmacymanagmentsystem.*;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import pharmacymanagmentsystem.DBConnection;


public class DeleteManufacturerPanel extends javax.swing.JPanel {

  private JTextField idField;
    private Manufacturer_Manager manager;

    public DeleteManufacturerPanel(Manufacturer_Manager manager) {
        this.manager = manager;
        setLayout(new BorderLayout(40, 40));

        // ===== Top Panel =====
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Enter Manufacturer ID to Delete:"));
        idField = new JTextField(15);
        topPanel.add(idField);

        JButton deleteBtn = new JButton("Delete");
        topPanel.add(deleteBtn);

        // ===== Status Label =====
        JLabel statusLabel = new JLabel(" ");
        statusLabel.setForeground(Color.BLUE);

        // ===== Add Components =====
        add(topPanel, BorderLayout.NORTH);
        add(statusLabel, BorderLayout.SOUTH);

        // ===== Delete Logic =====
        deleteBtn.addActionListener(e -> {
            String id = idField.getText().trim();

            if (id.isEmpty()) {
                statusLabel.setText("❗ Please enter Manufacturer ID.");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete manufacturer with ID: " + id + "?",
                    "Confirm Deletion",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                boolean deleted = manager.deleteManufacturerFromDatabase(id);
                if (deleted) {
                    idField.setText("");
                    statusLabel.setText("✅ Deleted successfully.");
                    JOptionPane.showMessageDialog(this, "Manufacturer deleted successfully.");
                } else {
                    statusLabel.setText("❌ Could not delete manufacturer.");
                    JOptionPane.showMessageDialog(this, "Failed to delete manufacturer from database.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

    }
    
    public List<Manufacturer> searchManufacturers(String keyword, String column) {
    List<Manufacturer> list = new ArrayList<>();
    String query = "SELECT * FROM manufacturer WHERE " + column + " LIKE ?";
    
    try (Connection conn = DBConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(query)) {
        
        stmt.setString(1, "%" + keyword + "%");
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
            Manufacturer m = new Manufacturer(
                rs.getString("manufacturer_id"),
                rs.getString("name"),
                rs.getString("address"),
                rs.getString("contact"),
                rs.getString("email"),
                rs.getString("country")
            );
            list.add(m);
        }
    } catch (Exception e) {
        System.out.println("Error searching manufacturers: " + e.getMessage());
    }
    
    return list;
    }
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
