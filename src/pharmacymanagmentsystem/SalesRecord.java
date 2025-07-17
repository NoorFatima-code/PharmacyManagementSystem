/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pharmacymanagmentsystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import pharmacymanagmentsystem.DBConnection;

/**
 *
 * @author mapple.pk
 */
public class SalesRecord {

    /////////Attributes//////
    private String saleID;
    private Customer customer;
    private List<SaleItem> soldItems;
   
    private String processedBy;
    private boolean editable;
    private LocalDateTime createdAt;
private boolean cancelled = false;
    ///////// Constructor///////
    public SalesRecord(String saleID,Customer customer, List<SaleItem> soldItems,  String processedBy, Boolean editable,
            LocalDateTime createdAt) {
        this.saleID = saleID;
        this.customer = customer;
//        this.soldItems = soldItems;
//        this.soldItems = soldItems != null ? soldItems : new ArrayList<>();
this.soldItems = new ArrayList<>();
if (soldItems != null) {
    for (SaleItem item : soldItems) {
        this.soldItems.add(new SaleItem(item.getMedicine(), item.getQuantity()));
    }
}
        this.processedBy = processedBy;
//        this.editable = true;
this.editable = editable != null ? editable : true; 
//        this.createdAt = LocalDateTime.now();  // n
this.createdAt = createdAt != null ? createdAt : LocalDateTime.now(); // ✅ اگر null ہو تو اب استعمال کریں

    }

    //////////// Getters and Setters////////
    public String getSaleID() {
        return saleID;
    }
    

    public boolean isCancelled() {
    return cancelled;
}

public void setCancelled(boolean cancelled) {
    this.cancelled = cancelled;
}
    public List<SaleItem> getSoldItems() {
        return soldItems;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    

 

    public String getProcessedBy() {
        return processedBy;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    ///////// Behaviors ///////////
//    public void processSale() {
//
//    }
//
//    public void recordSale() {
//
//    }
    public double calculateTotal() {
double total = 0;
        for (SaleItem item : soldItems) {
            total += item.calculateItemTotal();
        }
        return total;
    }
    
    


    public void printReceipt() {

        System.out.println("=========== SALE RECEIPT =========");
        System.out.println("Sale ID: " + saleID);
        System.out.println("Processed By: " + processedBy);
        if (customer != null) {
            System.out.println("Customer: " + customer.getName());
        }
        System.out.println("Items:");
        for (SaleItem item : soldItems) {
            item.displayItem();
        }
        System.out.printf("Total: Rs. %.2f\n", calculateTotal());
    }

    public void updateEditableStatus() {
        if (Duration.between(this.createdAt, LocalDateTime.now()).toMinutes() > 1) {
            editable = false;
        }
    }
   public void updateEditableStatusInDatabase() {
    if (!this.editable) { // اگر پہلے ہی false ہے تو update کرو
        try (Connection con = DBConnection.getConnection()) {
            String sql = "UPDATE sales_record SET editable = ? WHERE sale_id = ?";
            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setBoolean(1, false);
                stmt.setString(2, this.saleID);
                int rows = stmt.executeUpdate();
               
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}




    public void cancelSale() {
        // Wapas stock mein daal do har item ka quantity
        for (SaleItem item : soldItems) {
            item.getMedicine().updateStock(item.getQuantity());
        }
         soldItems.clear(); // Remove all sale items
         this.cancelled=true;
        
    }
}
