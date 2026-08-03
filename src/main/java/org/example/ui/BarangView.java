package org.example.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class BarangView {

    public BorderPane create() {

        BorderPane root = new BorderPane();

        // Header halaman
        Label title = new Label("Data Barang");

        title.setStyle("""
                -fx-font-size: 26px;
                -fx-font-weight: bold;
                -fx-text-fill: #1E293B;
                """);

        Label description = new Label(
                "Kelola data aki dan sparepart"
        );

        description.setStyle("""
                -fx-font-size: 14px;
                -fx-text-fill: #64748B;
                """);

        VBox titleBox = new VBox(5, title, description);

        // Tombol tambah
        Button addButton = new Button("+ Tambah Barang");

        addButton.setStyle("""
                -fx-background-color: #2563EB;
                -fx-text-fill: white;
                -fx-font-weight: bold;
                -fx-padding: 10 16;
                -fx-cursor: hand;
                """);

        HBox topBar = new HBox();

        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(25));
        topBar.setSpacing(20);

        HBox spacer = new HBox();

        HBox.setHgrow(
                spacer,
                javafx.scene.layout.Priority.ALWAYS
        );

        topBar.getChildren().addAll(
                titleBox,
                spacer,
                addButton
        );

        // Placeholder tabel
        Label tablePlaceholder = new Label(
                "Data barang akan ditampilkan di sini"
        );

        tablePlaceholder.setStyle("""
                -fx-font-size: 15px;
                -fx-text-fill: #94A3B8;
                """);

        VBox content = new VBox(tablePlaceholder);

        content.setAlignment(Pos.CENTER);

        content.setStyle("""
                -fx-background-color: white;
                -fx-border-color: #E2E8F0;
                -fx-border-radius: 8;
                -fx-background-radius: 8;
                """);

        content.setMargin(
                tablePlaceholder,
                new Insets(20)
        );

        BorderPane.setMargin(
                content,
                new Insets(0, 25, 25, 25)
        );

        root.setTop(topBar);
        root.setCenter(content);

        root.setStyle("""
                -fx-background-color: #F1F5F9;
                """);

        return root;
    }
}