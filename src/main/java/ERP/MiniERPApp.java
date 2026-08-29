/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ERP;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class MiniERPApp extends Application {

    // Scene variables so we can switch between them easily
    Scene loginScene, homeScene, inventoryScene, customerScene, salesScene;

    public void start(Stage primaryStage) {
        // Initialize DAOs
        UserDAO userDAO = new UserDAO();

        // 3- Scene (Creating the scenes by calling the methods below)
        loginScene = createLoginScene(primaryStage, userDAO);
        homeScene = createHomeScene(primaryStage);
        inventoryScene = createInventoryScene(primaryStage);
        customerScene = createCustomerScene(primaryStage);
        salesScene = createSalesScene(primaryStage);

        // 4- Stage (Setting up the main window)
        primaryStage.setScene(loginScene); // Start at the login page
        primaryStage.setTitle("Mini ERP System");
        primaryStage.setWidth(800);
        primaryStage.setHeight(600);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    // --- SCENE 1: LOGIN ---
    public Scene createLoginScene(Stage stage, UserDAO userDAO) {
        // 1- UI Controls
        Label title = new Label("ERP Login");
        title.setFont(Font.font(24));
        Label lblUser = new Label("Username:");
        Label lblPass = new Label("Password:");
        Label lblError = new Label(); // Hidden by default
        
        TextField txtUser = new TextField();
        PasswordField txtPass = new PasswordField();
        Button btnLogin = new Button("Login");
        btnLogin.setPrefSize(100, 30);

        // 2- Layout (Using GridPane for a neat form)
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setVgap(15);
        grid.setHgap(15);
        
        grid.add(title, 0, 0, 2, 1); // Spans 2 columns
        grid.add(lblUser, 0, 1);
        grid.add(txtUser, 1, 1);
        grid.add(lblPass, 0, 2);
        grid.add(txtPass, 1, 2);
        grid.add(btnLogin, 1, 3);
        grid.add(lblError, 1, 4);

        // Button Action Logic
        btnLogin.setOnAction(e -> {
            if (txtUser.getText().isEmpty() || txtPass.getText().isEmpty()) {
                lblError.setText("Please fill all fields!");
                lblError.setTextFill(Color.RED);
            } else if (userDAO.validateLogin(txtUser.getText(), txtPass.getText())) {
                stage.setScene(homeScene); // Go to Home Scene on success
            } else {
                lblError.setText("Invalid username or password.");
                lblError.setTextFill(Color.RED);
            }
        });

        // 3- Scene
        return new Scene(grid);
    }

    // --- SCENE 2: HOME / DASHBOARD ---
    public Scene createHomeScene(Stage stage) {
        // 1- UI Controls
        Label lblWelcome = new Label("Welcome to the ERP Dashboard");
        lblWelcome.setFont(Font.font(24));
        
        Button btnInventory = new Button("Manage Inventory");
        Button btnCustomers = new Button("Manage Customers");
        Button btnSales = new Button("New Sale");
        Button btnLogout = new Button("Logout");
        
        btnInventory.setPrefSize(200, 40);
        btnCustomers.setPrefSize(200, 40);
        btnSales.setPrefSize(200, 40);
        btnLogout.setPrefSize(200, 40);

        // Navigation Actions
        btnInventory.setOnAction(e -> stage.setScene(inventoryScene));
        btnCustomers.setOnAction(e -> stage.setScene(customerScene));
        btnSales.setOnAction(e -> stage.setScene(salesScene));
        btnLogout.setOnAction(e -> stage.setScene(loginScene));

        // 2- Layout
        VBox root = new VBox(lblWelcome, btnInventory, btnCustomers, btnSales, btnLogout);
        root.setAlignment(Pos.CENTER);
        root.setSpacing(20);

        // 3- Scene
        return new Scene(root);
    }

    // --- SCENE 3: INVENTORY ---
    public Scene createInventoryScene(Stage stage) {
        // 1- UI Controls
        Label title = new Label("Inventory List");
        title.setFont(Font.font(20));
        Button btnBack = new Button("Back to Dashboard");

        TableView<Product> table = new TableView<>();
        
        TableColumn<Product, Integer> colId = new TableColumn<>("Item Code");
        colId.setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        
        TableColumn<Product, String> colName = new TableColumn<>("Name");
        colName.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        
        TableColumn<Product, Integer> colStock = new TableColumn<>("Stock");
        colStock.setCellValueFactory(new PropertyValueFactory<>("stockQuantity"));
        
        TableColumn<Product, Double> colPrice = new TableColumn<>("Price");
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        table.getColumns().addAll(colId, colName, colStock, colPrice);
        
        // Load data from Database
        ProductDAO dao = new ProductDAO();
        table.setItems(dao.getAllProducts());

        btnBack.setOnAction(e -> stage.setScene(homeScene));

        // --- NEW UI CONTROLS TO ADD PRODUCTS ---
        TextField txtName = new TextField();
        txtName.setPromptText("Item Name");
        
        TextField txtStock = new TextField();
        txtStock.setPromptText("Stock Quantity");
        
        TextField txtPrice = new TextField();
        txtPrice.setPromptText("Price");
        
        Button btnAdd = new Button("Add Product");

        btnAdd.setOnAction(e -> {
            try {
                // Get the text from the fields
                String name = txtName.getText();
                int stock = Integer.parseInt(txtStock.getText());
                double price = Double.parseDouble(txtPrice.getText());

                if (name.isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Item Name cannot be empty.");
                    alert.show();
                    return;
                }

                // Call the DAO to save it to the database
                boolean success = dao.addProduct(name, stock, price);
                
                if (success) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Product added successfully!");
                    alert.show();
                    
                    // Clear the text fields so they are ready for the next item
                    txtName.clear();
                    txtStock.clear();
                    txtPrice.clear();
                    
                    // Refresh the table to show the newly added item instantly
                    table.setItems(dao.getAllProducts());
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Database error. Could not add product.");
                    alert.show();
                }
                
            } catch (NumberFormatException ex) {
                // Catches error if they type letters in the stock or price boxes
                Alert alert = new Alert(Alert.AlertType.ERROR, "Stock and Price must be numbers!");
                alert.show();
            }
        });

        // 2- Layout
        // Group the add product inputs horizontally using HBox
        HBox addProductLayout = new HBox(txtName, txtStock, txtPrice, btnAdd);
        addProductLayout.setSpacing(10);
        addProductLayout.setAlignment(Pos.CENTER);

        // Put everything in the main vertical box
        VBox root = new VBox(title, table, addProductLayout, btnBack);
        root.setSpacing(15);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);

        // 3- Scene
        return new Scene(root);
    }

    // --- SCENE 4: CUSTOMERS ---
    public Scene createCustomerScene(Stage stage) {
        // 1- UI Controls
        Label title = new Label("Customer Directory");
        title.setFont(Font.font(20));
        Button btnBack = new Button("Back to Dashboard");

        TableView<Customer> table = new TableView<>();
        
        TableColumn<Customer, Integer> colId = new TableColumn<>("Customer ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        
        TableColumn<Customer, String> colName = new TableColumn<>("Full Name");
        colName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        
        TableColumn<Customer, String> colPhone = new TableColumn<>("Phone Number");
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));

        table.getColumns().addAll(colId, colName, colPhone);
        
        // Load data from Database
        CustomerDAO dao = new CustomerDAO();
        table.setItems(dao.getAllCustomers());

        btnBack.setOnAction(e -> stage.setScene(homeScene));

        // 2- Layout
        VBox root = new VBox(title, table, btnBack);
        root.setSpacing(15);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);

        // 3- Scene
        return new Scene(root);
    }

    // --- SCENE 5: SALES ---
    public Scene createSalesScene(Stage stage) {
        // 1- UI Controls
        Label title = new Label("Sales Transactions");
        title.setFont(Font.font(20));
        Button btnBack = new Button("Back to Dashboard");

        // --- TABLE VIEW FOR TRANSACTIONS ---
        TableView<Sale> table = new TableView<>();
        
        TableColumn<Sale, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("saleId"));
        
        TableColumn<Sale, String> colCustomer = new TableColumn<>("Customer Name");
        colCustomer.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        
        TableColumn<Sale, String> colProduct = new TableColumn<>("Product Name");
        colProduct.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        
        TableColumn<Sale, Integer> colQty = new TableColumn<>("Quantity");
        colQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        
        TableColumn<Sale, Double> colTotal = new TableColumn<>("Total Price");
        colTotal.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        
        TableColumn<Sale, String> colDate = new TableColumn<>("Date");
        colDate.setCellValueFactory(new PropertyValueFactory<>("saleDate"));

        table.getColumns().addAll(colId, colCustomer, colProduct, colQty, colTotal, colDate);
        
        SaleDAO saleDao = new SaleDAO();
        table.setItems(saleDao.getAllSales());

        // --- NEW SALE CONTROLS ---
        ComboBox<String> comboCustomer = new ComboBox<>();
        comboCustomer.setPromptText("Select Customer");
        CustomerDAO customerDao = new CustomerDAO();
        for (Customer c : customerDao.getAllCustomers()) {
            // Format: "ID - Name" so we can easily separate them later
            comboCustomer.getItems().add(c.getCustomerId() + " - " + c.getFullName());
        }

        ComboBox<String> comboProduct = new ComboBox<>();
        comboProduct.setPromptText("Select Product");
        ProductDAO productDao = new ProductDAO();
        for (Product p : productDao.getAllProducts()) {
            // Format: "ID - Name"
            comboProduct.getItems().add(p.getItemCode() + " - " + p.getItemName());
        }

        TextField txtQty = new TextField();
        txtQty.setPromptText("Quantity");

        Button btnSell = new Button("Complete Sale");

        btnSell.setOnAction(e -> {
            try {
                String selectedCustomer = comboCustomer.getValue();
                String selectedProduct = comboProduct.getValue();
                int qty = Integer.parseInt(txtQty.getText());
                
                if (selectedCustomer == null || selectedProduct == null || qty <= 0) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Please select a customer, product, and valid quantity.");
                    alert.show();
                    return;
                }

                // Extract IDs and Names by splitting the string at " - "
                int customerId = Integer.parseInt(selectedCustomer.split(" - ")[0]);
                
                int itemCode = Integer.parseInt(selectedProduct.split(" - ")[0]);
                String itemName = selectedProduct.split(" - ")[1];

                // Find the price of the selected product to calculate the total
                double price = 0;
                for (Product p : productDao.getAllProducts()) {
                    if (p.getItemCode() == itemCode) {
                        price = p.getPrice();
                        break;
                    }
                }
                double totalPrice = price * qty;

                // 1. Record the transaction in the sales table
                boolean recorded = saleDao.recordSale(customerId, itemCode, qty, totalPrice);
                
                // 2. Deduct the stock in the products table
                boolean deducted = productDao.deductStock(itemName, qty);
                
                if (recorded && deducted) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Sale completed successfully!\nTotal: $" + totalPrice);
                    alert.show();
                    
                    txtQty.clear();
                    table.setItems(saleDao.getAllSales()); // Refresh transaction table
                    
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Database error. Could not complete sale.");
                    alert.show();
                }
                
            } catch (NumberFormatException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Quantity must be a number!");
                alert.show();
            }
        });

        btnBack.setOnAction(e -> stage.setScene(homeScene));

        // 2- Layout
        HBox newSaleLayout = new HBox(comboCustomer, comboProduct, txtQty, btnSell);
        newSaleLayout.setSpacing(10);
        newSaleLayout.setAlignment(Pos.CENTER);

        VBox root = new VBox(title, table, newSaleLayout, btnBack);
        root.setSpacing(15);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);

        // 3- Scene
        return new Scene(root);
    }
}