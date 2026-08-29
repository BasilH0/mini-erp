/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ERP;

public class Product {
    private int itemCode;
    private String itemName;
    private int stockQuantity;
    private double price;

    public Product(int itemCode, String itemName, int stockQuantity, double price) {
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.stockQuantity = stockQuantity;
        this.price = price;
    }

    // Getters required for the JavaFX TableView to read the data
    public int getItemCode() { return itemCode; }
    public String getItemName() { return itemName; }
    public int getStockQuantity() { return stockQuantity; }
    public double getPrice() { return price; }
}