package org.example.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import org.example.data.KasDAO;
import org.example.model.Kas;
import java.time.LocalDate;
import java.util.List;

public class CashView {
    private KasDAO kasDAO;
    private TableView<Kas> table;
    private ObservableList<Kas> kasList;
    private Label balanceLabel;

    public BorderPane create() {
        BorderPane root = new BorderPane();
        kasDAO = new KasDAO();

        Label title = new Label("Kas & Keuangan");
        title.setStyle("""
                -fx-font-size: 24px;
                -fx-font-weight: bold;
                -fx-text-fill: #1E293B;
                """);

        Label description = new Label("Pantau saldo kas, pemasukan, dan pengeluaran.");
        description.setStyle("""
                -fx-font-size: 13px;
                -fx-text-fill: #64748B;
                """);

        Button addEntry = new Button("+ Catat Transaksi");
        addEntry.setStyle("""
                -fx-background-color: #DC2626;
                -fx-text-fill: white;
                -fx-font-weight: bold;
                -fx-padding: 8 12;
                -fx-cursor: hand;
                """);
        
        addEntry.setOnAction(e -> showAddTransactionDialog());

        HBox topBar = new HBox(12, new VBox(4, title, description));
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(20));

        HBox spacer = new HBox();
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);
        topBar.getChildren().addAll(spacer, addEntry);

        // Balance Card
        balanceLabel = new Label("Rp 0");
        balanceLabel.setStyle("""
                -fx-font-size: 32px;
                -fx-font-weight: bold;
                -fx-text-fill: #10B981;
                """);

        Label balanceTitle = new Label("Saldo Kas");
        balanceTitle.setStyle("-fx-font-size: 14px; -fx-text-fill: #64748B;");

        VBox balanceCard = new VBox(10, balanceTitle, balanceLabel);
        balanceCard.setPadding(new Insets(20));
        balanceCard.setStyle("""
                -fx-background-color: white;
                -fx-border-color: #E2E8F0;
                -fx-border-radius: 8;
                -fx-background-radius: 8;
                """);
        balanceCard.setPrefWidth(250);

        // Table
        table = new TableView<>();
        loadKasTable();

        VBox tableContent = new VBox(table);
        tableContent.setAlignment(Pos.CENTER);
        tableContent.setPadding(new Insets(12));

        VBox content = new VBox(20, balanceCard, tableContent);
        content.setPadding(new Insets(12));

        BorderPane.setMargin(content, new Insets(0, 20, 20, 20));

        root.setTop(topBar);
        root.setCenter(new ScrollPane(content));
        root.setStyle("-fx-background-color: #F8FAFC;");

        refreshTable();

        return root;
    }

    private void loadKasTable() {
        TableColumn<Kas, Number> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(cell -> cell.getValue().idProperty());
        colId.setPrefWidth(50);

        TableColumn<Kas, String> colDeskripsi = new TableColumn<>("Deskripsi");
        colDeskripsi.setCellValueFactory(cell -> cell.getValue().deskripsiProperty());
        colDeskripsi.setPrefWidth(200);

        TableColumn<Kas, Object> colTanggal = new TableColumn<>("Tanggal");
        colTanggal.setCellValueFactory(cell -> new javafx.beans.property.SimpleObjectProperty<>(cell.getValue().getTanggal()));
        colTanggal.setPrefWidth(100);

        TableColumn<Kas, Number> colMasuk = new TableColumn<>("Masuk (Rp)");
        colMasuk.setCellValueFactory(cell -> cell.getValue().jumlahMasukProperty());
        colMasuk.setPrefWidth(130);

        TableColumn<Kas, Number> colKeluar = new TableColumn<>("Keluar (Rp)");
        colKeluar.setCellValueFactory(cell -> cell.getValue().jumlahKeluarProperty());
        colKeluar.setPrefWidth(130);

        TableColumn<Kas, String> colKategori = new TableColumn<>("Kategori");
        colKategori.setCellValueFactory(cell -> cell.getValue().kategoriProperty());
        colKategori.setPrefWidth(100);

        TableColumn<Kas, Void> colAction = new TableColumn<>("Aksi");
        colAction.setPrefWidth(100);
        colAction.setCellFactory(col -> new TableCell<Kas, Void>() {
            private final Button delBtn = new Button("Hapus");

            {
                delBtn.setStyle("-fx-padding: 4 8; -fx-font-size: 11; -fx-text-fill: white; -fx-background-color: #DC2626;");
                
                delBtn.setOnAction(e -> {
                    Kas k = getTableView().getItems().get(getIndex());
                    kasDAO.delete(k.getId());
                    refreshTable();
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(delBtn);
                }
            }
        });

        table.getColumns().addAll(colId, colDeskripsi, colTanggal, colMasuk, colKeluar, colKategori, colAction);
    }

    private void refreshTable() {
        List<Kas> kasListData = kasDAO.findAll();
        kasList = FXCollections.observableArrayList(kasListData);
        table.setItems(kasList);

        // Update balance
        double saldo = kasDAO.getSaldoTotal();
        balanceLabel.setText(String.format("Rp %,.0f", saldo));
        balanceLabel.setTextFill(saldo >= 0 ? Color.web("#10B981") : Color.web("#DC2626"));
    }

    private void showAddTransactionDialog() {
        Dialog<Kas> dialog = new Dialog<>();
        dialog.setTitle("Catat Transaksi");
        dialog.setHeaderText("Masukkan Data Transaksi Kas");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField deskripsiField = new TextField();
        TextField masukField = new TextField("0");
        TextField keluarField = new TextField("0");
        ComboBox<String> kategoriBox = new ComboBox<>();
        kategoriBox.getItems().addAll("SALES", "PURCHASE", "OTHER");
        kategoriBox.setValue("OTHER");
        DatePicker tanggalField = new DatePicker(LocalDate.now());

        grid.add(new Label("Deskripsi:"), 0, 0);
        grid.add(deskripsiField, 1, 0);
        grid.add(new Label("Tanggal:"), 0, 1);
        grid.add(tanggalField, 1, 1);
        grid.add(new Label("Jumlah Masuk (Rp):"), 0, 2);
        grid.add(masukField, 1, 2);
        grid.add(new Label("Jumlah Keluar (Rp):"), 0, 3);
        grid.add(keluarField, 1, 3);
        grid.add(new Label("Kategori:"), 0, 4);
        grid.add(kategoriBox, 1, 4);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                try {
                    if (deskripsiField.getText().isEmpty()) {
                        showError("Deskripsi tidak boleh kosong!");
                        return null;
                    }
                    double masuk = Double.parseDouble(masukField.getText());
                    double keluar = Double.parseDouble(keluarField.getText());
                    
                    if (masuk <= 0 && keluar <= 0) {
                        showError("Minimal ada satu jumlah yang harus diisi!");
                        return null;
                    }
                    
                    Kas k = new Kas(0, deskripsiField.getText(), tanggalField.getValue(), masuk, keluar, kategoriBox.getValue());
                    kasDAO.save(k);
                    return k;
                } catch (NumberFormatException ex) {
                    showError("Format angka tidak valid!");
                }
            }
            return null;
        });

        dialog.showAndWait();
        refreshTable();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
