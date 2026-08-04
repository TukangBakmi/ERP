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
import org.example.data.SupplierDAO;
import org.example.model.Supplier;
import java.util.List;

public class SupplierView {
    private SupplierDAO supplierDAO;
    private TableView<Supplier> table;
    private ObservableList<Supplier> supplierList;

    public BorderPane create() {
        BorderPane root = new BorderPane();
        supplierDAO = new SupplierDAO();

        Label title = new Label("Data Supplier");
        title.setStyle("""
                -fx-font-size: 24px;
                -fx-font-weight: bold;
                -fx-text-fill: #1E293B;
                """);

        Label description = new Label("Kelola informasi supplier: kontak, alamat, dan transaksi.");
        description.setStyle("""
                -fx-font-size: 13px;
                -fx-text-fill: #64748B;
                """);

        Button addButton = new Button("+ Tambah Supplier");
        addButton.setStyle("""
                -fx-background-color: #10B981;
                -fx-text-fill: white;
                -fx-font-weight: bold;
                -fx-padding: 8 12;
                -fx-cursor: hand;
                """);
        
        addButton.setOnAction(e -> showAddSupplierDialog());

        HBox topBar = new HBox(12, new VBox(4, title, description));
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(20));

        HBox spacer = new HBox();
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);
        topBar.getChildren().addAll(spacer, addButton);

        table = new TableView<>();
        loadSupplierTable();

        VBox content = new VBox(table);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(12));
        content.getStyleClass().add("content-panel");

        BorderPane.setMargin(content, new Insets(0, 20, 20, 20));

        root.setTop(topBar);
        root.setCenter(content);
        root.getStyleClass().add("root");
        root.setStyle("-fx-background-color: #F8FAFC;");

        refreshTable();

        return root;
    }

    private void loadSupplierTable() {
        TableColumn<Supplier, Number> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(cell -> cell.getValue().idProperty());
        colId.setPrefWidth(60);

        TableColumn<Supplier, String> colName = new TableColumn<>("Nama Supplier");
        colName.setCellValueFactory(cell -> cell.getValue().nameProperty());
        colName.setPrefWidth(220);

        TableColumn<Supplier, String> colContact = new TableColumn<>("Kontak");
        colContact.setCellValueFactory(cell -> cell.getValue().contactProperty());
        colContact.setPrefWidth(180);

        TableColumn<Supplier, Void> colAction = new TableColumn<>("Aksi");
        colAction.setPrefWidth(120);
        colAction.setCellFactory(col -> new TableCell<Supplier, Void>() {
            private final Button editBtn = new Button("Edit");
            private final Button delBtn = new Button("Hapus");

            {
                editBtn.setStyle("-fx-padding: 4 8; -fx-font-size: 11;");
                delBtn.setStyle("-fx-padding: 4 8; -fx-font-size: 11; -fx-text-fill: white; -fx-background-color: #DC2626;");
                
                editBtn.setOnAction(e -> showEditSupplierDialog(getTableView().getItems().get(getIndex())));
                delBtn.setOnAction(e -> {
                    Supplier s = getTableView().getItems().get(getIndex());
                    supplierDAO.delete(s.getId());
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

        table.getColumns().addAll(colId, colName, colContact, colAction);
    }

    private void refreshTable() {
        List<Supplier> supplierListData = supplierDAO.findAll();
        supplierList = FXCollections.observableArrayList(supplierListData);
        table.setItems(supplierList);
    }

    private void showAddSupplierDialog() {
        Dialog<Supplier> dialog = new Dialog<>();
        dialog.setTitle("Tambah Supplier");
        dialog.setHeaderText("Masukkan Data Supplier Baru");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField namaField = new TextField();
        TextField contactField = new TextField();

        grid.add(new Label("Nama Supplier:"), 0, 0);
        grid.add(namaField, 1, 0);
        grid.add(new Label("Kontak:"), 0, 1);
        grid.add(contactField, 1, 1);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                if (namaField.getText().isEmpty() || contactField.getText().isEmpty()) {
                    showError("Semua field harus diisi!");
                    return null;
                }
                Supplier s = new Supplier(0, namaField.getText(), contactField.getText());
                supplierDAO.save(s);
                return s;
            }
            return null;
        });

        dialog.showAndWait();
        refreshTable();
    }

    private void showEditSupplierDialog(Supplier supplier) {
        Dialog<Supplier> dialog = new Dialog<>();
        dialog.setTitle("Edit Supplier");
        dialog.setHeaderText("Edit Data Supplier");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField namaField = new TextField(supplier.getName());
        TextField contactField = new TextField(supplier.getContact());

        grid.add(new Label("Nama Supplier:"), 0, 0);
        grid.add(namaField, 1, 0);
        grid.add(new Label("Kontak:"), 0, 1);
        grid.add(contactField, 1, 1);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                if (namaField.getText().isEmpty() || contactField.getText().isEmpty()) {
                    showError("Semua field harus diisi!");
                    return null;
                }
                Supplier updated = new Supplier(supplier.getId(), namaField.getText(), contactField.getText());
                supplierDAO.update(updated);
                return updated;
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
