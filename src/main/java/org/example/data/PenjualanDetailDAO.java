package org.example.data;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PenjualanDetailDAO {
    private final Connection connection;

    public PenjualanDetailDAO() {
        this.connection = DatabaseManager.getInstance().getConnection();
    }

    public void save(int penjualanId, int barangId, int jumlah, double hargaSatuan) {
        String sql = """
            INSERT INTO penjualan_detail (penjualan_id, barang_id, jumlah, harga_satuan, subtotal)
            VALUES (?, ?, ?, ?, ?)
        """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, penjualanId);
            ps.setInt(2, barangId);
            ps.setInt(3, jumlah);
            ps.setDouble(4, hargaSatuan);
            ps.setDouble(5, jumlah * hargaSatuan);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error saving penjualan detail: " + e.getMessage());
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM penjualan_detail WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error deleting penjualan detail: " + e.getMessage());
        }
    }

    public List<DetailRow> findByPenjualanId(int penjualanId) {
        List<DetailRow> details = new ArrayList<>();
        String sql = """
            SELECT pd.id, pd.barang_id, b.nama, pd.jumlah, pd.harga_satuan, pd.subtotal
            FROM penjualan_detail pd
            JOIN barang b ON pd.barang_id = b.id
            WHERE pd.penjualan_id = ?
        """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, penjualanId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                details.add(new DetailRow(
                    rs.getInt("id"),
                    rs.getInt("barang_id"),
                    rs.getString("nama"),
                    rs.getInt("jumlah"),
                    rs.getDouble("harga_satuan"),
                    rs.getDouble("subtotal")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error finding penjualan details: " + e.getMessage());
        }
        return details;
    }

    public static class DetailRow {
        public int id;
        public int barangId;
        public String namaBarang;
        public int jumlah;
        public double hargaSatuan;
        public double subtotal;

        public DetailRow(int id, int barangId, String namaBarang, int jumlah, double hargaSatuan, double subtotal) {
            this.id = id;
            this.barangId = barangId;
            this.namaBarang = namaBarang;
            this.jumlah = jumlah;
            this.hargaSatuan = hargaSatuan;
            this.subtotal = subtotal;
        }
    }
}
