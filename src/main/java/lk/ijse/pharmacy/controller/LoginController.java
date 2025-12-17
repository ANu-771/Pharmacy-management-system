package lk.ijse.pharmacy.controller;

import javafx.event.ActionEvent;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lk.ijse.pharmacy.App;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private void btnLoginOnAction() throws IOException {
        String realUsername = "isuru";
        String realPassword = "123";

        String username = usernameField.getText();
        String password = passwordField.getText();

        String defaultStyle = "-fx-border-color: #e2e8f0; -fx-border-radius: 5; -fx-background-color: #f8fafc;";
        // Red border
        String errorStyle = "-fx-border-color: red; -fx-border-width: 2px; -fx-border-radius: 5; -fx-background-color: #fff0f0;";

        usernameField.setStyle(defaultStyle);
        passwordField.setStyle(defaultStyle);

        boolean isError = false;

        if (!username.equals(realUsername)) {
            usernameField.setStyle(errorStyle);
            usernameField.requestFocus();
            isError = true;
        }

        else if (!password.equals(realPassword)) {
            passwordField.setStyle(errorStyle);
            passwordField.requestFocus();
            isError = true;
        }

        if (isError) {
            System.out.println("Invalid User Name or Password");
        } else {
            System.out.println("Logged - IN Successful.!");
            App.setRoot("layout");
        }
    }

    @FXML
    void linkForgotPasswordOnAction(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Password Reset");
        alert.setHeaderText("Contact Administrator");
        alert.setContentText("Please contact the Shop Owner or the Software Company Administrator to reset your password.");

        try {

            Image image = new Image(getClass().getResourceAsStream("/image/pw locked.png"));

            ImageView imageView = new ImageView(image);

            imageView.setFitHeight(50);
            imageView.setFitWidth(50);

            alert.setGraphic(imageView);


        } catch (Exception e) {
            System.out.println("Icon image not found: " + e.getMessage());
        }

        alert.showAndWait();
    }

}

