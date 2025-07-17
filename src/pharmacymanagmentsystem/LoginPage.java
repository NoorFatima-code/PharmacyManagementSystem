package pharmacymanagmentsystem;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class LoginPage extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;
    private BackgroundPanel backgroundPanel;

    public LoginPage() {
        setTitle("Pharmacy Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Fullscreen
        setLocationRelativeTo(null);

        // ✅ CardLayout container
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // ✅ Login background with centered login form
        backgroundPanel = new BackgroundPanel("src/images/pharmacy_bg.jpg");
        backgroundPanel.setLayout(new GridBagLayout());

        LoginPanel loginPanel = new LoginPanel(this);
        loginPanel.setOpaque(false);

        backgroundPanel.add(loginPanel); // center loginPanel on background

        // ✅ Add both screens to mainPanel
        mainPanel.add(backgroundPanel, "login"); // show background + login form
        setContentPane(mainPanel); // mainPanel is your top-level content

        cardLayout.show(mainPanel, "login");
        setVisible(true);
    }

    public void showDashboard(String username) {
        // ✅ Create data managers
        List<Medicine> medicineList = new ArrayList<>();
        List<Manufacturer> manufacturerList = new ArrayList<>();
        List<Supplier> supplierList = new ArrayList<>();
        List<Customer> customerList = new ArrayList<>();
        List<SalesRecord> salesList = new ArrayList<>();

        Manufacturer_Manager manufacturerManager = new Manufacturer_Manager(manufacturerList);
        manufacturerManager.loadManufacturersFromDatabase();

        MedicineManager medicineManager = new MedicineManager(medicineList, manufacturerList);
        Supplier_Manager supplierManager = new Supplier_Manager(supplierList);
        SalesRecordManager salesManager = new SalesRecordManager(salesList, medicineList, customerList);
        Customer_Manager customerManager = new Customer_Manager(customerList);

        // ✅ Create full-size dashboard panel (fills JFrame)
        MainDashboardPanel dashboard = new MainDashboardPanel(
                medicineManager,
                manufacturerManager,
                supplierManager,
                salesManager,
                customerManager,
                username
        );

        // ✅ Add and show dashboard
        mainPanel.add(dashboard, "dashboard");
        cardLayout.show(mainPanel, "dashboard");

        // 🔄 Refresh display
        mainPanel.revalidate();
        mainPanel.repaint();
    }
}
