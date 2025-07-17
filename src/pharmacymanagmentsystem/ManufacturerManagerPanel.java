/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package pharmacymanagmentsystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ManufacturerManagerPanel extends javax.swing.JPanel {


  private CardLayout cardLayout;
    private JPanel contentPanel;

    private Manufacturer_Manager manufacturerManager;
private AddMedicinePanel addMedicinePanel;


    public ManufacturerManagerPanel(Manufacturer_Manager manager,AddMedicinePanel addMedicinePanel) {
        this.manufacturerManager=manager;
         this.addMedicinePanel = addMedicinePanel;
        setLayout(new BorderLayout());

        // Top button panel
        JPanel menuPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnAdd = new JButton("Add Manufacturer");
        JButton btnView = new JButton("View All");
        JButton btnUpdate = new JButton("Update Manufacturer");
        JButton btnDelete = new JButton("Delete Manufacturer");
        JButton btnSearch = new JButton("Search Manufacturer");

        menuPanel.add(btnAdd);
        menuPanel.add(btnView);
        menuPanel.add(btnUpdate);
        menuPanel.add(btnDelete);
        menuPanel.add(btnSearch);

        add(menuPanel, BorderLayout.NORTH);

        // Create card layout panel
        contentPanel = new JPanel();
        cardLayout = new CardLayout();
        contentPanel.setLayout(cardLayout);

        // Manager class
//        Manufacturer_Manager manager = new Manufacturer_Manager(manufacturerList);

        // Add individual panels to card layout
//        contentPanel.add(new AddManufacturerPanel(manager), "Add");
AddManufacturerPanel addManufacturerPanel = new AddManufacturerPanel(manager, addMedicinePanel);
contentPanel.add(addManufacturerPanel, "Add");

        contentPanel.add(new ViewManufacturerPanel(manager), "View");
        contentPanel.add(new UpdateManufacturerPanel(manager), "Update");
        contentPanel.add(new DeleteManufacturerPanel(manager), "Delete");
        contentPanel.add(new SearchManufacturerPanel(manager), "Search");

        // Add card panel to main panel
        add(contentPanel, BorderLayout.CENTER);

        // Button actions to switch cards
        btnAdd.addActionListener(e -> cardLayout.show(contentPanel, "Add"));
        btnView.addActionListener(e -> cardLayout.show(contentPanel, "View"));
        btnUpdate.addActionListener(e -> cardLayout.show(contentPanel, "Update"));
        btnDelete.addActionListener(e -> cardLayout.show(contentPanel, "Delete"));
        btnSearch.addActionListener(e -> cardLayout.show(contentPanel, "Search"));
        
//        contentPanel.add(new AddManufacturerPanel(manager), "Add");

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
