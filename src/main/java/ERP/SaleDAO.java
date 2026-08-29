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

public class SaleDAO {

    // Method to get all sales history for the TableView
    public ObservableList<Sale> getAllSales() {
        ObservableList<Sale> list = FXCollections.observableArrayList();
        // SQL JOIN query to get the actual names instead of just the IDs
        String sql = "SELECT s.sale_id, c.full_name, p.item_name, s.quantity, s.total_price, s.sale_date " +
                     "FROM sales s " +
                     "JOIN customers c ON s.customer_id = c.customer_id " +
                     "JOIN products p ON s.item_code = p.item_code";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Sale s = new Sale(
                    rs.getInt("sale_id"),
                    rs.getString("full_name"),
                    rs.getString("item_name"),
                    rs.getInt("quantity"),
                    rs.getDouble("total_price"),
                    rs.getString("sale_date")
                );
                list.add(s);
            }
        } catch (Exception ex) {
            System.out.println("Error loading sales: " + ex.getMessage());
        }
        return list;
    }

    // Method to insert a new transaction into the sales table
    public boolean recordSale(int customerId, int itemCode, int quantity, double totalPrice) {
        String sql = "INSERT INTO sales (customer_id, item_code, quantity, total_price) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, customerId);
            stmt.setInt(2, itemCode);
            stmt.setInt(3, quantity);
            stmt.setDouble(4, totalPrice);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (Exception ex) {
            System.out.println("Error recording sale: " + ex.getMessage());
            return false;
        }
    }
}