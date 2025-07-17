/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package pharmacymanagmentsystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author mapple.pk
 */
public class SearchMedicinePanel extends javax.swing.JPanel {

    /**
     * Creates new form SearchMedicinePanel
     */
    public SearchMedicinePanel() {
        initComponents();
    }
    private JComboBox<String> searchTypeCombo;
    private JTextField searchField;
    private JTable resultTable;
    private DefaultTableModel tableModel;
    private JButton searchButton, deleteButton;

    private MedicineManager manager;
    private Medicine selectedMedicine;

    public SearchMedicinePanel (MedicineManager manager) {
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
       
        // Search Action
//        searchButton.addActionListener(e -> {
//            String query = searchField.getText().trim();
//            tableModel.setRowCount(0); // Clear table
//            selectedMedicine = null;
//            deleteButton.setEnabled(false);
//
//            if (query.isEmpty()) {
//                JOptionPane.showMessageDialog(this, "Please enter search text.");
//                return;
//            }
//
//            String type = (String) searchTypeCombo.getSelectedItem();
//            Medicine med = null;
//            if (type.equals("Search by ID")) {
//                med = manager.findMedicineByID(query);
//            } else {
//                med = manager.findMedicineByName(query);
//            }
//
//            if (med != null) {
//                tableModel.addRow(new Object[]{
//                    med.getMedicineID(),
//                    med.getName(),
//                    med.getType(),
//                    med.getPrice(),
//                    med.getStockQuantity(),
//                    med.getExpiryDate(),
//                    med.getReorderLevel(),
//                    med.getRequiresPrescription(),
//                    med.getManufacturer().getName()
//                });
//                selectedMedicine = med;
//                deleteButton.setEnabled(true);
//            } else {
//                JOptionPane.showMessageDialog(this, "Medicine not found.");
//            }
//        });
// Key listener for real-time suggestions
searchButton.addActionListener(e -> {
    String query = searchField.getText().trim();
    tableModel.setRowCount(0); // Clear previous results
    selectedMedicine = null;

    if (query.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please enter search text.");
        return;
    }

    String type = (String) searchTypeCombo.getSelectedItem();
    if (type.equals("Search by ID")) {
        Medicine med = manager.findMedicineByID_DB(query);
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
        } else {
            JOptionPane.showMessageDialog(this, "Medicine not found.");
        }
    }
});

searchField.addKeyListener(new KeyAdapter() {
    @Override
    public void keyReleased(KeyEvent e) {
        String type = (String) searchTypeCombo.getSelectedItem();
        if (type.equals("Search by Name")) {
            String query = searchField.getText().trim();
            tableModel.setRowCount(0); // Clear table

            List<Medicine> results = manager.searchMedicinesByName_DB(query);
            for (Medicine med : results) {
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
            }
        }
    }
});

        // Table selection listener
        resultTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = resultTable.getSelectedRow();
            if (selectedRow >= 0) {
                String medID = (String) resultTable.getValueAt(selectedRow, 0);
                selectedMedicine = manager.findMedicineByID_DB(medID);
             
            }
        });

        // Delete action
        

        // Add to panel
        add(topPanel, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);
        
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
