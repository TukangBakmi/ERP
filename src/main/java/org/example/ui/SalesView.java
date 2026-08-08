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
import org.example.model.Penjualan;
import org.example.model.Customer;
import java.time.LocalDate;
import java.util.List;

public class SalesView {
    private PenjualanDAO penjualanDAO;
    private PenjualanDetailDAO detailDAO;
    private CustomerDAO customerDAO;
    private TableView<Penjualan> table;
    private ObservableList<Penjualan> penjualanList;

    public BorderPane create() {
        BorderPane root = new BorderPane();
        penjualanDAO = new PenjualanDAO();
        detailDAO = new PenjualanDetailDAO();
        customerDAO = new CustomerDAO();

        Label title = new Label("Penjualan");
        title.setStyle("""
                -fx-font-size: 24px;
                -fx-font-weight: bold;
                -fx-text-fill: #1E293B;
                """);

        Label description = new Label("Kelola penjualan sparepart dan invoice untuk customer.");
        description.setStyle("""
                -fx-font-size: 13px;
                -fx-text-fill: #64748B;
                """);

        Button newSale = new Button("+ Transaksi Penjualan");
        newSale.setStyle("""
                -fx-background-color: #F97316;
                -fx-text-fill: white;
                -fx-font-weight: bold;
                -fx-padding: 8 12;
                -fx-cursor: hand;
                """);
        
        newSale.setOnAction(e -> showAddPenjualanDialog());

        HBox topBar = new HBox(12, new VBox(4, title, description));
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(20));

        HBox spacer = new HBox();
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);
        topBar.getChildren().addAll(spacer, newSale);

        table = new TableView<>();
        loadPenjualanTable();

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

    private void loadPenjualanTable() {
        TableColumn<Penjualan, Number> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(cell -> cell.getValue().idProperty());
        colId.setPrefWidth(50);

        TableColumn<Penjualan, Object> colCustomer = new TableColumn<>("Customer");
        colCustomer.setCellValueFactory(cell -> new javafx.beans.property.SimpleObjectProperty<>(
            getCustomerName(cell.getValue().getCustomerId())));
        colCustomer.setPrefWidth(180);

        TableColumn<Penjualan, String> colNomorInvoice = new TableColumn<>("Nomor Invoice");
        colNomorInvoice.setCellValueFactory(cell -> cell.getValue().nomorInvoiceProperty());
        colNomorInvoice.setPrefWidth(130);

        TableColumn<Penjualan, Object> colTanggal = new TableColumn<>("Tanggal");
        colTanggal.setCellValueFactory(cell -> new javafx.beans.property.SimpleObjectProperty<>(
            cell.getValue().getTanggal()));
        colTanggal.setPrefWidth(100);

        TableColumn<Penjualan, Number> colTotal = new TableColumn<>("Total (Rp)");
        colTotal.setCellValueFactory(cell -> cell.getValue().jumlahTotalProperty());
        colTotal.setPrefWidth(130);

        TableColumn<Penjualan, String> colStatus = new TableColumn<>("Status");
        colStatus.setCellValueFactory(cell -> cell.getValue().statusProperty());
        colStatus.setPrefWidth(100);

        TableColumn<Penjualan, Void> colAction = new TableColumn<>("Aksi");
        colAction.setPrefWidth(150);
        colAction.setCellFactory(col -> new TableCell<Penjualan, Void>() {
            private final Button viewBtn = new Button("Lihat Detail");
            private final Button delBtn = new Button("Hapus");

            {
                viewBtn.setStyle("-fx-padding: 4 8; -fx-font-size: 11;");
                delBtn.setStyle("-fx-padding: 4 8; -fx-font-size: 11; -fx-text-fill: white; -fx-background-color: #DC2626;");
                
                viewBtn.setOnAction(e -> showDetailDialog(getTableView().getItems().get(getIndex())));
                delBtn.setOnAction(e -> {
                    Penjualan p = getTableView().getItems().get(getIndex());
                    penjualanDAO.delete(p.getId());
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

        table.getColumns().addAll(colId, colCustomer, colNomorInvoice, colTanggal, colTotal, colStatus, colAction);
    }

    private String getCustomerName(int customerId) {
        Customer c = customerDAO.findById(customerId);
        return c != null ? c.getName() : "Unknown";
    }

    private void refreshTable() {
        List<Penjualan> penjualanListData = penjualanDAO.findAll();
        penjualanList = FXCollections.observableArrayList(penjualanListData);
        table.setItems(penjualanList);
    }

    private void showAddPenjualanDialog() {
        Dialog<Penjualan> dialog = new Dialog<>();
        dialog.setTitle("Tambah Penjualan");
        dialog.setHeaderText("Buat Transaksi Penjualan Baru");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        ComboBox<Customer> customerBox = new ComboBox<>();
        customerBox.setItems(FXCollections.observableArrayList(customerDAO.findAll()));
        customerBox.setPrefWidth(250);

        TextField nomorInvoiceField = new TextField();
        DatePicker tanggalField = new DatePicker(LocalDate.now());
        TextField jumlahField = new TextField("0");

        ComboBox<String> statusBox = new ComboBox<>();
        statusBox.getItems().addAll("PENDING", "COMPLETED", "CANCELLED");
        statusBox.setValue("PENDING");

        grid.add(new Label("Customer:"), 0, 0);
        grid.add(customerBox, 1, 0);
        grid.add(new Label("Nomor Invoice:"), 0, 1);
        grid.add(nomorInvoiceField, 1, 1);
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
                    if (customerBox.getValue() == null) {
                        showError("Pilih customer!");
                        return null;
                    }
                    if (nomorInvoiceField.getText().isEmpty()) {
                        showError("Nomor Invoice harus diisi!");
                        return null;
                    }
                    Penjualan p = new Penjualan(0, customerBox.getValue().getId(), nomorInvoiceField.getText(),
                        tanggalField.getValue(), Double.parseDouble(jumlahField.getText()), statusBox.getValue());
                    penjualanDAO.save(p);
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

    private void showDetailDialog(Penjualan penjualan) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Detail Penjualan: " + penjualan.getNomorInvoice());
        dialog.setHeaderText("Nomor Invoice: " + penjualan.getNomorInvoice());

        VBox content = new VBox(10);
        content.setPadding(new Insets(20));

        Label infoLabel = new Label(String.format("Customer: %s | Tanggal: %s | Total: Rp %,.0f",
            getCustomerName(penjualan.getCustomerId()), penjualan.getTanggal(), penjualan.getJumlahTotal()));
        infoLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #64748B;");

        TableView<PenjualanDetailDAO.DetailRow> detailTable = new TableView<>();
        List<PenjualanDetailDAO.DetailRow> details = detailDAO.findByPenjualanId(penjualan.getId());
        detailTable.setItems(FXCollections.observableArrayList(details));

        TableColumn<PenjualanDetailDAO.DetailRow, String> colBarang = new TableColumn<>("Barang");
        colBarang.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().namaBarang));
        colBarang.setPrefWidth(200);

        TableColumn<PenjualanDetailDAO.DetailRow, Number> colJumlah = new TableColumn<>("Jumlah");
        colJumlah.setCellValueFactory(cell -> new javafx.beans.property.SimpleObjectProperty<>(cell.getValue().jumlah));
        colJumlah.setPrefWidth(80);

        TableColumn<PenjualanDetailDAO.DetailRow, Number> colHarga = new TableColumn<>("Harga Satuan");
        colHarga.setCellValueFactory(cell -> new javafx.beans.property.SimpleObjectProperty<>(cell.getValue().hargaSatuan));
        colHarga.setPrefWidth(130);

        TableColumn<PenjualanDetailDAO.DetailRow, Number> colSubtotal = new TableColumn<>("Subtotal (Rp)");
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
