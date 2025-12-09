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
}
