/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package pharmacymanagmentsystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import pharmacymanagmentsystem.*;

public class UpdateManufacturerPanel extends javax.swing.JPanel {

   private JTextField idField, nameField, addressField, contactField, emailField;
    private JComboBox<String> countryCombo;
    private JLabel statusLabel;

    private Manufacturer_Manager manager;
    private Manufacturer currentManufacturer;

    public UpdateManufacturerPanel(Manufacturer_Manager manager) {
        this.manager = manager;
        setLayout(new BorderLayout(10, 10));

        // ===== Top Search Panel =====
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Enter Manufacturer ID:"));
        idField = new JTextField(15);
        topPanel.add(idField);
        JButton searchBtn = new JButton("Search");
        topPanel.add(searchBtn);

        // ===== Center Form Panel =====
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        nameField = new JTextField();
        addressField = new JTextField();
        contactField = new JTextField();
        emailField = new JTextField();
        countryCombo = new JComboBox<>(new String[]{"Pakistan", "India", "USA", "UK", "China"});

        formPanel.add(new JLabel("Name:")); formPanel.add(nameField);
        formPanel.add(new JLabel("Address:")); formPanel.add(addressField);
        formPanel.add(new JLabel("Contact:")); formPanel.add(contactField);
        formPanel.add(new JLabel("Email:")); formPanel.add(emailField);
        formPanel.add(new JLabel("Country:")); formPanel.add(countryCombo);

        // ===== Bottom Button Panel =====
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton updateBtn = new JButton("Update");
        statusLabel = new JLabel(" ");
        bottomPanel.add(updateBtn);
        bottomPanel.add(statusLabel);

        // ===== Add All Panels =====
        add(topPanel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // ===== Search Button Logic =====
        searchBtn.addActionListener(e -> {
    String id = idField.getText().trim();
    currentManufacturer = manager.getManufacturerById(id); // 🔁 now from DB

    if (currentManufacturer != null) {
        fillForm(currentManufacturer);
        statusLabel.setText("Manufacturer found.");
    } else {
        clearForm();
        statusLabel.setText("Manufacturer not found.");
    }
});


        // ===== Update Button Logic =====
        updateBtn.addActionListener(e -> {
    if (currentManufacturer == null) {
        statusLabel.setText("Search for a manufacturer first.");
        return;
    }

    // Update fields from form into the object
    currentManufacturer.setName(nameField.getText().trim());
    currentManufacturer.setAddress(addressField.getText().trim());
    currentManufacturer.setContact(contactField.getText().trim());
    currentManufacturer.setEmail(emailField.getText().trim());
    currentManufacturer.setCountry((String) countryCombo.getSelectedItem());

    // Call database update method
    boolean updated = manager.updateManufacturerInDatabase(currentManufacturer);

    if (updated) {
        statusLabel.setText("Updated in database successfully.");
    } else {
        statusLabel.setText("Failed to update. Please try again.");
    }
});

    }

    private void fillForm(Manufacturer m) {
        nameField.setText(m.getName());
        addressField.setText(m.getAddress());
        contactField.setText(m.getContact());
        emailField.setText(m.getEmail());
        countryCombo.setSelectedItem(m.getCountry());
    }

    private void clearForm() {
        nameField.setText("");
        addressField.setText("");
        contactField.setText("");
        emailField.setText("");
        countryCombo.setSelectedIndex(0);
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
