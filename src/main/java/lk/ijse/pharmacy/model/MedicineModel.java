package lk.ijse.pharmacy.model;

import lk.ijse.pharmacy.dbconnection.DBConnection;
import lk.ijse.pharmacy.dto.MedicineDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedicineModel {

    public List<MedicineDTO> getAll() throws SQLException, ClassNotFoundException {
        List<MedicineDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM medicine";
        Connection connection = DBConnection.getInstance().getConnection();
        ResultSet resultSet = connection.createStatement().executeQuery(sql);

        while (resultSet.next()) {
            list.add(new MedicineDTO(
                    resultSet.getInt("medicine_id"), // Use getInt
                    resultSet.getString("med_name"),
                    resultSet.getString("brand"),
                    resultSet.getInt("qty_in_stock"),
                    resultSet.getDouble("unit_price"),
                    resultSet.getDate("exp_date")
            ));
        }
        return list;
    }

    public boolean save(MedicineDTO medicine) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO medicine (med_name, brand, unit_price, exp_date, qty_in_stock) VALUES (?, ?, ?, ?, ?)";
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setString(1, medicine.getMedName());
        pstm.setString(2, medicine.getBrand());
        pstm.setDouble(3, medicine.getUnitPrice());
        pstm.setDate(4, new java.sql.Date(medicine.getExpDate().getTime()));
        pstm.setInt(5, medicine.getQtyInStock());

        return pstm.executeUpdate() > 0;
    }

    public boolean update(MedicineDTO medicine) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE medicine SET med_name=?, brand=?, unit_price=?, exp_date=?, qty_in_stock=? WHERE medicine_id=?";
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setString(1, medicine.getMedName());
        pstm.setString(2, medicine.getBrand());
        pstm.setDouble(3, medicine.getUnitPrice());
        pstm.setDate(4, new java.sql.Date(medicine.getExpDate().getTime()));
        pstm.setInt(5, medicine.getQtyInStock());
        pstm.setInt(6, medicine.getMedicineId());

        return pstm.executeUpdate() > 0;
    }

    public boolean delete(int id) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM medicine WHERE medicine_id=?";
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setInt(1, id);
        return pstm.executeUpdate() > 0;
    }

    public MedicineDTO search(int id) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM medicine WHERE medicine_id=?";
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setInt(1, id);
        ResultSet resultSet = pstm.executeQuery();

        if (resultSet.next()) {
            return new MedicineDTO(
                    resultSet.getInt("medicine_id"),
                    resultSet.getString("med_name"),
                    resultSet.getString("brand"),
                    resultSet.getInt("qty_in_stock"),
                    resultSet.getDouble("unit_price"),
                    resultSet.getDate("exp_date")
            );
        }
        return null;
    }

    public MedicineDTO searchByName(String name) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM medicine WHERE med_name = ?";
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, name);
        ResultSet resultSet = pstm.executeQuery();

        if (resultSet.next()) {
            return new MedicineDTO(
                    resultSet.getInt("medicine_id"),
                    resultSet.getString("med_name"),
                    resultSet.getString("brand"),
                    resultSet.getInt("qty_in_stock"),
                    resultSet.getDouble("unit_price"),
                    resultSet.getDate("exp_date")
            );
        }
        return null;
    }
}