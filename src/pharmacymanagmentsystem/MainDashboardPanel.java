package pharmacymanagmentsystem;

import javax.swing.*;
import java.awt.*;

public class MainDashboardPanel extends JPanel {
    private Order_Manager order_Manager;

    public MainDashboardPanel(
        MedicineManager medicineManager,
        Manufacturer_Manager manufacturerManager,
        Supplier_Manager supplierManager,
        SalesRecordManager salesManager,
        Customer_Manager customerManager,
        String username
    ) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE); // optional

        // Load data
        medicineManager.getAllMedicinesFromDatabase();
        customerManager.loadCustomersFromDatabase();
        manufacturerManager.loadManufacturersFromDatabase();

        // Shared panels
        AddMedicinePanel addMedicinePanel = new AddMedicinePanel(medicineManager, manufacturerManager);
        AddNewSalePanel addPanel = new AddNewSalePanel(salesManager, medicineManager, customerManager);
        OrderManagerPanel orderPanel = new OrderManagerPanel(order_Manager, medicineManager, supplierManager, manufacturerManager);

        // Welcome message
        JLabel welcomeLabel = new JLabel("Welcome, " + username, SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // Tabs
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Medicines", new MedicineManagerPanel(medicineManager, manufacturerManager, addMedicinePanel));
        tabbedPane.addTab("Manufacturers", new ManufacturerManagerPanel(manufacturerManager, addMedicinePanel));
        tabbedPane.addTab("Suppliers", new SupplierManagerPanel(supplierManager));
        tabbedPane.addTab("Customers", new CustomerManagerPanel(customerManager, addPanel));
        tabbedPane.addTab("Sales", new SaleRecordManagerPanel(salesManager, medicineManager, customerManager, addPanel));
        tabbedPane.addTab("Orders", orderPanel);

        add(welcomeLabel, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);
    }
}
