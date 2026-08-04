package org.example.model;

import javafx.beans.property.*;
import java.time.LocalDate;

public class Penjualan {
    private final IntegerProperty id;
    private final IntegerProperty customerId;
    private final StringProperty nomorInvoice;
    private final ObjectProperty<LocalDate> tanggal;
    private final DoubleProperty jumlahTotal;
    private final StringProperty status;

    public Penjualan(int id, int customerId, String nomorInvoice, LocalDate tanggal, 
                     double jumlahTotal, String status) {
        this.id = new SimpleIntegerProperty(id);
        this.customerId = new SimpleIntegerProperty(customerId);
        this.nomorInvoice = new SimpleStringProperty(nomorInvoice);
        this.tanggal = new SimpleObjectProperty<>(tanggal);
        this.jumlahTotal = new SimpleDoubleProperty(jumlahTotal);
        this.status = new SimpleStringProperty(status);
    }

    public int getId() { return id.get(); }
    public int getCustomerId() { return customerId.get(); }
    public String getNomorInvoice() { return nomorInvoice.get(); }
    public LocalDate getTanggal() { return tanggal.get(); }
    public double getJumlahTotal() { return jumlahTotal.get(); }
    public String getStatus() { return status.get(); }

    public IntegerProperty idProperty() { return id; }
    public IntegerProperty customerIdProperty() { return customerId; }
    public StringProperty nomorInvoiceProperty() { return nomorInvoice; }
    public ObjectProperty<LocalDate> tanggalProperty() { return tanggal; }
    public DoubleProperty jumlahTotalProperty() { return jumlahTotal; }
    public StringProperty statusProperty() { return status; }

    public void setStatus(String status) { this.status.set(status); }
    public void setJumlahTotal(double jumlahTotal) { this.jumlahTotal.set(jumlahTotal); }
}
