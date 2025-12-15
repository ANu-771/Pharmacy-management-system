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
import lk.ijse.pharmacy.App;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private void btnLoginOnAction() throws IOException{

        String realUsername = "isuru";
        String realPassword = "123";

        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.equals(realUsername) && password.equals(realPassword)) {
            System.out.println("Logged - IN Sucessful.!");

            App.setRoot("layout");
        } else {
            System.out.println("Invalid user Name or Password");

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login message..");
            alert.setHeaderText("Invalid user name or password..");
            alert.show();
        }
    }
    @FXML
    public void linkForgotPasswordOnAction(ActionEvent actionEvent) {

    }
}

