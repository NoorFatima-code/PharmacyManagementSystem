/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package pharmacymanagmentsystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import pharmacymanagmentsystem.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddManufacturerPanel extends javax.swing.JPanel {
  private JTextField idField;
    private JTextField nameField;
    private JTextArea addressField;
    private JTextField contactField;
    private JTextField emailField;
    private JComboBox<String> countryBox;
    private Manufacturer_Manager manager;
private AddMedicinePanel addMedicinePanel;

    public AddManufacturerPanel(Manufacturer_Manager manager,AddMedicinePanel addMedicinePanel) {
        this.manager = manager;
           this.addMedicinePanel = addMedicinePanel;

        setLayout(new GridLayout(8, 2, 10, 10));

        idField = new JTextField(generateManufacturerId());
        idField.setEditable(false);

        nameField = new JTextField();
        addressField = new JTextArea(2, 20);
        contactField = new JTextField();
        emailField = new JTextField();
        countryBox = new JComboBox<>(new String[]{"Pakistan", "India", "China", "USA", "UK"});

        JButton saveBtn = new JButton("Save Manufacturer");

        add(new JLabel("Manufacturer ID:"));
        add(idField);
        add(new JLabel("Name:"));
        add(nameField);
        add(new JLabel("Address:"));
        add(new JScrollPane(addressField));
        add(new JLabel("Contact Number:"));
        add(contactField);
        add(new JLabel("Email:"));
        add(emailField);
        add(new JLabel("Country:"));
        add(countryBox);
        add(new JLabel());  // empty cell
        add(saveBtn);

//        saveBtn.addActionListener(e -> {
//            try {
//                String id = idField.getText().trim();
//                String name = nameField.getText().trim();
//                String address = addressField.getText().trim();
//                String contact = contactField.getText().trim();
//                String email = emailField.getText().trim();
//                String country = (String) countryBox.getSelectedItem();
//
//boolean added = manager.addManufacturerToDatabase(id, name, address, contact, email, country);
//
//                if (added) {
//                    JOptionPane.showMessageDialog(this, "Manufacturer saved successfully.");
//                     if (addMedicinePanel != null) {
//        addMedicinePanel.refreshManufacturerBox();
//                     }
//                    idField.setText(generateManufacturerId());
//                    nameField.setText("");
//                    addressField.setText("");
//                    contactField.setText("");
//                    emailField.setText("");
//                    countryBox.setSelectedIndex(0);
//                } else {
//                    JOptionPane.showMessageDialog(this, "Failed to add. Duplicate or invalid data.");
//                }
//            } catch (Exception ex) {
//                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
//            }
//        });
//    }
//
//    private String generateManufacturerId() {
//        int nextId = manager.getManufacturerCount() + 1;
//        return "MAN" + nextId;
//    }

saveBtn.addActionListener(e -> {
    try {
        String id = idField.getText().trim();
        String name = nameField.getText().trim();
        String address = addressField.getText().trim();
        String contact = contactField.getText().trim();
        String email = emailField.getText().trim();
        String country = (String) countryBox.getSelectedItem();

        boolean added = manager.addManufacturerToDatabase(id, name, address, contact, email, country);

        if (added) {
            JOptionPane.showMessageDialog(this, "✅ Manufacturer saved to database.");
            manager.loadManufacturersFromDatabase();
            if (addMedicinePanel != null) {
                addMedicinePanel.refreshManufacturerBox();
            }
            idField.setText(generateManufacturerId());
            nameField.setText("");
            addressField.setText("");
            contactField.setText("");
            emailField.setText("");
            countryBox.setSelectedIndex(0);
        } else {
            JOptionPane.showMessageDialog(this, "❌ Failed to add. Duplicate or DB error.");
        }
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
    }
});
    }
       private String generateManufacturerId() {
           return manager.getNextManufacturerIDFromDB();
    }

       

   


  
   

    
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
