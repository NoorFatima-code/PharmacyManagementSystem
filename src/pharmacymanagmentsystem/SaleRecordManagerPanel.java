/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pharmacymanagmentsystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
/**
 *
 * @author mapple.pk
 */
public class SaleRecordManagerPanel extends JPanel {
    
        private CardLayout cardLayout;
    private JPanel contentPanel;
    private SalesRecordManager salesManager;
    private MedicineManager medicineManager;
    private Customer_Manager customerManager;
    private String currentEmployee;
private AddNewSalePanel addPanel ;
    public SaleRecordManagerPanel(SalesRecordManager salesManager, MedicineManager medicineManager,Customer_Manager  customerManager,AddNewSalePanel addPanel )//,ViewAllSalePanel viewPanel
    {
        this.salesManager = salesManager;
        this.medicineManager = medicineManager;
//        this.currentEmployee = employee;

        setLayout(new BorderLayout());

        // Top Buttons Panel
        JPanel buttonPanel = new JPanel();
        JButton addSaleBtn = new JButton("Add New Sale");
        JButton viewSalesBtn = new JButton("View All Sales");
        JButton searchSaleBtn = new JButton("Search Sale");
        JButton editSaleBtn = new JButton("Edit Sale");
        JButton cancelSaleBtn = new JButton("Cancel Sale");

        buttonPanel.add(addSaleBtn);
        buttonPanel.add(viewSalesBtn);
        buttonPanel.add(searchSaleBtn);
        buttonPanel.add(editSaleBtn);
        buttonPanel.add(cancelSaleBtn);

        // Content Panel for Cards
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        ////////it will be in main dashboard frame bcz its shared between customer and sale ///
//        AddNewSalePanel addPanel = new AddNewSalePanel(salesManager, medicineManager, customerManager);
        
        ViewAllSalePanel viewPanel = new ViewAllSalePanel(salesManager,customerManager,medicineManager);
        SearchSalePanel searchPanel = new SearchSalePanel(salesManager,customerManager,medicineManager);
        EditSalePanel editPanel = new EditSalePanel(salesManager,customerManager,medicineManager);
         CancelSalePanel  cancelSalePanel= new CancelSalePanel(salesManager,medicineManager,customerManager);

        contentPanel.add(addPanel, "ADD");
        contentPanel.add(viewPanel, "VIEW");
        contentPanel.add(searchPanel, "SEARCH");
        contentPanel.add(editPanel, "EDIT");
        contentPanel.add(cancelSalePanel, "CANCEL");

//         Button Actions
        addSaleBtn.addActionListener((ActionEvent e) -> cardLayout.show(contentPanel, "ADD"));
//addSaleBtn.addActionListener((ActionEvent e) -> {
//    addPanel.refreshCustomerDropdown();  // 🔁 REFRESH every time
//    cardLayout.show(contentPanel, "ADD");
//});
        viewSalesBtn.addActionListener((ActionEvent e) -> cardLayout.show(contentPanel, "VIEW"));
        searchSaleBtn.addActionListener((ActionEvent e) -> cardLayout.show(contentPanel, "SEARCH"));
        editSaleBtn.addActionListener((ActionEvent e) -> cardLayout.show(contentPanel, "EDIT"));
        cancelSaleBtn.addActionListener((ActionEvent e) -> cardLayout.show(contentPanel, "CANCEL"));

        // Add Panels to Main
        add(buttonPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
    }
}


