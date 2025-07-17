/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package pharmacymanagmentsystem;

import javax.swing.*;
import java.awt.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.util.List;

/**
 *
 * @author mapple.pk
 */
public class DeleteMedicinePanel extends javax.swing.JPanel {

    /**
     * Creates new form DeleteMedicinePanel
     */
    public DeleteMedicinePanel() {
        initComponents();
    }
//public DeleteMedicinePanel(MedicineManager manager) {
//        setLayout(new BorderLayout());
//        JTextField nameField = new JTextField();
//        JButton deleteButton = new JButton("Delete Medicine");
//
//        deleteButton.addActionListener(e -> {
//            Medicine med = manager.findMedicineByName(nameField.getText());
//            if (med != null) {
//                int confirm = JOptionPane.showConfirmDialog(this, "Are you sure to delete?", "Confirm", JOptionPane.YES_NO_OPTION);
//                if (confirm == JOptionPane.YES_OPTION) {
//                    boolean deleted = manager.deleteMedicine(med);
//                    JOptionPane.showMessageDialog(this, deleted ? "Deleted successfully" : "Failed to delete");
//                }
//            } else {
//                JOptionPane.showMessageDialog(this, "Medicine not found");
//            }
//        });
//
//        add(new JLabel("Enter Medicine Name to Delete:"), BorderLayout.NORTH);
//        add(nameField, BorderLayout.CENTER);
//        add(deleteButton, BorderLayout.SOUTH);
//    }
//    private JComboBox<String> searchTypeCombo;
//    private JTextField searchField;
//    private JTextArea resultArea;
//    private JButton searchButton, deleteButton;
//    private Medicine selectedMedicine;
//
//    public DeleteMedicinePanel(MedicineManager manager) {
//        setLayout(new BorderLayout(10, 10));
//
//        // === TOP PANEL ===
//        JPanel topPanel = new JPanel(new FlowLayout());
//        searchTypeCombo = new JComboBox<>(new String[]{"Search by ID", "Search by Name"});
//        searchField = new JTextField(15);
//        searchButton = new JButton("Search");
//        topPanel.add(searchTypeCombo);
//        topPanel.add(searchField);
//        topPanel.add(searchButton);
//
//        // === CENTER RESULT DISPLAY ===
//        resultArea = new JTextArea(6, 30);
//        resultArea.setEditable(false);
//        JScrollPane scrollPane = new JScrollPane(resultArea);
//
//        // === DELETE BUTTON ===
//        deleteButton = new JButton("Delete Selected Medicine");
//        deleteButton.setEnabled(false); // only enabled when medicine is found
//
//        // === SEARCH ACTION ===
//        searchButton.addActionListener(e -> {
//            String query = searchField.getText().trim();
//            resultArea.setText("");
//            selectedMedicine = null;
//            deleteButton.setEnabled(false);
//
//            if (query.isEmpty()) {
//                JOptionPane.showMessageDialog(this, "Please enter search text.");
//                return;
//            }
//
//            String type = (String) searchTypeCombo.getSelectedItem();
//            if (type.equals("Search by ID")) {
//                selectedMedicine = manager.findMedicineByID(query);
//            } else {
//                selectedMedicine = manager.findMedicineByName(query);
//            }
//
//            if (selectedMedicine != null) {
//                resultArea.setText(selectedMedicine.displayDetailsAsString());
//                deleteButton.setEnabled(true);
//            } else {
//                resultArea.setText("Medicine not found.");
//            }
//        });
//
//        // === DELETE ACTION ===
//        deleteButton.addActionListener(e -> {
//            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure to delete this medicine?", "Confirm", JOptionPane.YES_NO_OPTION);
//            if (confirm == JOptionPane.YES_OPTION) {
//                boolean success = manager.deleteMedicine(selectedMedicine);
//                if (success) {
//                    JOptionPane.showMessageDialog(this, "Medicine deleted successfully.");
//                    resultArea.setText("");
//                    searchField.setText("");
//                    deleteButton.setEnabled(false);
//                } else {
//                    JOptionPane.showMessageDialog(this, "Failed to delete.");
//                }
//            }
//        });
//
//        // === ADD ALL COMPONENTS ===
//        add(topPanel, BorderLayout.NORTH);
//        add(scrollPane, BorderLayout.CENTER);
//        add(deleteButton, BorderLayout.SOUTH);
//    }

    private JComboBox<String> searchTypeCombo;
    private JTextField searchField;
    private JTable resultTable;
    private DefaultTableModel tableModel;
    private JButton searchButton, deleteButton;

    private MedicineManager manager;
    private Medicine selectedMedicine;

    public DeleteMedicinePanel(MedicineManager manager) {
        this.manager = manager;
        setLayout(new BorderLayout(10, 10));

        // Top Panel
        JPanel topPanel = new JPanel(new FlowLayout());
        searchTypeCombo = new JComboBox<>(new String[]{"Search by ID", "Search by Name"});
        searchField = new JTextField(15);
        searchButton = new JButton("Search");
        topPanel.add(searchTypeCombo);
        topPanel.add(searchField);
        topPanel.add(searchButton);

        // Table for displaying medicine
        String[] columns = {"ID", "Name", "Type", "Price", "Stock", "Expiry", "Reorder", "Prescription", "Manufacturer"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) {
                return false; // Make all cells non-editable

            }
        };

        resultTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(resultTable);

        // Delete Button
        deleteButton = new JButton("Delete Selected Medicine");
        deleteButton.setEnabled(false);

        // Search Action
        searchButton.addActionListener(e -> {
            String query = searchField.getText().trim();
            tableModel.setRowCount(0); // Clear table
            selectedMedicine = null;
            deleteButton.setEnabled(false);

            if (query.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter search text.");
                return;
            }

            String type = (String) searchTypeCombo.getSelectedItem();
            Medicine med = null;
//            if (type.equals("Search by ID")) {
//                med = manager.findMedicineByID(query);
//            } else {
//                med = manager.findMedicineByName(query);
//            }
if (type.equals("Search by ID")) {
    med = manager.findMedicineByID_DB(query);
} else {
    med = manager.findMedicineByName_DB(query);
}


            if (med != null) {
                tableModel.addRow(new Object[]{
                    med.getMedicineID(),
                    med.getName(),
                    med.getType(),
                    med.getPrice(),
                    med.getStockQuantity(),
                    med.getExpiryDate(),
                    med.getReorderLevel(),
                    med.getRequiresPrescription(),
                    med.getManufacturer().getName()
                });
                selectedMedicine = med;
                deleteButton.setEnabled(true);
            } else {
                JOptionPane.showMessageDialog(this, "Medicine not found.");
            }
        });

        // Table selection listener
        resultTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = resultTable.getSelectedRow();
            if (selectedRow >= 0) {
                String medID = (String) resultTable.getValueAt(selectedRow, 0);
                selectedMedicine = manager.findMedicineByID_DB(medID);
                deleteButton.setEnabled(true);
            }
        });

        // Delete action
        deleteButton.addActionListener(e -> {
    if (selectedMedicine != null) {
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure to delete this medicine?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            boolean deleted = manager.softDeleteMedicineInDatabase(selectedMedicine.getMedicineID());
            manager.loadMedicinesFromDatabase(); 
            if (deleted) {
                JOptionPane.showMessageDialog(this, "Medicine deleted successfully.");
                tableModel.setRowCount(0);
                searchField.setText("");
                deleteButton.setEnabled(true);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete.");
            }
        }
    }
});

        // Add to panel
        add(topPanel, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);
        add(deleteButton, BorderLayout.SOUTH);
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
