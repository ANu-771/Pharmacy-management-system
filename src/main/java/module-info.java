module lk.ijse.pharmacy2 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens lk.ijse.pharmacy2 to javafx.fxml;
    exports lk.ijse.pharmacy2;
}