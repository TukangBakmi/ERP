package org.example.data;

import org.example.model.Penjualan;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PenjualanDAO {
    private final Connection connection;

    public PenjualanDAO() {
        this.connection = DatabaseManager.getInstance().getConnection();
    }

    public void save(Penjualan penjualan) {
        String sql = """
            INSERT INTO penjualan (customer_id, nomor_invoice, tanggal, jumlah_total, status)
            VALUES (?, ?, ?, ?, ?)
        """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, penjualan.getCustomerId());
            ps.setString(2, penjualan.getNomorInvoice());
            ps.setDate(3, java.sql.Date.valueOf(penjualan.getTanggal()));
            ps.setDouble(4, penjualan.getJumlahTotal());
            ps.setString(5, penjualan.getStatus());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error saving penjualan: " + e.getMessage());
        }
    }

    public void update(Penjualan penjualan) {
        String sql = """
            UPDATE penjualan SET customer_id=?, nomor_invoice=?, tanggal=?, jumlah_total=?, status=? WHERE id=?
        """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, penjualan.getCustomerId());
            ps.setString(2, penjualan.getNomorInvoice());
            ps.setDate(3, java.sql.Date.valueOf(penjualan.getTanggal()));
            ps.setDouble(4, penjualan.getJumlahTotal());
            ps.setString(5, penjualan.getStatus());
            ps.setInt(6, penjualan.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating penjualan: " + e.getMessage());
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM penjualan WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error deleting penjualan: " + e.getMessage());
        }
    }

    public Penjualan findById(int id) {
        String sql = "SELECT * FROM penjualan WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return extractPenjualan(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error finding penjualan: " + e.getMessage());
        }
        return null;
    }

    public List<Penjualan> findAll() {
        List<Penjualan> penjualanList = new ArrayList<>();
        String sql = "SELECT * FROM penjualan ORDER BY tanggal DESC";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                penjualanList.add(extractPenjualan(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding all penjualan: " + e.getMessage());
        }
        return penjualanList;
    }

    public List<Penjualan> findByStatus(String status) {
        List<Penjualan> penjualanList = new ArrayList<>();
        String sql = "SELECT * FROM penjualan WHERE status=? ORDER BY tanggal DESC";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, status);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                penjualanList.add(extractPenjualan(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding penjualan by status: " + e.getMessage());
        }
        return penjualanList;
    }

    private Penjualan extractPenjualan(ResultSet rs) throws SQLException {
        return new Penjualan(
            rs.getInt("id"),
            rs.getInt("customer_id"),
            rs.getString("nomor_invoice"),
            rs.getDate("tanggal").toLocalDate(),
            rs.getDouble("jumlah_total"),
            rs.getString("status")
        );
    }
}
