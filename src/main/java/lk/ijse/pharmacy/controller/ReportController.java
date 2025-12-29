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
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    // --- Table Injection ---
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

    @FXML
    public void initialize() {
        // 1. Initialize ComboBox (Removed "Bill (Sales) Report")
        ObservableList<String> reportTypes = FXCollections.observableArrayList(
                "Customer Report",
                "Supplier Report",
                "Stock Report"
        );
        cmbReportType.setItems(reportTypes);

        // 2. Setup Table Columns
        colOrderId.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        colCustomer.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));

        // 3. Load Data
        loadSummaryLabels();
        loadTableData();
    }

    private void loadSummaryLabels() {
        try {
            // Existing: Today's Income
            double todayIncome = dashboardModel.getTodayIncome();
            lblTotalSales.setText(String.format("Rs. %.2f", todayIncome));

            // NEW: Total Orders Count
            int totalOrders = getTotalOrders();
            lblTotalOrders.setText(String.valueOf(totalOrders));

            // NEW: Total Items Sold
            int itemsSold = getItemsSold();
            lblItemsSold.setText(String.valueOf(itemsSold));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // --- NEW METHOD: Get Total Orders Count ---
    private int getTotalOrders() throws SQLException {
        String sql = "SELECT COUNT(*) FROM orders";
        Connection connection = DBConnection.getInstance().getConnection();
        ResultSet resultSet = connection.createStatement().executeQuery(sql);
        if (resultSet.next()) {
            return resultSet.getInt(1);
        }
        return 0;
    }

    // --- NEW METHOD: Get Total Items Sold ---
    private int getItemsSold() throws SQLException {
        String sql = "SELECT SUM(qty) FROM order_medicine";
        Connection connection = DBConnection.getInstance().getConnection();
        ResultSet resultSet = connection.createStatement().executeQuery(sql);
        if (resultSet.next()) {
            return resultSet.getInt(1);
        }
        return 0;
    }

    private void loadTableData() {
        ObservableList<ReportTM> list = FXCollections.observableArrayList();

        // Sorted by Date DESC and Order ID DESC
        String sql = "SELECT o.order_id, c.name, o.order_date, o.total " +
                "FROM orders o " +
                "JOIN customer c ON o.customer_id = c.customer_id " +
                "ORDER BY o.order_date DESC, o.order_id DESC";

        try {
            Connection connection = DBConnection.getInstance().getConnection();
            ResultSet resultSet = connection.createStatement().executeQuery(sql);

            while (resultSet.next()) {
                list.add(new ReportTM(
                        resultSet.getString("order_id"),
                        resultSet.getString("name"),
                        resultSet.getString("order_date"),
                        resultSet.getDouble("total")
                ));
            }

            tblReportDetails.setItems(list);

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

            // Removed "Bill (Sales) Report" case

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

    // --- Inner Class for Table Model (TM) ---
    public static class ReportTM {
        private String orderId;
        private String customerName;
        private String date;
        private Double total;

        public ReportTM(String orderId, String customerName, String date, Double total) {
            this.orderId = orderId;
            this.customerName = customerName;
            this.date = date;
            this.total = total;
        }

        public String getOrderId() { return orderId; }
        public String getCustomerName() { return customerName; }
        public String getDate() { return date; }
        public Double getTotal() { return total; }
    }
}