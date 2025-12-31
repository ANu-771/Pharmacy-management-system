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
import lk.ijse.pharmacy.model.UserModel; // Import the Model

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    // Create an instance of the Model to handle DB operations
    private UserModel userModel = new UserModel();

    @FXML
    private void btnLoginOnAction() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // UI Styles
        String defaultStyle = "-fx-border-color: #e2e8f0; -fx-border-radius: 5; -fx-background-color: #f8fafc;";
        String errorStyle = "-fx-border-color: red; -fx-border-width: 2px; -fx-border-radius: 5; -fx-background-color: #fff0f0;";

        // Reset styles initially
        usernameField.setStyle(defaultStyle);
        passwordField.setStyle(defaultStyle);

        // Basic Validation: Check if empty
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
            // --- LOGIC CHANGE HERE ---

            // Step A: Check if Username exists ONLY
            boolean isUserFound = userModel.isUsernameExists(username);

            if (!isUserFound) {
                // Username is WRONG -> Red Username Field ONLY
                usernameField.setStyle(errorStyle);
                usernameField.requestFocus();
                new Alert(Alert.AlertType.ERROR, "Username not found!").show();
                return; // Stop here
            }

            // Step B: Check Password (Login)
            String role = userModel.checkLogin(username, password);

            if (role != null) {
                // SUCCESS
                System.out.println("Login Successful! Role: " + role);
                // 1. Load the FXML manually
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/layout.fxml"));
                Parent root = loader.load();

                // 2. Get the Controller and PASS THE ROLE
                LayoutController layoutController = loader.getController();
                layoutController.setRole(role); // <--- THIS IS THE MISSING KEY!

                // 3. Show the new screen
                Stage stage = (Stage) usernameField.getScene().getWindow();
                stage.getScene().setRoot(root);
                stage.centerOnScreen();
                stage.show();
            } else {
                // User exists (Step A passed), so Password must be WRONG
                // Red Password Field ONLY
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