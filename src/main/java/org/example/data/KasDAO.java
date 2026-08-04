package org.example.data;

import org.example.model.Kas;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class KasDAO {
    private final Connection connection;

    public KasDAO() {
        this.connection = DatabaseManager.getInstance().getConnection();
    }

    public void save(Kas kas) {
        String sql = """
            INSERT INTO kas (deskripsi, tanggal, jumlah_masuk, jumlah_keluar, kategori)
            VALUES (?, ?, ?, ?, ?)
        """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, kas.getDeskripsi());
            ps.setDate(2, java.sql.Date.valueOf(kas.getTanggal()));
            ps.setDouble(3, kas.getJumlahMasuk());
            ps.setDouble(4, kas.getJumlahKeluar());
            ps.setString(5, kas.getKategori());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error saving kas: " + e.getMessage());
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM kas WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error deleting kas: " + e.getMessage());
        }
    }

    public Kas findById(int id) {
        String sql = "SELECT * FROM kas WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return extractKas(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error finding kas: " + e.getMessage());
        }
        return null;
    }

    public List<Kas> findAll() {
        List<Kas> kasList = new ArrayList<>();
        String sql = "SELECT * FROM kas ORDER BY tanggal DESC";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                kasList.add(extractKas(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding all kas: " + e.getMessage());
        }
        return kasList;
    }

    public List<Kas> findByDateRange(LocalDate startDate, LocalDate endDate) {
        List<Kas> kasList = new ArrayList<>();
        String sql = "SELECT * FROM kas WHERE tanggal BETWEEN ? AND ? ORDER BY tanggal DESC";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setDate(1, java.sql.Date.valueOf(startDate));
            ps.setDate(2, java.sql.Date.valueOf(endDate));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                kasList.add(extractKas(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding kas by date range: " + e.getMessage());
        }
        return kasList;
    }

    public double getSaldoTotal() {
        String sql = "SELECT COALESCE(SUM(jumlah_masuk), 0) - COALESCE(SUM(jumlah_keluar), 0) AS saldo FROM kas";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                return rs.getDouble("saldo");
            }
        } catch (SQLException e) {
            System.err.println("Error calculating saldo: " + e.getMessage());
        }
        return 0;
    }

    private Kas extractKas(ResultSet rs) throws SQLException {
        return new Kas(
            rs.getInt("id"),
            rs.getString("deskripsi"),
            rs.getDate("tanggal").toLocalDate(),
            rs.getDouble("jumlah_masuk"),
            rs.getDouble("jumlah_keluar"),
            rs.getString("kategori")
        );
    }
}
