package lk.ijse.pharmacy.controller;

import javafx.event.ActionEvent;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.pharmacy.dbconnection.DBConnection;
import lk.ijse.pharmacy.dto.CustomerDTO;
import lk.ijse.pharmacy.dto.MedicineDTO;
import lk.ijse.pharmacy.dto.OrderDTO;
import lk.ijse.pharmacy.dto.tm.CartTM;
import lk.ijse.pharmacy.model.CustomerModel;
import lk.ijse.pharmacy.model.MedicineModel;
import lk.ijse.pharmacy.model.OrderModel;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.geometry.Side;

import java.util.stream.Collectors;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class OrderController {

    @FXML
    private ComboBox<String> cmbCustomerId;

    @FXML
    private ComboBox<String> cmbMedicineId;

    @FXML
    private TableView<CartTM> tblOrderCart;

    @FXML
    private TableColumn<CartTM, String> colMedicineId;

    @FXML
    private TableColumn<CartTM, String> colDescription;

    @FXML
    private TableColumn<CartTM, Integer> colQty;

    @FXML
    private TableColumn<CartTM, Double> colUnitPrice;

    @FXML
    private TableColumn<CartTM, Double> colLineTotal;

    @FXML
    private TableColumn<CartTM, Button> colAction;

    @FXML
    private Label lblCustomerName;

    @FXML
    private TextField txtDescription;

    @FXML
    private Label lblOrderDate;

    @FXML
    private Label lblQtyOnHand;

    @FXML
    private Label lblUnitPrice;

    @FXML
    private Label lblNetTotal;

    @FXML
    private TextField txtOrderId;

    @FXML
    private TextField txtQty;

    @FXML
    private ComboBox<String> cmbPaymentMethod;

    @FXML
    private TextField txtCash;

    @FXML
    private Label lblBalance;

    private OrderModel orderModel = new OrderModel();
    private CustomerModel customerModel = new CustomerModel();
    private MedicineModel medicineModel = new MedicineModel();
    private List<String> allMedicineNames = new ArrayList<>();

    private MedicineDTO selectedMedicine = null;

    private ObservableList<CartTM> cartList = FXCollections.observableArrayList();
    private double netTotal = 0;

    @FXML
    public void initialize() {
        setCellValueFactory();
        loadNextOrderId();
        loadCustomerIds();
        loadMedicineIds();
        lblOrderDate.setText(String.valueOf(LocalDate.now()));

        loadMedicineNames();
        setupAutoSuggestion();
        setupPaymentLogic();
    }

    private void setCellValueFactory() {
        colMedicineId.setCellValueFactory(new PropertyValueFactory<>("medicineId"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colLineTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("btnRemove"));

        tblOrderCart.setItems(cartList);
    }

    private void loadNextOrderId() {

        try {
            txtOrderId.setText(orderModel.getNextOrderId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadCustomerIds() {
        try {
            List<CustomerDTO> allCustomers = customerModel.getAll();
            ObservableList<String> ids = FXCollections.observableArrayList();
            for (CustomerDTO c : allCustomers) {
                ids.add(String.valueOf(c.getCustomerId()));
            }
            cmbCustomerId.setItems(ids);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadMedicineIds() {
        try {
            List<MedicineDTO> allMedicines = medicineModel.getAll();
            ObservableList<String> ids = FXCollections.observableArrayList();
            for (MedicineDTO m : allMedicines) {
                ids.add(String.valueOf(m.getMedicineId()));
            }
            cmbMedicineId.setItems(ids);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void cmbCustomerOnAction(ActionEvent actionEvent) {
        String id = cmbCustomerId.getValue();

        if (id == null) {
            lblCustomerName.setText("");
            return;
        }

        try {
            CustomerDTO customer = customerModel.search(Integer.parseInt(id));
            if (customer != null) {
                lblCustomerName.setText(customer.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void cmbMedicineOnAction(ActionEvent actionEvent) {
        String id = cmbMedicineId.getValue();

        if (id == null) {
            return;
        }

        try {
            MedicineDTO medicine = medicineModel.search(Integer.parseInt(id));
            if (medicine != null) {
                selectedMedicine = medicine;

                txtDescription.setText(medicine.getMedName());
                lblUnitPrice.setText(String.valueOf(medicine.getUnitPrice()));
                lblQtyOnHand.setText(String.valueOf(medicine.getQtyInStock()));
                txtQty.requestFocus();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void btnAddToCartOnAction(ActionEvent actionEvent) {
        // med select chek krnw
        String medId = cmbMedicineId.getValue();
        if (medId == null || selectedMedicine == null) {  // Check selectedMedicine too
            new Alert(Alert.AlertType.WARNING, "Please select a medicine first!").show();
            return;
        }

        // exp date validate and check
        try {
            // Convert DTO Date to LocalDate
            java.time.LocalDate expDate = new java.sql.Date(selectedMedicine.getExpDate().getTime()).toLocalDate();
            java.time.LocalDate today = java.time.LocalDate.now();

            // Is it already expired?
            if (expDate.isBefore(today)) {
                new Alert(Alert.AlertType.ERROR,
                        "Cannot Sell! This medicine EXPIRED on: " + expDate).show();
                return; // Stop here
            }

            //Is it expiring soon.
            if (expDate.isBefore(today.plusDays(21))) {
                new Alert(Alert.AlertType.WARNING,
                        "Warning! Medicine expires soon (" + expDate + "). Cannot add to bill.").show();
                return;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return;
        }


        // qty enter krnld blnw
        String qtyText = txtQty.getText().trim();
        if (qtyText.isEmpty() || !qtyText.matches("\\d+") || Integer.parseInt(qtyText) <= 0) {
            new Alert(Alert.AlertType.ERROR, "Invalid Quantity! Please enter a valid number.").show();
            return;
        }


        try {
            String desc = txtDescription.getText();
            double unitPrice = Double.parseDouble(lblUnitPrice.getText());
            int qtyOnHand = Integer.parseInt(lblQtyOnHand.getText());
            int qty = Integer.parseInt(qtyText);

            // stock check
            if (qty > qtyOnHand) {
                new Alert(Alert.AlertType.ERROR, "Out of Stock! You only have " + qtyOnHand + " items.").show();
                return;
            }

            // add to my cart
            Button btnRemove = new Button("Remove");
            btnRemove.setStyle("-fx-background-color: #ef4444; -fx-text-fill: white; -fx-cursor: hand;");

            // total ek gnnw
            double total = qty * unitPrice;

            // item already cart eke tyd blnw
            CartTM existingItem = null;
            for (CartTM tm : cartList) {
                if (tm.getMedicineId().equals(medId)) {
                    existingItem = tm;
                    break;
                }
            }

            if (existingItem != null) {
                int newQty = existingItem.getQty() + qty;
                if (newQty > qtyOnHand) {
                    new Alert(Alert.AlertType.ERROR, "Not enough stock to add more!").show();
                    return;
                }
                existingItem.setQty(newQty);
                existingItem.setTotal(newQty * unitPrice);
                tblOrderCart.refresh();
            } else {
                CartTM newTm = new CartTM(medId, desc, qty, unitPrice, total, btnRemove);

                btnRemove.setOnAction((e) -> {
                    cartList.remove(newTm);
                    calculateNetTotal();
                });

                cartList.add(newTm);
            }

            calculateNetTotal();
            txtQty.clear();

        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Error reading medicine data. Please re-select the medicine.").show();
            e.printStackTrace();
        }
    }

    private void calculateNetTotal() {
        netTotal = 0;
        for (CartTM tm : cartList) {
            netTotal += tm.getTotal();
        }
        lblNetTotal.setText(String.valueOf(netTotal));
    }

    @FXML
    public void btnPlaceOrderOnAction(ActionEvent actionEvent) {
        String customerId = cmbCustomerId.getValue();

        if (customerId == null || cartList.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please select customer and add items to cart!").show();
            return;
        }

        // Validate Payment Input
        String paymentMethod = cmbPaymentMethod.getValue();
        if (paymentMethod == null || paymentMethod.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please select a Payment Method!").show();
            return;
        }

        //Ensure Cash amount is sufficient.
        if ("Cash".equals(paymentMethod)) {
            try {
                double cash = Double.parseDouble(txtCash.getText());
                if (cash < netTotal) {
                    new Alert(Alert.AlertType.ERROR, "Insufficient Cash provided!").show();
                    return;
                }
            } catch (NumberFormatException e) {
                new Alert(Alert.AlertType.ERROR, "Invalid Cash Amount!").show();
                return;
            }
        }


        OrderDTO order = new OrderDTO(null, customerId, "1", netTotal, new Date());
        List<CartTM> cartData = new ArrayList<>(cartList);

        double cashAmount = 0.0;
        if ("Cash".equals(paymentMethod)) {
            try {
                cashAmount = Double.parseDouble(txtCash.getText());
            } catch (NumberFormatException e) {
                cashAmount = netTotal;
            }
        } else {
            cashAmount = netTotal;
        }


        try {
            String orderId = orderModel.placeOrder(order, cartData, paymentMethod, cashAmount);
            // boolean isPlaced = orderModel.placeOrder(order, cartData);

            if (orderId != null) {
                new Alert(Alert.AlertType.INFORMATION, "Order Placed Successfully!").show();

                double balance = 0.0;
                if ("Cash".equals(paymentMethod)) {
                    try {
                        double cash = Double.parseDouble(txtCash.getText());
                        balance = cash - netTotal;
                    } catch (NumberFormatException e) {
                        balance = 0.0;
                    }
                }

                printBill(Integer.parseInt(orderId), balance);

                cartList.clear();
                calculateNetTotal();
                loadNextOrderId();

                cmbMedicineId.getSelectionModel().clearSelection();
                txtDescription.setText("");
                lblQtyOnHand.setText("");
                lblUnitPrice.setText("");
                txtQty.clear();
                txtCash.clear();
                lblBalance.setText("0.00");

                cmbCustomerId.getSelectionModel().clearSelection();
                cmbCustomerId.setValue(null);
                lblCustomerName.setText("");

            } else {
                new Alert(Alert.AlertType.ERROR, "Order Failed!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "SQL Error: " + e.getMessage()).show();
        }
    }

    private void printBill(int orderId, double balance) {
        try {
            InputStream reportStream = getClass().getResourceAsStream("/report/bill.jrxml");

            if (reportStream == null) {
                new Alert(Alert.AlertType.ERROR, "Bill report file not found!").show();
                return;
            }

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("p_order_id", orderId);
            parameters.put("p_balance", balance);

            JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);
            Connection connection = DBConnection.getInstance().getConnection();
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);

            JasperViewer.viewReport(jasperPrint, false);

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error printing bill: " + e.getMessage()).show();
        }
    }

    @FXML
    public void txtSearchMedicineOnAction(ActionEvent actionEvent) {
        String name = txtDescription.getText().trim();

        try {
            MedicineDTO medicine = medicineModel.searchByName(name);

            if (medicine != null) {
                selectedMedicine = medicine;

                cmbMedicineId.setValue(String.valueOf(medicine.getMedicineId()));

                lblUnitPrice.setText(String.valueOf(medicine.getUnitPrice()));
                lblQtyOnHand.setText(String.valueOf(medicine.getQtyInStock()));

                txtQty.requestFocus();
            } else {
                new Alert(Alert.AlertType.WARNING, "Medicine not found!").show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupPaymentLogic() {
        ObservableList<String> methods = FXCollections.observableArrayList("Cash", "Card");
        cmbPaymentMethod.setItems(methods);
        cmbPaymentMethod.setValue("Cash");

        txtCash.textProperty().addListener((observable, oldValue, newValue) -> {
            calculateBalance();
        });

        cmbPaymentMethod.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if ("Card".equals(newValue)) {
                txtCash.setDisable(true);
                lblBalance.setDisable(true);

                txtCash.clear();
                lblBalance.setText("0.00");
            } else {
                txtCash.setDisable(false);
                lblBalance.setDisable(false);

                txtCash.requestFocus();
            }
        });
    }

    private void calculateBalance() {
        try {
            if (txtCash.getText().isEmpty()) {
                lblBalance.setText("0.00");
                return;
            }
            double cash = Double.parseDouble(txtCash.getText());
            double balance = cash - netTotal;

            lblBalance.setText(String.format("%.2f", balance));

            if (balance < 0) {
                lblBalance.setStyle("-fx-text-fill: red; -fx-font-weight: bold; -fx-font-size: 18;");
            } else {
                lblBalance.setStyle("-fx-text-fill: #22c55e; -fx-font-weight: bold; -fx-font-size: 18;");
            }

        } catch (NumberFormatException e) {
            lblBalance.setText("Invalid");
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

    private void setupAutoSuggestion() {
        ContextMenu suggestionsMenu = new ContextMenu();

        txtDescription.textProperty().addListener((observable, oldValue, newValue) -> {
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
                    txtDescription.setText(match);
                    suggestionsMenu.hide();

                    txtSearchMedicineOnAction(null);
                });

                suggestionsMenu.getItems().add(item);
            }

            if (!suggestionsMenu.isShowing()) {
                suggestionsMenu.show(txtDescription, Side.BOTTOM, 0, 0);
            }
        });

        txtDescription.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) suggestionsMenu.hide();
        });
    }

}