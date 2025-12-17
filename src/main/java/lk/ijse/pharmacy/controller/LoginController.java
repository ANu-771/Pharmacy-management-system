/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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

//    @FXML
//    private void btnLoginOnAction() throws IOException{
//
//        String realUsername = "isuru";
//        String realPassword = "123";
//
//        String username = usernameField.getText();
//        String password = passwordField.getText();
//
//        if (username.equals(realUsername) && password.equals(realPassword)) {
//            System.out.println("Logged - IN Sucessful.!");
//
//            App.setRoot("layout");
//        } else {
//            System.out.println("Invalid user Name or Password");
//
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setTitle("Login message..");
//            alert.setHeaderText("Invalid user name or password..");
//            alert.show();
//        }
//    }

    @FXML
    private void btnLoginOnAction() throws IOException {
        String realUsername = "isuru";
        String realPassword = "123";

        String username = usernameField.getText();
        String password = passwordField.getText();

        // 1. Define your styles
        // The style for a normal field (matches your FXML)
        String defaultStyle = "-fx-border-color: #e2e8f0; -fx-border-radius: 5; -fx-background-color: #f8fafc;";
        // The style for an error field (Red border)
        String errorStyle = "-fx-border-color: red; -fx-border-width: 2px; -fx-border-radius: 5; -fx-background-color: #fff0f0;";

        // 2. Reset styles (Clear previous errors)
        usernameField.setStyle(defaultStyle);
        passwordField.setStyle(defaultStyle);

        // 3. Validation Logic
        boolean isError = false;

        // Check Username
        if (!username.equals(realUsername)) {
            usernameField.setStyle(errorStyle); // Turn Username Red
            usernameField.requestFocus(); // Put cursor back here
            isError = true;
        }
        // Check Password (only checks if username was correct, or you can check both)
        else if (!password.equals(realPassword)) {
            passwordField.setStyle(errorStyle); // Turn Password Red
            passwordField.requestFocus(); // Put cursor back here
            isError = true;
        }

        // 4. Final Decision
        if (isError) {
            System.out.println("Invalid User Name or Password");
            // No Alert created here, just the red styles above
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

