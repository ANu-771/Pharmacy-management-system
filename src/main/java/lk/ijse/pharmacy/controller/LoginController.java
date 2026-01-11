package lk.ijse.pharmacy.controller;

import javafx.event.ActionEvent;

import java.io.IOException;
import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import lk.ijse.pharmacy.App;
import lk.ijse.pharmacy.model.UserModel;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    private UserModel userModel = new UserModel();

    @FXML
    private void btnLoginOnAction() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        // UI Styles
        String defaultStyle = "-fx-border-color: #e2e8f0; -fx-border-radius: 5; -fx-background-color: #f8fafc;";
        String errorStyle = "-fx-border-color: red; -fx-border-width: 2px; -fx-border-radius: 5; -fx-background-color: #fff0f0;";

        usernameField.setStyle(defaultStyle);
        passwordField.setStyle(defaultStyle);

        if (username.isEmpty()) {
            usernameField.setStyle(errorStyle);
            usernameField.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            passwordField.setStyle(errorStyle);
            passwordField.requestFocus();
            return;
        }

        try {
            boolean isUserFound = userModel.isUsernameExists(username);

            if (!isUserFound) {
                usernameField.setStyle(errorStyle);
                usernameField.requestFocus();
                new Alert(Alert.AlertType.ERROR, "Username not found!").show();
                return;
            }

            //Check Password
            String role = userModel.checkLogin(username, password);

            if (role != null) {
                System.out.println("Login Successful! Role: " + role);
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/layout.fxml"));
                Parent root = loader.load();

                LayoutController layoutController = loader.getController();
                layoutController.setRole(role);

                Stage stage = (Stage) usernameField.getScene().getWindow();
                stage.getScene().setRoot(root);
                stage.centerOnScreen();
                stage.show();

            } else {
                passwordField.setStyle(errorStyle);
                passwordField.requestFocus();
                new Alert(Alert.AlertType.ERROR, "Incorrect Password!").show();
            }

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Database Error: " + e.getMessage()).show();
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void linkForgotPasswordOnAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/forgot_password.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Forgot Password");
            stage.centerOnScreen();
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Could not load Forgot Password form.").show();
        }
    }
}