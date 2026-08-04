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
        
        // Header
        Label title = new Label("Data Barang");
        title.setStyle("""
                -fx-font-size: 26px;
                -fx-font-weight: bold;
                -fx-text-fill: #1E293B;
                """);

        Label description = new Label("Kelola data aki dan sparepart");
        description.setStyle("""
                -fx-font-size: 14px;
                -fx-text-fill: #64748B;
                """);

        VBox titleBox = new VBox(5, title, description);

        Button addButton = new Button("+ Tambah Barang");
        addButton.setStyle("""
                -fx-background-color: #2563EB;
                -fx-text-fill: white;
                -fx-font-weight: bold;
                -fx-padding: 10 16;
                -fx-cursor: hand;
                """);
        
        addButton.setOnAction(e -> showAddBarangDialog());

        HBox topBar = new HBox();
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(25));
        topBar.setSpacing(20);

        HBox spacer = new HBox();
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);

        topBar.getChildren().addAll(titleBox, spacer, addButton);

        // Table
        table = new TableView<>();
        loadBarangTable();

        VBox content = new VBox(table);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(12));
        content.setStyle("""
                -fx-background-color: white;
                -fx-border-color: #E2E8F0;
                -fx-border-radius: 8;
                -fx-background-radius: 8;
                """);

        BorderPane.setMargin(content, new Insets(0, 25, 25, 25));

        root.setTop(topBar);
        root.setCenter(content);
        root.setStyle("-fx-background-color: #F1F5F9;");

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
        colAction.setPrefWidth(120);
        colAction.setCellFactory(col -> new TableCell<Barang, Void>() {
            private final Button editBtn = new Button("Edit");
            private final Button delBtn = new Button("Hapus");

            {
                editBtn.setStyle("-fx-padding: 4 8; -fx-font-size: 11;");
                delBtn.setStyle("-fx-padding: 4 8; -fx-font-size: 11; -fx-text-fill: white; -fx-background-color: #DC2626;");
                
                editBtn.setOnAction(e -> showEditBarangDialog(getTableView().getItems().get(getIndex())));
                delBtn.setOnAction(e -> {
                    Barang b = getTableView().getItems().get(getIndex());
                    barangDAO.delete(b.getId());
                    refreshTable();
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox actions = new HBox(5, editBtn, delBtn);
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
        dialog.setTitle("Tambah Barang");
        dialog.setHeaderText("Masukkan Data Barang Baru");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField kodeField = new TextField();
        TextField namaField = new TextField();
        TextField deskripsiField = new TextField();
        TextField stokField = new TextField();
        TextField minField = new TextField();
        TextField hargaBeliField = new TextField();
        TextField hargaJualField = new TextField();
        TextField satuanField = new TextField();

        grid.add(new Label("Kode:"), 0, 0);
        grid.add(kodeField, 1, 0);
        grid.add(new Label("Nama:"), 0, 1);
        grid.add(namaField, 1, 1);
        grid.add(new Label("Deskripsi:"), 0, 2);
        grid.add(deskripsiField, 1, 2);
        grid.add(new Label("Stok:"), 0, 3);
        grid.add(stokField, 1, 3);
        grid.add(new Label("Stok Min:"), 0, 4);
        grid.add(minField, 1, 4);
        grid.add(new Label("Harga Beli:"), 0, 5);
        grid.add(hargaBeliField, 1, 5);
        grid.add(new Label("Harga Jual:"), 0, 6);
        grid.add(hargaJualField, 1, 6);
        grid.add(new Label("Satuan:"), 0, 7);
        grid.add(satuanField, 1, 7);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                try {
                    Barang b = new Barang(
                        0,
                        namaField.getText(),
                        kodeField.getText(),
                        deskripsiField.getText(),
                        Integer.parseInt(stokField.getText()),
                        Integer.parseInt(minField.getText()),
                        Double.parseDouble(hargaBeliField.getText()),
                        Double.parseDouble(hargaJualField.getText()),
                        satuanField.getText()
                    );
                    barangDAO.save(b);
                    return b;
                } catch (NumberFormatException ex) {
                    showError("Format data tidak valid!");
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
        dialog.setHeaderText("Edit Data Barang");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField kodeField = new TextField(barang.getKode());
        TextField namaField = new TextField(barang.getNama());
        TextField deskripsiField = new TextField(barang.getDeskripsi());
        TextField stokField = new TextField(String.valueOf(barang.getStok()));
        TextField minField = new TextField(String.valueOf(barang.getStokMinimum()));
        TextField hargaBeliField = new TextField(String.valueOf(barang.getHargaBeli()));
        TextField hargaJualField = new TextField(String.valueOf(barang.getHargaJual()));
        TextField satuanField = new TextField(barang.getSatuan());

        grid.add(new Label("Kode:"), 0, 0);
        grid.add(kodeField, 1, 0);
        grid.add(new Label("Nama:"), 0, 1);
        grid.add(namaField, 1, 1);
        grid.add(new Label("Deskripsi:"), 0, 2);
        grid.add(deskripsiField, 1, 2);
        grid.add(new Label("Stok:"), 0, 3);
        grid.add(stokField, 1, 3);
        grid.add(new Label("Stok Min:"), 0, 4);
        grid.add(minField, 1, 4);
        grid.add(new Label("Harga Beli:"), 0, 5);
        grid.add(hargaBeliField, 1, 5);
        grid.add(new Label("Harga Jual:"), 0, 6);
        grid.add(hargaJualField, 1, 6);
        grid.add(new Label("Satuan:"), 0, 7);
        grid.add(satuanField, 1, 7);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                try {
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
                    showError("Format data tidak valid!");
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