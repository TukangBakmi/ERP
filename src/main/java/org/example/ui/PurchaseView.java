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
import org.example.data.*;
import org.example.model.Barang;
import org.example.model.Pembelian;
import org.example.model.Supplier;
import java.time.LocalDate;
import java.util.List;

public class PurchaseView {
    private PembelianDAO pembelianDAO;
    private PembelianDetailDAO detailDAO;
    private SupplierDAO supplierDAO;
    private BarangDAO barangDAO;
    private TableView<Pembelian> table;
    private ObservableList<Pembelian> pembelianList;

    public BorderPane create() {
        BorderPane root = new BorderPane();
        pembelianDAO = new PembelianDAO();
        detailDAO = new PembelianDetailDAO();
        supplierDAO = new SupplierDAO();
        barangDAO = new BarangDAO();

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
        
        newPurchase.setOnAction(e -> showAddPembelianDialog());

        HBox topBar = new HBox(12, new VBox(4, title, description));
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(20));

        HBox spacer = new HBox();
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);
        topBar.getChildren().addAll(spacer, newPurchase);

        table = new TableView<>();
        loadPembelianTable();

        VBox content = new VBox(table);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(12));

        BorderPane.setMargin(content, new Insets(0, 20, 20, 20));

        root.setTop(topBar);
        root.setCenter(content);
        root.setStyle("-fx-background-color: #F8FAFC;");

        refreshTable();

        return root;
    }

    private void loadPembelianTable() {
        TableColumn<Pembelian, Number> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(cell -> cell.getValue().idProperty());
        colId.setPrefWidth(50);

        TableColumn<Pembelian, Object> colSupplier = new TableColumn<>("Supplier");
        colSupplier.setCellValueFactory(cell -> new javafx.beans.property.SimpleObjectProperty<>(
            getSupplierName(cell.getValue().getSupplierId())));
        colSupplier.setPrefWidth(180);

        TableColumn<Pembelian, String> colNomorPo = new TableColumn<>("Nomor PO");
        colNomorPo.setCellValueFactory(cell -> cell.getValue().nomorPoProperty());
        colNomorPo.setPrefWidth(120);

        TableColumn<Pembelian, Object> colTanggal = new TableColumn<>("Tanggal");
        colTanggal.setCellValueFactory(cell -> new javafx.beans.property.SimpleObjectProperty<>(
            cell.getValue().getTanggal()));
        colTanggal.setPrefWidth(100);

        TableColumn<Pembelian, Number> colTotal = new TableColumn<>("Total (Rp)");
        colTotal.setCellValueFactory(cell -> cell.getValue().jumlahTotalProperty());
        colTotal.setPrefWidth(130);

        TableColumn<Pembelian, String> colStatus = new TableColumn<>("Status");
        colStatus.setCellValueFactory(cell -> cell.getValue().statusProperty());
        colStatus.setPrefWidth(100);

        TableColumn<Pembelian, Void> colAction = new TableColumn<>("Aksi");
        colAction.setPrefWidth(150);
        colAction.setCellFactory(col -> new TableCell<Pembelian, Void>() {
            private final Button viewBtn = new Button("Lihat Detail");
            private final Button delBtn = new Button("Hapus");

            {
                viewBtn.setStyle("-fx-padding: 4 8; -fx-font-size: 11;");
                delBtn.setStyle("-fx-padding: 4 8; -fx-font-size: 11; -fx-text-fill: white; -fx-background-color: #DC2626;");
                
                viewBtn.setOnAction(e -> showDetailDialog(getTableView().getItems().get(getIndex())));
                delBtn.setOnAction(e -> {
                    Pembelian p = getTableView().getItems().get(getIndex());
                    pembelianDAO.delete(p.getId());
                    refreshTable();
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox actions = new HBox(5, viewBtn, delBtn);
                    setGraphic(actions);
                }
            }
        });

        table.getColumns().addAll(colId, colSupplier, colNomorPo, colTanggal, colTotal, colStatus, colAction);
    }

    private String getSupplierName(int supplierId) {
        Supplier s = supplierDAO.findById(supplierId);
        return s != null ? s.getName() : "Unknown";
    }

    private void refreshTable() {
        List<Pembelian> pembelianListData = pembelianDAO.findAll();
        pembelianList = FXCollections.observableArrayList(pembelianListData);
        table.setItems(pembelianList);
    }

    private void showAddPembelianDialog() {
        Dialog<Pembelian> dialog = new Dialog<>();
        dialog.setTitle("Tambah Pembelian");
        dialog.setHeaderText("Buat Transaksi Pembelian Baru");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        ComboBox<Supplier> supplierBox = new ComboBox<>();
        supplierBox.setItems(FXCollections.observableArrayList(supplierDAO.findAll()));
        supplierBox.setPrefWidth(250);

        TextField nomorPoField = new TextField();
        DatePicker tanggalField = new DatePicker(LocalDate.now());
        TextField jumlahField = new TextField("0");

        ComboBox<String> statusBox = new ComboBox<>();
        statusBox.getItems().addAll("PENDING", "RECEIVED", "CANCELLED");
        statusBox.setValue("PENDING");

        grid.add(new Label("Supplier:"), 0, 0);
        grid.add(supplierBox, 1, 0);
        grid.add(new Label("Nomor PO:"), 0, 1);
        grid.add(nomorPoField, 1, 1);
        grid.add(new Label("Tanggal:"), 0, 2);
        grid.add(tanggalField, 1, 2);
        grid.add(new Label("Jumlah Total:"), 0, 3);
        grid.add(jumlahField, 1, 3);
        grid.add(new Label("Status:"), 0, 4);
        grid.add(statusBox, 1, 4);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                try {
                    if (supplierBox.getValue() == null) {
                        showError("Pilih supplier!");
                        return null;
                    }
                    if (nomorPoField.getText().isEmpty()) {
                        showError("Nomor PO harus diisi!");
                        return null;
                    }
                    Pembelian p = new Pembelian(0, supplierBox.getValue().getId(), nomorPoField.getText(),
                        tanggalField.getValue(), Double.parseDouble(jumlahField.getText()), statusBox.getValue());
                    pembelianDAO.save(p);
                    return p;
                } catch (NumberFormatException ex) {
                    showError("Format angka tidak valid!");
                }
            }
            return null;
        });

        dialog.showAndWait();
        refreshTable();
    }

    private void showDetailDialog(Pembelian pembelian) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Detail Pembelian: " + pembelian.getNomorPo());
        dialog.setHeaderText("Nomor PO: " + pembelian.getNomorPo());

        VBox content = new VBox(10);
        content.setPadding(new Insets(20));

        Label infoLabel = new Label(String.format("Supplier: %s | Tanggal: %s | Total: Rp %,.0f",
            getSupplierName(pembelian.getSupplierId()), pembelian.getTanggal(), pembelian.getJumlahTotal()));
        infoLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #64748B;");

        TableView<PembelianDetailDAO.DetailRow> detailTable = new TableView<>();
        List<PembelianDetailDAO.DetailRow> details = detailDAO.findByPembelianId(pembelian.getId());
        detailTable.setItems(FXCollections.observableArrayList(details));

        TableColumn<PembelianDetailDAO.DetailRow, String> colBarang = new TableColumn<>("Barang");
        colBarang.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().namaBarang));
        colBarang.setPrefWidth(200);

        TableColumn<PembelianDetailDAO.DetailRow, Number> colJumlah = new TableColumn<>("Jumlah");
        colJumlah.setCellValueFactory(cell -> new javafx.beans.property.SimpleObjectProperty<>(cell.getValue().jumlah));
        colJumlah.setPrefWidth(80);

        TableColumn<PembelianDetailDAO.DetailRow, Number> colHarga = new TableColumn<>("Harga Satuan");
        colHarga.setCellValueFactory(cell -> new javafx.beans.property.SimpleObjectProperty<>(cell.getValue().hargaSatuan));
        colHarga.setPrefWidth(130);

        TableColumn<PembelianDetailDAO.DetailRow, Number> colSubtotal = new TableColumn<>("Subtotal (Rp)");
        colSubtotal.setCellValueFactory(cell -> new javafx.beans.property.SimpleObjectProperty<>(cell.getValue().subtotal));
        colSubtotal.setPrefWidth(130);

        detailTable.getColumns().addAll(colBarang, colJumlah, colHarga, colSubtotal);

        content.getChildren().addAll(infoLabel, new Separator(), detailTable);

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        dialog.getDialogPane().setContent(scrollPane);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

        dialog.showAndWait();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
