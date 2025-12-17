package lk.ijse.pharmacy.controller;

import javafx.event.ActionEvent;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.pharmacy.dto.CustomerDTO;
import lk.ijse.pharmacy.dto.MedicineDTO;
import lk.ijse.pharmacy.dto.OrderDTO;
import lk.ijse.pharmacy.dto.tm.CartTM;
import lk.ijse.pharmacy.model.CustomerModel;
import lk.ijse.pharmacy.model.MedicineModel;
import lk.ijse.pharmacy.model.OrderModel;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
    private Label lblDescription;

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

    private OrderModel orderModel = new OrderModel();
    private CustomerModel customerModel = new CustomerModel();
    private MedicineModel medicineModel = new MedicineModel();

    private ObservableList<CartTM> cartList = FXCollections.observableArrayList();
    private double netTotal = 0;

    @FXML
    public void initialize() {
        setCellValueFactory();
        loadNextOrderId();
        loadCustomerIds();
        loadMedicineIds();
        lblOrderDate.setText(String.valueOf(LocalDate.now()));
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
        try {
            MedicineDTO medicine = medicineModel.search(Integer.parseInt(id));
            if (medicine != null) {
                lblDescription.setText(medicine.getMedName());
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
        // 1. Check if a Medicine is selected
        String medId = cmbMedicineId.getValue();
        if (medId == null) {
            new Alert(Alert.AlertType.WARNING, "Please select a medicine first!").show();
            return;
        }

        // 2. Check if Qty is entered
        String qtyText = txtQty.getText().trim();
        if (qtyText.isEmpty() || !qtyText.matches("\\d+") || Integer.parseInt(qtyText) <= 0) {
            new Alert(Alert.AlertType.ERROR, "Invalid Quantity! Please enter a valid number.").show();
            return;
        }

        // 3. Parse Data (Safely)
        try {
            String desc = lblDescription.getText();
            double unitPrice = Double.parseDouble(lblUnitPrice.getText());
            int qtyOnHand = Integer.parseInt(lblQtyOnHand.getText());
            int qty = Integer.parseInt(qtyText);

            // 4. Check Stock
            if (qty > qtyOnHand) {
                new Alert(Alert.AlertType.ERROR, "Out of Stock! You only have " + qtyOnHand + " items.").show();
                return;
            }

            // 5. Add to Cart Logic
            Button btnRemove = new Button("Remove");
            btnRemove.setStyle("-fx-background-color: #ef4444; -fx-text-fill: white; -fx-cursor: hand;");

            // Calculate Total
            double total = qty * unitPrice;

            // Check if item already exists in cart
            CartTM existingItem = null;
            for (CartTM tm : cartList) {
                if (tm.getMedicineId().equals(medId)) {
                    existingItem = tm;
                    break;
                }
            }

            if (existingItem != null) {
                // Update existing row
                int newQty = existingItem.getQty() + qty;
                if(newQty > qtyOnHand) {
                    new Alert(Alert.AlertType.ERROR, "Not enough stock to add more!").show();
                    return;
                }
                existingItem.setQty(newQty);
                existingItem.setTotal(newQty * unitPrice);
                tblOrderCart.refresh();
            } else {
                // Add new row
                CartTM newTm = new CartTM(medId, desc, qty, unitPrice, total, btnRemove);

                // Set Remove Button Action
                btnRemove.setOnAction((e) -> {
                    cartList.remove(newTm);
                    calculateNetTotal();
                });

                cartList.add(newTm);
            }

            calculateNetTotal();
            txtQty.clear();

        } catch (NumberFormatException e) {
            // This happens if labels are empty or have invalid text
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

        // Create Order Object
        // Note: Order ID is auto-generated in DB, pass whatever or null
        // User ID is hardcoded to "1" or get from login session
        OrderDTO order = new OrderDTO(null, customerId, "1", netTotal, new Date());

        // Convert ObservableList to normal List for Model
        List<CartTM> cartData = new ArrayList<>(cartList);

        try {
            boolean isPlaced = orderModel.placeOrder(order, cartData);
            if (isPlaced) {
                new Alert(Alert.AlertType.INFORMATION, "Order Placed Successfully!").show();
                cartList.clear();
                calculateNetTotal();
                loadNextOrderId(); // Refresh ID

                // Refresh Medicine fields as stock has changed
                cmbMedicineId.getSelectionModel().clearSelection();
                lblDescription.setText("");
                lblQtyOnHand.setText("");
                lblUnitPrice.setText("");
            } else {
                new Alert(Alert.AlertType.ERROR, "Order Failed!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "SQL Error: " + e.getMessage()).show();
        }
    }
}