# Mini ERP System

A small Enterprise Resource Planning (ERP) desktop application built with **JavaFX** and a **MySQL** database. It manages products (inventory), customers, and sales transactions, with a login screen guarding access.

Built as a group project for OOP2 (Applied Science University). This version is set up as a **Maven** project so it runs cleanly in VS Code or IntelliJ.

---

## Tech Stack

- **Java 17+** (JavaFX 21 for the UI)
- **MySQL** (data storage, accessed via JDBC)
- **Maven** (build + dependency management)

---

## Project Structure

All source code lives in `src/main/java/ERP/` and is organized into three layers:

### 1. Model classes (plain data holders)
| File | Represents |
|------|-----------|
| `User.java` | A login account (id, username, password, role) |
| `Product.java` | An inventory item (item code, name, stock, price) |
| `Customer.java` | A customer (id, name, phone) |
| `Sale.java` | A sales transaction (id, customer, product, quantity, total, date) |

### 2. DAO classes (Data Access Objects — the SQL layer)
| File | Responsibility |
|------|---------------|
| `UserDAO.java` | Validates login credentials |
| `ProductDAO.java` | Load all products, add a product, deduct stock |
| `CustomerDAO.java` | Load all customers |
| `SaleDAO.java` | Load sales history (with a JOIN), record a new sale |

### 3. Application + connection
| File | Responsibility |
|------|---------------|
| `DBConnection.java` | Opens the connection to the MySQL database |
| `MiniERPApp.java` | The JavaFX UI — 5 scenes (Login, Dashboard, Inventory, Customers, Sales) |

**Data flow:** JavaFX UI → asks a DAO → DAO runs SQL through `DBConnection` → MySQL → data returns → shown in a `TableView`.

---

## Setup

### Prerequisites
- Java JDK 17 or newer
- Maven
- MySQL server running on `localhost:3306`

*(JavaFX and the MySQL driver are handled automatically by Maven — no manual downloads.)*

### 1. Create the database
Import the included `minierp_db.sql`. It creates the `minierp_db` database, all four tables, and sample data.

```bash
mysql -u root < minierp_db.sql
```

Verify it loaded:
```bash
mysql -u root -e "USE minierp_db; SHOW TABLES;"
```

### 2. Check the DB credentials
`DBConnection.java` connects as user `root` with an empty password. If your MySQL root has a password, update the `PASS` field in that file.

### 3. Run the app
From the project folder:

```bash
mvn clean javafx:run
```

The first run downloads dependencies (takes a minute). The app opens at the Login screen.

---

## Login Credentials

| Username | Password |
|----------|----------|
| `admin` | `admin123` |
| `sara` | `1234` |

---

## Notes

- `item_code` (products) and `customer_id` (customers) are `AUTO_INCREMENT` — the database assigns them automatically.
- `sale_date` defaults to the current timestamp, so each new sale is stamped automatically.
- The `sales` table uses foreign keys to link each sale back to a customer and a product.
