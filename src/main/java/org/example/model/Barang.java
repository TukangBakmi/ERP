package org.example.model;

import javafx.beans.property.*;

public class Barang {
    private final IntegerProperty id;
    private final StringProperty nama;
    private final StringProperty kode;
    private final StringProperty deskripsi;
    private final IntegerProperty stok;
    private final IntegerProperty stokMinimum;
    private final DoubleProperty hargaBeli;
    private final DoubleProperty hargaJual;
    private final StringProperty satuan;

    public Barang(int id, String nama, String kode, String deskripsi, int stok, 
                  int stokMinimum, double hargaBeli, double hargaJual, String satuan) {
        this.id = new SimpleIntegerProperty(id);
        this.nama = new SimpleStringProperty(nama);
        this.kode = new SimpleStringProperty(kode);
        this.deskripsi = new SimpleStringProperty(deskripsi);
        this.stok = new SimpleIntegerProperty(stok);
        this.stokMinimum = new SimpleIntegerProperty(stokMinimum);
        this.hargaBeli = new SimpleDoubleProperty(hargaBeli);
        this.hargaJual = new SimpleDoubleProperty(hargaJual);
        this.satuan = new SimpleStringProperty(satuan);
    }

    public int getId() { return id.get(); }
    public String getNama() { return nama.get(); }
    public String getKode() { return kode.get(); }
    public String getDeskripsi() { return deskripsi.get(); }
    public int getStok() { return stok.get(); }
    public int getStokMinimum() { return stokMinimum.get(); }
    public double getHargaBeli() { return hargaBeli.get(); }
    public double getHargaJual() { return hargaJual.get(); }
    public String getSatuan() { return satuan.get(); }

    public IntegerProperty idProperty() { return id; }
    public StringProperty namaProperty() { return nama; }
    public StringProperty kodeProperty() { return kode; }
    public StringProperty deskripsiProperty() { return deskripsi; }
    public IntegerProperty stokProperty() { return stok; }
    public IntegerProperty stokMinimumProperty() { return stokMinimum; }
    public DoubleProperty hargaBeliProperty() { return hargaBeli; }
    public DoubleProperty hargaJualProperty() { return hargaJual; }
    public StringProperty satuanProperty() { return satuan; }

    public void setNama(String nama) { this.nama.set(nama); }
    public void setKode(String kode) { this.kode.set(kode); }
    public void setDeskripsi(String deskripsi) { this.deskripsi.set(deskripsi); }
    public void setStok(int stok) { this.stok.set(stok); }
    public void setStokMinimum(int stokMinimum) { this.stokMinimum.set(stokMinimum); }
    public void setHargaBeli(double hargaBeli) { this.hargaBeli.set(hargaBeli); }
    public void setHargaJual(double hargaJual) { this.hargaJual.set(hargaJual); }
    public void setSatuan(String satuan) { this.satuan.set(satuan); }
}
