package org.example.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class InMemoryRepository {
    private static final InMemoryRepository instance = new InMemoryRepository();

    private final ObservableList<Supplier> suppliers = FXCollections.observableArrayList();
    private final ObservableList<Customer> customers = FXCollections.observableArrayList();

    private int nextSupplierId = 1;
    private int nextCustomerId = 1;

    private InMemoryRepository() {
        addSupplier(new Supplier(nextSupplierId++, "PT. Sumber Spare", "021-555-0101"));
        addSupplier(new Supplier(nextSupplierId++, "CV. Sinar Motor", "021-555-0202"));

        addCustomer(new Customer(nextCustomerId++, "Budi Santoso", "0812-345-678"));
        addCustomer(new Customer(nextCustomerId++, "Toko Maju Jaya", "0821-999-000"));
    }

    public static InMemoryRepository getInstance() { return instance; }

    public ObservableList<Supplier> getSuppliers() { return suppliers; }
    public ObservableList<Customer> getCustomers() { return customers; }

    public void addSupplier(Supplier s) { suppliers.add(s); }
    public void addCustomer(Customer c) { customers.add(c); }

    public int nextSupplierId() { return nextSupplierId++; }
    public int nextCustomerId() { return nextCustomerId++; }
}
