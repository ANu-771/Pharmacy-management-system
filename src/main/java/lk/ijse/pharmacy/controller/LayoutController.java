package lk.ijse.pharmacy.controller;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lk.ijse.pharmacy.App;

public class LayoutController {

    @FXML
    public void initialize() {

    }

    private void loadDashboard(){
        try {
            Stage stage = new Stage();
            stage.setTitle("Dashboard");
            stage.setResizable(true);
            Scene scene = new Scene(App.loadFXML("dashboard"),1280,720);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
