/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pharmacymanagmentsystem;
import java.util.ArrayList;
 import java.util.List;
/**
 *
 * @author mapple.pk
 */
public class Supplier {
     ///////Attributes//////
     private String supplierID;
   private String name;
   private String company;
   private String contact;
   private List<Order> suppliedOrders; // This field is the cause of the mismatch
 
   // *** IMPORTANT: Ensure THIS 4-ARGUMENT CONSTRUCTOR EXISTS ***
   // This constructor is what your AddOrderPanel's populateSupplierComboBox expects
   public Supplier(String supplierID, String name, String company, String contact) {
       this.supplierID = supplierID;
       this.name = name;
       this.company = company;
       this.contact = contact;
       this.suppliedOrders = new ArrayList<>(); // Initialize the list to prevent NullPointerExceptions later
   }
 
   // You might also have the 5-argument constructor if you need it elsewhere,
   // but the 4-argument one is essential for populating from the DB.
   /*
   public Supplier(String supplierID, String name, String company, String contact, List<Order> suppliedOrders) {
       this.supplierID = supplierID;
       this.name = name;
       this.company = company;
       this.contact = contact;
       this.suppliedOrders = suppliedOrders;
   }
   */
 
   // Getters
   public String getSupplierID() {
       return supplierID;
   }
 
   public String getName() {
       return name;
   }
 
   public String getCompany() {
       return company;
   }
 
   public String getContact() {
       return contact;
   }
 
   // Setters (if needed for updates, etc.)
   public void setSupplierID(String supplierID) {
       this.supplierID = supplierID;
   }
 
   public void setName(String name) {
       this.name = name;
   }
 
   public void setCompany(String company) {
       this.company = company;
   }
 
   public void setContact(String contact) {
       this.contact = contact;
   }
 
   // Getter and Setter for suppliedOrders - CRUCIAL if you use this list in-memory
   public List<Order> getSuppliedOrders() {
       // Ensure this list is never null if you access it without checking
       if (suppliedOrders == null) {
           suppliedOrders = new ArrayList<>();
       }
       return suppliedOrders;
   }
 
   public void setSuppliedOrders(List<Order> suppliedOrders) {
       this.suppliedOrders = suppliedOrders;
   }
 
   @Override
   public String toString() {
       return "Supplier{" +
              "supplierID='" + supplierID + '\'' +
              ", name='" + name + '\'' +
              ", company='" + company + '\'' +
              ", contact='" + contact + '\'' +
              // ", suppliedOrders=" + suppliedOrders.size() + " orders" + // Be careful with large lists
              '}';
   }
    
}
