package lk.ijse.pharmacy.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.pharmacy.dto.MedicineDTO;
import lk.ijse.pharmacy.model.DashboardModel;

import java.util.Map;
import java.util.TreeMap;

public class DashboardController {

    @FXML
    private Label lblTotalMedicines;

    @FXML
    private Label lblActiveCustomers;

    @FXML
    private Label lblTodayIncome;

    @FXML
    private AreaChart<String, Number> chartSales;

    @FXML
    private TableView<MedicineDTO> tblExpiring;

    @FXML
    private TableColumn<MedicineDTO, String> colExpId;

    @FXML
    private TableColumn<MedicineDTO, String> colExpName;

    @FXML
    private TableColumn<MedicineDTO, String> colExpDate;

    @FXML
    private TableColumn<MedicineDTO, Integer> colExpQty;

    private DashboardModel dashboardModel = new DashboardModel();

    @FXML
    public void initialize() {
        loadDashboardCounts();
        loadChart();
        loadExpiringMedicines();
    }

    private void loadDashboardCounts() {
        try {
            int medCount = dashboardModel.getTotalMedicineCount();
            int cusCount = dashboardModel.getActiveCustomerCount();
            double income = dashboardModel.getTodayIncome();

            lblTotalMedicines.setText(String.valueOf(medCount));
            lblActiveCustomers.setText(String.valueOf(cusCount));
            lblTodayIncome.setText(String.format("Rs. %.2f", income));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadChart() {
        try {
            chartSales.getData().clear();
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Daily Sales");

            Map<String, Double> trends = dashboardModel.getIncomeTrends();


            Map<String, Double> sortedTrends = new TreeMap<>(trends);
            //use tree map bz hashmap mix the data tree map give corectly sortded data for chat


            for (Map.Entry<String, Double> entry : sortedTrends.entrySet()) {
                series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
            }

            chartSales.getData().add(series);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadExpiringMedicines() {
        colExpId.setCellValueFactory(new PropertyValueFactory<>("medicineId"));
        colExpName.setCellValueFactory(new PropertyValueFactory<>("medName"));
        colExpDate.setCellValueFactory(new PropertyValueFactory<>("expDate"));
        colExpQty.setCellValueFactory(new PropertyValueFactory<>("qtyInStock"));

        try {
            ObservableList<MedicineDTO> expiringList = FXCollections.observableArrayList(dashboardModel.getExpiringMedicines());
            tblExpiring.setItems(expiringList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

