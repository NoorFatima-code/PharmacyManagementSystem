/*

 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pharmacymanagmentsystem;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 *
 * @author mapple.pk
 */
public class Medicine {

    /////////Attributes//////////
    private String medicineID;
    private String name;
    private String type;
    private double price;
    private int stockQuantity;
    private String expiryDate;
    private int reorderLevel;
    private boolean requiresPrescription;
    private Manufacturer manufacturer;
       private String supplierId;
    private int stock;


    // //////Constructor///////
    public Medicine(String medicineID, String name, String type, double price, int stockQuantity,
            String expiryDate, int reorderLevel, boolean requiresPrescription, Manufacturer manufacturer) {
        this.medicineID = medicineID;
        this.name = name;
        this.type = type;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.expiryDate = expiryDate;
        this.reorderLevel = reorderLevel;
        this.requiresPrescription = requiresPrescription;
        this.manufacturer = manufacturer;
    }
    
    
        public Medicine(String id, String name, double price, int stock, String supplierId) {
    this.medicineID = id;
    this.name = name;
    this.price = price;
    this.stock = stock;
    this.supplierId = supplierId;
}

    ///////////Getters and Setters//////////
    public String getMedicineID() {
        return medicineID;
    }

    public void setMedicineID(String medicineID) {
        this.medicineID = medicineID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public int getReorderLevel() {
        return reorderLevel;
    }

    public void setReorderLevel(int reorderLevel) {
        this.reorderLevel = reorderLevel;
    }
           
    public boolean getRequiresPrescription() {
        return requiresPrescription;
    }

    public void setRequiresPrescription(boolean requiresPrescription) {
        this.requiresPrescription = requiresPrescription;
    }

    public Manufacturer getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(Manufacturer manufacturer) {
        this.manufacturer = manufacturer;
    }

                ////////// Behaviors ///////////
    public void updateStock(int quantity) {
        this.stockQuantity += quantity;
    }

    public boolean checkExpiry(String currentDate) {
//        return currentDate.compareTo(expiryDate) > 0;
try {
        LocalDate current = LocalDate.parse(currentDate);
        LocalDate expiry = LocalDate.parse(this.expiryDate);
        return current.isAfter(expiry);
    } catch (DateTimeParseException e) {
        return false;
    }

    }

    public boolean checkReorderStatus() {
        return stockQuantity <= reorderLevel;
    }

    public boolean dispenseMedicine(int quantity) {
        if (stockQuantity >= quantity) {
            stockQuantity -= quantity;
            return true;
        }
        return false;

    }

    public void showManufacturerDetails() {
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return "Medicine{" + "medicineID=" + medicineID + ", name=" + name + ", type=" + type + ", price=" + price + ", stockQuantity=" + stockQuantity + ", expiryDate=" + expiryDate + ", reorderLevel=" + reorderLevel + ", requiresPrescription=" + requiresPrescription + ", manufacturer=" + manufacturer + '}';
    }

    public void displayDetails() {
        System.out.println("----------------------------------");
        System.out.println("ID: " + medicineID);
        System.out.println("Name: " + name);
        System.out.println("Type: " + type);
        System.out.println("Price: Rs. " + price);
        System.out.println("Stock: " + stockQuantity);
        System.out.println("Expiry Date: " + expiryDate);
        System.out.println("Reorder Level: " + reorderLevel);
        System.out.println("Prescription Required: " + requiresPrescription);
        System.out.println("Manufacturer: " + manufacturer.getName()); // if available
        System.out.println("----------------------------------");
    }
    
    public String getFormattedDetails() {
    return "Name: " + name + "\n" +
           "Price: " + price + "\n" +
           "Stock: " + stockQuantity + "\n" +
           "Expiry: " + expiryDate;
}

    public String displayDetailsAsString() {
    return "ID: " + medicineID +
           "\nName: " + name +
           "\nType: " + type +
           "\nPrice: " + price +
           "\nStock: " + stockQuantity +
           "\nExpiry: " + expiryDate +
           "\nReorder Level: " + reorderLevel +
           "\nRequires Prescription: " + requiresPrescription +
           "\nManufacturer: " + manufacturer.getName();
}

//     public int getStock() {
//    return stock;
//}

}
