/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ERP;

public class Customer {
    private int customerId;
    private String fullName;
    private String phoneNumber;

    // This is the constructor that is currently missing
    public Customer(int customerId, String fullName, String phoneNumber) {
        this.customerId = customerId;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
    }

    // Getters for the TableView
    public int getCustomerId() { return customerId; }
    public String getFullName() { return fullName; }
    public String getPhoneNumber() { return phoneNumber; }
}