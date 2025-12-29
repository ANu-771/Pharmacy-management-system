package lk.ijse.pharmacy.controller;

import javafx.event.ActionEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import lk.ijse.pharmacy.dbconnection.DBConnection;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class ReportController {

    @FXML
    private ComboBox<String> cmbReportType;

    @FXML
    private DatePicker dpFromDate;

    @FXML
    private DatePicker dpToDate;

    @FXML
    public void initialize() {
        // 1. Initialize the ComboBox with Report Options
        ObservableList<String> reportTypes = FXCollections.observableArrayList(
                "Customer Report",
                "Stock Report",
                "Bill (Sales) Report"
        );
        cmbReportType.setItems(reportTypes);
    }

    @FXML
    void btnPrintOnAction(ActionEvent event) {
        String selectedReport = cmbReportType.getValue();

        if (selectedReport == null) {
            new Alert(Alert.AlertType.WARNING, "Please select a report type first!").show();
            return;
        }

        switch (selectedReport) {
            case "Customer Report":
                viewReport("Customer.jrxml", null);
                break;

            case "Stock Report":
                viewReport("stock_report.jrxml", null);
                break;

            case "Bill (Sales) Report":
                // Check if dates are selected for the Bill report
                if (dpFromDate.getValue() == null || dpToDate.getValue() == null) {
                    new Alert(Alert.AlertType.WARNING, "Please select From and To dates for the Bill Report!").show();
                    return;
                }

                Map<String, Object> parameters = new HashMap<>();
                parameters.put("fromDate", Date.valueOf(dpFromDate.getValue()));
                parameters.put("toDate", Date.valueOf(dpToDate.getValue()));

                viewReport("bill_report.jrxml", parameters);
                break;

            default:
                new Alert(Alert.AlertType.ERROR, "Invalid Selection").show();
        }
    }

    // Helper method to load and view the Jasper Report
    private void viewReport(String fileName, Map<String, Object> parameters) {
        try {
            // 1. Load the report file (ensure these files are in src/main/resources/report/)
            InputStream reportStream = getClass().getResourceAsStream("/report/" + fileName);

            if (reportStream == null) {
                new Alert(Alert.AlertType.ERROR, "Report file not found: " + fileName).show();
                return;
            }

            // 2. Connect to Database
            Connection connection = DBConnection.getInstance().getConnection();

            // 3. Compile the Report
            JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

            // 4. Fill the Report with Data (and Parameters)
            JasperPrint jasperPrint = JasperFillManager.fillReport(
                    jasperReport,
                    parameters,
                    connection
            );

            // 5. View the Report
            JasperViewer.viewReport(jasperPrint, false); // false = don't close the app when closing report

        } catch (JRException | SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error generating report: " + e.getMessage()).show();
        }
    }

    @FXML
    void btnGenerateOnAction(ActionEvent event) {
        // This button is for the TableView preview (Optional - can be implemented similarly)
        new Alert(Alert.AlertType.INFORMATION, "Click 'Print / PDF' to view the full Jasper Report.").show();
    }
}