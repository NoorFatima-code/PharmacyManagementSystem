/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pharmacymanagmentsystem;

import java.util.List;
/**
 *
 * @author mapple.pk
 */
public class Customer {
    /////////Attrubutes//////
   private String customerID;
    private String name;
    private String contactNumber;

    // Constructor
    public Customer(String customerID, String name, String contactNumber) {
        this.customerID = customerID;
        this.name =name;
        this.contactNumber = contactNumber;
        this.contactNumber = contactNumber;
    }

    //////// Getters and Setters/////////
    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    ////////// Behaviors/////////
    public void createCustomerRecord() {
        
    }

    public void viewPurchaseHistory() {
        
    }

    public void applyDiscount() {
        
    }

    public void addPurchase(SalesRecord sale) {
        
    }
    @Override
public String toString() {
    return customerID + " - " + name;
}

    
}
