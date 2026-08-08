package org.example.data;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PembelianDetailDAO {
    private final Connection connection;

    public PembelianDetailDAO() {
        this.connection = DatabaseManager.getInstance().getConnection();
    }

    public void save(int pembelianId, int barangId, int jumlah, double hargaSatuan) {
        String sql = """
            INSERT INTO pembelian_detail (pembelian_id, barang_id, jumlah, harga_satuan, subtotal)
            VALUES (?, ?, ?, ?, ?)
        """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, pembelianId);
            ps.setInt(2, barangId);
            ps.setInt(3, jumlah);
            ps.setDouble(4, hargaSatuan);
            ps.setDouble(5, jumlah * hargaSatuan);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error saving pembelian detail: " + e.getMessage());
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM pembelian_detail WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error deleting pembelian detail: " + e.getMessage());
        }
    }

    public List<DetailRow> findByPembelianId(int pembelianId) {
        List<DetailRow> details = new ArrayList<>();
        String sql = """
            SELECT pd.id, pd.barang_id, b.nama, pd.jumlah, pd.harga_satuan, pd.subtotal
            FROM pembelian_detail pd
            JOIN barang b ON pd.barang_id = b.id
            WHERE pd.pembelian_id = ?
        """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, pembelianId);
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
            System.err.println("Error finding pembelian details: " + e.getMessage());
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
