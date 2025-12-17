package lk.ijse.pharmacy.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import lk.ijse.pharmacy.App;

import java.io.IOException;
import java.util.Optional;

public class LayoutController {

    @FXML private StackPane contentArea;

    @FXML
    public void initialize() {

    }

    @FXML
    private void loadDashboard(){
        try {
            contentArea.getChildren().clear();
            contentArea.getChildren().setAll(App.loadFXML("dashboard"));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    private void loadCustomer(){
        try {
            contentArea.getChildren().clear();
            contentArea.getChildren().setAll(App.loadFXML("customer"));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    private void loadMedicine(){
        try {
            contentArea.getChildren().clear();
            contentArea.getChildren().setAll(App.loadFXML("medicine"));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    private void loadSupplier(){
        try {
            contentArea.getChildren().clear();
            contentArea.getChildren().setAll(App.loadFXML("supplier"));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    private void loadOrder(){
        try {
            contentArea.getChildren().clear();
            contentArea.getChildren().setAll(App.loadFXML("order"));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    private void loadReport(){
        try {
            contentArea.getChildren().clear();
            contentArea.getChildren().setAll(App.loadFXML("report"));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    // ///

    @FXML
    void handleLogout(ActionEvent event) throws IOException {
        // 1. Show a confirmation alert
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("You are about to logout!");
        alert.setContentText("Are you sure you want to return to the login screen?");

        // 2. Capture the user's response
        Optional<ButtonType> result = alert.showAndWait();

        // 3. If OK is clicked, go back to login
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Assuming your login fxml is named 'login' or 'Login'
            // Ensure this matches the string you use to load the login view
            App.setRoot("login");
        }
    }

}
