/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package pharmacymanagmentsystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import pharmacymanagmentsystem.*;

public class SearchManufacturerPanel extends javax.swing.JPanel {

  
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
  private JTextField searchField;
    private JComboBox<String> searchTypeCombo;
    private JTextArea resultArea;
    private Manufacturer_Manager manager;

    public SearchManufacturerPanel(Manufacturer_Manager manager) {
        this.manager = manager;
        setLayout(new BorderLayout(10, 10));

        // ===== TOP PANEL =====
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Search:"));
        searchField = new JTextField(15);
        topPanel.add(searchField);

        topPanel.add(new JLabel("Search by:"));
        searchTypeCombo = new JComboBox<>(new String[] { "ID", "Name" });
        topPanel.add(searchTypeCombo);

        JButton searchBtn = new JButton("Search");
        topPanel.add(searchBtn);

        // ===== RESULT AREA =====
        resultArea = new JTextArea(8, 30);
        resultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultArea);

        // ===== ADD TO MAIN PANEL =====
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // ===== SEARCH LOGIC =====
        searchBtn.addActionListener(e -> {
    String keyword = searchField.getText().trim();
    String searchBy = (String) searchTypeCombo.getSelectedItem();
    resultArea.setText("");

    if (keyword.isEmpty()) {
        resultArea.setText("Please enter a search keyword.");
        return;
    }

    String column = "manufacturer_id";  // Default
    if (searchBy.equals("Name")) {
        column = "name";
    }

    java.util.List<Manufacturer> results = manager.searchManufacturers(keyword, column);

    if (results.isEmpty()) {
        resultArea.setText("Manufacturer not found.");
    } else {
        for (Manufacturer m : results) {
            resultArea.append("ID: " + m.getManufacturerID() + "\n");
            resultArea.append("Name: " + m.getName() + "\n");
            resultArea.append("Address: " + m.getAddress() + "\n");
            resultArea.append("Contact: " + m.getContact() + "\n");
            resultArea.append("Email: " + m.getEmail() + "\n");
            resultArea.append("Country: " + m.getCountry() + "\n");
            resultArea.append("--------------------------------------------------\n");
        }
    }
});

    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
