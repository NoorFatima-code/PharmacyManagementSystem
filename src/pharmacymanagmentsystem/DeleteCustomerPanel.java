/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package pharmacymanagmentsystem;

import javax.swing.*;
import java.awt.*;

public class DeleteCustomerPanel extends javax.swing.JPanel {

     private Customer_Manager customerManager;
    private Customer selectedCustomer = null;

     public DeleteCustomerPanel(Customer_Manager customerManager) {
        this.customerManager = customerManager;
        setLayout(new BorderLayout(10, 10));

        // 🌟 Search Panel (Top)
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        JLabel searchTypeLabel = new JLabel("Search By:");
        String[] searchOptions = {"Customer ID", "Customer Name"};
        JComboBox<String> searchTypeBox = new JComboBox<>(searchOptions);

        JTextField searchField = new JTextField(15);
        JButton searchBtn = new JButton("🔍");

        searchBtn.setPreferredSize(new Dimension(50, 25));
        searchPanel.add(searchTypeLabel);
        searchPanel.add(searchTypeBox);
        searchPanel.add(searchField);
        searchPanel.add(searchBtn);

        // 🗑️ Delete Button (Bottom)
        JButton deleteBtn = new JButton("🗑️ Delete Customer");
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(deleteBtn);

        // Add components
        add(searchPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);

        // 🔍 Search Action
        searchBtn.addActionListener(e -> {
            String input = searchField.getText().trim();
            selectedCustomer = null;

            if (input.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter something to search.");
                return;
            }

            customerManager.loadCustomersFromDatabase(); // ✅ Always get latest from DB
            String selectedOption = (String) searchTypeBox.getSelectedItem();

            for (Customer c : customerManager.getCustomerList()) {
                if ((selectedOption.equals("Customer ID") && c.getCustomerID().equalsIgnoreCase(input)) ||
                    (selectedOption.equals("Customer Name") && c.getName().equalsIgnoreCase(input))) {
                    selectedCustomer = c;
                    break;
                }
            }

            if (selectedCustomer != null) {
                JOptionPane.showMessageDialog(this, "Customer found. Click Delete to remove.");
            } else {
                JOptionPane.showMessageDialog(this, "Customer not found!");
            }
        });

        // ❌ Delete Action
        deleteBtn.addActionListener(e -> {
            if (selectedCustomer == null) {
                JOptionPane.showMessageDialog(this, "Please search for a valid customer first.");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete this customer?",
                    "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                // ✅ Delete from database
                boolean success = customerManager.deleteCustomerFromDatabase(selectedCustomer.getCustomerID());

                if (success) {
                    JOptionPane.showMessageDialog(this, "Customer deleted successfully!");
                    selectedCustomer = null;
                    searchField.setText("");
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to delete customer from database.");
                }
            }
        });
    }

      public DeleteCustomerPanel() {
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
