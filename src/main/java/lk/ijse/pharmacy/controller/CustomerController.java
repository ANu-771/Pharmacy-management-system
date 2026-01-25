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
import lk.ijse.pharmacy.model.CustomerModel;
import javafx.scene.image.ImageView;

import java.io.IOException;
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
    private TableView<CustomerDTO> tblCustomer;
    @FXML
    private TableColumn<CustomerDTO, Integer> colId;
    @FXML
    private TableColumn<CustomerDTO, String> colName;
    @FXML
    private TableColumn<CustomerDTO, String> colContact;
    @FXML
    private TableColumn<CustomerDTO, String> colAddress;

    CustomerModel customerModel = new CustomerModel();
    ObservableList<CustomerDTO> customerList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {


        loadAllCustomers();


        tblCustomer.setItems(customerList);
        colId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("contact"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));

        tblCustomer.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                populateFields(newValue);
            }
        });


    }

    @FXML
    private void handlePressEnter(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            String id = txtId.getText() == null ? "" : txtId.getText().trim();

            if (!id.matches("^\\d+$")) {
                new Alert(Alert.AlertType.WARNING, "Please enter a valid ID!").show();
                clearFields();
                return;
            }

            CustomerDTO customerDTO;
            try {
                customerDTO = customerModel.search(Integer.parseInt(id));
                if (customerDTO == null) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Customer Not Found");
                    alert.showAndWait();
                    clearFields();
                    return;
                }
                populateFields(customerDTO);

            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Customer Not Found");
                alert.showAndWait();
            }
        }

    }

    //save
    @FXML
    private void btnSaveOnAction(ActionEvent event) {
        int id = 0;
        String name = txtName.getText() == null ? "" : txtName.getText().trim();
        String contact = txtContact.getText() == null ? "" : txtContact.getText().trim();
        String address = txtAddress.getText() == null ? "" : txtAddress.getText().trim();


        if (name.isEmpty() || contact.isEmpty() || address.isEmpty()) {
            new Alert(Alert.AlertType.INFORMATION, "Please fill all the fields").show();
            return;
        }

        if (!validateCustomerInput(name, contact, address)) {
            return;
        }

        // Loop through the existing list to see if this contact already exists
        for (CustomerDTO customer : customerList) {
            if (customer.getContact().equals(contact)) {
                new Alert(Alert.AlertType.WARNING, "A customer with this Contact Number already exists!").show();
                return; // Stop the save process
            }
        }


        CustomerDTO customer = new CustomerDTO(id, name, contact, address);

        try {
            boolean isSaved = customerModel.save(customer); //cAll the model
            if (isSaved) {
                new Alert(Alert.AlertType.INFORMATION, "Customer Saved Successfully!").show();
                loadAllCustomers();
                clearFields();
            }
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Error: " + e.getMessage()).show();
        }
    }

//    @FXML
//    void btnDeleteOnAction(ActionEvent event) {
//        String id = txtId.getText().trim() == null ? "" : txtId.getText().trim();
//
//        if (id.isEmpty()) {
//            new Alert(Alert.AlertType.WARNING, "Please enter an ID to delete").show();
//            return;
//        }
//        if (!id.matches("^\\d+$")) {
//            new Alert(Alert.AlertType.WARNING, "Please enter a valid ID!").show();
//            return;
//        }
//
//        try {
//            boolean isDeleted = customerModel.delete(Integer.parseInt(id));
//            if (isDeleted) {
//                new Alert(Alert.AlertType.INFORMATION, "Customer Deleted Successfully!").show();
//                loadAllCustomers();
//                clearFields();
//            } else {
//                new Alert(Alert.AlertType.WARNING, "Customer ID not found!").show();
//            }
//        } catch (SQLException | ClassNotFoundException e) {
//            new Alert(Alert.AlertType.ERROR, "Error: " + e.getMessage()).show();
//        }
//    }

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

        boolean confirmed = lk.ijse.pharmacy.util.AlertUtil.showConfirmation("Confirm Deletion",
                "Are you sure you want to delete this customer?",
                "delete-alert");

        if (!confirmed) return; // Stop if Cancel is clicked

        try {
            boolean isDeleted = customerModel.delete(Integer.parseInt(id)); //
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

//    @FXML
//    void btnUpdateOnAction(ActionEvent event) {
//        String id = txtId.getText() == null ? "" : txtId.getText().trim();
//        String name = txtName.getText().trim();
//        String contact = txtContact.getText().trim();
//        String address = txtAddress.getText().trim();
//
//        if (!id.matches("^\\d+$")) {
//            new Alert(Alert.AlertType.WARNING, "Please enter a valid ID!").show();
//            return;
//        }
//
//        if (!validateCustomerInput(name, contact, address)) {
//            return;
//        }
//
//        CustomerDTO customer = new CustomerDTO(Integer.parseInt(id), name, contact, address);
//
//        try {
//            boolean isUpdated = customerModel.update(customer);
//            if (isUpdated) {
//                new Alert(Alert.AlertType.INFORMATION, "Customer Updated Successfully!").show();
//
//                loadAllCustomers();
//                clearFields();
//
//            } else {
//                new Alert(Alert.AlertType.WARNING, "Customer ID not found!").show();
//            }
//        } catch (SQLException | ClassNotFoundException e) {
//            new Alert(Alert.AlertType.ERROR, "Error: " + e.getMessage()).show();
//        }
//    }


    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        String idText = txtId.getText() == null ? "" : txtId.getText().trim();
        String name = txtName.getText() == null ? "" : txtName.getText().trim();
        String contact = txtContact.getText() == null ? "" : txtContact.getText().trim();
        String address = txtAddress.getText() == null ? "" : txtAddress.getText().trim();

        if (!idText.matches("^\\d+$")) {
            new Alert(Alert.AlertType.WARNING, "Please enter a valid ID!").show();
            return;
        }

        if (!validateCustomerInput(name, contact, address)) {
            return;
        }

        boolean confirmed = lk.ijse.pharmacy.util.AlertUtil.showConfirmation("Confirm Update",
                "Are you sure you want to update this customer's details?",
                "update-alert");

        if (!confirmed) return; // Stop if Cancel is clicked

        CustomerDTO customer = new CustomerDTO(Integer.parseInt(idText), name, contact, address);

        try {
            boolean isUpdated = customerModel.update(customer);
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
        try {
            customerList.clear();
            customerList.setAll(customerModel.getAll());
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Error: " + e.getMessage()).show();
        }
    }

    private boolean validateCustomerInput(String name, String contact, String address) {
        if (!name.matches("[A-Za-z .'-]{3,}")) {
            new Alert(Alert.AlertType.ERROR, "Invalid Name! It must be more than 2 letters.").show();
            return false;
        }

        if (!contact.matches("^\\d{10}$")) {
            new Alert(Alert.AlertType.ERROR, "Invalid Contact! Must be exactly 10 numbers.").show();
            return false;
        }

        if (!address.matches("^[a-zA-Z0-9\\s]{3,}$")) {
            new Alert(Alert.AlertType.ERROR, "Invalid Address! Use only letters, numbers, and spaces.").show();
            return false;
        }

        return true;
    }


    @FXML
    void btnResetOnAction(ActionEvent event) {
        clearFields();
    }

    private void populateFields(CustomerDTO customerDTO) {

        txtId.setText(String.valueOf(customerDTO.getCustomerId()));
        txtName.setText(customerDTO.getName());
        txtContact.setText(customerDTO.getContact());
        txtAddress.setText(customerDTO.getAddress());
    }

    private void clearFields() {
        txtId.clear();
        txtName.clear();
        txtContact.clear();
        txtAddress.clear();
    }

}
