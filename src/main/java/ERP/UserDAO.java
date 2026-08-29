/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ERP;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAO {
    
    // Method to check if the username and password exist in the database
    public boolean validateLogin(String username, String password) {
        // SQL query to count how many users match the input
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            // Replace the '?' in the SQL string with the actual input
            stmt.setString(1, username);
            stmt.setString(2, password);
            
            ResultSet rs = stmt.executeQuery();
            
            // If rs.next() is true, it means a matching user was found
            return rs.next();
            
        } catch (Exception ex) {
            System.out.println("Database Error: " + ex.getMessage());
            return false;
        }
    }
}