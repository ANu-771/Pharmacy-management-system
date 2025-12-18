package lk.ijse.pharmacy.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import lk.ijse.pharmacy.dto.MedicineDTO;
import lk.ijse.pharmacy.model.MedicineModel;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class MedicineController {

    @FXML
    private Button btnClear, btnDelete, btnSave, btnUpdate;

    @FXML
    private TableColumn<MedicineDTO, String> colBrand;

    @FXML
    private TableColumn<MedicineDTO, String> colExpDate;

    @FXML
    private TableColumn<MedicineDTO, Integer> colId;

    @FXML
    private TableColumn<MedicineDTO, String> colName;

    @FXML
    private TableColumn<MedicineDTO, Double> colPrice;

    @FXML
    private TableColumn<MedicineDTO, Integer> colQty;

    @FXML
    private DatePicker dpExpDate;

    @FXML
    private TableView<MedicineDTO> tblMedicine;

    @FXML
    private TextField txtBrand, txtId,txtName, txtPrice, txtQty;



    private MedicineModel medicineModel = new MedicineModel();
    private ObservableList<MedicineDTO> medicineList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
       // txtId.setEditable(false);
        loadAllMedicines();
        setupTable();
        tblMedicine.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) populateFields(newVal);
        });
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        String name = txtName.getText().trim();
        String brand = txtBrand.getText().trim();
        String priceText = txtPrice.getText().trim();
        String qtyText = txtQty.getText().trim();
        LocalDate localExpDate = dpExpDate.getValue();

        if (name.isEmpty() || brand.isEmpty() || priceText.isEmpty() || qtyText.isEmpty() || localExpDate == null) {
            new Alert(Alert.AlertType.WARNING, "Please fill all fields!").show();
            return;
        }

        if (!validateInput(priceText, qtyText)) return;

        double price = Double.parseDouble(priceText);
        int qty = Integer.parseInt(qtyText);
        Date expDate = Date.from(localExpDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        MedicineDTO medicine = new MedicineDTO(name, brand, qty, price, expDate);

        try {
            if (medicineModel.save(medicine)) {
                new Alert(Alert.AlertType.INFORMATION, "Medicine Saved Successfully!").show();
                loadAllMedicines();
                clearFields();
            }
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Error: " + e.getMessage()).show();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        String idText = txtId.getText(); // ID is required for Update
        if (idText.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Select a medicine to update!").show();
            return;
        }

        String name = txtName.getText().trim();
        String brand = txtBrand.getText().trim();
        String priceText = txtPrice.getText().trim();
        String qtyText = txtQty.getText().trim();
        LocalDate localExpDate = dpExpDate.getValue();

        if (name.isEmpty() || brand.isEmpty() || priceText.isEmpty() || qtyText.isEmpty() || localExpDate == null) {
            new Alert(Alert.AlertType.WARNING, "Please fill all fields!").show();
            return;
        }

        if (!validateInput(priceText, qtyText)) return;

        int id = Integer.parseInt(idText);
        double price = Double.parseDouble(priceText);
        int qty = Integer.parseInt(qtyText);
        Date expDate = Date.from(localExpDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        MedicineDTO medicine = new MedicineDTO(id, name, brand, qty, price, expDate);

        try {
            if (medicineModel.update(medicine)) {
                new Alert(Alert.AlertType.INFORMATION, "Updated Successfully!").show();
                loadAllMedicines();
                clearFields();
            }
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Error: " + e.getMessage()).show();
        }
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String idText = txtId.getText();
        if (idText.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Select a medicine to delete!").show();
            return;
        }
        try {
            if (medicineModel.delete(Integer.parseInt(idText))) {
                new Alert(Alert.AlertType.INFORMATION, "Deleted Successfully!").show();
                loadAllMedicines();
                clearFields();
            }
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Error: " + e.getMessage()).show();
        }
    }

    @FXML
    private void handlePressEnter(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            String idText = txtId.getText().trim();

            if (!idText.matches("^\\d+$")) {
                new Alert(Alert.AlertType.WARNING, "Please enter a valid Medicine ID!").show();
                return;
            }

            try {
                MedicineDTO medicineDTO = medicineModel.search(Integer.parseInt(idText));

                if (medicineDTO != null) {
                    populateFields(medicineDTO);
                } else {
                    new Alert(Alert.AlertType.INFORMATION, "Medicine Not Found").show();
                }

            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, "Error searching: " + e.getMessage()).show();
            }
        }
    }


    private void setupTable() {
        tblMedicine.setItems(medicineList);
        colId.setCellValueFactory(new PropertyValueFactory<>("medicineId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("medName"));
        colBrand.setCellValueFactory(new PropertyValueFactory<>("brand"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colExpDate.setCellValueFactory(new PropertyValueFactory<>("expDate"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qtyInStock"));
    }

    private void loadAllMedicines() {
        try {
            medicineList.clear();
            medicineList.addAll(medicineModel.getAll());
        } catch (SQLException | ClassNotFoundException e) { e.printStackTrace(); }
    }

    private boolean validateInput(String price, String qty) {
        return price.matches("^\\d+(\\.\\d+)?$") && qty.matches("^\\d+$");
    }

    private void populateFields(MedicineDTO dto) {
        txtId.setText(String.valueOf(dto.getMedicineId()));
        txtName.setText(dto.getMedName());
        txtBrand.setText(dto.getBrand());
        txtPrice.setText(String.valueOf(dto.getUnitPrice()));
        txtQty.setText(String.valueOf(dto.getQtyInStock()));
        if (dto.getExpDate() != null) {
            dpExpDate.setValue(new java.sql.Date(dto.getExpDate().getTime()).toLocalDate());
        }
    }

    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearFields();
    }

    private void clearFields() {
        txtId.clear();
        txtName.clear();
        txtBrand.clear();
        txtPrice.clear();
        txtQty.clear();
        dpExpDate.setValue(null);
    }
}