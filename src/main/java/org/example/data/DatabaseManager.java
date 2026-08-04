package org.example.data;

import java.sql.*;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:h2:./sparepartfinance";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "";
    private static DatabaseManager instance;
    private Connection connection;

    private DatabaseManager() {
        initializeDatabase();
    }

    public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    private void initializeDatabase() {
        try {
            Class.forName("org.h2.Driver");
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            createTables();
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Database initialization failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void createTables() {
        try (Statement stmt = connection.createStatement()) {
            // Create Barang table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS barang (
                    id INT PRIMARY KEY AUTO_INCREMENT,
                    nama VARCHAR(255) NOT NULL,
                    kode VARCHAR(50) UNIQUE NOT NULL,
                    deskripsi VARCHAR(500),
                    stok INT NOT NULL DEFAULT 0,
                    stok_minimum INT NOT NULL DEFAULT 0,
                    harga_beli DOUBLE NOT NULL DEFAULT 0,
                    harga_jual DOUBLE NOT NULL DEFAULT 0,
                    satuan VARCHAR(50) NOT NULL,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
            """);

            // Create Supplier table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS supplier (
                    id INT PRIMARY KEY AUTO_INCREMENT,
                    nama VARCHAR(255) NOT NULL,
                    contact VARCHAR(20) NOT NULL,
                    alamat VARCHAR(500),
                    email VARCHAR(100),
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
            """);

            // Create Customer table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS customer (
                    id INT PRIMARY KEY AUTO_INCREMENT,
                    nama VARCHAR(255) NOT NULL,
                    phone VARCHAR(20) NOT NULL,
                    alamat VARCHAR(500),
                    email VARCHAR(100),
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
            """);

            // Create Pembelian table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS pembelian (
                    id INT PRIMARY KEY AUTO_INCREMENT,
                    supplier_id INT NOT NULL,
                    nomor_po VARCHAR(50) UNIQUE NOT NULL,
                    tanggal DATE NOT NULL,
                    jumlah_total DOUBLE NOT NULL DEFAULT 0,
                    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
                    keterangan VARCHAR(500),
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY (supplier_id) REFERENCES supplier(id)
                )
            """);

            // Create Pembelian Detail table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS pembelian_detail (
                    id INT PRIMARY KEY AUTO_INCREMENT,
                    pembelian_id INT NOT NULL,
                    barang_id INT NOT NULL,
                    jumlah INT NOT NULL,
                    harga_satuan DOUBLE NOT NULL,
                    subtotal DOUBLE NOT NULL,
                    FOREIGN KEY (pembelian_id) REFERENCES pembelian(id),
                    FOREIGN KEY (barang_id) REFERENCES barang(id)
                )
            """);

            // Create Penjualan table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS penjualan (
                    id INT PRIMARY KEY AUTO_INCREMENT,
                    customer_id INT NOT NULL,
                    nomor_invoice VARCHAR(50) UNIQUE NOT NULL,
                    tanggal DATE NOT NULL,
                    jumlah_total DOUBLE NOT NULL DEFAULT 0,
                    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
                    keterangan VARCHAR(500),
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY (customer_id) REFERENCES customer(id)
                )
            """);

            // Create Penjualan Detail table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS penjualan_detail (
                    id INT PRIMARY KEY AUTO_INCREMENT,
                    penjualan_id INT NOT NULL,
                    barang_id INT NOT NULL,
                    jumlah INT NOT NULL,
                    harga_satuan DOUBLE NOT NULL,
                    subtotal DOUBLE NOT NULL,
                    FOREIGN KEY (penjualan_id) REFERENCES penjualan(id),
                    FOREIGN KEY (barang_id) REFERENCES barang(id)
                )
            """);

            // Create Kas table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS kas (
                    id INT PRIMARY KEY AUTO_INCREMENT,
                    deskripsi VARCHAR(255) NOT NULL,
                    tanggal DATE NOT NULL,
                    jumlah_masuk DOUBLE NOT NULL DEFAULT 0,
                    jumlah_keluar DOUBLE NOT NULL DEFAULT 0,
                    kategori VARCHAR(50) NOT NULL,
                    keterangan VARCHAR(500),
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
            """);

            System.out.println("Database tables created successfully!");
        } catch (SQLException e) {
            System.err.println("Error creating tables: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            }
        } catch (SQLException e) {
            System.err.println("Connection failed: " + e.getMessage());
            e.printStackTrace();
        }
        return connection;
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }
}
