package org.example.data;

import org.example.model.Supplier;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SupplierDAO {
    private final Connection connection;

    public SupplierDAO() {
        this.connection = DatabaseManager.getInstance().getConnection();
    }

    public void save(Supplier supplier) {
        String sql = "INSERT INTO supplier (nama, contact, alamat, email) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, supplier.getName());
            ps.setString(2, supplier.getContact());
            ps.setString(3, "");
            ps.setString(4, "");
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error saving supplier: " + e.getMessage());
        }
    }

    public void update(Supplier supplier) {
        String sql = "UPDATE supplier SET nama=?, contact=?, alamat=?, email=? WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, supplier.getName());
            ps.setString(2, supplier.getContact());
            ps.setString(3, "");
            ps.setString(4, "");
            ps.setInt(5, supplier.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating supplier: " + e.getMessage());
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM supplier WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error deleting supplier: " + e.getMessage());
        }
    }

    public Supplier findById(int id) {
        String sql = "SELECT * FROM supplier WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return extractSupplier(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error finding supplier: " + e.getMessage());
        }
        return null;
    }

    public List<Supplier> findAll() {
        List<Supplier> suppliers = new ArrayList<>();
        String sql = "SELECT * FROM supplier";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                suppliers.add(extractSupplier(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding all suppliers: " + e.getMessage());
        }
        return suppliers;
    }

    private Supplier extractSupplier(ResultSet rs) throws SQLException {
        return new Supplier(
            rs.getInt("id"),
            rs.getString("nama"),
            rs.getString("contact")
        );
    }
}
