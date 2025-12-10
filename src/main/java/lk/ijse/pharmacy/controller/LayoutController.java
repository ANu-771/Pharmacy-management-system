package lk.ijse.pharmacy.controller;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import lk.ijse.pharmacy.App;

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
}
