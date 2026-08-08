package org.example.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.example.data.*;
import org.example.model.Barang;
import java.time.LocalDate;
import java.util.List;

public class ReportView {
    private PembelianDAO pembelianDAO;
    private PenjualanDAO penjualanDAO;
    private KasDAO kasDAO;
    private BarangDAO barangDAO;

    public BorderPane create() {
        BorderPane root = new BorderPane();
        pembelianDAO = new PembelianDAO();
        penjualanDAO = new PenjualanDAO();
        kasDAO = new KasDAO();
        barangDAO = new BarangDAO();

        Label title = ModernUIHelper.createTitleLabel("📊 Laporan Keuangan");
        Label description = ModernUIHelper.createSubtitleLabel("Ringkasan keuangan, penjualan, pembelian, dan inventori");

        VBox titleBox = new VBox(5, title, description);

        HBox topBar = new HBox(12, titleBox);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(28, 32, 28, 32));
        topBar.setStyle("-fx-background-color: white;");

        ScrollPane scrollPane = new ScrollPane(buildReportContent());
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle(ModernUIHelper.PAGE_BACKGROUND);

        root.setTop(topBar);
        root.setCenter(scrollPane);
        root.setStyle(ModernUIHelper.PAGE_BACKGROUND);

        return root;
    }

    private VBox buildReportContent() {
        VBox content = new VBox(24);
        content.setPadding(new Insets(32));
        content.setStyle(ModernUIHelper.PAGE_BACKGROUND);

        // Financial Summary Cards
        content.getChildren().add(buildFinancialSummary());

        // Transaction Summary
        content.getChildren().add(buildTransactionSummary());

        // Inventory Summary
        content.getChildren().add(buildInventorySummary());

        return content;
    }

    private VBox buildFinancialSummary() {
        VBox section = new VBox(15);
        section.setStyle("-fx-background-color: white; -fx-border-color: #E2E8F0; -fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 20;");

        Label sectionTitle = new Label("Ringkasan Keuangan");
        sectionTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #0F172A;");

        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(15);

        double saldoKas = kasDAO.getSaldoTotal();
        double totalPembelian = pembelianDAO.findAll().stream()
            .mapToDouble(p -> p.getJumlahTotal()).sum();
        double totalPenjualan = penjualanDAO.findAll().stream()
            .mapToDouble(p -> p.getJumlahTotal()).sum();
        double keuntungan = totalPenjualan - totalPembelian;

        grid.add(createCardPane("Saldo Kas", String.format("Rp %,.0f", saldoKas), 
            saldoKas >= 0 ? "#10B981" : "#DC2626"), 0, 0);
        grid.add(createCardPane("Total Penjualan", String.format("Rp %,.0f", totalPenjualan), "#0EA5A3"), 1, 0);
        grid.add(createCardPane("Total Pembelian", String.format("Rp %,.0f", totalPembelian), "#F59E0B"), 0, 1);
        grid.add(createCardPane("Laba/Rugi", String.format("Rp %,.0f", keuntungan), 
            keuntungan >= 0 ? "#6366F1" : "#DC2626"), 1, 1);

        section.getChildren().addAll(sectionTitle, grid);
        return section;
    }

    private VBox buildTransactionSummary() {
        VBox section = new VBox(15);
        section.setStyle("-fx-background-color: white; -fx-border-color: #E2E8F0; -fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 20;");

        Label sectionTitle = new Label("Ringkasan Transaksi");
        sectionTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #0F172A;");

        HBox statsBox = new HBox(30);
        statsBox.setPadding(new Insets(15));
        statsBox.setStyle("-fx-background-color: #F1F5F9; -fx-background-radius: 6;");

        int pembelianCount = pembelianDAO.findAll().size();
        int pembelianReceived = (int) pembelianDAO.findByStatus("RECEIVED").size();
        int penjualanCount = penjualanDAO.findAll().size();
        int penjualanCompleted = (int) penjualanDAO.findByStatus("COMPLETED").size();

        Label p1 = new Label("Pembelian: " + pembelianCount + " (" + pembelianReceived + " diterima)");
        Label p2 = new Label("Penjualan: " + penjualanCount + " (" + penjualanCompleted + " selesai)");
        Label p3 = new Label("Pending: " + (pembelianCount - pembelianReceived + penjualanCount - penjualanCompleted));

        p1.setStyle("-fx-font-size: 13px; -fx-text-fill: #334155;");
        p2.setStyle("-fx-font-size: 13px; -fx-text-fill: #334155;");
        p3.setStyle("-fx-font-size: 13px; -fx-text-fill: #DC2626; -fx-font-weight: bold;");

        statsBox.getChildren().addAll(p1, p2, p3);
        section.getChildren().addAll(sectionTitle, statsBox);

        return section;
    }

    private VBox buildInventorySummary() {
        VBox section = new VBox(15);
        section.setStyle("-fx-background-color: white; -fx-border-color: #E2E8F0; -fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 20;");

        Label sectionTitle = new Label("Ringkasan Inventori");
        sectionTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #0F172A;");

        List<Barang> lowStockItems = barangDAO.findLowStock();
        List<Barang> allItems = barangDAO.findAll();

        VBox statsBox = new VBox(10);
        statsBox.setPadding(new Insets(15));
        statsBox.setStyle("-fx-background-color: #F1F5F9; -fx-background-radius: 6;");

        Label totalItems = new Label("Total Item: " + allItems.size());
        Label lowStockLabel = new Label("Stok Rendah: " + lowStockItems.size());
        
        totalItems.setStyle("-fx-font-size: 13px; -fx-text-fill: #334155;");
        lowStockLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #DC2626; -fx-font-weight: bold;");

        statsBox.getChildren().addAll(totalItems, lowStockLabel);

        if (!lowStockItems.isEmpty()) {
            Label warningTitle = new Label("Item Stok Rendah:");
            warningTitle.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #DC2626;");
            VBox lowStockList = new VBox(5);
            for (Barang b : lowStockItems) {
                Label item = new Label("• " + b.getNama() + " (Stok: " + b.getStok() + ", Min: " + b.getStokMinimum() + ")");
                item.setStyle("-fx-font-size: 11px; -fx-text-fill: #475569; -fx-padding: 3 0 3 10;");
                lowStockList.getChildren().add(item);
            }
            statsBox.getChildren().addAll(warningTitle, lowStockList);
        }

        section.getChildren().addAll(sectionTitle, statsBox);
        return section;
    }

    private VBox createCardPane(String title, String value, String colorCode) {
        VBox card = new VBox(8);
        card.setPadding(new Insets(15));
        card.setStyle("-fx-background-color: white; -fx-border-color: " + colorCode + "33; -fx-border-radius: 6; -fx-background-radius: 6; -fx-border-width: 2;");

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #64748B;");

        Label valueLabel = new Label(value);
        valueLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: " + colorCode + ";");

        card.getChildren().addAll(titleLabel, valueLabel);
        return card;
    }
}
