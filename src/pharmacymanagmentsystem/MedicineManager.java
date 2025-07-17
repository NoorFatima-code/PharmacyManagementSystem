/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pharmacymanagmentsystem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;


//forr sql///
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import pharmacymanagmentsystem.DBConnection;
/**
 *
 * @author mapple.pk
 */
public class MedicineManager {

    private Scanner sc = new Scanner(System.in);

    private List<Medicine> medicineList;
    private List<Manufacturer> manufacturerList;

    // Constructor
    public MedicineManager(List<Medicine> medicineList, List<Manufacturer> manufacturerList) {
        this.medicineList = medicineList;
        this.manufacturerList = manufacturerList;
    }

    

    public List<Medicine> getMedicineList() {
        return medicineList;
    }

    public List<Manufacturer> getManufacturerList() {
        return manufacturerList;
    }

    public void showMedicineMenu(List<Medicine> medicinelist) {
        int choice;
        do {
            System.out.println("\n=== Medicine Menu ===\n");
            System.out.println("1. Add Medicine");
            System.out.println("2. View All Medicines");
            System.out.println("3. Update Medicine");
            System.out.println("4. Delete Medicine");
            System.out.println("5. Check Expired Medicines");
            System.out.println("6. Check Reorder Status");
            System.out.println("7. Search Medicine");
            System.out.println("8. Back to Main Menu");
            System.out.print("Enter your choice: ");
            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    addMedicine();
                case 2:
                    viewAllMedicines(medicineList);
                case 3:
                    updateMedicineStock();
                case 4:
                    deleteMedicine();
                case 5:
                    checkExpiredMedicines();
                case 6:
                    checkReorderStatus();
                case 7:
                    searchMedicine();
                case 8:
                    System.out.println("Returning to Main Menu...");
                default:
                    System.out.println("Invalid choice!");
            }
        } while (choice != 8);
    }

    public void addMedicine() {
        String medicineID = String.format("MED%03d", medicineList.size() + 1);

        System.out.print("Enter Medicine Name: ");
        String name = sc.nextLine();
        System.out.print("Enter Medicine Type (tablet/syrup): ");
        String type = sc.nextLine();
        System.out.print("Enter Medicine Price: ");
        double price = sc.nextDouble();
        System.out.print("Enter Stock Quantity: ");
        int quantity = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter Expiry Date (YYYY-MM-DD): ");
        String expiryDate = sc.nextLine();
        System.out.print("Enter Reorder Level: ");
        int reorderLevel = sc.nextInt();
        System.out.print("Requires Prescription? (true/false): ");
        boolean requiresPrescription = sc.nextBoolean();
        sc.nextLine();

        if (manufacturerList.isEmpty()) {
            System.out.println("No manufacturers available. Cannot add medicine.");
            return;
        }

        System.out.println("\nAvailable Manufacturers:");
        for (int i = 0; i < manufacturerList.size(); i++) {
            System.out.println((i + 1) + ". " + manufacturerList.get(i).getName());
        }
        System.out.print("Select Manufacturer (enter number): ");
        int manufacturerIndex = sc.nextInt();
        sc.nextLine();

        if (manufacturerIndex < 1 || manufacturerIndex > manufacturerList.size()) {
            System.out.println("Invalid manufacturer selection. Medicine not added.");
            return;
        }

        Manufacturer selectedManufacturer = manufacturerList.get(manufacturerIndex - 1);

        Medicine newMed = new Medicine(medicineID, name, type, price, quantity, expiryDate,
                reorderLevel, requiresPrescription, selectedManufacturer);

        medicineList.add(newMed);
        System.out.println("Medicine added successfully!");
    }

    public static void viewAllMedicines(List<Medicine> medicinelist) {
        if (medicinelist.isEmpty()) {
            System.out.println("No medicine available.");
        } else {
            for (Medicine m : medicinelist) {
                m.displayDetails();
            }
        }
    }

    public void updateMedicineStock() {
        System.out.println("Search medicine by:");
        System.out.println("1. Medicine ID");
        System.out.println("2. Medicine Name");
        int option = sc.nextInt();
        sc.nextLine();

        Medicine selectedMedicine = null;

        if (option == 1) {
            System.out.print("Enter Medicine ID: ");
            String id = sc.nextLine();
            for (Medicine m : medicineList) {
                if (m.getMedicineID().equalsIgnoreCase(id)) {
                    selectedMedicine = m;
                    break;
                }
            }
        } else if (option == 2) {
            System.out.print("Enter Medicine Name: ");
            String name = sc.nextLine();
            for (Medicine m : medicineList) {
                if (m.getName().equalsIgnoreCase(name)) {
                    selectedMedicine = m;
                    break;
                }
            }
        } else {
            System.out.println("invalid option");

        }

        if (selectedMedicine != null) {
            System.out.println("Current stock: " + selectedMedicine.getStockQuantity());
            System.out.print("Enter quantity to add: ");
            int qty = sc.nextInt();
            selectedMedicine.updateStock(qty);
            System.out.println("Updated stock: " + selectedMedicine.getStockQuantity());
        } else {
            System.out.println("Medicine not found.");
        }
    }

    public void deleteMedicine() {

        System.out.println("Search medicine by:");
        System.out.println("1. Medicine ID");
        System.out.println("2. Medicine Name");
        int option = sc.nextInt();
        sc.nextLine();

        Medicine selectedMedicine = null;

        if (option == 1) {
            System.out.print("Enter Medicine ID: ");
            String id = sc.nextLine();
            for (Medicine m : medicineList) {
                if (m.getMedicineID().equalsIgnoreCase(id)) {
                    selectedMedicine = m;
                    break;
                }
            }
        } else if (option == 2) {
            System.out.print("Enter Medicine Name: ");
            String name = sc.nextLine();
            for (Medicine m : medicineList) {
                if (m.getName().equalsIgnoreCase(name)) {
                    selectedMedicine = m;
                    break;
                }
            }
        } else {
            System.out.println("invalid option");

        }
        if (selectedMedicine != null) {
            System.out.print("Are you sure you want to delete this medicine? (yes/no): ");
            String confirm = sc.nextLine();
            if (confirm.equalsIgnoreCase("yes")) {
                medicineList.remove(selectedMedicine);
                System.out.println("Medicine deleted successfully.");
            } else {
                System.out.println("Deletion cancelled.");
            }
        } else {
            System.out.println("Medicine not found.");
        }
    }

    private void checkExpiredMedicines() {
        System.out.print("Enter current date (YYYY-MM-DD): ");
        String currentDate = sc.nextLine();

        List<Medicine> expiredList = new ArrayList<>();

        for (Medicine med : medicineList) {
            if (med.checkExpiry(currentDate)) {
                System.out.println("Expired: " + med.getName());
                expiredList.add(med);
            }
        }

        if (expiredList.isEmpty()) {
            System.out.println("No expired medicines found.");
        } else {
            System.out.print("Do you want to remove all expired medicines? (yes/no): ");
            String input = sc.nextLine();

            if (input.equalsIgnoreCase("yes")) {
                medicineList.removeAll(expiredList);
                System.out.println("Expired medicines removed successfully.");
            } else {
                System.out.println("Expired medicines were not removed.");
            }
        }
    }

    private void checkReorderStatus() {
        boolean found = false;

        for (Medicine med : medicineList) {
            if (med.checkReorderStatus()) {
                System.out.println("Reorder needed: " + med.getName() + " (Stock: " + med.getStockQuantity() + ")");
                found = true;
            }
        }

        if (!found) {
            System.out.println("All medicines have sufficient stock.");
        }
    }

    private void searchMedicine() {
        System.out.println("Search by:");
        System.out.println("1. Medicine ID");
        System.out.println("2. Medicine Name");
        int option = sc.nextInt();
        sc.nextLine();

        boolean found = false;

        if (option == 1) {
            System.out.print("Enter Medicine ID: ");
            String id = sc.nextLine();
            for (Medicine m : medicineList) {
                if (m.getMedicineID().equalsIgnoreCase(id)) {
                    System.out.println("Medicine Found:");
                    m.displayDetails();
                    found = true;
                    break;
                }
            }
        } else if (option == 2) {
            System.out.print("Enter Medicine Name: ");
            String name = sc.nextLine();
            for (Medicine m : medicineList) {
                if (m.getName().equalsIgnoreCase(name)) {
                    System.out.println("Medicine Found:");
                    m.displayDetails();
                    found = true;
                }
            }
        }

        if (!found) {
            System.out.println("Medicine not found.");
        }
    }

    /////helper methods THAT WILL WORK IN GUII//
    ////FOR ORDER MANAGER IN PLACING ORDER
    public List<Medicine> getSortedMedicinesByStock() {
        List<Medicine> sorted = new ArrayList<>(medicineList);
        sorted.sort(Comparator.comparingInt(Medicine::getStockQuantity));
        return sorted;
    }

    ///FOR SERACHINGG..PARTIAL SERACVH AND IT SHOW SY=UGESTIONSS//Returns a list of full Medicine objects whose names contain the search term.
    public List<Medicine> searchMedicinesByName(String partialName) {
        List<Medicine> results = new ArrayList<>();
        for (Medicine med : medicineList) {
            if (med.getName().toLowerCase().contains(partialName.toLowerCase())) {
                results.add(med);
            }
        }
        return results;
    }
    /////FOR UPDATE MEDICINE STOCK Gui///

    public void updateMedicineStockGUI(Medicine medicine, int quantityToAdd) {
        if (medicine != null && quantityToAdd > 0) {
            int newQty = medicine.getStockQuantity() + quantityToAdd;
            medicine.setStockQuantity(newQty);
        }
    }
    ///FOR ADDIMG A MEDICINE
    // Generates a unique medicine ID
    
    public void updateEssentialFields(Medicine med, int qtyToAdd, String newExpiry, int newReorder) {
    if (med != null) {
        updateMedicineStockGUI(med, qtyToAdd); // ⬅️ Already existing method
        med.setExpiryDate(newExpiry);
        med.setReorderLevel(newReorder);
    }
}




    public String generateMedicineID() {
        return String.format("MED%03d", medicineList.size() + 1);
    }

// Adds a new medicine object to list
    public void addMedicineToList(Medicine medicine) {
        if (medicine != null) {
            medicineList.add(medicine);
        }
    }

// Optional: check if a medicine with this name already exists
    public boolean medicineExists(String name) {
        for (Medicine m : medicineList) {
            if (m.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

////////for gui///////////
    
    
    

    ////FOR SEARCHING /////
    public Medicine findMedicineByID(String id) {
        for (Medicine m : medicineList) {
            if (m.getMedicineID().equalsIgnoreCase(id)) {
                return m;
            }
        }
        return null; // not found
    }

    public Medicine findMedicineByName(String name) {
        for (Medicine m : medicineList) {
            if (m.getName().equalsIgnoreCase(name)) {
                return m;
            }
        }
        return null;
    }
/////Returns a list of only matching names (Strings) for dropdown/auto-suggestions.

    public List<Medicine> getMedicineNameSuggestions(String query) {
        List<Medicine> suggestions = new ArrayList<>();
        for (Medicine m : medicineList) {
            if (m.getName().toLowerCase().contains(query.toLowerCase())) {
                suggestions.add(m);
            }
        }
        return suggestions;
    }

    //theen we click on buttin and it wi;; show that medicine with its detail 
    //////delete mediicine gui///
    public boolean deleteMedicine(Medicine med) {
        if (med != null && medicineList.contains(med)) {
            medicineList.remove(med);
            return true;
        }
        return false;
    }
//then click on button and it will show are you sure and then we will delete......also can use anither method not necesary to use this type of logic that afater deleting it ask are you sure or whatever

    ///////check expired medicinee list
    public List<Medicine> getExpiredMedicines(String currentDate) {
        List<Medicine> expired = new ArrayList<>();
        for (Medicine med : medicineList) {
            if (med.checkExpiry(currentDate)) {
                expired.add(med);
            }
        }
        return expired;
    }

    public boolean removeExpiredMedicines(List<Medicine> expiredList) {
        if (expiredList == null || expiredList.isEmpty()) {
            return false;
        }
        return medicineList.removeAll(expiredList);
    }

    ////check reorder medicine listt
    public List<Medicine> getMedicinesNeedingReorder() {
        List<Medicine> reorderList = new ArrayList<>();
        for (Medicine med : medicineList) {
            if (med.checkReorderStatus()) {
                reorderList.add(med);
            }
        }
        return reorderList;
    }

    public List<Medicine> getAllMedicines() {
        return new ArrayList<>(medicineList);  // Return a safe copy
    }

    //////////for viewing///
    public String getAllMedicineDetailsAsString() {
        if (medicineList.isEmpty()) {
            return "No medicine available.";
        }

        StringBuilder sb = new StringBuilder();
        for (Medicine m : medicineList) {
            sb.append(m.displayDetailsAsString()).append("\n----------------\n");
        }
        return sb.toString();
    }

    
    
    
    
    
    //////////////for data baseee//////////
    
    ////adding///
    public boolean insertMedicineIntoDatabase(Medicine medicine) {
    String sql = "INSERT INTO medicine (medicine_id, name, type, price, stock_quantity, expiry_date, reorder_level, requires_prescription, manufacturer_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    try (Connection con = DBConnection.getConnection();
         PreparedStatement stmt = con.prepareStatement(sql)) {

        stmt.setString(1, medicine.getMedicineID());
        stmt.setString(2, medicine.getName());
        stmt.setString(3, medicine.getType());
        stmt.setDouble(4, medicine.getPrice());
        stmt.setInt(5, medicine.getStockQuantity());
        stmt.setString(6, medicine.getExpiryDate());
        stmt.setInt(7, medicine.getReorderLevel());
//        stmt.setBoolean(8, medicine.getRequiresPrescription());
  //String requiresRx = medicine.getRequiresPrescription() ? "Yes" : "No";
        //stmt.setString(8, requiresRx);
        // ✅ Convert boolean to int (1 = true, 0 = false)
int requiresRx = medicine.getRequiresPrescription() ? 1 : 0;
stmt.setInt(8, requiresRx);

        stmt.setString(9, medicine.getManufacturer().getManufacturerID());

        int rowsInserted = stmt.executeUpdate();
        return rowsInserted > 0;

    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}
    
//    public boolean deleteMedicineFromDatabase(String medicineID) {
//    String sql = "DELETE FROM medicine WHERE medicine_id = ?";
//    try (Connection con = DBConnection.getConnection();
//         PreparedStatement stmt = con.prepareStatement(sql)) {
//        stmt.setString(1, medicineID);
//        int rows = stmt.executeUpdate();
//        return rows > 0;
//    } catch (SQLException e) {
//        e.printStackTrace();
//        return false;
//    }
//}
//
//     public boolean deleteMedicineFromDatabase(String medicineID) {
//    String checkSQL = "SELECT COUNT(*) FROM sale_item WHERE medicine_id = ?";
//    String deleteSQL = "DELETE FROM medicine WHERE medicine_id = ?";
//
//    try (Connection con = DBConnection.getConnection();
//         PreparedStatement checkStmt = con.prepareStatement(checkSQL)) {
//
//        checkStmt.setString(1, medicineID);
//        ResultSet rs = checkStmt.executeQuery();
//
//        if (rs.next()) {
//            int count = rs.getInt(1);
//           
//        }
//
//        try (PreparedStatement deleteStmt = con.prepareStatement(deleteSQL)) {
//            deleteStmt.setString(1, medicineID);
//            int rows = deleteStmt.executeUpdate();
//            return rows > 0;
//        }
//
//    } catch (SQLException e) {
//        e.printStackTrace();
//    }
//
//    return false;
//     }
//}
//public boolean deleteMedicineFromDatabase(String medicineID) {
//    String checkSQL = "SELECT COUNT(*) FROM sale_item WHERE medicine_id = ?";
//    String deleteSQL = "DELETE FROM medicine WHERE medicine_id = ?";
//
//    try (Connection con = DBConnection.getConnection();
//         PreparedStatement checkStmt = con.prepareStatement(checkSQL)) {
//
//        checkStmt.setString(1, medicineID);
//        ResultSet rs = checkStmt.executeQuery();
//
//        if (rs.next()) {
//            int count = rs.getInt(1);
//            if (count > 0) {
//                // Optional: Warn but continue
//                System.out.println("Warning: Medicine is linked to sales. Proceeding with delete (SET NULL in FK).");
//            }
//        }
//
//        try (PreparedStatement deleteStmt = con.prepareStatement(deleteSQL)) {
//            deleteStmt.setString(1, medicineID);
//            int rows = deleteStmt.executeUpdate();
//            return rows > 0;
//        }
//
//    } catch (SQLException e) {
//        e.printStackTrace();
//    }
//
//    return false;
//}
public boolean softDeleteMedicineInDatabase(String medicineID) {
    String sql = "UPDATE medicine SET status = 'Inactive' WHERE medicine_id = ?";
    try (Connection con = DBConnection.getConnection();
         PreparedStatement stmt = con.prepareStatement(sql)) {
        stmt.setString(1, medicineID);
        int rows = stmt.executeUpdate();
        return rows > 0;
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}


public Medicine findMedicineByID_DB(String id) {
    String sql = "SELECT * FROM medicine WHERE medicine_id = ? AND status = 'Active'";
    try (Connection con = DBConnection.getConnection();
         PreparedStatement stmt = con.prepareStatement(sql)) {
        stmt.setString(1, id);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return mapResultSetToMedicine(rs);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null;
}

    public Medicine findMedicineByName_DB(String name) {
    String sql = "SELECT * FROM medicine WHERE name = ? AND status = 'Active'";
    try (Connection con = DBConnection.getConnection();
         PreparedStatement stmt = con.prepareStatement(sql)) {
        stmt.setString(1, name);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return mapResultSetToMedicine(rs);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null;
}

    
    public List<Medicine> searchMedicinesByName_DB(String partialName) {
    List<Medicine> results = new ArrayList<>();
    String sql = "SELECT * FROM medicine WHERE name LIKE ? AND status = 'Active'";

    try (Connection con = DBConnection.getConnection();
         PreparedStatement stmt = con.prepareStatement(sql)) {

        stmt.setString(1, "%" + partialName + "%");
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            Medicine med = mapResultSetToMedicine(rs);
            results.add(med);
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return results;
}


    private Medicine mapResultSetToMedicine(ResultSet rs) throws SQLException {
    String id = rs.getString("medicine_id");
    String name = rs.getString("name");
    String type = rs.getString("type");
    double price = rs.getDouble("price");
    int stock = rs.getInt("stock_quantity");
    String expiry = rs.getString("expiry_date");
    int reorder = rs.getInt("reorder_level");
//    boolean requiresPrescription = rs.getString("requires_prescription").equalsIgnoreCase("Yes");
String requiresRxText = rs.getString("requires_prescription");
boolean requiresPrescription = "Yes".equalsIgnoreCase(requiresRxText);

    String manufacturerId = rs.getString("manufacturer_id");

    // Find Manufacturer from existing list:
    Manufacturer manufacturer = null;
    for (Manufacturer m : manufacturerList) {
        if (m.getManufacturerID().equals(manufacturerId)) {
            manufacturer = m;
            break;
        }
    }

    return new Medicine(id, name, type, price, stock, expiry, reorder, requiresPrescription, manufacturer);
}

    
    public List<Medicine> getAllMedicinesFromDatabase() {
    List<Medicine> medicines = new ArrayList<>();
    String sql = "SELECT * FROM medicine WHERE status = 'Active'";
    try (Connection con = DBConnection.getConnection();
         PreparedStatement stmt = con.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {
        while (rs.next()) {
            medicines.add(mapResultSetToMedicine(rs));
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return medicines;
}

    public void loadMedicinesFromDatabase() {
    List<Medicine> fromDB = getAllMedicinesFromDatabase();
    this.medicineList.clear();  // Clear existing list if needed
    this.medicineList.addAll(fromDB);
}
    
    public List<Medicine> getMedicinesByStatus(String status) {
    List<Medicine> filtered = new ArrayList<>();
    String sql = "SELECT * FROM medicine WHERE status = ?";
    
    try (Connection con = DBConnection.getConnection();
         PreparedStatement stmt = con.prepareStatement(sql)) {

        stmt.setString(1, status);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            filtered.add(mapResultSetToMedicine(rs));
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return filtered;
}

    
    public String generateMedicineIDFromDatabase() {
    String sql = "SELECT MAX(CAST(SUBSTRING(medicine_id, 4) AS UNSIGNED)) AS max_id FROM medicine";

    try (Connection con = DBConnection.getConnection();
          PreparedStatement stmt = con.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery(sql)) {

        if (rs.next()) {
            int maxId = rs.getInt("max_id");  // If no record, returns 0
            return String.format("MED%03d", maxId + 1);  // e.g., MED004
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    // DB access failed, better to throw error or alert — not MED001 blindly
    JOptionPane.showMessageDialog(null, "❌ Error generating Medicine ID from database. Check DB connection.");
    return null;  // Or throw an exception instead
}


    public boolean updateEssentialFields_DB(Medicine med, int qtyToAdd, String newExpiry, int newReorder) {
    String sql = "UPDATE medicine SET stock_quantity = stock_quantity + ?, expiry_date = ?, reorder_level = ? WHERE medicine_id = ?";
    
    try (Connection con = DBConnection.getConnection();
         PreparedStatement stmt = con.prepareStatement(sql)) {

        stmt.setInt(1, qtyToAdd);
        stmt.setString(2, newExpiry);
        stmt.setInt(3, newReorder);
        stmt.setString(4, med.getMedicineID());

        int rows = stmt.executeUpdate();

        if (rows > 0) {
            // ✅ Also update in-memory object
            updateMedicineStockGUI(med, qtyToAdd);
            med.setExpiryDate(newExpiry);
            med.setReorderLevel(newReorder);
            return true;
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}

    
    public List<Medicine> getMedicinesNeedingReorderFromDB() {
    List<Medicine> reorderList = new ArrayList<>();
    String sql = "SELECT * FROM medicine WHERE stock_quantity <= reorder_level AND status = 'Active'";

    try (Connection con = DBConnection.getConnection();
         PreparedStatement stmt = con.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {

        while (rs.next()) {
            Medicine med = mapResultSetToMedicine(rs);
            reorderList.add(med);
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return reorderList;
}

    public boolean removeExpiredMedicinesFromDatabase(List<Medicine> expiredList) {
    if (expiredList == null || expiredList.isEmpty()) return false;

    String deleteQuery = "DELETE FROM medicine WHERE medicine_id = ?";

    try (Connection con = DBConnection.getConnection();
         PreparedStatement stmt = con.prepareStatement(deleteQuery)) {

        for (Medicine m : expiredList) {
            stmt.setString(1, m.getMedicineID());
            stmt.addBatch();
        }

        stmt.executeBatch();
        return true;

    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Failed to remove expired medicines.");
        return false;
    }
}

public List<Medicine> getExpiredMedicinesFromDatabase(String currentDate) {
    List<Medicine> expiredList = new ArrayList<>();
    String query = "SELECT * FROM medicine WHERE expiry_date < ? AND status = 'Active'";

    try (Connection con = DBConnection.getConnection();
         PreparedStatement stmt = con.prepareStatement(query)) {

        stmt.setString(1, currentDate);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            Medicine m = mapResultSetToMedicine(rs); // Your existing mapper method
            expiredList.add(m);
        }

    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Error fetching expired medicines.");
    }

    return expiredList;
}


// MedicineManager.java
public void updateMedicineStockInDatabase(String medicineID, int newStock, Connection con) throws SQLException {
    String sql = "UPDATE medicine SET stock_quantity = ? WHERE medicine_id = ?";
    try (PreparedStatement stmt = con.prepareStatement(sql)) {
        stmt.setInt(1, newStock);
        stmt.setString(2, medicineID);
        stmt.executeUpdate();
    }
}


public boolean updateMedicineStockInDatabase(Medicine med) {
    String sql = "UPDATE medicine SET stock_quantity = ? WHERE medicine_id = ?";
    try (Connection con = DBConnection.getConnection(); 
         PreparedStatement stmt = con.prepareStatement(sql)) {
        stmt.setInt(1, med.getStockQuantity());
        stmt.setString(2, med.getMedicineID());
        return stmt.executeUpdate() > 0;
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}

    public static List<String> getAvailablePermissions() {
        return Arrays.asList(
                "Add Medicine",
                "View All Medicines",
                "Update Medicine Stock",
                "Delete Medicine",
                "Check Expired Medicines",
                "Check Reorder Status",
                "Search Medicine"
        );
    }
}

