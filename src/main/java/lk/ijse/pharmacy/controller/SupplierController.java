package lk.ijse.pharmacy.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import lk.ijse.pharmacy.dbconnection.DBConnection;
import lk.ijse.pharmacy.dto.CustomerDTO;
import lk.ijse.pharmacy.dto.SupplierDTO;
import lk.ijse.pharmacy.model.CustomerModel;
import lk.ijse.pharmacy.model.SupplierModel;

import java.io.IOException;
import java.sql.*;


public class SupplierController {

    @FXML
    private TextField txtId;
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtContact;
    @FXML
    private TextField txtEmail;

    @FXML
    private Button btnSave;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnReset;

    @FXML
    private TableView<SupplierDTO> tblSupplier;
    @FXML
    private TableColumn<SupplierDTO,Integer> colId;
    @FXML
    private TableColumn<SupplierDTO,String> colName;
    @FXML
    private TableColumn<SupplierDTO,String> colEmail;
    @FXML
    private TableColumn<SupplierDTO,String> colContact;

    SupplierModel supplierModel = new SupplierModel();
    ObservableList<SupplierDTO> supplierList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {


        loadAllSuppliers();


        tblSupplier.setItems(supplierList);
        colId.setCellValueFactory(new PropertyValueFactory<>("supplierId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("contactNum"));

        tblSupplier.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue)->{
            populateFields(newValue);
        });



    }

    @FXML
    private void handlePressEnter(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            String id = txtId.getText() == null ? "" : txtId.getText().trim();

            if (!id.matches("^\\d+$")) {
                new Alert(Alert.AlertType.WARNING, "Please enter a valid ID!").show();
                return;
            }

            SupplierDTO supplierDTO;
            try {
                supplierDTO = supplierModel.search(Integer.parseInt(id));
                if (supplierDTO == null) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Supplier Not Found");
                    alert.showAndWait();
                    return;
                }
                populateFields(supplierDTO);

            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Supplier Not Found");
                alert.showAndWait();
            }
        }

    }

    @FXML
    private void btnSaveOnAction(ActionEvent event) {
        int id = 0;
        String name = txtName.getText() == null ? "" : txtName.getText().trim();
        String contact = txtContact.getText() == null ? "" : txtContact.getText().trim();
        String email = txtEmail.getText() == null ? "" : txtEmail.getText().trim();


        if (!validateSupplierInput(name, contact, email)) {
            return;
        }

        SupplierDTO supplier = new SupplierDTO(id, name, contact, email);

        try {
            boolean isSaved = supplierModel.save(supplier);
            if (isSaved) {
                new Alert(Alert.AlertType.INFORMATION, "Supplier Saved Successfully!").show();
                loadAllSuppliers(); // Refresh Table
                clearFields();
            }
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Error: " + e.getMessage()).show();
        }
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String id = txtId.getText().trim() == null ? "" : txtId.getText().trim();

        if (id.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please enter an ID to delete").show();
            return;
        }
        if (!id.matches("^\\d+$")) {
            new Alert(Alert.AlertType.WARNING, "Please enter a valid ID!").show();
            return;
        }

        try {
            boolean isDeleted = supplierModel.delete(Integer.parseInt(id));
            if (isDeleted) {
                new Alert(Alert.AlertType.INFORMATION, "Supplier Deleted Successfully!").show();
                loadAllSuppliers();
                clearFields();
            } else {
                new Alert(Alert.AlertType.WARNING, "Supplier ID not found!").show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Error: " + e.getMessage()).show();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        String id = txtId.getText() == null ? "" : txtId.getText().trim();
        String name = txtName.getText();
        String contact = txtContact.getText();
        String email = txtEmail.getText();

        if (!id.matches("^\\d+$")) {
            new Alert(Alert.AlertType.WARNING, "Please enter a valid ID!").show();
            return;
        }

        if (!validateSupplierInput(name, contact, email)) {
            return;
        }

        SupplierDTO supplier = new SupplierDTO(Integer.parseInt(id), name, contact, email);

        try {
            boolean isUpdated = supplierModel.update(supplier);
            if (isUpdated) {
                new Alert(Alert.AlertType.INFORMATION, "Supplier Updated Successfully!").show();
                loadAllSuppliers();
                clearFields();
            } else {
                new Alert(Alert.AlertType.WARNING, "Supplier ID not found!").show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Error: " + e.getMessage()).show();
        }
    }

    private void loadAllSuppliers() {
        try {
            supplierList.clear();
            supplierList.setAll(supplierModel.getAll());
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Error: " + e.getMessage()).show();
        }
    }

    private boolean validateSupplierInput(String name, String contact, String email) {
        // 1. Validate Name: Must be MORE than 3 characters (length >= 4)
        // allowing letters, spaces, and common punctuation like - or '
        if (!name.matches("[A-Za-z .'-]{3,}")) {
            new Alert(Alert.AlertType.ERROR, "Invalid Name! It must be more than 2 letters.").show();
            return false;
        }

        // 2. Validate Contact: Must be exactly 10 digits
        if (!contact.matches("^\\d{10}$")) {
            new Alert(Alert.AlertType.ERROR, "Invalid Contact! Must be exactly 10 numbers.").show();
            return false;
        }

        // 3. Validate Address: Must be MORE than 3 characters
        if (!email.matches("^.{3,}$")) {
            new Alert(Alert.AlertType.ERROR, "Invalid email!").show();
            return false;
        }

        // If code reaches here, all checks passed
        return true;
    }

    @FXML
    void btnResetOnAction(ActionEvent event) {
        loadAllSuppliers();
        clearFields();
    }

    private void populateFields(SupplierDTO supplierDTO) {

        txtId.setText(String.valueOf(supplierDTO.getSupplierId()));
        txtName.setText(supplierDTO.getSupplierName());
        txtContact.setText(supplierDTO.getContactNum());
        txtEmail.setText(supplierDTO.getEmail());
    }

    private void clearFields() {
        txtId.clear();
        txtName.clear();
        txtContact.clear();
        txtEmail.clear();
    }

}
