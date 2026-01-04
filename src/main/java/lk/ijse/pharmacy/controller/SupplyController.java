package lk.ijse.pharmacy.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.geometry.Side;
import lk.ijse.pharmacy.dto.MedicineDTO;
import lk.ijse.pharmacy.dto.SupplierDTO;
import lk.ijse.pharmacy.dto.tm.SupplyRecordTM;
import lk.ijse.pharmacy.model.MedicineModel;
import lk.ijse.pharmacy.model.SupplierModel;
import lk.ijse.pharmacy.model.SupplyModel;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class SupplyController {

    @FXML
    private ComboBox<String> cmbSupplierId;
    @FXML
    private TextField txtSupplierName;
    @FXML
    private DatePicker dpSupplyDate;

    @FXML
    private ComboBox<String> cmbMedicineId;
    @FXML
    private TextField txtDescription;
    @FXML
    private TextField txtBuyingPrice;
    @FXML
    private TextField txtQty;

    @FXML
    private TableView<SupplyRecordTM> tblSupplyRecord;
    @FXML
    private TableColumn<SupplyRecordTM, String> colHistoryDate;
    @FXML
    private TableColumn<SupplyRecordTM, Integer> colHistorySupId;
    @FXML
    private TableColumn<SupplyRecordTM, String> colHistorySupName;
    @FXML
    private TableColumn<SupplyRecordTM, String> colHistoryMedName;
    @FXML
    private TableColumn<SupplyRecordTM, Integer> colHistoryQty;
    @FXML
    private TableColumn<SupplyRecordTM, Double> colHistoryUnitCost;
    @FXML
    private TableColumn<SupplyRecordTM, Double> colHistoryTotalCost;

    private SupplierModel supplierModel = new SupplierModel();
    private MedicineModel medicineModel = new MedicineModel();
    private SupplyModel supplyModel = new SupplyModel();

    private List<SupplierDTO> allSuppliers;
    private List<MedicineDTO> allMedicines;

    public void initialize() {
        dpSupplyDate.setValue(LocalDate.now());

        loadAllData();
        setupSupplierAutoSuggestion();
        setupMedicineAutoSuggestion();

        setTableFactory();
        loadHistoryTable();
    }

    private void setTableFactory() {
        colHistoryDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colHistorySupId.setCellValueFactory(new PropertyValueFactory<>("supplierId"));
        colHistorySupName.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
        colHistoryMedName.setCellValueFactory(new PropertyValueFactory<>("medicineName"));
        colHistoryQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colHistoryUnitCost.setCellValueFactory(new PropertyValueFactory<>("unitCost"));
        colHistoryTotalCost.setCellValueFactory(new PropertyValueFactory<>("totalCost"));
    }

    private void loadHistoryTable() {
        try {
            List<SupplyRecordTM> history = supplyModel.getAllSupplies();
            tblSupplyRecord.setItems(FXCollections.observableArrayList(history));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnAddOnAction(ActionEvent event) {
        if (cmbSupplierId.getValue() == null || txtDescription.getText().isEmpty() ||
                txtQty.getText().isEmpty() || txtBuyingPrice.getText().isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please fill all fields!").show();
            return;
        }

        String medName = txtDescription.getText().trim();

        // CHECK IF MEDICINE EXISTS
        boolean isMedicineExist = false;
        if (allMedicines != null) {
            isMedicineExist = allMedicines.stream()
                    .anyMatch(m -> m.getMedName().equalsIgnoreCase(medName));
        }

        if (!isMedicineExist) {
            new Alert(Alert.AlertType.ERROR, "Medicine not found! You cannot add new medicines here.").show();
            return;
        }

        try {
            int qty = Integer.parseInt(txtQty.getText());
            double unitCost = Double.parseDouble(txtBuyingPrice.getText());
            double totalCost = qty * unitCost;

            boolean isSaved = supplyModel.saveSupply(Integer.parseInt(cmbSupplierId.getValue()),medName,
                    dpSupplyDate.getValue(), qty, unitCost, totalCost);

            if (isSaved) {
                new Alert(Alert.AlertType.INFORMATION, "Supply Added Successfully!").show();
                loadHistoryTable();
                clearFields();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to Add!").show();
            }

        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Invalid Number format!").show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clearFields() {
        cmbMedicineId.getSelectionModel().clearSelection(); // Clear ID
        txtDescription.clear();
        txtBuyingPrice.clear();
        txtQty.clear();
    }

    private void loadAllData() {
        try {
            allSuppliers = supplierModel.getAll();
            allMedicines = medicineModel.getAll();

            // Load Supplier ID
            ObservableList<String> supIds = FXCollections.observableArrayList();
            for (SupplierDTO s : allSuppliers) supIds.add(String.valueOf(s.getSupplierId()));
            cmbSupplierId.setItems(supIds);

            // Load Medicine ID
            ObservableList<String> medIds = FXCollections.observableArrayList();
            for (MedicineDTO m : allMedicines) medIds.add(String.valueOf(m.getMedicineId()));
            cmbMedicineId.setItems(medIds);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void cmbSupplierOnAction(ActionEvent event) {
        String id = cmbSupplierId.getValue();
        if (id == null) return;
        allSuppliers.stream().filter(s -> String.valueOf(s.getSupplierId()).equals(id))
                .findFirst().ifPresent(s -> txtSupplierName.setText(s.getSupplierName()));
    }

    @FXML
    void cmbMedicineOnAction(ActionEvent event) {
        String id = cmbMedicineId.getValue();
        if (id == null) return;
        allMedicines.stream().filter(m -> String.valueOf(m.getMedicineId()).equals(id))
                .findFirst().ifPresent(m -> {
                    txtDescription.setText(m.getMedName());
                    txtBuyingPrice.requestFocus();
                });
    }

    private void setupSupplierAutoSuggestion() {
        ContextMenu menu = new ContextMenu();
        txtSupplierName.textProperty().addListener((obs, old, newVal) -> {
            if (newVal == null || newVal.isEmpty()) {
                menu.hide();
                return;
            }
            List<SupplierDTO> matches = allSuppliers.stream().filter(s -> s.getSupplierName().toLowerCase().contains(newVal.toLowerCase())).collect(Collectors.toList());
            if (matches.isEmpty()) {
                menu.hide();
                return;
            }
            menu.getItems().clear();
            for (SupplierDTO s : matches) {
                MenuItem item = new MenuItem(s.getSupplierName());
                item.setOnAction(e -> {
                    txtSupplierName.setText(s.getSupplierName());
                    cmbSupplierId.setValue(String.valueOf(s.getSupplierId()));
                    menu.hide();
                });
                menu.getItems().add(item);
            }
            if (!menu.isShowing()) menu.show(txtSupplierName, Side.BOTTOM, 0, 0);
        });
    }

    private void setupMedicineAutoSuggestion() {
        ContextMenu menu = new ContextMenu();
        txtDescription.textProperty().addListener((obs, old, newVal) -> {
            if (newVal == null || newVal.isEmpty()) {
                menu.hide();
                cmbMedicineId.getSelectionModel().clearSelection();
                return;
            }
            List<MedicineDTO> matches = allMedicines.stream().filter(m -> m.getMedName().toLowerCase().contains(newVal.toLowerCase())).collect(Collectors.toList());
            if (matches.isEmpty()) {
                menu.hide();
                return;
            }
            menu.getItems().clear();
            for (MedicineDTO m : matches) {
                MenuItem item = new MenuItem(m.getMedName());
                item.setOnAction(e -> {
                    txtDescription.setText(m.getMedName());
                    cmbMedicineId.setValue(String.valueOf(m.getMedicineId()));
                    menu.hide();
                });
                menu.getItems().add(item);
            }
            if (!menu.isShowing()) menu.show(txtDescription, Side.BOTTOM, 0, 0);
        });
        txtDescription.focusedProperty().addListener((obs, old, newVal) -> {
            if (!newVal) menu.hide();
        });
    }
}