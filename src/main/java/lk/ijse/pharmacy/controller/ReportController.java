package lk.ijse.pharmacy.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.pharmacy.dbconnection.DBConnection;
import lk.ijse.pharmacy.model.DashboardModel;
import lk.ijse.pharmacy.dto.tm.ReportTM;
import lk.ijse.pharmacy.model.ReportModel;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportController {

    @FXML
    private ComboBox<String> cmbReportType;

    @FXML
    private Label lblTotalSales;

    @FXML
    private Label lblTotalOrders;

    @FXML
    private Label lblItemsSold;

    @FXML
    private TableView<ReportTM> tblReportDetails;

    @FXML
    private TableColumn<ReportTM, String> colOrderId;

    @FXML
    private TableColumn<ReportTM, String> colCustomer;

    @FXML
    private TableColumn<ReportTM, String> colDate;

    @FXML
    private TableColumn<ReportTM, Double> colTotal;

    private DashboardModel dashboardModel = new DashboardModel();
    private ReportModel reportModel = new ReportModel();

    @FXML
    public void initialize() {
        ObservableList<String> reportTypes = FXCollections.observableArrayList(
                "Customer Report",
                "Supplier Report",
                "Stock Report"
        );
        cmbReportType.setItems(reportTypes);

        colOrderId.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        colCustomer.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));

        loadSummaryLabels();
        loadTableData();

        // double click show bill from table
        tblReportDetails.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && !tblReportDetails.getSelectionModel().isEmpty()) {
                ReportTM selectedItem = tblReportDetails.getSelectionModel().getSelectedItem();

                int orderId = Integer.parseInt(selectedItem.getOrderId());
                double total = selectedItem.getTotal();

                printBillFromReport(orderId, total);
            }
        });
    }

    private void loadSummaryLabels() {
        try {
            lblTotalSales.setText(String.format("Rs. %.2f", dashboardModel.getTodayIncome()));

            lblTotalOrders.setText(String.valueOf(reportModel.getTotalOrders()));
            lblItemsSold.setText(String.valueOf(reportModel.getItemsSold()));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadTableData() {
        try {
            List<ReportTM> orders = reportModel.getAllOrderDetails();
            tblReportDetails.setItems(FXCollections.observableArrayList(orders));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnGenerateOnAction(ActionEvent event) {
        String selectedReport = cmbReportType.getValue();

        if (selectedReport == null) {
            new Alert(Alert.AlertType.WARNING, "Please select a report type first!").show();
            return;
        }

        switch (selectedReport) {
            case "Customer Report":
                viewReport("customer.jrxml", null);
                break;

            case "Supplier Report":
                viewReport("supplier.jrxml", null);
                break;

            case "Stock Report":
                viewReport("stock.jrxml", null);
                break;

            default:
                new Alert(Alert.AlertType.ERROR, "Invalid Selection").show();
        }
    }

    private void viewReport(String fileName, Map<String, Object> parameters) {
        try {
            InputStream reportStream = getClass().getResourceAsStream("/report/" + fileName);
            if (reportStream == null) {
                new Alert(Alert.AlertType.ERROR, "Report file not found: " + fileName).show();
                return;
            }
            Connection connection = DBConnection.getInstance().getConnection();
            JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);
            JasperViewer.viewReport(jasperPrint, false);
        } catch (JRException | SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error: " + e.getMessage()).show();
        }
    }

    private void printBillFromReport(int orderId, double orderTotal) {
        try {
            double paidAmount = reportModel.getPaidAmount(orderId);
            double balance = paidAmount - orderTotal;

            InputStream reportStream = getClass().getResourceAsStream("/report/bill.jrxml");
            if (reportStream == null) {
                new Alert(Alert.AlertType.ERROR, "Bill report file not found!").show();
                return;
            }

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("p_order_id", orderId);
            parameters.put("p_balance", balance);

            JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);
            Connection connection = DBConnection.getInstance().getConnection();
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);

            JasperViewer.viewReport(jasperPrint, false);

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error printing bill: " + e.getMessage()).show();
        }
    }
}