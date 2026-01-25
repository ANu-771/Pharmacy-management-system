package lk.ijse.pharmacy.util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.Optional;

public class AlertUtil {

    public static boolean showConfirmation(String title, String content, String styleClass) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(title);
        alert.setContentText(content);

        DialogPane dialogPane = alert.getDialogPane();
        try {
            dialogPane.getStylesheets().add(AlertUtil.class.getResource("/view/style.css").toExternalForm());
        } catch (Exception e) {
            System.out.println("CSS file not found. Check path: /view/style.css");
        }
        dialogPane.getStyleClass().add(styleClass);

        ImageView icon = new ImageView();
        String imagePath = null;

        if ("delete-alert".equals(styleClass)) {
            imagePath = "/image/waste-management.png";
        } else if ("update-alert".equals(styleClass)) {
            imagePath = "/image/update.png";
        }

        if (imagePath != null) {
            try {
                Image img = new Image(AlertUtil.class.getResourceAsStream(imagePath));
                icon.setImage(img);
                icon.setFitHeight(48);
                icon.setFitWidth(48);
                alert.setGraphic(icon);
            } catch (Exception e) {
                // If icon fails, just ignore it
            }
        }

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }
}