module lk.ijse.pharmacy {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;

    requires org.controlsfx.controls;
    requires java.sql;
    requires java.desktop;
    requires net.sf.jasperreports.core;
    requires java.mail;


    opens lk.ijse.pharmacy to javafx.fxml;
    exports lk.ijse.pharmacy;
    opens lk.ijse.pharmacy.controller to javafx.fxml;
    exports lk.ijse.pharmacy.controller;
    opens lk.ijse.pharmacy.dto to java.base;
    exports lk.ijse.pharmacy.dto;
    opens lk.ijse.pharmacy.dto.tm to javafx.base;
    exports lk.ijse.pharmacy.dto.tm;
    exports lk.ijse.pharmacy.util;

}