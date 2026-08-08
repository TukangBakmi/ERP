package org.example.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class MainLayout {

    public BorderPane create() {

        BorderPane root = new BorderPane();

        BorderPane contentArea = new BorderPane();

        // =========================
        // HEADER (modern gradient)
        // =========================
        Label appTitle = new Label("📊 Sparepart Finance");
        appTitle.setStyle("""
                -fx-font-size: 20px;
                -fx-font-weight: 900;
                -fx-text-fill: white;
                -fx-letter-spacing: 0.5;
                """);

        javafx.scene.control.TextField search = new javafx.scene.control.TextField();
        search.setPromptText("🔍 Cari barang, supplier, pelanggan...");
        search.setPrefWidth(380);
        search.setStyle("""
                -fx-background-radius: 8;
                -fx-border-radius: 8;
                -fx-padding: 10 14 10 14;
                -fx-background-color: rgba(255,255,255,0.15);
                -fx-text-fill: #E6EEF8;
                -fx-prompt-text-fill: #94A3B8;
                -fx-border-color: rgba(255,255,255,0.1);
                -fx-border-width: 1;
                -fx-font-size: 13px;
                """);

        Label userLabel = new Label("👤 Admin");
        userLabel.setStyle("""
                -fx-text-fill: white;
                -fx-font-size: 14px;
                -fx-font-weight: 600;
                """);

        javafx.scene.shape.Circle avatar = new javafx.scene.shape.Circle(18);
        avatar.setStyle("""
                -fx-fill: linear-gradient(to bottom, #38BDF8, #0EA5A3);
                """);

        HBox userBox = new HBox(10, avatar, userLabel);
        userBox.setAlignment(Pos.CENTER);

        HBox header = new HBox(20);
        header.setPadding(new Insets(14, 24, 14, 24));
        header.setAlignment(Pos.CENTER_LEFT);

        HBox spacer = new HBox();
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);

        header.getChildren().addAll(appTitle, spacer, search, userBox);
        header.setStyle("""
                -fx-background-color: linear-gradient(to right, #0F172A 0%, #1E293B 50%, #0F172A 100%);
                -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0.0, 0, 4);
                """);

        // =========================
        // SIDEBAR (enhanced modern)
        // =========================
        Label menuTitle = new Label("MENU");
        menuTitle.setStyle("""
                -fx-font-size: 10px;
                -fx-font-weight: 900;
                -fx-text-fill: #94A3B8;
                -fx-padding: 6 0 12 0;
                -fx-letter-spacing: 1.2;
                """);

        Button dashboardButton = createMenuButton("📈 Dashboard", true);
        Button barangButton = createMenuButton("📦 Barang", false);
        Button supplierButton = createMenuButton("🏭 Supplier", false);
        Button customerButton = createMenuButton("👥 Pelanggan", false);
        Button purchaseButton = createMenuButton("🛒 Pembelian", false);
        Button salesButton = createMenuButton("💰 Penjualan", false);
        Button cashButton = createMenuButton("💵 Kas", false);
        Button reportButton = createMenuButton("📊 Laporan", false);

        VBox sidebar = new VBox(8);
        sidebar.setPadding(new Insets(20));
        sidebar.setPrefWidth(260);

        sidebar.getChildren().addAll(
                menuTitle,
                dashboardButton,
                new javafx.scene.control.Separator(),
                createSectionLabel("MASTER DATA"),
                barangButton,
                supplierButton,
                customerButton,
                createSectionLabel("TRANSAKSI"),
                purchaseButton,
                salesButton,
                createSectionLabel("KEUANGAN"),
                cashButton,
                reportButton,
                new VBox() // spacer
        );

        VBox.setVgrow(sidebar.getChildren().get(sidebar.getChildren().size() - 1), javafx.scene.layout.Priority.ALWAYS);

        sidebar.setStyle("""
                -fx-background-color: linear-gradient(to bottom, #0F172A 0%, #1A1F3A 100%);
                """);

        // Apply hover effects
        java.util.List<Button> menuButtons = java.util.Arrays.asList(
                dashboardButton, barangButton, supplierButton, customerButton,
                purchaseButton, salesButton, cashButton, reportButton
        );

        menuButtons.forEach(b -> applyButtonHover(b));

        // =========================
        // DASHBOARD (enhanced cards)
        // =========================
        Label welcomeTitle = new Label("👋 Selamat Datang, Admin");
        welcomeTitle.setStyle("""
                -fx-font-size: 28px;
                -fx-font-weight: 900;
                -fx-text-fill: #0F172A;
                -fx-letter-spacing: 0.5;
                """);

        Label welcomeDescription = new Label("Kelola persediaan dan keuangan sparepart dengan mudah dan efisien");
        welcomeDescription.setStyle("""
                -fx-font-size: 14px;
                -fx-text-fill: #64748B;
                -fx-wrap-text: true;
                """);

        HBox cards = new HBox(16);
        cards.setPadding(new Insets(20, 0, 20, 0));

        cards.getChildren().addAll(
                createCard("📦 Total Barang", "1,248", "#06B6D4", "#ECFDF5"),
                createCard("⚠️ Stok Rendah", "12", "#F59E0B", "#FFFBEB"),
                createCard("💳 Piutang", "Rp 24.5M", "#EF4444", "#FEF2F2"),
                createCard("💹 Laba Bulan Ini", "Rp 8.2M", "#10B981", "#F0FDF4")
        );

        VBox dashboard = new VBox(16, welcomeTitle, welcomeDescription, cards);
        dashboard.setPadding(new Insets(32));
        dashboard.setStyle("""
                -fx-background-color: linear-gradient(to bottom right, #F8FAFC 0%, #FFFFFF 50%, #F1F5F9 100%);
                """);

        contentArea.setCenter(new javafx.scene.control.ScrollPane(dashboard));

        // Navigation actions
        dashboardButton.setOnAction(event -> {
            contentArea.setCenter(new javafx.scene.control.ScrollPane(dashboard));
            updateMenuButtonState(menuButtons, dashboardButton);
        });

        barangButton.setOnAction(event -> {
            BarangView barangView = new BarangView();
            contentArea.setCenter(new javafx.scene.control.ScrollPane(barangView.create()));
            updateMenuButtonState(menuButtons, barangButton);
        });

        supplierButton.setOnAction(event -> {
            SupplierView supplierView = new SupplierView();
            contentArea.setCenter(new javafx.scene.control.ScrollPane(supplierView.create()));
            updateMenuButtonState(menuButtons, supplierButton);
        });

        customerButton.setOnAction(event -> {
            CustomerView customerView = new CustomerView();
            contentArea.setCenter(new javafx.scene.control.ScrollPane(customerView.create()));
            updateMenuButtonState(menuButtons, customerButton);
        });

        purchaseButton.setOnAction(event -> {
            PurchaseView view = new PurchaseView();
            contentArea.setCenter(new javafx.scene.control.ScrollPane(view.create()));
            updateMenuButtonState(menuButtons, purchaseButton);
        });

        salesButton.setOnAction(event -> {
            SalesView view = new SalesView();
            contentArea.setCenter(new javafx.scene.control.ScrollPane(view.create()));
            updateMenuButtonState(menuButtons, salesButton);
        });

        cashButton.setOnAction(event -> {
            CashView view = new CashView();
            contentArea.setCenter(new javafx.scene.control.ScrollPane(view.create()));
            updateMenuButtonState(menuButtons, cashButton);
        });

        reportButton.setOnAction(event -> {
            ReportView view = new ReportView();
            contentArea.setCenter(new javafx.scene.control.ScrollPane(view.create()));
            updateMenuButtonState(menuButtons, reportButton);
        });

        // =========================
        // SET LAYOUT
        // =========================
        root.setTop(header);
        root.setLeft(sidebar);
        root.setCenter(contentArea);

        // Load CSS styling
        try {
            root.getStylesheets().add(getClass().getResource("/org/example/ui/app.css").toExternalForm());
        } catch (Exception ignored) {}

        return root;
    }

    private Button createMenuButton(String text, boolean selected) {
        Button button = new Button(text);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setMinHeight(44);
        
        String style = selected 
            ? "-fx-background-color: rgba(37,99,235,0.2); -fx-text-fill: #38BDF8; -fx-font-size: 14px; -fx-alignment: CENTER_LEFT; -fx-padding: 12 16; -fx-background-radius: 8; -fx-font-weight: 600;"
            : "-fx-background-color: transparent; -fx-text-fill: #CBD5E1; -fx-font-size: 14px; -fx-alignment: CENTER_LEFT; -fx-padding: 12 16; -fx-background-radius: 8;";
        
        button.setStyle(style);
        return button;
    }

    private Label createSectionLabel(String text) {
        Label label = new Label(text);
        label.setPadding(new Insets(16, 0, 8, 0));
        label.setStyle("""
                -fx-font-size: 11px;
                -fx-font-weight: 900;
                -fx-text-fill: #475569;
                -fx-letter-spacing: 1;
                """);
        return label;
    }

    private void applyButtonHover(Button b) {
        String normal = "-fx-background-color: transparent; -fx-text-fill: #CBD5E1;";
        String hover = "-fx-background-color: rgba(255,255,255,0.08); -fx-text-fill: #E6EEF8;";
        String active = "-fx-background-color: rgba(37,99,235,0.2); -fx-text-fill: #38BDF8;";
        
        b.setStyle(normal + " -fx-font-size: 14px; -fx-alignment: CENTER_LEFT; -fx-padding: 12 16; -fx-background-radius: 8;");
        b.setOnMouseEntered(e -> b.setStyle(hover + " -fx-font-size: 14px; -fx-alignment: CENTER_LEFT; -fx-padding: 12 16; -fx-background-radius: 8;"));
        b.setOnMouseExited(e -> {
            if (!b.getStyle().contains("rgba(37,99,235,0.2)")) {
                b.setStyle(normal + " -fx-font-size: 14px; -fx-alignment: CENTER_LEFT; -fx-padding: 12 16; -fx-background-radius: 8;");
            }
        });
    }

    private void updateMenuButtonState(java.util.List<Button> buttons, Button active) {
        buttons.forEach(b -> {
            if (b == active) {
                b.setStyle("-fx-background-color: rgba(37,99,235,0.2); -fx-text-fill: #38BDF8; -fx-font-size: 14px; -fx-alignment: CENTER_LEFT; -fx-padding: 12 16; -fx-background-radius: 8; -fx-font-weight: 600;");
            } else {
                b.setStyle("-fx-background-color: transparent; -fx-text-fill: #CBD5E1; -fx-font-size: 14px; -fx-alignment: CENTER_LEFT; -fx-padding: 12 16; -fx-background-radius: 8;");
            }
        });
    }

    private VBox createCard(String title, String value, String accentColor, String bgColor) {
        // Icon + Title
        Label t = new Label(title);
        t.setStyle("""
                -fx-text-fill: #64748B;
                -fx-font-size: 13px;
                -fx-font-weight: 600;
                """);
        
        // Value
        Label v = new Label(value);
        v.setStyle("""
                -fx-text-fill: #0F172A;
                -fx-font-size: 24px;
                -fx-font-weight: 900;
                """);

        // Left accent bar
        javafx.scene.shape.Rectangle accent = new javafx.scene.shape.Rectangle(4, 60);
        accent.setStyle(String.format("-fx-fill: %s;", accentColor));
        accent.setArcWidth(2);
        accent.setArcHeight(2);

        HBox contentBox = new HBox(12, accent, new VBox(6, t, v));
        contentBox.setAlignment(Pos.CENTER_LEFT);

        VBox box = new VBox(contentBox);
        box.setPadding(new Insets(16));
        box.setPrefWidth(220);
        box.setStyle(String.format(
            "-fx-background-color: %s; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(15,23,42,0.08), 10, 0.0, 0, 2); -fx-border-color: %s33; -fx-border-radius: 12; -fx-border-width: 1;",
            bgColor, accentColor));

        return box;
    }
}