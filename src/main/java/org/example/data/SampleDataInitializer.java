package org.example.data;

import org.example.model.*;
import java.time.LocalDate;
import java.util.List;

public class SampleDataInitializer {
    private static final BarangDAO barangDAO = new BarangDAO();
    private static final SupplierDAO supplierDAO = new SupplierDAO();
    private static final CustomerDAO customerDAO = new CustomerDAO();
    private static final PembelianDAO pembelianDAO = new PembelianDAO();
    private static final PenjualanDAO penjualanDAO = new PenjualanDAO();
    private static final KasDAO kasDAO = new KasDAO();
    private static final PembelianDetailDAO pembelianDetailDAO = new PembelianDetailDAO();
    private static final PenjualanDetailDAO penjualanDetailDAO = new PenjualanDetailDAO();

    public static void initializeSampleData() {
        // Only initialize if database is empty
        if (!barangDAO.findAll().isEmpty()) {
            return;
        }

        System.out.println("Initializing sample data...");

        // Add Suppliers
        Supplier s1 = new Supplier(0, "PT. Sumber Spare", "021-555-0101");
        Supplier s2 = new Supplier(0, "CV. Sinar Motor", "021-555-0202");
        Supplier s3 = new Supplier(0, "UD. Karya Jaya", "031-888-1234");
        supplierDAO.save(s1);
        supplierDAO.save(s2);
        supplierDAO.save(s3);
        List<Supplier> suppliers = supplierDAO.findAll();

        // Add Customers
        Customer c1 = new Customer(0, "Budi Santoso", "0812-345-6789");
        Customer c2 = new Customer(0, "Toko Maju Jaya", "0821-999-0000");
        Customer c3 = new Customer(0, "CV. Mobil Sukses", "0851-2345-6789");
        Customer c4 = new Customer(0, "Workshop Rapi", "0877-888-8888");
        customerDAO.save(c1);
        customerDAO.save(c2);
        customerDAO.save(c3);
        customerDAO.save(c4);
        List<Customer> customers = customerDAO.findAll();

        // Add Products
        Barang b1 = new Barang(0, "Ban Motor 80/90", "BAN001", "Ban kualitas premium", 50, 10, 85000, 120000, "PCS");
        Barang b2 = new Barang(0, "Kampas Rem", "KPR001", "Kampas rem depan", 120, 20, 35000, 55000, "SET");
        Barang b3 = new Barang(0, "Aki 12V 5A", "AKI001", "Aki motor 12 volt", 30, 5, 250000, 350000, "PCS");
        Barang b4 = new Barang(0, "Lampu Depan", "LAM001", "Lampu H4 12V", 80, 15, 45000, 75000, "PCS");
        Barang b5 = new Barang(0, "Filter Oli", "FLT001", "Filter oli motor", 150, 30, 15000, 25000, "PCS");
        barangDAO.save(b1);
        barangDAO.save(b2);
        barangDAO.save(b3);
        barangDAO.save(b4);
        barangDAO.save(b5);
        List<Barang> barangs = barangDAO.findAll();

        // Add Purchase Orders
        LocalDate today = LocalDate.now();
        if (suppliers.size() >= 2 && barangs.size() >= 3) {
            Pembelian p1 = new Pembelian(0, suppliers.get(0).getId(), "PO-001-2024", today.minusDays(10), 
                                         6000000, "RECEIVED");
            pembelianDAO.save(p1);
            Pembelian p2 = new Pembelian(0, suppliers.get(1).getId(), "PO-002-2024", today.minusDays(5), 
                                         3500000, "RECEIVED");
            pembelianDAO.save(p2);

            List<Pembelian> pembelians = pembelianDAO.findAll();
            if (pembelians.size() >= 2) {
                pembelianDetailDAO.save(pembelians.get(0).getId(), barangs.get(0).getId(), 50, 85000);
                pembelianDetailDAO.save(pembelians.get(0).getId(), barangs.get(2).getId(), 10, 250000);
                
                pembelianDetailDAO.save(pembelians.get(1).getId(), barangs.get(1).getId(), 80, 35000);
                pembelianDetailDAO.save(pembelians.get(1).getId(), barangs.get(4).getId(), 100, 15000);
            }
        }

        // Add Sales Orders
        if (customers.size() >= 3 && barangs.size() >= 3) {
            Penjualan pj1 = new Penjualan(0, customers.get(0).getId(), "INV-001-2024", today.minusDays(8), 
                                          2400000, "COMPLETED");
            penjualanDAO.save(pj1);
            Penjualan pj2 = new Penjualan(0, customers.get(1).getId(), "INV-002-2024", today.minusDays(3), 
                                          1800000, "COMPLETED");
            penjualanDAO.save(pj2);
            Penjualan pj3 = new Penjualan(0, customers.get(2).getId(), "INV-003-2024", today, 
                                          3200000, "PENDING");
            penjualanDAO.save(pj3);

            List<Penjualan> penjualans = penjualanDAO.findAll();
            if (penjualans.size() >= 3) {
                penjualanDetailDAO.save(penjualans.get(0).getId(), barangs.get(0).getId(), 20, 120000);
                penjualanDetailDAO.save(penjualans.get(0).getId(), barangs.get(3).getId(), 0, 75000);
                
                penjualanDetailDAO.save(penjualans.get(1).getId(), barangs.get(1).getId(), 30, 55000);
                penjualanDetailDAO.save(penjualans.get(1).getId(), barangs.get(4).getId(), 10, 25000);
                
                penjualanDetailDAO.save(penjualans.get(2).getId(), barangs.get(2).getId(), 8, 350000);
                penjualanDetailDAO.save(penjualans.get(2).getId(), barangs.get(3).getId(), 5, 75000);
            }
        }

        // Add Cash Transactions
        kasDAO.save(new Kas(0, "Penerimaan dari penjualan INV-001", today.minusDays(8), 2400000, 0, "SALES"));
        kasDAO.save(new Kas(0, "Pembayaran pembelian PO-001", today.minusDays(8), 0, 6000000, "PURCHASE"));
        kasDAO.save(new Kas(0, "Penerimaan dari penjualan INV-002", today.minusDays(3), 1800000, 0, "SALES"));
        kasDAO.save(new Kas(0, "Biaya operasional bulanan", today.minusDays(2), 0, 500000, "OTHER"));

        System.out.println("Sample data initialized successfully!");
    }
}
