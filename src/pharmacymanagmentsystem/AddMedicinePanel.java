/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package pharmacymanagmentsystem;

import pharmacymanagmentsystem.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 *
 * @author mapple.pk
 */
public class AddMedicinePanel extends javax.swing.JPanel {
 private MedicineManager medicineManager;
    private Manufacturer_Manager manufacturerManager;
private JComboBox<Manufacturer> manufacturerBox;

    /**
     * Creates new form AddMedicinePanel
     */
    public AddMedicinePanel(MedicineManager manager, Manufacturer_Manager manufacturerManager) {
  this.medicineManager = medicineManager;
        this.manufacturerManager = manufacturerManager;
        manufacturerManager.loadManufacturersFromDatabase();
        setLayout(new GridLayout(10, 2, 10, 10));

        JTextField idField = new JTextField();
        idField.setEditable(false);
        String autoID = manager.generateMedicineIDFromDatabase();
        idField.setText(autoID);

        JTextField nameField = new JTextField();
        JComboBox<String> typeBox = new JComboBox<>(new String[]{"Tablet", "Syrup"});
        JTextField priceField = new JTextField();
        JTextField qtyField = new JTextField();
//        JTextField expiryField = new JTextField();
        JPanel datePanel = new JPanel();
        JComboBox<String> yearBox = new JComboBox<>();
        for (int y = 2005; y <= 3000; y++) {
            yearBox.addItem(String.valueOf(y));
        }

        JComboBox<String> monthBox = new JComboBox<>();
        for (int m = 1; m <= 12; m++) {
            monthBox.addItem(String.format("%02d", m));
        }

        JComboBox<String> dayBox = new JComboBox<>();
        for (int d = 1; d <= 31; d++) {
            dayBox.addItem(String.format("%02d", d));
        }

        JTextField reorderField = new JTextField();
        JCheckBox perscriptionBox = new JCheckBox("Prescription Required");
//        JComboBox<Manufacturer> manufacturerBox = new JComboBox<>();
manufacturerBox = new JComboBox<>();

        refreshManufacturerBox();

//        for (Manufacturer m : manufacturerManager.getManufacturerList()) {
//            manufacturerBox.addItem(m);
//        }

        JButton addBtn = new JButton("Save Medicine");
        add(new JLabel("MED ID:"));
        add(idField);
        add(new JLabel("Name:"));
        add(nameField);
        add(new JLabel("Type:"));
        add(typeBox);
        add(new JLabel("Price:"));
        add(priceField);
        add(new JLabel("Quantity:"));
        add(qtyField);
        add(new JLabel("Expiry Date (YYYY-MM-DD):"));
        datePanel.add(yearBox);
        datePanel.add(monthBox);
        datePanel.add(dayBox);
        add(datePanel, BorderLayout.NORTH);
        add(new JLabel("Reorder Level:"));
        add(reorderField);
        add(perscriptionBox);
        add(new JLabel());
        add(new JLabel("Manufacturer:"));
        add(manufacturerBox);
        add(new JLabel());
        add(addBtn);

      
        
        addBtn.addActionListener(e -> {
    try {
        String id = idField.getText();
        String name = nameField.getText();
        String type = (String) typeBox.getSelectedItem();
        double price = Double.parseDouble(priceField.getText());
        int qty = Integer.parseInt(qtyField.getText());

        String expiry = yearBox.getSelectedItem() + "-"
                + monthBox.getSelectedItem() + "-"
                + dayBox.getSelectedItem();

        int reorder = Integer.parseInt(reorderField.getText());
        boolean requiresRx = perscriptionBox.isSelected();
        Manufacturer mfg = (Manufacturer) manufacturerBox.getSelectedItem();

        // ✅ Yahan object banao ek baar
        Medicine med = new Medicine(id, name, type, price, qty, expiry, reorder, requiresRx, mfg);

        // ✅ Pehle DB mein insert karo
        boolean dbInserted = manager.insertMedicineIntoDatabase(med);

        // ✅ Agar DB insert ho gaya to list mein bhi add karo
        if (dbInserted) {
            manager.addMedicineToList(med); // aap new method bana lo manager mein
            JOptionPane.showMessageDialog(this, "Medicine added to system and database.");

            // clear fields
            idField.setText(manager.generateMedicineIDFromDatabase());
            nameField.setText("");
            typeBox.setSelectedIndex(0);
            priceField.setText("");
            qtyField.setText("");
            yearBox.setSelectedIndex(0);
            monthBox.setSelectedIndex(0);
            dayBox.setSelectedIndex(0);
            reorderField.setText("");
            perscriptionBox.setSelected(false);
            manufacturerBox.setSelectedIndex(0);
        } else {
            JOptionPane.showMessageDialog(this, "Failed to add. It may already exist.");
        }

    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
    }
});
    }
public void refreshManufacturerBox() {
        manufacturerBox.removeAllItems();
        for (Manufacturer m : manufacturerManager.getManufacturerList()) {
            manufacturerBox.addItem(m);
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
