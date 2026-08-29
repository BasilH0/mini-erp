-- ============================================================
--  Mini ERP System - Database Setup Script
--  Database: minierp_db
--
--  HOW TO USE:
--    Option A (phpMyAdmin / XAMPP):
--       - Open phpMyAdmin -> "Import" tab -> choose this file -> Go.
--    Option B (MySQL command line):
--       - mysql -u root -p  <  minierp_db.sql
--
--  This creates the database, all 4 tables, and inserts sample
--  data so the app has something to show and you can log in.
-- ============================================================

-- 1. Create the database and switch to it
CREATE DATABASE IF NOT EXISTS minierp_db;
USE minierp_db;

-- Start clean if the tables already exist (drop children before parents)
DROP TABLE IF EXISTS sales;
DROP TABLE IF EXISTS products;
DROP TABLE IF EXISTS customers;
DROP TABLE IF EXISTS users;

-- ============================================================
-- 2. USERS table
--    Used by UserDAO.validateLogin (SELECT ... WHERE username AND password)
-- ============================================================
CREATE TABLE users (
    id       INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50)  NOT NULL UNIQUE,
    password VARCHAR(50)  NOT NULL,
    role     VARCHAR(20)  NOT NULL DEFAULT 'admin'
);

-- ============================================================
-- 3. PRODUCTS table
--    item_code is AUTO_INCREMENT because ProductDAO.addProduct
--    inserts WITHOUT specifying it.
-- ============================================================
CREATE TABLE products (
    item_code      INT AUTO_INCREMENT PRIMARY KEY,
    item_name      VARCHAR(100)   NOT NULL,
    stock_quantity INT            NOT NULL DEFAULT 0,
    price          DECIMAL(10,2)  NOT NULL DEFAULT 0.00
);

-- ============================================================
-- 4. CUSTOMERS table
-- ============================================================
CREATE TABLE customers (
    customer_id  INT AUTO_INCREMENT PRIMARY KEY,
    full_name    VARCHAR(100) NOT NULL,
    phone_number VARCHAR(20)
);

-- ============================================================
-- 5. SALES table
--    SaleDAO.recordSale inserts customer_id, item_code, quantity,
--    total_price ONLY -> so sale_date must default itself.
--    Foreign keys link back to customers and products (matches
--    the JOINs in SaleDAO.getAllSales).
-- ============================================================
CREATE TABLE sales (
    sale_id     INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT           NOT NULL,
    item_code   INT           NOT NULL,
    quantity    INT           NOT NULL,
    total_price DECIMAL(10,2) NOT NULL,
    sale_date   TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES customers(customer_id),
    FOREIGN KEY (item_code)   REFERENCES products(item_code)
);

-- ============================================================
-- 6. SAMPLE DATA
-- ============================================================

-- Login accounts  (USE THESE TO LOG IN)
--   username: admin   password: admin123
--   username: sara    password: 1234
INSERT INTO users (username, password, role) VALUES
    ('admin', 'admin123', 'admin'),
    ('sara',  '1234',     'staff');

-- Products (item_code auto-generates: 1, 2, 3, 4, 5)
INSERT INTO products (item_name, stock_quantity, price) VALUES
    ('Laptop',        15, 650.00),
    ('Wireless Mouse',50, 12.50),
    ('Keyboard',      40, 25.00),
    ('Monitor 24"',   20, 145.00),
    ('USB-C Cable',  100, 7.75);

-- Customers (customer_id auto-generates: 1, 2, 3)
INSERT INTO customers (full_name, phone_number) VALUES
    ('Omar Khaled',   '0790000001'),
    ('Lina Ahmad',    '0790000002'),
    ('Yousef Nabil',  '0790000003');

-- A couple of past sales so the Sales table isn't empty.
-- (customer_id / item_code refer to the rows created above.)
INSERT INTO sales (customer_id, item_code, quantity, total_price) VALUES
    (1, 1, 1, 650.00),   -- Omar bought 1 Laptop
    (2, 2, 3, 37.50);    -- Lina bought 3 Wireless Mice

-- ============================================================
--  Done. You should now be able to run the JavaFX app and log in
--  with  admin / admin123
-- ============================================================
