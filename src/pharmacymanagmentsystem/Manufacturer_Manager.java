/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pharmacymanagmentsystem;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.swing.JOptionPane;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import pharmacymanagmentsystem.DBConnection;

/**
 *
 * @author mapple.pk
 */
public class Manufacturer_Manager {
    
  private static Scanner sc = new Scanner(System.in);
 private List<Manufacturer> manufacturerList;

   
    public List<Manufacturer> getManufacturerList() {
        return manufacturerList;
    }

    public Manufacturer_Manager(List<Manufacturer> manufacturerList) {
        this.manufacturerList = manufacturerList;
    }

    public void showManufacturerMenu() {
    int choice;
    do {
        System.out.println("\n=== Manufacturer Menu ===");
        System.out.println("1. Add Manufacturer");
        System.out.println("2. View All Manufacturers");
        System.out.println("3. Update Manufacturer");
        System.out.println("4. Delete Manufacturer");
        System.out.println("5. Search Manufacturer");
        System.out.println("6. Exit");
        System.out.print("Enter your choice: ");
        choice = sc.nextInt();
        sc.nextLine(); // Consume newline

        switch (choice) {
            case 1:
                addManufacturer();
                break;
            case 2:
//                viewAllManufacturers();
                break;
            case 3:
                updateManufacturer();
                break;
            case 4:
                deleteManufacturer();
                break;
            case 5:
                searchManufacturer();
                break;
            case 6:
                System.out.println("Returning...");
                break;
            default:
                System.out.println("Invalid choice.");
        }
    } while (choice != 6);
}

    public void addManufacturer() {
//    String manufacturerID = String.format("MED%03d", manufacturerList.size() + 1);
//
//    System.out.print("Enter Name: ");
//    String name = sc.nextLine();
//    Manufacturer m = new Manufacturer(manufacturerID, name);
//    manufacturerList.add(m);
//    System.out.println("Manufacturer added successfully.");
//}
//
//    public void viewAllManufacturers() {
//    if (manufacturerList.isEmpty()) {
//        System.out.println("No manufacturers found.");
//    } else {
//        for (Manufacturer m : manufacturerList) {
//            System.out.println("ID: " + m.getManufacturerID() +
//                               ", Name: " + m.getName());
//                            
//        }
//    }
    
}

    public void updateManufacturer() {
//    System.out.print("Enter Manufacturer ID to update: ");
//    String id = sc.nextLine();
//
//    Manufacturer found = findByID(id);
//    if (found != null) {
//        System.out.print("Enter new name: ");
//        String name = sc.nextLine();
//        System.out.print("Enter new contact: ");
//        String contact = sc.nextLine();
//
//        found.setName(name);
//       
//        System.out.println("Manufacturer updated successfully.");
//    } else {
//        System.out.println("Manufacturer not found.");
//    }
}
    
    public void deleteManufacturer() {
//    System.out.print("Enter Manufacturer ID to delete: ");
//    String id = sc.nextLine();
//
//    Manufacturer found = findByID(id);
//    if (found != null) {
//        manufacturerList.remove(found);
//        System.out.println("Manufacturer deleted successfully.");
//    } else {
//        System.out.println("Manufacturer not found.");
//    }
}

public void searchManufacturer() {
//    System.out.print("Enter Manufacturer ID or Name to search: ");
//    String input = sc.nextLine().trim().toLowerCase();
//
//    boolean found = false;
//    for (Manufacturer m : manufacturerList) {
//        if (m.getManufacturerID().equalsIgnoreCase(input) ||
//            m.getName().toLowerCase().contains(input)) {
//            System.out.println("ID: " + m.getManufacturerID() +
//                               ", Name: " + m.getName() );
//            found = true;
//        }
//    }
//
//    if (!found) {
//        System.out.println("Manufacturer not found.");
//    }
}


//////////////helper methods for guui////////////

// 1. find findByID
// 2. find manufacturerByName
// 3. for adding manufacture :
//////// generateMnaufatureID
//////// addMnufacturetoList
//////// manufactureExists
//////// addMnufactureFinal

// 4. no 1 and no 2 will be used for it

//5. for deleting manufacture ////gui ki class ma pehly serach karein gy by name ya by id (combo bix ky zaroye ) 
//phir jo result show ho ga  us manufacture ko delet ajrein gy by delete button sirf us ki logoc lganii deleteManufacturer(Mnaufacturer mn)

 //6. updateManufactureDetails:
//////////manufacture class ma aik meyhod para hua is ky liye ussi ko call krein gyy..is ma bhi pehly serach ho ag by id bya by name aur phir jab matching mil jaye gi to updatee



// main dashboard frame ..main frame..also useed card layoutt foer switching between diff panels ...is ky ander panel hoon like medicine manager panel ...manufacturer manager panel..ordermanager etc
/////ManufactureeManagerPanel(class py click -->new-->JPanel form 
/////mamufavture manaer apnel ky ander pura menu wala aaye ga un sab ky alag alag panel banein gy ..card layout ky zariye switch krein ga different panels ma
//// 1. add manufaturer oanel
//// 2. delete  manufaturer oanel
//// 3. view manufaturer oanel
//// 4.  search manufaturer oanel
///// 5. update manufaturer oanel

    // Returns all manufacturers — use in JComboBox in GUI///view ky liye bhi use ho jaye ga 

 public boolean addManufacturer(String id, String name, String address, String contact, String email, String country) {
    for (Manufacturer m : manufacturerList) {
        if (m.getManufacturerID().equalsIgnoreCase(id)) {
            return false; // Duplicate ID
        }
    }

    Manufacturer newMan = new Manufacturer(id, name, address, contact, email, country);
    manufacturerList.add(newMan);
    return true;
}
 
 public boolean addManufacturerToDatabase(String id, String name, String address, String contact, String email, String country) {

     String insertQuery = "INSERT INTO manufacturer (manufacturer_id, name, address, contact, email, country) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(insertQuery)) {

            stmt.setString(1, id);
            stmt.setString(2, name);
            stmt.setString(3, address);
            stmt.setString(4, contact);
            stmt.setString(5, email);
            stmt.setString(6, country);

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error adding manufacturer: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
// public String getNextManufacturerIDFromDB() {
//    String nextID = "MAN1";
//    try (Connection con = DBConnection.getConnection();
//         Statement stmt = con.createStatement();
//         ResultSet rs = stmt.executeQuery("SELECT manufacturer_id FROM manufacturer ORDER BY manufacturer_id DESC LIMIT 1")) {
//        
//        if (rs.next()) {
//            String lastId = rs.getString("manufacturer_id"); // e.g., MAN4
//            int lastNum = Integer.parseInt(lastId.substring(3)); // "4"
//            nextID = "MAN" + (lastNum + 1);
//        }
//    } catch (Exception e) {
//        System.out.println("Error getting next ID: " + e.getMessage());
//    }
//    return nextID;
//}
public String getNextManufacturerIDFromDB() {
    String nextID = "MAN001";
    String prefix = "MAN";
    try (Connection con = DBConnection.getConnection();
         Statement stmt = con.createStatement();
         ResultSet rs = stmt.executeQuery("SELECT manufacturer_id FROM manufacturer ORDER BY LENGTH(manufacturer_id) DESC, manufacturer_id DESC LIMIT 1")) {

        if (rs.next()) {
            String lastId = rs.getString("manufacturer_id"); // e.g., MAN011
            if (lastId != null && lastId.startsWith(prefix)) {
                int lastNum = Integer.parseInt(lastId.substring(prefix.length())); // 11
                nextID = String.format("%s%03d", prefix, lastNum + 1); // MAN012
            }
        }
    } catch (Exception e) {
        System.out.println("Error getting next manufacturer ID: " + e.getMessage());
    }
    return nextID;
}



 public List<Manufacturer> getAllManufacturersFromDatabase() {
    List<Manufacturer> list = new ArrayList<>();

    try (Connection conn = DBConnection.getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery("SELECT * FROM manufacturer")) {

        while (rs.next()) {
            String id = rs.getString("manufacturer_id");
            String name = rs.getString("name");
            String address = rs.getString("address");
            String contact = rs.getString("contact");
            String email = rs.getString("email");
            String country = rs.getString("country");

            Manufacturer m = new Manufacturer(id, name, address, contact, email, country);
            list.add(m);
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Error fetching manufacturers: " + e.getMessage());
    }

    return list;
}
 
 public List<Manufacturer> getAllManufacturers() {
    List<Manufacturer> manufacturerList = new ArrayList<>();
    String sql = "SELECT * FROM manufacturer";

    try (Connection con = DBConnection.getConnection();
         Statement stmt = con.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {

        while (rs.next()) {
            Manufacturer m = new Manufacturer(
                rs.getString("manufacturer_id"),
                rs.getString("name"),
                rs.getString("address"),
                rs.getString("contact"),
                rs.getString("email"),
                rs.getString("country")
            );
            manufacturerList.add(m);
        }
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Error fetching manufacturers from DB.");
    }

    return manufacturerList;
}
// 
// public boolean updateManufacturer(Manufacturer updated) {
//    try (Connection con = DBConnection.getConnection()) {
//        String sql = "UPDATE manufacturer SET name = ?, address = ?, contact = ?, email = ?, country = ? WHERE manufacturer_id = ?";
//        PreparedStatement ps = con.prepareStatement(sql);
//        ps.setString(1, updated.getName());
//        ps.setString(2, updated.getAddress());
//        ps.setString(3, updated.getContact());
//        ps.setString(4, updated.getEmail());
//        ps.setString(5, updated.getCountry());
//        ps.setString(6, updated.getManufacturerID());
//
//        int rowsAffected = ps.executeUpdate();
//        return rowsAffected > 0;
//    } catch (SQLException e) {
//        e.printStackTrace();
//        return false;
//    }
//}

 public boolean updateManufacturerInDatabase(Manufacturer m) {
    String query = "UPDATE manufacturer SET name=?, address=?, contact=?, email=?, country=? WHERE manufacturer_id=?";
    
    try (Connection conn = DBConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(query)) {

        stmt.setString(1, m.getName());
        stmt.setString(2, m.getAddress());
        stmt.setString(3, m.getContact());
        stmt.setString(4, m.getEmail());
        stmt.setString(5, m.getCountry());
        stmt.setString(6, m.getManufacturerID());

        int rowsAffected = stmt.executeUpdate();
        return rowsAffected > 0;

    } catch (Exception e) {
        System.out.println("Error updating manufacturer: " + e.getMessage());
        return false;
    }
}
 
public Manufacturer getManufacturerById(String id) {
    String query = "SELECT * FROM manufacturer WHERE manufacturer_id = ?";
    
    try (Connection conn = DBConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(query)) {
        stmt.setString(1, id);
        ResultSet rs = stmt.executeQuery();
        
        if (rs.next()) {
            return new Manufacturer(
                rs.getString("manufacturer_id"),
                rs.getString("name"),
                rs.getString("address"),
                rs.getString("contact"),
                rs.getString("email"),
                rs.getString("country")
            );
        }
    } catch (Exception e) {
        System.out.println("Error fetching manufacturer: " + e.getMessage());
    }
    
    return null;
}

//public boolean deleteManufacturerFromDatabase(String manufacturerId) {
//    String query = "DELETE FROM manufacturer WHERE manufacturer_id = ?";
//    
//    try (Connection conn = DBConnection.getConnection();
//         PreparedStatement stmt = conn.prepareStatement(query)) {
//        
//        stmt.setString(1, manufacturerId);
//        int rowsAffected = stmt.executeUpdate();
//        return rowsAffected > 0;
//        
//    } catch (Exception e) {
//        System.out.println("Error deleting manufacturer: " + e.getMessage());
//         e.printStackTrace();
//        return false;
//    }
//}
public boolean deleteManufacturerFromDatabase(String manufacturerID) {
    String checkSQL = "SELECT COUNT(*) FROM medicine WHERE manufacturer_id = ?";
    String deleteSQL = "DELETE FROM manufacturer WHERE manufacturer_id = ?";

    try (Connection con = DBConnection.getConnection();
         PreparedStatement checkStmt = con.prepareStatement(checkSQL)) {

        checkStmt.setString(1, manufacturerID);
        ResultSet rs = checkStmt.executeQuery();

        if (rs.next()) {
            int count = rs.getInt(1);
            if (count > 0) {
                JOptionPane.showMessageDialog(null, "❌ This manufacturer is linked to one or more medicines and cannot be deleted.");
                return false;
            }
        }

        try (PreparedStatement deleteStmt = con.prepareStatement(deleteSQL)) {
            deleteStmt.setString(1, manufacturerID);
            int rows = deleteStmt.executeUpdate();
            return rows > 0;
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return false;
}

public List<Manufacturer> searchManufacturers(String keyword, String column) {
    List<Manufacturer> list = new ArrayList<>();
    String query = "SELECT * FROM manufacturer WHERE " + column + " LIKE ?";
    
    try (Connection conn = DBConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(query)) {
        
        stmt.setString(1, "%" + keyword + "%");
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
            Manufacturer m = new Manufacturer(
                rs.getString("manufacturer_id"),
                rs.getString("name"),
                rs.getString("address"),
                rs.getString("contact"),
                rs.getString("email"),
                rs.getString("country")
            );
            list.add(m);
        }
    } catch (Exception e) {
        System.out.println("Error searching manufacturers: " + e.getMessage());
    }
    
    return list;
}

  
public void loadManufacturersFromDatabase() {
    try (Connection con = DBConnection.getConnection();
         Statement stmt = con.createStatement();
         ResultSet rs = stmt.executeQuery("SELECT * FROM manufacturer ORDER BY CAST(SUBSTRING(manufacturer_id, 4) AS UNSIGNED)")) {

        manufacturerList.clear();  // very important
        while (rs.next()) {
            String id = rs.getString("manufacturer_id");
            String name = rs.getString("name");
            String address = rs.getString("address");
            String contact = rs.getString("contact");
            String email = rs.getString("email");
            String country = rs.getString("country");
            Manufacturer m = new Manufacturer(id, name, address, contact, email, country);
            manufacturerList.add(m);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}


///medicien class ky helper sy mdad ly len

public int getManufacturerCount() {
        return manufacturerList.size();
    
        
        
        
}



}


