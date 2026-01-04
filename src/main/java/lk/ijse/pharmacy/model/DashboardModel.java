package lk.ijse.pharmacy.model;

import lk.ijse.pharmacy.dbconnection.DBConnection;
import lk.ijse.pharmacy.dto.MedicineDTO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashboardModel {

    public int getTotalMedicineCount() throws SQLException{
        String sql = "SELECT COUNT(*) FROM medicine";
        ResultSet resultSet = DBConnection.getInstance().getConnection().createStatement().executeQuery(sql);
        if (resultSet.next()) return resultSet.getInt(1);
        return 0;
    }

    public int getActiveCustomerCount() throws SQLException {
        String sql = "SELECT COUNT(*) FROM customer";
        ResultSet resultSet = DBConnection.getInstance().getConnection().createStatement().executeQuery(sql);
        if (resultSet.next()) return resultSet.getInt(1);
        return 0;
    }

    public double getTodayIncome() throws SQLException {
        String sql = "SELECT SUM(total) FROM orders WHERE order_date = CURRENT_DATE";
        ResultSet resultSet = DBConnection.getInstance().getConnection().createStatement().executeQuery(sql);
        if (resultSet.next()) return resultSet.getDouble(1);
        return 0.0;
    }

    public Map<String, Double> getIncomeTrends() throws SQLException {
        Map<String, Double> data = new HashMap<>();
        String sql = "SELECT order_date, SUM(total) FROM orders GROUP BY order_date ORDER BY order_date DESC LIMIT 7";
        ResultSet resultSet = DBConnection.getInstance().getConnection().createStatement().executeQuery(sql);
        while (resultSet.next()) {
            data.put(resultSet.getString(1), resultSet.getDouble(2));
        }
        return data;
    }

    public List<MedicineDTO> getExpiringMedicines() throws SQLException {
        List<MedicineDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM medicine WHERE exp_date BETWEEN CURRENT_DATE AND DATE_ADD(CURRENT_DATE, INTERVAL 30 DAY)";
        ResultSet resultSet = DBConnection.getInstance().getConnection().createStatement().executeQuery(sql);

        while (resultSet.next()) {
            list.add(new MedicineDTO(
                    resultSet.getInt("medicine_id"),
                    resultSet.getString("med_name"),
                    resultSet.getString("brand"),
                    resultSet.getInt("qty_in_stock"),
                    resultSet.getDouble("unit_price"),
                    resultSet.getDate("exp_date")
            ));
        }
        return list;
    }
}