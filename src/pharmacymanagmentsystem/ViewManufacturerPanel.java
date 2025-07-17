/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package pharmacymanagmentsystem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ViewManufacturerPanel extends javax.swing.JPanel {

   
//    private String check(String value) {
//        return (value == null || value.isEmpty()) ? "null" : value;
//    }
    
  private JTable table;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JComboBox<String> searchTypeCombo;
    private Manufacturer_Manager manager;

    public ViewManufacturerPanel(Manufacturer_Manager manager) {
        this.manager = manager;
        setLayout(new BorderLayout(10, 10));

        // ===== Table Setup =====
        String[] columns = {"ID", "Name", "Address", "Contact", "Email", "Country"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        // ===== Search Panel =====
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        searchPanel.add(new JLabel("Search:"));
        searchField = new JTextField(15);
        searchPanel.add(searchField);

        searchPanel.add(new JLabel("Search by:"));
        searchTypeCombo = new JComboBox<>(new String[]{"ID", "Name"});
        searchPanel.add(searchTypeCombo);

        JButton searchBtn = new JButton("Search");
        JButton refreshBtn = new JButton("Refresh");

        searchPanel.add(searchBtn);
        searchPanel.add(refreshBtn);

        // ===== Button Actions =====
        searchBtn.addActionListener(e -> performSearch());
        refreshBtn.addActionListener(e -> loadAllManufacturers());

        // ===== Add to Panel =====
        add(searchPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        loadAllManufacturers(); // Load initial data
    }

//    private void loadAllManufacturers() {
//        tableModel.setRowCount(0); // clear previous
//        List<Manufacturer> list = manager.getAllManufacturers();
//
//        for (Manufacturer m : list) {
//            tableModel.addRow(new Object[]{
//                check(m.getManufacturerID()),
//                check(m.getName()),
//                check(m.getAddress()),
//                check(m.getContact()),
//                check(m.getEmail()),
//                check(m.getCountry())
//            });
//        }
//    }
    
    private void loadAllManufacturers() {
    tableModel.setRowCount(0);
    List<Manufacturer> list = manager.getAllManufacturers();

    System.out.println("Manufacturers loaded: " + list.size()); // 👉 Print how many manufacturers loaded

    for (Manufacturer m : list) {
        System.out.println("DEBUG: " + m.getManufacturerID() + ", " + m.getName() + ", " + m.getAddress());

        tableModel.addRow(new Object[]{
            check(m.getManufacturerID()),
            check(m.getName()),
            check(m.getAddress()),
            check(m.getContact()),
            check(m.getEmail()),
            check(m.getCountry())
        });
    }
}


    private void performSearch() {
        String keyword = searchField.getText().trim().toLowerCase();
        String searchBy = (String) searchTypeCombo.getSelectedItem();

        tableModel.setRowCount(0); // clear table

        List<Manufacturer> list = manager.getAllManufacturers();

        for (Manufacturer m : list) {
            String valueToMatch = "ID".equals(searchBy) ? m.getManufacturerID() : m.getName();

            if (valueToMatch != null && valueToMatch.toLowerCase().contains(keyword)) {
                tableModel.addRow(new Object[]{
                    check(m.getManufacturerID()),
                    check(m.getName()),
                    check(m.getAddress()),
                    check(m.getContact()),
                    check(m.getEmail()),
                    check(m.getCountry())
                });
            }
        }
    }

//    private String check(String value) {
//        return (value == null || value.isEmpty()) ? "null" : value;
//    }
    
    private String check(String value) {
    return (value == null || value.trim().isEmpty()) ? "" : value;
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
