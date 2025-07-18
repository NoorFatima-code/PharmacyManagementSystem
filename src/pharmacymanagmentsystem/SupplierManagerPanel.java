/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package pharmacymanagmentsystem;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
/**
 *
 * @author HP
 */
public class SupplierManagerPanel extends javax.swing.JPanel {
private CardLayout cardLayout;
   private JPanel contentPanel;
 
   private Supplier_Manager supplierManager;
 
   public SupplierManagerPanel(Supplier_Manager manager) {
       this.supplierManager = manager;
       setLayout(new BorderLayout());
 
       // Top Buttons
       JPanel buttonPanel = new JPanel();
       JButton addBtn = new JButton("Add Supplier");
       JButton viewBtn = new JButton("View Suppliers");
       JButton updateBtn = new JButton("Update Supplier");
       JButton deleteBtn = new JButton("Delete Supplier");
       JButton searchBtn = new JButton("Search Supplier");
 
       buttonPanel.add(addBtn);
       buttonPanel.add(viewBtn);
       buttonPanel.add(updateBtn);
       buttonPanel.add(deleteBtn);
       buttonPanel.add(searchBtn);
 
       // Center Card Panel
       cardLayout = new CardLayout();
       contentPanel = new JPanel(cardLayout);
 
       contentPanel.add(new AddSupplierPanel(supplierManager), "ADD");
       contentPanel.add(new ViewAllSuppliersPanel(supplierManager), "VIEW");
       contentPanel.add(new UpdateSupplierPanel(supplierManager), "UPDATE");
       contentPanel.add(new DeleteSupplierPanel(supplierManager), "DELETE");
       contentPanel.add(new SearchSupplierPanel(supplierManager), "SEARCH");
 
       // Button Actions
       addBtn.addActionListener((ActionEvent e) -> cardLayout.show(contentPanel, "ADD"));
       viewBtn.addActionListener((ActionEvent e) -> cardLayout.show(contentPanel, "VIEW"));
       updateBtn.addActionListener((ActionEvent e) -> cardLayout.show(contentPanel, "UPDATE"));
       deleteBtn.addActionListener((ActionEvent e) -> cardLayout.show(contentPanel, "DELETE"));
       searchBtn.addActionListener((ActionEvent e) -> cardLayout.show(contentPanel, "SEARCH"));
 
       add(buttonPanel, BorderLayout.NORTH);
       add(contentPanel, BorderLayout.CENTER);
   }
 
    
    /**
     * Creates new form SupplierManagerPanel
     */
    public SupplierManagerPanel() {
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


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
