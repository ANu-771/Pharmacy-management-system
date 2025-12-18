package lk.ijse.pharmacy.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
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
    public void initialize() {
       // loadDashboard();
    }

    // 2. Helper Method to Handle Highlighting
    private void setButtonActive(Button activeButton) {
        // Reset all buttons to default style
        Button[] buttons = {btnDashboard, btnCustomer, btnMedicine, btnSupplier, btnOrder, btnReport};

        for (Button btn : buttons) {
            // Remove the active class if it exists
            btn.getStyleClass().remove("nav-btn-active");
            // Ensure the normal class is there
            if (!btn.getStyleClass().contains("nav-btn")) {
                btn.getStyleClass().add("nav-btn");
            }
        }

        // Add the active style to the clicked button
        activeButton.getStyleClass().add("nav-btn-active");
    }

    @FXML
    private void loadDashboard(){
        setButtonActive(btnDashboard);

        try {
            contentArea.getChildren().clear();
            contentArea.getChildren().setAll(App.loadFXML("dashboard"));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    private void loadCustomer(){
        setButtonActive(btnCustomer);

        try {
            contentArea.getChildren().clear();
            contentArea.getChildren().setAll(App.loadFXML("customer"));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    private void loadMedicine(){
        setButtonActive(btnMedicine);

        try {
            contentArea.getChildren().clear();
            contentArea.getChildren().setAll(App.loadFXML("medicine"));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    private void loadSupplier(){
        setButtonActive(btnSupplier);

        try {
            contentArea.getChildren().clear();
            contentArea.getChildren().setAll(App.loadFXML("supplier"));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    private void loadOrder(){
        setButtonActive(btnOrder);

        try {
            contentArea.getChildren().clear();
            contentArea.getChildren().setAll(App.loadFXML("order"));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    private void loadReport(){
        setButtonActive(btnReport);

        try {
            contentArea.getChildren().clear();
            contentArea.getChildren().setAll(App.loadFXML("report"));
        }catch (Exception e){
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
            App.setRoot("login");
        }
    }

}
