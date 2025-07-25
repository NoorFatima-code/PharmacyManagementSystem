/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package pharmacymanagmentsystem;
import pharmacymanagmentsystem.DBConnection;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
/**
 *
 * @author HP
 */
public class AddSupplierPanel extends javax.swing.JPanel {

    /**
     * Creates new form AddSupplierPanel
     */
    public AddSupplierPanel() {
        initComponents();
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
  public AddSupplierPanel(Supplier_Manager manager) {
       setLayout(new GridLayout(5, 2, 10, 10));
 
       JTextField idField = new JTextField();
        idField.setEditable(false); // Auto-generated ID
        String autoID = manager.generateSupplierID();
        idField.setText(autoID);
        
       JTextField nameField = new JTextField();
       JTextField companyField = new JTextField();
       JTextField contactField = new JTextField();
       JButton addBtn = new JButton("Add Supplier");
 
       add(new JLabel("Supplier ID:"));
       add(idField);
       add(new JLabel("Name:"));
       add(nameField);
       add(new JLabel("Company:"));
       add(companyField);
       add(new JLabel("Contact:"));
       add(contactField);
       add(new JLabel());
       add(addBtn);
 
       addBtn.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               String id = idField.getText().trim();
               String name = nameField.getText().trim();
               String company = companyField.getText().trim();
               String contact = contactField.getText().trim();
 
               // --- Input Validation ---
               if (id.isEmpty() || name.isEmpty()) {
                   JOptionPane.showMessageDialog(AddSupplierPanel.this, "Supplier ID and Name are required!", "Input Error", JOptionPane.WARNING_MESSAGE);
                   return;
               }
               if (!contact.isEmpty() && !contact.matches("\\d{10}")) {
                   JOptionPane.showMessageDialog(AddSupplierPanel.this, "Contact must be a 10-digit number if provided!", "Input Error", JOptionPane.WARNING_MESSAGE);
                   return;
               }
 
               Connection conn = null;
               PreparedStatement checkStmt = null;
               ResultSet rs = null;
               PreparedStatement insertStmt = null;
 
               try {
                   conn = DBConnection.getConnection(); // Get database connection
 
                   // --- Check if Supplier ID already exists in DB ---
                   String checkSql = "SELECT COUNT(*) FROM suppliers WHERE supplier_id = ?";
                   checkStmt = conn.prepareStatement(checkSql);
                   checkStmt.setString(1, id);
                   rs = checkStmt.executeQuery();
 
                   if (rs.next() && rs.getInt(1) > 0) {
                       JOptionPane.showMessageDialog(AddSupplierPanel.this, "Supplier with this ID already exists!", "Duplicate Supplier", JOptionPane.WARNING_MESSAGE);
                       return; // Exit if supplier exists
                   }
 
                   // --- Insert supplier into DB ---
                   String insertSql = "INSERT INTO suppliers (supplier_id, name, company, contact) VALUES (?, ?, ?, ?)";
                   insertStmt = conn.prepareStatement(insertSql);
                   insertStmt.setString(1, id);
                   insertStmt.setString(2, name);
                   insertStmt.setString(3, company);
                   insertStmt.setString(4, contact); // This will insert an empty string if contact is empty
 
                   int rows = insertStmt.executeUpdate();
 
                   if (rows > 0) {
                       JOptionPane.showMessageDialog(AddSupplierPanel.this, "Supplier added to database successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                       // Clear fields after adding
                       idField.setText("");
                       nameField.setText("");
                       companyField.setText("");
                       contactField.setText("");
                   } else {
                       JOptionPane.showMessageDialog(AddSupplierPanel.this, "Failed to add supplier to DB.", "Database Error", JOptionPane.ERROR_MESSAGE);
                   }
 
               } catch (SQLException ex) {
                   JOptionPane.showMessageDialog(AddSupplierPanel.this, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                   ex.printStackTrace(); // Print stack trace for debugging
               } catch (Exception ex) {
                   JOptionPane.showMessageDialog(AddSupplierPanel.this, "An unexpected error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                   ex.printStackTrace();
               } finally {
                   // --- Close resources in a finally block to ensure they are released ---
                   try {
                       if (rs != null) rs.close();
                       if (checkStmt != null) checkStmt.close();
                       if (insertStmt != null) insertStmt.close();
                       if (conn != null) conn.close();
                   } catch (SQLException closeEx) {
                       JOptionPane.showMessageDialog(AddSupplierPanel.this, "Error closing database resources: " + closeEx.getMessage(), "Resource Close Error", JOptionPane.ERROR_MESSAGE);
                       closeEx.printStackTrace();
                   }
               }
           }
       });
   }
     // Don't need this default constructor if you're not using it
   



    // Variables declaration - do not modify                     
    // End of variables declaration                   

}


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

