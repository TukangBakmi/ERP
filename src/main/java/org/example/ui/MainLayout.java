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
        // HEADER (modern)
        // =========================
        Label appTitle = new Label("Sparepart Finance");
        appTitle.setStyle("""
                -fx-font-size: 18px;
                -fx-font-weight: 700;
                -fx-text-fill: white;
                """);

        javafx.scene.control.TextField search = new javafx.scene.control.TextField();
        search.setPromptText("Search transactions, items, suppliers...");
        search.setPrefWidth(340);
        search.setStyle("""
                -fx-background-radius: 6;
                -fx-border-radius: 6;
                -fx-padding: 6 10 6 10;
                -fx-background-color: #0B1220;
                -fx-text-fill: #E6EEF8;
                -fx-prompt-text-fill: #94A3B8;
                """);

        Label userLabel = new Label("Admin");
        userLabel.setStyle("""
                -fx-text-fill: white;
                -fx-font-size: 13px;
                """);

        javafx.scene.shape.Circle avatar = new javafx.scene.shape.Circle(16);
        avatar.setStyle("""
                -fx-fill: linear-gradient(#38BDF8, #0EA5A3);
                """);

        HBox userBox = new HBox(10, avatar, userLabel);
        userBox.setAlignment(Pos.CENTER_RIGHT);

        HBox header = new HBox(20);
        header.setPadding(new Insets(14, 24, 14, 24));
        header.setAlignment(Pos.CENTER_LEFT);

        HBox spacer = new HBox();
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);

        header.getChildren().addAll(appTitle, spacer, search, userBox);
        header.setStyle("""
                -fx-background-color: linear-gradient(#0B1220, #0F172A);
                -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 6, 0.0, 0, 2);
                """);

        // =========================
        // SIDEBAR (modern)
        // =========================
        Label menuTitle = new Label("Menu");
        menuTitle.setStyle("""
                -fx-font-size: 11px;
                -fx-font-weight: 700;
                -fx-text-fill: #94A3B8;
                -fx-padding: 6 0 6 0;
                """);

        Button dashboardButton = createMenuButton("Dashboard");
        Button barangButton = createMenuButton("Barang");
        Button supplierButton = createMenuButton("Supplier");
        Button customerButton = createMenuButton("Customer");
        Button purchaseButton = createMenuButton("Pembelian");
        Button salesButton = createMenuButton("Penjualan");
        Button cashButton = createMenuButton("Kas");
        Button reportButton = createMenuButton("Laporan");

        VBox sidebar = new VBox(10);
        sidebar.setPadding(new Insets(18));
        sidebar.setPrefWidth(240);

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
                reportButton
        );

        sidebar.setStyle("""
                -fx-background-color: #071124;
                """);

        // Apply hover and selection visuals
        java.util.List<Button> menuButtons = java.util.Arrays.asList(
                dashboardButton, barangButton, supplierButton, customerButton,
                purchaseButton, salesButton, cashButton, reportButton
        );

        menuButtons.forEach(b -> {
            applyButtonHover(b);
        });

        // =========================
        // DASHBOARD (cards)
        // =========================
        Label welcomeTitle = new Label("Selamat Datang, Admin");
        welcomeTitle.setStyle("""
                -fx-font-size: 22px;
                -fx-font-weight: 700;
                -fx-text-fill: #0F172A;
                """);

        Label welcomeDescription = new Label("Kelola persediaan dan keuangan sparepart secara efisien.");
        welcomeDescription.setStyle("""
                -fx-font-size: 13px;
                -fx-text-fill: #64748B;
                """);

        HBox cards = new HBox(16);
        cards.setPadding(new Insets(16, 0, 16, 0));

        cards.getChildren().addAll(
                createCard("Total Barang", "1,248", "#0EA5A3"),
                createCard("Persediaan Rendah", "12", "#F59E0B"),
                createCard("Piutang", "Rp 24.500.000", "#EF4444"),
                createCard("Laba Bulan ini", "Rp 8.200.000", "#6366F1")
        );

        VBox dashboard = new VBox(14, welcomeTitle, welcomeDescription, cards);
        dashboard.setPadding(new Insets(22));
        dashboard.setStyle("""
                -fx-background-color: linear-gradient(#F8FAFC, #FFFFFF);
                """);

        contentArea.setCenter(new javafx.scene.control.ScrollPane(dashboard));

        // Navigation actions
        dashboardButton.setOnAction(event -> contentArea.setCenter(new javafx.scene.control.ScrollPane(dashboard)));

        barangButton.setOnAction(event -> {
            BarangView barangView = new BarangView();
            contentArea.setCenter(new javafx.scene.control.ScrollPane(barangView.create()));
        });

        supplierButton.setOnAction(event -> {
            SupplierView supplierView = new SupplierView();
            contentArea.setCenter(new javafx.scene.control.ScrollPane(supplierView.create()));
        });

        customerButton.setOnAction(event -> {
            CustomerView customerView = new CustomerView();
            contentArea.setCenter(new javafx.scene.control.ScrollPane(customerView.create()));
        });

        purchaseButton.setOnAction(event -> {
            PurchaseView view = new PurchaseView();
            contentArea.setCenter(new javafx.scene.control.ScrollPane(view.create()));
        });

        salesButton.setOnAction(event -> {
            SalesView view = new SalesView();
            contentArea.setCenter(new javafx.scene.control.ScrollPane(view.create()));
        });

        cashButton.setOnAction(event -> {
            CashView view = new CashView();
            contentArea.setCenter(new javafx.scene.control.ScrollPane(view.create()));
        });

        reportButton.setOnAction(event -> {
            ReportView view = new ReportView();
            contentArea.setCenter(new javafx.scene.control.ScrollPane(view.create()));
        });

        // =========================
        // SET LAYOUT
        // =========================
        root.setTop(header);
        root.setLeft(sidebar);
        root.setCenter(contentArea);

        // load shared css if available
        try {
            root.getStylesheets().add(getClass().getResource("/org/example/ui/app.css").toExternalForm());
        } catch (Exception ignored) {}

        // responsive sidebar width (18% of window)
        sidebar.prefWidthProperty().bind(root.widthProperty().multiply(0.18));

        return root;
    }

    private Button createMenuButton(String text) {
        Button button = new Button(text);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setStyle("""
                -fx-background-color: transparent;
                -fx-text-fill: #CFE8FF;
                -fx-font-size: 14px;
                -fx-alignment: CENTER_LEFT;
                -fx-padding: 10 14;
                """);
        return button;
    }

    private Label createSectionLabel(String text) {
        Label label = new Label(text);
        label.setPadding(new Insets(10, 0, 4, 0));
        label.setStyle("""
                -fx-font-size: 11px;
                -fx-font-weight: 700;
                -fx-text-fill: #94A3B8;
                """);
        return label;
    }

    private void applyButtonHover(Button b) {
        String normal = "-fx-background-color: transparent; -fx-text-fill: #CFE8FF;";
        String hover = "-fx-background-color: rgba(255,255,255,0.04); -fx-text-fill: white;";
        b.setStyle(normal);
        b.setOnMouseEntered(e -> b.setStyle(hover));
        b.setOnMouseExited(e -> b.setStyle(normal));
        b.setOnAction(e -> {
            // when clicked, briefly show selected background
            menuSelectionClear(b.getParent());
            b.setStyle("-fx-background-color: rgba(99,102,241,0.18); -fx-text-fill: white;");
        });
    }

    private void menuSelectionClear(javafx.scene.Parent parent) {
        if (parent instanceof VBox) {
            for (javafx.scene.Node n : ((VBox) parent).getChildren()) {
                if (n instanceof Button) {
                    n.setStyle("-fx-background-color: transparent; -fx-text-fill: #CFE8FF;");
                }
            }
        }
    }

    private VBox createCard(String title, String value, String color) {
        Label t = new Label(title);
        t.setStyle("-fx-text-fill: #334155; -fx-font-size: 11px;");
        Label v = new Label(value);
        v.setStyle("-fx-text-fill: #0F172A; -fx-font-size: 18px; -fx-font-weight: 700;");

        VBox box = new VBox(6, t, v);
        box.setPadding(new Insets(12));
        box.setPrefWidth(200);
        box.setStyle(String.format("-fx-background-color: white; -fx-background-radius: 8; -fx-effect: dropshadow(gaussian, rgba(15,23,42,0.06), 8, 0.0, 0, 2); -fx-border-color: %s22; -fx-border-radius: 8; -fx-border-width: 1;", color));

        return box;
    }
}