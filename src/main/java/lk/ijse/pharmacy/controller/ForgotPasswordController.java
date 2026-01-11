package lk.ijse.pharmacy.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lk.ijse.pharmacy.dto.UserDTO;
import lk.ijse.pharmacy.model.UserModel;
import lk.ijse.pharmacy.util.EmailService;

import java.sql.SQLException;

public class ForgotPasswordController {

    @FXML
    private TextField txtEmail;

    @FXML
    private Button btnSend;

    @FXML
    private Button btnCancel;

    private UserModel userModel = new UserModel();

    @FXML
    void btnSendOnAction(ActionEvent event) {
        String email = txtEmail.getText().trim();

        String defaultStyle = "-fx-border-color: #e2e8f0; -fx-border-radius: 5; -fx-background-color: #f8fafc;";
        String errorStyle = "-fx-border-color: red; -fx-border-width: 2px; -fx-border-radius: 5; -fx-background-color: #fff0f0;";

        txtEmail.setStyle(defaultStyle);

        if (email.isEmpty()) {
            txtEmail.setStyle(errorStyle);
            txtEmail.requestFocus();
            new Alert(Alert.AlertType.WARNING, "Please enter your email address.").show();
            return;
        }

        try {
            UserDTO user = userModel.getUserByEmail(email);

            if (user == null) {
                txtEmail.setStyle(errorStyle);
                txtEmail.requestFocus();
                new Alert(Alert.AlertType.ERROR, "This email is not registered in our system.").show();
                return;
            }

            btnSend.setText("Sending...");
            btnSend.setDisable(true);

            new Thread(() -> {
                String subject = "KK Pharmacy - Login Credentials Recovery";
                String htmlBody =
                        "<div style=\"font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; border: 1px solid #cbd5e1; border-radius: 10px; overflow: hidden;\">" +

                                "<div style=\"background-color: #1e3a8a; padding: 20px; text-align: center;\">" +
                                "<h1 style=\"color: #ffffff; margin: 0; font-size: 24px; font-weight: bold;\">KK PHARMACY</h1>" +
                                "<p style=\"color: #cbd5e1; margin: 5px 0 0;\">System Credential Recovery</p>" +
                                "</div>" +

                                "<div style=\"padding: 30px; background-color: #ffffff;\">" +
                                "<p style=\"font-size: 16px; color: #333;\">Hello <b>" + user.getUsername() + "</b>,</p>" +
                                "<p style=\"font-size: 16px; color: #555;\">We received a request to recover your login details. Here are your credentials:</p>" +

                                "<div style=\"background-color: #f1f5f9; border-left: 5px solid #3b82f6; padding: 15px; margin: 20px 0;\">" +
                                "<p style=\"margin: 5px 0; font-size: 16px;\"><strong>👤 Username:</strong> " + user.getUsername() + "</p>" +
                                "<p style=\"margin: 5px 0; font-size: 16px;\"><strong>🔑 Password:</strong> <span style=\"color: #d32f2f; font-weight: bold;\">" + user.getPassword() + "</span></p>" +
                                "</div>" +

                                "<p style=\"color: #777; font-size: 14px;\">Please keep these details safe. If you did not request this, contact the admin immediately.</p>" +
                                "</div>" +

                                "<div style=\"background-color: #f8fafc; padding: 15px; text-align: center; border-top: 1px solid #e2e8f0;\">" +
                                "<p style=\"color: #94a3b8; font-size: 12px; margin: 0;\">© 2026 KK Pharmacy Management System</p>" +
                                "</div>" +
                                "</div>";

                boolean isSent = EmailService.sendEmail(email, subject, htmlBody);
                Platform.runLater(() -> {
                    if (isSent) {
                        new Alert(Alert.AlertType.INFORMATION, "Credentials sent to your email successfully!").show();
                        closeWindow();
                    } else {
                        new Alert(Alert.AlertType.ERROR, "Failed to send email. Check your internet connection.").show();
                        btnSend.setText("Send Credentials");
                        btnSend.setDisable(false);
                    }
                });
            }).start();

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Database Error: " + e.getMessage()).show();
            e.printStackTrace();
        }
    }

    @FXML
    void btnCancelOnAction(ActionEvent event) {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) btnSend.getScene().getWindow();
        stage.close();
    }
}