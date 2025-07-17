/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package pharmacymanagmentsystem;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

/**
 *
 * @author mapple.pk
 */
public class ExpiredMedicinePanel extends javax.swing.JPanel {
//private MedicineManager manager;
//
//    public ExpiredMedicinePanel(MedicineManager manager) {
//        this.manager = manager; // ✅ Important fix
//
//        setLayout(new BorderLayout());
//
//        JTextField dateField = new JTextField("YYYY-MM-DD");
//        JTable resultTable = new JTable();
//        JButton checkButton = new JButton("Check Expired Medicines");
//
//        checkButton.addActionListener(e -> {
//            String date = dateField.getText();
//            List<Medicine> expired = manager.getExpiredMedicines(date); // ✅ Now this will work
//
//            if (expired.isEmpty()) {
//                JOptionPane.showMessageDialog(this, "No expired medicines.");
//                resultTable.setModel(new DefaultTableModel());
//            } else {
//                String[] columns = {"ID", "Name", "Type", "Price", "Stock", "Expiry", "Reorder", "Prescription", "Manufacturer"};
//                String[][] data = new String[expired.size()][columns.length];
//                for (int i = 0; i < expired.size(); i++) {
//                    Medicine m = expired.get(i);
//                    data[i][0] = m.getMedicineID();
//                    data[i][1] = m.getName();
//                    data[i][2] = m.getType();
//                    data[i][3] = String.valueOf(m.getPrice());
//                    data[i][4] = String.valueOf(m.getStockQuantity());
//                    data[i][5] = m.getExpiryDate();
//                    data[i][6] = String.valueOf(m.getReorderLevel());
//                    data[i][7] = String.valueOf(m.getRequiresPrescription());
//                    data[i][8] = m.getManufacturer().getName();
//                }
//                resultTable.setModel(new DefaultTableModel(data, columns));
//
//                int confirm = JOptionPane.showConfirmDialog(this, "Remove all expired medicines?", "Confirm", JOptionPane.YES_NO_OPTION);
//                if (confirm == JOptionPane.YES_OPTION) {
//                    manager.removeExpiredMedicines(expired);
//                    JOptionPane.showMessageDialog(this, "Expired medicines removed.");
//                }
//            }
//        });
//
//        add(dateField, BorderLayout.NORTH);
//        add(checkButton, BorderLayout.CENTER);
//        add(new JScrollPane(resultTable), BorderLayout.SOUTH);
//    }
    
//    private MedicineManager manager;
//
//    public ExpiredMedicinePanel(MedicineManager manager) {
//        this.manager = manager;
//        setLayout(new BorderLayout(10, 10));
//
//        // Year Dropdown
//        JComboBox<String> yearBox = new JComboBox<>();
//        for (int y = 2005; y <= 3000; y++) {
//            yearBox.addItem(String.valueOf(y));
//        }
//
//        // Month Dropdown
//        JComboBox<String> monthBox = new JComboBox<>();
//        for (int m = 1; m <= 12; m++) {
//            monthBox.addItem(String.format("%02d", m));
//        }
//
//        // Day Dropdown
//        JComboBox<String> dayBox = new JComboBox<>();
//        for (int d = 1; d <= 31; d++) {
//            dayBox.addItem(String.format("%02d", d));
//        }
//
//        // Button to Check
//        JButton checkButton = new JButton("Check Expired Medicines");
//
//        // Table
//        JTable resultTable = new JTable();
//        JScrollPane scrollPane = new JScrollPane(resultTable);
//
//        // Top Panel for Date
//        JPanel datePanel = new JPanel();
//        datePanel.add(new JLabel("Select Date:"));
//        datePanel.add(yearBox);
//        datePanel.add(monthBox);
//        datePanel.add(dayBox);
//        datePanel.add(checkButton);
//
//        // Action
//        checkButton.addActionListener(e -> {
//            String year = (String) yearBox.getSelectedItem();
//            String month = (String) monthBox.getSelectedItem();
//            String day = (String) dayBox.getSelectedItem();
//
//            String selectedDate = year + "-" + month + "-" + day;
//            List<Medicine> expired = manager.getExpiredMedicines(selectedDate);
//
//            if (expired.isEmpty()) {
//                JOptionPane.showMessageDialog(this, "No expired medicines found.");
//                resultTable.setModel(new DefaultTableModel());
//            } else {
//                int confirm = JOptionPane.showConfirmDialog(this, "Remove all expired medicines?", "Confirm", JOptionPane.YES_NO_OPTION);
//                if (confirm == JOptionPane.YES_OPTION) {
//                    manager.removeExpiredMedicines(expired);
//                    JOptionPane.showMessageDialog(this, "Expired medicines removed.");
//                      expired = manager.getExpiredMedicines(selectedDate); // refresh expired list
//                }
//
//                String[] cols = {"ID", "Name", "Type", "Price", "Stock", "Expiry", "Reorder", "Prescription", "Manufacturer"};
//                String[][] data = new String[expired.size()][cols.length];
//                for (int i = 0; i < expired.size(); i++) {
//                    Medicine m = expired.get(i);
//                    data[i][0] = m.getMedicineID();
//                    data[i][1] = m.getName();
//                    data[i][2] = m.getType();
//                    data[i][3] = String.valueOf(m.getPrice());
//                    data[i][4] = String.valueOf(m.getStockQuantity());
//                    data[i][5] = m.getExpiryDate();
//                    data[i][6] = String.valueOf(m.getReorderLevel());
//                    data[i][7] = String.valueOf(m.getRequiresPrescription());
//                    data[i][8] = m.getManufacturer().getName();
//                }
//
//                resultTable.setModel(new DefaultTableModel(data, cols));
//            }
//        });
//
//        add(datePanel, BorderLayout.NORTH);
//        add(scrollPane, BorderLayout.CENTER);
//    }
     private MedicineManager manager;
    private JTable resultTable;
    private List<Medicine> expiredList = new ArrayList<>();

    public ExpiredMedicinePanel(MedicineManager manager) {
        this.manager = manager;
        setLayout(new BorderLayout(10, 10));

        // Date selectors
        JComboBox<String> yearBox = new JComboBox<>();
        for (int y = 2005; y <= 3000; y++) yearBox.addItem(String.valueOf(y));

        JComboBox<String> monthBox = new JComboBox<>();
        for (int m = 1; m <= 12; m++) monthBox.addItem(String.format("%02d", m));

        JComboBox<String> dayBox = new JComboBox<>();
        for (int d = 1; d <= 31; d++) dayBox.addItem(String.format("%02d", d));

        JButton checkButton = new JButton("Check Expired Medicines");
        JButton removeButton = new JButton("Remove Expired Medicines");
        removeButton.setEnabled(false); // Disable by default

        // Table setup
        String[] cols = {"ID", "Name", "Type", "Price", "Stock", "Expiry", "Reorder", "Prescription", "Manufacturer"};
        DefaultTableModel tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int column, int row) {
                return false;
            }
        };
        resultTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(resultTable);

        // Date Panel
        JPanel datePanel = new JPanel();
        datePanel.add(new JLabel("Select Date:"));
        datePanel.add(yearBox);
        datePanel.add(monthBox);
        datePanel.add(dayBox);
        datePanel.add(checkButton);
        datePanel.add(removeButton);

        // Add to main panel
        add(datePanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Check button logic
        checkButton.addActionListener(e -> {
            String date = yearBox.getSelectedItem() + "-" +
                          monthBox.getSelectedItem() + "-" +
                          dayBox.getSelectedItem();

            expiredList = manager.getExpiredMedicinesFromDatabase(date);
            tableModel.setRowCount(0);

            if (expiredList.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No expired medicines found.");
                removeButton.setEnabled(false);
            } else {
                for (Medicine m : expiredList) {
                    tableModel.addRow(new Object[]{
                        m.getMedicineID(),
                        m.getName(),
                        m.getType(),
                        m.getPrice(),
                        m.getStockQuantity(),
                        m.getExpiryDate(),
                        m.getReorderLevel(),
                        m.getRequiresPrescription(),
                        m.getManufacturer().getName()
                    });
                }
                removeButton.setEnabled(true);
            }
        });

        // Remove button logic
        removeButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure to remove all expired medicines?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                manager.removeExpiredMedicinesFromDatabase(expiredList);
                JOptionPane.showMessageDialog(this, "Expired medicines removed.");
                expiredList.clear();
                tableModel.setRowCount(0);
                removeButton.setEnabled(false);
            }
        });
    }
    /**
     * Creates new form ExpiredMedicinePanel
     */
    public ExpiredMedicinePanel() {
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
