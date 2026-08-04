package org.example.model;

import javafx.beans.property.*;
import java.time.LocalDate;

public class Pembelian {
    private final IntegerProperty id;
    private final IntegerProperty supplierId;
    private final StringProperty nomorPo;
    private final ObjectProperty<LocalDate> tanggal;
    private final DoubleProperty jumlahTotal;
    private final StringProperty status;

    public Pembelian(int id, int supplierId, String nomorPo, LocalDate tanggal, 
                     double jumlahTotal, String status) {
        this.id = new SimpleIntegerProperty(id);
        this.supplierId = new SimpleIntegerProperty(supplierId);
        this.nomorPo = new SimpleStringProperty(nomorPo);
        this.tanggal = new SimpleObjectProperty<>(tanggal);
        this.jumlahTotal = new SimpleDoubleProperty(jumlahTotal);
        this.status = new SimpleStringProperty(status);
    }

    public int getId() { return id.get(); }
    public int getSupplierId() { return supplierId.get(); }
    public String getNomorPo() { return nomorPo.get(); }
    public LocalDate getTanggal() { return tanggal.get(); }
    public double getJumlahTotal() { return jumlahTotal.get(); }
    public String getStatus() { return status.get(); }

    public IntegerProperty idProperty() { return id; }
    public IntegerProperty supplierIdProperty() { return supplierId; }
    public StringProperty nomorPoProperty() { return nomorPo; }
    public ObjectProperty<LocalDate> tanggalProperty() { return tanggal; }
    public DoubleProperty jumlahTotalProperty() { return jumlahTotal; }
    public StringProperty statusProperty() { return status; }

    public void setStatus(String status) { this.status.set(status); }
    public void setJumlahTotal(double jumlahTotal) { this.jumlahTotal.set(jumlahTotal); }
}
