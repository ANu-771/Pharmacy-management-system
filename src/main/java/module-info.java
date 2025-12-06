module lk.ijse.pharmacy {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;

    requires org.controlsfx.controls;
    requires java.sql;


    opens lk.ijse.pharmacy to javafx.fxml;
    exports lk.ijse.pharmacy;
    opens lk.ijse.pharmacy.controller to javafx.fxml;
    exports lk.ijse.pharmacy.controller;

}