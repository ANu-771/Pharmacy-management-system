package lk.ijse.pharmacy.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import lk.ijse.pharmacy.App;

import java.io.IOException;
import java.util.Optional;

public class LayoutController {

    @FXML
    private StackPane contentArea;

    @FXML
    private Button btnCustomer;

    @FXML
    private Button btnDashboard;

    @FXML
    private Button btnMedicine;

    @FXML
    private Button btnOrder;

    @FXML
    private Button btnReport;

    @FXML
    private Button btnSupplier;

    @FXML
    private Button btnUser;

    @FXML
    private Button btnSupply;

    private String userRole = "user";

    public void setRole(String role) {
        this.userRole = role;
    }

    @FXML
    public void initialize() {
        // loadDashboard();
    }

    private void setButtonActive(Button activeButton) {
        Button[] buttons = {btnDashboard, btnCustomer, btnMedicine, btnSupplier, btnSupply, btnOrder, btnReport, btnUser};

        for (Button btn : buttons) {
            btn.getStyleClass().remove("nav-btn-active");
            if (!btn.getStyleClass().contains("nav-btn")) {
                btn.getStyleClass().add("nav-btn");
            }
        }

        activeButton.getStyleClass().add("nav-btn-active");
    }

    @FXML
    private void loadDashboard() {
        setButtonActive(btnDashboard);

        try {
            contentArea.getChildren().clear();
            contentArea.getChildren().setAll(App.loadFXML("dashboard"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void loadCustomer() {
        setButtonActive(btnCustomer);

        try {
            contentArea.getChildren().clear();
            contentArea.getChildren().setAll(App.loadFXML("customer"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void loadMedicine() {
        setButtonActive(btnMedicine);

        try {
            contentArea.getChildren().clear();
            contentArea.getChildren().setAll(App.loadFXML("medicine"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void loadSupplier() {
        setButtonActive(btnSupplier);

        try {
            contentArea.getChildren().clear();
            contentArea.getChildren().setAll(App.loadFXML("supplier"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void loadSupply() {
        setButtonActive(btnSupply);

        try {
            contentArea.getChildren().clear();
            contentArea.getChildren().setAll(App.loadFXML("supply"));
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load Supply Form!").show();
        }
    }

    @FXML
    private void loadOrder() {
        setButtonActive(btnOrder);

        try {
            contentArea.getChildren().clear();
            contentArea.getChildren().setAll(App.loadFXML("order"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void loadReport() {
        if (!"admin".equalsIgnoreCase(userRole)) {
            new Alert(Alert.AlertType.WARNING, "Access Denied!\nOnly Admin can view Reports.").show();
            return;
        }

        setButtonActive(btnReport);

        try {
            contentArea.getChildren().clear();
            contentArea.getChildren().setAll(App.loadFXML("report"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void loadUser() {
        if (!"admin".equalsIgnoreCase(userRole)) {
            new Alert(Alert.AlertType.WARNING, "Access Denied!\nOnly Admin can manage Users.").show();
            return;
        }

        setButtonActive(btnUser);

        try {
            contentArea.getChildren().clear();
            contentArea.getChildren().setAll(App.loadFXML("user"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    void handleLogout(ActionEvent event) throws IOException {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("You are about to logout!");
        alert.setContentText("Are you sure you want to return to the login screen?");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            // App.setRoot("login");
            Parent root = FXMLLoader.load(getClass().getResource("/view/login.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);
        }
    }
}