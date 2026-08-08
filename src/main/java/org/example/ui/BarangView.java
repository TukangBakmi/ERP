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
import org.example.data.BarangDAO;
import org.example.model.Barang;
import java.util.List;

public class BarangView {
    private BarangDAO barangDAO;
    private TableView<Barang> table;
    private ObservableList<Barang> barangList;

    public BorderPane create() {
        BorderPane root = new BorderPane();
        barangDAO = new BarangDAO();
        
        // Header with icon
        Label title = new Label("📦 Data Barang");
        title.setStyle("""
                -fx-font-size: 28px;
                -fx-font-weight: 900;
                -fx-text-fill: #0F172A;
                -fx-letter-spacing: 0.5;
                """);

        Label description = new Label("Kelola data aki, ban motor, dan sparepart lainnya");
        description.setStyle("""
                -fx-font-size: 13px;
                -fx-text-fill: #64748B;
                """);

        VBox titleBox = new VBox(5, title, description);

        Button addButton = new Button("➕ Tambah Barang Baru");
        addButton.setStyle("""
                -fx-background-color: #2563EB;
                -fx-text-fill: white;
                -fx-font-weight: bold;
                -fx-padding: 12 20;
                -fx-cursor: hand;
                -fx-background-radius: 8;
                -fx-font-size: 13px;
                """);
        
        addButton.setOnMouseEntered(e -> addButton.setStyle("""
                -fx-background-color: #1D4ED8;
                -fx-text-fill: white;
                -fx-font-weight: bold;
                -fx-padding: 12 20;
                -fx-cursor: hand;
                -fx-background-radius: 8;
                -fx-font-size: 13px;
                """));
        
        addButton.setOnMouseExited(e -> addButton.setStyle("""
                -fx-background-color: #2563EB;
                -fx-text-fill: white;
                -fx-font-weight: bold;
                -fx-padding: 12 20;
                -fx-cursor: hand;
                -fx-background-radius: 8;
                -fx-font-size: 13px;
                """));
        
        addButton.setOnAction(e -> showAddBarangDialog());

        HBox topBar = new HBox();
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(28, 32, 28, 32));
        topBar.setSpacing(20);

        HBox spacer = new HBox();
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);

        topBar.getChildren().addAll(titleBox, spacer, addButton);

        // Table
        table = new TableView<>();
        loadBarangTable();

        VBox content = new VBox(table);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(16));
        content.setStyle("""
                -fx-background-color: white;
                -fx-border-color: #E2E8F0;
                -fx-border-radius: 12;
                -fx-background-radius: 12;
                -fx-effect: dropshadow(gaussian, rgba(15,23,42,0.08), 12, 0.0, 0, 2);
                """);

        BorderPane.setMargin(content, new Insets(0, 32, 32, 32));

        root.setTop(topBar);
        root.setCenter(content);
        root.setStyle("-fx-background-color: #F8FAFC;");

        // Refresh data
        refreshTable();

        return root;
    }

    private void loadBarangTable() {
        TableColumn<Barang, Number> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(cell -> cell.getValue().idProperty());
        colId.setPrefWidth(50);

        TableColumn<Barang, String> colKode = new TableColumn<>("Kode");
        colKode.setCellValueFactory(cell -> cell.getValue().kodeProperty());
        colKode.setPrefWidth(100);

        TableColumn<Barang, String> colNama = new TableColumn<>("Nama Barang");
        colNama.setCellValueFactory(cell -> cell.getValue().namaProperty());
        colNama.setPrefWidth(200);

        TableColumn<Barang, Number> colStok = new TableColumn<>("Stok");
        colStok.setCellValueFactory(cell -> cell.getValue().stokProperty());
        colStok.setPrefWidth(70);

        TableColumn<Barang, Number> colMin = new TableColumn<>("Min");
        colMin.setCellValueFactory(cell -> cell.getValue().stokMinimumProperty());
        colMin.setPrefWidth(70);

        TableColumn<Barang, Number> colHargaBeli = new TableColumn<>("Harga Beli");
        colHargaBeli.setCellValueFactory(cell -> cell.getValue().hargaBeliProperty());
        colHargaBeli.setPrefWidth(120);

        TableColumn<Barang, Number> colHargaJual = new TableColumn<>("Harga Jual");
        colHargaJual.setCellValueFactory(cell -> cell.getValue().hargaJualProperty());
        colHargaJual.setPrefWidth(120);

        TableColumn<Barang, Void> colAction = new TableColumn<>("Aksi");
        colAction.setPrefWidth(130);
        colAction.setCellFactory(col -> new TableCell<Barang, Void>() {
            private final Button editBtn = new Button("✏️ Edit");
            private final Button delBtn = new Button("🗑️ Hapus");

            {
                editBtn.setStyle("""
                    -fx-padding: 6 12;
                    -fx-font-size: 12;
                    -fx-background-color: #0EA5A3;
                    -fx-text-fill: white;
                    -fx-background-radius: 6;
                    -fx-font-weight: 600;
                    """);
                
                delBtn.setStyle("""
                    -fx-padding: 6 12;
                    -fx-font-size: 12;
                    -fx-text-fill: white;
                    -fx-background-color: #DC2626;
                    -fx-background-radius: 6;
                    -fx-font-weight: 600;
                    """);
                
                editBtn.setOnMouseEntered(e -> editBtn.setStyle("""
                    -fx-padding: 6 12;
                    -fx-font-size: 12;
                    -fx-background-color: #0D9488;
                    -fx-text-fill: white;
                    -fx-background-radius: 6;
                    -fx-font-weight: 600;
                    """));
                
                editBtn.setOnMouseExited(e -> editBtn.setStyle("""
                    -fx-padding: 6 12;
                    -fx-font-size: 12;
                    -fx-background-color: #0EA5A3;
                    -fx-text-fill: white;
                    -fx-background-radius: 6;
                    -fx-font-weight: 600;
                    """));
                
                delBtn.setOnMouseEntered(e -> delBtn.setStyle("""
                    -fx-padding: 6 12;
                    -fx-font-size: 12;
                    -fx-text-fill: white;
                    -fx-background-color: #B91C1C;
                    -fx-background-radius: 6;
                    -fx-font-weight: 600;
                    """));
                
                delBtn.setOnMouseExited(e -> delBtn.setStyle("""
                    -fx-padding: 6 12;
                    -fx-font-size: 12;
                    -fx-text-fill: white;
                    -fx-background-color: #DC2626;
                    -fx-background-radius: 6;
                    -fx-font-weight: 600;
                    """));
                
                editBtn.setOnAction(e -> showEditBarangDialog(getTableView().getItems().get(getIndex())));
                delBtn.setOnAction(e -> {
                    Barang b = getTableView().getItems().get(getIndex());
                    Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                    confirm.setTitle("Konfirmasi Hapus");
                    confirm.setHeaderText(null);
                    confirm.setContentText("Yakin ingin menghapus barang \"" + b.getNama() + "\"?");
                    if (confirm.showAndWait().get() == ButtonType.OK) {
                        barangDAO.delete(b.getId());
                        refreshTable();
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox actions = new HBox(8, editBtn, delBtn);
                    actions.setAlignment(Pos.CENTER);
                    setGraphic(actions);
                }
            }
        });

        table.getColumns().addAll(colId, colKode, colNama, colStok, colMin, colHargaBeli, colHargaJual, colAction);
    }

    private void refreshTable() {
        List<Barang> barangListData = barangDAO.findAll();
        barangList = FXCollections.observableArrayList(barangListData);
        table.setItems(barangList);
    }

    private void showAddBarangDialog() {
        Dialog<Barang> dialog = new Dialog<>();
        dialog.setTitle("Tambah Barang Baru");
        dialog.setHeaderText("➕ Masukkan Data Barang");

        GridPane grid = new GridPane();
        grid.setHgap(12);
        grid.setVgap(14);
        grid.setPadding(new Insets(20));
        grid.setStyle("-fx-font-size: 13px;");

        TextField kodeField = new TextField();
        kodeField.setPromptText("Cth: BAN-001");
        TextField namaField = new TextField();
        namaField.setPromptText("Cth: Ban Motor 80/90-14");
        TextField deskripsiField = new TextField();
        deskripsiField.setPromptText("Deskripsi optional");
        TextField stokField = new TextField();
        stokField.setPromptText("0");
        TextField minField = new TextField();
        minField.setPromptText("10");
        TextField hargaBeliField = new TextField();
        hargaBeliField.setPromptText("50000");
        TextField hargaJualField = new TextField();
        hargaJualField.setPromptText("75000");
        TextField satuanField = new TextField();
        satuanField.setPromptText("Pcs");

        // Style fields
        for (TextField field : new TextField[]{kodeField, namaField, deskripsiField, stokField, minField, hargaBeliField, hargaJualField, satuanField}) {
            field.setStyle("-fx-padding: 10; -fx-font-size: 13px; -fx-border-radius: 6; -fx-background-radius: 6;");
        }

        Label labelKode = createFormLabel("Kode Barang *");
        Label labelNama = createFormLabel("Nama Barang *");
        Label labelDesk = createFormLabel("Deskripsi");
        Label labelStok = createFormLabel("Stok *");
        Label labelMin = createFormLabel("Stok Minimum *");
        Label labelBeli = createFormLabel("Harga Beli *");
        Label labelJual = createFormLabel("Harga Jual *");
        Label labelSatuan = createFormLabel("Satuan *");

        grid.add(labelKode, 0, 0);
        grid.add(kodeField, 1, 0);
        grid.add(labelNama, 0, 1);
        grid.add(namaField, 1, 1);
        grid.add(labelDesk, 0, 2);
        grid.add(deskripsiField, 1, 2);
        grid.add(labelStok, 0, 3);
        grid.add(stokField, 1, 3);
        grid.add(labelMin, 0, 4);
        grid.add(minField, 1, 4);
        grid.add(labelBeli, 0, 5);
        grid.add(hargaBeliField, 1, 5);
        grid.add(labelJual, 0, 6);
        grid.add(hargaJualField, 1, 6);
        grid.add(labelSatuan, 0, 7);
        grid.add(satuanField, 1, 7);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        
        // Style dialog buttons
        styleDialogButtons(dialog);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                try {
                    if (namaField.getText().isEmpty() || kodeField.getText().isEmpty()) {
                        showError("Kode dan Nama barang harus diisi!");
                        return null;
                    }
                    Barang b = new Barang(
                        0,
                        namaField.getText(),
                        kodeField.getText(),
                        deskripsiField.getText(),
                        Integer.parseInt(stokField.getText().isEmpty() ? "0" : stokField.getText()),
                        Integer.parseInt(minField.getText().isEmpty() ? "0" : minField.getText()),
                        Double.parseDouble(hargaBeliField.getText().isEmpty() ? "0" : hargaBeliField.getText()),
                        Double.parseDouble(hargaJualField.getText().isEmpty() ? "0" : hargaJualField.getText()),
                        satuanField.getText().isEmpty() ? "Pcs" : satuanField.getText()
                    );
                    barangDAO.save(b);
                    return b;
                } catch (NumberFormatException ex) {
                    showError("Format angka tidak valid!");
                }
            }
            return null;
        });

        dialog.showAndWait();
        refreshTable();
    }

    private void showEditBarangDialog(Barang barang) {
        Dialog<Barang> dialog = new Dialog<>();
        dialog.setTitle("Edit Barang");
        dialog.setHeaderText("✏️ Edit Data Barang");

        GridPane grid = new GridPane();
        grid.setHgap(12);
        grid.setVgap(14);
        grid.setPadding(new Insets(20));
        grid.setStyle("-fx-font-size: 13px;");

        TextField kodeField = new TextField(barang.getKode());
        TextField namaField = new TextField(barang.getNama());
        TextField deskripsiField = new TextField(barang.getDeskripsi());
        TextField stokField = new TextField(String.valueOf(barang.getStok()));
        TextField minField = new TextField(String.valueOf(barang.getStokMinimum()));
        TextField hargaBeliField = new TextField(String.valueOf(barang.getHargaBeli()));
        TextField hargaJualField = new TextField(String.valueOf(barang.getHargaJual()));
        TextField satuanField = new TextField(barang.getSatuan());

        // Style fields
        for (TextField field : new TextField[]{kodeField, namaField, deskripsiField, stokField, minField, hargaBeliField, hargaJualField, satuanField}) {
            field.setStyle("-fx-padding: 10; -fx-font-size: 13px; -fx-border-radius: 6; -fx-background-radius: 6;");
        }

        Label labelKode = createFormLabel("Kode Barang *");
        Label labelNama = createFormLabel("Nama Barang *");
        Label labelDesk = createFormLabel("Deskripsi");
        Label labelStok = createFormLabel("Stok *");
        Label labelMin = createFormLabel("Stok Minimum *");
        Label labelBeli = createFormLabel("Harga Beli *");
        Label labelJual = createFormLabel("Harga Jual *");
        Label labelSatuan = createFormLabel("Satuan *");

        grid.add(labelKode, 0, 0);
        grid.add(kodeField, 1, 0);
        grid.add(labelNama, 0, 1);
        grid.add(namaField, 1, 1);
        grid.add(labelDesk, 0, 2);
        grid.add(deskripsiField, 1, 2);
        grid.add(labelStok, 0, 3);
        grid.add(stokField, 1, 3);
        grid.add(labelMin, 0, 4);
        grid.add(minField, 1, 4);
        grid.add(labelBeli, 0, 5);
        grid.add(hargaBeliField, 1, 5);
        grid.add(labelJual, 0, 6);
        grid.add(hargaJualField, 1, 6);
        grid.add(labelSatuan, 0, 7);
        grid.add(satuanField, 1, 7);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        
        // Style dialog buttons
        styleDialogButtons(dialog);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                try {
                    if (namaField.getText().isEmpty() || kodeField.getText().isEmpty()) {
                        showError("Kode dan Nama barang harus diisi!");
                        return null;
                    }
                    barang.setKode(kodeField.getText());
                    barang.setNama(namaField.getText());
                    barang.setDeskripsi(deskripsiField.getText());
                    barang.setStok(Integer.parseInt(stokField.getText()));
                    barang.setStokMinimum(Integer.parseInt(minField.getText()));
                    barang.setHargaBeli(Double.parseDouble(hargaBeliField.getText()));
                    barang.setHargaJual(Double.parseDouble(hargaJualField.getText()));
                    barang.setSatuan(satuanField.getText());
                    barangDAO.update(barang);
                    return barang;
                } catch (NumberFormatException ex) {
                    showError("Format angka tidak valid!");
                }
            }
            return null;
        });

        dialog.showAndWait();
        refreshTable();
    }

    private Label createFormLabel(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-text-fill: #334155; -fx-font-weight: 600; -fx-font-size: 13px;");
        return label;
    }

    private void styleDialogButtons(Dialog<?> dialog) {
        dialog.getDialogPane().lookupButton(ButtonType.OK).setStyle("""
            -fx-padding: 10 24;
            -fx-background-color: #2563EB;
            -fx-text-fill: white;
            -fx-font-weight: 600;
            -fx-background-radius: 6;
            """);
        dialog.getDialogPane().lookupButton(ButtonType.CANCEL).setStyle("""
            -fx-padding: 10 24;
            -fx-background-color: #E2E8F0;
            -fx-text-fill: #334155;
            -fx-font-weight: 600;
            -fx-background-radius: 6;
            """);
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("⚠️ Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.getDialogPane().setStyle("-fx-font-size: 13px;");
        alert.showAndWait();
    }
}