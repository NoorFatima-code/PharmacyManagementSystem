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
public class Manufacturer {

    private static int counter = 1;
    private String manufacturerID;
    private String name;
    private String address;
    private String contact;
    private String email;
    private String country;

//    public Manufacturer(String name, String address, String contact, String email, String country) {
//        this.manufacturerID = "M" + String.format("%03d", counter++);
//        this.name = name;
//        this.address = address;
//        this.contact = contact;
//        this.email = email;
//        this.country = country;
//    }
    // Constructor with ID
    public Manufacturer(String manufacturerID, String name, String address, String contact, String email, String country) {
        this.manufacturerID = manufacturerID;
        this.name = name;
        this.address = address;
        this.contact = contact;
        this.email = email;
        this.country = country;
    }

    public String getManufacturerID() {
        return manufacturerID;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getContact() {
        return contact;
    }

    public String getEmail() {
        return email;
    }

    public String getCountry() {
        return country;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void updateManufactureDetails(String newName) {
        this.name = newName;
    }
    @Override
public String toString() {
    return name; // Or return name + " (" + country + ")" if you want more detail
}

}
