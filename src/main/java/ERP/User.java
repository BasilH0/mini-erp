/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ERP;

public class User {
    // Variables matching the database columns
    private int id;
    private String username;
    private String password;
    private String role;

    // Constructor to initialize the object
    public User(int id, String username, String password, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // Getters to retrieve the data
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
}