package org.example.model;

import javafx.beans.property.*;
import java.time.LocalDate;

public class Kas {
    private final IntegerProperty id;
    private final StringProperty deskripsi;
    private final ObjectProperty<LocalDate> tanggal;
    private final DoubleProperty jumlahMasuk;
    private final DoubleProperty jumlahKeluar;
    private final StringProperty kategori;

    public Kas(int id, String deskripsi, LocalDate tanggal, double jumlahMasuk, 
               double jumlahKeluar, String kategori) {
        this.id = new SimpleIntegerProperty(id);
        this.deskripsi = new SimpleStringProperty(deskripsi);
        this.tanggal = new SimpleObjectProperty<>(tanggal);
        this.jumlahMasuk = new SimpleDoubleProperty(jumlahMasuk);
        this.jumlahKeluar = new SimpleDoubleProperty(jumlahKeluar);
        this.kategori = new SimpleStringProperty(kategori);
    }

    public int getId() { return id.get(); }
    public String getDeskripsi() { return deskripsi.get(); }
    public LocalDate getTanggal() { return tanggal.get(); }
    public double getJumlahMasuk() { return jumlahMasuk.get(); }
    public double getJumlahKeluar() { return jumlahKeluar.get(); }
    public String getKategori() { return kategori.get(); }

    public IntegerProperty idProperty() { return id; }
    public StringProperty deskripsiProperty() { return deskripsi; }
    public ObjectProperty<LocalDate> tanggalProperty() { return tanggal; }
    public DoubleProperty jumlahMasukProperty() { return jumlahMasuk; }
    public DoubleProperty jumlahKeluarProperty() { return jumlahKeluar; }
    public StringProperty kategoriProperty() { return kategori; }

    public void setDeskripsi(String deskripsi) { this.deskripsi.set(deskripsi); }
    public void setJumlahMasuk(double jumlahMasuk) { this.jumlahMasuk.set(jumlahMasuk); }
    public void setJumlahKeluar(double jumlahKeluar) { this.jumlahKeluar.set(jumlahKeluar); }
}
