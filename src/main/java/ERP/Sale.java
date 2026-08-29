/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ERP;

public class Sale {
    private int saleId;
    private String customerName;
    private String itemName;
    private int quantity;
    private double totalPrice;
    private String saleDate;

    public Sale(int saleId, String customerName, String itemName, int quantity, double totalPrice, String saleDate) {
        this.saleId = saleId;
        this.customerName = customerName;
        this.itemName = itemName;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.saleDate = saleDate;
    }

    // Getters required for the TableView to read the data
    public int getSaleId() { return saleId; }
    public String getCustomerName() { return customerName; }
    public String getItemName() { return itemName; }
    public int getQuantity() { return quantity; }
    public double getTotalPrice() { return totalPrice; }
    public String getSaleDate() { return saleDate; }
}