/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ERP;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ProductDAO {
    
    // Method to get all products and return them as a list for the TableView
    public ObservableList<Product> getAllProducts() {
        ObservableList<Product> list = FXCollections.observableArrayList();
        String sql = "SELECT * FROM products";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            // Loop through every row in the database
            while (rs.next()) {
                // Create a new Product object and add it to the list
                Product p = new Product(
                    rs.getInt("item_code"),
                    rs.getString("item_name"),
                    rs.getInt("stock_quantity"),
                    rs.getDouble("price")
                );
                list.add(p);
            }
        } catch (Exception ex) {
            System.out.println("Error loading products: " + ex.getMessage());
        }
        return list;
    }
    
    // Method to deduct stock when a sale is made
    public boolean deductStock(String itemName, int quantityToDeduct) {
        // SQL query to subtract the quantity from the current stock
        String sql = "UPDATE products SET stock_quantity = stock_quantity - ? WHERE item_name = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, quantityToDeduct);
            stmt.setString(2, itemName);
            
            // executeUpdate() is used for INSERT, UPDATE, or DELETE
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0; // Returns true if the update was successful
            
        } catch (Exception ex) {
            System.out.println("Error updating stock: " + ex.getMessage());
            return false;
        }
    }
    
    
    // Method to add a new product to the database
    public boolean addProduct(String itemName, int stockQuantity, double price) {
        // SQL query to insert a new row into the products table
        // Notice we don't insert item_code because it is Auto Increment
        String sql = "INSERT INTO products (item_name, stock_quantity, price) VALUES (?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, itemName);
            stmt.setInt(2, stockQuantity);
            stmt.setDouble(3, price);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0; // Returns true if it successfully inserted a row
            
        } catch (Exception ex) {
            System.out.println("Error adding product: " + ex.getMessage());
            return false;
        }
    }
}