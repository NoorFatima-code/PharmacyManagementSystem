/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pharmacymanagmentsystem;

/**
 *
 * @author mapple.pk
 */
public class OrderItem {
     Medicine medicine;
    int quantity;
   
    public OrderItem(Medicine medicine, int quantity) {
        this.medicine = medicine;
        this.quantity = quantity;
       
    }
    
    

    public Medicine getMedicine() {
        return medicine;
    }


    public int getQuantity() {
        return quantity;
    }

  

    public double calculateItemTotal() {
        return medicine.getPrice() * quantity;
    }

    public void displayItem() {
        System.out.println(medicine.getName() + " | Ordered: " + quantity );
    }

    void setQuantity(int newQuantity) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}

    

