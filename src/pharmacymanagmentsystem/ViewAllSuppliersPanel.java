                

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package pharmacymanagmentsystem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
//import java.util.List;
import java.awt.event.ActionEvent; // For Action Listener
import java.awt.event.ActionListener; // For Action Listener
import java.sql.Connection;        // For database connection
import java.sql.PreparedStatement; // For prepared SQL statements
import java.sql.ResultSet;         // For retrieving query results
import java.sql.SQLException;      // For handling SQL exceptions
import pharmacymanagmentsystem.DBConnection;
/**
 *
 * @author HP
 */
public class ViewAllSuppliersPanel extends javax.swing.JPanel {

    /**
     * Creates new form ViewAllSuppliersPanel
     */
     private JTable suppliersTable; // The table to display supplier data
   private DefaultTableModel tableModel; // The model that holds the data for the table
 
   private JTextField searchField;
   private JComboBox<String> searchTypeBox;
   private JButton searchBtn;
   private JButton refreshBtn;
   private Supplier_Manager manager;
 
   // We no longer need the Supplier_Manager instance if all data operations are direct DB calls.
   // private Supplier_Manager manager;
 
   /**
   /**
    * Creates new form ViewAllSuppliersPanel
    */
   public ViewAllSuppliersPanel() {
       initComponents(); // Auto-generated GUI builder code
       initializeCustomComponents(); // Your custom components and logic
       loadAllSuppliers(); // Load all suppliers when the panel is first created
   }
   /**
    * This constructor can be used if you intend to pass a manager object,
    * but the data operations will be handled directly via JDBC.
    * @param manager An instance of Supplier_Manager (not used for DB operations in this version)
    */
  public ViewAllSuppliersPanel(Supplier_Manager manager) {
          initComponents();
       // this.manager = manager; // No longer storing the manager if all data is from DB
       initializeCustomComponents();
       loadAllSuppliers(); // Load all suppliers when the panel is first created
   }
       private void initializeCustomComponents() {
           setLayout(new BorderLayout(10, 10));// Use BorderLayout for the main panel
 
       // Top Panel (Search Section)
       JPanel topPanel = new JPanel(new FlowLayout());
 
       searchField = new JTextField(15);
       searchTypeBox = new JComboBox<>(new String[]{"Search by ID", "Search by Name"});
       searchBtn = new JButton("Search");
       refreshBtn = new JButton("Refresh All");
 
       topPanel.add(new JLabel("Search:"));
       topPanel.add(searchField);
       topPanel.add(searchTypeBox);
       topPanel.add(searchBtn);
       topPanel.add(refreshBtn);
 
       // --- Table Setup ---
       String[] columnNames = {"Supplier ID", "Name", "Company", "Contact"};
       tableModel = new DefaultTableModel(columnNames, 0) {
           @Override
           public boolean isCellEditable(int row, int column) {
               // Make all cells non-editable in the table
               return false;
           }
       };
 
       suppliersTable = new JTable(tableModel);
       suppliersTable.setFillsViewportHeight(true); // Table fills the viewport height
       JScrollPane scrollPane = new JScrollPane(suppliersTable); // Add scroll capability
 
       // --- Title Label ---
       JLabel titleLabel = new JLabel("All Suppliers List", SwingConstants.CENTER);
       titleLabel.setFont(new Font("Arial", Font.BOLD, 20)); // Larger, bold font
       titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0)); // Padding
 
       // Add components to the main panel
       add(titleLabel, BorderLayout.NORTH); // Title at the very top
       add(topPanel, BorderLayout.PAGE_START); // Search panel below the title
       add(scrollPane, BorderLayout.CENTER); // Table in the center
 
 
// --- Action listeners ---
 
       // Refresh Button: Loads all suppliers from the database
       refreshBtn.addActionListener(e -> loadAllSuppliers());
 
       // Search Button: Searches for a supplier in the database
       searchBtn.addActionListener(e -> {
           String query = searchField.getText().trim();
           if (query.isEmpty()) {
               JOptionPane.showMessageDialog(this, "Enter a search term.", "Input Required", JOptionPane.WARNING_MESSAGE);
               loadAllSuppliers(); // If search field is empty, show all
               return;
           }
           searchSuppliersInDatabase(query, (String) searchTypeBox.getSelectedItem());
       });
   }
 
   /**
    * Loads all supplier data from the database and populates the JTable.
    */
   private void loadAllSuppliers() {
       // Clear existing data from the table model
       tableModel.setRowCount(0);
 
       Connection conn = null;
       PreparedStatement pstmt = null;
       ResultSet rs = null;
 
       try {
           conn = DBConnection.getConnection(); // Get database connection
           // SQL query to select all supplier data, ordered by ID
           String sql = "SELECT supplier_id, name, company, contact FROM suppliers ORDER BY supplier_id";
           pstmt = conn.prepareStatement(sql);
           rs = pstmt.executeQuery(); // Execute the query
 
           int rowCount = 0; // Counter for rows added
           // Iterate through the ResultSet and add rows to the table model
           while (rs.next()) {
               String supplierId = rs.getString("supplier_id");
               String name = rs.getString("name");
               String company = rs.getString("company");
               String contact = rs.getString("contact");
 
               // Add a new row to the table model
               tableModel.addRow(new Object[]{supplierId, name, company, contact});
               rowCount++;
           }
 
           if (rowCount == 0) {
               JOptionPane.showMessageDialog(this, "No suppliers found in the database.", "No Data", JOptionPane.INFORMATION_MESSAGE);
           }
 
       } catch (SQLException ex) {
           JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
           ex.printStackTrace(); // Print stack trace for debugging
       } catch (Exception ex) {
           JOptionPane.showMessageDialog(this, "An unexpected error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
           ex.printStackTrace();
       } finally {
           // Close JDBC resources in a finally block to ensure they are always closed
           try {
               if (rs != null) rs.close();
               if (pstmt != null) pstmt.close();
               if (conn != null) conn.close();
           } catch (SQLException closeEx) {
               JOptionPane.showMessageDialog(this, "Error closing database resources: " + closeEx.getMessage(), "Resource Close Error", JOptionPane.ERROR_MESSAGE);
               closeEx.printStackTrace();
           }
       }
   }
 
   /**
    * Searches for suppliers in the database based on the query and search type.
    * @param query The search term (ID or Name).
    * @param searchType "Search by ID" or "Search by Name".
    */
   private void searchSuppliersInDatabase(String query, String searchType) {
       tableModel.setRowCount(0); // Clear existing data
 
       Connection conn = null;
       PreparedStatement pstmt = null;
       ResultSet rs = null;
 
       try {
           conn = DBConnection.getConnection();
           String sql;
 
           if ("Search by ID".equals(searchType)) {
               sql = "SELECT supplier_id, name, company, contact FROM suppliers WHERE supplier_id = ?";
           } else { // "Search by Name"
               // Using LIKE for partial name matching, with % wildcards
               sql = "SELECT supplier_id, name, company, contact FROM suppliers WHERE name LIKE ?";
               query = "%" + query + "%"; // Add wildcards for LIKE search
           }
 
           pstmt = conn.prepareStatement(sql);
           pstmt.setString(1, query);
           rs = pstmt.executeQuery();
 
           int rowCount = 0;
           while (rs.next()) {
               String supplierId = rs.getString("supplier_id");
               String name = rs.getString("name");
               String company = rs.getString("company");
               String contact = rs.getString("contact");
               tableModel.addRow(new Object[]{supplierId, name, company, contact});
               rowCount++;
           }
 
           if (rowCount == 0) {
               JOptionPane.showMessageDialog(this, "No suppliers found matching your search.", "No Results", JOptionPane.INFORMATION_MESSAGE);
           }
 
       } catch (SQLException ex) {
           JOptionPane.showMessageDialog(this, "Database Error during search: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
           ex.printStackTrace();
       } catch (Exception ex) {
           JOptionPane.showMessageDialog(this, "An unexpected error occurred during search: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
           ex.printStackTrace();
       } finally {
           try {
               if (rs != null) rs.close();
               if (pstmt != null) pstmt.close();
               if (conn != null) conn.close();
           } catch (SQLException closeEx) {
               JOptionPane.showMessageDialog(this, "Error closing search resources: " + closeEx.getMessage(), "Resource Error", JOptionPane.ERROR_MESSAGE);
               closeEx.printStackTrace();
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