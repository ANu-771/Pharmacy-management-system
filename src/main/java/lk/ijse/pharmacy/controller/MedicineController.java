package lk.ijse.pharmacy.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import lk.ijse.pharmacy.dto.MedicineDTO;
import lk.ijse.pharmacy.model.MedicineModel;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.geometry.Side;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javafx.scene.control.TableRow;

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
    private TextField txtBrand, txtId, txtName, txtPrice, txtQty;


    private MedicineModel medicineModel = new MedicineModel();
    private ObservableList<MedicineDTO> medicineList = FXCollections.observableArrayList();
    private List<String> allMedicineNames = new ArrayList<>();


    @FXML
    private void initialize() {
        loadAllMedicines();
        setupTable();

        loadMedicineNames();    // Load names into memory
        setupAutoSuggestion();

        tblMedicine.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) populateFields(newVal);
        });

        txtPrice.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d*)?")) {
                txtPrice.setText(oldValue);
            }
        });

        txtQty.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                txtQty.setText(oldValue);
            }
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

//    @FXML
//    void btnUpdateOnAction(ActionEvent event) {
//        String idText = txtId.getText().trim();
//        if (idText.isEmpty()) {
//            new Alert(Alert.AlertType.WARNING, "Select a medicine to update!").show();
//            return;
//        }
//
//        String name = txtName.getText().trim();
//        String brand = txtBrand.getText().trim();
//        String priceText = txtPrice.getText().trim();
//        String qtyText = txtQty.getText().trim();
//        LocalDate localExpDate = dpExpDate.getValue();
//
//        if (name.isEmpty() || brand.isEmpty() || priceText.isEmpty() || qtyText.isEmpty() || localExpDate == null) {
//            new Alert(Alert.AlertType.WARNING, "Please fill all fields!").show();
//            return;
//        }
//
//        if (!validateInput(priceText, qtyText)) return;
//
//        int id = Integer.parseInt(idText);
//        double price = Double.parseDouble(priceText);
//        int qty = Integer.parseInt(qtyText);
//        Date expDate = Date.from(localExpDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
//
//        MedicineDTO medicine = new MedicineDTO(id, name, brand, qty, price, expDate);
//
//        try {
//            if (medicineModel.update(medicine)) {
//                new Alert(Alert.AlertType.INFORMATION, "Updated Successfully!").show();
//                loadAllMedicines();
//                clearFields();
//            }
//        } catch (SQLException | ClassNotFoundException e) {
//            new Alert(Alert.AlertType.ERROR, "Error: " + e.getMessage()).show();
//        }
//    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        String idText = txtId.getText().trim();
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

        boolean confirmed = lk.ijse.pharmacy.util.AlertUtil.showConfirmation("Confirm Update",
                "Are you sure you want to update this record?",
                "update-alert");

        if (!confirmed) return;

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

//    @FXML
//    void btnDeleteOnAction(ActionEvent event) {
//        String idText = txtId.getText().trim();
//        if (idText.isEmpty()) {
//            new Alert(Alert.AlertType.WARNING, "Select a medicine to delete!").show();
//            return;
//        }
//        try {
//            if (medicineModel.delete(Integer.parseInt(idText))) {
//                new Alert(Alert.AlertType.INFORMATION, "Deleted Successfully!").show();
//                loadAllMedicines();
//                clearFields();
//            }
//        } catch (SQLException | ClassNotFoundException e) {
//            new Alert(Alert.AlertType.ERROR, "Error: " + e.getMessage()).show();
//        }
//    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String idText = txtId.getText().trim();
        if (idText.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Select a medicine to delete!").show();
            return;
        }

        boolean confirmed = lk.ijse.pharmacy.util.AlertUtil.showConfirmation("Confirm Deletion",
                "Are you sure you want to delete this medicine?",
                "delete-alert");

        if (!confirmed) return;

        try {
            if (medicineModel.delete(Integer.parseInt(idText))) { //
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
            String nameText = txtName.getText().trim();

            MedicineDTO medicineDTO = null;

            try {
                if (!idText.isEmpty()) {
                    if (idText.matches("^\\d+$")) {
                        medicineDTO = medicineModel.search(Integer.parseInt(idText));
                    } else {
                        new Alert(Alert.AlertType.WARNING, "Invalid ID format!").show();
                        return;
                    }
                } else if (!nameText.isEmpty()) {
                    medicineDTO = medicineModel.searchByName(nameText);
                } else {
                    new Alert(Alert.AlertType.WARNING, "Please enter an ID or Name to search!").show();
                    return;
                }

                if (medicineDTO != null) {
                    populateFields(medicineDTO);
                } else {
                    new Alert(Alert.AlertType.INFORMATION, "Medicine Not Found").showAndWait();
                    clearFields();
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


        tblMedicine.setRowFactory(tv -> new TableRow<MedicineDTO>() {
            @Override
            protected void updateItem(MedicineDTO item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty || item.getExpDate() == null) {
                    setStyle("");
                } else {

                    java.time.LocalDate expDate = new java.sql.Date(item.getExpDate().getTime()).toLocalDate();
                    java.time.LocalDate today = java.time.LocalDate.now();

                    if (expDate.isBefore(today)) {
                        // Deep Red
                        setStyle("-fx-background-color: #ff9999; -fx-text-background-color: white;");
                    } else if (expDate.isBefore(today.plusDays(21))) {
                        // EXPIRING SOON (Next 21 Days): Light Red
                        setStyle("-fx-background-color: #ffcdd2;");
                    }
                    // LOW STOCK (Yellow)
                    else if (item.getQtyInStock() <= 21) {
                        setStyle("-fx-background-color: #fef08a; -fx-text-background-color: black;");
                    } else {
                        // Clear style
                        setStyle("");
                    }
                }
            }
        });
    }

    private void loadAllMedicines() {
        try {
            medicineList.clear();
            medicineList.addAll(medicineModel.getAll());

            loadMedicineNames();

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
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

    private void loadMedicineNames() {
        try {
            List<MedicineDTO> allMedicines = medicineModel.getAll();
            allMedicineNames.clear();
            for (MedicineDTO m : allMedicines) {
                allMedicineNames.add(m.getMedName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Set up the popup listener
    private void setupAutoSuggestion() {
        ContextMenu suggestionsMenu = new ContextMenu();

        txtName.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                suggestionsMenu.hide();
                return;
            }

            List<String> matches = allMedicineNames.stream()
                    .filter(name -> name.toLowerCase().contains(newValue.toLowerCase()))
                    .collect(Collectors.toList());

            if (matches.isEmpty()) {
                suggestionsMenu.hide();
                return;
            }

            suggestionsMenu.getItems().clear();
            for (String match : matches) {
                MenuItem item = new MenuItem(match);

                item.setOnAction(event -> {
                    txtName.setText(match);
                    suggestionsMenu.hide();

                    try {
                        MedicineDTO medicine = medicineModel.searchByName(match);
                        if (medicine != null) {
                            populateFields(medicine);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

                suggestionsMenu.getItems().add(item);
            }

            if (!suggestionsMenu.isShowing()) {
                suggestionsMenu.show(txtName, Side.BOTTOM, 0, 0);
            }
        });

        txtName.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) suggestionsMenu.hide();
        });
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
