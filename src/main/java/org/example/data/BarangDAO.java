package org.example.data;

import org.example.model.Barang;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BarangDAO {
    private final Connection connection;

    public BarangDAO() {
        this.connection = DatabaseManager.getInstance().getConnection();
    }

    public void save(Barang barang) {
        String sql = """
            INSERT INTO barang (nama, kode, deskripsi, stok, stok_minimum, harga_beli, harga_jual, satuan)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, barang.getNama());
            ps.setString(2, barang.getKode());
            ps.setString(3, barang.getDeskripsi());
            ps.setInt(4, barang.getStok());
            ps.setInt(5, barang.getStokMinimum());
            ps.setDouble(6, barang.getHargaBeli());
            ps.setDouble(7, barang.getHargaJual());
            ps.setString(8, barang.getSatuan());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error saving barang: " + e.getMessage());
        }
    }

    public void update(Barang barang) {
        String sql = """
            UPDATE barang SET nama=?, kode=?, deskripsi=?, stok=?, stok_minimum=?, 
            harga_beli=?, harga_jual=?, satuan=? WHERE id=?
        """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, barang.getNama());
            ps.setString(2, barang.getKode());
            ps.setString(3, barang.getDeskripsi());
            ps.setInt(4, barang.getStok());
            ps.setInt(5, barang.getStokMinimum());
            ps.setDouble(6, barang.getHargaBeli());
            ps.setDouble(7, barang.getHargaJual());
            ps.setString(8, barang.getSatuan());
            ps.setInt(9, barang.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating barang: " + e.getMessage());
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM barang WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error deleting barang: " + e.getMessage());
        }
    }

    public Barang findById(int id) {
        String sql = "SELECT * FROM barang WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return extractBarang(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error finding barang: " + e.getMessage());
        }
        return null;
    }

    public List<Barang> findAll() {
        List<Barang> barangList = new ArrayList<>();
        String sql = "SELECT * FROM barang";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                barangList.add(extractBarang(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding all barang: " + e.getMessage());
        }
        return barangList;
    }

    public List<Barang> findLowStock() {
        List<Barang> barangList = new ArrayList<>();
        String sql = "SELECT * FROM barang WHERE stok <= stok_minimum";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                barangList.add(extractBarang(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding low stock items: " + e.getMessage());
        }
        return barangList;
    }

    private Barang extractBarang(ResultSet rs) throws SQLException {
        return new Barang(
            rs.getInt("id"),
            rs.getString("nama"),
            rs.getString("kode"),
            rs.getString("deskripsi"),
            rs.getInt("stok"),
            rs.getInt("stok_minimum"),
            rs.getDouble("harga_beli"),
            rs.getDouble("harga_jual"),
            rs.getString("satuan")
        );
    }
}
