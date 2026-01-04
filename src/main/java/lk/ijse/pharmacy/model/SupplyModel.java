package lk.ijse.pharmacy.model;

import lk.ijse.pharmacy.dbconnection.DBConnection;
import lk.ijse.pharmacy.dto.tm.SupplyRecordTM;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SupplyModel {


    public int getMedicineId(String medName) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();

        String checkSql = "SELECT medicine_id FROM medicine WHERE med_name = ?";
        PreparedStatement checkStmt = connection.prepareStatement(checkSql);
        checkStmt.setString(1, medName);
        ResultSet resultSet = checkStmt.executeQuery();

        if (resultSet.next()) {
            return resultSet.getInt("medicine_id");
        }
        return -1;
    }

    public boolean saveSupply(int supplierId, String medName, LocalDate date, int qty, double unitCost, double totalCost) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();

        int medicineId = getMedicineId(medName);

        if (medicineId == -1) {
            return false;
        }

        String sql = "INSERT INTO supplier_medicine (supplier_id, medicine_id, date, qty, unit_cost, total_cost) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setInt(1, supplierId);
        pstm.setInt(2, medicineId);
        pstm.setDate(3, java.sql.Date.valueOf(date));
        pstm.setInt(4, qty);
        pstm.setDouble(5, unitCost);
        pstm.setDouble(6, totalCost);

        return pstm.executeUpdate() > 0;
    }

    public List<SupplyRecordTM> getAllSupplies() throws SQLException {
        List<SupplyRecordTM> list = new ArrayList<>();
        String sql = "SELECT sm.date, sm.supplier_id, s.supplier_name, m.med_name, sm.qty, sm.unit_cost, sm.total_cost " +
                "FROM supplier_medicine sm " +
                "JOIN supplier s ON sm.supplier_id = s.supplier_id " +
                "JOIN medicine m ON sm.medicine_id = m.medicine_id " +
                "ORDER BY sm.date DESC, sm.supply_id DESC";

        Connection connection = DBConnection.getInstance().getConnection();
        ResultSet resultSet = connection.createStatement().executeQuery(sql);

        while (resultSet.next()) {
            list.add(new SupplyRecordTM(
                    resultSet.getString("date"),
                    resultSet.getInt("supplier_id"),
                    resultSet.getString("supplier_name"),
                    resultSet.getString("med_name"),
                    resultSet.getInt("qty"),
                    resultSet.getDouble("unit_cost"),
                    resultSet.getDouble("total_cost")
            ));
        }
        return list;
    }
}