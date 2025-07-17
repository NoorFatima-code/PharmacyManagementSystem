/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pharmacymanagmentsystem;

/**
 *
 * @author mapple.pk
 */
public class SaleItem {

    private Medicine medicine;
    private int quantity;
    

    public SaleItem(Medicine medicine, int quantity) {
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

    public void checkPrescriptionRequirement() {
         if (medicine.getRequiresPrescription()) {
            System.out.println("Prescription required for: " + medicine.getName());
        }

    }

    public void displayItem() {
        System.out.println(medicine.getName() + " x " + quantity + " = " + calculateItemTotal());
    }

}
