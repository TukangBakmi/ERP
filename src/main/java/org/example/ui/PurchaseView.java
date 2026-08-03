package org.example.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class PurchaseView {

    public BorderPane create() {
        BorderPane root = new BorderPane();

        Label title = new Label("Pembelian");
        title.setStyle("""
                -fx-font-size: 24px;
                -fx-font-weight: bold;
                -fx-text-fill: #1E293B;
                """);

        Label description = new Label("Catat pembelian sparepart dan retur supplier.");
        description.setStyle("""
                -fx-font-size: 13px;
                -fx-text-fill: #64748B;
                """);

        Button newPurchase = new Button("+ Transaksi Pembelian");
        newPurchase.setStyle("""
                -fx-background-color: #2563EB;
                -fx-text-fill: white;
                -fx-font-weight: bold;
                -fx-padding: 8 12;
                -fx-cursor: hand;
                """);

        HBox topBar = new HBox(12, new VBox(4, title, description));
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(20));

        HBox spacer = new HBox();
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);
        topBar.getChildren().addAll(spacer, newPurchase);

        Label placeholder = new Label("Form pembelian dan daftar transaksi muncul di sini.");
        placeholder.setStyle("-fx-text-fill: #94A3B8; -fx-font-size: 14px;");

        VBox content = new VBox(placeholder);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(18));
        content.setStyle("-fx-background-color: white; -fx-border-color: #E6EEF8; -fx-background-radius: 8; -fx-border-radius: 8;");

        BorderPane.setMargin(content, new Insets(0, 20, 20, 20));

        root.setTop(topBar);
        root.setCenter(content);
        root.setStyle("-fx-background-color: #F8FAFC;");

        return root;
    }
}
