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
import org.example.data.CustomerDAO;
import org.example.model.Customer;
import java.util.List;

public class CustomerView {
    private CustomerDAO customerDAO;
    private TableView<Customer> table;
    private ObservableList<Customer> customerList;

    public BorderPane create() {
        BorderPane root = new BorderPane();
        customerDAO = new CustomerDAO();

        Label title = new Label("Data Customer");
        title.setStyle("""
                -fx-font-size: 24px;
                -fx-font-weight: bold;
                -fx-text-fill: #1E293B;
                """);

        Label description = new Label("Kelola daftar customer dan histori pembelian mereka.");
        description.setStyle("""
                -fx-font-size: 13px;
                -fx-text-fill: #64748B;
                """);

        Button addButton = new Button("+ Tambah Customer");
        addButton.setStyle("""
                -fx-background-color: #06B6D4;
                -fx-text-fill: white;
                -fx-font-weight: bold;
                -fx-padding: 8 12;
                -fx-cursor: hand;
                """);
        
        addButton.setOnAction(e -> showAddCustomerDialog());

        HBox topBar = new HBox(12, new VBox(4, title, description));
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(20));

        HBox spacer = new HBox();
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);
        topBar.getChildren().addAll(spacer, addButton);

        table = new TableView<>();
        loadCustomerTable();

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

    private void loadCustomerTable() {
        TableColumn<Customer, Number> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(cell -> cell.getValue().idProperty());
        colId.setPrefWidth(60);

        TableColumn<Customer, String> colName = new TableColumn<>("Nama Customer");
        colName.setCellValueFactory(cell -> cell.getValue().nameProperty());
        colName.setPrefWidth(220);

        TableColumn<Customer, String> colPhone = new TableColumn<>("Telepon");
        colPhone.setCellValueFactory(cell -> cell.getValue().phoneProperty());
        colPhone.setPrefWidth(180);

        TableColumn<Customer, Void> colAction = new TableColumn<>("Aksi");
        colAction.setPrefWidth(120);
        colAction.setCellFactory(col -> new TableCell<Customer, Void>() {
            private final Button editBtn = new Button("Edit");
            private final Button delBtn = new Button("Hapus");

            {
                editBtn.setStyle("-fx-padding: 4 8; -fx-font-size: 11;");
                delBtn.setStyle("-fx-padding: 4 8; -fx-font-size: 11; -fx-text-fill: white; -fx-background-color: #DC2626;");
                
                editBtn.setOnAction(e -> showEditCustomerDialog(getTableView().getItems().get(getIndex())));
                delBtn.setOnAction(e -> {
                    Customer c = getTableView().getItems().get(getIndex());
                    customerDAO.delete(c.getId());
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

        table.getColumns().addAll(colId, colName, colPhone, colAction);
    }

    private void refreshTable() {
        List<Customer> customerListData = customerDAO.findAll();
        customerList = FXCollections.observableArrayList(customerListData);
        table.setItems(customerList);
    }

    private void showAddCustomerDialog() {
        Dialog<Customer> dialog = new Dialog<>();
        dialog.setTitle("Tambah Customer");
        dialog.setHeaderText("Masukkan Data Customer Baru");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField namaField = new TextField();
        TextField phoneField = new TextField();

        grid.add(new Label("Nama Customer:"), 0, 0);
        grid.add(namaField, 1, 0);
        grid.add(new Label("Telepon:"), 0, 1);
        grid.add(phoneField, 1, 1);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                if (namaField.getText().isEmpty() || phoneField.getText().isEmpty()) {
                    showError("Semua field harus diisi!");
                    return null;
                }
                Customer c = new Customer(0, namaField.getText(), phoneField.getText());
                customerDAO.save(c);
                return c;
            }
            return null;
        });

        dialog.showAndWait();
        refreshTable();
    }

    private void showEditCustomerDialog(Customer customer) {
        Dialog<Customer> dialog = new Dialog<>();
        dialog.setTitle("Edit Customer");
        dialog.setHeaderText("Edit Data Customer");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField namaField = new TextField(customer.getName());
        TextField phoneField = new TextField(customer.getPhone());

        grid.add(new Label("Nama Customer:"), 0, 0);
        grid.add(namaField, 1, 0);
        grid.add(new Label("Telepon:"), 0, 1);
        grid.add(phoneField, 1, 1);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                if (namaField.getText().isEmpty() || phoneField.getText().isEmpty()) {
                    showError("Semua field harus diisi!");
                    return null;
                }
                Customer updated = new Customer(customer.getId(), namaField.getText(), phoneField.getText());
                customerDAO.update(updated);
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
