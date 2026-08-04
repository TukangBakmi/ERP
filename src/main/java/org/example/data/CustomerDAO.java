package org.example.data;

import org.example.model.Customer;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {
    private final Connection connection;

    public CustomerDAO() {
        this.connection = DatabaseManager.getInstance().getConnection();
    }

    public void save(Customer customer) {
        String sql = "INSERT INTO customer (nama, phone, alamat, email) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, customer.getName());
            ps.setString(2, customer.getPhone());
            ps.setString(3, "");
            ps.setString(4, "");
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error saving customer: " + e.getMessage());
        }
    }

    public void update(Customer customer) {
        String sql = "UPDATE customer SET nama=?, phone=?, alamat=?, email=? WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, customer.getName());
            ps.setString(2, customer.getPhone());
            ps.setString(3, "");
            ps.setString(4, "");
            ps.setInt(5, customer.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating customer: " + e.getMessage());
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM customer WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error deleting customer: " + e.getMessage());
        }
    }

    public Customer findById(int id) {
        String sql = "SELECT * FROM customer WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return extractCustomer(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error finding customer: " + e.getMessage());
        }
        return null;
    }

    public List<Customer> findAll() {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM customer";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                customers.add(extractCustomer(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding all customers: " + e.getMessage());
        }
        return customers;
    }

    private Customer extractCustomer(ResultSet rs) throws SQLException {
        return new Customer(
            rs.getInt("id"),
            rs.getString("nama"),
            rs.getString("phone")
        );
    }
}
