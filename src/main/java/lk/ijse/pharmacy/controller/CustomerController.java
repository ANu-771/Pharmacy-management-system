package lk.ijse.pharmacy.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.pharmacy.dbconnection.DBConnection;
import lk.ijse.pharmacy.dto.CustomerDTO;
import lk.ijse.pharmacy.model.CustomerModel;

import java.sql.*;


public class CustomerController {

    @FXML
    private TextField txtId;
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtContact;
    @FXML
    private TextField txtAddress;

    @FXML
    private Button btnSave;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnReset;

    @FXML
    private TableView tblCustomer;
    @FXML
    private TableColumn colId;
    @FXML
    private TableColumn colName;
    @FXML
    private TableColumn colContact;
    @FXML
    private TableColumn colAddress;

    @FXML
    private void initialize() {
        System.out.println("Customer FXML is loaded!");
            // Initialize Table Columns
            colId.setCellValueFactory(new PropertyValueFactory<>("id"));
            colName.setCellValueFactory(new PropertyValueFactory<>("name"));
            colContact.setCellValueFactory(new PropertyValueFactory<>("contact"));
            colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));

            // Load data into table
            loadAllCustomers();

    }

    @FXML
    private void btnSaveOnAction(ActionEvent event) {
        int id = 0;
        String name = txtName.getText() == null ? "" : txtName.getText().trim();
        String contact = txtContact.getText()  == null ? "" : txtContact.getText().trim();
        String address = txtAddress.getText() == null ? "" : txtAddress.getText().trim();


//        if(name.isEmpty() || contact.isEmpty() || address.isEmpty()){
//            Alert alert = new Alert(Alert.AlertType.INFORMATION,"Please fill all the fields");
//            return;
//        }
        if(!validateCustomerInput(name, contact, address)){
            return;
        }

        CustomerDTO customer = new CustomerDTO(id, name, contact, address);

        try {
            boolean isSaved = CustomerModel.save(customer);
            if (isSaved) {
                new Alert(Alert.AlertType.INFORMATION, "Customer Saved Successfully!").show();
                loadAllCustomers(); // Refresh Table
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

        try {
            boolean isDeleted = CustomerModel.delete(Integer.parseInt(id));
            if (isDeleted) {
                new Alert(Alert.AlertType.INFORMATION, "Customer Deleted Successfully!").show();
                loadAllCustomers();
                clearFields();
            } else {
                new Alert(Alert.AlertType.WARNING, "Customer ID not found!").show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Error: " + e.getMessage()).show();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        int id = Integer.parseInt(txtId.getText());
        String name = txtName.getText();
        String contact = txtContact.getText();
        String address = txtAddress.getText();

        CustomerDTO customer = new CustomerDTO(id, name, contact, address);

        try {
            boolean isUpdated = CustomerModel.update(customer);
            if (isUpdated) {
                new Alert(Alert.AlertType.INFORMATION, "Customer Updated Successfully!").show();
                loadAllCustomers();
                clearFields();
            } else {
                new Alert(Alert.AlertType.WARNING, "Customer ID not found!").show();
            }
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Error: " + e.getMessage()).show();
        }
    }

    private void loadAllCustomers() {

    }

    private boolean validateCustomerInput(String name, String contact, String address) {
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
        if (!address.matches("^.{3,}$")) {
            new Alert(Alert.AlertType.ERROR, "Invalid Address! Must be more than 2 characters.").show();
            return false;
        }

        // If code reaches here, all checks passed
        return true;
    }

    @FXML
    void btnResetOnAction(ActionEvent event) {
        clearFields();
    }

    private void clearFields() {
        txtId.clear();
        txtName.clear();
        txtContact.clear();
        txtAddress.clear();
    }

}
