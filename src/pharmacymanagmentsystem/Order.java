package pharmacymanagmentsystem;

import java.util.List;
import java.util.ArrayList; // Added for initializing orderItems in no-arg constructor if needed

public class Order {
    private String orderID;
    private Supplier supplier;
    private String processedBy; // Renamed from "orderedBy" for consistency with AddOrderPanel
    private List<OrderItem> orderItems; // Renamed from "itemList"
    private String status;

    // Constructor for creating a new order (from AddOrderPanel)
    public Order(String orderID, Supplier supplier, String processedBy, List<OrderItem> orderItems) {
        this.orderID = orderID;
        this.supplier = supplier;
        this.processedBy = processedBy;
        this.orderItems = orderItems;
        this.status = "Pending"; // Default status for new orders
    }

    // Constructor for loading from DB (used in SearchOrderPanel)
    public Order(String orderID, Supplier supplier, String processedBy, String status) {
        this.orderID = orderID;
        this.supplier = supplier;
        this.processedBy = processedBy;
        this.status = status;
        this.orderItems = new ArrayList<>(); // Initialize an empty list, items will be added later
    }

    Order(String orderID, List<OrderItem> orderItems, String orderedBy, Supplier selectedSupplier, String pending) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    // Getters
    public String getOrderID() { return orderID; }
    public Supplier getSupplier() { return supplier; }
    public String getProcessedBy() { return processedBy; } // Consistent with AddOrderPanel
    public List<OrderItem> getOrderItems() { return orderItems; } // Consistent with AddOrderPanel
    public String getStatus() { return status; }

    // Setter for orderItems - IMPORTANT for SearchOrderPanel
    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    // Setter for status (if needed for update status functionality)
    public void setStatus(String status) { this.status = status; }

    // Calculates total from the current orderItems list
    public double calculateTotalAmount() { // Renamed from calculateTotalPrice
        double total = 0;
        if (orderItems != null) {
            for (OrderItem item : orderItems) {
                total += item.calculateItemTotal();
            }
        }
        return total;
    }

    

    Iterable<OrderItem> getItemList() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    Object[] calculateTotalPrice() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    void displayOrder() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    void setOrderedBy(String processedBy) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    void setProcessedBy(String processedBy) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    
}