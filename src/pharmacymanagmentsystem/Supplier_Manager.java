/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pharmacymanagmentsystem;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author mapple.pk
 */
public class Supplier_Manager {
    private int supplierCounter=1;
     private static Scanner sc = new Scanner(System.in);
private List<Supplier> supplierList;
private List<Order> orderList;
   public Supplier_Manager(List<Supplier> supplierList) {
       this.supplierList=supplierList;
       this.orderList=orderList;
   }

    
 
   public List<Supplier> getSupplierList() {
       return supplierList;
   }
 
   // Supplier menu display function
   public static void showSupplierMenu( ) {
       int choice;
       do {
           System.out.println("\n===Supplier Menu===\n");
           System.out.println("1. Add Supplier");
           System.out.println("2. View all Suppliers");
           System.out.println("3. Update Supplier");
           System.out.println("4. Delete Supplier");
           System.out.println("5. Search Supplier");
           System.out.println("6. View orders placed to supplier.");
           System.out.print("Enter your choice: ");
           choice = sc.nextInt();
           sc.nextLine();  // Consume newline
 
           switch (choice) {
               case 1:
                   addSupplier();
                   break;
               case 2:
                   viewAllSuppliers();
                   break;
               case 3:
                   updateSupplier();
                   break;
               case 4:
                   deleteSupplier();
                   break;
               case 5:
                   searchSupplier();
                   break;
               case 6:
//              
                   break;
 
               default:
                   System.out.println("Invalid choice. Please try again.");
           }
       } while (choice != 6);
   }
 
   // Add new supplier to the list
   public static void addSupplier() {
//        System.out.println("Enter Supplier ID: ");
//        String supplierID = sc.nextLine();
//        System.out.println("Enter Supplier Name: ");
//        String name = sc.nextLine();
//        System.out.println("Enter Supplier Company: ");
//        String company = sc.nextLine();
//        System.out.println("Enter Supplier Contact: ");
//        String contact = sc.nextLine();
//        
//        Supplier newSupplier = new Supplier(supplierID, name, company, contact, null);
//        supplierList.add(newSupplier);
//        System.out.println("Supplier added successfully!");
   }
 
   // View all suppliers
   public static void viewAllSuppliers() {
//        if (supplierList.isEmpty()) {
//            System.out.println("No suppliers available.");
//        } else {
//            for (Supplier supplier : supplierList) {
//                System.out.println(supplier);
//            }
//        }
   }
 
   
   public static void updateSupplier() {
//        System.out.println("Enter Supplier ID to update: ");
//        String supplierID = sc.nextLine();
//
//        Supplier selectedSupplier = null;
//        for (Supplier supplier : supplierList) {
//            if (supplier.getSupplierID().equals(supplierID)) {
//                selectedSupplier = supplier;
//                break;
//            }
//        }
 
//        if (selectedSupplier != null) {
//            System.out.println("Enter new contact details: ");
//            String newContact = sc.nextLine();
//            selectedSupplier.updateSupplierDetails(newContact);
//            System.out.println("Supplier contact updated successfully!");
//        } else {
//            System.out.println("Supplier not found.");
//        }
 }
 
   // Delete supplier from the list
   public static void deleteSupplier() {
//        System.out.println("Enter Supplier ID to delete: ");
//        String supplierID = sc.nextLine();
//
//        Supplier selectedSupplier = null;
//        for (Supplier supplier : supplierList) {
//            if (supplier.getSupplierID().equals(supplierID)) {
//                selectedSupplier = supplier;
//                break;
//            }
//        }
//
//        if (selectedSupplier != null) {
//            supplierList.remove(selectedSupplier);
//            System.out.println("Supplier deleted successfully!");
//        } else {
//            System.out.println("Supplier not found.");
//        }
   }
 
   // Search a supplier by ID
   public static void searchSupplier() {
//        System.out.println("Enter Supplier ID to search: ");
//        String supplierID = sc.nextLine();
//
//        boolean found = false;
//        for (Supplier supplier : supplierList) {
//            if (supplier.getSupplierID().equals(supplierID)) {
//                System.out.println("Supplier Found: " + supplier);
//                found = true;
//                break;
//            }
//        }
//
//        if (!found) {
//            System.out.println("Supplier not found.");
//        }
   }
   
   
   /////helperss//////
   
   ////finding supplier by name or by id
    public Supplier findSupplierByName(String name) {
   for (Supplier s : supplierList) {
       if (s.getName().equalsIgnoreCase(name)) {
           return s;
       }
   }
   return null;
}
public Supplier findSupplierByID(String id) {
   for (Supplier s : supplierList) {
       if (s.getSupplierID().equalsIgnoreCase(id)) return s;
   }
   return null;
}
 
///getting orders by supllier
public List<Order> getOrdersBySupplier(Supplier supplier) {
   List<Order> result = new ArrayList<>();
   for (Order order : orderList) {
       if (order.getSupplier().getSupplierID().equalsIgnoreCase(supplier.getSupplierID())) {
           result.add(order);
       }
   }
   return result;
}
 
////for deleteinh
public boolean deleteSupplier(Supplier supplier) {
   if (supplier != null && supplierList.contains(supplier)) {
       supplierList.remove(supplier);
       return true;
   }
   return false;
}
 
 
 
 
   
    public List<Supplier> getAllSuppliers() {
       return new ArrayList<>(supplierList);
   }
    public String generateSupplierID() {
       return "SUP" + String.format("%03d", supplierCounter++);
    }
}