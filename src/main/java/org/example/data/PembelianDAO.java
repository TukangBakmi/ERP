package org.example.data;

import org.example.model.Pembelian;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PembelianDAO {
    private final Connection connection;

    public PembelianDAO() {
        this.connection = DatabaseManager.getInstance().getConnection();
    }

    public void save(Pembelian pembelian) {
        String sql = """
            INSERT INTO pembelian (supplier_id, nomor_po, tanggal, jumlah_total, status)
            VALUES (?, ?, ?, ?, ?)
        """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, pembelian.getSupplierId());
            ps.setString(2, pembelian.getNomorPo());
            ps.setDate(3, java.sql.Date.valueOf(pembelian.getTanggal()));
            ps.setDouble(4, pembelian.getJumlahTotal());
            ps.setString(5, pembelian.getStatus());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error saving pembelian: " + e.getMessage());
        }
    }

    public void update(Pembelian pembelian) {
        String sql = """
            UPDATE pembelian SET supplier_id=?, nomor_po=?, tanggal=?, jumlah_total=?, status=? WHERE id=?
        """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, pembelian.getSupplierId());
            ps.setString(2, pembelian.getNomorPo());
            ps.setDate(3, java.sql.Date.valueOf(pembelian.getTanggal()));
            ps.setDouble(4, pembelian.getJumlahTotal());
            ps.setString(5, pembelian.getStatus());
            ps.setInt(6, pembelian.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating pembelian: " + e.getMessage());
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM pembelian WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error deleting pembelian: " + e.getMessage());
        }
    }

    public Pembelian findById(int id) {
        String sql = "SELECT * FROM pembelian WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return extractPembelian(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error finding pembelian: " + e.getMessage());
        }
        return null;
    }

    public List<Pembelian> findAll() {
        List<Pembelian> pembelianList = new ArrayList<>();
        String sql = "SELECT * FROM pembelian ORDER BY tanggal DESC";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                pembelianList.add(extractPembelian(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding all pembelian: " + e.getMessage());
        }
        return pembelianList;
    }

    public List<Pembelian> findByStatus(String status) {
        List<Pembelian> pembelianList = new ArrayList<>();
        String sql = "SELECT * FROM pembelian WHERE status=? ORDER BY tanggal DESC";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, status);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                pembelianList.add(extractPembelian(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding pembelian by status: " + e.getMessage());
        }
        return pembelianList;
    }

    private Pembelian extractPembelian(ResultSet rs) throws SQLException {
        return new Pembelian(
            rs.getInt("id"),
            rs.getInt("supplier_id"),
            rs.getString("nomor_po"),
            rs.getDate("tanggal").toLocalDate(),
            rs.getDouble("jumlah_total"),
            rs.getString("status")
        );
    }
}
