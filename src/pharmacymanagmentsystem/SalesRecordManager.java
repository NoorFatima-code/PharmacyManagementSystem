/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pharmacymanagmentsystem;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

//forr sql///
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.time.Duration;
import javax.swing.JOptionPane;
import pharmacymanagmentsystem.DBConnection;

/**
 *
 * @author mapple.pk
 */
public class SalesRecordManager {

    private Scanner sc = new Scanner(System.in);

    private List<SalesRecord> salesList;
    private List<Medicine> medicineList;
    private List<Customer> customerList;
///////////////constructor////////////////////////

    public SalesRecordManager(List<SalesRecord> salesList, List<Medicine> medicineList, List<Customer> customerList) {
        this.salesList = salesList;
        this.medicineList = medicineList;
        this.customerList = customerList;
    }

   

    //////////////////////////menu///////////////////////////
    public void showSaleRecordMenu(String processedBy) {
        int choice;
        do {
            System.out.println("\n=== Sales Menu ===");
            System.out.println("1. Add New Sale");
            System.out.println("2. View All Sales");
            System.out.println("3. Cancel a Sale");
            System.out.println("4. Edit a Sale");
            System.out.println("5. Search a Sale");
            System.out.println("6. exit...");

            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    addNewSale(processedBy);
                    break;
                case 2:
                    viewAllSales();
                    break;
                case 3:
                    cancelSale();
                    break;
                case 4:
                    EditSale();
                case 5:
                    searchSale();
                case 6:
                    System.out.println("Exiting sales menu...");
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        } while (choice != 5);

    }
/////////////beehaviours/////////////////////

    public void addNewSale(String processedBy) {
        List<SaleItem> items = new ArrayList<>();
//
//        System.out.println("💊 Enter medicine names to add (type 'done' to finish, 'list' to see available medicines):");
//
//        boolean adding = true;
//        while (adding) {
//            if (!addItemToSale(items)) {
//                break;
//            }
//        }
//        if (!items.isEmpty()) {
//            String saleID = "SALE" + (salesList.size() + 1);
//            String date = LocalDateTime.now().toString();
////            SalesRecord newSale = new SalesRecord(saleID, items,  processedBy, true, LocalDateTime.now());
//            salesList.add(newSale);
//
//            System.out.println("Sale Added Successfully!");
//            newSale.printReceipt();
//        }
    }

    public void viewAllSales() {
        // TODO: list all sales
        if (salesList.isEmpty()) {
            System.out.println("No sales recorded yet.");
            return;
        }
        for (SalesRecord sale : salesList) {
            sale.printReceipt();
        }

    }

    public void cancelSale() {
        // TODO: delete sale by ID
        SalesRecord sale = findSaleByID("Enter Sale ID to cancel: ");
        if (sale == null) {
            return;
        }
        if (sale.isEditable()) {
            sale.cancelSale();
        } else {
            System.out.println("This sale cannot be canceled (time limit exceeded).\n");
        }

    }

    public void EditSale() {
        SalesRecord sale = findSaleByID("Enter Sale ID to edit: ");
        if (sale == null) {
            return;
        }

        if (!sale.isEditable()) {
            System.out.println("Edit is not allowed... time limit exceeded.");
            return;
        }

        List<SaleItem> items = sale.getSoldItems();
        System.out.println("Current items in sale:");
        for (int i = 0; i < items.size(); i++) {
            System.out.printf("%d. ", i + 1);
            items.get(i).displayItem();
        }

        System.out.print("Enter item number to remove (0 to skip): ");
        int itemNum = sc.nextInt();
        sc.nextLine();
        if (itemNum > 0 && itemNum <= items.size()) {
            SaleItem removed = items.remove(itemNum - 1);
            removed.getMedicine().updateStock(removed.getQuantity());
            System.out.println("Item removed and stock updated.");
        }

        System.out.println("You can now add new items to the sale.");
        boolean adding = true;
        while (adding) {
            if (!addItemToSale(items)) {
                break;
            }
        }

        System.out.println("Sale updated successfully!");
        return;

    }

    public void searchSale() {

        System.out.print("Enter Sale ID to search: ");
        String saleID = sc.nextLine();
        for (SalesRecord sale : salesList) {
            if (sale.getSaleID().equalsIgnoreCase(saleID)) {
                sale.printReceipt();
                return;
            }
        }
        System.out.println("Sale not found.");

    }

    public void printReceipt(SalesRecord sale) {
        sale.printReceipt();
    }

    /////helper ///
    public Medicine findMedicineByName(String name) {
        for (Medicine m : medicineList) {
            if (m.getName().equalsIgnoreCase(name)) {
                return m;
            }
        }
        return null;
    }

    public int getValidQuantity(Medicine med) {
        System.out.print("Enter quantity: ");
        int qty = sc.nextInt();
        sc.nextLine();

        if (qty > med.getStockQuantity()) {
            System.out.println("Not enough stock.");
            return -1;
        }
        return qty;
    }

    private boolean addItemToSale(List<SaleItem> items) {
        System.out.print("\nEnter medicine name (or 'done' to finish): ");
        String name = sc.nextLine();

        if (name.equalsIgnoreCase("done")) {
            return false;
        }

        if (name.equalsIgnoreCase("list")) {
            MedicineManager.viewAllMedicines(medicineList);
            return true;
        }

        Medicine med = findMedicineByName(name);
        if (med == null) {
            System.out.println("Medicine not found.");
            return true;
        }

        int qty = getValidQuantity(med);
        if (qty == -1) {
            return true;
        }

        med.updateStock(-qty);
        SaleItem item = new SaleItem(med, qty);
        items.add(item);
        item.displayItem();

        return true;
    }

//public SalesRecord findSaleByID(String prompt) {
//    System.out.print(prompt);
//    String saleID = sc.nextLine();
//
//    for (SalesRecord sale : salesList) {
//        if (sale.getSaleID().equalsIgnoreCase(saleID)) {
//            return sale;
//        }
//    }
//
//    System.out.println("Sale not found.");
//    return null;
//}
/////////////////heelpersss method for guii////////////
    ////////for ading
    public String generateSaleID() {
        return "SALE" + (salesList.size() + 1);
    }

    public SalesRecord addSale(String saleID, Customer customer, List<SaleItem> items, String processedBy, LocalDateTime createdAt) {
        if (items == null || items.isEmpty() || processedBy == null) {
            return null;
        }
        System.out.println("Creating sale for: " + processedBy);
        System.out.println("Items being added: " + items.size());

        String date = LocalDateTime.now().toString();
        SalesRecord newSale = new SalesRecord(saleID, customer, items, processedBy, true, createdAt);
        salesList.add(newSale);

        return newSale;
    }

    public List<Medicine> getMedicineNameSuggestions(String query) {
        List<Medicine> suggestions = new ArrayList<>();
        for (Medicine m : medicineList) {
            if (m.getName().toLowerCase().contains(query.toLowerCase())) {
                suggestions.add(m);
            }
        }
        return suggestions;
    }

    public SaleItem createSaleItem(Medicine med, int quantity) {
        if (med == null || quantity <= 0 || quantity > med.getStockQuantity()) {
            return null;
        }

        med.updateStock(-quantity);  // Deduct stock
        return new SaleItem(med, quantity);
    }

    public List<Medicine> getAvailableMedicinesForSale() {
        List<Medicine> available = new ArrayList<>();
        for (Medicine med : medicineList) {
            if (med.getStockQuantity() > 0) {
                available.add(med);
            }
        }
        return available;
    }

    public List<String[]> getSaleItemDetails(SalesRecord sale) {
        List<String[]> details = new ArrayList<>();
        for (SaleItem item : sale.getSoldItems()) {
            String[] row = new String[]{
                item.getMedicine().getName(),
                String.valueOf(item.getQuantity()),
                String.format("%.2f", item.getMedicine().getPrice()),
                String.format("%.2f", item.calculateItemTotal())
            };
            details.add(row);
        }
        return details;
    }

    public List<SalesRecord> getAllSales() {
        return new ArrayList<>(salesList); // return safe copy
    }

//////////for searrching
    public List<SalesRecord> findSalesByProcessedByName(String name) {
        List<SalesRecord> results = new ArrayList<>();
        for (SalesRecord sale : salesList) {
            if (sale.getProcessedBy().equalsIgnoreCase(name)) {
                results.add(sale);
            }
        }
        return results;
    }

    public SalesRecord findSaleByID(String saleID) {
        for (SalesRecord sale : salesList) {
            if (sale.getSaleID().equalsIgnoreCase(saleID)) {
                return sale;
            }
        }
        return null;
    }

///for cancel
    public boolean cancelSale(SalesRecord sale) {
        if (sale == null) {
            return false;
        }

        sale.updateEditableStatus();
        if (sale.isEditable()) {
            sale.cancelSale();
            return true;
        }
        return false;
    }

    public List<String[]> getAllSalesAsTableData() {
        List<String[]> data = new ArrayList<>();
        for (SalesRecord sale : salesList) {
            data.add(new String[]{
                sale.getSaleID(),
                sale.getProcessedBy(),
                String.format("%.2f", sale.calculateTotal()),
                sale.isEditable() ? "Editable" : "Locked"
            });
        }
        return data;
    }

    public boolean editSale(SalesRecord sale, List<SaleItem> updatedItems) {
        if (sale == null) {
            return false;
        }

        sale.updateEditableStatus();
        if (!sale.isEditable()) {
            return false;
        }

        // Return removed items' stock
        for (SaleItem item : sale.getSoldItems()) {
            item.getMedicine().updateStock(item.getQuantity());
        }

        // Set new updated items
        sale.getSoldItems().clear();
        sale.getSoldItems().addAll(updatedItems);

        return true;
    }

//public List<Customer> getCustomerList() {
//    return customerList;
//}
    public Customer findCustomerByName(String name) {
        for (Customer customer : customerList) {
            if (customer.getName().equalsIgnoreCase(name)) {
                return customer;
            }
        }
        return null;
    }

//////
    public String generateSaleIDFromDatabase() {
        String newID = "SALE1";
        String query = "SELECT sale_id FROM sales_record ORDER BY sale_id DESC LIMIT 1";

        try (Connection con = DBConnection.getConnection(); PreparedStatement stmt = con.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                String lastID = rs.getString("sale_id"); // e.g. SALE5
                int num = Integer.parseInt(lastID.replaceAll("[^0-9]", ""));
                newID = "SALE" + (num + 1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return newID;
    }

    public boolean insertSaleRecordToDatabase(SalesRecord sale) {
        String insertSaleSQL = "INSERT INTO sales_record (sale_id, customer_id, processed_by, editable, created_at, cancelled) VALUES (?, ?, ?, ?, ?, ?)";
        String insertItemSQL = "INSERT INTO sale_item (sale_id, medicine_id, quantity) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement saleStmt = conn.prepareStatement(insertSaleSQL); PreparedStatement itemStmt = conn.prepareStatement(insertItemSQL)) {

            conn.setAutoCommit(false);  // Transaction begin

            // Insert into sales_record
            saleStmt.setString(1, sale.getSaleID());
            saleStmt.setString(2, sale.getCustomer() != null ? sale.getCustomer().getCustomerID() : null);
            saleStmt.setString(3, sale.getProcessedBy());
          
            saleStmt.setBoolean(4, sale.isEditable());
            saleStmt.setTimestamp(5, java.sql.Timestamp.valueOf(sale.getCreatedAt()));
            saleStmt.setBoolean(6, sale.isCancelled());
            saleStmt.executeUpdate();

            // Insert sale items
            for (SaleItem item : sale.getSoldItems()) {
                itemStmt.setString(1, sale.getSaleID());
                itemStmt.setString(2, item.getMedicine().getMedicineID());
                itemStmt.setInt(3, item.getQuantity());
                

                itemStmt.addBatch();
            }
            itemStmt.executeBatch();

            conn.commit();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<SalesRecord> getAllSalesFromDatabase(Customer_Manager customerManager, MedicineManager medicineManager) {
        List<SalesRecord> sales = new ArrayList<>();
        String sql = "SELECT * FROM sales_Record";

        try (Connection con = DBConnection.getConnection(); PreparedStatement stmt = con.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String saleID = rs.getString("sale_id");
                String customerID = rs.getString("customer_id");
                String processedBy = rs.getString("processed_by");
             
                LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();
                boolean isEditable = rs.getBoolean("editable");
                boolean isCancelled = rs.getBoolean("cancelled");

                // ✅ Use passed customerManager
                Customer customer = customerManager.getCustomerByIDFromDatabase(customerID);

                // ✅ Pass medicineManager here too
                List<SaleItem> items = getSaleItemsFromDatabase(saleID, medicineManager);

                SalesRecord sale = new SalesRecord(saleID, customer, items, processedBy, isEditable, createdAt);
                sale.updateEditableStatus();

                sale.updateEditableStatusInDatabase();

                sale.setCancelled(isCancelled);
                sales.add(sale);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return sales;
    }

    public List<SalesRecord> getAllSales(Customer_Manager customerManager, MedicineManager medicineManager) {
        return getAllSalesFromDatabase(customerManager, medicineManager);
    }

   public List<SaleItem> getSaleItemsFromDatabase(String saleID, MedicineManager medicineManager) {
    List<SaleItem> items = new ArrayList<>();
    String sql = "SELECT * FROM sale_item WHERE sale_id = ?";

    try (Connection con = DBConnection.getConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {

        stmt.setString(1, saleID);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            String medicineID = rs.getString("medicine_id");
            int qty = rs.getInt("quantity");
          
            Medicine med = medicineManager.findMedicineByID_DB(medicineID);

            if (med != null) {
                // 🔁 You may want to set custom price in SaleItem constructor
                SaleItem item = new SaleItem(med, qty);
               
                items.add(item);
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return items;
}

/////
    
    public SalesRecord findSaleByIDFromDatabase(String saleID, Customer_Manager customerManager, MedicineManager medicineManager) {
    String sql = "SELECT * FROM sales_record WHERE sale_id = ?";
    try (Connection con = DBConnection.getConnection();
         PreparedStatement stmt = con.prepareStatement(sql)) {
        stmt.setString(1, saleID);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            String customerID = rs.getString("customer_id");
            String processedBy = rs.getString("processed_by");
             
            LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();
            boolean isEditable = rs.getBoolean("editable");
            boolean isCancelled = rs.getBoolean("cancelled");

            Customer customer = customerManager.getCustomerByIDFromDatabase(customerID);
            List<SaleItem> items = getSaleItemsFromDatabase(saleID, medicineManager);

            SalesRecord sale = new SalesRecord(saleID, customer, items, processedBy, isEditable, createdAt);
            sale.setCancelled(isCancelled);
            sale.updateEditableStatus();
            sale.updateEditableStatusInDatabase();
            return sale;
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null;
}

    public List<SalesRecord> findSalesByProcessedByNameFromDatabase(String name, Customer_Manager customerManager, MedicineManager medicineManager) {
    List<SalesRecord> results = new ArrayList<>();
    String sql = "SELECT * FROM sales_record WHERE LOWER(processed_by) LIKE ?";

    try (Connection con = DBConnection.getConnection();
         PreparedStatement stmt = con.prepareStatement(sql)) {
        stmt.setString(1, "%" + name.toLowerCase() + "%");
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            String saleID = rs.getString("sale_id");
            String customerID = rs.getString("customer_id");
            String processedBy = rs.getString("processed_by");
             
            LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();
            boolean isEditable = rs.getBoolean("editable");
            boolean isCancelled = rs.getBoolean("cancelled");

            Customer customer = customerManager.getCustomerByIDFromDatabase(customerID);
            List<SaleItem> items = getSaleItemsFromDatabase(saleID, medicineManager);

            SalesRecord sale = new SalesRecord(saleID, customer, items, processedBy, isEditable, createdAt);
            sale.setCancelled(isCancelled);
            sale.updateEditableStatus();
            sale.updateEditableStatusInDatabase();
            results.add(sale);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return results;
}

//   
//    public boolean updateSaleItemsInDatabase(SalesRecord sale) {
//    if (sale == null || !sale.isEditable()) return false;
//
//    String deleteOldItemsSQL = "DELETE FROM sale_item WHERE sale_id = ?";
//    String insertNewItemSQL = "INSERT INTO sale_item (sale_id, medicine_id, quantity) VALUES (?, ?, ?)";
//
//    try (Connection con = DBConnection.getConnection()) {
//        con.setAutoCommit(false);
//
//        try (PreparedStatement deleteStmt = con.prepareStatement(deleteOldItemsSQL)) {
//            deleteStmt.setString(1, sale.getSaleID());
//            deleteStmt.executeUpdate();
//        }
//
//        try (PreparedStatement insertStmt = con.prepareStatement(insertNewItemSQL)) {
//            for (SaleItem item : sale.getSoldItems()) {
//                insertStmt.setString(1, sale.getSaleID());
//                insertStmt.setString(2, item.getMedicine().getMedicineID());
//                insertStmt.setInt(3, item.getQuantity());
//        
//                insertStmt.addBatch();
//            }
//            insertStmt.executeBatch();
//        }
//
//        con.commit();
//        return true;
//
//    } catch (SQLException e) {
//        e.printStackTrace();
//        return false;
//    }
//}
    
    public boolean updateSaleItemsInDatabase(SalesRecord sale, MedicineManager medicineManager) {
    if (sale == null || !sale.isEditable()) return false;

    String fetchOldItemsSQL = "SELECT medicine_id, quantity FROM sale_item WHERE sale_id = ?";
    String deleteOldItemsSQL = "DELETE FROM sale_item WHERE sale_id = ?";
    String insertNewItemSQL = "INSERT INTO sale_item (sale_id, medicine_id, quantity) VALUES (?, ?, ?)";

    try (Connection con = DBConnection.getConnection()) {
        con.setAutoCommit(false);

        // Step 1: Restore stock for old items
        try (PreparedStatement fetchStmt = con.prepareStatement(fetchOldItemsSQL)) {
            fetchStmt.setString(1, sale.getSaleID());
            ResultSet rs = fetchStmt.executeQuery();
            while (rs.next()) {
                String medID = rs.getString("medicine_id");
                int qty = rs.getInt("quantity");

                Medicine med = medicineManager.findMedicineByID_DB(medID);
                if (med != null) {
                    med.updateStock(qty); // restore stock
                    medicineManager.updateMedicineStockInDatabase(med); // update DB
                }
            }
        }

        // Step 2: Delete old items
        try (PreparedStatement deleteStmt = con.prepareStatement(deleteOldItemsSQL)) {
            deleteStmt.setString(1, sale.getSaleID());
            deleteStmt.executeUpdate();
        }

        // Step 3: Insert new items
        try (PreparedStatement insertStmt = con.prepareStatement(insertNewItemSQL)) {
            for (SaleItem item : sale.getSoldItems()) {
                insertStmt.setString(1, sale.getSaleID());
                insertStmt.setString(2, item.getMedicine().getMedicineID());
                insertStmt.setInt(3, item.getQuantity());

                item.getMedicine().updateStock(-item.getQuantity());
                medicineManager.updateMedicineStockInDatabase(item.getMedicine());

                insertStmt.addBatch();
            }
            insertStmt.executeBatch();
        }

        con.commit();
        return true;

    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}

//  
//    
//    
//   
//
    public void loadSalesFromDatabase(Customer_Manager customerManager, MedicineManager medicineManager) {
        salesList.clear();  // Memory list fresh karne ke liye
        String saleSQL = "SELECT * FROM sales_record";
        String itemSQL = "SELECT * FROM sale_item WHERE sale_id = ?";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement saleStmt = conn.prepareStatement(saleSQL); ResultSet rsSale = saleStmt.executeQuery()) {

            while (rsSale.next()) {
                String saleID = rsSale.getString("sale_id");
                String customerID = rsSale.getString("customer_id");
                String processedBy = rsSale.getString("processed_by");
                boolean editable = rsSale.getBoolean("editable");
                boolean cancelled = rsSale.getBoolean("cancelled");
                LocalDateTime createdAt = rsSale.getTimestamp("created_at").toLocalDateTime();

                Customer customer = customerManager.getCustomerByIDFromDatabase(customerID);

                List<SaleItem> itemList = new ArrayList<>();
                try (PreparedStatement itemStmt = conn.prepareStatement(itemSQL)) {
                    itemStmt.setString(1, saleID);
                    ResultSet rsItems = itemStmt.executeQuery();
                    while (rsItems.next()) {
                        String medID = rsItems.getString("medicine_id");
                        int qty = rsItems.getInt("quantity");
                        Medicine med = medicineManager.findMedicineByID(medID);
                        itemList.add(new SaleItem(med, qty));
                    }
                }

                SalesRecord sale = new SalesRecord(saleID, customer, itemList, processedBy, editable, createdAt);
                sale.setCancelled(cancelled);
                salesList.add(sale);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    public boolean cancelSaleInDatabase(SalesRecord sale, MedicineManager medicineManager) {
    if (sale == null || sale.isCancelled()) return false;

    String cancelSQL = "UPDATE sales_record SET cancelled = TRUE WHERE sale_id = ?";
    String restoreStockSQL = "UPDATE medicine SET stock_quantity = stock_quantity + ? WHERE medicine_id = ?";

    try (Connection con = DBConnection.getConnection()) {
        con.setAutoCommit(false);

        try (PreparedStatement cancelStmt = con.prepareStatement(cancelSQL)) {
            cancelStmt.setString(1, sale.getSaleID());
            cancelStmt.executeUpdate();
        }

        try (PreparedStatement stockStmt = con.prepareStatement(restoreStockSQL)) {
            for (SaleItem item : sale.getSoldItems()) {
                stockStmt.setInt(1, item.getQuantity());
                stockStmt.setString(2, item.getMedicine().getMedicineID());
                stockStmt.addBatch();
            }
            stockStmt.executeBatch();
        }

        con.commit();
        sale.setCancelled(true);
        return true;

    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}

   

    public static List<String> getAvailablePermissions() {
        return Arrays.asList(
                " Add New Sale",
                "View All Sales",
                "Update Medicine Stock",
                "Cancel a Sale",
                "Edit a Sale",
                "Search a Sale"
        );
    }

}
